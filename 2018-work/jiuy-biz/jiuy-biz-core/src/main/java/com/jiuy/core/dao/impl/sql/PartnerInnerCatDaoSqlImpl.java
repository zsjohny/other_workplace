package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.PartnerInnerCatDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.brand.PartnerInnerCat;

public class PartnerInnerCatDaoSqlImpl extends DomainDaoSqlSupport<PartnerInnerCat, Long> implements PartnerInnerCatDao {

	@Override
	public int searchVirtualCat(long id) {
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.PartnerInnerCatDaoSqlImpl.searchVirtualCat", id);
	}

	@Override
	public int addVirtualCat(long id) {
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.PartnerInnerCatDaoSqlImpl.addVirtualCat", id);
	}

	@Override
	public List<PartnerInnerCat> search(String name, long partnerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("name", name);
		param.put("partnerId", partnerId);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.PartnerInnerCatDaoSqlImpl.search", param);
	}

	@Override
	public int searchCount(String name, long partnerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("name", name);
		param.put("partnerId", partnerId);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.PartnerInnerCatDaoSqlImpl.searchCount", param);
	}

	@Override
	public int addInnerCat(PartnerInnerCat pic) {
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.PartnerInnerCatDaoSqlImpl.addInnerCat", pic);
	}

	@Override
	public int updateInnerCat(PartnerInnerCat pic) {
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.PartnerInnerCatDaoSqlImpl.updateInnerCat", pic);
	}

	@Override
	public int removeInnerCat(long id) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.PartnerInnerCatDaoSqlImpl.removeInnerCat", id);
	}

}
