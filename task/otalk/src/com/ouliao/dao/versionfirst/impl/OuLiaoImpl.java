package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.OuLiaoDao;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserGainCount;
import com.ouliao.repository.versionfirst.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by p on 2016/2/18.
 */
@Repository
public class OuLiaoImpl implements OuLiaoDao {
    @Resource
    private OuLiaoRepository ouLiaoRepository;
    @Resource
    private OuLiaoCrudRepository ouLiaoCrudRepository;
    @Resource
    private UserGgainCountRepository userGgainCountRepository;
    @Resource
    private UserGgainCountCrudRepository userGgainCountCrudRepository;
    @Resource
    private OuLiaoPageRepository ouLiaoPageRepository;

    public User regUser(User user) {
        return ouLiaoCrudRepository.save(user);
    }

    public void updateCodeByPhone(String code, String phone) {

        ouLiaoRepository.updateCodeByPhone(code, phone);
    }

    @Override
    public User queryUserByPhone(String phone) {
        return ouLiaoRepository.queryUserByPhone(phone);

    }

    @Override
    public void updatePassByPhone(String pass, String key, String phone) {
        ouLiaoRepository.updatePassByPhone(pass, key, phone);

    }

    @Override
    public User queryUserByNickName(String nickName) {

        return ouLiaoRepository.queryUserByNickName(nickName);
    }

    @Override
    public User queryUserByUserNum(String userNum) {

        return ouLiaoRepository.queryUserByUserNum(userNum);
    }

    /**
     * @param
     * @return
     */

    @Override
    public UserGainCount queryUserByIp(String realIp) {

        return userGgainCountRepository.queryUserByIp(realIp);
    }

    /**
     * @param
     * @return
     */

