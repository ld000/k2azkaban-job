package com.k2data.platform.persistence.resultset;

import java.sql.ResultSet;
import java.util.Map;

public interface PairRowMapper<K, V> {
    void mapRow(ResultSet rs, Map<K, V> map);
}
