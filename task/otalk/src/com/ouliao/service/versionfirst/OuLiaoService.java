package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserGainCount;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by p on 2016/2/18.
 */
public interface OuLiaoService {
    public boolean regUser(User user);

    void updateCodeByPhone(String code, String phone);

    User queryUserByPhone(String phone);

    void updatePassByPhone(String pass, String key, String phone);

    List<User> queryUserByUserNickNameOrUserAuth(Integer startCount, Integer pageSize, String word);

    User queryUserByNickName(String nickName);

    User queryUserByUserNum(String userNum);

    UserGainCount queryUserByIp(String realIp);

    boolean saveGainCountByIp(UserGainCount userGainCount);

    void updateCountByIp(Integer userGainCount, String userRealIp);

    void updateHeadPicByUserNum(String userHeadPic, String userNum);

    void updateTempKeyByUserNum(String currentTime, String tempKey, String userNum);

    void updateNickNameByUserNum(String userNickName, String userNum);

    void updateSignByUserNum(String userSign, String userNum);

    User queryUserByUserId(Integer userId);

    void updateUserAuthByUserPhone(String userGreet, String userLabel, String userCallTimeWeek, String userCallTime, String userNickName, String userAuth,
                                   Double userCallCost, String userPhone);

    int updateAlipayByUserNum(String userAlipayAccount, String userAlipayName, String userNum);

    void updateUserRecordByUserNum(String userRecord, String userNum);

    void updateUserLabelByUserNum(String userLabel, String userNum);

    void updateUserCallTimeByUserNum(String userCallTimeWeek, String userCallTime, String userNum);

    Object queryUserIsContractByAll(Integer startCount, Integer pageSize, String order);

    Integer queryUserContractCountByIsDeleted();

    int updateIsRecommondUserId(String userOwnerOrder, Integer userId);

    List<User> queryUserIsRecommondByAll();

    Page<User> queryUserIsContractByIsNotRecommond(Integer startCount, Integer pageSize, String order);

    int updateUserCallCostByUserId(Double userCallCost, Integer userId);

    List<User> queryUserContractByIsDeleted();

    int updateUserMoneyByUserId(Double userMoney, Integer userId);

    int updateUserPhoneByUserId(String userPhone, Integer userId);

    List<User> queryUserContractByUserNickNameOrUserAuth(Integer startCount, Integer pageSize, String word);

    int updateUserCallTotalByUserId(Double userCallTotal, Integer userId);

    int updateUserCallScoreByUserId(Long userCallScore, Integer userId);

    List<User> queryUserContractByUserNickNameOrUserAuthAndUserId(@Param("startCount") Integer startCount,
                                                                  Integer pageSize, List<Integer> ids, String word);

    int updateUserModifyTimeByUserId(Date userModifyTime, Integer userId);

    int updateUserMoneyEarnByUserId(Double userMoney, Double userCallEarn, Integer userId);

    int updateUserMoneyConsumeByUserId(Double userMoney, Double userCallConsume, Integer userId);

    int updateUserIsCheckPassByUserId(String isCheckPass, Integer userId);

    void updateCodeIsContractByPhoneBy(String code, String phone, String isRester);

    public List<User> queryUserByUserNickNameOrUserAuthAndUserId(Integer startCount, Integer pageSize,
                                                                 List<Integer> ids, String word);

    void updateUserAuthorAndUserContractByUserId(Integer userId);

    User queryUserByThridId(String thridId);

    int updateUserPhoneByThridId(String userPhone, String thridId);
}
