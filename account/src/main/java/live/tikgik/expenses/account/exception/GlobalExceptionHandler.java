package live.tikgik.expenses.account.exception;

import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.error.exception_handler.services.ExceptionHandlerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionHandlerFactory exceptionHandlerFactory;
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<ApiResponse> handleGlobalException(Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionHandlerFactory.getExceptionHandler(e).handleException(e));
    }
}
