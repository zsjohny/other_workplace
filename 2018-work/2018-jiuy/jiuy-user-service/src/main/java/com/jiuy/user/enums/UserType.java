package com.jiuy.user.enums;

/**
 *
 * 用户类型的枚举
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/11 17:10
 * @Copyright 玖远网络
 */
public enum UserType {

    /**
     * 后台用户
     */
    OPERATOR_USER(1,"后台用户"),
    /**
     * 门店用户
     */
    STORE_USER(2,"门店用户"),
    /**
     * 供应商用户
     */
    SUPPLIER_USER(3,"供应商用户");

    private String name;

    private Integer code;

    UserType(Integer code,String name) {
        this.name = name;
        this.code = code;
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
