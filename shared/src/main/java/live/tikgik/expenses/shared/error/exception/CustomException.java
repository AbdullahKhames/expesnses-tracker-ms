package live.tikgik.expenses.shared.error.exception;

import java.util.Map;

public abstract class CustomException extends RuntimeException {

    private static final long serialVersionUID = -1496369364310771936L;

    protected Map<String, String> varMap;

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String errorCode, Map<String, String> varMap) {
        super(errorCode);
        this.varMap = varMap;
    }

    public abstract String getErrorCode();
}
