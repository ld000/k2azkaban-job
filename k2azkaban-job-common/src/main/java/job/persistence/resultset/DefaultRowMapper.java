package job.persistence.resultset;

import com.k2data.job.exception.InternalException;
import com.k2data.job.utils.BeanUtils;
import com.k2data.job.utils.ClassUtils;
import com.k2data.job.utils.ConvertUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class DefaultRowMapper<T> implements RowMapper<T> {
    private Class<T> requiredType;

    public DefaultRowMapper(final Class<T> requiredType) {
        this.requiredType = requiredType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T mapRow(final ResultSet rs) {
        try {
            if (ClassUtils.isJdkType(requiredType)) {
                final ResultSetMetaData rsmd = rs.getMetaData();

                if (rsmd.getColumnCount() > 2) {
                    throw new SQLException("ResultSet expects 1 column, but provides " + rsmd.getColumnCount());
                }

                // 特殊判断Date类型
                if (requiredType.equals(Time.class)) {
                    return (T) rs.getTime(1);
                } else if (requiredType.equals(java.sql.Date.class)) {
                    return (T) new java.sql.Date(rs.getTimestamp(1).getTime());
                } else if (Date.class.isAssignableFrom(requiredType)) {
                    return (T) rs.getTimestamp(1);
                } else {
                    return (T) ConvertUtils.convert(rs.getObject(1), requiredType);
                }
            } else {
                final T result = ClassUtils.newInstance(requiredType);

                BeanUtils.copyProperties(rs, result);

                return result;
            }
        } catch (SQLException sqle) {
            throw new InternalException(sqle);
        }
    }

}
