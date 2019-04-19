package com.yujj.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.jiuyuan.constant.order.ServiceTicketStatus;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.BeanUtil;
import com.yujj.business.assembler.ProductAssembler;
import com.yujj.business.facade.OrderFacade;
import com.yujj.dao.mapper.ServiceTicketMapper;
//import com.yujj.entity.account.Address;
import com.yujj.entity.afterSale.FinanceTicket;
import com.yujj.entity.afterSale.ServiceTicket;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.order.OrderItemVO;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.product.Product;

@Service
public class AfterSaleService {
    
    @Autowired
    private ServiceTicketMapper serviceTicketMapper;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private YJJUserAddressService userAddressService;
    
    @Autowired
    private OrderFacade orderFacade;
    
    @Autowired
    private ProductAssembler productAssembler;
    
    public ServiceTicket getServiceTicketDetailById(long userId , long id  ) {
    	 return getServiceTicketDetailById( userId ,  id ,false) ;
    } 
    public ServiceTicket getServiceTicketDetailById(long userId , long id ,boolean isNumToId) {
    	
    	ServiceTicket serviceTicket = serviceTicketMapper.getServiceTicketById(userId, id);
    	if(serviceTicket != null && serviceTicket.getOrderItemId() > 0){
    		OrderItem orderItem = orderService.getOrderNewItemById(userId, serviceTicket.getOrderItemId());
    		serviceTicket.setOrderItem(getOrderItemWithProductInfo(orderItem,isNumToId));
    	}
    	

    	return serviceTicket;
    }
    
    
    public ServiceTicket getServiceTicketDetailByOrderNo(long userId , long orderNo) {
    	
    	ServiceTicket serviceTicket = serviceTicketMapper.getServiceTicketByOrderNo(userId, orderNo);
    	
    	return serviceTicket;
    }
    public FinanceTicket getFinanceTicketById(long serviceId) {
    	
    	FinanceTicket financeTicket = serviceTicketMapper.getFinanceTicketById(serviceId);
    	
    	return financeTicket;
    }
    public OrderItem getOrderItemWithProductInfo(OrderItem orderItem ) {
    	return getOrderItemWithProductInfo( orderItem, false) ;
    }
    
    public OrderItem getOrderItemWithProductInfo(OrderItem orderItem,boolean isNumToId) {
    	if(orderItem != null){
			List<OrderItemVO> composites = new ArrayList<OrderItemVO>();
			OrderItemVO vo = new OrderItemVO();
			BeanUtil.copyProperties(vo, orderItem);
			composites.add(vo);
			productAssembler.assemble(composites);
			
			//将款号改为商品ID
			if(isNumToId){
				for(OrderItemVO orderItemVO : composites){
					Product product = orderItemVO.getProduct();
					String clothesNumber = product.getClothesNumber();
					String productId = String.valueOf(product.getId());
					product.setClothesNumber(productId);
				}
			}
			
			return new ArrayList<OrderItem>(composites).get(0);
			
		}
    	return orderItem;
    	
    }
    
    
    public  int getItemAfterSaleValidCount( long userId, long itemId ,long orderNo){
    	
    	return serviceTicketMapper.getItemAfterSaleValidCount(userId, itemId, orderNo);
    	
    	
    }
    
    
    public List<ServiceTicket> getUserServiceTicketList(PageQuery pageQuery, long userId , String orderSearchNo) {
    	List<ServiceTicket> serviceTicketList  = serviceTicketMapper.getUserServiceTicketList(pageQuery, userId, orderSearchNo);
    	if(serviceTicketList != null && serviceTicketList.size() > 0){
    		Set<Long> orderItemIds = new HashSet<Long>();
    		for(ServiceTicket serviceTicket : serviceTicketList){
    			orderItemIds.add(serviceTicket.getOrderItemId());
    		}
    		Map<Long, OrderItemVO> orderItemsMap;
    		orderItemsMap = orderFacade.getServiceItemVOMap(userId, orderItemIds);
    		for(ServiceTicket serviceTicket : serviceTicketList){
    			serviceTicket.setOrderItem(orderItemsMap.get(serviceTicket.getOrderItemId()));
    		}
    		
    		
    	}
    	return serviceTicketList;
    }


    
    public int updateServiceTicketById(long userId, ServiceTicket serviceTicket) {
    	return serviceTicketMapper.updateServiceTicketById(userId, serviceTicket);
    }
    
    public int countUserServiceTicketList(long userId, String orderSearchNo){
    	return serviceTicketMapper.countUserServiceTicketList(userId, orderSearchNo);
    }
    
    
    public int updateServiceOrderPaid(long processOrderNo) {
    	
    	int newStatus = ServiceTicketStatus.REFUNDING.getIntValue();
    	int oldStatus = ServiceTicketStatus.UNPAID.getIntValue();
    	return serviceTicketMapper.updateServiceOrderPaid(processOrderNo, newStatus, oldStatus,System.currentTimeMillis());
    }

}
