package com.k2data.job.ldp;

import com.google.common.collect.Lists;
import com.k2data.job.common.BaseJob;
import com.k2data.job.common.GeneralQueryService;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.platform.etl.ETLTool;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lidong 12/1/16.
 */
public class MachineTypeJob extends BaseJob {

    public static void main(String[] args) throws Exception {
        runJob(new MachineTypeJob());
    }

    @Override
    public void run() {
        ETLTool.transportLDPData(getPath() + "mappings/machineType.json", list -> {
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
