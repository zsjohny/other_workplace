package com.jiuy.core.business.facade;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jiuy.core.dao.OrderItemDao;
import com.jiuy.core.dao.OrderNewDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.aftersale.ServiceTicketVO;
import com.jiuy.core.meta.order.OrderItem;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.service.UserCoinService;
import com.jiuy.core.service.UserManageService;
import com.jiuy.core.service.UserService;
import com.jiuy.core.service.aftersale.FinanceTicketService;
import com.jiuy.core.service.aftersale.ServiceTicketService;
import com.jiuy.core.service.order.OrderItemService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.order.PaymentTypeDetail;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.ServiceTicket;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class ServiceTicketFacade {

	@Autowired
	private ServiceTicketService serviceTicketService;
	
	@Autowired
	private FinanceTicketService financeTicketService;
	
	@Autowired
	private UserManageService userManageService;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private OrderItemDao orderItemDao;
	
	@Autowired
	private OrderNewDao orderNewDao;
	
	@Resource
	private UserCoinService userCoinService;
	
	@Resource
	private UserService userService;
	
	@Transactional(rollbackFor = Exception.class)
	public void confirmReturn(long serviceId, int processResult, double processMoney, double processExpressMoney,
			int processReturnJiuCoin, String message) {
		
		if(processMoney + processExpressMoney < 0.01 && processReturnJiuCoin != 0) {
			throw new ParameterErrorException("人民币为0元,则玖币必须为0,参数错误");
		}
		
		int status = 4;
		//生成相应的财务工单
		if (processResult == 1 || processResult == 2 || processResult == 4) {
			ServiceTicket serviceTicket = serviceTicketService.ServiceTicketOfId(serviceId);
			OrderNew orderNew = orderNewDao.orderNewOfOrderNo(serviceTicket.getOrderNo());
			int payType = orderNew.getPaymentType();
			int returnType = 0;
			if (payType == PaymentTypeDetail.ALIPAY.getIntValue() || payType == PaymentTypeDetail.ALIPAY_SDK.getIntValue()) {
				returnType = 1;
			} else if (payType == PaymentTypeDetail.WEIXINPAY_NATIVE.getIntValue() || payType == PaymentTypeDetail.WEIXINPAY_SDK.getIntValue() || payType == PaymentTypeDetail.WEIXIN_WEB_PAY.getIntValue()) {
				returnType = 3;
			} else if (payType == PaymentTypeDetail.BANKCARD_PAY.getIntValue()) {
				returnType = 5;
			}
			long yjjNumber = userService.getYjjNumberById(orderNew.getUserId());
			financeTicketService.add(serviceId, returnType,0,yjjNumber);//财务工单来源于售后
			status = 5;
		} else if(processResult == 3 && processMoney + processExpressMoney < 0.01) {
			status = 5;
		}
		serviceTicketService.updateServiceTicket(serviceId, processResult, processMoney, processExpressMoney, processReturnJiuCoin, message, status);

		ServiceTicket serviceTickets = serviceTicketService.ServiceTicketOfId(serviceId);
		OrderNew orderNew = orderNewDao.orderNewOfOrderNo(serviceTickets.getOrderNo());
		if (processResult == 3) {
			List<OrderItem> orderItems = orderItemDao.orderItemsOfIds(CollectionUtil.createList(serviceTickets.getOrderItemId()));
			if(orderItems.size() < 1) {
				throw new ParameterErrorException("orderItemId 参数错误");
			}
			long currentTime = System.currentTimeMillis();
			
			OrderNew orderNew2 = new OrderNew();
			orderNew2.setUserId(orderNew.getUserId());
			orderNew2.setOrderType(1);
			orderNew2.setOrderStatus(0);
			orderNew2.setTotalMoney(processMoney);
			orderNew2.setTotalPay(processMoney);
			orderNew2.setlOWarehouseId(orderItems.get(0).getlOWarehouseId());
			orderNew2.setTotalExpressMoney(processExpressMoney);
			orderNew2.setExpressInfo(orderNew.getExpressInfo());
			orderNew2.setCoinUsed(processReturnJiuCoin);
			orderNew2.setCreateTime(currentTime);
			orderNew2.setUpdateTime(currentTime);
			
			orderNewDao.insert(orderNew2);
			serviceTicketService.updateServiceTicket(serviceId, orderNew2.getOrderNo());
		}
		userCoinService.updateUserCoin(orderNew.getUserId(), 0, processReturnJiuCoin, "" + orderNew.getOrderNo(), new Date().getTime(),
				UserCoinOperation.ORDER_CANCEL);
	}

	public List<Map<String, Object>> search(PageQuery pageQuery, ServiceTicketVO serviceTicket) {
		List<Map<String, Object>> list = serviceTicketService.search(pageQuery, serviceTicket);
		
		Set<Long> userIds = new HashSet<>();
		Set<Long> skuNos = new HashSet<>();
		Set<Long> orderItemIds = new HashSet<>();
		for(Map<String, Object> map : list) {
			userIds.add((Long)map.get("UserId"));
			skuNos.add((Long)map.get("SkuNo"));
			orderItemIds.add((Long)map.get("OrderItemId"));
		}
		
		Map<Long, User> userById = userManageService.usersMapOfIds(userIds);
		Map<Long, ProductSKU> skuByNo = productSKUService.skuByNo(skuNos);
		Map<Long, OrderItem> orderItemById = orderItemService.OrderItemOfIds(orderItemIds);
		
		for(Map<String, Object> map : list) {
			long userId =(Long)map.get("UserId");
			long skuNo = (Long)map.get("SkuNo");
			long orderItemId = (Long)map.get("OrderItemId");
			String applyImgs = (String)map.get("ApplyImageUrl");
			
			if(applyImgs != null) {
				map.put("apply_imgs", JSON.parseArray(applyImgs));
			}
			map.put("user_info", userById.get(userId));
			map.put("sku_info", skuByNo.get(skuNo));
			map.put("order_item", orderItemById.get(orderItemId));
		}
		
		return list;
	}

}
