package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.StoreCouponDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuyuan.constant.coupon.StoreCoupon;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

public class StoreCouponDaoSqlImpl extends SqlSupport implements StoreCouponDao{
	@Override
	public int batchAdd(List<StoreCouponTemplate> storeCouponTemplates, Long publicAdminId, Long grantAdminId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("collection", storeCouponTemplates);
		params.put("publicAdminId", publicAdminId);
		params.put("grantAdminId", grantAdminId);
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.batchAdd", params);
	}

	@Override
	public List<StoreCoupon> getNullCode(List<StoreCouponTemplate> storeCouponTemplates) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("storeCouponTemplates", storeCouponTemplates);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.getNullCode",params);
	}

	@Override
	public int batchUpdate(List<StoreCoupon> storeCoupons) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("collection", storeCoupons);
		params.put("updateTime", System.currentTimeMillis());
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.batchUpdate", params);
	}

    @Override
    public int searchCount(StoreCoupon storeCoupon, Double moneyMin, Double moneyMax, Double limitMoneyMin , Double limitMoneyMax) {
        Map<String, Object> params = new HashMap<String, Object>();
        
        params.put("storeCoupon", storeCoupon);
        params.put("moneyMin", moneyMin);
        params.put("moneyMax", moneyMax);
        params.put("limitMoneyMin", limitMoneyMin);
        params.put("limitMoneyMax", limitMoneyMax);

        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.searchCount", params);
    }

    @Override
    public List<StoreCoupon> search(PageQuery pageQuery, StoreCoupon storeCoupon, Double moneyMin, Double moneyMax, Double limitMoneyMin , Double limitMoneyMax) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("storeCoupon", storeCoupon);
        params.put("moneyMin", moneyMin);
        params.put("moneyMax", moneyMax);
        params.put("pageQuery", pageQuery);
        params.put("limitMoneyMin", limitMoneyMin);
        params.put("limitMoneyMax", limitMoneyMax);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.search", params);
    }

    @Override
    public int update(Long id, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("id", id);
        params.put("status", status);
        params.put("updateTime", System.currentTimeMillis());

        return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.update", params);
    }

    @Override
    public List<StoreCoupon> search(Integer pushStatus) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("pushStatus", pushStatus);
        params.put("status", 0);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.search", params);
    }

	@Override
	public int update(Long id, Long storeId, Long businessNumber, Integer status, Integer pushStatus, String pushTitle, String pushDescription,
                      String pushUrl, String pushImage, Long adminId) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("id", id);
        params.put("storeId", storeId);
        params.put("businessNumber", businessNumber);
        params.put("pushStatus", pushStatus);
        params.put("pushTitle", pushTitle);
        params.put("pushDescription", pushDescription);
        params.put("pushUrl", pushUrl);
        params.put("pushImage", pushImage);
        params.put("adminId", adminId);
        params.put("status", status);
        params.put("updateTime", System.currentTimeMillis());

        return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.update", params);
	}

	@Override
	public int searchCount(Integer status, Long templateId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("status", status);
		params.put("couponTemplateId", templateId);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.searchCount", params);
	}

	@Override
	public int searchAvailableCount(Long templateId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponTemplateId", templateId);
		params.put("status", 0);
		params.put("availableTime", System.currentTimeMillis());
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.searchCount", params);
	}

	@Override
	public int searchExpiredCount(Long templateId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponTemplateId", templateId);
		params.put("status", 0);
		params.put("expiredTime", System.currentTimeMillis());
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<StoreCoupon> getAvaliable(Long templateId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponTemplateId", templateId);
		params.put("availableTime", System.currentTimeMillis());
		params.put("noOwner", "");
		params.put("status", 0);
		params.put("type", 0);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.search", params);
	}

	@Override
	public List<Map<String, Object>> availableOfTemplateId(Collection<Long> templateIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponTemplateIds", templateIds);
		params.put("availableTime", System.currentTimeMillis());
		params.put("status", 0);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.availableOfTemplateId", params);
	}

	@Override
	public StoreCoupon search(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.search", params);
	}

	@Override
	public List<StoreCoupon> search(Collection<Long> orderNos, String sortSql) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNos", orderNos);
		params.put("sortSql", sortSql);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.search", params);
	}

	@Override
	public int update(Integer status, Long orderNo, Collection<Long> couponIds, Long updateTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("status", status);
		params.put("orderNo", orderNo);
		params.put("ids", couponIds);
		params.put("updateTime", updateTime);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.update", params);
	}

	@Override
	public int update(Collection<Long> ids, Integer pushStatus, Long updateTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ids", ids);
		params.put("pushStatus", pushStatus);
		params.put("updateTime", updateTime);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.update", params);
	}

	@Override
	public int returnCoupon(Long id, Integer status, Long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("status", status);
		params.put("orderNo", orderNo);
		params.put("updateTime", System.currentTimeMillis());
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.update", params);
	}

	@Override
	public StoreCoupon add(StoreCoupon storeCoupon) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.add", storeCoupon);
		return storeCoupon;
	}

	@Override
	public int batchAddByCoupons(List<StoreCoupon> coupons) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("coupons", coupons);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.StoreCouponDaoSqlImpl.batchAddByCoupons", params);
	}

}
