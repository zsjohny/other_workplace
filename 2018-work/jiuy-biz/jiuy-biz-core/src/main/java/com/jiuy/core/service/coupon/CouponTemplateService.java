package com.jiuy.core.service.coupon;

import java.util.List;

import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

public interface CouponTemplateService {
    int searchCount(CouponTemplate couponTemplate, Integer publishCountMin, Integer publishCountMax, Double moneyMin,
                    Double moneyMax);

	List<CouponTemplate> search(PageQuery pageQuery, CouponTemplate couponTemplate,
			Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax);

	CouponTemplate add(CouponTemplate couponTemplate);

	int update(CouponTemplate couponTemplate);

	int update(Long id, Double money, Integer publishCount);

	CouponTemplate search(Long id);

	int update(Long id, Integer status);
}
