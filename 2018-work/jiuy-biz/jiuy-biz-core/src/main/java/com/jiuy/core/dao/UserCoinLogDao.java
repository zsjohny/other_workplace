/**
 * 
 */
package com.jiuy.core.dao;

import com.jiuy.core.meta.account.UserCoinLog;
import com.jiuyuan.constant.account.UserCoinOperation;

/**
 * @author Never
 *
 */
public interface UserCoinLogDao {

	int addUserCoinLog(UserCoinLog userCoinLog);

    int getUserCoinLogCount(long userId, long startTime, long endTime, UserCoinOperation operation);
    
    int addGrantUserCoinLog(int operation,int newUnavalCoins,long relatedId,long createTime,String content,int type);

}
