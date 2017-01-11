package com.k2data.platform.kmx.cond;

import com.k2data.platform.kmx.KmxClient;
import com.k2data.platform.kmx.domain.KmxDataRowsRspDomain;
import com.k2data.platform.utils.DateUtils;
import com.k2data.platform.utils.Global;
import com.k2data.platform.utils.StringUtils;

import java.util.*;

/**
 * @author lidong 17-1-11.
 */
public class KmxDataRowsV3Builder extends KmxCondBuilder {

    /** 请求参数 begin **/
    /* select 查询条件字段 begin */
    private Set<String> device;
    private Set<String> sensors;
    private Date start; // 开始时间
    private Date stop;  // 结束时间
    /* select 查询条件字段 end */

    private String valueFilter; // 值过滤
    private Set<String> aggregations; // 聚合条件
    private String order;   // 排序
    private Integer size;    // 每页大小 ，注:针对实时接口该字段为每个设备显示的最近点数
    private Integer page;    // 第几页

    private String whereMark;
    private String whereValue;
    /** 请求参数 end **/

    @Override
    public KmxCond build() {

        new KmxWhereBuilder().build();

        Map<String, String> params = new HashMap<>();
        if (device != null && device.size() != 0) {
            params.put("select", genSelectCond(device, sensors, start, stop));
        }
//        if (!StringUtils.isBlank(getWhereMark())) {
//            params.put("where", genWhereCond(getDevices(), getSensors(), getWhereMark(), getWhereValue()));
//        }
        if (!StringUtils.isBlank(valueFilter)) {
            params.put("valueFilter", valueFilter);
        }
        if (aggregations != null && aggregations.size() != 0) {
            params.put("aggregation", genAggregationCond(aggregations));
        }
        if (!StringUtils.isBlank(order)) {
            params.put("order", "{\"defaultOrder\": \"" + order + "\"}");
        }
        if (size != null) {
            params.put("size", size + "");
        }
        if (page != null) {
            params.put("page", page + "");
        }

        KmxCond cond = new KmxCond();
        cond.setUrl(Global.getConfig("kmx.data.rows.v3.url"));
        cond.setParams(params);
        cond.setClazz(KmxDataRowsRspDomain.class);

        return cond;
    }

    /**
     * 生成 select 查询条件, 实时接口，多设备查询
     *
     * @param devices 设备号数组
     * @param sensors 传感器
     * @param start 开始时间 精确到时间
     * @param stop 结束时间 精确到时间
     */
    private String genSelectCond(Set<String> devices, Set<String> sensors, Date start, Date stop) {
        String sliceStart = DateUtils.format(start, KmxClient.ISO_PATTERN).replace("+", "%2B");
        String sliceStop = DateUtils.format(stop, KmxClient.ISO_PATTERN).replace("+", "%2B");

        StringBuilder selectSb = new StringBuilder();
        selectSb.append("{\"sources\":{\"devices\":[");
        int j= 0;
        for(String device: devices) {
            if (j++ != 0)
                selectSb.append(",");
            selectSb.append("\"").append(device).append("\"");
        }
        selectSb.append("],\"sensors\":[");

        int i = 0;
        for(String sensor: sensors) {
            if (i++ != 0)
                selectSb.append(",");
            selectSb.append("\"").append(sensor).append("\"");
        }

        selectSb.append("]")
                .append(",\"timeRange\":{\"start\":{\"iso\":")
                .append("\"").append(sliceStart).append("\"")
                .append("},\"end\":{\"iso\":")
                .append("\"").append(sliceStop).append("\"")
                .append("}}}}");

        return selectSb.toString();
    }

    /**
     * 生成 where 查询条件, 实时接口，多设备查询，暂时支持单传感器条件，后续再扩展
     *
     * @param devices 设备号数组
     * @param sensors 传感器
     * @param logic 比较逻辑
     * @param value 比较值
     */
    private String genWhereCond(Set<String> devices, String sensors[], String logic, String value) {

        //where=[{"C20086-accStatus":{"$eq":"01"}},{"C20081-accStatus":{"$eq":"01"}},{"C20083-accStatus":{"$eq":"01"}},{"C2007A-accStatus":{"$eq":"01"}},{"C2006A-accStatus":{"$eq":"01"}}]
        if(StringUtils.isAnyBlank(sensors)) {
            return StringUtils.EMPTY;
        }
        //TODO 暂时取第一个传感器作为where条件
        String sensor = sensors[0];
        StringBuilder whereSb = new StringBuilder();
        whereSb.append("[");
        int i = 0;
        for(String device: devices) {
            if (i++ != 0)
                whereSb.append(",");
            whereSb.append("{\"").append(device).append("-").append(sensor).append("\"").append(":")
                    .append("{\"").append(logic).append("\":\"").append(value).append("\"").append("}}");
        }
        whereSb.append("]");
        return whereSb.toString();
    }

    /**
     * 生成聚合查询条件
     */
    private String genAggregationCond(Set<String> aggregations) {
        StringBuilder aggregationSb = new StringBuilder();
        aggregationSb.append("{\"defaultAggregation\":[");

        int i = 0;
        for(String aggregation: aggregations) {
            if (i++ != 0)
                aggregationSb.append(",");

            aggregationSb.append("\"").append(aggregation).append("\"");
        }
        aggregationSb.append("]}");

        return aggregationSb.toString();
    }

    /* device begin */
    public KmxDataRowsV3Builder devices(Set<String> devices) {
        this.device = devices;
        return this;
    }

    public KmxDataRowsV3Builder deivces(List<String> devices) {
        this.device = new HashSet<>(devices);
        return this;
    }

    public KmxDataRowsV3Builder deivces(String[] devices) {
        Collections.addAll(this.device, devices);
        return this;
    }

    public KmxDataRowsV3Builder device(String device) {
        this.device.add(device);
        return this;
    }
    /* device end */

    /* sensors begin */
    public KmxDataRowsV3Builder sensors(Set<String> sensors) {
        this.sensors = sensors;
        return this;
    }

    public KmxDataRowsV3Builder sensors(List<String> sensors) {
        this.sensors = new HashSet<>(sensors);
        return this;
    }

    public KmxDataRowsV3Builder sensors(String[] sensors) {
        Collections.addAll(this.sensors, sensors);
        return this;
    }

    public KmxDataRowsV3Builder sensor(String sensor) {
        this.sensors.add(sensor);
        return this;
    }
    /* sensors end */

    public KmxDataRowsV3Builder start(Date start) {
        this.start = start;
        return this;
    }

    public KmxDataRowsV3Builder stop(Date stop) {
        this.stop = stop;
        return this;
    }

    public KmxDataRowsV3Builder valueFilter(String valueFilter) {
        this.valueFilter = valueFilter;
        return this;
    }

    public KmxDataRowsV3Builder count() {
        this.aggregations.add("count");
        return this;
    }

    public KmxDataRowsV3Builder min() {
        this.aggregations.add("min");
        return this;
    }

    public KmxDataRowsV3Builder max() {
        this.aggregations.add("max");
        return this;
    }

    public KmxDataRowsV3Builder desc() {
        this.order = "desc";
        return this;
    }

    public KmxDataRowsV3Builder size(Integer size) {
        this.size = size;
        return this;
    }

    public KmxDataRowsV3Builder page(Integer page) {
        this.page = page;
        return this;
    }

}
