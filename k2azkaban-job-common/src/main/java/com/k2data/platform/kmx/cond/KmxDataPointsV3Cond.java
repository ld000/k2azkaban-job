package com.k2data.platform.kmx.cond;

import com.google.common.collect.Maps;
import com.k2data.platform.kmx.domain.KmxDataPointsRspDomain;
import com.k2data.platform.utils.Global;

import java.util.List;
import java.util.Map;

/**
 * KMX data-points v3 接口查询条件
 *
 * @author lidong 11/22/16.
 */
public class KmxDataPointsV3Cond extends KmxCond {

    private String device;
    private List<String> sensor;
    private Long timestamp;
    private ShiftType shiftType;

    public enum ShiftType {
        NEAREST("nearest"),
        BEFORE("before"),
        AFTER("after");

        private String shift;

        ShiftType(String shift) {
            this.shift = shift;
        }

        public String getShift() {
            return shift;
        }
        public void setShift(String shift) {
            this.shift = shift;
        }
    }

    @Override
    public Class getClazz() {
        return KmxDataPointsRspDomain.class;
    }

    @Override
    public String getUrl() {
        return Global.getConfig("kmx.data.points.v3.url");
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = Maps.newHashMap();
        params.put("select", genSelectCond());
        if (getShiftType() != null) {
            params.put("shift", String.format("{\"defaultShift\":\"%s\"}", getShiftType().getShift()));
        }

        return params;
    }

    private String genSelectCond() {
        StringBuilder selectSb = new StringBuilder();
        selectSb.append("{\"sources\":{\"device\":")
                .append("\"").append(getDevice()).append("\"")
                .append(",\"sensors\":[");

        int i = 0;
        for(String sensor: getSensor()) {
            if (i++ != 0)
                selectSb.append(",");

            selectSb.append("\"").append(sensor).append("\"");
        }

        selectSb.append("]")
                .append(",\"sampleTime\":{\"timestamp\":\"").append(getTimestamp()).append("\"}")
                .append("}}");

        return selectSb.toString();
    }

    public String getDevice() {
        return device;
    }
    public void setDevice(String device) {
        this.device = device;
    }
    public List<String> getSensor() {
        return sensor;
    }
    public void setSensor(List<String> sensor) {
        this.sensor = sensor;
    }
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public ShiftType getShiftType() {
        return shiftType;
    }
    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

}
