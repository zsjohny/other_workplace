package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.application.activity.Coupon;
import com.e_commerce.miscroservice.commons.entity.application.activity.CouponTemplate;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/9 11:54
 * @Copyright 玖远网络
 */
public interface CouponDao{
    /**
     * 根据订单号查优惠券
     *
     * @param orderNumber orderNumber
     * @return com.e_commerce.miscroservice.commons.entity.application.activity.Coupon
     * @author Charlie
     * @date 2018/11/9 12:00
     */
    Coupon findCouponByOrderNo(String orderNumber, Integer status);



    /**
     * 根据id查
     *
     * @param templateId templateId
     * @return com.e_commerce.miscroservice.commons.entity.application.activity.CouponTemplate
     * @author Charlie
     * @date 2018/11/9 13:33
     */
    CouponTemplate findTemplateById(Long templateId);
}
