package com.k2data.platform.kmx.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KmxDeviceDomain {

    private String id;                //设备编号
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private String deviceTypeId;    //设备模板
    private String status;
    private List<Object> tags;
    private List<Object> attributes;
    private List<KmxSensorDomain> sensors;    //传感器列表
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getDeviceTypeId() {
        return deviceTypeId;
    }
    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<Object> getTags() {
        if (tags == null)
            return new ArrayList<>();
        return tags;
    }
    public void setTags(List<Object> tags) {
        this.tags = tags;
    }
    public List<Object> getAttributes() {
        if (attributes == null)
            return new ArrayList<>();
        return attributes;
    }
    public void setAttributes(List<Object> attributes) {
        this.attributes = attributes;
    }
    public List<KmxSensorDomain> getSensors() {
        if (sensors == null)
            return new ArrayList<>();
        return sensors;
    }
    public void setSensors(List<KmxSensorDomain> sensors) {
        this.sensors = sensors;
    }
    
}
