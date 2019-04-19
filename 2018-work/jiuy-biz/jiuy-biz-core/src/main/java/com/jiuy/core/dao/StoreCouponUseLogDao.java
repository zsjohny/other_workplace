package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.jiuy.core.meta.coupon.CouponUseLog;
import com.jiuy.core.meta.coupon.StoreCouponUseLog;

public interface StoreCouponUseLogDao {

	List<StoreCouponUseLog> search(Collection<Long> orderNos, Integer i, String sortSQL);

	int add(Collection<StoreCouponUseLog> storeCouponUseLogs);


	int add(StoreCouponUseLog storeCouponUseLog);

	int updateStoreCouponUseLog(StoreCouponUseLog couponUseLog);

}
