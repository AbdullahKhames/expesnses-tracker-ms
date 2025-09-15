package live.tikgik.expenses.account.service.impl;

import jakarta.persistence.EntityManager;
import live.tikgik.expenses.account.config.UserContextHolder;
import live.tikgik.expenses.account.dao.AccountDAO;
import live.tikgik.expenses.account.dao.BudgetDAO;
import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.dto.request.BudgetReqDto;
import live.tikgik.expenses.account.dto.request.BudgetUpdateDto;
import live.tikgik.expenses.account.dto.response.BudgetRespDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.entity.Budget;
import live.tikgik.expenses.account.entity.BudgetType;
import live.tikgik.expenses.account.mapper.BudgetMapper;
import live.tikgik.expenses.account.service.BudgetService;
import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.enums.Models;
import live.tikgik.expenses.shared.error.exception.ErrorCode;
import live.tikgik.expenses.shared.error.exception.GeneralFailureException;
import live.tikgik.expenses.shared.error.exception_handler.models.ErrorCategory;
import live.tikgik.expenses.shared.error.exception_handler.models.ResponseError;
import live.tikgik.expenses.shared.utility.ValidateInputUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BudgetServiceImpl implements BudgetService {
    public static final String BUDGET = "Budget";
    private final BudgetDAO budgetDAO;
    private final BudgetMapper budgetMapper;
    private final AccountDAO accountDAO;


    @Override
    public Budget update(Budget entity, EntityManager entityManager) {
        return BudgetService.super.update(entity, entityManager);
    }

    @Override
    public ApiResponse create(BudgetReqDto BudgetReqDto) {
        try {
            Budget sentBudget = budgetMapper.reqDtoToEntity(BudgetReqDto);
            sentBudget.setCustomerId(UserContextHolder.getUsername());
            associateAccount(BudgetReqDto.getAccountRefNo(), sentBudget);
            Budget savedBudget = budgetDAO.create(sentBudget);
//            if (savedBudget == null || savedBudget.getId() == null){
//                throw new GeneralFailureException(ErrorCode.OBJECT_NOT_FOUND.getErrorCode(),
//                        Map.of("error", "savedBudget not found"));
//            }
            log.info("created Budget {}", savedBudget);
            return ApiResponse.getCreateResponse(BUDGET, savedBudget.getRefNo(), budgetMapper.entityToRespDto(savedBudget));
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void associateAccount(String accountRefNo, Budget sentBudget) {
        Optional<Account> accountOptional = accountDAO.get(accountRefNo);
        if (accountOptional.isEmpty()){
            throw new GeneralFailureException(ErrorCode.OBJECT_NOT_FOUND.getErrorCode(),
                    Map.of("error", "account not found"));
        }else {
            Account account = accountOptional.get();
            if (account.getBudgets() == null) {
                account.setBudgets(new HashSet<>());
            }
            account.getBudgets().add(sentBudget);
            sentBudget.setAccount(account);
            accountDAO.update(account);
        }
    }

    public Optional<Budget> getEntity(String refNo){
        try {
            Optional<Budget> BudgetOptional = budgetDAO.get(refNo);
            log.info("fetched Budget {}", BudgetOptional);
            return BudgetOptional;

        }catch (Exception ex){
            return Optional.empty();
        }
    }
    @Override
    public ApiResponse get(String refNo) {
        try {
            Optional<Budget> BudgetOptional = getEntity(refNo);
            log.info("fetched Budget {}", BudgetOptional);
            if (BudgetOptional.isPresent()){
                Budget Budget = BudgetOptional.get();
                return ApiResponse.getFetchResponse(BUDGET, Budget.getRefNo(), budgetMapper.entityToRespDto(Budget));
            }else {
                throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND,
                        Map.of("error", String.format("Budget with the ref number %s was not found", refNo)));
            }

        }catch (Exception ex){
            ResponseError responseError = new ResponseError();
            responseError.setErrorCategory(ErrorCategory.DATABASE_Error);
            responseError.setErrorCode(ErrorCode.OBJECT_NOT_FOUND.getErrorCode());
            responseError.setErrorMessage(String.format("Budget with the ref number %s was not found", refNo));
            return ApiResponse.getErrorResponse(804, responseError);
        }

    }

    @Override
    public ApiResponse update(String refNo, BudgetUpdateDto budgetUpdateDto) {
        Optional<Budget> BudgetOptional = getEntity(refNo);
        if (BudgetOptional.isPresent()){
            Budget budget = BudgetOptional.get();
            log.info("fetched Budget {}", budget);
            budgetMapper.update(budget, budgetUpdateDto);
            log.info("updated Budget {}", budget);
            budget.setUpdatedAt(LocalDateTime.now());
            return ApiResponse.getUpdateResponse(BUDGET, budget.getRefNo(), budgetMapper.entityToRespDto(budgetDAO.update(budget)));
        }
        ResponseError responseError = new ResponseError();
        responseError.setErrorCategory(ErrorCategory.DATABASE_Error);
        responseError.setErrorCode(ErrorCode.OBJECT_NOT_FOUND.getErrorCode());
        responseError.setErrorMessage(String.format("Budget with the ref number %s was not found", refNo));
        return ApiResponse.getErrorResponse(804, responseError);

    }
    @Override
    public Budget createDefaultBudget(){
        Budget Budget = new Budget();
        Budget.setName("default Budget");
        Budget.setDetails("default customer Budget");
        Budget.setBudgetType(BudgetType.DEFAULT);
        Budget.setDefaultSender(true);
        Budget.setDefaultReceiver(true);
        return Budget;
    }
    @Override
    public ApiResponse delete(String refNo) {
        return ApiResponse.getDeleteResponse(BUDGET,budgetDAO.delete(refNo));
    }

    @Override
    public ApiResponse getAllEntities(Pageable pageable) {
        Page<Budget> BudgetPage = budgetDAO.findAll(pageable);
        Page<BudgetRespDto> BudgetDtos = BudgetPage.map(budgetMapper::entityToRespDto);
        return ApiResponse.getFetchAllResponse(BUDGET, BudgetDtos);
    }

    @Override
    public Set<Budget> updateAll(Set<Budget> Budgets) {
        return budgetDAO.updateAll(Budgets);
    }

    @Override
    public ApiResponse getAllEntitiesWithoutAccount(Pageable pageable) {
        Page<Budget> BudgetPage = budgetDAO.findAllWithoutAccount(pageable);
        Page<BudgetRespDto> BudgetDtos = BudgetPage.map(budgetMapper::entityToRespDto);
        return ApiResponse.getFetchAllResponse(BUDGET, BudgetDtos);
    }

    @Override
    public Set<BudgetRespDto> entityToRespDto(Set<Budget> Budgets) {
        return budgetMapper.entityToRespDto(Budgets);
    }

    @Override
    public ApiResponse getBudgetByName(String name) {
        if (name == null || name.isBlank()) {
            return ApiResponse.getErrorResponse(804, "name cannot be null");
        }
        List<Budget> Budgets = budgetDAO.getByName(name);
        if (!Budgets.isEmpty()){
            return ApiResponse.getFetchAllResponse(BUDGET, budgetMapper.entityToRespDto(Budgets));
        }else {
            return ApiResponse.getErrorResponse(804, "not found");
        }
    }

    @Override
    public Budget update(Budget oldBudget) {
        return budgetDAO.update(oldBudget);
    }

    @Override
    public Set<Budget> getEntities(Set<String> refNos) {
        return budgetDAO.getEntities(refNos);
    }

    @Override
    public boolean addAssociation(Account entity, Models entityModel, String refNo) {
        if (ValidateInputUtils.isValidInput(entity, entity.getBudgets())) {
            Optional<Budget> BudgetOptional = getEntity(refNo);
            if (BudgetOptional.isEmpty()) {
                throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND,
                        Map.of("error", String.format("the Budget with given reference number : %s doesn't exist", refNo)));
            }
            Budget Budget = BudgetOptional.get();
            Long accountId = budgetDAO.checkAccountAssociation(Budget);
            if (accountId != null) {
                if (Objects.equals(accountId, entity.getId())) {
                    throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                            Map.of("error", "this Budget is already present in the given account!!"));
                }else {
                    throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                            Map.of("error", "this Budget is already present in another account!!"));
                }
            }

            entity.getBudgets().add(BudgetOptional.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAssociation(Account entity, Models entityModel, String refNo) {
        if (ValidateInputUtils.isValidInput(entity, entity.getBudgets())) {
            Optional<Budget> BudgetOptional = getEntity(refNo);
            if (BudgetOptional.isEmpty()) {
                throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND,
                        Map.of("error", String.format("the subBudget with given reference number : %s doesn't exist", refNo)));
            }
            Budget Budget = BudgetOptional.get();
            Long accountId = budgetDAO.checkAccountAssociation(Budget);
            if (accountId != null) {
                if (Objects.equals(accountId, entity.getId())) {
                    entity.getBudgets().remove(Budget);
                }else {
                    throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                            Map.of("error", "this Budget is already present in another account!!"));
                }
            }else {
                throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                        Map.of("error", "this Budget is bot present in any account!!"));
            }
            return true;
        }
        return false;
    }

    @Override
    public ApiResponse removeDtoAssociation(Object entity, Models entityModel, Set<?> associationsUpdateDto) {
        ApiResponse errorResponse = ValidateInputUtils.validateWildCardSet(associationsUpdateDto, BudgetUpdateDto.class);
        if (errorResponse != null) {
            return errorResponse;
        }
        try {
            Set<Budget> Budgets = new HashSet<>();
            for (Object obj : associationsUpdateDto) {
                BudgetUpdateDto BudgetUpdateDto = (BudgetUpdateDto) obj;
                Optional<Budget> BudgetOptional = getEntity(BudgetUpdateDto.getRefNo());
                if (BudgetOptional.isPresent())
                    Budgets.add(BudgetOptional.get());
                else {
                    Budgets.add(budgetMapper.reqEntityToEntity(BudgetUpdateDto));
                }
            }
        }catch (Exception ex){
            return ApiResponse.getErrorResponse(810, "error processing your request");
        }
        return null;
    }



    @Override
    public void updateAssociation(Account entity, AccountUpdateDto entityUpdateDto) {
        if (ValidateInputUtils.isValidInput(entity.getBudgets(), entityUpdateDto.getBudgets())) {
            Set<Budget> existingBudgets = entity.getBudgets();
            Set<BudgetUpdateDto> budgetUpdateDtos = entityUpdateDto.getBudgets();
            //new Budget will contain only the new Budget ie the one not included with refs
            Set<Budget> newBudgets = getBudgets(budgetUpdateDtos, existingBudgets);
            //remove non existent in the new collection
            Set<Budget> removedBudgets = new HashSet<>(existingBudgets);
            List<String> newListRefs = budgetUpdateDtos.stream().map(BudgetUpdateDto::getRefNo).toList();
            removedBudgets.removeIf(Budget -> newListRefs.contains(Budget.getRefNo()));
            entity.getBudgets().removeAll(removedBudgets);
            entity.getBudgets().addAll(newBudgets);
        }
    }
    private Set<Budget> getBudgets (Set<BudgetUpdateDto> BudgetUpdateDtos, Set < Budget > existingBudgets){
        Set<Budget> addedSubCategories = new HashSet<>();
        BudgetUpdateDtos.forEach(newBudget -> {
            if (newBudget.getRefNo() != null) {
                Budget existingBudget = findExistingEntity(existingBudgets, newBudget.getRefNo());
                if (existingBudget != null) {
                    budgetMapper.update(existingBudget, newBudget);
                    existingBudget.setUpdatedAt(LocalDateTime.now());
                }
            } else {
                //if id doesn't have id add it
                addedSubCategories.add(budgetMapper.reqEntityToEntity(newBudget));
            }
        });
        return addedSubCategories;
    }
    @Override
    public ApiResponse addAssociation(Object entity, Models entityModel, Set<String> refNos) {
        return null;
    }

    @Override
    public ApiResponse addDtoAssociation(Object entity, Models entityModel, Set<?> associationUpdateDto) {
        return null;
    }

    @Override
    public ApiResponse removeAssociation(Object entity, Models entityModel, Set<String> refNo) {
        return null;
    }

    private Budget findExistingEntity (Set < Budget > existingBudgets, String refNo){
        return existingBudgets.stream().filter(Budget -> Budget.getRefNo().equals(refNo)).findFirst().orElse(null);
    }
}
