package live.tikgik.expenses.shared.error.exception;

import java.util.List;

public class ApiValidationException extends RuntimeException {
    public static final String validation_Error_Code = "VAL_000";
    private static final long serialVersionUID = -1496369364310771936L;
    private List<String> statusMsgList;


    public ApiValidationException(List<String> statusMsgList) {
        super();
        this.statusMsgList = statusMsgList;
    }

    public List<String> getStatusMsgList() {
        return statusMsgList;
    }

    public void setStatusMsgList(List<String> statusMsgList) {
        this.statusMsgList = statusMsgList;
    }


}
