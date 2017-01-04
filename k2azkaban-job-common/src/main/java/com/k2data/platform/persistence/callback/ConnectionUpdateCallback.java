package com.k2data.platform.persistence.callback;

import com.k2data.platform.exception.InternalException;
import com.k2data.platform.persistence.support.BoundSql;
import com.k2data.platform.persistence.JdbcUtils;
import com.k2data.platform.persistence.PeisistenceLogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 增删改用{@link ConnectionCallback}
 * 
 * @author lidong
 */
public class ConnectionUpdateCallback implements ConnectionCallback<Integer> {

    /** 要执行的SQL **/
    private BoundSql boundSql;
    
    /**
     * 增删改用{@link ConnectionCallback}的构造函数
     * 
     * @param boundSql 要执行的SQL
     */
    public ConnectionUpdateCallback(final BoundSql boundSql) {
        this.boundSql = boundSql;
    }
    
    @Override
    public Integer doInConnection(final Connection conn) {
        PreparedStatement ps = null;

        try {
            PeisistenceLogManager.log(boundSql);

            ps = conn.prepareStatement(boundSql.getSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            JdbcUtils.fillPreparedStatement(ps, boundSql.getInValues());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new InternalException(e);
        } finally {
            JdbcUtils.closeStatement(ps);
        }
    }

}
