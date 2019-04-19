package com.jiuy.core.service.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
//import com.jiuy.core.business.facade.OrderFacade;
import com.jiuy.core.business.facade.OrderNewFacade;
import com.jiuy.core.business.facade.StoreOrderFacade;
import com.jiuy.core.dao.OrderNewDao;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.ShopMemberOrderNewMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.service.common.IOrderNewService;
import com.jiuyuan.service.common.IStoreOrderNewService;
import com.jiuyuan.service.common.OrderNewService;
import com.store.dao.mapper.ShopMemberOrderMapper;
/**
 * #########定时任务##########
 * task.aftersale.auto.confirm
 * #########################
 * 门店订单自动确认收货
 *
 */
@Service
public class OrderSuccessJob {

    private static final Logger logger = Logger.getLogger(OrderDisposingJob.class);
	
//	@Autowired
//	private OrderFacade orderFacade;

    @Autowired
    private OrderNewFacade orderNewFacade;
    
    @Autowired
    private StoreOrderFacade storeOrderFacade;

    @Autowired
    private GlobalSettingService glService;
    
    @Autowired
    private IStoreOrderNewService storeOrderNewService;

    @Autowired
    private OrderNewDao orderNewDao;
    
    @Autowired
    private SupplierOrderMapper supplierOrderMapper;
    
    @Autowired
	private IOrderNewService orderNewService;
    
   
	
    @Transactional(rollbackFor = Exception.class)
	public void execute() {
        long timeNow = System.currentTimeMillis();

        JSONArray jsonArray = glService.getJsonArray(GlobalSettingName.ORDER_AUTO_CONFIRM_TIME);
        int minutes = 24 * 60;
        for (Object obj : jsonArray) {
            minutes = ((JSONObject) obj).getInteger("autoConfirmMinutes");
            break;
        }
        long expireLenth = minutes * DateUtils.MILLIS_PER_MINUTE;

        // 旧表 旧表已经删除，所以注释掉
//        List<Order> list = orderFacade.getDefaultReceive(OrderStatus.DELIVER, expireLenth);
//		orderFacade.disposeDeliverOrder(list);

        // 新表
//        List<OrderNew> deliverOrderNews = orderNewDao.getOrderNewByOrderStatus(OrderStatus.DELIVER.getIntValue());
//        for (OrderNew deliverOrderNew : deliverOrderNews) {
//            long lastUpdateTime = deliverOrderNew.getUpdateTime();
//            if (timeNow - lastUpdateTime >= expireLenth) {
//                // 更新新订单状态
//                long orderNo = deliverOrderNew.getOrderNo();
//                try {
//                    orderNewFacade.updateOrderStatus(orderNo, OrderStatus.DELIVER.getIntValue(), OrderStatus.SUCCESS.getIntValue(), true);
//                } catch (ParameterErrorException e) {
//                    logger.info("com.jiuy.core.service.task.OrderDisposingJob OrderSuccessJob, orderNo: " + orderNo);
//                }
//            }
//        }
//        logger.info("开始自动确认收货！");
        Wrapper<StoreOrderNew> storeOrderNewWrapper = new EntityWrapper<StoreOrderNew>()
				.eq("OrderStatus", OrderStatus.DELIVER.getIntValue())
				.eq("Status", StoreOrderNew.status_normal).le("SendTime", timeNow-expireLenth).gt("ParentId", 0).eq("hangUp", 0);//未被挂起的订单
		List<StoreOrderNew> storeOrderNewList = supplierOrderMapper.selectList(storeOrderNewWrapper);
		for (StoreOrderNew storeOrderNew : storeOrderNewList) {
			Long refundStartTime = storeOrderNew.getRefundStartTime();
			if(refundStartTime>0){
				continue;
			}
			long buildSurplusSupplierAutoTakeTime = orderNewService.buildSurplusSupplierAutoTakeTime(storeOrderNew.getSendTime(), 
					refundStartTime, storeOrderNew.getAutoTakeGeliveryPauseTimeLength());
			if(buildSurplusSupplierAutoTakeTime>0){
				continue;
			}
			orderNewService.updateOrderNewStatus(storeOrderNew, OrderStatus.SUCCESS, OrderStatus.DELIVER, timeNow);
		}
//		logger.info("结束自动确认收货！");
        
//        List<StoreOrderNew> deliverStoreOrder = storeOrderNewService.getStoreOrderByOrderStatus(OrderStatus.DELIVER.getIntValue());
//        for(StoreOrderNew storeOrder:deliverStoreOrder){
//        	long lastUpdateTime = storeOrder.getUpdateTime();
////        	if (timeNow - lastUpdateTime >= expireLenth) {
//        	if (lastUpdateTime == 1511750521691L) {
//                // 更新新订单状态
//                long orderNo = storeOrder.getOrderNo();
//                logger.info("订单号："+orderNo);
//                try {
//                	storeOrderFacade.updateSotreOrderStatus(orderNo, OrderStatus.DELIVER.getIntValue(), OrderStatus.SUCCESS.getIntValue(), true);
//                } catch (ParameterErrorException e) {
//                    logger.info("com.jiuy.core.service.task.OrderDisposingJob OrderSuccessJob, orderNo: " + orderNo);
//                }
//            }
//        }
        
        
	}

   
    
}
