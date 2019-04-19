package com.jiuy.store.api.tool.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiuy.rb.enums.CouponSendEnum;
import com.jiuy.rb.enums.CouponStateEnum;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.util.CouponAcceptVo;
import com.jiuy.rb.util.CouponVo;
import com.jiuyuan.util.BizUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2API;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.StoreLoginDelegator;
import com.store.service.StoreUserService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping( "/store/login" )
public class StoreLoginController{
    private static final Log logger = LogFactory.get();

    private static final int NO_FIRST_LOGIN = 2;

    @Autowired
    private StoreLoginDelegator loginDelegator;

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private StoreLoginDelegator storeLoginDelegator;

    @Autowired
    private ShopGlobalSettingService globalSettingService;

    @Autowired
    private IStoreBusinessNewService storeBusinessNewService;

    @Resource(name = "couponServerNew")
    private ICouponServerNew couponServerNew;

    @Autowired
    @Qualifier( "weiXinV2API4App" )
    private WeiXinV2API weiXinV2API;

    @RequestMapping( value = "/submit" ) // , method = RequestMethod.POST
    @ResponseBody
    public JsonResponse submitLogin(@RequestParam( "username" ) String username,
                                    @RequestParam( "password" ) String password, HttpServletRequest request, HttpServletResponse response,
                                    @ClientIp String ip, ClientPlatform client) {
        try {
            return loginDelegator.mobileSubmitLogin(username, password, request, response, ip, client);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 手机号码验证码登陆2.4版本开始使用
     *
     * @param phone
     * @param verifyCode
     * @param sendType
     * @param response
     * @param ip
     * @param client
     * @return
     */
    @RequestMapping( value = "/verifyCommitV24" ) // , method = RequestMethod.POST
    @ResponseBody
    public JsonResponse verifyCommitV24(@RequestParam( "phone" ) String phone,
                                        @RequestParam( "verify_code" ) String verifyCode, @RequestParam( "send_type" ) String sendType,
                                        HttpServletRequest request, HttpServletResponse response, @ClientIp String ip, ClientPlatform client) {
        try {
            return loginDelegator.verifyCommitV24(phone, verifyCode, sendType, request, response, ip, client);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 验证码登录APP version 3.7.2
     *
     * @param: phone
     * @param: verifyCode 验证码
     * @param: sendType 验证码类型
     * @param: request
     * @param: response
     * @param: ip
     * @param: client
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/17 11:18
     */
    @RequestMapping( "/verifyCommitV372" )
    @ResponseBody
    public JsonResponse verifyCommitV372(@RequestParam( "phone" ) String phone,
                                         @RequestParam( "verify_code" ) String verifyCode, @RequestParam( "send_type" ) String sendType,
                                         HttpServletRequest request, HttpServletResponse response, @ClientIp String ip, ClientPlatform client) {
        try {
            JsonResponse js = loginDelegator.verifyLoginV372(phone, verifyCode, sendType, request, response, ip, client);
            grant(js);
            return js;
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse().setError(e.getMessage());
        }
    }

    /**
     * 发放优惠券 供新接口调用
     * @param storeId
     * @return
     */
    @RequestMapping( "/send/coupon" )
    @ResponseBody
    public JsonResponse sendCoupon(Long storeId) {
        CouponAcceptVo  couponVo = new CouponAcceptVo(null,storeId,null,CouponSysEnum.APP,CouponSendEnum.REGISTER,CouponStateEnum.NOT_USE);
        couponServerNew.grant(couponVo);
        System.out.println("发放优惠券"+couponVo);
        return JsonResponse.successful(couponVo);
    }

    /**
     * 发放注册优惠券新版
     *
     * @param js
     * @author Aison
     * @date 2018/8/3 17:22
     * @return void
     */
    private void grant(JsonResponse js) {
        Map<String,Object> map = (Map<String, Object>) js.getData();
        if(map==null ||map.get("coupon") == null){
            return;
        }
        Boolean coupon = (Boolean)map.get("coupon");
        // 需要发放优惠券
        if(coupon!=null && coupon) {
            try{
                Long storeId = (Long) map.get("storeId");
                CouponAcceptVo  couponVo = new CouponAcceptVo(null,storeId,null,CouponSysEnum.APP,CouponSendEnum.REGISTER,CouponStateEnum.NOT_USE);
                couponServerNew.grant(couponVo);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 用户登出
     *
     * @param request
     * @date: 2018/5/16 12:20
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping( "deviceLogout" )
    public JsonResponse logout(HttpServletRequest request) {
        try {
            loginDelegator.logout(request);
            return new JsonResponse().setSuccessful();
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }

    }

    /**
     * 首次登录时获取优惠券图
     */
    @RequestMapping( "/getFirstLoginPicture/auth" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse getFirstLoginPicture(UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        long storeId = userDetail.getId();
        if (storeId == 0) {
            logger.error("商家ID不能为空，请排查！");
            return jsonResponse.setError("商家ID不能为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        JSONArray new_user_coupon = globalSettingService.getJsonArray(GlobalSettingName.NEW_USER_COUPON);
        boolean flag = ((JSONObject) new_user_coupon.get(0)).getBoolean("switch");
        String image = ((JSONObject) new_user_coupon.get(0)).getString("couponImage");
        if (flag) {
            map.put("couponImage", image);
        } else {
            map.put("couponImage", null);
        }
        //更改新用户首次登录的状态
        storeBusinessNewService.updateFirstLoginStatus(storeId, NO_FIRST_LOGIN);
        return jsonResponse.setSuccessful().setData(map);
    }

    /**
     * 2.4版本根据手机号获取门店资料
     *
     * @param phone
     * @return
     */
    @RequestMapping( value = "/getStoreBusinessInfoV24" ) // , method =
    // RequestMethod.POST
    @ResponseBody
    public JsonResponse getStoreBusinessInfoV24(@RequestParam( "phone" ) String phone, ClientPlatform clientPlatform) {
        try {
            return loginDelegator.getStoreBusinessInfoV24(phone, clientPlatform);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }

    @RequestMapping( value = "/userDetail/auth" ) // , method = RequestMethod.POST
    @ResponseBody
    @Login
    public void submitLogin(UserDetail<StoreBusiness> userDetail) {
        StoreBusiness storeBusiness = userDetail.getUserDetail();
        if (storeBusiness != null) {
            System.out.println(storeBusiness.toString());
        } else {
            System.out.println("userDetail is null @@@@@@@@@@@");
        }
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
    @RequestMapping( value = "/ext/oauthV24" )
    @ResponseBody
    public JsonResponse externalOAuthLoginV24(@RequestParam( "openid" ) String openId,
                                              @RequestParam( "openuid" ) String openuid, @RequestParam( "access_token" ) String accessToken,
                                              @ClientIp String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {

        try {
            return storeLoginDelegator.weixinLoginV24(openId, openuid, accessToken, ip, client, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
        // //1、获取用户信息
        // JsonResponse jsonResponse = new JsonResponse();
        // Map<String, Object> data = new HashMap<String, Object>();
        // jsonResponse.setData(data);
        // Map<String, Object> map = new HashMap<String, Object>();
        // map.put(V2Constants.ACCESS_TOKEN, accessToken);
        // map.put("openid", openId);
        // IRawDataCredentials tokenCredentials = new RawDataCredentials(map);
        // ISnsResponse<ISnsEndUser> snsResponse =
        // weiXinV2API.getEndUser(tokenCredentials, ip);
        // if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
        // logger.error("fail to get user, snsResponse:{}", snsResponse);
        // return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        // }
        //
        // ISnsEndUser endUser = snsResponse.getData();
        // if (!StringUtils.equals(openuid, endUser.getPlatformIndependentId()))
        // {
        // logger.error("openuid do not equals, submit:{}, get from weixin:{}",
        // openuid,endUser.getPlatformIndependentId());
        // return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        // }
        //
        // //2.获取应用用户信息
        // StoreBusiness storeBusiness =
        // storeUserService.getStoreBusinessByWeixinId(endUser.getPlatformIndependentId());
        // String weiXinNickName = endUser.getNickName();
        //
        // if (storeBusiness != null) {
        //
        // String userNickName = storeBusiness.getBindWeixinName();
        // long id = storeBusiness.getId();
        // //同步昵称@@@
        //
        // storeUserService.synNickName(id, userNickName, weiXinNickName);
        //
        //
        //// //添加用户信息到Header中
        //// loginDelegator.addSetCookie(response, user);
        //// //记录用户登录日志
        //// loginDelegator.addUserLoginLog(ip, client, user);
        // data = storeLoginDelegator.loginUser(storeBusiness,response, ip
        // ,client);
        //
        // if (storeBusiness.getPhoneNumber() == null ||
        // storeBusiness.getPhoneNumber().length() != 11) {
        // data.put("needBindPhone", "YES");
        // }else{
        // data.put("needBindPhone", "NO");
        //
        //// int auditPassNum =
        // storeAuditService.getAuditCount(storeBusiness.getId(),
        // StoreAuditStatusEnum.pass.getIntValue());
        //// if(auditPassNum > 0){
        //// data.put("waitAudit", "NO");
        //// }else{
        //// int auditWaitNum =
        // storeAuditService.getAuditCount(storeBusiness.getId(),
        // StoreAuditStatusEnum.submit.getIntValue());
        //// if(auditWaitNum > 0){
        //// data.put("waitAudit", "YES");
        //// }else{
        //// data.put("needFillData", "YES");
        //// }
        //// }
        // if(storeBusiness.getLegalPerson() != null &&
        // storeBusiness.getLegalPerson().trim().length() > 1){
        // data.put("needFillData", "NO");
        // }else{
        // data.put("needFillData", "YES");
        // }
        // }
        // data.put("needRegFlag", "NO");
        // storeLoginDelegator.loginUser(storeBusiness, response, ip, client);
        // } else {
        // //return
        // jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED);
        // data.put("needRegFlag", "YES");
        // data.put("needBindPhone", "YES");
        // data.put("needFillData", "YES");
        //
        // }
        // String nextStep = "index";
        // if(data.get("needBindPhone") != null &&
        // data.get("needBindPhone").equals("YES")){
        // nextStep = "bindPhone";
        // }else if(data.get("needFillData") != null &&
        // data.get("needFillData").equals("YES")){
        // nextStep = "fillData";
        // }
        // data.put("nextStep", nextStep);
        // return jsonResponse.setSuccessful().setData(data);
    }

    /**
     * 微信登录 3.7.2版本
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
     * @date: 2018/5/17 13:46
     */
    @RequestMapping( value = "/wxLoginV372" )
    @ResponseBody
    public JsonResponse wxLogin372(@RequestParam( "openid" ) String openId,
                                   @RequestParam( "openuid" ) String openuid, @RequestParam( "access_token" ) String accessToken,
                                   @ClientIp String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {
        try {
            return storeLoginDelegator.wxLoginV372(openId, openuid, accessToken, ip, client, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse().setError(e.getMessage());
        }
    }


    // 微信新用户注册 绑定手机号发送验证码
    @RequestMapping( value = "/weixinReg/sendVerifyCode" )
    @ResponseBody
    public JsonResponse weixinRegSendPhoneVerifyCode(@RequestParam( "phone" ) String phone,
                                                     @RequestParam( "send_type" ) String sendType) {

        try {
            return storeUserService.weixinRegSendCode(phone, sendType);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }

    // 手机号登录发送验证码
    @RequestMapping( value = "/sendVerifyCode" )
    @ResponseBody
    public JsonResponse sendPhoneVerifyCode(@RequestParam( "phone" ) String phone) {

        try {
            return storeUserService.sendPhoneVerifyCode(phone);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }

    // 新微信用户注册提交（附加手机绑定）
    @RequestMapping( value = "/weiXinRegPhoneBind/commit", method = RequestMethod.POST )
    @ResponseBody
    public JsonResponse weiXinRegphoneNumberCommit(@RequestParam( "phone" ) String phone,
                                                   @RequestParam( "send_type" ) String sendType, @RequestParam( "verify_code" ) String verifyCode,
                                                   @RequestParam( "openid" ) String openId, @RequestParam( "openuid" ) String openuid,
                                                   @RequestParam( "access_token" ) String accessToken, HttpServletRequest request, HttpServletResponse response,
                                                   @RequestParam( "user_type" ) UserType userType, @ClientIp String ip, ClientPlatform client) {
        try {
            return storeUserService.weixinRegPhoneNumberCommit(phone, verifyCode, sendType, openId, openuid,
                    accessToken, userType, ip, request, response, client);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }

    // 新用户填充资料2.4版本开始使用
    @RequestMapping( value = "/fillDataCommitV24/auth"/*, method = RequestMethod.POST*/ ) //
    @ResponseBody
//	@Login
    public JsonResponse fillDataCommit(@RequestParam( value = "businessName", required = false ) String businessName,
                                       @RequestParam( value = "province", required = false ) String province,
                                       @RequestParam( value = "city", required = false ) String city,
                                       @RequestParam( value = "county", required = false ) String county,
                                       @RequestParam( value = "businessAddress", required = false ) String businessAddress,
                                       @RequestParam( value = "legalPerson", required = false ) String legalPerson,
                                       @RequestParam( value = "idNumber", required = false ) String idNumber,
                                       @RequestParam( value = "regPhone", required = false ) String regPhone,
                                       // @RequestParam(value="storeType",required=false) int storeType,
                                       @RequestParam( value = "qualificationProofImages", required = false ) String qualificationProofImages,
                                       @RequestParam( value = "referenceNumber", required = false, defaultValue = "" ) String referenceNumber,
                                       @RequestParam( value = "storeStyleIds", required = false, defaultValue = "" ) String storeStyle,//女装风格
                                       @RequestParam( value = "storeAgeIds", required = false, defaultValue = "" ) String storeAge,//年龄范围
                                       @RequestParam( value = "storeAreaScopeId", required = false, defaultValue = "" ) Integer storeAreaScope,//店铺面积
                                       @RequestParam( value = "priceLevel", required = false, defaultValue = "" ) String priceLevel,//价类编码
                                       @RequestParam( value = "purchaseChannel", required = false, defaultValue = "" ) String purchaseChannel,//进货渠道编码
                                       UserDetail userDetail, HttpServletResponse response, @ClientIp String ip, ClientPlatform client) {

        try {
            return storeUserService.fillDataCommitV24(businessName, province, city, county, businessAddress,
                    legalPerson, idNumber, regPhone, client, userDetail, response, ip, qualificationProofImages,
                    referenceNumber, storeStyle, storeAge, storeAreaScope, priceLevel, purchaseChannel);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }

    @RequestMapping( value = "/phoneBindWeixin/oauth", method = RequestMethod.POST )
    @ResponseBody
    public JsonResponse phoneBindWeixinOauth(@RequestParam( "openid" ) String openId,
                                             @RequestParam( "openuid" ) String openuid, @RequestParam( "access_token" ) String accessToken,
                                             @RequestParam( "phone" ) String phone, @ClientIp String ip, ClientPlatform client,
                                             HttpServletRequest request, HttpServletResponse response) {

        try {
            return storeLoginDelegator.phoneBindWeixinCommit(openId, openuid, accessToken, phone, ip, client, request,
                    response);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }

        // //1、获取用户信息
        // JsonResponse jsonResponse = new JsonResponse();
        // Map<String, Object> data = new HashMap<String, Object>();
        // jsonResponse.setData(data);
        // Map<String, Object> map = new HashMap<String, Object>();
        // map.put(V2Constants.ACCESS_TOKEN, accessToken);
        // map.put("openid", openId);
        // IRawDataCredentials tokenCredentials = new RawDataCredentials(map);
        // ISnsResponse<ISnsEndUser> snsResponse =
        // weiXinV2API.getEndUser(tokenCredentials, ip);
        // if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
        // logger.error("fail to get user, snsResponse:{}", snsResponse);
        // return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        // }
        //
        // ISnsEndUser endUser = snsResponse.getData();
        // if (!StringUtils.equals(openuid, endUser.getPlatformIndependentId()))
        // {
        // logger.error("openuid do not equals, submit:{}, get from weixin:{}",
        // openuid,endUser.getPlatformIndependentId());
        // return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        // }
        //
        // //2.获取应用用户信息
        // StoreBusiness storeBusiness =
        // storeUserService.getStoreBusinessByWeixinId(endUser.getPlatformIndependentId());
        // StoreBusiness storeBusinessByPhone =
        // storeUserService.getStoreBusinessByPhone(phone);
        // String weiXinNickName = endUser.getNickName();
        //
        // if (storeBusiness != null || storeBusinessByPhone != null) {
        // if(storeBusiness != null){
        //
        // logger.info("Weixin 已绑定其它手机，无法重复绑定");
        // return
        // jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED_LOGIN);
        // }else{
        // logger.info("手机号已绑定其它微信，无法重复绑定");
        // return
        // jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
        // }
        //
        // } else {
        // storeUserService.addUserWeixinAndPhone(phone,UserType.PHONE,
        // client,endUser.getPlatformIndependentId(), weiXinNickName,
        // endUser.getAvatar());
        // }
        // storeBusiness =
        // storeUserService.getStoreBusinessByWeixinId(endUser.getPlatformIndependentId());
        // storeLoginDelegator.loginUser(storeBusiness, response, ip, client);
        // String nextStep = "fillData";
        // data.put("nextStep", nextStep);
        // return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping( value = "/ucpaas/send" ) // , method = RequestMethod.POST
    @ResponseBody
    public JsonResponse ucpaasSendCode(@RequestParam( "phone" ) String phone, @RequestParam( "sendType" ) String sendType,
                                       HttpServletResponse response) {

        try {
            return loginDelegator.ucpaasSendCode(phone, sendType);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }

    @RequestMapping( value = "/logout" )
    @ResponseBody
    public JsonResponse logout(HttpServletResponse response) {
        LoginUtil.deleteLoginCookie(response);
        return new JsonResponse().setSuccessful();
    }

    // ====================================================================================================================================================================

    /**
     * 手机号码验证码登陆2.3版本和之前版本
     *
     * @param phone
     * @param verifyCode
     * @param sendType
     * @param response
     * @param ip
     * @param client
     * @return
     */
    @RequestMapping( value = "/verifyCommit" ) // , method = RequestMethod.POST
    @ResponseBody
    public JsonResponse verifyCommit(@RequestParam( "phone" ) String phone,
                                     @RequestParam( "verify_code" ) String verifyCode, @RequestParam( "send_type" ) String sendType,
                                     HttpServletRequest request, HttpServletResponse response, @ClientIp String ip, ClientPlatform client) {
        try {
            return loginDelegator.verifyCommit(phone, verifyCode, sendType, request, response, ip, client);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
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
    @RequestMapping( value = "/ext/oauth", method = RequestMethod.POST )
    @ResponseBody
    public JsonResponse externalOAuthLogin186(@RequestParam( "openid" ) String openId,
                                              @RequestParam( "openuid" ) String openuid, @RequestParam( "access_token" ) String accessToken,
                                              @ClientIp String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {
        try {
            return storeLoginDelegator.weixinLogin(openId, openuid, accessToken, ip, client, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }

    // =====================================================================================================================================================================
    // 新用户填充资料2.3版本,2.4版本开始不再使用
    @RequestMapping( value = "/fillDataCommit", method = RequestMethod.POST ) //
    @ResponseBody
//	@Login
    public JsonResponse fillDataCommit(@RequestParam( value = "businessName", required = false ) String businessName,
                                       @RequestParam( value = "province", required = false ) String province,
                                       @RequestParam( value = "city", required = false ) String city,
                                       @RequestParam( value = "county", required = false ) String county,
                                       @RequestParam( value = "businessAddress", required = false ) String businessAddress,
                                       @RequestParam( value = "legalPerson", required = false ) String legalPerson,
                                       @RequestParam( value = "idNumber", required = false ) String idNumber,
                                       @RequestParam( value = "regPhone", required = false ) String regPhone,
                                       // @RequestParam(value="storeType",required=false) int storeType,
                                       // @RequestParam(value="qualificationProofImages",required=false)
                                       // String qualificationProofImages,
                                       UserDetail userDetail, HttpServletResponse response, @ClientIp String ip, ClientPlatform client) {

        try {
            return storeUserService.fillDataCommit(businessName, province, city, county, businessAddress, legalPerson,
                    idNumber, regPhone, client, userDetail, response, ip);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }

    /**
     * 微信绑定手机号
     *
     * @param: verifyCode 手机验证码
     * @param: sendType 手机验证方式
     * @param: openId
     * @param: openuid
     * @param: accessToken
     * @param: phone
     * @param: ip
     * @param: client
     * @param: request
     * @param: response
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/17 17:06
     */
    @RequestMapping( value = "/bindWeixin/oauth")
    @ResponseBody
    public JsonResponse bindWeixinOauth(
            @RequestParam( "phone" ) String phone,
            @RequestParam( "verify_code" ) String verifyCode,
            @RequestParam( "send_type" ) String sendType,
            @RequestParam( "openid" ) String openId,
            @RequestParam( "openuid" ) String openuid,
            @RequestParam( "access_token" ) String accessToken,
            @ClientIp String ip, ClientPlatform client,
            HttpServletRequest request, HttpServletResponse response
    ) {

        try {

            JsonResponse js =  storeLoginDelegator.bindWeixinOauthV372(verifyCode, sendType, openId, openuid, accessToken, phone, ip, client, request,
                    response);
            grant(js);
            return js;
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse jsonResponse = new JsonResponse();
            return jsonResponse.setError(e.getMessage());
        }
    }


    /**
     * 绑定用户自己的手机号码, 不登录
     * @param: openId
     * @param: openuid
     * @param: accessToken
     * @param: ip
     * @param: client
     * @param: request
     * @param: response
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/18 11:45
     */
    @RequestMapping( value = "/bindWeixin/own/auth")
    @ResponseBody
    @Login
    public JsonResponse bindWeixinOwn(
            @RequestParam( "openid" ) String openId,
            @RequestParam( "openuid" ) String openuid,
            @RequestParam( "access_token" ) String accessToken,
            UserDetail<StoreBusiness> userDetail,
            @ClientIp String ip
    ) {
        try {
            return storeLoginDelegator.updateWeiXinInfo(openId, openuid, accessToken, userDetail.getId(), ip);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse().setError(e.getMessage());
        }
    }

    /**
     * 测试新人优惠券
     *
     * @param: userDetail
     * @return: com.jiuyuan.web.help.JsonResponse
     * @auther: Charlie(唐静)
     * @date: 2018/5/24 10:30
     */
    @RequestMapping( "/testCoupon" )
    @ResponseBody
    @Login
    public JsonResponse testCoupon(UserDetail<StoreBusiness> userDetail) {
        try {
            storeLoginDelegator.sendFirstLoginCoupon(userDetail.getUserDetail());
            return new JsonResponse().setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse().setError(e.getMessage());
        }
    }

}