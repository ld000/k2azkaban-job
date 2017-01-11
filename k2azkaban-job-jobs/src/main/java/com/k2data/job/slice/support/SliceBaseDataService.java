package com.k2data.job.slice.support;

import com.k2data.platform.domain.LgDeviceSliceStatics;
import com.k2data.platform.domain.MapCoord;
import com.k2data.platform.kmx.KmxClient;
import com.k2data.platform.kmx.KmxConvert;
import com.k2data.platform.kmx.SensorNameEnum;
import com.k2data.platform.kmx.cond.KmxCond;
import com.k2data.platform.kmx.domain.KmxDataRowsRspDomain;
import com.k2data.platform.persistence.SqlRunner;
import com.k2data.platform.utils.DateUtils;
import com.k2data.platform.utils.IdGen;
import com.k2data.platform.utils.MapCoordUtils;
import com.k2data.platform.utils.StringUtils;

import java.util.*;

public class SliceBaseDataService {

    //请求传感器参数
    private static final String[] dateSensors = new String[]{
            SensorNameEnum.ACC_STATUS.getSensorName(),
            SensorNameEnum.LATITUDE_NUM.getSensorName(),
            SensorNameEnum.LONGITUDE_NUM.getSensorName(),
            SensorNameEnum.CITYCODE.getSensorName(),
            SensorNameEnum.ADDRESS.getSensorName(),
            SensorNameEnum.ENGIN_ROTAT.getSensorName(),
            SensorNameEnum.PROVINCE.getSensorName(),
            SensorNameEnum.CITY.getSensorName(),
            SensorNameEnum.TOTAL_DURATION.getSensorName(),
            SensorNameEnum.START_TIMES.getSensorName(),
            SensorNameEnum.REALTIME_DURATION.getSensorName()
    };

    //异步请求入口
    public void saveAsyncDeviceSliceData(final String deviceNo, final Date recordTime){
        Date start = DateUtils.parseDate(DateUtils.formatDate(recordTime) + " 00:00:00");
        Date stop = DateUtils.parseDate(DateUtils.formatDate(recordTime) + " 23:59:59");

        KmxCond cond = KmxCond.dataRowsV3()
                .device(deviceNo)
                .sensors(dateSensors)
                .start(start)
                .stop(stop)
                .size(KmxClient.PAGE_SIZE)
                .build();

        KmxClient.getAsync(cond, rsp -> {

            List<LgDeviceSliceStatics> dateList = analysisSliceData(KmxConvert.objectToList((KmxDataRowsRspDomain) rsp), deviceNo);

            if(dateList != null && !dateList.isEmpty()) {
                SqlRunner.insert(SliceSqlProvider.insertBaseSlice(dateList));
            }
        });
    }

    //同步请求入口
    public void saveSyncDeviceSliceData(final String deviceNo, final Date recordTime){
        Date start = DateUtils.parseDate(DateUtils.formatDate(recordTime) + " 00:00:00");
        Date stop = DateUtils.parseDate(DateUtils.formatDate(recordTime) + " 23:59:59");

        saveSyncDeviceSliceData(deviceNo, start, stop);
    }

    public void saveSyncDeviceSliceData(final String deviceNo, Date start, Date stop){
        KmxCond cond = KmxCond.dataRowsV3()
                .device(deviceNo)
                .sensors(dateSensors)
                .start(start)
                .stop(stop)
                .size(KmxClient.PAGE_SIZE)
                .build();

        KmxDataRowsRspDomain rsp = KmxClient.getSync(cond);

        if (rsp != null) {
            List<LgDeviceSliceStatics> dateList = analysisSliceData(KmxConvert.objectToList(rsp), deviceNo);

            if(dateList != null && !dateList.isEmpty()) {
                SqlRunner.insert(SliceSqlProvider.insertBaseSlice(dateList));
            }
        }
    }

    private Map<Integer, List<Map<String, Object>>> listToStartTimesMap(List<Map<String, Object>> sliceList) {

        if(sliceList == null || sliceList.isEmpty()) {
            return null;
        }

        Map<Integer, List<Map<String, Object>>> startTimesMap = new TreeMap<>(Comparable::compareTo);

        int startTimes = 0;
        boolean lastEffect = false; //末点数据是否有效
        Map<String, Object> mapPrevious = sliceList.get(0);

        for(Map<String, Object> mapCurrent : sliceList) {

            lastEffect = false;
            startTimes = StringUtils.toInteger(mapCurrent.get(SensorNameEnum.START_TIMES.getSensorName()).toString());

            int prevStartTimes = StringUtils.toInteger(mapPrevious.get(SensorNameEnum.START_TIMES.getSensorName()).toString());
            long prevRecordTime = Optional.ofNullable(mapPrevious.get("iso")).map(prev -> ((Date) prev).getTime()).orElse(0L);
            long currentRecordTime = Optional.ofNullable(mapCurrent.get("iso")).map(prev -> ((Date) prev).getTime()).orElse(0L);

            if(prevRecordTime != currentRecordTime) { //写入上一条数据
                startTimesMap.putIfAbsent(prevStartTimes, new ArrayList<>());
                startTimesMap.get(prevStartTimes).add(mapPrevious);
                lastEffect = true;
            }
            mapPrevious = mapCurrent;
        }

        if(lastEffect) { //防止丢失末点回传数据
            startTimesMap.putIfAbsent(startTimes, new ArrayList<>());
            startTimesMap.get(startTimes).add(mapPrevious);
        }

        return startTimesMap;
    }


