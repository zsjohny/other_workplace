package com.jiuy.rb.enums;


/**
 * @author Charlie
 * @version V1.0  
 * @date 2018年06月15日 上午 10:55:45
 * @Copyright 玖远网络 
*/
public enum SalesVolumeProductTypeEnum{

    APP_RUSH_PURCHASE(1,"app限时抢购"),
    APP_PRODUCT(2,"app普通商品") ,
    SHOP_PRODUCT(50,"门店用户普通商品"),
    SHOP_TEAM(51,"门店用户团购活动"),
    SHOP_SECOND(52,"门店用户秒杀活动");

    private int code;
    private String description;

    SalesVolumeProductTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
