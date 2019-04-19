/**
 * 
 */
package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuy.core.dao.UserCoinDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;

/**
 * @author Never
 *
 */
public class UserCoinDaoSqlImpl extends DomainDaoSqlSupport<UserCoin, Long>  implements UserCoinDao{

    @Override
    public UserCoin getUserCoin(long userId) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId", userId);
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.UserCoinDaoSqlImpl.selectUserCoin", params);
    }

    @Override
    public int addUserCoin(UserCoin userCoin) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userCoin", userCoin);
        return getSqlSession().insert("com.jiuy.core.dao.impl.sql.UserCoinDaoSqlImpl.addUserCoin",params);
    }

    @Override
    public int updateUserCoin(long userId, int newAvalCoins, int oldAvalCoins, int newUnavalCoins, int oldUnavalCoins,
                              long time) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId", userId);
        params.put("newAvalCoins", newAvalCoins);
        params.put("oldAvalCoins", oldAvalCoins);
        params.put("newUnavalCoins", newUnavalCoins);
        params.put("oldUnavalCoins", oldUnavalCoins);
        params.put("time", time);
        return getSqlSession().update("com.jiuy.core.dao.impl.sql.UserCoinDaoSqlImpl.updateUserCoin",params);
    }

	@Override
	public int addAndUpdateUserCoin(Collection<Long> userIds, int avalCoins, int increaseCoins, long time) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userIds", userIds);
		params.put("avalCoins", avalCoins);
		params.put("increaseCoins", increaseCoins);
		params.put("time", time);
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.UserCoinDaoSqlImpl.addAndUpdateUserCoin",params);
	}
	
	@Override
	public Integer getTotalUserCoin(long invitorId, long startTime, long endTime, UserCoinOperation operation) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		
		params.put("invitorId", invitorId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("operation", operation);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.UserCoinDaoSqlImpl.getTotalUserCoin", params);
	}

}
