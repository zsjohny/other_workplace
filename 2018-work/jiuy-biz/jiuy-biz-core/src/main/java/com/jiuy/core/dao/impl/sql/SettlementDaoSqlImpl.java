package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.SettlementDao;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuyuan.entity.afterSale.SettlementOrderNewVO;
import com.jiuyuan.entity.query.PageQuery;
@Repository
public class SettlementDaoSqlImpl implements SettlementDao{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int searchCount(SettlementOrderNewVO settlementVO) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("params", settlementVO);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.SettlementDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<OrderNew> search(PageQuery pageQuery, SettlementOrderNewVO settlementVO) {
		   Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("pageQuery", pageQuery);
			params.put("params", settlementVO);
			
			return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.SettlementDaoSqlImpl.search", params);
	}

	@Override
	public List<OrderNew> searchAll(SettlementOrderNewVO settlementVO) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", settlementVO);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.SettlementDaoSqlImpl.searchAll", params);
}

}
