package com.k2data.platform.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 日志输出管理
 *
 * @author lidong 16-11-1.
 */
public class PeisistenceLogManager {

    private static final Logger logger = LogManager.getLogger(PeisistenceLogManager.class);

    public static void log(final BoundSql boundSql) {
        logger.debug(boundSql.getSql());
        logger.debug("params: {}", boundSql.toStringInValues());
    }

    public static void logBatch(final List<BoundSql> batchBoundSqls) {
        if (batchBoundSqls == null || batchBoundSqls.isEmpty()) {
            return;
        }

        int count = 0; // 要显示的数量

        StringBuilder sb = new StringBuilder("--------------------- BATCH SQL START ---------------------\n");

        String sql = "";

        for (int i = 0; i < batchBoundSqls.size(); i++) {
            BoundSql boundSql = batchBoundSqls.get(i);

            count++;

            if (i == 0) {
                sb.append("BATCH EXECUTE SQL :\n")
                        .append(sql).append("\n");
            }

            sb.append("EXECUTE No.").append(i + 1).append(" >\n")
                    .append(boundSql.toStringInValues()).append("\n");
        }

        sb.append("---------------------  BATCH SQL END  ---------------------");

        if (count > 0) {
            logger.info(sb.toString());
        }
    }

}
