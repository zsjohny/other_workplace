package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.OrderItemDao;
import com.jiuy.core.dao.OrderNewDao;
import com.jiuy.core.dao.OrderNewLogDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.aftersale.FinanceTicket;
import com.jiuy.core.meta.order.OrderItem;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.meta.order.OrderNewVO;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.UserService;
import com.jiuy.core.service.aftersale.FinanceTicketService;
import com.jiuy.core.service.aftersale.ServiceTicketService;
import com.jiuy.core.service.notifacation.NotifacationService;
import com.jiuy.core.service.order.OrderOldService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ServiceTicket;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class OrderNewFacade {

    private final long DAY_TIME = 24 * 60 * 60 * 1000;

    @Autowired
    private OrderOldService orderNewService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderNewDao orderNewDao;
    
    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderNewLogDao orderNewLogDao;
    

	@Autowired
	private NotifacationService notificationServiceImpl;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServiceTicketService serviceTicketService;
	
	@Autowired
	private FinanceTicketService financeTicketService;

    @Transactional(rollbackFor = Exception.class)
    public void createMergerd() {
    	//将昨天3点到今天3点的合并
        List<OrderNew> orderNews = 
        		orderNewService.childOfOrderNew(DateUtil.getERPTime() - DAY_TIME, DateUtil.getERPTime());
        long i = 0;
        long lastLOWarehouseId = 0;
        String lastExpressInfo = "";
        List<OrderNew> orderNews2 = null;
        Map<Long, List<OrderNew>> orderNewMap = new HashMap<Long, List<OrderNew>>();
        for (OrderNew orderNew : orderNews) {
            if (orderNew.getlOWarehouseId() != lastLOWarehouseId || !StringUtils.equals(lastExpressInfo, orderNew.getExpressInfo())) {
            	lastLOWarehouseId = orderNew.getlOWarehouseId();
            	lastExpressInfo = orderNew.getExpressInfo();
                orderNews2 = new ArrayList<OrderNew>();
                orderNewMap.put(++i, orderNews2);
            }
            orderNews2.add(orderNew);
        }

        // 不需要合并
        Set<Long> singleMerge = new HashSet<Long>();
        List<List<OrderNew>> mixMerge = new ArrayList<List<OrderNew>>();
        for (Map.Entry<Long, List<OrderNew>> entry : orderNewMap.entrySet()) {
            List<OrderNew> orderNews3 = entry.getValue();
            if (orderNews3.size() == 1) {
                singleMerge.add(orderNews3.get(0).getOrderNo());
            } else {
                mixMerge.add(orderNews3);
            }
        }

        orderNewService.updateMegerdSelf(singleMerge);
        createMergerdOrderNew(mixMerge);

    }

    private void createMergerdOrderNew(List<List<OrderNew>> mixMerge) {
        for (List<OrderNew> orderNews : mixMerge) {
            Set<Long> orderNos = new HashSet<Long>();
            OrderNew orderNew = new OrderNew();

            OrderNew firstOrderNew = orderNews.get(0);
            orderNew.setUserId(firstOrderNew.getUserId());
            orderNew.setOrderStatus(firstOrderNew.getOrderStatus());
            orderNew.setExpressInfo(firstOrderNew.getExpressInfo());
            orderNew.setlOWarehouseId(firstOrderNew.getlOWarehouseId());
            orderNew.setMergedId(-1);

            double totalMoney = 0.00;
            double totalPay = 0.00;
            double totalExpressMoney = 0.00;
            int totalCoinUsed = 0;
            long lastUpdateTime = 0;
            for (OrderNew orderNew2 : orderNews) {
                orderNos.add(orderNew2.getOrderNo());

                totalMoney += orderNew2.getTotalMoney();
                totalPay += orderNew2.getTotalPay();
                totalCoinUsed += orderNew2.getCoinUsed();
                totalExpressMoney += orderNew2.getTotalExpressMoney();
                
                long updateTime = orderNew2.getUpdateTime();
                if(updateTime > lastUpdateTime) {
                	lastUpdateTime = updateTime;
                }
            }

            orderNew.setTotalExpressMoney(totalExpressMoney);
            orderNew.setTotalPay(totalPay);
            orderNew.setCoinUsed(totalCoinUsed);
            orderNew.setTotalMoney(totalMoney);
            orderNew.setCreateTime(lastUpdateTime);
            orderNew.setUpdateTime(lastUpdateTime);

            OrderNew orNew = orderNewService.addMergerdOrderNew(orderNew);
            orderNewService.updateMegerdChild(orderNos, orNew.getOrderNo());
        }
    }

    public Map<Long, List<OrderNew>> parentMergedMap(long startTime, long endTime, int orderType) {
        List<OrderNew> parentMergedOrderNews = orderNewService.getParentMergedOrderNews(startTime, endTime, orderType);

        Set<Long> parentMergedOrderNos = new HashSet<Long>();

        for (OrderNew orderNew : parentMergedOrderNews) {
            parentMergedOrderNos.add(orderNew.getOrderNo());
        }

        if (parentMergedOrderNos.size() < 1) {
            return new HashMap<Long, List<OrderNew>>();
        }
        Map<Long, List<OrderNew>> parentMergedMap = orderNewService.parentMergedMap(parentMergedOrderNos);

        return parentMergedMap;
    }
    
    //拆分订单的map
    public Map<Long, List<OrderNew>> splitMapOfOrderNos(Collection<Long> splitOrderNos) {
    	return orderNewService.splitMapOfOrderNos(splitOrderNos);
    }

    //组合订单的map
    public Map<Long, List<OrderNew>> combinationMapOfOrderNos(Collection<Long> combinationOrderNos) {
    	return orderNewService.parentMergedMap(combinationOrderNos);
    }

    public List<OrderNewVO> searchOrderNews(PageQuery pageQuery, String orderNo, int orderType, long userId,
                                            String receiver, String phone,
			long startTime, long endTime, int orderStatus, Collection<Long> orderNos,int sendType) {
        List<OrderNew> list = orderNewService.searchOrderNews(pageQuery, orderNo, orderType, userId, receiver, phone,
            startTime, endTime, orderStatus, orderNos,sendType);
		
		List<OrderNewVO> orderNewVOs = new ArrayList<OrderNewVO>();
		for(OrderNew orderNew : list) {
			OrderNewVO orderNewVO = new OrderNewVO();
			orderNewVOs.add(orderNewVO);
			
			BeanUtils.copyProperties(orderNew, orderNewVO);
		}
		
		return orderNewVOs;
	}

	public int searchOrderNewsCount(String orderNo, int orderType, long userId, String receiver, String phone,
			long startTime, long endTime, int orderStatus, Set<Long> orderNos,int sendType) {
		return orderNewService.searchOrderNewsCount(orderNo, orderType, userId, receiver, phone,
	            startTime, endTime, orderStatus, orderNos,sendType);
	}
	/**
	 * 
	 * @param orderNo
	 * @param oldOrderSatus
	 * @param newOrderStatus
	 * @param needCompare	检查订单状态
	 */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(long orderNo, int oldOrderSatus, int newOrderStatus, boolean needCompare) {
    	 updateOrderStatus( orderNo,  oldOrderSatus,  newOrderStatus,  needCompare,null) ;
    }
	/**
	 * 
	 * @param orderNo
	 * @param oldOrderSatus
	 * @param newOrderStatus
	 * @param needCompare	检查订单状态
	 * @param expressNo 物流单号
	 */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(long orderNo, int oldOrderSatus, int newOrderStatus, boolean needCompare,String expressNo) {

        /*1、 更新orderNew表*/
        List<OrderNew> orderNews = orderNewDao.orderNewsOfOrderNos(CollectionUtil.createList(orderNo));//获取订单
        //1.1、检查订单数量是正确
        OrderNew orderNew = null;
        if (orderNews.size() == 1) {
            orderNew = orderNews.get(0);
        } else {
        	throw new ParameterErrorException(orderNo + ": is not right ");
        }
        long userId = orderNew.getUserId();
        
        //1.2、检查订单状态
        if(needCompare) {
           if(orderNew.getOrderStatus() != oldOrderSatus) {
                throw new ParameterErrorException(orderNew.getOrderNo() + ": oldStatus is" + orderNew.getOrderStatus() + " not " + oldOrderSatus);
           }
        }
        //1.3、获取相关对的编号集合(如果是组合订单则会包含其他子订单编号)普通订单、未拆分订单(未付款) 没有额外orderNo
        long combinationId = orderNew.getMergedId();
        Set<Long> orderNos = new HashSet<Long>();
        orderNos.add(orderNo);
        if(combinationId == -1) {// 组合订单，则获取其他子订单编号
            List<OrderNew> orderNewslist = orderNewDao.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
            for (OrderNew orderNew2 : orderNewslist) {
                orderNos.add(orderNew2.getOrderNo());
            }
        }

        //2、更新符合条件的orderNew
        orderNewService.updateOrderStatus(orderNos, newOrderStatus);
        
        // 更新的orderNo写入orderNewLog
        long currentTime = System.currentTimeMillis();
    	orderNewLogDao.updateLog(userId, orderNos, oldOrderSatus, newOrderStatus, currentTime);
    	
    	// todo 暂时屏蔽发货通知
    	//发送发货通知  OrderStatus.PAID.getIntValue(), OrderStatus.DELIVER.getIntValue()
    	if(oldOrderSatus == OrderStatus.PAID.getIntValue() && newOrderStatus == OrderStatus.DELIVER.getIntValue()){//发货时
    		sendDeliveryAddNotification(userId,orderNos,expressNo);
    	}
    	
    }
    
    /**
	 * 发货添加物流信息
	 * @param orderNo
	 * @param expressNo
	 */
	public void sendDeliveryAddNotification(long userId,Set<Long> orderNos, String expressNo) {
		for(Long orderNo : orderNos){
			System.out.println("sendDeliveryAddNotification------------userId:"+userId+",orderNo"+orderNo+",expressNo:"+expressNo);
			deliveryAddNotification(userId, orderNo, expressNo);
		}
	}
	/**
	 * 发送发货消息
	 * @param userId
	 * @param orderNo
	 * @param expressNo
	 */
	private void deliveryAddNotification(Long userId, long orderNo, String expressNo ) {
			OrderNew  orderNew = orderNewService.orderNewOfOrderNo(orderNo);
			if(orderNew.getMergedId() == -1){		//组合订单系统合成的订单 不进行发货通知
				return;
			}
			List<OrderItem>   orderItemList = orderItemDao.orderItemsOfOrderNo(orderNo);
			String productName = null;
			String image = null;
			OrderItem orderItem = null;
			if(orderItemList != null && orderItemList.size()>0){
				orderItem = orderItemList.get(0);
				long productId = orderItem.getProductId();
				 Product product = productService.getProductById(productId);
				 productName = product.getName();
				 image = product.getFirstImage();
			}
			String titleString = "订单已发货！您购买的“"+productName+"”已发货，快递单号"+expressNo;
		    notificationServiceImpl.addNotificationAndUserNotification(9,image,String.valueOf(orderNo),userId,titleString,titleString);
		    System.out.println("发货发送消息完成---deliveryAddNotification------------userId:"+userId+",orderNo"+orderNo+",expressNo:"+expressNo);
	}

	public List<OrderNewVO> searchUndelivered(PageQuery pageQuery, String orderNo, long userId, String receiver,
			String phone, long startTime, long endTime, Set<Long> orderNos,int orderType) {
			
		List<OrderNew> list = orderNewService.searchUndelivered(pageQuery, orderNo, userId, receiver, phone,
	            startTime, endTime, orderNos,orderType);
			
		List<OrderNewVO> orderNewVOs = new ArrayList<OrderNewVO>();
		for(OrderNew orderNew : list) {
			OrderNewVO orderNewVO = new OrderNewVO();
			orderNewVOs.add(orderNewVO);
			
			BeanUtils.copyProperties(orderNew, orderNewVO);
		}
		
		return orderNewVOs;
	}

    public int searchUndeliveredCount(String orderNo, long userId, String receiver, String phone, long startTime,
			long endTime, Set<Long> orderNos,int orderType) {
		return orderNewService.searchUndeliveredCount(orderNo, userId, receiver, phone,
	            startTime, endTime, orderNos,orderType);
	}

	public Map<String, Object> getTotalDataForTime(long startTimeL, long endTimeL) {
		Map<String, Object> orderAndReturnMoneyMap = orderNewDao.getOrderAndReturnMoneyForTime(startTimeL,endTimeL);
		Map<String, Object> map = orderNewDao.getTotalDataForTime(startTimeL,endTimeL);
		Map<String,Object> storeMap = orderNewDao.getStoreTotalDataForTime(startTimeL,endTimeL);
		map.put("toTalOrderMoney", orderAndReturnMoneyMap.get("toTalOrderMoney"));
		map.put("storeOrderMoney", orderAndReturnMoneyMap.get("storeOrderMoney"));
		map.put("totalReturnMoney", orderAndReturnMoneyMap.get("totalReturnMoney"));
		map.put("storeReturnMoney", orderAndReturnMoneyMap.get("storeReturnMoney"));
		map.put("storeSaleMoney", storeMap.get("storeSaleMoney"));
		map.put("storeBuyCount", storeMap.get("storeBuyCount"));
		map.put("storeOrderCount", storeMap.get("storeOrderCount"));
		map.put("storeUserCount", storeMap.get("storeUserCount"));
		return map;
	}

	
	/**
	 * 根据订单发货信息从会员的收货地址列表中找到收货地址信息
	 * @param addresses
	 * @param expressInfo
	 * @return
	 */
	public Address getAddrByExp(List<Address> addresses, String expressInfo) {
		for(Address address : addresses) {
			if (StringUtils.contains(expressInfo, address.getAddrFull())) {
				return address;
			}
		}
		return null;
	}


	public void addFinanceTicketFromRevoke(long orderNo, String message, HttpServletRequest request) {
		long nowTime = System.currentTimeMillis();
		OrderNew orderNew = orderNewService.orderNewOfOrderNo(orderNo);
		long yjjNumber = userService.getYjjNumberById(orderNew.getUserId());
		
		ServiceTicket serviceTicket = new ServiceTicket(orderNo,nowTime,message,0,0,orderNew.getUserId(),yjjNumber,0,5,1,orderNew.getTotalPay(),0);
		serviceTicketService.addFromRevoke(serviceTicket);
		long serviceId = serviceTicket.getId();
		
		AdminUser userinfo = (AdminUser)request.getSession().getAttribute("userinfo");
		FinanceTicket financeTicket = new FinanceTicket(serviceId,nowTime,0,orderNew.getPaymentType(),message,1,userinfo.getUserRealName());
		financeTicketService.addFromRevoke(financeTicket);
	}


}
