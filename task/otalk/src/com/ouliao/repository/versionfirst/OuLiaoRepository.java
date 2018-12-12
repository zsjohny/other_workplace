package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by p on 2016/2/18.
 */
@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
public interface OuLiaoRepository {

    @Modifying
    @Query("update User set userPhoneCode =:code where  isDeleted='0'  and (userPhone =:phone  or thridId=:phone)")
    void updateCodeByPhone(@Param("code") String code, @Param("phone") String phone);

    @Modifying
    @Query("update User set userPhoneCode =:code ,isRegister=:isRegister where  isDeleted='0'  and userPhone =:phone")
    void updateCodeIsContractByPhoneBy(@Param("code") String code, @Param("phone") String phone,
                                       @Param("isRegister") String isRegister);

    @Modifying
    @Query("update User set userPass =:pass , userKey =:key where  isDeleted='0'  and userPhone =:phone")
    void updatePassByPhone(@Param("pass") String pass, @Param("key") String key, @Param("phone") String phone);

    @Modifying
    @Query("update User set userHeadPic =:userHeadPic where isDeleted='0' and userNum =:userNum")
    void updateHeadPicByUserNum(@Param("userHeadPic") String userHeadPic, @Param("userNum") String userNum);

    @Modifying
    @Query("update User set userRecord =:userRecord where isDeleted='0' and userNum =:userNum")
    void updateUserRecordByUserNum(@Param("userRecord") String userRecord, @Param("userNum") String userNum);

    @Modifying
    @Query("update User set userLabel =:userLabel where isDeleted='0' and userNum =:userNum")
    void updateUserLabelByUserNum(@Param("userLabel") String userLabel, @Param("userNum") String userNum);

    @Modifying
    @Query("update User set userCallTimeWeek =:userCallTimeWeek ,userCallTime=:userCallTime where isDeleted='0' and userNum =:userNum")
    void updateUserCallTimeByUserNum(@Param("userCallTimeWeek") String userCallTimeWeek,
                                     @Param("userCallTime") String userCallTime, @Param("userNum") String userNum);

    @Modifying
    @Query("update User set currentTime =:currentTime ,tempKey =:tempKey where isDeleted='0' and userNum =:userNum")
    void updateTempKeyByUserNum(@Param("currentTime") String currentTime, @Param("tempKey") String tempKey,
                                @Param("userNum") String userNum);

    @Modifying
    @Query("update User set userNickName =:userNickName where  isDeleted='0'  and userNum =:userNum")
    void updateNickNameByUserNum(@Param("userNickName") String userNickName, @Param("userNum") String userNum);

    @Modifying
    @Query("update User set userGreet=:userGreet,userLabel=:userLabel, userCallTimeWeek =:userCallTimeWeek,userCallTime=:userCallTime,userNickName =:userNickName ,userContract='true',userAuth=:userAuth ,userCallCost=:userCallCost where  isDeleted='0'  and userPhone =:userPhone")
    void updateUserAuthByUserPhone(@Param("userGreet") String userGreet, @Param("userLabel") String userLabel, @Param("userCallTimeWeek") String userCallTimeWeek,
                                   @Param("userCallTime") String userCallTime, @Param("userNickName") String userNickName,
                                   @Param("userAuth") String userAuth, @Param("userCallCost") Double userCallCost,
                                   @Param("userPhone") String userPhone);

    @Modifying
    @Query("update User set userSign =:userSign where  isDeleted='0'  and userNum =:userNum")
    void updateSignByUserNum(@Param("userSign") String userSign, @Param("userNum") String userNum);

    @Modifying
    @Query("update User set userAlipayAccount =:userAlipayAccount  ,userAlipayName=:userAlipayName  where  isDeleted='0'  and userNum =:userNum")
    int updateAlipayByUserNum(@Param("userAlipayAccount") String userAlipayAccount,
                              @Param("userAlipayName") String userAlipayName, @Param("userNum") String userNum);

    @Modifying
    @Query("update User set isCheckPass =:isCheckPass  where  isDeleted='0'  and userId =:userId")
    int updateUserIsCheckPassByUserId(@Param("isCheckPass") String isCheckPass, @Param("userId") Integer userId);

    @Modifying
    @Query("update User set userOwnerOrder =:userOwnerOrder   where  isDeleted='0'  and userId =:userId")
    int updateIsRecommondUserId(@Param("userOwnerOrder") String userOwnerOrder, @Param("userId") Integer userId);

    @Modifying
    @Query("update User set userCallCost=:userCallCost where  isDeleted='0'  and userId =:userId")
    int updateUserCallCostByUserId(@Param("userCallCost") Double userCallCost, @Param("userId") Integer userId);

    @Modifying
    @Query("update User set userMoney=:userMoney where  isDeleted='0'  and userId =:userId")
    int updateUserMoneyByUserId(@Param("userMoney") Double userMoney, @Param("userId") Integer userId);

    @Modifying
    @Query("update User set userMoney=:userMoney ,userCallEarn=:userCallEarn where  isDeleted='0'  and userId =:userId")
    int updateUserMoneyEarnByUserId(@Param("userMoney") Double userMoney, @Param("userCallEarn") Double userCallEarn,
                                    @Param("userId") Integer userId);

