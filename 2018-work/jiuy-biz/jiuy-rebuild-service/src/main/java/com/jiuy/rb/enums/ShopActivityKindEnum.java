package com.jiuy.rb.enums;

/**
 * 活动类型, 团购 or 秒杀
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/8/3 17:48
 * @Copyright 玖远网络
 */
public enum ShopActivityKindEnum{

    /**
     * 团购
     */
    TEAM (1, "团购活动"),
    /**
     * 秒杀
     */
    SECOND (2, "秒杀活动"),;

    private int code;
    private String name;

    ShopActivityKindEnum(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public static boolean isThis(ShopActivityKindEnum source) {
        for (ShopActivityKindEnum obj : ShopActivityKindEnum.values ()) {
            if (obj == source) {
                return true;
            }
        }
        return false;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
