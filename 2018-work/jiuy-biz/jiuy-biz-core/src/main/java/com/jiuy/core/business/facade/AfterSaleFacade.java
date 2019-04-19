package com.jiuy.core.business.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.order.OrderItem;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.aftersale.ServiceTicketService;
import com.jiuy.core.service.notifacation.NotifacationService;
import com.jiuy.core.service.order.OrderItemService;
import com.jiuyuan.constant.order.AfterSaleStatus;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ServiceTicket;

@Service
public class AfterSaleFacade {

	@Autowired
	private ServiceTicketService serviceTicketService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private NotifacationService notificationServiceImpl;
	
	synchronized public void upDateServiceTickedAndNotification(long serviceId, int status, String message) {
		ServiceTicket serviceTicket = serviceTicketService.ServiceTicketOfId(serviceId);
		// 1、修改退换货状态并审核退换货申请添加售后信息
 		if (serviceTicket.getStatus() != status) {
			try {
				serviceTicketService.updateServiceTicket(serviceId, status, message);
				ServiceTicket newServiceTicket = serviceTicketService.ServiceTicketOfId(serviceId);
					if(newServiceTicket.getStatus() == status){
		//				auditApplyAddNotification(serviceId, status);
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			throw new ParameterErrorException("修改状态失败");
		}
	}

	/**
	 * 审核退换货申请添加售后信息
	 * 
	 * @param orderNo
	 * @param expressNo
	 */
	private void auditApplyAddNotification(long serviceId, int status) {

		ServiceTicket serviceTicket = serviceTicketService.ServiceTicketOfId(serviceId);

		long orderItemId = serviceTicket.getOrderItemId();
		OrderItem orderItem = orderItemService.OrderItemOfId(orderItemId);// OrderItemService
																			// orderNewService.orderNewOfServiceId(serviceId);
		long productId = orderItem.getProductId();
		Product product = productService.getProductById(productId);
		String image = product.getFirstImage();

		long userId = serviceTicket.getUserId();
		String ExamineMemo = serviceTicket.getExamineMemo();
		int type = serviceTicket.getType();// 服务类型 0:退货；1:换货
		String typeName = "";
		if (type == 0) {// 退货
			typeName = "退货";
		} else if (type == 1) {// 换货
			typeName = "换货";
		}

		String title = "";
		String abstracts = "服务编号：" + serviceId + "，申请说明：" + ExamineMemo + "";
		if (status == AfterSaleStatus.REJECTED.getIntValue()) {// 已驳回
			title = "" + typeName + "状态：" + "申请完成，" + typeName + "申请被驳回" + "";
		} else if (status == AfterSaleStatus.TO_DELIVERY.getIntValue()) {// 待买家发货
			title = "" + typeName + "状态：" + "受理中，等待买家发货退还" + "";
		}
		// 添加消息信息
		notificationServiceImpl.addNotificationAndUserNotification(10, image, String.valueOf(serviceId), userId, title,
				abstracts);
	}

}
