package com.k2data.platform.etl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.k2data.platform.persistence.BoundSql;
import com.k2data.platform.persistence.LDPSqlRunner;
import com.k2data.platform.persistence.SqlRunner;
import com.k2data.platform.persistence.transaction.TransactionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 临工数据库接口管理类
 * 
 * @author lidong
 */
public class ETLTool {

    private static final Integer PAGE_NUM = 500;

    /**
     * 同步 LDP 数据
     */
    public static void transportLDPData(String mappingFileName) {
        transportLDPData(mappingFileName, null);
    }

    /**
     * 同步 LDP 数据
     */
    public static void transportLDPData(String mappingFileName, final ETLDataPreHandler handler) {
        final ETLMappingDomain domain = resolveMappingFile(mappingFileName);

        List<Map<String, Object>> source = extraction(domain);

        if (source.size() == 0)
            return;

        cache(domain, source);

        try {
            TransactionUtils.beginTransaction();

            LDPSqlRunner.update(ETLSqlProvider.update1N5(domain, 5));

            // 拉链
            if ("zipper".equals(domain.getHistory().getType())) {
                if ("full".equals(domain.getType())) {
                    fullZip(domain);
                } else if ("add".equals(domain.getType())) {
                    SqlRunner.update(new BoundSql(ETLSqlProvider.zipAddTypeData(domain)));
                } else {
                    throw new ETLException("Illegal type!");
                }
            }

            source = transformation(source, handler);

            loadding(domain, source);

            LDPSqlRunner.update(ETLSqlProvider.update1N5(domain, 9));

            TransactionUtils.commitTransaction();
        } catch (Exception e) {
            TransactionUtils.rollbackTransaction();
            throw new ETLException(e);
        } finally {
            TransactionUtils.closeConnection();
        }
    }

    /**
     * 全量数据拉链
     */
    private static void fullZip(final ETLMappingDomain domain) {
        String columns = ETLSqlProvider.getSourceColumns(domain);
        String where = " endDate = \"9999-01-01\"";

        List<Map<String, Object>> bdmList =
                SqlRunner.selectListMap(new BoundSql(ETLSqlProvider.select(columns, domain.getHistory().getBDMTable())));

        List<Map<String, Object>> fdmList =
                SqlRunner.selectListMap(new BoundSql(ETLSqlProvider.select(columns, domain.getHistory().getHisTable(), where)));

        Set<Map<String, Object>> bdmSet = new HashSet<>(bdmList);
        Set<Map<String, Object>> fdmSet = new HashSet<>(fdmList);

        Set<Map<String, Object>> newData = Sets.difference(bdmSet, fdmSet);
        Set<Map<String, Object>> closeData = Sets.difference(fdmSet, bdmSet);

        if (closeData.size() != 0) {
            List<Map<String, Object>> closeList = new ArrayList<>(closeData);
            subExecute(domain, closeList, (domain1, subSource) -> SqlRunner.update(ETLSqlProvider.closeZip(domain1, subSource)));
        }

        if (newData.size() != 0) {
            List<Map<String, Object>> newList = new ArrayList<>(newData);
            subExecute(domain, newList, (domain1, subSource) -> SqlRunner.update(ETLSqlProvider.insertZipHis(domain1, subSource)));
        }
    }

    /**
     * 从源数据源拉取数据
     */
    private static List<Map<String, Object>> extraction(final ETLMappingDomain domain) {
        return LDPSqlRunner.selectListMap(new BoundSql(ETLSqlProvider.selectSourceData(domain)));
    }

    /**
     * 插入 BDM 缓冲表
     */
    private static void cache(final ETLMappingDomain domain, final List<Map<String, Object>> source) {
        try {
            TransactionUtils.beginTransaction();

            SqlRunner.update(new BoundSql(ETLSqlProvider.deleteAll(domain.getHistory().getBDMTable().toUpperCase(), null)));

            subExecute(domain, source, (domain1, subSource) -> SqlRunner.update(ETLSqlProvider.insertBDM(domain1, subSource)));

            TransactionUtils.commitTransaction();
        } catch (Exception e) {
            TransactionUtils.rollbackTransaction();
            throw new ETLException(e);
        } finally {
            TransactionUtils.closeConnection();
        }
    }

    /**
     * 转换数据
     */
    private static List<Map<String, Object>> transformation(List<Map<String, Object>> source, final ETLDataPreHandler handler) {
        if (handler != null)
            source = handler.handleData(source);

        return source;
    }

    /**
     * 写入目标表
     */
    private static void loadding(final ETLMappingDomain domain, List<Map<String, Object>> source) {
        if ("full".equals(domain.getType())) {
            SqlRunner.update(new BoundSql(ETLSqlProvider.deleteAll(domain.getTargetTable().toUpperCase(), domain.getDeleteWhere())));
        }

        subExecute(domain, source, (domain1, subSource) -> SqlRunner.update(ETLSqlProvider.insert2Target(domain1, subSource)));
    }

    /**
     * 解析映射文件
     */
    private static ETLMappingDomain resolveMappingFile(final String fileName) {
        File file = new File(fileName);

        String fileStr;
        try {
            fileStr = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new ETLException(e);
        }

        return JSON.parseObject(fileStr, ETLMappingDomain.class);
    }

    /**
     * 按数量批量执行
     */
    private static void subExecute(ETLMappingDomain domain, List<Map<String, Object>> source, ETLSubExeFunction function) {
        int size = source.size();
        int begin = 0;
        int end = PAGE_NUM > size ? size : PAGE_NUM;
        while (begin < size) {
            List<Map<String, Object>> subSource = source.subList(begin, end);
            begin = end;
            end = (end + PAGE_NUM) > size ? size : (end + PAGE_NUM);

            function.update(domain, subSource);
        }
    }

}
