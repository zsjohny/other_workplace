package com.jiuy.core.business.facade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuy.core.dao.StoreOrderDao;
import com.jiuy.core.dao.StoreOrderLogDao;
import com.jiuy.core.dao.StoreOrderMessageBoardDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.service.storeaftersale.StoreFinanceTicketService;
import com.jiuy.core.service.storeaftersale.StoreServiceTicketService;
import com.jiuy.core.service.storeorder.StoreOrderService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.dao.mapper.supplier.GroundBonusGrantMapper;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.ground.GroundBonusGrant;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicket;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicket;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.entity.storeorder.StoreOrderMessageBoard;
import com.jiuyuan.entity.storeorder.StoreOrderVO;
import com.jiuyuan.util.DateUtil;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午4:29:54
*/
@Service
public class StoreOrderFacade {
	
	@Resource
	private StoreOrderService storeOrderService;
	
	@Resource
	private StoreOrderDao storeOrderDao;
	
	@Resource
	private StoreOrderLogDao storeOrderLogDao;
	
	@Resource
	private StoreServiceTicketService storeServiceTicketService;
	
	@Resource
	private StoreFinanceTicketService storeFinanceTicketService;
	
	@Autowired
	private GroundBonusGrantMapper groundBonusGrantMapper;
	
	@Autowired
	private StoreOrderMessageBoardDao storeOrderMessageBoardDao;

	public List<StoreOrderVO> searchStoreOrders(PageQuery pageQuery, String orderNo, int orderType, long storeId, String receiver,
			String phone, long startTime, long endTime, int orderStatus, Collection<Long> orderNos) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<StoreOrder> list = storeOrderService.searchStoreOrders(pageQuery, orderNo, orderType, storeId, receiver, phone,
	            startTime, endTime, orderStatus, orderNos);
		
		List<StoreOrderVO> storeOrderVOs = new ArrayList<StoreOrderVO>();
		for(StoreOrder storeOrder : list){
			StoreOrderVO storeOrderVO = new StoreOrderVO();
			storeOrderVOs.add(storeOrderVO);
			//获取最新一条留言
			if(storeOrder.getOrderNo()>0){
				StoreOrderMessageBoard storeOrderMessageBoard = storeOrderMessageBoardDao.getLastByOrderNo(storeOrder.getOrderNo());
				if(storeOrderMessageBoard!=null){
					String message = storeOrderMessageBoard.getMessage();
					if(!StringUtils.isEmpty(message)){
						storeOrderVO.setMessage(message);
					}
					String adminName = storeOrderMessageBoard.getAdminName();
					if(!StringUtils.isEmpty(adminName)){
						storeOrderVO.setAdminName(adminName);
					}
					storeOrderVO.setMessageCreateTime(simpleDateFormat.format(new Date(storeOrderMessageBoard.getCreateTime())));
				}
			}
			
			BeanUtils.copyProperties(storeOrder, storeOrderVO);
		}
		
