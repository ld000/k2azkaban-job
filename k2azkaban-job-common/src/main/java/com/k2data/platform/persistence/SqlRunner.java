package com.k2data.platform.persistence;

import com.k2data.platform.exception.InternalException;
import com.k2data.platform.persistence.callback.*;
import com.k2data.platform.persistence.support.BoundSql;
import com.k2data.platform.persistence.transaction.TransactionUtils;
import com.k2data.platform.utils.ArrayUtils;
import com.k2data.platform.utils.NumberUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SQL增删改查操作的主类
 *
 * @author lidong 16-8-5.
 */
public class SqlRunner {

    private static final Integer MAX_BATCH_EXECUTE_SQLS = 500;

    /**
     * 查询满足条件的唯一值，依据接收类型，来转换成 JDK 自有类型或用 {@code set} 方法赋值给 Bean<br>
     * 如果查询结果多于一条，那么将会抛出 {@link InternalException} 异常
     *
     * @param requiredType 所需的类型，可以为 JDK 自带类型或 BEAN
     * @param sql 要执行的 SQL 语句
     * @param inValues 如果以 {@link PreparedStatement} 方式执行查询方法所需参数的值
     * @return T实例或 {@code null}
     *
     * @see SqlRunner#selectOne(Class, BoundSql)
     */
    public static <T> T selectOne(final Class<T> requiredType, final String sql, final Object ... inValues) {
        return selectOne(requiredType, new BoundSql(sql, inValues));
    }

    /**
     * 查询满足条件的唯一的一组数据，并将其存入 {@code Map<String, Object>} 中<br>
     * 如果查询结果多于一条，那么将会抛出 {@link InternalException} 异常<br>
     * {@code Map<String, Object>} 中 Key 为查询出来的列名的大写，Value 为对应的值
     *
     * @param sql 要执行的 SQL 语句
     * @param inValues 如果以 {@link PreparedStatement} 方式执行查询方法所需参数的值
     * @return {@code Map<String, Object>} 实例，如果查询结果为空，那么该 Map 也为空
     *
     * @see SqlRunner#selectOneMap(BoundSql)
     */
    public static Map<String, Object> selectOneMap(final String sql, final Object ... inValues) {
        return selectOneMap(new BoundSql(sql, inValues));
    }

    /**
     * 查询满足条件的所有数据，依据接收类型，来转换成JDK自有类型或用 {@code set} 方法赋值给 Bean<br>
     * 并将其按查询顺序存入 {@link List<T>}
     *
     * @param requiredType 所需的类型，可以为 JDK 自带类型或 BEAN
     * @param sql 要执行的 SQL 语句
     * @param inValues 如果以 {@link PreparedStatement} 方式执行查询方法所需参数的值
     * @return List<T> 实例，如果查询结果为空，那么该 List 也为空
     *
     * @see SqlRunner#selectList(Class, BoundSql)
     */
    public static <T> List<T> selectList(final Class<T> requiredType, final String sql, final Object ... inValues) {
        return selectList(requiredType, new BoundSql(sql, inValues));
    }

    /**
     * 查询满足条件的所有数据，将其每组数值存入{@code Map}，<br>
     * 并将{@code Map}按查询顺序存入{@code List}中<br>
     * {@code Map}中Key为查询出来的列名的大写，Value为对应的值
     *
     * @param sql 要执行的SQL语句
     * @param inValues 如果以{@link PreparedStatement}方式执行查询方法所需参数的值
     * @return {@code List<Map<String, Object>>}实例，如果查询结果为空，那么该List也为空
     *
     * @see SqlRunner#selectListMap(BoundSql)
     */
    public static List<Map<String, Object>> selectListMap(final String sql, final Object ... inValues) {
        return selectListMap(new BoundSql(sql, inValues));
    }

    /**
     * 执行添加操作
     *
     * @param sql 要执行的SQL添加语句
     * @param inValues 如果以{@link PreparedStatement}方式执行添加操作所需参数的值
     * @return 已添加的总行数
     *
     * @see SqlRunner#update(BoundSql)
     */
    public static int insert(final String sql, final Object ... inValues) {
        return update(new BoundSql(sql, inValues));
    }

    /**
     * 执行添加操作
     *
     * @param boundSql 要执行的SQL添加语句
     * @return 已添加的总行数
     *
     * @see SqlRunner#update(BoundSql)
     */
    public static int insert(final BoundSql boundSql) {
        return update(boundSql);
    }

