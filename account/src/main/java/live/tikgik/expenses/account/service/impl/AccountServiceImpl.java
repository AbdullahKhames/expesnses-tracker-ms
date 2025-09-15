package live.tikgik.expenses.account.service.impl;


import jakarta.persistence.EntityManager;
import live.tikgik.expenses.account.dao.AccountDAO;
import live.tikgik.expenses.account.dto.request.AccountReqDto;
import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.dto.response.AccountRespDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.mapper.AccountMapper;
import live.tikgik.expenses.account.service.AccountService;
import live.tikgik.expenses.account.service.BudgetService;
import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.enums.Models;
import live.tikgik.expenses.shared.error.exception.ErrorCode;
import live.tikgik.expenses.shared.error.exception.GeneralFailureException;
import live.tikgik.expenses.shared.error.exception_handler.models.ErrorCategory;
import live.tikgik.expenses.shared.error.exception_handler.models.ResponseError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
    public Account update(Account entity, EntityManager entityManager) {
        return AccountService.super.update(entity, entityManager);
    }

    @Override
    @Transactional(readOnly = false)
    public ApiResponse create(AccountReqDto entityReqDto) {
        Account sentAccount = accountMapper.reqDtoToEntity(entityReqDto);
        Account savedAccount = accountDAO.create(sentAccount);
        log.info("created account {}", savedAccount);
        return ApiResponse.getCreateResponse(ACCOUNT.name(), savedAccount.getRefNo(), accountMapper.entityToRespDto(savedAccount));
    }
    @Override
    public ApiResponse addAssociation(String accountRefNo, String budgetRefNo) {
        Optional<Account> accountOptional = getEntity(accountRefNo);
        if (accountOptional.isPresent()){
            Account account = accountOptional.get();
            if (accountAssociationManager.addAssociation(account, Models.Budget, budgetRefNo)){
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
    public ApiResponse removeAssociation(String accountRefNo, String budgetRefNo) {
        Optional<Account> accountOptional = getEntity(accountRefNo);
        if (accountOptional.isPresent()){
            Account account = accountOptional.get();
            if (accountAssociationManager.removeAssociation(account, Models.Budget, budgetRefNo)){
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
    public ApiResponse getAccountBudgets(String refNo) {
        Optional<Account> accountOptional = getEntity(refNo);
        if (accountOptional.isPresent()){
            Account account = accountOptional.get();
            return ApiResponse.getFetchResponse(ACCOUNT, account.getRefNo(), budgetService.entityToRespDto(account.getBudgets()));

        }else {
            return ApiResponse.getErrorResponse(804, "no account found with given ref");
        }
    }

    @Override
    public ApiResponse getAccountByName(String name) {
        if (name == null || name.isBlank()) {
            return ApiResponse.getErrorResponse(804, "name cannot be null");
        }
        List<Account> accounts = accountDAO.getByName(name);
        if (!accounts.isEmpty()){
            return ApiResponse.getFetchAllResponse(ACCOUNT, accountMapper.entityToRespDto(accounts));
        }else {
            return ApiResponse.getErrorResponse(804, "not found");
        }
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
    public ApiResponse get(String refNo) {
        try {
            Optional<Account> accountOptional = getEntity(refNo);
            log.info("fetched account {}", accountOptional);
            if (accountOptional.isPresent()){
                Account account = accountOptional.get();
                return ApiResponse.getFetchResponse(ACCOUNT, account.getRefNo(), accountMapper.entityToRespDto(account));
            }else {
                throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND,
                        Map.of("error", String.format("account with the ref number %s was not found", refNo)));
            }

        }catch (Exception ex){
            ResponseError responseError = new ResponseError();
            responseError.setErrorCategory(ErrorCategory.DATABASE_Error);
            responseError.setErrorCode(ErrorCode.OBJECT_NOT_FOUND.getErrorCode());
            responseError.setErrorMessage(String.format("account with the ref number %s was not found", refNo));
            return ApiResponse.getErrorResponse(804, responseError);
        }
    }

    @Override
    public Optional<Account> getEntity(String refNo) {
        try {
            Optional<Account> accountOptional = accountDAO.get(refNo);
            log.info("fetched account {}", accountOptional);
            return accountOptional;

        }catch (Exception ex){
            return Optional.empty();
        }
    }

    @Override
    public ApiResponse update(String refNo, AccountUpdateDto accountUpdateDto) {
        Optional<Account> accountOptional = getEntity(refNo);
        if (accountOptional.isPresent()){
            Account account = accountOptional.get();
            log.info("fetched account {}", account);
            accountMapper.update(account, accountUpdateDto);
//            updateAccountService.updateCategoryAssociations(account, accountUpdateDto);
            log.info("updated account {}", account);
            account.setUpdatedAt(LocalDateTime.now());
            return ApiResponse.getUpdateResponse(ACCOUNT, account.getRefNo(), accountMapper.entityToRespDto(accountDAO.update(account)));
        }
        ResponseError responseError = new ResponseError();
        responseError.setErrorCategory(ErrorCategory.DATABASE_Error);
        responseError.setErrorCode(ErrorCode.OBJECT_NOT_FOUND.getErrorCode());
        responseError.setErrorMessage(String.format("account with the ref number %s was not found", refNo));
        return ApiResponse.getErrorResponse(804, responseError);

    }

    @Override
    public ApiResponse delete(String refNo) {
        return ApiResponse.getDeleteResponse(ACCOUNT,accountDAO.delete(refNo));
    }

    @Override
    public ApiResponse getAllEntities(Pageable pageable) {
        Page<Account> accountPage = accountDAO.findAll(pageable);
        Page<AccountRespDto> accountDtos = accountPage.map(accountMapper::entityToRespDto);
        return ApiResponse.getFetchAllResponse(ACCOUNT, accountDtos);
    }

    @Override
    public Set<Account> getEntities(Set<String> refNos) {
        if (refNos == null || refNos.isEmpty()) {
            return new HashSet<>();
        }
        return accountDAO.getEntities(refNos);
    }
}
