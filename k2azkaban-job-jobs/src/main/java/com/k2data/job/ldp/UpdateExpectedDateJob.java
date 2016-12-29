package com.k2data.job.ldp;

import com.k2data.platform.persistence.SqlRunner;

/**
 * @author lidong 16-12-9.
 */
public class UpdateExpectedDateJob {

    public static void main(String[] args) throws Exception {
//        String propsFile = System.getenv("JOB_PROP_FILE");
//        Properties prop = new Properties();
//        prop.load(new BufferedReader(new FileReader(propsFile)));
//
//        String jobName = System.getenv("JOB_NAME");

        UpdateExpectedDateJob job = new UpdateExpectedDateJob();
        job.run();
    }

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
