package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.UserActive;

import java.util.List;

/**
 * Created by nessary on 16-5-10.
 */
public interface UserActiveServer {
    public void saveUserCountByUserActive(UserActive userActive);

    public void updateUserCountByUserId(Integer userCount, Integer userId);

    List<UserActive> queryUserActiveAllByUserId(Integer userId);
}
