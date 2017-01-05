package com.k2data.platform.persistence;

import com.k2data.platform.exception.InternalException;
import com.k2data.platform.utils.Global;

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
        String url = Global.getConfig("jdbc.ldp.url");
        String userName = Global.getConfig("jdbc.ldp.username");
        String password = Global.getConfig("jdbc.ldp.password");

        Connection conn;
        try {
            Class.forName(Global.getConfig("jdbc.ldp.driver"));
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InternalException(e);
        }

        return conn;
    }

    public static Connection getConnection() {
        String url = Global.getConfig("jdbc.url");
        String userName = Global.getConfig("jdbc.username");
        String password = Global.getConfig("jdbc.password");

        Connection conn;
        try {
            Class.forName(Global.getConfig("jdbc.driver"));
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InternalException(e);
        }

        return conn;
    }

}
