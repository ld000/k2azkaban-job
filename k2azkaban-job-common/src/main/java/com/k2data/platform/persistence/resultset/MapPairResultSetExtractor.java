package com.k2data.platform.persistence.resultset;

import com.k2data.platform.exception.InternalException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapPairResultSetExtractor<K, V> implements PairResultSetExtractor<K, V> {
    private PairRowMapper<K, V> rowMapper;
    
    public MapPairResultSetExtractor(final PairRowMapper<K, V> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public Map<K, V> extractData(final ResultSet rs) {
        final Map<K, V> map = new HashMap<K, V>();
        
        try {
            while (rs.next()) {
                this.rowMapper.mapRow(rs, map);
            }
        } catch (SQLException sqle) {
            throw new InternalException(sqle);
        }
        
        return map;
    }
}
