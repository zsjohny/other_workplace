//package com.jiuy.core.service.task;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.jiuy.core.dao.OrderNewDao;
//import com.jiuy.core.dao.OrderNewLogDao;
//import com.jiuy.core.dao.ServiceTicketDao;
//import com.jiuy.core.dao.StoreBusinessDao;
//import com.jiuy.core.service.GlobalSettingService;
//import com.jiuy.core.service.InvitedUserActionLogService;
//import com.jiuy.core.service.StoreOrderLogService;
//import com.jiuy.core.service.SupplierFinanceLogService;
//import com.jiuy.core.service.SupplierUserOldService;
//import com.jiuy.core.service.UserCoinService;
//import com.jiuy.core.service.order.OrderOldService;
//import com.jiuy.core.service.storeorder.StoreOrderService;
//import com.jiuyuan.RefundStatus;
//import com.jiuyuan.entity.newentity.RefundOrder;
//import com.jiuyuan.entity.newentity.RefundOrderActionLogEnum;
//import com.jiuyuan.service.common.IRefundOrderFacadeNJ;
//import com.jiuyuan.service.common.RefundOrderFacadeNJ;
//import com.store.service.NJShopMemberOrderService;
//
//@Service
//public class CustomerOverTimeWithoutDelivery {
//	private Logger logger = Logger.getLogger(CustomerOverTimeWithoutDelivery.class);
//	
//	private static final long THREE_DAYS = 3L*24*60*60*1000;
//	
//	@Autowired
//	private IRefundOrderFacadeNJ refundOrderFacadeNJ;
//	
//	//售后中，买家超时未发货关闭售后订单
//	@Transactional(rollbackFor = Exception.class)
//	public void execute() {
//		//搜索所用的售后订单，获取所有平台未介入，且售后状态为待卖家确认收货的售后订单
//		List<RefundOrder> refundOrderList = refundOrderFacadeNJ.searchCustomerDeliveryRefundOrder();
//		List<RefundOrder> needCloseRefundOrderList = new ArrayList<RefundOrder>();
//		//遍历所有的时间，再进行关闭售后单操作
//		for(RefundOrder refundOrder : refundOrderList){
//			Long SupplierAutoTakeDeliveryPauseTimeLength = refundOrder.getSupplierAutoTakeDeliveryPauseTimeLength();//获取买家发货延长时间
//			Long currentTime = System.currentTimeMillis();//当前时间
//			Long storeAllowRefundTime = refundOrder.getStoreAllowRefundTime();//获取卖家同意时间
//			if(currentTime-storeAllowRefundTime >= THREE_DAYS+SupplierAutoTakeDeliveryPauseTimeLength){
//				needCloseRefundOrderList.add(refundOrder);
//			}
//		}
//		//遍历关闭售后单操作，因为买家超时未发货
//		for(RefundOrder refundOrder:needCloseRefundOrderList){
//			try {
//				refundOrderFacadeNJ.updateOrderStatusWhenClose(refundOrder.getId(), RefundStatus.CUSTOMER_OVERTIME_UNDELIVERY, refundOrder.getOrderNo(), RefundOrderActionLogEnum.D, null);
//			} catch (Exception e) {
//				logger.error(e.getMessage());
//				logger.error("售后订单：NO."+refundOrder.getId()+"，出现问题无法关闭售后单操作！");
//			    throw new RuntimeException(e.getMessage());
//			}
//		}
//		
//		
//		
//		
//	}
//
//}
