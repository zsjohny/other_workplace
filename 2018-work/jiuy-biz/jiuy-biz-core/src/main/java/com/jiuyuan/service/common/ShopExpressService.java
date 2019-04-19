/**
 * 
 */
package com.jiuyuan.service.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.StoreExpressInfoMapper;
import com.jiuyuan.entity.logistics.LogisticsData;
import com.jiuyuan.entity.logistics.LogisticsRootResult;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.StoreExpressInfo;
import com.jiuyuan.service.common.MemcachedService;
import com.yujj.entity.ExpressSupplier;

/**
 * @author LWS u: help@yujiejie.com p: lws2015
 * 
 *         update at 2015/10/16 by LWS 1、 增加通过不同的物流匹配使用不同的物流API功能
 */
@Service
public class ShopExpressService {

	private static final Logger logger = LoggerFactory.getLogger(ShopExpressService.class);

	@Autowired
	private List<IShopExpressQuery> expressQueries;

	@Autowired
	private MemcachedService memcachedService;

	@Autowired
	private ShopExpressSupplierService expressSupplierService;
	
	@Autowired
	private StoreExpressInfoMapper storeExpressInfoMapper;

	public JSON queryExpressInfo(String expressSupplier, String expressNo) {

		String jsonExpress = "{ \"error_code\":-1,\"reason\":\"订单号不存在\",\"result\":{\"company\":\"" + expressSupplier
				+ "\",\"com\":\"" + expressSupplier + "\",\"no\":\"" + expressNo + "\"}}";
		JSONObject json1 = JSON.parseObject(jsonExpress);

		String groupKey = MemcachedKey.GROUP_KEY_EXPRESS;
		String key = expressSupplier + ":" + expressNo;
		ExpressSupplier expressSupplierTemp = expressSupplierService.getExpressSupplierByEngName(expressSupplier);
		if (expressSupplierTemp != null && expressSupplierTemp.getCnName() != null
				&& expressSupplierTemp.getCnName().length() > 0) {
			jsonExpress = "{ \"error_code\":-1,\"reason\":\"订单号不存在\",\"result\":{\"company\":\""
					+ expressSupplierTemp.getCnName() + "\",\"com\":\"" + expressSupplier + "\",\"no\":\"" + expressNo
					+ "\"}}";
			json1 = JSON.parseObject(jsonExpress);
		}
		for (IShopExpressQuery query : expressQueries) {
			if (query.support(expressSupplier)) {
				String text = query.queryExpressInfo(expressSupplier, expressNo);
				JSONObject json = JSON.parseObject(text.toString());
				if (json == null || json.get("result") == null
						|| ((JSONObject) json.get("result")).get("company") == null) {
					logger.info(
							"***************************************调用接口：存入缓存，暂无物流信息******************************************************");
//					memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_HOUR, json1);
					memcachedService.set(groupKey, key, 3600, json1);
					json = json1;
				}
				if (json != null && (JSONObject) json.get("result") != null
						&& ((JSONObject) json.get("result")).get("ischeck") != null
						&& ((JSONObject) json.get("result")).get("ischeck").equals(false)) {
					logger.info(
							"***************************************调用接口：存入缓存，未签收******************************************************");
//					memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_HOUR, json);
					memcachedService.set(groupKey, key, 3600, json);
				}
//				if (json != null && (JSONObject) json.get("result") != null
//						&& ((JSONObject) json.get("result")).get("ischeck") != null
//						&& ((JSONObject) json.get("result")).get("ischeck").equals(true)) {
//					logger.info(
//							"***************************************调用接口：清空缓存数据，已签收******************************************************");
//					Object obj1 = memcachedService.get(groupKey, key);
//					if (obj1 != null) {
//						memcachedService.remove(groupKey, key);
//					}
//				}
				return json;
			}
		}
		return json1;
	}

	
	/**
	 * 获取物流信息第一条
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getFirstExpressInfo(long orderNo) {
		Map<String, Object> map = new HashMap<>();
		Wrapper<StoreExpressInfo> wrapper = new EntityWrapper<StoreExpressInfo>().eq("OrderNo", orderNo);
		List<StoreExpressInfo> storeExpressInfoList = storeExpressInfoMapper.selectList(wrapper );
		if (storeExpressInfoList.size()<1) {
			map.put("context", "暂无物流信息");
			return map;
		}
		StoreExpressInfo storeExpressInfo = storeExpressInfoList.get(0);
		if (storeExpressInfo.getExpressInfo() != null) {
			String expressInfo = storeExpressInfo.getExpressInfo();
			JSONObject jsonObject = JSONObject.parseObject(JSON.parseObject(expressInfo).toJSONString());
			return parseJson1(jsonObject);
		} else {
			String groupKey = MemcachedKey.GROUP_KEY_EXPRESS;
			String expressOrderNo = storeExpressInfo.getExpressOrderNo();
			String expressSupplier = storeExpressInfo.getExpressSupplier();
			String key = expressSupplier + ":" + expressOrderNo;
			JSON obj = (JSON) memcachedService.get(groupKey, key);
			if (obj != null) {
				return parseJson1(obj);
			}
		}
		map.put("context", "暂无物流信息");
		return map;
	}

	
	/**
	 * 解析物流信息json
	 * 
	 * @param jsonObject
	 * @return
	 */
	public Map<String, Object> parseJson1(JSON jsonObject) {
		LogisticsRootResult logisticsRootResult = (LogisticsRootResult) JSONObject.toJavaObject(jsonObject,
				LogisticsRootResult.class);
		List<Map<String, Object>> list = new ArrayList<>();
		List<LogisticsData> logisticsDataList = logisticsRootResult.getResult().getData();
		Map<String, Object> data = new HashMap<>();
		if (logisticsDataList != null) {
			DateFormat df = new SimpleDateFormat("yyyy年MM月dd日 aHH:mm:ss");
			for (LogisticsData logisticsData : logisticsDataList) {
				Map<String, Object> map = new HashMap<>();
				String context = logisticsData.getContext();
				Date time = logisticsData.getTime();
				String date = df.format(time);
				map.put("context", context);
				map.put("date", date);
				list.add(map);
			}
		}
		data.put("list", list);
		data.put("commpany", logisticsRootResult.getResult().getCompany());
		data.put("orderNo", logisticsRootResult.getResult().getNo());
		return data;
	}
}
