package live.tikgik.expenses.shared.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ApiSearchResponse extends ApiResponse {

    Pagination pagination;

    public static ApiResponse success(Pagination metadata, Object data) {
        return ApiSearchResponse.builder().pagination(metadata).isSuccess(true).data(data).build();
    }

}
