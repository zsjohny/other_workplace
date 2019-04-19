package com.jiuy.core.service.qianmi;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.QMExpressInfoDao;
import com.jiuyuan.entity.qianmi.QMExpressInfo;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */

@Service
public class QMExpressInfoServiceImpl implements QMExpressInfoService {
	
	@Autowired
	private QMExpressInfoDao qMExpressInfoDao;

	@Override
	public Map<Long, QMExpressInfo> expressInfoOfNos(Collection<Long> orderNos) {
		if (orderNos != null && orderNos.size() < 1) {
			return new HashMap<>();
		}
		Map<Long, QMExpressInfo> map = new HashMap<>();
		List<QMExpressInfo> qmExpressInfos = qMExpressInfoDao.search(orderNos);
		for (QMExpressInfo qmExpressInfo : qmExpressInfos) {
			map.put(qmExpressInfo.getOrderNo(), qmExpressInfo);
		}
		
		return map;
	}
}
