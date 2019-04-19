/**
 * 
 */
package com.jiuy.core.service.task;

import java.util.Date;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jiuyuan.dao.mapper.CommonRefMapper;
import com.jiuyuan.service.common.area.BizCacheKey;
import com.jiuyuan.service.common.area.IBizCacheService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
//import com.jiuy.core.business.facade.OrderFacade;
import com.jiuy.core.business.facade.OrderNewFacade;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.coupon.CouponUseLog;
//import com.jiuy.core.meta.order.Order;
import com.jiuy.core.meta.coupon.StoreCouponUseLog;
import com.jiuy.core.meta.order.OrderItem;
import com.jiuy.core.meta.order.OrderNew;
//import com.jiuy.core.service.OrderService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.UserCoinService;
import com.jiuy.core.service.coupon.CouponService;
import com.jiuy.core.service.coupon.CouponUseLogService;
import com.jiuy.core.service.coupon.StoreCouponService;
import com.jiuy.core.service.coupon.StoreCouponUseLogService;
import com.jiuy.core.service.order.OrderItemService;
import com.jiuy.core.service.order.OrderOldService;
import com.jiuy.core.service.storeorder.StoreOrderItemService;
import com.jiuy.core.service.storeorder.StoreOrderService;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.coupon.CouponUseStatus;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductMapper;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductSkuMapper;
import com.jiuyuan.dao.mapper.supplier.SecondBuyActivityMapper;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.RestrictionActivityProductSku;
import com.jiuyuan.entity.newentity.SecondBuyActivity;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.order.ShopMemberOrderLog;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.entity.storeorder.StoreOrderItem;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.ParamSignUtils;
import com.store.entity.coupon.ShopMemberCoupon;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;

/**
 * #########定时任务##########
 * task.order.close
 * #########################
 * 待付款提醒通知定时任务 订单超时之前1小时进行发送通知
 * 门店订单未付款超时取消关闭
 * 小程序订单未付款超时取消
 * 取消已经结束的限购活动
 * 发送秒杀订单1小时未支付订单付款通知
 * 取消团购活动
 * 取消秒杀活动
 */
@Component
public class OrderDisposingJob {

	private static final Logger logger = Logger.getLogger(OrderDisposingJob.class);
    static final String version = "1.0.0"; 
	// @Autowired
	// private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserCoinService userCoinService;

	// @Autowired
	// private OrderFacade orderFacade;

	@Autowired
	private OrderNewFacade orderNewFacade;

	@Autowired
	private OrderOldService orderNewService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private StoreCouponService storeCouponService;

	@Autowired
	private CouponUseLogService couponUseLogService;

	@Autowired
	private StoreCouponUseLogService storeCouponUseLogService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private StoreOrderService storeOrderService;

	@Autowired
	private StoreOrderItemService storeOrderItemService;

	@Autowired
	private ProductSKUMapper productSKUMapper;

	@Autowired
	private ShopMemberOrderService shopMemberOrderService;
	@Autowired
	private MemcachedService memcachedService;
	@Autowired
	private SecondBuyActivityMapper secondBuyActivityMapper;
	
	@Autowired
	private RestrictionActivityProductSkuMapper restrictionActivityProductSkuMapper;
	
	@Autowired
	private IBizCacheService bizCacheService;

	@Autowired
	private RestrictionActivityProductMapper restrictionActivityProductMapper;

	@Autowired
	private CommonRefMapper commonRefMapper;


