package com.k2data.job.slice.support;

import com.google.common.collect.Lists;
import com.k2data.platform.domain.LgDeviceDateStatics;
import com.k2data.platform.domain.LgDeviceSliceStatics;
import com.k2data.platform.domain.LgDeviceStatics;
import com.k2data.platform.domain.MachineLatestInfo;
import com.k2data.platform.persistence.support.BoundSql;

import java.util.List;

/**
 * @author lidong 17-1-4.
 */
public class SliceSqlProvider {

    public static BoundSql updateLatestInfoByGpsNo(MachineLatestInfo domain) {
        String sql = "UPDATE lg_machinelatestinfo SET " +
                "latitude = ?, " +
                "longitude = ?, " +
                "province = ?, " +
                "city = ?, " +
                "address = ?, " +
                "position = ?, " +
                "slicestop = ? " +
                "WHERE deviceno = ?";

        List<Object> inValues = Lists.newArrayList();
        inValues.add(domain.getLatitude());
        inValues.add(domain.getLongitude());
        inValues.add(domain.getProvince());
        inValues.add(domain.getCity());
        inValues.add(domain.getAddress());
        inValues.add(domain.getPosition());
        inValues.add(domain.getSliceStop());
        inValues.add(domain.getDeviceNo());

        return new BoundSql(sql, inValues.toArray());
    }
    
    public static BoundSql insertNoneDateSlice(LgDeviceDateStatics domain) {
        String sql = "INSERT INTO lg_devicedatestatics " +
                "select " +
                "replace(uuid(),'-',''), " +
                "sysdate(), " +
                "?, " +
                "?, " +
                "workCount, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "runDurationTotal, " +
                "workDurationTotal, " +
                "runOffTotal, " +
                "?, " +
                "?, " +
                "?, " +
                "latitude, " +
                "longitude, " +
                "srcLatitude, " +
                "srcLongitude, " +
                "province, " +
                "city, " +
                "address, " +
                "position, " +
                "? " +
                "from lg_devicedatestatics " +
                "where deviceNo=? " +
                "and workDate=date_sub(date(?), interval 1 day)";
        
        Object[] inValues = new Object[] {
                domain.getDeviceNo(),
                domain.getWorkDate(),
                domain.getWorkBegin(),
                domain.getWorkStop(),
                domain.getSliceRunDuration(),
                domain.getSliceWorkDuration(),
                domain.getRunoffCount(),
                domain.getOilSum(),
                domain.getOilAvg(),
                domain.getRotationlSpeedMax(),
                domain.getAlarmCount(),
                domain.getDeviceNo(),
                domain.getWorkDate()
        };

        return new BoundSql(sql, inValues);
    }
    
    public static BoundSql insertDateSlice(LgDeviceDateStatics domain) {
        String sql = "INSERT INTO lg_devicedatestatics( " +
                "            id, " +
                "            inserttime, " +
                "            deviceno, " +
                "            workdate, " +
                "            workcount, " +
                "            workbegin, " +
                "            workstop, " +
                "            slicerunduration, " +
                "            sliceworkduration, " +
                "            runoffcount, " +
                "            rundurationtotal, " +
                "            workdurationtotal, " +
                "            runofftotal, " +
                "            oilsum, " +
                "            oilavg, " +
                "            rotationlspeedmax, " +
                "            latitude, " +
                "            longitude, " +
                "            srclatitude, " +
                "            srclongitude, " +
                "            province, " +
                "            city, " +
                "            address, " +
                "            position, " +
                "            alarmcount " +
                "        ) VALUES ( " +
                "            replace(uuid(),'-',''), " +
                "            sysdate(), " +
                "            ?, " +
                "            #{workDate}, " +
                "            #{workCount}, " +
                "            #{workBegin}, " +
                "            #{workStop}, " +
                "            #{sliceRunDuration}, " +
                "            #{sliceWorkDuration}, " +
                "            #{runoffCount}, " +
                "            #{runDurationTotal}, " +
                "            #{workDurationTotal}, " +
                "            #{runOffTotal}, " +
                "            #{oilSum}, " +
                "            #{oilAvg}, " +
                "            #{rotationlSpeedMax}, " +
                "            #{latitude}, " +
                "            #{longitude}, " +
                "            #{srcLatitude}, " +
                "            #{srcLongitude}, " +
                "            #{province}, " +
                "            #{city}, " +
                "            #{address}, " +
                "            #{position}, " +
                "            #{alarmCount} " +
                "        )";

        Object[] inValues = new Object[] {
                domain.getDeviceNo(),
                domain.getWorkDate(),
                domain.getWorkCount(),
                domain.getWorkBegin(),
                domain.getWorkStop(),
                domain.getSliceRunDuration(),
                domain.getSliceWorkDuration(),
                domain.getRunoffCount(),
                domain.getRunDurationTotal(),
                domain.getWorkDurationTotal(),
                domain.getRunOffTotal(),
                domain.getOilSum(),
                domain.getOilAvg(),
                domain.getRotationlSpeedMax(),
                domain.getLatitude(),
                domain.getLongitude(),
                domain.getSrcLatitude(),
                domain.getSrcLongitude(),
                domain.getProvince(),
                domain.getCity(),
                domain.getAddress(),
                domain.getPosition(),
                domain.getAlarmCount()
        };

        return new BoundSql(sql, inValues);
    }
    
