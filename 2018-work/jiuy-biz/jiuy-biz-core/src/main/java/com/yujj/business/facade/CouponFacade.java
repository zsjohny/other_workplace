package com.yujj.business.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.constant.coupon.RangeType;
import com.jiuyuan.entity.FetchCouponCenterLog;
import com.jiuyuan.util.DateUtil;
import com.yujj.business.service.BrandService;
import com.yujj.business.service.CategoryService;
import com.yujj.business.service.CouponTemplateService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.OrderCouponService;
import com.yujj.dao.mapper.FetchCouponCenterLogMapper;
import com.yujj.entity.Brand;
import com.yujj.entity.order.CouponTemplate;
import com.yujj.entity.product.Category;

@Service
public class CouponFacade {
	
	@Autowired
	private CouponTemplateService couponTemplateService;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private OrderCouponService orderCouponService;

	@Autowired
	private FetchCouponCenterLogMapper fetchCouponCenterLogMapper;
	
	public List<Map<String, Object>> loadFetchCenterCoupons(long userId) {
		List<Map<String, Object>> list = new ArrayList<>();
		JSONObject fetchCoupon = globalSettingService.getJsonObject(GlobalSettingName.FETCH_COUPON);
		
		JSONArray setting = (JSONArray) fetchCoupon.get("setting");
		if (setting == null || setting.size() < 1) {
			return list;
		}
		
		JSONArray setting_2 = filterItems(setting, userId);
		
		sortItems(setting_2);
		
		assembleFetchCenterList(setting_2, list);
		
		return list;
	}

