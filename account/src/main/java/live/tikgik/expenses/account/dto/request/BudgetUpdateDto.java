package live.tikgik.expenses.account.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import live.tikgik.expenses.account.entity.BudgetType;
import live.tikgik.expenses.shared.validators.validatoranootations.EnumNamePattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Valid
@Builder
public class BudgetUpdateDto {
    @NotNull
    private String name;
    private String refNo;
    private String details;
    @NotNull
    @EnumNamePattern(regexp = "ENTERTAINMENT|SAVINGS|BILLS|ALLOWANCE|MOM|MISC|DONATION|EXTERNAL")
    private BudgetType budgetType;
    @NotNull
    private Double amount;
}
