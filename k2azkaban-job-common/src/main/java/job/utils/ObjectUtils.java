package job.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 对象操作工具类
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    /**
     * 判断是否为空,只判断如下几种情况
     * 1. Null
     * 2. CharSequence or StringBuilder or StringBuffer = ""
     * 3. Map.size = 0
     * 4. Array.size = 0
     * 5. Collection.size = 0
     * 
     * @param target 被判断的Object
     * @return true 如果空
     */
    public static boolean isEmpty(final Object target) {
        if (target == null)
            return true;

        final Class<?> clazz = target.getClass();

        // 判断是不是CharSequence
        if (CharSequence.class.isAssignableFrom(clazz)) {
            return ((CharSequence)target).length() == 0;
        }

        // 判断是不是StringBuilder
        if (target instanceof StringBuilder)
            return ((StringBuilder) target).length()==0;

        // 判断是不是StringBuffer
        if (target instanceof StringBuffer)
            return ((StringBuffer) target).length()==0;

        // 判断是不是array
        if (clazz.isArray()) {
            return Array.getLength(target) == 0;

        }

        // 判断是不是Collection
        if (Collection.class.isAssignableFrom(clazz)) {
            return ((Collection<?>) target).size() == 0;

        }

        // 判断是不是Map
        if (Map.class.isAssignableFrom(clazz)) {
            return ((Map<?, ?>) target).size() == 0;
        }

        return false;
    }

}
