package com.jiuy.core.service.coupon;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.StoreCouponTemplateDao;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class StoreCouponTemplateServiceImpl implements StoreCouponTemplateService {
	@Autowired
	private StoreCouponTemplateDao storeCouponTemplateDao;

	@Override
    public int searchCount(StoreCouponTemplate storeCouponTemplate, Integer publishCountMin, Integer publishCountMax,
                           Double moneyMin, Double moneyMax,Double limitMoneyMin,Double limitMoneyMax,List<String> ids) {
		return storeCouponTemplateDao.searchCount(storeCouponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax,limitMoneyMin,limitMoneyMax ,ids);
	}

	@Override
	public List<StoreCouponTemplate> search(PageQuery pageQuery, StoreCouponTemplate storeCouponTemplate,
			Integer publishCountMin, Integer publishCountMax, Double moneyMin, Double moneyMax,Double limitMoneyMin,Double limitMoneyMax,List<String> ids) {
		return storeCouponTemplateDao.search(pageQuery, storeCouponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax,limitMoneyMin,limitMoneyMax,ids);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(StoreCouponTemplate storeCouponTemplate) {
		long time = System.currentTimeMillis();
		storeCouponTemplate.setCreateTime(time);
		storeCouponTemplate.setUpdateTime(time);
		
		//storeCouponTemplateDao.update(storeCouponTemplate);
		
		return storeCouponTemplateDao.add(storeCouponTemplate);
	}

	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(StoreCouponTemplate storeCouponTemplate) {
		StoreCouponTemplate storeCouponTem = storeCouponTemplateDao.search(storeCouponTemplate.getId());
		if(storeCouponTem.getExchangeLimitTotalCount() != storeCouponTemplate.getExchangeLimitTotalCount()){		//兑换总量变化 将exchangeCount置空
			storeCouponTemplateDao.clearExchangeCount(storeCouponTemplate.getId());
		}
		storeCouponTemplate.setUpdateTime(System.currentTimeMillis());
		return storeCouponTemplateDao.update(storeCouponTemplate);
	}

	@Override
	public int update(Long id, Double money, Integer publishCount, Integer grantCount, Integer availableCount) {
        return storeCouponTemplateDao.update(id, money, publishCount, grantCount, availableCount);
	}

	@Override
	public StoreCouponTemplate search(Long id) {
		return storeCouponTemplateDao.search(id);
	}

	@Override
	public int update(Long id, Integer status) {
		return storeCouponTemplateDao.update(id, status);
	}

	@Override
	public int searchCount(StoreCouponTemplate storeCouponTemplate, Integer publishCountMin, Integer publishCountMax,
			Double moneyMin, Double moneyMax) {
		return storeCouponTemplateDao.searchCount(storeCouponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax);
	}
}
