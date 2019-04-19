package com.jiuy.core.service.aftersale;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.StoreSettlementDao;
import com.jiuy.core.meta.aftersale.StoreSettlement;
import com.jiuy.core.meta.aftersale.StoreSettlementVO;
import com.jiuyuan.entity.query.PageQuery;
@Service
public class StoreSettlementServiceImpl implements StoreSettlementService{
	
	@Resource
	private StoreSettlementDao storeSettlementDao;
	

	@Override
	public int searchCount(StoreSettlementVO storeSettlementVO) {
		return storeSettlementDao.searchCount(storeSettlementVO);
	}


	@Override
	public List<StoreSettlement> search(PageQuery pageQuery, StoreSettlementVO storeSettlementVO) {
		return storeSettlementDao.search(pageQuery,storeSettlementVO);
	}


	@Override
	public List<StoreSettlement> searchAll(StoreSettlementVO storeSettlementVO) {
		return storeSettlementDao.searchAll(storeSettlementVO);
	}

}
