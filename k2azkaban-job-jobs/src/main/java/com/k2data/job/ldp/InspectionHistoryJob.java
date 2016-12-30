package com.k2data.job.ldp;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.platform.etl.ETLTool;

/**
 * @author lidong 16-11-14.
 */
public class InspectionHistoryJob extends BaseJob {

    public static void main(String[] args) throws Exception {
        runJob(new InspectionHistoryJob());
    }

    @Override
    public void run() {
        ETLTool.transportLDPData(getPath() + "mappings/inspectionHistory.json");
    }

}
