package com.jiuy.rb.util;

import lombok.Data;

/**
 * 使用条件
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/2 15:30
 * @Copyright 玖远网络
 */
@Data
public class CouponUse {

    /**
     * -1 无限制 正数为订单满足多少才能使用
     */
    private Integer canUse;

    /**
     * 1 为全场通用 2为 指定商品可以用 3为指定分类可用
     */
    private Integer type;

    /**
     * 全场 为 '' 其他对应对应的ids
     */
    private String ids ;
}
