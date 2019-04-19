package com.yujj.business.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.jiuyuan.entity.notification.StoreNotification;
import com.jiuyuan.entity.notification.StoreUserNotification;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.CollectionUtil;
import com.yujj.dao.mapper.NotificationMapper;
import com.yujj.dao.mapper.StoreNotificationMapper;
import com.yujj.entity.StoreBusiness;
import com.yujj.entity.notification.Notification;
import com.yujj.util.asyn.annotation.AsynExecutable;

@Service
public class NotificationService {
	private static final Logger logger = Logger.getLogger(NotificationService.class);

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private StoreNotificationMapper storeNotificationMapper;
    
//    public int getAllCount() {
//    	return notificationMapper.getAllCount();
//	}	
//    
//    public List<Notification> getAllList(Long userId, PageQuery pageQuery) {
//        return notificationMapper.getAllList(userId, pageQuery);
//    }
//    getNoReadCount
    
    public int getNoReadCountByType(Long userId,long registerTime,int type) {
    	return notificationMapper.getNoReadCountByType(userId,registerTime,type);
	}	
    public int getNoReadCount(Long userId,long registerTime) {
    	return notificationMapper.getNoReadCount(userId,registerTime);
	}	
    
    
    public int getCountExclude910(Long userId,long registerTime) {
    	return notificationMapper.getCountExclude910(userId,registerTime);
	}	
    public List<Notification> getListExclude910(Long userId,long registerTime) {
        return notificationMapper.getAllListExclude910(userId,registerTime);
    }
    public List<Notification> getListExclude910(Long userId,long registerTime, PageQuery pageQuery) {
        return notificationMapper.getListExclude910(userId, registerTime, pageQuery);
    }
    
    public int getCountByType(Long userId,int type,long registerTime) {
    	return notificationMapper.getCountByType(userId,registerTime,type);
	}	
    
    public List<Notification> getListByType(Long userId,long registerTime, PageQuery pageQuery,int type) {
        return notificationMapper.getListByType(userId, registerTime, pageQuery,type);
    }
    
    public List<Notification> getListByType(Long userId,long registerTime,int type) {
        return notificationMapper.getAllListByType(userId,registerTime,type);
    }
    
    
    public int addUserNotification(List<UserNotification> userNotifications) {
    	return notificationMapper.addUserNotification(userNotifications);
    }

	

	@AsynExecutable
	public int updateNotificationPageView(Long[] ids) {
		Collection<Long> list = new ArrayList<Long>();
		CollectionUtils.addAll(list, ids);
		return notificationMapper.updateNotificationPageView(list);
	}
	
	//@AsynExecutable
	public int updateNotificationPageViewOfList(List<Long> list) {
		Collection<Long> collectionList = list;
		return notificationMapper.updateNotificationPageView(collectionList);
	}
	

	public void pushList(String appId, String appKey, String master, long sendLenth, Notification nf) throws Exception {
		String host = "http://sdk.open.api.igexin.com/apiex.htm";
				
		IGtPush push = new IGtPush(host, appKey, master);
    	
    	// 通知透传模板
    	TransmissionTemplate template = getTemplate(appId, appKey, nf);
    	
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);

        AppMessage messagea = new AppMessage();
        messagea.setData(template);
        messagea.setAppIdList(appIds);
        messagea.setOffline(true);
        messagea.setOfflineExpireTime(1000 * 600);

        IPushResult ret = push.pushMessageToApp(messagea);
        logger.info("appId:" + appId + " ,content: " + nf.toString());
	}


	@Transactional(rollbackFor = Exception.class)
	public void  pushCID(String appId, String appKey, String master, StoreNotification nf, StoreBusiness store) throws Exception {
		String host = "http://sdk.open.api.igexin.com/apiex.htm";

		
		storeNotificationMapper.addNotification(nf);
		StoreUserNotification storeUserNotification = new StoreUserNotification();
		storeUserNotification.setNotificationId(nf.getId());
		storeUserNotification.setPageView(0);
		storeUserNotification.setUserId(store.getId());
		storeUserNotification.setCreateTime(nf.getCreateTime());
		storeUserNotification.setUpdateTime(nf.getCreateTime());
		storeNotificationMapper.addUserNotification(CollectionUtil.createList(storeUserNotification));
		
		IGtPush push = new IGtPush(host, appKey, master);

    	// 通知透传模板
    	TransmissionTemplate template = getTemplate(appId, appKey, nf);
        
        SingleMessage simgleMessage = new SingleMessage();
        simgleMessage.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        simgleMessage.setOfflineExpireTime(24 * 3600 * 1000);
        simgleMessage.setData(template);
        simgleMessage.setPushNetWorkType(0); // 可选，判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制网络环境。
        
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(store.getUserCID());
        IPushResult ret = push.pushMessageToSingle(simgleMessage, target);
    	
        logger.error("appId:" + appId + " ,content: " + nf.toString());
        logger.error("--------------" + ret.getResponse().toString());
	}
	
	
    public static TransmissionTemplate getTemplate(String appId, String appKey, StoreNotification nf) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionType(2);
        template.setTransmissionContent(JSONObject.toJSONString(nf));
        APNPayload payload = new APNPayload();
        payload.setBadge(1);
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.addCustomMsg("content", JSONObject.toJSONString(nf));
//      简单模式APNPayload.SimpleMsg
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(nf.getAbstracts()));
//      字典模式使用下者
//      payload.setAlertMsg(getDictionaryAlertMsg(contentJson));
        
        template.setAPNInfo(payload);
        return template;
    }
	
    public static TransmissionTemplate getTemplate(String appId, String appKey, Notification nf) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionType(2);
        template.setTransmissionContent(JSONObject.toJSONString(nf));
        APNPayload payload = new APNPayload();
        payload.setBadge(1);
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.addCustomMsg("content", JSONObject.toJSONString(nf));
//      简单模式APNPayload.SimpleMsg
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(nf.getAbstracts()));
//      字典模式使用下者
//      payload.setAlertMsg(getDictionaryAlertMsg(contentJson));
        
        template.setAPNInfo(payload);
        return template;
    }	
}
