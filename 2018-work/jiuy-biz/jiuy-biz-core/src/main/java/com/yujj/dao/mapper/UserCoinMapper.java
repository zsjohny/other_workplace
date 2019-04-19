package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.account.UserCoin;

@DBMaster
public interface UserCoinMapper {

    UserCoin getUserCoin(long userId);

    int addUserCoin(UserCoin userCoin);

    int updateUserCoin(@Param("userId") long userId, @Param("newAvalCoins") int newAvalCoins,
                       @Param("oldAvalCoins") int oldAvalCoins, @Param("newUnavalCoins") int newUnavalCoins,
                       @Param("oldUnavalCoins") int oldUnavalCoins, @Param("time") long time);
    
    int updateUserCoinNew(@Param("userId") long userId, @Param("getCoins") int getCoins, @Param("time") long time);
}
