package com.store.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.order.ShopMemberOrderLog;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.ShopMemberOrderItemMapper;
import com.store.dao.mapper.ShopMemberOrderLogMapper;
import com.store.dao.mapper.ShopMemberOrderMapper;
import com.store.dao.mapper.coupon.ShopMemberCouponMapper;
import com.store.entity.coupon.ShopMemberCoupon;
import com.store.service.store.ServiceAdviceFacade;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import javax.annotation.Resource;

/**
 * <p>
 * 会员订单表 服务实现类
 * </p>
 *
 * @since 2017-09-07
 */
@Service
public class NJShopMemberOrderService {
	private static final Log logger = LogFactory.get("NJShopMemberOrderService");
    
	@Autowired
	private ShopMemberOrderMapper shopMemberOrderMapper;
	@Autowired
	private ShopMemberOrderItemMapper shopMemberOrderItemMapper;
	
	@Autowired
	private ShopMemberOrderLogMapper shopMemberOrderLogMapper;

    @Autowired
    ServiceAdviceFacade serviceAdviceFacade;
    
    @Autowired
	private MemcachedService memcachedService;
    
    @Autowired
	private ShopMemberCouponMapper shopMemberCouponMapper;
    
	/**
	 * 关闭订单
	 * @param storeId 
	 * @param storeId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public void stopOrderById(long orderId, long storeId) {
		//update orderStatus,cancelReason,cancelReasonType,orderStopTime
		long time = System.currentTimeMillis();
		//1.获取该订单
		ShopMemberOrder shopMemberOrder = shopMemberOrderMapper.selectById(orderId);
		int oldStatus = shopMemberOrder.getOrderStatus();
		//2.更改该订单信息 orderStatus改为3订单关闭（已关闭，cancelReason改为商家取消 ，cancelReasonType改为2商家取消
		//orderStopTime 交易关闭时间
		shopMemberOrder.setOrderStatus(new Integer(3)); 
		shopMemberOrder.setCancelReason("抱歉，您购买的商品目前缺货！");
		shopMemberOrder.setCancelReasonType(new Integer(2)); 
		shopMemberOrder.setOrderStopTime(time);
		shopMemberOrder.setUpdateTime(time);
		//3.开始更新信息
		int i = shopMemberOrderMapper.updateById(shopMemberOrder);
		if(i != 1){
			logger.info("订单关闭失败，请排除问题！！！");
		}else{
			//添加log
			addShopMemberOrderLog(storeId,oldStatus,shopMemberOrder);
			//如果有优惠券,将优惠券设置为未使用
			long couponId = shopMemberOrder.getCouponId();
			if(couponId>0){
				ShopMemberCoupon shopMemberCoupon = new ShopMemberCoupon();
				shopMemberCoupon.setId(couponId);
				shopMemberCoupon.setAdminId(0L);
				shopMemberCoupon.setCheckTime(0L);
				shopMemberCoupon.setCheckMoney(0D);
				shopMemberCoupon.setStatus(ShopMemberCoupon.status_normal);
				shopMemberCoupon.setUpdateTime(time);
				int couponRecord = shopMemberCouponMapper.updateById(shopMemberCoupon);
				if(couponRecord!=1){
					logger.error("app取消订单:将优惠券设置为未使用couponId:"+couponId);
					throw new RuntimeException("app取消订单:将优惠券设置为未使用couponId:"+couponId);
				}
			}
			//发送模板通知给微信会员
			serviceAdviceFacade.orderCancelAdvice(orderId);
		}
	}
/*	*//**
	 * 确认二维码获取参数
	 * @param orderId
	 * @param storeId 
	 * @param userDetail
	 * @return
	 *//*
	@Transactional(rollbackFor = Exception.class)
	public JsonResponse confirmDeliveryById(String data, long storeId) {
		logger.info("确认二维码获取参数data:"+data+";storeId"+storeId);
		JsonResponse jsonResponse = new JsonResponse();
		try {
//			data = URLEncoder.encode(data,"UTF-8");
			Map<String,Object> dataMap = new HashMap<String,Object>();
			String[] paramArr = data.split("&");
			for (String param : paramArr) {
				String[] split = param.split("=");
				dataMap.put(split[0], split[1]);
			}
			dataMap.put("storeId", storeId);
			
			//核销二维码
			Map<String, String> result = getQRcode(dataMap);
			return jsonResponse.setSuccessful().setData(result);
		} catch (Exception e) {
    		logger.error("确认二维码获取参数"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}	*/



