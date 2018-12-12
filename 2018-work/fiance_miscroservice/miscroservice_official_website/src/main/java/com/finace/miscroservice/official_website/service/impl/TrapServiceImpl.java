package com.finace.miscroservice.official_website.service.impl;

import com.alibaba.fastjson.JSON;
import com.finace.miscroservice.commons.entity.CreditGoAccount;
import com.finace.miscroservice.commons.enums.TxCodeEnum;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.credit.CreditConstant;
import com.finace.miscroservice.commons.utils.credit.CreditUtils;
import com.finace.miscroservice.commons.utils.credit.SignUtil;
import com.finace.miscroservice.commons.utils.tools.HttpUtil;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import com.finace.miscroservice.official_website.rpc.UserRpcService;
import com.finace.miscroservice.official_website.service.TrapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * 圈存圈提
 */
@Service
public class TrapServiceImpl implements TrapService {

    private static final Logger logger = LoggerFactory.getLogger(TrapServiceImpl.class);

    @Resource
    private UserRpcService userRpcService;

    @Autowired
    private CreditUtils creditUtils;

    @Value("${base.host}")
    private String serverHost;

    @Value("${h5.host}")
    private String htmlHost;

    @Value("${creditgo.url}")
    protected String url;

    @Value("${creditgo.hb.account.id}")
    private String hbAccountId;   //红包账户

    @Value("${creditgo.cost.account.id}")
    private String costAccountId;  //费用账户



    @Autowired
    @Qualifier("userHashRedisTemplate")
    private HashOperations<String, String, String> userHashRedisTemplate;



