package com.jiuy.core.service.aftersale;

import java.util.List;

import com.jiuy.core.meta.aftersale.StoreSettlement;
import com.jiuy.core.meta.aftersale.StoreSettlementVO;
import com.jiuyuan.entity.query.PageQuery;

public interface StoreSettlementService {

	int searchCount(StoreSettlementVO storeSettlementVO);

	List<StoreSettlement> search(PageQuery pageQuery, StoreSettlementVO storeSettlementVO);

	List<StoreSettlement> searchAll(StoreSettlementVO storeSettlementVO);

}
