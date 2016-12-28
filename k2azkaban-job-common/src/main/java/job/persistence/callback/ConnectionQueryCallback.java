package job.persistence.callback;

import com.k2data.job.exception.InternalException;
import com.k2data.job.persistence.BoundSql;
import com.k2data.job.persistence.JdbcUtils;
import com.k2data.job.persistence.PeisistenceLogManager;
import com.k2data.job.persistence.resultset.DefaultResultSetExtractor;
import com.k2data.job.persistence.resultset.DefaultRowMapper;
import com.k2data.job.persistence.resultset.ResultSetExtractor;
import com.k2data.job.persistence.resultset.UniqueResultSetExtractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 查询用{@link ConnectionCallback}，可查唯一数据或多条数据
 * 
 * @param <T> 所需的类型，可以为JDK自带类型或BEAN
 * @param <R> 查询结果的类型，{@link List}（多条查询）或等用户所需的类型（单条查询）
 */
public class ConnectionQueryCallback<T, R> implements ConnectionCallback<R> {
    /** 查询结果的类型，如果查询多条那么是List，否则等于{@code #resultType} **/
    private Class<R> resultType;
    /** 查询所需的类型，可以为JDK自带类型或BEAN **/
    private Class<T> requiredType;
    /** 要执行的SQL **/
    private BoundSql boundSql;
    
    /**
     * 查询用{@link ConnectionCallback}的构造函数
     * 
     * @param requiredType 查询所需的类型
     * @param boundSql 要执行的SQL
     * @param unique 是否查询唯一数据
     */
    @SuppressWarnings("unchecked")
    public ConnectionQueryCallback(final Class<T> requiredType,
            final BoundSql boundSql, final boolean unique) {    
        this.requiredType = requiredType;
        if (unique) {
            this.resultType = (Class<R>) requiredType;
        } else {
            this.resultType = (Class<R>) List.class;
        }
        this.boundSql = boundSql;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public R doInConnection(final Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            PeisistenceLogManager.log(boundSql);

            ps = conn.prepareStatement(boundSql.getSql(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            
            JdbcUtils.fillPreparedStatement(ps, boundSql.getInValues());
            
            rs = ps.executeQuery();
            
            if (requiredType.equals(resultType)) { // 查询单条数据
                final ResultSetExtractor<T> extractor = new UniqueResultSetExtractor<T>(new DefaultRowMapper<T>(this.requiredType));
                
                return (R) extractor.extractData(rs);
            } else { // 查询多条数据
                final ResultSetExtractor<List<T>> extractor = new DefaultResultSetExtractor<T>(new DefaultRowMapper<T>(this.requiredType));
                
                return (R) extractor.extractData(rs);
            }
        } catch (SQLException sqle) {
            throw new InternalException(sqle);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }
    }

}
