package live.tikgik.expenses.shared.error.exception_handler;


import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.error.exception_handler.services.CachableApiError;
import live.tikgik.expenses.shared.error.exception_handler.services.ExceptionHandlerFactory;
import live.tikgik.expenses.shared.error.exception_handler.services.ExceptionHandlerStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier
public class ResponseExceptionBuilder {
    @Autowired
    private CachableApiError cachableApiError;

    @Autowired
    private ExceptionHandlerFactory exceptionHandlerFactory;

   public ApiResponse buildResponse(Throwable ex){
       ExceptionHandlerStrategy exceptionHandlerStrategy = exceptionHandlerFactory.getExceptionHandler(ex);
        return exceptionHandlerStrategy.handleException(ex);
    }

}
