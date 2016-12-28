package job.persistence.resultset;

import com.k2data.job.exception.InternalException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link ResultSet} 映射到 {@link Map}
 *
 * @author lidong 16-8-5.
 */
public class MapRowMapper implements RowMapper<Map<String, Object>> {
    final List<String> columnNames = new ArrayList<String>();

    @Override
    public Map<String, Object> mapRow(ResultSet rs) {
        if (rs == null)
            throw new InternalException("ResultSet should not be null.");

        final Map<String, Object> map = new HashMap<String, Object>();

        try {
            if (columnNames.isEmpty()) {
                final ResultSetMetaData rsmd = rs.getMetaData();

                int count = rsmd.getColumnCount();

                for (int i = 0; i < count; i++) {
                    columnNames.add(rsmd.getColumnName(i + 1).toUpperCase());
                }
            }

            for (String columnName : columnNames) {
                map.put(columnName, rs.getObject(columnName));
            }
        } catch (SQLException e) {
            throw new InternalException(e);
        }

        return map;
    }
}
