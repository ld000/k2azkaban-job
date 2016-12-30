package com.k2data.job.ldp;

import com.k2data.job.common.BaseJob;
import com.k2data.platform.etl.ETLTool;

/**
 * @author lidong 16-11-11.
 */
public class CityListJob extends BaseJob {

    public static void main(String[] args) throws Exception {
        runJob(new CityListJob());
    }

    @Override
    public void run() {
        ETLTool.transportLDPData(getPath() + "mappings/cityList.json");
    }

}
