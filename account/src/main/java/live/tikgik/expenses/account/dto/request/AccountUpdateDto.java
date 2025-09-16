package live.tikgik.expenses.account.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import live.tikgik.expenses.account.validator.AccountName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Valid
@Builder
public class AccountUpdateDto {
    @NotNull
    @AccountName
    private String name;
    private String refNo;
    private String details;
    @Builder.Default
    private Set<BudgetUpdateDto> budgets = new HashSet<>();

}
