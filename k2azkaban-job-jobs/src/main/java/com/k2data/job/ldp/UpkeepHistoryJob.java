package com.k2data.job.ldp;

import com.k2data.platform.etl.ETLTool;

import java.io.File;

/**
 * @author lidong 12/1/16.
 */
public class UpkeepHistoryJob {

    public static void main(String[] args) throws Exception {
//        String propsFile = System.getenv("JOB_PROP_FILE");
//        Properties prop = new Properties();
//        prop.load(new BufferedReader(new FileReader(propsFile)));
//
//        String jobName = System.getenv("JOB_NAME");

        UpkeepHistoryJob job = new UpkeepHistoryJob();
        job.run();
    }

    public void run() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);

        ETLTool.transportLDPData(path + "/mappings/upkeepHistory.json");
    }

}
