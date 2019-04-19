/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.constant.order.OrderCouponStatus;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.order.Coupon;
import com.yujj.entity.order.CouponUseLog;
import com.yujj.entity.order.Order;
import com.yujj.entity.order.OrderDiscountLog;

@DBMaster
public interface CouponUseLogMapper {

	int insertCouponUseLog(CouponUseLog couponUseLog);


}
