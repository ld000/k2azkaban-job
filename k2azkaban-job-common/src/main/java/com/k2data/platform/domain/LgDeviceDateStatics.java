/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.k2data.platform.domain;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 设备每日工作统计Entity
 * @author wangshengguo
 * @version 2016-05-30
 */
public class LgDeviceDateStatics {
    
    private static final long serialVersionUID = 1L;
    private String id;
    private Date insertTime;        // 写入时间
    private String deviceNo;        // 整机对外发布的编码
    private Date workDate;        // 工作日期
    private Integer workCount;        // 工作天数统计
    private Date workBegin;        // 当天初次开机时间
    private Date workStop;        // 当天最后一次工作结束时间
    private Integer sliceRunDuration;        // 开机时长
    private Integer sliceWorkDuration;        // 工作时长
    private Integer runoffCount;        // 当天开机次数

    private Integer runDurationTotal;    //累计开机时长
    private Integer workDurationTotal;    //累计工作时长
    private Integer runOffTotal;    //累计开机次数

    private Double oilSum;        // 总油耗
    private Double oilAvg;        // 平均油耗
    private Integer rotationlSpeedMax;        // 最高转速
    private Double latitude;        // 维度
    private Double longitude;        // 经度
    private Double srcLatitude;    // 北谷gps原始纬度（切片的最后时刻）
    private Double srcLongitude;        // 北谷gps原始经度
    private String province;        // 省份
    private String city;        // 城市
    private String address;        // 地址
    private String position;        // 行政位置
    private Integer alarmCount; //报警次数
    
    @Transient
    private String modelNumber;    // 型号
    @Transient
    private String orderNumber;    // 订货号
    @Transient
    private Integer inactiveCount; // 呆滞数量
    @Transient
    private String location;   // 位置
    @Transient
    private String saleUnit;   // 经销商
    @Transient
    private Date productDate;  // 生产时间
    @Transient
    private String[] machineTypeMult;  // 产品类型多选
    @Transient
    private String[] productTypeMult;  // 型号多选
    @Transient
    private String[] orderNumberMult;  // 订货号多选
    @Transient
    private String[] workProvinceMult; // 地区多选
    @Transient
    private String[] deviceNoMult;  // 设备号多选
    
    @Transient
    private List<String> batchNumberList; //试验批次号
    @Transient
    private Date dateBegin;                //试验开始时间
    @Transient
    private Date dateEnd;                //试验结束时间
    
    @Transient
    private Date productDateLess;   // 生产时间小于
    @Transient
    private Integer machineStatus;   // 机器状态
    @Transient
    private Date workDateGreaterOE;   // 工作时间大于等于
    
    @Transient
    private String machineType;    // 产品类型
    @Transient
    private String machineTypeName;    // 产品类型描述
    @Transient
    private Integer eventType;  // 大事件类别
    @Transient
    private String dataString;
    @Transient
    private String gpsNo;    //控制器编号
    @Transient
    private String batchNumber;    //试验批次号
    @Transient
    private Double sliceWorkDurationAvg;//平均工作时长
    private String saleUnitId;
    private String supplyId;
    private String saleYear;
    private String productType;
    private Date transportDate;
    private String transportUnitName;
    private String purchaserUnitName;
    private Date expectedDeliveryDate;
    private String carrier;
    private String carrierPhone;
    private String vehicleNo;
    private String purchaserAddress;
    private String purchaser;
    private String purchaserPhone;
    private Integer buyouted; // 是否买断
    private Integer invoiced; // 是否开票
    private Integer isLg;
    private Integer upkeepTimes;
    private Integer serviceTimes;
    private Integer workDays;
    
    private Integer limit;

    @Transient
    private Double workDurationHour;
    @Transient
    private Double expectDayDuration;
    @Transient
    private Double expectSumDuration;
    @Transient
    private Double workDurationTotalHour;
    @Transient
    private Integer sumWorkDuration;
    @Transient
    private Double sumWorkDurationHour;

