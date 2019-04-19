package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 退款类型
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/12 16:08
 * @Copyright 玖远网络
 */
public enum OrderRefundType {

    /**
     * 售后中
     */
    IN_SERVER(1,"售后中"),
    /**
     * 非售后中
     */
    NOT_IN_SERVER(0,"非售后中");

    private String name;

    private Integer code;

    OrderRefundType(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<String,OrderRefundType> refundTypeMap = new HashMap<>(2);

    static {
        for (OrderRefundType refundType : OrderRefundType.values()) {
            refundTypeMap.put(refundType.getCode().toString(),refundType);
        }
    }


    public static String getTypeName(String code) {
        OrderRefundType refundType = refundTypeMap.get(code);
        return  refundType == null ? "" : refundType.getName();
    }

    /**
     * 判断是否是此枚举
     *
     * @param code code
     * @author Aison
     * @date 2018/6/29 10:54
     * @return boolean
     */
    public boolean isThis(Integer code){

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
