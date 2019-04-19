package com.jiuy.rb.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class CouponSendList implements Serializable {

    private String couponId;

    private String templateId;

    private String couponName;

    private String userCouponTime;
    private String useBeginTime;
    private String useEndTime;
    private String couponUserType;
    private String storeName;
    private String storeId;
    private String storeMoney;
    private String storeOrderTime;
    private String wxaName;
    private String wxaId;
    private String wxaMoney;
    private String wxaTime;
    private String couponStatus;
    private String platformType;
    private String price;
    private String discount;
    private String sendObject;
    private String sendOrderMoney;
    private String sendOrderTime;
}
