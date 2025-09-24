package live.tikgik.expenses.shared.error.exception_handler.handlers;


import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.error.exception.CustomException;
import live.tikgik.expenses.shared.error.exception_handler.models.ErrorCategory;
import live.tikgik.expenses.shared.error.exception_handler.models.ResponseError;
import live.tikgik.expenses.shared.error.exception_handler.services.ExceptionHandlerStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("customExceptionHandler")
public class CustomExceptionHandler implements ExceptionHandlerStrategy {

    @Override
    public ApiResponse handleException(Throwable throwable) {
        ApiResponse responseDto = ApiResponse.failed(810, null);
        try {
            CustomException e = (CustomException) throwable;
            e.printStackTrace();
            String errorCode = e.getErrorCode();
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode(errorCode);
            responseError.setErrorMessage(e.getMessage());
            responseError.setErrorCategory(ErrorCategory.BusinessError);
            responseDto.setData(responseError);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseDto;
    }
}