package com.jiuy.rb.util;

import com.jiuy.base.util.Biz;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *  优惠券模板工具类
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/2 15:17
 * @Copyright 玖远网络
 */
@Data
public class CouponVo {

    /**
     * 构造函数
     **/
    public CouponVo(CouponTemplateNew couponTemplate) {

        this.couponTemplate = couponTemplate;
        this.who = Biz.jsonStr2Obj(couponTemplate.getGetRule(),CouponWho.class);
        String userIds = this.who.getUserIds();
        String supplierIds = this.who.getSupplierIds();
        if(Biz.isNotEmpty(userIds)) {
            this.who.setUserIdList(new ArrayList<>(Arrays.asList(userIds.split(","))));
        }
        if(Biz.isNotEmpty(supplierIds)) {
            this.who.setSupplierIdList(new ArrayList<>(Arrays.asList(supplierIds.split(","))));
        }
        String storeIds = this.who.getStoreIds();
        if(Biz.isNotEmpty(storeIds)) {
            this.who.setStoreIdsList(new ArrayList<>(Arrays.asList(storeIds.split(","))));
        }

    }
    /**
     *  模板
     */
    private CouponTemplateNew couponTemplate;

    /**
     * 如何发送
     */
    private CouponWho who;

    /**
     * 如何使用
     */
    private CouponUse use;
}
