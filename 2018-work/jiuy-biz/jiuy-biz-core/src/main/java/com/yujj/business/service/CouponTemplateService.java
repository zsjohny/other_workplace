package com.yujj.business.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.service.common.MemcachedService;
import com.yujj.dao.mapper.CouponTemplateMapper;
import com.yujj.entity.order.CouponTemplate;

@Service
public class CouponTemplateService {
	
	@Autowired
	private CouponTemplateMapper couponTemplateMapper;
	
    @Autowired
    private MemcachedService memcachedService;
    
	public CouponTemplate search(Long id) {
		String groupKey = MemcachedKey.GROUP_KEY_DROW_LOTTERY;
		String key = id + "";
		Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
        	return (CouponTemplate)obj;
        } else {
        	CouponTemplate couponTemplate = couponTemplateMapper.search(id);
        	if (couponTemplate != null) {
        		memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, couponTemplate);
			}
            return couponTemplate;
        }
	}

	public int update(Long id, Integer count) {
		return couponTemplateMapper.update(id, count);
	}

	public Map<Long, CouponTemplate> searchMap(Collection<Long> couponTemplateIds) {
		if (couponTemplateIds.size() < 1) {
			return new HashMap<>();
		}
		return couponTemplateMapper.searchMap(couponTemplateIds);
	} 

}
