package com.jiuy.rb.util;

import com.jiuy.rb.enums.CouponSendEnum;
import com.jiuy.rb.enums.CouponStateEnum;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuyuan.constant.coupon.CouponUseStatus;
import lombok.Data;

/**
 * 获取优惠券的参数
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/3 11:18
 * @Copyright 玖远网络
 */
@Data
public class CouponAcceptVo {


    public CouponAcceptVo(Long memberId, Long storeId, String orderNo, CouponSysEnum sysEnum, CouponSendEnum sendEnum, CouponStateEnum status) {
        this.memberId = memberId;
        this.storeId = storeId;
        this.orderNo = orderNo;
        this.sysEnum = sysEnum;
        this.sendEnum = sendEnum;
        this.status = status.getCode();
    }

    /**
     * 模板id
     */
    private Long tempId;

    /**
     * 小程序用户
     */
    private Long memberId;

    /**
     * 门店用户id
     */
    private Long storeId;

    /**
     * 订单号码
     */
    private String orderNo;

    /**
     * 那个系统的
     */
    private CouponSysEnum sysEnum;

    /**
     * 如何发送的
     */
    private CouponSendEnum sendEnum;

    /**
     * 1可用，2已使用，3已经过期
     */
    private Integer status;
}
