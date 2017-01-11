package com.k2data.platform.general;

import com.k2data.platform.general.GeneralQuerySqlProvider;
import com.k2data.platform.domain.MachineDimension;
import com.k2data.platform.persistence.SqlRunner;

import java.util.List;

/**
 * @author lidong 16-11-11.
 */
public class GeneralQueryService {

    private static GeneralQuerySqlProvider sqlProvider = new GeneralQuerySqlProvider();

    /**
     * 获取字典值
     *
     * @param label
     * @param type
     * @param defaultValue
     * @return
     */
    public static String queryDictValue(String label, String type, String defaultValue) {
        String value = SqlRunner.selectOne(String.class, sqlProvider.queryDictValue(label, type));

        if (value == null)
            return defaultValue;

        return value;
    }

    /**
     * 根据类型获取维度列表
     *
     * @param type
     * @return
     */
    public static List<MachineDimension> queryMachineDimensionList(Integer type) {
        return SqlRunner.selectList(MachineDimension.class, sqlProvider.queryMachineDimensionList(type));
    }

    /**
     * 获取 gpsNo 列表
     *
     * @return
     */
    public static List<String> queryGpsNo() {
        return SqlRunner.selectList(String.class, sqlProvider.queryGpsNoList());
    }

}
