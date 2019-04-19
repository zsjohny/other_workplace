package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.RefundStatus;
import com.jiuyuan.dao.mapper.supplier.OrderItemNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderActionLogMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.ShopStoreAuthReasonMapper;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.RefundOrderActionLogEnum;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class RefundOrderFacadeNJ implements IRefundOrderFacadeNJ{
	private static final Log logger = LogFactory.get(RefundOrderFacadeNJ.class);
	@Autowired
	private RefundOrderMapper refundOrderMapper;

	@Autowired
	private StoreOrderItemNewService storeOrderItemService;
	
	@Autowired
	private OrderItemNewMapper orderItemNewMapper;
	
	@Autowired
	private ProductNewMapper productNewMapper;
	
	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;
	
	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private RefundService refundService;
	
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	
	@Autowired
	private RefundOrderActionLogMapper refundOrderActionLogMapper;
	
	@Autowired
	private RefundOrderActionLogService refundOrderActionLogService;
	
	@Autowired
	private RefundOrderService refundOrderService;
	
	@Autowired
	private RefundSMSNotificationService refundSMSNotificationService;
	
	@Autowired
	private StoreOrderItemNewService storeOrderItemNewService;
	
	@Autowired
	private ShopStoreAuthReasonMapper shopStoreAuthReasonMapper;
	
	@Autowired
	private IRefundOrderFacade refundOrderFacade;
	
	

	/**
	 * 买家撤销
	 */
	@Transactional(rollbackFor = Exception.class)
	public void cancelRefundOrder(Long refundOrderId) {
		RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
		if(null == refundOrder ){
			logger.info("无售后订单无法撤销！");
			throw new RuntimeException("无售后订单无法撤销！");
		}
		Integer refundStatus = refundOrder.getRefundStatus();
		if(refundStatus != RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue()&&refundStatus != RefundStatus.SELLER_ACCEPT.getIntValue()){
			logger.info("该售后订单无法撤销！");
			throw new RuntimeException("该售后订单无法撤销！");
		}
		
		//添加相关操作日志以及更改售后单状态
		if(refundOrder.getRefundStatus() == RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue()){
			updateOrderStatusWhenClose(refundOrderId,RefundStatus.CUSTOMER_CLOSE,refundOrder.getOrderNo(),RefundOrderActionLogEnum.G,null);
		}
		if(refundOrder.getRefundStatus() == RefundStatus.SELLER_ACCEPT.getIntValue()){
			updateOrderStatusWhenClose(refundOrderId,RefundStatus.CUSTOMER_CLOSE_AFTER_AGREE,refundOrder.getOrderNo(),RefundOrderActionLogEnum.H,null);
		}
		
	}
    /**
     * 售后订单关闭，通用版
     * @param refundOrderId 
     * @param storeId
     * @param refundStatus
     */
    @Override
	public void updateOrderStatusWhenClose(Long refundOrderId,RefundStatus refundStatus,Long orderNo,RefundOrderActionLogEnum refundOrderActionLogEnum,String platformCloseReason) {
		//获取售后订单状态
		RefundOrder reOrder = refundOrderMapper.selectById(refundOrderId);

	    int status = reOrder.getRefundStatus();
		if(status == RefundStatus.SELLER_REFUSE.getIntValue()||
		   status == RefundStatus.CUSTOMER_OVERTIME_UNDELIVERY.getIntValue()||
		   status == RefundStatus.ADMIN_CLOSE.getIntValue()||
		   status == RefundStatus.CUSTOMER_CLOSE_AFTER_AGREE.getIntValue()){
			logger.error("该售后订单已经关闭，请勿重复操作！refundOrderId:"+refundOrderId+",refundStatus:"+status);
			throw new RuntimeException("该售后订单已经关闭，请勿重复操作！");
		}
		;
		//更改售后单状态
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setId(refundOrderId);
		refundOrder.setRefundStatus(refundStatus.getIntValue());
		//如果是平台关闭，还需要把卖家自动确认收货总暂停时长加进去
//		if(refundStatus.getIntValue() == RefundStatus.ADMIN_CLOSE.getIntValue()){
//			RefundOrder reOrder = refundOrderMapper.selectById(refundOrderId);
//			Long supplierAutoTakeDeliveryPauseTime = reOrder.getSupplierAutoTakeDeliveryPauseTime();
//			Long supplierAutoTakeDeliveryPauseTimeLength = reOrder.getSupplierAutoTakeDeliveryPauseTimeLength();
//			refundOrder.setSupplierAutoTakeDeliveryPauseTime(0L);
//		    Long time = System.currentTimeMillis()-supplierAutoTakeDeliveryPauseTime;
//			supplierAutoTakeDeliveryPauseTimeLength += time;
//			refundOrder.setSupplierAutoTakeDeliveryPauseTimeLength(supplierAutoTakeDeliveryPauseTimeLength);
//		}
		if(null != platformCloseReason){
			refundOrder.setPlatformCloseReason(platformCloseReason);
		}
		//平台关闭时间
		if(refundStatus.getIntValue() == RefundStatus.ADMIN_CLOSE.getIntValue()){
			refundOrder.setPlatformInterveneCloseTime(System.currentTimeMillis());
		}
		//卖家拒绝时间
		if(refundStatus.getIntValue() == RefundStatus.SELLER_REFUSE.getIntValue()){
			refundOrder.setStoreRefuseRefundTime(System.currentTimeMillis());
		}
		//买家超时未发货时间
		if(refundStatus.getIntValue() == RefundStatus.CUSTOMER_OVERTIME_UNDELIVERY.getIntValue()){
			refundOrder.setCustomerOvertimeTimeNoDelivery(System.currentTimeMillis());
		}
		//买家撤销时间
		if(refundStatus.getIntValue() == RefundStatus.CUSTOMER_CLOSE.getIntValue()){
			refundOrder.setCustomerCancelTime(System.currentTimeMillis());
		}
		//买家撤销时间
        if(refundStatus.getIntValue() == RefundStatus.CUSTOMER_CLOSE_AFTER_AGREE.getIntValue()){
			refundOrder.setCustomerCancelTime(System.currentTimeMillis());
		}
		refundOrderMapper.updateById(refundOrder);

		//更改订单状态以及计算自动确认收货总暂停时长
		addAutoTakeGeliveryPauseTimeAndClearSign(orderNo);

		//添加操作日志
		refundOrderActionLogService.addActionLog(refundOrderActionLogEnum, refundOrderId);
	}
    @Override
	public List<RefundOrder> searchCustomerDeliveryRefundOrder() {
		//获取待买家发货，平台未介入或介入结束的售后订单
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>();
		List<Integer> list = new ArrayList<Integer>();
//		list.add(RefundOrder.PLATFORM_NOT_INTERVENE);
//		list.add(RefundOrder.CLOSE_CUSTOMER_PLATFORM_INTERVENE);
//		list.add(RefundOrder.CLOSE_SELLER_PLATFORM_INTERVENE);
		wrapper.eq("supplier_auto_take_delivery_pause_time", RefundOrder.NO_PAUSE)
		       .eq("refund_status", RefundStatus.SELLER_ACCEPT.getIntValue())
		       .eq("refund_type", RefundOrder.refundType_refund_and_product);
		List<RefundOrder> refundOrderList = refundOrderMapper.selectList(wrapper);
		return refundOrderList;
	}
    
    /**
     * 更改订单状态以及计算自动确认收货总暂停时长
     */
    public void addAutoTakeGeliveryPauseTimeAndClearSign(long orderNo){
    	StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
		StoreOrderNew storeOrderNew1 = new StoreOrderNew();
		//添加延长自动确认收货时间
		long currentTime = System.currentTimeMillis();
		long startTime = storeOrderNew.getRefundStartTime();
		long autoTakeGeliveryPauseTimeLength = currentTime-startTime;
		autoTakeGeliveryPauseTimeLength += storeOrderNew.getAutoTakeGeliveryPauseTimeLength();
		storeOrderNew1.setAutoTakeGeliveryPauseTimeLength(autoTakeGeliveryPauseTimeLength);//添加自动确认收货总暂停时长
		storeOrderNew1.setRefundStartTime(0L);//售后开始时间
		//清楚售后标志
	    storeOrderNew1.setRefundUnderway(StoreOrderNew.REFUND_NOT_UNDERWAY);//开启订单，清楚售后标志
		storeOrderNew1.setOrderNo(orderNo);
		supplierOrderMapper.updateById(storeOrderNew1);
    }
    

	

}
