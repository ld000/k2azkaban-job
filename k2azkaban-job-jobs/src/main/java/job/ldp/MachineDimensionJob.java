package job.ldp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.k2data.platform.domain.MachineDimension;
import com.k2data.platform.etl.ETLTool;
import job.common.GeneralQueryService;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author lidong 12/1/16.
 */
public class MachineDimensionJob {

    public void run() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);

        List<MachineDimension> source = GeneralQueryService.queryMachineDimensionList(3);
        Map<String, MachineDimension> sourceMap = Maps.uniqueIndex(source, machineDimension -> machineDimension.getDimensionName());

        ETLTool.transportLDPData(path + "/mappings/machineDimension.json", list -> {
            List<Map<String, Object>> result = Lists.newArrayList();
            for (Map<String, Object> obj : list) {
                if ("03".equals(obj.get("TYPE"))) {
                    MachineDimension md = sourceMap.get(obj.get("NAME").toString());
                    Integer limit = md == null ? 0 : md.getDateLimit();
                    obj.put("DATELIMIT", limit);
                }

                result.add(obj);
            }

            return result;
        });
    }

}
