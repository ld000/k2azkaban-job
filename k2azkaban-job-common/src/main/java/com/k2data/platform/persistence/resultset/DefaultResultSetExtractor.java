package com.k2data.platform.persistence.resultset;

import com.k2data.platform.exception.InternalException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetExtractor<T> implements ResultSetExtractor<List<T>> {
    private RowMapper<T> rowMapper;
    
    public DefaultResultSetExtractor(final RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> extractData(final ResultSet rs) {
        final List<T> results = new ArrayList<T>();
        
        try {
            while (rs.next()) {
                results.add(this.rowMapper.mapRow(rs));
            }
        } catch (SQLException sqle) {
            throw new InternalException(sqle);
        }
        
        return results;
    }
}
