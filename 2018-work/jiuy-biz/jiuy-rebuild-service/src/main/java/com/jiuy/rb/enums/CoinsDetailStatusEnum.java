package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 明细的状态
 *
 * 0 待入账,1 已入账 ,2 已经失效,3已到期
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 15:26
 * @Copyright 玖远网络
 */
public enum  CoinsDetailStatusEnum {

    /**
     * 待入账
     *
     */
    WAIT_IN(0,"待入账"),
    /**
     * 已入账
     */
    IN(1,"已入账"),
    /**
     * 已经失效
     */
    LOSE(2,"已失效"),
    /**
     * 已到期
     */
    EXPIRE(3,"已到期"),
    /**
     * 提现中
     */
    CASH_WAIT(4,"提现中"),
    /**
     * 已经提现
     */
    CASH_OUT(5,"已提现"),

    /**
     * 提现失败
     */
    CASH_FAILED(6,"提现失败");



    private String name;

    private Integer code;

    CoinsDetailStatusEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,CoinsDetailStatusEnum> enumMap = new HashMap<>(2);
    static {
        for (CoinsDetailStatusEnum enumItem : CoinsDetailStatusEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        CoinsDetailStatusEnum enumItem = enumMap.get(code);
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
