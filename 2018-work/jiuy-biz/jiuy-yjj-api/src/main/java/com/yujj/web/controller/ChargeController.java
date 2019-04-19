/**
 * 
 */
package com.yujj.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.spring.ControllerUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.UserBankCardSignService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.order.OrderNew;
import com.yujj.util.uri.UriBuilder;
import com.yujj.util.uri.UriParams;
import com.yujj.web.controller.delegate.ChargeDelegator;

/**
 * @author LWS 通过支付接口进行收费处理
 */
@Controller
@RequestMapping("/pay")
public class ChargeController {
    private static final Logger logger = LoggerFactory.getLogger("PAY");
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ChargeDelegator chargeDelegator;
    
    @Autowired
    private UserBankCardSignService userBankCardSignService;
    /**
     * 
     * @param uriParams
     * @return
     */
    @RequestMapping("/return_url")
    public String postPayDispose(UriParams uriParams) {
        String orderNo = chargeDelegator.payCallbackDispose(uriParams);
        return ControllerUtil.redirect(new UriBuilder("/order/result.do").set("order_no", orderNo).toUri());
    }
    /**
     * 
     * @param uriParams
     * @return
     */
    @RequestMapping("/notify_url")
    @ResponseBody
    public String postPayDisposeAsyn(UriParams uriParams) {
        chargeDelegator.payCallbackDispose(uriParams);
        return "success";
    }
    
    @RequestMapping("return_url/bankcard")
    public String postPayDisposeBankCard(UriParams uriParams) {
    
    	Map<String,String> resultMap = chargeDelegator.payReturnDisposeBankCard(uriParams);
    	
    	if(resultMap != null){
    		
	    	String result = resultMap.get("result");
	    	if (result != null && result.equals("0")){
	    		  return ControllerUtil.redirect(new UriBuilder("/mobile/pay/directpay/success.json").set("order_no", resultMap.get("orderNo")).toUri());
	    	} else {
                return "fail";
            }
    	} else {
            return "fail";
        }
		
    }
    
    /**
     * 银行卡支付回调
     * @param uriParams
     * @return
     */
    @RequestMapping("/notify_url/bankcard")
    @ResponseBody
    public String postBankCardPayDisposeAsyn(UriParams uriParams) {
    	chargeDelegator.bankcardPayCallbackDispose(uriParams);
    	
//    	ResponseHandler resHandler = new ResponseHandler(request, response);
//    	
//    		String transaction_id = resHandler.getParameter("transaction_id");
//    		String		total_fee = resHandler.getParameter("total_fee");
//    		String pay_result = resHandler.getParameter("pay_result");
//
//    		String sp_billno = resHandler.getParameter("sp_billno");
    	return "success";
    }
    /**
     * 银行卡签约回调
     * @param uriParams
     * @return
     */
    @RequestMapping("/sign_notify_url/bankcard")
    @ResponseBody
    public String postBankCardSignDisposeAsyn(UriParams uriParams) {
    	chargeDelegator.bankcardSignCallbackDispose(uriParams);
    	return "success";
    }
    
    @RequestMapping("/updateSign")
    @ResponseBody
    public JsonResponse updateBankCardSign(long userId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	//userBankCardSignService.updateUserBankCardSign(userId, 1);
    	 return jsonResponse.setSuccessful();
    }
    
    /**
     * @param out_trade_no 商户网站订单系统中唯一订单号，必填
     *  subject 订单名称，必填
     *  total_fee 付款金额，必填
     *  body 订单描述，必填
     *  show_url 订单展示地址，需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html
     */
    @RequestMapping("/directpay")
    @Login
    public void payDispose(@RequestParam("out_trade_no") String out_trade_no, UserDetail userDetail,
                           @ClientIp String ip, HttpServletResponse response) throws IOException {

        if (1==1)return;
        long userId = userDetail.getUserId();
//        Order order = orderService.getUserOrderByNo(userId, out_trade_no);
        OrderNew orderNew = orderService.getUserOrderNewByNo(userDetail.getUserId(), out_trade_no);
        if (null == orderNew || orderNew.getOrderStatus() != OrderStatus.UNPAID) {
            return;
        }
        
        
        logger.debug("charge methd: USING ALI DIRECT PAY IN OFFICIAL WAY");
        String bodyContent = chargeDelegator.payDispose(Long.parseLong(out_trade_no), ip);

        PrintWriter out = null;
        out = response.getWriter();
        out.println(bodyContent);
        out.close();
    }
    /**
     * 网页支付回调
     * @param requestBody
     * @return
     */
    @RequestMapping("/notify_url/weixin")
    @ResponseBody
    public String weixinPayCallback(@RequestBody String requestBody) {
        chargeDelegator.weixinPayCallbackDispose(requestBody, PaymentType.WEIXINPAY_SDK);
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
    /**
     * app应用微信支付回调
     * @param requestBody
     * @return
     */
    @RequestMapping("/notify_url/weixin_native")
    @ResponseBody
    public String weixinPayCallbackNative(@RequestBody String requestBody) {
        chargeDelegator.weixinPayCallbackDispose(requestBody,  PaymentType.WEIXINPAY_NATIVE);
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
    /**
     * 公众号微信支付回调
     * @param requestBody
     * @return
     */
    @RequestMapping("/notify_url/weixin_public")
    @ResponseBody
    public String weixinPayCallbackPublic(@RequestBody String requestBody) {
    	logger.info("<测试分享下单1>:"+requestBody);
        chargeDelegator.weixinPayCallbackDispose(requestBody,  PaymentType.WEIXINPAY_PUBLIC);
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
}
