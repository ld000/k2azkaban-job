package com.k2data.job.common;

/**
 * @author lidong 16-12-7.
 */
public abstract class BaseAbstractJob {

    private String path;

    public abstract void run();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static void runJob(BaseAbstractJob baseJob) {
        JobClassLoader.loadJarPath(JobUtils.getRootPath() + "lib");

        BaseAbstractJob job = JobProxyFactory.getCglibProxy(baseJob);
        job.run();
    }

}
