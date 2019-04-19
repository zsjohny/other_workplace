package com.jiuy.rb.model.order; 

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ShopMemberOrderRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月17日 上午 11:25:54
 * @Copyright 玖远网络 
*/
@Data
public class ShopMemberOrderRbQuery extends ShopMemberOrderRb {


    /**
     * 当前时间的时间戳
     */
    private Long currentTime;

//    /**
//     * 商品ID
//     */
//    private String storeId;
//
//    /**
//     * 商品名称
//     */
    private String storeName;
//
//    /**
//     * 商品款号
//     */
//    private String clothesNumber;

    /**
     * 订单详情
     */

    /**
     * 创建时间开始
     */
    private String createTimeBegin;

    /**
     * 创建时间结束
     */
    private String createTimeEnd;

    /**
     * 物流公司
     */
    private String expressInfoCom;

    /**
     * 物流公司
     */
    private String expressInfoCompany;

    /**
     * 物流公司订单好
     */
    private String expressInfoNo;

    /**
     * 创建时间
     */
    private String createTimeStr;

    /**
     * 更新时间
     */
    private String updateTimeStr;

    /**
     * 发货时间时间
     */
    private String deliveryTimeStr;

    /**
     * 订单关闭时间
     */
    private String orderStopTimeStr;

    /**
     * 订单完成时间
     */
    private String orderFinishTimeStr;

    /**
     * 订单提货时间
     */
    private String takeDeliveryTimeStr;

    /**
     * 订单付款时间
     */
    private String payTimeStr;

    /**
     * 物流信息
     */
    List<Map<String, Object>> expressInfoList;

    /**
     * 是否锁表
     */
    private Boolean isSelectForUpd;

    /**
     * 平台优惠金额
     */
    private BigDecimal platformCoupon;

    /**
     * 商家优惠金额
     */
    private BigDecimal businessCoupon;

    List<ShopMemberOrderItemRbQuery> shopMemberOrderItemList;
}
