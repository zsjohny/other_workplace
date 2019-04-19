package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.CouponTemplateDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

public class CouponTemplateDaoSqlImpl extends SqlSupport implements CouponTemplateDao {

	@Override
	public int searchCount(CouponTemplate couponTemplate, Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponTemplate", couponTemplate);
		params.put("publishCountMin", publishCountMin);
		params.put("publishCountMax", publishCountMax);
		params.put("moneyMin", moneyMin);
		params.put("moneyMax", moneyMax);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.CouponTemplateDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<CouponTemplate> search(PageQuery pageQuery, CouponTemplate couponTemplate,
			Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pageQuery", pageQuery);
		params.put("couponTemplate", couponTemplate);
		params.put("publishCountMin", publishCountMin);
		params.put("publishCountMax", publishCountMax);
		params.put("moneyMin", moneyMin);
		params.put("moneyMax", moneyMax);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CouponTemplateDaoSqlImpl.search", params);
	}

	@Override
	public CouponTemplate add(CouponTemplate couponTemplate) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.CouponTemplateDaoSqlImpl.add", couponTemplate);
		return couponTemplate;
	}

	@Override
	public int update(CouponTemplate couponTemplate) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.CouponTemplateDaoSqlImpl.updatePublished", couponTemplate);
	}

	@Override
    public int update(Long id, Double money, Integer publishCount, Integer grantCount) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("money", money);
		params.put("publishCount", publishCount);
        params.put("grantCount", grantCount);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.CouponTemplateDaoSqlImpl.update", params);
	}

	@Override
	public CouponTemplate search(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.CouponTemplateDaoSqlImpl.search", params);
	}

	@Override
	public int update(Long id, Integer status) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("status", status);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.CouponTemplateDaoSqlImpl.update", params);
	}

	@Override
	public int clearExchangeCount(Long id) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.CouponTemplateDaoSqlImpl.clearExchangeCount",id);
	}
	
}
