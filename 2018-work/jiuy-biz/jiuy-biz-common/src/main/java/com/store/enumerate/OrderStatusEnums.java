package com.store.enumerate;

/**
 * 订单状态-普通订单-售后订单
 *
 * @author hyf
 * @version V1.0
 * @date 2018/12/4 13:59
 * @Copyright 玖远网络
 */
public enum OrderStatusEnums {

    /******************************************************************************************/
    /*****************************************正常订单*****************************************/
    /******************************************************************************************/
    /*------------------------------用户提交订单到付款前的阶段--------------------------------*/
    //    买家待付款
    COMMON_WAY_PAY(1001, "未付款"),
    COMMON_PAY_CLOSE(1011, "交易关闭"),
    /*------------------------------用户提交订单到付款前的阶段--------------------------------*/
/////////////////////////////////////////////////////////////////////////////////////////////////////
    /*-------------------------------用户付款到发货前的阶段-----------------------------------*/
    COMMON_WAY_SEND_GOODS(1021, "待发货"),
    COMMON_BUSINESS_CLOSE(1031, "交易关闭"),
    /*-------------------------------用户付款到发货前的阶段-----------------------------------*/
/////////////////////////////////////////////////////////////////////////////////////////////////////
    /*-------------------------平台（卖家）发货到用户确认售后前的阶段-------------------------*/
    COMMON_SEND_GOODS(1041, "已发货"),
    COMMON_SEND_GOODS_MORE(1051, "部分发货"),
    /*-------------------------平台（卖家）发货到用户确认售后前的阶段-------------------------*/
/////////////////////////////////////////////////////////////////////////////////////////////////////
    /*------------------------用户确认售后以后申请售后结束的阶段------------------------------*/
    COMMON_BUSINESS_SUCCESS(1061, "交易成功"),
    /*------------------------用户确认售后以后申请售后结束的阶段------------------------------*/
    /******************************************************************************************/
    /*****************************************售后订单*****************************************/
    /******************************************************************************************/

    /*-------------------------------用户付款到售后期结束阶段----------------------------------*/
    BACK_MONEY_WAY_ACCEPT(2001,"待受理"),
    BACK_MONEY_REFUSE_ACCEPT(2011,"已拒绝"),
    BACK_MONEY_WAY_BACK_MONEY(2021,"待退款"),
    BACK_MONEY_BACK_MONEY_ING(2022,"退款中"),
    BACK_MONEY_BACK_MONEY_FAIL(2023,"退款失败"),
    BACK_MONEY_BACK_MONEY_SUSSESS(2031,"退款成功"),
    /*-------------------------------用户付款到售后期结束阶段----------------------------------*/
/////////////////////////////////////////////////////////////////////////////////////////////////////
    /*------------------------------平台发货到售后期结束阶段-----------------------------------*/
    BACK_MONEY_GOODS_WAY_ACCEPT(3001,"待受理"),
    BACK_MONEY_GOODS_REFUSE_ACCEPT(3011,"已拒绝"),
    BACK_MONEY_GOODS_WAY_BACK_GOODS(3021,"待买家发货"),
    BACK_MONEY_GOODS_REFUSE_GOODS(3031,"拒绝收货"),
    BACK_MONEY_GOODS_WAY_BACK_MONEY(3041,"待退款"),
    BACK_MONEY_GOODS_BACK_MONEY_ING(3042,"退款中"),
    BACK_MONEY_GOODS_BACK_MONEY_FAIL(3043,"退款失败"),
    BACK_MONEY_GOODS_BACK_MONEY_SUSSESS(3051,"退款成功"),
    /*------------------------------平台发货到售后期结束阶段-----------------------------------*/

    ;
    private Integer code;
    private String value;

    OrderStatusEnums(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
