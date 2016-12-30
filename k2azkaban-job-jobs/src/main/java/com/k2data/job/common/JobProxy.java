package com.k2data.job.common;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author lidong 16-12-30.
 */
public class JobProxy implements MethodInterceptor {

    private BaseAbstractJob baseJob;

    public JobProxy(BaseAbstractJob baseJob) {
        this.baseJob = baseJob;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String path = JobUtils.getRootPath();

        baseJob.setPath(path);
        System.setProperty("rootPath", path);

        return proxy.invokeSuper(obj, args);
    }

}
