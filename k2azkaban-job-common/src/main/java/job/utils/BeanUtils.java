package job.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Java Bean相关的工具类
 */
public final class BeanUtils {
    private static final Logger logger = LogManager.getLogger(BeanUtils.class);
    
    /**
     * 复制{@link ResultSet}的Column的值，用{@code Setter}方法赋值到Column的名称对应的目标{@link Object}的成员变量上
     * 
     * @param resultSet 提供值的{@link ResultSet}实例
     * @param accepter 接受数据的{@link Object}实例
     */
    public static void copyProperties(ResultSet resultSet, Object accepter) throws SQLException {
        final ResultSetMetaData rsmd = resultSet.getMetaData();
        final Class<?> cls = accepter.getClass();
        
        for (int i=1; i <= rsmd.getColumnCount(); i++) {
            final String name = rsmd.getColumnName(i);
            
            final Method writer = MethodUtils.getWriteMethod(cls, name);
            
            if (writer != null) {
                final Class<?> parameterType = writer.getParameterTypes()[0];
                
                Object paramValue = null;
                
                // 特殊判断Date类型
                if (parameterType.equals(Time.class)) {
                    paramValue = resultSet.getTime(i);
                } else if (parameterType.equals(java.sql.Date.class)) {
                    final Timestamp timestamp = resultSet.getTimestamp(i);
                    
                    if (timestamp != null) {
                        paramValue = new java.sql.Date(resultSet.getTimestamp(i).getTime());
                    }
                } else if (parameterType.equals(Timestamp.class)) {
                    paramValue = resultSet.getTimestamp(i);
                } else if (Date.class.isAssignableFrom(parameterType)) {
                    final Timestamp timestamp = resultSet.getTimestamp(i);
                    
                    if (timestamp != null) {
                        paramValue = new Date(resultSet.getTimestamp(i).getTime());
                    }
                } else {
                    paramValue = ConvertUtils.convert(resultSet.getObject(i), parameterType);
                }
                
                if (paramValue != null) {
                    MethodUtils.invokeMethod(accepter, writer, paramValue);
                }
            } else {
                logger.debug("Column [" + name +"] doesn't have matched Property in Class [" + cls.getCanonicalName() + "].");
            }
        }
    }
    
    /**
     * 复制{@link HttpServletRequest}的Param的值，用{@code Setter}方法赋值到Param的名称对应的目标{@link Object}的成员变量上
     * 其中如果{@code prefix}和{@code suffix}非空的话，就要从Param的名称中去掉前后缀后比较
     *
     * @param request 提供值的{@link HttpServletRequest}实例
     * @param prefix 前缀
     * @param suffix 后缀
     */
    public static void copyProperties(final HttpServletRequest request, final Object accepter, final String prefix, final String suffix) {
        @SuppressWarnings("unchecked")
        final Enumeration<String> keys = request.getParameterNames();
        
        final String vPrefix = ((StringUtils.isBlank(prefix))? "" : prefix);
        final String vSuffix = ((StringUtils.isBlank(suffix))? "" : suffix);
        
        final Class<?> cls = accepter.getClass();
        
        while(keys.hasMoreElements()) {
            final String key = keys.nextElement();
            
            if (!StringUtils.startsWithIgnoreCase(key, vPrefix) || !StringUtils.endsWithIgnoreCase(key, vSuffix)) {
                continue;
            }
            
            final String name = StringUtils.removeEnd(StringUtils.removeStart(key.toLowerCase(), vPrefix.toLowerCase()), vSuffix.toLowerCase());
                        
            if (StringUtils.isBlank(name))
                continue;
            
            final String value = request.getParameter(key);
            
            final Method writer = MethodUtils.getWriteMethod(cls, name);
            
            if (writer != null && (ClassUtils.isProxy(cls) || value != null)) {
                final Class<?> parameterType = writer.getParameterTypes()[0];
                
                Object paramValue = ConvertUtils.convert(value, parameterType);
                
                if (ClassUtils.isProxy(cls)
                        || paramValue != null) {
                    MethodUtils.invokeMethod(accepter, writer, paramValue);
                }
            }
        }
    }
    
    /**
     * 复制{@link HttpServletRequest}的Param的值，用{@code Setter}方法赋值到Param的名称对应的目标{@link Object}的成员变量上
     * 
     * @param request 提供值的{@link HttpServletRequest}实例
     */
    public static void copyProperties(final HttpServletRequest request, final Object accepter) {
        copyProperties(request, accepter, null, null);
    }
    
    /**
     * 复制{@link Object}的成员变量的值到目标{@link Object}的相同名称的的成员变量上，不区分大小写。
     * 
     * @param provider 提供值的{@link Object}实例
     * @param accepter 接受数据的{@link Object}实例
     */
    public static void copyProperties(Object provider, Object accepter) {
        final Class<?> providerCls = provider.getClass();
        final Class<?> accepterCls = accepter.getClass();
        
        for (Class<?> cls = providerCls; cls != null; cls = cls.getSuperclass()) {
            for (Field providerField : cls.getDeclaredFields()) {
                final String propertyName = providerField.getName();
                
                final Method providerReader = MethodUtils.getReadMethod(cls, propertyName);
                
                if (providerReader == null) {
                    continue;
                }
                
                final Method accepterWriter = MethodUtils.getWriteMethod(accepterCls, propertyName);
                
                if (accepterWriter == null) {
                    continue;
                }
                
                
                final Class<?> parameterType = accepterWriter.getParameterTypes()[0];
                
                Object paramValue = ConvertUtils.convert(MethodUtils.invokeMethod(provider, providerReader), parameterType);
                
                if (paramValue == null) {
                    /** ignore **/
                } else {
                    MethodUtils.invokeMethod(accepter, accepterWriter, paramValue);
                }
            }
        }
    }
    
    /**
     * 复制{@link Map}的值，用{@code Setter}方法赋值到{@link Map}的{@code KEY}对应的目标{@link Object}的成员变量上
     * 其中如果{@code prefix}和{@code suffix}非空的话，就要从{@link Map}的{@code KEY}中去掉前后缀后比较
     * 
     * @param provider 提供值的{@link Map}实例
     * @param prefix 前缀
     * @param suffix 后缀
     */
    public static <T> void copyProperties(final Map<String, T> provider, final Object accepter, final String prefix, final String suffix) {
        final String vPrefix = ((StringUtils.isBlank(prefix))? "" : prefix);
        final String vSuffix = ((StringUtils.isBlank(suffix))? "" : suffix);
        
        final Class<?> cls = accepter.getClass();

        for (Entry<String, T> entry : provider.entrySet()) {
            final String key = entry.getKey();

            if (!StringUtils.startsWithIgnoreCase(key, vPrefix) || !StringUtils.endsWithIgnoreCase(key, vSuffix)) {
                continue;
            }

            final String name = StringUtils.removeEnd(StringUtils.removeStart(key.toLowerCase(), vPrefix.toLowerCase()), vSuffix.toLowerCase());

            if (StringUtils.isBlank(name))
                continue;

            final T value = provider.get(key);

            final Method writer = MethodUtils.getWriteMethod(cls, name);

            if (writer != null) {
                final Class<?> parameterType = writer.getParameterTypes()[0];

                Object paramValue = ConvertUtils.convert(value, parameterType);

                if (paramValue == null) {
                    /** ignore **/
                } else {
                    MethodUtils.invokeMethod(accepter, writer, paramValue);
                }
            }
        }
    }
    
}
