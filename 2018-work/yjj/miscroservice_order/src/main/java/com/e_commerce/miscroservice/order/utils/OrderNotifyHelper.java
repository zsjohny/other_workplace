package com.e_commerce.miscroservice.order.utils;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/14 9:49
 * @Copyright 玖远网络
 */
public class OrderNotifyHelper{

    /**
     * 传给微信,在支付回调后,微信会返回过来
     */
    public static final String WX_PAY_ATTACH = "notifyType";



    /*
     * 支付回调的工具类
     */

    private OrderNotifyHelper(){

    }

    public static OrderNotifyHelper me(String json) {

        return null;
    }


    /**
     * 支付回调类型
     */
    public enum NotifyType{
        /**
         * 从店中店购买
         */
        BUY_FROM_IN_SHOP(1,"")
        ;

        private int code;
        private String note;

        NotifyType(int code, String note) {
            this.code = code;
            this.note = note;
        }

        public int getCode() {
            return code;
        }

        public String getNote() {
            return note;
        }

        public static NotifyType create(Integer code) {
            if (code == null) {
                return null;
            }
            for (NotifyType type : values ()) {
                if (code.equals (code)) {
                    return type;
                }
            }
            return null;
        }
    }

}