    /**
     * 执行更新操作
     *
     * @param sql 要执行的SQL更新语句
     * @param inValues 如果以{@link PreparedStatement}方式执行更新操作所需参数的值
     * @return 已更新的总行数
     *
     * @see SqlRunner#update(BoundSql)
     */
    public static int update(final String sql, final Object ... inValues) {
        return update(new BoundSql(sql, inValues));
    }

    /**
     * 进行删除操作
     *
     * @param sql 要执行的SQL删除语句
     * @param inValues 如果以{@link PreparedStatement}方式执行删除操作所需参数的值
     * @return 已删除的总行数
     *
     * @see SqlRunner#update(BoundSql)
     */
    public static int delete(final String sql, final Object ... inValues) {
        return update(new BoundSql(sql, inValues));
    }

    /**
     * 进行删除操作
     *
     * @param boundSql 要执行的SQL删除语句
     * @return 已删除的总行数
     *
     * @see SqlRunner#update(BoundSql)
     */
    public static int delete(final BoundSql boundSql) {
        return update(boundSql);
    }

//    /**
//     * 查询满足条件的总数量
//     *
//     * @param sql 要执行的SQL查询语句
//     * @param inValues 如果以{@link PreparedStatement}方式执行查询操作所需参数的值
//     * @return 满足条件的总数量
//     *
//     * @see SqlRunner#selectOne(Class, BoundSql)
//     * @see SqlUtils#getTotalSql(String, String...)
//     * @see NumberUtils#toInt(Object)
//     */
//    public static final int count(final String sql, final Object ... inValues) {
//        return NumberUtils.toInt(selectOne(Integer.class, SqlUtils.getTotalSql(sql), inValues));
//    }

//    /**
//     * 查询满足条件的总数量
//     *
//     * @param boundSql 要执行的{@link BoundSql}
//     * @return 满足条件的总数量
//     *
//     * @see SqlRunner#selectOne(Class, BoundSql)
//     * @see SqlUtils#getTotalSql(String, String...)
//     * @see NumberUtils#toInt(Object)
//     */
//    public static int count(final BoundSql boundSql) {
//        return NumberUtils.toInt(count(boundSql.getSql(), boundSql.getInValues().toArray()));
//    }

    /**
     * 查询表中数据的总行数
     *
     * @param tableName 要查询的数据库表名
     * @return 表中数据的总行数
     *
     * @see SqlRunner#selectOne(Class, String, Object...)
     * @see NumberUtils#toInt(Object)
     */
    public static int countByTableName(final String tableName) {
        return NumberUtils.toInt(selectOne(Integer.class, "SELECT COUNT(*) FROM " + tableName.toUpperCase()));
    }

    /**********************************************************************************************************
     * 主要方法
     **********************************************************************************************************/
    /**
     * 查询满足条件的唯一值，依据接收类型，来转换成JDK自有类型或用{@code set}方法赋值给Bean<br>
     * 如果查询结果多于一条，那么将会抛出{@link InternalException}异常
     *
     * @param requiredType 所需的类型，可以为JDK自带类型或BEAN
     * @param boundSql 要执行的{@link BoundSql}
     * @return T实例或{@code null}
     *
     * @see ConnectionQueryCallback
     */
    public static <T> T selectOne(final Class<T> requiredType, final BoundSql boundSql) {
        return execute(new ConnectionQueryCallback<T, T>(requiredType, boundSql, true));
    }

    /**
     * 查询满足条件的唯一的一组数据，并将其存入{@code Map<String, Object>}中<br>
     * 如果查询结果多于一条，那么将会抛出{@link InternalException}异常<br>
     * {@code Map<String, Object>}中Key为查询出来的列名的大写，Value为对应的值
     *
     * @param boundSql 要执行的{@link BoundSql}
     * @return {@code Map<String, Object>} 实例，如果查询结果为空，那么该Map也为空
     *
     * @see ConnectionQueryMapCallback
     */
    public static Map<String, Object> selectOneMap(final BoundSql boundSql) {
        return execute(new ConnectionQueryMapCallback<Map<String, Object>>(boundSql, true));
    }

