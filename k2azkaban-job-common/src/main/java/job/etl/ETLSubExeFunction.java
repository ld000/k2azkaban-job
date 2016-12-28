package job.etl;

import java.util.List;
import java.util.Map;

/**
 * @author lidong 16-11-2.
 */
@FunctionalInterface
public interface ETLSubExeFunction {

    int update(final ETLMappingDomain domain, final List<Map<String, Object>> subSource);

}