	public void execute() {
		//待付款提醒通知定时任务 订单超时之前1小时进行发送通知
		sendMessage();
		long currentTime = System.currentTimeMillis();
		//门店订单未付款超时取消关闭
		disposeStoreOrders(currentTime);
		disposeYjjOrders(currentTime);
		//小程序订单未付款超时取消
		stopMemberOrderOvertime();
		//取消已经结束的限购活动
		stopRestrictionActivityProduct(currentTime);
		//发送秒杀订单1小时未支付订单付款通知
		sendSecondMessage();
		//取消团购活动
		stopMemberTeamOrderOvertime();
		//取消秒杀活动
		stopMemberSecondOrderOvertime();
	}
	/**
	 * 取消已经结束的限购活动
	 * @param currentTime
	 */
	@Transactional(rollbackFor = Exception.class)
	public void stopRestrictionActivityProduct(long currentTime) {
		Wrapper<RestrictionActivityProduct> wrapper = 
				new EntityWrapper<RestrictionActivityProduct>().le("activity_end_time", currentTime);
//		List<RestrictionActivityProduct> selectList = restrictionActivityProductMapper.selectList(wrapper);
		RestrictionActivityProduct restrictionActivityProduct = new RestrictionActivityProduct();
		restrictionActivityProduct.setProductStatus(RestrictionActivityProduct.has_been_removed);
		restrictionActivityProduct.setUpdateTime(currentTime);
		restrictionActivityProductMapper.update(restrictionActivityProduct, wrapper);
	}
	/**
	 * 取消秒杀超过2时未支付订单
	 */
	@Transactional(rollbackFor = Exception.class)
	public void stopMemberSecondOrderOvertime() {
		logger.error("取消超过两个小时未付款的秒杀订单定时任务开始！！！！");
		List<ShopMemberOrder> shopMemberOrderList = shopMemberOrderService.getMemberSecondOvertimeOrder(System.currentTimeMillis());
		for (ShopMemberOrder shopMemberOrder : shopMemberOrderList) {
			long nowTime = System.currentTimeMillis();
			shopMemberOrder.setCancelReasonType(3);
			shopMemberOrder.setCancelReason("超过2小时未支付，订单已被系统自动取消");
			shopMemberOrder.setOrderStopTime(nowTime);
			shopMemberOrder.setUpdateTime(nowTime);
			logger.error("超过两个小时未付款的秒杀订单orderId:" + shopMemberOrder.getId());
			int record = shopMemberOrderService.storeOrderByOrderId(shopMemberOrder);
			if (record != 1) {
				logger.error("超过两个小时未付款的秒杀订单orderId:" + shopMemberOrder.getId());
				throw new RuntimeException("超过两个小时未付款的秒杀订单orderId:" + shopMemberOrder.getId());
			}else{
				//判断活动是否结束，如果没结束，回库存
				SecondBuyActivity secondBuyActivity = secondBuyActivityMapper.selectById(shopMemberOrder.getSecondId());
				if (secondBuyActivity == null) {
					throw new RuntimeException("秒杀活动不存在！");
				}
				//活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
				int haveActivityStatusInt = secondBuyActivity.haveActivityStatusInt();
				if (haveActivityStatusInt == 2) {
//					SecondBuyActivity newSecondBuyActivity = new SecondBuyActivity();
//					newSecondBuyActivity.setId(secondBuyActivity.getId());
//					//参与人数-1
//					newSecondBuyActivity.setActivityMemberCount(secondBuyActivity.getActivityMemberCount()-1);
//					logger.info("自动取消超2小时未付款订单---参与人数减1");
//					secondBuyActivityMapper.updateById(newSecondBuyActivity);
					secondBuyActivityMapper.updateJoinUser (secondBuyActivity.getId (), -1, 0-shopMemberOrder.getCount ());
					//缓存中相关信息修改
					/*String groupKey = MemcachedKey.GROUP_KEY_activitySecondBuy;
					String key = "_secondBuyActivityId_"+String.valueOf(secondBuyActivity.getId());
					Object obj = memcachedService.getCommon(groupKey, key);
					logger.info("自动---取消秒杀订单取查询缓存数据groupKey+key:"+groupKey+key);
					if (obj != null) {
						logger.info("自动----取消秒杀订单前缓存中剩余活动商品数量obj:"+obj);
						int count = 1;
						String memcachedcountStr = (String)obj;
						int memcachedcount = Integer.valueOf(memcachedcountStr.trim());
						if(memcachedcount == 0){
							count = 2;
						}
						logger.info("自动----取消秒杀订单前缓存中剩余活动商品数量count"+count+",memcachedcount:"+memcachedcount);
						memcachedService.incrCommon(groupKey, key, count);
						logger.info("自动----取消秒杀订单缓存加1成功！");
						logger.info("自动----取消秒杀订单后缓存中剩余活动商品数量obj:"+memcachedService.getCommon(groupKey, key));
					}else{
						logger.info("自动----取消秒杀订单失败obj:"+obj);
					}*/
					String key = BizCacheKey.SHOP_SECOND_ACTIVITY_INVENTORY + secondBuyActivity.getId ();
					String inventory = bizCacheService.get (key);
					if (StringUtils.isBlank (key)) {
						logger.info("自动----取消秒杀订单失败 inventory:"+inventory);
					}
					else {
						Long incr = bizCacheService.incr (key, shopMemberOrder.getCount ());
						logger.info("自动取消秒杀订单成功 取消前库存["+inventory+"].取消后库存["+incr+"]");
					}
				}
			} 
		}
		
	}
	/**
	 * 发送秒杀订单1小时未支付订单付款通知
	 */
	@Transactional(rollbackFor = Exception.class)
	public void sendSecondMessage() {
		logger.error("发送秒杀订单1小时未支付订单付款通知定时任务开始！！！！");
		// 1、获取秒杀订单大于一小时未支付会员订单
		List<ShopMemberOrder> shopMemberOrderList = shopMemberOrderService.getWaitPaySecondOrderList();
		for (ShopMemberOrder shopMemberOrder : shopMemberOrderList) {
			long orderId = shopMemberOrder.getId();
			String wxaServiceUrl = AdminConstants.WXA_SERVER_URL;
			String apiUrl = "/miniapp/advice/waitPayAdvice.json";
			String url = wxaServiceUrl + apiUrl;
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderId", String.valueOf(orderId));
			logger.info("秒杀订单大于1小时未支付发送模板通知url:" + url + ",map:" + map);
			Response<String> resp = Requests.get(url).params(map).text();
			String ret = resp.getBody();
			logger.info("秒杀订单大于1小时未支付模板通知发送结果ret:" + ret);	
			if (StringUtils.isNotEmpty(ret)) {
				JSONObject retJSON = JSON.parseObject(ret);// json字符串转换成jsonobject对象
				boolean successful = retJSON.getBoolean("successful");
				if (successful) {
					logger.info("秒杀订单大于1小时未支付更改待支付发送标记，successful" + successful);
					// 更改发送待支付提醒标记为已发送
					shopMemberOrderService.updateOrderSendMessage(orderId);
				} else {
					logger.info("秒杀订单大于1小时未支付发送通知返回结果为失败，请排查问题，successful" + successful);
				}
			} else {
				logger.info("秒杀订单大于1小时未支付发送待付款通知失败请排查问题，ret:" + ret + ",url：" + url + ",map:" + map);
			}
		}
//		logger.info("完成待付款提醒通知定时任务");
	}
	/**
	 * 取消团购活动结束时未付款的团购订单
	 */
	@Transactional(rollbackFor = Exception.class)
	public void stopMemberTeamOrderOvertime() {
		logger.error("取消团购活动结束时未付款的团购订单定时任务开始！！！！");
		List<ShopMemberOrder> shopMemberOrderList = shopMemberOrderService.stopMemberTeamOrderOvertime(System.currentTimeMillis());
		for (ShopMemberOrder shopMemberOrder : shopMemberOrderList) {
			long nowTime = System.currentTimeMillis();
			shopMemberOrder.setCancelReasonType(3);
			shopMemberOrder.setCancelReason("活动已结束，订单已被系统自动取消");
			shopMemberOrder.setOrderStopTime(nowTime);
			shopMemberOrder.setUpdateTime(nowTime);
			int record = shopMemberOrderService.storeOrderByOrderId(shopMemberOrder);
			if (record != 1) {
				logger.error("团购活动结束时未付款-小程序取消团购订单orderId:" + shopMemberOrder.getId());
				throw new RuntimeException("团购活动结束时未付款-小程序取消团购订单orderId:" + shopMemberOrder.getId());
				}
			}
		}
	

