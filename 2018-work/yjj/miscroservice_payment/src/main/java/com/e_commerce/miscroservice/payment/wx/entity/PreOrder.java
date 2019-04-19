package com.e_commerce.miscroservice.payment.wx.entity;

import lombok.Data;

/**
 * 描述
 *
 * @author hyq
 * @date 2018/9/21 13:56
 * @return
 */
@Data
public class PreOrder {
    /*
    公众账号ID
     */
    private String appid;
    /*
     商户号
      */
    private String mch_id;
    /*
     随机字符串
      */
    private String nonce_str;
    /*
     签名
      */
    private String sign;
    /*
     商品描述
      */
    private String body;
    /*
     商户订单号
      */
    private String out_trade_no;
    /*
     订单总金额，单位为分
      */
    private int total_fee;
    /*
    APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
     */
    private String spbill_create_ip;
    /*
     接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
      */
    private String notify_url;
    /*
     取值如下：JSAPI，NATIVE，APP
      */
    private String trade_type;

    private String openid;

    private String attach;


}