		return storeOrderVOs;
	}
	
	public int searchStoreOrdersCount(String orderNo, int orderType, long storeId, String receiver, String phone, long startTime, long endTime, int orderStatus, Set<Long> orderNos){
		return storeOrderService.searchStoreOrdersCount(orderNo, orderType, storeId, receiver, phone, startTime, endTime, orderStatus, orderNos);
	}

	//拆分订单的map
	public Map<Long, List<StoreOrder>> splitMapOfOrderNos(Set<Long> splitOrderNos) {
		return storeOrderService.splitMapOfOrderNos(splitOrderNos);
	}

	//组合订单的map
	public Map<Long, List<StoreOrder>> combinationMapOfOrderNos(Collection<Long> combinationOrderNos) {
		return storeOrderService.parentMergedMap(combinationOrderNos);
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateSotreOrderStatus(long orderNo, int oldOrderSatus, int newOrderStatus, boolean needCompare) {
		//更新StoreOrder表
		List<StoreOrder> storeOrders = storeOrderDao.storeOrdersOfOrderNos(CollectionUtil.createList(orderNo));
		StoreOrder storeOrder = null;
		if(storeOrders.size() == 1){
			storeOrder = storeOrders.get(0);
		} else {
			throw new ParameterErrorException(orderNo + ": is not right ");
		}
		
		if(needCompare) {
			if(storeOrder.getOrderStatus() != oldOrderSatus) {
	             throw new ParameterErrorException(
	                storeOrder.getOrderNo() + ": oldStatus is" + storeOrder.getOrderStatus() + " not " + oldOrderSatus);
	        }
	    }
		
		long combinationId = storeOrder.getMergedId();
		Set<Long> orderNos = new HashSet<Long>();
		orderNos.add(orderNo);
		//组合订单
		if(combinationId == -1){
			List<StoreOrder> storeOrders2 = storeOrderDao.childOfCombinationOrderNos(CollectionUtil.createList(orderNo));
			for(StoreOrder storeOrder2 : storeOrders2){
				orderNos.add(storeOrder2.getOrderNo());
			}
		}
		
		// 普通订单、未拆分订单(未付款) 没有额外orderNo
        // 更新符合条件的storeOrder
		storeOrderService.updateOrderStatus(orderNos,newOrderStatus);
		
		//订单取消，待入账金额对应的减少
		GroundBonusGrant groundBonusGrant = new GroundBonusGrant();
		groundBonusGrant.setIntoTime(0L);
		groundBonusGrant.setIntoDate(DateUtil.getDateInt(0L));
		Wrapper<GroundBonusGrant> wrapper = new EntityWrapper<GroundBonusGrant>().gt("bonus_type", 2).eq("related_id", orderNo);
		int record = groundBonusGrantMapper.update(groundBonusGrant, wrapper);
		
		//更新的orderNo写入storeOrderLog
		long storeId = storeOrder.getStoreId();
		long currentTime = System.currentTimeMillis();
		
		storeOrderLogDao.updateLog(storeId, orderNos, oldOrderSatus, newOrderStatus, currentTime);
	}
	/**
	 * 自动确认收货
	 * @param orderNo
	 * @param oldOrderSatus
	 * @param newOrderStatus
	 * @param needCompare
	 */
	@Transactional(rollbackFor = Exception.class)
	public void autoOrderConfirm(long orderNo, int oldOrderSatus, int newOrderStatus, boolean needCompare){
//		//更新StoreOrder表
//		List<StoreOrder> storeOrders = storeOrderDao.storeOrdersOfOrderNos(CollectionUtil.createList(orderNo));
//		StoreOrder storeOrder = null;
//		if(storeOrders.size() == 1){
//			storeOrder = storeOrders.get(0);
//		} else {
//			throw new ParameterErrorException(orderNo + ": is not right ");
//		}
//		
//		if(needCompare) {
//			if(storeOrder.getOrderStatus() != oldOrderSatus) {
//	             throw new ParameterErrorException(
//	                storeOrder.getOrderNo() + ": oldStatus is" + storeOrder.getOrderStatus() + " not " + oldOrderSatus);
//	        }
//	    }
		
		
	}

	public List<StoreOrderVO> searchUndelivered(PageQuery pageQuery, String orderNo, long storeId, String receiver,
			String phone, long startTime, long endTime, Set<Long> orderNos) {
		List<StoreOrderVO> list = storeOrderService.searchUndelivered(pageQuery,orderNo,storeId,receiver,phone,startTime,endTime,orderNos);
		
		List<StoreOrderVO> storeOrderVOs = new ArrayList<StoreOrderVO>();
		for(StoreOrder storeOrder : list){
			StoreOrderVO storeOrderVO = new StoreOrderVO();
			storeOrderVOs.add(storeOrderVO);
			BeanUtils.copyProperties(storeOrder, storeOrderVO);
		}
		return storeOrderVOs;
	}
	
	public int searchUndeliveredCount(String orderNo, long storeId, String receiver, String phone, long startTime,
			long endTime, Set<Long> orderNos){
		return storeOrderService.searchUndeliveredCount(orderNo, storeId, receiver, phone,
	            startTime, endTime, orderNos);
	}

    public Map<Long, List<StoreOrder>> parentMergedMap(long startTime, long endTime) {
        List<StoreOrder> parentMergedOrderNews = storeOrderService.getParentMergedStoreOrder(startTime, endTime);

        Set<Long> parentMergedOrderNos = new HashSet<Long>();

        for (StoreOrder orderNew : parentMergedOrderNews) {
            parentMergedOrderNos.add(orderNew.getOrderNo());
        }

        if (parentMergedOrderNos.size() < 1) {
            return new HashMap<Long, List<StoreOrder>>();
        }
        Map<Long, List<StoreOrder>> parentMergedMap = storeOrderService.parentMergedMap(parentMergedOrderNos);

        return parentMergedMap;
    }

	public void addFinanceTicketFromRevoke(long orderNo, String remark,HttpServletRequest request) {
		long nowTime = System.currentTimeMillis();
		StoreOrder storeOrder = storeOrderService.orderOfOrderNo(orderNo);

		StoreServiceTicket storeServiceTicket = new StoreServiceTicket(orderNo,nowTime,remark,0,0,storeOrder.getStoreId(),storeOrder.getStoreId(),0,5,1,storeOrder.getTotalPay()-storeOrder.getCommission(),0);
		storeServiceTicketService.addFromRevoke(storeServiceTicket);
		long serviceId = storeServiceTicket.getId();
		
		AdminUser userinfo = (AdminUser)request.getSession().getAttribute("userinfo");
		//退款额减去收益部分
		StoreFinanceTicket storeFinanceTicket = new StoreFinanceTicket(serviceId,nowTime,0,storeOrder.getPaymentType(),remark,1,userinfo.getUserRealName());
		storeFinanceTicketService.addFromRevoke(storeFinanceTicket);
		
	}
	
}
