package live.tikgik.expenses.account.service;


import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.dto.request.BudgetReqDto;
import live.tikgik.expenses.account.dto.request.BudgetUpdateDto;
import live.tikgik.expenses.account.dto.response.BudgetRespDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.entity.Budget;
import live.tikgik.expenses.account.entity.BudgetType;
import live.tikgik.expenses.shared.service.CollectionAdder;
import live.tikgik.expenses.shared.service.CollectionRemover;
import live.tikgik.expenses.shared.service.CrudService;
import live.tikgik.expenses.shared.service.UpdateAssociation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface BudgetService extends
        CollectionAdder<Account>,
        CollectionRemover<Account>,
        UpdateAssociation<Account, AccountUpdateDto>,
        CrudService<BudgetReqDto, BudgetUpdateDto, String, Budget> {
    Budget createBudget(BudgetType budgetType, Account sentAccount, String customerId, boolean defaultReceiver, boolean defaultSender);

    void associateAccount(String accountRefNo, Budget sentBudget);

    Budget createDefaultBudget();

    Set<Budget> updateAll(Set<Budget> Budgets);

    Page<Budget> getAllEntitiesWithoutAccount(Pageable pageable);

    Set<BudgetRespDto> entityToRespDto(Set<Budget> Budgets);

    List<Budget> getBudgetByName(String name);

    Budget update(Budget oldBudget);

    void updateBudgets(Set<BudgetUpdateDto> updateDtoBudgets, Account account);

    void deleteByAccountRefNo(String refNo);
}