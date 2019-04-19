package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.CouponDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuy.core.meta.coupon.Coupon;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

public class CouponDaoSqlImpl extends SqlSupport implements CouponDao{

	@Override
	public int batchAdd(List<CouponTemplate> couponTemplates, Long publicAdminId, Long grantAdminId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("collection", couponTemplates);
		params.put("publicAdminId", publicAdminId);
		params.put("grantAdminId", grantAdminId);
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.batchAdd", params);
	}

	@Override
	public List<Coupon> getNullCode() {
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.getNullCode");
	}

	@Override
	public int batchUpdate(List<Coupon> coupons) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("collection", coupons);
		params.put("updateTime", System.currentTimeMillis());
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.batchUpdate", params);
	}

    @Override
    public int searchCount(Coupon coupon, Double moneyMin, Double moneyMax) {
        Map<String, Object> params = new HashMap<String, Object>();
        
        params.put("coupon", coupon);
        params.put("moneyMin", moneyMin);
        params.put("moneyMax", moneyMax);

        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.searchCount", params);
    }

    @Override
    public List<Coupon> search(PageQuery pageQuery, Coupon coupon, Double moneyMin, Double moneyMax) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("coupon", coupon);
        params.put("moneyMin", moneyMin);
        params.put("moneyMax", moneyMax);
        params.put("pageQuery", pageQuery);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.search", params);
    }

    @Override
    public int update(Long id, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("id", id);
        params.put("status", status);
        params.put("updateTime", System.currentTimeMillis());

        return getSqlSession().update("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.update", params);
    }

    @Override
    public List<Coupon> search(Integer pushStatus) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("pushStatus", pushStatus);
        params.put("status", 0);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.search", params);
    }

	@Override
	public int update(Long id, Long userId, Long yjjNumber, Integer status, Integer pushStatus, String pushTitle, String pushDescription,
                      String pushUrl, String pushImage, Long adminId) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("id", id);
        params.put("userId", userId);
        params.put("yjjNumber", yjjNumber);
        params.put("pushStatus", pushStatus);
        params.put("pushTitle", pushTitle);
        params.put("pushDescription", pushDescription);
        params.put("pushUrl", pushUrl);
        params.put("pushImage", pushImage);
        params.put("adminId", adminId);
        params.put("status", status);
        params.put("updateTime", System.currentTimeMillis());

        return getSqlSession().update("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.update", params);
	}

	@Override
	public int searchCount(Integer status, Long templateId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("status", status);
		params.put("couponTemplateId", templateId);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.searchCount", params);
	}

	@Override
	public int searchAvailableCount(Long templateId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponTemplateId", templateId);
		params.put("status", 0);
		params.put("availableTime", System.currentTimeMillis());
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.searchCount", params);
	}

	@Override
	public int searchExpiredCount(Long templateId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponTemplateId", templateId);
		params.put("status", 0);
		params.put("expiredTime", System.currentTimeMillis());
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<Coupon> getAvaliable(Long templateId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponTemplateId", templateId);
		params.put("availableTime", System.currentTimeMillis());
		params.put("noOwner", "");
		params.put("status", 0);
		params.put("type", 0);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.search", params);
	}

	@Override
	public List<Map<String, Object>> availableOfTemplateId(Collection<Long> templateIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponTemplateIds", templateIds);
		params.put("availableTime", System.currentTimeMillis());
		params.put("status", 0);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.availableOfTemplateId", params);
	}

	@Override
	public Coupon search(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.search", params);
	}

	@Override
	public List<Coupon> search(Collection<Long> orderNos, String sortSql) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNos", orderNos);
		params.put("sortSql", sortSql);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.search", params);
	}

	@Override
	public int update(Integer status, Long orderNo, Collection<Long> couponIds, Long updateTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("status", status);
		params.put("orderNo", orderNo);
		params.put("ids", couponIds);
		params.put("updateTime", updateTime);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.update", params);
	}

	@Override
	public int update(Collection<Long> ids, Integer pushStatus, Long updateTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("ids", ids);
		params.put("pushStatus", pushStatus);
		params.put("updateTime", updateTime);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.update", params);
	}

	@Override
	public int returnCoupon(Long id, Integer status, Long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("status", status);
		params.put("orderNo", orderNo);
		params.put("updateTime", System.currentTimeMillis());
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.update", params);
	}

	@Override
	public Coupon add(Coupon coupon) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.CouponDaoSqlImpl.add", coupon);
		return coupon;
	}

}
