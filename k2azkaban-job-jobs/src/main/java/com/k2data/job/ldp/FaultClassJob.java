package com.k2data.job.ldp;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.job.common.JobUtils;
import com.k2data.platform.etl.ETLTool;

/**
 * @author lidong 16-11-14.
 */
public class FaultClassJob implements BaseJob {

    public static void main(String[] args) throws Exception {
        FaultClassJob job = JobProxyFactory.getJdkProxy(FaultClassJob.class);
        job.run();
    }

    @Override
    public void run() {
        ETLTool.transportLDPData(JobUtils.getRootPath() + "mappings/faultClass.json");
    }

}
