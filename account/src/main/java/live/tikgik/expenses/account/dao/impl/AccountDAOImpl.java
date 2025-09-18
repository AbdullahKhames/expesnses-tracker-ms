package live.tikgik.expenses.account.dao.impl;


import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
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

    private final AccountRepository accountRepository;

    @Override
    public Account create(Account account) {
        try {
            return accountRepository.save(account);
        } catch (Exception ex) {
            throw new GeneralFailureException(GeneralFailureException.ERROR_PERSISTING,
                    Map.of("original error message", ex.getMessage(),
                            "error", "there was an error with your request couldn't persist"));
        }
    }

    @Override
    public Optional<Account> get(String refNo, String customerId) {
        try {
            return accountRepository.findByRefNoAndCustomerId(refNo, customerId);
        } catch (NoResultException ex) {
            return Optional.empty();
        } catch (NonUniqueResultException ex) {
            throw new GeneralFailureException(GeneralFailureException.NON_UNIQUE_IDENTIFIER,
                    Map.of("error", String.format("the query didn't return a single result for reference number %s", refNo)));
        } catch (Exception ex) {
            throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND,
                    Map.of("original error message", ex.getMessage(),
                            "error", String.format("there was an error with your request couldn't find object with reference number %s", refNo)));
        }
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        try {
            return accountRepository.findAll(pageable);
        } catch (Exception exception) {
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }

    }

    @Override
    public Page<Account> findAll(Pageable pageable, String customerId) {
        try {
            return accountRepository.findByCustomerId(customerId, pageable);
        } catch (Exception exception) {
            throw new GeneralFailureException(GeneralFailureException.ERROR_FETCH,
                    Map.of("original message", exception.getMessage().substring(0, 15),
                            "error", "there was an error with your request"));
        }

    }

    @Override
    public List<Account> get(String customerId) {
        return accountRepository.findByCustomerId(customerId, Pageable.unpaged()).getContent();
    }


    @Override
    public Account update(Account account) {
        try {
            return accountRepository.save(account);

        } catch (Exception ex) {
            throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't update entity"));
        }
    }

    @Override
    public String delete(String refNo, String customerId) {
        try {
            accountRepository.deleteByRefNoAndCustomerId(refNo, customerId);
            return refNo;
        } catch (Exception ex) {
            throw new GeneralFailureException(GeneralFailureException.ERROR_DELETE,
                    Map.of("original error message", ex.getMessage().substring(0, 15),
                            "error", "there was an error with your request couldn't delete entity"));
        }
    }

    @Override
    public Set<Account> getEntities(Set<String> refNos, String customerId) {
        if (refNos == null || refNos.isEmpty()) {
            return new HashSet<>();
        }
        return accountRepository.findByRefNosAndCustomerId(refNos, customerId);
    }

    @Override
    public List<Account> getByName(String name, String customerId) {
        try {
            return accountRepository.findAllByNameLikeAndCustomerId(name, customerId);
        } catch (NoResultException ex) {
            return Collections.emptyList();
        } catch (NonUniqueResultException ex) {
            throw new GeneralFailureException(GeneralFailureException.NON_UNIQUE_IDENTIFIER,
                    Map.of("error", String.format("the query didn't return a single result for reference number %s", name)));
        } catch (Exception ex) {
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
        return accountRepository.existsByNameAndCustomerId(name, customerId);
    }
}