    private List<LgDeviceSliceStatics> analysisStartTimesMap( Map<Integer, List<Map<String, Object>>> startTimesMap, String deviceNo) {

        if(startTimesMap == null || startTimesMap.isEmpty()) {
            return null;
        }

        List<LgDeviceSliceStatics> sliceStaticsesList = new ArrayList<>();

        int sliceCount = 1;

        for(Map.Entry<Integer, List<Map<String, Object>>> startTimesMapEntry : startTimesMap.entrySet()) {

            int startTimes = startTimesMapEntry.getKey();
            List<Map<String, Object>> startTimesList = startTimesMapEntry.getValue();

            //当期startTimes下少于两条记录，不做处理
            if(startTimesList == null || startTimesList.size() < 2) {
                continue;
            }

            Map<String, Object> mapBegin = startTimesList.get(0);
            Map<String, Object> mapEnd = null;
            Map<String, Object> mapPrevious = mapBegin;

            int realtimeDurationPrevious = 0;
            int realtimeDurationCurrent = 0;
            int duration = 0;
            for(Map<String, Object> mapCurrent : startTimesList) {

                //realtimeDurationPrevious = KmxConvert.verifyDuration(mapPrevious.get(SensorNameEnum.REALTIME_DURATION.getSensorName()));
                //realtimeDurationCurrent = KmxConvert.verifyDuration(mapCurrent.get(SensorNameEnum.REALTIME_DURATION.getSensorName()));

                realtimeDurationPrevious = StringUtils.toInteger(mapPrevious.get(SensorNameEnum.REALTIME_DURATION.getSensorName()));
                realtimeDurationCurrent = StringUtils.toInteger(mapCurrent.get(SensorNameEnum.REALTIME_DURATION.getSensorName()));

                if(realtimeDurationCurrent < realtimeDurationPrevious) {

                    //找到一个 realtimeDuration下降 -> 0
                    mapEnd = mapPrevious;
                    duration = realtimeDurationPrevious;
                    break;
                }

                mapPrevious = mapCurrent;
            }

            //当前次数下 realtimeDuration 一直增加 无下降
            if(mapEnd == null && realtimeDurationCurrent > 0) {
                mapEnd = mapPrevious;
                duration = realtimeDurationCurrent;
            }
            if(mapEnd != null && !mapEnd.isEmpty()) {

                double srcLatitude = StringUtils.toDouble(mapEnd.get(SensorNameEnum.LATITUDE_NUM.getSensorName()));
                double srcLongitude = StringUtils.toDouble(mapEnd.get(SensorNameEnum.LONGITUDE_NUM.getSensorName()));

                String lastCity = mapEnd.get(SensorNameEnum.CITY.getSensorName()).toString();
                lastCity = ("-".equals(lastCity) || StringUtils.isBlank(lastCity)) ? null : lastCity;
                String lastProvince = mapEnd.get(SensorNameEnum.PROVINCE.getSensorName()).toString();
                lastProvince = ("-".equals(lastProvince) || StringUtils.isBlank(lastProvince)) ? null : lastProvince;
                String lastAddress = mapEnd.get(SensorNameEnum.ADDRESS.getSensorName()).toString();
                lastAddress = ("-".equals(lastAddress) || StringUtils.isBlank(lastAddress)) ? null : lastAddress;

                MapCoord lastMapCoord = MapCoordUtils.nvr2bd(srcLatitude, srcLongitude);

                LgDeviceSliceStatics sliceStatics = new LgDeviceSliceStatics();
                sliceStatics.setId(IdGen.uuid());
                sliceStatics.setDeviceNo(deviceNo);
                sliceStatics.setSliceCount(sliceCount++);
                sliceStatics.setStartTimes(startTimes);
                sliceStatics.setSliceRunDuration(KmxConvert.convertDuration(duration));
                sliceStatics.setSliceWorkDuration(KmxConvert.convertDuration(duration));
                sliceStatics.setSliceStart((Date)mapBegin.get("iso"));
                sliceStatics.setSliceStop(KmxConvert.timeAdd((Date)mapEnd.get("iso"))); //结束时间均加上固定值
                sliceStatics.setLatitude(lastMapCoord.getLatitude());
                sliceStatics.setLongitude(lastMapCoord.getLongitude());
                sliceStatics.setSrcLatitude(srcLatitude);
                sliceStatics.setSrcLongitude(srcLongitude);
                sliceStatics.setCity(lastCity);
                sliceStatics.setProvince(lastProvince);
                sliceStatics.setAddress(lastAddress);
                sliceStatics.setAlarmCount(0);

                sliceStaticsesList.add(sliceStatics);
            }
        }

        return sliceStaticsesList;
    }
    //分析数据
    private List<LgDeviceSliceStatics>  analysisSliceData(List<Map<String, Object>> sliceList, String deviceNo) {

        return analysisStartTimesMap(listToStartTimesMap(sliceList), deviceNo);

    }

}