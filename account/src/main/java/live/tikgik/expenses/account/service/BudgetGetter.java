package live.tikgik.expenses.account.service;

import live.tikgik.expenses.account.entity.Budget;
import live.tikgik.expenses.shared.service.GetRefNo;

import java.util.Set;

public interface BudgetGetter extends GetRefNo {
    Set<Budget> getBudgets();
    void setBudgets(Set<Budget> budgets);
}