	/**
	 * 待付款提醒通知定时任务 订单超时之前1小时进行发送通知
	 */
	@Transactional(rollbackFor = Exception.class)
	public void sendMessage() {
		
		// logger.info("启动待付款提醒通知定时任务");
		// 1、获取大于一小时未支付会员订单
		List<ShopMemberOrder> shopMemberOrderList = shopMemberOrderService.getWaitPayTipOrderList();
		for (ShopMemberOrder shopMemberOrder : shopMemberOrderList) {
			long orderId = shopMemberOrder.getId();
			String wxaServiceUrl = AdminConstants.WXA_SERVER_URL;
			String apiUrl = "/miniapp/advice/waitPayAdvice.json";
			String url = wxaServiceUrl + apiUrl;
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderId", String.valueOf(orderId));
			Map<String, String> headers = new HashMap<String, String>();
			String sign = ParamSignUtils.getSign(map);
		 	headers.put("sign", sign);
		 	headers.put("version", version);
			logger.info("发送模板通知url:" + url + ",map:" + map);
			Response<String> resp = Requests.get(url).headers(headers).params(map).text();
			String ret = resp.getBody();
			logger.info("模板通知发送结果ret:" + ret);
			if (StringUtils.isNotEmpty(ret)) {
				JSONObject retJSON = JSON.parseObject(ret);// json字符串转换成jsonobject对象
				boolean successful = retJSON.getBoolean("successful");
				if (successful) {
					logger.info("更改待支付发送标记，successful" + successful);
					// 更改发送待支付提醒标记为已发送
					shopMemberOrderService.updateOrderSendMessage(orderId);
				} else {
					logger.info("发送通知返回结果为失败，请排查问题，successful" + successful);
				}
			} else {
				logger.info("发送待付款通知失败请排查问题，ret:" + ret + ",url：" + url + ",map:" + map);
			}
		}
//		logger.info("完成待付款提醒通知定时任务");
	}
	
