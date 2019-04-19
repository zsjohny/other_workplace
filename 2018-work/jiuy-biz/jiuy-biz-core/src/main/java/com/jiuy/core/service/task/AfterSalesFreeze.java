package com.jiuy.core.service.task;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.RefundStatus;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.RefundOrderActionLogEnum;
import com.jiuyuan.entity.newentity.SupplierDeliveryAddress;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.service.common.IRefundOrderFacade;
import com.jiuyuan.service.common.ISupplierDeliveryAddress;
import com.jiuyuan.service.common.RefundOrderActionLogService;
import com.jiuyuan.service.common.RefundSMSNotificationService;

/**
 * @author jeff.zhan
 * @version 2016年11月18日 上午9:16:03
 * 
 */
/**
 * #########定时任务##########
 * task.after.sales.freeze
 * #########################
 * 自动同意售后订单
 * 售后订单卖家自动确认收货
 *
 */
@Service
public class AfterSalesFreeze {

	private Logger logger = Logger.getLogger(AfterSalesFreeze.class);
	
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	
	@Autowired
	private IRefundOrderFacade refundOrderFacade;
	
	@Autowired
	private RefundSMSNotificationService refundSMSNotificationService;
	
	@Autowired
	private RefundOrderActionLogService refundOrderActionLogService;
	
	@Autowired
	private UserNewMapper userNewMapper;
	@Autowired
	private ISupplierDeliveryAddress deliveryAddressService;
//	private static final int NO_WITHDRAW = 1;
//
//	private static final int WITHDRAW = 2;
//
//	private static final BigDecimal NINETY_PERCENT = new BigDecimal(0.9);
//
//	private static final int INCOME_AVAILABLE_BALANCE = 1;
//	@Autowired
//	private StoreOrderService storeOrderService;
//
//	@Autowired
//	private StoreOrderLogService storeOrderLogService;
//
//	@Autowired
//	private SupplierFinanceLogService supplierFinanceLogService;
//
//	@Autowired
//	private SupplierUserOldService supplierUserService;
//
//	@Autowired
//	private OrderNewDao orderNewDao;
//
//	@Autowired
//	private OrderNewLogDao orderNewLogDao;
//
//	@Autowired
//	private GlobalSettingService globalSettingService;
//
//	@Autowired
//	private ServiceTicketDao serviceTicketDao;
//
//	@Autowired
//	private OrderOldService orderNewService;
//
//	@Autowired
//	private UserCoinService userCoinService;
//
//	@Autowired
//	private InvitedUserActionLogService invitedUserActionLogService;
//
//	@Autowired
//	private StoreBusinessDao storeBusinessDao;

	public void execute() {
		//自动同意售后订单
		autoAgreeRefundOrder();
		//售后订单卖家自动确认收货
		autoRefundOrderSellerConfirmaReceipt();
	}
	
	/**
	 * 自动同意售后订单
	 */
	private void autoAgreeRefundOrder() {
//		System.out.println(1);
		//3天之前的毫秒值
		long maxTime = System.currentTimeMillis()-3 * 24 * 60 * 60 * 1000;
//		System.out.println(maxTime);
		Wrapper<RefundOrder> wrapper = 
				new EntityWrapper<RefundOrder>().eq("refund_status",RefundStatus.CUSTOMER_APPLY_REFUND.getIntValue()).le("apply_time", maxTime);
		List<RefundOrder> refundOrderList = refundOrderMapper.selectList(wrapper);
		for (RefundOrder refundOrder : refundOrderList) {
			autoAgreeRefundOrderEach(wrapper,refundOrder);
		}
	}

