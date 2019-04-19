package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enums.StoreBillEnums;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.web.help.JsonResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.RefundStatus;
import com.jiuyuan.constant.MessageType;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.mapper.supplier.OrderItemNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderActionLogMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.NumberUtil;
import com.jiuyuan.util.SmallPage;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 
 */
@Service
public class RefundOrderFacade implements IRefundOrderFacade {
	
	private static final Log logger = LogFactory.get(RefundOrderFacade.class);
	
	
	private static final long FEB_TENTH = 1518192000000L;
	
	private static final long JAN_THIRTIETH = 1517241600000L;
	
	private static final long MAR_THIRD = 1520006400000L;

	@Autowired
	private RefundOrderActionLogMapper refundOrderActionLogMapper;

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
	private IBrandNewService brandNewService;
	
	@Autowired
	private RefundSMSNotificationService refundSMSNotificationService;
	
	@Autowired
	private RefundService refundService;
	@Autowired
	private StoreOrderItemNewService storeOrderItemNewService;
	
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;
	@Autowired
	private GroundBonusGrantService groundBonusGrantService;
	
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	

//	private static final int REFUND_REASON_TYPE = 2;
//
//	private static final int ONLY_REFUND = 1;
//	private static final int REFUND_WITH_DELIVERY = 2;
	
	
	private static final int ONLY_REFUND_REASON_TYPE = 3;
	private static final int REFUND_AND_PRODUCT_REASON_TYPE = 4;
	
	
	//售后订单供应商自动确认收货时间15天
	public static final long refundOrderSupplierAutoTakeTime = 15 * 24 * 60 * 60 * 1000;
	//售后订单买家发货限制时间
	public static final long refundOrderRestrictiveDeliverTime = 3 * 24 * 60 * 60 * 1000;
	//售后订单卖家限制确认时间
	public static final long refundOrderRestrictiveConfirmTime = 3 * 24 * 60 * 60 * 1000;
	
	/**
	 * 买家申请平台介入
	 * //		开发点：
//		1、更改订单售后状态（遍历所有该订单的售后单，是否全部处理，未售后或售后中，注意一起考虑平台介入状态）
//		1、	更改售后订单平台介入状态为“买家申请介入中”
//		2、	在售后订单表中记录买家申请平台介入时间
//		3、	更改订单中是否是售后进行中字段为“是”
//		4、	添加售后订单操作日志
	 * @param refundOrderId
	 * @param storeId
	 */
	@Override
	public void storeApplyPlatformIntervene(long refundOrderId, long storeId){
		RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
		long orderNo = refundOrder.getOrderNo();
		//1、校验、
		//1.1、当前订单是否已确认收货？提示：当前订单已确认收货了，不能再申请平台介入。
		StoreOrderNew order = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
		int orderStatus = order.getOrderStatus();//订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭
		if(orderStatus == OrderStatus.SUCCESS.getIntValue()){
			throw new RuntimeException("当前订单已确认收货了，不能再申请平台介入。");
		}
		//1.2、当前订单进行中的平台介入吗？提示：当前订单已申请平台介入，不能再申请。
		int platformInterveneState = refundOrder.getPlatformInterveneState();
		if(platformInterveneState == RefundOrder.CUSTOMER_PLATFORM_INTERVENE){
			throw new RuntimeException("当前订单已申请平台介入，不能再申请。");
		}
		
		//1.3、当前订单是否有进行中的售后单？提示：当前订单有进行中的售后，不能再申请平台介入
		List<RefundOrder> underWayRefundOrderList = refundOrderService.getUnderWayRefundOrderList(orderNo);
		if(underWayRefundOrderList.size() > 0){
			throw new RuntimeException("当前订单有进行中的售后，不能再申请平台介入。");
		}
		
		//1、设置订单为售后中
		
		storeOrderNewService.addRefundSign(orderNo);
		
		//2、更改售后订单平台介入状态为买家平台介入中
		refundOrderService.storeApplyPlatformIntervene(refundOrderId);
		
		//3、添加操作日志
		refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.I,refundOrderId);
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IRefundOrderFacade#applyRefund(java.lang.Long, java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Map<String, Object> applyRefund(Long orderNo, Long storeId, Integer refundType,Integer version) {
		Map<String, Object> map = new HashMap<String, Object>();
		StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
		long currentTime = System.currentTimeMillis();
		if(currentTime >= FEB_TENTH && currentTime <= FEB_TENTH){
			logger.info("目前工厂处于休假状态，平台暂停受理退款退货服务:orderNo:"+orderNo+",storeId:"+storeId);
			throw new RuntimeException("亲爱的客户：目前工厂处于休假状态，平台暂停受理退款退货服务，于2018年3月3日重新开始受理售后，为您带来的不便敬请谅解。");
		}

		// 填充商品数据
//		long productId = storeOrderItemNew.getProductId();
//		ProductNew product = productNewMapper.selectById(productId);
		Integer orderStatus = storeOrderNew.getOrderStatus();
		// 先进行判断是否订单
		if (orderStatus != OrderStatus.PAID.getIntValue() && orderStatus != OrderStatus.DELIVER.getIntValue()) {
			logger.info("该订单不在售后期限内,orderNo:" + orderNo + "orderStatus:" + orderStatus);
			throw new RuntimeException("该订单不在售后期限内，无法申请售后！");
		}
		RefundOrder reOrder = refundOrderService.getRefundOrderByOrderNoUnderWayOrSuccess(orderNo);
		//该订单是否在售后中
		if(storeOrderNew.getRefundUnderway() == StoreOrderNew.REFUND_UNDERWAY){
			if(reOrder == null){
				logger.error("当前订单已申请平台介入，不能再申请售后。");
				throw new RuntimeException("当前订单已申请平台介入，不能再申请售后。");
			}
			logger.error("当前订单已有进行中的售后单。不能再申请售后。");
			throw new RuntimeException("当前订单已有进行中的售后单。不能再申请售后。");
		}
		//再判断是否已经该订单是否已经在售后或者售后成功
		if(null != reOrder){
			int refundStatus = reOrder.getRefundStatus();
			if(refundStatus == RefundStatus.REFUND_SUCCESS.getIntValue()){
				logger.error("当前订单已有退款成功的售后单，不能再申请售后。");
				throw new RuntimeException("当前订单已有退款成功的售后单，不能再申请售后。");
			}
			if(refundStatus == RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue()||
			   refundStatus == RefundStatus.CUSTOMER_DELIVERY.getIntValue()||
			   refundStatus == RefundStatus.SELLER_ACCEPT.getIntValue()){
				logger.error("当前订单已有进行中的售后单。不能再申请售后。");
				throw new RuntimeException("当前订单已有进行中的售后单。不能再申请售后。");
			}
		}
		//判断该订单状态是否能够申请为退货退款
		if(refundType == RefundOrder.refundType_refund_and_product && orderStatus == OrderStatus.PAID.getIntValue()){
			logger.error("该订单无法申请退货退款！orderNo:"+orderNo+"orderStatus:"+orderStatus);
			throw new RuntimeException("该订单无法申请退货退款！");
		}

