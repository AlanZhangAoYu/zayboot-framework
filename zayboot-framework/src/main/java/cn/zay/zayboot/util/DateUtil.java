package cn.zay.zayboot.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
/**
 * 线程安全的, 比 java.util.Date更安全、好用的时间工具类 java.time.*
 * @author ZAY
 */
public class DateUtil {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)
            .withLocale(Locale.CHINA).withZone(ZoneId.systemDefault());
    /**
     * 返回系统当前时间
     * @return 系统当前时间
     */
    public static String now() {
        return FORMATTER.format(Instant.now());
    }
}