	/**
	 * 同意每个售后订单
	 * @param wrapper 
	 * @param refundOrder
	 */
	@Transactional(rollbackFor = Exception.class)
	private void autoAgreeRefundOrderEach(Wrapper<RefundOrder> wrapper, RefundOrder refundOrder) {
		long refundOrderId = refundOrder.getId();
		if(refundOrder.getRefundStatus()>=2){
			logger.error("受理售后订单:该订单已处理:refundOrderId:"+refundOrderId+";refundStatus:"+refundOrder.getRefundStatus());
//		}else if(StringUtils.isEmpty(refundOrder.getReceiver()) || StringUtils.isEmpty(refundOrder.getReceiverPhone()) 
//				|| StringUtils.isEmpty(refundOrder.getSupplierReceiveAddress())){
//			logger.error("受理售后订单:refundOrderId:"+refundOrderId+"收货人信息不全:收货人姓名:"+refundOrder.getReceiver()
//				+";收货人电话:"+refundOrder.getReceiverPhone()+";收货人地址:"+refundOrder.getSupplierReceiveAddress());
		}else{
			//1：同意
			if(refundOrder.getRefundType()==RefundOrder.refundType_refund){
				//退款
				refundOrderFacade.refundSuccessOperation(refundOrderId);
			}else if(refundOrder.getRefundType()==RefundOrder.refundType_refund_and_product){
				//退货退款
				long supplierId = refundOrder.getSupplierId();
				
				//3.5添加，，收货信息从供应商售后收货地址表中取
				SupplierDeliveryAddress defaultAddress = deliveryAddressService.getDefaultAddress(supplierId);
				
				//3.5注释掉######################
//				UserNew userNew = userNewMapper.selectById(supplierId);
//				if(StringUtils.isEmpty(userNew.getReceiver()) 
//						|| StringUtils.isEmpty(userNew.getReceiverPhone()) || StringUtils.isEmpty(userNew.getSupplierReceiveAddress())){
//					logger.error("受理售后订单:refundOrderId:"+refundOrderId+"收货人信息不全:收货人姓名:"+userNew.getReceiver()
//						+";收货人电话:"+userNew.getReceiverPhone()+";收货人地址:"+userNew.getSupplierReceiveAddress());
//				}
				
				if (defaultAddress == null) {
					logger.error("受理售后订单:refundOrderId:"+refundOrderId+"售后收货地址信息不全");
				}else{
					//1、设置售后单状态：（待买家发货）
					refundOrder.setRefundStatus(2);
					//2.售后卖家收货地址信息
					refundOrder.setReceiver(defaultAddress.getReceiverName());
					refundOrder.setReceiverPhone(defaultAddress.getPhoneNumber());
					refundOrder.setSupplierReceiveAddress(defaultAddress.getAddress());
					
					
					//2、通知买家发货（短信&APP消息：【俞姐姐门店宝】您提交的退货卖家已经同意了哦，请在3天内填写物流信息。3天内无物流更新系统将关闭您的退货申请，需要重新申请退货哦。）
					refundSMSNotificationService.sendSMSNotificationAndGEPush(null, 3, refundOrder.getStoreId(), null);
					//3、添加售后订单操作日志
					refundOrderActionLogService.addActionLog(RefundOrderActionLogEnum.C,refundOrderId);
					refundOrder.setStoreAllowRefundTime(System.currentTimeMillis());
					refundOrder.setStoreAgreeRemark("");
					refundOrderMapper.updateById(refundOrder);
				}
			}
		}
	}

	/**
	 * 售后订单卖家自动确认收货
	 */
	private void autoRefundOrderSellerConfirmaReceipt(){
		try {
//			System.out.println(1);
			Wrapper<RefundOrder> wrapper = 
					new EntityWrapper<RefundOrder>().eq("refund_status",RefundStatus.CUSTOMER_DELIVERY.getIntValue())
					.eq("refund_type", RefundOrder.refundType_refund_and_product)
					.in("platform_intervene_state", new String[]{"0","3","4"});
			List<RefundOrder> refundOrderList = refundOrderMapper.selectList(wrapper);
			for (RefundOrder refundOrder : refundOrderList) {
				autoRefundOrderSellerConfirmaReceiptEach(refundOrder);
			}
		} catch (Exception e) {
			logger.error("售后订单卖家自动确认收货:"+e.getMessage());
		}
		
	}

