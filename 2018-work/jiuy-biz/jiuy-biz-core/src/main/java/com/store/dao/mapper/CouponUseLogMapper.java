/**
 * 
 */
package com.store.dao.mapper;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.order.CouponUseLog;

@DBMaster
public interface CouponUseLogMapper {

	int insertCouponUseLog(CouponUseLog couponUseLog);

	int updateCouponUseLog(CouponUseLog couponUseLog);

}