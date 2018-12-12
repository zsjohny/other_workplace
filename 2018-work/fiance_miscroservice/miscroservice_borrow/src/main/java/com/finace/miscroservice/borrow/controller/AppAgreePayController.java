package com.finace.miscroservice.borrow.controller;


import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.rpc.ActivityRpcService;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.BorrowService;
import com.finace.miscroservice.borrow.service.Contract.ContractService;
import com.finace.miscroservice.borrow.service.FinanceBidService;
import com.finace.miscroservice.borrow.service.FuiouH5PayService;
import com.finace.miscroservice.borrow.service.SaleService;
import com.finace.miscroservice.borrow.service.fuiou.FuiouAgreePayService;
import com.finace.miscroservice.borrow.service.fuiou.FuiouBinCardQueryService;
import com.finace.miscroservice.borrow.service.fuiou.FuiouPayService;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.entity.UserBankCard;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import com.finace.miscroservice.commons.utils.tools.TextUtil;
import com.finace.miscroservice.commons.utils.tools.TokenProcessor;
import com.fuiou.util.MD5;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 购买模块主要接口:h5跳转到购买界面, h5富有购买
 */
@RestController
@Configuration
@RefreshScope
@RequestMapping(value = "/agree")
public class AppAgreePayController extends BaseController {

    private Log logger = Log.getInstance(AppAgreePayController.class);

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private FinanceBidService financeBidService;

    @Autowired
    private UserRpcService userRpcService;

    @Autowired
    private ActivityRpcService activityRpcService;

    @Autowired
    private FuiouH5PayService fuiouH5PayService;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    private SaleService saleService;

    @Autowired
    private FuiouBinCardQueryService fuiouBinCardQueryService;

    @Autowired
    private FuiouPayService fuiouPayService;

    @Value("${borrow.pay.success.url}")
    private String paySuccessUrl;

    @Value("${borrow.pay.error.url}")
    private String payErrorUrl;

    @Value("${borrow.pay.timeout}")
    private String timeout;

    @Value("${borrow.pay.go.fuiou.html}")
    private String goToFuiouHtml;

    @Autowired
    private ContractService contractService;

    @Autowired
    private FuiouAgreePayService fuiouAgreePayService;


