package com.k2data.platform.kmx;

/**
 * 大数据平台接口结果处理接口
 */
@FunctionalInterface
public interface KmxResponseHandler<T> {
    
    void handleResponse(T rsp);

}
