package live.tikgik.expenses.shared.error.exception;

import java.util.Map;

public class APIException extends CustomException {
    private static final long serialVersionUID = -1872038337563959954L;
    protected Map<String, String> varMap;
    private String errorCode;

    public APIException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public APIException(String errorCode, Map<String, String> varMap) {
        super(errorCode);
        this.errorCode = errorCode;
        this.varMap = varMap;
    }

    public APIException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, String> getVarMap() {
        return this.varMap;
    }

    public void setVarMap(Map<String, String> varMap) {
        this.varMap = varMap;
    }
}
