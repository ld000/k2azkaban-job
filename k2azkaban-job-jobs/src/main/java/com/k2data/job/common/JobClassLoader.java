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
    public static void loadDirInnerJar(String path) {
        List<File> files = new ArrayList<File>();
        File lib = new File(path);
        loopFiles(lib, files);
        for (File file : files) {
            loadJarFile(file);
        }
    }

}
