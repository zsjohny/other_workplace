package com.jiuy.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuy.core.dao.UserCoinDao;
import com.jiuy.core.dao.UserCoinLogDao;
import com.jiuy.core.meta.account.UserCoinLog;

@Service
public class UserCoinService {
    @Autowired
    private UserCoinDao userCoinDao;

    @Autowired
    private UserCoinLogDao userCoinLogDao;

    public UserCoin getUserCoin(long userId) {
        return userCoinDao.getUserCoin(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserCoin(long userId, int diffAvalCoins, int diffUnavalCoins, String relatedId, long time,
                               UserCoinOperation operation) {
		
    	if (diffUnavalCoins == 0) {
			return;
		}
    	
    	String msg =
            new StringBuilder("update user coin error!").append("userId:" + userId).append(", ").append(
                "operation:" + operation).append(", ").append("diffAvalCoins:" + diffAvalCoins).append(", ").append(
                "diffUnavalCoins:" + diffUnavalCoins).toString();

        int oldAvalCoins = 0;
        int oldUnavalCoins = 0;
        UserCoin userCoin = getUserCoin(userId);
        if (userCoin != null) {
            oldAvalCoins = userCoin.getAvalCoins();
            oldUnavalCoins = userCoin.getUnavalCoins();
        }
        int newAvalCoins = oldAvalCoins + diffAvalCoins;
        int newUnavalCoins = oldUnavalCoins + diffUnavalCoins;
        if (newAvalCoins < 0 || newUnavalCoins < 0) {
            throw new IllegalArgumentException(msg);
        }

        int count = 0;
        if (userCoin == null) {
            userCoin = new UserCoin();
            userCoin.setAvalCoins(newAvalCoins);
            userCoin.setUnavalCoins(newUnavalCoins);
            userCoin.setUserId(userId);
            userCoin.setCreateTime(time);
            userCoin.setUpdateTime(time);
            count = userCoinDao.addUserCoin(userCoin);
        } else {
            count =
                userCoinDao.updateUserCoin(userId, newAvalCoins, oldAvalCoins, newUnavalCoins, oldUnavalCoins, time);
        }
        if (count != 1) {
            throw new IllegalStateException(msg);
        }

        UserCoinLog userCoinLog = new UserCoinLog();
        userCoinLog.setUserId(userId);
        userCoinLog.setOperation(operation);
        userCoinLog.setOldAvalCoins(oldAvalCoins);
        userCoinLog.setNewAvalCoins(newAvalCoins);
        userCoinLog.setOldUnavalCoins(oldUnavalCoins);
        userCoinLog.setNewUnavalCoins(newUnavalCoins);
        userCoinLog.setRelatedId(relatedId);
        userCoinLog.setCreateTime(time);
        userCoinLogDao.addUserCoinLog(userCoinLog);
    }

    public int getUserCoinLogCount(long userId, long startTime, long endTime, UserCoinOperation operation) {
        return userCoinLogDao.getUserCoinLogCount(userId, startTime, endTime, operation);
    }

	public int getTotalUserCoin(long invitorId, long startTime, long endTime, UserCoinOperation operation) {
		Integer coin = userCoinDao.getTotalUserCoin(invitorId, startTime, endTime, operation);
		return coin == null ? 0 : coin;
	}

}
