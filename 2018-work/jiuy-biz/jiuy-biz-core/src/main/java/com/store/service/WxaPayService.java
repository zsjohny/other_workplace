/**
 * 
 */
package com.store.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.WapPublicConstants;
import com.jiuyuan.constant.WxaPayConstants;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.EncodeUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.yujj.web.controller.wap.pay2.RefundData;
import com.yujj.web.controller.wap.pay2.WapPayHttpUtil;
import com.yujj.web.controller.wap.pay2.WxPayResultData;
import com.yujj.web.controller.wap.pay2.WxPaySendData;
//import com.yujj.web.controller.wap.pay2.WapPayHttpUtil;
//import com.yujj.web.controller.wap.pay2.WxPayResultData;
//import com.yujj.web.controller.wap.pay2.WxPaySendData;
//import com.yujj.web.controller.wap.pay2.WxSign;
import com.yujj.web.controller.wap.pay2.WxSign;

/**
 * 微信支付请求微信获取数据service  
 * @author zhaoxinglin
 *
 * @version 2017年5月4日下午6:07:36
 */
@Service
public class WxaPayService {
    private static final Logger logger = LoggerFactory.getLogger(WxaPayService.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	//公众账号ID
	public static String APPID = "wx176676190b6d64ae";
	//微信支付分配的商户号
	public static String MCH_ID =  "1309737801";;
	//异步回调地址
	public static String NOTIFY_URL = "https://wxalocal.yujiejie.com/pay/notify_url/weixin_public.do";
	//商户支付密钥API Key。审核通过后，在微信发送的邮件中查看
	public static String WX_PAY_KEY = "yjj7chTcyBgGNPM3Yk2cESzmes53gyjj";
	//公众账号ID 
	public static String SECRET = "fd7a5b43f81e6d9a7f09fd55b29a839d";
	
	
	
	public static String authorizeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize";
	public static String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public static String userinfoUrl = "https://api.weixin.qq.com/sns/userinfo";
    
    @Autowired
    private MemcachedService memcachedService;
    
  	/** 
       * 解析微信支付回调发来的请求数据（XML） 
       *  
       * @param request 
       * @return 
       * @throws Exception 
       */  
      @SuppressWarnings("unchecked")  
      public  Map<String, String> parseXml2Map(HttpServletRequest request) throws Exception {  
          // 将解析结果存储在HashMap中  
          Map<String, String> map = new HashMap<String, String>();  

          // 从request中取得输入流  
          InputStream inputStream = request.getInputStream();  
          // 读取输入流  
          SAXReader reader = new SAXReader();  
          Document document = reader.read(inputStream);  
          // 得到xml根元素  
          Element root = document.getRootElement();  
          // 得到根元素的所有子节点  
          List<Element> elementList = root.elements();  

          // 遍历所有子节点  
          for (Element e : elementList)  
              map.put(e.getName(), e.getText());  

          // 释放资源  
          inputStream.close();  
          inputStream = null;  

          return map;  
      }

      /**
       * 微信支付回调接口返回参数
       * @param return_code
       * @param return_msg
       * @return
       */
      public  String notifyReturnXML(String return_code, String return_msg) {
          return "<xml><return_code><![CDATA[" + return_code
                  + "]]></return_code><return_msg><![CDATA[" + return_msg
                  + "]]></return_msg></xml>";
      }
      
    /**
	 * 组装签名数据
	 * @param resultData
	 * @return
	 */
    public  SortedMap<Object, Object> buildSignData(WxPayResultData resultData,String payKey) {
		SortedMap<Object,Object> signMap = new TreeMap<Object,Object>();
		signMap.put("appId", resultData.getAppid()); //公众号id
		signMap.put("timeStamp", WxSign.getTimeStamp());//时间戳 		timeStamp	String	是	时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
		signMap.put("nonceStr", resultData.getNonce_str());//随机字符串	nonceStr	String	是	随机字符串，长度为32个字符以下。
		signMap.put("package", "prepay_id="+resultData.getPrepay_id());//订单详情扩展字符串  packageValue	package	String	是	统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*
		signMap.put("signType", "MD5");//签名方式		signType	String	是	签名算法，暂支持 MD5
		String paySign = WxSign.createSign(signMap,payKey);	//WapPublicConstants.WX_PAY_KEY
		logger.info("生成的签名PaySIGN:"+paySign);
		signMap.put("paySign", paySign);//签名	paySign	String	是	签名,具体签名方案参见小程序支付接口文档;
		return signMap;
	}
    /**
     * 解析微信下单接口返回的XML信息
     * @param result
     * @return
     */
	public  WxPayResultData buildWxPayResultData(String result) {
		 logger.info("调用微信下单接口返回XML result:"+result);
		XStream xstream = new XStream(new DomDriver());
        xstream.alias("xml", WxPayResultData.class);
        WxPayResultData resultData = (WxPayResultData) xstream.fromXML(result);
		return resultData;
	}
	
	/**
     * 发送请求调用微信统一下单
     * @param data
     * @param key
     * @return
     */
	public  String unifiedorder(String orderNo, String  productName,int total_fee, String publicOpenId,String businessName, String appId,String mchId,String payKey,String user_ip){
    	
    	 WxPaySendData data = buildWxPaySenData(orderNo, productName,total_fee, publicOpenId,businessName,appId,mchId, user_ip);
    	
//    	String key = WapPublicConstants.WX_PAY_KEY;
        //统一下单支付
        String returnXml = null;
        try {
          //生成sign签名
          SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
          parameters.put("appid", data.getAppid()); 
          parameters.put("attach", data.getAttach());
          parameters.put("body", data.getBody());
          parameters.put("mch_id", data.getMch_id());
          parameters.put("nonce_str", data.getNonce_str());
          parameters.put("notify_url", data.getNotify_url());
          parameters.put("out_trade_no", data.getOut_trade_no());
          parameters.put("total_fee", data.getTotal_fee());
          parameters.put("trade_type", data.getTrade_type());
          parameters.put("spbill_create_ip", data.getSpbill_create_ip());
          parameters.put("openid", data.getOpenid());
          parameters.put("device_info", data.getDevice_info());
          parameters.put("time_start", data.getTime_start());

          logger.info("SIGN:"+WxSign.createSign(parameters,payKey));
          data.setSign(WxSign.createSign(parameters,payKey));

          logger.info("获取回调函数={}"+data.getNotify_url());
          XStream xs = new XStream(new DomDriver("UTF-8",new XmlFriendlyNameCoder("-_", "_")));
          xs.alias("xml", WxPaySendData.class);
          String xml = xs.toXML(data);
//          logger.info("统一下单xml为:\n" + xml);
//        HttpClientUtil util = HttpClientUtil.getInstance();

          returnXml = WapPayHttpUtil.sendPostHttp("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
//        returnXml = util.doPostForString("https://api.mch.weixin.qq.com/pay/unifiedorder", null, xml);
//          logger.info("统一下单返回结果:" + returnXml);
        } catch (Exception e) {
          e.printStackTrace();
          logger.error("调用统一下单方法错误",e);
        } 
        return returnXml;
    }
	
	/**
     * 组装订单发送参数数据
     * @param orderNo
     * @param publicOpenId  注意这里是openid
     * @param user_ip
     * @return
     */
	private  WxPaySendData buildWxPaySenData(String orderNo,String  productName,int total_fee, String publicOpenId, String businessName,String appId,String mchId,String user_ip) {
		WxPaySendData sendData = new WxPaySendData();
        sendData.setAppid(appId);//WapPublicConstants.APPID
        sendData.setAttach(businessName);
        sendData.setBody(productName);
        sendData.setMch_id(mchId);//WapPublicConstants.MCH_ID
        sendData.setNonce_str(WxSign.getNonceStr());
        sendData.setNotify_url(WxaPayConstants.NOTIFY_URL);//这里使用APP微信异步回调//WapPayConstants.PAY_NOTIFY_URL);
        sendData.setOut_trade_no(orderNo);
        sendData.setTotal_fee(total_fee);//单位：分
        sendData.setTrade_type("JSAPI");
        sendData.setSpbill_create_ip(user_ip);
        sendData.setOpenid(publicOpenId);//payUser.getWechatId()
        Date date = new Date();
        sendData.setTime_start(sdf.format(date));
		return sendData;
	}		
    
	
	
	/**
	 * 获得初始化JSSDK对象数据
	 * @param thisUrl
	 * @return
	 */
	public  Map<String, String> getInitConfigDate(String thisUrl) { 
		
	    
	    //2、获取Ticket  TODO 这里需要缓存
	    String jsapi_ticket = getTicket();  
	    if(StringUtils.isEmpty(jsapi_ticket)){
	    	logger.info("WxRequestUtil.getInitConfigDate jsapi_ticket must is not null jsapi_ticket:"+jsapi_ticket);
	    	return null;
	    }
	    //3、时间戳和随机字符串  
	    String nonceStr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串  
	    String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳  
	      
	    //4、获取url  
	    String url= EncodeUtil.decodeURL(thisUrl);
	      
	    //5、将参数排序并拼接字符串  
	    String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;  
	    logger.info("WxRequestUtil.getInitConfigDate 将参数排序并拼接字符串 str:"+str);  
	    //6、将字符串进行sha1加密  
	    String signature =SHA1(str);  
	    logger.info("WxRequestUtil.getInitConfigDate 参数："+str+"签名："+signature);  
	    Map<String, String> confingMap = new HashMap<String, String>();
	    confingMap.put("appId", WapPublicConstants.APPID); //公众号id
	    confingMap.put("timestamp", timestamp);////WxSign.getTimeStamp());//时间戳
	    confingMap.put("nonceStr", nonceStr);////WxSign.getNonceStr());//随机字符串
	    confingMap.put("signature", signature);////签名
	    logger.info("WxRequestUtil.getInitConfigDate 返回的Map数据："+confingMap.toString());  
	    return confingMap;
	}  
	 
	
	/**
	 * 第一步、获取access_token 
	 * 因为只在getTicket中被调用，getTicket方法中已经做了缓存所以这里就不进行缓存了，每次都去最新的，
	 * 如果其他处有需要获取access_token的请注意不要频发调用
	 * AccessToken有效期2小时 = 7200的是有效期7200秒  ，
	 * https://mp.weixin.qq.com/wiki/11/0e4b294685f817b95cbed85ba5e82b8f.html
	 * 公众平台的API调用所需的access_token的使用及生成方式说明：
	 *  1、为了保密appsecrect，第三方需要一个access_token获取和刷新的中控服务器。而其他业务逻辑服务器所使用的access_token均来自于该中控服务器，不应该各自去刷新，否则会造成access_token覆盖而影响业务；
	 *	2、目前access_token的有效期通过返回的expire_in来传达，目前是7200秒之内的值。中控服务器需要根据这个有效时间提前去刷新新access_token。在刷新过程中，中控服务器对外输出的依然是老access_token，此时公众平台后台会保证在刷新短时间内，新老access_token都可用，这保证了第三方业务的平滑过渡；
	 *	3、access_token的有效时间可能会在未来有调整，所以中控服务器不仅需要内部定时主动刷新，还需要提供被动刷新access_token的接口，这样便于业务服务器在API调用获知access_token已超时的情况下，可以触发access_token的刷新流程。
	 * @return
	 */
	private  String getAccessToken() {
		 String access_token = null; 
		 
	    String grant_type = "client_credential";//获取access_token填写client_credential   
	    String AppId=WapPublicConstants.APPID;//第三方用户唯一凭证  
	    String secret=WapPublicConstants.SECRET;//第三方用户唯一凭证密钥，即appsecret   
	    //这个url链接地址和参数皆不能变
	    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+AppId+"&secret="+secret;  
	    logger.info("WxRequestUtil.getAccessToken url:"+url);  
	    try {  
	        URL urlGet = new URL(url); 
	        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();  
	        http.setRequestMethod("GET"); // 必须是get方式请求  
	        http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
	        http.setDoOutput(true);  
	        http.setDoInput(true);  
	        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒  
	        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒  
	        http.connect();  
	        InputStream is = http.getInputStream();  
	        int size = is.available();  
	        byte[] jsonBytes = new byte[size];  
	        is.read(jsonBytes);  
	        String message = new String(jsonBytes, "UTF-8");  
//	        JSONObject demoJson = JSONObject.fromObject(message);  
//	        System.out.println("JSON字符串："+demoJson);  
//	        access_token = demoJson.getString("access_token");  
	        
	        Map<String, Object> map = JSON.parseObject(message,new TypeReference<Map<String, Object>>(){} );
	        if(map.get("access_token") != null){
	        	 access_token = map.get("access_token").toString();
	        	 logger.info("WxRequestUtil.getAccessToken map:"+map.toString());
	        	 //access_token 放到缓存中 access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token。开发者需要进行妥善保存。access_token的存储至少要保留512个字符空间。access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效。
	        	 //有效期 微信说明access_token有效期为7200秒
	        }else{
//		         {errcode=45009, errmsg=reach max api daily quota limit hint: [IcjZcA0513e578]}
//		                     这个错误意思是公众号接口访问次数到达了上限，一日内无法再进行访问，无法解决。没办法，这是微信为了减少服务器负载的硬性限制。
	        	logger.info("WxRequestUtil.getAccessToken access_token缓存不存在从微信获取失败，返回信息map:"+map.toString());  
	        }
	        is.close();  
	    } catch (Exception e) {  
	            e.printStackTrace();  
	    }  
	    
	    return access_token;  
	}  
	
	
	
	
	/**
	 * 第二步、获取jsapi_ticket
	 * 注意微信接口调用测试有显示，要从缓存获取
	 * @param access_token
	 * @return
	 */
	public  String getTicket( ) {  
		String ticket = null;
		 //1、注意在切换公众号时或切换测试和正式时需要重新获取ticket否则会js会出现config:invalid signature，这时
		//TODO 经分析这里会初选偶尔config:invalid signature的情况，因为在ticket失效时会有多个请求同时判断缓存无ticket，都去微信获取ticket，后获取的ticket会导致前面获取的ticket失效，后期需要考虑方案进行优化，可以先加锁机制进行处理
		
		String groupKey = MemcachedKey.GROUP_KEY_WENXIN_PUBLIC;
		String key = "ticket_by_appid_"+WapPublicConstants.APPID;

		Object obj = memcachedService.get(groupKey, key);
		if (obj != null) {
			ticket = (String) obj;
			if(StringUtils.isNotEmpty( ticket)){
				  logger.info("WxRequestUtil.getAccessToken ticket缓存已存在直接返回了ticket:"+ticket);  
				return ticket;  
			}
		}

		//1、获取AccessToken  
	    String accessToken = getAccessToken();  
	    if(StringUtils.isEmpty(accessToken)){
	    	logger.error("WxRequestUtil.getTicket accessToken must is not null accessToken:"+accessToken);
	    	return null;
	    }
		
	    String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ accessToken +"&type=jsapi";//这个url链接和参数不能变  
	    try {  
	        URL urlGet = new URL(url);  
	        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();  
	        http.setRequestMethod("GET"); // 必须是get方式请求  
	        http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
	        http.setDoOutput(true);  
	        http.setDoInput(true);  
	        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒  
	        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒  
	        http.connect();  
	        InputStream is = http.getInputStream();  
	        int size = is.available();  
	        byte[] jsonBytes = new byte[size];  
	        is.read(jsonBytes);  
	        String message = new String(jsonBytes, "UTF-8");  
	        Map<String, Object> map = JSON.parseObject(message,new TypeReference<Map<String, Object>>(){} );
	       
	        
	        if(map.get("ticket") != null){
	        	 ticket = map.get("ticket").toString();
	        	 //ticket 放到缓存中
	          	 //有效期 微信说明access_token有效期为7200秒
	          	 int expiry = 7000;
	          	 memcachedService.set(groupKey, key, expiry, ticket);
	          	 logger.info("WxRequestUtil.getAccessToken ticket缓存不存在从微信获取成功后返回了ticket:"+ticket);  
	        }else{
	        	logger.error("WxRequestUtil.getTicket ticket缓存不存在从微信获取失败，返回信息map:"+map.toString());  
	        	//如果根据
	        	
	        	
	        	
	        }
	        is.close();  
	    } catch (Exception e) {  
	            e.printStackTrace();  
	    }  
	    return ticket;  
	}  
	
	
	
	
	
	/**
	 * 拿到了jsapi_ticket之后就要参数名排序和拼接字符串，并加密了。以下为sha1的加密算法：
	 * 加密算法转载自：http://www.open-open.com/lib/view/open1392185662160.html
	 * @param decript
	 * @return
	 */
	public  String SHA1(String decript) {  
	    try {  
	        MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");  
	        digest.update(decript.getBytes());  
	        byte messageDigest[] = digest.digest();  
	        // Create Hex String  
	        StringBuffer hexString = new StringBuffer();  
	        // 字节数组转换为 十六进制 数  
	            for (int i = 0; i < messageDigest.length; i++) {  
	                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);  
	                if (shaHex.length() < 2) {  
	                    hexString.append(0);  
	                }  
	                hexString.append(shaHex);  
	            }  
	            return hexString.toString();  
	   
	        } catch (NoSuchAlgorithmException e) {  
	            e.printStackTrace();  
	        }  
	        return "";  
	}
	
	private RefundData buildRefundData(String appId,String mchId,String orderNo,int total_fee,int refund_fee) {
		RefundData sendData = new RefundData();
        sendData.setAppid(appId);//WapPublicConstants.APPID
        sendData.setMch_id(mchId);//WapPublicConstants.MCH_ID
        sendData.setNonce_str(WxSign.getNonceStr());
        sendData.setOut_trade_no(orderNo);
        long time = System.currentTimeMillis();
        sendData.setOut_refund_no(String.valueOf(time));
        sendData.setTotal_fee(total_fee);//单位：分
        sendData.setRefund_fee(refund_fee);//单位：分
        sendData.setOp_user_id(mchId);
		return sendData;
	}
	

	/**
	 * 退款
	 * @return
	 */
	public String refund(String orderNo, String bindWeixin, String appId, String mchId, String payKey, String ip,int total_fee,int refund_fee) {
		
		RefundData data = buildRefundData( appId, mchId, orderNo, total_fee, refund_fee);
    	
//    	String key = WapPublicConstants.WX_PAY_KEY;
        //统一下单支付
        String returnXml = null;
        try {
          //生成sign签名
          SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
          parameters.put("appid",appId); 
//          parameters.put("attach", data.getAttach());
//          parameters.put("body", data.getBody());
          parameters.put("mch_id", mchId);
          parameters.put("nonce_str", data.getNonce_str());
          parameters.put("out_trade_no", data.getOut_trade_no());
          parameters.put("out_refund_no", data.getOut_refund_no());
          parameters.put("total_fee", data.getTotal_fee());
          parameters.put("refund_fee", data.getRefund_fee());
          parameters.put("op_user_id", data.getOp_user_id());
          logger.info("SIGN:"+WxSign.createSign(parameters,payKey));
          data.setSign(WxSign.createSign(parameters,payKey));

          XStream xs = new XStream(new DomDriver("UTF-8",new XmlFriendlyNameCoder("-_", "_")));
          xs.alias("xml", RefundData.class);
          String xml = xs.toXML(data);
          logger.info("退款xml为:\n" + xml);
          System.out.println("退款xml为:\n" + xml);
//        HttpClientUtil util = HttpClientUtil.getInstance();

//          returnXml = WapPayHttpUtil.sendPostHttp("https://api.mch.weixin.qq.com/secapi/pay/refund", xml);
          returnXml = WapPayHttpUtil.clientCustomSSL("https://api.mch.weixin.qq.com/secapi/pay/refund", xml,mchId);
//        returnXml = util.doPostForString("https://api.mch.weixin.qq.com/pay/unifiedorder", null, xml);
          logger.info("退款返回结果:" + returnXml);
        } catch (Exception e) {
          e.printStackTrace();
          logger.error("调用统一下单方法错误",e);
          System.out.println("调用统一下单方法错误");
        } 
        return returnXml;
    }

	

	

}
