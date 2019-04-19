package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.meta.notification.Notification;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.entity.query.PageQuery;

public interface NotificationDao {

	List<Notification> searchNotification(String title, PageQuery pageQuery);

	int searchNotificationCount(String title);

	Notification addNotification(Notification notification);
	
	Notification addNotificationBasicInfo(Notification notification);
	

	UserNotification addUserNotification(UserNotification userNotification);
	
	Notification addFullNotification(Notification notification);

	int addUserNotificationList(List<UserNotification> userNotificationList);

	int rmNotification(long id, long updateTime);

	int updateNotification(Notification notification);

	List<Notification> getPushingNotification(long startTime, long endTime);
	
	List<UserNotification> getUserNotificationList(long notificationId);

	int updatePushStatus(long id);

}
