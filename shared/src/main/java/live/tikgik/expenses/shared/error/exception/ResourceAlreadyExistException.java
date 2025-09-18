package live.tikgik.expenses.shared.error.exception;

public class ResourceAlreadyExistException extends APIException{
    public ResourceAlreadyExistException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exist with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
