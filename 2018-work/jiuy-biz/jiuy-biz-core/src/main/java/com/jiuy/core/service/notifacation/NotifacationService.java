package com.jiuy.core.service.notifacation;

import java.util.List;

import com.jiuy.core.meta.notification.Notification;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.entity.query.PageQuery;

public interface NotifacationService {
	
	/**
	 * 商品更新發送系統通知
	 * @param productId 
	 */
	void updateProductSendNotification(long productId,List<Long> storeIdList);

	List<Notification> searchNotification(String title, PageQuery pageQuery);

	int searchNotificationCount(String title);
	
	/**
	 * 添加消息和消息用户关联信息
	 * @param image 可以是对应的存储ID或图片路径
	 * @param userId
	 * @param titleString 标题
	 * @param abstractsString	摘要
	 */
	public void addNotificationAndUserNotification(int type,String image,String linkUrl, long userId, String title, String abstracts);
	
	ResultCode addNotificationObj(Notification notification);


	Notification addNotification(Notification notification);
	
	ResultCode addNotificationBasicInfo(Notification notification);
	
	ResultCode addFullNotification(Notification notification);
	
	ResultCode addUserNotificationList(List<UserNotification> userNotifications);

	ResultCode rmNotification(long id);

	ResultCode updateNotification(Notification notification);

	void updateSendNotification(String appId, String appKey, String master, String host, long sEND_LENGTH) throws Exception;

	void putAwayProductSKUSendNotification(long productId,List<Long> storeIdList);
	/**
	 * 发送下架商品通知
	 * @param productId
	 * @param storeIdList
	 */
	void sendSoldoutProductSKUSendNotification(long productId,List<Long> storeIdList);

}
