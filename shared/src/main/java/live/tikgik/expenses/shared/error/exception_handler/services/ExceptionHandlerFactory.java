package live.tikgik.expenses.shared.error.exception_handler.services;



import jakarta.validation.ConstraintViolationException;
import live.tikgik.expenses.shared.error.exception.ApiValidationException;
import live.tikgik.expenses.shared.error.exception.CustomException;
import live.tikgik.expenses.shared.error.exception_handler.handlers.ApiValidationExceptionHandler;
import live.tikgik.expenses.shared.error.exception_handler.handlers.ConstraintViolationExceptionHandler;
import live.tikgik.expenses.shared.error.exception_handler.handlers.CustomExceptionHandler;
import live.tikgik.expenses.shared.error.exception_handler.handlers.GeneralExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ExceptionHandlerFactory {
    @Autowired
    @Qualifier("apiValidationExceptionHandler")
    private ApiValidationExceptionHandler apiValidationExceptionHandler;

    @Autowired
    @Qualifier("customExceptionHandler")
    private CustomExceptionHandler customExceptionHandler;

    @Autowired
    @Qualifier("generalExceptionHandler")
    private GeneralExceptionHandler generalExceptionHandler;
    @Autowired
    @Qualifier("constraintViolationExceptionHandler")
    private ConstraintViolationExceptionHandler constraintViolationExceptionHandler;

    public ExceptionHandlerStrategy getExceptionHandler(Throwable throwable) {
        try{
            switch (throwable) {

                case ApiValidationException apiValidationException -> {
                    return apiValidationExceptionHandler;
                }
                case CustomException customException -> {
                    return customExceptionHandler;
                }
                case ConstraintViolationException constraintViolationException -> {
                    return constraintViolationExceptionHandler;
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
