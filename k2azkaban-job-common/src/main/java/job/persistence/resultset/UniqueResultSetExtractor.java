package job.persistence.resultset;

import com.k2data.job.exception.InternalException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UniqueResultSetExtractor<T> implements ResultSetExtractor<T> {
    private RowMapper<T> rowMapper;
    
    public UniqueResultSetExtractor(final RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T extractData(final ResultSet rs) {
        final List<T> results = new ArrayList<T>();
        
        try {
            while (rs.next()) {
                if (!results.isEmpty()) {
                    throw new InternalException("ResultSet expects unique result, but has more than one results");
                }
                
                results.add(this.rowMapper.mapRow(rs));
            }
        } catch (SQLException sqle) {
            throw new InternalException(sqle);
        }
        
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }
}
