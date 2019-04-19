package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.constant.coupon.StoreCoupon;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

public interface StoreCouponDao {

	int batchAdd(List<StoreCouponTemplate> storeCouponTemplates, Long publicAdminId, Long grantAdminId);

	List<StoreCoupon> getNullCode(List<StoreCouponTemplate> storeCouponTemplates);

	int batchUpdate(List<StoreCoupon> coupons);

    int searchCount(StoreCoupon storeCoupon, Double moneyMin, Double moneyMax, Double limitMoneyMin , Double limitMoneyMax);

    List<StoreCoupon> search(PageQuery pageQuery, StoreCoupon storeCoupon, Double moneyMin, Double moneyMax, Double limitMoneyMin , Double limitMoneyMax);

    int update(Long id, Integer status);

    List<StoreCoupon> search(Integer pushStatus);

    int update(Long id, Long userId, Long yjjNumber, Integer status, Integer pushStatus, String pushTitle, String pushDescription, String pushUrl,
               String pushImage, Long adminId);

	int searchCount(Integer status, Long templateId);

	int searchAvailableCount(Long templateId);

	int searchExpiredCount(Long templateId);

	List<StoreCoupon> getAvaliable(Long templateId);

	List<Map<String, Object>> availableOfTemplateId(Collection<Long> templateIds);

	StoreCoupon search(Long id);

	List<StoreCoupon> search(Collection<Long> orderNos, String sortSql);

	int update(Integer status, Long orderNo, Collection<Long> couponIds, Long currentTimeMillis);

	int update(Collection<Long> ids, Integer pushStatus, Long updateTime);
	
	int returnCoupon(Long id, Integer status, Long orderNo);

	StoreCoupon add(StoreCoupon storeCoupon);

	int batchAddByCoupons(List<StoreCoupon> coupons);
	
}
