package com.ouliao.dao.versionsecond;

import com.ouliao.domain.versionfirst.User;
import org.springframework.data.repository.query.Param;

/**
 * Created by nessary on 16-5-12.
 */
public interface UserSecondDao {
    void updateUserEmailCodeAndUserEmailByUserNum(String eamil, String emailCode,
                                                  String userNum);

    void updateUserPassByUserNum(String userPass, String userNum);

    void updateUserBackPicUrlByUserNum(String backPicUrl,
                                       String userNum);

    void updateUserheadPicUrlByUserNum(String headPicUrl,
                                       String userNum);

    int updateUserGreetlByUserNum(String userGreet, String userNum);

    User queryUserByEamil(String eamil);
}

