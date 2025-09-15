package live.tikgik.expenses.shared.validators.validatoranootations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import live.tikgik.expenses.shared.validators.validatorclasses.UniqueUserNameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserNameValidator.class)
public @interface UserNameValidator {
    String message() default "username must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
