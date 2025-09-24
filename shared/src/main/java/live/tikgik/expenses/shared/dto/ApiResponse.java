package live.tikgik.expenses.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import live.tikgik.expenses.shared.enums.Models;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class ApiResponse {

    private Boolean isSuccess;
    private List<String> errorMessages;
    @Builder.Default
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time = LocalDateTime.now();
    private int statusCode;
    private Object data;

    public static ApiResponse success(Object data) {
        return ApiResponse.builder().isSuccess(true).data(data).build();
    }

    public static ApiResponse success() {
        return ApiResponse.builder().isSuccess(true).build();
    }

    public static ApiResponse failed(List<String> errorMessages, String localizedMessage) {
        return ApiResponse.builder().isSuccess(false).errorMessages(errorMessages).data(localizedMessage).statusCode(810).build();
    }

    public static ApiResponse failed(List<String> errorMessage) {
        return ApiResponse.builder().isSuccess(false).errorMessages(errorMessage).build();
    }

    public static ApiResponse failed(int statusCode) {
        return ApiResponse.builder().isSuccess(false).statusCode(statusCode).build();
    }

    public static ApiResponse failed(int statusCode, Object error) {
        return ApiResponse.builder().isSuccess(false).statusCode(statusCode).data(error).build();
    }

    public static ApiResponse getCreateResponse(String resourceName, String refNo, Object data) {
        return createResponse(
                String.format("%s resource was created successfully with reference number : %s", resourceName, refNo),
                true,
                801,
                data);
    }

    public static ApiResponse getErrorResponse(int code, Object data) {
        return createResponse(
                "There was an Error Processing your request\nplease Try again later!",
                false,
                code,
                data);
    }

    public static ApiResponse getUpdateResponse(Models models, String refNo, Object data) {
        return getUpdateResponse(models.name(), refNo, data);
    }
    public static ApiResponse getUpdateResponse(String resourceName, String refNo, Object data) {
        return createResponse(
                String.format("%s resource with reference number : %s was updated successfully ", resourceName, refNo),
                true,
                802,
                data);
    }

    public static ApiResponse getDeleteResponse(Models models, String refNo) {
        return getDeleteResponse(models.name(), refNo);
    }
    public static ApiResponse getDeleteResponse(String resourceName, String refNo) {
        return createResponse(
                String.format("%s resource with reference number : %s was deleted successfully ", resourceName, refNo),
                true,
                805,
                null);
    }
    public static ApiResponse getFetchResponse(Models models, String refNo, Object data) {
        return getFetchResponse(models.name(), refNo, data);
    }
    public static ApiResponse getFetchResponse(String resourceName, String refNo, Object data) {
        return createResponse(
                String.format("%s resource with reference number : %s was fetched successfully ", resourceName, refNo),
                true,
                800,
                data);
    }

    public static <T> ApiResponse getFetchAllResponse(Models models, Page<T> entityPage) {
        return getFetchAllResponse(models.name(), entityPage);
    }

    public static <T> ApiResponse getFetchAllResponse(String resourceName, Page<T> entityPage) {
        return createResponse(
                String.format("Page %s of %s resources was fetched successfully ", entityPage.getNumber(), resourceName),
                true,
                800,
                entityPage);
    }
    public static ApiResponse getFetchAllResponse(Models models, List<?> entities) {
        return getFetchAllResponse(models.name(), entities);
    }
    public static ApiResponse getFetchAllResponse(String resourceName, List<?> entities) {
        return createResponse(
                String.format("list of %s resources was fetched successfully ", resourceName),
                true,
                800,
                entities);
    }

    public static ApiResponse createResponse(String message, boolean status, int code, Object data) {
        return ApiResponse.builder()
                .errorMessages(status ? null : List.of(message))
                .isSuccess(status)
                .statusCode(code)
                .data(data)
                .build();
    }

}
