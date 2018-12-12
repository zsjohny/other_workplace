package com.ouliao.dao.versionsecond;

import com.ouliao.domain.versionsecond.UserPersionalShow;

import java.util.List;

/**
 * Created by nessary on 16-5-17.
 */
public interface UserPersionalShowDao {
    void createUserPersionalShow(UserPersionalShow userPersionalShow);

    void deleteUserPersionalShowById(List<Long> userPersionalShowIds, Integer userId);

    List<UserPersionalShow> queryUserPersionalAllByUserId(Integer start, Integer pagecount, Integer userId);

}
