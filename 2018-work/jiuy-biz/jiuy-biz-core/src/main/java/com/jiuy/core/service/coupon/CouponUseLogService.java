package com.jiuy.core.service.coupon;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.coupon.CouponUseLog;

public interface CouponUseLogService {

	Map<Long, List<CouponUseLog>> getLogsOfOrderNo(Collection<Long> orderNos);

	int add(Collection<CouponUseLog> couponUseLogs);

	List<CouponUseLog> search(Collection<Long> parentOrderNos, int status, String sortSQL);

}
