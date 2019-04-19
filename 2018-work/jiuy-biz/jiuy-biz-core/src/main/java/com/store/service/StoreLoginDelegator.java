package com.store.service;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import com.jiuyuan.entity.StoreAudit;
import com.jiuyuan.entity.common.DataDictionary;
import com.jiuyuan.entity.log.UserLoginLogNew;
import com.jiuyuan.entity.newentity.ShopNotification;
import com.jiuyuan.service.common.*;
import com.jiuyuan.service.common.logs.ILogsService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.GetuiUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.jiuyuan.constant.CookieConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.UserLoginLog;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.WhitePhoneService;
import com.jiuyuan.util.ClientUtil;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.util.oauth.common.credential.IRawDataCredentials;
import com.jiuyuan.util.oauth.common.credential.RawDataCredentials;
import com.jiuyuan.util.oauth.sns.common.response.ISnsResponse;
import com.jiuyuan.util.oauth.sns.common.response.SnsResponseType;
import com.jiuyuan.util.oauth.sns.common.user.ISnsEndUser;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2API;
import com.jiuyuan.util.oauth.v2.V2Constants;
import com.jiuyuan.web.help.JsonResponse;
import com.store.enumerate.StoreAuditStatusEnum;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;


@Service
public class StoreLoginDelegator{
    private static final Logger logger = LoggerFactory.getLogger(StoreLoginDelegator.class);

    private static final int ORIGINAL_FIRST_LOGIN = 0;
    private static final int FIRST_LOGIN = 1;
    private static final int NO_FIRST_LOGIN = 2;

    /**
     * app版本号
     */
    public static final String APP_VERSION_372 = "372";

    @Autowired
    private StoreUserService userService;

    @Autowired
    private UcpaasService ucpaasService;

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private StoreAuditServiceShop storeAuditService;

    @Autowired
    private WhitePhoneService whitePhoneService;

    @Autowired
    private YunXinSmsService yunXinSmsService;

    @Autowired
    @Qualifier( "weiXinV2API4App" )
    private WeiXinV2API weiXinV2API;

    @Autowired
    private UserNewMapper userNewMapper;

    @Autowired
    private IStoreBusinessNewService storeBusinessNewService;

    @Autowired
    private ShopGlobalSettingService globalSettingService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private ILogsService logsService;

    private static ThreadPoolTaskExecutor threadPoolTaskExecutor;

