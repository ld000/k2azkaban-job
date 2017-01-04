package com.k2data.job.slice.support;

import com.k2data.platform.domain.SliceDateIssue;
import com.k2data.platform.kmx.KmxConvert;
import com.k2data.platform.kmx.SensorNameEnum;
import com.k2data.platform.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author lidong 11/24/16.
 */
public class IssuePlugin {

    private final static Integer DURATION_OVER_24 = 1;
    private final static Integer DURATION_LESS_THAN_0 = 2;
    private final static Integer ONLY_SINGLE_DATA = 3;
    private final static Integer MISS_LOCATION_INFO = 4;
    private final static Integer EMPTY_DATA = 5;
    private final static Integer RUN_OFF_COUNT_LESS_THAN_0 = 6;

    /**
     * 时长超过 24h
     *
     * @return
     */
    public SliceDateIssue durationOver24(List<Map<String, Object>> sliceList) {
        if(sliceList == null || sliceList.isEmpty() || sliceList.size() == 1) {
            return null;
        }

        Map<String, Object> firstMap = sliceList.get(0);    //当天第一条记录
        Map<String, Object> lastMap = sliceList.get(sliceList.size() - 1); //当天最后一条记录

        int startDurationTotal = StringUtils.toInteger(firstMap.get(SensorNameEnum.TOTAL_DURATION.getSensorName()));
        int endDurationTotal = StringUtils.toInteger(lastMap.get(SensorNameEnum.TOTAL_DURATION.getSensorName()));

        int sliceRunDuration = KmxConvert.convertDuration(endDurationTotal - startDurationTotal);

        if (sliceRunDuration > 24 * 60) {
            SliceDateIssue sliceDateIssue = new SliceDateIssue();

            sliceDateIssue.setIssueType(DURATION_OVER_24);
            sliceDateIssue.setIssueValue(String.valueOf(sliceRunDuration));

            return sliceDateIssue;
        }

        return null;
    }

    /**
     * 时长小于 0
     *
     * @return
     */
    public SliceDateIssue durationLessThan0(List<Map<String, Object>> sliceList) {
        if(sliceList == null || sliceList.isEmpty() || sliceList.size() == 1) {
            return null;
        }

        Map<String, Object> firstMap = sliceList.get(0);    //当天第一条记录
        Map<String, Object> lastMap = sliceList.get(sliceList.size() - 1); //当天最后一条记录

        int startDurationTotal = StringUtils.toInteger(firstMap.get(SensorNameEnum.TOTAL_DURATION.getSensorName()));
        int endDurationTotal = StringUtils.toInteger(lastMap.get(SensorNameEnum.TOTAL_DURATION.getSensorName()));

        int sliceRunDuration = KmxConvert.convertDuration(endDurationTotal - startDurationTotal);

        if (sliceRunDuration < 0) {
            SliceDateIssue sliceDateIssue = new SliceDateIssue();

            sliceDateIssue.setIssueType(DURATION_LESS_THAN_0);
            sliceDateIssue.setIssueValue(String.valueOf(sliceRunDuration));

            return sliceDateIssue;
        }

        return null;
    }

    /**
     * 只有一条回传数据
     *
     * @return
     */
    public SliceDateIssue onlySingleData(List<Map<String, Object>> sliceList) {
        if(sliceList == null || sliceList.isEmpty()) {
            return null;
        }

        if (sliceList.size() == 1) {
            SliceDateIssue sliceDateIssue = new SliceDateIssue();

            sliceDateIssue.setIssueType(ONLY_SINGLE_DATA);
            sliceDateIssue.setIssueValue(String.valueOf(1));

            return sliceDateIssue;
        }

        return null;
    }

