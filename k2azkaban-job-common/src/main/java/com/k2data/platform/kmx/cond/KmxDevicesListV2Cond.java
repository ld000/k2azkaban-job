package com.k2data.platform.kmx.cond;

import com.google.common.collect.Maps;
import com.k2data.platform.kmx.domain.KmxDataRowsRspDomain;
import com.k2data.platform.utils.Global;

import java.util.Map;

/**
 * KMX devices-list v2 接口查询条件
 *
 * @author lidong 11/22/16.
 */
public class KmxDevicesListV2Cond extends KmxCond {

    private String deviceNo;

    @Override
    public Class getClazz() {
        return KmxDataRowsRspDomain.class;
    }

    @Override
    public String getUrl() {
        return Global.getConfig("kmx.devices.list.v2.url") + "/" + getDeviceNo();
    }

    @Override
    public Map<String, String> getParams() {
        return Maps.newHashMap();
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

}
