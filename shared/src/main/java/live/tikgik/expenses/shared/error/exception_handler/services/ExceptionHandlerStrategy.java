package live.tikgik.expenses.shared.error.exception_handler.services;


import live.tikgik.expenses.shared.dto.ApiResponse;

public interface ExceptionHandlerStrategy {
    public ApiResponse handleException(Throwable throwable);

}
