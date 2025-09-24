package live.tikgik.expenses.account.service.impl;


import live.tikgik.expenses.shared.model.UserContextHolder;
import live.tikgik.expenses.account.dao.AccountDAO;
import live.tikgik.expenses.account.dto.request.AccountReqDto;
import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.entity.Budget;
import live.tikgik.expenses.account.entity.BudgetType;
import live.tikgik.expenses.shared.error.exception.ResourceNotFoundException;
import live.tikgik.expenses.account.mapper.AccountMapper;
import live.tikgik.expenses.account.service.AccountService;
import live.tikgik.expenses.account.service.BudgetService;
import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.enums.Models;
import live.tikgik.expenses.shared.error.exception.ErrorCode;
import live.tikgik.expenses.shared.error.exception_handler.models.ErrorCategory;
import live.tikgik.expenses.shared.error.exception_handler.models.ResponseError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static live.tikgik.expenses.shared.constant.AppConstants.REF_NO;
import static live.tikgik.expenses.shared.enums.Models.ACCOUNT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountDAO accountDAO;
    private final AccountMapper accountMapper;
    private final AccountAssociationManager accountAssociationManager;
    private final BudgetService budgetService;

    @Override
    @Transactional
    public Account create(AccountReqDto entityReqDto) {
        Account sentAccount = accountMapper.reqDtoToEntity(entityReqDto);
        String customerId = UserContextHolder.getUser().getId();
        sentAccount.getCustomerIds().add(customerId);
        Budget main = budgetService.createBudget(BudgetType.MAIN, sentAccount, customerId, true, true);
        Budget service = budgetService.createBudget(BudgetType.SERVICE, sentAccount, customerId, false, false);
        Budget donation = budgetService.createBudget(BudgetType.DONATION, sentAccount, customerId, false, false);
        Budget external = budgetService.createBudget(BudgetType.EXTERNAL, sentAccount, customerId, false, false);
        sentAccount.getBudgets().add(service);
        sentAccount.getBudgets().add(main);
        sentAccount.getBudgets().add(donation);
        sentAccount.getBudgets().add(external);
        Account savedAccount = accountDAO.create(sentAccount);
        log.info("created account {}", savedAccount);
        return savedAccount;
    }



    @Override
    @Transactional
    public ApiResponse addAssociation(String accountRefNo, String budgetRefNo) {
        Optional<Account> accountOptional = getEntity(accountRefNo);
        if (accountOptional.isPresent()){
            Account account = accountOptional.get();
            if (accountAssociationManager.addAssociation(account, Models.BUDGET, budgetRefNo)){
                return ApiResponse.getUpdateResponse(ACCOUNT, accountRefNo, accountMapper.entityToRespDto(account));
            }
            return ApiResponse.getErrorResponse(804, "something went wrong couldn't add");

        }
        ResponseError responseError = new ResponseError();
        responseError.setErrorCategory(ErrorCategory.DATABASE_Error);
        responseError.setErrorCode(ErrorCode.OBJECT_NOT_FOUND.getErrorCode());
        responseError.setErrorMessage(String.format("account with the ref number %s was not found", accountRefNo));
        return ApiResponse.getErrorResponse(804, responseError);
    }

    @Override
    @Transactional
    public ApiResponse removeAssociation(String accountRefNo, String budgetRefNo) {
        Optional<Account> accountOptional = getEntity(accountRefNo);
        if (accountOptional.isPresent()){
            Account account = accountOptional.get();
            if (accountAssociationManager.removeAssociation(account, Models.BUDGET, budgetRefNo)){
                return ApiResponse.getUpdateResponse(ACCOUNT, accountRefNo, accountMapper.entityToRespDto(account));
            }
            return ApiResponse.getErrorResponse(804, "something went wrong couldn't remove");
        }
        ResponseError responseError = new ResponseError();
        responseError.setErrorCategory(ErrorCategory.DATABASE_Error);
        responseError.setErrorCode(ErrorCode.OBJECT_NOT_FOUND.getErrorCode());
        responseError.setErrorMessage(String.format("account with the ref number %s was not found", accountRefNo));
        return ApiResponse.getErrorResponse(804, responseError);
    }

    @Override
    public Set<Budget> getAccountBudgets(String refNo) {
        return getEntity(refNo).map(Account::getBudgets).orElseThrow(() -> new ResourceNotFoundException(ACCOUNT.name(), REF_NO.getKey(), refNo));

    }

    @Override
    public List<Account> getAccountByName(String name) {
        return accountDAO.getByName(name, UserContextHolder.getUser().getId());
    }

    @Override
    public Account getDefaultAccount() {
        Account defaultAccount = accountDAO.getDefaultAccount();

        if (defaultAccount == null) {
            defaultAccount = new Account();
            defaultAccount.setName("default account");
            defaultAccount.setDetails("default account for all customers");
            accountDAO.create(defaultAccount);
        }
        return defaultAccount;
    }
    
    @Override
    public Account get(String refNo) {
        return getEntity(refNo).orElseThrow(() -> new ResourceNotFoundException(ACCOUNT.name(), REF_NO.getKey(), refNo));
    }

    @Override
    public Optional<Account> getEntity(String refNo) {
        try {
            Optional<Account> accountOptional = accountDAO.get(refNo, UserContextHolder.getUser().getId());
            log.info("fetched account {}", accountOptional);
            return accountOptional;

        }catch (Exception _){
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Account update(String refNo, AccountUpdateDto accountUpdateDto) {
        Account account = getEntity(refNo).orElseThrow(() -> new ResourceNotFoundException(ACCOUNT.name(), REF_NO.getKey(), refNo));
        accountMapper.update(account, accountUpdateDto);
        budgetService.updateBudgets(accountUpdateDto.getBudgets(), account);
        return accountDAO.update(account);
    }

    @Override
    @Transactional
    public void delete(String refNo) {
        budgetService.deleteByAccountRefNo(refNo);
    }

    @Override
    public Page<Account> getAllEntities(Pageable pageable) {
        return accountDAO.findAll(pageable, UserContextHolder.getUser().getId());
    }

    @Override
    public Set<Account> getEntities(Set<String> refNos) {
        return accountDAO.getEntities(refNos, UserContextHolder.getUser().getId());
    }
}
