package com.finace.miscroservice.borrow.controller;


import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.service.BidService;
import com.finace.miscroservice.borrow.service.BorrowService;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.credit.CreditConstant;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 投标
 */
@RestController
@RefreshScope
public class AppBidController extends BaseController {
    private Log logger = Log.getInstance(AppBidController.class);


    @Autowired
    private BidService bidService;

    @Autowired
    private BorrowService borrowService;

    @Value("${borrow.credit.pay.success.url}")
    private String paySuccessUrl;

    @Value("${borrow.pay.error.url}")
    private String payErrorUrl;

    /**
     * 江西银行存管投标
     *
     * @param amt 投标金额
     * @param borrowId 标的id
     * @param channel 渠道
     * @param hbid 红包id
     * @param imei 设备id
     *
     * @return
     */
    @RequestMapping(value = "creditPay/auth")
    public Response creditPay(@RequestParam("amt") String amt,
                              @RequestParam("borrowId") Integer borrowId,
                              @RequestParam(value = "channel", required = false) String channel,
                              @RequestParam(value = "hbid", required = false) String hbid,
                              @RequestParam(value = "imei", required = false) String imei) {

        String userId = getUserId();

        logger.info("用户{}开始购买{}标的。amt={},channel={}", userId, borrowId, amt, channel);

        if (StringUtils.hasEmpty(amt, channel, borrowId.toString())) {
            logger.warn("用户{}购买{}标的。参数错误channel={}", userId, borrowId, channel);
            return Response.errorMsg("参数错误");
        }

        BigDecimal buyAmt = BigDecimal.valueOf(Double.parseDouble(amt));
        if (buyAmt.compareTo(BigDecimal.ZERO) <= 0 || buyAmt.compareTo(BigDecimal.valueOf(100000)) > 0) {
            logger.warn("用户{}购买{}标的。单笔不超过10万元!", userId, borrowId);
            return Response.errorMsg("单笔不超过10万元!");
        }

        if(!NumberUtil.isInteger(amt)){
            logger.warn("用户{}购买{}标的。投资金额必须是整数!amt={}", userId, borrowId, amt);
            return Response.errorMsg("投资金额必须是整数!");
        }

        try {
            return  bidService.creditPay(userId, amt, borrowId, hbid, channel, response.get());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("购买支付失败,异常信息：{}", e);
        }

        return Response.errorMsg("购买支付失败");
    }


    /**
     * 投标业务回调
     */
    @RequestMapping(value = "creditPayNotify")
    public void creditPayNotify() {
        String bgData=request.getParameter("bgData");
        bidService.creditPayNotify(bgData,response.get());
    }


    /**
     * 投标确认
     */
    @RequestMapping(value = "creditPaySuccess")
    public void creditPaySuccess() throws Exception{
        Map map = this.getParamsMap(request);

        String txCode = map.get("txCode").toString();
        String retCode = map.get("retCode").toString();
        String seqNo = map.get("seqNo").toString();
        String txTime = map.get("txTime").toString();
        String txDate = map.get("txDate").toString();
        String productId = map.get("productId").toString();
        String txAmount = map.get("txAmount").toString();
        String retMsg = map.get("retMsg").toString();

        if( productId == null ){
            logger.warn("投标确认,标的号不对{}", productId);
            return;
        }

        productId = productId.substring(2,productId.length());
        BorrowPO borrowPO = borrowService.getBorrowById(Integer.valueOf(productId));
        if( borrowPO == null ){
            logger.warn("投标确认,标的不存在{}", productId);
            return;
        }

        if ( retCode.equals(CreditConstant.CREDIT_SUCCESS) ) {
            response.get().sendRedirect(paySuccessUrl+"?amt="+txAmount+"&ytjName="+ URLEncoder.encode(borrowPO.getName(), "UTF-8") +"&ytjTimeLimitDay="+borrowPO.getTimeLimitDay()+"&ytjApr="+borrowPO.getApr());
        }else{
            response.get().sendRedirect(payErrorUrl + "?responseMsg=" + (retMsg != null ? retMsg : ""));
        }
    }


    /**
     * 放款 合法性校验通知链接
     */
    @RequestMapping(value = "loanMoneyNotify")
    public void loanMoneyNotify() {
        String bgData=request.getParameter("bgData");
        bidService.loanMoneyNotify(bgData,response.get());
    }


    /**
     * 放款 业务结果通知
     */
    @RequestMapping(value = "loanMoneyRetNotify")
    public void loanMoneyRetNotify() {
        String bgData=request.getParameter("bgData");
        bidService.loanMoneyRetNotify(bgData,response.get());
    }










}








