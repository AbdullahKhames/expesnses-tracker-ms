package live.tikgik.expenses.account.dao;



import live.tikgik.expenses.account.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountDAO {
    Account create(Account expense);
    Optional<Account> get(String refNo, String customerId);
    Page<Account> findAll(Pageable pageable);
    Page<Account> findAll(Pageable pageable, String customerId);
    List<Account> get(String customerId);
    Account update(Account expense);
    String delete(String refNo, String customerId);
    Set<Account> getEntities(Set<String> refNos, String customerId);

    List<Account> getByName(String name, String customerId);

    Account getDefaultAccount();

    boolean existByNameAndCustomerIdsContaining(String name, String customerId);
}