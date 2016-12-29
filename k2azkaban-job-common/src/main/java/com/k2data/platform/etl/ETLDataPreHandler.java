package com.k2data.platform.etl;

import java.util.List;
import java.util.Map;

/**
 * 数据同步预处理
 *
 * @author lidong 16-9-22.
 */
public interface ETLDataPreHandler {

    List<Map<String, Object>> handleData(List<Map<String, Object>> list);

}
