package com.k2data.platform.nvr.domain;

/**
 * 获取接口设备信息的类
 * @author chenjingsi
 * @version 2016-05-09
 */
public class NvrMachineContrastContent {
    @Override
    public String toString() {
        return "NvrMachineContrastContent{" +
                "licenseid='" + licenseid + '\'' +
                ", devicenum='" + devicenum + '\'' +
                ", buytime='" + buytime + '\'' +
                ", orgid='" + orgid + '\'' +
                ", enginetype='" + enginetype + '\'' +
                ", id='" + id + '\'' +
                ", reportType='" + reportType + '\'' +
                ", statDate='" + statDate + '\'' +
                ", recordTime='" + recordTime + '\'' +
                ", statType='" + statType + '\'' +
                ", statSubtype='" + statSubtype + '\'' +
                ", statDesc='" + statDesc + '\'' +
                ", num='" + num + '\'' +
                '}';
    }

    private String licenseid;
    private String devicenum;
    private String buytime;
    private String orgid;
    private String enginetype;

    private String id;
    private String reportType;
    private String statDate;
    private String recordTime;
    private String statType;
    private String statSubtype;
    private String statDesc;
    private String num;

    public String getLicenseid() {
        return licenseid;
    }
    public void setLicenseid(String licenseid) {
        this.licenseid = licenseid;
    }
    public String getDevicenum() {
        return devicenum;
    }
    public void setDevicenum(String devicenum) {
        this.devicenum = devicenum;
    }
    public String getBuytime() {
        return buytime;
    }
    public void setBuytime(String buytime) {
        this.buytime = buytime;
    }
    public String getOrgid() {
        return orgid;
    }
    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }
    public String getEnginetype() {
        return enginetype;
    }
    public void setEnginetype(String enginetype) {
        this.enginetype = enginetype;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getReportType() {
        return reportType;
    }
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    public String getStatDate() {
        return statDate;
    }
    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }
    public String getRecordTime() {
        return recordTime;
    }
    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
    public String getStatType() {
        return statType;
    }
    public void setStatType(String statType) {
        this.statType = statType;
    }
    public String getStatSubtype() {
        return statSubtype;
    }
    public void setStatSubtype(String statSubtype) {
        this.statSubtype = statSubtype;
    }
    public String getStatDesc() {
        return statDesc;
    }
    public void setStatDesc(String statDesc) {
        this.statDesc = statDesc;
    }
    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }

}