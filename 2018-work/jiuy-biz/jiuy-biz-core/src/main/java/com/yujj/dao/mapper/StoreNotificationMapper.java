/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.notification.StoreNotification;
import com.jiuyuan.entity.notification.StoreUserNotification;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author Dongzhong
 *
 */
@DBMaster
public interface StoreNotificationMapper {
    public List<StoreNotification> getNotificationList(@Param("userId") Long userId, @Param("pageQuery") PageQuery pageQuery);
    public int addUserNotification(@Param("userNotifications") List<StoreUserNotification> userNotifications);
    
    public int addNotification(StoreNotification notification);

    public int getAllNotificationCount();
	public int updateNotificationPageView(@Param("ids") Collection<Long> ids);
}
