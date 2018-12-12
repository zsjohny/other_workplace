package com.finace.miscroservice.borrow.service.fuiou;

import com.finace.miscroservice.borrow.dao.BorrowDao;
import com.finace.miscroservice.borrow.dao.FinanceBidDao;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.service.LoanMoneyService;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.current.ExecutorService;
import com.finace.miscroservice.commons.current.ExecutorTask;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.credit.DateUtil;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.fuiou.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.finace.miscroservice.commons.enums.MqChannelEnum.AUTO_UP_BORROW;

@Service
@RefreshScope
public class FuiouAgreePayService {

    private Log logger = Log.getInstance(FuiouAgreePayService.class);

    private static final String VERSION = "1.0";

    private static final String SUCCESS = "0000";
    @Autowired
    @Lazy
    private MqTemplate mqTemplate;
    @Autowired
    private BorrowDao borrowDao;
    @Autowired
    private FinanceBidDao financeBidDao;
    @Autowired
    private LoanMoneyService loanMoneyService;
    @Value("${borrow.fuiou.fcds}")
    private String fcd;

    @Value("${borrow.fuiou.fkeys}")
    private String fkey;

    @Value("${borrow.host}")
    private String baseHost;

    //发送短信
    @Value("${fuiou.agree.pay.msg}")
    private String msgUrl;

    //绑定银行卡
    @Value("${fuiou.agree.pay.bind}")
    private String bindUrl;

    //解绑银行卡
    @Value("${fuiou.agree.pay.unbind}")
    private String unbindUrl;

    //绑卡查询
    @Value("${fuiou.agree.pay.bind.query}")
    private String queryBindCardUrl;

    //支付
    @Value("${fuiou.agree.pay.order}")
    private String orderUrl;

    @Value("${fuiou.agree.pay.back.url}")
    private String backUrl;

    //富有订单号查询
    @Value("${fuiou.agree.pay.query.order}")
    private String queryOrderUrl;

    //商户订单号查询
    @Value("${fuiou.agree.pay.check.order}")
    private String checkOrderUrl;

    //支持银行卡查询
    @Value("${fuiou.agree.pay.query.card.bin}")
    private String cardBinUrl;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;


    public class CardBinResult {
        private String Rcd;
        private String RDesc;

        public String getRcd() {
            return Rcd;
        }

        public void setRcd(String rcd) {
            Rcd = rcd;
        }

        public String getRDesc() {
            return RDesc;
        }

        public void setRDesc(String RDesc) {
            this.RDesc = RDesc;
        }
    }

    /**
     * 绑定银行卡短信发送
     *
     * @param userId
     * @param name
     * @param bankCard
     * @param idCard
     * @param phone
     * @return
     */
    public String sendBindMsg(String userId, String name, String bankCard, String idCard, String phone) {
        String mchntssn = DateUtil.getDate() + DateUtil.getTime() + DateUtil.getRandomStr(12);
        StringBuffer singStr = new StringBuffer().append(VERSION).append("|");
        singStr.append(mchntssn).append("|");
        singStr.append(fcd).append("|");
        singStr.append(userId).append("|");
        singStr.append(name).append("|");
        singStr.append(bankCard).append("|");
        singStr.append("0").append("|");
        singStr.append(idCard).append("|");
        singStr.append(phone).append("|");
        singStr.append(fkey);
        //签名
        String sign = MD5.MD5Encode(singStr.toString());

        //请求参数
        StringBuffer request = new StringBuffer("<REQUEST>");
        request.append("<VERSION>").append(VERSION).append("</VERSION>");
        request.append("<MCHNTCD>").append(fcd).append("</MCHNTCD>");
        request.append("<USERID>").append(userId).append("</USERID>");
        request.append("<TRADEDATE>").append(DateUtils.getNowDateStr5()).append("</TRADEDATE>");
        request.append("<MCHNTSSN>").append(mchntssn).append("</MCHNTSSN>");
        request.append("<ACCOUNT>").append(name).append("</ACCOUNT>");
        request.append("<CARDNO>").append(bankCard).append("</CARDNO>");
        request.append("<IDTYPE>").append("0").append("</IDTYPE>");
        request.append("<IDCARD>").append(idCard).append("</IDCARD>");
        request.append("<MOBILENO>").append(phone).append("</MOBILENO>");
        request.append("<SIGN>").append(sign).append("</SIGN>");
        request.append("</REQUEST>");
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("MCHNTCD", fcd);
            params.put("APIFMS", DESCoderFUIOU.desEncrypt(request.toString(), DESCoderFUIOU.getKeyLength8(fkey)));

            String respStr = HttpPostUtil.postForward(msgUrl, params);
            respStr = DESCoderFUIOU.desDecrypt(respStr, DESCoderFUIOU.getKeyLength8(fkey));

            logger.info("绑定银行卡短信发送接口,返回数据={}", respStr);
            String rcode = respStr.substring(respStr.indexOf("<RESPONSECODE>") + 14, respStr.indexOf("</RESPONSECODE>"));
            String rdesc = respStr.substring(respStr.indexOf("<RESPONSEMSG>") + 13, respStr.indexOf("</RESPONSEMSG>"));
            // 校验签名
            if (rcode != null && SUCCESS.equals(rcode)) {
                String mssn = respStr.substring(respStr.indexOf("<MCHNTSSN>") + 10, respStr.indexOf("</MCHNTSSN>"));
                //保存发送短信流水号
                userStrHashRedisTemplate.set(Constant.AGREE_BIND_BANK_CARD_SEND_MSG + userId, mchntssn, 61, TimeUnit.SECONDS);
                return "success";
            } else {
                return rdesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("绑定银行卡短信发送接口,异常信息{}", e);
        }

        return null;
    }