    public static BoundSql insertWeekSlice(LgDeviceStatics entity) {
        String sql = "INSERT INTO lg_deviceweekstatics " +
                "  SELECT " +
                "    replace(uuid(),'-','') as id, " +
                "    sysdate(), " +
                "    y.deviceno, " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    x.workcount, " +
                "    x.workbegin, " +
                "    x.workstop, " +
                "    x.slicerunduration, " +
                "    x.sliceworkduration, " +
                "    x.runoffcount, " +
                "    x.oilsum, " +
                "    x.oilavg, " +
                "    x.rotationlspeedmax, " +
                "    y.latitude, " +
                "    y.longitude, " +
                "    y.province, " +
                "    y.city, " +
                "    y.address, " +
                "    y.position, " +
                "    x.alarmcount " +
                "  FROM " +
                "  (SELECT " +
                "    count(1) AS workcount, " +
                "    min(a.workbegin) AS workbegin, " +
                "    max(a.workstop) AS workstop, " +
                "    sum(a.slicerunduration) AS slicerunduration, " +
                "    sum(a.sliceworkduration) AS sliceworkduration, " +
                "    sum(a.runoffcount) AS runoffcount, " +
                "    sum(a.oilsum) AS oilsum, " +
                "    avg(a.oilavg) AS oilavg, " +
                "    max(a.rotationlspeedmax) AS rotationlspeedmax, " +
                "    sum(a.alarmcount) AS alarmcount " +
                "    FROM lg_deviceDateStatics a " +
                "    where  a.sliceWorkDuration > 0 " +
                "      And a.deviceno = ? " +
                "      AND a.workdate between ? and ? " +
                "    GROUP BY a.deviceno " +
                "    LIMIT 1 " +
                "  ) x, " +
                "  (SELECT " +
                "    b.id, " +
                "    b.inserttime, " +
                "    b.deviceno, " +
                "    b.workdate, " +
                "    b.latitude, " +
                "    b.longitude, " +
                "    b.province, " +
                "    b.city, " +
                "    b.address, " +
                "    b.position, " +
                "    b.alarmcount " +
                "    FROM lg_deviceDateStatics b " +
                "    where b.workdate BETWEEN ? and ? " +
                "      And deviceno = ? " +
                "      and sliceWorkDuration > 0 " +
                "    order by workdate desc " +
                "    LIMIT 1 " +
                "  ) y";

        Object[] inValues = new Object[] {
                entity.getWorkDateId(),
                entity.getWorkDateName(),
                entity.getDateBegin(),
                entity.getDateEnd(),
                entity.getDeviceNo(),
                entity.getDateBegin(),
                entity.getDateEnd(),
                entity.getDateBegin(),
                entity.getDateEnd(),
                entity.getDeviceNo()
        };

        return new BoundSql(sql, inValues);
    }
    
