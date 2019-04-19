package com.jiuy.core.service.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.coupon.Coupon;
import com.jiuy.core.meta.notification.Notification;
import com.jiuy.core.service.UserManageService;
import com.jiuy.core.service.coupon.CouponService;
import com.jiuy.core.service.notifacation.NotifacationService;
//import com.jiuy.web.controller.NotifacationController;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.constant.LinkType;
import com.jiuyuan.constant.coupon.CouponPushStatus;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.util.GetuiUtil;

public class CouponNotificationJob {

    private static final Logger logger = Logger.getLogger(CouponNotificationJob.class);
	
    @Autowired
    private CouponService couponService;
    
	@Resource
	private NotifacationService notificationServiceImpl;

    @Autowired
    private UserManageService userManageService;
    
    public void execute() throws Exception {
        List<Coupon> coupons =couponService.search(0);
        
        Map<Long, User> userMap = new HashMap<Long, User>();
        Set<Long> yjjNumbers = new HashSet<Long>();
        Set<Coupon> allMemberCoupon = new HashSet<Coupon>();
        for(Coupon coupon : coupons) {
        	if(coupon.getType() != null && coupon.getType().intValue() == 1){
        		allMemberCoupon.add(coupon);
        	}else{
        		yjjNumbers.add(coupon.getyJJNumber());
        	}
        }
        userMap = userManageService.usersMapOfYJJNumbers(yjjNumbers);
        
        if (userMap.values().size() < 1 && allMemberCoupon.size() < 1)
			return;
        
        Set<Long> successCouponIds = new HashSet<Long>();
        Set<Long> failCouponIds = new HashSet<Long>();
        Set<Long> pushingCouponIds = new HashSet<Long>();
        
        long currentTime = System.currentTimeMillis();
    
        List<UserNotification> userNotificationList = new ArrayList<>();
        for(Coupon coupon : coupons) {
        	JSONObject jsonObject = new JSONObject();
    	    jsonObject.put("title", coupon.getPushTitle());
    	    jsonObject.put("abstracts", coupon.getPushDescription());
    	    jsonObject.put("linkUrl", coupon.getPushUrl());
    	    jsonObject.put("image", coupon.getPushImage());
    	    jsonObject.put("type", LinkType.COUPON.getIntValue());
    	    jsonObject.put("pushTime", currentTime);
            User user = userMap.get(coupon.getyJJNumber());
                       
            long couponId = coupon.getId();

            String result = "";
            
            if (coupon.getType() == 1) {

//            	result = GetuiUtil.pushGeTui(null, jsonObject);
            	Notification no = new Notification();
            	no.setTitle(coupon.getPushTitle());
            	no.setAbstracts(coupon.getPushDescription());
            	no.setLinkUrl(coupon.getPushUrl());
            	no.setImage(coupon.getPushImage() == null ? "" : coupon.getPushImage());
            	no.setPushStatus(0);  // 正式设为	no.setPushStatus(0);  
            	no.setType("" + LinkType.COUPON.getIntValue()); //优惠券
            	no.setStatus(0);
            	no.setCreateTime(currentTime);
            	no.setUpdateTime(currentTime);
            	no.setPushTime(currentTime);
//            	logger.error("job linkurl:" + no.getLinkUrl() );
            	notificationServiceImpl.addFullNotification(no);
            	pushingCouponIds.add(couponId);

			} else if (coupon.getType() == 0 && user == null) {
            	logger.error("CouponNotificationJob Error" + coupon.getyJJNumber() + "找不到对应user");
				continue;
			} else if (coupon.getType() == 0 && user != null){
				result = GetuiUtil.pushGeTui(CollectionUtil.createList(user.getUserCID()), jsonObject);
				//增加推送内容
				//指定用户优惠券代码。暂时注销170328   
				Notification no = new Notification();
            	no.setTitle(coupon.getPushTitle());
            	no.setAbstracts(coupon.getPushDescription());
            	no.setLinkUrl(coupon.getPushUrl());
            	no.setImage(coupon.getPushImage() == null ? "" : coupon.getPushImage());
            	no.setPushStatus(1);  
            	no.setType("" + LinkType.DESIGNATE_COUPON.getIntValue()); //优惠券
            	no.setStatus(0);
            	no.setCreateTime(currentTime);
            	no.setUpdateTime(currentTime);
            	no.setPushTime(currentTime);
            	notificationServiceImpl.addFullNotification(no);
            	
            	UserNotification userNotification = new UserNotification();
            	userNotification.setCreateTime(currentTime);
            	userNotification.setUpdateTime(currentTime);
            	userNotification.setUserId(user.getUserId());
            	userNotification.setNotificationId(no.getId());
            	userNotificationList.add(userNotification);	
			}

            if (!StringUtils.contains(result, "{result=ok,")) {
            	failCouponIds.add(couponId);
            	logger.error("CouponNotificationJob Error" + couponId + "推送失败。result：" + result);
			} else {
				successCouponIds.add(couponId);
			}
        }
        couponService.updatePushStatus(successCouponIds, CouponPushStatus.PUSHED.getIntValue(), currentTime);
        couponService.updatePushStatus(failCouponIds, CouponPushStatus.FAIL.getIntValue(), currentTime);
        couponService.updatePushStatus(pushingCouponIds, CouponPushStatus.PUSHED.getIntValue(), currentTime);
 //指定用户优惠券代码。暂时注销170328       
//        if(userNotificationList.size() > 0){
//        	notificationServiceImpl.addUserNotificationList(userNotificationList);
//        }
        
    }

}
