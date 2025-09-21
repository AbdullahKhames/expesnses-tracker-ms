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
    @Query("SELECT p FROM Budget p WHERE p.account is null")
    Page<Budget> findAllWithoutAccount(Pageable pageable);

    @Modifying
    void deleteByRefNo(String refNo);

    List<Budget> findByNameAndCustomerId(String name, String customerId);

    Set<Budget> findByRefNoInAndCustomerId(Set<String> refNos, String customerId);

    @Query("SELECT c.id FROM Account c JOIN c.budgets s WHERE s.id = :id")
    Long findAccountById(Long id);

    Optional<Budget> findByRefNoAndCustomerId(String refNo, String customerId);

    Page<Budget> findByCustomerId(String customerId, Pageable pageable);

    @Modifying
    @Query("delete from Budget b where b.account.refNo = :refNo")
    void deleteByAccount_RefNo(String refNo);
}