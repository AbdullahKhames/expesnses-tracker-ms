package live.tikgik.expenses.category.repository;

import live.tikgik.expenses.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Single query with join fetch to avoid N+1 problem
    @Query("SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN FETCH c.subCategories sc " +
            "WHERE c.refNo IN :refNos " +
            "AND (:customerId MEMBER OF c.customerIds OR :customerId IS NULL)")
    Set<Category> findByRefNoInAndCustomerId(@Param("refNos") Collection<String> refNos,
                                             @Param("customerId") String customerId);

    // Single query with join fetch for name search
    @Query("SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN FETCH c.subCategories sc " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND (:customerId MEMBER OF c.customerIds OR :customerId IS NULL)")
    List<Category> findByNameLikeIgnoreCaseAndCustomerId(@Param("name") String name,
                                                         @Param("customerId") String customerId);

    // Single query with join fetch for single category
    @Query("SELECT c FROM Category c " +
            "LEFT JOIN FETCH c.subCategories sc " +
            "WHERE c.refNo = :refNo " +
            "AND (:customerId MEMBER OF c.customerIds OR :customerId IS NULL)")
    Optional<Category> findByRefNoAndCustomerId(@Param("refNo") String refNo,
                                                @Param("customerId") String customerId);

    // Additional methods for common use cases
    @Query("SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN FETCH c.subCategories sc " +
            "WHERE :customerId MEMBER OF c.customerIds")
    List<Category> findAllByCustomerId(@Param("customerId") String customerId);

    // Count query for validation
    @Query("SELECT COUNT(c) FROM Category c " +
            "WHERE c.refNo = :refNo " +
            "AND :customerId MEMBER OF c.customerIds")
    long countByRefNoAndCustomerId(@Param("refNo") String refNo,
                                   @Param("customerId") String customerId);


    @Query(value = "SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN FETCH c.subCategories sc " +
            "WHERE (:customerId MEMBER OF c.customerIds OR :customerId IS NULL)",
            countQuery = "SELECT COUNT(DISTINCT c) FROM Category c " +
                    "WHERE (:customerId MEMBER OF c.customerIds OR :customerId IS NULL)")
    Page<Category> findAllWithCustomerIdOptimized(@Param("customerId") String customerId, Pageable pageable);

}