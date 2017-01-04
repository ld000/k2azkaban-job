package com.k2data.platform.kmx.domain;

import java.util.ArrayList;
import java.util.List;

public class KmxSensorDomain {

    private String id;
    private String description;
    private String valueType;
    private List<Object> intervals;
    private String url;
    
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
    public String getValueType() {
        return valueType;
    }
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    public List<Object> getIntervals() {
        if (intervals == null)
            return new ArrayList<>();
        return intervals;
    }
    public void setIntervals(List<Object> intervals) {
        this.intervals = intervals;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}
