package com.k2data.platform.kmx.cond;

import java.util.Map;

/**
 * @author lidong 17-1-10.
 */
public class KmxCond {

    private String url;
    private Map<String, String> params;
    private Class<?> clazz;
    private Boolean v2;

    public static KmxDataPointsV3Builder dataPointsV3() {
        return new KmxDataPointsV3Builder();
    }

    public static KmxDataRowsV3Builder dataRowsV3() {
        return new KmxDataRowsV3Builder();
    }

    public static KmxDevicesListV2Builder devicesListV2() {
        return new KmxDevicesListV2Builder();
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Map<String, String> getParams() {
        return params;
    }
    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    public Class<?> getClazz() {
        return clazz;
    }
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
    public Boolean isV2() {
        return v2;
    }
    public void setV2(Boolean v2) {
        this.v2 = v2;
    }

}
