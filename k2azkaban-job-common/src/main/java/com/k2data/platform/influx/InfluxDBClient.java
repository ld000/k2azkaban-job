package com.k2data.platform.influx;

import com.k2data.platform.utils.Global;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

/**
 * @author lidong 17-1-10.
 */
public class InfluxDBClient {

    public static InfluxDB getInfluxDB() {
        return InfluxDBFactory.connect(Global.getConfig("influx.url"),
                Global.getConfig("influx.username"),
                Global.getConfig("influx.password"));
    }

}
