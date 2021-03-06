package com.k2data.platform.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * {@link Date}相关的工具类
 */
public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
            "yyyy-MM-dd HH:mm:ss.SSS"};
    
    /**
     * 日期和时间的格式化方法
     * 
     * @param dateTime 要被格式化的{@link Date}
     * @return 格式化为{@link #DEFAULT_DATETIME_FORMAT}的{@link String}
     */
    public static String formatDateTime(final Date dateTime) {
        if (dateTime == null)
            return "";
        
        return new SimpleDateFormat(DEFAULT_DATETIME_FORMAT).format(dateTime);
    }
    
    /**
     * 日期的格式化方法
     * 
     * @param date 要被格式化的{@link Date}
     * @return 格式化为{@link #DEFAULT_DATE_FORMAT}的{@link String}
     */
    public static String formatDate(final Date date) {
        if (date == null)
            return "";
        
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
    }
    
    /**
     * 时间的格式化方法
     * 
     * @param time 要被格式化的{@link Date}
     * @return 格式化为{@link #DEFAULT_DATE_FORMAT}的{@link String}
     */
    public static String formatTime(final Date time) {
        if (time == null)
            return "";
        
        return new SimpleDateFormat(DEFAULT_TIME_FORMAT).format(time);
    }
    
    /**
     * 根据指定的格式，格式化日期和时间的方法
     * 
     * @return 格式化为{@link #DEFAULT_DATETIME_FORMAT}的{@link String}
     */
    public static String format(final Date time, final String pattern) {
        return new SimpleDateFormat(pattern).format(time);
    }
    
    /**
     * 指定的日期上增加年份
     * 
     * @param date 指定的日期
     * @param amount 要增加的年份
     * @return 修改后的日期
     */
    public static Date addYear(final Date date, final int amount) {
        return add(date, amount, Calendar.YEAR);
    }
    
    /**
     * 指定的日期上增加月份
     * 
     * @param date 指定的日期
     * @param amount 要增加的月份
     * @return 修改后的日期
     */
    public static Date addMonth(final Date date, final int amount) {
        return add(date, amount, Calendar.MONTH);
    }
    
    /**
     * 指定的日期上增加天数
     * 
     * @param date 指定的日期
     * @param amount 要增加的天数
     * @return 修改后的日期
     */
    public static Date addDay(final Date date, final int amount) {
        return add(date, amount, Calendar.DAY_OF_YEAR);
    }
    
    /**
     * 把指定的日期转换成从January 1, 1970, 00:00:00 GMT开始的天数
     * 
     * @param date 指定的日期
     * @return 天数
     */
    public static Long getDays(final Date date) {
        if (date == null) {
            return 0L;
        }
        
        return date.getTime() / (1000 * 60 * 60 * 24);
    }
    
    /**
     * 指定的日期上，在指定的项加上值
     * 
     * @param date 指定的日期
     * @param amount 要增加的值
     * @param field 日期的项，从Calendar处取值
     * @return 修改后的日期
     */
    private static Date add(final Date date, final int amount, final int field) {
        if (date == null)
            return null;
        
        Calendar instance = Calendar.getInstance();
        
        instance.setTime(date);
        
        instance.add(field, amount);
        
        return instance.getTime();
    }

    public static Date parseDate(Object str) {
        if (str == null){
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }
    
}
