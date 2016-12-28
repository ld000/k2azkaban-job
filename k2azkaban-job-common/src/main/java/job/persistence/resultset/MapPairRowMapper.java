package job.persistence.resultset;

import com.k2data.job.exception.InternalException;
import com.k2data.job.utils.ConvertUtils;
import com.k2data.job.utils.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.Map;

public class MapPairRowMapper<K, V> implements PairRowMapper<K, V> {
    /** Map的key的所需类型 **/
    private Class<K> keyType;
    /** Map的value的所需类型 **/
    private Class<V> valueType;
    /** 指定的列名，其对应值为map的key，如果为空，就选第一列 **/
    private String keyColumnName;
    /** 指定的列名，其对应值为map的value，如果为空，就选第二列 **/
    private String valueColumnName;
    
    public MapPairRowMapper(final Class<K> keyType, final Class<V> valueType, final String keyColumnName, final String valueColumnName) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.keyColumnName = keyColumnName;
        this.valueColumnName = valueColumnName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void mapRow(final ResultSet rs, Map<K, V> map) {
        if (rs == null) {
            throw new IllegalArgumentException("ResultSet should not be null.");
        }

        try {
            K key;
            V value;
            
            // 特殊判断Date类型
            if (keyType.equals(Time.class)) {
                if (StringUtils.isBlank(keyColumnName)) {
                    key = (K) rs.getTime(1);
                } else {
                    key = (K) rs.getTime(keyColumnName);
                }
            } else if (keyType.equals(java.sql.Date.class)) {
                if (StringUtils.isBlank(keyColumnName)) {
                    key = (K) new java.sql.Date(rs.getTimestamp(1).getTime());
                } else {
                    key = (K) new java.sql.Date(rs.getTimestamp(keyColumnName).getTime());
                }
            } else if (Date.class.isAssignableFrom(keyType)) {
                if (StringUtils.isBlank(keyColumnName)) {
                    key = (K) rs.getTimestamp(1);
                } else {
                    key = (K) rs.getTimestamp(keyColumnName);
                }
            } else {
                if (StringUtils.isBlank(keyColumnName)) {
                    key = (K) ConvertUtils.convert(rs.getObject(1), keyType);
                } else {
                    key = (K) ConvertUtils.convert(rs.getObject(keyColumnName), keyType);
                }
            }
            
            // 特殊判断Date类型
            if (valueType.equals(Time.class)) {
                if (StringUtils.isBlank(keyColumnName)) {
                    value = (V) rs.getTime(2);
                } else {
                    value = (V) rs.getTime(valueColumnName);
                }
            } else if (valueType.equals(java.sql.Date.class)) {
                if (StringUtils.isBlank(keyColumnName)) {
                    value = (V) new java.sql.Date(rs.getTimestamp(2).getTime());
                } else {
                    value = (V) new java.sql.Date(rs.getTimestamp(valueColumnName).getTime());
                }
            } else if (Date.class.isAssignableFrom(valueType)) {
                if (StringUtils.isBlank(keyColumnName)) {
                    value = (V) rs.getTimestamp(2);
                } else {
                    value = (V) rs.getTimestamp(valueColumnName);
                }
            } else {
                if (StringUtils.isBlank(keyColumnName)) {
                    value = (V) ConvertUtils.convert(rs.getObject(2), valueType);
                } else {
                    value = (V) ConvertUtils.convert(rs.getObject(valueColumnName), valueType);
                }
            }
            map.put(key, value);
        } catch (SQLException sqle) {
            throw new InternalException(sqle);
        }
    }
}
