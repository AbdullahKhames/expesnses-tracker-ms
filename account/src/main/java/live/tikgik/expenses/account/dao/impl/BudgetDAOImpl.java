package live.tikgik.expenses.account.dao.impl;


import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import live.tikgik.expenses.account.config.UserContextHolder;
import live.tikgik.expenses.account.dao.BudgetDAO;
import live.tikgik.expenses.account.entity.Budget;
import live.tikgik.expenses.account.repository.BudgetRepository;
import live.tikgik.expenses.shared.error.exception.GeneralFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class BudgetDAOImpl implements BudgetDAO {
    private final BudgetRepository budgetRepository;

    @Override
    public void deleteByAccountRefNo(String refNo) {
        budgetRepository.deleteByAccount_RefNo(refNo);
    }

    @Override
    public Budget create(Budget Budget) {
        try{
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
            return budgetRepository.findByRefNoAndCustomerId(refNo, UserContextHolder.getUser().getId());
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
    public Page<Budget> findAllForCustomer(Pageable pageable) {
        try {
            return budgetRepository.findByCustomerId(UserContextHolder.getUser().getId(), pageable);
        }catch (Exception exception){
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }

    }
    @Override
    public Page<Budget> findAllWithoutAccount(Pageable pageable) {
        try {
            return budgetRepository.findAllWithoutAccount(pageable);

        } catch (Exception exception) {
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }
    }
    @Override
    public List<Budget> get() {
        return budgetRepository.findByCustomerId(UserContextHolder.getUser().getId(), Pageable.unpaged()).getContent();
    }



    @Override
    public String delete(String refNo) {
        try {
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
        try{
            return budgetRepository.findAccountById(Budget.getId());
        }catch (Exception ex){
            return null;
        }
    }

    @Override
    public Set<Budget> getEntities(Set<String> refNos) {
        if (refNos == null || refNos.isEmpty()) {
            return new HashSet<>();
        }
        return budgetRepository.findByRefNoInAndCustomerId(refNos, UserContextHolder.getUser().getId());
    }

    @Override
    public Set<Budget> saveAll(Set<Budget> Budgets) {
        Set<Budget> savedBudgets = new HashSet<>();
        if (Budgets != null && !Budgets.isEmpty()) {
            for (Budget Budget : Budgets) {
                if (Budget != null && Budget.getId() != null) {
                    budgetRepository.save(Budget);
                    savedBudgets.add(Budget);
                }
            }
        }
        return savedBudgets;
    }
    @Override
    public Budget update(Budget Budget) {
        try {
            return budgetRepository.save(Budget);

        }catch (Exception ex){
            throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't update entity"));
        }
    }

    @Override
    public List<Budget> getByName(String name) {
        try {
            return budgetRepository.findByNameAndCustomerId(name, UserContextHolder.getUser().getId());
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