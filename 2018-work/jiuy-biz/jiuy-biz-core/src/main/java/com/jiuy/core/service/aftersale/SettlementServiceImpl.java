package com.jiuy.core.service.aftersale;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.SettlementDao;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuyuan.entity.afterSale.SettlementOrderNewVO;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class SettlementServiceImpl implements SettlementService{

	@Resource
	private SettlementDao settlementDao;
	
	@Override
	public int searchCount(SettlementOrderNewVO settlementVO) {
		return settlementDao.searchCount(settlementVO);
	}

	@Override
	public List<OrderNew> search(PageQuery pageQuery, SettlementOrderNewVO settlementVO) {
		return settlementDao.search(pageQuery,settlementVO);
	}

	@Override
	public List<OrderNew> searchAll(SettlementOrderNewVO settlementVO) {
		return settlementDao.searchAll(settlementVO);
	}

}
