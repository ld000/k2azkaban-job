package com.k2data.job.ldp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.k2data.job.common.BaseJob;
import com.k2data.platform.annotation.Influx;
import com.k2data.platform.general.GeneralQueryService;
import com.k2data.job.common.JobUtils;
import com.k2data.platform.domain.MachineDimension;
import com.k2data.platform.etl.ETLTool;

import java.util.List;
import java.util.Map;

/**
 * @author lidong 12/1/16.
 */
@Influx(measurement = "job_log", tag = {"from:ldp", "type:machine_dimension"})
public class MachineDimensionJob implements BaseJob {

    @Override
    public long run() {
        List<MachineDimension> source = GeneralQueryService.queryMachineDimensionList(3);
        Map<String, MachineDimension> sourceMap = Maps.uniqueIndex(source, machineDimension -> machineDimension.getDimensionName());

        return ETLTool.pullLDPData(JobUtils.getRootPath() + "mappings/machineDimension.json", list -> {
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
