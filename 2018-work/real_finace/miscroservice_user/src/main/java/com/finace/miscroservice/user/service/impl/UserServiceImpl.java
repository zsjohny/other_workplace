package com.finace.miscroservice.user.service.impl;

import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.current.ExecutorService;
import com.finace.miscroservice.commons.current.ExecutorTask;
import com.finace.miscroservice.commons.entity.*;
import com.finace.miscroservice.commons.enums.MsgCodeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.*;
import com.finace.miscroservice.commons.utils.tools.*;
import com.finace.miscroservice.commons.utils.tools.message.SendMessageUtil;
import com.finace.miscroservice.user.config.LoadRealm;
import com.finace.miscroservice.user.dao.UserBankCardDao;
import com.finace.miscroservice.user.dao.UserDao;
import com.finace.miscroservice.user.entity.CashReqExt;
import com.finace.miscroservice.user.entity.EnchashmentTrustPost;
import com.finace.miscroservice.user.entity.EnchashmentTrustReturn;
import com.finace.miscroservice.user.entity.beans.BankCardBin;
import com.finace.miscroservice.user.entity.response.MsgSizeResponse;
import com.finace.miscroservice.user.entity.SignUtils;
import com.finace.miscroservice.user.po.*;
import com.finace.miscroservice.user.rpc.ActivityRpcService;
import com.finace.miscroservice.user.rpc.AuthorizeRpcService;
import com.finace.miscroservice.user.rpc.BorrowRpcService;
import com.finace.miscroservice.user.service.*;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.finace.miscroservice.commons.enums.MqChannelEnum.NEW_USER_GRANT_HB;
import static com.finace.miscroservice.commons.utils.JwtToken.TOKEN;

/**
 * 用户的service 实现层
 */
@Service
public class UserServiceImpl implements UserService {
    private Log logger = Log.getInstance(UserServiceImpl.class);

    private static String RECV_ORD_ID = "RECV_ORD_ID_";

    @Autowired
    private UserDao userDao;

    @Autowired
    private ActivityRpcService activityRpcService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BorrowRpcService borrowRpcService;

    @Autowired
    private AccountFsstransService accountFsstransService;

    @Autowired
    private UserBankCardService userBankCardService;

    @Autowired
    private UserBankCardDao userBankCardDao;

    @Autowired
    private LoadRealm loadRealm;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    private AuthorizeRpcService authorizeRpcService;

    @Autowired
    @Lazy
    private MqTemplate mqTemplate;

    @Autowired
    private OpenAccountService openAccountService;

    @Value("${user.shareKey}")
    protected String shareKey;

    @Value("${user.server.status}")
    protected String ustatus;

    @Value("${user.active.topic}")
    protected String topic;


    /**
     * 根据手机号码获取用户信息
     *
     * @param userPhone
     * @return
     */
    public UserPO getUserByUserPhone(String userPhone) {

        return this.userDao.getUserByUserPhone(userPhone);
    }

