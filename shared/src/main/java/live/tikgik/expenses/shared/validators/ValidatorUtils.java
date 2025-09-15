package live.tikgik.expenses.shared.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class ValidatorUtils {
    public Set<ConstraintViolation<Object>> hasValidationErrors(Object object) {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        return violations;
    }
}