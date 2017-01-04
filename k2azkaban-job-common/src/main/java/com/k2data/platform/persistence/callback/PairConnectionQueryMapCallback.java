package com.k2data.platform.persistence.callback;

import com.k2data.platform.exception.InternalException;
import com.k2data.platform.persistence.support.BoundSql;
import com.k2data.platform.persistence.JdbcUtils;
import com.k2data.platform.persistence.PeisistenceLogManager;
import com.k2data.platform.persistence.resultset.MapPairResultSetExtractor;
import com.k2data.platform.persistence.resultset.MapPairRowMapper;
import com.k2data.platform.persistence.resultset.PairResultSetExtractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 查询{@link Map}用{@link ConnectionCallback}，可查唯一数据或多条数据<br>
 * {@link Map}的Key查询出来的指定列对应的值的组合 {@link Map}的value查询出来的指定列对应的值的组合
 *
 * @param <K> 查询结果的类型
 * @param <V> 查询结果的类型
 */
public class PairConnectionQueryMapCallback<K, V> implements PairConnectionCallback<K, V> {
    /** map的key的所需类型 **/
    private Class<K> keyType;
    /** map的value的所需类型 **/
    private Class<V> valueType;
    /** 要执行的SQL **/
    private BoundSql boundSql;
    /** 指定的列名，其对应值为map的key，如果为空，就选第一列 **/
    private String keyColumnName;
    /** 指定的列名，其对应值为map的value，如果为空，就选第二列 **/
    private String valueColumnName;
    
    public PairConnectionQueryMapCallback(final Class<K> keyType, final Class<V> valueType, final BoundSql boundSql, final String keyColumnName, final String valueColumnName) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.boundSql = boundSql;
        this.keyColumnName = keyColumnName;
        this.valueColumnName = valueColumnName;
    }
    
    public PairConnectionQueryMapCallback(final Class<K> keyType, final Class<V> valueType, final BoundSql boundSql) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.boundSql = boundSql;
    }

    @Override
    public Map<K, V> doInConnection(final Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            PeisistenceLogManager.log(boundSql);

            ps = conn.prepareStatement(boundSql.getSql(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            JdbcUtils.fillPreparedStatement(ps, boundSql.getInValues());

            rs = ps.executeQuery();

            final PairResultSetExtractor<K, V> extractor = new MapPairResultSetExtractor<K, V>(new MapPairRowMapper<K, V>(this.keyType, this.valueType, this.keyColumnName,this.valueColumnName));

            return extractor.extractData(rs);
        } catch (SQLException sqle) {
            throw new InternalException(sqle);
        } finally {
            JdbcUtils.closeResultSet(rs);
            rs = null;
            JdbcUtils.closeStatement(ps);
            ps = null;
        }
    }

}
