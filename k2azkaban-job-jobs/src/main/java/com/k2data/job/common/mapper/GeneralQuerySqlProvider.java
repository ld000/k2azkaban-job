package com.k2data.job.common.mapper;

import com.k2data.platform.persistence.support.BoundSql;

/**
 * @author lidong 12/1/16.
 */
public class GeneralQuerySqlProvider {

    public BoundSql queryDictValue(String label, String type) {
        return new BoundSql("SELECT value FROM sys_dict WHERE type = ? AND label = ?", label, type);
    }

    public BoundSql queryMachineDimensionList(Integer type) {
        String sql = "SELECT id, dimensionType, dimensionName, ldpid, dateLimit" +
                " FROM lg_machineDimension" +
                " WHERE dimensionType = ?";

        return new BoundSql(sql, type);
    }

    public String queryGpsNoList() {
        return "SELECT gpsNo FROM lg_machineGpsContrast a WHERE a.isValid = 1";
    }

}