		double expressMoney = storeOrderNew.getTotalExpressMoney();
		//获取订单信息
		Map<String,Object> orderMap = new HashMap<String,Object>();
		orderMap.put("orderNo", storeOrderNew.getOrderNo());
		orderMap.put("totalExpressMoney",BizUtil.savepoint(expressMoney,2) );
		String orderNoStr = String.format("%09d", orderNo);
		orderMap.put("orderNoStr", orderNoStr);
		Map<String, Object> refuseMap = new HashMap<String,Object>();
		// 获取退款原因列表
		//
		if(refundType == RefundOrder.refundType_refund){
			refuseMap = shopStoreAuthReasonService.getRefuseReasonList(ONLY_REFUND_REASON_TYPE);
		}
		if(refundType == RefundOrder.refundType_refund_and_product){
			refuseMap = shopStoreAuthReasonService.getRefuseReasonList(REFUND_AND_PRODUCT_REASON_TYPE);
		}
		// 最大退款额为实付金额（不含邮费）
		Map<String, Object> refundMap = new HashMap<String, Object>();
		// 售后类型 1：仅退款 2：退货退款
		refundMap.put("refundType", refundType);
		// 最大退款金额
		double MostrefundFee = storeOrderNew.getTotalPay() ;

		// 老版本的已经付款未发货的情况需要将金额添加起来  已发货的情况不需要相加..
		if(storeOrderNew.getOrderStatus()==OrderStatus.PAID.getIntValue()) {
			if(version<BizUtil.VERSION372) {
				MostrefundFee +=  expressMoney;
			}
		}

