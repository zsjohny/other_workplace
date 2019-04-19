package com.jiuy.rb.enums;

/**
 * 小程序活动的状态
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/7/30 13:48
 * @Copyright 玖远网络
 */
public enum ShopActivityStatusEnum{

    /**
     * 待开始
     */
    WAITING_2_START (1, "待开始"),
    /**
     * 进行中
     */
    UNDERWAY (2, "进行中"),
    /**
     * 已结束
     */
    TERMINATE (3, "已结束");

    private Integer code;
    private String description;

    ShopActivityStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }


    /**
     * 通过code获得状态描述
     *
     * @param code code
     * @return java.lang.String
     * @author Charlie
     * @date 2018/7/30 13:57
     */
    public static String findDescriptionByCode(int code) {
        for (ShopActivityStatusEnum status : ShopActivityStatusEnum.values ()) {
            if (status.getCode ().equals (code)) {
                return status.getDescription ();
            }
        }
        return "";
    }


    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
