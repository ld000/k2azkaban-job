package job.ldp;

import com.google.common.collect.Lists;
import com.k2data.platform.etl.ETLTool;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lidong 12/1/16.
 */
public class MachineTransHisJob {

    public void run() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);

        ETLTool.transportLDPData(path + "/mappings/machineTransHis.json", list -> {
            List<Map<String, Object>> result = Lists.newArrayList();

            for (Map<String, Object> obj : list) {
                if (handleTransStatus(obj)) {
                    result.add(obj);
                }
            }

            return result;
        });

//        lgMachineTransportHistoryDao.updateDealerId();
    }

    private static Boolean handleTransStatus(Map<String, Object> obj) {
        String sourceTransStatus = Objects.toString(obj.get("TRANS_STATUS"));
        String overFlag;
        switch (sourceTransStatus) {
            case "已到达":
                overFlag = "1";
                break;
            case "已接车":
                overFlag = "1";
                break;
            case "已出发":
                overFlag = "0";
                break;
            case "未接车":
                overFlag = "0";
                break;
            default:
//                jobLogService.saveSingleLog(context.getJobDetail().getKey().getName(),
//                        context.getJobDetail().getKey().getGroup(),
//                        0,
//                        1,
//                        "machineTransHis, transStatus not found! id: " + obj.get("ID") + ", value: " + sourceTransStatus);
                return false;
        }
        obj.put("TRANS_STATUS", overFlag);

        return true;
    }

}
