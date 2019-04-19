package com.yujj.business.facade;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserSign;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserSignService;

@Service
public class UserSignFacade {
    
    @Autowired
    private UserSignService userSignService;

    @Autowired
    private UserCoinService userCoinService;

    @Transactional(rollbackFor = Exception.class)
    synchronized public void signin(long userId, DateTime dateTime, int grantCoins, String version) {
        long time = dateTime.getMillis();

        UserSign userSign = new UserSign();
        userSign.setUserId(userId);
        userSign.setDayTime(Integer.parseInt(dateTime.toString("yyyyMMdd")));
        userSign.setWeekDay(dateTime.getDayOfWeek());
        userSign.setMondayTime(Integer.parseInt(dateTime.dayOfWeek().withMinimumValue().toString("yyyyMMdd")));
        userSign.setGrantCoins(grantCoins);
        userSign.setCreateTime(time);
        userSign.setUpdateTime(time);
        try {
        	userSignService.insertUserSign(userSign);
		} catch (Exception e) {
		}
       
        userCoinService.updateUserCoin(userId, 0, grantCoins, String.valueOf(userSign.getId()), time,  UserCoinOperation.SIGN_GRANT, null, version);
        
       /* if(VersionUtil.compareVersion(version , "1.8.11") >= 0){
        	userCoinService.updateUserCoin(userId, 0, 0, String.valueOf(userSign.getId()), time,  UserCoinOperation.SIGN_GRANT);
        }else{
        	 userCoinService.updateUserCoin(userId, 0, grantCoins, String.valueOf(userSign.getId()), time, UserCoinOperation.SIGN_GRANT);
        } */
    }

}
