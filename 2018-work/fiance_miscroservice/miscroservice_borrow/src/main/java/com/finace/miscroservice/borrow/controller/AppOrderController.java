package com.finace.miscroservice.borrow.controller;


import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.rpc.ActivityRpcService;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.BorrowService;
import com.finace.miscroservice.borrow.service.Contract.ContractService;
import com.finace.miscroservice.borrow.service.FinanceBidService;
import com.finace.miscroservice.borrow.service.FuiouH5PayService;
import com.finace.miscroservice.borrow.service.SaleService;
import com.finace.miscroservice.borrow.service.fuiou.FuiouBinCardQueryService;
import com.finace.miscroservice.borrow.service.fuiou.FuiouPayService;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.TimerScheduler;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.entity.UserBankCard;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.Regular;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import com.finace.miscroservice.commons.utils.tools.TokenProcessor;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 购买模块主要接口:h5跳转到购买界面, h5富有购买
 */
@RestController
@Configuration
@RefreshScope
public class AppOrderController extends BaseController {

    private Log logger = Log.getInstance(AppOrderController.class);

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

        if( borrowId == null ){
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

        UserBankCard userBankCard = userRpcService.getUserBankCardByUserId(userId);
        if (userBankCard != null && StringUtil.isNotEmpty(userBankCard.getBankCard())) {
            logger.info("用户{}有银行卡{}信息", userId, userBankCard.getBankCard());

            map.put("shortCard", userBankCard.getBankCard().substring(userBankCard.getBankCard().length() - 4, userBankCard.getBankCard().length()));
            map.put("bankCardName", userBankCard.getBankName());
            map.put("bankCard", userBankCard.getBankCard());
            logger.info("用户{}银行卡信息,银行卡名称={},银行卡卡号={}", userId, userBankCard.getBankName(), userBankCard.getBankCard());
            if (null != userBankCard.getInscd()) {
                logger.info("开始查询银行卡限额{}", userBankCard.getInscd());
                FuiouBinCardQueryService.AmtResultLimit amtResultLimit = fuiouBinCardQueryService.queryLimitByCard(userBankCard.getInscd());
                logger.info("结束查询银行卡限额{}", userBankCard.getInscd());
                if (null != amtResultLimit) {
                    map.put("ymoney", NumberUtil.round(Double.valueOf(amtResultLimit.getAmtlimittime()) / 1000000, 0));  //单笔限额
                    map.put("dmoney", NumberUtil.round(Double.valueOf(amtResultLimit.getAmtlimitday()) / 1000000, 0));  //每天限额
                    map.put("mmoney", "100"); //每月限额
                }
            }
        } else {
            map.put("shortCard", "");
            map.put("bankCardName", "");
            map.put("bankCard", "");
            map.put("ymoney", "");  //单笔限额
            map.put("dmoney", "");  //每天限额
            map.put("mmoney", ""); //每月限额
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

        return Response.successByMap(map);
    }

    /**
     * h5富有购买
     *
     * @param amt        购买金额
     * @param borrowId   标的id
     * @param usehbToken 请求token
     * @param channel    渠道
     * @param name       用户姓名
     * @param hbid       福利券id
     * @param pid        用户身份证号码
     * @param bankCard   银行号码
     * @param imei       设备id
     *                   <p>
     *                   {
     *                   "msg": "",
     *                   "code": 200,
     *                   "data": "" 这个里面的数据显示在一个空白界面
     *                   }
     * @return
     */
    @RequestMapping(value = "postFuiouPay/auth")
    public Response postFuiouPay(@RequestParam("amt") String amt,
                                 @RequestParam("borrowId") int borrowId,
                                 @RequestParam(value = "usehbToken", required = false) String usehbToken,
                                 @RequestParam(value = "channel", required = false) String channel,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "hbid", required = false) String hbid,
                                 @RequestParam(value = "pid", required = false) String pid,
                                 @RequestParam(value = "bankCard", required = false) String bankCard,
                                 @RequestParam(value = "imei", required = false) String imei,
                                 @RequestParam(value = "version", required = false) String version) {

        String userId = getUserId();

        logger.info("用户{}开始购买{}标的。name={},amt={},channel={}", userId, borrowId, name, amt, channel);


        if (StringUtils.hasEmpty(amt, channel)) {
            logger.warn("用户{}购买{}标的。参数错误", userId, borrowId);
            return Response.errorMsg("参数错误");
        }

        if (null != name && !Regular.checkNameMatch(name)) {
            logger.warn("用户{}姓名{}有误", userId, name);
            return Response.errorMsg("用户姓名错误");
        }

        if (StringUtil.isNotEmpty(pid) && pid.length() > 18) {
            logger.warn("用户{}购买{}标的。身份证号码不对", userId, borrowId);
            return Response.errorMsg("身份证号码不对");
        }

        /*String userPayToken = userStrHashRedisTemplate.get(Constant.USER_TOKEN + userId);
        logger.info("用户{}购买token={}", userId, userPayToken);
        if (!usehbToken.equals(userPayToken)) {
            logger.warn("用户{}购买{}标的。订单不能重复提交!", userId, borrowId);
            return Response.errorMsg("订单不能重复提交!");
        }*/

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
        if( "0".equals(goToFuiouHtml) ||  "undefined".equals(version)){
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
            userStrHashRedisTemplate.set(Constant.PURCHASE_AMT + borrowId, String.valueOf(Double.valueOf(rmoney) - Double.valueOf(amt)));
        }


        UserBankCard userBankCard = userRpcService.getUserBankCardByCard(bankCard);
        if (userBankCard != null && userId.equals(String.valueOf(userBankCard.getUserId()))) {
            bankCard = userBankCard.getBankCard();
            name = userBankCard.getName();
            pid = userBankCard.getPid();
        }


        if (StringUtils.hasEmpty(bankCard, name, pid)) {
            logger.warn("用户{}购买{}标的。购买信息有误!", userId, borrowId);
            return Response.errorMsg("购买信息有误!");
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
        orderParam.setBankCard(bankCard);
        orderParam.setName(name);
        orderParam.setPid(pid.toUpperCase());
        orderParam.setApr(BigDecimal.valueOf(borrow.getApr()));

        if (channel != null) {
            orderParam.setRegChannel(channel);
        }

        if (imei != null) {
            orderParam.setChannel(imei);
        }

        try {

            logger.info("用户{}购买{}标的。开始创建订单!", userId, borrowId);
            FinanceBidPO order = saleService.makeOrder(orderParam,version);
            logger.info("用户{}购买{}标的。创建订单成功，开始支付!", userId, borrowId);

            FuiouH5PayService.Param param = new FuiouH5PayService.Param();
            param.setUserId(userId);
            param.setAmt(buyAmt);
            param.setBankCard(bankCard);
            param.setPid(pid.toUpperCase());
            param.setName(name);
            param.setOrderId(order.getOrderSn());

            String payForm = fuiouH5PayService.makePayForm(param);

            logger.info("用户{}购买{}标的。购买成功,待支付!订单号{}", userId, borrowId, order.getOrderSn());
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("payForm", payForm);
            resultMap.put("timeout", timeout);
            resultMap.put("orderId", String.valueOf(order.getOrderSn()));


            return Response.success(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("购买支付失败,异常信息：{}", e);
        }

        return Response.errorMsg("购买支付失败");
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
            if (financeBidPO.getPay()==1){
                return;
            }
            String borrowId = String.valueOf(financeBidPO.getBorrowId());
            String rmoney = userStrHashRedisTemplate.get(Constant.NEW_PURCHASE_AMT + borrowId);
            logger.info("标的{},订单{}可购买金额已恢复,rmoney={},amt={}",borrowId, mchntOrderId, rmoney, Double.valueOf(amt)/100 );
            if (null != rmoney) {
                userStrHashRedisTemplate.set(Constant.NEW_PURCHASE_AMT + borrowId, String.valueOf(Double.valueOf(rmoney) + Double.valueOf(amt)/100));
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


    /**
     * 获取云合同的合同文件流
     *
     * @param yhttoken   云合同token
     * @param contractId 云合同合同id
     * @return
     */
    @RequestMapping(value = "getContract/auth")
    public void getContract(@RequestParam(value = "yhttoken") String yhttoken,
                            @RequestParam(value = "contractId") String contractId) {
        String userId = getUserId();
        logger.info("用户{}访问合同{}", userId, contractId);
        ServletOutputStream out = null;

        try {
            BufferedInputStream input = new BufferedInputStream(contractService.download(yhttoken, contractId));
            byte buffBytes[] = new byte[1024];
            out = response.get().getOutputStream();
            int read = 0;
            while ((read = input.read(buffBytes)) != -1) {
                out.write(buffBytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("获取pdf流，异常信息:{}", e);
            //关闭流
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ie) {
                    logger.error("输入流关闭错误", ie);
                }
            }
        }

    }


    /**
     * 客户端关闭订单
     *
     * @param orderId 投资记录id
     * @return
     */
    @RequestMapping(value = "closeOrder/auth")
    public Response closeOrder(@RequestParam(value = "orderId") String orderId) {
        String userId = getUserId();
        logger.info("用户{}订单{}关闭开始", userId, orderId);
        //判断是否是用户订单
        if (!orderId.contains(userId)) {
            return Response.error();
        }

        String msg = financeBidService.closeOrder(orderId, goToFuiouHtml,"");
        logger.info("用户{}订单{}关闭开始,msg={}", userId, orderId, msg);
        if ("success".equals(msg)) {
            return Response.success();
        }

        return Response.error();
    }

    /**
     * 判断是否跳转到富有H5
     *
     * <p>
     * {
     * "msg": "",
     * "code": 200,
     * "data": "0"   0--富有1--我们自己
     * }
     *
     * @return
     */
    @RequestMapping(value = "goToFuiouHtml")
    public Response goToFuiouHtml() {
        logger.info("判断支付界面是富有还是我们自己goToFuiouHtml={},0--富有1--我们自己", goToFuiouHtml);
        return Response.success(goToFuiouHtml);
    }

    /**
     * 富有支付发送短信
     *
     * @param reqXml 请求xml
     * @return
     */
    @RequestMapping(value = "sendFuiouMessage/auth")
    public Response sendFuiouMessage(@RequestParam(value = "reqXml", required = false) String reqXml) {
        String userId = getUserId();
        logger.info("用户{},开始发送富有支付短信,reqXml={}", userId, reqXml);
        String result = fuiouPayService.sendFuiouMessage(reqXml);
        if (null == result) {
            return Response.error();
        }
        return Response.success(result);
    }


    /**
     * 富有支付接口
     *
     * @param reqXml 请求xml
     * @return
     */
    @RequestMapping(value = "doFuiouPay/auth")
    public Response doFuiouPay(@RequestParam(value = "reqXml", required = false) String reqXml) {
        String userId = getUserId();
        logger.info("用户{},富有支付接口,reqXml={}", userId, reqXml);

        String result = fuiouPayService.doFuiouPay(reqXml);
        if (null == result) {
            return Response.error();
        }
        return Response.success(result);
    }



    /**
     * 购买判断是否可以支付
     *
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(value = "canBePay/auth")
    public Response canBePay(@RequestParam(value = "orderId", required = false) String orderId) {
        String userId = getUserId();
        logger.info("用户{}购买支付，判读是否可以支付orderId={}", userId, orderId);

        if (orderId == null) {
            logger.warn("用户{}购买支付，判读是否可以支付,orderId={}为空", userId, orderId);
            return Response.errorMsg("订单id为空");
        }

        return financeBidService.canBePay(userId, orderId);
    }


    /**
     * 恢复可购买金额
     *
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(value = "recoveryAmt/auth")
    public Response recoveryAmt(@RequestParam(value = "orderId", required = false) String orderId) {
        String userId = getUserId();
        logger.info("用户{}支付失败，恢复可购买金额orderId={}", userId, orderId);

        if (orderId == null) {
            logger.warn("用户{}支付失败，恢复可购买金额,orderId={}为空", userId, orderId);
            return Response.errorMsg("订单id为空");
        }

        return financeBidService.recoveryAmt(userId, orderId);
    }


}
