package com.k2data.platform.persistence.resultset;

import java.sql.ResultSet;

public interface ResultSetExtractor<T> {
    T extractData(ResultSet rs);
}
