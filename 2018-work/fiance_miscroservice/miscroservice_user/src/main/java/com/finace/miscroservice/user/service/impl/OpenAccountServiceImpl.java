package com.finace.miscroservice.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.current.ExecutorService;
import com.finace.miscroservice.commons.current.ExecutorTask;
import com.finace.miscroservice.commons.entity.TimerScheduler;
import com.finace.miscroservice.commons.enums.AccountLogTypeEnums;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import com.finace.miscroservice.commons.enums.TxCodeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.commons.utils.credit.CheckIdCardUtils;
import com.finace.miscroservice.commons.utils.credit.CreditUtils;
import com.finace.miscroservice.commons.utils.credit.SignUtil;
import com.finace.miscroservice.commons.utils.tools.*;
import com.finace.miscroservice.user.dao.AccountLogDao;
import com.finace.miscroservice.user.dao.OpenAccountDao;
import com.finace.miscroservice.user.dao.OpenAccountLogDao;
import com.finace.miscroservice.user.entity.po.CreditAccountLog;
import com.finace.miscroservice.user.enums.AccountUseTypeEnum;
import com.finace.miscroservice.user.enums.IdentityTypeEnum;
import com.finace.miscroservice.user.enums.MsgTypeEnums;
import com.finace.miscroservice.user.enums.SmsFlagTypeEnum;
import com.finace.miscroservice.user.po.AccountLogPO;
import com.finace.miscroservice.user.po.CreditGoAccountPO;
import com.finace.miscroservice.user.service.OpenAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAccountServiceImpl implements OpenAccountService{

    private Log logger = Log.getInstance(OpenAccountServiceImpl.class);

    @Autowired
    private CreditUtils credit2go;

    @Autowired
    private OpenAccountDao openAccountDao;
    @Autowired
    private AccountLogDao accountLogDao;
    @Autowired
    private OpenAccountLogDao openAccountLogDao;

    @Autowired
    @Qualifier("userHashRedisTemplate")
    private HashOperations<String, String, String> userHashRedisTemplate;
    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    @Lazy
    private MqTemplate mqTemplate;

    @Value("${user.account.coinstName:1}")
    protected String coinstName;
    @Value("${user.account.retUrl:1}")
    protected String retUrl;
    @Value("${user.account.successfulUrl:1}")
    protected String successfulUrl;
    @Value("${user.account.notifyUrl:1}")
    protected String notifyUrl;
    @Value("${creditgo.url:1}")
    protected String url;
    @Value("${creditgo.online.url:1}")
    protected String onlineUrl;
    @Value("${creditgo.payment.max.amount:1}")
    protected String maxAmount;
    @Value("${creditgo.payment.deadline:1}")
    protected String deadline;
    @Value("${user.withdraw.proportion:1}")
    protected String proportion;//提现手续费 百分比
    @Value("${user.withdraw.service.charge:1}")
    protected String serviceCharge; //提现手续费
    @Value("${user.withdraw.daily.time:1}")
    protected String dailyTime; //每日可免费提现次数
    private final static String RET_CODE = "00000000";
    private final static String CE999028 = "CE999028";
    private final static String YTJ = "ytj";

    private final static String USER_WITHDRAW_TIME="%s:withdraw:time";

    @Override
    public void openAccountNotify(String bgData, HttpServletResponse response) {
        JSONObject map= JSONObject.parseObject(bgData);
        String txCode = map.get("txCode").toString();
        String seqNo = map.get("seqNo").toString();
        String txTime = map.get("txTime").toString();
        String txDate = map.get("txDate").toString();
        String retCode = map.get("retCode").toString();
        String retMsg = map.get("retMsg").toString();
        logger.info("江西银行存款回调{}",bgData);
        String accountId =map.get("accountId").toString();
        Double txAmount = 0d;
        if (map!=null){
            txAmount = map.get("txAmount")==null?0d:Double.valueOf(map.get("txAmount").toString());
        }

        if (txCode.equals(TxCodeEnum.ACCOUNT_OPEN_PAGE.getCode())){
            String cardNo =map.get("cardNo").toString();
            if (retCode.equals(RET_CODE)&&StringUtils.isNotEmpty(accountId)&&StringUtils.isNotEmpty(cardNo)){
                Map map1 = cardBindDetailsQuery(accountId,"1");
                Object obj = map1.get("subPacks");
                JSONArray jsonArray = JSONArray.parseArray(obj.toString());
                String realCardNo =null;
                if (jsonArray.size()>0){
                    Object object = jsonArray.get(0);
                    JSONObject jsonObject = JSONObject.parseObject(object.toString());
                    realCardNo = jsonObject.get("cardNo").toString();
                    logger.info("accountId = {}绑定银行卡查询成功",accountId);
                }else {
                    logger.warn("accountId = {}未查到已绑定的银行卡retCode={},retMsg={}",accountId,retCode,retMsg);
                }
                if (StringUtils.isNotEmpty(realCardNo)){
                    cardNo=realCardNo;
                }
                upCardNoByAccountId(accountId,cardNo,seqNo,txTime,txDate);
                logger.info("accountId = {}设置银行卡信息成功",accountId);
            }else {
                logger.info("accountId = {}设置银行卡信息失败 retCode={},retMsg={}",accountId,retCode,retMsg);
            }
        }

        if (txCode.equals(TxCodeEnum.PASSWORD_SET.getCode())){
            if (StringUtils.isNotEmpty(accountId)&&retCode.equals(RET_CODE)){
                openAccountDao.upSetPass(accountId);
                logger.info("accountId = {}设置密码成功",accountId);
            }else {
                logger.warn("accountId = {}设置密码失败retCode={},retMsg={}",accountId,retCode,retMsg);

            }
        }
        if (txCode.equals(TxCodeEnum.PASSWORD_RESET_PLUS.getCode())){
            if (retCode.equals(RET_CODE)){
                logger.info("accountId = {}重置密码成功",accountId);
            }else {
                logger.warn("accountId = {}重置密码失败retCode={},retMsg={}",accountId,retCode,retMsg);
            }
        }
//        if (txCode.equals(TxCodeEnum.BIND_CARD_PAGE.getCode())){
//            if ("1".equals(map.get("txState").toString())){
//
//            }
//            Map map1 = cardBindDetailsQuery(accountId,"1");
//            Object obj = map1.get("subPacks");
//            logger.info("obj-->",obj);
//            JSONArray jsonArray = JSONArray.parseArray(obj.toString());
//            logger.info("BIND_CARD_PAGE-->",jsonArray);
//            if (jsonArray.size()>0){
//                Object object = jsonArray.get(0);
//                JSONObject jsonObject = JSONObject.parseObject(object.toString());
//                String cardNo = jsonObject.get("cardNo").toString();
//                openAccountDao.upCardNo(accountId,cardNo);
//                logger.info("accountId = {}绑定银行卡成功",accountId);
//            }else {
//                logger.warn("accountId = {}未查到已绑定的银行卡retCode={},retMsg={}",accountId,retCode,retMsg);
//            }
//        }
        CreditAccountLog accountLog = new CreditAccountLog();
        accountLog.setAccountId(accountId);
        accountLog.setSeqNo(seqNo);
        accountLog.setTxDate(txDate);
        accountLog.setTxTime(txTime);
        accountLog.setTxCode(txCode);
        accountLog.setTxMoney(txAmount);
        if (txCode.equals(TxCodeEnum.DIRECT_RECHARGE_PAGE.getCode())){
            if (retCode.equals(RET_CODE)){
//                balanceQuery(accountId);
//                accountLogDao.upAccountLogBySeqNo(seqNo,AccountLogTypeEnums.RECHARGE_SUCCESS.getCode(),String.format(AccountLogTypeEnums.RECHARGE_SUCCESS.getValue(),AccountLogTypeEnums.RECHARGE_SUCCESS.getValue(),txAmount.toString()));
                //修改 需扣款金额
                openAccountDao.upOpenAccountCutPayment(accountId,String.valueOf(txAmount));
                logger.info("accountId = {}充值成功，充值金额{}",accountId,txAmount);
                accountLog.setIsSuccess(0);
                saveCallBack(accountLog);
            }else {
//                accountLogDao.upAccountLogBySeqNo(seqNo,AccountLogTypeEnums.RECHARGE_FAIL.getCode(),String.format(AccountLogTypeEnums.RECHARGE_FAIL.getValue(),AccountLogTypeEnums.RECHARGE_FAIL.getValue()));
                logger.warn("accountId = {}充值失败retCode={},retMsg={}",accountId,retCode,retMsg);
                accountLog.setIsSuccess(1);
                saveCallBack(accountLog);
            }

//            openAccountDao.upTxMoney(accountId,txAmount);
        }
        if (txCode.equals(TxCodeEnum.WITHDRAW.getCode())){
//            if (retCode.equals(RET_CODE)||retCode.equals(CE999028)){
            if (retCode.equals(RET_CODE)){
                accountLog.setIsSuccess(0);
                saveCallBack(accountLog);
                logger.info("accountId = {}提现成功，提现金额{}",accountId,txAmount);
            } else if(retCode.equals(CE999028)){
                try {
//                SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
                    TimerScheduler timerScheduler = new TimerScheduler();
                    //订单是否成功处理
                    timerScheduler.setType(TimerSchedulerTypeEnum.WITHDRAW_DEPOSIT_BANK_DELAY_TASK.toNum());
                    timerScheduler.setName(TimerSchedulerTypeEnum.WITHDRAW_DEPOSIT_BANK_DELAY_TASK.toChar() + UUIdUtil.generateUuid());
//                timerScheduler.setCron(sdf.format(DateUtils.dateAndDayByDate(String.valueOf(Integer.valueOf(DateUtils.getNowTimeStr()) + 20*60), "0")));
                    timerScheduler.setParams(JSONObject.toJSONString(map));
                    mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));
                }catch (Exception e){
                    logger.error("订单accountId={},失效消息发送发送失败，异常信息{}", accountId, e);
                }
                logger.info("提现处理accountId={},seqNo={}",accountId,seqNo);
                accountLog.setIsSuccess(2);
                saveCallBack(accountLog);
            }else {
                logger.warn("提现失败accountId={},seqNo={},txCode={}",accountId,seqNo,txCode);
                accountLog.setIsSuccess(1);
                saveCallBack(accountLog);
            }

        }
        if (txCode.equals(TxCodeEnum.OFFLINE_RECHARGE_CALL.getCode())){
            if (retCode.equals(RET_CODE)){
//                 balanceQuery(accountId);
                logger.info("accountId = {}线下充值成功，充值金额{}",accountId,txAmount);
                saveCallBack(accountLog);
            }else {
                logger.warn("accountId = {}线下充值成功失败retCode={},retMsg={}",accountId,retCode,retMsg);
                saveCallBack(accountLog);
            }
        }
        if (txCode.equals(TxCodeEnum.PAYMENT_AUTH_PAGE.getCode())){
            if (retCode.equals(RET_CODE)){
                openAccountDao.upPaymentAuth(accountId);
                logger.info("accountId = {}缴费授权成功",accountId);
            }else {
                logger.warn("accountId = {}缴费授权失败retCode={},retMsg={}",accountId,retCode,retMsg);
            }
        }
        if (txCode.equals(TxCodeEnum.REPAY_AUTH_PAGE.getCode())){
            if (retCode.equals(RET_CODE)){
                openAccountDao.upRepayAuth(accountId);
                logger.info("accountId = {}还款授权成功",accountId);
            }else {
                logger.warn("accountId = {}还款授权失败retCode={},retMsg={}",accountId,retCode,retMsg);
            }
        }

        try {
            response.getWriter().write("success");
        }catch (Exception e){
            logger.warn(e.getMessage());
            e.printStackTrace();
        }

    }

    private static ExecutorService openAccountExecutor = new ExecutorService(Runtime.getRuntime().availableProcessors() << 1, "openAccount");



    @Override
    public String openAccount(String userId, String idNo, String name, String mobile,String acctUse,String identity) {
        logger.info("userId={}请求开户",userId);
        Map<String, String> reqMap =new TreeMap<>();
        try {
            name = URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String deName=name.toString();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode", TxCodeEnum.ACCOUNT_OPEN_PAGE.getCode());
        reqMap.put("idType","01"); //default 身份证
        reqMap.put("idNo",idNo);
        reqMap.put("name",name);
        reqMap.put("gender", CheckIdCardUtils.getGenderByIdCard(idNo));
        reqMap.put("mobile", mobile);
        reqMap.put("acctUse",acctUse);
        reqMap.put("smsFlag", SmsFlagTypeEnum.SEND.getCode());
        reqMap.put("identity", identity);
        reqMap.put("coinstName", coinstName);
        reqMap.put("retUrl", retUrl);
        reqMap.put("successfulUrl", YTJ+TxCodeEnum.ACCOUNT_OPEN_PAGE.getCode());
        reqMap.put("notifyUrl", notifyUrl);
        try {
            //生成待签名字符串
            String requestMapMerged = credit2go.mergeMap(reqMap);
            //生成签名
            String sign = SignUtil.sign(requestMapMerged);
            reqMap.put("sign", sign);
            String reqUrl = url+"/"+TxCodeEnum.ACCOUNT_OPEN_PAGE.getCode();
            String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");
            openAccountExecutor.addTask(new ExecutorTask() {
                @Override
                public void doJob() {
                    logger.info("将开户信息存入数据库中");
                    CreditGoAccountPO account = openAccountDao.findOpenAccountByUserId(userId);
                    if (account==null){
                        openAccountDao.openAccount(reqMap.get("txDate"),reqMap.get("txTime"),reqMap.get("seqNo"),userId,"01",idNo,deName, CheckIdCardUtils.getGenderByIdCard(idNo),mobile,AccountUseTypeEnum.COMMON_ACCOUNT.getCode(),
                                SmsFlagTypeEnum.SEND.getCode(),IdentityTypeEnum.LENDER.getCode(),coinstName);
                    }else if (account!=null && StringUtils.isEmpty(account.getAccountId())){
                        Map map = findAccountIdByIdNo(userId,idNo,"01");
                        if (map.get("acctState")==null&&map.get("accountId")!=null){

                        }else {
                            openAccountDao.upOpenAccount(reqMap.get("txDate"),reqMap.get("txTime"),reqMap.get("seqNo"),userId,"01",idNo,deName, CheckIdCardUtils.getGenderByIdCard(idNo),mobile,AccountUseTypeEnum.COMMON_ACCOUNT.getCode(),
                                    SmsFlagTypeEnum.SEND.getCode(),IdentityTypeEnum.LENDER.getCode(),coinstName);
                        }

                    }

                }
            });


            return reqStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    @Override
    public CreditGoAccountPO findOpenAccountByUserId(String userId) {
        return openAccountDao.findOpenAccountByUserId(userId);
    }

    @Override
    public String setPass(CreditGoAccountPO account) {
        logger.info("userId={}请求设置密码",account.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode", TxCodeEnum.PASSWORD_SET.getCode());
        reqMap.put("idType","01"); //default 身份证
        reqMap.put("idNo",account.getIdNo());
        reqMap.put("name",account.getName());
        reqMap.put("accountId", account.getAccountId());
        reqMap.put("mobile", account.getMobile());
        reqMap.put("retUrl", retUrl);
        reqMap.put("successfulUrl", YTJ+TxCodeEnum.PASSWORD_SET.getCode());
//        reqMap.put("successfulUrl", TxCodeEnum.PASSWORD_SET.getCode());
        reqMap.put("notifyUrl", notifyUrl);
        try {
            //生成待签名字符串
            String requestMapMerged = credit2go.mergeMap(reqMap);
            //生成签名
            String sign = SignUtil.sign(requestMapMerged);
            reqMap.put("sign", sign);
            String reqUrl = url+"/"+"passwordset";
            String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");
            logger.info("reqMp={}",reqMap);
            return reqStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void upCardNoByAccountId(String accountId, String cardNo, String seqNo, String txTime, String txDate) {
        logger.info("accountId={}修改银行卡号cardNo={}",accountId,cardNo);
        openAccountDao.upCardNoByAccountId(accountId,cardNo,seqNo,txTime,txDate);
    }

    @Override
    public CreditGoAccountPO findOpenAccountByAccountId(String accountId) {
        return openAccountDao.findOpenAccountByAccountId(accountId);
    }



    @Override
    public Map findAccountIdByIdNo(String userId, String idNo, String idType) {
        logger.info("userId={}请求查询电子账户idNo={}",userId,idNo);
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("idNo",idNo);
        reqMap.put("idType",idType);
        reqMap.put("txCode",TxCodeEnum.ACCOUNT_ID_QUERY.getCode());
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
//        String reqStr = HttpUtil.doPost(onlineUrl, reqMap, "UTF-8");

        try {
            Map map = credit2go.creditGo(reqMap);
//            String accountId = map.get("accountId").toString();
//            String cardNo = map.get("cardNo").toString();
//            String idNoBack = map.get("idNo").toString();
//            upCardNoByAccountId(accountId,cardNo,);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//        return reqStr;
    }

    @Override
    public String resetPass(CreditGoAccountPO account,String msgCode) {
        logger.info("userId={}请求充值密码",account.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.PASSWORD_RESET_PLUS.getCode());
        reqMap.put("idNo",account.getIdNo());
        reqMap.put("accountId",account.getAccountId());
        reqMap.put("idType",account.getIdType());
        reqMap.put("name",account.getName());
        reqMap.put("mobile",account.getMobile());
        reqMap.put("retUrl", retUrl);
        reqMap.put("successfulUrl",YTJ+ TxCodeEnum.PASSWORD_RESET_PLUS.getCode());
        reqMap.put("notifyUrl", notifyUrl);
        String lastSrvAuthCode = userHashRedisTemplate.get(account.getMobile(),MsgTypeEnums.PASSWORD_RESET_PLUS.getCode());
        if (StringUtils.isEmpty(lastSrvAuthCode)){
            lastSrvAuthCode="000000";
        }
        reqMap.put("lastSrvAuthCode", lastSrvAuthCode);
        reqMap.put("smsCode",msgCode );
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
        String reqUrl = url+"/"+"mobile/plus";
        String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");
        return reqStr;
    }

    @Override
    public String bindCard(CreditGoAccountPO account, String ip) {
        logger.info("userId={}请求绑定银行卡",account.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.BIND_CARD_PAGE.getCode());
        reqMap.put("idNo",account.getIdNo());
        reqMap.put("userIP",ip);
        reqMap.put("accountId",account.getAccountId());
        reqMap.put("idType",account.getIdType());
        reqMap.put("name",account.getName());
        reqMap.put("retUrl", retUrl);
        reqMap.put("successfulUrl", YTJ+TxCodeEnum.BIND_CARD_PAGE.getCode());
        reqMap.put("notifyUrl", notifyUrl);
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
        String reqUrl = url+"/"+TxCodeEnum.BIND_CARD_PAGE.getCode();
        String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");
        return reqStr;
    }

    @Override
    public Map unbindCard(CreditGoAccountPO account) {
        logger.info("userId={}请求解绑银行卡 cardNO={}",account.getUserId(),account.getCardNo());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("idNo",account.getIdNo());
        reqMap.put("idType",account.getIdType());
        reqMap.put("txCode",TxCodeEnum.CARD_UN_BIND.getCode());
        reqMap.put("accountId",account.getAccountId());
        reqMap.put("name",account.getName());
        reqMap.put("mobile",account.getMobile());
        reqMap.put("cardNo",account.getCardNo());
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
//        String reqStr = HttpUtil.doPost(onlineUrl, reqMap, "UTF-8");
        try {
            Map map = credit2go.creditGo(reqMap);
            String retCode = map.get("retCode").toString();
            String reAccountId = (String) map.get("accountId");
            if (RET_CODE.equals(retCode)){
                openAccountDao.delCardNo(reAccountId);
            }
            logger.info("mapUnbindCard={}",map);
            return map;
        } catch (Exception e) {
            logger.warn("userId = {}解绑卡失败",account.getUserId());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String directRecharge(CreditGoAccountPO account,Double txAmount) {
        logger.info("userId={}请求充值",account.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.DIRECT_RECHARGE_PAGE.getCode());
        reqMap.put("idNo",account.getIdNo());
        reqMap.put("cardNo",account.getCardNo());
        reqMap.put("accountId",account.getAccountId());
        reqMap.put("idType",account.getIdType());
        reqMap.put("name",account.getName());
        reqMap.put("mobile",account.getMobile());
        reqMap.put("currency",account.getCurrency());
        reqMap.put("txAmount",String.valueOf(txAmount));

        reqMap.put("forgotPwdUrl",TxCodeEnum.FORGET_PASS_URL.getCode());
        reqMap.put("retUrl", retUrl);
        reqMap.put("successfulUrl", YTJ+TxCodeEnum.DIRECT_RECHARGE_PAGE.getCode());
        reqMap.put("notifyUrl", notifyUrl);
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
        String reqUrl = url+"/"+TxCodeEnum.DIRECT_RECHARGE_PAGE.getCode();
        String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");
//        AccountLog log = new AccountLog();
//        log.setMoney(bid.getBuyAmt().doubleValue());    操作金额
//        log.setTotal(0.0);  总金额
//        log.setUseMoney(0.0); 已投资金额
//        log.setUser_id(bid.getUserId());//
//        log.setType("tender");
//        log.setNoUseMoney(0.0); 可用余额
//        log.setToUser(borrow.getUserId());
//        log.setAddtime(DateUtils.getNowTimeStr());
//        log.setCollection(0.0); 待还款金额
//        log.setRemark("投资成功，冻结投资者的投标资金" + NumberUtil.format4(bid.getBuyAmt().doubleValue())); 备注
//        us.addAccountLog(log);
//        String reqNo = reqMap.get("seqNo");
//        openAccountExecutor.addTask(new ExecutorTask() {
//            @Override
//            public void doJob() {
//                logger.info("userId={}用户充值日志log",account.getUserId());
//                AccountLogPO accountLog  = new AccountLogPO();
//                accountLog.setMoney(txAmount);//充值金额
//                accountLog.setTotal(0d);
//                accountLog.setUseMoney(0d);
//                accountLog.setUser_id(account.getUserId());
//                accountLog.setType(AccountLogTypeEnums.RECHARGE.getCode());
//                accountLog.setNoUseMoney(0d);
//                accountLog.setToUser(0);
//                accountLog.setAddtime(DateUtils.getNowTimeStr());
//                accountLog.setCollection(0.0);
//                accountLog.setSeqNo(reqNo);
//                accountLog.setRemark(String.format(AccountLogTypeEnums.RECHARGE.getValue(),AccountLogTypeEnums.RECHARGE.getValue(),txAmount.toString()));
//                accountLogDao.addAccountLogSeqNo(accountLog);
//            }
//        });

        return reqStr;
    }

    @Override
    public Map cardBindDetailsQuery(String accountId,String state) {
        logger.info("accountId={}请求查询绑卡关系 state={}",accountId ,state);
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.CARD_BIND_DETAILS_QUERY.getCode());
        reqMap.put("accountId",accountId);
        reqMap.put("state",state);
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
//        String reqStr = HttpUtil.doPost(onlineUrl, reqMap, "UTF-8");
        try {
            Map map = credit2go.creditGo(reqMap);
//            Object obj = map.get("subPacks");
//            JSONArray jsonArray = JSONArray.parseArray(obj.toString());
//            JSONObject jsonObject = JSON.parseObject(jsonArray.get(0).toString());
//            System.out.println(jsonObject.get("cardNo").toString());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map balanceQuery(String accountId) {
        if (accountId==null){
            return null;
        }
        logger.info("accountId={}请求电子账户查询",accountId);
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.BALANCE_QUERY.getCode());
        reqMap.put("accountId",accountId);
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
//        String reqStr = HttpUtil.doPost(onlineUrl, reqMap, "UTF-8");
        try {
            Map map = credit2go.creditGo(reqMap);
//            String reAccountId = map.get("accountId").toString();
//            Double availBal = Double.parseDouble(map.get("availBal").toString()); //可用余额
//            Double currBal = Double.parseDouble(map.get("currBal").toString()); //账面余额
//            Double iceMoney = NumberUtil.subtract(2,currBal,availBal); //冻结金额
//            openAccountDao.upMoney(reAccountId,availBal,currBal,iceMoney);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void smsCodeApply(Integer srvTxCode, String phone, String smsType, CreditGoAccountPO account ) {
        String txCode=null;
        switch (srvTxCode){
            case 1:
                txCode=MsgTypeEnums.MOBILE_MODIFY_PLUS.getCode();
                break;
            case 2:
                txCode=MsgTypeEnums.PASSWORD_RESET_PLUS.getCode();
                break;
            case 3:
                txCode=MsgTypeEnums.AUTO_BID_AUTH_PLUS.getCode();
                break;
            case 4:
                txCode=MsgTypeEnums.AUTO_CREDIT_INVEST_AUTH_PLUS.getCode();
                break;
        }

        logger.info("phone={}请求发送短信 srvTxCode={}",phone,txCode);
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.SMS_CODE_APPLY.getCode());
        reqMap.put("accountId",account.getAccountId());
        reqMap.put("srvTxCode",txCode);
        reqMap.put("smsType",smsType);
        reqMap.put("mobile",account.getMobile());
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
//        String reqStr = HttpUtil.doPost(onlineUrl, reqMap, "UTF-8");
        try {
            Map map = credit2go.creditGo(reqMap);
//            String reAccountId = map.get("accountId").toString();
            String srvAuthCode = map.get("srvAuthCode").toString();
            String reSrvTxCode = map.get("srvTxCode").toString();
            String mobile = map.get("mobile").toString();
            userHashRedisTemplate.put(mobile,reSrvTxCode,srvAuthCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String creditWithdrow(CreditGoAccountPO account, Double txAmount,String routeCode) {
        logger.info("userId={}请求提现",account.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.WITHDRAW.getCode());
        reqMap.put("idNo",account.getIdNo());
        reqMap.put("accountId",account.getAccountId());
        reqMap.put("idType",account.getIdType());
        reqMap.put("name",account.getName());
        reqMap.put("mobile",account.getMobile());
        reqMap.put("cardNo",account.getCardNo());
        JSONObject jsonObject = withdrawPre(String.valueOf(account.getUserId()),account);
//        jsonObject.put("dailyTime",time<0?0:time); //每日剩余可提现次数
//        jsonObject.put("proportion",proportion); //需扣手续费 金额百分比
//        jsonObject.put("serviceCharge",serviceCharge); //超出免费次数 后 每笔提现手续费
//        jsonObject.put("payment",payment); //可免费提现金额
//        jsonObject.put("availBal",availBal);// 可用余额

//        Map map = balanceQuery(account.getAccountId());
//        Double money = 0d;
//        Double availBal = 0d;
//        if (map!=null){
//            availBal = Double.parseDouble(map.get("availBal")==null||map.get("availBal")==""?"0":map.get("availBal").toString()); //可用余额
//            money = Double.parseDouble(map.get("currBal")==null||map.get("availBal")==""?"0":map.get("currBal").toString()); //账面余额
//        }
//        Double payment = NumberUtil.subtract(2,availBal,account.getCutPayment())<0d?0d:NumberUtil.subtract(2,availBal,account.getCutPayment());

        Double txFee = 0d;
        Double serviceCharges = 0d;
        Double proportions = 0d;
        if (jsonObject!=null){
            if (Integer.valueOf(jsonObject.get("dailyTime").toString())<=0){
                serviceCharges=NumberUtil.adds(2,txFee,Double.valueOf(serviceCharge));
                logger.info("1-手续费txFee={},serviceCharge={}",txFee,serviceCharge);
            }
            Double payment = Double.valueOf(jsonObject.get("payment").toString()); //可免费提现金额
            Double txMoney = NumberUtil.subtract(2,txAmount,payment);
            if (txMoney>0){
                proportions = NumberUtil.multiply(2,txMoney,Double.valueOf(proportion));
                logger.info("2-手续费txFee={},proportion={},txMoney={}",proportions,proportion,txMoney);
            }
        }
        txFee=NumberUtil.adds(2,serviceCharges,proportions);
        Double countAmount = NumberUtil.subtract(2,txAmount,txFee);
        logger.info("3-手续费txFee={}",txFee);
        reqMap.put("txFee",String.valueOf(txFee));
        reqMap.put("txAmount",String.valueOf(countAmount));

        if (StringUtils.isNotEmpty(routeCode)){
            reqMap.put("routeCode", routeCode);
            reqMap.put("cardBankCnaps", account.getCardBankCnaps());
        }

        reqMap.put("retUrl", YTJ+TxCodeEnum.WITHDRAW.getCode());
        reqMap.put("forgotPwdUrl", TxCodeEnum.FORGET_PASS_URL.getCode());
        reqMap.put("successfulUrl", YTJ+TxCodeEnum.WITHDRAW.getCode());
        reqMap.put("notifyUrl", notifyUrl);
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
        String reqUrl = url+"/"+TxCodeEnum.WITHDRAW.getCode();
        String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");

        Integer time=1;
        String times = userStrHashRedisTemplate.get(String.format(USER_WITHDRAW_TIME,account.getUserId()));
        if (StringUtils.isNotEmpty(times)){
            time=Integer.valueOf(times)+1;
        }
        //第二天凌晨 清零
        userStrHashRedisTemplate.set(String.format(USER_WITHDRAW_TIME,account.getUserId()),String.valueOf(time),DateUtils.calSec(), TimeUnit.SECONDS);


        return reqStr;
    }

    @Override
    public Response updatePhone(String phone, String smsCode, CreditGoAccountPO account) {
        logger.info("userId={}请求修改手机号",account.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.MOBILE_MODIFY_PLUS.getCode());
        reqMap.put("accountId",account.getAccountId());
        reqMap.put("option","1");
        reqMap.put("mobile",phone);
        String lastSrvAuthCode = userHashRedisTemplate.get(phone,TxCodeEnum.MOBILE_MODIFY_PLUS.getCode());
        reqMap.put("lastSrvAuthCode",lastSrvAuthCode);
        reqMap.put("smsCode",smsCode);

        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);

        try {
            Map map = credit2go.creditGo(reqMap);
            String retCode = map.get("retCode").toString();
            String retMsg = map.get("retMsg").toString();
            if (retCode.equals(RET_CODE)){
                return Response.success();
            }else {
                return Response.errorMsg(retMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String  paymentAuthPage(CreditGoAccountPO account) {
        logger.info("userId={}请求缴费授权",account.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.PAYMENT_AUTH_PAGE.getCode());
        reqMap.put("accountId",account.getAccountId());
        reqMap.put("maxAmt",maxAmount);
        String  deadTime = DateUtils.calDate(Integer.parseInt(deadline));
        reqMap.put("deadline",deadTime);


        reqMap.put("forgotPwdUrl", TxCodeEnum.FORGET_PASS_URL.getCode());
        reqMap.put("retUrl", retUrl);
        reqMap.put("successfulUrl", YTJ+TxCodeEnum.PAYMENT_AUTH_PAGE.getCode());
        reqMap.put("notifyUrl", notifyUrl);
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
        String reqUrl = url+"/"+TxCodeEnum.PAYMENT_AUTH_PAGE.getCode();
        String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");
        return reqStr;
    }

    @Override
    public String repayAuthPage(CreditGoAccountPO account) {
        logger.info("userId={}请求还款授权",account.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.REPAY_AUTH_PAGE.getCode());
        reqMap.put("accountId",account.getAccountId());
        reqMap.put("maxAmt",maxAmount);
        String  deadTime = DateUtils.calDate(Integer.parseInt(deadline));
        reqMap.put("deadline",deadTime);


        reqMap.put("forgotPwdUrl", TxCodeEnum.FORGET_PASS_URL.getCode());
        reqMap.put("retUrl", retUrl);
        reqMap.put("successfulUrl", YTJ+TxCodeEnum.REPAY_AUTH_PAGE.getCode());
        reqMap.put("notifyUrl", notifyUrl);
        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);
        String reqUrl = url+"/"+TxCodeEnum.REPAY_AUTH_PAGE.getCode();
        String reqStr = HttpUtil.doPost(reqUrl, reqMap, "UTF-8");
        return reqStr;
    }

    @Override
    public JSONObject withdrawPre(String userId, CreditGoAccountPO account) {
        String times = userStrHashRedisTemplate.get(String.format(USER_WITHDRAW_TIME,account.getUserId()));
        Integer time=0;
        if (StringUtils.isNotEmpty(times)){
            time=Integer.valueOf(times);
        }
        time = Integer.valueOf(dailyTime)-time;
        Map map = balanceQuery(account.getAccountId());
        Double money = 0d;
        Double availBal = 0d;
        if (map!=null){
            // TODO: 2018/6/1  不确定是账目余额还是  可用余额 减去 需扣款金额为 可提现金额
            availBal = Double.parseDouble(map.get("availBal")==null||map.get("availBal")==""?"0":map.get("availBal").toString()); //可用余额
            money = Double.parseDouble(map.get("currBal")==null||map.get("availBal")==""?"0":map.get("currBal").toString()); //账面余额

        }

        Double payment = NumberUtil.subtract(2,money,account.getCutPayment())<0d?0d:NumberUtil.subtract(2,money,account.getCutPayment());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dailyTime",time<0?0:time); //每日剩余可提现次数
        jsonObject.put("proportion",proportion); //需扣手续费 金额百分比
        jsonObject.put("serviceCharge",serviceCharge); //超出免费次数 后 每笔提现手续费
        jsonObject.put("payment",payment); //可免费提现金额
        jsonObject.put("availBal",availBal);// 可用余额

        return jsonObject;
    }

    @Override
    public Map paymentAuthCancel(CreditGoAccountPO account) {
        logger.info("userId={}请求缴费授权解约",account.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.PAYMENT_AUTH_CHANCEL.getCode());
        reqMap.put("accountId",account.getAccountId());
//        reqMap.put("option","1");
//        reqMap.put("mobile",phone);
//        String lastSrvAuthCode = userHashRedisTemplate.get(phone,TxCodeEnum.MOBILE_MODIFY_PLUS.getCode());
//        reqMap.put("lastSrvAuthCode",lastSrvAuthCode);
//        reqMap.put("smsCode",smsCode);
//
//        //生成待签名字符串
//        String requestMapMerged = credit2go.mergeMap(reqMap);
//        //生成签名
//        String sign = SignUtil.sign(requestMapMerged);
//        reqMap.put("sign", sign);
//
//        try {
//            Map map = credit2go.creditGo(reqMap);
//            String retCode = map.get("retCode").toString();
//            String retMsg = map.get("retMsg").toString();
//            if (retCode.equals(RET_CODE)){
//                return Response.success();
//            }else {
//                return Response.errorMsg(retMsg);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public void saveCallBack(CreditAccountLog accountLog) {
        logger.info("credit_account_log 日志 txCode={} accountId={}",accountLog.getTxCode(),accountLog.getAccountId());
        //回调存储
        Map balanceQuery = balanceQuery(accountLog.getAccountId());
        Double availBal = 0d;
        if (balanceQuery!=null){
            availBal = Double.parseDouble(balanceQuery.get("availBal").toString()); //可用余额
        }
        CreditGoAccountPO creditGoAccountPO =  findOpenAccountByAccountId(accountLog.getAccountId());
        logger.info("creditAccountPo={}",creditGoAccountPO);
        if (creditGoAccountPO!=null){
            accountLog.setUserId(creditGoAccountPO.getUserId());
            accountLog.setAvailBal(availBal);
        }else {
            logger.warn("该用户不存在SeqNo={}",accountLog.getSeqNo());
        }
        openAccountLogDao.addOpenAccountLog(accountLog);
    }

    @Override
    public void upOpenAccountLogBySeqNo(String seqNo,String txTime) {

        CreditAccountLog accountLog = openAccountLogDao.findOpenAccountLogBySeqNo(seqNo,txTime);
        logger.info("seqNo={},userId={}请求单笔资金类业务交易查询",seqNo,accountLog.getUserId());
        Map<String, String> reqMap =new TreeMap<>();
        credit2go.getHeadReq(reqMap);
        reqMap.put("txCode",TxCodeEnum.FUND_TRANS_QUERY.getCode());
        reqMap.put("accountId",accountLog.getAccountId());
        reqMap.put("orgTxDate",accountLog.getTxDate());
        reqMap.put("orgTxTime",accountLog.getTxTime());
        reqMap.put("orgSeqNo",accountLog.getSeqNo());


        //生成待签名字符串
        String requestMapMerged = credit2go.mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);
        reqMap.put("sign", sign);

        try {
            Map map = credit2go.creditGo(reqMap);
            String result = map.get("result").toString();
           if ("00".equals(result)){
               openAccountLogDao.upWithdrawIsSuccess(seqNo,accountLog.getTxTime(),0);
           }else {
               openAccountLogDao.upWithdrawIsSuccess(seqNo,accountLog.getTxTime(),1);
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateCreditAccount(CreditGoAccountPO creditGoAccountPO) {
        openAccountDao.updateCreditAccount(creditGoAccountPO);
    }

    @Override
    public String reFindCardNo(String accountId, int i) {
        Map map1 = cardBindDetailsQuery(accountId,"1");
        Object obj = map1.get("subPacks");
        if (obj!=null){
            JSONArray jsonArray = JSONArray.parseArray(obj.toString());
            if (jsonArray!=null&&jsonArray.size()>0){
                Object object = jsonArray.get(0);
                JSONObject jsonObject = JSONObject.parseObject(object.toString());
                String cardNo = jsonObject.get("cardNo").toString();
                openAccountDao.upCardNo(accountId,cardNo);
                logger.info("accountId = {}绑定银行卡成功",accountId);
                return cardNo;
            }else {
                logger.warn("accountId = {}未查到已绑定的银行卡retCode={},retMsg={}",accountId);
            }
        }

        return null;
    }

    @Override
    public Response findOpenAccountByIdNo(String idNo, String userId) {
        Boolean flag=Boolean.TRUE;
        logger.info("userId={}根据用户身份证查询开户信息",userId);
        List<String> idNOs = openAccountDao.findAccountByIdNo(idNo);
        if (idNOs.size()==0){
            flag=Boolean.FALSE;
        }
        return Response.success(flag);
    }


}
