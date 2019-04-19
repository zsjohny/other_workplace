package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.RefundStatus;
import com.jiuyuan.constant.MessageType;
import com.jiuyuan.dao.mapper.supplier.OrderItemNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderActionLogMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.RefundOrderActionLog;
import com.jiuyuan.entity.newentity.RefundOrderActionLogEnum;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.util.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 
 */
@Service
public class RefundOrderAdminFacade implements IRefundOrderAdminFacade {
	@Autowired
	private RefundOrderActionLogMapper refundOrderActionLogMapper;

	private static final Log logger = LogFactory.get(RefundOrderFacade.class);
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
	private ShopStoreAuthReasonService shopStoreAuthReasonService;

	@Autowired
	private IRefundOrderActionLogService refundOrderActionLogService;
	
	@Autowired
	private IStoreOrderNewService storeOrderNewService;

	@Autowired
	private RefundOrderService refundOrderService;
	
	@Autowired
	private IUserNewService userNewService;
	
	@Autowired
	private IExpressNewService supplierExpressService;
	
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;

	
	@Autowired
	private RefundSMSNotificationService refundSMSNotificationService;
	

	
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;

	@Autowired
	private IRefundOrderFacade refundOrderFacade;
	@Autowired
	private IRefundOrderFacadeNJ refundOrderFacadeNJ;
	
