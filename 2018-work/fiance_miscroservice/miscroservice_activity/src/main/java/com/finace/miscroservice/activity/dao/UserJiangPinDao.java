package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.GiftPO;
import com.finace.miscroservice.activity.po.InvitationPO;
import com.finace.miscroservice.activity.po.UserJiangPinPO;
import com.finace.miscroservice.commons.entity.User;

import java.util.List;

/**
 *
 */
public interface UserJiangPinDao {



    /**
     * 新增用户奖励信息
     * @param userJiangPinPO
     */
    public void addUserJiangPin(UserJiangPinPO userJiangPinPO);

    /**
     * 获取用户奖励信息
     * @param userId
     * @return
     */
    public List<UserJiangPinPO> getUserJiangPinByUserId(String userId);

    /**
     * 获取所有用户奖励信息
     * @return
     */
    public List<UserJiangPinPO> getAllUserJiangPin();

    /**
     * 获取奖励类型
     * @return
     */
    public List<UserJiangPinPO> getUserJplx(String userId);


    /**
     * 添加奖品
     * @param userId
     * @param jiangPinName
     * @param addTime
     * @param remark
     * @param code
     */
    void addUserAward(Integer underUser,Integer userId, String jiangPinName, String addTime, String remark, Integer code, Integer isSend);

    List<UserJiangPinPO> findUserAward(Integer underUser,Integer userId, Integer code);

    List<GiftPO> findUserAwards(int userId, Integer code,String starttime,String endtime);

    Integer findJdCardMoneyAmtByUserId(int userId);

    InvitationPO getInvitations(Integer userId,String date,String endTime);

}
