package com.k2data.job.ldp;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.platform.persistence.SqlRunner;

/**
 * @author lidong 16-12-12.
 */
public class UpdateTransHisDealerIdJob implements BaseJob {

    @Override
    public void run() {
        String updateDealIdSql = "UPDATE lg_machineTransportHistory a" +
                "        SET a.dealerId = (" +
                "            SELECT b.id" +
                "            FROM lg_dealerInChanNet b" +
                "            WHERE a.chanNetId = b.chanNetId" +
                "            AND a.supplyId = b.supplyId" +
                "        )";
        SqlRunner.update(updateDealIdSql);
    }
    
}
