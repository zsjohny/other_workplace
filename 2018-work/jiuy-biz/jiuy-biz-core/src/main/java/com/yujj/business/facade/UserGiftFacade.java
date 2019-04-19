package com.yujj.business.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserGiftLog;
import com.yujj.business.service.CouponTemplateService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.OrderCouponService;
import com.yujj.business.service.UserCoinService;
import com.yujj.dao.mapper.UserGiftLogMapper;
import com.yujj.entity.order.CouponTemplate;

@Service
public class UserGiftFacade {

	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private OrderCouponService orderCouponService;
	
	@Autowired
	private UserGiftLogMapper userGiftLogMapper;
	
	@Autowired
	private CouponTemplateService couponTemplateService;
	
	@Autowired
	private UserCoinService userCoinService;
	
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> fetch(long userId, ClientPlatform clientPlatform) {
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.GIFT_CONTENT);

		JSONArray jsonArray = (JSONArray) jsonObject.get("content");
		if (jsonArray == null ) {
			return new HashMap<>();
		}
		
		Map<Long, Integer> giftMap = new HashMap<Long, Integer>();
		int totalCount = 0;
		for (Object object : jsonArray) {
			JSONObject jsonObject2 = (JSONObject)object;
			Long couponTemplateId = Long.parseLong(jsonObject2.get("coupon_template_id").toString());
			Integer count = Integer.parseInt(jsonObject2.get("coupon_count").toString());
			
			totalCount += count;
			giftMap.put(couponTemplateId, count);
		}
		long current = System.currentTimeMillis();
		UserGiftLog userGiftLog = new UserGiftLog();
		userGiftLog.setCreateTime(current);
		userGiftLog.setGiftId(-1L);
		userGiftLog.setUserId(userId);
		userGiftLog.setContent(jsonObject.toJSONString());
		userGiftLogMapper.add(userGiftLog);
		
        Map<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry<Long, Integer> item : giftMap.entrySet()) {
			Long templateId = item.getKey();
			Integer count = item.getValue();
			orderCouponService.getCoupon(templateId, count, userId, CouponGetWay.FETCH, true);
		}

		JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
		int giftBag = jiucoin_global_setting.getInteger("giftBag");
		userCoinService.updateUserCoin(userId, 0, giftBag, "礼包送积分", current, UserCoinOperation.FETCH_GIFT, null, clientPlatform.getVersion());
		
		map.put("fetch_count", totalCount);
		
		return map;
	}
	
	public List<Map<String, Object>> getGiftInfo() {
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.GIFT_CONTENT);
		JSONArray jsonArray = (JSONArray) jsonObject.get("content");
		Set<Long> couponTemplateIds = new HashSet<>();
		for (Object object : jsonArray) {
			JSONObject jsonObject2= (JSONObject)object;
			Long couponTemplateId = Long.parseLong(jsonObject2.get("coupon_template_id").toString()); 
			couponTemplateIds.add(couponTemplateId);
		}
		
		Map<Long, CouponTemplate> couponTemplateMap = couponTemplateService.searchMap(couponTemplateIds);
		List<Map<String, Object>> list = new ArrayList<>();
		for (Object object : jsonArray) {
			JSONObject jsonObject2= (JSONObject)object;
			Long couponTemplateId = Long.parseLong(jsonObject2.get("coupon_template_id").toString()); 
			Integer couponCount = Integer.parseInt(jsonObject2.get("coupon_count").toString()); 
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			CouponTemplate couponTemplate = couponTemplateMap.get(couponTemplateId);
			map.put("coupon", couponTemplate.description());
			map.put("coupon_count", couponCount);
			list.add(map);
		}
		
		return list;
	}

	public Object getGiftTitle() {
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.GIFT_CONTENT);
		return jsonObject.get("title");
	}

}
