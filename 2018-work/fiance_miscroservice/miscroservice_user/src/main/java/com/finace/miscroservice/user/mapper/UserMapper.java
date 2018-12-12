package com.finace.miscroservice.user.mapper;

import com.finace.miscroservice.user.entity.response.MsgSizeResponse;
import com.finace.miscroservice.user.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据用户手机查询用户信息
     *
     * @param userPhone 用户的手机
     * @return
     */
    UserPO findUserOneByUserPhone(@Param("userPhone") String userPhone);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return
     */
    UserPO findUserOneByUsername(@Param("username") String username);

    /**
     * 新增用户
     * @param user
     */
    int insterUser(UserPO user);

    /**
     * 根据用户id获取用户信息
     * @param userid
     * @return
     */
    UserPO findUserOneById(@Param("userid") String userid);

    /**
     * 获取用户登录信息
     * @param username
     * @param password
     * @return
     */
    UserPO getUserLoginInfo(@Param("username") String username, @Param("password") String password);

    /***
     * 修改用户密码
     * @param map
     * @return
     */
    int updateUserPass(Map<String, String> map);

    /**
     * 根据邀请人id获取被邀请人列表
     * @param inviter
     * @return
     */
    List<UserPO> getUserListByInviter(@Param("inviter") int inviter);

    /**
     * 根据邀请人id获取被邀请人总数
     * @param inviter
     * @return
     */
    int getUserCountByInviter(@Param("inviter") int inviter);
    
    /**
     * 获取新增注册人数
     * @param
     */
    public int getCountNewUserNum();

    /**
     *修改用户风险等级
     */
    public int updateUserRating(Map<String,Object> map);


    /**
     * 根据用户id获取用户手机号码
     * @param userId
     * @return
     */
    String getUserPhoneByUserId(@Param("userId") String userId);

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
    void addFeedBack(@Param("userId") Integer userId, @Param("content") String content, @Param("ipadress") String ipadress,
                     @Param("code") Integer code, @Param("value") String value, @Param("phone") String phone,
                     @Param("username") String username, @Param("addTime") String addTime);

    /**
     * 添加消息
     * @param userId
     * @param msgCode
     * @param topic
     * @param msg
     * @param addtime
     */
    void addMsg(@Param("userId") Integer userId,@Param("msgCode") Integer msgCode,
                @Param("topic") String topic, @Param("msg") String msg, @Param("addtime") Long  addtime);

    /**
     * 消息中心 数量
     * @param userId
     * @return
     */
    List<MsgSizeResponse> findMsgSize(@Param("userId")Integer userId);

    List<UserPO> getUsersByInviterInTime(@Param("inviter")int inviter, @Param("starttime")String starttime, @Param("endtime")String endtime);

    int getUserCountByInviterInTime(@Param("inviter")int inviter, @Param("starttime")String starttime, @Param("endtime")String endtime);
}
