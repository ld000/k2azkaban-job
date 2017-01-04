/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.k2data.platform.domain;

import java.util.Date;

/**
 * 设备切片统计Entity
 * @author chenjingsi
 * @version 2016-05-20
 */
public class LgDeviceSliceStatics {
    private static final long serialVersionUID = 1L;

    private String id;
    private Date insertTime;        // 写入时间
    private String deviceNo;        // 整机对外发布的编码
    private Integer sliceCount;        // 设备切片统计
    private Date sliceStart;        // 开始时间
    private Date sliceStop;        // 结束时间
    private Integer sliceRunDuration;        // 开机时长（分钟）
    private Integer sliceWorkDuration;        // 工作时长（分钟）
    private Double latitude;        // 纬度（切片的最后时刻）
    private Double longitude;        // 经度
    private Double srcLatitude;    // 北谷gps原始纬度（切片的最后时刻）
    private Double srcLongitude;        // 北谷gps原始经度
    private String province;        // 省份
    private String city;        // 城市
    private String address;        // 详细地址
    private String position;        // 行政位置
    private Integer alarmCount;        //报警次数

    private Integer startTimes; //开机次数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Integer getSliceCount() {
        return sliceCount;
    }

    public void setSliceCount(Integer sliceCount) {
        this.sliceCount = sliceCount;
    }

    public Date getSliceStart() {
        return sliceStart;
    }

    public void setSliceStart(Date sliceStart) {
        this.sliceStart = sliceStart;
    }

    public Date getSliceStop() {
        return sliceStop;
    }

    public void setSliceStop(Date sliceStop) {
        this.sliceStop = sliceStop;
    }

    public Integer getSliceRunDuration() {
        return sliceRunDuration;
    }

    public void setSliceRunDuration(Integer sliceRunDuration) {
        this.sliceRunDuration = sliceRunDuration;
    }

    public Integer getSliceWorkDuration() {
        return sliceWorkDuration;
    }

    public void setSliceWorkDuration(Integer sliceWorkDuration) {
        this.sliceWorkDuration = sliceWorkDuration;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSrcLatitude() {
        return srcLatitude;
    }

    public void setSrcLatitude(Double srcLatitude) {
        this.srcLatitude = srcLatitude;
    }

    public Double getSrcLongitude() {
        return srcLongitude;
    }

    public void setSrcLongitude(Double srcLongitude) {
        this.srcLongitude = srcLongitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(Integer alarmCount) {
        this.alarmCount = alarmCount;
    }

    public Integer getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(Integer startTimes) {
        this.startTimes = startTimes;
    }
}