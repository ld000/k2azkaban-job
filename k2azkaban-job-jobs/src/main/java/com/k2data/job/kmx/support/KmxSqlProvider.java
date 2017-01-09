package com.k2data.job.kmx.support;

import com.google.common.collect.Lists;
import com.k2data.platform.persistence.support.BoundSql;

import java.util.List;
import java.util.Map;

/**
 * @author lidong 2016/12/13.
 */
public class KmxSqlProvider {

    public String getQueryCountSql(final Map<String, Object> para) {
        return "SELECT count(*)" +
                " FROM " + para.get("table") +
                " WHERE begin_date <= #{date}" +
                " AND end_date >= #{date}";
    }

    public String getQueryIncreaseCountSql(final Map<String, Object> para) {
        return "SELECT count(*)" +
                " FROM " + para.get("table") +
                " WHERE begin_date = #{date}";
    }

    public String getQueryChangedCountSql(final Map<String, Object> para) {
        return "SELECT (" +
                    "SELECT COUNT(*) FROM " + para.get("table") + " WHERE begin_date = #{date}" +
                ") - (" +
                    "SELECT COUNT(*) FROM " + para.get("table") + " WHERE end_date = date_sub(#{date}, interval 1 day)" +
                ")";
    }

    public static BoundSql getQueryLDPDailyCountSql(String tableName, String beginDate, String endDate) {
        String ldpDailyCountSql = "SELECT COUNT, DAY" +
                " FROM TB_LGBD_DATE_REPORT_DAILY" +
                " WHERE TABLE_NAME = ?" +
                " AND TO_DATE(DAY,'yyyy-mm-dd') BETWEEN TO_DATE(?,'yyyy-mm-dd') AND TO_DATE(?,'yyyy-mm-dd')" +
                " ORDER BY TO_DATE(DAY,'yyyy-mm-dd')";

        List<Object> inValues = Lists.newArrayList();
        inValues.add(tableName);
        inValues.add(beginDate);
        inValues.add(endDate);

        return new BoundSql(ldpDailyCountSql, inValues.toArray());
    }

    public String getQueryAuditorTableSql(Map<String, Object> para) {
        String sql = " select auditor_table " +
                "from audit_register " +
                "where topology='AdapterTopology' " +
                "and auditor='adapterMetrics' " +
                "and register_time <= #{beginDate} " +
                "order by register_time desc " +
                "limit 1";

        return sql;
    }

    public String getQueryRecordTotalSql(Map<String, Object> para) {
        return "select sum(value) " +
                "from " + para.get("tableName") +
                " where audit_time >= #{beginDate} " +
                "and audit_time < #{endDate} " +
                "and schema_id = 'RECORD_TOTAL'";
    }

}
