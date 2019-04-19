/**
 * 
 */
package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin.common.constant.factory.PageFactory;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.ProxyCustomerMapper;
import com.jiuyuan.dao.mapper.supplier.ProxyOrderMapper;
import com.jiuyuan.entity.newentity.ProxyOrder;
import com.jiuyuan.entity.newentity.ProxyProduct;

/**
 * 新商品SKU服务
 */

@Service
public class ProxyOrderService implements IProxyOrderService  {
	private static final Logger logger = LoggerFactory.getLogger(ProxyOrderService.class);
	
	private static final int LATEST_SITUATION_CURRENT = 1;
	
	private static final int LATEST_SITUATION_PAGE_SIZE = 5;
	
	@Autowired
	private ProxyOrderMapper proxyOrderMapper;
	
	@Autowired
	private IProxyCustomerService proxyCustomerService;
	@Autowired
	private IProxyUserService proxyUserService;
	
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;
	
	
	/**
	 * 关闭订单
	 */
	@Transactional(rollbackFor = Exception.class)
	public void closeOrder(long orderId){
		ProxyOrder order = proxyOrderMapper.selectById(orderId);
		if(order.getOrderState() == ProxyOrder.orderState_new || order.getOrderState() == ProxyOrder.orderState_auditing){
			if(order.getOrderState() == ProxyOrder.orderState_auditing){//
				//1、如果是受理中则退还库存
				long proxyUserId = order.getProxyUserId();
				int proxyProductCount = order.getProxyProductCount();
				proxyUserService.returnStockCount(proxyUserId,proxyProductCount);
			}
			//2、
			ProxyOrder updOrder =  new ProxyOrder();
			updOrder.setId(orderId);
			updOrder.setOrderState(ProxyOrder.orderState_close);
			proxyOrderMapper.updateById(updOrder);
		}else{
			throw new RuntimeException("关闭失败");
		}
	}
	
	/**
	 * 完成订单
	 */
	@Transactional(rollbackFor = Exception.class)
	public void finishOrder(long orderId){
		ProxyOrder order = proxyOrderMapper.selectById(orderId);
		if(order.getOrderState() == ProxyOrder.orderState_auditing){
			//1、开通小程序
			storeBusinessNewService.openWxa(order.getApplyPhone(),order.getProxyProductId(),order.getProxyProductCount());
			
			//2、添加代理商客户
			proxyCustomerService.addProxyCustomer(order);
					
			//3、修改订单
			ProxyOrder updOrder =  new ProxyOrder();
			updOrder.setId(orderId);
			updOrder.setOrderState(ProxyOrder.orderState_finish);
			proxyOrderMapper.updateById(updOrder);
			
			//4、增加代理商用户销量
			proxyUserService.incrSellOutCount(order.getProxyUserId(),order.getProxyProductCount());
		}else{
			throw new RuntimeException("完成订单失败");
		}
		
	}
	
	/**
	 * 确认订单
	 */
	@Transactional(rollbackFor = Exception.class)
	public void confirmOrder(long orderId, String applyFullName, int proxyProductCount){
		ProxyOrder order = proxyOrderMapper.selectById(orderId);
		if(order.getOrderState() == ProxyOrder.orderState_new){
			//1、减少代理商库存
			long proxyUserId = order.getProxyUserId();
			proxyUserService.reduceStockCount(proxyUserId,proxyProductCount);
			//2、修改订单
			ProxyOrder updOrder =  new ProxyOrder();
			updOrder.setId(orderId);
			updOrder.setApplyFullName(applyFullName);
			updOrder.setProxyProductCount(proxyProductCount);
			updOrder.setOrderState(ProxyOrder.orderState_auditing);
			proxyOrderMapper.updateById(updOrder);
		}else{
			throw new RuntimeException("确认订单失败");
		}
		
	}
	 
	
	public ProxyOrder getProxyOrder(long proxyOrderId){
		return proxyOrderMapper.selectById(proxyOrderId);
	}
	
	public List<ProxyOrder> searchProxyProductList(long proxyUserId,  String applyFullName,
			String applyPhone, int orderState, String proxyProductName, Page<Map<String, String>> page){
		Wrapper<ProxyOrder> wrapper = new EntityWrapper<ProxyOrder>();
		wrapper.eq("proxy_user_id", proxyUserId);
		
		if(StringUtils.isNotEmpty(applyFullName)){
			wrapper.like("apply_full_name",applyFullName);
		}
		if(StringUtils.isNotEmpty(applyPhone)){
			wrapper.like("apply_phone",applyPhone);
		}
		//订单状态：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
		if(orderState != -1){
			wrapper.eq("order_state",orderState);
		}
		if(StringUtils.isNotEmpty(proxyProductName)){
			wrapper.like("proxy_product_name",proxyProductName);
		}
		wrapper.orderBy("id", false);
		List<ProxyOrder> list = proxyOrderMapper.selectPage(page,wrapper);
		return list;
	}
	
	public List<ProxyOrder> getProxyOrderList(String proxyUserNo, String proxyUserName, String applyFullName, String applyPhone,
			int orderState,
			Page<Map<String, String>> page){
		Wrapper<ProxyOrder> wrapper = new EntityWrapper<ProxyOrder>();
		
		if(StringUtils.isNotEmpty(proxyUserNo)){
			wrapper.like("proxy_user_no",proxyUserNo);
		}
		if(StringUtils.isNotEmpty(proxyUserName)){
			wrapper.like("proxy_user_name",proxyUserName);
		}
		
		if(StringUtils.isNotEmpty(applyFullName)){
			wrapper.like("apply_full_name",applyFullName);
		}
		
		if(StringUtils.isNotEmpty(applyPhone)){
			wrapper.like("apply_phone",applyPhone);
		}
		if(orderState != -1){
			wrapper.eq("order_state",orderState);
		}
		wrapper.orderBy("id", false);
		List<ProxyOrder> list = proxyOrderMapper.selectPage(page,wrapper);
		return list;
	}
    /**
     * 获取受理中的订单数
     */
	@Override
	public int getDealingOrderCount(long proxyUserId) {
		Wrapper<ProxyOrder> wrapper = new EntityWrapper<ProxyOrder>();
		wrapper.eq("order_state", ProxyOrder.orderState_auditing)
		       .eq("proxy_user_id", proxyUserId);
		int dealingOrderCount = proxyOrderMapper.selectCount(wrapper);
		return dealingOrderCount;
	}
    
	/**
	 * 获取已完成的订单数
	 */
	@Override
	public int getfinishedOrderCount(long proxyUserId) {
		Wrapper<ProxyOrder> wrapper = new EntityWrapper<ProxyOrder>();
		wrapper.eq("order_state", ProxyOrder.orderState_finish)
		       .eq("proxy_user_id", proxyUserId);
		int finishedOrderCount = proxyOrderMapper.selectCount(wrapper);
		return finishedOrderCount;
	}
    
	/**
	 * 获取最新动态
	 */
	@Override
	public List<ProxyOrder> selectLatestSituation(long proxyUserId) {
		Page<ProxyOrder> page = new Page<>(LATEST_SITUATION_CURRENT, LATEST_SITUATION_PAGE_SIZE);
		Wrapper<ProxyOrder> wrapper = new EntityWrapper<ProxyOrder>();
		wrapper.eq("proxy_user_id", proxyUserId)
		       .orderBy("update_time", false);
		return proxyOrderMapper.selectPage(page, wrapper);
	}
}