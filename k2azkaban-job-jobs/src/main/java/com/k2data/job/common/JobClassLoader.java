package com.k2data.job.common;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong 16-12-7.
 */
public class JobClassLoader {

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("必须指定映射文件位置。");
        }

        loadJarPath(System.getenv("AIRFLOW_HOME") + "/dags/lib");

//        if ("ldp".equals(args[0])) {
//            ETLTool.transportLDPData(args[1]);
//        }


        URLStreamHandler streamHandler = null;
        try {
            URLClassLoader loader = new URLClassLoader(new URL[]{new URL(null,
                    "file:" + args[0],
                    streamHandler)});

            Class clazz = loader.loadClass(args[1]);
            BaseJob job = (BaseJob) clazz.newInstance();
            job.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 循环遍历目录，找出所有的JAR包
     */
    private static void loopFiles(File file, List<File> files) {
        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                loopFiles(tmp, files);
            }
        } else {
            if (file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip")) {
                files.add(file);
            }
        }
    }

    /**
     * 加载JAR文件
     */
    public static void loadJarFile(File file) {
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true);

            URLClassLoader system = (URLClassLoader) ClassLoader.getSystemClassLoader();


            add.invoke(system, file.toURI().toURL());
            System.out.println("load jar: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从一个目录加载所有JAR文件
     */
    public static void loadJarPath(String path) {
        List<File> files = new ArrayList<File>();
        File lib = new File(path);
        loopFiles(lib, files);
        for (File file : files) {
            loadJarFile(file);
        }
    }

}
