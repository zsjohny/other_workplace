package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.OuLiaoDao;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserGainCount;
import com.ouliao.service.versionfirst.OuLiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by p on 2016/2/18.
 */
@Service
@Transactional
public class OuLiaoServiceImpl implements OuLiaoService {
    @Autowired
    private OuLiaoDao ouLiaoDao;

    public boolean regUser(User user) {
        if (user == null || (StringUtils.isEmpty(user.getUserPhone()) && StringUtils.isEmpty(user.getThridId()))) {
            return false;
        }
        user = ouLiaoDao.regUser(user);

        if (user == null) {
            return false;
        }

        return true;
    }

    public void updateCodeByPhone(String code, String phone) {
        ouLiaoDao.updateCodeByPhone(code, phone);
    }

    @Override
    public User queryUserByPhone(String phone) {

        return ouLiaoDao.queryUserByPhone(phone);

    }

    @Override
    public void updatePassByPhone(String pass, String key, String phone) {
        ouLiaoDao.updatePassByPhone(pass, key, phone);

    }

    @Override
    public User queryUserByNickName(String nickName) {

        return ouLiaoDao.queryUserByNickName(nickName);
    }

    @Override
    public User queryUserByUserNum(String userNum) {

        return ouLiaoDao.queryUserByUserNum(userNum);
    }

    /**
     * @param
     * @return
     */

    @Override
    public UserGainCount queryUserByIp(String realIp) {

        return ouLiaoDao.queryUserByIp(realIp);
    }

    /**
     * @param
     * @return
     */

