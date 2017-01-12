package com.k2data.job.common;

import com.k2data.platform.annotation.Influx;
import com.k2data.platform.influx.InfluxDBClient;
import com.k2data.platform.utils.Global;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lidong 2016-12-28
 */
public class JobProxyHandler<T> implements InvocationHandler {

    private Class<T> proxyClazz;

    public JobProxyHandler(Class<T> proxyClazz) {
        this.proxyClazz = proxyClazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        initLog4j();

        Object result = 0;
        try {
            result = method.invoke(proxyClazz.newInstance(), args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (proxyClazz.isAnnotationPresent(Influx.class)) {
                insertInflux(Long.valueOf(result.toString()), proxyClazz.getAnnotation(Influx.class));
            }
        }

        return result;
    }

    private void initLog4j() {
        try {
            Configurator.initialize(null, new ConfigurationSource(new FileInputStream(JobUtils.getRootPath() + "conf/log4j2.xml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertInflux(long count, Influx influx) {
        String[] tags = influx.tag();
        Map<String, String> tag2Add = new HashMap<>();
        if (tags.length != 0) {
            for (String tag : tags) {
                String[] splitTag = tag.split(":");
                tag2Add.put(splitTag[0], splitTag[1]);
            }
        }

        BatchPoints batchPoints = BatchPoints
                .database(Global.getConfig("influx.dbname"))
                .build();

        Point point = Point.measurement(influx.measurement())
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag(tag2Add)
                .addField("count", count)
                .build();

        batchPoints.point(point);

        InfluxDBClient.getInfluxDB().write(batchPoints);
    }

}
