package com.k2data.platform.utils;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * {@link Object}转换相关的工具类
 */
public final class ConvertUtils {

    /**
     * 默认的Encoding用于{@code char[]}和{@code byte[]}的互转
     */
    public static final String DefaultCharEncoding = "UTF-8";
    
    /**
     * 转化{@link Object}为{@link T}类型的数据，如果转换成{@code null}，就用默认值替代
     * 
     * @param value {@link Object}实例
     * @param clazz {@link T}类型
     * @param defaultValue 默认的值
     * @return 转换成的{@link T}类型实例
     */
    public static <T> T convert(final Object value, final Class<T> clazz, final T defaultValue) {
        final T convertedValue = convert(value, clazz);
        
        if (convertedValue == null) {
            return defaultValue;
        }
        
        return convertedValue;
    }
    
    /**
     * 转化{@link Object}为指定类型的值，对于基础类型如果为空，就用默认的代替
     * 
     * @param value {@link Object}实例
     * @param className 类型名称
     * @return 转换成{@code className}对应的类型实例
     */
    public static Object convert(final Object value, final String className) {
        return convert(value, ClassUtils.getClass(className));
    }
    
    /**
     * 转化{@link Object}为{@link T}类型的数据
     * 
     * @param value {@link Object}实例
     * @param clazz {@link T}类型
     * @return 转换成的{@link T}类型实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(final Object value, final Class<T> clazz) {
        if (value == null || clazz == null) {
            return null;
        }
        
        final Class<T> cls = ClassUtils.primitiveToWrapper(ClassUtils.getOriginalClass(clazz));
        
        if (cls.isAssignableFrom(value.getClass())) {
            return (T)value;
        }
        
        if (String.class.equals(cls)) {
            return (T) convertToString(value);
        }
        
        if (BigDecimal.class.equals(cls)) {
            return (T) convertToBigDecimal(value);
        }
        
        if (BigInteger.class.equals(cls)) {
            return (T) convertToBigInteger(value);
        }
        
        if (Long.class.equals(cls)) {
            return (T) convertToLong(value);
        }
        
        if (Double.class.equals(cls)) {
            return (T) convertToDouble(value);
        }
        
        if (Float.class.equals(cls)) {
            return (T) convertToFloat(value);
        }
        
        if (Integer.class.equals(cls)) {
            return (T) convertToInteger(value);
        }
        
        if (Short.class.equals(cls)) {
            return (T) convertToShort(value);
        }
        
        if (Character.class.equals(cls)) {
            return (T) convertToCharacter(value);
        }
        
        if (Byte.class.equals(cls)) {
            return (T) convertToByte(value);
        }
        
        if (Date.class.equals(cls)) {
            return (T) convertToDate(value);
        }
        
        if (Calendar.class.equals(cls)) {
            return (T) convertToCalendar(value);
        }
        
        if (Time.class.equals(cls)) {
            return (T) convertToSqlTime(value);
        }
        
        if (Timestamp.class.equals(cls)) {
            return (T) convertToSqlTimestamp(value);
        }
        
        if (java.sql.Date.class.equals(cls)) {
            return (T) convertToSqlDate(value);
        }
        
        if (Boolean.class.equals(cls)) {
            return (T) convertToBoolean(value);
        }
        
        if (Blob.class.equals(cls)) {
            return (T) convertToBlob(value);
        }
        
        if (Clob.class.equals(cls)) {
            return (T) convertToClob(value);
        }
        
        if (byte[].class.equals(cls)) {
            return (T) convertToBytes(value);
        }
        
        if (char[].class.equals(cls)) {
            return (T) convertToChars(value);
        }
        
        if (Byte[].class.equals(cls)) {
            return (T) convertToWrapperByteArray(convertToBytes(value));
        }
        
        if (Character[].class.equals(cls)) {
            return (T) convertToWrapperCharArray(convertToChars(value));
        }
        
        if (long[].class.equals(cls)) {
            return (T) convertToLongArray((Long[])value);
        }
        
        if (int[].class.equals(cls)) {
            return (T) convertToIntArray((Integer[])value);
        }
        
        if (double[].class.equals(cls)) {
            return (T) convertToDoubleArray((Double[])value);
        }
        
        if (float[].class.equals(cls)) {
            return (T) convertToFloatArray((Float[])value);
        }
        
        if (short[].class.equals(cls)) {
            return (T) convertToShortArray((Short[])value);
        }
        
        if (long[].class.equals(value.getClass())) {
            return (T) convertToWrapperLongArray((long[])value);
        }
        
        if (int[].class.equals(value.getClass())) {
            return (T) convertToWrapperIntArray((int[])value);
        }
        
        if (double[].class.equals(value.getClass())) {
            return (T) convertToWrapperDoubleArray((double[])value);
        }
        
        if (float[].class.equals(value.getClass())) {
            return (T) convertToWrapperFloatArray((float[])value);
        }
        
        if (short[].class.equals(value.getClass())) {
            return (T) convertToWrapperShortArray((short[])value);
        }
        
        try {
            return cls.cast(value);
        } catch (ClassCastException ex) {
            return null;
        }
    }
    
    /******************************************************************************************************
     *  转换成String类型
     ******************************************************************************************************/    
    /**
     * 转换{@code T}类型的值为{@link String}，转换方式如下：
     * 1. String -> String
     * 2. StringBuffer -> String
     * 3. Number -> String
     * 4. Blob -> String
     * 5. Clob -> String
     * 6. Date -> String
     * 7. Calendar -> String
     * 8. Character -> String
     * 9. Boolean -> String
     * 10. byte[] -> String
     * 11. char[] -> String
     * 12. Ref -> String
     * 13. InputStream -> String
     * 14. Reader -> String
     * 15. SQLXML -> String
     * 16. 其他 -> null
     * 
     * @param value {@code T}类型的实例
     * @return 转换成String，如果不可以就返回{@code null}
     */
    private static <T> String convertToString(final T value) {
        if (value == null)
            return null;
        
        if (ObjectUtils.isEmpty(value))
            return "";
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (String.class.isAssignableFrom(cls)) {
            return (String)value;
        }
        
        if (StringBuilder.class.isAssignableFrom(cls)) {
            return ((StringBuffer)value).toString();
        }
        
        if (StringBuffer.class.isAssignableFrom(cls)) {
            return ((StringBuffer)value).toString();
        }
        
        if (Number.class.isAssignableFrom(cls)) {
            return NumberUtils.toString(value);
        }
        
        if (Blob.class.isAssignableFrom(cls)) {
            final Blob blob = (Blob)value;
            try {
                return new String(blob.getBytes(1, (int)(blob.length())));
            } catch (SQLException ignored) {
            }
            
            return null;
        }
        
        if (Clob.class.isAssignableFrom(cls)) {
            Clob clob = (Clob)value;
            Reader reader = null;
            try {
                reader = clob.getCharacterStream();
                final char[] chars = new char[(int) (clob.length())];
                reader.read(chars);            
                return String.valueOf(chars);
            } catch (SQLException | IOException ignored) {
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch(Exception ignored) {}
                    reader = null;
                }
            }
            
            return null;
        }
        
