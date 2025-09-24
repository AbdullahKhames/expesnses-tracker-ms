package live.tikgik.expenses.shared.error.exception_handler.services;


import jakarta.validation.ConstraintViolationException;
import live.tikgik.expenses.shared.error.exception.CustomException;
import live.tikgik.expenses.shared.error.exception_handler.handlers.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;


@Service
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFactory {
    private final CustomExceptionHandler customExceptionHandler;
    private final GeneralExceptionHandler generalExceptionHandler;
    private final ConstraintViolationExceptionHandler constraintViolationExceptionHandler;
    private final MethodArgumentNotValidExceptionHandler methodArgumentNotValidExceptionHandler;

    public ExceptionHandlerStrategy getExceptionHandler(Throwable throwable) {
        try{
            switch (throwable) {

                case CustomException customException -> {
                    return customExceptionHandler;
                }
                case ConstraintViolationException constraintViolationException -> {
                    return constraintViolationExceptionHandler;
                }
                case MethodArgumentNotValidException methodArgumentNotValidException -> {
                    return methodArgumentNotValidExceptionHandler;
                }
                case null, default -> {
                    return generalExceptionHandler;
                }

            }

        }catch (Exception ex){
            return generalExceptionHandler;
        }
    }
}
