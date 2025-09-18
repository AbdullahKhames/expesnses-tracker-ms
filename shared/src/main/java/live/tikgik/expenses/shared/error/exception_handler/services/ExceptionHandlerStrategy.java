package live.tikgik.expenses.shared.error.exception_handler.services;


import live.tikgik.expenses.shared.dto.ApiResponse;

public interface ExceptionHandlerStrategy {
    ApiResponse handleException(Throwable throwable);

}
