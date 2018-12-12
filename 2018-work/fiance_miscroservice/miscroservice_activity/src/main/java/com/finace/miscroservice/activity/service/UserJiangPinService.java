package com.finace.miscroservice.activity.service;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.po.GiftPO;
import com.finace.miscroservice.activity.po.InvitationPO;
import com.finace.miscroservice.activity.po.MyFriendsPO;
import com.finace.miscroservice.activity.po.UserJiangPinPO;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface UserJiangPinService {

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
     * 用户获得奖励个数
     * @param userId
     * @return
     */
    public Map<String, Object> rewardHome(String userId);


    /**
     * 添加奖品
     * @param userId
     * @param jiangPinName
     * @param addTime
     * @param remark
     * @param code
     */
    void addUserAward(Integer underUser,Integer userId, String jiangPinName, String addTime, String remark, Integer code, Integer isSend);

    /**
     * 查找奖品
     * @param userId
     * @param code
     * @return
     */
    List<UserJiangPinPO> findUserAward(Integer underUser,Integer userId, Integer code);

    /**
     * 根据页码查找奖品
     * @param userId
     * @return
     */
    JSONObject findUserAwards(int userId);

    JSONObject findMyFriends(String userId, Integer page);

    InvitationPO getInvitations(Integer userId);
}
