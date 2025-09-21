package live.tikgik.expenses.account.dao;



import live.tikgik.expenses.account.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BudgetDAO {
    Budget create(Budget expense);
    Optional<Budget> get(String refNo);
    Page<Budget> findAllForCustomer(Pageable pageable);

    Page<Budget> findAllWithoutAccount(Pageable pageable);

    List<Budget> get();
    String delete(String refNo);

    Long checkAccountAssociation(Budget Budget);
    Set<Budget> getEntities(Set<String> refNos);

    Set<Budget> saveAll(Set<Budget> Budgets);
    Set<Budget> updateAll(Set<Budget> Budgets);
    Budget update(Budget expense);

    List<Budget> getByName(String name);

    void deleteByAccountRefNo(String refNo);
}