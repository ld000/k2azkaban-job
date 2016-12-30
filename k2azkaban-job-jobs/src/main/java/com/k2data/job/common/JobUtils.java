package com.k2data.job.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lidong 16-12-30.
 */
public class JobUtils {

    public static void setRootPath() {
        String propsFile = System.getenv("JOB_PROP_FILE");
//        Properties prop = new Properties();
//        prop.load(new BufferedReader(new FileReader(propsFile)));
//
//        String jobName = System.getenv("JOB_NAME");

        Pattern pattern = Pattern.compile("^(.*/[0-9]*?/)(.*)$");
        Matcher matcher = pattern.matcher(propsFile);

        String path;
        if (matcher.find()) {
            path = matcher.group(1);
            System.setProperty("path", path);
        } else {
            throw new RuntimeException("解析 project 路径出错...");
        }

        System.setProperty("rootPath", path);
    }

    public static String getRootPath() {
        return System.getProperty("rootPath");
    }

}
