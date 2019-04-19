package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.meta.order.OrderNew;
import com.jiuyuan.entity.afterSale.SettlementOrderNewVO;
import com.jiuyuan.entity.query.PageQuery;

public interface SettlementDao {

	int searchCount(SettlementOrderNewVO settlementVO);

	List<OrderNew> search(PageQuery pageQuery, SettlementOrderNewVO settlementVO);

	List<OrderNew> searchAll(SettlementOrderNewVO settlementVO);

}
