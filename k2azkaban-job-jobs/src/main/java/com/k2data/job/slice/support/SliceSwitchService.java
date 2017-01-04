//package com.k2data.job.slice.support;
//
//import com.k2data.platform.common.config.Global;
//import com.k2data.platform.common.service.CrudService;
//import com.k2data.platform.common.utils.DateUtils;
//import com.k2data.platform.common.utils.IdGen;
//import com.k2data.platform.modules.lg.common.kmx.KmxClient;
//import com.k2data.platform.modules.lg.common.kmx.cond.KmxDataRowsV3Cond;
//import com.k2data.platform.modules.lg.common.kmx.domain.LgSliceDataRows;
//import com.k2data.platform.modules.lg.common.kmx.domain.LgSliceDataRowsPoints;
//import com.k2data.platform.modules.lg.common.kmx.domain.LgSliceDataRowsRsp;
//import com.k2data.platform.modules.lg.common.utils.MathUtils;
//import com.k2data.platform.modules.lg.dao.LgDeviceSliceStaticsDao;
//import com.k2data.platform.modules.lg.dao.LgGpsTemplateDetailDao;
//import com.k2data.platform.modules.lg.dao.LgSliceStaticsSwitchDao;
//import com.k2data.platform.modules.lg.entity.LgDeviceSliceStatics;
//import com.k2data.platform.modules.lg.entity.LgSliceStaticsSwitch;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
///**
// * 切片开关量 service
// */
//@Service
//public class SliceSwitchService extends CrudService<LgSliceStaticsSwitchDao, LgSliceStaticsSwitch> {
//
//    @Autowired
//    private LgDeviceSliceStaticsDao lgDeviceSliceStaticsDao;
//    @Autowired
//    private LgGpsTemplateDetailDao lgGpsTemplateDetailDao;
//    @Autowired
//    private LgSliceStaticsSwitchDao lgSliceStaticsSwitchDao;
//
//    @Transactional
//    public void sliceSwitchData(final String deviceNo, final Date start, final Date end) {
//        Map<String,Object> params = new HashMap<String,Object>();
//        params.put("deviceNo", deviceNo);
//        params.put("startTime", start);
//        params.put("endTime", end);
//
//        List<LgDeviceSliceStatics> sliceStatics = lgDeviceSliceStaticsDao.getSliceInfo(params);
//
//        List<String> sensorMarkList = lgGpsTemplateDetailDao.getSensorMarkList(deviceNo, Global.SWITCH_SENSOR_TYPE);
//
//        for (LgDeviceSliceStatics deviceSliceStatics : sliceStatics) {
//            getSwitchValueList(deviceSliceStatics, sensorMarkList);
//        }
//    }
//
//    private void getSwitchValueList(final LgDeviceSliceStatics lgDeviceSliceStatics, final List<String> sensorMarkList) {
//        final List<String> sensorMarks = new ArrayList<String>();
//        final Map<String, List<String>> sensors = new HashMap<String, List<String>>();
//        for(String sensorMark : sensorMarkList){
//            sensorMarks.add(sensorMark);
//            sensors.put(sensorMark, new ArrayList<>());
//        }
//
//        KmxDataRowsV3Cond cond = new KmxDataRowsV3Cond();
//        cond.setDeviceNo(lgDeviceSliceStatics.getDeviceNo());
//        cond.setSensors(sensorMarks.toArray(new String[sensorMarks.size()]));
//        cond.setStart(lgDeviceSliceStatics.getSliceStart());
//        cond.setStop(lgDeviceSliceStatics.getSliceStop());
//        cond.setSize(KmxClient.PAGE_SIZE + "");
//
//        LgSliceDataRowsRsp rsp = KmxClient.getSync(cond);
//        for (LgSliceDataRows dataRow : rsp.getDataRows()) {
//            List<LgSliceDataRowsPoints> dataPoints = dataRow.getDataPoints();
//            for (LgSliceDataRowsPoints rowsPoint : dataPoints) {
//                sensors.get(rowsPoint.getSensor()).add(rowsPoint.getValue().toString());
//            }
//        }
//
//        insertSwitchValue(lgDeviceSliceStatics, sensors);
//    }
//
//    @Transactional
//    private void insertSwitchValue(final LgDeviceSliceStatics lgDeviceSliceStatics, final Map<String, List<String>> sensors){
//        List<LgSliceStaticsSwitch> switchList = new ArrayList<LgSliceStaticsSwitch>();
//
//        for (Map.Entry<String, List<String>> entry : sensors.entrySet()) {
//
//
//
//
//
//            switchList.add(generateSwitchObject(lgDeviceSliceStatics, entry));
//        }
//
//        lgSliceStaticsSwitchDao.batchInsert(switchList);
//    }
//
//    @SuppressWarnings("unchecked")
//    public LgSliceStaticsSwitch generateSwitchObject(final LgDeviceSliceStatics lgDeviceSliceStatics, final Map.Entry<String, List<String>> sensor) {
//        String sensorMark = sensor.getKey();
//        List<String> valueList = sensor.getValue();
//
//        Date sliceStart = lgDeviceSliceStatics.getSliceStart();
//        Date sliceEnd = lgDeviceSliceStatics.getSliceStop();
//
//        Integer runDuration = (int) DateUtils.getPassedMinBetweenDate(sliceStart, sliceEnd);
//
//        LgSliceStaticsSwitch lgSliceStaticsSwitch = new LgSliceStaticsSwitch();
//        lgSliceStaticsSwitch.setId(IdGen.uuid());
//        lgSliceStaticsSwitch.setInsertTime(new Date());
//        lgSliceStaticsSwitch.setDeviceSliceId(lgDeviceSliceStatics.getId());
//        lgSliceStaticsSwitch.setDeviceNo(lgDeviceSliceStatics.getDeviceNo());
//        lgSliceStaticsSwitch.setSensorType(Global.SWITCH_SENSOR_TYPE);
//        lgSliceStaticsSwitch.setDataPointCount(valueList.size());
//        lgSliceStaticsSwitch.setSensorDataStart(sliceStart);
//        lgSliceStaticsSwitch.setSensorDataEnd(sliceEnd);
//        lgSliceStaticsSwitch.setSensorDataDuration(runDuration);
//        lgSliceStaticsSwitch.setSensorMark(sensorMark);
//        lgSliceStaticsSwitch.setSendFrequence(runDuration == 0 ? 0 : ((double)valueList.size() / (double)runDuration));
//
//        Map<String, Object> resultMap =  MathUtils.analysisStatusDataAndSwitchData(valueList);
//        Map<String, List<Integer>> statusMap = (Map<String, List<Integer>>) resultMap.get(MathUtils.STATUS_MAP);
//        Map<String, Integer> switchMap = (Map<String, Integer>) resultMap.get(MathUtils.SWITCH_MAP);
//
//        Double highCount = 0.0;
//        Double lowCount = 0.0;
//        List<Integer> lowCountList = statusMap.get(MathUtils.STATUS_01);
//        List<Integer> highCountList = statusMap.get(MathUtils.STATUS_10);
//        List<Integer> lowDurationList = new ArrayList<Integer>();
//        List<Integer> highDurationList = new ArrayList<Integer>();
//        for (Integer aLowCountList : lowCountList) {
//            lowDurationList.add(aLowCountList * 3);
//            lowCount += aLowCountList;
//        }
//        for (Integer aHighCountList : highCountList) {
//            highDurationList.add(aHighCountList * 3);
//            highCount += aHighCountList;
//        }
//        int lowToHigh = switchMap.get(MathUtils.STATUS_SWITCH_01_10);
//        int highToLow = switchMap.get(MathUtils.STATUS_SWITCH_10_01);
//        lgSliceStaticsSwitch.setChangeCount((double)(lowToHigh + highToLow));
//
//        Map<String, Double> highDurationReturnMap = MathUtils.computeVariance(highDurationList, 1);//获取具体需要的值
//        lgSliceStaticsSwitch.setHighCount(highCount);
//        lgSliceStaticsSwitch.setHighDurationMax(highDurationReturnMap.get(MathUtils.MAX_VALUE));
//        lgSliceStaticsSwitch.setHighDurationMin(highDurationReturnMap.get(MathUtils.MIN_VALUE));
//        lgSliceStaticsSwitch.setHighDurationAvg(highDurationReturnMap.get(MathUtils.AVERAGE));
//        lgSliceStaticsSwitch.setHighDuration5(MathUtils.computePercent(highDurationList, 5, 1));
//        lgSliceStaticsSwitch.setHighDuration25(MathUtils.computePercent(highDurationList, 25, 1));
//        lgSliceStaticsSwitch.setHighDuration50(MathUtils.computePercent(highDurationList, 50, 1));
//        lgSliceStaticsSwitch.setHighDuration75(MathUtils.computePercent(highDurationList, 75, 1));
//        lgSliceStaticsSwitch.setHighDuration95(MathUtils.computePercent(highDurationList, 95, 1));
//        lgSliceStaticsSwitch.setHighDurationStandardStd(highDurationReturnMap.get(MathUtils.STD_VARIANCE));
//        lgSliceStaticsSwitch.setHighDurationVarianceStd(highDurationReturnMap.get(MathUtils.VARIANCE));
//
//        Map<String, Double> highCountReturnMap = MathUtils.computeVariance(highCountList, 1);//获取具体需要的值
//        lgSliceStaticsSwitch.setHighMax(highCountReturnMap.get(MathUtils.MAX_VALUE));
//        lgSliceStaticsSwitch.setHighMin(highCountReturnMap.get(MathUtils.MIN_VALUE));
//        lgSliceStaticsSwitch.setHighAvg(highCountReturnMap.get(MathUtils.AVERAGE));
//        lgSliceStaticsSwitch.setHigh5(MathUtils.computePercent(highCountList, 5, 1));
//        lgSliceStaticsSwitch.setHigh25(MathUtils.computePercent(highCountList, 25, 1));
//        lgSliceStaticsSwitch.setHigh50(MathUtils.computePercent(highCountList, 50, 1));
//        lgSliceStaticsSwitch.setHigh75(MathUtils.computePercent(highCountList, 75, 1));
//        lgSliceStaticsSwitch.setHigh95(MathUtils.computePercent(highCountList, 95, 1));
//        lgSliceStaticsSwitch.setHighStandardStd(highCountReturnMap.get(MathUtils.STD_VARIANCE));
//        lgSliceStaticsSwitch.setHighVarianceStd(highCountReturnMap.get(MathUtils.VARIANCE));
//
//        Map<String, Double> lowDurationReturnMap = MathUtils.computeVariance(lowDurationList, 1);
//        lgSliceStaticsSwitch.setLowCount(lowCount);
//        lgSliceStaticsSwitch.setLowDurationMax(lowDurationReturnMap.get(MathUtils.MAX_VALUE));
//        lgSliceStaticsSwitch.setLowDurationMin(lowDurationReturnMap.get(MathUtils.MIN_VALUE));
//        lgSliceStaticsSwitch.setLowDurationAvg(lowDurationReturnMap.get(MathUtils.AVERAGE));
//        lgSliceStaticsSwitch.setLowDuration5(MathUtils.computePercent(lowDurationList, 5, 1));
//        lgSliceStaticsSwitch.setLowDuration25(MathUtils.computePercent(lowDurationList, 25, 1));
//        lgSliceStaticsSwitch.setLowDuration50(MathUtils.computePercent(lowDurationList, 50, 1));
//        lgSliceStaticsSwitch.setLowDuration75(MathUtils.computePercent(lowDurationList, 75, 1));
//        lgSliceStaticsSwitch.setLowDuration95(MathUtils.computePercent(lowDurationList, 95, 1));
//        lgSliceStaticsSwitch.setLowDurationStandardStd(lowDurationReturnMap.get(MathUtils.STD_VARIANCE));
//        lgSliceStaticsSwitch.setLowDurationVarianceStd(lowDurationReturnMap.get(MathUtils.VARIANCE));
//
//        Map<String, Double> lowCountReturnMap = MathUtils.computeVariance(lowCountList,1);
//        lgSliceStaticsSwitch.setLowMax(lowCountReturnMap.get(MathUtils.MAX_VALUE));
//        lgSliceStaticsSwitch.setLowMin(lowCountReturnMap.get(MathUtils.MIN_VALUE));
//        lgSliceStaticsSwitch.setLowAvg(lowCountReturnMap.get(MathUtils.AVERAGE));
//        lgSliceStaticsSwitch.setLow5(MathUtils.computePercent(lowCountList, 5, 1));
//        lgSliceStaticsSwitch.setLow25(MathUtils.computePercent(lowCountList, 25, 1));
//        lgSliceStaticsSwitch.setLow50(MathUtils.computePercent(lowCountList, 50, 1));
//        lgSliceStaticsSwitch.setLow75(MathUtils.computePercent(lowCountList, 75, 1));
//        lgSliceStaticsSwitch.setLow95(MathUtils.computePercent(lowCountList, 95, 1));
//        lgSliceStaticsSwitch.setLowStandardStd(lowCountReturnMap.get(MathUtils.STD_VARIANCE));
//        lgSliceStaticsSwitch.setLowVarianceStd(lowCountReturnMap.get(MathUtils.VARIANCE));
//
//        return lgSliceStaticsSwitch;
//    }
//
//}
