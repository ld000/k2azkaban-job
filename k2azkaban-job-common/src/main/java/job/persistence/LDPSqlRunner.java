package job.persistence;

import com.k2data.job.persistence.callback.ConnectionCallback;
import com.k2data.job.persistence.callback.ConnectionQueryMapCallback;
import com.k2data.job.persistence.callback.ConnectionUpdateCallback;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * LDP 系统 SQL 增删改查操作的主类
 *
 * @author lidong 16-8-5.
 */
public class LDPSqlRunner {

    /**
     * 查询满足条件的所有数据, 值存入 {@link Map}
     *
     * @param boundSql 要执行的{@link BoundSql}
     * @return List<Map>实例，如果查询结果为空，那么该List也为空
     */
    public static List<Map<String, Object>> selectListMap(final BoundSql boundSql) {
        return execute(new ConnectionQueryMapCallback<List<Map<String, Object>>>(boundSql, false));
    }

    /**
     * 执行增删改操作
     *
     * @param boundSql 要执行的{@link BoundSql}
     * @return 执行增删改成功的数量
     *
     * @see ConnectionUpdateCallback
     */
    public static int update(final BoundSql boundSql) {
        return execute(new ConnectionUpdateCallback(boundSql));
    }

    /**********************************************************************************************************
     * 私有方法
     **********************************************************************************************************/
    /**
     * 通用执行SQL语句私有方法，执行{@link ConnectionCallback#doInConnection(Connection)}
     *
     * @param callback 要被执行的{@link ConnectionCallback}实例
     * @return 执行{@link ConnectionCallback#doInConnection(Connection)}后的返回值
     */
    private static <T> T execute(final ConnectionCallback<T> callback) {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getLDPConnection();

            return callback.doInConnection(conn);
        } finally {
            JdbcUtils.closeConnection(conn);
        }
    }

}