    public static BoundSql insertMonthSlice(LgDeviceStatics entity) {
        String sql = "INSERT INTO lg_devicemonthstatics " +
                "  SELECT " +
                "    replace(uuid(),'-','') as id, " +
                "    sysdate(), " +
                "    y.deviceno, " +
                "    ?, " +
                "    ?, " +
                "    x.workcount, " +
                "    x.workbegin, " +
                "    x.workstop, " +
                "    x.slicerunduration, " +
                "    x.sliceworkduration, " +
                "    x.runoffcount, " +
                "    x.oilsum, " +
                "    x.oilavg, " +
                "    x.rotationlspeedmax, " +
                "    y.latitude, " +
                "    y.longitude, " +
                "    y.province, " +
                "    y.city, " +
                "    y.address, " +
                "    y.position, " +
                "    x.alarmcount " +
                "  FROM " +
                "  (SELECT " +
                "    count(1) AS workcount, " +
                "    min(a.workbegin) AS workbegin, " +
                "    max(a.workstop) AS workstop, " +
                "    sum(a.slicerunduration) AS slicerunduration, " +
                "    sum(a.sliceworkduration) AS sliceworkduration, " +
                "    sum(a.runoffcount) AS runoffcount, " +
                "    sum(a.oilsum) AS oilsum, " +
                "    avg(a.oilavg) AS oilavg, " +
                "    max(a.rotationlspeedmax) AS rotationlspeedmax, " +
                "    sum(a.alarmcount) AS alarmcount " +
                "    FROM lg_deviceDateStatics a " +
                "    where a.sliceWorkDuration > 0 " +
                "      And a.deviceno = ? " +
                "      AND a.workdate between #{dateBegin} and #{dateEnd} " +
                "    GROUP BY a.deviceno " +
                "     LIMIT 1 " +
                "  ) x, " +
                "  (SELECT " +
                "    b.id, " +
                "    b.inserttime, " +
                "    b.deviceno, " +
                "    b.workdate, " +
                "    b.latitude, " +
                "    b.longitude, " +
                "    b.province, " +
                "    b.city, " +
                "    b.address, " +
                "    b.position, " +
                "    b.alarmcount " +
                "    FROM lg_deviceDateStatics b " +
                "    where b.workdate BETWEEN #{dateBegin} and #{dateEnd} " +
                "      And deviceno = ? " +
                "      and sliceWorkDuration > 0 " +
                "    order by workdate desc " +
                "    LIMIT 1 " +
                "  ) y";

        Object[] inValues = new Object[] {
                entity.getWorkDateId(),
                entity.getWorkDateName(),
                entity.getDeviceNo(),
                entity.getDateBegin(),
                entity.getDateEnd(),
                entity.getDateBegin(),
                entity.getDateEnd(),
                entity.getDeviceNo()
        };

        return new BoundSql(sql, inValues);
    }
    
    public static BoundSql insertSeasonSlice(LgDeviceStatics entity) {
        String sql = "INSERT INTO lg_deviceseasonstatics " +
                "  SELECT " +
                "    replace(uuid(),'-','') as id, " +
                "    sysdate(), " +
                "    y.deviceno, " +
                "    ?, " +
                "    ?, " +
                "    x.workcount, " +
                "    x.workbegin, " +
                "    x.workstop, " +
                "    x.slicerunduration, " +
                "    x.sliceworkduration, " +
                "    x.runoffcount, " +
                "    x.oilsum, " +
                "    x.oilavg, " +
                "    x.rotationlspeedmax, " +
                "    y.latitude, " +
                "    y.longitude, " +
                "    y.province, " +
                "    y.city, " +
                "    y.address, " +
                "    y.position, " +
                "    x.alarmcount " +
                "  FROM " +
                "  (SELECT " +
                "    sum(a.workcount) as workcount, " +
                "    min(a.workbegin) AS workbegin, " +
                "    max(a.workstop) AS workstop, " +
                "    sum(a.slicerunduration) AS slicerunduration, " +
                "    sum(a.sliceworkduration) AS sliceworkduration, " +
                "    sum(a.runoffcount) AS runoffcount, " +
                "    sum(a.oilsum) AS oilsum, " +
                "    avg(a.oilavg) AS oilavg, " +
                "    max(a.rotationlspeedmax) AS rotationlspeedmax, " +
                "    sum(a.alarmcount) AS alarmcount " +
                "    FROM lg_devicemonthstatics a " +
                "      where a.sliceWorkDuration > 0 " +
                "      And a.deviceno = ? " +
                "      AND a.workMonthId  between ? and ? " +
                "    GROUP BY a.deviceno " +
                "    LIMIT 1 " +
                "  ) x, " +
                "  (SELECT " +
                "    b.id, " +
                "    b.inserttime, " +
                "    b.deviceno, " +
                "    b.latitude, " +
                "    b.longitude, " +
                "    b.province, " +
                "    b.city, " +
                "    b.address, " +
                "    b.position, " +
                "    b.alarmcount " +
                "    FROM lg_devicemonthstatics b " +
                "          where b.workMonthId between ? and ? " +
                "      And b.deviceno = ? " +
                "      and b.sliceWorkDuration > 0 " +
                "    order by b.workMonthId desc " +
                "    LIMIT 1 " +
                "  ) y";

        Object[] inValues = new Object[] {
                entity.getWorkDateId(),
                entity.getWorkDateName(),
                entity.getDeviceNo(),
                entity.getBeginWorkId(),
                entity.getEndWorkId(),
                entity.getBeginWorkId(),
                entity.getEndWorkId(),
                entity.getDeviceNo()
        };

        return new BoundSql(sql, inValues);
    }
    
