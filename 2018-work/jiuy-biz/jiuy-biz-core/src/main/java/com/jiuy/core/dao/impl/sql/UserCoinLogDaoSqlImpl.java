/**
 * 
 */
package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;

import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuy.core.dao.UserCoinLogDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuy.core.meta.account.UserCoinLog;

/**
 * @author Never
 *
 */
public class UserCoinLogDaoSqlImpl extends DomainDaoSqlSupport<UserCoinLog, Long> implements UserCoinLogDao {

    @Override
    public int addUserCoinLog(UserCoinLog userCoinLog) {
    	return getSqlSession().insert("com.jiuy.core.dao.impl.sql.UserCoinLogDaoSqlImpl.addUserCoinLog", userCoinLog);
    }

    @Override
    public int getUserCoinLogCount(long userId, long startTime, long endTime, UserCoinOperation operation) {
      
        return 0;
    }

	@Override
	public int addGrantUserCoinLog(int operation, int newUnavalCoins, long relatedId,
			long createTime, String content, int type) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("operation", operation);
		params.put("newUnavalCoins", newUnavalCoins);
		params.put("relatedId", relatedId);
		params.put("createTime", createTime);
		params.put("content", content);
		params.put("type", type);
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.UserCoinLogDaoSqlImpl.addGrantUserCoinLog", params);
	}


}
