package com.jiuyuan.service.common;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.StoreCouponTemplateNew;

public interface IStoreCouponNewService {

	void add(StoreCouponTemplateNew storeCouponTemplateNew);

	void update(StoreCouponTemplateNew storeCouponTemplateNew);

	List<Map<String, Object>> list(String couponName, Double minMoney, Double maxMoney, String minValidStartTime,
			String maxValidEndTime, Integer minValidTotalCount, Integer maxValidTotalCount, Double minValidTotalAmount,
			Double maxValidTotalAmount, Integer publishStatus, Long userId, Page<Map<String, Object>> page) throws ParseException;

	Map<String, Object> getStatistics(long userId);

	void delete(long storeCouponTemplateId, long supplierId);

	void stopDrawStoreCoupon(long supplierId, long storeTemplateId);

	List<Map<String, Object>> brandListDrawCoupon(long brandId);

	void drawSupplierCouponTemplate(long supplierCouponTemplateId, long userId, long businessNumber);

	Map<String, Object> detail(long storeCouponTemplateId, long userId);

	void doStatisticsByCouponTemplateIdWhenUse(long couponTemplateId);

}