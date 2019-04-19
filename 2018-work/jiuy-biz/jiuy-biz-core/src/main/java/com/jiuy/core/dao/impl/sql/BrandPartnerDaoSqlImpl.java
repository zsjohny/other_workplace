package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.BrandPartnerDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.brand.PartnerVO;
import com.jiuyuan.entity.brandcategory.PartnerCategory;
import com.jiuyuan.entity.query.PageQuery;
import com.store.entity.Brand;

public class BrandPartnerDaoSqlImpl extends DomainDaoSqlSupport<PartnerCategory, Long> implements BrandPartnerDao{

	@Override
	public int getIdByName(String name) {
		return getSqlSession().selectOne("Partner.getIdByEngName", name);
	}

	@Override
	public int addBrand(Brand brand) {
		return getSqlSession().insert("Partner.addBrand", brand);
	}

	@Override
	public List<Map<String, Object>> getPartnerByName(String name) {
		return getSqlSession().selectList("Partner.getPartnerByName", name);
	}

	@Override
	public List<Map<String, Object>> getPartnerById(long id) {
		return getSqlSession().selectList("Partner.getPartnerById", id);
	}

	@Override
	public List<PartnerVO> search(PageQuery query, String name, String engName) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("PageQuery", query);
		params.put("name", name);
		params.put("engName", engName);
		
		return getSqlSession().selectList("Partner.search", params);
	}

	@Override
	public int searchCount(String name, String engName) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", name);
		params.put("engName", engName);
		
		return getSqlSession().selectOne("Partner.searchCount", params);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public int addBrand(PartnerVO paVo) {
		return getSqlSession().insert("Partner.addBrand", paVo);
	}

	@Override
	public int updateBrand(PartnerVO paVo) {
		return getSqlSession().update("Partner.updateBrand", paVo);
	}

	@Override
	public int removeBrand(long id) {
		return getSqlSession().update("Partner.removeBrand", id);
	}

	@Override
	public String getPartnerUrl(long partnerId) {
		return getSqlSession().selectOne("Partner.getPartnerUrl", partnerId);
	}

	@Override
	public int addPartnerUrl(long partnerId, String url) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("partnerId", partnerId);
		params.put("url", url);
		
		return getSqlSession().update("Partner.addPartnerUrl", params);
	}
	
}
