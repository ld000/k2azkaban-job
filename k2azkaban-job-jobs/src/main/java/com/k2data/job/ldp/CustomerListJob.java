package com.k2data.job.ldp;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.job.common.JobUtils;
import com.k2data.platform.etl.ETLTool;

/**
 * @author lidong 16-11-11.
 */
public class CustomerListJob implements BaseJob {

    public static void main(String[] args) throws Exception {
        CustomerListJob job = JobProxyFactory.getJdkProxy(CustomerListJob.class);
        job.run();
    }

    @Override
    public void run() {
        ETLTool.transportLDPData(JobUtils.getRootPath() + "mappings/customerList.json");
    }

}
