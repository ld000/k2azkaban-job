package job.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author lidong 16-11-7.
 */
public class Global {

    private static Map<String, String> propMap = Maps.newHashMap();

    private static PropertiesUtils loader = new PropertiesUtils("k2data.properties");

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

}