    /**
     * 没有位置信息
     *
     * @return
     */
    public SliceDateIssue missLocationInfo(List<Map<String, Object>> sliceList) {
        if(sliceList == null || sliceList.isEmpty() || sliceList.size() == 1) {
            return null;
        }

        double srcLatitude = 0d;
        double srcLongitude = 0d;
        String lastCity = "";
        String lastProvince = "";
        String lastAddress = "";

        Map<String, Object> lastMap = sliceList.get(sliceList.size() - 1);

        srcLatitude = StringUtils.toDouble(lastMap.get(SensorNameEnum.LATITUDE_NUM.getSensorName()));
        srcLongitude = StringUtils.toDouble(lastMap.get(SensorNameEnum.LONGITUDE_NUM.getSensorName()));

        lastCity = lastMap.get(SensorNameEnum.CITY.getSensorName()).toString();
        lastCity = ("-".equals(lastCity) || StringUtils.isBlank(lastCity)) ? null : lastCity;
        lastProvince = lastMap.get(SensorNameEnum.PROVINCE.getSensorName()).toString();
        lastProvince = ("-".equals(lastProvince) || StringUtils.isBlank(lastProvince)) ? null : lastProvince;
        lastAddress = lastMap.get(SensorNameEnum.ADDRESS.getSensorName()).toString();
        lastAddress = ("-".equals(lastAddress) || StringUtils.isBlank(lastAddress)) ? null : lastAddress;

        if (srcLatitude != 0d
                && srcLongitude != 0d
                && (lastCity != null
                || "北京市".equals(lastProvince)
                || "天津市".equals(lastProvince)
                || "重庆市".equals(lastProvince)
                || "上海市".equals(lastProvince))
                && lastProvince != null
                && lastAddress != null) {
            return null;
        }


        String value = "";
        if (srcLatitude == 0D) {
            value += "latitude";
        }

        if (srcLongitude == 0D) {
            if (StringUtils.isNotBlank(value)) {
                value += ",";
            }
            value += "longitude";
        }

        if (lastCity == null) {
            if (StringUtils.isNotBlank(value)) {
                value += ",";
            }
            value += "city";
        }

        if (lastProvince == null) {
            if (StringUtils.isNotBlank(value)) {
                value += ",";
            }
            value += "province";
        }

        if (lastAddress == null) {
            if (StringUtils.isNotBlank(value)) {
                value += ",";
            }
            value += "address";
        }

        if (StringUtils.isNotBlank(value)) {
            SliceDateIssue sliceDateIssue = new SliceDateIssue();

            sliceDateIssue.setIssueType(MISS_LOCATION_INFO);
            sliceDateIssue.setIssueValue(value);

            return sliceDateIssue;
        }

        return null;
    }

    /**
     * 全天无回传数据
     *
     * @return
     */
    public SliceDateIssue emptyData(List<Map<String, Object>> sliceList) {
        if (sliceList == null || sliceList.isEmpty()) {
            SliceDateIssue sliceDateIssue = new SliceDateIssue();

            sliceDateIssue.setIssueType(EMPTY_DATA);
            sliceDateIssue.setIssueValue("");

            return sliceDateIssue;
        }

        return null;
    }

    /**
     * 开机次数小于 0
     *
     * @param sliceList
     * @return
     */
    public SliceDateIssue runOffCountLessThan0(List<Map<String, Object>> sliceList) {
        if(sliceList == null || sliceList.isEmpty() || sliceList.size() == 1) {
            return null;
        }

        Map<String, Object> firstMap = sliceList.get(0);    //当天第一条记录
        Map<String, Object> lastMap = sliceList.get(sliceList.size() - 1); //当天最后一条记录

        int startRunOffCount = StringUtils.toInteger(firstMap.get(SensorNameEnum.START_TIMES.getSensorName()));
        int endRunOffCount = StringUtils.toInteger(lastMap.get(SensorNameEnum.START_TIMES.getSensorName()));

        int runOffCount = endRunOffCount - startRunOffCount;

        if (runOffCount < 0) {
            SliceDateIssue sliceDateIssue = new SliceDateIssue();

            sliceDateIssue.setIssueType(RUN_OFF_COUNT_LESS_THAN_0);
            sliceDateIssue.setIssueValue(runOffCount + "");

            return sliceDateIssue;
        }

        return null;
    }

}
