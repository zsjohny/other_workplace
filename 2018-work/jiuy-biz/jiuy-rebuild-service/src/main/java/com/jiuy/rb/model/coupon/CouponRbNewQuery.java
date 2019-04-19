package com.jiuy.rb.model.coupon; 

import lombok.Data; 

/**
 * CouponRbNew的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月03日 上午 11:07:30
 * @Copyright 玖远网络 
*/
@Data
public class CouponRbNewQuery extends CouponRbNew {  



    /**
     * 有效类型 1:可用的 0 是失效 2 是已使用
     * 主要用来控制时间
     */
    private Integer aliveType;

    /**
     * 使用场景
     * 1 全场可用，2指定商品可用 3指定分类可用
     */
    private Integer useRange;

    /**
     * 品牌ids 或者商品ids
     */
    private String rangeIds;

    /**
     *描述 发放对象描述
     */
    private String sendStrComment;

    /**
     * 优惠券类型:,0红包,1优惠券,2打折券
     */
    private Integer couponType;

} 
