package live.tikgik.expenses.shared.error.exception_handler.handlers;


import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.error.exception_handler.services.ExceptionHandlerStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@Service
@Qualifier("methodArgumentNotValidExceptionHandler")
public class MethodArgumentNotValidExceptionHandler implements ExceptionHandlerStrategy {
    @Override
    public ApiResponse handleException(Throwable throwable) {
        MethodArgumentNotValidException e = (MethodArgumentNotValidException) throwable;
        Map<String, String> res = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(violation -> {
            String fieldName = violation.getField();
            String message = violation.getDefaultMessage();
            res.put(fieldName, message);
        });
        return ApiResponse.failed(811, res);
    }
}
