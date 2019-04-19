package com.jiuyuan.common;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 优惠券的ref对象
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/7 10:14
 * @Copyright 玖远网络
 */
@Data
public class CouponRbRef {

    /**
     * 优惠券Id
     */
    private Long id;


    /**
     * 优惠券名称
     */
    private String name ;

    /**
     * 优惠券价格
     */
    private BigDecimal price;


    /**
     * 优惠券折扣
     */
    private BigDecimal discount;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 发布者id
     */
    private Long publishUserId;

    /**
     * 状态 -2删除 -1:作废  0:未用 1:已使用
     */
    private Integer status;

    /**
     * 使用门槛
     */
    private BigDecimal limitMoney;

    /**
     * 发布者姓名
     */
    private String publishUser;

    /**
     * 扣减了多少钱
     * 临时字段
     */
    private BigDecimal subMoney;

    /**
     * 订单金额
     * 临时字段
     */
    private BigDecimal totalMonye;
}
