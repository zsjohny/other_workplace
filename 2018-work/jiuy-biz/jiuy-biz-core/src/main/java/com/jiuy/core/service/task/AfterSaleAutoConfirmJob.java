package com.jiuy.core.service.task;

import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.HttpClientUtils;
import com.util.CallBackUtil;
import com.util.ServerPathUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.aftersale.ServiceTicketService;
import com.jiuyuan.RefundStatus;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MessageType;
import com.jiuyuan.constant.order.AfterSaleStatus;
import com.jiuyuan.dao.mapper.supplier.ShopMemberOrderNewMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.ServiceTicket;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.RefundOrderActionLogEnum;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.service.common.IRefundOrderFacadeNJ;
import com.jiuyuan.service.common.RefundSMSNotificationService;
import com.yujj.dao.mapper.UserMapper;
/**
 * #########定时任务##########
 * task.aftersale.auto.confirm
 * #########################
 * 买家超时未发货定时任务
 * 小程序自动确认收货
 *
 */
public class AfterSaleAutoConfirmJob {
	private static final Logger logger = LoggerFactory.getLogger(AfterSaleAutoConfirmJob.class);

	private static final long THREE_DAYS = 3L * 24 * 60 * 60 * 1000;

	@Autowired
	private IRefundOrderFacadeNJ refundOrderFacadeNJ;

	@Autowired
	private ServiceTicketService serviceTicketService;

	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private RefundSMSNotificationService refundSMSNotificationService;
	
	@Autowired
	private UserNewMapper userNewMapper;
	
	 @Autowired
    private ShopMemberOrderService shopMemberOrderService;
    @Autowired
    private ShopMemberOrderNewMapper shopMemberOrderMapper;

	public void execute() {
		try {
			logger.info("开始买家超时未发货定时任务！");
			customerOverTimeWithoutDelivery();
			logger.info("结束买家超时未发货定时任务！");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		  //小程序自动确认收货
		 long timeNow = System.currentTimeMillis();
	     try {
			//小程序自动确认收货
	     	automaticConfirmation(timeNow);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
	     
	     
		List<ServiceTicket> serviceTickets = serviceTicketService
				.getItems(AfterSaleStatus.RETURNED_OR_DELIVERED.getIntValue(), 1);

		Long expireTime = globalSettingService.getLong(GlobalSettingName.AFTER_SALE_AUTO_CONFIRM);

		long now = System.currentTimeMillis();
		Set<Long> serviceIds = new HashSet<Long>();
		for (ServiceTicket serviceTicket : serviceTickets) {
			if (now - serviceTicket.getSellerTime() > expireTime) {
				serviceIds.add(serviceTicket.getId());
			}
		}

		if (serviceIds.size() < 1) {
			return;
		}
		serviceTicketService.updateServiceTicket(serviceIds, AfterSaleStatus.CONFIRM_RECEIVE.getIntValue());
	}

	// 售后中，买家超时未发货关闭售后订单
	@Transactional(rollbackFor = Exception.class)
	public void customerOverTimeWithoutDelivery() {
		// 搜索所用的售后订单，获取所有平台未介入，且售后状态为待卖家确认收货的售后订单
		List<RefundOrder> refundOrderList = refundOrderFacadeNJ.searchCustomerDeliveryRefundOrder();
		if(refundOrderList.size() == 0){
			return;
		}
		List<RefundOrder> needCloseRefundOrderList = new ArrayList<RefundOrder>();
		// 遍历所有的时间，再进行关闭售后单操作
		Long currentTime = System.currentTimeMillis();// 当前时间
		//该为测试需要，用完就关闭
//		currentTime=1000000000000000L;
		for (RefundOrder refundOrder : refundOrderList) {
			Long SupplierAutoTakeDeliveryPauseTimeLength = refundOrder.getSupplierAutoTakeDeliveryPauseTimeLength();// 获取买家发货延长时间
			Long storeAllowRefundTime = refundOrder.getStoreAllowRefundTime();// 获取卖家同意时间
			if (currentTime - storeAllowRefundTime >= THREE_DAYS + SupplierAutoTakeDeliveryPauseTimeLength) {
				needCloseRefundOrderList.add(refundOrder);
			}
		}
		// 遍历关闭售后单操作，因为买家超时未发货
		for (RefundOrder refundOrder : needCloseRefundOrderList) {
			try {
				logger.info("OrderNo:"+refundOrder.getOrderNo());
				refundOrderFacadeNJ.updateOrderStatusWhenClose(refundOrder.getId(),
						RefundStatus.CUSTOMER_OVERTIME_UNDELIVERY, refundOrder.getOrderNo(), RefundOrderActionLogEnum.D,
						null);
				UserNew userNew = userNewMapper.selectById(refundOrder.getSupplierId());
				refundSMSNotificationService.sendSMSNotificationAndGEPush(null, MessageType.E.getIntValue(), refundOrder.getStoreId(), userNew.getPhone());
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("售后订单：NO." + refundOrder.getId() + "，出现问题无法关闭售后单操作！");
				throw new RuntimeException(e.getMessage());
			}
		}
		
	}

     //小程序自动确认收货
	@Transactional(rollbackFor = Exception.class)
 	public void automaticConfirmation(long timeNow) {
		BizUtil.todo ("3.8.5--小程序开始自动确认收货");
 		logger.info("小程序开始自动确认收货！");
 		long deliveryTime = timeNow - 15 * 24 * 60 * 60 * 1000 ;
        String url = "/distribution/distribution/cashOutIn/dstbSuccess";
        logger.info ("转发分销 url={}", url);

		Wrapper<ShopMemberOrder> wrapper = new EntityWrapper<ShopMemberOrder>();
 		wrapper.eq("order_status", 6).eq("order_type", 1).le("delivery_time", deliveryTime);
 		List<ShopMemberOrder> shopMemberOrderList = shopMemberOrderMapper.selectList(wrapper );
 		logger.info ("小程序开始自动确认收货 size={}", shopMemberOrderList.size ());
 		for (ShopMemberOrder shopMemberOrder : shopMemberOrderList) {
 			ShopMemberOrder newShopMemberOrder = new ShopMemberOrder();
			long current = System.currentTimeMillis ();
			newShopMemberOrder.setId(shopMemberOrder.getId());
 			newShopMemberOrder.setOrderStatus(ShopMemberOrder.order_status_order_fulfillment);
 			 newShopMemberOrder.setConfirmSignedTime(current);//确认收货时间
 			newShopMemberOrder.setOrderFinishTime(current);
 			 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
 		   	 newShopMemberOrder.setConfirmSignedDate(Integer.valueOf(formatter.format(new Date())));
 			shopMemberOrderMapper.updateById(newShopMemberOrder);
 			logger.info("小程序结束自动确认收货！shopmemberId:"+shopMemberOrder.getId());


			//小程序自动确认收货--分佣返现
			JSONObject map = new JSONObject();
			map.put ("orderNumber", shopMemberOrder.getOrderNumber ());
			map.put ("wx2DstbSign", new Md5Hash (shopMemberOrder.getOrderNumber () + "xiaochengxu2dstb").toString ());
			map.put ("orderSuccessTime", current);
			logger.info ("请求记录分销 url={},map={}", url,map);
//				String response = HttpClientUtils.get (url, map);
			 CallBackUtil.send(map.toJSONString(),url,"get");
 		}

 		logger.info("小程序结束自动确认收货！");
 	}
     
	
}
