package com.jiuy.rb.enums;

/**
 * 小程序订单取消原因
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/8/3 17:25
 * @Copyright 玖远网络
 */
public enum MemberOrderCancelEnum{


    /**
     * 无
     */
    NULL(0,"无"),
    /**
     * 会员取消
     */
    MEMBER_DO(1,"会员取消"),
    /**
     * 商家取消
     */
    STORE_DO(2,"商家取消"),
    /**
     * 系统自动取消
     */
    SYSTEM_DO(3,"系统自动取消"),
    /**
     * 商家手动结束活动
     */
    STORE_CLOSE_ACTIVITY(4,"商家手动结束活动,关闭订单"),
    /**
     * 平台客服关闭订单
     */
    CUSTOMER_SERVICE_CLOSE(5,"平台客服关闭订单")
    ;

    private int code;
    private String description;

    MemberOrderCancelEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static boolean isThis(MemberOrderCancelEnum source) {
        for (MemberOrderCancelEnum obj : MemberOrderCancelEnum.values ()) {
            if (source == obj) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
