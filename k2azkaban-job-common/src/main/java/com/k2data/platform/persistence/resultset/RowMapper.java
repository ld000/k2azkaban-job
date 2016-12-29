package com.k2data.platform.persistence.resultset;

import java.sql.ResultSet;

/**
 * {@link ResultSet} 映射到其他对象接口
 *
 * @param <T>
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs);
}
