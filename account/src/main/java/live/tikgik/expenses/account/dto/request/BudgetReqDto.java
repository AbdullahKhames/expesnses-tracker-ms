package live.tikgik.expenses.account.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
@Builder
@Valid
public class BudgetReqDto {
    @NotNull
    private String name;
    private String details;
    @NotNull
    private Double amount;
    @NotNull
    @EnumNamePattern(regexp = "MAIN|SAVINGS|BILLS|ALLOWANCE|MISC|DONATION|EXTERNAL")
    private BudgetType budgetType;
    @NotNull
    @NotBlank
    private String accountRefNo;

}

