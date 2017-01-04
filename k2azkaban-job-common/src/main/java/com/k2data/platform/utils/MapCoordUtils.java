package com.k2data.platform.utils;

import com.k2data.platform.domain.MapCoord;

/**
 * 地理坐标转换
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。
 * GCJ02坐标系：即火星坐标系，WGS84坐标系经加密后的坐标系。
 * BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系。
 */
public class MapCoordUtils {

    private static final double SEMI_MAJOR_AXIS = 6378245.0;
    private static final double FLATTENING = 0.00335233;
    private static final double SEMI_MINOR_AXIS = SEMI_MAJOR_AXIS * (1.0 - FLATTENING);
    private static final double AA = SEMI_MAJOR_AXIS;
    private static final double BB = SEMI_MINOR_AXIS;
    private static final double EE = (AA * AA - BB * BB) / (AA * BB);

    private static final double M_PI = Math.PI;
    private static final double A = SEMI_MAJOR_AXIS;
    private static final double X_M_PI = M_PI * 3000.0 / 180.0;



    //北谷gps原始值 转 BD09 坐标
    public static MapCoord nvr2bd(double lat, double lon) {

        MapCoord mapCoord = new MapCoord();

        if(verifyCoord(lat, lon)) {
            mapCoord.setLatitude(lat);
            mapCoord.setLongitude(lon);
            return mapCoord;
        }

        //转换北谷原始坐标
        double[] nvr2wgs = nvr2wgs(lat, lon);
        double[] wgs2bd = wgs2bd(nvr2wgs[0], nvr2wgs[1]);
        mapCoord.setLatitude(wgs2bd[0]);
        mapCoord.setLongitude(wgs2bd[1]);
        return mapCoord;
    }

    //北谷gps原始值 转 wgs 坐标
    public static double[] nvr2wgs(double lat, double lon) {
        return new double[]{convertLocationData(lat), convertLocationData(lon)};
    }
    //WGS84 -> BD09
    public static double[] wgs2bd(double lat, double lon) {
        double[] wgs2gcj = wgs2gcj(lat, lon);
        double[] gcj2bd = gcj2bd(wgs2gcj[0], wgs2gcj[1]);
        return gcj2bd;
    }

    //GCJ02 ->  BD09
    public static double[] gcj2bd(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_M_PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_M_PI);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[]{bd_lat, bd_lon};
    }

    //BD09 -> GCJ02
    public static double[] bd2gcj(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_M_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_M_PI);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[]{gg_lat, gg_lon};
    }

    //WGS84 -> GCJ02
    public static double[] wgs2gcj(double lat, double lon) {
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * M_PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * M_PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * M_PI);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    private static double transformLat(double lat, double lon) {
        double ret = -100.0 + 2.0 * lat + 3.0 * lon + 0.2 * lon * lon + 0.1 * lat * lon + 0.2 * Math.sqrt(Math.abs(lat));
        ret += (20.0 * Math.sin(6.0 * lat * M_PI) + 20.0 * Math.sin(2.0 * lat * M_PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lon * M_PI) + 40.0 * Math.sin(lon / 3.0 * M_PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lon / 12.0 * M_PI) + 320 * Math.sin(lon * M_PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double lat, double lon) {
        double ret = 300.0 + lat + 2.0 * lon + 0.1 * lat * lat + 0.1 * lat * lon + 0.1 * Math.sqrt(Math.abs(lat));
        ret += (20.0 * Math.sin(6.0 * lat * M_PI) + 20.0 * Math.sin(2.0 * lat * M_PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * M_PI) + 40.0 * Math.sin(lat / 3.0 * M_PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lat / 12.0 * M_PI) + 300.0 * Math.sin(lat / 30.0 * M_PI)) * 2.0 / 3.0;
        return ret;
    }

    //将终端上传的经纬度转换成实际地图使用的经纬度,转换方法就是终端的分除以0.6是实际的
    //param : locationDataSrc 数据库中的经纬度值. 117.1223250
    //return : 实际地图中使用的经纬度值
    public static double convertLocationData(double coord) {
        if (coord == 0) {
            return coord;
        }

        Double degree = Math.floor(coord);
        Double mins = coord - degree;

        //北谷计算规则,保留7位
        Double minsTarget = Double.parseDouble(String.format("%.7f", (mins / 0.6 )));
        Double coordResult = degree + minsTarget;

        return coordResult;
    }
    //校验是否经纬度为0
    private static boolean verifyCoord(double lat, double lon) {
        return lat == 0 && lon == 0;
    }

    /**
     * NVR -> BD09 北谷原始数据转百度数据
     * @param coord
     * @return
     */
//    public static MapCoord nvr2bd(MapCoord coord) {
//
//        //转换北谷原始坐标
//        MapCoord wgsCoord = nvr2wgs(coord);
//
//        double lat = wgsCoord.getLatitude();
//        double lon = wgsCoord.getLongitude();
//        double[] wgs2bd = wgs2bd(lat, lon);
//
//        MapCoord mapCoord = new MapCoord();
//        mapCoord.setLatitude(wgs2bd[0]);
//        mapCoord.setLongitude(wgs2bd[1]);
//        return mapCoord;
//    }

    //北谷gps原始值 转 wgs 坐标
//    public static MapCoord nvr2wgs(MapCoord coord) {
//
//        MapCoord mapCoord = new MapCoord();
//
//        mapCoord.setLatitude(convertLocationData(coord.getLatitude()));
//        mapCoord.setLongitude(convertLocationData(coord.getLongitude()));
//
//        return mapCoord;
//    }

}
