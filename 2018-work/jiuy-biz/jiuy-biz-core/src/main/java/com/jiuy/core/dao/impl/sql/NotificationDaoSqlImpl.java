package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.NotificationDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.notification.Notification;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.entity.query.PageQuery;

public class NotificationDaoSqlImpl extends DomainDaoSqlSupport<Notification, Long> implements NotificationDao {

	@Override
	public List<Notification> searchNotification(String title, PageQuery pageQuery) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("title", title);
		params.put("pageQuery", pageQuery);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.searchNotification", params);
	}

	@Override
	public int searchNotificationCount(String title) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("title", title);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.searchNotificationCount", params);
	}

	
	@Override
	public Notification addNotification(Notification notification) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.addNotification", notification);
		return notification;
	}
	
	@Override
	public UserNotification addUserNotification(UserNotification userNotification) {
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.addUserNotification", userNotification);
		return userNotification;
	}
	
	@Override
	public Notification addNotificationBasicInfo(Notification notification) {
		getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.addNotificationBasicInfo", notification);
		return notification;
	}
	
	@Override
	public Notification addFullNotification(Notification notification) {
//		logger.error("dao  ddao linkurl:" + notification.getLinkUrl() );
		getSqlSession().insert("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.addFullNotification", notification);
		
		return notification;
	}
	
	@Override
	public int addUserNotificationList(List<UserNotification> notificationList) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("notificationList", notificationList);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.addUserNotificationList", params);
	}
	


	@Override
	public int rmNotification(long id, long updateTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		params.put("updateTime", updateTime);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.rmNotification", params);
	}

	@Override
	public int updateNotification(Notification notification) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.updateNotification", notification);
	}

	@Override
	public List<Notification> getPushingNotification(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.getPushingNotification", params);
	}

	@Override
	public List<UserNotification> getUserNotificationList(long notificationId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("notificationId", notificationId);
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.getUserNotificationList", notificationId);
	}

	@Override
	public int updatePushStatus(long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("id", id);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.NotificationDaoSqlImpl.updatePushStatus", params);
	}

}
