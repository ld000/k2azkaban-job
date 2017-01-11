package com.k2data.platform.kmx.cond;

/**
 * @author lidong 17-1-11.
 */
public enum KmxCondType {

    dataPointsV3(KmxDataPointsV3Builder.class),
    dataRowsV3(KmxDataRowsV3Builder.class),
    devicesListV2(KmxDevicesListV2Builder.class);

    private Class<? extends KmxCondBuilder> builder;

    KmxCondType(Class<? extends KmxCondBuilder> builder) {
        this.builder = builder;
    }

    public Class<? extends KmxCondBuilder> getBuilder() {
        return builder;
    }
    public void setBuilder(Class<? extends KmxCondBuilder> builder) {
        this.builder = builder;
    }

}
