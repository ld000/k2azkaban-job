package com.k2data.platform.persistence.resultset;

import java.sql.ResultSet;
import java.util.Map;

public interface PairResultSetExtractor<K, V> {
	Map<K, V> extractData(ResultSet rs);
}
