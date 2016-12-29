package job.ldp;

import com.k2data.platform.etl.ETLTool;

import java.io.File;

/**
 * @author lidong 16-11-14.
 */
public class InspectionHistoryJob {

    public void run() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);

        ETLTool.transportLDPData(path + "/mappings/inspectionHistory.json");
    }

}
