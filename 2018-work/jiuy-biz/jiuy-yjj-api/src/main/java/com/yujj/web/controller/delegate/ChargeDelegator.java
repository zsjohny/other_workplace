/**
 * 
 */
package com.yujj.web.controller.delegate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.constant.order.ServiceTicketStatus;
import com.jiuyuan.entity.newentity.alipay.direct.AlipaySubmit;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.jiuyuan.util.bankcardpay.BankCardPayConfig;
import com.yujj.business.facade.ChargeFacade;
import com.yujj.business.facade.WeixinChargeFacade;
import com.yujj.business.service.AfterSaleService;
import com.yujj.business.service.OrderService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.afterSale.ServiceTicket;
import com.yujj.entity.order.OrderNew;
import com.yujj.util.uri.UriParams;

import cmb.netpayment.Settle;

/**
 * @author LWS
 *
 */
@Service
public class ChargeDelegator {
    private static final Logger logger = LoggerFactory.getLogger("PAY");

    @Autowired
    private ChargeFacade chargeFacade;

    @Autowired
    private WeixinChargeFacade weixinChargeFacade;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private AfterSaleService afterSaleService;
    
    private Settle settle;
    private int iRet;

    public String mobilePayDispose(long orderNo) {
        // 生成请求数据给客户端做自行处理
        // String paramString = MobileAlipaySubmit.buildRequestParaString(sParaTemp);
    	//删除旧表
//    	OrderNew orderNew = orderService.queryOrderNewFromOld(order);
    	OrderNew orderNew = orderService.getUserOrderNewByNo(String.valueOf(orderNo));
    	String paramString = "";
    	if(orderNew != null){
    		
    		paramString = chargeFacade.buildRequestParaString4Mobile(orderNew);
    	}else {
    		paramString = "支付失败";
    	}
        logger.info("redirect pay sdk, orderNo:{}, paramString:{}", orderNew.getOrderNo(), paramString);
        return paramString;
    }
    public String mobilePayDisposeNew(OrderNew order) {
    	// 生成请求数据给客户端做自行处理
    	// String paramString = MobileAlipaySubmit.buildRequestParaString(sParaTemp);
    	String paramString = chargeFacade.buildRequestParaString4Mobile(order);
    	logger.info("redirect pay sdk, orderNo:{}, paramString:{}", order.getOrderNo(), paramString);
    	return paramString;
    }
    
    public String mobileBankCardPayDispose(long orderNo, String ip) {
    	// 生成请求数据给客户端做自行处理
    	// String paramString = MobileAlipaySubmit.buildRequestParaString(sParaTemp);
    	String paramString = "";
//        OrderNew orderNew = orderService.queryOrderNewFromOld(order);
        OrderNew orderNew = orderService.getUserOrderNewByNo(String.valueOf(orderNo));
        if(orderNew!=null){ 
        		paramString = chargeFacade.buildBankCardRequestParaString4Mobile(orderNew, ip);
        		logger.info("redirect pay sdk, orderNo:{}, paramString:{}", orderNo, paramString);
        		
        	}else{
        		paramString = "支付失败";
        	}
    	return paramString;
    }
    
    public String mobileBankCardPayDisposeNew(OrderNew order, String ip) {
    	// 生成请求数据给客户端做自行处理
    	// String paramString = MobileAlipaySubmit.buildRequestParaString(sParaTemp);
    	String paramString = chargeFacade.buildBankCardRequestParaString4Mobile(order, ip);
    	logger.info("redirect pay sdk, orderNo:{}, paramString:{}", order.getOrderNo(), paramString);
    	return paramString;
    }
    
    public String mobileBankCardPayAfterSale(ServiceTicket serviceTicket, long addrId, String ip) {
    	// 生成请求数据给客户端做自行处理
    	// String paramString = MobileAlipaySubmit.buildRequestParaString(sParaTemp);
    	String paramString = "";
    	if(serviceTicket.getProcessOrderNo()>0){
    		OrderNew orderNew = orderService.getUserOrderNewByNo(serviceTicket.getProcessOrderNo() + "");
    		
    		if ( orderNew!=null && addrId > 0 && serviceTicket.getStatus() == ServiceTicketStatus.UNPAID.getIntValue()) {
        		orderService.updateOrderAddressAfterSale(serviceTicket.getUserId() ,orderNew.getOrderNo(), addrId);
    			
    		}
//    		if (addr_id != null && Long.parseLong(addr_id) > 0) {
//    			saveUserReplaceAddress(service_id, Long.parseLong(addr_id));
//    		}
    		paramString = chargeFacade.buildBankCardRequestParaString4Mobile(orderNew, ip);
    		
    		//String paramString = chargeFacade.buildBankCardRequestParaString4AfterSale(serviceTicket, ip);
    		logger.info("redirect pay sdk, service:{}, paramString:{}", serviceTicket.getId(), paramString);
    		
    	}
    	return paramString;
    }
    
