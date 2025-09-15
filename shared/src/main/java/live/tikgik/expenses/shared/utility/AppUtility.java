package live.tikgik.expenses.shared.utility;

import live.tikgik.expenses.shared.constant.AppConstants;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

@UtilityClass
public class AppUtility {
    static ZoneId utc = ZoneId.of(AppConstants.UTC_ZONE_ID.getKey());

    public static Instant setUTCDateFrom(String date) {
        return Objects.nonNull(date) ? LocalDate.parse(date).atStartOfDay(utc).toInstant() : null;
    }

    public static Instant setUTCDateTo(String date) {
        return Objects.nonNull(date) ? LocalDate.parse(date).plusDays(1).atStartOfDay(utc).toInstant() : null;
    }

    public static Instant setUTCDateFrom(Instant date) {
        return date.atZone(utc).toLocalDate().atStartOfDay(utc).toInstant();
    }

    public static Instant setUTCDateTo(Instant date) {
        return date.atZone(utc).toLocalDate().atStartOfDay(utc).plusDays(1).toInstant();
    }


}