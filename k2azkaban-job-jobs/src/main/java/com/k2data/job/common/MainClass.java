package com.k2data.job.common;

/**
 * 入口，第一个参数为 job 全类名
 *
 * @author lidong 17-1-9.
 */
public class MainClass {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        JobUtils.setRootPath(false);
        JobClassLoader.loadDirInnerJar(JobUtils.getRootPath() + "lib");

        Class<? extends BaseJob> clazz;
        try {
            clazz = (Class<? extends BaseJob>) Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        BaseJob job = JobProxyFactory.getJdkProxy(clazz);
        job.run();
    }

}
