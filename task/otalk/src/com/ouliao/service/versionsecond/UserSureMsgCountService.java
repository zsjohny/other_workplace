package com.ouliao.service.versionsecond;

import com.ouliao.domain.versionsecond.UserSureMsgCount;

/**
 * Created by nessary on 16-5-19.
 */
public interface UserSureMsgCountService  {
    void saverUserSureMsgCountDao(UserSureMsgCount userSureMsgCount);

    Integer findUserSureMsgCountByUserId(Integer userId);


    void updateUSerSureMsgCountByUserId(Integer readCount, Integer userId);


}
