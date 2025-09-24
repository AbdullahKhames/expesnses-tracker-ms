package live.tikgik.expenses.category.repository;

import live.tikgik.expenses.category.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    @Modifying
    void deleteByRefNo(String refNo);
    Optional<SubCategory> findByRefNo(String refNo);

    Page<SubCategory> findByCategoryNull(Pageable pageable);

    List<SubCategory> findByNameLikeIgnoreCase(String name);

    Set<SubCategory> findByRefNoIn(Collection<String> refNos);

    @Query("SELECT c.id FROM Category c JOIN c.subCategories s WHERE s.id = :id")
    Long checkCategoryAssociation(Long id);

    Page<SubCategory> findByCategory_RefNo(String refNo, Pageable pageable);
    // Paginated findAll with customer ID filtering
    @Query(value = "SELECT DISTINCT sc FROM SubCategory sc " +
            "LEFT JOIN FETCH sc.category c " +
            "WHERE (:customerId MEMBER OF sc.customerIds OR :customerId IS NULL)",
            countQuery = "SELECT COUNT(DISTINCT sc) FROM SubCategory sc " +
                    "WHERE (:customerId MEMBER OF sc.customerIds OR :customerId IS NULL)")
    Page<SubCategory> findAllWithCustomerId(@Param("customerId") String customerId, Pageable pageable);

    // Paginated search by name
    @Query(value = "SELECT DISTINCT sc FROM SubCategory sc " +
            "LEFT JOIN FETCH sc.category c " +
            "WHERE LOWER(sc.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND (:customerId MEMBER OF sc.customerIds OR :customerId IS NULL)",
            countQuery = "SELECT COUNT(DISTINCT sc) FROM SubCategory sc " +
                    "WHERE LOWER(sc.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
                    "AND (:customerId MEMBER OF sc.customerIds OR :customerId IS NULL)")
    Page<SubCategory> findByNameContainingIgnoreCaseAndCustomerId(@Param("name") String name,
                                                                  @Param("customerId") String customerId,
                                                                  Pageable pageable);

    // Paginated subcategories by category ID
    @Query(value = "SELECT DISTINCT sc FROM SubCategory sc " +
            "LEFT JOIN FETCH sc.category c " +
            "WHERE sc.category.id = :categoryId " +
            "AND (:customerId MEMBER OF sc.customerIds OR :customerId IS NULL)",
            countQuery = "SELECT COUNT(DISTINCT sc) FROM SubCategory sc " +
                    "WHERE sc.category.id = :categoryId " +
                    "AND (:customerId MEMBER OF sc.customerIds OR :customerId IS NULL)")
    Page<SubCategory> findByCategoryIdAndCustomerId(@Param("categoryId") Long categoryId,
                                                    @Param("customerId") String customerId,
                                                    Pageable pageable);

    // Previous methods remain the same
    @Query("SELECT DISTINCT sc FROM SubCategory sc " +
            "LEFT JOIN FETCH sc.category c " +
            "WHERE sc.refNo IN :refNos " +
            "AND (:customerId MEMBER OF sc.customerIds OR :customerId IS NULL)")
    Set<SubCategory> findByRefNoInAndCustomerId(@Param("refNos") Collection<String> refNos,
                                                @Param("customerId") String customerId);

    @Query("SELECT DISTINCT sc FROM SubCategory sc " +
            "LEFT JOIN FETCH sc.category c " +
            "WHERE LOWER(sc.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND (:customerId MEMBER OF sc.customerIds OR :customerId IS NULL)")
    List<SubCategory> findByNameLikeIgnoreCaseAndCustomerId(@Param("name") String name,
                                                            @Param("customerId") String customerId);

    @Query("SELECT sc FROM SubCategory sc " +
            "LEFT JOIN FETCH sc.category c " +
            "WHERE sc.refNo = :refNo " +
            "AND (:customerId MEMBER OF sc.customerIds OR :customerId IS NULL)")
    Optional<SubCategory> findByRefNoAndCustomerId(@Param("refNo") String refNo,
                                                   @Param("customerId") String customerId);

    @Query("SELECT DISTINCT sc FROM SubCategory sc " +
            "LEFT JOIN FETCH sc.category c " +
            "WHERE :customerId MEMBER OF sc.customerIds")
    List<SubCategory> findAllByCustomerId(@Param("customerId") String customerId);
}