        if (Ref.class.isAssignableFrom(cls)) {
            try {
                return convertToString(((Ref)value).getObject());
            } catch (SQLException sqle) {
                return null;
            }
        }
        
        if (SQLXML.class.isAssignableFrom(cls)) {
            try {
                return ((SQLXML)value).getString();
            } catch (SQLException sqle) {
                return null;
            }
        }
        
        if (Date.class.isAssignableFrom(cls)) {
            if (Timestamp.class.isAssignableFrom(cls)) {
                return DateUtils.formatDateTime((Timestamp)value);
            }
            
            if (Time.class.isAssignableFrom(cls)) {
                return DateUtils.formatTime((Time)value);
            }
            
            return DateUtils.formatDateTime((Date)value);
        }
        
        if (Calendar.class.isAssignableFrom(cls)) {
            return DateUtils.formatDateTime(((Calendar)value).getTime());
        }
        
        if (Character.class.isAssignableFrom(cls)) {
            return Character.toString((Character)value);
        }
        
        if (Boolean.class.isAssignableFrom(cls)) {
            return Boolean.toString((Boolean)value);
        }
        
        if (InputStream.class.isAssignableFrom(cls)) {
            return convertToString(new InputStreamReader((InputStream)value));
        }
        
        if (Reader.class.isAssignableFrom(cls)) {
            BufferedReader reader = new BufferedReader((Reader)value);
            
            StringBuilder sb = new StringBuilder();
            
            String line = "";
            
            try {
                while((line=reader.readLine())!= null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException e) {
                /* ignore */
            } finally {
                try {
                    reader.close();
                } catch (IOException ignored) {}
                reader = null;
            }
             
             return sb.toString();
        }
        
        if (cls.isArray()) {
            if (Array.getLength(value) == 0)
                return "";
            
            if (byte[].class.isAssignableFrom(cls)) {
                return new String(((byte[])value));
            }
            
            if (Byte[].class.isAssignableFrom(cls)) {
                return new String(convertToByteArray((Byte[])value));
            }
            
            if (char[].class.isAssignableFrom(cls)) {
                return new String((char[])value);
            }
            
            if (Character[].class.isAssignableFrom(cls)) {
                return new String(convertToCharArray((Character[])value));
            }
            
            final StringBuilder sb = new StringBuilder();
            
            for (Object o : (Object[])value) {
                if (sb.length() > 0)
                    sb.append(",");
                sb.append(ConvertUtils.convertToString(o));
            }
            
            return sb.toString();
        }
        
        if (Collection.class.isAssignableFrom(cls)) {
            return cls.getSimpleName() + Collections.list(Collections.enumeration((Collection<?>)value)).toString();
        }
        
        if (Map.class.isAssignableFrom(cls)) {
            return cls.getSimpleName() + Arrays.toString(((Map<?,?>)value).entrySet().toArray());
        }
        
        return value.toString();
    }
    
