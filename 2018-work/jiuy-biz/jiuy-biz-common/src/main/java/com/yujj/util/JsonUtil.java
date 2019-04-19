package com.yujj.util;

import org.apache.commons.lang3.StringUtils;

public class JsonUtil {

    public static boolean isJsonObjectLike(String text) {
        return StringUtils.isNotBlank(text) && text.trim().startsWith("{");
    }

    public static boolean isJsonArrayLike(String text) {
        return StringUtils.isNotBlank(text) && text.trim().startsWith("[");
    }
}
