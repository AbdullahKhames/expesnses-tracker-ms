package live.tikgik.expenses.account.repository;

import live.tikgik.expenses.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Account, Long> {


    Optional<Account> findByRefNo(String refNo);

    @Modifying
//    @Query("DELETE FROM Account a WHERE a.refNo = :refNo")
    void deleteByRefNo(String refNo);

    @Query("SELECT a FROM Account a WHERE a.refNo IN :refNos")
    Set<Account> findAllByRefNoIn(Set<String> refNos);

    List<Account> findAllByNameLike(String name);
    @Query("SELECT a FROM Account a WHERE a.name = :name AND :customerId MEMBER OF a.customerIds")
    Optional<Account> findByNameAndCustomerIdsContaining(String name, String customerId);
    boolean existsByNameAndCustomerIdsContaining(String name, String customerId);
}