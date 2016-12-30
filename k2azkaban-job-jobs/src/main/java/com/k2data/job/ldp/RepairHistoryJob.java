package com.k2data.job.ldp;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.job.common.JobUtils;
import com.k2data.platform.etl.ETLTool;

/**
 * @author lidong 12/1/16.
 */
public class RepairHistoryJob implements BaseJob {

    public static void main(String[] args) throws Exception {
        RepairHistoryJob job = JobProxyFactory.getJdkProxy(RepairHistoryJob.class);
        job.run();
    }

    @Override
    public void run() {
        ETLTool.transportLDPData(JobUtils.getRootPath() + "mappings/repairHistory.json");
    }

}
