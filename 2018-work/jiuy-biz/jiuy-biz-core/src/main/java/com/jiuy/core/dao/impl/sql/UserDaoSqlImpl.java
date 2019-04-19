package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuyuan.constant.account.UserType;
import com.jiuy.core.dao.UserDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.account.User;

public class UserDaoSqlImpl extends DomainDaoSqlSupport<User, Long> implements UserDao {

	@Override
	public List<User> loadAll() {
		return getSqlSession().selectList("User.loadAll");
	}
	
	protected Class<User> getMetaClass() {
		return User.class;
	}

	@Override
	public User getByProperty(User user) {
		return getSqlSession().selectOne("User.getUserByProperty", user);
	}

	@Override
	public int updateUser(User user) {
		return getSqlSession().update("User.updateUserProperty", user);
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
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("userIds", userIds);

        return getSqlSession().selectList("User.userOfIds", params);
    }
}
