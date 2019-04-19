package com.jiuy.core.business.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jiuy.core.dao.StoreOrderDao;
import com.jiuy.core.dao.StoreOrderItemDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.member.StoreBusinessService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuy.core.service.storeaftersale.StoreFinanceTicketService;
import com.jiuy.core.service.storeaftersale.StoreServiceTicketService;
import com.jiuy.core.service.storeorder.StoreOrderItemService;
import com.jiuy.core.service.storeorder.StoreOrderService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.order.PaymentTypeDetail;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicket;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicketVO;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.entity.storeorder.StoreOrderItem;

@Service

public class StoreServiceTicketFacade {
	
	@Autowired
	private StoreServiceTicketService storeServiceTicketService;
	
	@Autowired
	private StoreBusinessService storeBusinessService;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private StoreOrderItemService storeOrderItemService;
	
	@Autowired
	private StoreOrderDao storeOrderDao;
	
	@Autowired
	private StoreFinanceTicketService storeFinanceTicketService;
	
	@Autowired
	private StoreOrderService storeOrderService;
	
	@Autowired
	private StoreOrderItemDao storeOrderItemDao;
	
	public List<Map<String, Object>> search(PageQuery pageQuery, StoreServiceTicketVO serviceTicket) {
		List<Map<String, Object>> list = storeServiceTicketService.search(pageQuery, serviceTicket);
		
		Set<Long> userIds = new HashSet<>();
		Set<Long> skuNos = new HashSet<>();
		Set<Long> orderItemIds = new HashSet<>();
		long processOrderNo = 0L;
		for(Map<String, Object> map : list) {
			userIds.add((Long)map.get("UserId"));	
			skuNos.add((Long)map.get("SkuNo"));
			orderItemIds.add((Long)map.get("OrderItemId"));
			if(list != null && list.size() == 1 && map != null && map.get("ProcessOrderNo")!= null){
				processOrderNo = (long) map.get("ProcessOrderNo");
			}
		}
		
		Map<Long, StoreBusiness> userById = storeBusinessService.storesMapOfIds(userIds);
		Map<Long, ProductSKU> skuByNo = productSKUService.skuByNo(skuNos);
		Map<Long, StoreOrderItem> orderItemById = storeOrderItemService.OrderItemOfIds(orderItemIds);
		StoreOrder storeOrder = storeOrderService.orderOfOrderNo(processOrderNo);
		
		for(Map<String, Object> map : list) {
			long userId =(Long)map.get("UserId");
			long skuNo = (Long)map.get("SkuNo");
			long orderItemId = (Long)map.get("OrderItemId");
			String applyImgs = (String)map.get("ApplyImageUrl");
			String applyTimeStr = DateUtil.convertMSEL((long) map.get("ApplyTime"));
			String processTimeStr = DateUtil.convertMSEL((long)map.get("ProcessTime"));
			
			String examineTimeStr = DateUtil.convertMSEL((long)map.get("ExamineTime"));
			if(applyImgs != null) {
				map.put("apply_imgs", JSON.parseArray(applyImgs));
			}
			map.put("user_info", userById.get(userId));
			map.put("sku_info", skuByNo.get(skuNo));
			map.put("order_item", orderItemById.get(orderItemId));
			map.put("ApplyTimeStr", applyTimeStr);
			map.put("ProcessTimeStr", processTimeStr);
			map.put("ExamineTimeStr", examineTimeStr);
			if(storeOrder != null){
				String expressInfo	= storeOrder.getExpressInfo();
				StoreBusiness storeBusiness = (StoreBusiness) map.get("user_info");
				String[] addressInfo = expressInfo.split(",");
				if(addressInfo.length >= 3){
//					storeBusiness.setReceiverName(addressInfo[0]);
//					storeBusiness.setTelephone(addressInfo[1]);
//					storeBusiness.setAddrFull(addressInfo[2]);
				}
			}
		}
		
		return list;
	}


	@Transactional(rollbackFor = Exception.class)
	public void confirmReturn(long serviceId, int processResult, double processMoney, double processExpressMoney,
			String message) {
		
		if(processMoney + processExpressMoney < 0.01 ) {
			throw new ParameterErrorException("人民币为0,参数错误");
		}
		
		int status = 4;
		//生成相应的财务工单
		if (processResult == 1 || processResult == 2 || processResult == 4) {
			StoreServiceTicket storeServiceTicket = storeServiceTicketService.ServiceTicketOfId(serviceId);
			StoreOrder storeOrder = storeOrderDao.orderNewOfOrderNo(storeServiceTicket.getOrderNo());
			int payType = storeOrder.getPaymentType();
			int returnType = 0;
			if (payType == PaymentTypeDetail.ALIPAY.getIntValue() || payType == PaymentTypeDetail.ALIPAY_SDK.getIntValue()) {
				returnType = 1;
			} else if (payType == PaymentTypeDetail.WEIXINPAY_NATIVE.getIntValue() || payType == PaymentTypeDetail.WEIXINPAY_SDK.getIntValue()) {
				returnType = 3;
			} else if (payType == PaymentTypeDetail.BANKCARD_PAY.getIntValue()) {
				returnType = 5;
			}
			storeFinanceTicketService.add(serviceId, returnType,0,storeOrder.getStoreId());//财务工单来源于售后
			status = 5;
		} else if(processResult == 3 && processMoney + processExpressMoney < 0.01) {
			status = 5;
		}
		storeServiceTicketService.updateServiceTicket(serviceId, processResult, processMoney, processExpressMoney, message, status);

		StoreServiceTicket storeServiceTicket = storeServiceTicketService.ServiceTicketOfId(serviceId);
		StoreOrder storeOrder = storeOrderDao.orderNewOfOrderNo(storeServiceTicket.getOrderNo());
		if (processResult == 3) {
			List<StoreOrderItem> orderItems = storeOrderItemDao.orderItemsOfIds(CollectionUtil.createList(storeServiceTicket.getOrderItemId()));
			if(orderItems.size() < 1) {
				throw new ParameterErrorException("storeOrderItemId 参数错误");
			}
			long currentTime = System.currentTimeMillis();
			
			StoreOrder orderNew2 = new StoreOrder();
			orderNew2.setStoreId(storeOrder.getStoreId());
			orderNew2.setOrderType(1);
			orderNew2.setOrderStatus(0);
			orderNew2.setTotalMoney(processMoney);
			orderNew2.setTotalPay(processMoney);
			orderNew2.setLoWarehouseId(orderItems.get(0).getlOWarehouseId());
			orderNew2.setTotalExpressMoney(processExpressMoney);
			orderNew2.setExpressInfo(storeOrder.getExpressInfo());
			orderNew2.setCreateTime(currentTime);
			orderNew2.setUpdateTime(currentTime);
			
			storeOrderDao.insert(orderNew2);
			storeServiceTicketService.updateServiceTicket(serviceId, orderNew2.getOrderNo());
		}
	}

}
