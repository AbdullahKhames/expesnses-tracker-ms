package live.tikgik.expenses.category.dtos.response;

import live.tikgik.expenses.shared.service.CurrentUserReg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubCategoryRespDto implements CurrentUserReg {
    private String name;
//    @Builder.Default
//    private Set<ExpenseRespDto> expenses = new HashSet<>();
    private String refNo;
    private boolean currentCustomerRegistered;
    private String details;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double totalSpent;
}
