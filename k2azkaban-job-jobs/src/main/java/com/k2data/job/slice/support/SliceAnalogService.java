//package com.k2data.job.slice.support;
//
//import com.google.common.collect.Lists;
//
//import java.util.*;
//import java.util.Map.Entry;
//
///**
// * 切片模拟量 service
// */
//public class SliceAnalogService {
//
//    /**
//     * 获取切片模拟量数据
//     *
//     * @param deviceNo 设备编号
//     * @param start 开始时间
//     * @param end 结束时间
//     */
//    public void sliceAnalogData(final String deviceNo, final Date start, final Date end) {
//        Map<String,Object> params = new HashMap<String,Object>();
//        params.put("deviceNo", deviceNo);
//        params.put("startTime", start);
//        params.put("endTime", end);
//
//        List<LgDeviceSliceStatics> sliceStatics = lgDeviceSliceStaticsDao.getSliceInfo(params);
//
//        List<String> sensorMarkList = lgGpsTemplateDetailDao.getSensorMarkList(deviceNo, Global.ANALOG_SENSOR_TYPE);
//
//        for (LgDeviceSliceStatics deviceSliceStatics : sliceStatics) {
//            getAnalogValueList(deviceSliceStatics, sensorMarkList);
//        }
//    }
//
//    private void getAnalogValueList(final LgDeviceSliceStatics lgDeviceSliceStatics, final List<String> sensorMarkList){
//        final List<String> sensorMarks = new ArrayList<String>();
//        final Map<String, List<String>> sensors = new HashMap<String, List<String>>();
//        for(String sensorMark : sensorMarkList) {
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
//
//        for (LgSliceDataRows dataRow : rsp.getDataRows()) {
//            List<LgSliceDataRowsPoints> dataPoints = dataRow.getDataPoints();
//            for (LgSliceDataRowsPoints rowsPoint : dataPoints) {
//                sensors.get(rowsPoint.getSensor()).add(rowsPoint.getValue().toString());
//            }
//        }
//
//        insertAnalogValue(lgDeviceSliceStatics, sensors);
//    }
//
//    /**
//     * 计算各字段值后, 插入数据库
//     *
//     * @param lgDeviceSliceStatics 切片实体类
//     * @param sensors 传感器名字和值对照
//     */
//    private void insertAnalogValue(final LgDeviceSliceStatics lgDeviceSliceStatics, final Map<String, List<String>> sensors){
//        List<LgSliceStaticsAnalog> analogs = new ArrayList<LgSliceStaticsAnalog>();
//
//        for (Entry<String, List<String>> entry : sensors.entrySet()) {
//            analogs.add(generateAnalogObject(lgDeviceSliceStatics, entry));
//        }
//
//        lgSliceStaticsAnalogDao.batchInsert(analogs);
//    }
//
//    public LgSliceStaticsAnalog generateAnalogObject(final LgDeviceSliceStatics lgDeviceSliceStatics, final Entry<String, List<String>> sensor) {
//        String sensorMark = sensor.getKey();
//        List<String> sourceValueList = sensor.getValue();
//
//        // 模拟量转成Double类型
//        List<Double> valueList = Lists.newArrayList();
//        for (String value : sourceValueList) {
//            valueList.add(Double.parseDouble(value));
//        }
//
//        Date sliceStart = lgDeviceSliceStatics.getSliceStart();
//        Date sliceEnd = lgDeviceSliceStatics.getSliceStop();
//
//        Integer runDurationMin = (int) DateUtils.getPassedMinBetweenDate(sliceStart, sliceEnd);
//
//        LgSliceStaticsAnalog lgSliceStaticsAnalog = new LgSliceStaticsAnalog();
//        lgSliceStaticsAnalog.setId(IdGen.uuid());
//        lgSliceStaticsAnalog.setDeviceSliceId(lgDeviceSliceStatics.getId());
//        lgSliceStaticsAnalog.setInsertTime(new Date());//写入时间
//        lgSliceStaticsAnalog.setDeviceNo(lgDeviceSliceStatics.getDeviceNo());//设备号
//        lgSliceStaticsAnalog.setSensorType(Global.ANALOG_SENSOR_TYPE);
//        lgSliceStaticsAnalog.setSensorNo(sensorMark);
//        lgSliceStaticsAnalog.setDataPointCount(valueList.size());//回传次数为查询结果的长度
//        lgSliceStaticsAnalog.setSensorDataStart(sliceStart);
//        lgSliceStaticsAnalog.setSensorDataEnd(sliceEnd);
//        lgSliceStaticsAnalog.setSensorDataDuration(runDurationMin);
//        lgSliceStaticsAnalog.setSendFrequence(runDurationMin == 0 ? 0 : ((double)valueList.size() / (double)runDurationMin));
//
//        Map<String, Double> returnMap = MathUtils.computeVariance(valueList, 1);//获取具体需要的值
//        lgSliceStaticsAnalog.setValueMax(returnMap.get(MathUtils.MAX_VALUE));
//        lgSliceStaticsAnalog.setValueMin(returnMap.get(MathUtils.MIN_VALUE));
//        lgSliceStaticsAnalog.setValueAvg(returnMap.get(MathUtils.AVERAGE));
//        lgSliceStaticsAnalog.setStandardStd(returnMap.get(MathUtils.STD_VARIANCE));
//        lgSliceStaticsAnalog.setVarianceStd(returnMap.get(MathUtils.VARIANCE));
//
//        lgSliceStaticsAnalog.setValue5(MathUtils.computePercent(valueList, 5, 1));
//        lgSliceStaticsAnalog.setValue25(MathUtils.computePercent(valueList, 25, 1));
//        lgSliceStaticsAnalog.setValue50(MathUtils.computePercent(valueList, 50, 1));
//        lgSliceStaticsAnalog.setValue75(MathUtils.computePercent(valueList, 75, 1));
//        lgSliceStaticsAnalog.setValue95(MathUtils.computePercent(valueList, 95, 1));
//
//        //梯度处理
//        List<Double> gradList = MathUtils.getGradient(valueList);//获取梯度值列表
//        Map<String, Double> gradMap = MathUtils.computeVariance(gradList, 1);//获取梯度具体需要的值
//        lgSliceStaticsAnalog.setGradMax(gradMap.get(MathUtils.MAX_VALUE));
//        lgSliceStaticsAnalog.setGradMin(gradMap.get(MathUtils.MIN_VALUE));
//        lgSliceStaticsAnalog.setGradAvg(gradMap.get(MathUtils.AVERAGE));
//        lgSliceStaticsAnalog.setGrad5(MathUtils.computePercent(gradList, 5, 1));
//        lgSliceStaticsAnalog.setGrad25(MathUtils.computePercent(gradList, 25, 1));
//        lgSliceStaticsAnalog.setGrad50(MathUtils.computePercent(gradList, 50, 1));
//        lgSliceStaticsAnalog.setGrad75(MathUtils.computePercent(gradList, 75, 1));
//        lgSliceStaticsAnalog.setGrad95(MathUtils.computePercent(gradList, 95, 1));
//
//        return lgSliceStaticsAnalog;
//    }
//
//}
