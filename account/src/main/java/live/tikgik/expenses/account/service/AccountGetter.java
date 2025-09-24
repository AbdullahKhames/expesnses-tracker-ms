package live.tikgik.expenses.account.service;

import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.shared.service.GetRefNo;

import java.util.Set;

public interface AccountGetter extends GetRefNo {
    Set<Account> getAccounts();
    void setAccounts(Set<Account> accounts);
}