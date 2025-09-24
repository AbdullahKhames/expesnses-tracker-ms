package live.tikgik.expenses.category.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class SubCategoryUpdateDto {
    @NotNull
    private String name;
    private String refNo;
    private String details;

//    @Builder.Default
//    @Valid
//    private Set<ExpenseUpdateDto> expenses = new HashSet<>();
}
