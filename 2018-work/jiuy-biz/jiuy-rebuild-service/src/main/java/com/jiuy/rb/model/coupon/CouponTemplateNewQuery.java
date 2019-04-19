package com.jiuy.rb.model.coupon; 

import lombok.Data; 

/**
 * CouponTemplateNew的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月06日 上午 10:32:48
 * @Copyright 玖远网络 
*/
@Data
public class CouponTemplateNewQuery extends CouponTemplateNew {  


    /**
     * 是否发送光了 1是未发送  0 已经没有了
     */
    private Integer isAlive;


    /**
     * 不在发放中的 1 不在发放中的
     */
    private Integer notSending;

    /**
     * 1的话在领取时间范围内的
     *
     */
    private Integer canReceive;


} 
