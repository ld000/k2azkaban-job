package job.persistence.resultset;

import java.sql.ResultSet;
import java.util.Map;

public interface PairResultSetExtractor<K, V> {
	Map<K, V> extractData(ResultSet rs);
}
