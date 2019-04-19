package com.jiuy.core.service.storeaftersale;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.StoreFinanceTicketDao;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicket;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicketVO;
@Service
public class StoreFinanceTicketServiceImpl implements StoreFinanceTicketService{
	
	@Resource
	private StoreFinanceTicketDao financeTicketDao;

	@Override
	public int searchCount(StoreFinanceTicketVO financeTicket) {
		return financeTicketDao.searchCount(financeTicket);
	}

	@Override
	public List<Map<String, Object>> search(PageQuery pageQuery, StoreFinanceTicketVO financeTicket) {
		List<Map<String, Object>> list = financeTicketDao.search(pageQuery,financeTicket);
		for(Map<String, Object> map : list) {
			map.put("ApplyTimeStr",DateUtil.convertMSEL((long)map.get("ApplyTime")));
			map.put("CreateTimeStr",DateUtil.convertMSEL((long)map.get("CreateTime")));
			map.put("ProcessTimeStr",DateUtil.convertMSEL((long)map.get("ProcessTime")));
		}
		return list;
	}

	@Override
	public int updateFinanceTicket(StoreFinanceTicket financeTicket) {
		
		return financeTicketDao.updateWithDraw(financeTicket);
	}

	@Override
	public int add(long serviceId, int returnType,int sourceType,long storeId) {
		long time = System.currentTimeMillis();
		return financeTicketDao.addFinance(serviceId, returnType, time,sourceType,storeId);
	}

	@Override
	public int addFromRevoke(StoreFinanceTicket storeFinanceTicket) {
		return financeTicketDao.addFromRevoke(storeFinanceTicket);
	}
}
