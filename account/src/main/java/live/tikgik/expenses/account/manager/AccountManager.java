package live.tikgik.expenses.account.manager;

import live.tikgik.expenses.account.dto.request.AccountReqDto;
import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.dto.response.AccountRespDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.mapper.AccountMapper;
import live.tikgik.expenses.account.service.AccountService;
import live.tikgik.expenses.account.service.BudgetService;
import live.tikgik.expenses.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static live.tikgik.expenses.shared.enums.Models.ACCOUNT;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountManager {
    private final AccountService service;
    private final AccountMapper mapper;
    private final BudgetService budgetService;

    public ApiResponse create(AccountReqDto entityReqDto) {
        Account entity = service.create(entityReqDto);
        return ApiResponse.getCreateResponse(ACCOUNT.name(), entity.getRefNo(), mapper.entityToRespDto(entity));
    }

    public ApiResponse addAssociation(String accountRefNo, String budgetRefNo) {
        return service.addAssociation(accountRefNo, budgetRefNo);
    }

    public ApiResponse removeAssociation(String accountRefNo, String budgetRefNo) {
        return service.removeAssociation(accountRefNo, budgetRefNo);
    }

    public ApiResponse getAccountBudgets(String refNo) {
        return ApiResponse.getFetchResponse(ACCOUNT, refNo, budgetService.entityToRespDto(service.getAccountBudgets(refNo)));
    }

    public ApiResponse getAccountByName(String name) {
        if (name == null || name.isBlank()) {
            return ApiResponse.getErrorResponse(804, "name cannot be null");
        }
        List<Account> accounts = service.getAccountByName(name);
        if (!accounts.isEmpty()){
            return ApiResponse.getFetchAllResponse(ACCOUNT, mapper.entityToRespDto(accounts));
        }else {
            return ApiResponse.getErrorResponse(804, "not found");
        }
    }

    public Account getDefaultAccount() {
        return service.getDefaultAccount();
    }

    public ApiResponse get(String refNo) {
        return ApiResponse.getFetchResponse(ACCOUNT, refNo, mapper.entityToRespDto(service.get(refNo)));
    }

    public ApiResponse update(String refNo, AccountUpdateDto accountUpdateDto) {
        return ApiResponse.getUpdateResponse(ACCOUNT, refNo, mapper.entityToRespDto(service.update(refNo, accountUpdateDto)));

    }

    public ApiResponse delete(String refNo) {
        service.delete(refNo);
        return ApiResponse.getDeleteResponse(ACCOUNT, refNo);
    }

    public ApiResponse getAllEntities(Pageable pageable) {
        Page<Account> accountPage = service.getAllEntities(pageable);
        Page<AccountRespDto> accountDtos = accountPage.map(mapper::entityToRespDto);
        return ApiResponse.getFetchAllResponse(ACCOUNT, accountDtos);
    }

    public Set<Account> getEntities(Set<String> refNos) {
        if (refNos == null || refNos.isEmpty()) {
            return new HashSet<>();
        }
        return service.getEntities(refNos);
    }
}