    static {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(200);
        executor.setKeepAliveSeconds(120);
        executor.setQueueCapacity(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        threadPoolTaskExecutor = executor;
    }


    /**
     * 手机登陆，判断用户名和密码是否为空
     *
     * @param username
     * @param password
     * @param response
     * @param ip
     * @param client
     * @return
     */
    public JsonResponse mobileSubmitLogin(String username, String password, HttpServletRequest request,
                                          HttpServletResponse response, String ip,
                                          ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();
        if (checkNull(username)) {
            return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USERNAME_NULL);
        }
        if (checkNull(password)) {
            return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_PASSWORD_NULL);
        }
        return submitLogin(username, password, request, response, ip, client);
    }


    public JsonResponse verifyCommitV24(String phone, String verifyCode, String sendType, HttpServletRequest request,
                                        HttpServletResponse response,
                                        String ip, ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        if (whitePhoneService.getWhitePhone(phone) == 0) {
            // 如果手机号不在白名单
            if (sendType.equals("sms") && ! yunXinSmsService.verifyCode(phone, verifyCode)) {
                return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
            } else if (sendType.equals("voice") && ! ucpaasService.verifyCode(phone, verifyCode)) {
                return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
            }
        }
        StoreBusiness storeBusiness = userService.getStoreBusinessByPhone(phone);
        String nextStep = "index";
        if (storeBusiness == null) {
            data.put("needBindWeixin", "YES");
            data.put("needFillData", "YES");
            // 暂时不启用
            data.put("waitAudit", "YES");
            // 创建手机用户？
        } else if (storeBusiness.getBindWeixinId() == null || storeBusiness.getBindWeixinId().trim().length() <= 0) {
            data.put("needBindWeixin", "YES");
            data.put("needFillData", "YES");
            data.put("waitAudit", "YES");
        } else if (storeAuditService.getAuditCount(storeBusiness.getId(),
                // 已通过
                StoreAuditStatusEnum.pass.getIntValue()) > 0) {
            data.put("login", "YES");
            data.put("needFillData", "NO");
            data.put("auditStatus", "1");
            loginUser(storeBusiness, request, response, ip, client);
        } else if (storeAuditService.chectStoreBusiness(storeBusiness)) {
            data.put("needFillData", "YES");

            data.put("waitAudit", "YES");
            data.put("phoneNumber", storeBusiness.getPhoneNumber());
        } else if (storeAuditService.getAuditCount(storeBusiness.getId(),
                // 已提交审核
                StoreAuditStatusEnum.submit.getIntValue()) > 0) {
            data.put("needFillData", "NO");
            data.put("auditStatus", "0");
            loginUser(storeBusiness, request, response, ip, client);
        } else if (storeAuditService.getAuditCount(storeBusiness.getId(),
                // 审核未通过
                StoreAuditStatusEnum.fail.getIntValue()) > 0) {
            data.put("needFillData", "YES");
            data.put("result", "");
            data.put("auditStatus", "-1");
            data.put("phoneNumber", storeBusiness.getPhoneNumber());
            loginUser(storeBusiness, request, response, ip, client);
        } else if (storeAuditService.getAuditCount(storeBusiness.getId(),
                // 已禁用
                StoreAuditStatusEnum.forbidden.getIntValue()) > 0) {
            return jsonResponse.setResultCode(ResultCode.STOREBUSINESS_FORBIDDEN).setError("该账号已禁用");
        } else {
            // 暂时不启用
            data.put("login", "NO");
            data.put("needBindWeixin", "NO");
            data.put("needFillData", "YES");
            data.put("waitAudit", "YES");
        }
        if (data.get("needBindWeixin") != null && data.get("needBindWeixin").equals("YES")) {
            nextStep = "bindWeixin";
        } else if (data.get("needFillData") != null && data.get("needFillData").equals("YES")) {
            nextStep = "fillData";
        }
        data.put("nextStep", nextStep);
        //查看该用户是否第一次登录 TODO ?
        if (storeBusiness == null) {
            data.put("firstLogin", false);
        } else {
            boolean firstLogin = storeBusiness.getFirstLoginStatus() == FIRST_LOGIN ? true : false;
            data.put("firstLogin", firstLogin);
        }

        data.put("tip", "俞姐姐门店宝批发精品女装，价格仅对“门店采购负责人”开放，请理解...");

        //登陆多设备校验
        userLoginLogSwitcherV372(request, 1, phone);
        return jsonResponse.setSuccessful().setData(data);
    }



    /**
     * 372版本, 暂时不要允许多设备登录
     * 在这里, 做全部控制, 是否运行多设备登录
     * @param: request
     * @param: loginWay
     * @param: phone
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/5/27 17:03
     */
    private void userLoginLogSwitcherV372(HttpServletRequest request, Integer loginWay, String phone) {
//        if (StringUtils.isNotBlank(phone)) {
//            userLoginLog(request, loginWay, phone);
//        }
    }


    /**
     * 手机验证码登录 版本3.7.2
     * 用户可以直接用手机号验证码登录认证, 生成自己的门店id, 门店id默认审核通过
     *
     * @param: phone
     * @param: verifyCode
     * @param: sendType
     * @param: request
     * @param: response
     * @param: ip
     * @param: client
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/17 10:22
     */
    @Transactional( rollbackFor = Exception.class )
    public JsonResponse verifyLoginV372(String phone, String verifyCode, String sendType, HttpServletRequest request, HttpServletResponse response, String ip, ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();

        if (whitePhoneService.getWhitePhone(phone) == 0) {
            // 如果手机号不在白名单
            boolean phoneVerifySuccess = phoneVerify(verifyCode, sendType, phone);
            if (! phoneVerifySuccess) {
                return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
            }
        }

        HashMap<String, Object> result = new HashMap<>(2);
        result.put("isFirstLogin", false);
        //获取用户信息, 首次登录则生成一条新的用户信息
        StoreBusiness user = storeBusinessNewService.getStoreBusinessByPhone(phone);
        if (user == null) {
            user = buildDefaultStore();
            user.setFirstLoginStatus(FIRST_LOGIN);
            user.setPhoneNumber(phone);
            storeBusinessNewService.add(user);
            user.setBusinessNumber(user.getId().longValue() + 800000000);
            storeBusinessNewService.update(user);
            result.put("isFirstLogin", true);
            result.put("coupon",true);
            result.put("storeId",user.getId());
            //发优惠券
            // sendFirstLoginCoupon(user);
        } else {
            //3.7.2版本以后的用户, 更新登录状态
            if (!isAppVersionBeforeV372(user) && user.getFirstLoginStatus() != FIRST_LOGIN) {
                user.setFirstLoginStatus(NO_FIRST_LOGIN);
                storeBusinessNewService.update(user);
            }
            //兼容3.7.2版本以前的用户状态
            compatibleHistoryBeforeV372(user);

        }



        //登录
        loginUser(user, request, response, ip, client);
        result.put("tip", "俞姐姐门店宝批发精品女装，欢迎您");

        userLoginLogSwitcherV372(request, 1, phone);
        return jsonResponse.setSuccessful().setData(result);
    }


    /**
     * 兼容历史版本, 更新新版本后, 更新店铺资料审核状态, 并且补发版本迭代的新人优惠券
     * 3.72版本以前的用户, 在更新版本以后, 将登录认证设为通过
     *
     * @param: user
     * @return: 是否需要派发优惠券:
     *  这是一个特殊情况, 用户用老版本登录, 但是未提交认证, 再更新新版本, 需要领优惠券
     * @auther: Charlie(唐静)
     * @date: 2018/5/22 15:09
     */
    private boolean compatibleHistoryBeforeV372(StoreBusiness user) {
        boolean needSendCoupon = false;
        if (user == null) {
            throw new NullPointerException("StoreBusiness 对象不可为空");
        }
        //低于372的版本
        if (isAppVersionBeforeV372(user)) {
            /* 老版本的 0：提交审核 分两个状态, 注册并已提交和注册未提交资料, 老版本都是提交状态 */
            if (user.getAuditStatus() == 0) {
                List<StoreAudit> history = storeAuditService.getAuditByStoreId(user.getId());
                if (history == null || history.size() == 0) {
                    //未提交过认证资料 history
                    user.setDataAuditStatus(2);
                } else {
                    //提交过认证资料
                    user.setDataAuditStatus(0);
                }

            } else {
                user.setDataAuditStatus(user.getAuditStatus());
            }

            /* 老版本用户一次成功的认真审核都没有也没有, 派发优惠券, 设为首次登录 */
            if (user.getAuditStatus() != 1 && user.getFirstLoginStatus() == ORIGINAL_FIRST_LOGIN){
                needSendCoupon = true;
                user.setFirstLoginStatus(FIRST_LOGIN);
            }

            user.setDataAuditTime(user.getAuditTime());
            user.setAppId(APP_VERSION_372);
//            user.setAuditStatus(StoreAuditStatusEnum.pass.getIntValue());
//            user.setAuditTime(System.currentTimeMillis());
            storeBusinessNewService.update(user);
            logger.warn("用户登录,更新登录认证信息:storeId=" + user.getId() + ";auditStatus=" + user.getAuditStatus() + ";auditTime=" + user.getAuditTime());
        }

        /* 发优惠券 */
//        if (needSendCoupon) {
//            // sendFirstLoginCoupon(user);
//        }
        return needSendCoupon;
    }

    /**
     * 用户登录版本
     * @param: user
     * @return: boolean 用户版本是3.7.2版本以前的
     * @auther: Charlie(唐静)
     * @date: 2018/5/25 9:05
     */
    public static boolean isAppVersionBeforeV372(StoreBusiness user) {
        return StringUtils.isBlank(user.getAppId()) || Integer.parseInt(user.getAppId()) < Integer.parseInt(APP_VERSION_372);
    }


    /**
     * 创建一个默认的实体bean
     *
     * @param: phone 电话号码
     * @return:
     * @auther: Charlie(唐静)
     * @date: 2018/5/17 14:07
     */
    public static StoreBusiness buildDefaultStore() {
        long curr = System.currentTimeMillis();
        StoreBusiness user = new StoreBusiness();
        //账户资金
        user.setCashIncome(0.0);
        user.setAvailableBalance(0.0);
        //提现申请次数
        user.setWithdrawApply(0);
        user.setCreateTime(curr);
        user.setUpdateTime(curr);
        user.setBankCardFlag(0);
        user.setAlipayFlag(0);
        user.setWeixinFlag(0);
        user.setBusinessType(0);
        user.setLastErrorWithdrawPasswordTime(0L);
        user.setErrorCount(0);
        user.setDeep(1L);
        user.setOneStageTime(0);
        user.setTwoStageTime(0);
        user.setThreeStageTime(0);
        user.setGroundUserId(0L);
        user.setShopReservationsOrderSwitch(1);
        user.setGreetingSendType(0);
        user.setGreetingWords("");
        user.setGreetingImage("");

        //默认值
        user.setCommissionPercentage(0.0);
        user.setMemberCommissionPercentage(0.0);
        user.setDefaultCommissionPercentage(0.0);
        user.setHasHotonline(0);
        user.setSynchronousButtonStatus(0);
        user.setVip(0);
        user.setSupplierId(0L);
        user.setIsOpenWxa(0);
        user.setUsedCouponTotalMemberCount(0);
        user.setUsedCouponTotalCount(0);
        user.setUsedCouponTotalMoney(0.0);
        user.setWxaType(0);
        user.setRate(0.0);
        user.setWxaArticleShow(0);
        user.setWxaOpenTime(0L);
        user.setWxaCloseTime(0L);
        user.setBankCardUseFlag(0);
        user.setAlipayUseFlag(0);
        user.setWeixinUseFlag(0);
        user.setMemberNumber(0);
        user.setStoreArea(0.0);
        user.setStoreShowImgs("[]");
        user.setGrade(0);
        user.setWxaRenewProtectCloseTime(0L);
        user.setHotOnline("");
        user.setOnlineWxaVersion("1.1.1");
        user.setAppId(APP_VERSION_372);
        //首次登录
        user.setFirstLoginStatus(FIRST_LOGIN);

        user.setAuditStatus(StoreAuditStatusEnum.submit.getIntValue());
        user.setAuditTime(0L);

        //店铺资料认证 2未提交
        user.setDataAuditStatus(2);
        user.setDataAuditTime(0L);

        //0:未激活；>0激活时间
        user.setActiveTime(0L);
        user.setActivationTime(0L);
        //账户状态0正常，-1删除，1 禁用
        user.setStatus(0);
        //0正常，1 禁用
        user.setDistributionStatus(0);

        user.setWxaBusinessType(0);
        return user;
    }




    /**
     * 用户登出
     *
     * @param request
     * @date: 2018/5/16 12:09
     * @author: Aison
     */
    public void logout(HttpServletRequest request) {

        String deviceId = request.getHeader("uniqueId");
        String deviceName = request.getHeader("nickname");
        String version = request.getHeader("version");
        String platform = request.getHeader("platform");
        String phone = request.getParameter("phone");
        String cid = request.getHeader("cid");

        deviceId = deviceId == null ? request.getParameter("uniqueId") : deviceId;
        deviceName = deviceName == null ? request.getParameter("nickname") : deviceId;
        cid = cid == null ? request.getParameter("cid") : cid;

        if (BizUtil.hasEmpty(deviceId, phone)) {
            return;
        }
        UserLoginLogNew userLoginLog = new UserLoginLogNew();
        userLoginLog.setAlive(1);
        userLoginLog.setCreateTime(System.currentTimeMillis());
        userLoginLog.setDeviceId(deviceId);
        userLoginLog.setDeviceName(deviceName);
        userLoginLog.setVersion(version);
        userLoginLog.setPlatform(platform);
        userLoginLog.setUserName(phone);
        userLoginLog.setCid(cid);

        logsService.logout(userLoginLog);


    }





    /**
     * 用户登陆历史记录
     *
     * @param request
     * @param loginWay 登陆方式 1 短信登陆 2 微信登陆
     * @date: 2018/5/15 16:36
     * @author: Aison
     */
    private void userLoginLog(HttpServletRequest request, Integer loginWay, String phone) {

        String deviceId = request.getHeader("uniqueId");
        String deviceName = request.getHeader("nickname");
        String version = request.getHeader("version");
        String platform = request.getHeader("platform");
        String cid = request.getHeader("cid");
        String userAge = request.getHeader("X-User-Agent");
        try {
            deviceName = BizUtil.base64Decode(deviceName);
            if (BizUtil.isNotEmpty(userAge)) {
                String[] userAges = userAge.split("\\|");
                String platformStr = userAges[0];
                String versionStr = userAges[1];
                platform = platformStr.split("=")[1];
                version = versionStr.split("=")[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果没有设备id和cid 则不做记录 可能是老的版本
        if (BizUtil.hasEmpty(deviceId, cid)) {
            return;
        }
        UserLoginLogNew userLoginLog = new UserLoginLogNew();
        userLoginLog.setAlive(1);
        userLoginLog.setCreateTime(System.currentTimeMillis());
        userLoginLog.setDeviceId(deviceId);
        userLoginLog.setDeviceName(deviceName);
        userLoginLog.setVersion(version);
        userLoginLog.setPlatform(platform);
        userLoginLog.setUserName(phone);
        userLoginLog.setCid(cid);
        userLoginLog.setLoginWay(loginWay);

        threadPoolTaskExecutor.execute(() -> {
            try {
                // 先添加 不在事务里面
                logsService.addUserLoginLog(userLoginLog);
                logsService.singleDeviceLogin(userLoginLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    /**
     * 云之讯发送短信/语音验证码
     *
     * @param phone
     * @param sendType
     * @return
     */
    public JsonResponse ucpaasSendCode(String phone, String sendType) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        // Boolean sendFlag = true;
        int whiteNum = whitePhoneService.getWhitePhone(phone);
        Boolean sendFlag = true;
        if (whiteNum == 0) {
            sendFlag = ucpaasService.send(phone, sendType, 2);
        }
        data.put("sendFlag", sendFlag);
        return jsonResponse.setSuccessful().setData(data);
    }

    /**
     * 登录验证
     *
     * @param username
     * @param password
     * @param response
     * @param ip
     * @param client
     * @return
     */
    public JsonResponse submitLogin(String username, String password, HttpServletRequest request,
                                    HttpServletResponse response, String ip,
                                    ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,2,5-9])|(177))\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        boolean flag = pattern.matcher(username).matches();// 判断用户是否用已注册手机号登陆

        StoreBusiness storeBusiness = null;
        if (flag) {
            storeBusiness = userService.getStoreBusinessByPhone(username);
        } else {
            storeBusiness = userService.getStoreBusiness4Login(username);
        }


        if (storeBusiness == null) {
            return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USER_NOT_EXISTS);
        } else if (! StringUtils.equals(storeBusiness.getUserPassword(), DigestUtils.md5Hex(password))) {
            return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_INVALID_PASSWORD);
        }

        Map<String, Object> data = loginUser(storeBusiness, request, response, ip, client);
        return jsonResponse.setSuccessful().setData(data);
    }

    public Map<String, Object> loginUser(StoreBusiness storeBusiness, HttpServletRequest request,
                                         HttpServletResponse response, String ip,
                                         ClientPlatform client) {

        Map<String, Object> data = new HashMap<String, Object>();
        if (storeBusiness.getActiveTime() == 0) {
            data.put("activeFlag", "0");

        } else {
            data.put("activeFlag", "1");
        }
        if (storeBusiness.getProtocolTime() == null || storeBusiness.getProtocolTime() == 0) {
            data.put("protocolFlag", "0");

        } else {
            data.put("protocolFlag", "1");
        }

        String cookieValue = LoginUtil.buildLoginCookieValue(storeBusiness.getBusinessNumber() + "", UserType.PHONE);
        response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
        logger.debug("cookie :{}", LoginUtil.buildLoginCookieHeaderValue(cookieValue));

        // spring session
        request.getSession().setAttribute(CookieConstants.COOKIE_NAME_SESSION, cookieValue);

        // 记录用户登录日志
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setBusinessId(storeBusiness.getId());
        if (client != null) {
            userLoginLog.setClientType(client.getPlatform().getValue());
            userLoginLog.setClientVersion(client.getVersion());
        }
        userLoginLog.setIp(ip);
        userLoginLog.setCreateTime(System.currentTimeMillis());
        userService.addUserLoginLog(userLoginLog);
        return data;
    }

    public JsonResponse submitLogin(String username, String password, HttpServletRequest request,
                                    HttpServletResponse response) {

        return submitLogin(username, password, request, response, "", null);
    }

    /**
     * 微信登录2.4版本开始使用
     *
     * @param openId
     * @param openuid
     * @param accessToken
     * @param ip
     * @param client
     * @param response
     * @return
     */
    public JsonResponse weixinLoginV24(String openId, String openuid, String accessToken, String ip,
                                       ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {

        // 1、获取用户信息
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        jsonResponse.setData(data);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(V2Constants.ACCESS_TOKEN, accessToken);
        map.put("openid", openId);
        IRawDataCredentials tokenCredentials = new RawDataCredentials(map);
        ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(tokenCredentials, ip);
        if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
            logger.error("fail to get user, snsResponse:{}", snsResponse);
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }

        ISnsEndUser endUser = snsResponse.getData();
        if (! StringUtils.equals(openuid, endUser.getPlatformIndependentId())) {
            logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid,
                    endUser.getPlatformIndependentId());
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }

        // 2.获取应用用户信息
        StoreBusiness storeBusiness = storeUserService.getStoreBusinessByWeixinId(endUser.getPlatformIndependentId());
        String weiXinNickName = endUser.getNickName();
        if (storeBusiness == null) {
            data.put("needRegFlag", "YES");
            data.put("needBindPhone", "YES");
            data.put("needFillData", "YES");
        } else if (storeBusiness.getPhoneNumber() == null || storeBusiness.getPhoneNumber().length() != 11) {
            data.put("needBindPhone", "YES");
        } else if (storeAuditService.getAuditCount(storeBusiness.getId(),
                // 已通过
                StoreAuditStatusEnum.pass.getIntValue()) > 0) {
            String userNickName = storeBusiness.getBindWeixinName();
            long id = storeBusiness.getId();
            // 同步昵称@@@

            storeUserService.synNickName(id, userNickName, weiXinNickName);
            data = loginUser(storeBusiness, request, response, ip, client);
            data.put("needFillData", "NO");
            data.put("auditStatus", "1");

        } else if (storeAuditService.chectStoreBusiness(storeBusiness)) {
            data.put("needFillData", "YES");
            data.put("phoneNumber", storeBusiness.getPhoneNumber());
        } else if (storeAuditService.getAuditCount(storeBusiness.getId(),
                // 已提交审核
                StoreAuditStatusEnum.submit.getIntValue()) > 0) {
            data = loginUser(storeBusiness, request, response, ip, client);
            data.put("needFillData", "NO");
            data.put("auditStatus", "0");
            // 2.4版本临时添加，之后版本不用，可删除可保留
            data.put("phoneNumber", storeBusiness.getPhoneNumber());
        } else if (storeAuditService.getAuditCount(storeBusiness.getId(),
                // 审核未通过
                StoreAuditStatusEnum.fail.getIntValue()) > 0) {
            data = loginUser(storeBusiness, request, response, ip, client);
            data.put("needFillData", "YES");
            data.put("result", "");
            data.put("auditStatus", "-1");
            data.put("phoneNumber", storeBusiness.getPhoneNumber());
        } else if (storeAuditService.getAuditCount(storeBusiness.getId(),
                // 已禁用
                StoreAuditStatusEnum.forbidden.getIntValue()) > 0) {
            return jsonResponse.setResultCode(ResultCode.STOREBUSINESS_FORBIDDEN).setError("该账号已禁用");
        } else {
            data.put("login", "NO");
            data.put("needFillData", "YES");
        }
        String nextStep = "index";
        if (data.get("needBindPhone") != null && data.get("needBindPhone").equals("YES")) {
            nextStep = "bindPhone";
        } else if (data.get("needFillData") != null && data.get("needFillData").equals("YES")) {
            nextStep = "fillData";
        }
        data.put("nextStep", nextStep);
        //查看该用户是否第一次登录
        if (storeBusiness == null) {
            data.put("firstLogin", false);
        } else {
            boolean firstLogin = storeBusiness.getFirstLoginStatus() == FIRST_LOGIN ? true : false;
            data.put("firstLogin", firstLogin);
        }

        if (storeBusiness != null) {
                userLoginLog(request, 2, storeBusiness.getPhoneNumber());
        }
        data.put("tip", "俞姐姐门店宝批发精品女装，价格仅对“门店采购负责人”开放，请理解...");
        return jsonResponse.setSuccessful().setData(data);

    }


    /**
     * 微信登录3.7.2版本开始使用
     *
     * @param: openId
     * @param: openuid
     * @param: accessToken
     * @param: ip
     * @param: client
     * @param: request
     * @param: response
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/17 13:11
     */
    @Transactional( rollbackFor = Exception.class )
    public JsonResponse wxLoginV372(String openId, String openuid, String accessToken, String ip,
                                    ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {

        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> result = new HashMap<>(4);
        result.put("isFirstLogin", true);

        //用户微信信息
        Map<String, Object> wxRequestParams = new HashMap<>(2);
        wxRequestParams.put(V2Constants.ACCESS_TOKEN, accessToken);
        wxRequestParams.put("openid", openId);
        ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(new RawDataCredentials(wxRequestParams), ip);
        if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
            logger.error("fail to get user, snsResponse:{}", snsResponse);
            result.put("needBindPhone", true);
            result.put("phone", null);
            result.put("msg", "微信认证失败");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setData(result);
        }

        ISnsEndUser wxUser = snsResponse.getData();
        if (! StringUtils.equals(openuid, wxUser.getPlatformIndependentId())) {
            logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid,
                    wxUser.getPlatformIndependentId());
            result.put("needBindPhone", true);
            result.put("phone", null);
            result.put("msg", "微信认证失败");
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setData(result);
        }

        //平台用户信息
        List<StoreBusiness> storeList = storeBusinessNewService.getStoreBusinessByWeixinId(wxUser.getPlatformIndependentId());
        if (storeList == null || storeList.size() == 0 || StringUtils.isBlank(storeList.get(0).getPhoneNumber())) {
            //没有用户信息, 请绑定手机号
            result.put("needBindPhone", true);
            result.put("phone", null);
            result.put("msg", "请绑定手机号");
            return jsonResponse.setSuccessful().setData(result);
        }
        StoreBusiness store = storeList.get(0);

        //新版本用户是否需要更新firstLogin
        boolean needUpdFirstLogin = !isAppVersionBeforeV372(store) && store.getFirstLoginStatus() == FIRST_LOGIN;
        if (needUpdFirstLogin) {
            store.setFirstLoginStatus(NO_FIRST_LOGIN);
        }
        //用户需要更新微信信息
        boolean needUpdWeiXinInfo = WeiXinInfoIsChange(wxUser, store);
        if (needUpdWeiXinInfo || needUpdFirstLogin) {
            storeBusinessNewService.update(store);
        }

        //兼容3.7.2版本以前的用户状态
        compatibleHistoryBeforeV372(store);

        //登录
        Map<String, Object> data = loginUser(store, request, response, ip, client);
        data.put("isFirstLogin", false);
        data.put("tip", "俞姐姐门店宝批发精品女装，价格仅对“门店采购负责人”开放，请理解...");
        data.put("needBindPhone", false);
        data.put("phone", store.getPhoneNumber());
        result.put("msg", "success");


        //多登录校验
        userLoginLogSwitcherV372(request, 2, store.getPhoneNumber());

        return jsonResponse.setSuccessful().setData(data);
    }


    public JsonResponse phoneBindWeixinCommit(String openId, String openuid, String accessToken, String phone,
                                              String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {
        // 1、获取用户信息
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        jsonResponse.setData(data);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(V2Constants.ACCESS_TOKEN, accessToken);
        map.put("openid", openId);
        IRawDataCredentials tokenCredentials = new RawDataCredentials(map);
        ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(tokenCredentials, ip);
        if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
            logger.error("fail to get user, snsResponse:{}", snsResponse);
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }

        ISnsEndUser endUser = snsResponse.getData();
        if (! StringUtils.equals(openuid, endUser.getPlatformIndependentId())) {
            logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid,
                    endUser.getPlatformIndependentId());
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }

        // 2.获取应用用户信息
        StoreBusiness storeBusiness = storeUserService.getStoreBusinessByWeixinId(endUser.getPlatformIndependentId());
        StoreBusiness storeBusinessByPhone = storeUserService.getStoreBusinessByPhone(phone);
        String weiXinNickName = endUser.getNickName();
        String nextStep = "fillData";

        if (storeBusiness != null) {

            logger.info("Weixin 已绑定其它手机，无法重复绑定");
            return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED_LOGIN);
        } else if (storeBusinessByPhone != null) {
            if (storeBusinessByPhone.getBindWeixinId() != null
                    && storeBusinessByPhone.getBindWeixinId().trim().length() > 0) {
                logger.info("手机号已绑定其它微信，无法重复绑定");
                return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
            }
            int count = storeUserService.oldUserBindWeixin(storeBusinessByPhone.getId(),
                    endUser.getPlatformIndependentId(), endUser.getNickName(), endUser.getAvatar());
            if (count == 1) {
                storeBusiness = storeUserService.getStoreBusinessByWeixinId(endUser.getPlatformIndependentId());
                loginUser(storeBusiness, request, response, ip, client);
                nextStep = "index";
                data.put("needFillData", "NO");
            }
            // logger.info("手机号已绑定其它微信，无法重复绑定");
            // return
            // jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
        } else {
            storeUserService.addUserWeixinAndPhone(phone, UserType.PHONE, client, endUser.getPlatformIndependentId(),
                    weiXinNickName, endUser.getAvatar());
            storeBusiness = storeUserService.getStoreBusinessByWeixinId(endUser.getPlatformIndependentId());
            loginUser(storeBusiness, request, response, ip, client);
            data.put("needFillData", "YES");
        }
