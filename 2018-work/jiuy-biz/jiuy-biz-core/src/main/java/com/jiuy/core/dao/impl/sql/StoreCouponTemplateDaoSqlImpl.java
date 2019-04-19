package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.StoreCouponTemplateDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

public class StoreCouponTemplateDaoSqlImpl extends SqlSupport implements StoreCouponTemplateDao {

	@Override
	public int searchCount(StoreCouponTemplate storeCouponTemplate, Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax,Double limitMoneyMin,Double limitMoneyMax,List<String> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("storeCouponTemplate", storeCouponTemplate);
		params.put("publishCountMin", publishCountMin);
		params.put("publishCountMax", publishCountMax);
		params.put("moneyMin", moneyMin);
		params.put("moneyMax", moneyMax);
		params.put("limitMax", limitMoneyMax);
		params.put("limitMin", limitMoneyMin);
		params.put("ids", ids);
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<StoreCouponTemplate> search(PageQuery pageQuery, StoreCouponTemplate storeCouponTemplate,
			Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax,Double limitMoneyMin,Double limitMoneyMax,List<String> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("storeCouponTemplate", storeCouponTemplate);
		params.put("publishCountMin", publishCountMin);
		params.put("publishCountMax", publishCountMax);
		params.put("moneyMin", moneyMin);
		params.put("moneyMax", moneyMax);
		params.put("limitMax", limitMoneyMax);
		params.put("limitMin", limitMoneyMin);
		params.put("ids", ids);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.search", params);
	}

	@Override
	public int add(StoreCouponTemplate storeCouponTemplate) {
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.add", storeCouponTemplate);
	}

	@Override
	public int update(StoreCouponTemplate storeCouponTemplate) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.update", storeCouponTemplate);
	}

	@Override
    public int update(Long id, Double money, Integer publishCount, Integer grantCount, Integer availableCount) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("money", money);
		params.put("publishCount", publishCount);
        params.put("grantCount", grantCount);
        params.put("availableCount", availableCount);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.update", params);
	}

	@Override
	public StoreCouponTemplate search(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.search", params);
	}

	@Override
	public int update(Long id, Integer status) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("status", status);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.update", params);
	}

	@Override
	public int clearExchangeCount(Long id) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.clearExchangeCount",id);
	}

	@Override
	public StoreCouponTemplate searchValidity(Long templateId, long time) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("templateId", templateId);
		params.put("time", time);	
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.searchValidity",params);
	}
	
	@Override
	public int updateCount(Long templateId, Integer count) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("templateId", templateId);
		params.put("count", count);	
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.updateCount",params);
	}

	@Override
	public int updateGrant(Long templateId, int count) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("templateId", templateId);
		params.put("count", count);	
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.updateGrant",params);
	}

	@Override
	public int searchCount(StoreCouponTemplate storeCouponTemplate, Integer publishCountMin, Integer publishCountMax,
			Double moneyMin, Double moneyMax) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("storeCouponTemplate", storeCouponTemplate);
		params.put("publishCountMin", publishCountMin);
		params.put("publishCountMax", publishCountMax);
		params.put("moneyMin", moneyMin);
		params.put("moneyMax", moneyMax);
			return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.StoreCouponTemplateDaoSqlImpl.searchCount", params);
	}
	
}
