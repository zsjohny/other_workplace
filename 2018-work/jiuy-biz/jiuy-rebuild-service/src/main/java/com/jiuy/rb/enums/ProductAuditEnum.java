package com.jiuy.rb.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 商品审核状态的枚举
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/13 9:30
 * @Copyright 玖远网络
 */
public enum ProductAuditEnum {

    /**
     * 待审核
     */
    SERVER_WAIT(0, "待审核"),
    /**
     * 客服审核通过
     */
    SERVER_PASS(1, "客服审核通过"),
    /**
     * 客服审核不通过
     */
    SERVER_NOT_PASS(2, "客服审核不通过"),
    /**
     * 待买手审核
     */
    BUYER_WAIT(3, "待买手审核"),
    /**
     * 买手审核通过
     */
    BUYER_PASS(4, "买手审核通过"),
    /**
     * 买手审核不通过
     */
    BUYER_NOT_PASS(5, "买手审核不通过");


    private Integer code;

    private String name;

    ProductAuditEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 判断是否是当前的枚举
     *
     * @param code code
     * @author Aison
     * @date 2018/6/13 9:59
     */
    public boolean isThis(Integer code) {
        return this.getCode().equals(code);
    }

    private static Map<String,ProductAuditEnum> auditStateMap = new HashMap<>();

    static {
        for (ProductAuditEnum productAuditState : ProductAuditEnum.values()) {
            auditStateMap.put(productAuditState.getCode().toString(),productAuditState);
        }
    }


    public static ProductAuditEnum getByCode(Integer code) {

        return auditStateMap.get(code == null ? "" : code.toString());
    }

    public static String getByCode(String code) {

        ProductAuditEnum productAuditState =  auditStateMap.get(code);
        return productAuditState == null ? "" : productAuditState.getName();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
