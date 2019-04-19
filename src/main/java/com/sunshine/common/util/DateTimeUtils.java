package com.sunshine.common.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * <p>DateTimeUtils</p>
 *
 * @author wangjn
 * @date 2019/3/1
 */
public final class DateTimeUtils {

    public static final String FORMAT_TEMPLATE_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_TEMPLATE_TWO = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_TEMPLATE_THREE = "yyyy/MM/dd";
    public static final String FORMAT_TEMPLATE_TIME = "HH:mm:ss";
    public static final String FORMAT_TEMPLATE_TIME_SIMPLE = "HH:mm";

    /**
     * 默认时区
     */
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Shanghai");


    /**
     * get now time str
     *
     * @return
     */
    public static String nowDateTimeSimpleStr() {
        LocalDateTime now = LocalDateTime.now();
        return toStrByPattern(now, FORMAT_TEMPLATE_DEFAULT);
    }

    /**
     * LocalDateTime to str by pattern
     *
     * @param dateTime
     * @param pattern
     * @return
     */
    public static String toStrByPattern(LocalDateTime dateTime, String pattern) {
        if (Objects.isNull(dateTime)) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * timeStamp to str by pattern
     *
     * @param dateTime
     * @param pattern
     * @return
     */
    public static String toStrByPattern(Timestamp dateTime, String pattern) {
        return toStrByPattern(dateTime.toLocalDateTime(), pattern);
    }

    /**
     *  dateTimeStr to LocalDateTime by pattern
     *
     * @param dateTimeStr
     * @param pattern
     * @return
     */
    public static LocalDateTime getDateTimeByStr(String dateTimeStr, String pattern) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
        return dateTime;
    }

    /**
     * dateTimeStr to Date by pattern
     *
     * @param dateTimeStr
     * @param pattern
     * @return
     */
    public static Date getDateByStr(String dateTimeStr, String pattern) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return null;
        }
        LocalDateTime dateTime = getDateTimeByStr(dateTimeStr, pattern);

        ZonedDateTime zdt = dateTime.atZone(DEFAULT_ZONE_ID);
        return Date.from(zdt.toInstant());
    }

    /**
     * date to LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(DEFAULT_ZONE_ID).toLocalDateTime();
        return localDateTime;
    }

    /**
     * date to str by pattern
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToStr(Date date, String pattern) {
        if (Objects.isNull(date)) {
            return null;
        }
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return toStrByPattern(localDateTime, pattern);
    }
}
