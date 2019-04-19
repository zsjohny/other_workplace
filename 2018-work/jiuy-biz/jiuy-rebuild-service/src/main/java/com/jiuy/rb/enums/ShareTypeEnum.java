package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 分享类型枚举
 * 1:活动分享,2:商品分享,3:分享优惠劵
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 17:39
 * @Copyright 玖远网络
 */
public enum ShareTypeEnum {

    /**
     * 活动分享
     *activity
     */
    ACTIVITY_SHARE(1,"活动分享"),
    /**
     * 商品分享
     */
    PRODUCT_SHARE(2,"商品分享"),
    /**
     * 分享优惠劵
     */
    COUPON_SHARE(3,"分享优惠劵");

    private String name;

    private Integer code;

    ShareTypeEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,ShareTypeEnum> enumMap = new HashMap<>(2);
    static {
        for (ShareTypeEnum enumItem : ShareTypeEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        ShareTypeEnum enumItem = enumMap.get(code);
        return  enumItem == null ? "" : enumItem.getName();
    }

    public static ShareTypeEnum getIem(Integer code) {
        return enumMap.get(code);
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
