package com.jiuy.rb.model.order; 

import com.jiuy.rb.enums.OrderCloseTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * StoreOrderRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月12日 下午 06:21:32
 * @Copyright 玖远网络 
*/
@Data
public class StoreOrderRbQuery extends StoreOrderRb {

    /**
     * 更新时间开始
     */
    private String updateTimeStart;

    /**
     * 更新时间结束
     */
    private String updateTimeEnd;

    /**
     * 创建时间开始
     */
    private String createTimeBegin;

    /**
     * 创建时间结束
     */
    private String createTimeEnd;

    /**
     * 收件人名字
     */
    private String addresseeName;

    /**
     * 收件人电话
     */
    private String addresseeTelePhone;

    /**
     * 款号
     */
    private String clothesNumbers;

    /**
     * 客户名称 - store
     */
    private String customerName;

    /**
     * 客户电话  store
     */
    private String customerPhone;

    /**
     * 快递单号
     */
    private String expressNo;
    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 订单状态
     */
    private String orderStatusName;

    /**
     * 订单加价金额
     */
    private BigDecimal supplierAddPrice;

    /**
     * 是否是客户
     */
    private Integer isCustomer;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 是否有售后 1 有 0 没有
     */
    private Integer haveRefund;

    /**
     * 门店名称
     */
    private String businessName;

    /**
     * 订单详情
     */
    List<StoreOrderItemRbQuery> storeOrderItemList;


    /**
     * 物流公司简写
     */
    private String expressCompanyName;

    /**
     * 物流公司中文名称
     */
    private String expressCnName;



    public Long getRealCloseTime () {
        long closeTime = 0;
        //orderCloseType订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105平台客服关闭订单
        int orderCloseType = this.getOrderCloseType();
        //101买家主动取消订单
        if(OrderCloseTypeEnum.BUYER_CLOSE.isThis(orderCloseType)){
            closeTime = this.getOrderCloseTime();
        //102超时未付款系统自动关闭订单
        }else if(OrderCloseTypeEnum.UNPAID.isThis(orderCloseType)){
            closeTime = this.getOrderCloseTime();
        //103全部退款关闭订单
        }else if(OrderCloseTypeEnum.REFUND_ALL_CLOSE.isThis(orderCloseType)){
            closeTime = this.getOrderCloseTime();
        //104卖家关闭订单
        }
        return closeTime;
    }

} 