    /******************************************************************************************************
     *  转换成Number类型
     *  
     *  1.Number  -> Number (BigDecimal, BigInteger, Long, Double, Float, Integer, Short, Byte)
     *  2.String  -> Number
     *  3.Boolean -> Number (true : 1 false : 0)
     *  4.其他           -> null
     ******************************************************************************************************/
    /**
     * 转换{@code T}类型的值为{@link BigDecimal}
     * 
     * @param value 要被转换的值
     * @return {@link BigDecimal}实例或{@code null}
     */
    private static <T> BigDecimal convertToBigDecimal(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Number.class.isAssignableFrom(cls)
                || String.class.isAssignableFrom(cls)) {
            try {
                return new BigDecimal("" + value);
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        
        if (Boolean.class.isAssignableFrom(cls)) {
            if ((Boolean)value)
                return new BigDecimal("1");
            else
                return new BigDecimal("0");
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link BigInteger}
     * 
     * @param value 要被转换的值
     * @return {@link BigInteger}实例或{@code null}
     */
    private static <T> BigInteger convertToBigInteger(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Number.class.isAssignableFrom(cls)
                || String.class.isAssignableFrom(cls)) {
            try {
                return new BigDecimal("" + value).toBigInteger();
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        
        if (Boolean.class.isAssignableFrom(cls)) {
            if ((Boolean)value)
                return new BigInteger("1");
            else
                return new BigInteger("0");
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link Long}
     * 
     * @param value 要被转换的值
     * @return {@link Long}实例或{@code null}
     */
    private static <T> Long convertToLong(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Number.class.isAssignableFrom(cls)
                || String.class.isAssignableFrom(cls)) {
            try {
                return new BigDecimal("" + value).longValue();
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        
        if (Boolean.class.isAssignableFrom(cls)) {            
            if ((Boolean)value)
                return 1L;
            else
                return 0L;
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link Double}
     * 
     * @param value 要被转换的值
     * @return {@link Double}实例或{@code null}
     */
    private static <T> Double convertToDouble(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Number.class.isAssignableFrom(cls)
                || String.class.isAssignableFrom(cls)) {
            try {
                return new BigDecimal("" + value).doubleValue();
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        
        if (Boolean.class.isAssignableFrom(cls)) {
            if ((Boolean)value)
                return 1D;
            else
                return 0D;
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link Float}
     * 
     * @param value 要被转换的值
     * @return {@link Float}实例或{@code null}
     */
    private static <T> Float convertToFloat(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Number.class.isAssignableFrom(cls)
                || String.class.isAssignableFrom(cls)) {
            try {
                return new BigDecimal("" + value).floatValue();
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        
        
        if (Boolean.class.isAssignableFrom(cls)) {            
            if ((Boolean)value)
                return 1F;
            else
                return 0F;
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link Integer}
     * 
     * @param value 要被转换的值
     * @return {@link Integer}实例或{@code null}
     */
    private static <T> Integer convertToInteger(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Number.class.isAssignableFrom(cls)
                || String.class.isAssignableFrom(cls)) {
            try {
                return new BigDecimal("" + value).intValue();
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        
        if (Boolean.class.isAssignableFrom(cls)) {            
            if ((Boolean)value)
                return 1;
            else
                return 0;
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link Short}
     * 
     * @param value 要被转换的值
     * @return {@link Short}实例或{@code null}
     */
    private static <T> Short convertToShort(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Number.class.isAssignableFrom(cls)
                || String.class.isAssignableFrom(cls)) {
            try {
                return new BigDecimal("" + value).shortValue();
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        
        
        if (Boolean.class.isAssignableFrom(cls)) {            
            if ((Boolean)value)
                return 1;
            else
                return 0;
        }
        
        return null;
    }
    
    /******************************************************************************************************
     *  转换成Character类型
     *  
     *  1. String -> Character 只有长度为0或1，超过1就转换成Integer, Integer只有从0到9的才转换
     *  2. 其它        -> 先转换成String后判断
     ******************************************************************************************************/
    /**
     * 转换{@code T}类型的值为{@link Character}
     * 
     * @param value 要被转换的值
     * @return {@link Byte}实例或{@code null}
     */
    private static <T> Character convertToCharacter(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        String strValue = convertToString(value);
        
        if (strValue != null) {
            int length = strValue.length();
            
            if (length == 0) {
                return ' ';
            } else if (length == 1) {
                return strValue.charAt(0);
            } else { // 如果长度超过1，那么转换成int
                Integer intValue = convertToInteger(strValue);
                
                if (intValue != null && intValue >= 0 && intValue <10) {
                    return Character.forDigit(intValue, 10);
                }
            }
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link Byte}
     * 
     * @param value 要被转换的值
     * @return {@link Byte}实例或{@code null}
     */
    private static <T> Byte convertToByte(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Number.class.isAssignableFrom(cls)) {
            try {
                return new Byte("" + value);
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        
        if (String.class.isAssignableFrom(cls)) {            
            try {
                return new Byte(((String)value).trim());
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        
        if (Boolean.class.isAssignableFrom(cls)) {            
            if ((Boolean)value)
                return 1;
            else
                return 0;
        }
        
        return null;
    }
    
    /******************************************************************************************************
     *  转换成Date类型
     *  
     *  1. Date   -> Date (Date, Calendar, Sql Date, Sql Time, Sql Timestamp)
     *  2. String -> Date
     *  3. Long   -> Date
     *  4. 其它        -> null
     ******************************************************************************************************/
    /**
     * 转换{@code T}类型的值为{@link Date}
     * 
     * @param value 要被转换的值
     * @return {@link Date}实例或{@code null}
     */
    @SuppressWarnings("unchecked")
    private static <T> Date convertToDate(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Date.class.isAssignableFrom(cls)) {
            return new Date(((Date)value).getTime());
        }
        
        if (String.class.isAssignableFrom(cls)) {
            final String strValue = ((String)value).trim();
            
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DEFAULT_DATETIME_FORMAT);
                return sdf.parse(strValue);
            } catch (ParseException pe) {
                /* ignore */
            }
            
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DEFAULT_DATE_FORMAT);
                return sdf.parse(strValue);
            } catch (ParseException pe) {
                /* ignore */
            }
            
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DEFAULT_TIME_FORMAT);
                return sdf.parse(strValue);
            } catch (ParseException pe) {
                /* ignore */
            }
        }
        
        if (Long.class.isAssignableFrom(cls)) {
            return new Date((Long)value);
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link Calendar}
     * 
     * @param value 要被转换的值
     * @return {@link Calendar}实例或{@code null}
     */
    private static <T> Calendar convertToCalendar(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Calendar.class.isAssignableFrom(cls)) {
            return (Calendar)value;
        }
        
        if (String.class.isAssignableFrom(cls)) {            
            final Date date = convertToDate(((String)value).trim());
            
            if (date != null) {
                final Calendar calendar = Calendar.getInstance();
                
                calendar.setTime(date);
                
                return calendar;
            }
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link java.sql.Date}
     * 
     * @param value 要被转换的值
     * @return {@link java.sql.Date}实例或{@code null}
     */
    private static <T> java.sql.Date convertToSqlDate(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Date.class.isAssignableFrom(cls)) {
            return new java.sql.Date(((Date)value).getTime());
        }
        
        if (String.class.isAssignableFrom(cls)) {
            final Date date = convertToDate(((String)value).trim());
            
            if (date != null) {
                return new java.sql.Date(date.getTime());
            }
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link Time}
     * 
     * @param value 要被转换的值
     * @return {@link Time}实例或{@code null}
     */
    private static <T> Time convertToSqlTime(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Date.class.isAssignableFrom(cls)) {
            return new Time(((Date)value).getTime());
        }
        
        if (String.class.isAssignableFrom(cls)) {
            try {
                return Time.valueOf(((String)value).trim());
            } catch (IllegalArgumentException ex) {
                /* ignored */
            }
        }
        
        return null;
    }
    
    /**
     * 转换{@code T}类型的值为{@link Timestamp}
     * 
     * @param value 要被转换的值
     * @return {@link Timestamp}实例或{@code null}
     */
    private static <T> Timestamp convertToSqlTimestamp(final T value) {
        if (ObjectUtils.isEmpty(value))
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Date.class.isAssignableFrom(cls)) {
            return new Timestamp(((Date)value).getTime());
        }
        
        if (String.class.isAssignableFrom(cls)) {                
            final String v = ((String)value).trim();
            
            try {
                return Timestamp.valueOf(v);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            
            try {
                final Date date = convertToDate(v);
                
                if (date != null)
                    return new Timestamp(date.getTime());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    /******************************************************************************************************
     *  转换成byte[]类型
     *  
     *  1. byte[]      -> byte[]
     *  2. Byte[]      -> byte[]
     *  3. String      -> byte[]
     *  4. char[]      -> byte[]
     *  5. Character[] -> byte[]
     *  6. Blob        -> byte[]
     *  7. Clob        -> byte[]
     *  8. 其它                      -> null
     ******************************************************************************************************/
    /**
     * 转换{@code T}类型的值为{@link byte[]}
     * 
     * @param value 要被转换的值
     * @return {@link byte[]}实例或{@code null}
     */
    private static <T> byte[] convertToBytes(final T value) {
        if (value == null)
            return null;
        
        @SuppressWarnings("unchecked")
        Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (byte[].class.isAssignableFrom(cls)) {
            return (byte[])value;
        }
        
        if (Byte[].class.isAssignableFrom(cls)) {
            return convertToByteArray((Byte[])value);
        }
        
        if (String.class.isAssignableFrom(cls)) {
            return ((String)value).getBytes();
        }
        
        if (char[].class.isAssignableFrom(cls) ||
                Character[].class.isAssignableFrom(cls)) {
            char[] chars = null;
            if (char[].class.isAssignableFrom(cls))
                chars = (char[])value;
            else
                chars = convertToCharArray((Character[])value);
            
            
            if (chars.length ==0)
                return new byte[0];
            
            final Charset cs = Charset.forName(DefaultCharEncoding);
            final CharBuffer cb = CharBuffer.allocate(chars.length);
            cb.put(chars);
            cb.flip();

            return cs.encode(cb).array();
        }
        
        if (Blob.class.isAssignableFrom(cls)) {
            final Blob blob = (Blob)value;
            InputStream is = null;
            try {
                if (blob.length() > 0) {
                    is = blob.getBinaryStream();
                    final byte[] bytes = new byte[(int)(blob.length())];
                    is.read(bytes, 0, (int)(blob.length()));
                    return bytes;
                }
            } catch (SQLException | IOException sqle) {
                sqle.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    is = null;
                }
            }
            
            return null;
        }
        
        if (Clob.class.isAssignableFrom(cls)) {
            final Clob clob = (Clob)value;
            Reader reader = null;
            try {
                if (clob.length() > 0) {
                    reader = clob.getCharacterStream();
                    final char[] chars = new char[(int) (clob.length())];
                    reader.read(chars);
                    return convertToBytes(chars);
                }
            } catch (SQLException | IOException sqle) {
                sqle.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch(Exception ignored) {
                    }
                    reader = null;
                }
            }
        }
        
        return null;
    }
    
    /******************************************************************************************************
     *  转换成char[]类型
     *  
     *  1. char[]      -> char[]
     *  2. Character[] -> char[]
     *  3. String      -> char[]
     *  4. byte[]      -> char[]
     *  5. Byte[]      -> char[]
     *  6. Clob    -> char[]
     *  7. Blob    -> char[]
     *  8. 其它         -> null
     ******************************************************************************************************/
    /**
     * 转换{@code T}类型的值为{@link char[]}
     * 
     * @param value 要被转换的值
     * @return {@link char[]}实例或{@code null}
     */
    private static <T> char[] convertToChars(final T value) {
        if (value == null)
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (char[].class.isAssignableFrom(cls)) {
            return (char[])value;
        }
        
        if (Character[].class.isAssignableFrom(cls)) {
            return convertToCharArray((Character[])value);
        }
        
        if (String.class.isAssignableFrom(cls)) {
            return ((String)value).toCharArray();
        }
        
        if (byte[].class.isAssignableFrom(cls) ||
                Byte[].class.isAssignableFrom(cls)) {
            byte[] bytes = null;
            
            if (byte[].class.isAssignableFrom(cls))
                bytes = (byte[]) value;
            else
                bytes = convertToByteArray((Byte[])value);

            if (bytes.length == 0)
                return new char[0];

            final Charset cs = Charset.forName(DefaultCharEncoding);
            final ByteBuffer bb = ByteBuffer.allocate(bytes.length);
            bb.put(bytes);
            bb.flip();
            
            return cs.decode(bb).array();
        }
        
        if (Clob.class.isAssignableFrom(cls)) {
            final Clob clob = (Clob)value;
            Reader reader = null;
            try {
                reader = clob.getCharacterStream();
                final char[] chars = new char[(int) (clob.length())];                    
                reader.read(chars);            
                return chars;
            } catch (SQLException | IOException ignored) {
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch(Exception ignored) {}
                    reader = null;
                }
            }

            return null;
        }
        
        if (Blob.class.isAssignableFrom(cls)) {
            final Blob blob = (Blob)value;
            InputStream is = null;
            try {                
                is = blob.getBinaryStream();
                final byte[] bytes = new byte[(int)(blob.length())];                    
                is.read(bytes, 0, (int)(blob.length()));                    
                return convertToChars(bytes);            
            } catch (SQLException sqle) {
            } catch (IOException ioe) {
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch(Exception e) {}
                    is = null;
                }
            }

            return null;
        }
        
        return null;
    }
    
    /******************************************************************************************************
     *  转换成Boolean类型
     *  
     *  1. Boolean  -> Boolean
     *  2. String   -> Boolean  (YES -> TRUE  NO -> FALSE)
     *  3. Number   -> Boolean (true : >0)
     *  3. 其它              -> null
     ******************************************************************************************************/
    /**
     * 转换{@code T}类型的值为{@link Boolean}
     * 
     * @param value 要被转换的值
     * @return {@link Boolean}实例或{@code null}
     */
    private static <T> Boolean convertToBoolean(final T value) {
        if (value == null)
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Boolean.class.isAssignableFrom(cls)) {
            return (Boolean)value;
        }
        
        if (String.class.isAssignableFrom(cls)) {
            final String v = ((String)value).trim().toLowerCase();
            
            if ("true".equals(v) || "yes".equals(v))
                return Boolean.TRUE;
            
            return Boolean.FALSE;
        }
        
        if (Number.class.isAssignableFrom(cls)) {
            return ((Number)value).doubleValue()>0;
        }
        
        return null;
    }
    
    /******************************************************************************************************
     *  转换成Clob类型
     *  
     *  1. Clob    -> Clob
     *  2. String  -> Clob
     *  3. byte[]  -> Clob
     *  4. char[]  -> Clob
     *  3. 其它         -> null
     ******************************************************************************************************/
    /**
     * 转换{@code T}类型的值为{@link Blob}
     * 
     * @param value 要被转换的值
     * @return {@link Blob}实例或{@code null}
     */
    private static <T> Clob convertToClob(final T value) {
        if (value == null)
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Clob.class.isAssignableFrom(cls)) {
            return (Clob)value;
        }
        
        if (String.class.isAssignableFrom(cls)) {
            if (StringUtils.isEmpty((String)value)) {
                return null;
            }
            
            try {
                return new SerialClob(((String)value).toCharArray());
            } catch (SerialException e) {
            } catch (SQLException e) {
            }
            
            return null;
        }
        
        if (char[].class.isAssignableFrom(cls)) {
            try {
                return new SerialClob((char[])value);
            } catch (SerialException e) {
            } catch (SQLException e) {
            }
            
            return null;
        }
        
        if (byte[].class.isAssignableFrom(cls)) {
            try {
                return new SerialClob(convertToChars((byte[])value));
            } catch (SerialException e) {
            } catch (SQLException e) {
            }
            
            return null;
        }
                
        return null;
    }    
    
    /******************************************************************************************************
     *  转换成Blob类型
     *  
     *  1. Blob    -> Blob
     *  2. String  -> Blob
     *  3. byte[]  -> Blob
     *  4. char[]  -> Blob
     *  3. 其它         -> null
     ******************************************************************************************************/
    /**
     * 转换{@code T}类型的值为{@link Blob}
     * 
     * @param value 要被转换的值
     * @return {@link Blob}实例或{@code null}
     */
    private static <T> Blob convertToBlob(final T value) {
        if (value == null)
            return null;
        
        @SuppressWarnings("unchecked")
        final Class<T> cls = ClassUtils.primitiveToWrapper((Class<T>) value.getClass());
        
        if (Blob.class.isAssignableFrom(cls)) {
            return (Blob)value;
        }
        
        if (String.class.isAssignableFrom(cls)) {
            if (StringUtils.isEmpty((String)value)) {
                return null;
            }
            
            try {
                return new SerialBlob(((String)value).getBytes());
            } catch (SerialException e) {
            } catch (SQLException e) {
            }
            
            return null;
        }
        
        if (char[].class.isAssignableFrom(cls)) {
            try {
                return new SerialBlob(convertToBytes((char[])value));
            } catch (SerialException e) {
            } catch (SQLException e) {
            }
            
            return null;
        }
        
        if (byte[].class.isAssignableFrom(cls)) {
            try {
                return new SerialBlob((byte[])value);
            } catch (SerialException e) {
            } catch (SQLException e) {
            }
            
            return null;
        }
                
        return null;
    }
    
    /******************************************************************************************************
     *  转换成基本基本类型的数组
     *  
     *  1. Character[]    -> char[]
     *  2. Byte[]         -> byte[]
     *  3. Long[]         -> long[]
     *  4. Integer[]      -> int[]
     *  5. Double[]       -> double[]
     *  6. Float[]        -> float[]
     *  7. Short[]        -> short[]
     ******************************************************************************************************/
    
    /**
     * 转化{@link Character}为{@link char}数组
     * 
     * @param array 要转换的{@link Character[]}数组
     * @return {@link char}数组
     */
    public static char[] convertToCharArray(final Character array[]) {
        if (array==null || array.length == 0)
            return new char[0];
        
        final char[] newArray = new char[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /**
     * 转化{@link Byte}为{@link byte}数组
     * 
     * @param array 要转换的{@link Byte[]}数组
     * @return {@link byte}数组
     */
    public static byte[] convertToByteArray(final Byte[] array) {
        if (array==null || array.length == 0)
            return new byte[0];
        
        final byte[] newArray = new byte[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }    
    
    /**
     * 转化{@link Long}为{@link long}数组
     * 
     * @param array 要转换的{@link Long[]}数组
     * @return {@link long}数组
     */
    public static long[] convertToLongArray(final Long[] array) {
        if (array==null || array.length == 0)
            return new long[0];
        
        final long[] newArray = new long[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }    
    
    /**
     * 转化{@link Integer}为{@link int}数组
     * 
     * @param array 要转换的{@link Integer[]}数组
     * @return {@link int}数组
     */
    public static int[] convertToIntArray(final Integer[] array) {
        if (array==null || array.length == 0)
            return new int[0];
        
        final int[] newArray = new int[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /**
     * 转化{@link Double}为{@link double}数组
     * 
     * @param array 要转换的{@link Double[]}数组
     * @return {@link double}数组
     */
    public static double[] convertToDoubleArray(final Double[] array) {
        if (array==null || array.length == 0)
            return new double[0];
        
        final double[] newArray = new double[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }    
    
    /**
     * 转化{@link Float}为{@link float}数组
     * 
     * @param array 要转换的{@link Float[]}数组
     * @return {@link float}数组
     */
    public static float[] convertToFloatArray(final Float[] array) {
        if (array==null || array.length == 0)
            return new float[0];
        
        final float[] newArray = new float[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }    
    
    /**
     * 转化{@link Short}为{@link short}数组
     * 
     * @param array 要转换的{@link Short[]}数组
     * @return {@link short}数组
     */
    public static short[] convertToShortArray(final Short[] array) {
        if (array==null || array.length == 0)
            return new short[0];
        
        final short[] newArray = new short[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }    
    
    
    /******************************************************************************************************
     *  转换成基本基本类型的数组
     *  
     *  1. char[]   -> Character[]
     *  2. byte[]   -> Byte[]
     *  3. long[]   -> Long[]
     *  4. int[]    -> Integer[]
     *  5. double[] -> Double[]
     *  6. float[]  -> Float[]
     *  7. short[]  -> Short[]
     ******************************************************************************************************/
    
    /**
     * 转化{@link char}为{@link Character}数组
     * 
     * @param array 要转换的{@link char[]}数组
     * @return {@link Character}数组
     */
    public static Character[] convertToWrapperCharArray(final char[] array) {
        if (array==null || array.length == 0)
            return new Character[0];
        
        final Character[] newArray = new Character[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /**
     * 转化{@link byte}为{@link Byte}数组
     * 
     * @param array 要转换的{@link byte[]}数组
     * @return {@link Byte}数组
     */
    public static Byte[] convertToWrapperByteArray(final byte[] array) {
        if (array==null || array.length == 0)
            return new Byte[0];
        
        final Byte[] newArray = new Byte[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /**
     * 转化{@link long}为{@link Long}数组
     * 
     * @param array 要转换的{@link long[]}数组
     * @return {@link Long}数组
     */
    public static Long[] convertToWrapperLongArray(final long[] array) {
        if (array==null || array.length == 0)
            return new Long[0];
        
        final Long[] newArray = new Long[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /**
     * 转化{@link int}为{@link Integer}数组
     * 
     * @param array 要转换的{@link int[]}数组
     * @return {@link Integer}数组
     */
    public static Integer[] convertToWrapperIntArray(final int[] array) {
        if (array==null || array.length == 0)
            return new Integer[0];
        
        final Integer[] newArray = new Integer[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /**
     * 转化{@link double}为{@link Double}数组
     * 
     * @param array 要转换的{@link double[]}数组
     * @return {@link Double}数组
     */
    public static Double[] convertToWrapperDoubleArray(final double[] array) {
        if (array==null || array.length == 0)
            return new Double[0];
        
        final Double[] newArray = new Double[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /**
     * 转化{@link float}为{@link Float}数组
     * 
     * @param array 要转换的{@link float[]}数组
     * @return {@link Float}数组
     */
    public static Float[] convertToWrapperFloatArray(final float[] array) {
        if (array==null || array.length == 0)
            return new Float[0];
        
        final Float[] newArray = new Float[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /**
     * 转化{@link short}为{@link Short}数组
     * 
     * @param array 要转换的{@link short[]}数组
     * @return {@link Short}数组
     */
    public static Short[] convertToWrapperShortArray(final short[] array) {
        if (array==null || array.length == 0)
            return new Short[0];
        
        final Short[] newArray = new Short[array.length];
        
        for (int i = 0 ; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
}
