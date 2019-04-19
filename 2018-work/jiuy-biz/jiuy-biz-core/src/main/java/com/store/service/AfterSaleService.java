package com.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.order.ServiceTicketStatus;
import com.jiuyuan.entity.ServiceTicket;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.util.BeanUtil;
import com.store.dao.mapper.ServiceTicketMapper;
import com.store.entity.OrderAfterSaleCountVO;
import com.store.entity.ShopStoreOrderItem;
import com.store.entity.ShopStoreOrderItemVO;

@Service
public class AfterSaleService {
    
    @Autowired
    private ServiceTicketMapper serviceTicketMapper;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductAssembler productAssembler;
    
    public int updateServiceOrderPaid(long processOrderNo) {
    	
    	int newStatus = ServiceTicketStatus.REFUNDING.getIntValue();
    	int oldStatus = ServiceTicketStatus.UNPAID.getIntValue();
    	return serviceTicketMapper.updateServiceOrderPaid(processOrderNo, newStatus, oldStatus,System.currentTimeMillis());
    }
    
    public  int getOrderAfterSaleCount( long userId, long orderNo){
    	return serviceTicketMapper.getOrderAfterSaleCount(userId,  orderNo);
    }

	public  Map<Long, OrderAfterSaleCountVO> getOrderAfterSaleMap(long userId, long orderNo){
    	return serviceTicketMapper.getOrderAfterSaleMap(userId,  orderNo);
    }

	public ServiceTicket getServiceTicketDetailById(UserDetail userDetail , long id) {
    	ServiceTicket serviceTicket = serviceTicketMapper.getServiceTicketById(userDetail.getId(), id);
    	if(serviceTicket != null && serviceTicket.getOrderItemId() > 0){
    		ShopStoreOrderItem orderItem = orderService.getOrderNewItemById(userDetail.getId(), serviceTicket.getOrderItemId());
    		serviceTicket.setOrderItem(getOrderItemWithProductInfo(orderItem, userDetail));
    	}
    	return serviceTicket;
	}
	
	public ShopStoreOrderItem getOrderItemWithProductInfo(ShopStoreOrderItem orderItem, UserDetail userDetail) {
    	if(orderItem != null){
			List<ShopStoreOrderItemVO> composites = new ArrayList<ShopStoreOrderItemVO>();
			ShopStoreOrderItemVO vo = new ShopStoreOrderItemVO();
			BeanUtil.copyProperties(vo, orderItem);
			composites.add(vo);
			productAssembler.assemble(composites, userDetail);
			return new ArrayList<ShopStoreOrderItem>(composites).get(0);
		}
    	return orderItem;
    }
}