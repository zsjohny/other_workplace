package com.jiuy.core.service.coupon;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.coupon.CouponUseLog;
import com.jiuy.core.meta.coupon.StoreCouponUseLog;

public interface StoreCouponUseLogService {

	Map<Long, List<StoreCouponUseLog>> getLogsOfOrderNo(Collection<Long> orderNos);

	int add(Collection<StoreCouponUseLog> couponUseLogs);

	List<StoreCouponUseLog> search(Collection<Long> parentOrderNos, int status, String sortSQL);

	int updateStoreCouponUseLog(StoreCouponUseLog couponUseLog);

}
