package com.k2data.platform.etl;

import com.k2data.platform.persistence.support.BoundSql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 拉取临工数据的 SQL 构造类
 * 
 * @author lidong
 */
public class ETLSqlProvider {

    /**
     * 生成用于查询的源数据源字段 string
     */
    public static String getSourceColumns(final ETLMappingDomain domain) {
        String columns = "";
        int i = 0;
        for (ETLMappingDomain.ColumnMappings mappings : domain.getColumnMappings()) {
            if (i++ != 0) {
                columns += ",";
            }

            columns += mappings.getSource().toUpperCase();
        }

        return columns;
    }

    /**
     * 增量拉链处理
     */
    public static String zipAddTypeData(final ETLMappingDomain domain) {
        String column = "";
        int i = 0;
        for (ETLMappingDomain.ColumnMappings mappings : domain.getColumnMappings()) {
            if (i != 0) {
                column += ",";
            }

            column += mappings.getSource().toUpperCase();

            i++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(domain.getHistory().getHisTable().toUpperCase())
          .append(" (").append(column).append(", beginDate, endDate").append(")")
          .append(" SELECT ").append(column).append(", SYSDATE() AS \"beginDate\", \"9999-01-01\" AS \"endDate\" ")
          .append(" FROM ").append(domain.getHistory().getBDMTable().toUpperCase());

        return sb.toString();
    }

    /**
     * 简易拼装 select 语句
     */
    public static String select(String columns, String table, String where) {
        return "SELECT " + columns + " FROM " + table + " WHERE " + where;
    }

    /**
     * 简易拼装 select 语句
     */
    public static String select(String columns, String table) {
        return "SELECT " + columns + " FROM " + table;
    }

    /**
     * 闭链
     */
    public static BoundSql closeZip(final ETLMappingDomain domain, final List<Map<String, Object>> data) {
        String sql = "";
        List<Object> inValues = new ArrayList<>();

        String id = domain.getColumnMappings().get(0).getSource().toUpperCase();

        for (Map<String, Object> obj : data) {
            sql += "UPDATE " + domain.getHistory().getHisTable()
                    + " SET endDate = DATE_SUB(SYSDATE(), INTERVAL 1 DAY)"
                    + " WHERE " + id + " = ?"
                    + " AND endDate = \"9999-01-01\";";
            inValues.add(obj.get(id));
        }

        return new BoundSql(sql, inValues.toArray());
    }

    /**
     * 写新链
     */
    public static BoundSql insertZipHis(final ETLMappingDomain domain, final List<Map<String, Object>> data) {
        StringBuilder sb = new StringBuilder();
        List<Object> inValues = new ArrayList<>();
        String columns = "";
        int k = 0;
        for (Map.Entry<String, Object> obj : data.get(0).entrySet()) {
            if (k++ != 0)
                columns += ",";

            columns += obj.getKey();
        }

        sb.append("INSERT INTO ").append(domain.getHistory().getHisTable().toUpperCase())
                .append(" (").append(columns).append(", beginDate, endDate").append(")")
                .append(" VALUES ");

        int i = 0;
        for (Map<String, Object> obj : data) {
            if (i != 0)
                sb.append(",");

            sb.append(" (");
            int j = 0;
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                if (j++ != 0)
                    sb.append(",");

                sb.append("?");
                inValues.add(entry.getValue());
            }
            sb.append(", SYSDATE(), \"9999-01-01\"");
            sb.append(")");

            i++;
        }

        return new BoundSql(sb.toString(), inValues.toArray());
    }

    /**
     * 全量拉链处理
     */
    public static String zipFullTypeData(final ETLMappingDomain domain) {
        String column = "";
        String rColumn = "";
        int i = 0;
        for (ETLMappingDomain.ColumnMappings mappings : domain.getColumnMappings()) {
            if (i != 0) {
                column += ",";
                rColumn += ",";
            }

            column += mappings.getSource().toUpperCase();
            rColumn += "r." + mappings.getSource().toUpperCase();

            i++;
        }

        StringBuilder sb = new StringBuilder();
        // 闭链，旧有新没有
        sb.append("UPDATE ")
          .append(domain.getHistory().getHisTable().toUpperCase())
          .append(" SET endDate = DATE_SUB(SYSDATE(), INTERVAL 1 DAY)")
          .append(" WHERE ROW(").append(column).append(")")
          .append(" IN (")
          .append(" SELECT ").append(rColumn)
              .append(" FROM (")
                    .append("SELECT ").append(column).append(" FROM ").append(domain.getHistory().getHisTable().toUpperCase())
                    .append(" WHERE endDate = \"9999-01-01\"")
              .append(") r")
          .append(" WHERE ROW(").append(rColumn).append(")")
          .append(" NOT IN (SELECT ").append(column).append(" FROM ").append(domain.getHistory().getBDMTable().toUpperCase()).append(")")
          .append(")");

        sb.append(";");

        // 开新链，新有旧没有
        sb.append("INSERT INTO ").append(domain.getHistory().getHisTable().toUpperCase())
          .append(" (").append(column).append(", beginDate, endDate").append(")")
          .append(" SELECT ").append(column).append(", SYSDATE() AS \"beginDate\", \"9999-01-01\" AS \"endDate\" ")
          .append(" FROM ").append(domain.getHistory().getBDMTable().toUpperCase())
          .append(" WHERE ROW(").append(column).append(")")
          .append(" NOT IN (")
          .append(" SELECT ").append(column).append(" FROM ").append(domain.getHistory().getHisTable().toUpperCase())
          .append(" WHERE beginDate <= SYSDATE() AND endDate >= SYSDATE()")
          .append(");")
        ;

        return sb.toString();
    }

