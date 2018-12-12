package com.finace.miscroservice.borrow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.borrow.dao.BorrowDao;
import com.finace.miscroservice.borrow.dao.BorrowTenderDao;
import com.finace.miscroservice.borrow.dao.FinanceBidDao;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.rpc.ActivityRpcService;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.BidService;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.*;
import com.finace.miscroservice.commons.enums.AccountLogTypeEnums;
import com.finace.miscroservice.commons.enums.ActiveGiftEnums;
import com.finace.miscroservice.commons.enums.MsgCodeEnum;
import com.finace.miscroservice.commons.enums.PushExtrasEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.JiguangPush;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.credit.CreditConstant;
import com.finace.miscroservice.commons.utils.credit.CreditUtils;
import com.finace.miscroservice.commons.utils.credit.SignUtil;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.HttpUtil;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.finace.miscroservice.commons.enums.MqChannelEnum.GENERATE_CONTRACT;
import static com.finace.miscroservice.commons.enums.MqChannelEnum.LOAN_MONEY;
import static com.finace.miscroservice.commons.enums.MqChannelEnum.INVITATION_USER_GRANT_HB;
import static com.finace.miscroservice.commons.utils.Constant.SERVICE_PHONE;
import static com.finace.miscroservice.commons.enums.MqChannelEnum.AUTO_UP_BORROW;


/**
 * 投标实现类
 */
@Service
public class BidServiceImpl implements BidService {
    private Log logger = Log.getInstance(BidServiceImpl.class);

    private static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyMMddHHmmss");
    private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private CreditUtils creditUtils;

    @Autowired
    private BorrowDao borrowDao;

    @Autowired
    private FinanceBidDao financeBidDao;

    @Autowired
    private BorrowTenderDao borrowTenderDao;

    @Autowired
    private UserRpcService userRpcService;

    @Autowired
    private ActivityRpcService activityRpcService;

    @Autowired
    @Lazy
    private MqTemplate mqTemplate;

    @Value("${borrow.pay.timeout}")
    private String timeout;

    @Value("${borrow.acitve.starttime}")
    private String starttime;

    @Value("${borrow.acitve.endtime}")
    private String endtime;

    @Value("${base.host}")
    private String baseHost;

    @Value("${borrow.pay.success.url}")
    private String paySuccessUrl;

    @Value("${borrow.pay.error.url}")
    private String payErrorUrl;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;


