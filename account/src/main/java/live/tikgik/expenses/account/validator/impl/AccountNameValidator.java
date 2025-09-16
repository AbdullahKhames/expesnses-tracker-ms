package live.tikgik.expenses.account.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import live.tikgik.expenses.account.config.UserContextHolder;
import live.tikgik.expenses.account.dao.AccountDAO;
import live.tikgik.expenses.account.validator.AccountName;
import live.tikgik.expenses.shared.constant.ErrorMsg;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountNameValidator implements ConstraintValidator<AccountName, String> {

    private final AccountDAO accountDAO;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (accountDAO.existByNameAndCustomerIdsContaining(name, UserContextHolder.getUser().getId())) {
            context.buildConstraintViolationWithTemplate(ErrorMsg.ACCOUNT_ALREADY_EXIST_FOR_USER).addConstraintViolation();
            return false;
        }
        return true;
    }
}
