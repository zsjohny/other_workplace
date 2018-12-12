package com.finace.miscroservice.user.service;

import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.user.entity.response.MsgSizeResponse;
import com.finace.miscroservice.user.po.UserPO;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户service
 */
public interface UserService {

    /**
     * 根据手机号码获取用户信息
     * @param userPhone 用户手机号码
     * @return 用户信息
     */
    public UserPO getUserByUserPhone(String userPhone);

    /**
     * 根据手机号码判断该手机号码是否存在
     * @param mobile 用户手机号码
     * @return 号码是否存在
     */
    public boolean isRightPhone(String mobile);

    /***
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    public UserPO getUserByUsername(String username);

    /**
     * 根据用户名判断用户是否存在
     * @param username 用户名
     * @return 用户名是否存在
     */
    public boolean isRightUsername(String username);

    /**
     *新增用户信息
     * @param user 用户信息
     */
    public int insterUser(UserPO user);

    /**
     * 根据用户id获取用户信息
     * @param userid 用户id
     * @return
     */
    public UserPO findUserOneById(String userid);

    /**
     * 获取用户登录信息
     * @param username 用户登录名
     * @param password 用户密码
     * @return
     */
    UserPO getUserLoginInfo(String username, String password);

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
    public List<UserPO> getUserListByInviter(int inviter, int page);

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
     * @param number
     * @return
     */
    public int updateUserRating(String userId, Integer number);


    /**
     * 添加意见反馈
     * @param userId 用户id
     * @param content 反馈内容
     * @param ipadress ip地址
     * @param code 联系方式code
     * @param value 联系方式 value
     * @param phone 手机号
     * @param username  用户名
     * @param addTime 添加时间
     * @return
     */
    Response addFeedBack(Integer userId, String content, String ipadress, Integer code, String value, String phone, String username, String addTime);

    /**
     * 根据用户id获取用户手机号码
     * @param userId
     * @return
     */
    public String getUserPhoneByUserId(String userId);


    Response scode(String phone, Integer i, String ustatus);

    void sendBakMsg(String phone, String content, String ustatus);

    String regValidate(String mobile, String password, String mobile1);

    Response upIsTrue(Map<String, Object> result, boolean isTrue);

    String[] decryptPass(String uid, String phone, String npassword);

    /**
     * 添加消息

     */
    void addMsg(Integer userId, Integer msgCode, String topic, String msg, Long  addtime);

    List<MsgSizeResponse> findMsgSize(Integer userId);


    /**
     * 注册
     * @param mobile
     * @param password
     * @param code
     * @param refereeuser
     * @param regChannel
     * @param uid
     * @param imei
     * @param pushId
     * @param ipadress
     * @param isDevice
     * @return
     */
    Response signIn(String mobile, String password, String code, String refereeuser, String regChannel, String uid, String imei, String pushId, String ipadress, Integer isDevice, HttpServletResponse response);

    /**
     * 分享注册
     * @param shareid
     * @param channel
     * @param mobile
     * @param password
     * @param code
     * @param ipadress
     * @param isDevice
     * @return
     */
    Response shareSingIn(String shareid, String channel, String mobile, String password, String code, String ipadress, Integer isDevice);

    /**
     * 发送验证码
     * @param phone
     * @param type
     * @param userId
     * @return
     */
    Response sendCode(String phone, String type, String userId);

    /**
     * 忘记密码
     * @param npassword
     * @param code
     * @param phone
     * @param uid
     * @return
     */
    Response modifyPass(String npassword, String code, String phone, String uid);
    /**
     * 修改密码
     * @param opassword
     * @param npassword
     * @param rpassword
     * @param uid
     * @param userId
     * @return
     */
    Response updatePass(String opassword, String npassword, String rpassword, String uid, String userId,HttpServletResponse response);


    /**
     * 我的
     * @param userId
     * @return
     */
    Response homeIndex(String userId);

    /**
     * 提现
     * @param tmoney
     * @param webhost
     * @param trustreturl
     * @param trustbgreturl
     * @param hhhost
     * @param mercustid
     * @param userId
     * @param response
     * @return
     */
    Response withdraw(String tmoney, String webhost, String trustreturl, String trustbgreturl, String hhhost, String mercustid, String userId, HttpServletResponse response);

    /**
     * 取现成功返回
     * @param map
     * @param params
     * @param response
     * @param mercustid
     */
    void withdrawNotify(ModelMap map, Map<String, String> params, HttpServletResponse response, String mercustid);

    List<UserPO> getUsersByInviterInTime(int inviter, int page, String starttime, String endtime);

    int getUserCountByInviterInTime(int inviter, String starttime, String endtime);
    /**
     * 购买页面发送短信验证码
     */

    Response sendFuiouCode(String userId);
}