	/**
	 * 每个售后订单卖家自动确认收货
	 * @param refundOrder
	 */
	@Transactional(rollbackFor = Exception.class)
	private void autoRefundOrderSellerConfirmaReceiptEach(RefundOrder refundOrder) {
		if(refundOrder.getPlatformInterveneState()==RefundOrder.PLATFORM_NOT_INTERVENE 
				&& (System.currentTimeMillis()-refundOrder.getCustomerReturnTime())>(15*24*60*60*1000)){//平台未介入过并且超过自动确认收货时间
			//退款
			refundOrderFacade.refundSuccessOperation(refundOrder.getId());
		}else if(refundOrder.getPlatformInterveneState()==RefundOrder.CLOSE_CUSTOMER_PLATFORM_INTERVENE 
				 || refundOrder.getPlatformInterveneState()==RefundOrder.CLOSE_SELLER_PLATFORM_INTERVENE){//平台介入过
			if(refundOrderFacade.buildSurplusSupplierAutoTakeTime(refundOrder)<=0){//超过自动确认收货时间
				//退款
				refundOrderFacade.refundSuccessOperation(refundOrder.getId());
			}
		}
	}

//yjj售后订单相关功能
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
//	@Transactional(rollbackFor = Exception.class)
//	public void execute() {
//		// 2017-10-23
//		// 获取确认收货后的订单
//		List<Long> list = storeOrderService.searchStoreOrderFreezeOrders(OrderStatus.SUCCESS, NO_WITHDRAW);
//		// 获取需要售后的时间(是否在 yjj_GlobalSetting设置了jsonArray，设置售后时间)
//		// 更改了售后时间！！！！！！测试用，注意改回来
//		// afterSaleMinutes=5L*60*1000;
//		// 根据售后时间查找需要冻结的订单
//		Set<Long> freezeOrderNos1 = new HashSet<>();
//		long currentTime1 = System.currentTimeMillis();
//		JSONArray order_setting = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
//		Long afterSaleMinutes = ((JSONObject) order_setting.get(0)).getLong("afterSaleMinutes");
//		if (afterSaleMinutes == null) {
//			logger.info("售后冻结时间未设置");
//			return;
//		}
//		// 测试
//		//afterSaleMinutes = 0L;
//		if(list!=null&&list.size()>0){
//			List<StoreOrderLog1> storeOrderLogs = storeOrderLogService.getByOrderNos(list, OrderStatus.DELIVER,
//					OrderStatus.SUCCESS);
//			for (StoreOrderLog1 storeOrderLog : storeOrderLogs) {
//				if (storeOrderLog.getCreateTime() + afterSaleMinutes * DateUtils.MILLIS_PER_MINUTE < currentTime1) {
//					freezeOrderNos1.add(storeOrderLog.getOrderNo());
//				}
//			}
//			if(freezeOrderNos1!=null&&freezeOrderNos1.size()>0){
//				// 处理可提现金额，写死90%
//				List<StoreOrder> storeOrders = storeOrderService.ordersOfOrderNos(freezeOrderNos1);
//				for (StoreOrder storeOrder : storeOrders) {
//					// 添加提现金额,计算(优惠前订单金额+运费)*90%
//					long supplierId = storeOrder.getSupplierId();
//					long orderNo = storeOrder.getOrderNo();
//					BigDecimal totalMoney = new BigDecimal(storeOrder.getTotalMoney());
//					BigDecimal total = totalMoney.add(new BigDecimal(storeOrder.getTotalExpressMoney()));
//					BigDecimal withdraw = total.multiply(NINETY_PERCENT).setScale(2, RoundingMode.HALF_UP);
//					// 处理售后冻结
//					handle(supplierId, withdraw, orderNo);
//					
//				}
//			}
//		}
//
//		List<Integer> afterSellStatus_list = Arrays.asList(0, 1);
//
//		// logger.info("AfterSalesFreeze execute");
//
//		// 找出收货的可售后订单
//		List<Long> orderNos = orderNewDao.getOrderNosByOrderStatus(OrderStatus.SUCCESS.getIntValue(),
//				afterSellStatus_list);
//
//		// 找出售后还未解决的订单
//		List<Integer> status_list = Arrays.asList(6, 7);
//		List<ServiceTicket> on_aftersale = serviceTicketDao.getByNotStatus(status_list);
//		List<Long> problem_order_nos = gatherOrderNos(on_aftersale);
//
//		// 移除正在售后的订单。剩下没有问题的订单（未售后或售后结束）
//		orderNos.removeAll(problem_order_nos);
//
//		
//		Set<Long> freezeOrderNos = new HashSet<>();
//		long currentTime = System.currentTimeMillis();
//		List<OrderNewLog> orderNewLogs = orderNewLogDao.getByOrderNos(orderNos, OrderStatus.DELIVER,
//				OrderStatus.SUCCESS);
//		for (OrderNewLog orderNewLog : orderNewLogs) {
//			if (orderNewLog.getCreateTime() + afterSaleMinutes * DateUtils.MILLIS_PER_MINUTE < currentTime) {
//				freezeOrderNos.add(orderNewLog.getOrderNo());
//			}
//		}
//
//		if (freezeOrderNos.size() < 1) {
//			return;
//		}
//		// 冻结售后
//		orderNewDao.freezeAfterSales(freezeOrderNos);
//
//		List<OrderNew> orderNews = orderNewService.orderNewsOfOrderNos(freezeOrderNos);
//		for (OrderNew orderNew : orderNews) {
//
//			/**
//			 * 处理玖币
//			 */
//			handleJiucoin(currentTime, orderNew, orderNew.getOrderNo(), orderNew.getUserId());
//
//			/**
//			 * 门店提成 给提成
//			 */
//			handleAvaliableBalance(orderNew);
//
//			/**
//			 * 原始母订单记录
//			 */
//			if (orderNew.getParentId() > 0 && orderNew.getParentId() != orderNew.getOrderNo()) {
//				orderNewDao.increseAvailableCommission(orderNew.getParentId(), orderNew.getCommission(),
//						orderNew.getReturnCommission());
//			}
//		}
//
//
//
//	}
//
//	private void handle(long supplierId, BigDecimal withdraw, long orderNo) {
//		// 添加进供应商的可提现金额中
//		List<BigDecimal> list = handleSupplierAvailableBalance(supplierId, withdraw, orderNo);
//		// 添加供应商收支日志
//		if (list == null || list.size() <= 1) {
//			return;
//		}
//		handleSupplierFinanceLog(supplierId, withdraw, orderNo, list);
//		// 更改订单状态，改为订单已经提现
//		int i = storeOrderService.updateHasWithdrawed(orderNo, WITHDRAW);
//		if (i != 1) {
//			logger.error("更改订单已经提现失败！");
//			throw new RuntimeException("更改订单已经提现失败！");
//		}
//	}
//
//	private List<BigDecimal> handleSupplierAvailableBalance(long supplierId, BigDecimal withdraw, long orderNo) {
//		if (supplierId == 0) {
//			return null;
//		}
//		BigDecimal newAvailableBalance = null;
//		BigDecimal oldAvailableBalance = supplierUserService.getAvailableBalanceById(supplierId);
//		int i = supplierUserService.handleSupplierAvailableBalance(supplierId, withdraw);
//		if (i != 1) {
//			logger.error("可提现金额添加失败！！" + "supplierId:" + supplierId + ",orderNo:" + orderNo + ",availableBalance:"
//					+ withdraw);
//			throw new RuntimeException("可提现金额添加失败！！" + "supplierId:" + supplierId + ",orderNo:" + orderNo
//					+ ",availableBalance:" + withdraw);
//		} else {
//			newAvailableBalance = oldAvailableBalance.add(withdraw);
//			logger.info("可提现金额添加成功：" + "supplierId:" + supplierId + ",orderNo:" + orderNo + ",withdraw:" + withdraw
//					+ ",oldAvailableBalance:" + oldAvailableBalance + ",newAvailableBalance:" + newAvailableBalance);
//		}
//		List<BigDecimal> list = new ArrayList<BigDecimal>();
//		if (oldAvailableBalance != null) {
//			list.add(oldAvailableBalance);
//		}
//		if (newAvailableBalance != null) {
//			list.add(newAvailableBalance);
//		}
//		return list;
//
//	}
//
//	private void handleSupplierFinanceLog(long supplierId, BigDecimal withdraw, long orderNo, List<BigDecimal> list) {
//		if (supplierId == 0) {
//			return;
//		}
//		FinanceLogNew supplierFinanceLog = new FinanceLogNew();
//		supplierFinanceLog.setSupplierId(supplierId);
//		supplierFinanceLog.setCash(withdraw);
//		supplierFinanceLog.setType(INCOME_AVAILABLE_BALANCE);
//		supplierFinanceLog.setRelatedid(orderNo);
//		supplierFinanceLog.setCreatetime(System.currentTimeMillis());
//		supplierFinanceLog.setUpdatetime(System.currentTimeMillis());
//		supplierFinanceLog.setOldAvailableBalance(list.get(0));
//		supplierFinanceLog.setNewAvailableBalance(list.get(1));
//
//		int i = supplierFinanceLogService.addSupplierFinanceLog(supplierFinanceLog);
//		if (i != 1) {
//			logger.error(
//					"供应商收支日志添加失败！！" + "supplierId:" + supplierId + ",orderNo:" + orderNo + ",withdraw:" + withdraw);
//			throw new RuntimeException(
//					"供应商收支日志添加失败！！" + "supplierId:" + supplierId + ",orderNo:" + orderNo + ",withdraw:" + withdraw);
//		} else {
//			logger.info("供应商收支日志明细：(收入)" + "supplierId:" + supplierId + ",orderNo:" + orderNo + ",withdraw: " + withdraw
//					+ ",oldAvailableBalance:" + list.get(0) + ",newAvailableBalance:" + list.get(1));
//		}
//	}
//
//	private void handleAvaliableBalance(OrderNew orderNew) {
//		storeBusinessDao.increseAvaliableBalance(orderNew.getBelongBusinessId(), orderNew.getAvailableCommission());
//	}
//
//	private List<Long> gatherOrderNos(List<ServiceTicket> on_aftersale) {
//		List<Long> orderNos = new ArrayList<>();
//		for (ServiceTicket serviceTicket : on_aftersale) {
//			orderNos.add(serviceTicket.getOrderNo());
//		}
//		return orderNos;
//	}
//
//	private void handleJiucoin(long currentTime, OrderNew order, Long orderNo, long userId) {
//		InvitedUserActionLog actionLog = invitedUserActionLogService.getByUserId(userId, 0);
//
//		long time = System.currentTimeMillis();
//		/**
//		 * 下单人表&邀请人返利
//		 */
//		double totalMoney = order.getTotalExpressMoney() + order.getTotalPay();
//
//		JSONObject jiucoin_global_setting = globalSettingService
//				.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
//		JSONObject shoppingJiuCoinSetting = jiucoin_global_setting.getJSONObject("shoppingJiuCoinSetting");
//		JSONObject jiuCoinAttribute = jiucoin_global_setting.getJSONObject("jiuCoinAttribute");
//		double worthRmb = jiuCoinAttribute.getDouble("worthRmb");
//		int percentage = shoppingJiuCoinSetting.getInteger("returnPercentage");
//
//		userCoinService.updateUserCoin(userId, 0, (int) (totalMoney * percentage / 100 / worthRmb), orderNo + "", time,
//				UserCoinOperation.PURCHASE);
//
//		if (actionLog != null) {
//			// 记录
//			long invitorId = actionLog.getInvitor();
//
//			/**
//			 * @author Jeff.Zhan 邀请送积分
//			 */
//			JSONObject invitationSetting = jiucoin_global_setting.getJSONObject("invitationSetting");
//			int returnPercentage = invitationSetting.getInteger("returnPercentage");
//			int returnCycle = invitationSetting.getInteger("returnCycle");
//			int maxJiuCoinReturnCycle = invitationSetting.getInteger("maxJiuCoinReturnCycle");
//
//			long startTime = DateUtil.getTodayEnd() - returnCycle * DateUtils.MILLIS_PER_DAY;
//			long endTime = DateUtil.getTodayEnd();
//
//			UserCoinOperation operation = UserCoinOperation.INVITED_ORDER;
//			int total_gain_coin = userCoinService.getTotalUserCoin(invitorId, startTime, endTime, operation);
//			if (total_gain_coin < maxJiuCoinReturnCycle || maxJiuCoinReturnCycle < 0) {
//				userCoinService.updateUserCoin(invitorId, 0, (int) (totalMoney * returnPercentage / 100 / worthRmb),
//						orderNo + "", time, operation);
//			}
//		}
//	}
}
