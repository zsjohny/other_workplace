package com.e_commerce.miscroservice.order.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.enums.order.OrderStatusEnums;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/10 21:09
 * @Copyright 玖远网络
 */
@Data
@Table("store_biz_order")
public class StoreBizOrder{

    @Id
    private Long id;

    @Column(value = "order_no", commit = "订单号", length = 40)
    private String orderNo;


    /**
     * 订单类型,1平台商品,2购买会员商品(字典表)
     * <p>根据订单类型查询不同的订单详情表</p>
     */
    @Column(value = "type", commit = "订单类型,1平台商品,2购买会员商品", length = 4, defaultVal = "1")
    private Integer type;



//>>>>>================================================================
    //3个状态
    /**
     * @see OrderStatusEnums
     */
    @Column(value = "order_status", commit = "订单状态", length = 4)
    private Integer orderStatus;

//<<<<<================================================================


    /**
     * 用户id(当前只是门店用户在用)
     */
    @Column(value = "buyer_id", commit = "购买者userId", length = 20, isNUll = false)
    private Long buyerId;


    /**
     * 支付渠道,0线下,1微信,2支付宝
     */
    @Column(value = "pay_way", commit = "支付渠道,0线下,1微信,2支付宝", length = 4, isNUll = false)
    private Integer payWay;

    /**
     * 微信预支付id
     */
    @Column(value = "prepay_id", commit = "微信预支付id", length = 50)
    private String prepayId;

    /**
     * 第三方流水号
     */
    @Column(value = "third_pay_no", commit = "第三方流水号", length = 50)
    private String thirdPayNo;

//>>>>>================================================================
    //可能会出现的排序查询时间,其他时间建议放在订单详情表中
    /**
     * 支付时间
     */
    @Column(value = "pay_time", commit = "支付时间", defaultVal = "0", length = 20)
    private Long payTime;

    /**
     * 订单完成/关闭(不再有后续状态)
     */
    @Column(value = "finish_time", commit = "结束时间", defaultVal = "0", length = 20)
    private Long finishTime;
//<<<<<================================================================




    /**
     * 备注
     */
    @Column(value = "note", commit = "备注", length = 500)
    private String note;





//>>>>>================================================================
    //总金额-->...(若干金额计算)-->真实金额支付金额
    /**
     * 总金额
     */
    @Column(value = "total_price", length = 10,precision = 2, commit = "总金额")
    private BigDecimal totalPrice;



    /**
     * 真实金额,支付/退款金额
     */
    @Column(value = "real_price", length = 10,precision = 2, commit = "真实金额,支付/退款金额")
    private BigDecimal realPrice;
//<<<<<================================================================


    /**
     * 0可用,1删除,2失效
     */
    @Column(value = "del_status", length = 4, defaultVal = "0")
    private Integer delStatus;

    @Column(value = "create_time",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE,commit = "创建时间")
    private Timestamp createTime;

    @Column(value = "update_time",dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE,commit = "更新时间")
    private Timestamp updateTime;

}
