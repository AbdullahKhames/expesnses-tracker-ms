package live.tikgik.expenses.account.service;


import live.tikgik.expenses.account.dto.request.AccountReqDto;
import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.service.CrudService;

public interface AccountService extends CrudService<AccountReqDto, AccountUpdateDto, String, Account> {
    ApiResponse addAssociation(String accountRefNo, String budgetRefNo);

    ApiResponse removeAssociation(String accountRefNo, String budgetRefNo);

    ApiResponse getAccountBudgets(String refNo);

    ApiResponse getAccountByName(String name);

    Account getDefaultAccount();
}
