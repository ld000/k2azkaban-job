package com.k2data.platform.kmx.cond;

import java.util.Map;

/**
 * KMX 查询条件抽象类
 *
 * @author lidong 11/22/16.
 */
public abstract class KmxCond {

    public abstract Class getClazz();

    public abstract String getUrl();

    public abstract Map<String, String> getParams();

}
