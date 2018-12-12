package com.ouliao.dao.versionsecond;

import com.ouliao.domain.versionsecond.UserDisconery;

import java.util.List;

/**
 * Created by nessary on 16-5-18.
 */
public interface UserDisconeryDao {
    void saveUserDiconvery(UserDisconery userDisconery);

    List<UserDisconery> queryUserDisconveryAll(Integer start, Integer pageCount);

    UserDisconery queryUserDisconveryByUserId(Integer userId);


    void cancelUserContractByUserId(Integer userId);


    void deleteUserDiscoveryByUserId(Integer userId);

    void updateUserDiscoveryByUserId(String classicProgram, String disPicUrls, String listenProgram, Integer userId);



}