    @Modifying
    @Query("update User set userMoney=:userMoney ,userCallConsume=:userCallConsume where  isDeleted='0'  and userId =:userId")
    int updateUserMoneyConsumeByUserId(@Param("userMoney") Double userMoney,
                                       @Param("userCallConsume") Double userCallConsume, @Param("userId") Integer userId);

    @Modifying
    @Query("update User set userCallTotal=:userCallTotal where  isDeleted='0'  and userId =:userId")
    int updateUserCallTotalByUserId(@Param("userCallTotal") Double userCallTotal, @Param("userId") Integer userId);

    @Modifying
    @Query("update User set userCallScore=:userCallScore where  isDeleted='0'  and userId =:userId")
    int updateUserCallScoreByUserId(@Param("userCallScore") Long userCallScore, @Param("userId") Integer userId);

    @Modifying
    @Query("update User set userPhone=:userPhone where  isDeleted='0'  and userId =:userId")
    int updateUserPhoneByUserId(@Param("userPhone") String userPhone, @Param("userId") Integer userId);

    @Modifying
    @Query("update User set userModifyTime=:userModifyTime where  isDeleted='0'  and userId =:userId")
    int updateUserModifyTimeByUserId(@Param("userModifyTime") Date userModifyTime, @Param("userId") Integer userId);


    @Modifying
    @Query("update User set userAuth='false' , userContract='false' where  isDeleted='0'  and userId =:userId")
    void updateUserAuthorAndUserContractByUserId(@Param("userId") Integer userId);

    @Query("from User  where  isDeleted='0'  and userPhone =:phone")
    User queryUserByPhone(@Param("phone") String phone);

    @Query("from User  where  isDeleted='0'  and userNickName =:nickName")
    User queryUserByNickName(@Param("nickName") String nickName);

    @Query("from User  where  isDeleted='0'  and thridId =:thridId")
    User queryUserByThridId(@Param("thridId") String thridId);

    @Query("from User  where  isDeleted='0'  and userNum =:userNum")
    User queryUserByUserNum(@Param("userNum") String userNum);

    @Query("from User  where  isDeleted='0'  and userId =:userId")
    User queryUserByUserId(@Param("userId") Integer userId);

//    @Query("select count(userId) from User where isDeleted='0' and userContract ='true'")
//    Integer queryUserContractCountByIsDeleted();

    @Query(value = "select count(userId) from user where isdeleted='0' and userContract ='true'", nativeQuery = true)
    Integer queryUserContractCountByIsDeleted();

    @Query("from User  where  isDeleted='0'  and userOwnerOrder ='true' and userContract ='true'")
    List<User> queryUserIsRecommondByAll();

    @Query("from User  where  isDeleted='0'  and   userContract ='true'")
    List<User> queryUserContractByIsDeleted();

    @Query(value = "select * from user  where  isDeleted='0'  and   userContract ='true' and ( userNickName like %:word% or userAuth like %:word%) limit :startCount,:pageSize", nativeQuery = true)
    List<User> queryUserContractByUserNickNameOrUserAuth(@Param("startCount") Integer startCount,
                                                         @Param("pageSize") Integer pageSize, @Param("word") String word);

    @Query(value = "select * from user  where  isDeleted='0'   and ( userNickName like %:word% or userAuth like %:word%) limit :startCount,:pageSize", nativeQuery = true)
    List<User> queryUserByUserNickNameOrUserAuth(@Param("startCount") Integer startCount,
                                                 @Param("pageSize") Integer pageSize, @Param("word") String word);

    @Query(value = "select count(*) from user  where  isDeleted='0'  and   userContract ='true' and ( userNickName like %:word% or userAuth like %:word%) ", nativeQuery = true)
    Integer queryUserContractCountByUserNickNameOrUserAuth(@Param("word") String word);

    @Query(value = "select * from User  where  isDeleted='0'  and   userContract ='true' and ( userNickName like %:word% or userAuth like %:word%) and userId in :ids  limit :startCount,:pageSize", nativeQuery = true)
    List<User> queryUserContractByUserNickNameOrUserAuthAndUserId(@Param("startCount") Integer startCount,
                                                                  @Param("pageSize") Integer pageSize, @Param("ids") List<Integer> ids, @Param("word") String word);

    @Query(value = "select * from User  where  isDeleted='0'  and ( userNickName like %:word% or userAuth like %:word%) and userId in :ids  limit :startCount,:pageSize", nativeQuery = true)
    List<User> queryUserByUserNickNameOrUserAuthAndUserId(@Param("startCount") Integer startCount,
                                                          @Param("pageSize") Integer pageSize, @Param("ids") List<Integer> ids, @Param("word") String word);

    @Modifying
    @Query("update User set userPhone=:userPhone where  isDeleted='0'  and thridId =:thridId")
    int updateUserPhoneByThridId(@Param("userPhone") String userPhone, @Param("thridId") String thridId);


    //活动
    @Modifying
    @Query("update User set isFirstConcern =:isFirstConcern ,firstConcernId =:firstConcernId  where  isDeleted='0' and userId =:userId ")
    void updateOwnerFirstConcernByUserId(@Param("isFirstConcern") String isFirstConcern, @Param("firstConcernId") Integer firstConcernId, @Param("userId") Integer userId);


}
