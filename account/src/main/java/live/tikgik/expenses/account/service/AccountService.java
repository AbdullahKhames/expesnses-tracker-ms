package live.tikgik.expenses.account.service;


import live.tikgik.expenses.account.dto.request.AccountReqDto;
import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.entity.Budget;
import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.service.CrudService;

import java.util.List;
import java.util.Set;

public interface AccountService extends CrudService<AccountReqDto, AccountUpdateDto, String, Account> {
    ApiResponse addAssociation(String accountRefNo, String budgetRefNo);

    ApiResponse removeAssociation(String accountRefNo, String budgetRefNo);

    Set<Budget> getAccountBudgets(String refNo);

    List<Account> getAccountByName(String name);

    Account getDefaultAccount();
}
