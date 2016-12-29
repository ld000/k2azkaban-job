package job.ldp;

import com.google.common.collect.Lists;
import com.k2data.platform.etl.ETLTool;
import job.common.GeneralQueryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lidong 16-11-11.
 */
public class ChanNetListJob {

    private static Logger logger = LogManager.getLogger(ChanNetListJob.class);

    public void run() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);

        ETLTool.transportLDPData(path + "/mappings/chanNetList.json", list -> {
            List<Map<String, Object>> result = Lists.newArrayList();
            for (Map<String, Object> objMap : list) {
                if (handleMachineType(objMap)) {
                    result.add(objMap);
                }
            }

            return result;
        });
    }

    private static Boolean handleMachineType(Map<String, Object> objMap) {
        String sourceMachineType = Objects.toString(objMap.get("CHANNEL_NAME"));
        String machineType = GeneralQueryService.queryDictValue(sourceMachineType, "machineType", null);

        if (StringUtils.isBlank(machineType)) {
            logger.warn("chanNetList, machineType not found! id: " + objMap.get("SCC_CHAN_NET_ID") + ", value: " + sourceMachineType);
            return false;
        }

        objMap.put("CHANNEL_NAME", machineType);

        return true;
    }

}