	/**
	 * 取消超时未付款的订单
	 */
	@Transactional(rollbackFor = Exception.class)
	public void stopMemberOrderOvertime() {
		List<ShopMemberOrder> shopMemberOrderList = shopMemberOrderService.getUnPaidMemberOrderList();
		for (ShopMemberOrder shopMemberOrder : shopMemberOrderList) {
			long nowTime = System.currentTimeMillis();
			shopMemberOrder.setCancelReasonType(3);
			shopMemberOrder.setCancelReason("超过24小时未支付，订单已被系统自动取消");
			shopMemberOrder.setOrderStopTime(nowTime);
			shopMemberOrder.setUpdateTime(nowTime);
			int record = shopMemberOrderService.storeOrderByOrderId(shopMemberOrder);
			if (record != 1) {
				logger.error("获取订单列表-小程序取消订单orderId:" + shopMemberOrder.getId());
				throw new RuntimeException("获取订单列表-小程序取消订单orderId:" + shopMemberOrder.getId());
			} else {
				// 添加日志
				ShopMemberOrderLog shopMemberOrderLog = new ShopMemberOrderLog();
				shopMemberOrderLog.setOldStatus(shopMemberOrder.getOrderStatus());
				shopMemberOrderLog.setNewStatus(ShopMemberOrderLog.order_status_order_closed);
				shopMemberOrderLog.setOrderId(shopMemberOrder.getId());
				shopMemberOrderLog.setCreateTime(shopMemberOrder.getUpdateTime());
				shopMemberOrderService.addShopMemberOrderLog(shopMemberOrderLog);

				// 如果有优惠券,将优惠券设置为未使用
				long couponId = shopMemberOrder.getCouponId();
				if (couponId > 0) {
					String orderNo = shopMemberOrder.getOrderNumber();

					int couponRecord = commonRefMapper.updateCouponStatus(couponId,-1L,1,0);

//					ShopMemberCoupon shopMemberCoupon = new ShopMemberCoupon();
//					shopMemberCoupon.setId(couponId);
//					shopMemberCoupon.setAdminId(0L);
//					shopMemberCoupon.setCheckTime(0L);
//					shopMemberCoupon.setCheckMoney(0D);
//					shopMemberCoupon.setStatus(ShopMemberCoupon.status_normal);
//					shopMemberCoupon.setUpdateTime(nowTime);
//					int couponRecord = shopMemberOrderService.updateShopMemberCouponStatus(shopMemberCoupon);
					if (couponRecord != 1) {
						logger.error("小程序取消订单:将优惠券设置为未使用couponId:" + couponId);
						throw new RuntimeException("小程序取消订单:将优惠券设置为未使用couponId:" + couponId);
					}
				}
			}
		}
	}

	
	
