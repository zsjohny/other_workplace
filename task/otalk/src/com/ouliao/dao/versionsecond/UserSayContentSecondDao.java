package com.ouliao.dao.versionsecond;

import com.ouliao.domain.versionfirst.UserCommont;
import com.ouliao.domain.versionfirst.UserSayContent;
import com.ouliao.domain.versionfirst.UserSupportSay;

import java.util.List;
import java.util.Map;

/**
 * Created by nessary on 16-5-14.
 */
public interface UserSayContentSecondDao {
    List<UserSayContent> queryUserSayContentBySubjectOrContent(Integer startCount,
                                                               Integer pageSize, String word, Integer userId);

    List<UserSupportSay> querySupporAllByUserId(Integer startCount,
                                                Integer pageSize, Integer userSupportId);


    UserSayContent queryUserSayContentByUserSayContentId(Integer userSayContentId);

    List<UserCommont> queryUserCommontAllByUserId(Integer startCount,
                                                  Integer pageSize, Integer userContractId);

    List<UserSupportSay> querySupporAllByUserSayContentId(Integer startCount,
                                                          Integer pageSize, Integer userSayContentId);

    void updateUserSupportIsReadByUserSupportId(Integer userSupportId);

    void updateUserCommonttIsReadByUserContractId(Integer userContractId);

    Map<String, Integer> queryNewSupportCommontAndSupportByUserId(Integer userId);

}
