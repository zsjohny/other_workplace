package com.jiuy.core.service.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.StoreOrderDao;
import com.jiuy.core.dao.StoreProductDao;
import com.jiuy.core.dao.StoreServiceTicketDao;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.storeorder.StoreOrderItemService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicket;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.entity.storeorder.StoreOrderItem;

/**
 * @author jeff.zhan
 * @version 2016年12月31日 下午9:45:30
 * 
 */

public class StoreOrderSuccessJob {

	@Autowired
	private StoreOrderDao storeOrderDao;
	
	@Autowired
	private StoreOrderItemService storeOrderItemService;
	
    @Autowired
    private StoreProductDao storeProductDao;
    
    @Autowired
    private StoreServiceTicketDao storeServiceTicketDao;
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
	public void execute() {
		List<Integer> afterSellStatus_list = Arrays.asList(0, 1);
    	
    	//找出收货的可售后订单
		List<Long> orderNos = storeOrderDao.getOrderNosByOrderStatus(OrderStatus.SUCCESS.getIntValue(), afterSellStatus_list);

		//找出售后还未解决的订单
		List<Integer> status_list = Arrays.asList(6, 7);
		List<StoreServiceTicket> on_aftersale = storeServiceTicketDao.getByNotStatus(status_list);
		List<Long> problem_order_nos = gatherOrderNos(on_aftersale);
		
		//移除正在售后的订单。剩下没有问题的订单（未售后或售后结束）
		orderNos.removeAll(problem_order_nos);
			
		JSONArray order_setting = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
		Long afterSaleMinutes = ((JSONObject)order_setting.get(0)).getLong("afterSaleMinutes");
		if (afterSaleMinutes == null) {
			return;
		}
		
		long currentTime = System.currentTimeMillis();
		
        // 新表
        List<StoreOrder> storeOrdes = storeOrderDao.getOrders(OrderStatus.SUCCESS);
        for (StoreOrder storeOrde : storeOrdes) {
        	if (storeOrde.getPayTime() + afterSaleMinutes * DateUtils.MILLIS_PER_MINUTE < currentTime) {
        		// 更新新订单状态
        		List<StoreOrderItem> storeOrderItems = storeOrderItemService.getByOrderNo(storeOrde.getOrderNo());
        		if(storeOrderItems != null && storeOrderItems.size() > 0) {
        			storeProductDao.insertStoreProduct(storeOrderItems);
        		}
        	}
        }
	}
	
	private List<Long> gatherOrderNos(List<StoreServiceTicket> on_aftersale) {
		List<Long> orderNos = new ArrayList<>();
		for (StoreServiceTicket serviceTicket : on_aftersale) {
			orderNos.add(serviceTicket.getOrderNo());
		}
		return orderNos;
	}
	
}