		refundMap.put("MostrefundFee", BizUtil.savepoint(MostrefundFee,2));
		map.putAll(orderMap);
		map.putAll(refuseMap);
		map.putAll(refundMap);
		return map;
	}

	
	/**
	 * 获取售后订单详情（app端service）
	 * 
	 * @param
	 * @param refundOrderId
	 * @return
	 */
	@Override
	public Map<String, Object> getRefundOrderInfo(long refundOrderId){
	
		RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
		if (refundOrder == null) {
			throw new RuntimeException("未找到该售后单");
		}
		Map<String, Object> refundOrderData = new HashMap<String, Object>();

		// 填充售后订单数据
		int refundStatus = refundOrder.getRefundStatus();
		refundOrderData.put("refundOrderId", refundOrder.getId());// 售后单ID
		refundOrderData.put("refundOrderNo", refundOrder.getRefundOrderNo());//售后单编号
		refundOrderData.put("refundStatus", refundStatus);// 售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(买家主动关闭)、8、（平台客服主动关闭）
		refundOrderData.put("platformInterveneState", refundOrder.getPlatformInterveneState());// platformInterveneState 平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中
		refundOrderData.put("refundStatusName", buildInfoRefundStatusName(refundStatus));// 售后订单状态名称
		refundOrderData.put("refundType", refundOrder.getRefundType());// 退款类型：1.仅退款
																		// 2.退货退款
		refundOrderData.put("returnCount", refundOrder.getReturnCount());// 退款数量
		refundOrderData.put("refundCost", refundOrder.getRefundCost());// 退款金额
		
		refundOrderData.put("refundReason", refundOrder.getRefundReason());// 退款原因
		refundOrderData.put("refundRemark", refundOrder.getRefundRemark());// 退款说明
		refundOrderData.put("refundProofImages", refundOrder.getRefundProofImages());// 退款凭证
		
		refundOrderData.put("takeProductStateName", buildTakeProductStateName(refundOrder));// 货物状态名称：已收到货、未收到货
		// 填充订单数据
		long orderNo = refundOrder.getOrderNo();
		refundOrderData.put("orderNo",orderNo );// 订单Id
		refundOrderData.put("brandName", refundOrder.getBrandName());// 订单品牌名称
		StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
		refundOrderData.put("orderStatus",storeOrderNew.getOrderStatus());//订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭
		double  totalExpressMoney = storeOrderNew.getTotalExpressMoney();
		double  totalPay = storeOrderNew.getTotalPay();
		double  practicalTotalPay = totalPay + totalExpressMoney;
		
		refundOrderData.put("totalBuyCount",storeOrderNew.getTotalBuyCount());//订单实付金额（含运费）
		refundOrderData.put("practicalTotalPay",practicalTotalPay);//订单实付金额（含运费）
		refundOrderData.put("totalExpressMoney",totalExpressMoney);//运费金额
				

		// 填充订单明细
		refundOrderData.put("storeRefuseReason", refundOrder.getStoreRefuseReason());// 卖家拒绝退款理由
		refundOrderData.put("storeAgreeRemark", refundOrder.getStoreAgreeRemark());// 卖家同意退款备注   对应APP端  卖家备注：这里显示卖家同意时的备注
		refundOrderData.put("handlingSuggestion", refundOrder.getHandlingSuggestion());//平台介入处理意见
		refundOrderData.put("refundOrderCloseReason", buildRefundOrderCloseReason(refundOrder));
		// 售后订单关闭原因：买家主动关闭售后单、买家超时未发货、平台关闭填写的关闭理由
		
		refundOrderData.put("overtimeNoDeliverCloseReason", "在卖家同意后，买家3天内没有发货，导致售后单超时失效。");//超时失效未发货关闭原因
		refundOrderData.put("platformCloseReason", refundOrder.getPlatformCloseReason());//平台关闭关闭原因：这里显示由平台填写的关闭理由

		refundOrderData.put("surplusAffirmTime", buildSurplusAffirmTime(refundOrder));//剩余卖家确认时间毫秒数
		refundOrderData.put("surplusDeliverTime", buildSurplusDeliverTime(refundOrder));//剩余买家发货时间毫秒数
		refundOrderData.put("surplusSupplierAutoTakeTime", buildSurplusSupplierAutoTakeTime(refundOrder));//剩余卖家自动确认收货时间毫秒数

		
		refundOrderData.put("customerExpressNo", refundOrder.getCustomerExpressNo());// 买家发货快递单号
		refundOrderData.put("customerExpressCompany", refundOrder.getCustomerExpressCompany());// 买家发货快递公司
		refundOrderData.put("customerExpressCompanyName", refundOrder.getCustomerExpressCompanyName());// 买家发货快递公司名称
		//售后收货地址
		refundOrderData.put("receiveAddress", refundOrder.getSupplierReceiveAddress());// 收货地址
		refundOrderData.put("receiver", refundOrder.getReceiver());// 收货人
		refundOrderData.put("receiverPhone", refundOrder.getReceiverPhone());// 收货人联系电话
		
		
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
	 * 剩余卖家自动确认收货时间毫秒数
	 * @param refundOrder
	 * @return
	 */
	@Override
	public long buildSurplusSupplierAutoTakeTime(RefundOrder refundOrder) {
		
		long customerReturnTime = refundOrder.getCustomerReturnTime();//买家发货时间
		if(customerReturnTime == 0 ){//未发货则返回0
			return 0;
		}
		
		long supplierAutoTakeDeliveryPauseTime = refundOrder.getSupplierAutoTakeDeliveryPauseTime();//卖家自动确认收货暂停时间，为0是则表示未暂停，大于0表示已暂停
		long supplierAutoTakeDeliveryPauseTimeLength = refundOrder.getSupplierAutoTakeDeliveryPauseTimeLength();//卖家自动确认收货总暂停时长（毫秒）
		long time = 0;//
		if(supplierAutoTakeDeliveryPauseTime == 0){//未暂停
			time = System.currentTimeMillis();//当前时间
		}else{//卖家申请平台介入售后订单暂停
			time = supplierAutoTakeDeliveryPauseTime;//暂停时间
		}
		
		long supplierAutoTakeTime = customerReturnTime + refundOrderSupplierAutoTakeTime + supplierAutoTakeDeliveryPauseTimeLength;//自动确认收货时间节点 
		long surplusSupplierAutoTakeTime = 0;//剩余卖家确认时间
		surplusSupplierAutoTakeTime = supplierAutoTakeTime - time; //剩余自动确认收货时间  =  自动确认收货时间节点  - 当前时间或暂停时间
		if(surplusSupplierAutoTakeTime < 0){
			surplusSupplierAutoTakeTime = 0;
		}
		return surplusSupplierAutoTakeTime;
		
	}

	/**
	 * app售后
	 * @param refundOrderId
	 * @return
	 */
	@Override
    public JsonResponse dealMoney(Long refundOrderId) {
		try {
			JsonResponse response=new JsonResponse();
			RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
			if(null == refundOrder){
				logger.error("该售后订单不存在！");
				throw new RuntimeException("该售后订单不存在！");
			}
			Long orderNo = refundOrder.getOrderNo();
			StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
			deal( storeOrderNew, refundOrder, refundOrderId);
			return response.setSuccessful();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException("退款失败");
		}
	}
	public JsonResponse deal(StoreOrderNew storeOrderNew,RefundOrder refundOrder,Long refundOrderId) {
		JsonResponse response=new JsonResponse();
		try {
			if (storeOrderNew.getPaymentType()==2){//支付宝退款
				refundService.alipayRefund(storeOrderNew, String.valueOf(refundOrder.getRealBackMoney()), refundOrder.getRefundReason(),String.valueOf(refundOrderId));
			}else if (storeOrderNew.getPaymentType()==3){//微信退款
				refundService.weixinRefund(storeOrderNew, String.valueOf(refundOrder.getRealBackMoney()), refundOrder.getRefundReason(), true,
						String.valueOf(refundOrderId));
			}
			return response.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return response.setError("退款失败");
		}
	}

	/**
	 * 剩余买家发货时间毫秒数
	 * 
	 * @param refundOrder
	 * @return
	 */
	@Override
	public long buildSurplusDeliverTime(RefundOrder refundOrder) {
	
		long storeAllowRefundTime = refundOrder.getStoreAllowRefundTime();//卖家同意时间
		if(storeAllowRefundTime == 0 ){
			return 0;
		}
		long deliverTime = storeAllowRefundTime + refundOrderRestrictiveDeliverTime;//结束发货时间
		long time = System.currentTimeMillis();//当前时间
		long surplusDeliverTime = 0;//剩余卖家确认时间
		surplusDeliverTime = deliverTime - time;
		if(surplusDeliverTime < 0){
			surplusDeliverTime = 0;
		}
		return surplusDeliverTime;
	}

	/**
	 * 剩余卖家确认时间毫秒数
	 * 
	 * @param refundOrder
	 * @return
	 */
	@Override
	public long buildSurplusAffirmTime(RefundOrder refundOrder) {
		
		long applyTime = refundOrder.getApplyTime();//申请售后时间 
		if(applyTime == 0 ){
			return 0;
		}
		long endAffirmTime = applyTime + refundOrderRestrictiveConfirmTime;//结束确认时间
		long time = System.currentTimeMillis();//当前时间
		long surplusAffirmTime = 0;//剩余卖家确认时间
		surplusAffirmTime = endAffirmTime - time;
		if(surplusAffirmTime < 0){
			surplusAffirmTime = 0;
		}
		return surplusAffirmTime;
	}

	/**
	 * 货物状态
	 */
	@Override
	public String buildTakeProductStateName(RefundOrder refundOrder) {
		String takeProductState = "";
		int refundType = refundOrder.getRefundType();
		if (refundType == RefundOrder.refundType_refund_and_product) {// 2.退货退款
			takeProductState = "已收到货";
		}
		return takeProductState;
	}

	/**
	 * 根据列表售后订单状态返回显示名称
	 * 
	 * @param refundStatus
	 * @return
	 */
	@Override
	public String buildInfoRefundStatusName(int refundStatus) {
		//  售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
		String refundStatusName = "";
		if (refundStatus == 1) {// 1(待卖家确认、默认)、
			refundStatusName = "待卖家确认";
		} else if (refundStatus == 2) {// 2（待买家退货）
			refundStatusName = "卖家已同意，待买家退货";
		} else if (refundStatus == 3) {// 、3（待卖家确认收货）、
			refundStatusName = "买家已发货，待卖家退款";
		} else if (refundStatus == 4) {// 4(退款成功)、
			refundStatusName = "退款成功";
		} else if (refundStatus == 5) {// 5(卖家拒绝售后关闭)、
			refundStatusName = "卖家拒绝";
		} else if (refundStatus == 6) {// 6（买家超时未发货自动关闭）、
			refundStatusName = "已失效";
		} else if (refundStatus == 7) {// 7(卖家同意前买家主动关闭)、
			refundStatusName = "已撤销";
		} else if (refundStatus == 8) {// 8（平台客服主动关闭）
			refundStatusName = "平台关闭";
		} else if (refundStatus == 9) {// 9（卖家同意后买家主动关闭）
			refundStatusName = "已撤销";
		} else {
			logger.info("未知售后订单状态,请尽快处理");
			throw new RuntimeException("未知售后订单状态");
		}
		return refundStatusName;
	}

	/* (non-Javadoc)new Page<RefundOrder>(current,size)
	 * @see com.jiuyuan.service.common.IRefundOrderFacade#getRefundOrderList(long, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public SmallPage getRefundOrderList(long storeId, int current,int size) {
		Page page = new Page<RefundOrder>(current,size);
		
		// 1、查询数据
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>().eq("store_id", storeId);
		wrapper.orderBy("apply_time", false);
		List<RefundOrder> refundOrderList = refundOrderMapper.selectPage(page, wrapper);
		// 2、组装数据
		List<Map<String, Object>> refundOrderListDataList = new ArrayList<Map<String, Object>>();
		for (RefundOrder refundOrder : refundOrderList) {
			Map<String, Object> refundOrderData = new HashMap<String, Object>();
			//填充售后订单数据
			refundOrderData.put("refundOrderId", refundOrder.getId());// 售后单ID
			int refundStatus = refundOrder.getRefundStatus();
			refundOrderData.put("refundStatus", refundStatus);// 售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(买家主动关闭)、8、（平台客服主动关闭）
			refundOrderData.put("refundStatusName", buildListRefundStatusName(refundStatus));// 售后订单状态名称
			refundOrderData.put("refundType", refundOrder.buildRefundTypeName());// 退款类型：1.仅退款2.退货退款
			refundOrderData.put("refundCost", refundOrder.getRefundCost());//退款金额
			refundOrderData.put("brandName", refundOrder.getBrandName());//品牌名称
			refundOrderData.put("applyTime",DateUtil.parseLongTime2Str(refundOrder.getApplyTime()));//申请售后时间
			long orderNo = refundOrder.getOrderNo();
			refundOrderData.put("orderNo",orderNo );// 订单Id
			StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
			refundOrderData.put("totalBuyCount",storeOrderNew.getTotalBuyCount());//总购买件数数
			double  totalExpressMoney = storeOrderNew.getTotalExpressMoney();
			double  totalPay = storeOrderNew.getTotalPay();
			double  practicalTotalPay = totalPay + totalExpressMoney;
			refundOrderData.put("practicalTotalPay",practicalTotalPay);//订单实付金额（含运费）
			refundOrderData.put("totalExpressMoney",totalExpressMoney);//运费金额
			
			refundOrderListDataList.add(refundOrderData);
		}
		SmallPage smallPage = new SmallPage(page);
		smallPage.setRecords(refundOrderListDataList);
		return smallPage;
	}

	/**
	 * 获取关闭理由
	 * 
	 * @param refundOrder
	 * @return
	 */
	@Override
	public String buildRefundOrderCloseReason(RefundOrder refundOrder) {
		int refundStatus = refundOrder.getRefundStatus();
		String refundOrderCloseReason = "";
		if (refundStatus == 6) {// 6（买家超时未发货自动关闭）、
			refundOrderCloseReason = "买家超时未发货";
		} else if (refundStatus == 7) {// 7(买家主动关闭)、
			refundOrderCloseReason = "买家主动关闭售后单";
		} else if (refundStatus == 8) {// 8（平台客服主动关闭）
			refundOrderCloseReason = refundOrder.getPlatformCloseReason();
		} else if (refundStatus == 9) {// 7(买家主动关闭)、
			refundOrderCloseReason = "买家主动关闭售后单";
		}

		return refundOrderCloseReason;
	}

	/**
	 * 根据列表售后订单状态返回显示名称
	 * 
	 * @param refundStatus
	 * @return
	 */
	private String buildListRefundStatusName(int refundStatus) {
		//  售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
		String refundStatusName = "";
		if (refundStatus == 1) {// 1(待卖家确认、默认)、
			refundStatusName = "待卖家确认";
		} else if (refundStatus == 2) {// 2（待买家发货）
			refundStatusName = "卖家同意";
		} else if (refundStatus == 3) {// 、3（待卖家确认收货）、
			refundStatusName = "待卖家收货";
		} else if (refundStatus == 4) {// 4(退款成功)、
			refundStatusName = "已退款";
		} else if (refundStatus == 5) {// 5(卖家拒绝售后关闭)、
			refundStatusName = "卖家拒绝";
		} else if (refundStatus == 6) {// 6（买家超时未发货自动关闭）、
			refundStatusName = "已失效";
		} else if (refundStatus == 7) {// 7(卖家同意前买家主动关闭)、
			refundStatusName = "已撤销";
		} else if (refundStatus == 8) {// 8（平台客服主动关闭）
			refundStatusName = "平台关闭";
		} else if (refundStatus == 9) {// 9(卖家同意后买家主动关闭)、
			refundStatusName = "已撤销";
		} else {
			logger.info("未知售后订单状态,请尽快处理");
			throw new RuntimeException("未知售后订单状态");
		}
		return refundStatusName;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IRefundOrderFacade#submitRefundOrder(java.lang.Integer, java.lang.String, java.lang.Long, java.lang.Long, java.lang.Double, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public  Map<String,Object> submitRefundOrder(Integer refundType, String refundReason, Long refundReasonId, Long orderNo,
			Double refundCost, String refundRemark, String refundProofImages, Long storeId,String storeName,String phone,Integer version) {

		long currentTime =System.currentTimeMillis();
		logger.info(String.valueOf(currentTime));
		StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
		// 不能退款别人的订单..
		if(storeOrderNew.getStoreId().longValue() !=storeId){
			throw new RuntimeException("订单号码错误");
		}
		//校验参数
		checkParam(refundCost, refundType);

		//校验业务逻辑
		long supplierId  = checkBusiness372(orderNo,refundCost,refundType, storeOrderNew);
		/*if(version>= BizUtil.VERSION) {
			supplierId = checkBusiness372(orderNo,refundCost,refundType, storeOrderNew);
		} */

		UserNew userNew = userNewService.getSupplierUserInfo(supplierId);

		// 开始生成售后订单
		RefundOrder refundOrder = new RefundOrder();
		// 售后订单编号
		refundOrder.setRefundOrderNo(NumberUtil.genOrderNo(3));
		refundOrder.setStoreId(storeId);
		refundOrder.setOrderNo(orderNo);
		refundOrder.setBrandId(userNew.getBrandId());
		BrandNew brandNew = brandNewService.getBrandByBrandId(userNew.getBrandId());
		refundOrder.setBrandName(brandNew.getBrandName());
		refundOrder.setRefundType(refundType);
		refundOrder.setRefundStatus(RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue());
		// 退款金额
		refundOrder.setRefundCost(refundCost);
		refundOrder.setReturnCount(storeOrderNew.getTotalBuyCount());
		refundOrder.setRefundReason(refundReason);
		refundOrder.setRefundRemark(refundRemark);
		refundOrder.setRefundProofImages(refundProofImages);
		refundOrder.setRefundWay(storeOrderNew.getPaymentType());
		refundOrder.setApplyTime(System.currentTimeMillis());
		refundOrder.setSupplierId(storeOrderNew.getSupplierId());
		refundOrder.setRefundReasonId(refundReasonId);
		refundOrder.setPlatformInterveneState(RefundOrder.PLATFORM_NOT_INTERVENE);
		refundOrder.setStoreName(storeName);
		refundOrder.setStorePhone(phone);
		int i = refundOrderMapper.insert(refundOrder);
		if (i == -1) {
			logger.error("售后订单生成失败！");
			throw new RuntimeException("售后订单生成失败！");
		}
		// 添加订单售后标志以及添加售后开始时间时间戳
		storeOrderNewService.addRefundSign(orderNo);

		//  添加短信通知
		refundSMSNotificationService.sendSMSNotificationAndGEPush(null, MessageType.A.getIntValue(), storeId, userNew.getPhone());
		// 添加售后订单操作日志
		RefundOrder order = refundOrderService.getRefundOrderByOrderNoUnderWayOrSuccess(orderNo);
		if (order == null) {
			logger.error("未找到售后订单，请排查问题！");
			throw new RuntimeException("售后订单生成失败！");
		}
		refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.A,order.getId());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("refundOrderId", order.getId());
		return map;

	}
	/**
	 * 校验业务逻辑
	 * @param storeOrderNew 
	 */
	private long checkBusiness(Long orderNo, Double refundCost, Integer refundType, StoreOrderNew storeOrderNew) {
		// 获取该门店用户的订单
		// 填充商品数据
//				long productId = storeOrderItemNew.getProductId();
//				ProductNew product = productNewMapper.selectById(productId);
			Integer orderStatus = storeOrderNew.getOrderStatus();
			// 先进行判断是否订单
			if (orderStatus != OrderStatus.PAID.getIntValue() && orderStatus != OrderStatus.DELIVER.getIntValue()) {
				logger.info("该订单不在售后期限内,orderNo:" + orderNo + "orderStatus:" + orderStatus);
				throw new RuntimeException("该订单不在售后期限内，无法申请售后！");
			}
			RefundOrder reOrder = refundOrderService.getRefundOrderByOrderNoUnderWayOrSuccess(orderNo);
			//该订单是否在售后中
			if(storeOrderNew.getRefundUnderway() == StoreOrderNew.REFUND_UNDERWAY){
				if(reOrder == null){
					logger.error("当前订单已申请平台介入，不能再申请售后。");
					throw new RuntimeException("当前订单已申请平台介入，不能再申请售后。");
				}
				logger.error("当前订单已有进行中的售后单，不能再申请。");
				throw new RuntimeException("当前订单已有进行中的售后单，不能再申请。");
			}
			//再判断是否已经该订单是否已经在售后或者售后成功
			if(null != reOrder){
				int refundStatus = reOrder.getRefundStatus();
				if(refundStatus == RefundStatus.REFUND_SUCCESS.getIntValue()){
					logger.error("当前订单已有退款成功的售后单，不能再申请。");
					throw new RuntimeException("当前订单已有退款成功的售后单，不能再申请。");
				}
				if(refundStatus == RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue()||
						refundStatus == RefundStatus.CUSTOMER_DELIVERY.getIntValue()||
						refundStatus == RefundStatus.SELLER_ACCEPT.getIntValue()){
					logger.error("当前订单已有进行中的售后单，不能再申请。");
					throw new RuntimeException("当前订单已有进行中的售后单，不能再申请。");
				}
			}
			//判断该订单状态是否能够申请为退货退款
			if(refundType == RefundOrder.refundType_refund_and_product && orderStatus == OrderStatus.PAID.getIntValue()){
				logger.error("该订单无法申请退货退款！orderNo:"+orderNo+"orderStatus:"+orderStatus);
				throw new RuntimeException("该订单无法申请退货退款！");
			}
			//检测退款金额是否小于实付金额
			double totalPay = storeOrderNew.getTotalPay();
			if(refundCost > totalPay){
				logger.error("该订单的退款金额大于最大退款金额！无法提交售后！");
				throw new RuntimeException("该订单的退款金额超过最大退款金额！");
			}
			//判断在已付款状态下，仅退款是否是全额退款
			if(orderStatus == OrderStatus.PAID.getIntValue() &&  refundCost != totalPay){
				logger.error("在已付款的状态下，订单仅退款不能退部分金额！orderNo:"+orderNo);
				throw new RuntimeException("该订单只能全额退款");
			}
			return storeOrderNew.getSupplierId();
		
	}


	/**
	 * 校验业务逻辑
	 * @param storeOrderNew
	 */
	private long checkBusiness372(Long orderNo, Double refundCost, Integer refundType, StoreOrderNew storeOrderNew) {
		// 获取该门店用户的订单
		// 填充商品数据
//				long productId = storeOrderItemNew.getProductId();
//				ProductNew product = productNewMapper.selectById(productId);
		Integer orderStatus = storeOrderNew.getOrderStatus();
		// 先进行判断是否订单
		if (orderStatus != OrderStatus.PAID.getIntValue() && orderStatus != OrderStatus.DELIVER.getIntValue()) {
			logger.info("该订单不在售后期限内,orderNo:" + orderNo + "orderStatus:" + orderStatus);
			throw new RuntimeException("该订单不在售后期限内，无法申请售后！");
		}
		RefundOrder reOrder = refundOrderService.getRefundOrderByOrderNoUnderWayOrSuccess(orderNo);
		//该订单是否在售后中
		if(storeOrderNew.getRefundUnderway() == StoreOrderNew.REFUND_UNDERWAY){
			if(reOrder == null){
				logger.error("当前订单已申请平台介入，不能再申请售后。");
				throw new RuntimeException("当前订单已申请平台介入，不能再申请售后。");
			}
			logger.error("当前订单已有进行中的售后单，不能再申请。");
			throw new RuntimeException("当前订单已有进行中的售后单，不能再申请。");
		}
		//再判断是否已经该订单是否已经在售后或者售后成功
		if(null != reOrder){
			int refundStatus = reOrder.getRefundStatus();
			if(refundStatus == RefundStatus.REFUND_SUCCESS.getIntValue()){
				logger.error("当前订单已有退款成功的售后单，不能再申请。");
				throw new RuntimeException("当前订单已有退款成功的售后单，不能再申请。");
			}
			if(refundStatus == RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue()||
					refundStatus == RefundStatus.CUSTOMER_DELIVERY.getIntValue()||
					refundStatus == RefundStatus.SELLER_ACCEPT.getIntValue()){
				logger.error("当前订单已有进行中的售后单，不能再申请。");
				throw new RuntimeException("当前订单已有进行中的售后单，不能再申请。");
			}
		}
		//判断该订单状态是否能够申请为退货退款
		if(refundType == RefundOrder.refundType_refund_and_product && orderStatus == OrderStatus.PAID.getIntValue()){
			logger.error("该订单无法申请退货退款！orderNo:"+orderNo+"orderStatus:"+orderStatus);
			throw new RuntimeException("该订单无法申请退货退款！");
		}
		//检测退款金额是否小于实付金额
		double totalPay = storeOrderNew.getTotalPay();

		// 已经发货了 则不能退运费 最大退款金额 totalPay
		if(orderStatus == OrderStatus.DELIVER.getIntValue()){
			if(refundCost > totalPay){
				logger.error("该订单的退款金额大于最大退款金额！无法提交售后！");
				throw new RuntimeException("该订单的退款金额超过最大退款金额！");
			}
		}

		Double express = storeOrderNew.getTotalExpressMoney();
		BigDecimal totalPayBig = new BigDecimal(totalPay);
		BigDecimal expressBig= new BigDecimal(express);
        BigDecimal all = totalPayBig.add(expressBig).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal refundCostBig = new BigDecimal(refundCost).setScale(2,BigDecimal.ROUND_HALF_UP);

		//如果是已支付的状态 则最大退款金额要加上运费
		if(orderStatus == OrderStatus.PAID.getIntValue()){
			// 如果不是未发货的状态 则最大退款金额为实际支付金额+运费
            if(refundCostBig.compareTo(all)>0) {
                logger.error("该订单的退款金额大于最大退款金额！无法提交售后！");
                throw new RuntimeException("该订单的退款金额超过最大退款金额！");
            }
		}

		//判断在已付款状态下，仅退款是否是全额退款
		if(orderStatus == OrderStatus.PAID.getIntValue() && refundCostBig.compareTo(all) !=0){
			logger.error("在已付款的状态下，订单仅退款不能退部分金额！orderNo:"+orderNo);
			throw new RuntimeException("该订单只能全额退款");
		}
		return storeOrderNew.getSupplierId();

	}


	/**
	 * 校验参数
	 */
    private void checkParam(Double refundCost, Integer refundType) {
    	//退款金额
		if(refundCost == 0){
			logger.info("退款金额不能为0!");
			throw new RuntimeException("退款金额不能为0!");
		}
		//refundType
		if(refundType != RefundOrder.refundType_refund &&
		   refundType != RefundOrder.refundType_refund_and_product	){
			logger.info("退款方式未知！");
			throw new RuntimeException("退款方式未知！");
		}
		
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IRefundOrderFacade#customerDelivery(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Map<String, Object> customerDelivery(Long orderNo, Long storeId) {
		// 获取该门店用户的订单
		StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
		RefundOrder reOrder = refundOrderService.getRefundOrderByOrderNoUnderWayOrSuccess(orderNo);
//		if(null == reOrder ||reOrder.getRefundStatus() != RefundStatus.SELLER_ACCEPT.getIntValue()&&reOrder.getRefundStatus() != RefundStatus.CUSTOMER_DELIVERY.getIntValue()){
//			logger.info("该售后订单不存在或该售后订单状态不处于在待买家发货状态");
//			throw new RuntimeException("该售后订单不存在或该售后订单状态不处于在待买家发货状态");
//		}
		Map<String,Object> map = new HashMap<String,Object>();
		//获取卖家收货信息
//		UserNew userNew = userNewService.getSupplierUserInfo(storeOrderNew.getSupplierId());
//		map.put("receiver", userNew.getReceiver());//收货人
//		map.put("receiverPhone", userNew.getReceiverPhone());//收货人手机号
//		map.put("supplierReceiveAddress", userNew.getSupplierReceiveAddress());//供应商收货地址
		
		map.put("receiver", reOrder.getReceiver());//收货人
		map.put("receiverPhone", reOrder.getReceiverPhone());//收货人手机号
		map.put("supplierReceiveAddress", reOrder.getSupplierReceiveAddress());//供应商收货地址
		//获取邮寄公司名称列表
		List<Map<String,Object>> allExpressCompanys = supplierExpressService.getAllExpressCompanyNames();
		map.put("allExpressCompanys", allExpressCompanys);//邮寄公司名称列表
		return map;
	}

	/* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IRefundOrderFacade#submitDelivery(java.lang.String, java.lang.Long, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	@Override
	public void submitDelivery(String expressNo, Long expressSupplierId, String expressSupplier, Long storeId, Long refundOrderId, String expressSupplierCNName) {
		if(expressNo.equals("")||expressSupplier.equals("")||expressSupplierId == 0){
			logger.info("快递单号不能为空！");
			throw new RuntimeException("快递单号不能为空！refundOrderId："+refundOrderId);
		}
		RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);

		if(!refundOrder.getStoreId().equals(storeId) ){
			logger.info("该售后订单不属于该商家！");
			throw new RuntimeException("该售后订单不属于该商家！");
		}
		//保存买家发货信息
		refundOrderService.updateExpressInfo(expressNo,expressSupplier,refundOrderId, expressSupplierCNName);//更新买家
		//短信通知通知卖家
		UserNew userNew = userNewService.getSupplierUserInfo(refundOrder.getSupplierId());
		refundSMSNotificationService.sendSMSNotificationAndGEPush(null, MessageType.D.getIntValue(), storeId, userNew.getPhone());
		//记录操作日志,买家发货
		refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.E,refundOrderId);
		
	}
	
//	/**
//	 * 更改订单售后状态（遍历所有该订单的售后单，是否全部处理，未售后或售后中，注意一起考虑平台介入状态）
//	 * @param orderNo
//	 * @param refundOrderId
//	 * @return boolean 表示是否成功更改售後狀態
//	 */
//		public boolean updateStoreOrderStatusWithRefundStatus(long orderNo,long refundOrderId){
//			boolean flag = true;
//			Wrapper<RefundOrder> allRefundOrderWrapper = 
//					new EntityWrapper<RefundOrder>().eq("order_no",orderNo);
//			List<RefundOrder> allRefundOrderList = refundOrderMapper.selectList(allRefundOrderWrapper);
//			for (RefundOrder refundOrderEach : allRefundOrderList) {
//				//遍历所有该订单的售后单，是否全部处理，未售后或售后中，注意一起考虑平台介入状态
//				if(refundOrderEach.getPlatformInterveneState()!=0 || refundOrderEach.getRefundStatus()<4){
//					flag = false;
//					break;
//				}
//			}
//			//更改订单售后状态
//			if(flag){
//				StoreOrderNew storeOrderNewUpdate = new StoreOrderNew();
//				storeOrderNewUpdate.setOrderNo(orderNo);
//				//设置为不在售后中
//				storeOrderNewUpdate.setRefundUnderway(0);
//				//TODO (3)统计订单暂停自动确认收货时长维护到订单表对应字段中（拒绝时间-申请时间）
//				refundOrderService.settlementAutoDeliveryPauseTime(refundOrderId);
//				supplierOrderMapper.updateById(storeOrderNewUpdate);
//			}
//			return flag;
//		}
	
	/**
	 * 退款成功一系列操作
	 */
	@Override
	public void refundSuccessOperation(Long refundOrderId){
		//获取售后订单信息
		RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
		if(null == refundOrder){
			logger.error("该售后订单不存在！");
			throw new RuntimeException("该售后订单不存在！");
		}
		Long orderNo = refundOrder.getOrderNo();
		StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
		int orderStatus = storeOrderNew.getOrderStatus();
		if(orderStatus != OrderStatus.PAID.getIntValue()&& orderStatus != OrderStatus.DELIVER.getIntValue()){
			logger.error("该订单不在已付款或者已发货状态，无法进行售后退款！订单号："+orderNo);
			throw new RuntimeException("该订单不能进行售后，无法进行售后退款！订单号："+orderNo);
		}
		double refundCost = refundOrder.getRefundCost();
		double totalPay = storeOrderNew.getTotalPay();
		// 退优惠券
		// 3.7.9 注释优惠券 by hyq
		//storeOrderNewService.retreatingCoupons(storeOrderNew,refundCost);
		//退地推待入账金额
		groundBonusGrantService.reduceToEnterIntoAccount(orderNo,refundCost,refundOrderId);
		//退供应商待结算金额,无需填写供应商待结算
//		userNewService.reduceSettlingAmount(orderNo);
		//退供应商待结算金额,因为供应商待结算金额是实时的，所以不需要在这里进行更改字段
		
		//更改订单售后状态等
		boolean flag = false;
		if(refundOrder.getPlatformInterveneState() == RefundOrder.SELLER_PLATFORM_INTERVENE){
			flag = true;
		}
		refundOrderService.updateRefundOrderInfoWhenRefunded(refundOrderId,flag);
		//更改订单退款状态以及更改退款金额
		refundOrderService.updateOrderRefundStatusAndRefundCost(orderNo,RefundStatus.REFUND_SUCCESS.getIntValue(),refundCost);
		//记录操作日志
		refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.F,refundOrderId);
		//记录退款日志
		refundOrderActionLogService.addRefundLog(refundOrder,storeOrderNew);
		//自动退款
		try {
//			refundService.alipayRefund(storeOrderNew, String.valueOf(refundOrder.getRefundCost()), refundOrder.getRefundReason(), String.valueOf(refundOrderId));
			if(storeOrderNew.getPaymentType()==PaymentType.ALIPAY_SDK.getIntValue()){
				refundService.alipayRefund(storeOrderNew, String.valueOf(refundOrder.getRefundCost()), refundOrder.getRefundReason(),String.valueOf(refundOrderId));
			}else if(storeOrderNew.getPaymentType()==PaymentType.WEIXINPAY_SDK.getIntValue()){
				refundService.weixinRefund(storeOrderNew, String.valueOf(refundOrder.getRefundCost()), refundOrder.getRefundReason(), true,
						String.valueOf(refundOrderId));
			}
         else if (storeOrderNew.getPaymentType()==4) {
				YjjStoreBusinessAccountLogNew yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLogNew();
				yjjStoreBusinessAccountLog.setUserId(storeOrderNew.getStoreId());
				yjjStoreBusinessAccountLog.setOperMoney(refundOrder.getRefundCost());
				yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.INCOME.getCode());
				yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.REFUND_MONEY.getValue() + "-" + "APP商品退款");
				yjjStoreBusinessAccountLog.setType(StoreBillEnums.APP_REFUND_3.getCode());
				yjjStoreBusinessAccountLog.setAboutOrderNo(storeOrderNew.getOrderNo().toString());
				yjjStoreBusinessAccountLog.setOrderNo(storeOrderNew.getOrderNo().toString());
				YjjStoreBusinessAccountNew yjjStoreBusinessAccount = refundOrderMapper.selectMoney(storeOrderNew.getStoreId());
				Double reallyMoney = yjjStoreBusinessAccount.getRealUseMoney();
				yjjStoreBusinessAccount.setRealUseMoney(reallyMoney + refundOrder.getRefundCost());
				yjjStoreBusinessAccountLog.setRemainderMoney(yjjStoreBusinessAccount.getRealUseMoney());
				yjjStoreBusinessAccount.setCountMoney(yjjStoreBusinessAccount.getWaitInMoney() + yjjStoreBusinessAccount.getRealUseMoney());
				int i = refundOrderMapper.updateMoney(storeOrderNew.getStoreId(), yjjStoreBusinessAccount.getRealUseMoney(), yjjStoreBusinessAccount.getCountMoney());
				if (i != 1) {
					logger.info("退款失败");
				}
				//记录流水日志
				int i1 = refundOrderMapper.insertInto(yjjStoreBusinessAccountLog);
				if (i1 != 1) {
					logger.info("流水日志记录失败");
				}
				if (refundCost == (storeOrderNew.getTotalExpressMoney() + storeOrderNew.getTotalPay())) {
					int i2 = refundOrderMapper.updateOrderStatus(100, storeOrderNew.getOrderNo());
					if (i2 != 1) {
						logger.info("订单关闭失败");
					}
				}
			}
