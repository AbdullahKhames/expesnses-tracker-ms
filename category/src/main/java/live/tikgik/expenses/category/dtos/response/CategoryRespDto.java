package live.tikgik.expenses.category.dtos.response;

import live.tikgik.expenses.shared.service.CurrentUserReg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryRespDto implements CurrentUserReg {
    private String name;
    @Builder.Default
    private Set<SubCategoryRespDto> subCategories = new HashSet<>();
    private String details;
    private Double totalSpent;
    private String refNo;
    private boolean currentCustomerRegistered;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
