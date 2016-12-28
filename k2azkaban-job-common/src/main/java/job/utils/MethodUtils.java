package job.utils;

import com.k2data.job.exception.InternalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * {@link Method}反射相关的工具类
 */
public final class MethodUtils {
    
    private static final Logger logger = LogManager.getLogger(MethodUtils.class);
    
    /**
     * 通过成员方法的名称和参数的类型来获取对应的{@link Method}，方法名称区分大小写
     *
     * @param clazz 成员方法从这个类获取
     * @param methodName 成员方法的名称
     * @param parameterTypes 成员方法所需的参数的类型
     * @return 成员方法 或  {@code null} 
     */
    public static Method getExactMethod(final Class<?> clazz, final String methodName, final Class<?> ... parameterTypes) {
        return getMethod(clazz, methodName, false, parameterTypes);
    }
    
    /**
     * 通过成员方法的名称和参数的类型来获取对应的{@link Method}，方法名称不区分大小写
     *
     * @param clazz 成员方法从这个类获取
     * @param methodName 成员方法的名称
     * @param parameterTypes 成员方法所需的参数的类型
     * @return 成员方法 或  {@code null} 
     */
    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?> ... parameterTypes) {
        return getMethod(clazz, methodName, true, parameterTypes);
    }
    
    /**
     * 获取成员变量的读取方法，也就是{@code Getter}方法
     * 
     * @param clazz 成员变量从这个类获取
     * @param propertyName 成员变量的名称
     * @return 成员方法 或  {@code null} 
     */
    public static Method getReadMethod(final Class<?> clazz, final String propertyName) {
        Method reader = getMethod(clazz, "get" + propertyName);
        
        if (reader == null) {
            reader = getMethod(clazz, "is" + propertyName);
        }
        
        return reader;
    }
    
    /**
     * 获取成员变量的写入方法，也就是{@code Setter}方法
     * 
     * @param clazz 成员变量从这个类获取
     * @param propertyName 成员变量的名称
     * @return 成员方法 或  {@code null} 
     */
    public static Method getWriteMethod(final Class<?> clazz, final String propertyName) {
        return getMethod(clazz, "set" + propertyName);
    }
    
    /**
     * 调用成员方法
     *   
     * @param object 成员方法从这个实例调用
     * @param method 成员方法
     * @param args 成员方法所需的参数
     * @return 方法的结果
     */
    public static Object invokeMethod(final Object object, final Method method, final Object ... args) {        
        Class<?>[] types = method.getParameterTypes();
         
        final Object[] cArgs = new Object[args== null ? 0 : args.length];

        if (args != null && args.length>0) {
            for (int i = 0; i < args.length; i++) {
                cArgs[i] = ConvertUtils.convert(args[i], types[i]);
            }
        }
        
        if (!method.isAccessible())
            method.setAccessible(true);
        
        try {
            return method.invoke(object, cArgs);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException iae) {
            throw new InternalException(iae);
        }
    }
    
    /**
     * 用{@code Getter}方法获取成员变量的值
     * 
     * @param object 成员变量从这个实例调用
     * @param propertyName 成员变量的名称
     * @return
     */
    public static Object readProperty(final Object object, final String propertyName) {
        final Method method = getReadMethod(object.getClass(), propertyName);
        
        if (method == null) {
            return null;
        }
        
        return invokeMethod(object, method);
    }
    
    /*********************************************************************************************
     * 私有工具方法
     *********************************************************************************************/
    /**
     * 通过成员方法的名称和参数的类型来获取对应的{@link Method}，名称区分大小写是可选的
     *
     * @param clazz 成员方法从这个类获取
     * @param methodName 成员方法的名称
     * @param ignoreCase 是否大小写区分
     * @param parameterTypes 成员方法所需的参数的类型，如果不区分大小写将会被忽略
     * @return 成员方法 或  {@code null} 
     */
    private static Method getMethod(final Class<?> clazz, final String methodName, boolean ignoreCase, final Class<?> ... parameterTypes) {        
        final Method[] methods = clazz.getMethods();
        
        for (Method  method : methods) {
            if (ignoreCase) {
                if (method.getName().equalsIgnoreCase(methodName)) {
                    return method;
                }
            } else {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Class " + clazz + " doesn't have method " + methodName);
        }
        
        return null;
    }
}
