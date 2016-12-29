package com.k2data.platform.utils;

import com.k2data.platform.exception.InternalException;

/**
 * 类相关工具类
 * 
 * @author lidong
 */
public class ClassUtils {

    /**
     * 判断是否是基本类型或其包装类型
     *
     * @param clazz 要判断的类
     * @return true 如果是基本类型或包装类型
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return Integer.TYPE.equals(clazz) || Integer.class.equals(clazz) ||
                Double.TYPE.equals(clazz) || Double.class.equals(clazz) ||
                Long.TYPE.equals(clazz) || Long.class.equals(clazz) ||
                Float.TYPE.equals(clazz) || Float.class.equals(clazz) ||
                Short.TYPE.equals(clazz) || Short.class.equals(clazz) ||
                Byte.TYPE.equals(clazz) || Byte.class.equals(clazz) ||
                Character.TYPE.equals(clazz) || Character.class.equals(clazz) ||
                Boolean.TYPE.equals(clazz) || Boolean.class.equals(clazz);
    }

    /**
     * 转换基本类型为包装类型
     *
     * @param type 基本类型
     * @return 包装类型
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> primitiveToWrapper(final Class<T> type) {
        if (type == null || !type.isPrimitive()) {
            return type;
        }

        if (type == Integer.TYPE) {
            return (Class<T>) Integer.class;
        } else if (type == Double.TYPE) {
            return (Class<T>) Double.class;
        } else if (type == Long.TYPE) {
            return (Class<T>) Long.class;
        } else if (type == Boolean.TYPE) {
            return (Class<T>) Boolean.class;
        } else if (type == Float.TYPE) {
            return (Class<T>) Float.class;
        } else if (type == Short.TYPE) {
            return (Class<T>) Short.class;
        } else if (type == Byte.TYPE) {
            return (Class<T>) Byte.class;
        } else if (type == Character.TYPE) {
            return (Class<T>) Character.class;
        } else {
            return type;
        }
    }

    /**
     * 转换包装类型为基本类型
     *
     * @param type 基本类型
     * @return 包装类型
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrapperToPrimitive(final Class<T> type) {
        if (type == null || type.isPrimitive()) {
            return type;
        }

        if (type == Integer.class) {
            return (Class<T>) Integer.TYPE;
        } else if (type == Double.class) {
            return (Class<T>) Double.TYPE;
        } else if (type == Long.class) {
            return (Class<T>) Long.TYPE;
        } else if (type == Boolean.class) {
            return (Class<T>) Boolean.TYPE;
        } else if (type == Float.class) {
            return (Class<T>) Float.TYPE;
        } else if (type == Short.class) {
            return (Class<T>) Short.TYPE;
        } else if (type == Byte.class) {
            return (Class<T>) Byte.TYPE;
        } else if (type == Character.class) {
            return (Class<T>) Character.TYPE;
        } else {
            return type;
        }
    }

    /**
     * 获取对应{@link Class}的一个实例，该{@link Class}必须要有空的构造函数
     *
     * @param clazz 需要创建实例的{@link Class}
     * @return 一个实例
     */
    public static <T> T newInstance(final Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException ie) {
            throw new InternalException(ie);
        } catch (IllegalAccessException iae) {
            throw new InternalException(iae);
        }
    }

    /**
     * 用于判断是否是Proxy
     * CGLib 代理  判断字段  {@code $$EnhancerByCGLIB$$}
     * JDK 代理  判断字段  {@code $Proxy}
     *
     * @param clazz {@link Class}用来判断
     * @return {@code true} 如果是Proxy
     */
    public static boolean isProxy(final Class<?> clazz) {
        if (clazz == null)
            return false;

        final String name = clazz.getName();

        return name.contains("$$EnhancerByCGLIB$$") ||
                name.contains("$Proxy");
    }

    /**
     * 获取一个类的原始类，避免取得{@code proxy}
     *
     * @param <T> 类名
     * @param clazz 要被取原始类的{@link Class}
     * @return 原生类
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getOriginalClass(final Class<T> clazz) {
        if (isProxy(clazz)) {
            return (Class<T>) clazz.getSuperclass();
        } else {
            return clazz;
        }
    }

    /**
     * 对于数组获取数组中数据的类型
     *
     * @param arrayClazz 数组的类
     * @return 数组中数据的实际类型
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getArrayClass(final Class<?> arrayClazz) {
        final String className = arrayClazz.getName();


        if ("[I".equals(className)) {
            return (Class<T>) Integer.TYPE;
        } else if ("[J".equals(className)) {
            return (Class<T>) Long.TYPE;
        } else if ("[D".equals(className)) {
            return (Class<T>) Double.TYPE;
        } else if ("[F".equals(className)) {
            return (Class<T>) Float.TYPE;
        } else if ("[C".equals(className)) {
            return (Class<T>) Character.TYPE;
        } else if ("[S".equals(className)) {
            return (Class<T>) Short.TYPE;
        } else if ("[Z".equals(className)) {
            return (Class<T>) Boolean.TYPE;
        } else if ("[B".equals(className)) {
            return (Class<T>) Byte.TYPE;
        } else if ("[V".equals(className)) {
            return (Class<T>) Void.TYPE;
        }

        return (Class<T>) arrayClazz;
    }

    /**
     * 通过类名获取对应的类
     *
     * @param className 类名
     * @return 对应的类
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(final String className) {
        final ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        final ClassLoader loader = contextCL == null ? ClassLoader.getSystemClassLoader(): contextCL;

        try {
            final String abbreviation = getClassAbbreviation(className);
            if (className.equals(abbreviation)) { // 非基础类型
                return (Class<T>) Class.forName(getFullyQualifiedName(className), false, loader);
            } else { // 基础类型
                return (Class<T>) Class.forName("[" + abbreviation, false, loader).getComponentType();
            }
        } catch(ClassNotFoundException cnfe) {
            throw new InternalException(cnfe);
        }
    }

    /**
     * 是否是Java 自有的类型
     *
     * @param clazz 要判断的类
     * @return true 如果是JDK自有类型
     */
    public static boolean isJdkType(final Class<?> clazz) {
        if (clazz == null)
            return true;

        final String name = clazz.getName();

        return clazz.isPrimitive()
                || name.startsWith("java.applet.")
                || name.startsWith("java.awt.")
                || name.startsWith("java.beans.")
                || name.startsWith("java.io.")
                || name.startsWith("java.lang.")
                || name.startsWith("java.math.")
                || name.startsWith("java.net.")
                || name.startsWith("java.nio.")
                || name.startsWith("java.rmi.")
                || name.startsWith("java.security.")
                || name.startsWith("java.sql.")
                || name.startsWith("java.text.")
                || name.startsWith("java.util.")
                || name.startsWith("java.rmi.")
                || name.startsWith("com.oracle.")
                || name.startsWith("com.sun.")
                || name.startsWith("javax.accessibility.")
                || name.startsWith("javax.activation.")
                || name.startsWith("javax.annotation.")
                || name.startsWith("javax.imageio.")
                || name.startsWith("javax.jws.")
                || name.startsWith("javax.lang.")
                || name.startsWith("javax.management.")
                || name.startsWith("javax.naming.")
                || name.startsWith("javax.print.")
                || name.startsWith("javax.rmi.")
                || name.startsWith("javax.script.")
                || name.startsWith("javax.security.")
                || name.startsWith("javax.smartcardio.")
                || name.startsWith("javax.sound.")
                || name.startsWith("javax.sql.")
                || name.startsWith("javax.swing.")
                || name.startsWith("javax.tools.")
                || name.startsWith("javax.transaction.")
                || name.startsWith("javax.xml.")
                || name.startsWith("org.ietf.")
                || name.startsWith("org.jcp.")
                || name.startsWith("org.omg.")
                || name.startsWith("org.w3c.")
                || name.startsWith("org.xml.")
                || name.startsWith("sun.applet.")
                || name.startsWith("sun.audio.")
                || name.startsWith("sun.awt.")
                || name.startsWith("sun.beans.")
                || name.startsWith("sun.corba.")
                || name.startsWith("sun.dc.")
                || name.startsWith("sun.font.")
                || name.startsWith("sun.instrument.")
                || name.startsWith("sun.io.")
                || name.startsWith("sun.java2d.")
                || name.startsWith("sun.jdbc.")
                || name.startsWith("sun.jkernel.")
                || name.startsWith("sun.management.")
                || name.startsWith("sun.misc.")
                || name.startsWith("sun.net.")
                || name.startsWith("sun.nio.")
                || name.startsWith("sun.org.")
                || name.startsWith("sun.print.")
                || name.startsWith("sun.reflect.")
                || name.startsWith("sun.rmi.")
                || name.startsWith("sun.security.")
                || name.startsWith("sun.swing.")
                || name.startsWith("sun.text.")
                || name.startsWith("sun.tools.")
                || name.startsWith("sun.usagetracker.")
                || name.startsWith("sun.util.")
                || name.startsWith("sunw.io.")
                || name.startsWith("sunw.util.")
                || name.startsWith("javax.net.")
                || name.startsWith("javax.crypto.");

    }


    /******************************************************************************************
     * 内部私有的工具类
     ******************************************************************************************/
    /**
     * 获取类型的全部描述，用于从{@link ClassLoader}那里获取对应的{@link Class}
     *
     * @param className 类名
     * @return 类型的全部描述
     */
    private static String getFullyQualifiedName(final String className) {
        if (className.endsWith("[]")) { // 对于Array、Entry等要加以判断
            StringBuilder sb = new StringBuilder();

            String name = className;

            while(name.endsWith("[]")) {
                name = name.substring(0, name.length() - 2);

                sb.append("[");
            }

            final String abbreviation = getClassAbbreviation(name);

            if (StringUtils.isBlank(abbreviation)) {
                sb.append("L").append(name).append(";");
            } else {
                sb.append(abbreviation);
            }

            return sb.toString();
        }

        return className;
    }

    /**
     * 获取类名的简写，用于从{@link ClassLoader}那里获取对应的{@link Class}
     *
     * @param className 类名
     * @return 类名的简写，如果有的话
     */
    private static String getClassAbbreviation(final String className) {
        if ("int".equals(className)) {
            return "I";
        } else if ("long".equals(className)) {
            return "J";
        } else if ("double".equals(className)) {
            return "D";
        } else if ("float".equals(className)) {
            return "F";
        } else if ("char".equals(className)) {
            return "C";
        } else if ("short".equals(className)) {
            return "S";
        } else if ("boolean".equals(className)) {
            return "Z";
        } else if ("byte".equals(className)) {
            return "B";
        } else if ("void".equals(className)) {
            return "V";
        }

        return className;
    }

}