    @Override
    public Response creditPay(String userId, String amt, Integer borrowId, String hbid, String channel, HttpServletResponse response) {


        User user = userRpcService.getUserByUserId(String.valueOf(userId));
        if (null == user) {
            logger.warn("用户{}购买{}标的。用户不存在!", userId, borrowId);
            return Response.errorMsg("用户不存在!");
        }

        CreditGoAccount account = userRpcService.getUserAccountByUserId(userId);
        if (account == null) {
            logger.warn("用户{}购买{}标的。用户没有开户!", userId, borrowId);
            return Response.errorMsg("用户没有开通存管账户!");
        }
        Double buyAmt = Double.valueOf(amt);

        // 判断可投金额小于最小额度限制时 最小额度不做判断 account - acccount_yes > lowestAccount
        /*if ( buyAmt > Double.valueOf(account.getAvailBal())) {
            logger.warn("用户{}购买{}标的。账户余额不足!", userId, borrowId);
            return Response.errorMsg("账户余额不足,请充值!");
        }*/

        BorrowPO borrow = borrowDao.getBorrowById(borrowId);
        if (null == borrow) {
            logger.warn("用户{}购买{}标的。标的不存在!", userId, borrowId);
            return Response.errorMsg("标的不存在!");
        }

        //新手标判断
        if (0 == Integer.valueOf(borrow.getUse())) {
            Double allAccount = financeBidDao.getAllFinaceByUserId(Integer.valueOf(userId)) + borrowTenderDao.getAllTenderByUserId(Integer.valueOf(userId));
            if (allAccount > 0) {
                logger.warn("对不起,该标只允许新手投资!");
                return Response.error(406, "对不起,该标只允许新手投资!");
            }
        }

        // 判断可投金额小于最小额度限制时 最小额度不做判断 account - acccount_yes > lowestAccount
        if (borrow.getRemmoney() > Double.valueOf(borrow.getLowestAccount())) {
            /*logger.warn("用户{}购买{}标的。剩余金额不足，请投其他标的!", userId, borrowId);
            return Response.errorMsg("剩余金额不足，请投其他标的!");*/

            if (buyAmt < Double.valueOf(borrow.getLowestAccount())) {
                logger.warn("用户{}购买{}标的。购买金额不能小于起投金额!", userId, borrowId);
                return Response.errorMsg("购买金额不能小于起投金额!");
            }
        } else {
            if (buyAmt < borrow.getRemmoney()) {
                logger.warn("用户{}购买{}标的。剩余金额必须全部购买!", userId, borrowId);
                return Response.errorMsg("对不起，剩余金额必须全部购买!");
            }
        }

        if (buyAmt > Double.valueOf(borrow.getRemmoney())) {
            logger.warn("用户{}购买{}标的。超过可购金额!", userId, borrowId);
            return Response.errorMsg("因剩余金额过小，扫尾可得10元红包!");
        }

        //标的可购买金额-购买金额 < 起投金额  不能购买
        if (((Double.valueOf(borrow.getRemmoney()) - buyAmt) < Double.valueOf(borrow.getLowestAccount())) && (Double.valueOf(borrow.getRemmoney()) - buyAmt) != 0) {
            logger.warn("用户{}购买{}标的。标的可购买金额减去购买金额不能小于起投金额!", userId, borrowId);
            return Response.errorMsg("标的可购买金额减去购买金额不能小于起投金额!");
        }

        if (Double.valueOf(borrow.getMostAccount()) != 0 && buyAmt > Double.valueOf(borrow.getMostAccount())) {
            logger.warn("用户{}购买{}标的。超过限额!", userId, borrowId);
            return Response.errorMsg("超过限额!");
        }

        FinanceBidPO financeBid = new FinanceBidPO();
        Map<String, String> reqMap = new TreeMap<>();
        //判断使用红包
        if (!"".equals(hbid) && hbid != null) {
            UserRedPackets userRedPackets = this.activityRpcService.getRpById(Integer.valueOf(hbid));
            if (null == userRedPackets) {
                logger.warn("用户{}购买{}标的。对不起,选择的福利劵不存在!", userId, borrowId);
                return Response.errorMsg("对不起,选择的福利劵不存在!");
            }

            // 判断投资期限是否可以使用该红包
            if (userRedPackets.getSday() != 0 && userRedPackets.getSday() > borrow.getTimeLimitDay()) {
                logger.warn("用户{}购买{}标的。该投资期限不能使用该福利卷!", userId, borrowId);
                return Response.errorMsg("该投资期限不能使用该福利卷!");
            }

            // 判断投资金额是否大于 红包使用金额限制
            if (Double.valueOf(userRedPackets.getSmoney()) > buyAmt) {
                logger.warn("用户{}购买{}标的。该投资金额不能使用该福利卷!", userId, borrowId);
                return Response.errorMsg("该投资金额不能使用该福利卷!");
            }
            financeBid.setHbid(hbid);

            if (userRedPackets.getHbtype() == 1) {
//                reqMap.put("bonusFlag", "1");  //0-不使用红包（若不出现，默认为0） 1-使用红包
//                reqMap.put("bonusAmount", userRedPackets.getHbmoney().toString());
                financeBid.setCouponAmt(BigDecimal.valueOf(userRedPackets.getHbmoney()));  //红包金额
            } else {

                financeBid.setCouponRate(BigDecimal.valueOf(userRedPackets.getHbmoney())); //加息劵
            }
        }

        logger.info("用户{}购买{}标的。开始创建订单!", userId, borrowId);

        String oId = CreditConstant.orderId + userId + yyyyMMddHHmmss.format(new Date());
        financeBid.setOrderSn(oId);
        financeBid.setBorrowId(borrowId);
        financeBid.setBuyAmt(BigDecimal.valueOf(buyAmt));
        financeBid.setUserId(Integer.valueOf(userId));
        financeBid.setRate(BigDecimal.valueOf(borrow.getApr()));
        financeBid.setPayName(account.getName());
        financeBid.setPayPid(account.getIdNo());
        financeBid.setBankCardNo(account.getCardNo());
        financeBid.setPayChannel("credit");
        financeBid.setChannel(channel);
        financeBid.setRegChannel(channel);
        int isSuccess = financeBidDao.addFinanceBid(financeBid);

        if (isSuccess > 0) {
            logger.info("用户{}购买{}标的。开始登记银行存管标的!oId={}", userId, borrowId, oId);
            try {
                creditUtils.getHeadReq(reqMap);
                reqMap.put("txCode", "bidApply");
                reqMap.put("accountId", account.getAccountId());   //电子账号
                reqMap.put("orderId", oId);   //订单号
                reqMap.put("txAmount", buyAmt.toString());
                reqMap.put("productId", CreditConstant.productIdPre + borrow.getId());   //标的号
                reqMap.put("frzFlag", "1");  //0-不冻结 1-冻结
                reqMap.put("acqRes", String.valueOf(borrow.getId()));  //标的id
                reqMap.put("forgotPwdUrl", "ytjForgotPasswordUrl");  //忘记密码跳转
                reqMap.put("retUrl", payErrorUrl + "?responseMsg=交易失败!");  //返回交易页面链接
                reqMap.put("successfulUrl", baseHost + "/borrow/creditPaySuccess");  //交易成功跳转链接
                reqMap.put("notifyUrl", baseHost + "/borrow/creditPayNotify");  //后台通知链接
                reqMap.put("bonusFlag", "0");  //0-不使用红包 1-使用红包

                //生成待签名字符串
                String requestMapMerged = creditUtils.mergeMap(reqMap);
                //生成签名
                String sign = SignUtil.sign(requestMapMerged);
                reqMap.put("sign", sign);

                logger.info("投标请求信息：\r\n" + JSON.toJSON(reqMap).toString().replace(",", ",\r\n"));
                String reqUrl = CreditUtils.PAGE_URI + "/" + CreditConstant.bidapply;
                String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");

                if (response == null) {
                    logger.warn("用户{}购买{}标的。返回请求失败", userId, borrowId);
                    return Response.errorMsg("返回请求失败!");
                }

                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html;charset=utf-8");
//                reqStr = "<script type=\"text/javascript\" src=\"https://code.jquery.com/jquery-3.3.1.js\"></script>"+reqStr;
                reqStr = "<script type=\"text/javascript\" src=\"https://www.etongjin.net/jquery.min.js\"></script>" + reqStr;
                logger.info(reqStr);
                response.getWriter().write(reqStr);

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("用户{}投标：{}", userId, e);
            }
        }

        return null;
    }


