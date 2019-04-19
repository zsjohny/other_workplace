package com.jiuy.core.service.task;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuy.core.dao.ShopMemberOrderDao;
import com.jiuy.core.dao.ShopMemberOrderLogDao;
import com.jiuyuan.dao.mapper.supplier.ShopMemberOrderNewMapper;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.order.ShopMemberOrderLog;
import com.store.dao.mapper.ShopMemberOrderMapper;
import com.store.entity.coupon.ShopMemberCoupon;

@Service
public class ShopMemberOrderServiceImpl implements ShopMemberOrderService{
	
	private static final Logger logger = Logger.getLogger(ShopMemberOrderServiceImpl.class);
	
	@Autowired
	private ShopMemberOrderDao shopMemberOrderDao;

	@Autowired
	private ShopMemberOrderLogDao shopMemberOrderLogDao;
	
	@Autowired
	private ShopMemberOrderNewMapper shopMemberOrderNewMapper;
	
	
	
	/**
	 * 获取待付款提醒订单列表
	 */
	@Override
	public List<ShopMemberOrder> getWaitPayTipOrderList() {
		//超时时间
		long timeOut = 24 * 60 * 60 * 1000; 
		//付款提醒时间（24小时减1小时即23小时）
		long payTipTime = timeOut - 60*60*1000;
		//23小时前的时间点
		long twentyThreeBefore = System.currentTimeMillis() - payTipTime;
		
		//获取下单时间小于23小时前的时间点的未付款订单
		return shopMemberOrderDao.getWaitPayTipOrderList(twentyThreeBefore);
	}
	
	

	/**
	 * 取消超时的未付款订单
	 */
	@Override
	public int storeOrderByOrderId(ShopMemberOrder shopMemberOrder) {
		logger.info("取消超时的未付款订单:shopMemberOrderId-"+shopMemberOrder.getId());
		return shopMemberOrderDao.storeOrderByOrderId(shopMemberOrder);
	}

	/**
	 * 添加日志
	 */
	@Override
	public int addShopMemberOrderLog(ShopMemberOrderLog shopMemberOrderLog) {
		logger.info("添加日志:orderId-"+shopMemberOrderLog.getOrderId());
		return shopMemberOrderLogDao.addShopMemberOrderLog(shopMemberOrderLog);
	}

	/**
	 * 即将超时订单发送通知后记录一下
	 */
	@Override
	public int updateOrderSendMessage(Long orderId) {
		logger.info("即将超时订单发送通知后记录一下:orderId-"+orderId);
		return shopMemberOrderDao.updateOrderSendMessage(orderId);
	}

	/**
	 * 修改优惠券状态
	 */
	@Override
	public int updateShopMemberCouponStatus(ShopMemberCoupon shopMemberCoupon) {
		logger.info("将优惠券设置为未使用:couponId-"+shopMemberCoupon.getId());
		return shopMemberOrderDao.updateShopMemberCouponStatus(shopMemberCoupon);
	}



	@Override
	public List<ShopMemberOrder> getUnPaidMemberOrderList() {
		long nowTime = System.currentTimeMillis();
		long outTime = nowTime - 24*60*60*1000;
		return shopMemberOrderDao.getUnPaidMemberOrderList(outTime);
	}


	/**
	 * 获取秒杀超过一小时未付款订单；
	 */
	@Override
	public List<ShopMemberOrder> getWaitPaySecondOrderList() {
		
		//付款提醒时间（定单结束前1小时）
		long payTipTime = 60*60*1000;
		//当前时间-1小时
		long oneBefore = System.currentTimeMillis() - payTipTime;
		Wrapper<ShopMemberOrder> wrapper = new EntityWrapper<ShopMemberOrder>()
				.eq("order_status", ShopMemberOrder.order_status_pending_payment)
				.isNotNull("second_id").le("create_time", oneBefore).eq("send_message", 0);
		//获取下单时间小于1小时前的时间点的未付款订单
		return shopMemberOrderNewMapper.selectList(wrapper );
	}



	@Override
	public List<ShopMemberOrder> stopMemberTeamOrderOvertime(long nowTime) {
		List<ShopMemberOrder> list = shopMemberOrderNewMapper.stopMemberTeamOrderOvertime(nowTime);
		return list;
	}



	@Override
	public List<ShopMemberOrder> getMemberSecondOvertimeOrder(long nowTime) {
		List<ShopMemberOrder> list = shopMemberOrderNewMapper.getMemberSecondOvertimeOrder(nowTime);
		return list;
	}
}