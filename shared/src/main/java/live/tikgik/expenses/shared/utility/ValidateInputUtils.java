package live.tikgik.expenses.shared.utility;


import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.error.exception.ErrorCode;
import live.tikgik.expenses.shared.error.exception.GeneralFailureException;

import java.util.Map;
import java.util.Set;

public class ValidateInputUtils {
    public static boolean isValidInput(Object category, Object association){
        return category != null && association != null;
    }
    public static void isValidInput(Object ...objects){
        if (objects == null) {
//            throw new GeneralFailureException(ErrorCode.MISSING_PASSWORD.getErrorCode(),
//                    Map.of("error", "the parameter must not be null"));
            return;
        }
        for (Object obj: objects){
            if (obj == null) {
                throw new GeneralFailureException(ErrorCode.MISSING_PASSWORD.getErrorCode(),
                        Map.of("error", "the parameters must not be null"));
            }
        }
    }
    public static <T> ApiResponse validateWildCardSet(Set<?> associationsUpdateDto, Class<T> clazz) {
        try {
            if (associationsUpdateDto.isEmpty()) {
                return ApiResponse.getErrorResponse(810, "No associations provided");
            }

            for (Object obj : associationsUpdateDto) {
                if (!clazz.isInstance(obj)) {
                    return ApiResponse.getErrorResponse(810, "Associations must be of type " + clazz.getSimpleName());
                }
            }
            return null;
        } catch (Exception exception) {
            return ApiResponse.getErrorResponse(815, "error casting the iterator of wildcard to type " + clazz.getSimpleName());
        }
    }
    public static <T> ApiResponse validateEntity(Object obj, Class<T> clazz) {
        try {
            if (obj == null) {
                return ApiResponse.getErrorResponse(810, "No obj provided");
            }
            if (!clazz.isInstance(obj)) {
                return ApiResponse.getErrorResponse(810, "obj must be of type " + clazz.getSimpleName());
            }
            return null;
        } catch (Exception exception) {
            return ApiResponse.getErrorResponse(815, "error casting the obj to type " + clazz.getSimpleName());
        }
    }

}