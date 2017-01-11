package com.k2data.job.gpstemplate;

import com.k2data.job.common.BaseJob;
import com.k2data.platform.annotation.Influx;
import com.k2data.platform.kmx.KmxClient;
import com.k2data.platform.kmx.cond.KmxCond;
import com.k2data.platform.kmx.domain.KmxDataRowsRspDomain;
import com.k2data.platform.persistence.SqlRunner;

import java.util.List;

/**
 * @author lidong 17-1-4.
 */
@Influx(measurement = "job_log", tag = {"from:kmx", "type:gps_contrast"})
public class GpsContrastKmxJob implements BaseJob {

    @Override
    public long run() {
        List<String> gpsNoList = SqlRunner.selectList(String.class,
                "SELECT gpsNo FROM lg_machinegpscontrast a WHERE a.gpsTemplateNo is null or gpsTemplateNo = ''");

        if (gpsNoList == null || gpsNoList.isEmpty()) {
            return 0;
        }

        long count = 0;
        for (String gpsNo : gpsNoList) {
            KmxCond cond = KmxCond.devicesListV2()
                    .device(gpsNo)
                    .build();

            KmxDataRowsRspDomain rsp = KmxClient.getSync(cond);

            if (rsp != null && rsp.getDevice() != null) {
                count += SqlRunner.update("update lg_machinegpscontrast SET gpsTemplateNo = ? where gpsno = ?",
                        rsp.getDevice().getDeviceTypeId(),
                        gpsNo);
            }
        }

        return count;
    }

}
