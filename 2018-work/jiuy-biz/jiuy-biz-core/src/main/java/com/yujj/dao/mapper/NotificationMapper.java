/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.entity.notification.Notification;

/**
 * @author Dongzhong
 *
 */
@DBMaster
public interface NotificationMapper {
	
	 public int getNoReadCount(@Param("userId") Long userId, @Param("registerTime") Long registerTime);
	 public int getNoReadCountByType(@Param("userId") Long userId, @Param("registerTime") Long registerTime,@Param("type") Integer type);
	    

	 public List<Notification> getAllListExclude910(@Param("userId") Long userId, @Param("registerTime") Long registerTime);
	 public List<Notification> getListExclude910(@Param("userId") Long userId, @Param("registerTime") Long registerTime, @Param("pageQuery") PageQuery pageQuery);
	 public int getCountExclude910(@Param("userId") Long userId, @Param("registerTime") Long registerTime);

    public List<Notification> getAllListByType(@Param("userId") Long userId, @Param("registerTime") Long registerTime,@Param("type") Integer type);
    public List<Notification> getListByType(@Param("userId") Long userId, @Param("registerTime") Long registerTime, @Param("pageQuery") PageQuery pageQuery,@Param("type") Integer type);
    public int getCountByType(@Param("userId") Long userId, @Param("registerTime") Long registerTime, @Param("type") Integer type);
    
    
    public int addUserNotification(@Param("userNotifications") List<UserNotification> userNotifications);
	
	public int updateNotificationPageView(@Param("ids") Collection<Long> ids);
}