    /**
     * 协议支付绑定银行卡发送短信
     *
     * @param name     姓名
     * @param bankCard 卡号
     * @param idCard   身份证号
     * @param phone    预留手机号
     * @return
     */
    @RequestMapping("bindCardSendMsg/auth")
    public Response bindCardSendMsg(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "bankCard", required = false) String bankCard,
                                    @RequestParam(value = "idCard", required = false) String idCard,
                                    @RequestParam(value = "phone", required = false) String phone) throws UnsupportedEncodingException {


        String userId = getUserId();
        logger.info("用户{}绑卡发送短信。name={},bankCard={},idCard={},phone={}", userId, name, bankCard, idCard, phone);
        if (StringUtils.hasEmpty(name, bankCard, idCard, phone)) {
            logger.warn("用户{}绑卡发送短信，数据为空name={},bankCard={},idCard={},phone={}", userId, name, bankCard, idCard, phone);
            return Response.errorMsg("参数为空");
        }

        String pno = fuiouAgreePayService.queryBindCard(userId);
        if (pno != null) {
            logger.warn("用户{}绑定银行卡，查询是否已经绑卡pno={}", userId, pno);
            return Response.errorMsg("用户已经绑卡");
        }

        String isSuccess = fuiouAgreePayService.sendBindMsg(userId, name, bankCard, idCard, phone);
        if ("success".equals(isSuccess)) {
            return Response.successMsg("短信发送成功");
        } else {
            return Response.errorMsg(isSuccess);
        }

    }


    /**
     * 绑卡接口
     *
     * @param name     开户名称
     * @param bankCard 银行卡号
     * @param idCard   身份证号
     * @param phone    预留手机号
     * @param msgCode  验证码
     * @return
     */
    @RequestMapping("bindCard/auth")
    public Response bindCard(@RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "bankCard", required = false) String bankCard,
                             @RequestParam(value = "idCard", required = false) String idCard,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "msgCode", required = false) String msgCode) throws UnsupportedEncodingException {

//        if (name != null && Charset.forName("iso-8859-1").newEncoder().canEncode(name)) {
//            name = new String(name.getBytes("iso8859-1"), "utf-8");
//        }
        String userId = getUserId();
        logger.info("用户{}开始绑卡。name={},bankCard={},idCard={},phone={},msgCode={}", userId, name, bankCard, idCard, phone, msgCode);
        if (StringUtils.hasEmpty(name, bankCard, idCard, phone)) {
            logger.warn("用户{}绑卡，数据为空name={},bankCard={},idCard={},phone={}, msgCode={}", userId, name, bankCard, idCard, phone, msgCode);
            return Response.errorMsg("参数为空");
        }


        FuiouAgreePayService.QueryCardBinResult rs = fuiouAgreePayService.queryCardBin(bankCard);
        if (rs != null && rs.getRdesc() != null) {
            logger.warn("用户{}绑定银行卡，查询银行卡是否支持rdesc={}", userId, rs.getRdesc());
            return Response.errorMsg(rs.getRdesc());
        }

        String pno = fuiouAgreePayService.queryBindCard(userId);
        if (pno != null) {
            logger.warn("用户{}绑定银行卡，查询是否已经绑卡pno={}", userId, pno);
            return Response.errorMsg("用户已经绑卡");
        }

        String protocolNo[] = fuiouAgreePayService.bindCard(userId, name, bankCard, idCard, phone, msgCode);
        if (protocolNo[0] != null && "0000".equals(protocolNo[0])) {

            UserBankCard existBankCardList = userRpcService.getAgreeBaknCardByCard(bankCard);
            // 绑卡
            if (null == existBankCardList) {
                UserBankCard card = new UserBankCard();
                card.setUserId(Integer.valueOf(userId));
                card.setStatus("agreeEnable");
                card.setPid(idCard);
                card.setName(name);
                card.setBankCard(bankCard);
                card.setPhone(phone);

                if (rs != null) {
                    card.setBankName(rs.getCnm());
                    card.setInscd(rs.getInsCd());
                }
                card.setProtocolno(protocolNo[1]);  //协议号
                userRpcService.addUserBankCard(card);
            }
            logger.info("用户{}，绑卡成功bankCard={}，protocolno={}", bankCard, protocolNo);
            return Response.successMsg("绑卡成功");
        } else {
            return Response.errorMsg(protocolNo[1]);
        }

    }


    /**
     * 跳转到购买界面
     *
     * @param borrowId  标的id
     * @param channel   渠道
     * @param os        系统标识
     * @param mac       mac地址
     * @param androidid androidid
     * @param imei      设备识别号
     *                  <p>
     *                  {
     *                  "msg": "",
     *                  "code": 200,
     *                  "data": {
     *                  "mostAccount": "0",  最大投资金额限制 0表示无限制
     *                  "lowestAccount": "100", 最小投资金额
     *                  "os": null,
     *                  "channel": null, 渠道
     *                  "remmoney": 10000, 可投资金额
     *                  "imei": null, 设备型号
     *                  "borrowId": 10636, 标的id
     *                  "shortCard": "4317", 银行卡号后四位
     *                  "bankCardName": "中国农业银行", 银行名称
     *                  "mac": null,
     *                  "androidid": null
     *                  "bankCard": "", 完整银行卡号
     *                  "apr": 10, 利率
     *                  "day": 90, 项目天数
     *                  "ymoney": "5",  每笔限额
     *                  "mmoney": "100", 每月限额
     *                  "dmoney": "10", 每天限额
     *                  }
     *                  }
     * @return
     */
    @RequestMapping("toBorrowOrder/auth")
    public Response toBorrowOrder(@RequestParam(value = "borrowId", required = false) Integer borrowId,
                                  @RequestParam(value = "channel", required = false) String channel,
                                  @RequestParam(value = "os", required = false) String os,
                                  @RequestParam(value = "mac", required = false) String mac,
                                  @RequestParam(value = "androidid", required = false) String androidid,
                                  @RequestParam(value = "imei", required = false) String imei) {

        String userId = getUserId();
        logger.info("用户{}访问购买界面", userId);

        if (borrowId == null) {
            logger.warn("用户{}访问购买界面,标的{}信息不存在", userId, borrowId);
            return Response.errorMsg("标的信息不存在");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("borrowId", borrowId);

        BorrowPO borrow = borrowService.getBorrowById(borrowId);
        if (null == borrow) {
            logger.warn("用户{}访问购买界面,标的{}信息不存在", userId, borrowId);
            return Response.errorMsg("标的信息不存在");
        }
        map.put("remmoney", borrow.getRemmoney());
        map.put("apr", borrow.getApr());
        map.put("day", borrow.getTimeLimitDay());
        map.put("lowestAccount", borrow.getLowestAccount());
        map.put("mostAccount", borrow.getMostAccount());
        map.put("channel", channel);

        UserBankCard userBankCard = userRpcService.getAgreeEnableCardByUserId(userId);
        if (userBankCard != null && StringUtil.isNotEmpty(userBankCard.getBankCard())) {
            logger.info("用户{}有银行卡{}信息", userId, userBankCard.getBankCard());
            map.put("shortCard", TextUtil.hideCardNo(userBankCard.getBankCard()));
            map.put("bankCardName", userBankCard.getBankName());
        }

        map.put("os", os);
        map.put("mac", mac);
        map.put("androidid", androidid);
        map.put("imei", imei);

        String usehbToken = TokenProcessor.getInstance().getHbTokeCode(userId + "", borrowId + "");
        userStrHashRedisTemplate.set(Constant.USER_TOKEN + userId, usehbToken);
        map.put("usehbToken", usehbToken);
        logger.info("用户{}生成购买token={}", userId, usehbToken);
        if (0 == Integer.valueOf(borrow.getUse())) {
            if (financeBidService.getAllFinaceByUserId(Integer.valueOf(userId)) > 0) {
                logger.warn("对不起,该标只允许新手投资!");
                return Response.error(406, "对不起,该标只允许新手投资!");
            }
        }

        map.put("name", TextUtil.hideRealnameChar(userBankCard.getName()));
        map.put("phone", TextUtil.hidePhoneNo(userBankCard.getPhone()));

        return Response.successByMap(map);
    }

    /**
     * h5富有购买
     *
     * @param amt      购买金额
     * @param borrowId 标的id
     * @param channel  渠道
     * @param hbid     福利券id
     * @param imei     设备id
     *
     *                 <p>
     *                 {
     *                 "msg": "",
     *                 "code": 200,
     *                 "data": "" 这个里面的数据显示在一个空白界面
     *                 }
     * @return
     */
    @RequestMapping(value = "postFuiouPay/auth")
    public Response postFuiouPay(HttpServletRequest request, @RequestParam("amt") String amt,
                                 @RequestParam("borrowId") Integer borrowId,
                                 @RequestParam(value = "channel", required = false) String channel,
                                 @RequestParam(value = "hbid", required = false) String hbid,
                                 @RequestParam(value = "imei", required = false) String imei,
                                 @RequestParam(value = "version", required = false) String version,
                                 @RequestParam(value = "code") String code) {

        String userId = getUserId();

        logger.info("用户{}开始购买{}标的。amt={},channel={}, hbid={}", userId, borrowId, amt, channel, hbid);
        if (StringUtils.isEmpty(code)) {
            logger.warn("验证码为空");
            return Response.errorMsg("验证码为空");
        }
        UserBankCard userBankCard = userRpcService.getAgreeEnableCardByUserId(userId);
        String co = null;
        if (userBankCard != null) {
            co = userStrHashRedisTemplate.get(userBankCard.getPhone());
        }
        if (StringUtils.isEmpty(co) || !code.equals(co)) {
            logger.warn("用户={} 购买标的验证码错误", userBankCard.getPhone());
            return Response.errorMsg("手机验证码错误");
        }
        if (StringUtils.hasEmpty(amt, channel)) {
            logger.warn("用户{}购买{}标的。参数错误", userId, borrowId);
            return Response.errorMsg("参数错误");
        }

        BigDecimal buyAmt = BigDecimal.valueOf(Double.parseDouble(amt));
        if (buyAmt.compareTo(BigDecimal.ZERO) <= 0 || buyAmt.compareTo(BigDecimal.valueOf(100000)) > 0) {
            logger.warn("用户{}购买{}标的。单笔不超过10万元!", userId, borrowId);
            return Response.errorMsg("单笔不超过10万元!");
        }

        User user = userRpcService.getUserByUserId(String.valueOf(userId));
        if (null == user) {
            logger.warn("用户{}购买{}标的。用户不存在!", userId, borrowId);
            return Response.errorMsg("用户不存在!");
        }

        BorrowPO borrow = borrowService.getBorrowById(borrowId);
        if (null == borrow) {
            logger.warn("用户{}购买{}标的。标的不存在!", userId, borrowId);
            return Response.errorMsg("标的不存在!");
        }

        // 判断可投金额小于最小额度限制时 最小额度不做判断 account - acccount_yes > lowestAccount
        if (borrow.getRemmoney() < Double.valueOf(borrow.getLowestAccount())) {
            logger.warn("用户{}购买{}标的。剩余金额不足，请投其他标的!", userId, borrowId);
            return Response.errorMsg("剩余金额不足，请投其他标的!");
        }

        if (Double.valueOf(amt) < Double.valueOf(borrow.getLowestAccount())) {
            logger.warn("用户{}购买{}标的。购买金额不能小于起投金额!", userId, borrowId);
            return Response.errorMsg("购买金额不能小于起投金额!");
        }

        if (Double.valueOf(amt) > Double.valueOf(borrow.getRemmoney())) {
            logger.warn("用户{}购买{}标的。超过可购金额!", userId, borrowId);
            return Response.errorMsg("超过可购金额!");
        }

        if (Double.valueOf(borrow.getMostAccount()) != 0 && Double.valueOf(amt) > Double.valueOf(borrow.getMostAccount())) {
            logger.warn("用户{}购买{}标的。超过限额!", userId, borrowId);
            return Response.errorMsg("超过限额!");
        }

        //0--富有1--我们自己
        if ("0".equals(goToFuiouHtml) || "undefined".equals(version)) {
            String rmoney = userStrHashRedisTemplate.get(Constant.PURCHASE_AMT + borrowId) != null ? userStrHashRedisTemplate.get(Constant.PURCHASE_AMT + borrowId) : borrow.getRemmoney().toString();
            if (Double.valueOf(rmoney) - Double.valueOf(amt) < 0) {
                String oData = financeBidService.getFinanceBidByDesc(borrowId, timeout);
                //判断上次的购买超时后是否解冻资金
                if (oData != null) {
                    //关闭订单
                    financeBidService.closeOrder(oData, goToFuiouHtml, version);
                }
                logger.warn("用户{}购买{}标的。该标的剩余金额已有其他用户正在购买，请选择其他标的购买!", userId, borrowId);
                return Response.errorMsg("该标的剩余金额已有其他用户正在购买，请选择其他标的购买或稍后再试!");
            }

            //保存可购金额
            String canPay = String.valueOf(Double.valueOf(rmoney) - Double.valueOf(amt));
            logger.info("222============PostFuiouPay,canPay={},rmoney={},amt={}", canPay, rmoney, amt);
            userStrHashRedisTemplate.set(Constant.PURCHASE_AMT + borrowId, String.valueOf(Double.valueOf(rmoney) - Double.valueOf(amt)));
        }


//        UserBankCard userBankCard = userRpcService.getAgreeEnableCardByUserId(userId);

        if (userBankCard == null) {
            logger.warn("用户{}没有绑定银行卡", userId);
            return Response.errorMsg("对不起,没有绑定银行卡!");
        }

        SaleService.Param orderParam = new SaleService.Param();
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
            if (Double.valueOf(userRedPackets.getSmoney()) > buyAmt.doubleValue()) {
                logger.warn("用户{}购买{}标的。该投资金额不能使用该福利卷!", userId, borrowId);
                return Response.errorMsg("该投资金额不能使用该福利卷!");
            }

            orderParam.setHbid(hbid);
        }

        orderParam.setUserId(Integer.valueOf(userId));
        orderParam.setBorrowId(borrowId);
        orderParam.setBuyAmt(buyAmt);
        orderParam.setBankCard(userBankCard.getBankCard());
        orderParam.setName(userBankCard.getName());
        orderParam.setPid(userBankCard.getPid());
        orderParam.setApr(BigDecimal.valueOf(borrow.getApr()));

        if (channel != null) {
            orderParam.setRegChannel(channel);
        }

        if (imei != null) {
            orderParam.setChannel(imei);
        }

        try {

            String userIp = Iptools.gainRealIp(request);
            Map resultMap = new HashMap<>();
            logger.info("用户{}购买{}标的。开始创建订单!", userId, borrowId);
            FinanceBidPO order = saleService.makeAgreeOrder(orderParam, version);
            logger.info("用户{}购买{}标的。创建订单成功，开始支付!", userId, borrowId);

            FuiouH5PayService.Param param = new FuiouH5PayService.Param();
            param.setUserId(userId);
            param.setAmt(buyAmt);
            param.setBankCard(userBankCard.getBankCard());
            param.setPid(userBankCard.getPid());
            param.setName(userBankCard.getName());
            param.setOrderId(order.getOrderSn());

            FuiouAgreePayService.OrderPayResult orderPayResult = fuiouAgreePayService.orderPay(amt, userId, userBankCard.getProtocolno(), userIp, order.getOrderSn());
            if (orderPayResult != null) {
                resultMap.put("orderId", String.valueOf(order.getOrderSn()));
                resultMap.put("amt", buyAmt);
                resultMap.put("apr", borrow.getApr());
                resultMap.put("timeLimitDay", borrow.getTimeLimitDay());
                resultMap.put("borrowName", borrow.getName());
                resultMap.put("rdesc", orderPayResult.getRdesc());

                return Response.success(resultMap);
            }

//            logger.info("用户{}购买{}标的。购买成功,待支付!订单号{}", userId, borrowId, order.getOrderSn());
//            Map<String, String> resultMap = new HashMap<>();
//            resultMap.put("payForm", payForm);
//            resultMap.put("timeout", timeout);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("购买支付失败,异常信息：{}", e);
        }

        return Response.errorMsg("购买支付失败");
    }


    @Value("${borrow.fuiou.fcds}")
    private String fcd;

    @Value("${borrow.fuiou.fkeys}")
    private String fkey;

    /**
     * 支付业务处理回调
     *
     * @return
     */
    @RequestMapping(value = "fuiouPayBack")
    public void fuiouPayBack() {
        String version = request.getParameter("VERSION");
        String type = request.getParameter("TYPE");

        String rcode = request.getParameter("RESPONSECODE");
        String rdesc = request.getParameter("RESPONSEMSG");
        String mchntCd = request.getParameter("MCHNTCD");
        String rorderId = request.getParameter("MCHNTORDERID");
        String fuiouOrderId = request.getParameter("ORDERID");
        String uId = request.getParameter("USERID");
        String pNo = request.getParameter("PROTOCOLNO");
        String cardNo = request.getParameter("BANKCARD");
        String ramt = request.getParameter("AMT");
        String rsign = request.getParameter("SIGN");

        logger.info("富有协议支付回调,返回参数:rcode={},rdesc={},mchntCd={},rorderId={},fuiouOrderId={},cardNo={},ramt={},uId={},sign={}", rcode, rdesc, mchntCd, rorderId, fuiouOrderId, cardNo, ramt, uId, MD5.MD5Encode("03|1.0|" + rcode + "|" + fcd + "|" + rorderId + "|" + fuiouOrderId + "|" + ramt + "|" + cardNo + "|" + fkey));

        // 校验签名
        if (rsign.equals(MD5.MD5Encode("03|1.0|" + rcode + "|" + fcd + "|" + rorderId + "|" + fuiouOrderId + "|" + ramt + "|" + cardNo + "|" + fkey))) {
            // 校验签名
            if (rcode != null && "0000".equals(rcode)) {
                try {
                    FuiouH5PayService.PayCallBackResult result = new FuiouH5PayService.PayCallBackResult();
                    result.setVersion(version);
                    result.setType(type);
                    result.setResponseCode(rcode);
                    result.setResponseMsg(rdesc);
                    result.setMchntCd(mchntCd);
                    result.setMchntOrderId(rorderId);
                    result.setOrderId(fuiouOrderId);
                    result.setBankCard(cardNo);
                    result.setAmt(ramt);
                    result.setSign(rsign);

//                    if (!fuiouH5PayService.isFromFuiouServer(result)) {
//                        logger.warn(String.format("伪装支付回调:%s", result.toString()));
//                        return;
//                    }
//                    if (!fuiouH5PayService.isPaySuccess(result)) {
//                        logger.warn(String.format("支付失败:%s", result.toString()));
//                        return;
//                    }

                    if (null == result) {
                        logger.warn(String.format("结果为空:%s", result.toString()));
                        return;
                    }

                    if (null == result.getOrderId()) {
                        logger.warn(String.format("订单ID为空:%s", result.toString()));
                        return;
                    }

                    saleService.onAgreeFuiouPaySuccess(result);  //支付成功逻辑处理

                    logger.info("富有协议支付回调成功mchntOrderId={}", rorderId);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("富有协议失败,responseCode={},异常信息：{}", rcode, e);
                }
            }
        }

    }


    /**
     * 购买判断是否可以支付
     *
     * @param borrowId
     * @param amt
     * @return
     */
    @RequestMapping(value = "canBePay/auth")
    public Response canBePay(@RequestParam(value = "borrowId", required = false) String borrowId,
                             @RequestParam(value = "amt", required = false) String amt) {
        String userId = getUserId();
        logger.info("用户{}购买支付，判读是否可以支付borrowId={}", userId, borrowId);

        if (StringUtils.isEmpty(borrowId)) {
            logger.warn("用户{}购买支付，判读是否可以支付,borrowId={}为空", userId, borrowId);
            return Response.errorMsg("标的id为空");
        }

        if (StringUtils.isEmpty(amt)) {
            logger.warn("用户{}购买支付，判读是否可以支付,amt={}为空", userId, amt);
            return Response.errorMsg("购买金额不能为空");
        }

        Double buyAmt = Double.valueOf(amt);
        if (buyAmt <= 0) {
            logger.warn("用户{}购买支付，判读是否可以支付,buyAmt={}必须大于0", userId, buyAmt);
            return Response.errorMsg("购买金额必须大于0");
        }


        return financeBidService.agreeCanBePay(userId, borrowId, buyAmt);
    }


    /**
     * 判断协议支付是否成功
     *
     * @return
     */
    @RequestMapping(value = "fuiouPaySuccess")
    public Response fuiouPaySuccess(String orderId) {
        logger.info("查询订单号{}协议支付是否成功", orderId);
        String isSuccess = fuiouAgreePayService.checkOrder(orderId);
        if ("success".equals(isSuccess)) {

            return Response.success();
        }

        return Response.error();
    }


    /**
     * 界面处理回调
     *
     * @return
     */
    @RequestMapping(value = "callbackFuiouHtml")
    public void callbackFuiouHtml() throws Exception {
        String mchntOrderId = request.getParameter("MCHNTORDERID");
        String orderId = request.getParameter("ORDERID");
        String responseCode = request.getParameter("RESPONSECODE");
        String responseMsg = request.getParameter("RESPONSEMSG");
        String bankCard = request.getParameter("BANKCARD");
        String amt = request.getParameter("AMT");

        // FinanceBidPO financeBidPO = financeBidService.getFidByOrderId(mchntOrderId);

        if ("0000".equals(responseCode)) {
            logger.info("订单{}支付成功,富有支付回调到h5界面,paySuccessUrl={}", mchntOrderId, paySuccessUrl);

            response.get().sendRedirect(paySuccessUrl + "?amt=" + (amt != null ? Double.valueOf(amt) / 100 : 0));

        } else {
            logger.warn("callbackFuiouHtml,订单{}支付失败,富有支付回调到h5界面,amt={},bankCard={},orderId={},responseCode={},responseMsg={},payErrorUrl={}", mchntOrderId, amt, bankCard, orderId, responseCode, responseMsg, payErrorUrl);
            response.get().sendRedirect(payErrorUrl + "?responseMsg=" + (responseMsg != null ? responseMsg : ""));
        }
    }

    /**
     * h5获取支付成功界面数据
     *
     * @param orderId 订单号
     */
    @RequestMapping(value = "getFuiouHtmlData")
    public Response getFuiouHtmlData(@RequestParam("orderId") String orderId) {
        FinanceBidPO financeBidPO = financeBidService.getFidByOrderId(orderId);
        if (null != financeBidPO) {
            Map<String, Object> map = new HashMap<>();
            map.put("order", financeBidPO.getOrderSn());
            map.put("amt", financeBidPO.getBuyAmt());
            map.put("apr", financeBidPO.getRate());
            map.put("interest", financeBidPO.getInterest());
            map.put("dqr", DateUtils.dateStr2(financeBidPO.getEndProfit()));
            return Response.success(map);
        }
        return Response.error();
    }


    /**
     * 业务处理回调
     *
     * @return
     */
    @RequestMapping(value = "callbackFuiou")
    public void callbackFuiou() throws Exception {
        logger.info("富有支付回调开始");

        Map<String, Object> map = new HashMap<>();
        String version = request.getParameter("VERSION");
        String type = request.getParameter("TYPE");
        String responseCode = request.getParameter("RESPONSECODE");
        String responseMsg = request.getParameter("RESPONSEMSG");
        String mchntCd = request.getParameter("MCHNTCD");
        String mchntOrderId = request.getParameter("MCHNTORDERID");
        String orderId = request.getParameter("ORDERID");
        String bankCard = request.getParameter("BANKCARD");
        String amt = request.getParameter("AMT");
        String sign = request.getParameter("SIGN");

        if ("0000".equals(responseCode)) {
            logger.info("订单{}支付成功,富有支付回调", mchntOrderId);
        } else {
            FinanceBidPO financeBidPO = financeBidService.getFidByOrderId(mchntOrderId);
            //若该订单已处理过 为成功的 直接return
            if (financeBidPO.getPay() == 1) {
                return;
            }
            String borrowId = String.valueOf(financeBidPO.getBorrowId());
            String rmoney = userStrHashRedisTemplate.get(Constant.NEW_PURCHASE_AMT + borrowId);
            logger.info("标的{},订单{}可购买金额已恢复,rmoney={},amt={}", borrowId, mchntOrderId, rmoney, Double.valueOf(amt) / 100);
            if (null != rmoney) {
                userStrHashRedisTemplate.set(Constant.NEW_PURCHASE_AMT + borrowId, String.valueOf(Double.valueOf(rmoney) + Double.valueOf(amt) / 100));
            }

            //关闭我们自己订单
            Map<String, Object> omap = new HashMap<>();
            omap.put("pay", -2);
            omap.put("orderId", mchntOrderId);
            financeBidService.updatePayFinanceBidByOrderId(omap);

            logger.warn("callbackFuiou,订单{}支付失败,富有支付回调到h5界面,orderId={},responseCode={},responseMsg={},payErrorUrl={}", mchntOrderId, orderId, responseCode, responseMsg, payErrorUrl);
            return;
        }

        logger.info("富有支付回调,返回参数:responseCode={},mchntCd={},mchntOrderId={},orderId={},bankCard={},amt={}", responseCode, mchntCd, mchntOrderId, orderId, bankCard, amt);
        try {
            FuiouH5PayService.PayCallBackResult result = new FuiouH5PayService.PayCallBackResult();
            result.setVersion(version);
            result.setType(type);
            result.setResponseCode(responseCode);
            result.setResponseMsg(responseMsg);
            result.setMchntCd(mchntCd);
            result.setMchntOrderId(mchntOrderId);
            result.setOrderId(orderId);
            result.setBankCard(bankCard);
            result.setAmt(amt);
            result.setSign(sign);

            if (!fuiouH5PayService.isFromFuiouServer(result)) {
                logger.warn(String.format("伪装支付回调:%s", result.toString()));
                return;
            }
            if (!fuiouH5PayService.isPaySuccess(result)) {
                logger.warn(String.format("支付失败:%s", result.toString()));
                return;
            }
            if (null == result) {
                logger.warn(String.format("结果为空:%s", result.toString()));
                return;
            }

            if (null == result.getOrderId()) {
                logger.warn(String.format("订单ID为空:%s", result.toString()));
                return;
            }

            saleService.onFuiouPaySuccess(result);  //支付成功逻辑处理

            logger.info("富有支付回调成功mchntOrderId={}", mchntOrderId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("支付失败,responseCode={},异常信息：{}", responseCode, e);
        }
    }


}
