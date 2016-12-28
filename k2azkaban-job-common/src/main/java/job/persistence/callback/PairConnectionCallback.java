package job.persistence.callback;

import java.sql.Connection;
import java.util.Map;

public interface PairConnectionCallback<K, V> {

    Map<K, V> doInConnection(Connection conn);

}