    @Transient
    private String batchType;   //试验批次产品类型
    @Transient
    private String batchTypeName;   //试验批次产品类型
    @Transient
    private String doneType;        //试验车是否达标 1-达标 0-未达标
    @Transient
    private Integer unsale;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getWorkDays() {
        return workDays;
    }

    public void setWorkDays(Integer workDays) {
        this.workDays = workDays;
    }

    public Integer getUpkeepTimes() {
        return upkeepTimes;
    }

    public void setUpkeepTimes(Integer upkeepTimes) {
        this.upkeepTimes = upkeepTimes;
    }

    public Integer getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(Integer serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    public Integer getIsLg() {
        return isLg;
    }

    public void setIsLg(Integer isLg) {
        this.isLg = isLg;
    }

    public Integer getBuyouted() {
        return buyouted;
    }

    public void setBuyouted(Integer buyouted) {
        this.buyouted = buyouted;
    }

    public Integer getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(Integer invoiced) {
        this.invoiced = invoiced;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Date getTransportDate() {
        return transportDate;
    }

    public void setTransportDate(Date transportDate) {
        this.transportDate = transportDate;
    }

    public String getTransportUnitName() {
        return transportUnitName;
    }

    public void setTransportUnitName(String transportUnitName) {
        this.transportUnitName = transportUnitName;
    }

    public String getPurchaserUnitName() {
        return purchaserUnitName;
    }

    public void setPurchaserUnitName(String purchaserUnitName) {
        this.purchaserUnitName = purchaserUnitName;
    }

    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCarrierPhone() {
        return carrierPhone;
    }

    public void setCarrierPhone(String carrierPhone) {
        this.carrierPhone = carrierPhone;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getPurchaserAddress() {
        return purchaserAddress;
    }

    public void setPurchaserAddress(String purchaserAddress) {
        this.purchaserAddress = purchaserAddress;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    public String getPurchaserPhone() {
        return purchaserPhone;
    }

    public void setPurchaserPhone(String purchaserPhone) {
        this.purchaserPhone = purchaserPhone;
    }

    public String getSaleYear() {
        return saleYear;
    }

    public void setSaleYear(String saleYear) {
        this.saleYear = saleYear;
    }

    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }

    public String getSaleUnitId() {
        return saleUnitId;
    }

    public void setSaleUnitId(String saleUnitId) {
        this.saleUnitId = saleUnitId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
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
    
    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }
    
    public Integer getWorkCount() {
        return workCount;
    }

    public void setWorkCount(Integer workCount) {
        this.workCount = workCount;
    }

    public Date getWorkBegin() {
        return workBegin;
    }

    public void setWorkBegin(Date workBegin) {
        this.workBegin = workBegin;
    }
    
    public Date getWorkStop() {
        return workStop;
    }

    public void setWorkStop(Date workStop) {
        this.workStop = workStop;
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
    
    public Integer getRunoffCount() {
        return runoffCount;
    }

    public void setRunoffCount(Integer runoffCount) {
        this.runoffCount = runoffCount;
    }
    
    public Integer getRunDurationTotal() {
        return runDurationTotal;
    }

    public void setRunDurationTotal(Integer runDurationTotal) {
        this.runDurationTotal = runDurationTotal;
    }

    public Integer getWorkDurationTotal() {
        return workDurationTotal;
    }

    public void setWorkDurationTotal(Integer workDurationTotal) {
        this.workDurationTotal = workDurationTotal;
    }

    public Integer getRunOffTotal() {
        return runOffTotal;
    }

    public void setRunOffTotal(Integer runOffTotal) {
        this.runOffTotal = runOffTotal;
    }
    
    public Double getOilSum() {
        return oilSum;
    }

    public void setOilSum(Double oilSum) {
        this.oilSum = oilSum;
    }
    
    public Double getOilAvg() {
        return oilAvg;
    }

    public void setOilAvg(Double oilAvg) {
        this.oilAvg = oilAvg;
    }
    
    public Integer getRotationlSpeedMax() {
        return rotationlSpeedMax;
    }

    public void setRotationlSpeedMax(Integer rotationlSpeedMax) {
        this.rotationlSpeedMax = rotationlSpeedMax;
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

    public Integer getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(Integer alarmCount) {
        this.alarmCount = alarmCount;
    }

    public String[] getMachineTypeMult() {
        return machineTypeMult;
    }

    public void setMachineTypeMult(String[] machineTypeMult) {
        this.machineTypeMult = machineTypeMult;
    }

    public String[] getProductTypeMult() {
        return productTypeMult;
    }

    public void setProductTypeMult(String[] productTypeMult) {
        this.productTypeMult = productTypeMult;
    }

    public String[] getOrderNumberMult() {
        return orderNumberMult;
    }

    public void setOrderNumberMult(String[] orderNumberMult) {
        this.orderNumberMult = orderNumberMult;
    }

    public String[] getWorkProvinceMult() {
        return workProvinceMult;
    }

    public void setWorkProvinceMult(String[] workProvinceMult) {
        this.workProvinceMult = workProvinceMult;
    }

    public Date getProductDateLess() {
        return productDateLess;
    }

    public void setProductDateLess(Date productDateLess) {
        this.productDateLess = productDateLess;
    }

    public Integer getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(Integer machineStatus) {
        this.machineStatus = machineStatus;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getInactiveCount() {
        return inactiveCount;
    }

    public void setInactiveCount(Integer inactiveCount) {
        this.inactiveCount = inactiveCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }

    public Date getProductDate() {
        return productDate;
    }

    public void setProductDate(Date productDate) {
        this.productDate = productDate;
    }

    public Date getWorkDateGreaterOE() {
        return workDateGreaterOE;
    }

    public void setWorkDateGreaterOE(Date workDateGreaterOE) {
        this.workDateGreaterOE = workDateGreaterOE;
    }

    public String[] getDeviceNoMult() {
        return deviceNoMult;
    }

    public void setDeviceNoMult(String[] deviceNoMult) {
        this.deviceNoMult = deviceNoMult;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getMachineTypeName() {
        return machineTypeName;
    }

    public void setMachineTypeName(String machineTypeName) {
        this.machineTypeName = machineTypeName;
    }

    public String getGpsNo() {
        return gpsNo;
    }

    public void setGpsNo(String gpsNo) {
        this.gpsNo = gpsNo;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<String> getBatchNumberList() {
        return batchNumberList;
    }

    public void setBatchNumberList(List<String> batchNumberList) {
        this.batchNumberList = batchNumberList;
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

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Double getSliceWorkDurationAvg() {
        return sliceWorkDurationAvg;
    }

    public void setSliceWorkDurationAvg(Double sliceWorkDurationAvg) {
        this.sliceWorkDurationAvg = sliceWorkDurationAvg;
    }


    public Double getExpectDayDuration() {
        return expectDayDuration;
    }

    public void setExpectDayDuration(Double expectDayDuration) {
        this.expectDayDuration = expectDayDuration;
    }

    public Double getExpectSumDuration() {
        return expectSumDuration;
    }

    public void setExpectSumDuration(Double expectSumDuration) {
        this.expectSumDuration = expectSumDuration;
    }


    public void setWorkDurationHour(Double workDurationHour) {
        this.workDurationHour = workDurationHour;
    }


    public void setWorkDurationTotalHour(Double workDurationTotalHour) {
        this.workDurationTotalHour = workDurationTotalHour;
    }

    public Integer getSumWorkDuration() {
        return sumWorkDuration;
    }

    public void setSumWorkDuration(Integer sumWorkDuration) {
        this.sumWorkDuration = sumWorkDuration;
    }


    public void setSumWorkDurationHour(Double sumWorkDurationHour) {
        this.sumWorkDurationHour = sumWorkDurationHour;
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

    public String getBatchTypeName() {
        return batchTypeName;
    }

    public void setBatchTypeName(String batchTypeName) {
        this.batchTypeName = batchTypeName;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public String getDoneType() {
        return doneType;
    }

    public void setDoneType(String doneType) {
        this.doneType = doneType;
    }

    public Integer getUnsale() {
        return unsale;
    }

    public void setUnsale(Integer unsale) {
        this.unsale = unsale;
    }
}