package cn.onecolour.utils;




import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yang
 * @date 2022/6/3
 * @description
 */
@SuppressWarnings("unused")
public class TimeUtils {
    public final static DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public final static ZoneId LOCAL_ZONE = ZoneId.of("Asia/Shanghai");
    public final static FormatType DEFAULT_FORMAT_TYPE = FormatType.yyyy_MM_dd_HH_mm_ss_default;

    /**
     * 秒/毫秒时间戳转jdk8时间
     * @param timestamp 时间戳
     * @param unit 时间单位
     * @return jdk8时间
     */
    public static LocalDateTime timestampToLocalDateTime(long timestamp, @NotNull TimeUnit unit) {
        switch (unit) {
            case SECONDS:
                timestamp = timestamp * 1000L;
            case MILLISECONDS:
                return timestampToLocalDateTime(timestamp);
            default:
                throw new IllegalArgumentException("Timestamp unit is illegal. Accept seconds and milliseconds.");
        }
    }

    /**
     * 毫秒时间戳转jdk8时间
     * @param millisecondTimestamp 毫秒时间戳
     * @return jdk8时间
     */
    public static LocalDateTime timestampToLocalDateTime(long millisecondTimestamp) {
        Instant instant = Instant.ofEpochMilli(millisecondTimestamp);
        return LocalDateTime.ofInstant(instant, TimeUtils.LOCAL_ZONE);
    }

    public static String timeFormat(@NotNull LocalDateTime time, @NotNull FormatType type, @Nullable String ... separations) {
        return FormatType.timeFormat(time, type, separations);
    }

    public static String timeFormat(@NotNull LocalDateTime time) {
        return timeFormat(time, DEFAULT_FORMAT_TYPE);
    }


    public static String timeFormat(long millisecondTimestamp, @NotNull FormatType type, @Nullable String ... separations) {
        return timeFormat(millisecondTimestamp, TimeUnit.MILLISECONDS, type, separations);
    }

    public static String timeFormat(long millisecondTimestamp) {
        return timeFormat(millisecondTimestamp, TimeUnit.MILLISECONDS, DEFAULT_FORMAT_TYPE);
    }

    public static String timeFormat(long timestamp, @NotNull TimeUnit unit) {
        return timeFormat(timestamp, unit, DEFAULT_FORMAT_TYPE);
    }

    public static String timeFormat(long timestamp, @NotNull TimeUnit unit,@NotNull FormatType type, @Nullable String ... separations) {
        return timeFormat(timestampToLocalDateTime(timestamp, unit), type, separations);
    }



    public enum FormatType {
        yyyy_MM_dd_HH_mm_ss("yyyy{0}MM{0}dd HH{1}mm{1}ss"),
        yyyy_MM_dd_HH_mm_ss_default("yyyy-MM-dd HH:mm:ss"),
        yyyy_MM_dd_HH_mm_ss_no_separation("yyyyMMddHHmmss"),
        yy_MM_dd_HH_mm_ss("yy{0}MM{0}dd HH{1}mm{1}ss"),
        yy_MM_dd_HH_mm_ss_default("yy-MM-dd HH:mm:ss"),
        yy_MM_dd_HH_mm_ss_no_separation("yyMMddHHmmss"),
        ;

        private final String format;

        private final static Map<String, DateTimeFormatter> FORMATTER_MAP = new HashMap<>();

        public String getFormat() {
            return format;
        }

        FormatType(String format) {
            this.format = format;
        }

        private static String timeFormat(@NotNull LocalDateTime time,
                                  @NotNull TimeUtils.FormatType type,
                                  @Nullable String ... separations) {
            String formatString = type.getFormat();
            if (separations != null && separations.length > 0) {
                formatString = MessageFormat.format(formatString, (Object[]) separations);
            }
            DateTimeFormatter formatter;
            if (FORMATTER_MAP.containsKey(formatString)) {
                formatter = FORMATTER_MAP.get(formatString);
            } else {
                synchronized (FormatType.class) {
                    if (!FORMATTER_MAP.containsKey(formatString)) {
                        formatter = DateTimeFormatter.ofPattern(formatString, Locale.CHINA);
                        FORMATTER_MAP.put(formatString, formatter);
                    } else {
                        formatter = FORMATTER_MAP.get(formatString);
                    }
                }
            }
            return time.format(formatter);
        }
    }
}
