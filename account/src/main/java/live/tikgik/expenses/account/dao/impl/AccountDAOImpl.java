package live.tikgik.expenses.account.dao.impl;


import jakarta.persistence.*;
import live.tikgik.expenses.account.dao.AccountDAO;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.repository.AccountRepository;
import live.tikgik.expenses.shared.error.exception.GeneralFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;


@Transactional
@RequiredArgsConstructor
@Service
public class AccountDAOImpl implements AccountDAO {

//    @PersistenceContext(unitName = "expenses-unit")
//    private EntityManager entityManager;
    private final AccountRepository accountRepository;

    @Override
    public Account create(Account account) {
        try {
//            entityManager.getTransaction().begin();
//
//            if (account.getId() != null && entityManager.find(Account.class, account.getId()) != null) {
//                // Object exists in the database, so update it
//                account = entityManager.merge(account);
//            } else {
//                // Object doesn't exist, so persist it
//                entityManager.persist(account);
//            }
//
//            entityManager.getTransaction().commit();
//            return account;
            return accountRepository.save(account);
        } catch (Exception ex) {
//            entityManager.getTransaction().rollback();
            throw new GeneralFailureException(GeneralFailureException.ERROR_PERSISTING,
                    Map.of("original error message", ex.getMessage(),
                            "error", "there was an error with your request couldn't persist"));
        }
    }

    @Override
    public Optional<Account> get(String refNo) {
        try {
//            TypedQuery<Account> accountTypedQuery = entityManager.createQuery("SELECT e from Account e WHERE e.refNo = :refNo", Account.class);
//            accountTypedQuery.setParameter("refNo", refNo);
//            return Optional.ofNullable(accountTypedQuery.getSingleResult());
            return accountRepository.findByRefNo(refNo);
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
    public Page<Account> findAll(Pageable pageable) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Account> query = cb.createQuery(Account.class);
//        Root<Account> root = query.from(Account.class);
//
//        query.select(root);
//
//        Path<Object> sortByPath;
//        if (FieldValidator.hasField(sortBy, Account.class)) {
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
//            TypedQuery<Account> typedQuery = entityManager.createQuery(query);
//            typedQuery.setFirstResult((int) ((pageNumber - 1) * pageSize));
//            typedQuery.setMaxResults(Math.toIntExact(pageSize));
//            List<Account> accounts = typedQuery.getResultList();
//            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//            countQuery.select(cb.count(countQuery.from(Account.class)));
//            Long totalElements = entityManager.createQuery(countQuery).getSingleResult();
//            return PageUtil.createPage(pageNumber, pageSize, accounts, totalElements);

            return accountRepository.findAll(pageable);
        }catch (Exception exception){
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }

    }

    @Override
    public List<Account> get() {
//        return entityManager.createQuery("SELECT e FROM Account e", Account.class).getResultList();
        return accountRepository.findAll();
    }

    @Override
    public Account update(Account account) {
        try {
//            entityManager.getTransaction().begin();
//            Account updatedAccount = entityManager.merge(account);
//            entityManager.getTransaction().commit();
//            return updatedAccount;
            return accountRepository.save(account);

        }catch (Exception ex){
//            entityManager.getTransaction().rollback();
            throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't update entity"));
        }
    }

    @Override
    public String delete(String refNo) {
        try {
//            TypedQuery<Account> accountTypedQuery = entityManager.createQuery("SELECT e from Account e WHERE e.refNo = :refNo", Account.class);
//            accountTypedQuery.setParameter("refNo", refNo);
//            Account account = accountTypedQuery.getSingleResult();
//            if (account != null) {
//                entityManager.remove(account);
//            }
            accountRepository.deleteByRefNo(refNo);
            return refNo;
        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.ERROR_DELETE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't delete entity"));
        }
    }

    @Override
    public Set<Account> getEntities(Set<String> refNos) {
        if (refNos == null || refNos.isEmpty()) {
            return new HashSet<>();
        }

//        TypedQuery<Account> query = entityManager.createQuery(
//                "SELECT a FROM Account a WHERE a.refNo IN :refNos", Account.class);
//        query.setParameter("refNos", refNos);
//        return new HashSet<>(query.getResultList());
        return accountRepository.findAllByRefNoIn(refNos);
    }

    @Override
    public List<Account> getByName(String name) {
        try {
//            TypedQuery<Account> categoryTypedQuery = entityManager.createQuery("SELECT e from Account e WHERE e.name like :name", Account.class);
//            categoryTypedQuery.setParameter("name", "%" + name + "%");
//            return categoryTypedQuery.getResultList();
            return accountRepository.findAllByNameLike(name);
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
    public Account getDefaultAccount() {
        return accountRepository.findById(1L).orElse(null);
    }

    @Override
    public boolean existByNameAndCustomerIdsContaining(String name, String customerId) {
        return accountRepository.existsByNameAndCustomerIdsContaining(name, customerId);
    }
}