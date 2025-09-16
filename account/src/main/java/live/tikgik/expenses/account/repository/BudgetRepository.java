package live.tikgik.expenses.account.repository;

import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByRefNo(String refNo);

    @Query("SELECT p FROM Budget p WHERE p.account is null")
    Page<Budget> findAllWithoutAccount(Pageable pageable);

    @Modifying
    @Transactional
    void deleteByRefNo(String refNo);

    List<Budget> findByName(String name);

    Set<Budget> findByRefNoIn(Set<String> refNos);

    @Query("SELECT c.id FROM Account c JOIN c.budgets s WHERE s.id = :id")
    Long findAccountById(Long id);
}