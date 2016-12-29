package com.k2data.platform.persistence.callback;

import com.k2data.platform.exception.InternalException;
import com.k2data.platform.persistence.BoundSql;
import com.k2data.platform.persistence.JdbcUtils;
import com.k2data.platform.persistence.PeisistenceLogManager;
import com.k2data.platform.persistence.resultset.MapRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询用{@link ConnectionCallback}，可查唯一数据或多条数据
 *
 * @author lidong 16-8-5.
 */
public class ConnectionQueryMapCallback<T> implements ConnectionCallback<T> {

    /** 查询所需的类型，只能是Map或List **/
    private Class<T> resultType;
    /** 要执行的SQL **/
    private BoundSql boundSql;

    /**
     * 查询用{@link ConnectionCallback}的构造函数
     *
     * @param boundSql 要执行的SQL
     */
    @SuppressWarnings("unchecked")
    public ConnectionQueryMapCallback(final BoundSql boundSql, final boolean unique) {
        if (unique) {
            this.resultType = (Class<T>) Map.class;
        } else {
            this.resultType = (Class<T>) List.class;
        }
        this.boundSql = boundSql;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T doInConnection(Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            PeisistenceLogManager.log(boundSql);

            ps = conn.prepareStatement(boundSql.getSql(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            JdbcUtils.fillPreparedStatement(ps, boundSql.getInValues());

            rs = ps.executeQuery();

            if (List.class.isAssignableFrom(resultType)) { // 查询多条数据
                final List<Map<String, Object>> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(new MapRowMapper().mapRow(rs));
                }

                return (T) results;
            } else {
                final List<Map<String, Object>> results = new ArrayList<>();

                try {
                    while (rs.next()) {
                        if (!results.isEmpty()) {
                            throw new InternalException("ResultSet expects unique result, but has more than one results");
                        }

                        results.add(new MapRowMapper().mapRow(rs));
                    }
                } catch (SQLException sqle) {
                    throw new InternalException(sqle);
                }

                if (results.isEmpty()) {
                    return null;
                } else {
                    return (T) results.get(0);
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }
    }

}
