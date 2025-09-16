package live.tikgik.expenses.account.dao;



import live.tikgik.expenses.account.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountDAO {
    Account create(Account expense);
    Optional<Account> get(String refNo);
    Page<Account> findAll(Pageable pageable);
    List<Account> get();
    Account update(Account expense);
    String delete(String refNo);
    Set<Account> getEntities(Set<String> refNos);

    List<Account> getByName(String name);

    Account getDefaultAccount();

    boolean existByNameAndCustomerIdsContaining(String name, String customerId);
}