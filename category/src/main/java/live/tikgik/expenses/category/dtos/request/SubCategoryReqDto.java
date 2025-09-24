package live.tikgik.expenses.category.dtos.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Valid
public class SubCategoryReqDto {
    @NotNull
    private String name;
    private String details;
    private String categoryRefNo;
}
