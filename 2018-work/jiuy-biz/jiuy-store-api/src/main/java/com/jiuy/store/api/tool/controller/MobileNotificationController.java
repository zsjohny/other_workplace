package com.jiuy.store.api.tool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.ShopNotification;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.MemberService;
import com.store.service.NotificationService;

/**
 * 系统通知相关Controller
 * @author zhuzl
 *
 */
@Controller
@Login
@RequestMapping("/mobile/notification")
public class MobileNotificationController {
	
	private static final Logger logger = Logger.getLogger(MobileNotificationController.class);
	
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private MemberService memberService;
	
    /**
     * 获取消息列表
     * 说明：会排除物流信息和售后信息
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/list/auth")
    @ResponseBody
	public JsonResponse list(PageQuery pageQuery, UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	long storeId = userDetail.getId();
    	StoreBusiness storeBusiness = userDetail.getUserDetail();
    	long registerTime = storeBusiness.getCreateTime();
    	//1、获取总页数,获取的是用户需要看且并没有看的消息
       // int totalCount = notificationService.getCountExclude910(storeId,registerTime);
         int totalCount = notificationService.getNotificationAllCount(storeId,registerTime);
       
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        
        //2、获取列表,获取的是用户需要看且并没有看的消息 分页
    	//List<ShopNotification> notificationList = notificationService.getListExclude910(storeId,registerTime, pageQuery);
    	List<ShopNotification> notificationList = notificationService.getNotificationList(storeId,registerTime, pageQuery);
    	data.put("data", notificationList);
    	
    	//3、增加消息读取数量,表示以上消息已经看过notificationList
    	addReadCount(storeBusiness);
    	
    	//获取门店客服数量,平台客服数量前端可以通过七鱼获取
    	int storeNoReadCount = memberService.getAllNoReadCount(storeBusiness.getId());
    	data.put("storeNoReadCount", storeNoReadCount);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 删除消息列表
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/deleteNotification/auth")
    @ResponseBody
	public JsonResponse deleteNotification(@RequestParam(value = "notificationIds") String notificationIds,
			UserDetail<StoreBusiness> userDetail) {
    	logger.info("删除消息列表:消息IDs:"+notificationIds);
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	StoreBusiness storeBusiness = userDetail.getUserDetail();
    	if(storeId==0 || storeBusiness==null){
    		return jsonResponse.setError("用户不能为空");
    	}
    	try {
    		notificationService.deleteNotification(notificationIds,storeId);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error("删除消息列表:"+e.getMessage());
			return jsonResponse.setError("删除消息列表:"+e.getMessage());
		}
    }
    
    /**
     * 清空消息列表
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/deleteAllNotification/auth")
    @ResponseBody
	public JsonResponse deleteAllNotification(UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	StoreBusiness storeBusiness = userDetail.getUserDetail();
    	if(storeId==0 || storeBusiness==null){
    		return jsonResponse.setError("用户不能为空");
    	}
    	try {
    		List<ShopNotification> notificationList = notificationService.getNotificationList(storeId,storeBusiness.getCreateTime(), null);
    		notificationService.deleteAllNotification(notificationList,storeId);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error("清空消息列表:"+e.getMessage());
			return jsonResponse.setError("清空消息列表:"+e.getMessage());
		}
    }
    
    /**
     * 增加消息读取数量
     * @param 
     * @param userId
     * @return
     */
    private int addReadCount(StoreBusiness storeBusiness){
    	long registerTime = storeBusiness.getCreateTime();
    	//获取的是用户需要看且并没有看的消息
    	List<ShopNotification> notificationList = notificationService.getListExclude910(storeBusiness.getId(),registerTime);
    	return notificationListAddReadCount(notificationList,storeBusiness.getId());
    }
    
    /**
     * 增加消息读取数量
     * @param notificationList
     * @param userId
     * @return
     */
    private int notificationListAddReadCount(List<ShopNotification> notificationList,long userId){
 	   List<Long> ids = new ArrayList<Long>();
 	   //添加所有该用户未看的消息
 	   for(ShopNotification notification : notificationList){
 	   		ids.add(notification.getId());
 	   }
 	   int nRet = 0;
 	   if (ids != null && ids.size() > 0){
 		    long time = System.currentTimeMillis();
 	    	List<UserNotification> userNotifications = new ArrayList<UserNotification>();
 	    	for (Long id : ids) {
 	    		UserNotification userNotification = new UserNotification();
 	    		userNotification.setUserId(userId);
 	    		userNotification.setNotificationId(id);
 	    		userNotification.setCreateTime(time);
 	    		userNotification.setUpdateTime(time);
 	    		userNotifications.add(userNotification);
 	    	}
 	    	//shop_notification的消息放进shop_user_notification中，表示该用户需要看的信息
 	    	nRet = notificationService.addUserNotification(userNotifications);
 	    	//设置为已读
 	    	nRet = notificationService.updateNotificationPageViewOfList(ids);
 	    	if(nRet > 0){
 	    	    nRet = 0;
 	    	}
 	   }
 	   return nRet;
    }
}