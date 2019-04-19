package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 玖币明细类型
 * 类型: 1 分享进账,2 邀请者购买进账, 3 被邀请者购买进账 , 50 兑换出账, 51 提现出账
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 14:05
 * @Copyright 玖远网络
 */
public enum  CoinsDetailTypeEnum {

    /**
     * 分享进账
     */
    SHARE_IN(1,"分享进账"),

    /**
     * 被邀请者购买后,邀请者的进账
     */
    INVITE_BUY_IN(2,"被邀请者购买后,邀请者的进账"),
    /**
     * 被邀请者购买之后 被邀请者的进账
     */
    INVITEE_BUY_IN(3," 被邀请者购买之后 被邀请者的进账"),
    /**
     * 兑换出账
     */
    EXCHANGE_OUT(50,"兑换出账"),
    /**
     * 提现出账
     */
    CASH_OUT(51,"提现出账");


    private String name;

    private Integer code;

    CoinsDetailTypeEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    private static Map<Integer, CoinsDetailTypeEnum> enumMap = new HashMap<>(2);

    static {
        for (CoinsDetailTypeEnum enumItem : CoinsDetailTypeEnum.values()) {
            enumMap.put(enumItem.getCode(), enumItem);
        }
    }


    public static CoinsDetailTypeEnum getEnum(Integer code) {
        return enumMap.get(code);
    }

    public static String getStatusName(Integer code) {
        CoinsDetailTypeEnum enumItem = enumMap.get(code);
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
