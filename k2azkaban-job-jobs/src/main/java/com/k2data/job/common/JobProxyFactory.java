package com.k2data.job.common;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Proxy;

/**
 * @author lidong 16-12-30.
 */
public class JobProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getCglibProxy(BaseAbstractJob baseJob) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(baseJob.getClass());
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, new JobProxy(baseJob)});
        enhancer.setCallbackFilter(new JobProxyFilter());

        return (T) enhancer.create();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getJdkProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new JobProxyHandler<T>(clazz));
    }

}
