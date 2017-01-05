package com.k2data.job.ldp;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.platform.persistence.SqlRunner;

/**
 * @author lidong 16-12-9.
 */
public class UpdateExpectedDateJob implements BaseJob {

    public static void main(String[] args) throws Exception {
        BaseJob job = JobProxyFactory.getJdkProxy(UpkeepHistoryJob.class);
        job.run();
    }

    @Override
    public void run() {
        String sql = "UPDATE lg_machineTransportHistory a" +
                "        SET a.expectedDeliveryDate = (" +
                "            SELECT DATE_ADD(b.transportDate,INTERVAL t.datelimit day)" +
                "              FROM (SELECT * FROM lg_machineTransportHistory) b " +    // -- mysql...........
                "              LEFT JOIN lg_machineDimension t ON t.dimensionType = 3 AND b.supplyId = t.ldpid" +
                "             WHERE a.id = b.id" +
                "               AND t.datelimit <> 0" +
                "        )";

        SqlRunner.update(sql);
    }

}
