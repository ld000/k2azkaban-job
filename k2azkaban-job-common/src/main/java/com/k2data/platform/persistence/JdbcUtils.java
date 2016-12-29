package com.k2data.platform.persistence;

import com.k2data.platform.exception.InternalException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;

/**
 * JDBC 的工具类
 *
 * @author lidong 2016-8-5
 */
public final class JdbcUtils {

    private static final Logger logger = LogManager.getLogger(JdbcUtils.class);

    /**
     * 关闭{@link Connection}
     *
     * @param conn 要被关闭的{@link Connection}
     */
    public static void closeConnection(final Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch(SQLException sqle) {
                logger.debug("关闭Connection失败", sqle);
            }
        }
    }

    /**
     * 关闭{@link ResultSet}
     *
     * @param rs 要被关闭的{@link ResultSet}
     */
    public static void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqle) {
                logger.debug("关闭ResultSet失败", sqle);
            }
        }
    }

    /**
     * 关闭{@link Statement}
     *
     * @param stmt 要被关闭的{@link Statement}
     */
    public static void closeStatement(final Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqle) {
                logger.debug("关闭Statement失败", sqle);
            }
        }
    }

    /**
     * 用{@code #inValues}填充{@link PreparedStatement}
     */
    public static void fillPreparedStatement(final PreparedStatement ps, final List<Object> inValues) {
        try {
            final ParameterMetaData pmd = ps.getParameterMetaData();

            int expected = pmd.getParameterCount();

            if (expected == 0)
                return;

            int provided = (inValues == null ? 0 : inValues.size());

            if (expected != provided)
                throw new InternalException("prepared Statement expects " + expected + " in values,but provides " + provided + " in values");

            for (int i = 0; i < inValues.size(); i++) {
                Object inValue = inValues.get(i);




                if (inValue != null && java.util.Date.class.isAssignableFrom(inValue.getClass())) {
                    ps.setTimestamp(i + 1, (Timestamp) ConvertUtils.convert(inValue, Timestamp.class));
                } else if (inValue != null && Clob.class.isAssignableFrom(inValue.getClass())) {
                    ps.setClob(i + 1, ((Clob)inValue).getCharacterStream());
                } else if (inValue != null && Blob.class.isAssignableFrom(inValue.getClass())) {
                    ps.setBytes(i + 1, (byte [])ConvertUtils.convert(inValue, byte [].class));
                } else {
                    ps.setObject(i + 1, inValue);
                }
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }
    }
}

