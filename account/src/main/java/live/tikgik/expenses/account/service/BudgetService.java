package live.tikgik.expenses.account.service;



import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.dto.request.BudgetReqDto;
import live.tikgik.expenses.account.dto.request.BudgetUpdateDto;
import live.tikgik.expenses.account.dto.response.BudgetRespDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.entity.Budget;
import live.tikgik.expenses.account.entity.BudgetType;
import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.service.CollectionAdder;
import live.tikgik.expenses.shared.service.CollectionRemover;
import live.tikgik.expenses.shared.service.CrudService;
import live.tikgik.expenses.shared.service.UpdateAssociation;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface BudgetService extends
        CollectionAdder<Account>,
        CollectionRemover<Account>,
        UpdateAssociation<Account, AccountUpdateDto>,
        CrudService<BudgetReqDto, BudgetUpdateDto, String, Budget> {
    ApiResponse create(BudgetReqDto expense);
    Budget createBudget(BudgetType budgetType, Account sentAccount, String customerId, boolean defaultReceiver, boolean defaultSender);
    void associateAccount(String accountRefNo, Budget sentBudget);

    ApiResponse get(String refNo);

    ApiResponse update(String refNo, BudgetUpdateDto expense);

    Budget createDefaultBudget();

    ApiResponse delete(String refNo);

    ApiResponse getAllEntities(Pageable pageable);

    Set<Budget> updateAll(Set<Budget> Budgets);
    ApiResponse getAllEntitiesWithoutAccount(Pageable pageable);

    Set<BudgetRespDto> entityToRespDto(Set<Budget> Budgets);

    ApiResponse getBudgetByName(String name);

    Budget update(Budget oldBudget);


    void updateBudgets(Set<BudgetUpdateDto> updateDtoBudgets, Account account);

    void deleteByAccountRefNo(String refNo);
}