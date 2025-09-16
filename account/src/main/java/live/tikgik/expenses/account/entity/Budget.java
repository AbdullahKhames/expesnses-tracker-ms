package live.tikgik.expenses.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import live.tikgik.expenses.shared.model.BaseEntity;
import lombok.*;

import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Budget extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String details;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private BudgetType budgetType;
    @Column(columnDefinition = "boolean default false")
    private boolean defaultReceiver = false;
    @Column(columnDefinition = "boolean default false")
    private boolean defaultSender = false;
    private String customerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Account account;

    public Budget(String name, BigDecimal amount, BudgetType budgetType, Account account, String customerId) {
        this.name = name;
        this.amount = amount;
        this.budgetType = budgetType;
        this.customerId = customerId;
        this.account = account;
    }

    public Budget(String name, BigDecimal amount, BudgetType budgetType, boolean defaultReceiver, boolean defaultSender, Account account, String customerId) {
        this.name = name;
        this.amount = amount;
        this.budgetType = budgetType;
        this.defaultReceiver = defaultReceiver;
        this.defaultSender = defaultSender;
        this.account = account;
        this.customerId = customerId;
    }

    //    @PreUpdate
//    protected void onUpdate() {
//        this.setUpdatedAt(LocalDateTime.now());
//    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Budget budget = (Budget) o;
        return getId() != null && Objects.equals(getId(), budget.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
