package com.k2data.job.gpstemplate;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.platform.kmx.KmxClient;
import com.k2data.platform.kmx.cond.KmxDevicesListV2Cond;
import com.k2data.platform.kmx.domain.KmxDataRowsRspDomain;
import com.k2data.platform.persistence.SqlRunner;

import java.util.List;

/**
 * @author lidong 17-1-4.
 */
public class GpsContrastKmxJob implements BaseJob {

    @Override
    public void run() {
        List<String> gpsNoList = SqlRunner.selectList(String.class,
                "SELECT gpsNo FROM lg_machinegpscontrast a WHERE a.gpsTemplateNo is null or gpsTemplateNo = ''");

        if (gpsNoList == null || gpsNoList.isEmpty()) {
            return;
        }

        for (String gpsNo : gpsNoList) {
            KmxDevicesListV2Cond cond = new KmxDevicesListV2Cond();
            cond.setDeviceNo(gpsNo);
            KmxDataRowsRspDomain rsp = KmxClient.getSync(cond);

            if (rsp != null && rsp.getDevice() != null) {
                SqlRunner.update("update lg_machinegpscontrast SET gpsTemplateNo = ? where gpsno = ?",
                        rsp.getDevice().getDeviceTypeId(),
                        gpsNo);
            }
        }
    }

}
