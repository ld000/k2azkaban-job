package com.k2data.platform.persistence.callback;

import com.k2data.platform.exception.InternalException;
import com.k2data.platform.persistence.JdbcUtils;
import com.k2data.platform.utils.ObjectUtils;
import com.k2data.platform.utils.StringUtils;

import java.sql.*;
import java.util.List;

/**
 * 批量增删改用 {@link ConnectionCallback}
 */
public class ConnectionBatchUpdateCallback implements ConnectionCallback<int[]> {
    /** 要执行的SQL **/
    private String sql;
    /** 以{@link PreparedStatement}方式批量执行{@link #sql}所需参数的值  **/
    private List<List<Object>> batchInValues;
    /** 要批量执行的SQL **/
    private List<String> batchSqls;
    
    /**
     * 批量增删改用{@link ConnectionCallback}的构造函数
     * 
     * @param sql 要执行的SQL
     * @param batchInValues 批量执行 {@link #sql}所需的参数
     */
    public ConnectionBatchUpdateCallback(final String sql, final List<List<Object>> batchInValues) {
        this.sql = sql;
        this.batchInValues = batchInValues;
    }
    
    /**
     * 批量增删改用{@link ConnectionCallback}的构造函数
     * 
     * @param batchSqls 要批量执行的SQL
     */
    public ConnectionBatchUpdateCallback(final List<String> batchSqls) {
        this.batchSqls = batchSqls;
    }
    
    @Override
    public int[] doInConnection(final Connection conn) {
        Statement stmt = null;

        try {
            if (StringUtils.isBlank(sql)) { // 要用Statement批量操作
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
                for (String sql : batchSqls) {
                    stmt.addBatch(sql);
                }
                
                return stmt.executeBatch();
            } else { // 要用PreparedStatement批量操作
                stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
                if (!ObjectUtils.isEmpty(batchInValues)) {
                    for (List<Object> inValues : batchInValues) {
                        JdbcUtils.fillPreparedStatement((PreparedStatement)stmt, inValues);
                        ((PreparedStatement)stmt).addBatch();
                    }
                }
                
                return stmt.executeBatch();
            }
        } catch (SQLException sqle) {
            throw new InternalException(sqle);
        } finally {
            JdbcUtils.closeStatement(stmt);
        }
    }

}
