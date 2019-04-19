package com.jiuy.core.dao.impl.cache;

import java.util.List;
import java.util.Set;

import com.jiuyuan.constant.account.UserType;
import com.jiuy.core.dao.UserDao;
import com.jiuy.core.dao.impl.sql.UserDaoSqlImpl;
import com.jiuy.core.dao.support.DomainDaoCacheSupport;
import com.jiuy.core.meta.account.User;

public class UserDaoCacheImpl extends DomainDaoCacheSupport<User, Long, UserDaoSqlImpl>
		implements UserDao {

	@Override
	public List<User> loadAll() {
		return this.sqlDao.loadAll();
	}

	@Override
	public User add(User obj) {
		return sqlDao.add(obj);
	}

	@Override
	public User getById(Long id) {
		User user = this.getObjectFromCache(id);
		if(user == null)
		{
			user = sqlDao.getById(id);
			if(user != null) {
				this.saveObjectIntoCache(user);
			}
		}
		return user;
	}

	@Override
	public List<User> listByIds(Long[] ids) {
		List<User> users = this.getObjectsFromCache(ids);
		if(users == null || users.size() != ids.length) {
			users = sqlDao.listByIds(ids);
			for(User user : users) {
				this.saveObjectIntoCache(user);
			}
		}
		return users;
	}

	@Override
	public int deleteByIds(Long... ids) {
		int count = sqlDao.deleteByIds(ids);
		if(count > 0) {
			this.deleteObjectsFromCache(ids);
		}
		return count;
	}

	/**
	 * 处理逻辑：
	 * 1）传入的查询条件为空，则直接返回null
	 * 2）传入的查询条件带id,则使用id进行（缓存或者数据库）查询，否则直接从数据库进行查询
	 */
	@Override
	public User getByProperty(User user) {
		if(null != user){
			if(user.getUserId() > 0){
				return this.getById(user.getUserId());
			}else{
				User yjjUser = sqlDao.getByProperty(user);
				return yjjUser;
			}
		}
		return null;
	}

    @Override
    public int updateUser(User user) {
        return 0;
    }

    @Override
    public User getUser(long userId) {
        return null;
    }

    @Override
    public User getUserByUserName(String userName) {
        return null;
    }

    @Override
    public User getUserByRelatedName(String relatedName, UserType userType) {
        return null;
    }

    @Override
    public int updateUserPassword(long userId, String password) {
        return 0;
    }

    @Override
    public List<User> userOfIds(Set<Long> userIds) {
        return null;
    }

}
