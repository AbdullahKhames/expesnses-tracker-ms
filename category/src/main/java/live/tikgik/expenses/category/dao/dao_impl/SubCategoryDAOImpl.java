package live.tikgik.expenses.category.dao.dao_impl;

import jakarta.ejb.Stateless;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import name.expenses.config.advice.RepoAdvice;
import name.expenses.error.exception.GeneralFailureException;
import name.expenses.features.category.models.Category;
import name.expenses.features.sub_category.dao.SubCategoryDAO;
import name.expenses.features.sub_category.models.SubCategory;
import name.expenses.globals.Page;
import name.expenses.globals.SortDirection;
import name.expenses.utils.FieldValidator;
import name.expenses.utils.PageUtil;

import java.util.*;

@Stateless
@Interceptors(RepoAdvice.class)
@Transactional
public class SubCategoryDAOImpl implements SubCategoryDAO {

    @PersistenceContext(unitName = "expenses-unit")
    private EntityManager entityManager;

    @Override
    public SubCategory create(SubCategory subCategory) {
        try{
            if (subCategory.getId() != null && entityManager.find(SubCategory.class, subCategory.getId()) != null) {
                return entityManager.merge(subCategory);
            } else {
                entityManager.persist(subCategory);
                return subCategory;
            }
        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.ERROR_PERSISTING,
                    Map.of("original error message", ex.getMessage(),
                            "error", "there was an error with your request couldn't persist"));
        }
    }

    @Override
    public Optional<SubCategory> get(String refNo) {
        try {
            TypedQuery<SubCategory> subCategoryTypedQuery = entityManager.createQuery("SELECT e from SubCategory e WHERE e.refNo = :refNo", SubCategory.class);
            subCategoryTypedQuery.setParameter("refNo", refNo);
            return Optional.ofNullable(subCategoryTypedQuery.getSingleResult());
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
    public Page<SubCategory> findAll(Long pageNumber, Long pageSize, String sortBy, SortDirection sortDirection) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SubCategory> query = cb.createQuery(SubCategory.class);
        Root<SubCategory> root = query.from(SubCategory.class);

        query.select(root);

        Path<Object> sortByPath;
        if (FieldValidator.hasField(sortBy, SubCategory.class)) {
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
            TypedQuery<SubCategory> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) ((pageNumber - 1) * pageSize));
            typedQuery.setMaxResults(Math.toIntExact(pageSize));
            List<SubCategory> subCategorys = typedQuery.getResultList();
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            countQuery.select(cb.count(countQuery.from(SubCategory.class)));
            Long totalElements = entityManager.createQuery(countQuery).getSingleResult();
            return PageUtil.createPage(pageNumber, pageSize, subCategorys, totalElements);

        }catch (Exception exception){
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }

    }
    @Override
    public Page<SubCategory> findAllWithoutCategory(Long pageNumber, Long pageSize, String sortBy, SortDirection sortDirection) {
        try {
            TypedQuery<SubCategory> typedQuery = entityManager.createQuery("SELECT s FROM SubCategory s WHERE s.category is null", SubCategory.class);
            List<SubCategory> subCategorys = typedQuery.getResultList();
            return PageUtil.createPage(pageNumber, pageSize, subCategorys, subCategorys.size());

        } catch (Exception exception) {
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }
    }

    @Override
    public List<SubCategory> getByName(String name) {
        try {
            TypedQuery<SubCategory> categoryTypedQuery = entityManager.createQuery("SELECT e from SubCategory e WHERE e.name like :name", SubCategory.class);
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

    @Override
    public List<SubCategory> get() {
        return entityManager.createQuery("SELECT e FROM SubCategory e", SubCategory.class).getResultList();
    }

    @Override
    public SubCategory update(SubCategory subCategory) {
        try {
            return entityManager.merge(subCategory);
        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't update entity"));
        }
    }

    @Override
    public String delete(String refNo) {
        try {
            TypedQuery<SubCategory> subCategoryTypedQuery = entityManager.createQuery("SELECT e from SubCategory e WHERE e.refNo = :refNo", SubCategory.class);
            subCategoryTypedQuery.setParameter("refNo", refNo);
            SubCategory subCategory = subCategoryTypedQuery.getSingleResult();
            if (subCategory != null) {
                entityManager.remove(subCategory);
            }
            return refNo;
        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.ERROR_DELETE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't delete entity"));
        }
    }

    @Override
    public Long checkCategoryAssociation(SubCategory subCategory) {
        Category category;
        try{
            TypedQuery<Category> query = entityManager.createQuery("SELECT c FROM Category c JOIN c.subCategories s WHERE s.id = :id", Category.class);
            query.setParameter("id", subCategory.getId());
            category = query.getSingleResult();
        }catch (Exception ex){
            return null;
        }
        return category.getId();
    }

    @Override
    public Set<SubCategory> getEntities(Set<String> refNos) {
        if (refNos == null || refNos.isEmpty()) {
            return new HashSet<>();
        }

        TypedQuery<SubCategory> query = entityManager.createQuery(
                "SELECT a FROM SubCategory a WHERE a.refNo IN :refNos", SubCategory.class);
        query.setParameter("refNos", refNos);
        List<SubCategory> accounts = query.getResultList();

        return new HashSet<>(accounts);
    }
}