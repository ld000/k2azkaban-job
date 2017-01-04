package com.k2data.platform.domain;

/**
 * Created by Yew on 16/10/10.
 * 坐标实体,经纬度
 */
public class MapCoord {

    private Double longitude;   //经度
    private Double latitude;    //维度

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

    @Override
    public String toString() {
        return longitude + "," + latitude;
    }
}
