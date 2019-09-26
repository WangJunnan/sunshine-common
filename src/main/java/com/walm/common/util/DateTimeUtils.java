package com.walm.common.util;

import java.sql.Timestamp;
import java.time.*;
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


    //********************************** To Time String ************************************//

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

    //********************************** To LocalDateTime ************************************//

    /**
     * date to LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return dateToLocalDateTime(date, DEFAULT_ZONE_ID);
    }

    /**
     * date to LocalDateTime by zoneId
     *
     * @param date
     * @param zoneId
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date, ZoneId zoneId) {
        if (Objects.isNull(date)) {
            return null;
        }
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
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
     * timestamp to localDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime getDateTimeByTimestamp(Timestamp timestamp) {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }

    //********************************** To Date ************************************//

    /**
     * dateTimeStr to Date by pattern
     *
     * @param dateTimeStr
     * @param pattern
     * @return
     */
    public static Date getDateByStr(String dateTimeStr, String pattern) {
        return getDateByStr(dateTimeStr, pattern, DEFAULT_ZONE_ID);
    }

    /**
     * dateTimeStr to Date by pattern and zoneId
     *
     * @param dateTimeStr
     * @param pattern
     * @param zoneId
     * @return
     */
    public static Date getDateByStr(String dateTimeStr, String pattern, ZoneId zoneId) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return null;
        }
        LocalDateTime dateTime = getDateTimeByStr(dateTimeStr, pattern);

        ZonedDateTime zdt = dateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * timestamp to date
     *
     * @param timestamp
     * @return
     */
    public static Date getDateByTimestamp(Timestamp timestamp) {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        return Date.from(timestamp.toInstant());
    }

    //********************************** To Timestamp ************************************//

    /**
     * get now timestamp
     *
     * @return
     */
    public static Timestamp nowTimestamp() {
        return Timestamp.from(Instant.now());
    }
}
