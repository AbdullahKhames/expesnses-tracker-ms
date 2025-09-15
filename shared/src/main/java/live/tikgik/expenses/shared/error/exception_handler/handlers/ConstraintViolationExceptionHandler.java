package live.tikgik.expenses.shared.error.exception_handler.handlers;



import jakarta.validation.ConstraintViolationException;
import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.error.exception_handler.services.ExceptionHandlerStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Qualifier("constraintViolationExceptionHandler")
public class ConstraintViolationExceptionHandler implements ExceptionHandlerStrategy {
    @Override
    public ApiResponse handleException(Throwable throwable) {
        ConstraintViolationException e = (ConstraintViolationException) throwable;
        Map<String, String> res = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            res.put(fieldName, message);
        });
        return ApiResponse.failed(811, res);
    }
}