	/**
	 * 确认二维码获取参数
	 * @param storeId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> confirmDeliveryById(String data, long storeId) {
		logger.info("确认二维码获取参数data:"+data+";storeId"+storeId);
		Map<String,Object> dataMap = new HashMap<> ();
		String[] paramArr = data.split("&");
		for (String param : paramArr) {
			String[] split = param.split("=");
			dataMap.put(split[0], split[1]);
		}
		dataMap.put("storeId", storeId);

		//核销二维码
		return getQRcode (dataMap);
	}
	
	/**
	 * 核销二维码
	 * @param dataMap
	 */
	private Map<String, String> getQRcode(Map<String, Object> dataMap) {
//		logger.info("核销二维码dataMap:"+dataMap);
		String type = (String) dataMap.get("type");
		long storeId = (long) dataMap.get("storeId");
		long id = 0;
		if("dingdan".equals(type)){
			id = Long.parseLong((String)dataMap.get("id"));
		}
		Map<String,String> result = new HashMap<String,String>();
//		logger.info("核销二维码type："+type+",id:"+id);
//		logger.info("核销二维码key::::::::::::::::::："+MemcachedKey.GROUP_KEY_QRCODE_EXPIRATION_TIME);
		Object obj = memcachedService.getCommon(MemcachedKey.GROUP_KEY_QRCODE_EXPIRATION_TIME, 
				type+"="+id+"");
//		logger.info("核销二维码obj："+obj);

		logger.info ("确认提货===>从缓存中获取 groupKey[{}].key[{}].order[{}].type[{}]",MemcachedKey.GROUP_KEY_QRCODE_EXPIRATION_TIME, type+"="+id, obj, type);
		if(obj==null){//缓存时间已过
			result.put("result", "核销失败");
			result.put("text", "二维码已失效");
		}else{
			if("dingdan".equals(type)){
				result = updMemberOrder((ShopMemberOrder)obj,storeId);
			}else{//其他异常
				result.put("result", "核销失败");
				result.put("text", "");
			}
		}
		return result;
	}
	
	/**
	 * 核销提货二维码
	 * @param shopMemberOrder
	 * @param storeId
	 * @return
	 */
	private Map<String, String> updMemberOrder(ShopMemberOrder shopMemberOrder,long storeId) {
		logger.info("核销提货二维码shopMemberOrder:"+shopMemberOrder+";storeId:"+storeId);
		Map<String,String> result = new HashMap<String,String>();
		int oldStatus = shopMemberOrder.getOrderStatus();
		if(oldStatus==ShopMemberOrder.order_status_order_closed){//该订单已经关闭
			result.put("result", "核销失败");
			result.put("text", "该订单已经关闭");
		}else if(oldStatus==ShopMemberOrder.order_status_refund){//该订单已在退款中
			result.put("result", "核销失败");
			result.put("text", "该订单已在退款中");
		}else if(oldStatus==ShopMemberOrder.order_status_order_fulfillment){//该订单已经完成过了
			result.put("result", "核销失败");
			result.put("text", "该订单已经完成过了");
		}else if(shopMemberOrder.getStoreId()!=storeId){//该优惠券不是本店的
			throw new RuntimeException("该优惠券不是本店的");
		}else{ 
			//update orderStatus,takeDeliveryTime,order_finish_time
//			long time = System.currentTimeMillis();
//			shopMemberOrder.setOrderStatus(ShopMemberOrder.order_status_order_fulfillment);
//			shopMemberOrder.setTakeDeliveryTime(time);
//			shopMemberOrder.setOrderFinishTime(time);
//			int record = shopMemberOrderMapper.updateById(shopMemberOrder);
//			if(record != 1){
//				logger.info("订单确认提货失败，请排除问题！！！orderId:"+shopMemberOrder.getId());
//				throw new RuntimeException("订单确认提货失败，请排除问题！！！orderId:"+shopMemberOrder.getId());
//			}else{
//				//添加log
//				addShopMemberOrderLog(storeId,oldStatus,shopMemberOrder);
//			}
			result.put("result", "核销成功");
			result.put("text", "");
			result.put("orderId", shopMemberOrder.getId ()+"");
		}
		return result;
	}
		
