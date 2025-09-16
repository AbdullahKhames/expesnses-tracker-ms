package live.tikgik.expenses.account.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import live.tikgik.expenses.account.validator.AccountName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Valid
public class AccountReqDto {
    @NotNull
    @AccountName
    private String name;
    private String details;
}

