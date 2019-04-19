package com.jiuy.core.service.task;

import org.springframework.stereotype.Service;

@Service
public class StoreOrderAfterSalesFreeze {
//	 private Logger logger = Logger.getLogger(StoreOrderAfterSalesFreeze.class);
//	 
//	 private static final int NO_WITHDRAW = 0;
//	 
//	 private static final int WITHDRAW = 1;
//	 
//	 private static final BigDecimal NINETY_PERCENT = new BigDecimal(0.9);
//	 
//	 private static final int INCOME_AVAILABLE_BALANCE = 1;
//	 @Autowired
//	 private StoreOrderService storeOrderService;
//	 
//	 @Autowired
//	 private GlobalSettingService globalSettingService;
//	 
//	 @Autowired
//	 private StoreOrderLogService storeOrderLogService;
//	 
//	 @Autowired
//	 private SupplierFinanceLogService supplierFinanceLogService;
//	 
//	 @Autowired
//	 private SupplierUserService supplierUserService;
//
//	 
//	 
//	 public void execute() {
//		 logger.info("开始订单售后冻结！进行提现金额");
//		 //获取确认收货后的订单
//		 List<Long> list = storeOrderService.searchStoreOrderFreezeOrders(OrderStatus.SUCCESS,NO_WITHDRAW);
//		 //获取需要售后的时间(是否在 yjj_GlobalSetting设置了jsonArray，设置售后时间)
//		 JSONArray order_setting = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
//		 Long afterSaleMinutes = ((JSONObject)order_setting.get(0)).getLong("afterSaleMinutes");
//		 if (afterSaleMinutes == null) {
//			 logger.info("未设置售后申请时间");
//			 logger.info("订单提现金额结束");
//		 return;
//		 }
//		 //更改了售后时间！！！！！！测试用，注意改回来
//		 //afterSaleMinutes=5L*60*1000;
//		 //根据售后时间查找需要冻结的订单
//		 Set<Long> freezeOrderNos = new HashSet<>();
//		 long currentTime = System.currentTimeMillis();
//		 if(list.size()<=0){
//			 logger.info("本次没有冻结订单售后数目！");
//			 logger.info("订单提现金额结束");
//			 return;
//		 }
//		 
//		 List<StoreOrderLog> storeOrderLogs = storeOrderLogService.getByOrderNos(list, OrderStatus.DELIVER, OrderStatus.SUCCESS);
//		 for (StoreOrderLog storeOrderLog : storeOrderLogs) {
//			if (storeOrderLog.getCreateTime() + afterSaleMinutes * DateUtils.MILLIS_PER_MINUTE < currentTime) {
//					freezeOrderNos.add(storeOrderLog.getOrderNo());
//			}
//		 }
//		 
//		 //处理可提现金额，写死90%,不计算邮费
//		 List<StoreOrder> storeOrders = storeOrderService.ordersOfOrderNos(freezeOrderNos);
//		 a:for(StoreOrder storeOrder:storeOrders){
//			 //添加提现金额,只计算实付金额，不算运费
//			 long supplierId = storeOrder.getSupplierId();
//			 long orderNo = storeOrder.getOrderNo();
//			 double totalPay = storeOrder.getTotalPay();
//			 BigDecimal availableBalance = new BigDecimal(totalPay).multiply(NINETY_PERCENT).setScale(2, RoundingMode.HALF_UP);
//			 try {
//				 handle(supplierId,availableBalance,orderNo);
//			} catch (Exception e) {
//				logger.error(e.getMessage());
//				continue a;
//			}
//		 }
//		 logger.info("订单提现金额结束");
//		 
//		 
//		 
//	 }
//
//
//	@Transactional(rollbackFor = Exception.class)
//	private void handle(long supplierId, BigDecimal availableBalance, long orderNo) {
//		//添加进供应商的可提现金额中
//		 handleSupplierAvailableBalance(supplierId,availableBalance,orderNo);
//		 //添加供应商收支日志
//		 handleSupplierFinanceLog(supplierId,availableBalance,orderNo);
//		 //更改订单状态，改为订单已经提现
//		 int i =storeOrderService.updateHasWithdrawed(orderNo,WITHDRAW);
//		 if(i != 1 ){
//			 throw new RuntimeException("更改订单已经提现失败！");
//		 }
//	}
//
//
//
//	private void handleSupplierAvailableBalance(long supplierId, BigDecimal availableBalance,long orderNo) {
//		if(supplierId == 0){
//			return;
//		}
//		int i = supplierUserService.handleSupplierAvailableBalance(supplierId,availableBalance);
//		if(i != 1){
//			throw new RuntimeException("可提现金额添加失败！！"+"supplierId:"+supplierId+",orderNo:"+orderNo+",availableBalance:"+availableBalance);
//		}else{
//			logger.info("可提现金额添加成功："+"supplierId:"+supplierId+",orderNo:"+orderNo+",availableBalance:"+availableBalance);
//		}
//		
//	}
//
//
//
//	private void handleSupplierFinanceLog(long supplierId, BigDecimal availableBalance, long orderNo) {
//		if(supplierId == 0){
//			return;
//		}
//		SupplierFinanceLog supplierFinanceLog = new SupplierFinanceLog();
//		supplierFinanceLog.setSupplierId(supplierId);
//		supplierFinanceLog.setCash(availableBalance);
//		supplierFinanceLog.setType(INCOME_AVAILABLE_BALANCE);
//		supplierFinanceLog.setRelatedid(orderNo);
//		supplierFinanceLog.setCreatetime(System.currentTimeMillis());
//		supplierFinanceLog.setUpdatetime(System.currentTimeMillis());
//		
//		int i=supplierFinanceLogService.addSupplierFinanceLog(supplierFinanceLog);
//		if(i != 1){
//		    throw new RuntimeException("供应商收支日志添加失败！！"+"supplierId ="+supplierId+",orderNo ="+orderNo+",availableBalance"+availableBalance);
//		}else{
//			logger.info("供应商收支日志明细：(收入)"+"supplierId ="+supplierId+",orderNo ="+orderNo+",availableBalance"+availableBalance);
//		}
//	}
//
//


}
