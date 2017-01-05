package com.k2data.platform.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong 16-11-7.
 */
public class Global {

    private static Map<String, String> propMap = new HashMap<>();

    private static PropertiesUtils loader = new PropertiesUtils("k2data.properties");

    /**
     * 是/否
     */
    public static final String YES = "1";
    public static final String NO = "0";

    public static final Integer YES_INT = 1;
    public static final Integer NO_INT = 0;

    /**
     * 对/错
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /** 机器大事件类型 **/
    public static final Integer OFFLINE_DATE_EVENT = 1; // 机器下线日期
    public static final Integer ARRIVED_SALEUNIT_EVENT = 2; // 运抵经销商处
    public static final Integer SALE_DATE_EVENT = 3;    // 销售日期
    public static final Integer FIRST_WORK_EVENT = 4;   // 第一次开工
    public static final Integer RUN_COUNT_HOUR_EVENT = 5;   // 累计运行事件
    public static final Integer OPEN_COUNT_EVENT = 6;   // 累计运行次数
    public static final Integer UPKEEP_EVENT = 7;   // 保养事件
    public static final Integer MAINTAIN_EVENT = 8; // 维修事件
    public static final Integer YEAR_COUNT_DATA_EVENT = 9;  // 每年统计事件

    /** 传感器类型 **/
    public static final Integer ANALOG_SENSOR_TYPE = 1; // 模拟量
    public static final Integer SWITCH_SENSOR_TYPE = 2; // 开关量
    public static final Integer STATE_SENSOR_TYPE = 3;  // 状态量

    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        String value = propMap.get(key);
        if (value == null){
            value = loader.getProperty(key);
            propMap.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    public static void main(String[] args) {

        System.out.println(Global.getConfig("jdbc.url"));
    }

}
