package com.finace.miscroservice.user.controller;


import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.user.entity.CashReqExt;
import com.finace.miscroservice.user.entity.EnchashmentTrustPost;
import com.finace.miscroservice.user.entity.EnchashmentTrustReturn;
import com.finace.miscroservice.user.entity.SignUtils;
import com.finace.miscroservice.user.po.AccountCashPO;
import com.finace.miscroservice.user.po.AccountLogPO;
import com.finace.miscroservice.user.po.AccountPO;
import com.finace.miscroservice.user.po.UserPO;
import com.finace.miscroservice.user.service.AccountService;
import com.finace.miscroservice.user.service.UserService;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 提现模块主要接口:汇付提现
 */
@RestController
public class AppWithdrawController extends BaseController {
    private Log logger = Log.getInstance(AppWithdrawController.class);


    private static String RECV_ORD_ID = "RECV_ORD_ID_";

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;


    /**
     * 提现
     *
     * @param tmoney        提现金额
     * @param webhost
     * @param trustreturl
     * @param trustbgreturl
     * @param hhhost
     * @param mercustid     app_page=cash
     *                      msg成功是提现成功金额，失败代码如下提示
     *                      411--取现通道异常，稍后再试
     *                      412--取现通道异常，稍后再试
     *                      403--余额不足，取现失败
     *                      406--当前取现卡不存在
     *                      999--取现处理中
     *                      其他--取现失败，如有疑问请联系客服
     * @return
     */
    @RequestMapping("withdraw/auth")
    public Response withdraw(@RequestParam("tmoney") String tmoney,
                             @Value("${user.chinapnr.webhost}") String webhost,
                             @Value("${user.chinapnr.trustreturl}") String trustreturl,
                             @Value("${user.chinapnr.trustbgreturl}") String trustbgreturl,
                             @Value("${user.chinapnr.hhhost}") String hhhost,
                             @Value("${user.chinapnr.mercustid}") String mercustid) throws IOException {


        String userId = getUserId();
        logger.info("用户{}开始提现money={}", userId, tmoney);
        UserPO user = userService.findUserOneById(userId);

        // 提现记录
        AccountCashPO cash = new AccountCashPO();
        cash.setUserId(Integer.valueOf(userId));

        cash.setAddtime(DateUtils.getNowTimeStr());
        cash.setAddip("");
        cash.setTotal(tmoney);
        cash.setStatus(0);

        // 平台提现服务费
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
            // 平台收取提现服务费
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

        // 汇付提现手续费收取方
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
        response.get().sendRedirect(url);

        return null;
    }

    /**
     * 异步提现返回
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("trustqxReturn")
    public void qxReturn(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> params = getParamsMap(request);
        String recode = params.get("RespCode");
        String transAmt = params.get("TransAmt");
        logger.info("提现异步回调,跳转拦截RespCode={}, transAmt={}", recode, transAmt);
        String msg = "";
        if (recode.equals("000")) {
            // return Response.successMsg("提现成功，" + "提现金额" + transAmt + "元");
            logger.info("提现成功");
            response.sendRedirect("https://www.etongjin.net/html/huifucashmsg.html?app_page=cash&msg=" + transAmt + "&code=200");
        } else {
//            switch (Integer.valueOf(recode)) {
//                case 411:
//                    msg = "取现通道异常，稍后再试";
//                    break;
//                case 412:
//                    msg = "取现通道异常，稍后再试";
//                    break;
//                case 403:
//                    msg = "余额不足，取现失败";
//                    break;
//                case 406:
//                    msg = "当前取现卡不存在";
//                    break;
//                case 999:
//                    msg = "取现处理中";
//                    break;
//                default:
//                    msg = "取现失败，如有疑问请联系客服";
//            }
            response.sendRedirect("https://www.etongjin.net/html/huifucashmsg.html?app_page=cash&msg=" + recode + "&code=400");
        }
    }


    @RequestMapping("rediredtlocal")
    public void rediredtlocal() {
        //用于app重定向提示信息
    }


    /**
     * 取现成功返回
     *
     * @param map
     * @param request
     * @param response
     * @param mercustid
     */
    @RequestMapping("trustnotifyCash")
    public void withdraw_notify(ModelMap map, HttpServletRequest request, HttpServletResponse response,
                                @Value("${user.chinapnr.mercustid}") String mercustid) {

        Map<String, String> params = getParamsMap(request);
        logger.info("取现异步返回时间:" + DateUtils.dateStr4(DateUtils.getNowTimeStr()) + " 参数：" + params);

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


                    //汇付提现手续费收取方
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

                //解冻资金

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


}
