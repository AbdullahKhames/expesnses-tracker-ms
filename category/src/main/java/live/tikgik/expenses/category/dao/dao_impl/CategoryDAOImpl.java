package live.tikgik.expenses.category.dao.dao_impl;

import jakarta.ejb.Stateless;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import name.expenses.config.advice.RepoAdvice;
import name.expenses.error.exception.GeneralFailureException;
import name.expenses.features.category.dao.CategoryDAO;
import name.expenses.features.category.models.Category;
import name.expenses.features.expesnse.models.Expense;
import name.expenses.features.sub_category.models.SubCategory;
import name.expenses.globals.Page;
import name.expenses.globals.SortDirection;
import name.expenses.utils.FieldValidator;
import name.expenses.utils.PageUtil;

import java.util.*;

@Stateless
@Interceptors(RepoAdvice.class)
@Transactional
public class CategoryDAOImpl implements CategoryDAO {

    @PersistenceContext(unitName = "expenses-unit")
    private EntityManager entityManager;

    @Override
    public Category create(Category category) {
        try{
            if (category.getId() != null && entityManager.find(Category.class, category.getId()) != null) {
                return entityManager.merge(category);
            } else {
                entityManager.persist(category);
                return category;
            }
        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.ERROR_PERSISTING,
                    Map.of("original error message", ex.getMessage(),
                            "error", "there was an error with your request couldn't persist"));
        }
    }

    @Override
    public Optional<Category> get(String refNo) {
        try {
            TypedQuery<Category> categoryTypedQuery = entityManager.createQuery("SELECT e from Category e WHERE e.refNo = :refNo", Category.class);
            categoryTypedQuery.setParameter("refNo", refNo);
            return Optional.ofNullable(categoryTypedQuery.getSingleResult());
        }catch (NoResultException ex){
            return Optional.empty();
        }catch (NonUniqueResultException ex){
            throw new GeneralFailureException(GeneralFailureException.NON_UNIQUE_IDENTIFIER,
                    Map.of("error", String.format("the query didn't return a single result for reference number %s", refNo)));
        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND,
                    Map.of("original error message", ex.getMessage(),
                            "error", String.format("there was an error with your request couldn't find object with reference number %s", refNo)));
        }
    }

    @Override
    public Page<Category> findAll(Long pageNumber, Long pageSize, String sortBy, SortDirection sortDirection) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        query.select(root);

        Path<Object> sortByPath;
        if (FieldValidator.hasField(sortBy, Category.class)) {
            sortByPath = root.get(sortBy);
        }else {
            sortByPath = root.get("id");
        }

        if (sortDirection == SortDirection.ASC) {
            query.orderBy(cb.asc(sortByPath));
        } else {
            query.orderBy(cb.desc(sortByPath));
        }
        try {
            TypedQuery<Category> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) ((pageNumber - 1) * pageSize));
            typedQuery.setMaxResults(Math.toIntExact(pageSize));
            List<Category> categorys = typedQuery.getResultList();
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            countQuery.select(cb.count(countQuery.from(Category.class)));
            Long totalElements = entityManager.createQuery(countQuery).getSingleResult();
            return PageUtil.createPage(pageNumber, pageSize, categorys, totalElements);

        }catch (Exception exception){
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }

    }

    @Override
    public List<Category> get() {
        return entityManager.createQuery("SELECT e FROM Category e", Category.class).getResultList();
    }

    @Override
    public Category update(Category category) {
        try {
            entityManager.getTransaction().begin();
            for (SubCategory subCategory : category.getSubCategories()) {
                for (Expense expense : subCategory.getExpenses()) {
                    if (expense.getId() == null) {
                        entityManager.persist(expense);
                    }
                }
                if (subCategory.getId() == null) {
                    entityManager.persist(subCategory);
                }
            }
            Category updatedCategory = entityManager.merge(category);
            entityManager.getTransaction().commit();
            return updatedCategory;

        }catch (Exception ex){
            entityManager.getTransaction().rollback();
            throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't update entity"));
        }
    }

    @Override
    public String delete(String refNo) {
        try {
            TypedQuery<Category> categoryTypedQuery = entityManager.createQuery("SELECT e from Category e WHERE e.refNo = :refNo", Category.class);
            categoryTypedQuery.setParameter("refNo", refNo);
            Category category = categoryTypedQuery.getSingleResult();
            if (category != null) {
                entityManager.remove(category);
            }
            return refNo;
        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.ERROR_DELETE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't delete entity"));
        }
    }

    @Override
    public Set<Category> getEntities(Set<String> refNos) {
        if (refNos == null || refNos.isEmpty()) {
            return new HashSet<>();
        }

        TypedQuery<Category> query = entityManager.createQuery(
                "SELECT a FROM Category a WHERE a.refNo IN :refNos", Category.class);
        query.setParameter("refNos", refNos);
        return new HashSet<>(query.getResultList());
    }

    public Page<SubCategory> getSubcategories(String refNo, Long pageNumber, Long pageSize, String sortBy, SortDirection sortDirection) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SubCategory> query = cb.createQuery(SubCategory.class);
            Root<SubCategory> root = query.from(SubCategory.class);

            // Join with Category based on reference number
            Join<SubCategory, Category> categoryJoin = root.join("category", JoinType.INNER);
            Predicate refNoPredicate = cb.equal(categoryJoin.get("refNo"), refNo);

            // Where clause
            query.where(refNoPredicate);

            // Sorting
            Path<Object> sortByPath;
            if (FieldValidator.hasField(sortBy, SubCategory.class)) {
                sortByPath = root.get(sortBy);
            } else {
                sortByPath = root.get("id");
            }
            if (sortDirection == SortDirection.ASC) {
                query.orderBy(cb.asc(sortByPath));
            } else {
                query.orderBy(cb.desc(sortByPath));
            }

            // Count query
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<SubCategory> subCategoryRoot = countQuery.from(SubCategory.class);
            countQuery.select(cb.count(subCategoryRoot));
            Join<SubCategory, Category> newCategoryJoin = subCategoryRoot.join("category", JoinType.INNER);
            Predicate categoryPredicate = cb.equal(newCategoryJoin.get("refNo"), refNo);
            countQuery.where(categoryPredicate);
            Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

            // Typed query
            TypedQuery<SubCategory> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) ((pageNumber - 1) * pageSize));
            typedQuery.setMaxResults(Math.toIntExact(pageSize));
            List<SubCategory> subCategories = typedQuery.getResultList();

            return PageUtil.createPage(pageNumber, pageSize, subCategories, totalElements);
        } catch (Exception exception) {
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }
    }

    @Override
    public List<Category> getByName(String name) {
        try {
            TypedQuery<Category> categoryTypedQuery = entityManager.createQuery("SELECT e from Category e WHERE e.name like :name", Category.class);
            categoryTypedQuery.setParameter("name", "%" + name + "%");
            return categoryTypedQuery.getResultList();
        }catch (NoResultException ex){
            return Collections.emptyList();
        }catch (NonUniqueResultException ex){
            throw new GeneralFailureException(GeneralFailureException.NON_UNIQUE_IDENTIFIER,
                    Map.of("error", String.format("the query didn't return a single result for reference number %s", name)));
        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND,
                    Map.of("original error message", ex.getMessage(),
                            "error", String.format("there was an error with your request couldn't find object with reference number %s", name)));
        }
    }
}