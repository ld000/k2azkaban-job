/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.k2data.platform.domain;

import javax.persistence.Table;
import java.util.Date;

/**
 * 机器最新信息Entity
 * @author lidong
 * @version 2016-08-19
 */
@Table(name = "lgMachineLatestInfo")
public class MachineLatestInfo {

    private String id;
    private String deviceNo;        // 车辆编号
    private Double latitude;        // 纬度
    private Double longitude;        // 经度
    private String province;        // 省份
    private String city;        // 城市
    private String address;        // 地址
    private String position;        // 行政位置
    private Date sliceStop;        // 最后有位置数据切片结束时间
    
    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
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
    
    public Date getSliceStop() {
        return sliceStop;
    }

    public void setSliceStop(Date sliceStop) {
        this.sliceStop = sliceStop;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}