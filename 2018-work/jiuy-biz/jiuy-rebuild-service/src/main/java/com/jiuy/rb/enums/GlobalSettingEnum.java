package com.jiuy.rb.enums;


/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title 全局配置表
 * @package jiuy-biz
 * @description
 * @date 2018/6/19 18:08
 * @copyright 玖远网络
 */
public enum  GlobalSettingEnum{

    /** 优惠券模版发放金额,账户总额 */
    STORE_COUPON_PUBLISH_BALANCE("store_coupon")
    ;

    /**
     * 属性值
     */
    private String propertyValue;

    GlobalSettingEnum(String propValue) {
        this.propertyValue = propValue;
    }


    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
