package live.tikgik.expenses.shared.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppConstants {

    UTC_ZONE_ID("UTC"),
    X_USER_ID("X-User-Id"),
    X_USER_NAME("X-Username"),
    X_EMAIL("X-Email"),
    X_ROLES("X-Roles"),
    REF_NO("refNo"),

    SYMBOL_WHITESPACE(" "),

    SYMBOL_COMMA(","),

    SYMBOL_EMAIL("@"),

    SYMBOL_UNDERSCORE("_"),

    SYMBOL_SLASH("/"),

    INVALID_CODE("Invalid code: %s");
    private final String key;
}