    /**
     * 插入目标表
     */
    public static BoundSql insert2Target(final ETLMappingDomain domain, final List<Map<String, Object>> source) {
        List<String> sourceColumn = new ArrayList<>();
        String targetColumn = "";
        int k = 0;
        for (ETLMappingDomain.ColumnMappings mappings : domain.getColumnMappings()) {
            if (k != 0) {
                targetColumn += ",";
            }

            sourceColumn.add(mappings.getSource().toUpperCase());
            targetColumn += mappings.getTarget().toUpperCase();

            k++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(domain.getTargetTable().toUpperCase())
          .append(" (").append(targetColumn).append(")")
          .append(" VALUES ");

        List<Object> inValues = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            if (i != 0)
                sb.append(",");

            sb.append("(");
            int j = 0;
            for (String column : sourceColumn) {
                if (j++ != 0)
                    sb.append(",");

                sb.append("?");
                inValues.add(source.get(i).get(column));
            }
            sb.append(")");
        }

        return new BoundSql(sb.toString(), inValues.toArray());
    }

    /**
     * 删除所有数据
     */
    public static String deleteAll(String table, List<String> deleteWhere) {
        String sql = "DELETE FROM " + table;

        if (deleteWhere != null) {
            int i = 0;
            sql += " WHERE ";
            for (String where : deleteWhere) {
                if (i++ != 0)
                    sql += " AND ";

                sql += where;
            }
        }

        return sql;
    }

    /**
     * 插入 BDM 表
     */
    public static BoundSql insertBDM(final ETLMappingDomain domain, final List<Map<String, Object>> source) {
        List<Object> inValues = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO ")
          .append(domain.getHistory().getBDMTable().toUpperCase());

        sb.append(" (");
        int k = 0;
        for (Map.Entry<String, Object> obj : source.get(0).entrySet()) {
            if (k != 0)
                sb.append(",");

            sb.append("`").append(obj.getKey().toUpperCase()).append("`");

            k++;
        }
        sb.append(")");

        sb.append(" VALUES ");
        for (int i = 0; i < source.size(); i++) {
            if (i != 0)
                sb.append(",");

            sb.append("(");
            int j = 0;
            for (Map.Entry<String, Object> entry : source.get(i).entrySet()) {
                if (j++ != 0)
                    sb.append(",");

                sb.append("?");
                inValues.add(entry.getValue());
            }
            sb.append(")");
        }

        return new BoundSql(sb.toString(), inValues.toArray());
    }

    /**
     * 查询源数据源数据
     */
    public static String selectSourceData(final ETLMappingDomain domain) {
        StringBuilder sb = new StringBuilder("SELECT ");

        int i = 0;
        for (ETLMappingDomain.ColumnMappings mappings : domain.getColumnMappings()) {
            if (i != 0)
                sb.append(",");

            if (mappings.getConstant() != null) {
                sb.append(mappings.getConstant()).append(" \"").append(mappings.getSource().toUpperCase()).append("\"");
            } else {
                sb.append(mappings.getSource().toUpperCase());
            }

            i++;
        }

        sb.append(" FROM ").append(domain.getSourceTable().toUpperCase());

        if (domain.getSourceWhere() != null && domain.getSourceWhere().size() != 0) {
            sb.append(" WHERE ");
            for (int j = 0; j < domain.getSourceWhere().size(); j++) {
                if (j != 0)
                    sb.append(" AND ");

                sb.append(domain.getSourceWhere().get(j).toUpperCase());
            }
        }

        return sb.toString();
    }

    /**
     * 更新 1 和 5 状态到指定状态
     */
    public static BoundSql update1N5(final ETLMappingDomain domain, Integer flag) {
        List<Object> inValues = new ArrayList<>();

        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(domain.getSourceTable())
                .append(" SET ")
                .append(domain.getFlagColumn()).append(" = ?")
                .append(" WHERE FLAG = 1 OR FLAG = 5");
        inValues.add(flag);

        return new BoundSql(sb.toString(), inValues.toArray());
    }
    
}
