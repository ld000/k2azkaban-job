package com.k2data.platform.utils;

/**
 * Array相关的工具类
 */
public final class ArrayUtils {
    
    /**
     * 把第二个数组从指定位置开始填充到另外第一个数组<br>
     * 
     * @param first 要被填充的数组
     * @param second 要填充的数组
     * @param pos 填充的起始位置（从0开始）
     */
    public static void fill(final int[] first, final int[] second, final int pos) {
        if (first == null || second == null || pos < 0 || first.length < second.length + pos) {
            return;
        }
        
        for (int i = 0; i < second.length; i++) {
            first[i + pos] = second[i];
        }
    }
    
}
