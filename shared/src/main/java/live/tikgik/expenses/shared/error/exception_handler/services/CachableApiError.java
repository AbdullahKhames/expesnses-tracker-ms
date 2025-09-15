package live.tikgik.expenses.shared.error.exception_handler.services;


import live.tikgik.expenses.shared.error.exception_handler.models.APIError;

public interface CachableApiError {
    public APIError getExpensesAPIError(String errorCode) throws Exception;
}
