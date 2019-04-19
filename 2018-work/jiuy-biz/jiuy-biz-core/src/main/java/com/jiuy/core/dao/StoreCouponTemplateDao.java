package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

public interface StoreCouponTemplateDao {

	int searchCount(StoreCouponTemplate storeCouponTemplate, Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax,Double limitMoneyMin,Double limitMoneyMax,List<String> ids );

	List<StoreCouponTemplate> search(PageQuery pageQuery, StoreCouponTemplate storeCouponTemplate,
			Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax,Double limitMoneyMin,Double limitMoneyMax,List<String> ids);

	int add(StoreCouponTemplate storeCouponTemplate);

	int update(StoreCouponTemplate storeCouponTemplate);

    int update(Long id, Double money, Integer publishCount, Integer grantCount, Integer availableCount);

    StoreCouponTemplate search(Long id);

	int update(Long id, Integer status);
	
	int clearExchangeCount(Long id);

	StoreCouponTemplate searchValidity(Long templateId, long time);
	
	/**
	 * 更新发行量
	 * @param templateId
	 * @param count
	 * @return
	 */
	int updateCount(Long templateId, Integer count);

	/**
	 * 更新发放量
	 * @param templateId
	 * @param count
	 * @return
	 */
	int updateGrant(Long templateId, int count);

	int searchCount(StoreCouponTemplate storeCouponTemplate, Integer publishCountMin, Integer publishCountMax,
			Double moneyMin, Double moneyMax);

}
