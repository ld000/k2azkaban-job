package com.k2data.platform.kmx.domain;

import java.util.List;

/**
 * @author lidong 11/25/16.
 */
public class KmxDataPointsRspDomain {

    private int code;
    private String message;
    private List<KmxDataPointsDomain> dataPoints;

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

    public List<KmxDataPointsDomain> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<KmxDataPointsDomain> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
