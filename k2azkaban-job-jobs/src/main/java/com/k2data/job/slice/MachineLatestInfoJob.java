package com.k2data.job.slice;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.k2data.job.common.BaseJob;
import com.k2data.platform.general.GeneralQueryService;
import com.k2data.job.slice.support.SliceSqlProvider;
import com.k2data.platform.domain.MachineLatestInfo;
import com.k2data.platform.kmx.KmxClient;
import com.k2data.platform.kmx.KmxUtils;
import com.k2data.platform.kmx.SensorNameEnum;
import com.k2data.platform.kmx.cond.KmxCond;
import com.k2data.platform.kmx.domain.KmxDataPointsDomain;
import com.k2data.platform.kmx.domain.KmxDataPointsRspDomain;
import com.k2data.platform.persistence.SqlRunner;
import com.k2data.platform.persistence.support.BoundSqlBuilder;
import com.k2data.platform.utils.IdGen;
import com.k2data.platform.utils.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lidong 17-1-4.
 */
public class MachineLatestInfoJob implements BaseJob {

    @Override
    public long run() {
        List<String> sensors = Lists.newArrayList();
        sensors.add(SensorNameEnum.LATITUDE_NUM.getSensorName());
        sensors.add(SensorNameEnum.LONGITUDE_NUM.getSensorName());
        sensors.add(SensorNameEnum.ADDRESS.getSensorName());
        sensors.add(SensorNameEnum.PROVINCE.getSensorName());
        sensors.add(SensorNameEnum.CITY.getSensorName());

        List<String> gpsNoList = GeneralQueryService.queryGpsNo();

        for (String gpsNo : gpsNoList) {
            KmxCond cond = KmxCond.dataPointsV3()
                    .device(gpsNo)
                    .sensors(sensors)
                    .before()
                    .timestamp(new Date().getTime())
                    .build();

            KmxDataPointsRspDomain domain = KmxClient.getSync(cond);

            if (domain != null && domain.getDataPoints() != null) {
                List<KmxDataPointsDomain> kmxDataPointsDomainList = domain.getDataPoints();

                Map<String, KmxDataPointsDomain> map =
                        Maps.uniqueIndex(kmxDataPointsDomainList, kmxDataPointsDomain -> kmxDataPointsDomain.getSensor());

                MachineLatestInfo entity = new MachineLatestInfo();
                entity.setId(IdGen.getUUID());
                entity.setDeviceNo(gpsNo);
                entity.setLatitude(StringUtils.toDouble(map.get(SensorNameEnum.LATITUDE_NUM.getSensorName()).getValue()));
                entity.setLongitude(StringUtils.toDouble(map.get(SensorNameEnum.LONGITUDE_NUM.getSensorName()).getValue()));

                String address = String.valueOf(map.get(SensorNameEnum.ADDRESS.getSensorName()).getValue());
                entity.setAddress(KmxUtils.safeString(address));
                String province = String.valueOf(map.get(SensorNameEnum.PROVINCE.getSensorName()).getValue());
                entity.setProvince(KmxUtils.safeString(province));
                String city = String.valueOf(map.get(SensorNameEnum.CITY.getSensorName()).getValue());
                entity.setCity(KmxUtils.safeString(city));
                entity.setPosition("");
                entity.setSliceStop(new Date(map.get(SensorNameEnum.LATITUDE_NUM.getSensorName()).getTimestamp()));

                if (SqlRunner.update(SliceSqlProvider.updateLatestInfoByGpsNo(entity)) == 0) {
                    SqlRunner.insert(BoundSqlBuilder.buildInsertBoundSql(entity));
                }
            }
        }

        return gpsNoList.size();
    }

}
