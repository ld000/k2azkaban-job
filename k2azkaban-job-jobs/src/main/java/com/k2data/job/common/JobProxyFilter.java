package com.k2data.job.common;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * @author lidong 16-12-30.
 */
public class JobProxyFilter implements CallbackFilter {

    @Override
    public int accept(Method method) {
        if ("run".equalsIgnoreCase(method.getName())) {
            return 1;
        }

        return 0;
    }

}
