package com.jiuyuan.util.constant;

import java.util.HashMap;
import java.util.Map;

import com.jiuyuan.util.placeholder.StringPlaceholderResolver;


public class PlaceHolder {

    private String value;

    public PlaceHolder() {
        //
    }

    public PlaceHolder(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String resolve(Object obj) {
        return StringPlaceholderResolver.INSTANCE_SHARP.resolve(this.value, obj);
    }

    public String resolve(Map<String, ?> map) {
        return StringPlaceholderResolver.INSTANCE_SHARP.resolve(this.value, map);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("hi_god", "again");

    }
}
