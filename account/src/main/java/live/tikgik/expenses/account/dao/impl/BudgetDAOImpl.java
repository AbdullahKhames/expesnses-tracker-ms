package live.tikgik.expenses.account.dao.impl;


import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import live.tikgik.expenses.account.dao.BudgetDAO;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.entity.Budget;
import live.tikgik.expenses.account.repository.BudgetRepository;
import live.tikgik.expenses.shared.error.exception.GeneralFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
@Service
public class BudgetDAOImpl implements BudgetDAO {

//    @PersistenceContext(unitName = "expenses-unit")
//    private EntityManager entityManager;
    private final BudgetRepository budgetRepository;

    @Override
    public Budget create(Budget Budget) {
        try{
//            if (Budget.getId() != null && entityManager.find(Budget.class, Budget.getId()) != null) {
//                return entityManager.merge(Budget);
//            } else {
//                entityManager.persist(Budget);
//                return Budget;
//            }
            return budgetRepository.save(Budget);
        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.ERROR_PERSISTING,
                    Map.of("original error message", ex.getMessage(),
                            "error", "there was an error with your request couldn't persist"));
        }
    }

    @Override
    public Optional<Budget> get(String refNo) {
        try {
//            TypedQuery<Budget> BudgetTypedQuery = entityManager.createQuery("SELECT e from Budget e WHERE e.refNo = :refNo", Budget.class);
//            BudgetTypedQuery.setParameter("refNo", refNo);
//            return Optional.ofNullable(BudgetTypedQuery.getSingleResult());
            return budgetRepository.findByRefNo(refNo);
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
    public Page<Budget> findAll(Pageable pageable) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Budget> query = cb.createQuery(Budget.class);
//        Root<Budget> root = query.from(Budget.class);
//
//        query.select(root);
//
//        Path<Object> sortByPath;
//        if (FieldValidator.hasField(sortBy, Budget.class)) {
//            sortByPath = root.get(sortBy);
//        }else {
//            sortByPath = root.get("id");
//        }
//
//        if (sortDirection == SortDirection.ASC) {
//            query.orderBy(cb.asc(sortByPath));
//        } else {
//            query.orderBy(cb.desc(sortByPath));
//        }
        try {
//            TypedQuery<Budget> typedQuery = entityManager.createQuery(query);
//            typedQuery.setFirstResult((int) ((pageNumber - 1) * pageSize));
//            typedQuery.setMaxResults(Math.toIntExact(pageSize));
//            List<Budget> Budgets = typedQuery.getResultList();
//            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//            countQuery.select(cb.count(countQuery.from(Budget.class)));
//            Long totalElements = entityManager.createQuery(countQuery).getSingleResult();
//            return PageUtil.createPage(pageNumber, pageSize, Budgets, totalElements);
            return budgetRepository.findAll(pageable);
        }catch (Exception exception){
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }

    }
    @Override
    public Page<Budget> findAllWithoutAccount(Pageable pageable) {
        try {
//            TypedQuery<Budget> typedQuery = entityManager.createQuery("SELECT p FROM Budget p WHERE p.account is null", Budget.class);
//            List<Budget> Budgets = typedQuery.getResultList();
//            return PageUtil.createPage(pageNumber, pageSize, Budgets, Budgets.size());
            return budgetRepository.findAllWithoutAccount(pageable);

        } catch (Exception exception) {
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }
    }
    @Override
    public List<Budget> get() {
//        return entityManager.createQuery("SELECT e FROM Budget e", Budget.class).getResultList();
        return budgetRepository.findAll();
    }



    @Override
    public String delete(String refNo) {
        try {
//            TypedQuery<Budget> BudgetTypedQuery = entityManager.createQuery("SELECT e from Budget e WHERE e.refNo = :refNo", Budget.class);
//            BudgetTypedQuery.setParameter("refNo", refNo);
//            Budget Budget = BudgetTypedQuery.getSingleResult();
//            if (Budget != null) {
//                entityManager.remove(Budget);
//            }
            budgetRepository.deleteByRefNo(refNo);
            return refNo;
        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.ERROR_DELETE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't delete entity"));
        }
    }

    @Override
    public Long checkAccountAssociation(Budget Budget) {
        Account account;
        try{
//            TypedQuery<Account> query = entityManager.createQuery("SELECT c FROM Account c JOIN c.budgets s WHERE s.id = :id", Account.class);
//            query.setParameter("id", Budget.getId());
//            account = query.getSingleResult();
            account = budgetRepository.findAccountById(Budget.getId());
        }catch (Exception ex){
            return null;
        }
        return account.getId();
    }

    @Override
    public Set<Budget> getEntities(Set<String> refNos) {
        if (refNos == null || refNos.isEmpty()) {
            return new HashSet<>();
        }

//        TypedQuery<Budget> query = entityManager.createQuery(
//                "SELECT a FROM Budget a WHERE a.refNo IN :refNos", Budget.class);
//        query.setParameter("refNos", refNos);
//        return new HashSet<>(query.getResultList());
        return budgetRepository.findByRefNoIn(refNos);
    }

    @Override
    public Set<Budget> saveAll(Set<Budget> Budgets) {
        Set<Budget> savedBudgets = new HashSet<>();
        if (Budgets != null && !Budgets.isEmpty()) {
            for (Budget Budget : Budgets) {
                if (Budget != null && Budget.getId() != null) {
//                    entityManager.persist(Budget);
                    budgetRepository.save(Budget);
                    savedBudgets.add(Budget);
                }
            }
//            entityManager.flush();
//            entityManager.clear();
        }
        return savedBudgets;
    }
    @Override
    public Budget update(Budget Budget) {
        try {
//            entityManager.getTransaction().begin();
//            Budget updatedBudget = entityManager.merge(Budget);
//            entityManager.getTransaction().commit();
//            return updatedBudget;
            return budgetRepository.save(Budget);

        }catch (Exception ex){
//            entityManager.getTransaction().rollback();
            throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't update entity"));
        }
    }

    @Override
    public List<Budget> getByName(String name) {
        try {
//            TypedQuery<Budget> categoryTypedQuery = entityManager.createQuery("SELECT e from Budget e WHERE e.name like :name", Budget.class);
//            categoryTypedQuery.setParameter("name", "%" + name + "%");
//            return categoryTypedQuery.getResultList();
            return budgetRepository.findByName(name);
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
    public Set<Budget> updateAll(Set<Budget> Budgets) {
        return Budgets
                .stream()
                .map(this::update)
                .collect(Collectors.toSet());
    }
}