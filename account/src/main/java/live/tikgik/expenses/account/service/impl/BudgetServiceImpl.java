package live.tikgik.expenses.account.service.impl;

import live.tikgik.expenses.shared.model.UserContextHolder;
import live.tikgik.expenses.account.dao.AccountDAO;
import live.tikgik.expenses.account.dao.BudgetDAO;
import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.dto.request.BudgetReqDto;
import live.tikgik.expenses.account.dto.request.BudgetUpdateDto;
import live.tikgik.expenses.account.dto.response.BudgetRespDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.entity.Budget;
import live.tikgik.expenses.account.entity.BudgetType;
import live.tikgik.expenses.shared.error.exception.ResourceNotFoundException;
import live.tikgik.expenses.account.mapper.BudgetMapper;
import live.tikgik.expenses.account.service.BudgetService;
import live.tikgik.expenses.shared.enums.Models;
import live.tikgik.expenses.shared.error.exception.ErrorCode;
import live.tikgik.expenses.shared.error.exception.GeneralFailureException;
import live.tikgik.expenses.shared.utility.ValidateInputUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static live.tikgik.expenses.shared.constant.AppConstants.REF_NO;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetDAO budgetDAO;
    private final BudgetMapper budgetMapper;
    private final AccountDAO accountDAO;

    @Override
    public void deleteByAccountRefNo(String refNo) {
        budgetDAO.deleteByAccountRefNo(refNo);
    }

    @Override
    public Budget createBudget(BudgetType budgetType, Account sentAccount, String customerId, boolean defaultReceiver, boolean defaultSender) {
        return new Budget(budgetType.name() + " budget for " + sentAccount.getName(), BigDecimal.ZERO, budgetType, defaultReceiver, defaultSender, sentAccount, customerId);
    }

    @Override
    @Transactional
    public Budget create(BudgetReqDto budgetReqDto) {
        Budget sentBudget = budgetMapper.reqDtoToEntity(budgetReqDto);
        sentBudget.setCustomerId(UserContextHolder.getUser().getId());
        associateAccount(budgetReqDto.getAccountRefNo(), sentBudget);
        return budgetDAO.create(sentBudget);
    }

    @Override
    public void associateAccount(String accountRefNo, Budget sentBudget) {
        Optional<Account> accountOptional = accountDAO.get(accountRefNo, UserContextHolder.getUser().getId());
        if (accountOptional.isEmpty()) {
            throw new GeneralFailureException(ErrorCode.OBJECT_NOT_FOUND.getErrorCode(), Map.of("error", "account not found"));
        } else {
            Account account = accountOptional.get();
            if (account.getBudgets() == null) {
                account.setBudgets(new HashSet<>());
            }
            account.getBudgets().add(sentBudget);
            sentBudget.setAccount(account);
        }
    }

    public Optional<Budget> getEntity(String refNo) {
        try {
            return budgetDAO.get(refNo);

        } catch (Exception _) {
            return Optional.empty();
        }
    }

    @Override
    public Budget get(String refNo) {
        return getEntity(refNo).orElseThrow(() -> new ResourceNotFoundException(Models.BUDGET.name(), REF_NO.getKey(), refNo));
    }

    @Override
    @Transactional
    public Budget update(String refNo, BudgetUpdateDto budgetUpdateDto) {
        Budget budget = get(refNo);
        budgetMapper.update(budget, budgetUpdateDto);
        log.info("updated BUDGET {}", budget);
        budget.setUpdatedAt(LocalDateTime.now());
        return budgetDAO.update(budget);

    }

    @Override
    public Budget createDefaultBudget() {
        Budget budget = new Budget();
        budget.setName("default BUDGET");
        budget.setDetails("default customer BUDGET");
        budget.setBudgetType(BudgetType.DEFAULT);
        budget.setDefaultSender(true);
        budget.setDefaultReceiver(true);
        return budget;
    }

    @Override
    @Transactional
    public void delete(String refNo) {
        budgetDAO.delete(refNo);
    }

    @Override
    public Page<Budget> getAllEntities(Pageable pageable) {
        return budgetDAO.findAllForCustomer(pageable);
    }

    @Override
    @Transactional
    public Set<Budget> updateAll(Set<Budget> budgets) {
        return budgetDAO.updateAll(budgets);
    }

    @Override
    public Page<Budget> getAllEntitiesWithoutAccount(Pageable pageable) {
        return budgetDAO.findAllWithoutAccount(pageable);
    }

    @Override
    public Set<BudgetRespDto> entityToRespDto(Set<Budget> budgets) {
        return budgetMapper.entityToRespDto(budgets);
    }

    @Override
    public List<Budget> getBudgetByName(String name) {
        return budgetDAO.getByName(name);
    }

    @Override
    public Budget update(Budget oldBudget) {
        return budgetDAO.update(oldBudget);
    }

    public Collection<Budget> createBudgets(List<BudgetUpdateDto> addedBudgets, Account account) {
        return budgetMapper.reqEntityToEntity(addedBudgets, account);
    }

    @Override
    public void updateBudgets(Set<BudgetUpdateDto> updateDtoBudgets, Account account) {
        List<BudgetUpdateDto> addedBudgets = new ArrayList<>();
        Map<String, BudgetUpdateDto> updatedBudgets = new HashMap<>();
        updateDtoBudgets.forEach(budgetUpdateDto -> {
            if (budgetUpdateDto != null) {
                if (budgetUpdateDto.getRefNo() == null) {
                    addedBudgets.add(budgetUpdateDto);
                } else {
                    updatedBudgets.put(budgetUpdateDto.getRefNo(), budgetUpdateDto);
                }
            }
        });
        account.getBudgets().removeIf(budget -> !updatedBudgets.containsKey(budget.getRefNo()));
        account.getBudgets().forEach(budget -> budgetMapper.update(budget, updatedBudgets.get(budget.getRefNo())));
        account.getBudgets().addAll(this.createBudgets(addedBudgets, account));
    }


    @Override
    public Set<Budget> getEntities(Set<String> refNos) {
        return budgetDAO.getEntities(refNos);
    }

    @Override
    public boolean addAssociation(Account entity, Models entityModel, String refNo) {
        if (ValidateInputUtils.isValidInput(entity, entity.getBudgets())) {
            Optional<Budget> budgetOptional = getEntity(refNo);
            if (budgetOptional.isEmpty()) {
                throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND, Map.of("error", String.format("the BUDGET with given reference number : %s doesn't exist", refNo)));
            }
            Budget budget = budgetOptional.get();
            Long accountId = budgetDAO.checkAccountAssociation(budget);
            if (accountId != null) {
                if (Objects.equals(accountId, entity.getId())) {
                    throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE, Map.of("error", "this BUDGET is already present in the given account!!"));
                } else {
                    throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE, Map.of("error", "this BUDGET is already present in another account!!"));
                }
            }

            entity.getBudgets().add(budgetOptional.get());
            budget.setAccount(entity);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAssociation(Account entity, Models entityModel, String refNo) {
        if (ValidateInputUtils.isValidInput(entity, entity.getBudgets())) {
            Optional<Budget> budgetOptional = getEntity(refNo);
            if (budgetOptional.isEmpty()) {
                throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND, Map.of("error", String.format("the subBudget with given reference number : %s doesn't exist", refNo)));
            }
            Budget budget = budgetOptional.get();
            Long accountId = budgetDAO.checkAccountAssociation(budget);
            if (accountId != null) {
                if (Objects.equals(accountId, entity.getId())) {
                    entity.getBudgets().remove(budget);
                } else {
                    throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE, Map.of("error", "this BUDGET is already present in another account!!"));
                }
            } else {
                throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE, Map.of("error", "this BUDGET is bot present in any account!!"));
            }
            return true;
        }
        return false;
    }


    @Override
    public void updateAssociation(Account entity, AccountUpdateDto entityUpdateDto) {
        if (ValidateInputUtils.isValidInput(entity.getBudgets(), entityUpdateDto.getBudgets())) {
            Set<Budget> existingBudgets = entity.getBudgets();
            Set<BudgetUpdateDto> budgetUpdateDtos = entityUpdateDto.getBudgets();
            //new BUDGET will contain only the new BUDGET ie the one not included with refs
            Set<Budget> newBudgets = getBudgets(budgetUpdateDtos, existingBudgets, entity);
            //remove non existent in the new collection
            Set<Budget> removedBudgets = new HashSet<>(existingBudgets);
            List<String> newListRefs = budgetUpdateDtos.stream().map(BudgetUpdateDto::getRefNo).toList();
            removedBudgets.removeIf(Budget -> newListRefs.contains(Budget.getRefNo()));
            entity.getBudgets().removeAll(removedBudgets);
            entity.getBudgets().addAll(newBudgets);
        }
    }

    private Set<Budget> getBudgets(Set<BudgetUpdateDto> BudgetUpdateDtos, Set<Budget> existingBudgets, Account entity) {
        Set<Budget> addedSubCategories = new HashSet<>();
        BudgetUpdateDtos.forEach(newBudget -> {
            if (newBudget.getRefNo() != null) {
                Budget existingBudget = findExistingEntity(existingBudgets, newBudget.getRefNo());
                if (existingBudget != null) {
                    budgetMapper.update(existingBudget, newBudget);
                }
            } else {
                //if id doesn't have id add it
                addedSubCategories.add(budgetMapper.reqEntityToEntity(newBudget, entity));
            }
        });
        return addedSubCategories;
    }

    private Budget findExistingEntity(Set<Budget> existingBudgets, String refNo) {
        return existingBudgets.stream().filter(budget -> budget.getRefNo().equals(refNo)).findFirst().orElse(null);
    }
}
