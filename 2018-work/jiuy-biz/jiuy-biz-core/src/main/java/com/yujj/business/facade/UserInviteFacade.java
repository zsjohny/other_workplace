package com.yujj.business.facade;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.account.UserInvite;
import com.yujj.business.service.UserInviteService;

@Service
public class UserInviteFacade {
    private static final char[] CODE_CHARS = new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N',
                                                        'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2',
                                                        '3', '4', '5', '6', '7', '8', '9' };

    @Autowired
    private UserInviteService userInviteService;
    
    public UserInvite createNewIfNotExists(long userId) {
        UserInvite userInvite = userInviteService.getUserInvite(userId);
        if (userInvite != null) {
            return userInvite;
        }

        userInvite = new UserInvite();
        userInvite.setUserId(userId);
        userInvite.setInviteCount(0);
        long time = System.currentTimeMillis();
        userInvite.setCreateTime(time);
        userInvite.setUpdateTime(time);
        for (int i = 1; i <= 3; i++) {
            try{
                userInvite.setInviteCode(RandomStringUtils.random(6, CODE_CHARS));
                userInviteService.addUserInvite(userInvite);
                break;
            } catch (Exception e) {
                
            }
            if (i == 3) {
                throw new RuntimeException("add user invite error");
            }
        }
        return userInvite;
    }

}
