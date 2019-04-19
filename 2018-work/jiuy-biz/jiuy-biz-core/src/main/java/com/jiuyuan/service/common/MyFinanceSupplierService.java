package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.dao.mapper.supplier.WithdrawApplyNewMapper;
import com.jiuyuan.entity.newentity.FinanceLogNew;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.newentity.WithdrawApplyNew;
import com.jiuyuan.util.DoubleUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class MyFinanceSupplierService implements IMyFinanceSupplierService {
	private static final Log logger = LogFactory.get("MyFinanceService");
	
	private static final int DEALING_ORDER_STATUS = 0;
	
	private static final long ONE_DAY = 86400000L;
	
	private static final int ORIGNAL_ORDER = 0;
	
	private static final int FIVE_BIT = 5;
	
	private static final int BRAND_PAYMENT = 1;
    
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	
	@Autowired
	private WithdrawApplyNewMapper supplierWithdrawApplyMapper;
	
	@Autowired
	private UserNewMapper supplierUserMapper;
	
	@Autowired
	private FinanceLogNewService supplierFinanceLogService;
	
	@Autowired
	private IRefundOrderService refundOrderService;

	/* (non-Javadoc)
	 * @see com.supplier.service.IMyFinanceSupplierService#getTotalOrderAmount(long)
	 */
	@Override
	public Map<String,Object> getTotalOrderAmount(long supplierId) {
		Map<String,Object> map = new HashMap<String,Object>();
		//获取订单销售总额，状态已付款
		Wrapper<StoreOrderNew> wrapper = new EntityWrapper<StoreOrderNew>();
//		wrapper.ge("OrderStatus", OrderStatus.PAID.getIntValue()).eq("supplierId", supplierId)
//		       .ne("OrderStatus", OrderStatus.UNCHECK.getIntValue())
//		       .ne("OrderStatus", OrderStatus.CHECKED.getIntValue())
//		       .ne("OrderStatus", OrderStatus.CHECK_FAIL.getIntValue())
//		       .ne("OrderStatus", OrderStatus.CLOSED.getIntValue());
		wrapper.or("OrderStatus = "+OrderStatus.PAID.getIntValue())
		       .or("OrderStatus = "+OrderStatus.DELIVER.getIntValue())
		       .or("OrderStatus = "+OrderStatus.SIGNED.getIntValue())
		       .or("OrderStatus = "+OrderStatus.SUCCESS.getIntValue())
		       .or("OrderStatus = "+OrderStatus.REFUNDING.getIntValue())
		       .or("OrderStatus = "+OrderStatus.REFUNDED.getIntValue())
		       .andNew("supplierId = "+supplierId).gt("ParentId", ORIGNAL_ORDER);
		List<StoreOrderNew> storeOrderList = supplierOrderMapper.selectList(wrapper);
		BigDecimal totalOrderAmount = new BigDecimal(0);
		for(StoreOrderNew storeOrder:storeOrderList){
			//总订单额包含每个订单优惠前的金额+邮费
			BigDecimal totalMoney = new BigDecimal(storeOrder.getTotalMoney());
			BigDecimal totalExpressMoney = new BigDecimal(storeOrder.getTotalExpressMoney());
			BigDecimal total = totalMoney.add(totalExpressMoney);
			totalOrderAmount =totalOrderAmount.add(total);
		}
		
		map.put("totalOrderAmount", totalOrderAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		
		return map;
	}

    /* (non-Javadoc)
	 * @see com.supplier.service.IMyFinanceSupplierService#getWithdrawOrderList(com.baomidou.mybatisplus.plugins.Page, long, double, double, java.lang.String, java.lang.String, int)
	 */
    
	@Override
	public List<WithdrawApplyNew> getWithdrawOrderList(Page<Map<String,Object>> page, long supplierId, double minApplyMoney, double maxApplyMoney, String minCreateTime, String maxCreateTime, int status) {
		//转换时间
		long createTimeMinL = 0L;
		long createTimeMaxL = -1L;
		try {
			createTimeMinL = parseDate(minCreateTime);
			if(!StringUtils.equals(maxCreateTime, "")){
				createTimeMaxL = parseDate(maxCreateTime)+ONE_DAY;
			}
		} catch (ParseException e) {
			throw new RuntimeException("startTime:" + createTimeMinL + " endTime:" + createTimeMaxL);
		}
		
		Wrapper<WithdrawApplyNew> wrapper = new EntityWrapper<WithdrawApplyNew>();
		//状态
		if(status != -1){
			wrapper.eq("status", status);
		}
		//获取申请金额上下限
		if(minApplyMoney!=0){
			wrapper.ge("ApplyMoney", minApplyMoney);
		}
		if(maxApplyMoney!=0){
			wrapper.le("ApplyMoney", maxApplyMoney);
		}
		//获取申请提交时间上下限
		if(createTimeMinL!=-1){
			
			wrapper.ge("CreateTime", createTimeMinL);
		}
		if(createTimeMaxL!=-1){
			wrapper.le("CreateTime", createTimeMaxL);
		}
		
		//获取供应商ID
		wrapper.eq("RelatedId", supplierId).eq("Type", BRAND_PAYMENT).orderBy("CreateTime", false);
		//获取可以提现的订单
		List<WithdrawApplyNew> list = supplierWithdrawApplyMapper.selectPage(page,wrapper);
		return list;
	}
	
	private long parseDate(String time) throws ParseException{
        SimpleDateFormat secFormatter = new SimpleDateFormat("yyyy-MM-dd");
        secFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return secFormatter.parse(time).getTime();
	}
	
	/* (non-Javadoc)
	 * @see com.supplier.service.IMyFinanceSupplierService#getWithdrawOrderInfo(long)
	 */
	
	@Override
	public WithdrawApplyNew getWithdrawOrderInfo( long id) {
		WithdrawApplyNew withdrawApply = supplierWithdrawApplyMapper.selectById(id);
		return withdrawApply;
	}
    /* (non-Javadoc)
	 * @see com.supplier.service.IMyFinanceSupplierService#getCountOfDealingWDOrder(long)
	 */
	@Override
	public int getCountOfDealingWDOrder(long supplierId) {
		Wrapper<WithdrawApplyNew> wrapper = new EntityWrapper<WithdrawApplyNew>();
		wrapper.eq("status", DEALING_ORDER_STATUS).eq("RelatedId", supplierId).eq("Type", BRAND_PAYMENT);
		int count = supplierWithdrawApplyMapper.selectCount(wrapper);
		return count;
	}
    /* (non-Javadoc)
	 * @see com.supplier.service.IMyFinanceSupplierService#submitApply(long, double)
	 */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void submitApply(long supplierId, double applyMoney) {
		//获取供应商信息
		UserNew supplierUser = supplierUserMapper.selectById(supplierId);
		double minWithdrawal = supplierUser.getMinWithdrawal().doubleValue();
		BigDecimal oldAvailableBalance = new BigDecimal(supplierUser.getAvailableBalance());
		//判断申请金额是否达到最低限额且金额是否足够
		if(applyMoney < minWithdrawal){
			logger.info("supplierId:"+supplierId+",最低提款额不能低于"+minWithdrawal);
			throw new RuntimeException("最低提款额不能低于"+minWithdrawal);
		}
		if(applyMoney>supplierUser.getAvailableBalance().doubleValue()){
			logger.info("supplierId:"+supplierId+",超过可提现金额！"+supplierUser.getAvailableBalance().doubleValue());
			throw new RuntimeException("超过可提现金额！");
		}
		//生成提现申请订单
		//生成订单号
		long orderNo = 0;
		try {
			orderNo = generateOrderNo();
		} catch (Exception e) {
			logger.error(e);
		}
		//生成订单
		WithdrawApplyNew withdrawApply = new WithdrawApplyNew();
		withdrawApply.setRelatedId(supplierId);
		withdrawApply.setApplyMoney(applyMoney);
		withdrawApply.setCreateTime(System.currentTimeMillis());
		withdrawApply.setUpdateTime(System.currentTimeMillis());
		//订单状态 0 未处理 1已处理
		withdrawApply.setStatus(DEALING_ORDER_STATUS);
		//商家类型 0 门店 1 品牌货款 2 品牌物流 3 品牌
		withdrawApply.setType(BRAND_PAYMENT);
		//订单编号19位
		withdrawApply.setTradeId(orderNo);
		
		int i = supplierWithdrawApplyMapper.insert(withdrawApply);
		if(i == -1){
			logger.info("生成提现订单失败！");
			throw new RuntimeException("生成提现订单失败！");
		}else{
			logger.info("提现订单生成成功！提现订单编号:"+orderNo);
		}
		//获取提现主键ID
		Wrapper<WithdrawApplyNew> withdrawApplyWrapper = new EntityWrapper<WithdrawApplyNew>().eq("TradeId", orderNo);
		List<WithdrawApplyNew> withdrawApplyList = supplierWithdrawApplyMapper.selectList(withdrawApplyWrapper);
		long id = withdrawApplyList.get(0).getId();
		//更改供应商的可提现金额数目
		Wrapper<UserNew> wrapper = new EntityWrapper<UserNew>();
		wrapper.eq("id", supplierId);
		//减去申请金额
		BigDecimal newAvailableBalance = new BigDecimal(supplierUser.getAvailableBalance()).subtract(new BigDecimal(applyMoney));
		supplierUser.setAvailableBalance(newAvailableBalance.doubleValue());
		int h = supplierUserMapper.update(supplierUser, wrapper);
		
		if(h == -1){
			logger.info("可提现金额减去失败！");
			throw new RuntimeException("可提现金额减去失败！");
		}else{
			logger.info("申请提现金额为："+applyMoney+",扣除后可提现金额为"+newAvailableBalance.doubleValue());
		}
		//做收支记录
		FinanceLogNew supplierFinanceLog = new FinanceLogNew();
		supplierFinanceLog.setSupplierId(supplierId);
		//2：支出-供应商进行提现申请  relatedId代表商家提现申请审批表id,主键'
		supplierFinanceLog.setType(2);
		supplierFinanceLog.setRelatedid(id);
		supplierFinanceLog.setCreatetime(System.currentTimeMillis());
		supplierFinanceLog.setUpdatetime(System.currentTimeMillis());
		supplierFinanceLog.setCash(new BigDecimal(applyMoney));
		//获取供应商表中的
		supplierFinanceLog.setOldAvailableBalance(oldAvailableBalance);
		supplierFinanceLog.setNewAvailableBalance(newAvailableBalance);
		
		try {
			int g = supplierFinanceLogService.addSupplierFinanceLog(supplierFinanceLog);
			if(g != 1){
				logger.error("com.jiuy.web.controller/business/withdraw/confirm ERROR: 供应商收支信息生成失败！"+",supplierId:"+supplierId+",tradeId:"+orderNo);
				throw new RuntimeException("供应商收支信息生成失败！");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
	}
	

	/**
	 * 生成提现订单号
	 * @return
	 * @throws ParseException
	 */
    private long generateOrderNo() throws ParseException{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	String date = sdf.format(new Date());
    	Random random = new Random();
    	for(int i=0;i<FIVE_BIT;i++){
    		int j = random.nextInt(10);
            date += j;    		
    	}
    	long OrderNo = Long.parseLong(date);
		return OrderNo;
	}

	/* (non-Javadoc)
	 * @see com.supplier.service.IMyFinanceSupplierService#getAvailableBalance(long)
	 */
	@Override
	public double getAvailableBalance(long supplierId) {
		UserNew supplierUser = supplierUserMapper.selectById(supplierId);
		return supplierUser.getAvailableBalance().doubleValue();
	}

	@Override
	public Map<String, Object> getSettlingAmount(long supplierId) {
		//获取订单状态在已支付和已发货的订单
		Map<String,Object> map = new HashMap<String,Object>();
		Wrapper<StoreOrderNew> wrapper = new EntityWrapper<StoreOrderNew>();
		wrapper.or("OrderStatus = "+OrderStatus.PAID.getIntValue())
		       .or("OrderStatus = "+OrderStatus.DELIVER.getIntValue())
		       .andNew("supplierId = "+supplierId).gt("ParentId", ORIGNAL_ORDER);
		List<StoreOrderNew> storeOrderList = supplierOrderMapper.selectList(wrapper);
		//待结算金额
		BigDecimal settlingMoney = new BigDecimal(0);
		//遍历所有订单查询该订单是否有售后
		for(StoreOrderNew storeOrderNew : storeOrderList){
			//判断该订单是否有成功退款的售后单,如果没有售后订单，那么待结算金额包含优惠，否则，不包含优惠
			Long orderNo = storeOrderNew.getOrderNo();
//			BigDecimal totalMoney = new BigDecimal(storeOrderNew.getTotalMoney());
			BigDecimal totalPay = new BigDecimal(storeOrderNew.getTotalPay());//实付金额
			BigDecimal totalExpressMoney = new BigDecimal(storeOrderNew.getTotalExpressMoney());//邮费
			BigDecimal platformTotalPreferential = new BigDecimal(storeOrderNew.getPlatformTotalPreferential());//平台优惠
			BigDecimal total = new BigDecimal(0);
			total = totalPay.add(totalExpressMoney).add(platformTotalPreferential);
			//获取退款金额
			List<RefundOrder> refundOrderList = refundOrderService.getRefundSuccessListByOrderNo(orderNo);
			//去掉平台优惠
			if(refundOrderList.size()>0){
				total = totalPay.add(totalExpressMoney);
			}
			for(RefundOrder refundOrder:refundOrderList){
				total = total.subtract(new BigDecimal(refundOrder.getRefundCost()));
			}
			settlingMoney = settlingMoney.add(total);
		}
		settlingMoney = settlingMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		map.put("settlingMoney",settlingMoney);
		return map;
	}

	

}
