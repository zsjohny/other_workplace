package com.jiuy.wxa.api.controller.wxa;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.StoreExpressInfoMapper;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.newentity.StoreExpressInfo;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.service.common.ShopExpressService;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.ShopMemberOrderMapper;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * <p>
 * 小程序会员订单表 前端控制器
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
	private MemcachedService memcachedService;
	
	@Autowired
	private ShopMemberOrderMapper shopMemberOrderMapper;

	@RequestMapping("/healths")
	@ResponseBody
	public String healths() {
		return "OK";
	}
	@RequestMapping("/expressInfo")
	@ResponseBody
	public JsonResponse getExpressInfo(@RequestParam(value = "shopMemberOrderId", required = true) long shopMemberOrderId) {//订单编号，不是快递单号
		JsonResponse jsonResponse = new JsonResponse();
		String groupKey = MemcachedKey.GROUP_KEY_EXPRESS;
		// JSON expressData = expressService.queryExpressInfo(name, order);
		try {
			Wrapper<ShopMemberOrder> wrapper = new EntityWrapper<ShopMemberOrder>().eq("id", shopMemberOrderId);
			List<ShopMemberOrder> shopMemberOrderList = shopMemberOrderMapper.selectList(wrapper );
			if (shopMemberOrderList.size()<1) {
				return jsonResponse.setError("订单信息不存在！");
			}
			ShopMemberOrder shopMemberOrderExpressInfo = shopMemberOrderList.get(0);
			String expressSupplier = shopMemberOrderExpressInfo.getExpressSupplier();
			String expressNo = shopMemberOrderExpressInfo.getExpressNo();
			if (expressSupplier == null) {
				return jsonResponse.setError("订单信息不存在！");
			}
			if (expressNo == null) {
				return jsonResponse.setError("订单信息不存在！");
			}
			if (shopMemberOrderExpressInfo.getExpressInfo() != null) {
				//查数据库
				logger.info("********************************未调用接口：查询数据库并取到数据********************************");
				return jsonResponse.setSuccessful().setData(expressService.parseJson1(JSON.parseObject(shopMemberOrderExpressInfo.getExpressInfo())));
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
						ShopMemberOrder newShopMemberOrder  = new  ShopMemberOrder();
						newShopMemberOrder.setId(shopMemberOrderExpressInfo.getId());
						newShopMemberOrder.setExpressInfo(jsonObject.toJSONString());
						newShopMemberOrder.setUpdateTime(System.currentTimeMillis());
						shopMemberOrderMapper.updateById(newShopMemberOrder);
						logger.info("***************************************已签收，存入数据库******************************************************");
					}
					return jsonResponse.setSuccessful().setData(expressService.parseJson1(jsonObject));
				}
			}
		} catch (Exception e) {
			return jsonResponse.setError("小程序查询物流信息e:" + e.getMessage());
		}
	}

	
}
