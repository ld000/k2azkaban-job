package com.k2data.platform.persistence.callback;

import java.sql.Connection;

public interface ConnectionCallback<T> {

    T doInConnection(Connection conn);

}
