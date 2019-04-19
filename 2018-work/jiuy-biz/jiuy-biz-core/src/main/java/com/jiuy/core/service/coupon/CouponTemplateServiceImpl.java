package com.jiuy.core.service.coupon;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.CouponTemplateDao;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {
	@Autowired
	private CouponTemplateDao couponTemplateDao;

	@Override
    public int searchCount(CouponTemplate couponTemplate, Integer publishCountMin, Integer publishCountMax,
                           Double moneyMin, Double moneyMax) {
		return couponTemplateDao.searchCount(couponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax);
	}

	@Override
	public List<CouponTemplate> search(PageQuery pageQuery, CouponTemplate couponTemplate,
			Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax) {
		return couponTemplateDao.search(pageQuery, couponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public CouponTemplate add(CouponTemplate couponTemplate) {
		long time = System.currentTimeMillis();
		couponTemplate.setCreateTime(time);
		couponTemplate.setUpdateTime(time);
		couponTemplateDao.add(couponTemplate);
		couponTemplateDao.update(couponTemplate);
		
		return couponTemplate;
	}

	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(CouponTemplate couponTemplate) {
		CouponTemplate couponTem = couponTemplateDao.search(couponTemplate.getId());
		if(couponTem.getExchangeLimitTotalCount() != couponTemplate.getExchangeLimitTotalCount()){		//兑换总量变化 将exchangeCount置空
			couponTemplateDao.clearExchangeCount(couponTemplate.getId());
		}
		couponTemplate.setUpdateTime(System.currentTimeMillis());
		return couponTemplateDao.update(couponTemplate);
	}

	@Override
	public int update(Long id, Double money, Integer publishCount) {
        return couponTemplateDao.update(id, money, publishCount, null);
	}

	@Override
	public CouponTemplate search(Long id) {
		return couponTemplateDao.search(id);
	}

	@Override
	public int update(Long id, Integer status) {
		return couponTemplateDao.update(id, status);
	}
}
