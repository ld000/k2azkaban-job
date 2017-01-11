package com.k2data.platform.kmx.cond;

import com.google.common.collect.Maps;
import com.k2data.platform.kmx.domain.KmxDataPointsRspDomain;
import com.k2data.platform.utils.Global;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lidong 17-1-11.
 */
public class KmxDataPointsV3Builder extends KmxCondBuilder {

    private String device;
    private Set<String> sensor;
    private Long timestamp;
    private ShiftType shiftType;

    enum ShiftType {
        NEAREST("nearest"),
        BEFORE("before"),
        AFTER("after");

        private String shift;

        ShiftType(String shift) { this.shift = shift; }

        public String getShift() { return shift; }
        public void setShift(String shift) { this.shift = shift; }
    }

    public KmxDataPointsV3Builder device(final String device) {
        this.device = device;
        return this;
    }

    public KmxDataPointsV3Builder sensors(final Set<String> sensor) {
        this.sensor = sensor;
        return this;
    }

    public KmxDataPointsV3Builder sensors(final List<String> sensor) {
        this.sensor = new HashSet<>(sensor);
        return this;
    }

    public KmxDataPointsV3Builder sensor(final String sensor) {
        this.sensor.add(sensor);
        return this;
    }

    public KmxDataPointsV3Builder timestamp(final Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public KmxDataPointsV3Builder nearest() {
        this.shiftType = ShiftType.NEAREST;
        return this;
    }

    public KmxDataPointsV3Builder before() {
        this.shiftType = ShiftType.BEFORE;
        return this;
    }

    public KmxDataPointsV3Builder after() {
        this.shiftType = ShiftType.AFTER;
        return this;
    }

    @Override
    public KmxCond build() {
        KmxCond cond = new KmxCond();
        cond.setUrl(Global.getConfig("kmx.data.points.v3.url"));

        Map<String, String> params = Maps.newHashMap();
        params.put("select", genSelectCond());
        if (shiftType != null) {
            params.put("shift", String.format("{\"defaultShift\":\"%s\"}", shiftType.getShift()));
        }

        cond.setParams(params);
        cond.setClazz(KmxDataPointsRspDomain.class);
        cond.setV2(true);

        return cond;
    }

    private String genSelectCond() {
        StringBuilder selectSb = new StringBuilder();
        selectSb.append("{\"sources\":{\"device\":")
                .append("\"").append(device).append("\"")
                .append(",\"sensors\":[");

        int i = 0;
        for(String sensor: sensor) {
            if (i++ != 0)
                selectSb.append(",");

            selectSb.append("\"").append(sensor).append("\"");
        }

        selectSb.append("]")
                .append(",\"sampleTime\":{\"timestamp\":\"").append(timestamp).append("\"}")
                .append("}}");

        return selectSb.toString();
    }

}
