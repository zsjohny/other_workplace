package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.coupon.StoreCoupon;
import com.jiuyuan.dao.annotation.DBMaster;

/**
* @author WuWanjian
* @version 创建时间: 2017年5月27日 下午5:46:50
*/
@DBMaster
public interface StoreCouponMapper {

	int batchAddByCoupons(@Param("coupons") List<StoreCoupon> coupons);
}