    /**
     * 协议支付绑定银行卡
     *
     * @param userId
     * @param name
     * @param bankCard
     * @param idCard
     * @param phone
     * @param msgCode
     * @return
     */
    public String[] bindCard(String userId, String name, String bankCard, String idCard, String phone, String msgCode) {
//        String mch = DateUtil.getDate()+DateUtil.getTime()+DateUtil.getRandomStr(11);

        String mch = userStrHashRedisTemplate.get(Constant.AGREE_BIND_BANK_CARD_SEND_MSG + userId);
        if ("".equals(mch) || null == mch) {
            return null;
        }

        StringBuffer singStr = new StringBuffer().append(VERSION).append("|");
        singStr.append(mch).append("|");
        singStr.append(fcd).append("|");
        singStr.append(userId).append("|");
        singStr.append(name).append("|");
        singStr.append(bankCard).append("|");
        singStr.append("0").append("|");
        singStr.append(idCard).append("|");
        singStr.append(phone).append("|");
        singStr.append(msgCode).append("|");
        singStr.append(fkey);
        //签名
        String sign = MD5.MD5Encode(singStr.toString());

        //请求参数
        StringBuffer request = new StringBuffer("<REQUEST>");
        request.append("<VERSION>").append(VERSION).append("</VERSION>");
        request.append("<MCHNTCD>").append(fcd).append("</MCHNTCD>");
        request.append("<USERID>").append(userId).append("</USERID>");
        request.append("<TRADEDATE>").append(DateUtils.getNowDateStr5()).append("</TRADEDATE>");
        request.append("<MCHNTSSN>").append(mch).append("</MCHNTSSN>");
        request.append("<ACCOUNT>").append(name).append("</ACCOUNT>");
        request.append("<CARDNO>").append(bankCard).append("</CARDNO>");
        request.append("<IDTYPE>").append("0").append("</IDTYPE>");
        request.append("<IDCARD>").append(idCard).append("</IDCARD>");
        request.append("<MOBILENO>").append(phone).append("</MOBILENO>");
        request.append("<MSGCODE>").append(msgCode).append("</MSGCODE>");
        request.append("<SIGN>").append(sign).append("</SIGN>");
        request.append("</REQUEST>");

        try {

            Map<String, String> params = new HashMap<String, String>();
            params.put("MCHNTCD", fcd);
            params.put("APIFMS", DESCoderFUIOU.desEncrypt(request.toString(), DESCoderFUIOU.getKeyLength8(fkey)));

            String respStr = HttpPostUtil.postForward(bindUrl, params);
            respStr = DESCoderFUIOU.desDecrypt(respStr, DESCoderFUIOU.getKeyLength8(fkey));
            logger.info("协议支付绑定银行卡接口,返回数据={}", respStr);

            String rcode = respStr.substring(respStr.indexOf("<RESPONSECODE>") + 14, respStr.indexOf("</RESPONSECODE>"));
            String rdesc = respStr.substring(respStr.indexOf("<RESPONSEMSG>") + 13, respStr.indexOf("</RESPONSEMSG>"));

            String rmsg[] = new String[2];
            rmsg[0] = rcode;
            // 校验签名
            if (rcode != null && SUCCESS.equals(rcode)) {
                String mchntssn = respStr.substring(respStr.indexOf("<MCHNTSSN>") + 10, respStr.indexOf("</MCHNTSSN>"));
                String protocolNo = respStr.substring(respStr.indexOf("<PROTOCOLNO>") + 12, respStr.indexOf("</PROTOCOLNO>"));
                rmsg[1] = protocolNo;
                return rmsg;
            } else {
                rmsg[1] = rdesc;
                return rmsg;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("协议支付绑定银行卡接口,异常信息{}", e);
        }

        return null;
    }


    /**
     * 协议支付解绑银行卡
     *
     * @param userId
     * @param protocolNo
     * @return
     */
    public String unbindCard(String userId, String protocolNo) {

        StringBuffer singStr = new StringBuffer().append(VERSION).append("|");
        singStr.append(fcd).append("|");
        singStr.append(userId).append("|");
        singStr.append(protocolNo).append("|");
        singStr.append(fkey);
        //签名
        String sign = MD5.MD5Encode(singStr.toString());

        //请求参数
        StringBuffer request = new StringBuffer("<REQUEST>");
        request.append("<VERSION>").append(VERSION).append("</VERSION>");
        request.append("<MCHNTCD>").append(fcd).append("</MCHNTCD>");
        request.append("<USERID>").append(userId).append("</USERID>");
        request.append("<PROTOCOLNO>").append(protocolNo).append("</PROTOCOLNO>");
        request.append("<SIGN>").append(sign).append("</SIGN>");
        request.append("</REQUEST>");

        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("MCHNTCD", fcd);
            params.put("APIFMS", DESCoderFUIOU.desEncrypt(request.toString(), DESCoderFUIOU.getKeyLength8(fkey)));

            String respStr = HttpPostUtil.postForward(unbindUrl, params);
            respStr = DESCoderFUIOU.desDecrypt(respStr, DESCoderFUIOU.getKeyLength8(fkey));
            logger.info("协议支付解绑银行卡接口,返回数据={}", respStr);

            String rcode = respStr.substring(respStr.indexOf("<RESPONSECODE>") + 14, respStr.indexOf("</RESPONSECODE>"));
            String rdesc = respStr.substring(respStr.indexOf("<RESPONSEMSG>") + 13, respStr.indexOf("</RESPONSEMSG>"));

            // 校验签名
            if (rcode != null && SUCCESS.equals(rcode)) {
                String mchntssn = respStr.substring(respStr.indexOf("<MCHNTSSN>") + 10, respStr.indexOf("</MCHNTSSN>"));
                String uId = respStr.substring(respStr.indexOf("<USERID>") + 8, respStr.indexOf("</USERID>"));
                String pNo = respStr.substring(respStr.indexOf("<PROTOCOLNO>") + 10, respStr.indexOf("</PROTOCOLNO>"));

                return pNo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("协议支付解绑银行卡接口,异常信息{}", e);
        }
        return null;
    }


    /**
     * 查询用户绑卡信息
     *
     * @param userId
     * @return
     */
    public String queryBindCard(String userId) {

        StringBuffer singStr = new StringBuffer().append(VERSION).append("|");
        singStr.append(fcd).append("|");
        singStr.append(userId).append("|");
        singStr.append(fkey);
        //签名
        String sign = MD5.MD5Encode(singStr.toString());

        //请求参数
        StringBuffer request = new StringBuffer("<REQUEST>");
        request.append("<VERSION>").append(VERSION).append("</VERSION>");
        request.append("<MCHNTCD>").append(fcd).append("</MCHNTCD>");
        request.append("<USERID>").append(userId).append("</USERID>");
        request.append("<SIGN>").append(sign).append("</SIGN>");
        request.append("</REQUEST>");

        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("MCHNTCD", fcd);
            params.put("APIFMS", DESCoderFUIOU.desEncrypt(request.toString(), DESCoderFUIOU.getKeyLength8(fkey)));

            String respStr = HttpPostUtil.postForward(queryBindCardUrl, params);
            respStr = DESCoderFUIOU.desDecrypt(respStr, DESCoderFUIOU.getKeyLength8(fkey));
            logger.info("查询用户绑卡信息接口,返回数据={}", respStr);

            String rcode = respStr.substring(respStr.indexOf("<RESPONSECODE>") + 14, respStr.indexOf("</RESPONSECODE>"));
            String rdesc = respStr.substring(respStr.indexOf("<RESPONSEMSG>") + 13, respStr.indexOf("</RESPONSEMSG>"));

            // 校验签名
            if (rcode != null && SUCCESS.equals(rcode)) {
                String uId = respStr.substring(respStr.indexOf("<USERID>") + 8, respStr.indexOf("</USERID>"));
                String pNo = respStr.substring(respStr.indexOf("<PROTOCOLNO>") + 10, respStr.indexOf("</PROTOCOLNO>"));
                String cardNo = respStr.substring(respStr.indexOf("<CARDNO>") + 8, respStr.indexOf("</CARDNO>"));

                return pNo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户绑卡信息接口,异常信息{}", e);
        }
        return null;
    }

    /**
     * 支付结果返回类
     */
    public class OrderPayResult {

        private String userId;
        private String orderId;
        private String fuiouOrderId;
        private String protocolno;
        private String bankCard;
        private String amt;
        private String rdesc;

        public String getRdesc() {
            return rdesc;
        }

        public void setRdesc(String rdesc) {
            this.rdesc = rdesc;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getFuiouOrderId() {
            return fuiouOrderId;
        }

        public void setFuiouOrderId(String fuiouOrderId) {
            this.fuiouOrderId = fuiouOrderId;
        }

        public String getProtocolno() {
            return protocolno;
        }

        public void setProtocolno(String protocolno) {
            this.protocolno = protocolno;
        }

        public String getBankCard() {
            return bankCard;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }

        public String getAmt() {
            return amt;
        }

        public void setAmt(String amt) {
            this.amt = amt;
        }
    }
    private static ExecutorService authOrder = new ExecutorService(Runtime.getRuntime().availableProcessors() << 1, "authOrder");


    /**
     * 协议支付接口
     *
     * @param amt
     * @param userId
     * @param protocolNo
     * @param userIp
     * @param orderId
     * @return
     */
    public OrderPayResult orderPay(String amt, String userId, String protocolNo, String userIp, String orderId) {
        long buyAmt = Double.valueOf(Double.valueOf(amt) * 100).longValue();  //支付接口以分为单位

        StringBuffer singStr = new StringBuffer().append("03").append("|");
        singStr.append(VERSION).append("|");
        singStr.append(fcd).append("|");
        singStr.append(orderId).append("|");
        singStr.append(userId).append("|");
        singStr.append(protocolNo).append("|");
        singStr.append(buyAmt).append("|");
        singStr.append(baseHost + backUrl).append("|");
        singStr.append(userIp.trim()).append("|");
        singStr.append(fkey);
        logger.info("协议支付接口,签名的明文窜={}", singStr.toString());
        //签名
        String sign = MD5.MD5Encode(singStr.toString());

        //请求参数
        StringBuffer request = new StringBuffer("<REQUEST>");
        request.append("<VERSION>").append(VERSION).append("</VERSION>");
        request.append("<USERIP>").append(userIp).append("</USERIP>");
        request.append("<MCHNTCD>").append(fcd).append("</MCHNTCD>");
        request.append("<TYPE>").append("03").append("</TYPE>");
        request.append("<MCHNTORDERID>").append(orderId).append("</MCHNTORDERID>");
        request.append("<USERID>").append(userId).append("</USERID>");
        request.append("<AMT>").append(buyAmt).append("</AMT>");
        request.append("<PROTOCOLNO>").append(protocolNo).append("</PROTOCOLNO>");
        request.append("<NEEDSENDMSG>").append("0").append("</NEEDSENDMSG>");
        request.append("<BACKURL>").append(baseHost + backUrl).append("</BACKURL>");
        request.append("<SIGNTP>").append("MD5").append("</SIGNTP>");
        request.append("<SIGN>").append(sign).append("</SIGN>");
        request.append("</REQUEST>");

        logger.info("协议支付接口,请求明文数据={}", request);
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("MCHNTCD", fcd);
            params.put("APIFMS", DESCoderFUIOU.desEncrypt(request.toString(), DESCoderFUIOU.getKeyLength8(fkey)));

            String respStr = HttpPostUtil.postForward(orderUrl, params);
            respStr = DESCoderFUIOU.desDecrypt(respStr, DESCoderFUIOU.getKeyLength8(fkey));
            logger.info("协议支付接口,返回数据={}", respStr);

            String rcode = respStr.substring(respStr.indexOf("<RESPONSECODE>") + 14, respStr.indexOf("</RESPONSECODE>"));
            String rdesc = respStr.substring(respStr.indexOf("<RESPONSEMSG>") + 13, respStr.indexOf("</RESPONSEMSG>"));

            // 校验签名
            if (rcode != null && !SUCCESS.equals(rcode)) {
                return null;
            }

//          String mchntssn = respStr.substring(respStr.indexOf("<MCHNTSSN>") + 10, respStr.indexOf("</MCHNTSSN>"));
            String rorderId = respStr.substring(respStr.indexOf("<MCHNTORDERID>") + 14, respStr.indexOf("</MCHNTORDERID>"));
            String fuiouOrderId = respStr.substring(respStr.indexOf("<ORDERID>") + 9, respStr.indexOf("</ORDERID>"));
            String uId = respStr.substring(respStr.indexOf("<USERID>") + 8, respStr.indexOf("</USERID>"));
            String pNo = respStr.substring(respStr.indexOf("<PROTOCOLNO>") + 10, respStr.indexOf("</PROTOCOLNO>"));
            String cardNo = respStr.substring(respStr.indexOf("<BANKCARD>") + 10, respStr.indexOf("</BANKCARD>"));
            String ramt = respStr.substring(respStr.indexOf("<AMT>") + 8, respStr.indexOf("</AMT>"));
            String rsign = respStr.substring(respStr.indexOf("<SIGN>") + 6, respStr.indexOf("</SIGN>"));

            OrderPayResult result = new OrderPayResult();
            result.setUserId(uId);
            result.setAmt(ramt);
            result.setBankCard(cardNo);
            result.setFuiouOrderId(fuiouOrderId);
            result.setOrderId(rorderId);
            result.setProtocolno(pNo);
            result.setRdesc(rdesc);
            //自动上标
//            rorderId

             FinanceBidPO financeBidPO = financeBidDao.getFidByOrderId(rorderId);
             BorrowPO borrow = null;

             if (financeBidPO!=null){
                borrow =borrowDao.getBorrowById(financeBidPO.getBorrowId());
             }

             logger.info("自动上标");
             if (borrow!=null) {
                logger.info("自动上标 group={},",borrow.getBorrow_group());
                String group = borrow.getBorrow_group();
                 //自动上标
                 authOrder.addTask(new ExecutorTask() {
                    @Override
                    public void doJob() {
                        loanMoneyService.AutoUpAgreeBorrow(group);

                    }
                });
             //                mqTemplate.sendMsg(AUTO_UP_BORROW.toName(), String.valueOf(borrow.getBorrow_group()));
             }

            return result;

            // 校验签名
            /*if ( rsign.equals(MD5.MD5Encode("03|"+VERSION+"|"+rcode+"|"+fcd+"|"+rorderId+"|"+fuiouOrderId+"|"+ramt+"|"+cardNo+"|"+fkey)) ) {

            }*/

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("协议支付接口,异常信息{}", e);
        }
        return null;
    }


    /**
     * 根据商户订单号查询订单信息
     *
     * @param orderId
     * @return
     */
    public String checkOrder(String orderId) {

        StringBuffer singStr = new StringBuffer().append("03").append("|");
        singStr.append(VERSION).append("|");
        singStr.append(fcd).append("|");
        singStr.append(orderId).append("|");
        singStr.append(fkey);
        //签名
        String sign = MD5.MD5Encode(singStr.toString());

        //请求参数
        StringBuffer request = new StringBuffer("<REQUEST>");
        request.append("<VERSION>").append(VERSION).append("</VERSION>");
        request.append("<MCHNTCD>").append(fcd).append("</MCHNTCD>");
        request.append("<MCHNTORDERID>").append(orderId).append("</MCHNTORDERID>");
        request.append("<SIGN>").append(sign).append("</SIGN>");
        request.append("</REQUEST>");
        Map<String, String> params = new HashMap<String, String>();
        params.put("REQUEST", request.toString());

        try {
            String respStr = HttpPostUtil.postForward(checkOrderUrl, params);
            logger.info("根据商户订单号查询订单信息,返回数据={}", respStr);

            String rcode = respStr.substring(respStr.indexOf("<RESPONSECODE>") + 14, respStr.indexOf("</RESPONSECODE>"));
            String rdesc = respStr.substring(respStr.indexOf("<RESPONSEMSG>") + 13, respStr.indexOf("</RESPONSEMSG>"));
            String rorderId = respStr.substring(respStr.indexOf("<MCHNTORDERID>") + 14, respStr.indexOf("</MCHNTORDERID>"));
            String ramt = respStr.substring(respStr.indexOf("<AMT>") + 8, respStr.indexOf("</AMT>"));
            String rorderDay = respStr.substring(respStr.indexOf("<ORDERDATE>") + 8, respStr.indexOf("</ORDERDATE>"));
            String bankCard = respStr.substring(respStr.indexOf("<BANKCARD>") + 8, respStr.indexOf("</BANKCARD>"));
            String rsign = respStr.substring(respStr.indexOf("<SIGN>") + 6, respStr.indexOf("</SIGN>"));

            // 校验签名
            if (rsign.equals(MD5.MD5Encode(VERSION + "|" + rcode + "|" + rdesc + "|" + rorderId + "|" + fkey))) {
                logger.info("商户订单号查询订单信息rcode={},rdesc={}", rcode, rdesc);
                if (rcode != null && SUCCESS.equals(rcode)) {
                    return "success";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据商户订单号查询订单信息,异常信息{}", e);
        }
        return null;
    }

    public class QueryCardBinResult {
        private String cnm;   //银行名称
        private String insCd; //银行机构号
        private String ctp;   //01-借记卡,02-信用卡,03-准贷记卡,04-富友卡,05-非法卡号
        private String rdesc;

        public String getCnm() {
            return cnm;
        }

        public void setCnm(String cnm) {
            this.cnm = cnm;
        }

        public String getInsCd() {
            return insCd;
        }

        public void setInsCd(String insCd) {
            this.insCd = insCd;
        }

        public String getCtp() {
            return ctp;
        }

        public void setCtp(String ctp) {
            this.ctp = ctp;
        }

        public String getRdesc() {
            return rdesc;
        }

        public void setRdesc(String rdesc) {
            this.rdesc = rdesc;
        }
    }


    @Value("${borrow.fuiou.fcd}")
    private String fcds;

    @Value("${borrow.fuiou.fkey}")
    private String fkeys;

    /**
     * 支持银行卡查询
     *
     * @param cardNo
     * @return
     */
    public QueryCardBinResult queryCardBin(String cardNo) {

        StringBuffer singStr = new StringBuffer().append(fcds).append("|");
        singStr.append(cardNo).append("|");
        singStr.append(fkeys);
        //签名
        String sign = MD5.MD5Encode(singStr.toString());

        //请求参数
        StringBuffer request = new StringBuffer("<FM>");
        request.append("<MchntCd>").append(fcds).append("</MchntCd>");
        request.append("<Ono>").append(cardNo).append("</Ono>");
        request.append("<Sign>").append(sign).append("</Sign>");
        request.append("</FM>");
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("FM", request.toString());

            String respStr = HttpPostUtil.postForward(cardBinUrl, params);
            logger.info("支持银行卡查询,返回数据={}", respStr);

            String rcode = respStr.substring(respStr.indexOf("<Rcd>") + 5, respStr.indexOf("</Rcd>"));
            String rdesc = respStr.substring(respStr.indexOf("<RDesc>") + 7, respStr.indexOf("</RDesc>"));
            String rsign = respStr.substring(respStr.indexOf("<Sign>") + 6, respStr.indexOf("</Sign>"));

            // 校验签名
            if (rsign.equals(MD5.MD5Encode(rcode + "|" + fkeys))) {
                QueryCardBinResult cardBinResult = new QueryCardBinResult();
                if (rcode != null && SUCCESS.equals(rcode)) {

                    String ctp = respStr.substring(respStr.indexOf("<Ctp>") + 5, respStr.indexOf("</Ctp>"));
                    String cnm = respStr.substring(respStr.indexOf("<Cnm>") + 5, respStr.indexOf("</Cnm>"));
                    String insCd = respStr.substring(respStr.indexOf("<InsCd>") + 7, respStr.indexOf("</InsCd>"));

                    cardBinResult.setCtp(ctp);
                    cardBinResult.setCnm(cnm);
                    cardBinResult.setInsCd(insCd);
                } else {
                    cardBinResult.setRdesc(rdesc);
                }
                return cardBinResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("支持银行卡查询,异常信息{}", e);
        }
        return null;
    }


}
