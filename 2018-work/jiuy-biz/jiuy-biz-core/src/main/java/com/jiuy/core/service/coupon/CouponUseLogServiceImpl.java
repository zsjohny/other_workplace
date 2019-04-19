package com.jiuy.core.service.coupon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.CouponUseLogDao;
import com.jiuy.core.meta.coupon.CouponUseLog;

@Service
public class CouponUseLogServiceImpl implements CouponUseLogService {
	
	@Autowired
	private CouponUseLogDao couponUseLogDao;

	@Override
	public Map<Long, List<CouponUseLog>> getLogsOfOrderNo(Collection<Long> orderNos) {
		if (orderNos.size() < 1) {
			return new HashMap<Long, List<CouponUseLog>>();
		}
		String sortSQL = " order by OrderNo asc";
		List<CouponUseLog> couponUseLogs = couponUseLogDao.search(orderNos, 0, sortSQL);
		
		Map<Long, List<CouponUseLog>> cMap = new HashMap<Long, List<CouponUseLog>>();
		Long lastOderNo = -1L;
		List<CouponUseLog> logs = null;
		
		for (CouponUseLog couponUseLog : couponUseLogs) {
			Long orderNo = couponUseLog.getOrderNo();
			if (orderNo != lastOderNo) {
				lastOderNo = orderNo;
				logs = new ArrayList<CouponUseLog>();
				cMap.put(lastOderNo, logs);
			}
			logs.add(couponUseLog);
		}
		
		return cMap;
	}

	@Override
	public int add(Collection<CouponUseLog> couponUseLogs) {
		if (couponUseLogs.size() < 1) {
			return 0;
		}
		return couponUseLogDao.add(couponUseLogs);
	}

	@Override
	public List<CouponUseLog> search(Collection<Long> parentOrderNos, int status, String sortSQL) {
		if (parentOrderNos.size() < 1) {
			return new ArrayList<>();
		}
		return couponUseLogDao.search(parentOrderNos, status, sortSQL);
	}
}
