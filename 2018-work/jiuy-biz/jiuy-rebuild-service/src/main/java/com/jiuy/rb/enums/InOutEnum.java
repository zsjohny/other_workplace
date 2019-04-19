package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 进出帐枚举
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 12:00
 * @Copyright 玖远网络
 */
public enum  InOutEnum {

    /**
     * 进账
     */
    IN(1, "进账"),
    /**
     * 出账
     */
    OUT(0, "出账");

    private String name;

    private Integer code;

    InOutEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    private static Map<Integer, InOutEnum> enumMap = new HashMap<>(2);

    static {
        for (InOutEnum enumItem : InOutEnum.values()) {
            enumMap.put(enumItem.getCode(), enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        InOutEnum enumItem = enumMap.get(code);
        return enumItem == null ? "" : enumItem.getName();
    }

    public boolean isThis(Integer code) {
        return this.getCode().equals(code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
