package com.k2data.platform.persistence.callback;

import java.sql.Connection;
import java.util.Map;

public interface PairConnectionCallback<K, V> {

    Map<K, V> doInConnection(Connection conn);

}
