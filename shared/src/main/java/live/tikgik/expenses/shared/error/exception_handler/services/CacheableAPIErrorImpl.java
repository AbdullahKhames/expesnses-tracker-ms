package live.tikgik.expenses.shared.error.exception_handler.services;


import live.tikgik.expenses.shared.error.exception_handler.models.APIError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CacheableAPIErrorImpl implements CachableApiError {

    private static Map<String, APIError> ExpensesErrorMap;

    static {
        ExpensesErrorMap = new HashMap<String, APIError>();
    }
    @Autowired
    private APIErrorLoader apiErrorLoader;

    public APIError getExpensesAPIError(String errorCode) throws Exception {
        APIError apiError = null;

        if (ExpensesErrorMap.containsKey(errorCode)) {
            apiError = ExpensesErrorMap.get(errorCode);
        } else {
            apiError = apiErrorLoader.loadApiError(errorCode);

            if (apiError != null) {

                ExpensesErrorMap.put(errorCode, apiError);
            }
        }
        return apiError;
    }
}
