package com.k2data.job.common;

import java.lang.reflect.Proxy;

/**
 * @author lidong 2016-12-28
 */
public class JobProxyHelper {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new JobProxyHandler<T>(clazz));
    }

}