	/**
	 * 平台关闭售后单
	 */
	public void platformCloseRefundOrder(long refundOrderId,String platformCloseReason){
		RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
		if(null == refundOrder){
			logger.info("无售后订单无法关闭售后单！");
			throw new RuntimeException("无售后订单无法关闭售后单！");
		}
		Integer refundStatus = refundOrder.getRefundStatus();
		if(refundStatus == RefundStatus.CUSTOMER_CLOSE.getIntValue()&&
		   refundStatus == RefundStatus.REFUND_SUCCESS.getIntValue()&&
		   refundStatus == RefundStatus.SELLER_REFUSE.getIntValue()&&
		   refundStatus == RefundStatus.CUSTOMER_OVERTIME_UNDELIVERY.getIntValue()&&
		   refundStatus == RefundStatus.CUSTOMER_CLOSE.getIntValue()&&
		   refundStatus == RefundStatus.ADMIN_CLOSE.getIntValue()&&
		   refundStatus == RefundStatus.CUSTOMER_CLOSE_AFTER_AGREE.getIntValue()
		   ){
			logger.info("该售后订单已关闭或退款成功，请勿重复关闭售后订单！");
			throw new RuntimeException("该售后订单已关闭或退款成功，请勿重复关闭售后订单！");
		}
		//1、 修改售后订单状态等、修改订单详情的售后状态以及时间
		refundOrderFacadeNJ.updateOrderStatusWhenClose(refundOrderId,RefundStatus.ADMIN_CLOSE,refundOrder.getOrderNo(),RefundOrderActionLogEnum.M,platformCloseReason);
		
		//2、记录操作日志
		refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.M,refundOrderId);
		
//		3、 短信通知  【俞姐姐门店宝】经平台介入沟通，您提交的退货申请已被平台关闭。
		long storeId = refundOrder.getStoreId();
		UserNew userNew = userNewService.getSupplierUserInfo(refundOrder.getSupplierId());
		refundSMSNotificationService.sendSMSNotificationAndGEPush(null, MessageType.H.getIntValue(), storeId, userNew.getPhone());
	}

	/**
	 * 获取售后订单详情（新运营平台service）
	 * 
	 * @param storeId
	 * @param refundOrderId
	 * @return
	 */
	public Map<String, Object> getRefundOrderInfoAdmin(long refundOrderId) {
		RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
		if (refundOrder == null) {
			throw new RuntimeException("未找到该售后单");
		}
		Map<String, Object> refundOrderData = new HashMap<String, Object>();

		// 填充售后订单数据
		int refundStatus = refundOrder.getRefundStatus();
		refundOrderData.put("refundOrderId", refundOrder.getId());// 售后单ID
		refundOrderData.put("refundStatus", refundStatus);// 售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(买家主动关闭)、8、（平台客服主动关闭）
		refundOrderData.put("platformInterveneState", refundOrder.getPlatformInterveneState());// platformInterveneState 平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中
		refundOrderData.put("refundStatusName", refundOrderFacade.buildInfoRefundStatusName(refundStatus));// 售后订单状态名称
		refundOrderData.put("refundType", refundOrder.getRefundType());// 退款类型：1.仅退款// 2.退货退款
		refundOrderData.put("returnCount", refundOrder.getReturnCount());// 退款数量
		refundOrderData.put("refundCost", refundOrder.getRefundCost());// 退款金额
		
		refundOrderData.put("refundReason", refundOrder.getRefundReason());// 退款原因
		refundOrderData.put("refundRemark", refundOrder.getRefundRemark());// 退款说明
		refundOrderData.put("refundProofImages", refundOrder.getRefundProofImages());// 退款凭证
		refundOrderData.put("refundOrderNo", refundOrder.getRefundOrderNo());// 退款单编号
		refundOrderData.put("takeProductStateName", refundOrderFacade.buildTakeProductStateName(refundOrder));// 货物状态名称：已收到货、未收到货
		
		refundOrderData.put("customerExpressNo", refundOrder.getCustomerExpressNo());// 买家发货快递单号
		refundOrderData.put("customerExpressCompany", refundOrder.getCustomerExpressCompany());// 买家发货快递公司
		refundOrderData.put("customerExpressCompanyName", refundOrder.getCustomerExpressCompanyName());// 买家发货快递公司名称
		refundOrderData.put("brandName", refundOrder.getBrandName());// 订单品牌名称
		
		// 填充订单数据
		long orderNo = refundOrder.getOrderNo();
		refundOrderData.put("orderNo",orderNo );// 订单Id
		StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
		refundOrderData.put("totalBuyCount",storeOrderNew.getTotalBuyCount());//总购买件数
		double  totalExpressMoney = storeOrderNew.getTotalExpressMoney();
		double  totalPay = storeOrderNew.getTotalPay();
		double  practicalTotalPay = totalPay + totalExpressMoney;
		refundOrderData.put("practicalTotalPay",practicalTotalPay);//订单实付金额（含运费）
		refundOrderData.put("totalExpressMoney",totalExpressMoney);//运费金额

		// 填充订单明细
		refundOrderData.put("overtimeNoDeliverCloseReason", "在卖家同意后，买家3天内没有发货，导致售后单超时失效。");//超时失效未发货关闭原因
		refundOrderData.put("platformCloseReason", refundOrder.getPlatformCloseReason());//平台关闭关闭原因：这里显示由平台填写的关闭理由
		
		refundOrderData.put("storeRefuseReason", refundOrder.getStoreRefuseReason());// 卖家拒绝退款理由
		refundOrderData.put("storeAgreeRemark", refundOrder.getStoreAgreeRemark());// 卖家同意退款备注
		refundOrderData.put("handlingSuggestion", refundOrder.getHandlingSuggestion());//平台介入处理意见

		refundOrderData.put("surplusAffirmTime", refundOrderFacade.buildSurplusAffirmTime(refundOrder));// 剩余卖家确认时间毫秒数
		refundOrderData.put("surplusDeliverTIme", refundOrderFacade.buildSurplusDeliverTime(refundOrder));// 剩余买家发货时间毫秒数
		refundOrderData.put("surplusSupplierAutoTakeTime", refundOrderFacade.buildSurplusSupplierAutoTakeTime(refundOrder));//剩余卖家自动确认收货时间毫秒数
		
		// 填充售后订单操作日志
		List<RefundOrderActionLog> refundOrderActionLogList = refundOrderActionLogService.getRefundOrderActionLogList(refundOrderId);
		List<Map<String, String>> actionLogList = new ArrayList<Map<String, String>>();
		for (RefundOrderActionLog refundOrderActionLog : refundOrderActionLogList) {
			Map<String, String> actionLogMap = new HashMap<String, String>();
			actionLogMap.put("actionTime", DateUtil.parseLongTime2Str(refundOrderActionLog.getActionTime()));
			actionLogMap.put("actionName", refundOrderActionLog.getActionName());
			actionLogList.add(actionLogMap);
		}
		refundOrderData.put("actionLogList", actionLogList);
		return refundOrderData;
	}

	 /**
	 * 售后订单列表
	 */
	public List<Map<String, Object>> getRefundOrderList(Page<Map<String, Object>> page, String refundOrderNo,
			 String brandName, String receiver, int refundType, int refundStatus,
			long beginApplyTime, long endApplyTime, int beginReturnCount, int endReturnCount,
			double beginRefundCost, double endReturnCost, int platformInterveneState,long orderNo) {
		// TODO Auto-generated method stub
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>();
		if (StringUtils.isNotEmpty(refundOrderNo)) {
			wrapper.eq("refund_order_no", refundOrderNo);
		}
		if (StringUtils.isNotEmpty(brandName)) {
			wrapper.like("brand_name", brandName);
		}
		if (orderNo != -1) {
			wrapper.eq("order_no", orderNo);
		}
		if (StringUtils.isNotEmpty(receiver)) {
			wrapper.like("receiver", receiver);
		}
		if (refundStatus != -1) {
			if (refundStatus == 7) {
				wrapper.and("refund_status = 7 or refund_status=9");
			}else{
				wrapper.eq("refund_status", refundStatus);
			}
		}
		if (refundType != -1) {
			wrapper.eq("refund_type", refundType);
		}
		if (beginApplyTime != -1) {
			wrapper.ge("apply_time", beginApplyTime);
		}
		if (endApplyTime != -1) {
			wrapper.le("apply_time", endApplyTime);
		}
		if (beginReturnCount != -1) {
			wrapper.ge("return_count", beginReturnCount);
		}
		if (endReturnCount != -1) {
			wrapper.le("return_count", endReturnCount);
		}
		if (beginRefundCost != -1) {
			wrapper.ge("refund_cost", beginRefundCost);
		}
		if (endReturnCost != -1) {
			wrapper.le("refund_cost", endReturnCost);
		}
		if (platformInterveneState != -1) {
			wrapper.ge("platform_intervene_state", 1);//平台介入状态 >1的所有状态
		}
		wrapper.orderBy("apply_time",false);
		List<RefundOrder> selectList = refundOrderMapper.selectPage(page, wrapper);
		List<Map<String,Object>> refundOrderList = new ArrayList<Map<String,Object>>();
		for (RefundOrder refundOrder : selectList) {
			Map<String, Object> refundOrderMap = new HashMap<String, Object>();
			refundOrderMap.put("refundOrderId", refundOrder.getId());//售后订单ID
			refundOrderMap.put("refundOrderNo", refundOrder.getRefundOrderNo());//售后订单编号
			
			refundOrderMap.put("applyTime",DateUtil.format(refundOrder.getApplyTime(), "yyyy-MM-dd HH:mm:ss"));// 申请时间
			
			refundOrderMap.put("refundTypeName", refundOrder.buildRefundTypeName());//售后类型名称  退款类型：1.仅退款  2.退货退款
			
			refundOrderMap.put("returnCount", refundOrder.getReturnCount());//退款件数
			
			refundOrderMap.put("refundCost", refundOrder.getRefundCost());//退款金额
			
			Long supplierId = refundOrder.getSupplierId();//获得供应商ID
			
			UserNew supplierUserInfo = userNewService.getSupplierUserInfo(supplierId);//获得供应商信息
			
			refundOrderMap.put("supplierLinkMan",supplierUserInfo.getBusinessName());//供应商联系人
			
			Wrapper<RefundOrder> wrapper1 = new EntityWrapper<RefundOrder>().eq("order_no", refundOrder.getOrderNo());
				
			refundOrderMap.put("refundOrderCount",refundOrderMapper.selectCount(wrapper1));//售后单数量	
			
//			StoreBusiness storeBusiness = userNewService.getStoreBusinessByStoreId(refundOrder.getStoreId());//获得供应商注册信息
			
			refundOrderMap.put("supplierPhone",supplierUserInfo.getPhone());//供应商注册时的联系方式
			
			refundOrderMap.put("brandName",refundOrder.getBrandName());//品牌名
			
			refundOrderMap.put("orderNo",refundOrder.getOrderNo());//订单号
			
			StoreOrderNew storeOrderByOrderNo = storeOrderNewService.getStoreOrderByOrderNo(refundOrder.getOrderNo());//获取订单信息
			
			refundOrderMap.put("orderInfo",storeOrderByOrderNo.getExpressInfo());//收件人姓名，地址，电话
			
			refundOrderMap.put("storeName", refundOrder.getStoreName());//门店名称
			
			refundOrderMap.put("storePhone", refundOrder.getStorePhone());//门店手机号
			
			StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessById(refundOrder.getStoreId());
			String storeBusinessName = "";
			if(storeBusiness != null){
				storeBusinessName = storeBusiness.getLegalPerson();
			}else{
				logger.info("获取售后单列表时获取门店信息为空，请尽快排查问题，refundOrder.getStoreId()："+refundOrder.getStoreId());
			}
			refundOrderMap.put("storeBusinessName", storeBusinessName);//门店联系人姓名(法人)
			
			refundOrderMap.put("refundStatus", refundOrder.getRefundStatus());//售后状态
			
			refundOrderMap.put("platformInterveneState", refundOrder.getPlatformInterveneState());//平台接入状态
			
			refundOrderMap.put("platformCloseReason", refundOrder.getPlatformCloseReason());//关闭原因
			
			refundOrderMap.put("handlingSuggestion", refundOrder.getHandlingSuggestion());//平台介入处理意见 
			
			refundOrderList.add(refundOrderMap);
		}
		return refundOrderList;
	}
	
	/**
	 * 根据订单获取售后订单列表
	 * @param orderNo 订单ID
	 * @return
	 */
	public List<Map<String, Object>> getRefundOrderListByOrderNo(long orderNo){
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>().eq("order_no", orderNo);
		List<RefundOrder> selectList = refundOrderMapper.selectList(wrapper);
		List<Map<String,Object>> refundOrderList = new ArrayList<Map<String,Object>>();
		for (RefundOrder refundOrder : selectList) {
			Map<String, Object> refundOrderMap = new HashMap<String, Object>();
			refundOrderMap.put("refundOrderId", refundOrder.getId());//售后订单ID
			refundOrderMap.put("refundOrderNo", refundOrder.getRefundOrderNo());//售后订单编号
			refundOrderMap.put("refundTypeName", refundOrder.buildRefundTypeName());//售后类型名称  退款类型：1.仅退款  2.退货退款
			refundOrderList.add(refundOrderMap);
		}
		return refundOrderList;
	}
	

	
	/**
	 * 结束平台介入
	 * @param refundOrderId 售后订单ID
	 * @param handlingSuggestion 处理意见
	 * @return
	 */
	public void stopPlatformIntervene(long refundOrderId,String handlingSuggestion){
		
		RefundOrder refundOrderOld = refundOrderMapper.selectById(refundOrderId);
		if(null == refundOrderOld){
			logger.error("该售后订单不存在！");
			throw new RuntimeException("该售后订单不存在！");
		}
		long orderNo = refundOrderOld.getOrderNo();
		
		//1、修改平台介入状态
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setId(refundOrderId);
		refundOrder.setHandlingSuggestion(handlingSuggestion);
		//平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束
		int platformInterveneState = refundOrderOld.getPlatformInterveneState();
		logger.info("平台介入状态：platformInterveneState:"+platformInterveneState);
		if(platformInterveneState == RefundOrder.CUSTOMER_PLATFORM_INTERVENE){//1买家申请平台介入中
			refundOrder.setPlatformInterveneState(RefundOrder.CLOSE_CUSTOMER_PLATFORM_INTERVENE);
			//如果是卖家拒绝时买家申请平台介入时则需要修改订单售后状态和结算暂停订单自动确认售后时长 
			refundOrderFacadeNJ.addAutoTakeGeliveryPauseTimeAndClearSign(orderNo);//更改订单状态以及计算自动确认收货总暂停时长
		}else if(platformInterveneState == RefundOrder.SELLER_PLATFORM_INTERVENE){
			refundOrder.setPlatformInterveneState(RefundOrder.CLOSE_SELLER_PLATFORM_INTERVENE);
		}else{
			logger.info("平台介入状态不为介入中，不能结束platformInterveneState："+platformInterveneState);
			throw new RuntimeException("平台介入状态不为介入中，不能结束");
		}
		refundOrderMapper.updateById(refundOrder);
		
		//2、记录操作日志
		refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.K,refundOrderId);
		
		//3、结束卖家自动确认收货时间
		stopSupplierAutoTakeDeliveryPauseTime(refundOrderId);
	}
	

	/**
	 * 结束卖家自动确认收货时间
	 * @param refundOrderOld
	 */
	public void stopSupplierAutoTakeDeliveryPauseTime(long refundOrderId) {
		RefundOrder refundOrderOld = refundOrderMapper.selectById(refundOrderId);
		long supplierAutoTakeDeliveryPauseTime = refundOrderOld.getSupplierAutoTakeDeliveryPauseTime();//卖家自动确认收货暂停时间，为0是则表示未暂停，大于0表示已暂停
		long supplierAutoTakeDeliveryPauseTimeLength = refundOrderOld.getSupplierAutoTakeDeliveryPauseTimeLength();// 卖家自动确认收货总暂停时长（毫秒）
		if(supplierAutoTakeDeliveryPauseTime == 0){
			logger.info("出现错误尽快排查");
			
		}else{
			RefundOrder refundOrder = new RefundOrder();
			refundOrder.setId(refundOrderId);
			long time = System.currentTimeMillis();
			long pauseTime = time - supplierAutoTakeDeliveryPauseTime;//计算暂停时间
			refundOrder.setSupplierAutoTakeDeliveryPauseTimeLength(supplierAutoTakeDeliveryPauseTimeLength + pauseTime);
			refundOrder.setSupplierAutoTakeDeliveryPauseTime(0L);
			refundOrderMapper.updateById(refundOrder);
		}
	}
	/**
	 * 获取订单下未关闭的售后单
	 * @param orderNo
	 * @return
	 */
	public List<RefundOrder> getNotCloseRefundOrderListByOrderNo(Long orderNo) {
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>().eq("order_no", orderNo).notIn("refund_status", "5,6,7,8,9");//未关闭的售后单
		List<RefundOrder> selectList = refundOrderMapper.selectList(wrapper);
		return selectList;
	}

}