    public static BoundSql insertYearSlice(LgDeviceStatics entity) {
        String sql = "INSERT INTO lg_deviceyearstatics " +
                "    SELECT " +
                "      replace(uuid(),'-','') as id, " +
                "      sysdate(), " +
                "      y.deviceno, " +
                "      ?, " +
                "      ?, " +
                "      x.workcount, " +
                "      x.workbegin, " +
                "      x.workstop, " +
                "      x.slicerunduration, " +
                "      x.sliceworkduration, " +
                "      x.runoffcount, " +
                "      x.oilsum, " +
                "      x.oilavg, " +
                "      x.rotationlspeedmax, " +
                "      y.latitude, " +
                "      y.longitude, " +
                "      y.province, " +
                "      y.city, " +
                "      y.address, " +
                "      y.position, " +
                "      x.alarmcount " +
                "    FROM " +
                "    (SELECT " +
                "     sum(a.workcount) as workcount, " +
                "      min(a.workbegin) AS workbegin, " +
                "      max(a.workstop) AS workstop, " +
                "      sum(a.slicerunduration) AS slicerunduration, " +
                "      sum(a.sliceworkduration) AS sliceworkduration, " +
                "      sum(a.runoffcount) AS runoffcount, " +
                "      sum(a.oilsum) AS oilsum, " +
                "      avg(a.oilavg) AS oilavg, " +
                "      max(a.rotationlspeedmax) AS rotationlspeedmax, " +
                "      sum(a.alarmcount) AS alarmcount " +
                "      FROM lg_deviceseasonstatics a " +
                "        where a.sliceWorkDuration > 0 " +
                "        And a.deviceno = ? " +
                "        AND a.workQuarterId  between ? and ? " +
                "      GROUP BY a.deviceno " +
                "      LIMIT 1 " +
                "    ) x, " +
                "    (SELECT " +
                "      b.id, " +
                "      b.inserttime, " +
                "      b.deviceno, " +
                "      b.latitude, " +
                "      b.longitude, " +
                "      b.province, " +
                "      b.city, " +
                "      b.address, " +
                "      b.position, " +
                "      b.alarmcount " +
                "      FROM lg_deviceseasonstatics b " +
                "        where b.workQuarterId between ? and ? " +
                "        And b.deviceno = ? " +
                "        and b.sliceWorkDuration > 0 " +
                "      order by b.workQuarterId desc " +
                "      LIMIT 1 " +
                "    ) y";

        Object[] inValues = new Object[] {
                entity.getWorkDateId(),
                entity.getWorkDateName(),
                entity.getDeviceNo(),
                entity.getBeginWorkId(),
                entity.getEndWorkId(),
                entity.getBeginWorkId(),
                entity.getEndWorkId(),
                entity.getDeviceNo()
        };

        return new BoundSql(sql, inValues);
    }
    
    public static BoundSql insertBaseSlice(List<LgDeviceSliceStatics> dataList) {
        String sql = "INSERT INTO lg_deviceslicestatics( " +
                "            id, " +
                "            inserttime, " +
                "            deviceno, " +
                "            slicecount, " +
                "            slicestart, " +
                "            slicestop, " +
                "            slicerunduration, " +
                "            sliceworkduration, " +
                "            latitude, " +
                "            longitude, " +
                "            srclatitude, " +
                "            srclongitude, " +
                "            province, " +
                "            city, " +
                "            address, " +
                "            position, " +
                "            alarmcount, " +
                "            startTimes " +
                "        ) VALUES ";

        List<Object> inValues = Lists.newArrayList();

        int i = 1;
        for (LgDeviceSliceStatics data : dataList) {
            if (i++ != dataList.size()) {
                sql += ",";
            }

            sql += "(?, sysdate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            inValues.add(data.getId());
            inValues.add(data.getDeviceNo());
            inValues.add(data.getSliceCount());
            inValues.add(data.getSliceStart());
            inValues.add(data.getSliceStop());
            inValues.add(data.getSliceRunDuration());
            inValues.add(data.getSliceWorkDuration());
            inValues.add(data.getLatitude());
            inValues.add(data.getLongitude());
            inValues.add(data.getSrcLatitude());
            inValues.add(data.getSrcLongitude());
            inValues.add(data.getProvince());
            inValues.add(data.getCity());
            inValues.add(data.getAddress());
            inValues.add(data.getPosition());
            inValues.add(data.getAlarmCount());
            inValues.add(data.getStartTimes());
        }

        return new BoundSql(sql, inValues.toArray());
    }

}