//			}else if (storeOrderNew.getPaymentType()==5){
//				YjjStoreBusinessAccountLogNew yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLogNew();
//				yjjStoreBusinessAccountLog.setUserId(storeOrderNew.getStoreId());
//				yjjStoreBusinessAccountLog.setOperMoney(refundOrder.getRefundCost());
//				yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.INCOME.getCode());
//				yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.REFUND_MONEY.getValue()+"-"+"APP商品退款");
//				yjjStoreBusinessAccountLog.setType(StoreBillEnums.REFUND_MONEY_SUCCESS.getCode());
//				yjjStoreBusinessAccountLog.setAboutOrderNo(storeOrderNew.getOrderNo().toString());
//				yjjStoreBusinessAccountLog.setOrderNo(storeOrderNew.getOrderNo().toString());
//				YjjStoreBusinessAccountNew yjjStoreBusinessAccount = refundOrderMapper.selectMoney(storeOrderNew.getStoreId());
//				Double waitInMoney = yjjStoreBusinessAccount.getWaitInMoney();
//				yjjStoreBusinessAccount.setWaitInMoney(waitInMoney+refundOrder.getRefundCost());
//				yjjStoreBusinessAccountLog.setRemainderMoney(yjjStoreBusinessAccount.getWaitInMoney());
//				yjjStoreBusinessAccount.setCountMoney(yjjStoreBusinessAccount.getWaitInMoney()+yjjStoreBusinessAccount.getRealUseMoney());
//				int i = refundOrderMapper.updateMoney(storeOrderNew.getStoreId(), yjjStoreBusinessAccount.getWaitInMoney(), yjjStoreBusinessAccount.getCountMoney());
//				if (i!=1){
//					logger.info("退款失败");
//				}
//				//记录流水日志
//				int i1 = refundOrderMapper.insertInto(yjjStoreBusinessAccountLog);
//				if (i1!=1){
//					logger.info("流水日志记录失败");
//				}
//				if (refundCost==(storeOrderNew.getTotalExpressMoney()+storeOrderNew.getTotalPay())){
//					int i2 = refundOrderMapper.updateOrderStatus(100, storeOrderNew.getOrderNo());
//					if (i2!=1){
//						logger.info("订单关闭失败");
//					}
//				}
//			}
			else{
				logger.error("订单的支付方式未知PaymentType为："+storeOrderNew.getPaymentType());
				throw new RuntimeException("受理售后订单:订单的支付方式未知PaymentType为："+storeOrderNew.getPaymentType());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		//短信通知买家
		UserNew userNew = userNewService.getSupplierUserInfo(refundOrder.getSupplierId());
		refundSMSNotificationService.sendSMSNotificationAndGEPush(null, MessageType.F.getIntValue(), storeOrderNew.getStoreId(), userNew.getPhone());
		
	}


	@Override
	public void checkApplyRefund(Long orderNo, long storeId) {
		//再判断是否已经该订单是否已经在售后或者售后成功
				RefundOrder reOrder = refundOrderService.getRefundOrderByOrderNoUnderWayOrSuccess(orderNo);
				StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
				logger.info("售后订单校验！");
				long currentTime = System.currentTimeMillis();
				if(currentTime >= FEB_TENTH && currentTime <= FEB_TENTH){
					logger.info("目前工厂处于休假状态，平台暂停受理退款退货服务:orderNo:"+orderNo+",storeId:"+storeId);
					throw new RuntimeException("亲爱的客户：目前工厂处于休假状态，平台暂停受理退款退货服务，于2018年3月3日重新开始受理售后，为您带来的不便敬请谅解。");
				}
				// 填充商品数据
//				long productId = storeOrderItemNew.getProductId();
//				ProductNew product = productNewMapper.selectById(productId);
				Integer orderStatus = storeOrderNew.getOrderStatus();
				// 先进行判断是否订单
				if (orderStatus != OrderStatus.PAID.getIntValue() && orderStatus != OrderStatus.DELIVER.getIntValue()) {
					logger.info("该订单不在售后期限内,orderNo:" + orderNo + "orderStatus:" + orderStatus);
					throw new RuntimeException("该订单不在售后期限内，无法申请售后！");
				}
				//该订单是否在售后中
				if(storeOrderNew.getRefundUnderway() == StoreOrderNew.REFUND_UNDERWAY){
					if(reOrder == null){
						logger.error("当前订单已申请平台介入，不能再申请售后。");
						throw new RuntimeException("当前订单已申请平台介入，不能再申请售后。");
					}
					logger.error("当前订单已有进行中的售后单，不能再申请。");
					throw new RuntimeException("当前订单已有进行中的售后单，不能再申请。");
				}
				//再判断是否已经该订单是否已经在售后或者售后成功
				if(null != reOrder){
					int refundStatus = reOrder.getRefundStatus();
					if(refundStatus == RefundStatus.REFUND_SUCCESS.getIntValue()){
						logger.error("当前订单已有退款成功的售后单，不能再申请。");
						throw new RuntimeException("当前订单已有退款成功的售后单，不能再申请。");
					}
					if(refundStatus == RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue()||
					   refundStatus == RefundStatus.CUSTOMER_DELIVERY.getIntValue()||
					   refundStatus == RefundStatus.SELLER_ACCEPT.getIntValue()){
						logger.error("当前订单已有进行中的售后单，不能再申请。");
						throw new RuntimeException("当前订单已有进行中的售后单，不能再申请。");
					}
				}
				logger.info("检验结束！");
	}


	
	
	
	



}