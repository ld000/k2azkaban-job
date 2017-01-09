package com.k2data.job.common;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.FileInputStream;
import java.io.IOException;
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
        initLog4j();

        Object result;
        try {
            result = method.invoke(proxyObj.newInstance(), args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private void initLog4j() {
        try {
            Configurator.initialize(null, new ConfigurationSource(new FileInputStream(JobUtils.getRootPath() + "conf/log4j2.xml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
