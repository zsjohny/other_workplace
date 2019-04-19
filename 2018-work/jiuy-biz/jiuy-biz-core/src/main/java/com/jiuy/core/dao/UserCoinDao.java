/**
 * 
 */
package com.jiuy.core.dao;

import java.util.Collection;

import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.account.UserCoin;

/**
 * @author Never
 *
 */
public interface UserCoinDao {

    UserCoin getUserCoin(long userId);

    int addUserCoin(UserCoin userCoin);

    int updateUserCoin(long userId, int newAvalCoins, int oldAvalCoins, int newUnavalCoins, int oldUnavalCoins,
                       long time);
    
    public int addAndUpdateUserCoin(Collection<Long> userIds,int avalCoins,int increaseCoins,long time);

	Integer getTotalUserCoin(long invitorId, long startTime, long endTime, UserCoinOperation operation);
}
