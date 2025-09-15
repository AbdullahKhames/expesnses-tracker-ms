package live.tikgik.expenses.shared.validators.validatorclasses;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import live.tikgik.expenses.shared.validators.validatoranootations.UserNameValidator;

public class UniqueUserNameValidator implements ConstraintValidator<UserNameValidator, String> {

//    public UniqueUserNameValidator() {
//        this.userRepo = (UserRepo) ContextProvider.getBean(UserRepo.class);
//    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }

    @Override
    public void initialize(UserNameValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
