package com.k2data.job.ldp;

import com.google.common.collect.Lists;
import com.k2data.platform.etl.ETLTool;
import com.k2data.platform.persistence.SqlRunner;
import com.k2data.platform.utils.StringUtils;
import com.k2data.job.common.GeneralQueryService;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lidong 12/1/16.
 */
public class MachineProfileJob {

    public static void main(String[] args) throws Exception {
//        String propsFile = System.getenv("JOB_PROP_FILE");
//        Properties prop = new Properties();
//        prop.load(new BufferedReader(new FileReader(propsFile)));
//
//        String jobName = System.getenv("JOB_NAME");

        MachineProfileJob job = new MachineProfileJob();
        job.run();
    }

    public void run() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);

        ETLTool.transportLDPData(path + "/mappings/machineProfile.json", list -> {
            SqlRunner.update("DELETE from lg_machineProfileNoGps");

            List<Map<String, Object>> result = Lists.newArrayList();

            for (Map<String, Object> objMap: list) {
                if (handleMachineStatus(objMap)) {
                    result.add(objMap);
                }
            }

            return result;
        });
    }

    private static Boolean handleMachineStatus(Map<String, Object> objMap) {
        String sourceMachineStatus = Objects.toString(objMap.get("MACHINE_STATUS"));
        String machineStatus = GeneralQueryService.queryDictValue(sourceMachineStatus, "machineStatus", null);

        if (StringUtils.isBlank(machineStatus)) {
//            jobLogService.saveSingleLog(context.getJobDetail().getKey().getName(),
//                    context.getJobDetail().getKey().getGroup(),
//                    0,
//                    1,
//                    "machineProfile, machineStatus not found! id: " + objMap.get("MSS_MACHINE_ID") + ", value: " + sourceMachineStatus);

            return false;
        }

        objMap.put("MACHINE_STATUS", machineStatus);

        return true;
    }

}