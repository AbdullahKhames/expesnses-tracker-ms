package live.tikgik.expenses.account.dto.response;

import live.tikgik.expenses.account.entity.BudgetType;
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
public class BudgetRespDto implements CurrentUserReg {
    private String name;
    private String details;
    private Double amount;
    private String accountName;
    private String accountRefNo;
//    private String customerName;
    private boolean currentCustomerRegistered;
    private BudgetType budgetType;
    private String refNo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