	/**
	 * 包装数据显示
	 */
	private void assembleFetchCenterList(JSONArray jsonArray, List<Map<String, Object>> list) {
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			Long coupon_template_id = jsonObject.getLong("coupon_template_id");
			Integer coupon_count = jsonObject.getInteger("coupon_count");
			Long start_time = jsonObject.getLong("start_time");
			Long end_time = jsonObject.getLong("end_time");
			
			Map<String, Object> map = new HashMap<>();
			
			Object obj = jsonObject.get("fetching");
			map.put("fetching", obj);
			
			CouponTemplate couponTemplate = couponTemplateService.search(coupon_template_id);
			map.put("coupon_template", couponTemplate);
			map.put("count_down", calcTimeDif(start_time, end_time, map));
			
			int rangType = couponTemplate.getRangeType();
			RangeType rt = RangeType.getByValue(rangType);
			map.put("rang_type", rt == null ? "" : rt.getDescription());
			map.put("start_time", DateUtil.format(start_time, "yyyy-MM-dd HH:mm:ss"));
			map.put("end_time", DateUtil.format(end_time, "yyyy-MM-dd HH:mm:ss"));
			int isLimit = couponTemplate.getIsLimit();
			List<String> descriptions = new ArrayList<>();
			
			String rangeContent = couponTemplate.getRangeContent();
			
			descriptions.add(isLimit == 0 ? "1、本券不可使用于优惠活动。" : "1、本券可与优惠活动同时使用。");
			descriptions.add(assembleDescription(rangeContent, rt, map, couponTemplate));
			
			map.put("descriptions", descriptions);
			
			for (int i = 0; i < coupon_count; i++) {
				list.add(map);
			}
		}
		
	}

	private String assembleDescription(String rangeContent, RangeType rt, Map<String, Object> map, CouponTemplate couponTemplate) {
		StringBuilder builder = new StringBuilder();
		List<String> abstracts = new ArrayList<>();
		List<Object> list = new ArrayList<>();
		switch (rt) {
		case GENERAL:
			builder.append("2、平台所有商品均可使用。");
			
			abstracts.add(couponTemplate.getMoney() + "元 " + couponTemplate.getName());
			abstracts.add("通用");
			break;
		case CATEGORY:

			list = getValue(rangeContent, "categoryIds");
			if (list.size() > 0) { 
				builder.append("2、仅限用于购买");
				for (Object categoryId_obj : list) {
					Long categoryId = Long.parseLong(categoryId_obj.toString());
					Category category = categoryService.getCategoryById(categoryId);
					builder.append(category.getCategoryName() + "、");
				}
				builder.deleteCharAt(builder.length()-1);
				builder.append("品类中的商品");
			} else {
				builder.append("2、暂无指定品类。");
			}
			
			double limitMoney = couponTemplate.getLimitMoney();
			if (limitMoney >= 0.01) {
				builder.append("，满" + limitMoney + "元可用");
			} else {
				builder.append("。");
			}
			
			abstracts.add( couponTemplate.getMoney() + "元 " + couponTemplate.getName());
			abstracts.add("特定品类可用");
			
			break;
		case BRAND:
			list = getValue(rangeContent, "brandIds");
			if (list.size() > 0) {
				builder.append("2、仅限用于购买");
				for (Object brandId_obj : list) {
					Long brandId = Long.parseLong(brandId_obj.toString());
					Brand brand = brandService.getBrand(brandId);
					builder.append(brand.getBrandName() + "、");
				}
				builder.deleteCharAt(builder.length()-1);
				builder.append("品牌的商品。");
			} else {
				builder.append("2、暂无指定品牌。");
			}
			
			abstracts.add(couponTemplate.getMoney() + "元 " + couponTemplate.getName());
			abstracts.add("特定品牌可用");
			
			break;
		case LIMIT_ORDER:
			list = getValue(rangeContent, "limitOrders");
			if (list.size() > 0) {
				builder.append("2、全场不限品类满");
				Integer money = 0;
				for (Object limitOrder_obj : list) {
					money = Integer.parseInt(limitOrder_obj.toString());
					break;
				}
				builder.deleteCharAt(builder.length()-1);
				builder.append(money + "元可以使用。");
				
				abstracts.add(couponTemplate.getMoney() + "元 " + couponTemplate.getName());
				abstracts.add("满" + money + "元可用");
			} else {
				builder.append("2、暂无指定限额。");
			}
			break;
		default:
			break;
		}
		
		map.put("abstracts", abstracts);
		return builder.toString();
	}
	
	public List<Object> getValue(String rangeContent, String key) {
		JSONObject jsonObject = JSON.parseObject(rangeContent);
		JSONArray jsonArray = (JSONArray) jsonObject.get(key);
		List<Object> objs = new ArrayList<>();
		for (Object object : jsonArray) {
			objs.add(object);
		}
		
		return objs;
	}

	private String calcTimeDif(Long start_time, Long end_time, Map<String, Object> map) {
		long time = System.currentTimeMillis();
		long ms = 0;
		if (start_time > time) {
			ms = start_time - time;
		} else {
			ms = end_time - time;
		}
		
		map.put("count_down_millis", ms);
		
        int ss = 1000;  
        int mi = ss * 60;  
        int hh = mi * 60;  
        int dd = hh * 24;  

        long day = ms / dd;  
        long hour = (ms - day * dd) / hh;  
        long minute = (ms - day * dd - hour * hh) / mi;  
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;  

        StringBuilder builder = new StringBuilder("");
        String strDay = day < 10 ? "0" + day : "" + day;  
        builder.append(strDay + "天");
        
        String strHour = hour < 10 ? "0" + hour : "" + hour; 
        builder.append(strHour + "小时");

        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        builder.append(strMinute + "分");

        String strSecond = second < 10 ? "0" + second : "" + second;
        builder.append(strSecond + "秒");
        
		return builder.toString();
	}

	/**
	 * 按照到期时间排序
	 */
	private void sortItems(JSONArray jsonArray) {
		Collections.sort(jsonArray, new Comparator<Object>() {  
            public int compare(Object arg0, Object arg1) {  
                Long endTime0 = Long.parseLong(((JSONObject)arg0).get("end_time").toString());
                Long endTime1 = Long.parseLong(((JSONObject)arg1).get("end_time").toString()); 
                if (endTime1 < endTime0) {  
                    return 1;  
                } else if (endTime1 == endTime0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        }); 
	}

	/**
	 * 过期代金券、已领取代金券不显示
	 * @param userId 
	 */ 
	private JSONArray filterItems(JSONArray jsonArray, long userId) {
 		JSONArray settings = new JSONArray();
		long time = System.currentTimeMillis();
		
		Set<Long> templateIds = new HashSet<>();
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject)object;
			Long coupon_template_id = jsonObject.getLong("coupon_template_id");
			templateIds.add(coupon_template_id);
		}
		
		Map<Long, CouponTemplate> cMap = couponTemplateService.searchMap(templateIds);
		
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject)object;
			Long startTime = jsonObject.getLong("start_time");
			Long endTime = jsonObject.getLong("end_time");
			Long coupon_template_id = jsonObject.getLong("coupon_template_id");
			Integer coupon_count = jsonObject.getInteger("coupon_count");

			/* 模板过期不显示 */
			CouponTemplate couponTemplate = cMap.get(coupon_template_id);
			if (couponTemplate.getValidityEndTime() < time) {
				continue;
			}
			
			/* 领取过期不显示 */
			if (endTime < time) {
				continue;
			}
			
			/* 没有可发放的代金券了(即代金券发行量=发放量) */
			if (couponTemplate.getPublishCount() <= couponTemplate.getGrantCount()) {
				continue;
			}
			
			if (startTime > time) {
				jsonObject.put("fetching", "NO");
			} else {
				jsonObject.put("fetching", "YES");
			}

			/* 去除已领取的代金券 */
			int fetchCount = fetchCouponCenterLogMapper.getCount(userId, coupon_template_id, startTime, endTime);
			
			if (fetchCount != 0) {
				int remain_count = coupon_count - fetchCount;
				if (remain_count <= 0) {
					continue;
				}
				jsonObject.put("coupon_count", remain_count);
			} 
		
			settings.add(object);
		}
		return settings;
	}

	@Transactional(rollbackFor = Exception.class)
	public void fetchCoupon(Long templateId, int count, long userId, CouponGetWay couponGetWay) {
		
//		double restMoney = orderCouponService.getRestMoney();
//		CouponTemplate couponTemplate = couponTemplateMapper.searchValidity(templateId, System.currentTimeMillis());
//		
//		if(couponTemplate == null) {
//			throw new ParameterErrorException("找不到对应的代金券模板" + templateId + ", 不存在或已模板过期！");
//		}
//		
//		double money = couponTemplate.getMoney();
//		double publishMoney = money * count;
//		if (restMoney >= publishMoney) 
//			orderCouponService.getCoupon(templateId, count, userId, couponGetWay, true);
		
		//从已有发行量中减去
		orderCouponService.getCouponFromPublished(templateId, count, userId, couponGetWay);
		
		FetchCouponCenterLog fetchCouponCenterLog = new FetchCouponCenterLog();
		fetchCouponCenterLog.setCouponTemplateId(templateId);
		fetchCouponCenterLog.setUserId(userId);
		fetchCouponCenterLog.setCreateTime(System.currentTimeMillis());
		
		fetchCouponCenterLogMapper.add(fetchCouponCenterLog);
	}
	
}
