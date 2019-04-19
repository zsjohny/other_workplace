package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.coupon.Coupon;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

public interface CouponDao {

	int batchAdd(List<CouponTemplate> couponTemplates, Long publicAdminId, Long grantAdminId);

	List<Coupon> getNullCode();

	int batchUpdate(List<Coupon> coupons);

    int searchCount(Coupon coupon, Double moneyMin, Double moneyMax);

    List<Coupon> search(PageQuery pageQuery, Coupon coupon, Double moneyMin, Double moneyMax);

    int update(Long id, Integer status);

    List<Coupon> search(Integer pushStatus);

    int update(Long id, Long userId, Long yjjNumber, Integer status, Integer pushStatus, String pushTitle, String pushDescription, String pushUrl,
               String pushImage, Long adminId);

	int searchCount(Integer status, Long templateId);

	int searchAvailableCount(Long templateId);

	int searchExpiredCount(Long templateId);

	List<Coupon> getAvaliable(Long templateId);

	List<Map<String, Object>> availableOfTemplateId(Collection<Long> templateIds);

	Coupon search(Long id);

	List<Coupon> search(Collection<Long> orderNos, String sortSql);

	int update(Integer status, Long orderNo, Collection<Long> couponIds, Long currentTimeMillis);

	int update(Collection<Long> ids, Integer pushStatus, Long updateTime);
	
	int returnCoupon(Long id, Integer status, Long orderNo);

	Coupon add(Coupon coupon);
	
}
