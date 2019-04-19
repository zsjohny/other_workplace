package com.jiuyuan.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderActionLogMapper;
import com.jiuyuan.dao.mapper.supplier.StoreRefundFinanceLogMapper;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.RefundOrderActionLog;
import com.jiuyuan.entity.newentity.RefundOrderActionLogEnum;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.StoreRefundFinanceLog;
import com.jiuyuan.util.DoubleUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class RefundOrderActionLogService implements IRefundOrderActionLogService {

	private static final Log logger = LogFactory.get(RefundOrderActionLogService.class);
	@Autowired
	private RefundOrderActionLogMapper refundOrderActionLogMapper;
	
	@Autowired
	private StoreRefundFinanceLogMapper storeRefundFinanceLogMapper;
	
	/**
	 * 获取售后订单的操作日志列表
	 * @param refundOrderId
	 * @return
	 */
	public List<RefundOrderActionLog> getRefundOrderActionLogList(long refundOrderId) {
		Wrapper<RefundOrderActionLog> wrapper = new EntityWrapper<RefundOrderActionLog>().eq("refund_order_id",
				refundOrderId);
		List<RefundOrderActionLog> refundOrderActionLogList = refundOrderActionLogMapper.selectList(wrapper);
		return refundOrderActionLogList;
	}
	/**
	 * 添加售后订单的操作日志
	 * @param refundOrderActionLogEnum
	 */
	public void addActionLog(RefundOrderActionLogEnum refundOrderActionLogEnum,Long refundOrderId){
		long time = System.currentTimeMillis();
		RefundOrderActionLog log = new RefundOrderActionLog();
		log.setActionName(refundOrderActionLogEnum.getDesc());
		log.setActionTime(time);
		log.setRefundOrderId(refundOrderId);
		refundOrderActionLogMapper.insert(log);
		logger.info("添加操作日志完成，"+refundOrderActionLogEnum.getDesc());
	}
	//添加退款日志
	@Override
	public void addRefundLog(RefundOrder refundOrder, StoreOrderNew storeOrderNew) {
		StoreRefundFinanceLog storeRefundFinanceLog = new StoreRefundFinanceLog();
		storeRefundFinanceLog.setStoreId(storeOrderNew.getStoreId());
		storeRefundFinanceLog.setType(1);
		storeRefundFinanceLog.setOrderNo(storeOrderNew.getOrderNo());
		storeRefundFinanceLog.setRefundOrderId(refundOrder.getId());
		storeRefundFinanceLog.setRefundCost(refundOrder.getRefundCost());
		Long currentTime = System.currentTimeMillis();
		storeRefundFinanceLog.setCreatetime(currentTime);
		storeRefundFinanceLog.setUpdatetime(currentTime);
		storeRefundFinanceLog.setPaymentType(refundOrder.getRefundWay());
		storeRefundFinanceLog.setPaymentNo(storeOrderNew.getPaymentNo());
		storeRefundFinanceLogMapper.insert(storeRefundFinanceLog);
		
	}
	
}