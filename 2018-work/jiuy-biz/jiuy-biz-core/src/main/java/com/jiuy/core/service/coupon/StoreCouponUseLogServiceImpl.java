package com.jiuy.core.service.coupon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.CouponUseLogDao;
import com.jiuy.core.dao.StoreCouponUseLogDao;
import com.jiuy.core.meta.coupon.CouponUseLog;
import com.jiuy.core.meta.coupon.StoreCouponUseLog;

@Service
public class StoreCouponUseLogServiceImpl implements StoreCouponUseLogService {
	
	@Autowired
	private StoreCouponUseLogDao storeCouponUseLogDao;

	@Override
	public Map<Long, List<StoreCouponUseLog>> getLogsOfOrderNo(Collection<Long> orderNos) {
		if (orderNos.size() < 1) {
			return new HashMap<Long, List<StoreCouponUseLog>>();
		}
		String sortSQL = " order by OrderNo asc";
		List<StoreCouponUseLog> storeCouponUseLogs = storeCouponUseLogDao.search(orderNos, 0, sortSQL);
		
		Map<Long, List<StoreCouponUseLog>> cMap = new HashMap<Long, List<StoreCouponUseLog>>();
		Long lastOderNo = -1L;
		List<StoreCouponUseLog> logs = null;
		
		for (StoreCouponUseLog storeCouponUseLog : storeCouponUseLogs) {
			Long orderNo = storeCouponUseLog.getOrderNo();
			if (orderNo != lastOderNo) {
				lastOderNo = orderNo;
				logs = new ArrayList<StoreCouponUseLog>();
				cMap.put(lastOderNo, logs);
			}
			logs.add(storeCouponUseLog);
		}
		
		return cMap;
	}

	@Override
	public int add(Collection<StoreCouponUseLog> couponUseLogs) {
		if (couponUseLogs.size() < 1) {
			return 0;
		}
		return storeCouponUseLogDao.add(couponUseLogs);
	}

	@Override
	public List<StoreCouponUseLog> search(Collection<Long> parentOrderNos, int status, String sortSQL) {
		if (parentOrderNos.size() < 1) {
			return new ArrayList<>();
		}
		return storeCouponUseLogDao.search(parentOrderNos, status, sortSQL);
	}

	@Override
	public int updateStoreCouponUseLog(StoreCouponUseLog couponUseLog) {
		return storeCouponUseLogDao.updateStoreCouponUseLog(couponUseLog);
	}
}
