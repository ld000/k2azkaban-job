package com.k2data.platform.persistence;

import com.k2data.platform.exception.InternalException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 *
 * @author lidong 16-8-5.
 */
public class ConnectionUtils {

    /**
     * 获取 LDP Oracle 数据库连接
     *
     * @return 数据库连接
     */
    public static Connection getLDPConnection() {
        String url = System.getenv("LDP_JDBC_URL");
        String userName = System.getenv("LDP_JDBC_USERNAME");
        String password = System.getenv("LDP_JDBC_PASSWORD");

        Connection conn;
        try {
            Class.forName(System.getenv("LDP_JDBC_DRIVER"));
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InternalException(e);
        }

        return conn;
    }

    public static Connection getConnection() {
        String url = System.getenv("JDBC_URL");
        String userName = System.getenv("JDBC_USERNAME");
        String password = System.getenv("JDBC_PASSWORD");

        Connection conn;
        try {
            Class.forName(System.getenv("JDBC_DRIVER"));
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InternalException(e);
        }

        return conn;
    }

}
