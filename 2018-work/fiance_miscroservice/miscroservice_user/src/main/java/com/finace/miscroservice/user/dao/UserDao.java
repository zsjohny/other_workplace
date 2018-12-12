package com.finace.miscroservice.user.dao;

import com.finace.miscroservice.user.entity.response.MsgSizeResponse;
import com.finace.miscroservice.user.po.UserPO;

import java.util.List;
import java.util.Map;

/**
 * 用户Dao层
 */
public interface UserDao {
    /**
     * 根据手机查找用户
     * @param userPhone 用户手机
     * @return
     */
    public UserPO getUserByUserPhone(String userPhone);


    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return
     */
    public UserPO getUserByUsername(String username);

    /**
     * 新增用户信息
     * @param user  用户信息
     */
    public int insterUser(UserPO user);

    /**
     * 根据用户id获取用户信息
     * @param userid
     * @return
     */
    public UserPO findUserOneById(String userid);

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public UserPO getUserLoginInfo(String username, String password);

    /**
     *
     * @param map
     * @return
     */
    public int updateUserPass(Map<String, String> map);

    /**
     * 根据邀请人id获取被邀请人列表
     * @param inviter
     * @return
     */
    public List<UserPO> getUserListByInviter(int inviter);

    /**
     * 根据邀请人id获取被邀请人总数
     * @param inviter
     * @return
     */
    public int getUserCountByInviter(int inviter);
    
    /**
     * 获取新增注册人数
     * @param
     */
    public int getCountNewUserNum();

    /**
     *修改用户风险等级
     * @param userId
     * @return
     */
    public int updateUserRating(String userId, Integer number);

    /**
     * 根据用户id获取用户手机号码
     * @param userId
     * @return
     */
    public String getUserPhoneByUserId(String userId);

    /**
     * 添加 反馈信息
     * @param userId
     * @param content
     * @param ipadress
     * @param code
     * @param value
     * @param phone
     * @param username
     * @param addTime
     */
    void addFeedBack(Integer userId, String content, String ipadress, Integer code, String value, String phone, String username, String addTime);

    /**
     * 添加消息
     * @param userId
     * @param msgCode
     * @param topic
     * @param msg
     * @param addtime
     */
    void addMsg(Integer userId, Integer msgCode,  String topic, String msg, Long  addtime);

    /**
     * 消息中心 数量
     * @param userId
     * @return
     */
    List<MsgSizeResponse> findMsgSize(Integer userId);
    /**
     * 根据邀请人id 活动时间 获取被邀请人列表
     *
     * @param inviter
     * @return
     */
    List<UserPO> getUsersByInviterInTime(int inviter, String starttime, String endtime);

    /**
     * 根据邀请人id 活动时间 获取被邀请人总数
     *
     * @param inviter
     * @return
     */
    int getUserCountByInviterInTime(int inviter, String starttime, String endtime);
}