    public Map<String,String> queryPayResult(OrderNew order) {
    	Map<String,String> resultMap = new HashMap<String,String>();
    	settle=new Settle();
        //settle.testit();
    	iRet = settle.SetOptions("payment.ebank.cmbchina.com");
    	//iRet = settle.SetOptions("61.144.248.29");
   		if (iRet == 0)
		{
    	}
    	else
		{
			
		}

    	iRet = settle.LoginC(BankCardPayConfig.BRANCH_ID, BankCardPayConfig.CO_NO, BankCardPayConfig.CO_PSWD);
    	//iRet = settle.LoginC("0571","001186","001186");
    	
    	 int result =99;
    	
    	if (iRet == 0)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			//iRet = settle.RefundOrder("20050607","004003","0.1","test","AaBb123601234569");
			StringBuffer sBuffer= new StringBuffer();
		    result = settle.QuerySingleOrder(sdf.format(new Date(order.getCreateTime())) , String.format("%010d", order.getOrderNo()), sBuffer);   
		    resultMap.put("payResult", result+"");
		    resultMap.put("failReason",settle.GetLastErr(iRet));
		        
		    //String err = settle.GetLastErr(result);
	    }
    	else
		{
			resultMap.put("payResult", iRet+"");
		    resultMap.put("failReason",settle.GetLastErr(iRet));
			
			//resultMap.put("failReason",settle.GetLastErr(iRet));
			
			
		}
       
      
        settle.Logout();
        return resultMap;
    }
    public int queryPayResult(String orderNo) {
    	settle=new Settle();
    	settle.testit();
    	//iRet = settle.SetOptions("payment.ebank.cmbchina.com");
    	iRet = settle.SetOptions("61.144.248.29");
    	if (iRet == 0)
    	{
    	}
    	else
    	{
    		
    	}
    	
    	iRet = settle.LoginC(BankCardPayConfig.BRANCH_ID,BankCardPayConfig.CO_NO,BankCardPayConfig.CO_NO);
    	//iRet = settle.LoginC("0571","001186","001186");
    	
    	if (iRet == 0)
    	{
    		//iRet = settle.RefundOrder("20050607","004003","0.1","test","AaBb123601234569");
    	}
    	else
    	{
    		
    	}
    	
    	StringBuffer sBuffer= new StringBuffer();
    	int result = settle.QuerySingleOrder(UtilDate.getDate(),orderNo.substring(8, 18),sBuffer);
    	
    	//"2016062715584400753274260"
    	
    	String err = settle.GetLastErr(result);
    	settle.Logout();
    	return result;
    }

    /**
     * @param order
     * @param ip
     * @return
     */
    public String payDispose(long orderNo, String ip) {
        // 建立请求
        Map<String, String> sParaTemp = chargeFacade.payDispose(orderNo, ip);
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
        logger.info("redirect pay, orderNo:{}, ip:{}, sHtmlText:{}", orderNo, ip, sHtmlText);
        return makeHttpBodyComponent(sHtmlText);
    }
    
    private String makeHttpBodyComponent(String bodyContent) {
        StringBuilder sbBody =
            new StringBuilder(
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>支付宝即时到账交易接口</title></head><body>");
        sbBody.append(bodyContent);
        sbBody.append("</body></html>");
        return sbBody.toString();
    }

    /**
     * 
     * @param request
     * @return
     */
    public String payCallbackDispose(UriParams uriParams) {
        if (StringUtils.equals(uriParams.getSingle("sign_type"), "RSA")) {
            return chargeFacade.mobilePayCallbackDispose(uriParams);
        } else {
            return chargeFacade.payCallbackDispose(uriParams);
        }
    }
    public Map<String, String> queryPayResultBankCard(String orderNo, UserDetail userDetail) {
    	Map<String, String> data = new HashMap<String, String>();
    	OrderNew order = orderService.getUserOrderNewByNo(userDetail.getUserId(), orderNo);
    	if(order != null){
    		if(order.getOrderStatus().equals(OrderStatus.PAID) && order.getPaymentType() != 0 && order.getPaymentNo() != null && order.getPaymentNo().length() > 0){
    			data.put("payResult", "0");
    		} else {	
    			data = queryPayResult(order);
    		}

    	}else{
    		data.put("payResult", "99");	
    	}
    	return data;
    }
    public Map<String, String> queryPayResultBankCardByServiceId(String serviceId, UserDetail userDetail) {
    	Map<String, String> data = new HashMap<String, String>();
    	ServiceTicket serviceTicket = afterSaleService.getServiceTicketDetailById(userDetail.getUserId(), Long.parseLong(serviceId));
    	if(serviceTicket != null && serviceTicket.getProcessOrderNo() > 0){
    		
    		OrderNew order = orderService.getUserOrderNewByNo(userDetail.getUserId(), serviceTicket.getProcessOrderNo() + "");
    		if(order != null){
    			if(order.getOrderStatus().equals(OrderStatus.PAID) && order.getPaymentType() != 0 && order.getPaymentNo() != null && order.getPaymentNo().length() > 0){
    				data.put("payResult", "0");
    			} else {	
    				data = queryPayResult(order);
    			}
    			
    		}else{
    			data.put("payResult", "99");	
    		}
    	} else {
    		data.put("payResult", "99");	 
    	}
    	return data;
    }
    /**
     * 
     * @param request
     * @return
     */
    public Map<String,String> payReturnDisposeBankCard(UriParams uriParams) {
    	String merchantPara = uriParams.getSingle("MerchantPara");
    	Map<String,String> resultMap = new HashMap<String,String>();
    	resultMap.put("payResult", "");
		resultMap.put("failReason","");
    	resultMap.put("orderNo", "");
//    	logger.info("merchantPara:" + merchantPara);
    	if( merchantPara != null && merchantPara.length() > 0 ){
    		
    		merchantPara = merchantPara.replaceAll("%3D", "=");
    		String[] splitArray = merchantPara.split("=");
    		//logger.info("splitArray length:" + splitArray.length);
    		
    		if( splitArray != null && splitArray.length >= 2){
    		//logger.info("splitArray[1]:" + splitArray[1]);
    		
    		String out_trade_no = splitArray[1];
    		OrderNew order = orderService.getUserOrderNewByNo(out_trade_no);
    		if (order == null) {
    				//order = orderService.getOrderByNo("2015120710203249283722189");
    				String msg = "can not find order11 of " + out_trade_no;
    				logger.error(msg);
    				return resultMap;
    				
    			}else{
    				Map<String,String> resultTemp = queryPayResult(order);
    				if(resultTemp != null ){
    					resultTemp.put("orderNo" , order.getOrderNo()+"" );
    					return resultTemp;
    				}
    				//return queryPayResult(order);
    			}
    		
    		}
    	}
    		return resultMap;
    }
    
    /**
     * 银行卡支付回调
     * @param request
     * @return
     */
    public String bankcardPayCallbackDispose(UriParams uriParams) {
    		return chargeFacade.mobileBankcardPayCallbackDispose(uriParams);
    	
    }
    
    /**
     * 银行卡签约回调
     * @param request
     * @return
     */
    public String bankcardSignCallbackDispose(UriParams uriParams) {
    	
    	return chargeFacade.mobileBankcardSignCallbackDispose(uriParams);
    	
    }

    public Map<String, Object> weixinPayDispose(long orderNo, String ip) {
//    	OrderNew orderNew = orderService.queryOrderNewFromOld(order);
    	 OrderNew orderNew = orderService.getUserOrderNewByNo(String.valueOf(orderNo));
        return weixinChargeFacade.genPayData4AppNew(orderNew, ip);
    }
    public Map<String, Object> weixinPayDisposeNew(OrderNew orderNew, String ip) {
    	return weixinChargeFacade.genPayData4AppNew(orderNew, ip);
    }

    public String weixinPayCallbackDispose(String requestBody,  PaymentType paymentType) {
        return weixinChargeFacade.weixinPayCallbackDispose(requestBody,    paymentType);
    }
    

}
