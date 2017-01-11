package com.k2data.job.slice.support;

import com.k2data.platform.kmx.cond.KmxCond;
import com.k2data.platform.utils.Global;
import com.k2data.platform.domain.LgDeviceDateStatics;
import com.k2data.platform.domain.LgDeviceStatics;
import com.k2data.platform.domain.MapCoord;
import com.k2data.platform.domain.SliceDateIssue;
import com.k2data.platform.kmx.KmxClient;
import com.k2data.platform.kmx.KmxConvert;
import com.k2data.platform.kmx.KmxResponseHandler;
import com.k2data.platform.kmx.SensorNameEnum;
import com.k2data.platform.kmx.domain.KmxDataRowsRspDomain;
import com.k2data.platform.persistence.SqlRunner;
import com.k2data.platform.persistence.support.BoundSqlBuilder;
import com.k2data.platform.utils.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备 日/周/月/季度/年 工作统计
 */
public class SliceDateService {

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
            SensorNameEnum.START_TIMES.getSensorName()
    };

    /**
     * 设备每日统计请求KMX，异步入口函数
     */
    public void saveAsyncDayData(final String deviceNo, final Date recordTime){
        Date start = DateUtils.parseDate(DateUtils.formatDate(recordTime) + " 00:00:00");
        Date stop = DateUtils.parseDate(DateUtils.formatDate(recordTime) + " 23:59:59");

        KmxCond cond = KmxCond.dataRowsV3()
                .device(deviceNo)
                .sensors(dateSensors)
                .start(start)
                .stop(stop)
                .size(KmxClient.PAGE_SIZE)
                .build();

        KmxClient.getAsync(cond, new KmxResponseHandler<KmxDataRowsRspDomain>() {
            @Override
            public void handleResponse(KmxDataRowsRspDomain rsp) {
                LgDeviceDateStatics lgDeviceDateStatics = analysisSliceData(KmxConvert.objectToList(rsp), deviceNo, recordTime);
                if(lgDeviceDateStatics == null) {   //当天无记录 依赖上一天数据
                    //当天无记录，插入一条0 记录
                    lgDeviceDateStatics = new LgDeviceDateStatics();
                    lgDeviceDateStatics.setDeviceNo(deviceNo);
                    lgDeviceDateStatics.setWorkDate(recordTime);
                    lgDeviceDateStatics.setWorkCount(0);
                    lgDeviceDateStatics.setSliceRunDuration(0);
                    lgDeviceDateStatics.setSliceWorkDuration(0);
                    lgDeviceDateStatics.setRunoffCount(0);
                    lgDeviceDateStatics.setOilSum(0d);
                    lgDeviceDateStatics.setOilAvg(0d);
                    lgDeviceDateStatics.setRotationlSpeedMax(0);
                    lgDeviceDateStatics.setLatitude(0d);
                    lgDeviceDateStatics.setLongitude(0d);
                    lgDeviceDateStatics.setSrcLatitude(0d);
                    lgDeviceDateStatics.setSrcLongitude(0d);
                    lgDeviceDateStatics.setAlarmCount(0);

                    SqlRunner.insert(SliceSqlProvider.insertNoneDateSlice(lgDeviceDateStatics));
                } else {    //不依赖上一天数据
                    SqlRunner.insert(SliceSqlProvider.insertDateSlice(lgDeviceDateStatics));
                }
            }
        });
    }

    /**
     * 设备每日统计请求KMX，同步步入口函数
     */
    public void saveSyncDayData(final String deviceNo, final Date recordTime){
        Date start = DateUtils.parseDate(DateUtils.formatDate(recordTime) + " 00:00:00");
        Date stop = DateUtils.parseDate(DateUtils.formatDate(recordTime) + " 23:59:59");

        KmxCond cond = KmxCond.dataRowsV3()
                .device(deviceNo)
                .sensors(dateSensors)
                .start(start)
                .stop(stop)
                .size(KmxClient.PAGE_SIZE)
                .build();

        KmxDataRowsRspDomain rsp = KmxClient.getSync(cond);

        if (rsp != null) {
            LgDeviceDateStatics lgDeviceDateStatics = analysisSliceData(KmxConvert.objectToList(rsp), deviceNo, recordTime);
            if(lgDeviceDateStatics == null) {

                lgDeviceDateStatics = new LgDeviceDateStatics();
                lgDeviceDateStatics.setDeviceNo(deviceNo);
                lgDeviceDateStatics.setWorkDate(recordTime);
                lgDeviceDateStatics.setWorkCount(0);
                lgDeviceDateStatics.setSliceRunDuration(0);
                lgDeviceDateStatics.setSliceWorkDuration(0);
                lgDeviceDateStatics.setRunoffCount(0);
                lgDeviceDateStatics.setOilSum(0d);
                lgDeviceDateStatics.setOilAvg(0d);
                lgDeviceDateStatics.setRotationlSpeedMax(0);
                lgDeviceDateStatics.setAlarmCount(0);

                //当天无记录 依赖上一天数据
                SqlRunner.insert(SliceSqlProvider.insertNoneDateSlice(lgDeviceDateStatics));
            } else {
                //不依赖上一天数据
                SqlRunner.insert(SliceSqlProvider.insertDateSlice(lgDeviceDateStatics));
            }
        }
    }



    //分析数据为实体类
    private LgDeviceDateStatics analysisSliceData(List<Map<String, Object>> sliceList, String deviceNo, Date workDate) {



        IssuePlugin issuePlugin = new IssuePlugin();

        SliceDateIssue sliceDateIssue = null;
        int flag = Global.NO_INT;
        if ((sliceDateIssue = issuePlugin.missLocationInfo(sliceList)) != null) {
            flag = Global.NO_INT;

            sliceDateIssue.setId(IdGen.getUUID());
            sliceDateIssue.setGpsNo(deviceNo);
            sliceDateIssue.setWorkDate(workDate);
            sliceDateIssue.setInsertTime(new Date());

            SqlRunner.insert(BoundSqlBuilder.buildInsertBoundSql(sliceDateIssue));
        }
        if ((sliceDateIssue = issuePlugin.onlySingleData(sliceList)) != null) {
            flag = Global.YES_INT;

            sliceDateIssue.setId(IdGen.getUUID());
            sliceDateIssue.setGpsNo(deviceNo);
            sliceDateIssue.setWorkDate(workDate);
            sliceDateIssue.setInsertTime(new Date());

            SqlRunner.insert(BoundSqlBuilder.buildInsertBoundSql(sliceDateIssue));
        }
        if ((sliceDateIssue = issuePlugin.runOffCountLessThan0(sliceList)) != null) {
            flag = Global.YES_INT;

            sliceDateIssue.setId(IdGen.getUUID());
            sliceDateIssue.setGpsNo(deviceNo);
            sliceDateIssue.setWorkDate(workDate);
            sliceDateIssue.setInsertTime(new Date());

            SqlRunner.insert(BoundSqlBuilder.buildInsertBoundSql(sliceDateIssue));
        }
        if ((sliceDateIssue = issuePlugin.durationLessThan0(sliceList)) != null) {
            flag = Global.YES_INT;

            sliceDateIssue.setId(IdGen.getUUID());
            sliceDateIssue.setGpsNo(deviceNo);
            sliceDateIssue.setWorkDate(workDate);
            sliceDateIssue.setInsertTime(new Date());

            SqlRunner.insert(BoundSqlBuilder.buildInsertBoundSql(sliceDateIssue));
        }
        if ((sliceDateIssue = issuePlugin.durationOver24(sliceList)) != null) {
            flag = Global.YES_INT;

            sliceDateIssue.setId(IdGen.getUUID());
            sliceDateIssue.setGpsNo(deviceNo);
            sliceDateIssue.setWorkDate(workDate);
            sliceDateIssue.setInsertTime(new Date());

            SqlRunner.insert(BoundSqlBuilder.buildInsertBoundSql(sliceDateIssue));
        }
        if ((sliceDateIssue = issuePlugin.emptyData(sliceList)) != null) {
            flag = Global.YES_INT;

            sliceDateIssue.setId(IdGen.getUUID());
            sliceDateIssue.setGpsNo(deviceNo);
            sliceDateIssue.setWorkDate(workDate);
            sliceDateIssue.setInsertTime(new Date());

            SqlRunner.insert(BoundSqlBuilder.buildInsertBoundSql(sliceDateIssue));
        }

        if (flag == Global.YES_INT) {
            return null;
        }

//        if(sliceList == null || sliceList.isEmpty() || sliceList.size() == 1) {
//            return null;
//        }

        return moreRecord(sliceList, deviceNo, workDate);
    }

    //多条回传数据处理
    private LgDeviceDateStatics moreRecord(List<Map<String, Object>> sliceList, String deviceNo, Date workDate) {

        Map<String, Object> firstMap = sliceList.get(0);    //当天第一条记录
        Map<String, Object> lastMap = sliceList.get(sliceList.size() - 1); //当天最后一条记录

        //工作时长为 = (最后一条记录的总累计时长 - 第一条记录的总累计时长) x 3
        //int startDurationTotal = KmxConvert.verifyDuration(firstMap.get(SensorNameEnum.TOTAL_DURATION.getSensorName()));
        //int endDurationTotal = KmxConvert.verifyDuration(lastMap.get(SensorNameEnum.TOTAL_DURATION.getSensorName()));

        int startDurationTotal = StringUtils.toInteger(firstMap.get(SensorNameEnum.TOTAL_DURATION.getSensorName()));
        int endDurationTotal = StringUtils.toInteger(lastMap.get(SensorNameEnum.TOTAL_DURATION.getSensorName()));

        int sliceRunDuration = KmxConvert.convertDuration(endDurationTotal - startDurationTotal);
        int durationTotal = KmxConvert.convertDuration(endDurationTotal);

        //开机次数为 最后一条记录的总开机次数 - 第一条记录的总开机次数
        int startRunOffCount = StringUtils.toInteger(firstMap.get(SensorNameEnum.START_TIMES.getSensorName()));
        int endRunOffCount = StringUtils.toInteger(lastMap.get(SensorNameEnum.START_TIMES.getSensorName()));

        int runOffCount = endRunOffCount - startRunOffCount;

        //当天有开机次数 则认为当天有工作 , 1-有工作 0-无工作
        int workCount = runOffCount > 0 ? 1 : 0;

        Date workBegin = (Date)firstMap.get("iso");
        Date workStop = (Date)lastMap.get("iso");

        int rotationlSpeedMax = 0;
        for(Map<String, Object> sliceMap : sliceList) {
            int engin_rotate = StringUtils.toInteger(sliceMap.get(SensorNameEnum.ENGIN_ROTAT.getSensorName()));
            rotationlSpeedMax = Math.max(rotationlSpeedMax, engin_rotate);
        }



//        double srcLatitude = StringUtils.toDouble(lastMap.get(SensorNameEnum.LATITUDE_NUM.getSensorName()));
//        double srcLongitude = StringUtils.toDouble(lastMap.get(SensorNameEnum.LONGITUDE_NUM.getSensorName()));
//        MapCoord mapCoord = MapCoordUtils.nvr2bd(srcLatitude, srcLongitude);
//
//        String lastCity = lastMap.get(SensorNameEnum.CITY.getSensorName()).toString();
//        lastCity = ("-".equals(lastCity) || StringUtils.isBlank(lastCity)) ? null : lastCity;
//        String lastProvince = lastMap.get(SensorNameEnum.PROVINCE.getSensorName()).toString();
//        lastProvince = ("-".equals(lastProvince) || StringUtils.isBlank(lastProvince)) ? null : lastProvince;
//        String lastAddress = lastMap.get(SensorNameEnum.ADDRESS.getSensorName()).toString();
//        lastAddress = ("-".equals(lastAddress) || StringUtils.isBlank(lastAddress)) ? null : lastAddress;


        double srcLatitude = 0d;
        double srcLongitude = 0d;
        String lastCity = "";
        String lastProvince = "";
        String lastAddress = "";

        Map<String, Object> dataMap = sliceList.get(sliceList.size() - 1);

        srcLatitude = StringUtils.toDouble(dataMap.get(SensorNameEnum.LATITUDE_NUM.getSensorName()));
        srcLongitude = StringUtils.toDouble(dataMap.get(SensorNameEnum.LONGITUDE_NUM.getSensorName()));

        lastCity = dataMap.get(SensorNameEnum.CITY.getSensorName()).toString();
        lastCity = ("-".equals(lastCity) || StringUtils.isBlank(lastCity)) ? null : lastCity;
        lastProvince = dataMap.get(SensorNameEnum.PROVINCE.getSensorName()).toString();
        lastProvince = ("-".equals(lastProvince) || StringUtils.isBlank(lastProvince)) ? null : lastProvince;
        lastAddress = dataMap.get(SensorNameEnum.ADDRESS.getSensorName()).toString();
        lastAddress = ("-".equals(lastAddress) || StringUtils.isBlank(lastAddress)) ? null : lastAddress;

        MapCoord mapCoord = MapCoordUtils.nvr2bd(srcLatitude, srcLongitude);



        LgDeviceDateStatics lgDeviceDateStatics = new LgDeviceDateStatics();

        lgDeviceDateStatics.setWorkCount(workCount);
        lgDeviceDateStatics.setDeviceNo(deviceNo);
        lgDeviceDateStatics.setWorkDate(workDate);
        lgDeviceDateStatics.setWorkBegin(workBegin);
        lgDeviceDateStatics.setWorkStop(workStop);
        lgDeviceDateStatics.setSliceRunDuration(sliceRunDuration);
        lgDeviceDateStatics.setSliceWorkDuration(sliceRunDuration);
        lgDeviceDateStatics.setRunoffCount(runOffCount);
        lgDeviceDateStatics.setRunDurationTotal(durationTotal);
        lgDeviceDateStatics.setWorkDurationTotal(durationTotal);
        lgDeviceDateStatics.setRunOffTotal(endRunOffCount);
        lgDeviceDateStatics.setOilSum(0d);
        lgDeviceDateStatics.setOilAvg(0d);
        lgDeviceDateStatics.setRotationlSpeedMax(rotationlSpeedMax);
        lgDeviceDateStatics.setLatitude(mapCoord.getLatitude());
        lgDeviceDateStatics.setLongitude(mapCoord.getLongitude());
        lgDeviceDateStatics.setSrcLatitude(srcLatitude);
        lgDeviceDateStatics.setSrcLongitude(srcLongitude);
        lgDeviceDateStatics.setCity(lastCity);
        lgDeviceDateStatics.setProvince(lastProvince);
        lgDeviceDateStatics.setAddress(lastAddress);
        lgDeviceDateStatics.setAlarmCount(0);

        return lgDeviceDateStatics;
    }



    /**
     * 按周统计 从按天统计中取数据
     *
     * @param deviceNo 设备编码
     * @param year 年
     * @param week 周
     * @return 影响数据条数
     */
    public int saveDeviceWeekStatics(String deviceNo, int year, int week) {
        LgDeviceStatics entity = new LgDeviceStatics();
        String weekBegin = CalendarUtils.getYearWeekFirstDay(year, week);
        String weekEnd = CalendarUtils.getYearWeekEndDay(year, week);
        entity.setDeviceNo(deviceNo);
        entity.setDateBegin(DateUtils.parseDate(weekBegin));
        entity.setDateEnd(DateUtils.parseDate(weekEnd));
        entity.setWorkDateId(CalendarUtils.genWeekNumber(year, week));
        entity.setWorkDateName(CalendarUtils.genWeekName(year, week));

        return SqlRunner.insert(SliceSqlProvider.insertWeekSlice(entity));
    }

    /**
     * 按月统计 从按天统计中取数据
     *
     * @param deviceNo 设备编码
     * @param year 年
     * @param month 月
     * @return 影响数据条数
     */
    public int saveDeviceMonthStatics(String deviceNo, int year, int month) {
        LgDeviceStatics entity = new LgDeviceStatics();
        String monthBegin = CalendarUtils.getYearMonthFirstDay(year, month);
        String monthEnd = CalendarUtils.getYearMonthEndDay(year, month);
        entity.setDeviceNo(deviceNo);
        entity.setDateBegin(DateUtils.parseDate(monthBegin));
        entity.setDateEnd(DateUtils.parseDate(monthEnd));
        entity.setWorkDateId(CalendarUtils.genMonthNumber(year, month));
        entity.setWorkDateName(CalendarUtils.genMonthName(year, month));
        return SqlRunner.insert(SliceSqlProvider.insertMonthSlice(entity));
    }

    /**
     * 按季度统计 从按月统计中取数据
     *
     * @param deviceNo 设备编码
     * @param year 年
     * @param quarter 季度
     * @return 影响数据条数
     */
    public int saveDeviceSeasonStatics(String deviceNo, int year, int quarter) {
        LgDeviceStatics entity = new LgDeviceStatics();
        int beginWorkId = CalendarUtils.getYearQuarterFirstMonthNumber(year, quarter);
        int endWorkId = CalendarUtils.getYearQuarterEndMonthNumber(year, quarter);
        entity.setDeviceNo(deviceNo);
        entity.setBeginWorkId(beginWorkId);
        entity.setEndWorkId(endWorkId);
        entity.setWorkDateId(CalendarUtils.genQuarterNumber(year, quarter));
        entity.setWorkDateName(CalendarUtils.genQuarterName(year, quarter));
        return SqlRunner.insert(SliceSqlProvider.insertSeasonSlice(entity));
    }

    /**
     * 按年统计 从按季度统计中取数据
     *
     * @param deviceNo 设备编码
     * @param year 年
     * @return 影响数据条数
     */
    public int saveDeviceYearStatics(String deviceNo, int year) {
        LgDeviceStatics entity = new LgDeviceStatics();
        int beginWorkId = CalendarUtils.getYearFirstQuarterNumber(year);
        int endWorkId = CalendarUtils.getYearEndQuarterNumber(year);
        entity.setDeviceNo(deviceNo);
        entity.setBeginWorkId(beginWorkId);
        entity.setEndWorkId(endWorkId);
        entity.setWorkDateId(CalendarUtils.genYearNumber(year));
        entity.setWorkDateName(CalendarUtils.genYearName(year));
        return SqlRunner.insert(SliceSqlProvider.insertYearSlice(entity));
    }

}
