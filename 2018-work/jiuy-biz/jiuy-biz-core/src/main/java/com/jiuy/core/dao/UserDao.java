package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuy.core.meta.account.User;
import com.jiuyuan.constant.account.UserType;

public interface UserDao extends DomainDao<User, Long> {
	/****************
	 * 装载所有domain
	 * @return
	 */
	public List<User> loadAll();
	
	/**
	 * 根据用户的一个或多个属性值获取用户
	 * 
	 * @param user
	 * @return
	 */
	public User getByProperty(User user);

	/**
	 * 修改用户属性
	 * 
	 * @param user
	 * @return
	 */
	public int updateUser(User user);

    public User getUser(long userId);

    public User getUserByUserName(String userName);

    public User getUserByRelatedName(String relatedName, UserType userType);

    public int updateUserPassword(long userId, String password);

    public List<User> userOfIds(Set<Long> userIds);
}
