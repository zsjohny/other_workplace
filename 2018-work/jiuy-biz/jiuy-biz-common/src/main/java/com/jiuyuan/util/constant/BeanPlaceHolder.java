package com.jiuyuan.util.constant;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.jiuyuan.util.placeholder.BeanPlaceholderResolver;


public class BeanPlaceHolder {

    private String value;

    public BeanPlaceHolder() {
        //
    }

    public BeanPlaceHolder(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String resolve(Object obj) {
        return BeanPlaceholderResolver.INSTANCE_SHARP.resolve(this.value, obj);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> sub = new HashMap<String, Object>();
        map.put("hi", sub);
        sub.put("god", new String[]{ "again", "2" });

    }
}
