package job.common;

import com.k2data.platform.domain.MachineDimension;
import job.common.mapper.GeneralQueryDao;

import java.util.List;

/**
 * @author lidong 16-11-11.
 */
public class GeneralQueryService {

    private static GeneralQueryDao generalQueryDao = PersistenceHelper.getMapper(GeneralQueryDao.class);

    public static String queryDictValue(String label, String type, String defaultValue) {
        String value = generalQueryDao.queryDictValue(label, type);

        if (value == null)
            return defaultValue;

        return value;
    }

    public static List<MachineDimension> queryMachineDimensionList(Integer type) {
        return generalQueryDao.queryMachineDimensionList(type);
    }

}