    /**
     * 查询满足条件的所有数据，将其每组数值存入{@code Map<String, Object>}，<br>
     * 并将{@code Map<String, Object>}按查询顺序存入{@code List<Map<String, Object>>}中<br>
     * {@code Map<String, Object>}中Key为查询出来的列名的大写，Value为对应的值
     *
     * @param boundSql 要执行的{@link BoundSql}
     * @return {@code List<Map<String, Object>>} 实例，如果查询结果为空，那么该List也为空
     *
     * @see ConnectionQueryMapCallback
     */
    public static List<Map<String, Object>> selectListMap(final BoundSql boundSql) {
        return execute(new ConnectionQueryMapCallback<List<Map<String, Object>>>(boundSql, false));
    }

    /**
     * 查询满足条件的所有数据，依据接收类型，来转换成JDK自有类型或用{@code set}方法赋值给Bean<br>
     * 并将其按查询顺序存入 {@link List<T>}
     *
     * @param requiredType 所需的类型，可以为JDK自带类型或BEAN
     * @param boundSql 要执行的{@link BoundSql}
     * @return List<T> 实例，如果查询结果为空，那么该List也为空
     *
     * @see ConnectionQueryCallback
     */
    public static <T> List<T> selectList(final Class<T> requiredType, final BoundSql boundSql) {
        return execute(new ConnectionQueryCallback<T, List<T>>(requiredType, boundSql, false));
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

    /**
     * 通过批量增删改所需参数的值来执行SQL语句，
     *
     * @param sql 要执行的SQL语句
     * @param batchInValues {@link PreparedStatement}执行SQL语句所需的批量参数
     * @return int[] 每组SQL语句执行状态的数组
     *
     * @see ConnectionBatchUpdateCallback
     */
    public static int[] batch(final String sql, final List<List<Object>> batchInValues) {
        if (batchInValues.size() <= MAX_BATCH_EXECUTE_SQLS) { // 每次批量操作最多的数量
            return execute(new ConnectionBatchUpdateCallback(sql, batchInValues));
        } else { // 数量大于每次批量操作最大值时分批更新
            int[] array = new int[batchInValues.size()];

            int loop = 0;

            boolean complete = false; // 批量循环处理是否结束

            while (true) {
                final List<List<Object>> subBatchInValues = new ArrayList<>();

                if ((loop + 1) * MAX_BATCH_EXECUTE_SQLS >= batchInValues.size()) {
                    complete = true;
                    subBatchInValues.addAll(batchInValues.subList(MAX_BATCH_EXECUTE_SQLS * loop, batchInValues.size()));
                } else {
                    subBatchInValues.addAll(batchInValues.subList(MAX_BATCH_EXECUTE_SQLS * loop, MAX_BATCH_EXECUTE_SQLS * (loop + 1)));
                }

                ArrayUtils.fill(array, execute(new ConnectionBatchUpdateCallback(sql, subBatchInValues)), MAX_BATCH_EXECUTE_SQLS * loop);

                if (complete) {
                    break;
                } else {
                    loop++;
                }
            }

            return array;
        }
    }

    /**
     * 执行批量增删改操作
     *
     * @param batchSqls 要批量执行的SQL语句数组
     * @return int[] 每组SQL语句执行状态的数组
     *
     * @see ConnectionBatchUpdateCallback
     */
    public static int[] batch(final List<String> batchSqls) {
        if (batchSqls.size() <= MAX_BATCH_EXECUTE_SQLS) { // 每次批量操作最多的数量
            return execute(new ConnectionBatchUpdateCallback(batchSqls));
        } else { // 数量大于每次批量操作最大值时分批更新
            int[] array = new int[batchSqls.size()];

            int loop = 0;

            boolean complete = false; // 批量循环处理是否结束

            while (true) {
                final List<String> subBatchSqls = new ArrayList<>();

                if ((loop + 1) * MAX_BATCH_EXECUTE_SQLS >= batchSqls.size()) {
                    complete = true;
                    subBatchSqls.addAll(batchSqls.subList(MAX_BATCH_EXECUTE_SQLS * loop, batchSqls.size()));
                } else {
                    subBatchSqls.addAll(batchSqls.subList(MAX_BATCH_EXECUTE_SQLS * loop, MAX_BATCH_EXECUTE_SQLS * (loop + 1)));
                }

                ArrayUtils.fill(array, execute(new ConnectionBatchUpdateCallback(subBatchSqls)), MAX_BATCH_EXECUTE_SQLS * loop);

                if (complete) {
                    break;
                } else {
                    loop++;
                }
            }

            return array;
        }
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
            conn = TransactionUtils.getConnection();

            return callback.doInConnection(conn);
        } finally {
            try {
                if (conn != null && conn.getAutoCommit())
                    JdbcUtils.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
