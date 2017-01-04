package com.k2data.platform.kmx.cond;

import com.google.common.collect.Maps;
import com.k2data.platform.kmx.KmxClient;
import com.k2data.platform.kmx.domain.KmxDataRowsRspDomain;
import com.k2data.platform.utils.DateUtils;
import com.k2data.platform.utils.Global;
import com.k2data.platform.utils.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * KMX data-rows v3 接口查询条件
 */
public class KmxDataRowsV3Cond extends KmxCond {

    /** 请求参数 begin **/
    /* select 查询条件字段 begin */
    private String deviceNo;  // 设备号
    private String[] devices;  //实时接口新增，支持多设备查询
    private String[] sensors;   // 传感器列表
    private Date start; // 开始时间
    private Date stop;  // 结束时间
    /* select 查询条件字段 end */

    private String valueFilter; // 值过滤
    private String[] aggregations; // 聚合条件
    private String order;   // 排序
    private String size;    // 每页大小 ，注:针对实时接口该字段为每个设备显示的最近点数 
    private String page;    // 第几页
    
    private String where; //where筛选条件
    /** 请求参数 end **/

    private String whereMark;
    private String whereValue;

    @Override
    public Class getClazz() {
        return KmxDataRowsRspDomain.class;
    }

    @Override
    public String getUrl() {
        return Global.getConfig("kmx.data.rows.v3.url");
    }

    @Override
    public Map<String, String> getParams() {
        return genDataRowsParams();
    }

    /**
     * 转换查询参数格式
     */
    private Map<String, String> genDataRowsParams() {
        Map<String, String> params = Maps.newHashMap();
        if(StringUtils.isNotBlank(getDeviceNo())) {
            params.put("select", genSelectCond(getDeviceNo(), getSensors(), getStart(), getStop()));
        }
        if(getDevices() != null && getDevices().length != 0) {
            params.put("select", genSelectCond(getDevices(), getSensors(), getStart(), getStop()));
        }
        if (!StringUtils.isBlank(getWhereMark()))
            params.put("where", genWhereCond(getDevices(), getSensors(), getWhereMark(), getWhereValue()));
        if (!StringUtils.isBlank(getValueFilter()))
            params.put("valueFilter", getValueFilter());
        if (getAggregations() != null && getAggregations().length != 0)
            params.put("aggregation", genAggregationCond(getAggregations()));
        if (!StringUtils.isBlank(getOrder()))
            params.put("order", genOrderCond(getOrder()));
        if (!StringUtils.isBlank(getSize()))
            params.put("size", getSize());
        if (!StringUtils.isBlank(getPage()))
            params.put("page", getPage());

        return params;
    }

    /**
     * 生成 select 查询条件
     *
     * @param device 设备号
     * @param sensors 传感器
     * @param start 开始时间
     * @param stop 结束时间
     */
    private String genSelectCond(String device, String[] sensors, Date start, Date stop) {
        String sliceStart = DateUtils.format(start, KmxClient.ISO_PATTERN).replace("+", "%2B");
        String sliceStop = DateUtils.format(stop, KmxClient.ISO_PATTERN).replace("+", "%2B");

        StringBuilder selectSb = new StringBuilder();
        selectSb.append("{\"sources\":{\"device\":")
                .append("\"").append(device).append("\"")
                .append(",\"sensors\":[");

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
     * 生成 select 查询条件, 实时接口，多设备查询
     *
     * @param devices 设备号数组
     * @param sensors 传感器
     * @param start 开始时间 精确到时间
     * @param stop 结束时间 精确到时间
     */
    private String genSelectCond(String[] devices, String[] sensors, Date start, Date stop) {

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
    private String genWhereCond(String[] devices, String sensors[], String logic, String value) {

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
    private String genAggregationCond(String[] aggregations) {
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
    /**
     * 生成排序方式查询条件
     */
    private String genOrderCond(String orderMode) {
        return "{\"defaultOrder\": \""+  orderMode +  "\"}";
    }

    public String getWhereMark() {
        return whereMark;
    }
    public void setWhereMark(String whereMark) {
        this.whereMark = whereMark;
    }
    public String getWhereValue() {
        return whereValue;
    }
    public void setWhereValue(String whereValue) {
        this.whereValue = whereValue;
    }
    public String getDeviceNo() {
        return deviceNo;
    }
    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }
	public String[] getDevices() {
		return devices;
	}
	public void setDevices(String[] devices) {
		this.devices = devices;
	}
    public String[] getSensors() {
        return sensors;
    }
    public void setSensors(String[] sensors) {
        this.sensors = sensors;
    }
    public Date getStart() {
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }
    public Date getStop() {
        return stop;
    }
    public void setStop(Date stop) {
        this.stop = stop;
    }
    public String getValueFilter() {
        return valueFilter;
    }
    public void setValueFilter(String valueFilter) {
        this.valueFilter = valueFilter;
    }
    public String getOrder() {
        return order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
    public String[] getAggregations() {
        return aggregations;
    }
    public void setAggregations(String[] aggregations) {
        this.aggregations = aggregations;
    }
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}

}
