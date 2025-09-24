package live.tikgik.expenses.shared.utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldValidator {
    public static <T> boolean hasField(String field, Class<T> classType) {
        try {
            classType.getDeclaredField(field);
            return true;
        } catch (NoSuchFieldException e) {
            log.error("{}", e.getMessage());
            return false;
        }
    }
}