	@Transactional(rollbackFor=Exception.class)
	public void disposeStoreOrders(long currentTime) {
		List<StoreOrder> unpaidStoreOrder = storeOrderService.allUnpaidFacepayOrderNew(currentTime);
		Set<Long> overdueUnpaid = new HashSet<Long>();
		// Map<Long, Order> oldById = new HashMap<Long, Order>();
		//退优惠券
		Set<Long> nos = new HashSet<Long>();
		for (StoreOrder unpaidOrder : unpaidStoreOrder) {
			nos.add(unpaidOrder.getOrderNo());
		}
		Map<Long, List<StoreCouponUseLog>> couponUseLogsByOrderNo = storeCouponUseLogService.getLogsOfOrderNo(nos);
		for (StoreOrder storeOrder : unpaidStoreOrder) {
			List<StoreCouponUseLog> couponUseLogs = couponUseLogsByOrderNo.get(storeOrder.getOrderNo());
			if (couponUseLogs != null && couponUseLogs.size() > 0) {
				restoreCouponStore(couponUseLogs, currentTime);
			}
		}
		//退库存
		for (StoreOrder storeOrder : unpaidStoreOrder) {
			storeOrderService.updateOrderStatus(storeOrder.getStoreId(), storeOrder.getOrderNo(),
					OrderStatus.UNPAID.getIntValue(), OrderStatus.CLOSED.getIntValue(), currentTime);
			if(storeOrder.getParentId()>=0){
				restoreSKU(storeOrder);
			}
		}

	}

