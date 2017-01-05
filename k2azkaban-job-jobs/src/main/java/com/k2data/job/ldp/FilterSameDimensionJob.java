package com.k2data.job.ldp;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.platform.domain.MachineDimension;
import com.k2data.platform.persistence.SqlRunner;
import com.k2data.platform.persistence.support.BoundSql;
import com.k2data.platform.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lidong 16-12-8.
 */
public class FilterSameDimensionJob implements BaseJob {

    public static void main(String[] args) throws Exception {
        BaseJob job = JobProxyFactory.getJdkProxy(FilterSameDimensionJob.class);
        job.run();
    }

    @Override
    public void run() {
        Map<String, List<Map<String, Object>>> map = Maps.newHashMap();
        String sameDataSql = "SELECT a.dimensionName, a.dimensionType" +
                " FROM lg_machineDimension a" +
                " GROUP BY a.dimensionName, a.dimensionType" +
                " HAVING COUNT(1) > 1" +
                " ORDER BY a.dimensionType";
        List<MachineDimension> sameData = SqlRunner.selectList(MachineDimension.class, sameDataSql);

        String listSql = "SELECT a,id, a.dimensionName, a.dimensionType, a.ldpId, a.dimensionCode " +
                " FROM lg_machineDimension a" +
                " WHERE a.dimensionName = ?" +
                " AND a.dimensionType = ?" +
                " ORDER BY a.ldpId + 0";    // 保留最小的ldpid

        String deleteByIdSql = "DELETE FROM lg_machineDimension  WHERE id = ?";

        for (MachineDimension machineDimension : sameData) {
            List<MachineDimension> list = SqlRunner.selectList(MachineDimension.class,
                    new BoundSql(listSql,  machineDimension.getDimensionName(), machineDimension.getDimensionType()));   // 按类型和名字查出所有数据

            List<Map<String, Object>> innerList = map.computeIfAbsent(machineDimension.getDimensionType(), k -> Lists.newArrayList());

            Map<String, Object> ldpMap = Maps.newHashMap();
            List<String> targetList = Lists.newArrayList();
            for (int i = 0; i < list.size(); i++) {
                MachineDimension obj = list.get(i);

                if (i == 0) {
                    ldpMap.put("source", String.valueOf(obj.getLdpid()));
                } else {
                    targetList.add(String.valueOf(obj.getLdpid()));
                    SqlRunner.delete(deleteByIdSql, obj.getId());
                }
            }
            ldpMap.put("target", targetList);

            innerList.add(ldpMap);
        }

        // 1 machineprofile machineprofilenogps.bookBuildingId, repairhistory.serviceno, servicehistory.serviceno, upkeephistory.supplyid
        // 3 dealerinchannet.supplyid, machineprofile machineprofilenogps.saleunitid, machinetranshis.supplyid
        // 10 replacehistory.supplier oldsupplier
        for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
            // 只有1, 3, 10重复
            switch (entry.getKey()) {
                case "1":
                    for (Map<String, Object> uMap : entry.getValue()) {
                        SqlRunner.update(getFilterType1Sql((String)uMap.get("source"), (List<String>)uMap.get("target")));
                    }

                    break;
                case "3":
                    for (Map<String, Object> uMap : entry.getValue()) {
                        SqlRunner.update(getFilterType3Sql((String)uMap.get("source"), (List<String>)uMap.get("target")));
                    }
                    break;
                case "10":
                    for (Map<String, Object> uMap : entry.getValue()) {
                        SqlRunner.update(getFilterType10Sql((String)uMap.get("source"), (List<String>)uMap.get("target")));
                    }
                    break;
                default:
                    throw new RuntimeException("type没配置!!! type=" + entry.getKey());
            }
        }
    }

    private BoundSql getFilterType1Sql(String source, List<String> list) {
        ArrayList<Object> inValues = Lists.newArrayList();

        String sql = "UPDATE lg_machineProfile SET bookBuildingId = ? WHERE bookBuildingId IN ($);" +
                "UPDATE lg_machineProfileNoGps SET bookBuildingId = ? WHERE bookBuildingId IN ($);" +
                "UPDATE lg_repairHistory SET serviceNo = ? WHERE serviceNo IN ($);" +
                "UPDATE lg_serviceHistory SET serviceNo = ? WHERE serviceNo IN ($);" +
                "UPDATE lg_upkeepHistory SET supplyId = ? WHERE supplyId IN ($);";
        sql = StringUtils.replace(sql, "$", Strings.repeat("?,", list.size()).substring(0, list.size() - 1));

        ArrayList<Object> temp = Lists.newArrayList();
        temp.add(source);
        for (String target : list) {
            temp.add(target);
        }

        for (int i = 0; i < 5; i++) {
            inValues.addAll(temp);
        }

        return new BoundSql(sql, inValues.toArray());
    }

    private BoundSql getFilterType3Sql(String source, List<String> list) {
        ArrayList<Object> inValues = Lists.newArrayList();

        String sql = "UPDATE lg_dealerInChanNet SET supplyId = ? WHERE supplyId IN ($);" +
                "UPDATE lg_machineProfile SET saleUnitId = ? WHERE saleUnitId IN ($);" +
                "UPDATE lg_machineProfileNoGps SET saleUnitId = ? WHERE saleUnitId IN ($);" +
                "UPDATE lg_machineTransportHistory SET supplyId = ? WHERE supplyId IN ($);";
        sql = StringUtils.replace(sql, "$", Strings.repeat("?,", list.size()).substring(0, list.size() - 1));

        ArrayList<Object> temp = Lists.newArrayList();
        temp.add(source);
        for (String target : list) {
            temp.add(target);
        }

        for (int i = 0; i < 4; i++) {
            inValues.addAll(temp);
        }

        return new BoundSql(sql, inValues.toArray());
    }

    private BoundSql getFilterType10Sql(String source, List<String> list) {
        ArrayList<Object> inValues = Lists.newArrayList();

        String sql = "UPDATE lg_replaceHistory SET supplier = ? WHERE supplier IN ($);" +
                "UPDATE lg_replaceHistory SET oldSupplier = ? WHERE oldSupplier IN ($);";
        sql = StringUtils.replace(sql, "$", Strings.repeat("?,", list.size()).substring(0, list.size() - 1));

        ArrayList<Object> temp = Lists.newArrayList();
        temp.add(source);
        for (String target : list) {
            temp.add(target);
        }

        for (int i = 0; i < 2; i++) {
            inValues.addAll(temp);
        }

        return new BoundSql(sql, inValues.toArray());
    }

}
