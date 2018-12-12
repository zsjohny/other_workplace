package com.finace.miscroservice.user.controller;


import com.finace.miscroservice.commons.annotation.RateVerify;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.enums.InterceptorModeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.JwtToken;
import com.finace.miscroservice.commons.utils.Regular;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.DeviceUtil;
import com.finace.miscroservice.commons.utils.tools.iphelper.IPUtils;
import com.finace.miscroservice.user.enums.ContactInformationTypeEumus;
import com.finace.miscroservice.user.po.AccountLogPO;
import com.finace.miscroservice.user.po.UserPO;
import com.finace.miscroservice.user.rpc.BorrowRpcService;
import com.finace.miscroservice.user.service.AccountService;
import com.finace.miscroservice.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户模块主要接口:app用户注册,h5分享注册,修改密码,忘记密码,手机验证,验证码发送,投资记录,回款日历,资金明细,我的,分享界面发送手机号码
 */
@RestController
@RefreshScope
public class AppUserController extends BaseController {
    private Log logger = Log.getInstance(AppUserController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BorrowRpcService borrowRpcService;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Value("${user.shareKey}")
    protected String shareKey;

    @Value("${user.server.status}")
    protected String ustatus;


    /**
     * app用户注册
     *
     * @param mobile      手机号码
     * @param password    密码
     * @param code        验证码
     * @param refereeuser 邀请码
     * @param regChannel  渠道
     * @param uid         设备id
     * @param imei        ios的idfa,android的imei
     * @param pushId      推送id
     * @return
     */
    @PostMapping("signup")
    public Response signup(@RequestParam(value = "telphone", required = true) String mobile,
                           @RequestParam(value = "password", required = true) String password,
                           @RequestParam(value = "code", required = true) String code,
                           @RequestParam(value = "referee", required = false) String refereeuser,
                           @RequestParam(value = "channel", required = false) String regChannel,
                           @RequestHeader(value = JwtToken.UID, required = false) String uid,
                           @RequestParam(value = "imei", required = false) String imei,
                           @RequestParam(value = "pushId", required = false) String pushId) {

        String ipadress = IPUtils.getRemortIP(request); // 用户ip
        Integer isDevice = DeviceUtil.isDevice(request.getHeader("user-agent"));
        return userService.signIn(mobile, password, code, refereeuser, regChannel, uid, imei, pushId, ipadress, isDevice, response.get());
    }


    /**
     * h5分享注册
     *
     * @param shareid  分享的id,url最后的字段
     * @param channel  手机号码
     * @param mobile   密码
     * @param password 验证码
     * @param code     渠道
     * @return
     */
    @PostMapping("shareSignup")
    public Response shareSignup(@RequestParam(value = "shareid", required = false) String shareid,
                                @RequestParam(value = "channel", required = false) String channel,
                                @RequestParam("telphone") String mobile,
                                @RequestParam("password") String password,
                                @RequestParam("code") String code) {
        String ipadress = IPUtils.getRemortIP(request); // 用户ip
        Integer isDevice = DeviceUtil.isDevice(request.getHeader("user-agent"));
        return userService.shareSingIn(shareid, channel, mobile, password, code, ipadress, isDevice);
    }


    /**
     * 分享界面发送手机号码
     *
     * @param phone   手机号码
     * @param shareid 分享id
     * @return
     */
    @RateVerify(value = 5, time = 10, timeUnit = TimeUnit.MINUTES, interceptingField = "shareid", interceptingMode = InterceptorModeEnum.BODY_PARAM, allowedCrossDomain = false)
    @RequestMapping("shareSendCode")
    public Response shareSendCode(@RequestParam String phone,
                                  @RequestParam(value = "shareid", required = false) String shareid) {
        logger.info("分享界面,开始向{}发送验证码,shareid={}", phone, shareid);
        return userService.scode(phone, 1, ustatus);
    }


    /**
     * 渠道界面发送手机号码
     *
     * @param phone 手机号码
     *              url拦截 did h5获取设备id
     * @return
     */
    @RateVerify(value = 10, time = 60, timeUnit = TimeUnit.SECONDS, interceptingField = "did", interceptingMode = InterceptorModeEnum.PATH_PARAM, allowedCrossDomain = false)
    @RequestMapping("channelSendCode/{uid}")
    public Response channelSendCode(@RequestParam String phone,
                                    @RequestParam(value = "did", required = false) String did) {
        logger.info("开始向{}发送验证码", phone);
        return userService.scode(phone, 2, ustatus);
    }


    /**
     * 发送验证码
     *
     * @param phone 手机
     * @param type  名称 1--登入后，修改登入密码 2--注册时的手机验证码 3--忘记密码，找回密码发送验证码
     * @return 在header中传过来
     */

    @RateVerify(value = 5, time = 10, timeUnit = TimeUnit.MINUTES)
    @RequestMapping("sendcode")
    public Response sendcode(@RequestParam String phone,
                             @RequestParam(value = "type", required = false) String type) {
        Map<String, Object> result = new HashMap<>();
        logger.info("开始向{}发送验证码,type={}", phone, type);
        if (null == type) {
            return Response.errorMsg("发送短信类型为空");
        }
        String userId = request.getParameter(JwtToken.ID);

        return userService.sendCode(phone, type, userId);
    }

    /**
     * 购买页面发送短信验证码
     *
     * @return 在header中传过来
     */

    @RateVerify(value = 100, time = 10, timeUnit = TimeUnit.MINUTES)
    @RequestMapping("sendFuiouCode/auth")
    public Response sendFuiouCode() {

        String userId = getUserId();
        if (StringUtils.isEmpty(userId)) {
            logger.warn("用户id为空");
            return Response.errorMsg("参数不全");
        }
        logger.info("购买页面向userId={}发送短信验证码", userId);
        return userService.sendFuiouCode(userId);
    }

    /**
     * 用户手机判断是否已注册
     *
     * @param phone 手机号码
     *              data里面status
     *              10001--用户已注册
     *              10002--用户没有注册
     * @return
     */
    @RequestMapping("verphone")
    public Response verphone(@RequestParam(value = "phone", required = true) String phone) {
        logger.info("开始判断手机={},是否已注册 ", phone);
        Map<String, Object> result = new HashMap<>();
        //判断手机格式
        if (Regular.checkPhone(phone)) {
            //判断是否已注册
            boolean isTrue = userService.isRightPhone(phone);
            return userService.upIsTrue(result, isTrue);
        } else if (Regular.checkUserName(phone)) {
            boolean isTrue = userService.isRightUsername(phone);
            return userService.upIsTrue(result, isTrue);
        }
        logger.info("{}手机或用户名格式不正确", phone);
        return Response.errorMsg("手机或用户名格式不正确");
    }

    /**
     * 忘记密码
     *
     * @param npassword 新密码
     * @param code      验证码
     * @param phone     手机号码
     * @param uid       设备标识
     * @return
     */
    @RateVerify(value = 5, time = 10, timeUnit = TimeUnit.MINUTES)
    @PostMapping("modifyPass")
    public Response modifyPass(@RequestParam String npassword,
                               @RequestParam String code,
                               @RequestParam String phone,
                               @RequestHeader(JwtToken.UID) String uid) {
        return userService.modifyPass(npassword, code, phone, uid);
    }


    /**
     * 修改密码
     *
     * @param opassword 原密码
     * @param npassword 新密码
     * @param rpassword 确认密码
     * @param uid       设备标识
     * @return
     */
    @PostMapping("updatePass/auth")
    public Response updatePass(String opassword, String npassword, String rpassword, @RequestHeader(JwtToken.UID) String uid) {

        String userId = getUserId();
        return userService.updatePass(opassword, npassword, rpassword, uid, userId, response.get());
    }


    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping("logout")
    public Response logout() {

        return Response.success();
    }


    /**
     * 我的
     * <p>
     * "sysMsgSize": 0,  系统消息数量
     * "noticeCenter": 0,  公告中心数量
     * "officialNotice": 0,  官方通知数量
     * "phone": "18812322345",  电话
     * "accountTotal": 144553.29, 总资产
     * "hkNum": 0,  回款笔数
     * "useMoney": 23049.9,  可提现余额
     * "interestTotal": 0,  总收益
     * "payChannel": "", 支付渠道 富有--fuiou 汇付--"" or huifu
     * "hbNum": 0,  红包数量
     * "bankCard": 0,  银行卡号
     * "bankName": 0,  银行名称
     * "fsstrans": {  生利宝
     * "id": 3,
     * "userId": 20113, 用户id
     * "status": 2,
     * "total": 134000,  资金总额
     * "yesterdayInterest": 133.423616, 昨天收益
     * "totalInterest": "0.000000", 累计收益
     * "yesterdayApr": "36.343", 最新收益
     * "lastSevenDayApr": "6.3631"  七天年化收益
     * },
     * "investTotal": 0 投资总额
     * "shareid":gfdsghshgfdhngdngbf   分享id
     * "invitationSize":10   邀请人数
     * "isxs" 0  是否是新手  0--不是  1--是
     * "memberLevel":  用户等级  1-保守型2-谨慎型3-稳健型4-积极型5-激进型
     * }
     *
     * @return
     */
    @RequestMapping("homeindex/auth")
    public Response homeindex() {

        String userId = getUserId();
        logger.info("用户{}开始访问,我的", userId);

        return userService.homeIndex(userId);
    }


    /**
     * 回款日历
     *
     * @param tmonth 月份(例:2018-01)
     * @param tday   日期(例:2018-01-02)
     *               <p>
     *               {
     *               "showDay": [
     *               "2018-01-05" 当月有回款的日期
     *               ],
     *               "showDayList": 回款列表[
     *               {
     *               "principal": 20000,  回款本金
     *               "endProfit": 199.23,  回款利息
     *               "nowProfit": 0,
     *               "withdrawPrincipal": 0,
     *               "withdrawProfit": 0,
     *               "borrowName": "1212"  标的名称
     *               "bidStatus":"counting" 状态(例:counting--待还款 repayment--已还款)
     *               }
     *               ],
     *               "showMonth": { 本月回款信息
     *               "principal": 142958,
     *               "endProfit": 0, 本月回款金额
     *               "nowProfit": 0,
     *               "withdrawPrincipal": 27950, 本月已回款金额
     *               "withdrawProfit": 0,
     *               "borrowName": null
     *               }
     *               }
     * @return
     */
    @RequestMapping("returnCalendar/auth")
    public Response returnCalendar(@RequestParam("tmonth") String tmonth, @RequestParam("tday") String tday) {
        String userId = getUserId();
        logger.info("用户={} 开始访问回款日历,tmonth={}, tday={}", userId, tmonth, tday);
        Map<String, Object> map = borrowRpcService.getReturnCalendar(userId, tmonth, tday);
        return Response.successByMap(map);
    }


    /**
     * 资金明细
     *
     * @param page 第几页
     * @param type 类型(cash_success--提现)
     *             <p>
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data": [
     *             {
     *             "id": 5073,
     *             "userId": 20397,
     *             "type": "tender",
     *             "total": 0,
     *             "money": 200, 操作资金
     *             "useMoney": 0, 可用余额
     *             "noUseMoney": 0,  冻结金额
     *             "collection": 0,
     *             "toUser": 20318,
     *             "remark": "0",
     *             "addtime": "1513738907", 交易时间
     *             "toUserName": "13100000001",
     *             "typeName": "投标",  类型
     *             "sunMoney": 0
     *             }
     *             ]
     *             }
     * @return
     */
    @RequestMapping("moneylog/auth")
    public Response moneylog(@RequestParam("page") int page,
                             @RequestParam(value = "type", required = false) String type) {
        String userId = getUserId();
        logger.info("用户{}开始访问资金记录", userId);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("type", type);
        List<AccountLogPO> accountLog = accountService.getZjmxByUserId(map, page);
        return Response.success(accountLog);
    }


    /**
     * 新增用户风险测评
     *
     * @param number 风险测评值(1-保守型2-谨慎型3-稳健型4-积极型5-激进型)
     * @return
     */
    @RequestMapping("addUserRating/auth")
    public Response addUserRating(@RequestParam(value = "number", required = false) String number) {

        String userId = getUserId();
        logger.info("新增用户{}评级分数{}", userId, number);
        if (null == number) {
            logger.warn("新增用户{}评级分数{}");
            return Response.errorMsg("用户评级分数错误");
        }

        if (userService.updateUserRating(userId, Integer.valueOf(number)) > 0) {

            userStrHashRedisTemplate.set(Constant.USER_IS_EVALUATION + userId, "success", 30, TimeUnit.DAYS);
            return Response.success();
        } else {
            logger.warn("用户{}风险测评失败", userId);
            return Response.errorMsg("用户风险测评信息失败");
        }
    }

    /**
     * 判断用户是否需要重新测评
     * <p>
     * <p>
     * code == 200需要测评 其他的不需要
     */
    @RequestMapping("userIsRating/auth")
    public Response addUserRating() {

        String userId = getUserId();
        logger.info("判断用户{}是否需要测评", userId);

        String isRating = userStrHashRedisTemplate.get(Constant.USER_IS_EVALUATION + userId);
        if (isRating == null) {
            logger.info("用户{}需要测评isRating={}", userId, isRating);
            return Response.success();
        } else {
            logger.warn("用户{}不需要测评isRating={}", userId, isRating);
            return Response.error();
        }
    }


    /**
     * 添加意见反馈
     *
     * @param content 意见反馈内容
     * @return
     */
    @RequestMapping("add/feedBack/auth")
    public Response addFeedBack(String content) {
        String userId = getUserId();
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(userId)) {
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
//        String ipadress = "13265654165"; // 用户ip
        String ipadress = IPUtils.getRemortIP(request);
        UserPO user = userService.findUserOneById(userId);
        return userService.addFeedBack(Integer.valueOf(userId), content, ipadress, ContactInformationTypeEumus.PHONE.getCode(), ContactInformationTypeEumus.PHONE.getValue(), user.getPhone(), user.getUsername(), DateUtils.getNowDateStr());
    }


}
