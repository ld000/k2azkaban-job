package com.k2data.platform.kmx.cond;

import com.k2data.platform.kmx.domain.KmxDataRowsRspDomain;
import com.k2data.platform.utils.Global;

import java.util.HashMap;

/**
 * @author lidong 17-1-11.
 */
public class KmxDevicesListV2Builder extends KmxCondBuilder {

    private String deviceNo;

    @Override
    public KmxCond build() {
        KmxCond cond = new KmxCond();
        cond.setUrl(Global.getConfig("kmx.devices.list.v2.url") + "/" + deviceNo);
        cond.setParams(new HashMap<>());
        cond.setClazz(KmxDataRowsRspDomain.class);

        return cond;
    }

    public KmxDevicesListV2Builder device(String deviceNo) {
        this.deviceNo = deviceNo;
        return this;
    }

}
