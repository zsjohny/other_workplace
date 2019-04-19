package com.jiuy.core.service.coupon;

import java.util.List;

import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

public interface StoreCouponTemplateService {
    int searchCount(StoreCouponTemplate storeCouponTemplate, Integer publishCountMin, Integer publishCountMax, Double moneyMin,
                    Double moneyMax,Double limitMoneyMin,Double limitMoneyMax ,List<String> ids);
    
	List<StoreCouponTemplate> search(PageQuery pageQuery, StoreCouponTemplate storeCouponTemplate,
			Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax,Double limitMoneyMin,Double limitMoneyMax,List<String> ids);

	int add(StoreCouponTemplate storeCouponTemplate);

	int update(StoreCouponTemplate storeCouponTemplate);

	int update(Long id, Double money, Integer publishCount, Integer grantCount, Integer availableCount);

	StoreCouponTemplate search(Long id);

	int update(Long id, Integer status);

	int searchCount(StoreCouponTemplate storeCouponTemplate, Integer publishCountMin, Integer publishCountMax,
			Double moneyMin, Double moneyMax);
}
