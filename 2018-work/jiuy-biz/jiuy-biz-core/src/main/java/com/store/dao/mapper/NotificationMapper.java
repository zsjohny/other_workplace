/**
 * 
 */
package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ShopNotification;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author Dongzhong
 *
 */
@DBMaster
public interface NotificationMapper {
	
	public int getCountExclude910(@Param("userId") Long userId, @Param("registerTime") Long registerTime,@Param("keyWord") String keyWord);
	
	public List<ShopNotification> getAllListExclude910(@Param("storeId") Long userId, @Param("registerTime") Long registerTime,@Param("keyWord") String keyWord);
	
	public List<ShopNotification> getListExclude910(@Param("storeId") Long storeId, @Param("registerTime") Long registerTime, 
			@Param("pageQuery") PageQuery pageQuery,@Param("keyWord") String keyWord);
	
	public int addUserNotification(@Param("userNotifications") List<UserNotification> userNotifications);
	
	public int updateNotificationPageView(@Param("ids") Collection<Long> ids);

	public int updatePushStatus(@Param("id") long id);

	public List<ShopNotification> getPushingNotification(@Param("startTime")long startTime,@Param("endTime") long endTime);
	
	public int addNotification(@Param("shopNotification") ShopNotification shopNotification);

	int addNotificationSpecial(@Param("shopNotification") ShopNotification shopNotification);

	public List<ShopNotification> getNotificationList(@Param("storeId")long storeId, @Param("registerTime")long registerTime, 
			@Param("pageQuery")PageQuery pageQuery,@Param("keyWord")String keyWord);

	public int getNotificationAllCount(@Param("storeId")long storeId, @Param("registerTime")long registerTime,@Param("keyWord")String keyWord);

	public void deleteNotification(@Param("userNotificationList")List<UserNotification> userNotificationList, @Param("storeId")long storeId);

}