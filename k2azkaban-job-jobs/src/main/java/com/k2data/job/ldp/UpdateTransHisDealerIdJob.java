package com.k2data.job.ldp;

import com.k2data.platform.persistence.SqlRunner;

/**
 * @author lidong 16-12-12.
 */
public class UpdateTransHisDealerIdJob {

    public static void main(String[] args) throws Exception {
//        String propsFile = System.getenv("JOB_PROP_FILE");
//        Properties prop = new Properties();
//        prop.load(new BufferedReader(new FileReader(propsFile)));
//
//        String jobName = System.getenv("JOB_NAME");

        UpdateTransHisDealerIdJob job = new UpdateTransHisDealerIdJob();
        job.run();
    }
    
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
