package com.k2data.job.ldp;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.job.common.JobUtils;
import com.k2data.platform.etl.ETLTool;

/**
 * @author lidong 16-11-11.
 */
public class DealerInChanNetJob implements BaseJob {

    public static void main(String[] args) throws Exception {
        BaseJob job = JobProxyFactory.getJdkProxy(DealerInChanNetJob.class);
        job.run();
    }

    @Override
    public void run() {
        ETLTool.pullLDPData(JobUtils.getRootPath() + "mappings/dealerInChannet.json");
    }

}
