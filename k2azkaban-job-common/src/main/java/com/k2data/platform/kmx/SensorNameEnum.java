/**
 * Copyright &copy; 2016 <a href="https://www.k2data.com.cn">K2DATA</a> All rights reserved.
 */
package com.k2data.platform.kmx;

/**
 * 传感器列表 Enum
 * @author K2DATA.wsguo
 * @date Jul 25, 2016 9:23:14 AM    
 * 
 */
public enum SensorNameEnum {

    ACC_STATUS("accStatus"),
    COMM_STATUS("commStatus"),
    ELEC_STATUS("elecStatus"),
    ENGIN_ROTAT("enginRotate"),
    ENGINE_TEMPERATURE("engineTemperature"),
    GPRS_SIGNAL("gprsSignal"),
    GPS_POWER_STATUS("gpsPowerStatus"),
    GPS_SPEED("gpsSpeed"),
    GSM_SIGNAL("gsmSignal"),
    IMITATE1("imitate1"),
    IMITATE2("imitate2"),
    LATITUDE_NAME("latitudeName"),
    LATITUDE_NUM("latitudeNum"),
    LED_STATUS("ledStatus"),
    LOCATE_DATE("locateDate"),
    LOCATE_STATUS("locateStatus"),
    LOCK_STATUS("lockStatus"),
    LOCKER_STATUS("lockerStatus"),
    LONGITUDE_NAME("longitudeName"),
    LONGITUDE_NUM("longitudeNum"),
    OIL_LEVEL("oilLevel"),
    OIL_TEMPERATURE("oilTemperature"),
    POWER_TYPE("powerType"),
    PRESSURE_METER("pressureMeter"),
    REALTIME_DURATION("realtimeDuration"),
    RECORD_TIME("recordTime"),
    START_TIMES("startTimes"),
    TOTAL_DURATION("totalDuration"),
    WIRE_STATUS("wireStatus"),
    WORK_MONITOR("workMonitor"),
    ADDRESS("address"),
    AMAPLATITUDE_NUM("amaplatitudeNum"),
    AMAPLONGITUDE_NUM("amaplongitudeNum"),
    CITY("city"),
    CITYCODE("citycode"),
    PROVINCE("province"),
    LOCALTION_WARNING("localtionWarning"),
    VALID_ALARM("validAlarm"),
    TOTAL_MILEAGE("totalMileage");

    private SensorNameEnum(String sensorName) {
        this.sensorName = sensorName;
    }
    
    private String sensorName;    //传感器名称

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }


}
