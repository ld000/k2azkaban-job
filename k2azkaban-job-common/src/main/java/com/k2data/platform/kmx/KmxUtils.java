package com.k2data.platform.kmx;

import com.k2data.platform.utils.StringUtils;

/**
 * @author lidong 17-1-9.
 */
public class KmxUtils {

    public static String safeString(String str) {
        return ("-".equals(str) || "null".equals(str) || StringUtils.isBlank(str)) ? "" : str;
    }

}
