package job.persistence.resultset;

import java.sql.ResultSet;

public interface ResultSetExtractor<T> {
    T extractData(ResultSet rs);
}