	/**
	 * 获取商家订单的订单状态数量
	 * @param storeId
	 * @return
	 */
	public Map<String, Integer> getMemberOrderStatusCount(long storeId) {
		Map<String,Integer> memberOrderCount = new HashMap<String,Integer>();
		//1.获取查询结果
		//代付款
		Wrapper<ShopMemberOrder> daiFuKuanCountWrapper = new EntityWrapper<ShopMemberOrder>()
				.eq("order_status", ShopMemberOrder.order_status_pending_payment)
				.eq("store_id", storeId);
		int daiFuKuanCount = shopMemberOrderMapper.selectCount(daiFuKuanCountWrapper);
		//待提货
		Wrapper<ShopMemberOrder> daiTiHuoCountWrapper = new EntityWrapper<ShopMemberOrder>()
				.eq("order_status", ShopMemberOrder.order_status_paid)
				.eq("store_id", storeId);
		int daiTiHuoCount = shopMemberOrderMapper.selectCount(daiTiHuoCountWrapper);
		//待发货数量
		Wrapper<ShopMemberOrder> daiFaHuoCountWrapper = new EntityWrapper<ShopMemberOrder>()
				.eq("order_status", ShopMemberOrder.order_status_pending_delivery)
				.eq("store_id", storeId);
		int daiFaHuoCount = shopMemberOrderMapper.selectCount(daiFaHuoCountWrapper);
		//已完成
		Wrapper<ShopMemberOrder> yiChengGongCountWrapper = new EntityWrapper<ShopMemberOrder>()
				.eq("order_status", ShopMemberOrder.order_status_order_fulfillment)
				.eq("store_id", storeId);
		int yiChengGongCount = shopMemberOrderMapper.selectCount(yiChengGongCountWrapper);
		
		//加入集合
		memberOrderCount.put("daiFuKuanCount", daiFuKuanCount);//待付款
		memberOrderCount.put("daiTiHuoCount", daiTiHuoCount);//待提货
		memberOrderCount.put("daiFaHuoCount", daiFaHuoCount);//待发货
		memberOrderCount.put("yiChengGongCount", yiChengGongCount);//已完成
		return memberOrderCount;
	}
	
	/**
	 * 添加log
	 * @param storeId
	 * @param oldStatus
	 * @param shopMemberOrder
	 */
	private void addShopMemberOrderLog(long storeId, int oldStatus, ShopMemberOrder shopMemberOrder){
		ShopMemberOrderLog shopMemberOrderLog = new ShopMemberOrderLog();
		shopMemberOrderLog.setOldStatus(shopMemberOrder.getOrderStatus());
		shopMemberOrderLog.setStoreId(storeId);
		shopMemberOrderLog.setNewStatus(shopMemberOrder.getOrderStatus());
		shopMemberOrderLog.setOrderId(shopMemberOrder.getId());
		shopMemberOrderLog.setCreateTime(shopMemberOrder.getUpdateTime());
		shopMemberOrderLogMapper.insert(shopMemberOrderLog);
	}
}