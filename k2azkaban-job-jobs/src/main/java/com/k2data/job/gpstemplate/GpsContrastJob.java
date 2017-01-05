package com.k2data.job.gpstemplate;

import com.alibaba.fastjson.JSON;
import com.k2data.job.common.BaseJob;
import com.k2data.job.common.JobProxyFactory;
import com.k2data.platform.domain.LgMachineGpsContrast;
import com.k2data.platform.nvr.NvrClient;
import com.k2data.platform.nvr.domain.NvrMachineContrast;
import com.k2data.platform.nvr.domain.NvrMachineContrastContent;
import com.k2data.platform.persistence.SqlRunner;
import com.k2data.platform.persistence.support.BoundSqlBuilder;
import com.k2data.platform.persistence.transaction.TransactionUtils;
import com.k2data.platform.utils.Global;
import com.k2data.platform.utils.IdGen;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong 17-1-4.
 */
public class GpsContrastJob implements BaseJob {

    public static void main(String[] args) {
        GpsContrastJob job = JobProxyFactory.getJdkProxy(GpsContrastJob.class);
        job.run();
    }

    @Override
    public void run() {
        String token = NvrClient.getToken();
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("token", token);
        String json = NvrClient.getJson(Global.getConfig("nvr.synchroData"), null, headers);
        NvrMachineContrast nvr = JSON.parseObject(json, NvrMachineContrast.class);
        List<NvrMachineContrastContent> content = nvr.getContent();

        if (content == null || content.size() == 0) {
            throw new RuntimeException("从北谷同步gpsNo对照, 返回数据为空, content == null || content.size() == 0");
        }

        TransactionUtils.beginTransaction();
        try {
            SqlRunner.delete("delete from lg_machinegpscontrast");
            for (NvrMachineContrastContent machineContrast : content) {
                LgMachineGpsContrast mgc = new LgMachineGpsContrast();
                mgc.setDeviceNo(machineContrast.getLicenseid());
                mgc.setGpsNo(machineContrast.getDevicenum());
                if (machineContrast.getBuytime()!=null) {
                    mgc.setBuyDate(new Date(Long.valueOf(machineContrast.getBuytime())));
                }
                mgc.setId(IdGen.getUUID());
                mgc.setOrg(machineContrast.getOrgid());
                mgc.setEngineType(machineContrast.getEnginetype());
                mgc.setIsValid(1);
                mgc.setRunTime(new Date());

                SqlRunner.insert(BoundSqlBuilder.buildInsertBoundSql(mgc));
            }
            TransactionUtils.commitTransaction();
        } catch (Exception e) {
            TransactionUtils.rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            TransactionUtils.closeConnection();
        }
    }

}
