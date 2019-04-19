package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 个推类型枚举
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/29 12:00
 * @Copyright 玖远网络
 */
public enum GeTuiEnum {


    LOG_OUT(101,"登陆退出"),
    PACKAGE_SEND(9,"物流发货");

    private String name;

    private Integer code;

    GeTuiEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }


    private static Map<Integer,GeTuiEnum> enumMap = new HashMap<>(2);

    static {
        for (GeTuiEnum enumItem : GeTuiEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        GeTuiEnum enumItem = enumMap.get(code);
        return  enumItem == null ? "" : enumItem.getName();
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
