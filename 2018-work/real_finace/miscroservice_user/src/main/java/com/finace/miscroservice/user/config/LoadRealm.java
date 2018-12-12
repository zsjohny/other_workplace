package com.finace.miscroservice.user.config;


import com.finace.miscroservice.commons.entity.UserAuth;
import com.finace.miscroservice.commons.enums.PushExtrasEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.*;
import com.finace.miscroservice.commons.utils.tools.MD5;
import com.finace.miscroservice.commons.utils.tools.TextUtil;
import com.finace.miscroservice.user.dao.PcUserDao;
import com.finace.miscroservice.user.dao.UserDao;
import com.finace.miscroservice.user.entity.po.Register;
import com.finace.miscroservice.user.po.UserPO;
import com.finace.miscroservice.user.rpc.AuthorizeRpcService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.finace.miscroservice.commons.utils.JwtToken.TOKEN;


/**
 * 自定义的realm实现
 */
@Component
public class LoadRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PcUserDao pcUserDao;

    @Autowired
    @Lazy
    private AuthorizeRpcService authorizeRpcService;

    private Log logger = Log.getInstance(LoadRealm.class);

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;


    /**
     * 之前注册老用户的判断标识
     */
    private ThreadLocal<Boolean> priorRegisterFlag = new ThreadLocal<>();

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String userPhone = (String) authenticationToken.getPrincipal();
        logger.info("用户{} 开始进行校验登录", userPhone);
        AuthenticationInfo authenticationInfo = null;

        if (Regular.checkPhone(userPhone) || Regular.checkUserName(userPhone)) {
            UserPO user = getUserByPhone(userPhone);

            if (user != null) {

                String pass = user.getPassword();

                //判断是否是之前注册的密码
                if (priorRegisterFlag.get() == null) {
                    pass = AesUtil.decode(pass, userPhone);
                }
                logger.info("验证参数为userPhone={}，pass={}，userName={}", userPhone, pass, user.getUsername());
                authenticationInfo = new SimpleAuthenticationInfo(userPhone, pass, user.getUsername());


            }
            logger.info("用户{} 结束进行校验登录", userPhone);
            return authenticationInfo;
        }
        logger.info("用户{} 输入登陆信息不符合规则", userPhone);

        return authenticationInfo;
    }

    /**
     * 根据用户的手机查找用户信息
     *
     * @param phone 用户的手机
     * @return
     */
    private UserPO getUserByPhone(String phone) {
        UserPO userPO;
        UserAuth userAuth = authorizeRpcService.findUserByName(phone);
        if (userAuth == null || userAuth.wasEmpty()) {
            logger.info("用户{}登录，从数据库读取", phone);
            userPO = userDao.getUserByUserPhone(phone);
        } else {
            logger.info("用户{}登录，从认证服务器读取", phone);
            userPO = new UserPO();
            userPO.setPhone(userAuth.getName());
            userPO.setPassword(AesUtil.encode(userAuth.getPass(), userPO.getPhone()));
            userPO.setUser_id(userAuth.getUid());
            userPO.setUsername(userAuth.getNickName());
        }

        return userPO;
    }


    /**
     * 注册
     *
     * @param userPO   用户信息
     * @param response 返回response
     * @return
     */
    public Boolean reg(String uid, UserPO userPO, HttpServletResponse response, String pushId) {

        Boolean regFlag = Boolean.FALSE;

        try {
            //校验参数
            if (userPO == null || userPO.isEmpty() || StringUtils.isEmpty(userPO.getUid())) {
                logger.warn("用户注册所传信息为空");
                return regFlag;
            }

            //先是base64转码编译
            String base64Pass = new String(Base64.getDecoder().decode(userPO.getPassword()));
            if (!base64Pass.matches("[\\w+=/\\\\]+")) {
                logger.warn("用户 ={} 注册 所输入的密码={} 不符合规范", userPO.getPhone(), userPO.getPassword());
                return regFlag;
            }

            //首先是密码进行前端解密
            String realPass = DesUtil.decrypt(base64Pass, userPO.getUid());

            if (realPass.length() < 6) {
                logger.warn("用户={} 注册 所输入的密码不符合规范,密码长度小于6", userPO.getPhone());
                return regFlag;
            }

            if (realPass.length() > 15) {
                logger.warn("用户={} 注册 所输入的密码不符合规范,密码长度大于15", userPO.getPhone());
                return regFlag;
            }

            if (StringUtils.isEmpty(realPass)) {
                logger.warn("用户={} 注册 所输入的密码不符合规范,密码不能为空", userPO.getPhone());
                return regFlag;
            }

            //设置新密码
            userPO.setPassword(AesUtil.encode(realPass, userPO.getPhone()));

            //新增用户
            int iu = userDao.insterUser(userPO);

            if (iu <= 0) {
                logger.warn("用户={} 注册失败", userPO.getPhone());
                return Boolean.FALSE;
            }
            //修改注册临时表信息
            Register register = pcUserDao.findRegisterTmp(userPO.getPhone());
            if (register != null) {
                pcUserDao.upRegisterTmp(userPO.getPhone());
            }
            //插入认证服务器start--------------------
            UserAuth user = new UserAuth();
            user.setNickName(userPO.getUsername());
            user.setPass(realPass);
            user.setName(userPO.getPhone());
            user.setUid(iu);
            List<UserAuth> users = new ArrayList<>();
            users.add(user);
//            logger.info("用户={} 注册渠道信息2：{}",userPO.getPhone(),userPO.getRegChannel());
            authorizeRpcService.insertUsers(users);
            //插入认证服务器end--------------------


            userPO.setUser_id(iu);
            //设置token
            String token = JwtToken.toToken(userPO.getUser_id(), userPO.getUid());
            response.addHeader(TOKEN, token);
            //保存登陆信息
            TokenOperateUtil.save(userPO.getUser_id(), token);

            if (null != pushId && !"".equals(pushId)) {
                pushMsg(uid, pushId, String.valueOf(userPO.getUser_id()), userPO.getPhone());//登录消息推送
                userStrHashRedisTemplate.set(Constant.PUSH_ID + userPO.getUser_id(), pushId);
            }
            logger.info("用户={} 注册设置token成功", userPO.getPhone());

            regFlag = Boolean.TRUE;

        } catch (Exception e) {
            logger.error("用户={} 注册失败", userPO.getPhone(), e);
        }
        return regFlag;
    }


    /**
     * 校验登录
     *
     * @param phone 用户手机
     * @param pass  用户密码
     * @param uid   用户手机标识
     * @return
     */
    public Boolean load(String phone, String pass, String uid, HttpServletResponse response, String pushId) {
        Boolean loadFlag = Boolean.FALSE;

        try {


            //检验参数
            if (StringUtils.isAnyEmpty(phone, pass, uid)) {
                logger.warn("用户所传phone={} pass={} uid={} 部分参数为空", phone, pass, uid);
                return loadFlag;
            }

            //先是base64转码编译
            String base64Pass = new String(Base64.getDecoder().decode(pass));
            if (!base64Pass.matches("[\\w+=/\\\\]+")) {
                logger.warn("用户={} 登陆 所输入的密码={} 不符合规范", phone, pass);
                return loadFlag;
            }

            //首先是密码进行前端解密
            String realPass = DesUtil.decrypt(base64Pass, uid);

            if (StringUtils.isEmpty(realPass)) {
                logger.warn("用户={} 登陆 所输入的密码不符合规范", phone);
                return loadFlag;
            }

            String entryPass = realPass;


            //先获取用户注册信息
            UserPO user = getUserByPhone(phone);

            if (user == null || user.isEmpty()) {
                logger.warn("数据库查询不到用户={} 的信息", phone);
                return loadFlag;
            } else {
                //如果是之前的md5则不需要进行解密
                if (user.getPassword().matches("[0-9abcdef]{32}")) {
                    priorRegisterFlag.set(Boolean.TRUE);
                    entryPass = MD5.getInstance().getMD5ofStr(realPass);
                }
            }


            //验证认证服务器start--------------------
            UserAuth userAuth = new UserAuth();
            userAuth.setName(phone);
            userAuth.setPass(realPass);
            userAuth.setUid(user.getUser_id());
            //验证认证服务器end--------------------

            if (!authorizeRpcService.checkUserPass(userAuth)) {
                //降级本地shiro 处理
                //校验
                UsernamePasswordToken token = new UsernamePasswordToken(user.getPhone(), entryPass);

                SecurityUtils.getSubject().login(token);

            }

            //如果是md5清除
            if (priorRegisterFlag.get() != null) {
                //把之前的密码修改并且清除缓存
                Map modifyMap = new HashMap();
                modifyMap.put("password", AesUtil.encode(realPass, user.getPhone()));
                modifyMap.put("id", user.getUser_id());
                //修改密码
                userDao.updateUserPass(modifyMap);

            }


            //设置token
            String token = JwtToken.toToken(user.getUser_id(), uid);
            response.addHeader(TOKEN, token);
            loadFlag = Boolean.TRUE;
            //保存登陆信息
            TokenOperateUtil.save(user.getUser_id(), token);

            if (null != pushId && !"".equals(pushId)) {
                pushMsg(uid, pushId, String.valueOf(user.getUser_id()), phone);//登录消息推送
                userStrHashRedisTemplate.set(Constant.PUSH_ID + user.getUser_id(), pushId);
            }
        } catch (AuthenticationException e) {
            logger.warn("用户{},登录失败", phone, e);
        } catch (Exception e) {

            logger.error("用户{},登录异常", phone, e);

        } finally {
            //清除内存
            if (priorRegisterFlag != null) {
                priorRegisterFlag.remove();
            }
        }

        return loadFlag;
    }

    /**
     * 登录消息推送
     *
     * @param pushId
     */
    private void pushMsg(String uid, String pushId, String userId, String phone) {
        String rpushId = userStrHashRedisTemplate.get(Constant.PUSH_ID + userId) != null ? userStrHashRedisTemplate.get(Constant.PUSH_ID + userId) : "";
        logger.info("登录消息推送, rpushId={},pushId={}, userId={}", rpushId, pushId, userId);
        if (!pushId.equals(rpushId) && StringUtils.isNotEmpty(rpushId)) {
            logger.info("开始登录消息推送,向用户{}推送消息rpushId={},pushId={}", userId, rpushId, pushId);
            Map<String, String> map = new HashMap<>();
            map.put("type", PushExtrasEnum.LOGIN_ONLY.getCode());
            map.put("phone", TextUtil.hidePhoneNo(phone));
            JiguangPush.sendPushIosAndroidMsgByalias(PushExtrasEnum.LOGIN_ONLY.getValue(), rpushId, map);
        }
    }

}
