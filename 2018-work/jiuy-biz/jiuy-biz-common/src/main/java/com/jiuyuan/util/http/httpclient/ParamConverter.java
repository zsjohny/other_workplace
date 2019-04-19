package com.jiuyuan.util.http.httpclient;

public class ParamConverter {

    public static Boolean getBooleanValue(Object obj, Boolean defaultValue) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof String) {
            return Boolean.parseBoolean((String) obj);
        } else {
            return defaultValue;
        }
    }

    public static Integer getIntValue(Object obj, Integer defaultValue) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        } else {
            return defaultValue;
        }
    }

    public static Long getLongValue(Object obj, Long defaultValue) {
        if (obj instanceof Long) {
            return (Long) obj;
        } else {
            return defaultValue;
        }
    }
}