    @Override
    public Response getAccountBalance(String userId) {
        Map<String, String> reqMap = new TreeMap<>();
        try {

            creditUtils.getHeadReq(reqMap);
            reqMap.put("txCode", "balanceQuery");

            CreditGoAccount creditAccountPO = userRpcService.getUserAccountByUserId(userId);
            if (creditAccountPO == null) {
                return Response.errorMsg("企业用户没有开通存管账户");
            }

            reqMap.put("accountId", creditAccountPO.getAccountId());
            Map map = creditUtils.sendRequest(CreditUtils.URI + "/balanceQuery", reqMap);
            if (map != null && map.get("retCode").equals(CreditConstant.CREDIT_SUCCESS)) {
                logger.info("获取用户{}账户余额成功：",userId);

                return Response.success(map.get("availBal"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户{}查询账户余额失败,异常信息：{}", userId, e);
        }

        return Response.error();
    }


    @Override
    public String resetPass(CreditGoAccount account,String msgCode) throws Exception{
        logger.info("userId={}请求充值密码",account.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        creditUtils.getHeadReq(reqMap);
        reqMap.put("txCode", TxCodeEnum.PASSWORD_RESET_PLUS.getCode());
        reqMap.put("idNo",account.getIdNo());
        reqMap.put("accountId",account.getAccountId());
        reqMap.put("idType",account.getIdType());
        reqMap.put("name",account.getName());
        reqMap.put("mobile",account.getMobile());
        reqMap.put("retUrl", htmlHost+ "/newPCWeb/newPCWeb/html/error.html?responseMsg="+URLEncoder.encode("验证码错误", "UTF-8") );  //返回交易页面链接
        reqMap.put("successfulUrl", serverHost +"/official_website/tarp/toForgotPwdSuccessUrl");
        reqMap.put("notifyUrl", serverHost+"user/open/account/notify");
        String lastSrvAuthCode = userHashRedisTemplate.get(account.getMobile(), "passwordResetPlus");
        if (StringUtils.isEmpty(lastSrvAuthCode)){
            lastSrvAuthCode="000000";
        }
        reqMap.put("lastSrvAuthCode", lastSrvAuthCode);
        reqMap.put("smsCode",msgCode );
        //生成待签名字符串
        String requestMapMerged = creditUtils.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
        String reqUrl = url+"/"+"mobile/plus";
        String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");
        return reqStr;
    }

    @Override
    public Response doTrap(String phone, String money, String accountType) {

        Map<String, String> reqMap = new TreeMap<>();
        try {

            String accountId = null;
            creditUtils.getHeadReq(reqMap);
            reqMap.put("txCode", "creditForLoadPage");
            if ("10000".equals(accountType)) {
                accountId = hbAccountId;
            } else if ("10001".equals(accountType)) {
                accountId = costAccountId;
            }

            if (accountId == null) {
                return Response.errorMsg("账户不存在");
            }

            CreditGoAccount creditAccountPO = userRpcService.getUserAccountByAccountId(accountId);
            if (creditAccountPO == null) {
                return Response.errorMsg("企业用户没有开通存管账户");
            }

            reqMap.put("accountId", accountId);
            reqMap.put("cardNo", creditAccountPO.getCardNo());
            reqMap.put("currency", "156");
            reqMap.put("txAmount", money);
            reqMap.put("idType", creditAccountPO.getIdType());
            reqMap.put("idNo", creditAccountPO.getIdNo());
            reqMap.put("name", creditAccountPO.getName());
            reqMap.put("mobile", creditAccountPO.getMobile());
//            reqMap.put("authFlag", "bidApply");
//            reqMap.put("authSeq", "bidApply");
            reqMap.put("forgotPwdUrl", serverHost + "/official_website/tarp/toForgotPwdUrl");
            reqMap.put("retUrl", serverHost);
//            reqMap.put("verifyOrderNoUrl", serverHost + "/official_website/tarp/tarpVerifyOrderNoUrl");
            reqMap.put("successfulUrl", serverHost + "/official_website/tarp/tarpSuccessfulUrl");
            reqMap.put("notifyUrl", serverHost + "/official_website/tarp/tarpNotifyUrl");

            //生成待签名字符串
            String requestMapMerged = creditUtils.mergeMap(reqMap);
            //生成签名
            String sign = SignUtil.sign(requestMapMerged);
            reqMap.put("sign", sign);

            logger.info("投标请求信息：\r\n" + JSON.toJSON(reqMap).toString().replace(",", ",\r\n"));
            String reqUrl = CreditUtils.PAGE_URI + "/creditForLoadPage";
            String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");
            reqStr = "<script type=\"text/javascript\" src=\"https://www.etongjin.net/activity/jquery.min.js\"></script>" + reqStr;
            logger.info("圈存请求回调数据"+reqStr);

            return Response.success(reqStr);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户{}圈提失败,异常信息：{}", phone, e);
        }
        return Response.error();
    }


    @Override
    public Response doRing(String phone, String money, String accountType) {
        Map<String, String> reqMap = new TreeMap<>();
        try {

            String accountId = null;
            creditUtils.getHeadReq(reqMap);
            reqMap.put("txCode", "creditForLoadPage");
            if ("10000".equals(accountType)) {
                accountId = hbAccountId;
            } else if ("10001".equals(accountType)) {
                accountId = costAccountId;
            }

            if (accountId == null) {
                return Response.errorMsg("账户不存在");
            }

            CreditGoAccount creditAccountPO = userRpcService.getUserAccountByAccountId(accountId);
            if (creditAccountPO == null) {
                return Response.errorMsg("企业用户没有开通存管账户");
            }

            reqMap.put("accountId", accountId);
            reqMap.put("bankName", "");
            reqMap.put("cardNo", creditAccountPO.getCardNo());
            reqMap.put("currency", "156");
            reqMap.put("idType", creditAccountPO.getIdType());
            reqMap.put("idNo", creditAccountPO.getIdNo());
            reqMap.put("name", creditAccountPO.getName());
            reqMap.put("mobile", creditAccountPO.getMobile());
            reqMap.put("txAmount", money);
            reqMap.put("txFee", "0");

            reqMap.put("forgotPwdUrl", serverHost + "/official_website/tarp/toForgotPwdUrl");
            reqMap.put("retUrl", "serverHost");
//            reqMap.put("verifyOrderNoUrl", serverHost +"/official_website/tarp/ringVerifyOrderNoUrl");
            reqMap.put("successfulUrl", serverHost +"/official_website/tarp/ringSuccessfulUrl");
            reqMap.put("notifyUrl", serverHost +"/official_website/tarp/ringNotifyUrl");

            //生成待签名字符串
            String requestMapMerged = creditUtils.mergeMap(reqMap);
            //生成签名
            String sign = SignUtil.sign(requestMapMerged);
            reqMap.put("sign", sign);

            logger.info("投标请求信息：\r\n" + JSON.toJSON(reqMap).toString().replace(",", ",\r\n"));
            String reqUrl = CreditUtils.PAGE_URI + "/creditForUnloadPage";
            String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");
            reqStr = "<script type=\"text/javascript\" src=\"https://www.etongjin.net/activity/jquery.min.js\"></script>" + reqStr;
            logger.info("圈提请求接口回调数据"+reqStr);

            return Response.success(reqStr);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户{}圈提失败,异常信息：{}", phone, e);
        }
        return Response.error();


    }


}
