package live.tikgik.expenses.shared.error.exception_handler;


import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.error.exception_handler.services.CachableApiError;
import live.tikgik.expenses.shared.error.exception_handler.services.ExceptionHandlerFactory;
import live.tikgik.expenses.shared.error.exception_handler.services.ExceptionHandlerStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Qualifier
@Component
@RequiredArgsConstructor
public class ResponseExceptionBuilder {
    private final CachableApiError cachableApiError;

    private final ExceptionHandlerFactory exceptionHandlerFactory;

   public ApiResponse buildResponse(Throwable ex){
       ExceptionHandlerStrategy exceptionHandlerStrategy = exceptionHandlerFactory.getExceptionHandler(ex);
        return exceptionHandlerStrategy.handleException(ex);
    }

}
