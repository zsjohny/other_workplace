package com.ouliao.repository.versionsecond;

import com.ouliao.domain.versionfirst.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * Created by nessary on 16-5-12.
 */
public interface UserRepository extends Repository<User, Integer> {
    @Modifying
    @Query("update User set eamil=:eamil,emailCode=:emailCode where  isDeleted='0'  and userNum =:userNum ")
    int updateUserEmailCodeAndUserEmailByUserNum(@Param("eamil") String eamil, @Param("emailCode") String emailCode,
                                                 @Param("userNum") String userNum);

    @Modifying
    @Query("update User set userPass=:userPass where  isDeleted='0'  and userNum =:userNum ")
    int updateUserPassByUserNum(@Param("userPass") String userPass, @Param("userNum") String userNum);

    @Modifying
    @Query("update User set backPicUrl=:backPicUrl where  isDeleted='0'  and userNum =:userNum ")
    int updateUserBackPicUrlByUserNum(@Param("backPicUrl") String backPicUrl,
                                      @Param("userNum") String userNum);

    @Modifying
    @Query("update User set userGreet=:userGreet where  isDeleted='0'  and userNum =:userNum ")
    int updateUserGreetlByUserNum(@Param("userGreet") String userGreet,
                                  @Param("userNum") String userNum);

    @Query("from User  where  isDeleted='0'  and eamil =:eamil")
    User queryUserByEamil(@Param("eamil") String eamil);


    @Modifying
    @Query("update User set userHeadPic=:userHeadPic where  isDeleted='0'  and userNum =:userNum ")
    int updateUserheadPicUrlByUserNum(@Param("userHeadPic") String headPicUrl,
                                      @Param("userNum") String userNum);


}
