package com.ouliao.service.versionsecond;

import com.ouliao.domain.versionfirst.User;

/**
 * Created by nessary on 16-5-12.
 */
public interface UserSecondService {
    void updateUserEmailCodeAndUserEmailByUserNum(String eamil, String emailCode,
                                                  String userNum);

    void updateUserPassByUserNum(String userPass, String userNum);

    void updateUserBackPicUrlByUserNum(String backPicUrl,
                                       String userNum);

    int updateUserGreetlByUserNum(String userGreet, String userNum);
    User queryUserByEamil(String eamil);

    void updateUserheadPicUrlByUserNum(String geadPicUrl,
                                       String userNum);


}
