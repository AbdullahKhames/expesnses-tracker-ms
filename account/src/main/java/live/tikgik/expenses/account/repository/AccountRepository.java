package live.tikgik.expenses.account.repository;

import live.tikgik.expenses.account.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByRefNo(String refNo);

    @Modifying
    @Query("delete from Account a where a.refNo = :refNo and :customerId member of a.customerIds")
    void deleteByRefNoAndCustomerId(@Param("refNo") String refNo, @Param("customerId") String customerId);
    @Modifying
    @Query("delete from Account a where a.refNo = :refNo")
    void deleteByRefNo(@Param("refNo") String refNo);
    @Query("select a from Account a where a.refNo in :refNos")
    Set<Account> findAllByRefNoIn(@Param("refNos") Set<String> refNos);

    @EntityGraph(attributePaths = {"budgets"})
    @Query("select distinct a from Account a join a.customerIds cid where a.name like :name and cid = :customerId")
    List<Account> findAllByNameLikeAndCustomerId(@Param("name") String name, @Param("customerId") String customerId);

    @EntityGraph(attributePaths = {"budgets"})
    @Query("select distinct a from Account a join a.customerIds cid where a.name = :name and cid = :customerId")
    Optional<Account> findByNameAndCustomerId(@Param("name") String name, @Param("customerId") String customerId);

    @Query("select (count(a) > 0) from Account a join a.customerIds cid where a.name = :name and cid = :customerId")
    boolean existsByNameAndCustomerId(@Param("name") String name, @Param("customerId") String customerId);

    @EntityGraph(attributePaths = {"budgets"})
    @Query("select distinct a from Account a join a.customerIds cid where a.refNo = :refNo and cid = :customerId")
    Optional<Account> findByRefNoAndCustomerId(@Param("refNo") String refNo, @Param("customerId") String customerId);

    @EntityGraph(attributePaths = {"budgets"})
    @Query(
            value = "select distinct a from Account a join a.customerIds cid where cid = :customerId",
            countQuery = "select count(distinct a) from Account a join a.customerIds cid where cid = :customerId"
    )
    Page<Account> findByCustomerId(@Param("customerId") String customerId, Pageable pageable);

    @EntityGraph(attributePaths = {"budgets"})
    @Query("select distinct a from Account a join a.customerIds cid where a.refNo in :refNos and cid = :customerId")
    Set<Account> findByRefNosAndCustomerId(@Param("refNos") Collection<String> refNos,
                                           @Param("customerId") String customerId);
}
