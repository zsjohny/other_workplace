package com.jiuy.supplier.modular.myOrder.controller;

import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.ShopExpressSupplierMapper;
import com.jiuyuan.dao.mapper.supplier.StoreExpressInfoMapper;
import com.jiuyuan.entity.logistics.LogisticsData;
import com.jiuyuan.entity.logistics.LogisticsRootResult;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.StoreExpressInfo;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.service.common.ShopExpressService;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * <p>
 * 会员订单表 前端控制器
 * </p>
 *
 * @author 赵兴林
 * @since 2017-09-05
 */
@Controller
@RequestMapping("/express")
public class ShopExpressController {
	private static final Log logger = LogFactory.get(ShopExpressController.class);
	@Autowired
	private ShopExpressService expressService;

	@Autowired
	private RefundOrderMapper refundOrderMapper;
	@Autowired
	private MemcachedService memcachedService;
	
	@Autowired
	private StoreExpressInfoMapper storeExpressInfoMapper;

	@RequestMapping("/expressInfo")
	@ResponseBody
	public JsonResponse getExpressInfo(@RequestParam(value = "orderNo", required = true) long orderNo) {//订单编号，不是快递单号
		JsonResponse jsonResponse = new JsonResponse();
		String groupKey = MemcachedKey.GROUP_KEY_EXPRESS;
		// JSON expressData = expressService.queryExpressInfo(name, order);
		try {
			Wrapper<StoreExpressInfo> wrapper = new EntityWrapper<StoreExpressInfo>().eq("OrderNo", orderNo);
			List<StoreExpressInfo> storeExpressInfoList = storeExpressInfoMapper.selectList(wrapper );
			if (storeExpressInfoList.size()<1) {
				return jsonResponse.setError("不订单信息存在！");
			}
			StoreExpressInfo storeExpressInfo = storeExpressInfoList.get(0);
			String expressSupplier = storeExpressInfo.getExpressSupplier();
			String expressNo = storeExpressInfo.getExpressOrderNo();
			if (expressSupplier == null) {
				return jsonResponse.setError("订单信息不存在！");
			}
			if (expressNo == null) {
				return jsonResponse.setError("订单信息不存在！");
			}
			if (storeExpressInfo.getExpressInfo() != null) {
				//查数据库
				logger.info("********************************未调用接口：查询数据库并取到数据********************************");
				return jsonResponse.setSuccessful().setData(expressService.parseJson1(JSON.parseObject(storeExpressInfo.getExpressInfo())));
			} else {
				// 查询缓存
				String key = expressSupplier + ":" + expressNo;
				Object obj = memcachedService.get(groupKey, key);
				if (obj != null) {
					logger.info("********************************未调用接口：查询缓存并取到数据********************************");
					return jsonResponse.setSuccessful().setData(expressService.parseJson1((JSON) obj));
				} else {
					JSONObject jsonObject = new JSONObject();
					//调用接口
					logger.info("***************************************调用接口******************************************************");
					jsonObject = (JSONObject) expressService.queryExpressInfo(expressSupplier, expressNo);
					//判断接口返回结果是否被签收（已被签收则清空缓存，存入数据库）
					if (jsonObject.get("result") != null
							&& ((JSONObject) jsonObject.get("result")).get("ischeck") != null
							&& ((JSONObject) jsonObject.get("result")).get("ischeck").equals(true)) {
						Object obj1 = memcachedService.get(groupKey, key);
						if (obj1 != null) {
						logger.info("***************************************清空缓存数据，已签收******************************************************");
							//清空缓存
							memcachedService.remove(groupKey, key);
						}
						//存入数据库
						StoreExpressInfo newStoreExpressInfo  = new  StoreExpressInfo();
						newStoreExpressInfo.setId(storeExpressInfo.getId());
						newStoreExpressInfo.setExpressInfo(jsonObject.toJSONString());
						newStoreExpressInfo.setUpdateTime(System.currentTimeMillis());
						storeExpressInfoMapper.updateById(newStoreExpressInfo);
						logger.info("***************************************已签收，存入数据库******************************************************");
					}
					return jsonResponse.setSuccessful().setData(expressService.parseJson1(jsonObject));
				}
			}
		} catch (Exception e) {
			return jsonResponse.setError("供应商查询物流信息e:" + e.getMessage());
		}
	}

	
	@RequestMapping("/getRefundOrderExpressInfo")
	@ResponseBody
	public JsonResponse getRefundOrderExpressInfo(@RequestParam(value = "refundOrderId", required = true) long refundOrderId) {//订单编号，不是快递单号
		JsonResponse jsonResponse = new JsonResponse();
		String groupKey = MemcachedKey.GROUP_KEY_EXPRESS;
		// JSON expressData = expressService.queryExpressInfo(name, order);
		try {
			RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
			if (refundOrder == null) {
				return jsonResponse.setError("订单信息不存在！");
			}
			String expressSupplier = refundOrder.getCustomerExpressCompany();
			String expressNo = refundOrder.getCustomerExpressNo();
			if (expressSupplier == null) {
				return jsonResponse.setError("订单信息不存在！");
			}
			if (expressNo == null) {
				return jsonResponse.setError("订单信息不存在！");
			}
			if (refundOrder.getExpressInfo() != null) {
				//查数据库
				logger.info("********************************未调用接口：查询数据库并取到数据********************************");
				return jsonResponse.setSuccessful().setData(expressService.parseJson1(JSON.parseObject(refundOrder.getExpressInfo())));
			} else {
				// 查询缓存
				String key = expressSupplier + ":" + expressNo;
				Object obj = memcachedService.get(groupKey, key);
				if (obj != null) {
					logger.info("********************************未调用接口：查询缓存并取到数据********************************");
					return jsonResponse.setSuccessful().setData(expressService.parseJson1((JSON) obj));
				} else {
					JSONObject jsonObject = new JSONObject();
					//调用接口
					logger.info("***************************************调用接口******************************************************");
					jsonObject = (JSONObject) expressService.queryExpressInfo(expressSupplier, expressNo);
					//判断接口返回结果是否被签收（已被签收则清空缓存，存入数据库）
					if (jsonObject.get("result") != null
							&& ((JSONObject) jsonObject.get("result")).get("ischeck") != null
							&& ((JSONObject) jsonObject.get("result")).get("ischeck").equals(true)) {
						Object obj1 = memcachedService.get(groupKey, key);
						if (obj1 != null) {
						logger.info("***************************************清空缓存数据，已签收******************************************************");
							//清空缓存
							memcachedService.remove(groupKey, key);
						}
						//存入数据库
						RefundOrder order = new RefundOrder();
						order.setId(refundOrder.getId());
						order.setExpressInfo(jsonObject.toJSONString());
						refundOrderMapper.updateById(order);
						logger.info("***************************************已签收，存入数据库******************************************************");
					}
					return jsonResponse.setSuccessful().setData(expressService.parseJson1(jsonObject));
				}
			}
		} catch (Exception e) {
			return jsonResponse.setError("供应商查询物流信息e:" + e.getMessage());
		}
	}

	

}
