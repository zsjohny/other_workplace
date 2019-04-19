package com.jiuyuan.web.help;

import java.util.HashMap;
import java.util.Map;

public class JsonMap {

    private Map<String, Object> map = new HashMap<String, Object>();

    public void put(String key, Object value) {
        this.map.put(key, value);
    }

    public void putInt(String key, int value) {
        this.map.put(key, value);
    }

    public void putLong(String key, long value) {
        this.map.put(key, value);
    }

    public void putString(String key, long value) {
        this.map.put(key, value);
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