	private void restoreSKU(StoreOrder storeOrder) {
		Map<Long, Integer> productCountMap = new HashMap<Long, Integer>();
		List<StoreOrderItem> storeOrderItems = storeOrderItemService.getByOrderNo(storeOrder.getOrderNo());
		for (StoreOrderItem item : storeOrderItems) {
			Integer count = productCountMap.get(item.getSkuId());
			if (count == null) {
				count = 0;
			}
			count = count + item.getBuyCount();
			productCountMap.put(item.getSkuId(), count);
		}
		//限购活动订单超时未付款需要恢复库存
		long restrictionActivityProductId = storeOrder.getRestriction_activity_product_id();
        if(restrictionActivityProductId>0){
        	Set<Long> skuIds = productCountMap.keySet();
        	Wrapper<RestrictionActivityProductSku> wrapper = new EntityWrapper<RestrictionActivityProductSku>()
        			.eq("activity_product_id", restrictionActivityProductId);
        	List<RestrictionActivityProductSku> restrictionActivityProductSkuList = restrictionActivityProductSkuMapper.selectList(wrapper);
        	int allBuyCount = 0;
        	for (RestrictionActivityProductSku restrictionActivityProductSku : restrictionActivityProductSkuList) {
        		long productSkuId = restrictionActivityProductSku.getProductSkuId();
        		int remainCount = restrictionActivityProductSku.getRemainCount();//剩余活动商品数量
        		//初始化活动剩余商品数量到缓存
				String groupKey = MemcachedKey.GROUP_KEY_restrictionActivityProductId;
				String key = "_restrictionActivityProductId_"+String.valueOf(restrictionActivityProductId)+"_skuId_"
						+String.valueOf(restrictionActivityProductSku.getProductSkuId());
				//将剩余活动商品数量放入缓存
				int time = 7*24*60*60;//有效期1周，注意这里有效期不能大于30天
				logger.info("从数据库中获取剩余活动商品数量surplusActivityProductCount："+remainCount
						+",restrictionActivityProductSkuId："+restrictionActivityProductSku.getId());
				if(skuIds.contains(productSkuId)){
					int buyCount = productCountMap.get(productSkuId);
					long restrictionActivityProductSkuId = restrictionActivityProductSku.getId();
					RestrictionActivityProductSku restrictionActivityProductSkuNew = new RestrictionActivityProductSku();
					restrictionActivityProductSkuNew.setId(restrictionActivityProductSkuId);
					remainCount = remainCount + buyCount;
					restrictionActivityProductSkuNew.setRemainCount(remainCount);
					restrictionActivityProductSkuMapper.updateById(restrictionActivityProductSkuNew);
					logger.info(String.valueOf("orderNo:"+storeOrder.getOrderNo()+",skuId:"+productSkuId+",remainCount:"+remainCount));
					memcachedService.setCommon(groupKey, key, time , String.valueOf(remainCount).trim());//注意这里必须转为string否则不能加减
				}
				allBuyCount = allBuyCount + remainCount;
			}
        	RestrictionActivityProduct restrictionActivityProduct = new RestrictionActivityProduct();
        	restrictionActivityProduct.setId(restrictionActivityProductId);
        	restrictionActivityProduct.setRemainCount(allBuyCount);
			restrictionActivityProductMapper.updateById(restrictionActivityProduct);
        }else{
        	Set<Entry<Long, Integer>> entries = productCountMap.entrySet();
    		Iterator<Entry<Long, Integer>> i = entries.iterator();
    		while (i.hasNext()) {
    			Entry<Long, Integer> curEntry = i.next();
    			productService.updateProductSKU(curEntry.getKey(), curEntry.getValue());
//    			productService.updateProductSaleCount(curEntry.getKey(), curEntry.getValue());
    		}
        }
		
	
	}