//        //获取供应商信息
//        long supplierId = storeBusiness.getSupplierId();
//        //关联供应商
//        if(supplierId == 0){
//        	Wrapper<UserNew> wrapper = new EntityWrapper<UserNew>();
//        	wrapper.eq("phone", storeBusiness.getPhoneNumber());
//        	List<UserNew> list = userNewMapper.selectList(wrapper);
//        	if(list != null && list.size() > 0){
//        		try {
//        			storeBusinessNewService.updateSupplierIdById(storeBusiness.getId(),list.get(0).getId());
//				} catch (Exception e) {
//					logger.info(e.getMessage());
//				}
//        	}
//        }


        data.put("nextStep", nextStep);

        // 2.4版本临时添加，之后版本不用，可删除可保留
        data.put("phoneNumber", storeBusiness.getPhoneNumber());

        return jsonResponse.setSuccessful().setData(data);
    }


    /**
     * 微信绑定手机号
     *
     * @param: phoneVerifyCode 手机验证码
     * @param: sendType 手机验证方式
     * @param: openId
     * @param: openuid
     * @param: accessToken
     * @param: phone 手机号
     * @param: ip
     * @param: client
     * @param: request
     * @param: response
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/18 9:11
     */
    public JsonResponse bindWeixinOauthV372(String phoneVerifyCode, String sendType, String openId, String openuid, String accessToken, String phone,
                                            String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {
        JsonResponse jsonResponse = new JsonResponse();
        HashMap<String, Object> result = new HashMap<>(4);
        result.put("isFirstLogin", true);

        // 手机验证
        if (whitePhoneService.getWhitePhone(phone) == 0) {
            // 如果手机号不在白名单
            boolean phoneVerifySuccess = phoneVerify(phoneVerifyCode, sendType, phone);
            if (! phoneVerifySuccess) {
                result.put("needBindPhone", true);
                result.put("phone", phone);
                return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID).setData(result);
            }
        }


        // 获取微信信息
        Map<String, Object> wxRequestParams = new HashMap<>();
        wxRequestParams.put(V2Constants.ACCESS_TOKEN, accessToken);
        wxRequestParams.put("openid", openId);
        ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(new RawDataCredentials(wxRequestParams), ip);
        if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
            logger.error("fail to get user, snsResponse:{}", snsResponse);
            result.put("needBindPhone", true);
            result.put("phone", phone);
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }

        ISnsEndUser wxUser = snsResponse.getData();
        if (! StringUtils.equals(openuid, wxUser.getPlatformIndependentId())) {
            logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid,
                    wxUser.getPlatformIndependentId());
            result.put("needBindPhone", true);
            result.put("phone", phone);
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }


        // 2.获取应用用户信息
        List<StoreBusiness> wxStoreList = storeBusinessNewService.getStoreBusinessByWeixinId(wxUser.getPlatformIndependentId());
        StoreBusiness phoneStore = storeBusinessNewService.getStoreBusinessByPhone(phone);


        /*
         * 该微信号已经绑定
         * 该手机号已经绑定
         * 用户登录过, 手机号没绑定微信
         * 用户没有登录过, 手机没绑定微信
         */
        if (wxStoreList != null && wxStoreList.size() > 0) {
            /* 该微信号已经绑定*/
            result.put("needBindPhone", true);
            result.put("phone", phone);
            return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED_LOGIN).setSuccessful();
        } else if (phoneStore != null && StringUtils.isNotBlank(phoneStore.getBindWeixinId())) {
            /* 该手机号已经绑定*/
            result.put("needBindPhone", true);
            result.put("phone", phone);
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
        } else if (phoneStore != null) {

            /* 已登录过 */
            //3.7.2版本用户是否需要更新firstLogin
            boolean needUpdFirstLogin = !isAppVersionBeforeV372(phoneStore) && phoneStore.getFirstLoginStatus() == FIRST_LOGIN;
            if (needUpdFirstLogin) {
                phoneStore.setFirstLoginStatus(NO_FIRST_LOGIN);
            }
            //用户需要更新微信信息
            boolean needUpdWeiXinInfo = WeiXinInfoIsChange(wxUser, phoneStore);

            if (needUpdWeiXinInfo || needUpdFirstLogin) {
                storeBusinessNewService.update(phoneStore);
            }

            //兼容3.7.2版本以前的用户状态
            compatibleHistoryBeforeV372(phoneStore);

            //登录
            result.put("isFirstLogin", false);
            loginUser(phoneStore, request, response, ip, client);
            //登陆多设备校验
            userLoginLogSwitcherV372(request, 2, phone);
        } else {
            /* 用户没有登录过, 手机没绑定微信 */
            phoneStore = buildDefaultStore();
            phoneStore.setFirstLoginStatus(FIRST_LOGIN);
            phoneStore.setPhoneNumber(phone);
            WeiXinInfoIsChange(wxUser, phoneStore);
            phoneStore.setUserName(phoneStore.getBindWeixinId());
            storeBusinessNewService.add(phoneStore);
            phoneStore.setBusinessNumber(phoneStore.getId().longValue() + 800000000);
            storeBusinessNewService.update(phoneStore);
            //发优惠券
            result.put("coupon",true);
            result.put("storeId",phoneStore.getId());

            //sendFirstLoginCoupon(phoneStore);
            //登录
            result.put("isFirstLogin", true);
            loginUser(phoneStore, request, response, ip, client);
        }


        result.put("needBindPhone", false);
        result.put("phone", phone);
        return jsonResponse.setSuccessful().setData(result);
    }


    /**
     * 首次登录发送优惠券
     *
     * @param: phoneStore
     * @return: void
     * @auther: Charlie(唐静)
     * @date: 2018/5/23 14:12
     */
    @Transactional( rollbackFor = Exception.class )
    public void sendFirstLoginCoupon(StoreBusiness user) {
        // 给新注册并且第一次通过审核的用户发送优惠券和系统通知和推送
        int storeCouponCount = sendNewStoreBusinessStoreCouponAndNotificationAndGetui(user);
        if (storeCouponCount > 0) {
            sendNotificationAndGetui(user.getUserCID(), "欢迎您来到俞姐姐门店宝", "恭喜您获得 " + storeCouponCount + " 张优惠券！", "", "",
                    "5", System.currentTimeMillis() + "", "," + user.getId());
        }
    }


    /**
     * 给新注册并且第一次通过审核的用户发送优惠券
     */
    private int sendNewStoreBusinessStoreCouponAndNotificationAndGetui(StoreBusiness storeBusiness) {
        int count = 0;
        String setting = globalSettingService.getSetting(GlobalSettingName.STORE_COUPON_SEND_1_SETTING);
        logger.info("给新注册并且第一次通过审核的用户发送优惠券，setting：" + setting);
        if (! StringUtils.isBlank(setting)) {
            JSONObject jsonObject = JSONObject.parseObject(setting);
            logger.info("给新注册并且第一次通过审核的用户发送优惠券，jsonObject：" + jsonObject);
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("setting");
                logger.info("给新注册并且第一次通过审核的用户发送优惠券，jsonArray：" + jsonArray);
                if (jsonArray != null && jsonArray.size() > 0) {
                    for (Object object2 : jsonArray) {
                        JSONObject jsonObject2 = JSONObject.parseObject((String) object2.toString());
                        logger.info("给新注册并且第一次通过审核的用户发送优惠券，jsonObject2：" + jsonObject2);
                        if (jsonObject2 != null) {
                            int couponCount = Integer.parseInt(jsonObject2.get("coupon_count").toString());
                            String jsonObject2String = jsonObject2.get("coupon_template_id").toString();
                            logger.info("给新注册并且第一次通过审核的用户发送优惠券，jsonObject2String：" + jsonObject2String);
                            if (! StringUtils.isEmpty(jsonObject2String)) {
                                long storeCouponTemplateId = Long.parseLong(jsonObject2String);
                                // 给新注册并且第一次通过审核的用户发送优惠券
                                boolean flag = shopCouponService.batchStoreCouponToNewStoreAudit(couponCount,
                                        storeCouponTemplateId, storeBusiness);
                                if (flag) {
                                    count = count + couponCount;
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    @Autowired
    private com.store.service.NotificationService notifacationService;
    @Autowired
    private ShopCouponService shopCouponService;

    // 发送系统通知和推送
    private void sendNotificationAndGetui(String cid, String title, String abstracts, String linkUrl, String image,
                                          String type, String pushTime, String storeIdArrays) {
        try {

            // UserCID是用户第一次登陆获取的，所以暂时获取不到，无法推送
            // boolean ret = GetuiUtil.pushGeTui(cid, title, abstracts, linkUrl,
            // image, type, pushTime);
            if (StringUtils.isNotBlank(cid)) {
                 GetuiUtil.pushGeTui(cid, title, abstracts, linkUrl, image, type, pushTime);
            }

            /* 记录通知信息 */
            ShopNotification notification = new ShopNotification();
            notification.setTitle(title);
            notification.setAbstracts(abstracts);
            notification.setPushStatus(1);
            notification.setImage(image);
            notification.setType(Integer.parseInt(type));
            notification.setLinkUrl(linkUrl);
            notification.setStoreIdArrays(storeIdArrays);
            long createTime = System.currentTimeMillis();
            notification.setCreateTime(createTime);
            notification.setUpdateTime(createTime);
            notification.setPushTime(createTime);
            notifacationService.addNotification(notification);
            /* 推送 */
//            GetuiUtil.pushGeTui(title, abstracts, linkUrl, image, type , pushTime);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送新注册用户优惠券通知和推送时发生异常:" + e.getMessage());
        }
    }


    /**
     * 如果用户的微信信息改变了, 返回true
     * 并同时更新实体微信字段的信息
     *
     * @param: wxUser
     * @param: phoneStore
     * @return: void
     * @auther: Charlie(唐静)
     * @date: 2018/5/18 11:39
     */
    private boolean WeiXinInfoIsChange(ISnsEndUser wxUser, StoreBusiness phoneStore) {
        boolean noChange = (ObjectUtils.nullSafeEquals(phoneStore.getBindWeixinId(), wxUser.getPlatformIndependentId()) &&
                ObjectUtils.nullSafeEquals(phoneStore.getBindWeixinName(), wxUser.getNickName()) &&
                ObjectUtils.nullSafeEquals(phoneStore.getBindWeixinIcon(), wxUser.getAvatar())
        );

        if (! noChange) {
            phoneStore.setBindWeixinName(wxUser.getNickName());
            phoneStore.setBindWeixinId(wxUser.getPlatformIndependentId());
            phoneStore.setBindWeixinIcon(wxUser.getAvatar());
            phoneStore.setUpdateTime(System.currentTimeMillis());
        }
        return ! noChange;
    }


    /**
     * 手机验证码验证
     *
     * @param: phoneVerifyCode 手机验证码
     * @param: sendType 验证方式
     * @param: phone 手机号
     * @return: boolean 手机验证是否成功
     * @auther: Charlie(唐静)
     * @date: 2018/5/18 9:07
     */
    private boolean phoneVerify(String phoneVerifyCode, String sendType, String phone) {
        boolean smsSuccess = (sendType.equals("sms") && yunXinSmsService.verifyCode(phone, phoneVerifyCode));
        boolean voiceSuccess = (sendType.equals("voice") && ucpaasService.verifyCode(phone, phoneVerifyCode));
        return smsSuccess || voiceSuccess;
    }


    /**
     * 判断是否为空或者空字符串
     *
     * @param str
     * @return
     */
    public boolean checkNull(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    // =====================================================================================================================================================================

    /**
     * 手机号码验证码登陆2.3版本和之前版本
     *
     * @param phone
     * @param verifyCode
     * @param response
     * @param ip
     * @param client
     * @return
     */
    public JsonResponse verifyCommit(String phone, String verifyCode, String sendType, HttpServletRequest request,
                                     HttpServletResponse response,
                                     String ip, ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        if (whitePhoneService.getWhitePhone(phone) == 0) {// 如果手机号不在白名单
            // if (verifyCode == null || !yunXinSmsService.verifyCode(phone,
            // verifyCode)) {
            // return
            // jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
            // }
            if (sendType.equals("sms") && ! yunXinSmsService.verifyCode(phone, verifyCode)) {
                return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
            } else if (sendType.equals("voice") && ! ucpaasService.verifyCode(phone, verifyCode)) {
                return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
            }
        }
        StoreBusiness storeBusiness = userService.getStoreBusinessByPhone(phone);

        String nextStep = "index";
        if (storeBusiness == null) {
            // long storeId = storeUserService.addPhoneUser(phone);
            data.put("needBindWeixin", "YES");
            data.put("needFillData", "YES");
            data.put("waitAudit", "YES"); // 暂时不启用
            // 创建手机用户？
        } else {

            // int auditFailNum =
            // storeAuditService.getAuditCount(storeBusiness.getId(),
            // StoreAuditStatusEnum.fail.getIntValue());
            if (storeBusiness.getBindWeixinId() != null && storeBusiness.getBindWeixinId().trim().length() > 0) {
                data.put("needBindWeixin", "NO");
                int auditPassNum = storeAuditService.getAuditCount(storeBusiness.getId(),
                        StoreAuditStatusEnum.pass.getIntValue());
                if (auditPassNum > 0) {
                    data.put("waitAudit", "NO");
                    data.put("needFillData", "NO");
                } else {

                    if (storeBusiness.getActiveTime() > 0) {
                        data.put("needFillData", "NO");
                    } else {
                        int auditWaitNum = storeAuditService.getAuditCount(storeBusiness.getId(),
                                StoreAuditStatusEnum.submit.getIntValue());
                        if (auditWaitNum > 0) {
                            data.put("needFillData", "NO");
                        } else {
                            data.put("needFillData", "YES");
                        }

                    }
                }
            } else {
                data.put("needBindWeixin", "YES");
                data.put("needFillData", "YES");
                data.put("waitAudit", "YES"); // 暂时不启用
            }
            loginUser(storeBusiness, request, response, ip, client);
        }

        if (data.get("needBindWeixin") != null && data.get("needBindWeixin").equals("YES")) {
            nextStep = "bindWeixin";
        } else if (data.get("needFillData") != null && data.get("needFillData").equals("YES")) {
            nextStep = "fillData";
        }
        data.put("nextStep", nextStep);

        // loginUser(storeBusiness, response, ip, client);
        return jsonResponse.setSuccessful().setData(data);
    }

    /**
     * 微信登录2.3版本和之前版本
     *
     * @param openId
     * @param openuid
     * @param accessToken
     * @param ip
     * @param client
     * @param response
     * @return
     */
    public JsonResponse weixinLogin(String openId, String openuid, String accessToken, String ip, ClientPlatform client,
                                    HttpServletRequest request, HttpServletResponse response) {

        // 1、获取用户信息
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        jsonResponse.setData(data);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(V2Constants.ACCESS_TOKEN, accessToken);
        map.put("openid", openId);
        IRawDataCredentials tokenCredentials = new RawDataCredentials(map);
        ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(tokenCredentials, ip);
        if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
            logger.error("fail to get user, snsResponse:{}", snsResponse);
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }

        ISnsEndUser endUser = snsResponse.getData();
        if (! StringUtils.equals(openuid, endUser.getPlatformIndependentId())) {
            logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid,
                    endUser.getPlatformIndependentId());
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }

        // 2.获取应用用户信息
        StoreBusiness storeBusiness = storeUserService.getStoreBusinessByWeixinId(endUser.getPlatformIndependentId());
        String weiXinNickName = endUser.getNickName();

        if (storeBusiness != null) {

            String userNickName = storeBusiness.getBindWeixinName();
            long id = storeBusiness.getId();
            // 同步昵称@@@

            storeUserService.synNickName(id, userNickName, weiXinNickName);

            // //添加用户信息到Header中
            // loginDelegator.addSetCookie(response, user);
            // //记录用户登录日志
            // loginDelegator.addUserLoginLog(ip, client, user);
            data = loginUser(storeBusiness, request, response, ip, client);

            if (storeBusiness.getPhoneNumber() == null || storeBusiness.getPhoneNumber().length() != 11) {
                data.put("needBindPhone", "YES");
            } else {
                data.put("needBindPhone", "NO");

                // int auditPassNum =
                // storeAuditService.getAuditCount(storeBusiness.getId(),
                // StoreAuditStatusEnum.pass.getIntValue());
                // if(auditPassNum > 0){
                // data.put("waitAudit", "NO");
                // }else{
                // int auditWaitNum =
                // storeAuditService.getAuditCount(storeBusiness.getId(),
                // StoreAuditStatusEnum.submit.getIntValue());
                // if(auditWaitNum > 0){
                // data.put("waitAudit", "YES");
                // }else{
                // data.put("needFillData", "YES");
                // }
                // }
                if (storeBusiness.getActiveTime() > 0) {
                    data.put("needFillData", "NO");
                } else {
                    int auditWaitNum = storeAuditService.getAuditCount(storeBusiness.getId(),
                            StoreAuditStatusEnum.submit.getIntValue());
                    if (auditWaitNum > 0) {
                        data.put("needFillData", "NO");
                    } else {
                        data.put("needFillData", "YES");
                    }

                }
                // if(storeBusiness.getLegalPerson() != null &&
                // storeBusiness.getLegalPerson().trim().length() > 1){
                // data.put("needFillData", "NO");
                // }else{
                // data.put("needFillData", "YES");
                // }
            }
            data.put("needRegFlag", "NO");
            loginUser(storeBusiness, request, response, ip, client);
        } else {
            // return
            // jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED);
            data.put("needRegFlag", "YES");
            data.put("needBindPhone", "YES");
            data.put("needFillData", "YES");

        }
        String nextStep = "index";
        if (data.get("needBindPhone") != null && data.get("needBindPhone").equals("YES")) {
            nextStep = "bindPhone";
        } else if (data.get("needFillData") != null && data.get("needFillData").equals("YES")) {
            nextStep = "fillData";
        }
        data.put("nextStep", nextStep);
        return jsonResponse.setSuccessful().setData(data);

    }

    /**
     * 2.4版本根据手机号获取门店资料
     *
     * @param phone
     * @return
     */
    public JsonResponse getStoreBusinessInfoV24(String phone, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        StoreBusiness storeBusiness = userService.getStoreBusinessByPhone(phone);

        if (storeBusiness == null) {
            return jsonResponse.setResultCode(ResultCode.STOREBUSINESS_INFO_IS_NULL);
        }
        data.put("businessName", storeBusiness.getBusinessName());
        data.put("legalPerson", storeBusiness.getLegalPerson());
        if (clientPlatform.isIphone() && ClientUtil.compareTo(clientPlatform.getVersion(), "2.4.0") == 0) {
            data.put("province", "");
            data.put("city", "");
            data.put("county", "");
        } else {
            data.put("province", storeBusiness.getProvince());
            data.put("city", storeBusiness.getCity());
            data.put("county", storeBusiness.getCounty());
        }
        data.put("businessAddress", storeBusiness.getBusinessAddress());
        data.put("referenceNumber", storeBusiness.getGroundUserPhone());
        String qualificationProofImages = storeBusiness.getQualificationProofImages();
        List<String> imagesList = new ArrayList<String>();
        if (! StringUtils.isEmpty(qualificationProofImages)) {
            String[] images = qualificationProofImages.split(",");
            for (String image : images) {
                imagesList.add(image);
            }
        }
//		List<Map<String,Object>> styleList =new ArrayList<>();

        JSONArray styleJson = globalSettingService.getJsonArray(GlobalSettingName.STORE_STYLE);
        JSONArray areaJson = globalSettingService.getJsonArray(GlobalSettingName.STORE_AREA_SCOPE);
        JSONArray ageJson = globalSettingService.getJsonArray(GlobalSettingName.STORE_AGE);
//		for(int i = 0 ;i<jsonArray.size();i++){
//    		JSONObject json = jsonArray.getJSONObject(i);
//    		Map<String,Object> styleMap = new HashMap<>();
//    		styleMap.put("styleId", json.getLong("styleId"));
//    		styleMap.put("styleName", json.getString("styleName"));
//    		styleList.add(styleMap);
//    	}
        data.put("choosedStoreStyle", storeBusiness.getStoreStyle());
        data.put("choosedStoreAge", storeBusiness.getStoreAge());
        data.put("choosedStoreAreaScope", storeBusiness.getStoreAreaScope());


        data.put("storeStyleTip", "请选择女装风格。");
        data.put("storeAreaTip", "请选择店铺面积。");
        data.put("storeAgeTip", "请选择店铺年龄定位。");

        List<DataDictionary> dataDictionarys = dataDictionaryService.getDictionaryGroups(new String[]{"priceLevel", "purchaseChannel"});

        List<Map<String, Object>> priceLevels = new ArrayList<>();
        List<Map<String, Object>> purchaseCls = new ArrayList<>();

        for (DataDictionary dd : dataDictionarys) {
            Map<String, Object> map = new HashMap<>();
            String code = dd.getCode();
            String group = dd.getGroupCode();
            map.put("id", code);
            map.put("name", dd.getVal());
            if ("priceLevel".equals(group)) {
                priceLevels.add(map);
            } else if ("purchaseChannel".equals(group)) {
                purchaseCls.add(map);
            }
        }

        data.put("purchaseChannelSelected", storeBusiness.getPurchaseChannel());
        data.put("priceLevelSelected", storeBusiness.getPriceLevel());

        data.put("purchaseChannels", purchaseCls);
        data.put("priceLevel", priceLevels);

        data.put("storeStyleList", styleJson);
        data.put("storeAreaScopeList", areaJson);
        data.put("storeAgeList", ageJson);
        data.put("qualificationProofImages", imagesList);
        return jsonResponse.setSuccessful().setData(data);
    }


    /**
     * 更新用户微信信息
     *
     * @param: openId
     * @param: openuid
     * @param: accessToken
     * @param: storeId
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/18 11:59
     */
    public JsonResponse updateWeiXinInfo(String openId, String openuid, String accessToken, Long storeId, String ip) {
        JsonResponse jsonResponse = new JsonResponse();

        // 获取微信信息
        Map<String, Object> wxRequestParams = new HashMap<>();
        wxRequestParams.put(V2Constants.ACCESS_TOKEN, accessToken);
        wxRequestParams.put("openid", openId);
        ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(new RawDataCredentials(wxRequestParams), ip);
        if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
            logger.error("fail to get user, snsResponse:{}", snsResponse);
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }

        ISnsEndUser wxUser = snsResponse.getData();
        if (! StringUtils.equals(openuid, wxUser.getPlatformIndependentId())) {
            logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid,
                    wxUser.getPlatformIndependentId());
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }


        StoreBusiness store = storeBusinessNewService.getById(storeId);
        if (store == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }
        //同步微信资料
        if (WeiXinInfoIsChange(wxUser, store)) {
            storeBusinessNewService.update(store);
        }

        return jsonResponse.setSuccessful();
    }


}
