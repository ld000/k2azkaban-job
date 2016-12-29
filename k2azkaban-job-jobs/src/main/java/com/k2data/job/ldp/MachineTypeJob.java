package com.k2data.job.ldp;

import com.google.common.collect.Lists;
import com.k2data.job.common.GeneralQueryService;
import com.k2data.platform.etl.ETLTool;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lidong 12/1/16.
 */
public class MachineTypeJob {

    public static void main(String[] args) throws Exception {
//        String propsFile = System.getenv("JOB_PROP_FILE");
//        Properties prop = new Properties();
//        prop.load(new BufferedReader(new FileReader(propsFile)));
//
//        String jobName = System.getenv("JOB_NAME");

        MachineTypeJob job = new MachineTypeJob();
        job.run();
    }

    public void run() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);

        ETLTool.transportLDPData(path + "/mappings/machineType.json", list -> {
            List<Map<String, Object>> result = Lists.newArrayList();

            for (Map<String, Object> obj : list) {
                String sourceMachineType = Objects.toString(obj.get("SCC_CHANNEL_NAME"));
                String machineType = GeneralQueryService.queryDictValue(sourceMachineType, "machineType", null);

                if (StringUtils.isBlank(machineType)) {
//                    jobLogService.saveSingleLog(context.getJobDetail().getKey().getName(),
//                            context.getJobDetail().getKey().getGroup(),
//                            0,
//                            1,
//                            "machineType, machineType not found! id: " + obj.get("MSS_PRODUCT_ID") + ", value: " + sourceMachineType);
                } else {
                    obj.put("SCC_CHANNEL_NAME", machineType);

                    result.add(obj);
                }
            }

            return result;
        });
    }

}
