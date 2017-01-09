package com.k2data.job.ldp;

import com.google.common.collect.Lists;
import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.job.common.JobUtils;
import com.k2data.platform.etl.ETLTool;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lidong 12/1/16.
 */
public class MachineTransHisJob implements BaseJob {

    @Override
    public void run() {
        ETLTool.pullLDPData(JobUtils.getRootPath() + "mappings/machineTransHis.json", list -> {
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
