package com.k2data.platform.persistence.support;

/**
 * 排序
 * 
 * @author lidong
 */
public class Order {
    private String order;
    
    private Order(String order) {
        this.order = order;
    }
    
    public final String getOrder() {
        return this.order;
    }
    
    /**
     * 正序
     * 
     * @param columnName 排序的字段
     * @return 排序Order
     */
    public static final Order asc(final String columnName) {
        return new Order(columnName);
    }
    
    /**
     * 倒序
     * 
     * @param columnName 排序的字段
     * @return 排序Order
     */
    public static final Order desc(final String columnName) {
        return new Order(columnName + " DESC");
    }
}
