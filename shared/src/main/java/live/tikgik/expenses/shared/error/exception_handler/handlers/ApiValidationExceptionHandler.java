package live.tikgik.expenses.shared.error.exception_handler.handlers;



import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.error.exception.ApiValidationException;
import live.tikgik.expenses.shared.error.exception_handler.models.ErrorCategory;
import live.tikgik.expenses.shared.error.exception_handler.models.ResponseError;
import live.tikgik.expenses.shared.error.exception_handler.services.ExceptionHandlerStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
@Component("apiValidationExceptionHandler")
public class ApiValidationExceptionHandler implements ExceptionHandlerStrategy {
    @Override
    public ApiResponse handleException(Throwable throwable) {
        ApiValidationException e = (ApiValidationException) throwable;
        e.printStackTrace();
        List<String> statusMsgList = e.getStatusMsgList();

        ApiResponse responseDto = ApiResponse.failed(810);
        try {
            ResponseError responseError = new ResponseError();

            responseError.setErrorCode(ApiValidationException.validation_Error_Code);
            responseError.setErrorMsgList(statusMsgList);
            responseError.setErrorCategory(ErrorCategory.BusinessError);
            responseDto.setData(responseError);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseDto;
    }
}