    @Override
    public void creditPayNotify(String bgData, HttpServletResponse response) {

        JSONObject map = JSONObject.parseObject(bgData);
        String txCode = map.get("txCode").toString();
        String seqNo = map.get("seqNo").toString();
        String txTime = map.get("txTime").toString();
        String txDate = map.get("txDate").toString();
        String retCode = map.get("retCode").toString();
        String retMsg = map.get("retMsg").toString();

        String accountId = map.get("accountId").toString();
        String orderId = map.get("orderId").toString();
        String txAmount = map.get("txAmount").toString();
        String productId = map.get("productId").toString();
        String acqRes = map.get("acqRes").toString();

        if (retCode != null && retCode.equals(CreditConstant.CREDIT_SUCCESS)) {
            String authCode = map.get("authCode").toString();
            logger.info("投标记录开始处理,retCode={},retMsg={},productId={},accountId={},orderId={},txAmount={},acqRes={},authCode={}", retCode, retMsg, productId, accountId, orderId, txAmount, acqRes, authCode);
            final FinanceBidPO bid = financeBidDao.getFidByNoOrderId(orderId);
            if (bid != null) {
                BorrowPO borrow = borrowDao.getBorrowById(bid.getBorrowId());

                bid.setPay(1);  //已支付
                bid.setSummary(authCode);  //授权码
                bid.setRate(BigDecimal.valueOf(borrow.getApr()));
                try {
                    // 起息T+1
                    Date today0Hour = null;
                    today0Hour = yyyyMMdd.parse(yyyyMMdd.format(new Date()));

                    //today0Hour.setTime(Long.valueOf(DateUtils.dateToStampSimple(borrow.getEachTime())));
                    today0Hour.setTime(Long.valueOf(DateUtils.getNowTimeStr()));

                    bid.setBeginProfit(new Date(today0Hour.getTime()));
                    today0Hour.setDate(today0Hour.getDate() + borrow.getTimeLimitDay());
                    bid.setEndProfit(new Date(today0Hour.getTime()));
                } catch (Exception e) {
                    logger.error("投标回调失败：{}", e);
                }
                bid.setBuyAmt(BigDecimal.valueOf(Double.valueOf(txAmount)));
                financeBidDao.updateFinanceBid(bid);  //修改投标记录

                //判断是否有红包使用
                if (!"".equals(bid.getHbid()) && bid.getHbid() != null) {

                    //修改福利券状态
                    this.activityRpcService.updateHbStatus(bid.getHbid(), bid.getUserId(), borrow.getName(), Double.valueOf(txAmount));
                }

                logger.info("投资成功，修改标的{}的已购买次数tenderTimes={}和已购买金额accountYes={},本次购买金额={}", borrow.getId(), borrow.getTenderTimes(), borrow.getAccountYes(), txAmount);
                // 修改borrow信息
                //borrow.setTenderTimes(Integer.toString((StringUtil.isNotEmpty(borrow.getTenderTimes()) ? Integer.parseInt(borrow.getTenderTimes()) : 0) + 1));
                //borrow.setAccountYes(BigDecimal.valueOf(StringUtil.isNotEmpty(borrow.getAccountYes()) ? Double.parseDouble(borrow.getAccountYes()) : 0).add().doubleValue() + "");

                if ((Double.valueOf(borrow.getAccount()) - Double.valueOf(borrow.getAccountYes()) - Double.valueOf(txAmount)) <= 0) {
                    borrow.setStatus(5);  //已满标

                    //满标放款
                    mqTemplate.sendMsg(LOAN_MONEY.toName(), String.valueOf(borrow.getId()));
                }
                borrow.setAccountYes(txAmount);
                borrowDao.updateBorrow(borrow);  //修改标的记录


//                AccountLog log = new AccountLog();
//                log.setMoney(bid.getBuyAmt().doubleValue());
//                log.setTotal(0.0);
//                log.setUseMoney(0.0);
//                log.setUser_id(bid.getUserId());//
//                log.setType("tender");
//                log.setNoUseMoney(0.0);
//                log.setToUser(borrow.getUserId());
//                log.setAddtime(DateUtils.getNowTimeStr());
//                log.setCollection(0.0);
//                log.setRemark("投资成功，冻结投资者的投标资金" + NumberUtil.format4(bid.getBuyAmt().doubleValue()));
//                userRpcService.addAccountLog(log);

                //做活动
                this.doActivity(String.valueOf(bid.getUserId()), Double.valueOf(txAmount), Integer.valueOf(borrow.getTimeLimitDay()));

                try {
                    logger.info("用户={}投资{}元，{}天标送金豆", bid.getUserId(), bid.getBuyAmt(), borrow.getTimeLimitDay());
                    activityRpcService.investGrantGlodBean(String.valueOf(bid.getUserId()), borrow.getTimeLimitDay(), bid.getBuyAmt().doubleValue());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("投资送金豆失败");
                }

                //生成合同
                mqTemplate.sendMsg(GENERATE_CONTRACT.toName(), String.valueOf(bid.getId()));
                //计算预期收益
                Double dev = NumberUtil.multiply(2, bid.getBuyAmt(), borrow.getTimeLimitDay(), bid.getRate());
                Double insterst = NumberUtil.divide(2, dev, 36500);
                //投标成功后 添加消息 至 消息中心
                userRpcService.addMsg(bid.getUserId(), MsgCodeEnum.SYS_MSG.getCode(), MsgCodeEnum.SYS_SUBTYPE_BID.getValue(),
                        String.format(MsgCodeEnum.SYS_MSG_TEXT.getValue(), borrow.getName(), String.valueOf(bid.getBuyAmt()), String.valueOf(insterst), borrow.getTimeLimitDay(), SERVICE_PHONE));

                String alias = userStrHashRedisTemplate.get(Constant.PUSH_ID + bid.getUserId()) != null ? userStrHashRedisTemplate.get(Constant.PUSH_ID + bid.getUserId()) : "";
                if (!"".equals(alias)) {
                    logger.info("开始向用户{}推送消息pushId={}", bid.getUserId(), alias);
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("msgCode", String.valueOf(MsgCodeEnum.SYS_MSG.getCode()));
                    JiguangPush.sendPushIosAndroidByAlias(alias, MsgCodeEnum.SYS_MSG.getValue(), MsgCodeEnum.SYS_SUBTYPE_BID.getValue(), map1);
                }

                //自动上标
                mqTemplate.sendMsg(AUTO_UP_BORROW.toName(), String.valueOf(borrow.getBorrow_group()));

                //查找用户的账户信息
                CreditGoAccount creditGoAccount = userRpcService.getUserAccountByUserId(String.valueOf(bid.getUserId()));
                creditGoAccount.setCutPayment(creditGoAccount.getCutPayment() - bid.getBuyAmt().doubleValue());  //修改用户可用提现余额
                //修改用户可用提现余额
                userRpcService.updateCreditAccount(creditGoAccount);

                //添加资金流水记录
                AccountLogPo accountLog  = new AccountLogPo();
                accountLog.setUserId(bid.getUserId());
                accountLog.setTxMoney(bid.getBuyAmt().doubleValue());
                accountLog.setTxCode("tender");
                accountLog.setIsSuccess(0);  //投资成功
                accountLog.setAccountId(creditGoAccount.getAccountId());
                userRpcService.saveCallBack(accountLog);

                //CreditGoAccount account = userRpcService.getUserAccountByUserId(userId);


            } else {
                logger.warn("投标记录{}已处理,retCode={},retMsg={},productId={},accountId={},txAmount={},acqRes={}", orderId, retCode, retMsg, productId, accountId, orderId, txAmount, acqRes);
            }
        } else {
            logger.warn("投标失败,retCode={},retMsg={},productId={},accountId={},orderId={},txAmount={},acqRes={}", retCode, retMsg, productId, accountId, orderId, txAmount, acqRes);
        }

        try {
            response.getWriter().write("success");
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 投资活动
     */
    private void doActivity(String userId, Double amt, Integer timeLimitDay) {
        //判断是否有推荐人
        User user = userRpcService.getUserByUserId(userId);
        try {

            //当前时间
            String now = DateUtils.getNowDateStr();
            //开始时间小于于当前时间 且 结束时间大于当前时间
            logger.info("活动时间-->{}<-<-->->{}", starttime, endtime);
            logger.info("用户注册时间-->{}", user.getAddtime());
            if (DateUtils.compareDate(now, starttime) && DateUtils.compareDate(endtime, now) && Long.valueOf(user.getAddtime()) > DateUtils.getTime(starttime)) {
                logger.info("活动期间投资送红包");

                DateUtils.getDistanceTime(DateUtils.dateStr2(user.getAddtime()), DateUtils.dateStr2(String.valueOf(System.currentTimeMillis() / 1000)));
                String nowDay = DateUtils.getNowDateStr();
                Date time1 = DateUtils.getDate(user.getAddtime());
                String date = DateUtils.dateStr4(time1);
                String distance = DateUtils.getDistanceTime(date, nowDay).split("天")[0];
                String distance2 = DateUtils.getDistanceTime(date, nowDay).split("天")[0];
                //判断是否是首次投资
                if (amt.compareTo(financeBidDao.getAllFinaceByUserId(Integer.valueOf(userId))) == 0) {
                    //第一次投资推送现象
                    pushMsg(userStrHashRedisTemplate.get(Constant.PUSH_ID + user.getUser_id()) != null ? userStrHashRedisTemplate.get(Constant.PUSH_ID + user.getUser_id()) : null);

                    //被邀请用户 注册之日7天内 首投5000元以上
                    if (null != user && user.getInviteUserid() != 0 && amt >= 5000d && Integer.parseInt(distance) < 7) {
                        logger.info("被邀请人{}首次投资{}，送红包", userId, amt);
                        Map<String, Object> map = new HashMap<>();
                        map.put("inviter", user.getInviteUserid());
                        map.put("userid", user.getUser_id());
                        map.put("buyamt", amt);
                        map.put("code", ActiveGiftEnums.SING_UP_GIRT.getCode());
                        mqTemplate.sendMsg(INVITATION_USER_GRANT_HB.toName(), JSON.toJSONString(map));

                        map.put("timeLimitDay", timeLimitDay);
                    }
                }
                //根据用户id查找 累计投资金额
                String amountMoney = financeBidDao.findAmountMoneyByUserId(user.getUser_id(), starttime, endtime);
                amountMoney = amountMoney == null ? "0" : amountMoney;
                //根据 用户id和邀请人id查询  佣金奖
                List<UserJiangPin> list = activityRpcService.findUserJiangPin(user.getUser_id(), user.getInviteUserid(), ActiveGiftEnums.INVITATION_GIRT.getCode());
                //推荐人不为空 且 邀请单个好友累计投资金额每满10,000元 投资新手标除外，邀请单个好友可赚取的奖励上限为300元京东卡
                logger.info("用户={}累计投资={}", user.getUser_id(), amountMoney);

                if (null != user && user.getInviteUserid() != 0 && list.size() < 3 && Integer.parseInt(distance2) < 30) {
                    if (Double.parseDouble(amountMoney) >= 10000d) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("inviter", user.getInviteUserid());
                        map.put("userid", user.getUser_id());
                        map.put("buyamt", amountMoney);
                        map.put("code", ActiveGiftEnums.INVITATION_GIRT.getCode());
                        mqTemplate.sendMsg(INVITATION_USER_GRANT_HB.toName(), JSON.toJSONString(map));
                    }
                }
                //推荐人不为空 且 邀请每满3人，且该3人每人累计投资金额至少满10,000元 获得次数少于3次
                List<UserJiangPin> list2 = activityRpcService.findUserJiangPin(null, user.getInviteUserid(), ActiveGiftEnums.TEAM_GIRT.getCode());
                List<UserJiangPin> lis3 = activityRpcService.findUserJiangPin(user.getUser_id(), user.getInviteUserid(), ActiveGiftEnums.TEAM_GIRT.getCode());
                //邀请好友人数
                Integer invitationDistinctSize = financeBidDao.findInvitationDistanct(user.getInviteUserid(), starttime, endtime);
                if (null != user && user.getInviteUserid() != 0 && list2.size() < 9 && lis3.size() < 1 && Integer.parseInt(distance2) < 30) {
                    if (Double.valueOf(amountMoney) >= 10000d) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("inviter", user.getInviteUserid());
                        map.put("userid", user.getUser_id());
                        map.put("buyamt", amountMoney);
                        map.put("code", ActiveGiftEnums.TEAM_GIRT.getCode());
                        mqTemplate.sendMsg(INVITATION_USER_GRANT_HB.toName(), JSON.toJSONString(map));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户{}被邀请人{}投资{}，送红包失败", user.getInviteUserid(), userId, amt);
        }
    }


    /**
     * 第一次购买
     *
     * @param pushId
     */
    private void pushMsg(String pushId) {
        if (null == pushId) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("type", PushExtrasEnum.BUY_FIRST.getCode());
        JiguangPush.sendPushIosAndroidMsgByalias(PushExtrasEnum.BUY_FIRST.getValue(), pushId, map);
    }


    @Override
    public void loanMoneyNotify(String bgData, HttpServletResponse response) {
        JSONObject map = JSONObject.parseObject(bgData);
        String txCode = map.get("txCode").toString();
        String batchNo = map.get("batchNo").toString();  //标的id
        String txAmount = map.get("txAmount").toString();
        String txCounts = map.get("txCounts").toString();

        logger.info("标的{}放款,数据合法性检查的异步通知,数据回调,txAmount={},txCounts={}", batchNo, txAmount, txCounts);
        BorrowPO borrowPO = borrowDao.getBorrowById(Integer.valueOf(batchNo));
        if (borrowPO == null) {
            logger.info("标的{}放款,数据合法性检查的异步通知失败,标的不存在", batchNo);
            return;
        }

        if (Double.valueOf(txAmount) == Double.valueOf(borrowPO.getAccount()) && Double.valueOf(txCounts) == Double.valueOf(borrowPO.getTenderTimes())) {
            try {
//                //获取投资记录 开始做放款操作
//                List<FinanceBidPO> flist = financeBidDao.getRealFidByBorrowId(batchNo);
//                if (flist != null && flist.size() > 0) {
//                    for (FinanceBidPO financeBidPO : flist) {
//                        financeBidPO.setType("loan");
//                        financeBidDao.updateFinance(financeBidPO);  //修改投资记录为已放款
//                    }
//                }

                response.getWriter().write("success");
            } catch (Exception e) {
                logger.warn(e.getMessage());
                e.printStackTrace();
            }
        }

    }

    @Override
    public void loanMoneyRetNotify(String bgData, HttpServletResponse response) {
        JSONObject map = JSONObject.parseObject(bgData);
        String txCode = map.get("txCode").toString();
        String batchNo = map.get("batchNo").toString();

        String sucAmount = map.get("sucAmount").toString();  //成功交易金额
        String sucCounts = map.get("sucCounts").toString();  //成功交易笔数

        String failAmount = map.get("failAmount").toString(); //失败交易金额
        String failCounts = map.get("failCounts").toString(); //失败交易笔数

        logger.info("标的{}放款,业务结果通知知,sucAmount={},sucCounts={},failAmount={},failCounts={}", batchNo, sucAmount, sucCounts, failAmount, failCounts);

        BorrowPO borrowPO = borrowDao.getBorrowById(Integer.valueOf(batchNo));
        if (borrowPO == null) {
            logger.info("标的{}放款,业务结果通知失败,标的不存在", batchNo);
            return;
        }
        borrowPO.setFlowCount(Integer.valueOf(failCounts));  //失败交易笔数
        borrowPO.setPartAccount(Double.valueOf(failAmount)); //失败交易金额

        borrowPO.setFlowYescount(Integer.valueOf(sucCounts));
        borrowPO.setOpenAccount(sucAmount);
        borrowDao.updateAllBorrow(borrowPO);

        try {
//            if (null == failCounts || "0".equals(failCounts)) {
//                //获取投资记录 开始做放款操作
//                List<FinanceBidPO> flist = financeBidDao.getRealFidByBorrowId(batchNo);
//                if (flist != null && flist.size() > 0) {
//
//                    for (FinanceBidPO financeBidPO : flist) {
//                        financeBidPO.setType("success_loan");
//                        financeBidDao.updateFinance(financeBidPO);  //修改投资记录为已放款
//                    }
//                }
//            }

            response.getWriter().write("success");
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }

    }


}

