    @Override
    public boolean saveGainCountByIp(UserGainCount userGainCount) {
        UserGainCount save = userGgainCountCrudRepository.save(userGainCount);

        if (save == null) {
            return false;
        }
        return true;
    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateCountByIp(Integer userGainCount, String userRealIp) {
        userGgainCountRepository.updateCountByIp(userGainCount, userRealIp);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateHeadPicByUserNum(String userHeadPic, String userNum) {
        ouLiaoRepository.updateHeadPicByUserNum(userHeadPic, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateTempKeyByUserNum(String currentTime, String tempKey, String userNum) {
        ouLiaoRepository.updateTempKeyByUserNum(currentTime, tempKey, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateNickNameByUserNum(String userNickName, String userNum) {
        ouLiaoRepository.updateNickNameByUserNum(userNickName, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateSignByUserNum(String userSign, String userNum) {
        ouLiaoRepository.updateSignByUserNum(userSign, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public User queryUserByUserId(Integer userId) {

        return ouLiaoRepository.queryUserByUserId(userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateUserAuthByUserPhone(String userGreet, String userLabel,String userCallTimeWeek, String userCallTime, String userNickName,
                                          String userAuth, Double userCallCost, String userPhone) {
        ouLiaoRepository.updateUserAuthByUserPhone(userGreet,userLabel,userCallTimeWeek, userCallTime, userNickName, userAuth, userCallCost,
                userPhone);

    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateAlipayByUserNum(String userAlipayAccount, String userAlipayName, String userNum) {
        return ouLiaoRepository.updateAlipayByUserNum(userAlipayAccount, userAlipayName, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateUserRecordByUserNum(String userRecord, String userNum) {
        ouLiaoRepository.updateUserRecordByUserNum(userRecord, userNum);

    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateUserLabelByUserNum(String userLabel, String userNum) {
        ouLiaoRepository.updateUserLabelByUserNum(userLabel, userNum);
    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateUserCallTimeByUserNum(String userCallTimeWeek, String userCallTime, String userNum) {
        ouLiaoRepository.updateUserCallTimeByUserNum(userCallTimeWeek, userCallTime, userNum);
    }

    /**
     * @param
     * @return
     */

    @Override
    public Page<User> queryUserIsContractByAll(Integer startCount, Integer pageSize, String order) {

        final String sortSign = order;
        Sort sort = new Sort(Direction.DESC, sortSign);
        Pageable pageable = new PageRequest(startCount, pageSize, sort);
        Specification<User> specification = new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
                list.add(cb.equal(root.get("userContract").as(String.class), "true"));

                return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        };
        return ouLiaoPageRepository.findAll(specification, pageable);

    }

    /**
     * @param
     * @return
     */

    @Override
    public Integer queryUserContractCountByIsDeleted() {
        return ouLiaoRepository.queryUserContractCountByIsDeleted();
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateIsRecommondUserId(String userOwnerOrder, Integer userId) {
        return ouLiaoRepository.updateIsRecommondUserId(userOwnerOrder, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserIsRecommondByAll() {
        return ouLiaoRepository.queryUserIsRecommondByAll();
    }

    /**
     * @param
     * @return
     */

    @Override
    public Page<User> queryUserIsContractByIsNotRecommond(Integer startCount, Integer pageSize, String order) {
        final String sortSign = order;
        Sort sort = new Sort(Direction.DESC, sortSign);
        Pageable pageable = new PageRequest(startCount, pageSize, sort);
        Specification<User> specification = new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
                list.add(cb.equal(root.get("userContract").as(String.class), "true"));
                list.add(cb.or(cb.notEqual(root.get("userOwnerOrder").as(String.class), "true"),
                        cb.isNull(root.get("userOwnerOrder"))));
                return cq.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        };

        return ouLiaoPageRepository.findAll(specification, pageable);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserCallCostByUserId(Double userCallCost, Integer userId) {
        return ouLiaoRepository.updateUserCallCostByUserId(userCallCost, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserContractByIsDeleted() {
        return ouLiaoRepository.queryUserContractByIsDeleted();
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserMoneyByUserId(Double userMoney, Integer userId) {
        return ouLiaoRepository.updateUserMoneyByUserId(userMoney, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserPhoneByUserId(String userPhone, Integer userId) {
        return ouLiaoRepository.updateUserPhoneByUserId(userPhone, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserContractByUserNickNameOrUserAuth(Integer startCount, Integer pageSize, String word) {
        // final String words = "'%" + word + "%'";
        final String words = word;
        // System.out.println(word + "--------------");
        // Sort sort = new Sort(Direction.DESC, "userCreateTime");
        // Pageable pageable = new PageRequest(startCount, pageSize, sort);
        // Specification<User> specification = new Specification<User>() {
        //
        // @Override
        // public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq,
        // CriteriaBuilder cb) {
        //
        // List<Predicate> list = new ArrayList<>();
        // list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
        // list.add(cb.equal(root.get("userContract").as(String.class),
        // "true"));
        // list.add(cb.like(root.get("userNickName").as(String.class), words));
        // // list.add(cb.or(cb.like(root.get("userAuth").as(String.class),
        // // words),
        // // cb.like(root.get("userNickName").as(String.class), words)));
        // // return cq.where(list.toArray(new
        // // Predicate[list.size()])).getRestriction();
        // return cb.equal(root.get("userNickName").as(String.class), "'1234'");
        // }
        // };
        //
        // return ouLiaoPageRepository.findAll(specification, pageable);
        return ouLiaoRepository.queryUserContractByUserNickNameOrUserAuth(startCount, pageSize, words);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserCallTotalByUserId(Double userCallTotal, Integer userId) {
        return ouLiaoRepository.updateUserCallTotalByUserId(userCallTotal, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserCallScoreByUserId(Long userCallScore, Integer userId) {
        return ouLiaoRepository.updateUserCallScoreByUserId(userCallScore, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserContractByUserNickNameOrUserAuthAndUserId(Integer startCount, Integer pageSize,
                                                                         List<Integer> ids, String word) {
        return ouLiaoRepository.queryUserContractByUserNickNameOrUserAuthAndUserId(startCount, pageSize, ids, word);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserModifyTimeByUserId(Date userModifyTime, Integer userId) {
        return ouLiaoRepository.updateUserModifyTimeByUserId(userModifyTime, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserMoneyEarnByUserId(Double userMoney, Double userCallEarn, Integer userId) {
        return ouLiaoRepository.updateUserMoneyEarnByUserId(userMoney, userCallEarn, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserMoneyConsumeByUserId(Double userMoney, Double userCallConsume, Integer userId) {
        return ouLiaoRepository.updateUserMoneyConsumeByUserId(userMoney, userCallConsume, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public int updateUserIsCheckPassByUserId(String isCheckPass, Integer userId) {
        return ouLiaoRepository.updateUserIsCheckPassByUserId(isCheckPass, userId);
    }

    /**
     * @param
     * @return
     */

    @Override
    public void updateCodeIsContractByPhoneBy(String code, String phone, String isRester) {

        ouLiaoRepository.updateCodeIsContractByPhoneBy(code, phone, isRester);

    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserByUserNickNameOrUserAuth(Integer startCount, Integer pageSize, String word) {

        final String words = word;
        return ouLiaoRepository.queryUserByUserNickNameOrUserAuth(startCount, pageSize, words);
    }

    /**
     * @param
     * @return
     */

    @Override
    public List<User> queryUserByUserNickNameOrUserAuthAndUserId(Integer startCount, Integer pageSize,
                                                                 List<Integer> ids, String word) {
        return ouLiaoRepository.queryUserByUserNickNameOrUserAuthAndUserId(startCount, pageSize, ids, word);
    }

    @Override
    public void updateUserAuthorAndUserContractByUserId(Integer userId) {
        ouLiaoRepository.updateUserAuthorAndUserContractByUserId(userId);
    }

    @Override
    public User queryUserByThridId(String thridId) {
        return ouLiaoRepository.queryUserByThridId(thridId);
    }

    @Override
    public int updateUserPhoneByThridId(String userPhone, String thridId) {
        return ouLiaoRepository.updateUserPhoneByThridId(userPhone, thridId);
    }

}
