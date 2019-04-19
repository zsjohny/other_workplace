package com.store.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jiuy.core.meta.notification.Notification;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.newentity.ShopNotification;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.entity.query.PageQuery;
import com.store.dao.mapper.NotificationMapper;

@Service
public class NotificationService{
    private static final Logger logger = Logger.getLogger(NotificationService.class);

    @Autowired
    private NotificationMapper notificationMapper;

//    @Autowired
//    private StoreNotificationMapper storeNotificationMapper;

    public int getCountExclude910(Long userId, long registerTime) {
        String keyWord = "" + userId;
        return notificationMapper.getCountExclude910(userId, registerTime, keyWord);
    }

    public List<ShopNotification> getListExclude910(Long userId, long registerTime) {
        String keyWord = "" + userId;
        return notificationMapper.getAllListExclude910(userId, registerTime, keyWord);
    }

    public List<ShopNotification> getListExclude910(Long storeId, long registerTime, PageQuery pageQuery) {
        String keyWord = "" + storeId;
        return notificationMapper.getListExclude910(storeId, registerTime, pageQuery, keyWord);
    }

    public int addUserNotification(List<UserNotification> userNotifications) {
        return notificationMapper.addUserNotification(userNotifications);
    }

    //@AsynExecutable
    public int updateNotificationPageViewOfList(List<Long> list) {
        Collection<Long> collectionList = list;
        return notificationMapper.updateNotificationPageView(collectionList);
    }

    public List<ShopNotification> getNotificationList(long storeId, long registerTime, PageQuery pageQuery) {
        String keyWord = "" + storeId;
        return notificationMapper.getNotificationList(storeId, registerTime, pageQuery, keyWord);
    }

    public int getNotificationAllCount(long storeId, long registerTime) {
        String keyWord = "" + storeId;
        return notificationMapper.getNotificationAllCount(storeId, registerTime, keyWord);
    }

    /**
     * 删除消息列表
     *
     * @param notificationIds
     * @param storeId
     */
    public void deleteNotification(String notificationIds, long storeId) {
        String[] notificationIdArr = notificationIds.split(",");
        List<UserNotification> userNotificationList = new ArrayList<UserNotification>();
        for (String id : notificationIdArr) {
            long notificationId = Long.parseLong(id);
            UserNotification userNotification = new UserNotification();
            userNotification.setNotificationId(notificationId);
            userNotification.setPageView(1);
            userNotification.setCreateTime(System.currentTimeMillis());
            userNotification.setUpdateTime(userNotification.getCreateTime());
            userNotification.setUserId(storeId);
            userNotification.setStatus(- 1);
            userNotificationList.add(userNotification);
        }
        notificationMapper.deleteNotification(userNotificationList, storeId);
    }

    /**
     * 清空消息列表
     *
     * @param notificationList
     * @param storeId
     */
    public void deleteAllNotification(List<ShopNotification> notificationList, long storeId) {
        List<UserNotification> userNotificationList = new ArrayList<UserNotification>();
        if (notificationList != null && notificationList.size() > 0) {
            for (ShopNotification shopNotification : notificationList) {
                UserNotification userNotification = new UserNotification();
                userNotification.setNotificationId(shopNotification.getId());
                userNotification.setPageView(1);
                userNotification.setCreateTime(System.currentTimeMillis());
                userNotification.setUpdateTime(userNotification.getCreateTime());
                userNotification.setUserId(storeId);
                userNotification.setStatus(- 1);
                userNotificationList.add(userNotification);
            }
            notificationMapper.deleteNotification(userNotificationList, storeId);
        }
    }


    /**
     * 插入一个对象
     *
     * @param: notification
     * @return: void
     * @auther: Charlie(唐静)
     * @date: 2018/5/24 7:22
     */
    public void addNotification(ShopNotification notification) {
        notificationMapper.addNotificationSpecial(notification);
    }


//  	//发送系统通知
//  	@Transactional(rollbackFor = Exception.class)
//	public void updateSendNotification(String appId, String appKey, String master, String host, long sendLenth) throws Exception {
//		long currentTime = System.currentTimeMillis();
//		long startTime = currentTime - sendLenth;
//		long endTime = currentTime + sendLenth;
//        List<Notification> notifications = notificationMapper.getPushingNotification(startTime,endTime);
////        logger.info("检查是否有消息要推送currentTime："+ currentTime+",notifications.size():"+notifications.size());
////        logger.info("startTime:"+ startTime+",endTime:"+endTime);
//		for(Notification nf : notifications) {
//			String type = nf.getType();//9：物流 10：售后
//			long notificationId = nf.getId();
//			logger.info("物流信息或售后信息发送推送开始,notificationId:"+notificationId);
//            String title = nf.getTitle();
//            String abstracts = nf.getAbstracts();
//            String linkUrl = nf.getLinkUrl();
//            String image = nf.getImage();
//            String pushTime = String.valueOf(currentTime);
//            List<String> cidList = getCidList(notificationId);
//			boolean ret = GetuiUtil.pushGeTui(cidList,title, abstracts, linkUrl, image, type , pushTime);
//			if(ret){
//				notificationMapper.updatePushStatus(nf.getId());
//			}
////			logger.info("物流信息或售后信息发送推送完成，notificationId："+notificationId+",发送用户集合："+cidList.toString());
//		}
////		logger.info("检查到的消息推送完成");
//	}
}