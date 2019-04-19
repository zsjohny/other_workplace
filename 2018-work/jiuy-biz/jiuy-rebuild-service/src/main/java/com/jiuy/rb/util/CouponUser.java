package com.jiuy.rb.util;

import lombok.Data;

/**
 * 优惠券目标用户
 * 目的是可以通用
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/3 11:32
 * @Copyright 玖远网络
 */
@Data
public class CouponUser {

    private Long storeId;

    private Long memberId;

    private Integer sysType;
}