    /***
     * 根据手机号码判断该手机号码是否存在
     * @param mobile
     * @return
     */
    public boolean isRightPhone(String mobile) {

        UserPO user = userDao.getUserByUserPhone(mobile);
        if (user == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    public UserPO getUserByUsername(String username) {

        return this.userDao.getUserByUsername(username);
    }

    /**
     * @param username 用户名
     * @return
     */
    public boolean isRightUsername(String username) {

        UserPO user = userDao.getUserByUsername(username);
        if (user == null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @param user 用户信息
     */
    @Transactional
    public int insterUser(UserPO user) {
        return this.userDao.insterUser(user);
    }


    /**
     * @param userid 用户id
     * @return
     */
    public UserPO findUserOneById(String userid) {

        return this.userDao.findUserOneById(userid);
    }


    /**
     * @param username 用户登录名
     * @param password 用户密码
     * @return
     */
    public UserPO getUserLoginInfo(String username, String password) {

        return this.userDao.getUserLoginInfo(username, password);
    }

    /**
     * @param map
     * @return
     */
    @Transactional
    public int updateUserPass(Map<String, String> map) {
        return this.userDao.updateUserPass(map);
    }

    @Override
    public List<UserPO> getUserListByInviter(int inviter, int page) {
        BasePage.setPage(page);
        List<UserPO> list = userDao.getUserListByInviter(inviter);
        return list;
    }


    @Override
    public int getUserCountByInviter(int inviter) {
        return userDao.getUserCountByInviter(inviter);
    }

    @Override
    public int getCountNewUserNum() {
        return userDao.getCountNewUserNum();
    }


    @Transactional
    @Override
    public int updateUserRating(String userId, Integer number) {

        return userDao.updateUserRating(userId, number);
    }

    /**
     * 添加 反馈信息
     *
     * @param userId
     * @param content
     * @param ipadress
     * @param code
     * @param value
     * @param phone
     * @param username
     * @param addTime
     */
    @Transactional
    @Override
    public Response addFeedBack(Integer userId, String content, String ipadress, Integer code, String value, String phone, String username, String addTime) {
        logger.info("用户userId={},Ip={},添加反馈content={}", userId, ipadress, content);
        userDao.addFeedBack(userId, content, ipadress, code, value, phone, username, addTime);
        return Response.success();
    }

    @Override
    public String getUserPhoneByUserId(String userId) {
        return userDao.getUserPhoneByUserId(userId);
    }


    /**
     * 发送手机号码
     *
     * @param phone
     * @return
     */
    @Override
    public Response scode(String phone, Integer type, String ustatus) {
        boolean isReg = isRightPhone(phone);
        if (isReg) {
            logger.info("开始向{}发送验证码", phone);
            String content = "您在一桶金申请的验证码为:{rand}, 有效时间5分钟，请尽快完成操作。非本人操作请致电：400-888-7140";
            sendBakMsg(phone, content, ustatus);
            logger.info("结束向{}发送验证码", phone);
        } else {
            logger.warn(type == 2 ? "渠道界面" : "分享界面" + ",注册时的手机验证码,{}用户被注册", phone);
            return Response.errorMsg("用户已被注册"); // 用户被注册
        }
        return Response.success();
    }

    /**
     * 发送短信
     *
     * @param content
     * @return
     */
    public void sendBakMsg(String phone, String content, String ustatus) {
        // 生成随机码
        Random random = new Random();
        String rand = "";
        for (int i = 0; i < 6; i++) {
            String r = String.valueOf(random.nextInt(10));
            rand += r;
        }

        content = content.replace("{rand}", rand);
        logger.info("向手机号码{},发送短信内容:{},发送开始", phone, content);
        try {
            if (ustatus.equals("pro")) {
                boolean sendSuccess = SendMessageUtil.sendSMS(phone, content);
                if (sendSuccess) {
                    logger.info("向手机号码{},发送短信内容:{},发送成功", phone, content);
                }
            } else {
                rand = "000000";
            }
            userStrHashRedisTemplate.set(phone, rand, Constant.USER_VER_CODE_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("向{}发送验证码失败,异常信息：{}", phone, e);
        }
    }

    /**
     * 验证注册信息
     *
     * @param username
     * @param password
     * @param mobile
     * @return
     */
    public String regValidate(String username, String password, String mobile) {

        if (org.apache.commons.lang3.StringUtils.isEmpty(username)) {
            return "用户名为空";
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(password)) {
            return "密码为空";
        }
        if (org.apache.commons.lang3.StringUtils.length(username) < 4) {
            return "用户名不能少于4位";
        }
        if (org.apache.commons.lang3.StringUtils.length(username) > 16) {
            return "用户名不能大于16位";
        }

        // 密码是否含有汉字
        int count = 0;
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(password);
        while (m.find()) {
            count = 1;
        }
        if (count == 1) {
            return "密码不能为汉字";
        }

        // 检查用户名是否已经注册
        if (!isRightUsername(username)) {
            return "用户名已被注册";
        }

        // 用户是否含有特殊字符
        regEx = "[~!/@#$%^&*()-_=+\\[{}];:\'\",<.>/?]+";
        count = 0;
        p = Pattern.compile(regEx);
        m = p.matcher(username);
        while (m.find()) {
            count = 1;
        }
        if (count == 1) {
            return "用户名含特殊字符";
        }

        // 检查手机号码是否绑定过
        if (!isRightPhone(mobile)) {

            return "手机号码已绑定";
        }

        return null;
    }


    /**
     * 判断手机号码或用户名是否已注册
     *
     * @param result
     * @param isTrue
     * @return
     */
    @Override
    public Response upIsTrue(Map<String, Object> result, boolean isTrue) {
        if (isTrue) {
            result.put("status", "10002");
            return Response.successByMap(result);
        } else {
            result.put("status", "10001");
            return Response.successByMap(result);
        }
    }


    /**
     * 密码先解密再加密  数组0--加密后密码  1--明文密码
     *
     * @param uid
     * @param npassword
     * @return
     */
    @Override
    public String[] decryptPass(String uid, String phone, String npassword) {
        try {
            //先是base64转码编译
            String base64Pass = new String(Base64.getDecoder().decode(npassword));
            if (!base64Pass.matches("[\\w+=/\\\\]+")) {
                // logger.warn("用户 ={} 注册 所输入的密码={} 不符合规范", userPO.getPhone(), userPO.getPassword());
                return null;
            }
            //首先是密码进行前端解密
            String realPass = DesUtil.decrypt(base64Pass, uid);
            String rdata[] = new String[2];
            rdata[0] = AesUtil.encode(realPass, phone);//最后加密
            rdata[1] = realPass;
            return rdata;
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    @Transactional
    public void addMsg(Integer userId, Integer msgCode, String topic, String msg, Long addtime) {

        userDao.addMsg(userId, msgCode, topic, msg, addtime);
    }

    @Override
    public List<MsgSizeResponse> findMsgSize(Integer userId) {
        return userDao.findMsgSize(userId);
    }

    private static ExecutorService signInExecutor = new ExecutorService(Runtime.getRuntime().availableProcessors() << 1, "signIn");


    @Override
    public Response signIn(String mobile, String password, String code, String refereeuser, String regChannel, String uid, String imei, String pushId, String ipadress, Integer isDevice, HttpServletResponse response) {
        logger.info("手机渠道===》regChannel={}",regChannel);
        Map<String, Object> map = new HashMap<>();
        try {
            logger.info("用户={} 开始注册,ipadress={},isDevice={}", mobile, ipadress, isDevice);

            if (!(code != null && code.equals(userStrHashRedisTemplate.get(mobile)))) {
                logger.warn("用户={} 注册手机验证码错误", mobile);
                return Response.errorMsg("手机验证码错误");
            }

            String msg = regValidate(mobile, password, mobile);
            if (null != msg) {
                logger.warn("用户={} " + msg, mobile);
                return Response.errorMsg(msg);
            }

            UserPO user = new UserPO();

            // 推荐人id
            UserPO inviteUser = userDao.getUserByUsername(refereeuser);
            if (inviteUser != null) {
                logger.info("用户={} 的推荐人是{}", mobile, inviteUser.getPhone());
                user.setInviteUserid(inviteUser.getUser_id());
            }

            if("ifengwo".equals(regChannel)){
                user.setRegChannel(regChannel);
            }
            else {
                String cl = activityRpcService.getIosChannel(imei != null ? imei : uid);
                if (null != cl) {
                    user.setRegChannel(cl);

                } else {
                    user.setRegChannel(regChannel);
                }
            }
            user.setUsername(mobile);
            user.setPhone(mobile);
            user.setAddip(ipadress);
            user.setIsDevice(isDevice); // 设备
            user.setRealStatus("0"); // 实名未认证
            user.setPayChannel("fuiou");
            user.setPaypassword(MD5.getInstance().getMD5ofStr(password));
            user.setPassword(password);
            user.setIsPhone(1);
            user.setUid(uid);
            user.setTypeId(Constant.WEB_USER_TYPEID_L);
            user.setAddtime(String.valueOf(System.currentTimeMillis() / 1000));
            user.setStatus(1); // 默认用户状态是1
            logger.info("用户={} 注册渠道信息1：{},regchannel={}",mobile,user.getRegChannel(),regChannel);

            Boolean istrue = loadRealm.reg(uid,user, response, pushId);
            if (istrue) {
                logger.info("用户{}注册成功", user.getPhone());
                if (user.getUser_id() != 0) {
                    logger.info("用户{}注册成功，开始发放福利券", user.getPhone());
                    //消息队列发送红包
                    mqTemplate.sendMsg(NEW_USER_GRANT_HB.toName(), String.valueOf(user.getUser_id()));
                    //添加系统消息
                    userDao.addMsg(user.getUser_id(), MsgCodeEnum.SYS_MSG.getCode(), MsgCodeEnum.SYS_SUBTYPE_REGISTER.getValue(), MsgCodeEnum.SYS_MSG_TEXT2.getValue(), System.currentTimeMillis());
                } else {
                    logger.warn("用户={} 通过app注册, 插入数据库失败", mobile);
                    return Response.errorMsg("注册失败");
                }
            } else {
                logger.warn("用户={} 通过app注册, 检查不通过,注册失败", mobile);
                return Response.errorMsg("注册失败");
            }
            signInExecutor.addTask(new ExecutorTask() {
                @Override
                public void doJob() {
                    //头条注册检测回调
                    logger.info("头条检测回调uid={}",uid);
                    activityRpcService.doRegHeadChannelCallback(uid);
                }
            });

            map.put("shareid", Rc4Utils.toHexString(user.getPhone(), shareKey));

            logger.info("用户={} 注册成功", mobile);
            return Response.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户={} 通过app注册失败,{}", mobile, e);
        }

        return Response.errorMsg("注册失败");
    }

    @Override
    public Response shareSingIn(String shareid, String channel, String mobile, String password, String code, String ipadress, Integer isDevice) {
        try {
            logger.info("用户={} 开始注册, shareid={}", mobile, shareid);
            String msg = regValidate(mobile, password, mobile);
            if (null != msg) {
                logger.warn("用户={} " + msg, mobile);
                return Response.errorMsg(msg);
            }

            if (channel == null) {
                logger.warn("用户={}通过h5界面注册渠道为空", mobile);
                return Response.errorMsg("注册渠道为空");
            }

            if (!(code != null && code.equals(userStrHashRedisTemplate.get(mobile)))) {
                logger.warn("用户={} 注册手机验证码错误", mobile);
                return Response.errorMsg("手机验证码错误");
            }

            UserPO user = new UserPO();

            //分享使用
            if (!"".equals(shareid) && null != shareid) {
                String inviterid = "";
                logger.info("用户{}注册，分享shareid={}",mobile, shareid);
                if (shareid.length() > 11) {
                    inviterid = Rc4Utils.toString(shareid, shareKey);
                } else {
                    //手机号码
                    inviterid = shareid;
                }

                logger.info("用户{}注册，inviterid={}",mobile, inviterid);
                // 推荐人id
                UserPO inviteUser = userDao.getUserByUsername(inviterid);
                if (inviteUser != null) {
                    logger.info("用户={} 的推荐人是{}", mobile, inviteUser.getPhone());
                    user.setInviteUserid(inviteUser.getUser_id());
                }
            }

            user.setUsername(mobile);
            user.setPhone(mobile);
            user.setAddip(ipadress);
            user.setIsDevice(isDevice); // 设备
            user.setRealStatus("0"); // 实名未认证
            user.setPayChannel("fuiou");
            user.setRegChannel2(channel);
            user.setPaypassword(MD5.getInstance().getMD5ofStr(password));
            user.setPassword(AesUtil.encode(password, mobile));
            user.setIsPhone(1);
            user.setAddtime(String.valueOf(System.currentTimeMillis() / 1000));
            user.setStatus(1); // 默认用户状态是1
            user.setTypeId(Constant.WEB_USER_TYPEID_L);

            int userid = userDao.insterUser(user);
            if (userid != 0) {
                logger.info("用户={}通过h5界面注册成功，开始发放福利券", user.getPhone());
                //消息队列发送红包
                mqTemplate.sendMsg(NEW_USER_GRANT_HB.toName(), String.valueOf(userid));
            } else {
                logger.warn("用户={}通过h5界面注册,数据库新增失败", mobile);
                return Response.errorMsg("注册失败");
            }

            logger.info("用户={} h5界面注册成功", mobile);
            return Response.success();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户={} 通过h5界面注册失败,{}", mobile, e);
        }
        return Response.errorMsg("注册失败");
    }


    @Override
    public Response sendCode(String phone, String type, String userId) {
        // 登入后，修改登入密码--1
        if ("1".equals(type)) {
            UserPO user = userDao.findUserOneById(userId);
            if (user != null && user.getPhone().equals(phone)) {
                logger.info("{}修改密码发送验证码", phone);
                String content = "您在一桶金修改登入密码操作，验证码是：{rand}";
                sendBakMsg(phone, content, ustatus);
            } else {
                logger.warn("登入后,修改登入密码发送验证码,{}用户手机不符", phone);
                return Response.errorMsg("用户手机不符"); //用户手机不符
            }

            // 注册时的手机验证码--2
        } else if ("2".equals(type)) {
            if (Regular.checkPhone(phone)) {
                logger.info("{}注册发送验证码", phone);
                String content = "您在一桶金申请的验证码为:{rand}, 有效时间5分钟，请尽快完成操作。非本人操作请致电：400-888-7140";
                sendBakMsg(phone, content, ustatus);
            } else {
                logger.warn("{}注册发送验证码, 手机号码格式不正确", phone);
                return Response.errorMsg("手机号码格式不正确");
            }

            // 忘记密码，找回密码发送验证码--3
        } else if ("3".equals(type)) {
            UserPO user = userDao.getUserByUserPhone(phone);
            if (user == null) {
                logger.warn("忘记密码，找回密码发送验证码,{}用户不存在", phone);
                return Response.errorMsg("用户不存在");
            } else {
                logger.info("{}找回密码发送验证码", phone);
                String content = "您在一桶金使用忘记密码找回操作，验证码是：{rand} ";
                sendBakMsg(phone, content, ustatus);
            }
        } else {
            logger.warn("{}发送短信类型错误,type={}", phone, type);
            return Response.errorMsg("发送短信类型错误");
        }

        return Response.success();
    }

    @Override
    public Response modifyPass(String npassword, String code, String phone, String uid) {
        try {
            //手机验证码检测
            String rcode = userStrHashRedisTemplate.get(phone) != null ? userStrHashRedisTemplate.get(phone).toLowerCase() : "";
            if (!code.toLowerCase().equals(rcode)) {
                logger.warn("用户{}手机验证码错误", phone);
                return Response.errorMsg("手机验证码错误");
            }

            UserPO user = userDao.getUserByUserPhone(phone);
            if (null == user) {
                logger.warn("用户{}不存在", phone);
                return Response.errorMsg("用户不存在");
            }

            String password[] = decryptPass(uid, user.getPhone(), npassword); //数组0--加密后密码  1--明文密码
            if (null == password) {
                logger.warn("用户{}系统加密失败", phone);
                return Response.errorMsg("系统加密失败");
            }

            Map<String, String> dmap = new HashMap<>();
            dmap.put("password", password[0]);
            dmap.put("id", String.valueOf(user.getUser_id()));
            int count = userDao.updateUserPass(dmap);
            if (count > 0) {
                //修改授权服务器的密码
                UserAuth auser = new UserAuth();
                auser.setNickName(user.getUsername());
                auser.setPass(password[1]);
                auser.setName(user.getPhone());
                auser.setUid(user.getUser_id());
                authorizeRpcService.updateUser(auser);

                logger.info("用户{}忘记密码,修改密码成功", phone);
                return Response.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户{}修改密码失败", phone);
        }

        logger.warn("用户{}修改密码失败", phone);
        return Response.errorMsg("修改密码失败");
    }

    @Override
    public Response updatePass(String opassword, String npassword, String rpassword, String uid, String userId,HttpServletResponse response) {
        try {

            if (!npassword.equals(rpassword)) {
                logger.warn("用户{}修改密码,两次密码不一致", userId);
                return Response.errorMsg("两次密码不一致");
            }

            UserPO user = userDao.findUserOneById(userId);

            if (null == user) {
                logger.warn("用户{}修改密码,用户不存在", userId);
                return Response.errorMsg("用户不存在");
            }

            if (!user.getPassword().equals(decryptPass(uid, user.getPhone(), opassword)[0])) {
                logger.warn("用户{}修改密码,用户登录密码错误", userId);
                return Response.errorMsg("用户登录密码错误");
            }

            String password[] = decryptPass(uid, user.getPhone(), npassword); //数组0--加密后密码  1--明文密码
            if (null == password) {
                logger.warn("用户{}修改密码,系统加密失败", userId);
                return Response.errorMsg("系统加密失败");
            }

            Map<String, String> dmap = new HashMap<>();
            dmap.put("password", password[0]);
            dmap.put("id", String.valueOf(user.getUser_id()));
            int count = userDao.updateUserPass(dmap);
            //设置token
            String token = JwtToken.toToken(user.getUser_id(), uid);
            response.addHeader(TOKEN, token);
            //保存登陆信息
            TokenOperateUtil.save(user.getUser_id(), token);

            if (count > 0) {
                //修改授权服务器的密码
                UserAuth auser = new UserAuth();
                auser.setNickName(user.getUsername());
                auser.setPass(password[1]);
                auser.setName(user.getPhone());
                auser.setUid(user.getUser_id());
                authorizeRpcService.updateUser(auser);

                logger.info("用户{}修改密码,修改密码成功", userId);
                return Response.success();
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户{}修改密码,修改密码失败", userId, e);
        }
        logger.warn("用户{}修改密码,修改密码失败", userId);
        return Response.errorMsg("修改密码失败");
    }

    @Override
    public Response homeIndex(String userId) {
        Map<String, Object> map = new HashMap<>();
        //消息中心数量
        List<MsgSizeResponse> msgSizeResponse = userDao.findMsgSize(Integer.parseInt(userId));
        map.put("sysMsgSize", 0);
        map.put("noticeCenter", 0);
        map.put("officialNotice", 0);
        for (MsgSizeResponse size : msgSizeResponse) {
            if (size.getMsgCode() == null || size.getMsgCode() == MsgCodeEnum.SYS_MSG.getCode()) {
                map.put("sysMsgSize", size.getSize());
            }
            if (size.getMsgCode() == MsgCodeEnum.NOTICE_CENTER.getCode()) {
                map.put("noticeCenter", size.getSize());
            }
            if (size.getMsgCode() == MsgCodeEnum.OFFICIAL_NOTICE.getCode()) {
                map.put("officialNotice", size.getSize());
            }

        }

        try {
            logger.info("用户{}开始加载我的数据", userId);

            UserPO user = userDao.findUserOneById(userId);
            double useMoney = 0d; //可提现余额
            double accountTotal = 0d;  //总资产
            double totalInterest = 0d; //总收益
            double investTotal = 0d; //投资总额
            int hbNum = 0; //红包数量
            int hkNum = 0; //回款笔数
            AccountPO accountPO = accountService.getAccountByUserId(userId);
            if (null != accountPO) {
                accountTotal = NumberUtil.add(accountTotal, accountPO.getTotal());
                useMoney = accountPO.getUseMoney();
            }

            BorrowTender borrowTender = borrowRpcService.getBorrowTenderMoneyInfo(userId);
            if (null != borrowTender) {
                totalInterest = NumberUtil.add(totalInterest, borrowTender.getInterestPast());
                investTotal = NumberUtil.add(investTotal, borrowTender.getAccountPast());
            }

            FinanceMoney financeMoney = borrowRpcService.getFinanceMoneyInfo(userId);
            if (null != financeMoney) {
                accountTotal = NumberUtil.add(accountTotal, financeMoney.getPrincipal().add(financeMoney.getEndProfit()).doubleValue());
                //totalInterest = NumberUtil.add(totalInterest, financeMoney.getEndProfit().add(financeMoney.getWithdrawProfit()).doubleValue());
                totalInterest = NumberUtil.add(totalInterest, financeMoney.getNowProfit().doubleValue());
                investTotal = NumberUtil.add(investTotal, financeMoney.getPrincipal().add(financeMoney.getWithdrawPrincipal()).doubleValue());
            }

            //投资总额
            if (investTotal > 0) {
                map.put("isxs", 0);
            } else {
                map.put("isxs", 1);
            }

            map.put("accountTotal", accountTotal);
            map.put("interestTotal", totalInterest);
            map.put("useMoney", useMoney);
            map.put("investTotal", investTotal);

            hbNum = activityRpcService.getCountRedPacketsByUserId(userId, "", "0");
            hkNum = borrowRpcService.getUserFidCount(userId);
            map.put("hbNum", hbNum); //红包数量
            map.put("hkNum", hkNum); //回款笔数
            map.put("payChannel", user.getPayChannel() != null ? user.getPayChannel() : ""); //支付渠道
            map.put("phone", user.getPhone() != null ? TextUtil.hidePhoneNo(user.getPhone()) : "");//手机号
            map.put("memberLevel", user.getMemberlevel());//用户风险等级


            AccountFsstransPO fsstrans = accountFsstransService.getFsstransByUserId(userId);
            if (null != fsstrans) {
                map.put("fsstrans", fsstrans);
            } else {
                map.put("fsstrans", null);
            }

            Map<String, String> bmap = borrowRpcService.getUserBankCardByUserId(userId);
            if( bmap != null && bmap.size() > 0){
                map.put("bankCard", TextUtil.hideCardNo(bmap.get("bankCard"))); //银行卡号
                map.put("bankName", bmap.get("bankCardName")); //银行名称
                map.put("ymoney", bmap.get("ymoney") != null ? bmap.get("ymoney") : "0");
                map.put("dmoney", bmap.get("dmoney") != null ? bmap.get("dmoney") : "0");
            }else{
                map.put("bankCard", "");
                map.put("bankName", "");
                map.put("ymoney", "");
                map.put("dmoney", "");
            }

            //协约支付 银行卡
//            UserBankCardPO userBankCardPO = userBankCardService.getUserEnableCardByUserId(userId);
//            if (null != userBankCardPO ) {
//                map.put("bankCard", TextUtil.hideCardNo(userBankCardPO.getBankCard())); //银行卡号
//                map.put("bankName", userBankCardPO.getBankName()); //银行名称
//            } else {
//                map.put("bankCard", "");
//                map.put("bankName", "");
//            }

            //协议支付 银行卡
            UserBankCardPO agreeBank = userBankCardService.getAgreeEnableCardByUserId(userId);
            if (null != agreeBank && agreeBank.getProtocolno() != null ) {
                map.put("agreeBankCard", TextUtil.hideCardNo(agreeBank.getBankCard())); //银行卡号
                map.put("agreeBankName", agreeBank.getBankName()); //银行名称
                map.put("isBindBank", "1"); //是否绑卡
            } else {
//                map.put("agreeBankCard", "");
//                map.put("agreeBankName", "");
                map.put("isBindBank", "0"); //是否绑卡
            }

            String shareid = Rc4Utils.toHexString(user.getPhone(), shareKey);

            map.put("shareid", shareid);
            map.put("activityTopic", topic);
            CreditGoAccountPO goAccountPO = openAccountService.findOpenAccountByUserId(userId);
            Integer isOpenAccount = 0;
            Integer isSetBankCard = 0;
            Integer isSetPass = 0;
            Integer isSetPayment = 0;
            String openAccountName = null;
            String openAccountIdNo = null;
            String cardName = null;
            String cardNo = null;
            Double availBal =0d;
            if (goAccountPO!=null){
                isOpenAccount=goAccountPO.getAccountId()==null?0:1;
                isSetBankCard=goAccountPO.getCardNo()==null?0:1;
                isSetPass=goAccountPO.getIsSetPass();
                isSetPayment = goAccountPO.getPayment();
                if (StringUtils.isNotEmpty(goAccountPO.getName())){
                    openAccountName=TextUtil.hideRealnameChar(goAccountPO.getName());
                }
                if (StringUtils.isNotEmpty(goAccountPO.getIdNo())){
                    openAccountIdNo=TextUtil.hideIdNo(goAccountPO.getIdNo());
                }

              if (isOpenAccount==1){
                  map.put("openAccountName",openAccountName);  //脱敏后 姓名
                  map.put("openAccountIdNo",openAccountIdNo);//脱敏后身份证
              }
              if (isSetBankCard==1){
                  if (StringUtils.isNotEmpty(goAccountPO.getCardNo())){
                      cardName=BankCardBin.getNameOfBank(goAccountPO.getCardNo());
                      cardNo=TextUtil.hideCardNo(goAccountPO.getCardNo());
                  }
                  map.put("cardNo",cardNo);//  脱敏后 银行卡号
                  map.put("cardName",cardName);//  银行卡名称
              }else {
                   if (goAccountPO.getAccountId()!=null){
                       String cardNo2 = openAccountService.reFindCardNo(goAccountPO.getAccountId(),1);
                       if (StringUtils.isNotEmpty(cardNo2)){
                           cardName=BankCardBin.getNameOfBank(cardNo2);
                           cardNo=TextUtil.hideCardNo(cardNo2);
                           map.put("cardNo",cardNo);//  脱敏后 银行卡号
                           map.put("cardName",cardName);//  银行卡名称
                           isSetBankCard=1;
                       }

                   }
              }
                Map maps = openAccountService.balanceQuery(goAccountPO.getAccountId());
                if (maps!=null&& maps.get("availBal")!=null && !"".equals(maps.get("availBal"))){
                    availBal = Double.parseDouble(maps.get("availBal").toString()); //可用余额
                }

            }
            map.put("paymentAuth",isSetPayment);//是否设置缴费授权
            map.put("availBal",availBal);//  可用余额
            map.put("isOpenAccount",isOpenAccount);  //是否开户
            map.put("isSetBankCard",isSetBankCard);  //是否绑卡
            map.put("isSetPass",isSetPass);//是否设置密码

            logger.info("用户{}加载我的数据成功shareid={}", userId, shareid);
            return Response.successByMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户{}加载数据失败,异常信息：{}", userId, e);
        }

        logger.warn("用户{}加载数据失败", userId);
        return Response.errorMsg("加载数据失败");

    }

    @Override
    public Response withdraw(String tmoney, String webhost, String trustreturl, String trustbgreturl, String hhhost, String mercustid, String userId, HttpServletResponse response) {
        logger.info("用户{}开始提现money={}", userId, tmoney);
        UserPO user = userDao.findUserOneById(userId);

        /** 提现记录 */
        AccountCashPO cash = new AccountCashPO();
        cash.setUserId(Integer.valueOf(userId));

        cash.setAddtime(DateUtils.getNowTimeStr());
        cash.setAddip("");
        cash.setTotal(tmoney);
        cash.setStatus(0);

        /** 平台提现服务费 */
        String cashFee = "0";  //提现费率
        String maxCash = "10000000"; //最高提现额度
        String mixCash = "2"; //最低提现额度

        AccountPO account = accountService.getAccountByUserId(userId);

        if (!NumberUtil.isNumeric(tmoney)) {

            return Response.errorMsg("请输入正确的金额");
        }

        if (Double.valueOf(tmoney) < 0) {
            return Response.errorMsg("请输入正确的金额");
        }


        if (Double.valueOf(tmoney) >= Double.valueOf(maxCash)) {

            return Response.errorMsg("提现金额不能大于" + maxCash);
        }
        if (account.getUseMoney() < Double.valueOf(tmoney)) {

            return Response.errorMsg("您没有足够的余额");
        }

        if (Double.valueOf(tmoney) < Double.valueOf(mixCash)) {

            return Response.errorMsg("提现金额不能小于" + mixCash);
        }

        double fee = 0;

        if (StringUtil.isEmpty(cashFee)) {
            fee = 0;
        } else {
            /** 平台收取提现服务费*/
            if (user.getTypeId() == Constant.WEB_USER_TYPEID_L) {
                //投资人
                fee = Double.valueOf(cashFee) * Double.valueOf(tmoney);
            } else {
                //借款人需手续费
//              fee = Double.valueOf(cashFee)*Double.valueOf(tmoney);
            }
        }

        double credited = Double.valueOf(tmoney) - fee;

        cash.setFee(fee + "");
        cash.setCredited(credited + "");
        accountService.addAccountCash(cash);

        EnchashmentTrustPost etp = new EnchashmentTrustPost();
        etp.setVersion("20");
        etp.setCmdId("Cash");
        etp.setOrdId(String.valueOf(cash.getId()));
        etp.setUsrCustId(user.getTrustUsrCustId());
        etp.setTransAmt(NumberUtil.strFormat2Round(credited));
        etp.setServFee(NumberUtil.strFormat2Round(String.valueOf(fee)));
        etp.setServFeeAcctId("MDT000001");
        etp.setRetUrl(webhost + trustreturl);
        etp.setBgRetUrl(webhost + trustbgreturl);
        etp.setMerCustId(mercustid);

        /**汇付提现手续费收取方*/
        String whoPay = "U";
        if (StringUtils.isNotEmpty(whoPay)) {
            CashReqExt cre = new CashReqExt();
            cre.setFeeObjFlag(whoPay);
            if ("M".equals(whoPay)) {
                cre.setFeeAcctId("MDT000001");
            } else {
//        		FAST|GENERAL|IMMEDIATE
                //cre.setCashChl("FAST|GENERAL");
                cre.setCashChl("GENERAL");
            }
            Gson gson = new Gson();
            etp.setReqExt("[" + gson.toJson(cre) + "]");
        }

        etp.setChkValue(etp.getChkValue());

        String url = hhhost + "?MerCustId=" + etp.getMerCustId() + "&CmdId=" + etp.getCmdId()

                + "&Version=" + etp.getVersion() + "&BgRetUrl=" + etp.getBgRetUrl() + "&UsrCustId=" + etp.getUsrCustId() + "&OrdId=" + etp.getOrdId()

                + "&TransAmt=" + etp.getTransAmt() + "&ServFee=" + etp.getServFee() + "&ServFeeAcctId=" + etp.getServFeeAcctId()
                + "&RetUrl=" + etp.getRetUrl() + "&ReqExt=" + etp.getReqExt();

        url = url + "&ChkValue=" + etp.getChkValue();

        logger.info("提现向第三方提交参数url---" + url);
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void withdrawNotify(ModelMap map, Map<String, String> params, HttpServletResponse response, String mercustid) {
        EnchashmentTrustReturn etr = new EnchashmentTrustReturn();
        etr.setRespType(params.get("RespType"));
        etr.setCmdId(params.get("CmdId"));
        etr.setRespCode(params.get("RespCode"));
        etr.setRespDesc(params.get("RespDesc"));
        etr.setMerCustId(params.get("MerCustId"));
        etr.setOrdId(params.get("OrdId"));
        etr.setUsrCustId(params.get("UsrCustId"));
        etr.setTransAmt(params.get("TransAmt"));// 交易金额，提现提交金额
        etr.setRealTransAmt(params.get("RealTransAmt"));// 实际到账金额
        etr.setOpenAcctId(params.get("OpenAcctId"));
        etr.setOpenBankId(params.get("OpenBankId"));
        etr.setFeeAmt(params.get("FeeAmt"));//手续费金额
        etr.setFeeCustId(params.get("FeeCustId"));//手续费扣款客户号
        etr.setFeeAcctId(params.get("FeeAcctId"));
        etr.setServFee(params.get("ServFee"));//商户收取服务费金额
        etr.setServFeeAcctId(params.get("ServFeeAcctId"));
        etr.setRetUrl(params.get("RetUrl"));
        etr.setBgRetUrl(params.get("BgRetUrl"));
        etr.setMerPriv(params.get("MerPriv"));
        etr.setRespExt(params.get("RespExt"));
        etr.setMerCustId(mercustid);
        try {
            etr.setRetUrl(URLDecoder.decode(params.get("RetUrl"), "UTF-8"));
            etr.setBgRetUrl(URLDecoder.decode(params.get("BgRetUrl"), "UTF-8"));
            etr.setMerPriv(URLDecoder.decode(params.get("MerPriv"), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        boolean flag = false;
        try {
            flag = SignUtils.verifyByRSA(etr.getChkValue(), params.get("ChkValue"));
            logger.info("withdraw==============" + flag + "=================11111=============");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!flag) {

            //验签失败

        } else {

            if (StringUtils.isEmpty(etr.getRespType()) && etr.getRespCode().equals("000")) {

                map.addAttribute("returnstr", RECV_ORD_ID + etr.getOrdId());
                AccountCashPO ac = accountService.getAccountCashById(Integer.valueOf(etr.getOrdId()));

                logger.info("withdraw==============" + ac + "=================222=============");

                int newVersion = 0;
                if (ac.getVersion() == null) {
                    newVersion = 1;
                } else {
                    newVersion = ac.getVersion() + 1;
                }

                if (ac.getStatus() == 0) {
                    double TransAmt = NumberUtil.getDouble(etr.getTransAmt());//交易金额(提现提交金额)
                    logger.info("提现提交金额TransAmt：" + TransAmt);

                    double RealTransAmt = NumberUtil.getDouble(etr.getRealTransAmt());// 实际到账金额
                    logger.info("实际到账金额：" + RealTransAmt);

                    double FeeAmt = NumberUtil.getDouble(etr.getFeeAmt());//手续费
                    logger.info("手续费金额feeAmt：" + FeeAmt);

                    double ServFee = NumberUtil.getDouble(etr.getServFee());//商户服务手续费
                    logger.info("商户收取服务费金额ServFee：" + ServFee);

                    AccountPO account = accountService.getAccountByUserId(ac.getUserId() + "");
                    AccountLogPO alog = new AccountLogPO();


                    //账号减少的钱
                    double tmoney = 0.00;
                    double fee = 0.00;


                    /**汇付提现手续费收取方*/
                    String whoPay = "U";
                    if (StringUtils.isNotEmpty(whoPay)) {
                        if ("M".equals(whoPay)) {
                            String cashFee = "0";
                            if (StringUtils.isNotEmpty(cashFee)) {
                                tmoney = RealTransAmt + ServFee;
                                fee = ServFee;
                                ac.setVerifyRemark("通过第三方托管提现" + tmoney + "元,扣除平台服务费" + ServFee + "元");
                                alog.setRemark("通过第三方托管提现" + tmoney + "元,扣除平台服务费" + ServFee + "元");
                            } else {
                                tmoney = RealTransAmt;
                                ac.setVerifyRemark("通过第三方托管提现" + tmoney + "元");
                                alog.setRemark("通过第三方托管提现" + tmoney + "元");
                            }
                        } else {
                            String cashFee = "0";
                            if (StringUtils.isNotEmpty(cashFee)) {
                                tmoney = RealTransAmt + ServFee + FeeAmt;
                                fee = ServFee + FeeAmt;
                                ac.setVerifyRemark("通过第三方托管提现" + tmoney + "元,扣除平台服务费" + ServFee + "元,扣除提现手续费" + FeeAmt + "元");
                                alog.setRemark("通过第三方托管提现" + tmoney + "元,扣除平台服务费" + ServFee + "元,扣除提现手续费" + FeeAmt + "元");
                            } else {
                                tmoney = RealTransAmt + FeeAmt;
                                fee = FeeAmt;
                                ac.setVerifyRemark("通过第三方托管提现" + tmoney + "元,扣除提现手续费" + FeeAmt + "元");
                                alog.setRemark("通过第三方托管提现" + tmoney + "元,扣除提现手续费" + FeeAmt + "元");
                            }
                        }
                    }

                    ac.setTotal(String.valueOf(tmoney));
                    ac.setCredited(String.valueOf(tmoney - fee));
                    ac.setFee(String.valueOf(fee));
                    ac.setStatus(1);  //审核通过
                    ac.setVerifyUserid(1L);  //表明是系统自动审核通过
                    ac.setVerifyTime(DateUtils.getNowTimeStr());
                    ac.setVersion(newVersion);


                    alog.setAddtime(DateUtils.getNowTimeStr());
                    alog.setAddip("");
                    accountService.cashing(ac, alog, 1);
                }
            } else if (etr.getRespCode().equals("999")) {

                //不做任何操作，等待异步通知

            } else {

                /**
                 * 解冻资金
                 */
//               AccountCash ac = accountService.queryCashById(Long.valueOf(etr.getOrdId()));
//
//               double tamoney = Double.valueOf(etr.getRealTransAmt())+Double.valueOf(etr.getFeeAmt());
//
//               if(ac.getStatus()==0){
//
//                   ac.setTotal(String.valueOf(tamoney));
//                   ac.setStatus(2);  //审核失败
//                   ac.setVerifyUserid(1L);  //表明是系统自动审核通过
//                   ac.setVerifyRemark("通过第三方托管提现"+etr.getRealTransAmt()+"元失败");
//
//                   AccountLog alog = new AccountLog();
//
//                   alog.setAddtime(getNowCreateTime());
//                   alog.setAddip(getRequestIp(request));
//
//                   accountService.cashing(ac, alog, 2);
//
//                   return "redirect: myhome/withdraw.html?msg="+"提现申请失败，请重新申请！";
//               }

            }

        }
//        return "huifureturn";
    }

    @Override
    public List<UserPO> getUsersByInviterInTime(int inviter, int page, String starttime, String endtime) {
        BasePage.setPage(page);
        List<UserPO> list = userDao.getUsersByInviterInTime(inviter,starttime,endtime);
        return list;
    }

    @Override
    public int getUserCountByInviterInTime(int inviter, String starttime, String endtime) {
        return userDao.getUserCountByInviterInTime(inviter,starttime,endtime);
    }

    @Override
    public Response sendFuiouCode(String userId) {
        try {
            UserBankCardPO userBankCardPO = userBankCardDao.getAgreeEnableCardByUserId(userId);

            logger.info("{}发送购买验证码", userBankCardPO.getPhone());
            String content = "付款验证码：{rand}，您正在使用尾号"+TextUtil.hideBankCard(userBankCardPO.getBankCard())+"银行卡支付，请勿向任何人提供您收到的短信验证码，若非本人操作，请致电400-888-7140";
            sendBakMsg(userBankCardPO.getPhone(), content, ustatus);
        }catch (Exception e){
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        return  Response.success();
    }


}


