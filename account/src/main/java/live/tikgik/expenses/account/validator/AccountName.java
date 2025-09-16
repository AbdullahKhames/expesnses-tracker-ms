package live.tikgik.expenses.account.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import live.tikgik.expenses.account.validator.impl.AccountNameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AccountNameValidator.class)
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountName {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
