/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.k2data.platform.domain;

import java.util.Date;

/**
 * 设备工作统计Entity
 * @author wangshengguo
 * @version 2016-07-13
 */
public class LgDeviceStatics {

    private static final long serialVersionUID = 1L;

    private String id;
    private String deviceNo;        //设备号
    private int workDateId;            //workId，比如workYearId ，workMonthId，时间维度表lg_dimdate
    private String workDateName;    //workDateId对应名称
    private int beginWorkId;        //workDateId起始查询条件 between beginWorkId and endWorkId
    private int endWorkId;            //workDateId结束查询条件  
    private Date dateBegin;            //workDate 开始查询条件，针对统计周和月的情况需要按日期统计
    private Date dateEnd;

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
    public int getWorkDateId() {
        return workDateId;
    }
    public void setWorkDateId(int workDateId) {
        this.workDateId = workDateId;
    }
    public String getWorkDateName() {
        return workDateName;
    }
    public void setWorkDateName(String workDateName) {
        this.workDateName = workDateName;
    }
    public int getBeginWorkId() {
        return beginWorkId;
    }
    public void setBeginWorkId(int beginWorkId) {
        this.beginWorkId = beginWorkId;
    }
    public int getEndWorkId() {
        return endWorkId;
    }
    public void setEndWorkId(int endWorkId) {
        this.endWorkId = endWorkId;
    }
    public Date getDateBegin() {
        return dateBegin;
    }
    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }
    public Date getDateEnd() {
        return dateEnd;
    }
    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
    
    
}