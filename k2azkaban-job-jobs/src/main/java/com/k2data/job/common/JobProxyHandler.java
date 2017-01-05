package com.k2data.job.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lidong 2016-12-28
 */
public class JobProxyHandler<T> implements InvocationHandler {

    private Class<T> proxyObj;

    public JobProxyHandler(Class<T> proxyObj) {
        this.proxyObj = proxyObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        JobUtils.setRootPath(false);
        JobClassLoader.loadJarPath(JobUtils.getRootPath() + "lib");

        try {
            return method.invoke(proxyObj.newInstance(), args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
