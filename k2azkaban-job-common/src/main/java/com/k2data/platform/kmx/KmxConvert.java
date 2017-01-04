package com.k2data.platform.kmx;

import com.k2data.platform.kmx.domain.KmxDataPointsDomain;
import com.k2data.platform.kmx.domain.KmxDataRowsDomain;
import com.k2data.platform.kmx.domain.KmxDataRowsRspDomain;
import com.k2data.platform.utils.StringUtils;

import java.util.*;

/**
 * Created by yew on 16-10-23.
 * KMX 结构转换
 */
public class KmxConvert {

    /**
     * 对象转List<Map<String, Object>>
     */
    public static List<Map<String, Object>> objectToList(final KmxDataRowsRspDomain rsp) {

        return Optional.ofNullable(rsp).map(r -> {
                List<Map<String, Object>> sliceList = new ArrayList<>();

                for (KmxDataRowsDomain dataRows : r.getDataRows()) {
                    Map<String, Object> rowsMap = new HashMap<>();

                    if (dataRows.getDataPointsCount() > 0) {

                        rowsMap.put("iso", dataRows.getIso());
                        rowsMap.put("dataPointsCount", dataRows.getDataPointsCount());

                        for (KmxDataPointsDomain dataPoints : dataRows.getDataPoints()) {
                            String sensor = dataPoints.getSensor();
                            String value = "null".equals(dataPoints.getValue()) ? "" : dataPoints.getValue();

                            rowsMap.put(sensor, value);
                        }
                        sliceList.add(rowsMap);
                    }
                }
                return sliceList;
            }
        ).orElse(Collections.emptyList());
    }

    /**
     * NVR时长 转换为分钟
     */
    public static int convertDuration(final int src) {
        return src * 3;
    }

    /**
     * 纠正NVR 累计时长 超大数字 高低位转换
     */
    private static int verify(final long e) {
        return ((e & 0x0000ffff)) == 0 && (e >> 16) > 0 ?
                (int) (((e & 0xff000000) >> 24) | ((e & 0xff0000) >> 8)
                        | ((e & 0xff00) << 8) | ((e & 0xff) << 24))
                : (int) e;
    }

    /**
     * 纠正NVR 累计时长 超大数字 高低位转换
     */
    public static int verifyDuration(final Object str) {
        return verify(StringUtils.toLong(str));
    }

    /**
     * 时间均加一固定值 90s
     */
    public static Date timeAdd(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, 90); //时间均加 90s
        return calendar.getTime();
    }

}
