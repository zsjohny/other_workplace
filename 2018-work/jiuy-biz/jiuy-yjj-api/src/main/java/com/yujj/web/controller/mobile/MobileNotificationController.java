package com.yujj.web.controller.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;

import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.NotificationService;
import com.yujj.business.service.UserService;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.notification.Notification;

@Controller
@Login
@RequestMapping("/mobile/message")
public class MobileNotificationController {
    @Autowired
    private NotificationService notificationService;
	
	@Autowired
	private UserService userService;
    
    /**
     * V2.1代替/newcount接口
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @NoLogin
    @RequestMapping(value = "/getAllNewCount", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getAllNewCount(UserDetail userDetail) {
    	long userId = userDetail.getUserId();
    	int count = 0;
    	JsonResponse jsonResponse = new JsonResponse();
    	if(userId != 0){
    		User user = userService.getUser(userId);
    		if(user != null){
    			long registerTime = user.getCreateTime();
           	 count = notificationService.getNoReadCount(userId,registerTime);
    		}
    	}
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("newcount", count);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 获取消息列表
     * 说明：会排除物流信息和售后信息
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse list(PageQuery pageQuery, UserDetail userDetail) {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	long userId = userDetail.getUserId();
    	User user = userService.getUser(userId);
    	long registerTime = user.getCreateTime();
    	//1、获取总页数
        int totalCount = notificationService.getCountExclude910(userId,registerTime);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        
        //2、获取列表
    	List<Notification> notificationList = notificationService.getListExclude910(userId,registerTime, pageQuery);
    	data.put("data", notificationList);
    	
    	//3、增加消息读取数量
    	addReadCount(userId);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    /**
     * V2.0的接口，为了数据兼容该接口保留，V2.1被/getAllNewCount接口代替
     * 说明：当V2.0客户端废弃后该接口即可废弃
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @NoLogin
    @RequestMapping(value = "/newcount", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse newcount() {
    	JsonResponse jsonResponse = new JsonResponse();
    	int count = 0;//notificationService.get01234Count();
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("newcount", count);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    
    /**
     * 获取消息类型列表（只要是物流消息和售后消息）
     * V2.1接口
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/getTypeList", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getTypeList(UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	List<Map<String, String>> typeList = new ArrayList<Map<String, String>>();
    	long userId = userDetail.getUserId();
    	User user = userService.getUser(userId);
    	long registerTime = user.getCreateTime();
    	
    	int wuLiuNoReadCount = notificationService.getNoReadCountByType(userId,registerTime,9);
    	int shouHouNoReadCount = notificationService.getNoReadCountByType(userId,registerTime,10);
    	
    	//物流消息
    	Map<String, String> wuLiu = new HashMap<String, String>();
    	wuLiu.put("typeCode", "9");//类型代码
    	wuLiu.put("typeName", "物流消息");//类型名称
    	wuLiu.put("iconUrl", "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/message/express.png");//图标地址
    	wuLiu.put("noReadCount", String.valueOf(wuLiuNoReadCount));//未读消息条数 
    	typeList.add(wuLiu);
    	Map<String, String> shouHou = new HashMap<String, String>();
    	//售后通知
    	shouHou.put("typeCode", "10");//类型代码
    	shouHou.put("typeName", "售后通知");//类型名称
    	shouHou.put("iconUrl", "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/message/aftersale.png");//图标地址
    	shouHou.put("noReadCount", String.valueOf(shouHouNoReadCount));//未读消息条数 
    	typeList.add(shouHou);
    	return jsonResponse.setSuccessful().setData(typeList);
    }
    
    /**
     * 根据类型获取消息（用于获取物流消息列表和售后消息列表）
     * V2.1接口
     * @param pageQuery
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/getListByType", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse get01234NotificationList(PageQuery pageQuery, UserDetail userDetail,int type) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getUserId();
    	User user = userService.getUser(userId);
    	long registerTime = user.getCreateTime();
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	//1、获取总页数
        int totalCount = notificationService.getCountByType(userId,type,registerTime);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        
        //2、获取列表
    	List<Notification> notificationList = notificationService.getListByType(userId,registerTime, pageQuery,type);
    	data.put("data", notificationList);
    	
    	//3、增加消息读取数量
    	addReadCountByType(userId,type);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    
    /**
     * 记录消息阅读次数
     * V2.1根据需求记录已读数量改为加载列表即为已读，则该接口V2.1版本以后废弃。
     * @param ids
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse confirmNotification(Long[] ids, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	int nRet = 0;
    	if (ids == null || ids.length <= 0) return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
    	
    	long time = System.currentTimeMillis();
    	
    	List<UserNotification> userNotifications = new ArrayList<UserNotification>();
    	for (Long id : ids) {
    		UserNotification userNotification = new UserNotification();
    		userNotification.setUserId(userDetail.getUserId());
    		userNotification.setNotificationId(id);
    		userNotification.setCreateTime(time);
    		userNotification.setUpdateTime(time);
    		userNotifications.add(userNotification);
    	}
    	
    	nRet = notificationService.addUserNotification(userNotifications);
    	nRet = notificationService.updateNotificationPageView(ids);
    	if(nRet > 0){
    	    nRet = 0;
    	}
    	return jsonResponse.setSuccessful().setCode(nRet);
    }
    /**
     * 增加消息读取数量
     * @param 
     * @param userId
     * @return
     */
    private int addReadCount(long userId){
    	User user = userService.getUser(userId);
    	long registerTime = user.getCreateTime();
    	
    	  List<Notification> notificationList = notificationService.getListExclude910(userId,registerTime);
    	  return notificationListAddReadCount(notificationList,userId);
    }
    
    /**
     * 增加消息读取数量
     * @param 
     * @param userId
     * @return
     */
    private int addReadCountByType(long userId,int type){
    	User user = userService.getUser(userId);
    	long registerTime = user.getCreateTime();
    	
    	 List<Notification> notificationList = notificationService.getListByType(userId,registerTime,type);
    	 return notificationListAddReadCount(notificationList,userId);
    }
    
   /**
    * 增加消息读取数量
    * @param type 9(物流消息)、10（售后消息）、-1其他消息
    * @param userId
    * @return
    */
   private int notificationListAddReadCount(List<Notification> notificationList,long userId){
	   
	   List<Long> ids = new ArrayList<Long>();
	   for(Notification notification : notificationList){
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
	    	nRet = notificationService.addUserNotification(userNotifications);
	    	nRet = notificationService.updateNotificationPageViewOfList(ids);
	    	if(nRet > 0){
	    	    nRet = 0;
	    	}
	   }
	   return nRet;
   }
    
    @NoLogin
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse test(Long[] ids) {
        JsonResponse jsonResponse = new JsonResponse();
        int nRet = 0;
        nRet = notificationService.updateNotificationPageView(ids);
        if(nRet > 0){
            nRet = 0;
        }
        return jsonResponse.setSuccessful().setCode(nRet);
    }
}
