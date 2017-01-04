/**
 * Copyright &copy; 2016 <a href="https://www.k2data.com.cn">K2DATA</a> All rights reserved.
 */
package com.k2data.platform.kmx.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KmxDataRowsDomain {

    private Date iso;
    private List<KmxDataPointsDomain> dataPoints;
    private int dataPointsCount;
    private String device;
    private List<KmxAggregationResultsDomain> aggregationResults;

    public Date getIso() {
        return iso;
    }
    public void setIso(Date iso) {
        this.iso = iso;
    }
    public List<KmxDataPointsDomain> getDataPoints() {
        if (dataPoints == null)
            return new ArrayList<>();
        return dataPoints;
    }
    public void setDataPoints(List<KmxDataPointsDomain> dataPoints) {
        this.dataPoints = dataPoints;
    }
    
    public int getDataPointsCount() {
        return dataPointsCount;
    }
    public void setDataPointsCount(int dataPointsCount) {
        this.dataPointsCount = dataPointsCount;
    }
    public String getDevice() {
        return device;
    }
    public void setDevice(String device) {
        this.device = device;
    }
    
    public List<KmxAggregationResultsDomain> getAggregationResults() {
        if (aggregationResults == null)
            return new ArrayList<>();
        return aggregationResults;
    }
    public void setAggregationResults(List<KmxAggregationResultsDomain> aggregationResults) {
        this.aggregationResults = aggregationResults;
    }
    
}
