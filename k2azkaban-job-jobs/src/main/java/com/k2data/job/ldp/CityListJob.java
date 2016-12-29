package com.k2data.job.ldp;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyHelper;
import com.k2data.platform.etl.ETLTool;

import java.io.File;

/**
 * @author lidong 16-11-11.
 */
public class CityListJob implements BaseJob {

    public static void main(String[] args) throws Exception {
        BaseJob job = JobProxyHelper.getProxy(CityListJob.class);
        job.run();
    }

    @Override
    public void run() {
        ETLTool.transportLDPData(System.getProperty("path") + "mappings/cityList.json");
    }

}
