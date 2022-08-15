package cn.zay.zayboot.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * @author ZAY
 **/
public class DateUtil {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.CHINA)
                    .withZone(ZoneId.systemDefault());

    public static String now() {
        return FORMATTER.format(Instant.now());
    }
}
