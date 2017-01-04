/**
 * Copyright &copy; 2016 <a href="https://www.k2data.com.cn">K2DATA</a> All rights reserved.
 */
package com.k2data.platform.kmx.domain;

import java.util.ArrayList;
import java.util.List;

public class KmxDataRowsRspDomain {

    private int code;
    private String message;

    private List<KmxDataRowsDomain> dataRows;
    private KmxPageInfoDomain pageInfo;

    private KmxDeviceDomain device;    //设备信息

    public KmxDeviceDomain getDevice() {
        return device;
    }
    public void setDevice(KmxDeviceDomain device) {
        this.device = device;
    }
    public List<KmxDataRowsDomain> getDataRows() {
        if (dataRows == null)
            return new ArrayList<>();
        return dataRows;
    }
    public void setDataRows(List<KmxDataRowsDomain> dataRows) {
        this.dataRows = dataRows;
    }
    public KmxPageInfoDomain getPageInfo() {
        return pageInfo;
    }
    public void setPageInfo(KmxPageInfoDomain pageInfo) {
        this.pageInfo = pageInfo;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
