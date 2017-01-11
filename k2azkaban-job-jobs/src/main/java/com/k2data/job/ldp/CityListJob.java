package com.k2data.job.ldp;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobUtils;
import com.k2data.platform.annotation.Influx;
import com.k2data.platform.etl.ETLTool;

/**
 * @author lidong 16-11-11.
 */
@Influx(measurement = "job_log", tag = {"from:ldp", "type:city_list"})
public class CityListJob implements BaseJob {

    @Override
    public long run() {
        return ETLTool.pullLDPData(JobUtils.getRootPath() + "mappings/cityList.json");
    }

}