    @Override
    public boolean saveGainCountByIp(UserGainCount userGainCount) {

        return ouLiaoDao.saveGainCountByIp(userGainCount);
    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateCountByIp(Integer userGainCount, String userRealIp) {
        ouLiaoDao.updateCountByIp(userGainCount, userRealIp);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateHeadPicByUserNum(String userHeadPic, String userNum) {
        ouLiaoDao.updateHeadPicByUserNum(userHeadPic, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateTempKeyByUserNum(String currentTime, String tempKey, String userNum) {
        ouLiaoDao.updateTempKeyByUserNum(currentTime, tempKey, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateNickNameByUserNum(String userNickName, String userNum) {
        ouLiaoDao.updateNickNameByUserNum(userNickName, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateSignByUserNum(String userSign, String userNum) {
        ouLiaoDao.updateSignByUserNum(userSign, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public User queryUserByUserId(Integer userId) {

        return ouLiaoDao.queryUserByUserId(userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateUserAuthByUserPhone(String userGreet, String userLabel, String userCallTimeWeek, String userCallTime, String userNickName,
                                          String userAuth, Double userCallCost, String userPhone) {
        ouLiaoDao.updateUserAuthByUserPhone(userGreet, userLabel, userCallTimeWeek, userCallTime, userNickName, userAuth, userCallCost,
                userPhone);

    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateAlipayByUserNum(String userAlipayAccount, String userAlipayName, String userNum) {
        return ouLiaoDao.updateAlipayByUserNum(userAlipayAccount, userAlipayName, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateUserRecordByUserNum(String userRecord, String userNum) {
        ouLiaoDao.updateUserRecordByUserNum(userRecord, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateUserLabelByUserNum(String userLabel, String userNum) {
        ouLiaoDao.updateUserLabelByUserNum(userLabel, userNum);
    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateUserCallTimeByUserNum(String userCallTimeWeek, String userCallTime, String userNum) {
        ouLiaoDao.updateUserCallTimeByUserNum(userCallTimeWeek, userCallTime, userNum);
    }

    /**
     * @param
     * @return
     */

    @Override
    public Object queryUserIsContractByAll(Integer startCount, Integer pageSize, String order) {
        // try {
        //
        // // 利用缓存的技术
        // Jedis jedis = new Jedis("localhost", 10086);
        //
        // SortingParams sortingParams = new SortingParams();
        // sortingParams.desc();
        // sortingParams.by(String.valueOf("user:" + order + ":*"));
        //
        // sortingParams.limit(startCount * pageSize, pageSize);
        //
        // List<String> bytes = jedis.sort(("user:list"), sortingParams);
        //
        // List<User> users = new ArrayList<>();
        // for (String b : bytes) {
        // if (b == null) {
        // continue;
        // }
        // byte[] by = jedis.get(String.valueOf("user:" + b).getBytes());
        // ByteArrayInputStream bais = new ByteArrayInputStream(by);
        // ObjectInputStream ois = new ObjectInputStream(bais);
        // User user = (User) ois.readObject();
        // users.add(user);
        // }
        //
        // return users;
        //
        // } catch (Exception e) {
        // Page<User> userPage = ouLiaoDao.queryUserIsContractByAll(startCount,
        // pageSize, order);
        //
        // return userPage;
        // }

        return ouLiaoDao.queryUserIsContractByAll(startCount, pageSize, order);

    }

    /**
     * @param
     * @return
     */

    @Override
    public Integer queryUserContractCountByIsDeleted() {
        return ouLiaoDao.queryUserContractCountByIsDeleted();
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateIsRecommondUserId(String userOwnerOrder, Integer userId) {
        return ouLiaoDao.updateIsRecommondUserId(userOwnerOrder, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserIsRecommondByAll() {
        return ouLiaoDao.queryUserIsRecommondByAll();
    }

    /**
     * @param
     * @return
     */

    @Override
    public Page<User> queryUserIsContractByIsNotRecommond(Integer startCount, Integer pageSize, String order) {
        return ouLiaoDao.queryUserIsContractByIsNotRecommond(startCount, pageSize, order);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserCallCostByUserId(Double userCallCost, Integer userId) {
        return ouLiaoDao.updateUserCallCostByUserId(userCallCost, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserContractByIsDeleted() {
        return ouLiaoDao.queryUserContractByIsDeleted();
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserMoneyByUserId(Double userMoney, Integer userId) {
        return ouLiaoDao.updateUserMoneyByUserId(userMoney, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserPhoneByUserId(String userPhone, Integer userId) {
        return ouLiaoDao.updateUserPhoneByUserId(userPhone, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserContractByUserNickNameOrUserAuth(Integer startCount, Integer pageSize, String word) {

        return ouLiaoDao.queryUserContractByUserNickNameOrUserAuth(startCount, pageSize, word);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserCallTotalByUserId(Double userCallTotal, Integer userId) {
        return ouLiaoDao.updateUserCallTotalByUserId(userCallTotal, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserCallScoreByUserId(Long userCallScore, Integer userId) {
        return ouLiaoDao.updateUserCallScoreByUserId(userCallScore, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserContractByUserNickNameOrUserAuthAndUserId(Integer startCount, Integer pageSize,
                                                                         List<Integer> ids, String word) {
        return ouLiaoDao.queryUserContractByUserNickNameOrUserAuthAndUserId(startCount, pageSize, ids, word);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserModifyTimeByUserId(Date userModifyTime, Integer userId) {
        return ouLiaoDao.updateUserModifyTimeByUserId(userModifyTime, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserMoneyEarnByUserId(Double userMoney, Double userCallEarn, Integer userId) {
        return ouLiaoDao.updateUserMoneyEarnByUserId(userMoney, userCallEarn, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserMoneyConsumeByUserId(Double userMoney, Double userCallConsume, Integer userId) {
        return ouLiaoDao.updateUserMoneyConsumeByUserId(userMoney, userCallConsume, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserIsCheckPassByUserId(String isCheckPass, Integer userId) {
        return ouLiaoDao.updateUserIsCheckPassByUserId(isCheckPass, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateCodeIsContractByPhoneBy(String code, String phone, String isRester) {

        ouLiaoDao.updateCodeIsContractByPhoneBy(code, phone, isRester);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserByUserNickNameOrUserAuth(Integer startCount, Integer pageSize, String word) {
        return ouLiaoDao.queryUserByUserNickNameOrUserAuth(startCount, pageSize, word);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserByUserNickNameOrUserAuthAndUserId(Integer startCount, Integer pageSize,
                                                                 List<Integer> ids, String word) {
        return ouLiaoDao.queryUserByUserNickNameOrUserAuthAndUserId(startCount, pageSize, ids, word);
    }

    @Override
    public void updateUserAuthorAndUserContractByUserId(Integer userId) {
        ouLiaoDao.updateUserAuthorAndUserContractByUserId(userId);
    }

    @Override
    public User queryUserByThridId(String thridId) {
        return ouLiaoDao.queryUserByThridId(thridId);
    }

    @Override
    public int updateUserPhoneByThridId(String userPhone, String thridId) {
        return ouLiaoDao.updateUserPhoneByThridId(userPhone, thridId);
    }

}
