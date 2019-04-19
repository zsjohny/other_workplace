package com.yujj.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.constant.coupon.StoreCoupon;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.yujj.dao.mapper.StoreBusinessMapper;
import com.yujj.dao.mapper.StoreCouponMapper;
import com.yujj.dao.mapper.StoreCouponTemplateMapper;
import com.yujj.entity.StoreBusiness;
import com.yujj.exception.ParameterErrorException;

/**
* @author WuWanjian
* @version 创建时间: 2017年5月27日 下午5:50:22
*/
@Service
public class StoreCouponService {
	
	@Autowired
	private StoreCouponMapper storeCouponMapper;
	
	@Autowired
	private StoreBusinessMapper storeBusinessMapper;
	
	@Autowired
	private StoreCouponTemplateMapper storeCouponTemplateMapper;
	
	@Transactional(noRollbackFor = ParameterErrorException.class, rollbackFor = Exception.class)
	public void getCoupon(Long templateId, Integer count, long storeId, CouponGetWay couponGetWay, boolean needLimit){
		long time = System.currentTimeMillis();
		StoreCouponTemplate couponTemplate = storeCouponTemplateMapper.searchValidity(templateId, time);
		
		if(couponTemplate == null) {
			throw new ParameterErrorException("找不到对应的优惠券模板" + templateId + ", 不存在或已模板过期！");
		}
		
		double money = couponTemplate.getMoney();
		double publishMoney = money * count;
		
		long validityEndTime = couponTemplate.getValidityEndTime();
		if (validityEndTime != 0 && validityEndTime < System.currentTimeMillis())
			throw new ParameterErrorException("优惠券模板已过期!");
		StoreBusiness storeBusiness =storeBusinessMapper.getById(storeId);
	//	User user = userMapper.getUser(userId);
		
		if (storeBusiness == null) {
			throw new ParameterErrorException("用户不存在!");
		}
		
		StoreCoupon coupon = new StoreCoupon();
		coupon.setCouponTemplateId(couponTemplate.getId());
		coupon.setTemplateName(couponTemplate.getName());
		coupon.setType(couponTemplate.getType());
		coupon.setMoney(couponTemplate.getMoney());
		coupon.setRangeType(couponTemplate.getRangeType());
		coupon.setRangeContent(couponTemplate.getRangeContent());
		coupon.setValidityStartTime(couponTemplate.getValidityStartTime());
		coupon.setValidityEndTime(couponTemplate.getValidityEndTime());
		coupon.setIsLimit(couponTemplate.getIsLimit());
		coupon.setCoexist(couponTemplate.getCoexist());
		coupon.setGrantAdminId(-1L);
		coupon.setPublishAdminId(-1L);
		coupon.setPushStatus(-1);
		coupon.setStoreId(storeBusiness.getId());
		coupon.setBusinessNumber(storeBusiness.getBusinessNumber());
		coupon.setCreateTime(time);
		coupon.setUpdateTime(time);
		coupon.setGetWay(couponGetWay.getValue());
		coupon.setLimitMoney(couponTemplate.getLimitMoney());
		
		List<StoreCoupon> coupons = new ArrayList<StoreCoupon>();
		for (int i = 0; i < count; i++) {
			coupons.add(coupon);
		}
		storeCouponMapper.batchAddByCoupons(coupons);
		storeCouponTemplateMapper.updateCount(templateId, count);
		storeCouponTemplateMapper.updateGrant(templateId, count);
	}
}
