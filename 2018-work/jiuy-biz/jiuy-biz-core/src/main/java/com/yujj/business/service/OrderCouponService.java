package com.yujj.business.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.dao.mapper.CouponTemplateMapper;
import com.yujj.dao.mapper.OrderCouponMapper;
import com.yujj.dao.mapper.UserMapper;
import com.yujj.entity.account.User;
import com.yujj.entity.order.Coupon;
import com.yujj.entity.order.CouponTemplate;
import com.yujj.exception.ParameterErrorException;

@Service
public class OrderCouponService {
	
	@Autowired
	private OrderCouponMapper orderCouponMapper;
	
	@Autowired
	private CouponTemplateMapper couponTemplateMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	public List<Coupon> getAvailableCoupons(long templateId, int count) {
		long time = System.currentTimeMillis();
		return orderCouponMapper.searchAvailableCoupons(templateId, count, time);
	}

	public void batchUpdateCouponOwners(List<Coupon> coupons, long yJJNumber) {
		StringBuilder builder = new StringBuilder();
		builder.append("set YJJNumber = case id");
		StringBuilder ids = new StringBuilder();
		for (Coupon coupon : coupons) {
			long id = coupon.getId();
			
			ids.append(id + ",");
			builder.append(" when " + id + " then " + yJJNumber);
		}
		builder.append(" end, ");
		long currentTime = System.currentTimeMillis();
		builder.append("UpdateTime = " + currentTime);
		ids.deleteCharAt(ids.length()-1);
		builder.append(" where id in (" + ids + ")");
		
		//批量更新
		orderCouponMapper.batchUpdate(builder.toString());
	}

	@Transactional(noRollbackFor = ParameterErrorException.class, rollbackFor = Exception.class)
	public void getCoupon(Long templateId, Integer count, long userId, CouponGetWay couponGetWay, boolean needLimit) {
		long time = System.currentTimeMillis();
		CouponTemplate couponTemplate = couponTemplateMapper.searchValidity(templateId, time);
		
		if(couponTemplate == null) {
			throw new ParameterErrorException("找不到对应的代金券模板" + templateId + ", 不存在或已模板过期！");
		}
		
		double money = couponTemplate.getMoney();
		double publishMoney = money * count;
        
        if (needLimit) {
        	double restMoney = getRestMoney();
        	if (restMoney < publishMoney)
        		throw new ParameterErrorException("发放" + money + "元代金券" + count + "张，合计价值" + publishMoney 
        				+ "元，当前发行可用余额为" + restMoney + "，超出发行上限，请联系系统管理员。");
		}
        
        synchronized (this) {
        	JSONObject jsonObject = globalSettingService.getJsonObjectNoCache(GlobalSettingName.COUPON);
        	Double publishedMoney = jsonObject.getDouble("published_money");
        	publishedMoney += publishMoney;
        	jsonObject.put("published_money", publishedMoney);
        	globalSettingService.update(GlobalSettingName.COUPON, jsonObject.toJSONString());
		}
        
		long validityEndTime = couponTemplate.getValidityEndTime();
		if (validityEndTime != 0 && validityEndTime < System.currentTimeMillis())
			throw new ParameterErrorException("金券模板已过期!");
		User user = userMapper.getUser(userId);
		
		if (user == null) {
			throw new ParameterErrorException("用户不存在!");
		}
		
		Coupon coupon = new Coupon();
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
		coupon.setUserId(userId);
		coupon.setyJJNumber(user.getyJJNumber());
		coupon.setCreateTime(time);
		coupon.setUpdateTime(time);
		coupon.setGetWay(couponGetWay.getValue());
		coupon.setLimitMoney(couponTemplate.getLimitMoney());
		
		List<Coupon> coupons = new ArrayList<Coupon>();
		for (int i = 0; i < count; i++) {
			coupons.add(coupon);
		}
		orderCouponMapper.batchAdd(coupons);
		couponTemplateMapper.update(templateId, count);
		couponTemplateMapper.updateGrant(templateId, count);
	}


    public double getRestMoney() {
    	String couponJson = globalSettingService.getSettingNoCache(GlobalSettingName.COUPON);

        JSONObject jsonObject = JSONObject.parseObject(couponJson);
        double totalMoney = Double.parseDouble(jsonObject.get("total_money").toString());
        double publishedMoney = Double.parseDouble(jsonObject.get("published_money").toString());

        return totalMoney - publishedMoney;
    }

	public List<Coupon> search(PageQuery pageQuery, long userId, CouponGetWay couponGetWay) {
		return orderCouponMapper.search(pageQuery, userId, couponGetWay == null ? null : couponGetWay.getValue());
	}

	public int searchCount(long userId, CouponGetWay couponGetWay) {
		return orderCouponMapper.searchCount(userId, couponGetWay == null ? null : couponGetWay.getValue());
	}

	@Transactional(rollbackFor = Exception.class)
	public void getCouponFromPublished(Long templateId, int count, long userId, CouponGetWay couponGetWay) {
		long time = System.currentTimeMillis();
		CouponTemplate couponTemplate = couponTemplateMapper.searchValidity(templateId, time);
		
		if(couponTemplate == null) {
			throw new ParameterErrorException("找不到对应的代金券模板" + templateId + ", 不存在或已模板过期！");
		}
		
		long validityEndTime = couponTemplate.getValidityEndTime();
		if (validityEndTime != 0 && validityEndTime < System.currentTimeMillis())
			throw new ParameterErrorException("金券模板已过期!");
		User user = userMapper.getUser(userId);
		
		if (user == null) {
			throw new ParameterErrorException("用户不存在!");
		}
		
		List<Coupon> coupons = getAvailableCoupons(templateId, count);
		if (coupons.size() < count) {
			throw new ParameterErrorException("已发行代金券不足!");
		}

		Set<Long> couponIds = new HashSet<>();
		for (Coupon coupon : coupons) {
			couponIds.add(coupon.getId());
		}
		orderCouponMapper.batchUpdateGrant(couponIds, -1, -1, userId, user.getyJJNumber(), time, couponGetWay.getValue());
		couponTemplateMapper.updateGrant(templateId, count);
	}
	
}
