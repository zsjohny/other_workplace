package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreSettlementDao;
import com.jiuy.core.meta.aftersale.StoreSettlement;
import com.jiuy.core.meta.aftersale.StoreSettlementVO;
import com.jiuyuan.entity.query.PageQuery;
@Repository
public class StoreSettlementDaoSqlImpl implements StoreSettlementDao{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int searchCount(StoreSettlementVO storeSettlementVO) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("params", storeSettlementVO);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreSettlementDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<StoreSettlement> search(PageQuery pageQuery, StoreSettlementVO storeSettlementVO) {
		   Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageQuery", pageQuery);
			params.put("params", storeSettlementVO);
		
			return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreSettlementDaoSqlImpl.search", params);
	}

	@Override
	public List<StoreSettlement> searchAll(StoreSettlementVO storeSettlementVO) {
		 Map<String, Object> params = new HashMap<String, Object>();
			params.put("params", storeSettlementVO);
		
			return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreSettlementDaoSqlImpl.searchAll", params);
	}

}
