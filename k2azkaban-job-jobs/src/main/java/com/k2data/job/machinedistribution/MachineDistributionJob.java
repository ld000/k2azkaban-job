package com.k2data.job.machinedistribution;

import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.platform.persistence.SqlRunner;
import com.k2data.platform.utils.CalendarUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author lidong 17-1-4.
 */
public class MachineDistributionJob implements BaseJob {

    @Override
    public long run() {
        Calendar calendar = CalendarUtils.getCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        int year = calendar.get(Calendar.YEAR);
        Date lastWeekMonday = calendar.getTime();
        calendar.clear();
        calendar.setTime(lastWeekMonday);
        int month = calendar.get(Calendar.MONTH) + 1;

        Integer workMonthId = CalendarUtils.genMonthNumber(year, month);

        String sql = "INSERT INTO lg_workhotdistribution " +
                "        SELECT " +
                "            replace(uuid(),'-','') as id, " +
                "            a.workMonthId, " +
                "            a.workMonthName, " +
                "            b.machineType, " +
                "            c.label AS machineTypeName, " +
                "            b.modelnumber, " +
                "            b.ordernumber, " +
                "            a.province, " +
                "            a.city, " +
                "            sum(a.sliceRunDuration) AS runDuration, " +
                "            sum(a.sliceWorkDuration) AS workDuration, " +
                "            sum(a.runOffCount) AS runOffCount, " +
                "            sum(1) AS machineCount, " +
                "            sysdate() " +
                "        FROM lg_devicemonthstatics a " +
                "        INNER JOIN lg_machineprofile b ON (a.deviceNo = b.gpsNo and b.machineStatus = 0) " +
                "        LEFT JOIN sys_dict c ON ( " +
                "            c.value = b.machineType AND c.type = 'machineType' " +
                "        ) " +
                "        where a.workMonthId = ? " +
                "        GROUP BY a.workMonthId, b.machineType, b.modelnumber, b.orderNumber, a.province, a.city";

        return SqlRunner.insert(sql, workMonthId);
    }
    
}