	private void disposeYjjOrders(long currentTime) {
		List<OrderNew> unpaidOrderNews = orderNewService.allUnpaidFacepayOrderNew();
		for (OrderNew orderNew : unpaidOrderNews) {
			orderNewFacade.updateOrderStatus(orderNew.getOrderNo(), OrderStatus.UNPAID.getIntValue(),
					OrderStatus.CLOSED.getIntValue(), true);
		}

		// List<Order> unpaidOrders =
		// orderService.loadAllOrdersList(OrderStatus.UNPAID.getIntValue());

		// Set<Long> overdueUnpaid = new HashSet<Long>();
		// Map<Long, Order> oldById = new HashMap<Long, Order>();
		// for (Long orderNo : orderNos) {
		// overdueUnpaid.add(orderNo);
		// oldById.put(unpaidOrder.getId(), unpaidOrder);
		// }

		// 老订单表已经删除，所以这块吧原来的代码删除了一下
		// Map<Long, Long> oldByNew =
		// orderFacade.associateNewOrder(overdueUnpaid);
		List<Long> orderNos = orderNewService.getOrderNosByOrderStatus(OrderStatus.UNPAID.getIntValue());
		Set<Long> nos = new HashSet<Long>();
		for (Long orderNo : orderNos) {
			nos.add(orderNo);// 订单编号
		}
		Map<Long, OrderNew> newById = orderNewService.orderNewMapOfOrderNos(nos);
		Map<Long, List<CouponUseLog>> couponUseLogsByOrderNo = couponUseLogService.getLogsOfOrderNo(nos);

		for (Long orderNo : orderNos) {
			// long orderId = entry.getKey();
			// long orderNo = entry.getValue(); //订单编号
			OrderNew orderNew = newById.get(orderNo);
			// Order unpaidOrder = oldById.get(orderId);
			if (orderNew.getExpiredTime() < currentTime) {
				if (orderNew.canCancel()) {
					try {
						restoreOrder(orderNo, orderNew, couponUseLogsByOrderNo, currentTime);
					} catch (ParameterErrorException e) {
						logger.error("com.jiuy.core.service.task.OrderDisposingJob Exception, orderNo: " + orderNo);
					}
				}
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void restoreOrder(long orderNo, OrderNew orderNew, Map<Long, List<CouponUseLog>> couponUseLogsByOrderNo,
			long currentTime) {

		// 还原库存
		restoreSKUNew(orderNo);
		// 还原用户玖币
		restoreUserCoin(orderNew);
		// 更新订单状态，旧表已经删除，所以注释掉
		// orderFacade.updateOrderStatus(orderId,
		// OrderStatus.UNPAID.getIntValue(), OrderStatus.CLOSED.getIntValue(),
		// true);

		orderNewFacade.updateOrderStatus(orderNo, OrderStatus.UNPAID.getIntValue(), OrderStatus.CLOSED.getIntValue(),
				true);

		List<CouponUseLog> couponUseLogs = couponUseLogsByOrderNo.get(orderNo);
		if (couponUseLogs != null && couponUseLogs.size() > 0) {
			restoreCoupon(couponUseLogs, currentTime);
		}
	}

	private void restoreCoupon(List<CouponUseLog> couponUseLogs, Long currentTime) {
		Set<Long> couponIds = new HashSet<Long>();
		for (CouponUseLog couponUseLog : couponUseLogs) {
			couponUseLog.setStatus(CouponUseStatus.GIVE_BACK);
			couponUseLog.setCreateTime(currentTime);

			couponIds.add(couponUseLog.getCouponId());
		}

		couponUseLogService.add(couponUseLogs);
		couponService.giveBack(couponIds);

	}

	private void restoreCouponStore(List<StoreCouponUseLog> couponUseLogs, Long currentTime) {
		Set<Long> couponIds = new HashSet<Long>();
		for (StoreCouponUseLog couponUseLog : couponUseLogs) {
			couponUseLog.setStatus(CouponUseStatus.GIVE_BACK.getIntValue());
			couponUseLog.setCreateTime(currentTime);

			couponIds.add(couponUseLog.getCouponId());
			// 返还优惠券
			commonRefMapper.updateCouponStatus(couponUseLog.getCouponId(),-1L,1,0);
			 // storeCouponUseLogService.updateStoreCouponUseLog(couponUseLog);
		}

//		storeCouponUseLogService.add(couponUseLogs);
		storeCouponService.giveBack(couponIds);

	}

	private void restoreUserCoin(OrderNew orderNew) {
		if (null != orderNew) {
			// int unaCoins = unpaidOrder.getUnavalCoinUsed();
			int unaCoins = orderNew.getCoinUsed();// unpaidOrder.getAvalCoinUsed();
			long userid = orderNew.getUserId();
			String relatedId = String.valueOf(orderNew.getOrderNo());
			userCoinService.updateUserCoin(userid, 0, unaCoins, relatedId, new Date().getTime(),
					UserCoinOperation.ORDER_CLOSE);
		}
	}

	private void restoreSKUNew(long orderId) {
		List<OrderItem> orderItems = orderItemService.orderItemsOfOrderId(CollectionUtil.createList(orderId));
		for (OrderItem item : orderItems) {
			ProductSKU sku = productSKUMapper.searchById(item.getSkuId());

			if (sku == null)
				return;

			if (sku.getlOWarehouseId2() > 0 && sku.getlOWarehouseId2() == item.getlOWarehouseId()) {
				productSKUMapper.updateRemainCountSecond(item.getSkuId(), item.getBuyCount());
			} else {
				productSKUMapper.updateRemainCount(item.getSkuId(), item.getBuyCount());
			}
		}
	}

	
}
