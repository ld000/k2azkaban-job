package com.k2data.platform.persistence.transaction;

import com.k2data.platform.persistence.ConnectionUtils;
import com.k2data.platform.persistence.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 提供简单的事务控制，用于同步其他数据源数据
 *
 * @author lidong 16-11-1.
 */
public class TransactionUtils {

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>(); // 绑定 Connection 对象到当前线程，用作事务控制

    /**
     * 获取可控制事务的 {@link Connection} 类
     *
     * @return {@link Connection}
     */
    public static Connection getConnection() {
        Connection conn = connectionHolder.get();
        try {
            if (conn == null || conn.isClosed()) {
                conn = ConnectionUtils.getConnection();
                connectionHolder.set(conn);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }

    /**
     * 关闭连接
     */
    public static void closeConnection() {
        JdbcUtils.closeConnection(getConnection());
    }

    /**
     * 开启事务
     */
    public static void beginTransaction() {
        Connection conn = getConnection();

        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection conn = getConnection();

        try {
            conn.commit();
            conn.setAutoCommit(true);
            connectionHolder.remove();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection conn = getConnection();

        try {
            conn.rollback();
            conn.setAutoCommit(true);
            connectionHolder.remove();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
