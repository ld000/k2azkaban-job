/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.k2data.platform.domain;

import java.util.Date;

/**
 * 整机与GPS设备对照Entity
 * @author chenjingsi
 * @version 2016-06-03
 */
public class LgMachineGpsContrast {
    
    private static final long serialVersionUID = 1L;

    private String id;
    private String deviceNo;        // 车辆编号
    private String gpsNo;        // 控制器编码
    private String gpsTemplateNo;        // GPS传感器模板编号
    private Integer isValid;        // 有效标志（0无效1有效）
    private Date buyDate;//购买日期
    private String org;//所属组织
    private Date installDate;//安装日期
    private String engineType;//发动机类型
    private Date runTime;//启用时间
    private Date stopTime;//停用时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getGpsNo() {
        return gpsNo;
    }

    public void setGpsNo(String gpsNo) {
        this.gpsNo = gpsNo;
    }

    public String getGpsTemplateNo() {
        return gpsTemplateNo;
    }

    public void setGpsTemplateNo(String gpsTemplateNo) {
        this.gpsTemplateNo = gpsTemplateNo;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public Date getInstallDate() {
        return installDate;
    }

    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public Date getRunTime() {
        return runTime;
    }

    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }
}