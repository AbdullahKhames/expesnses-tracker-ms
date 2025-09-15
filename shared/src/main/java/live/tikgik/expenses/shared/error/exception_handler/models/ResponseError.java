package live.tikgik.expenses.shared.error.exception_handler.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseError {
    private ErrorCategory errorCategory;
    private String errorMessage;
    private String errorCode;
    private List<String> errorMsgList;
}