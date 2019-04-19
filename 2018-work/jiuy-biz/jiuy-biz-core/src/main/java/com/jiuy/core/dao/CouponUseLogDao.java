package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.jiuy.core.meta.coupon.CouponUseLog;

public interface CouponUseLogDao {

	List<CouponUseLog> search(Collection<Long> orderNos, Integer i, String sortSQL);

	int add(Collection<CouponUseLog> couponUseLogs);

	int add(CouponUseLog couponUseLog);

}
