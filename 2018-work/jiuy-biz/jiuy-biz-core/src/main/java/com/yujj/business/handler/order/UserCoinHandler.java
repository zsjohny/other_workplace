package com.yujj.business.handler.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.ClientPlatform;
import com.yujj.business.service.UserCoinService;
import com.yujj.entity.order.Order;
import com.yujj.entity.order.OrderNew;

@Service
public class UserCoinHandler implements OrderHandler {

    @Autowired
    private UserCoinService userCoinService;

    @Override
    public void postOrderCreation(Order order, String version) {
        long time = order.getCreateTime();
        long userId = order.getUserId();
        int avalCoinUsed = order.getAvalCoinUsed();
        int unavalCoinUsed = order.getUnavalCoinUsed();
        String relatedId = String.valueOf(order.getId());
        
//        if(VersionUtil.compareVersion(version , "1.8.11") >= 0){
//        	userCoinService.updateUserCoin(userId, 0, 0, relatedId, time,
//        			UserCoinOperation.PURCHASE);
//        	
//        }else{
//        	
//        }
    	userCoinService.updateUserCoin(userId, -avalCoinUsed, -unavalCoinUsed, relatedId, time,
                    UserCoinOperation.CLOTHES_EXCHANGE, null, version);
    }
    
//    @Override
//    public void postOrderNewCreation(OrderNew order, String version) {
//    	long time = order.getCreateTime();
//    	long userId = order.getUserId();
//    	int avalCoinUsed = 0;
//    	int unavalCoinUsed = order.getCoinUsed();
//    	String relatedId = String.valueOf(order.getOrderNo());
//    	
//
//    	userCoinService.updateUserCoin(userId, -avalCoinUsed, -unavalCoinUsed, relatedId, time,
//    			UserCoinOperation.PURCHASE);
//    }
    //删除旧表
//    @Override
//    public void postOrderCancel(Order order,String version) {
//        long time = order.getCreateTime();
//        long userId = order.getUserId();
//        int avalCoinUsed = order.getAvalCoinUsed();
//        int unavalCoinUsed = order.getUnavalCoinUsed();
//        String relatedId = String.valueOf(order.getId());
//        userCoinService.updateUserCoin(userId, avalCoinUsed, unavalCoinUsed, relatedId, time, UserCoinOperation.ORDER_CANCEL, null, version);
//    }
    
    @Override
    public void postOrderNewCancel(OrderNew order, String version) {
    	long time = order.getCreateTime();
    	long userId = order.getUserId();
    	int avalCoinUsed = order.getCoinUsed();
    	int unavalCoinUsed = order.getCoinUsed();
    	String relatedId = String.valueOf(order.getOrderNo());
    	userCoinService.updateUserCoin(userId, avalCoinUsed, unavalCoinUsed, relatedId, time, UserCoinOperation.ORDER_CANCEL, null, version);
    }

}
