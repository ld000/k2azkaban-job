/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.k2data.platform.domain;

import java.util.Calendar;
import java.util.Date;

/**
 * 每日切片错误数据Entity
 *
 * @author lidong 2016-11-24
 */
public class SliceDateIssue {
    
    private static final long serialVersionUID = 1L;

    private String id;
    private String gpsNo;        // 设备号
    private Date workDate;        // 日期
    private Integer issueType;        // 异常类型
    private String issueValue;        // 异常值
    private Date insertTime;        // 插入时间
    private Integer handleFlag;        // 是否已处理
    private String handler;        // 处理人
    private Date handleTime;        // 处理时间
    private String remark;        // 备注

    private String deviceNo;
    private Integer deviceNoType; //deviceNo 来源类型，1-整机档案，2-gpsNo对照表

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGpsNo() {
        return gpsNo;
    }

    public void setGpsNo(String gpsNo) {
        this.gpsNo = gpsNo;
    }
    
    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }
    
    public String getIssueValue() {
        return issueValue;
    }

    public void setIssueValue(String issueValue) {
        this.issueValue = issueValue;
    }
    
    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    
    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIssueType() {
        return issueType;
    }

    public void setIssueType(Integer issueType) {
        this.issueType = issueType;
    }

    public Integer getHandleFlag() {
        return handleFlag;
    }

    public void setHandleFlag(Integer handleFlag) {
        this.handleFlag = handleFlag;
    }

    public Integer getDeviceNoType() {
        return deviceNoType;
    }

    public void setDeviceNoType(Integer deviceNoType) {
        this.deviceNoType = deviceNoType;
    }
}