package com.k2data.job.common;

import com.k2data.platform.exception.InternalException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String propsFile = System.getenv("JOB_PROP_FILE");
//        Properties prop = new Properties();
//        prop.load(new BufferedReader(new FileReader(propsFile)));
//
//        String jobName = System.getenv("JOB_NAME");

        Pattern pattern = Pattern.compile("^(.*/[0-9]*?/)(.*)$");
        Matcher matcher = pattern.matcher(propsFile);

        String path = "";
        if (matcher.find()) {
            path = matcher.group(1);
            System.setProperty("path", path);
        } else {
            throw new InternalException("解析 project 路径出错...");
        }

        JobClassLoader.loadJarPath(path + "/lib");

        try {
            return method.invoke(proxyObj.newInstance(), args);
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

}
