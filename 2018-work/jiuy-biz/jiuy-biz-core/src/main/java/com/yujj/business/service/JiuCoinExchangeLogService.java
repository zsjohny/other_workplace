package com.yujj.business.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.JiuCoinExchangeLog;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.DateUtil;
import com.yujj.dao.mapper.JiuCoinExchangeLogMapper;
import com.yujj.dao.mapper.OrderCouponMapper;
import com.yujj.dao.mapper.OrderItemMapper;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.order.CouponTemplate;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductSKU;

/**
 * @author jeff.zhan
 * @version 2016年12月16日 下午5:08:05
 * 
 */

@Service
public class JiuCoinExchangeLogService {

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private MemcachedService memcachedService;
	
	@Autowired
	private OrderCouponMapper orderCouponMapper;

	@Autowired
	private JiuCoinExchangeLogMapper jiuCoinExchangeLogMapper;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private CouponTemplateService couponTemplateService;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> pointExchangeList(PageQuery pageQuery, UserDetail userDetail, int type) throws IllegalAccessException, InvocationTargetException {
		Map<String, Object> data = new HashMap<String, Object>();
		// getMemCache
		String groupKey = MemcachedKey.GROUP_KEY_COIN_CHANGE_LIST;
		String key = userDetail.getUserId() + "exchange" + pageQuery.getPage() + type;
		Map<String, Object> obj = (Map<String, Object>) memcachedService.get(groupKey, key);
		if (obj != null) {
			data.put("results", obj.get("results"));
			data.put("pageQuery", obj.get("pageQuery"));
		} else {
			// 获取用户兑换记录
			int totalCount = this.searchCount(userDetail, type);
			PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
			data.put("pageQuery", pageQueryResult);
			
			List<JiuCoinExchangeLog> jiuCoinExchangeLogs = this.search(userDetail, pageQuery, type);
			
			List<Map<String, Object>> results = new ArrayList<>();
			for(JiuCoinExchangeLog jiuCoinExchangeLog : jiuCoinExchangeLogs) {
				Map<String, Object> result = new HashMap<>();
				result.put("type", jiuCoinExchangeLog.getType());
				result.put("jiu_coin", jiuCoinExchangeLog.getJiuCoin());
				result.put("count", jiuCoinExchangeLog.getCount());
				result.put("create_time_str", DateUtil.parseLongTime2Str(jiuCoinExchangeLog.getCreateTime()));
				result.put("create_time", jiuCoinExchangeLog.getCreateTime());
				
				if(jiuCoinExchangeLog.getType() == 2){
					ProductSKU productSKU = productSKUService.getProductSKU(jiuCoinExchangeLog.getRelatedId());
					Product product = productService.getProductById(productSKU.getProductId());
					
					result.put("product_name", product.getName());
					result.put("product_image", product.getImage());
					    
				} else if(jiuCoinExchangeLog.getType() == 1){
					CouponTemplate couponTemplate = couponTemplateService.search(jiuCoinExchangeLog.getRelatedId());
					result.put("coupon_template_name", couponTemplate == null ? "" : couponTemplate.getName());
					result.put("coupon_image", jiuCoinExchangeLog.getContent());
				}
				results.add(result);
			}
			
			data.put("results", results);
			
			memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE, data);
		}
		return data;
	}

	private List<JiuCoinExchangeLog> search(UserDetail userDetail, PageQuery pageQuery, int type) {
		return jiuCoinExchangeLogMapper.search(userDetail.getUserId(), pageQuery, type);
	}

	public int searchCount(UserDetail userDetail, int type) {
		return jiuCoinExchangeLogMapper.searchCount(userDetail.getUserId(), type);
	}

	public int getCount(Long userId, Integer type, Long relatedId, Long startTime) {
		return jiuCoinExchangeLogMapper.getCount(userId, type, relatedId, startTime, null) == null ? 0 : jiuCoinExchangeLogMapper.getCount(userId, type, relatedId, startTime, null);
	}

}
