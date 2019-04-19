package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.activity.Coupon;
import com.e_commerce.miscroservice.commons.entity.application.activity.CouponTemplate;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.dao.CouponDao;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/9 11:55
 * @Copyright 玖远网络
 */
@Component
public class CouponDaoImpl implements CouponDao{


    /**
     * 根据订单号查优惠券
     *
     * @param orderNumber orderNumber
     * @return com.e_commerce.miscroservice.commons.entity.application.activity.Coupon
     * @author Charlie
     * @date 2018/11/9 12:00
     */
    @Override
    public Coupon findCouponByOrderNo(String orderNumber, Integer status) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new Coupon (),
                new MybatisSqlWhereBuild (Coupon.class)
                .eq (Coupon::getStatus, status)
                .eq (Coupon::getOrderNo, orderNumber)
        );
    }



    /**
     * 根据id查
     *
     * @param templateId templateId
     * @return com.e_commerce.miscroservice.commons.entity.application.activity.CouponTemplate
     * @author Charlie
     * @date 2018/11/9 13:33
     */
    @Override
    public CouponTemplate findTemplateById(Long templateId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new CouponTemplate (),
                new MybatisSqlWhereBuild (CouponTemplate.class)
                        .eq (CouponTemplate::getId, templateId)
        );
    }
}
