package com.yujj.web.controller.wap.pay2;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.UserService;
import com.yujj.business.service.WeiXinPayService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.WapPublicConstants;
import com.jiuyuan.constant.account.ThirdAppType;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.account.BindUserRelation;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.product.Product;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 微信公众号支付
 * 参考网络文章：http://www.jb51.net/article/70537.htm
 * @author zhaoxinglin
 * 一、页面请求调用微信的统一下单接口，获得prepay_id返回页面。
 * 二、通过页面JS请求微信支付接口（注：只能在微信自带的浏览器使用）
 * 
 */
@Controller
@RequestMapping("/m/pay")
public class WapPayController {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Logger logger = LoggerFactory.getLogger(WapPayController.class);
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private WeiXinPayService weiXinPayService;
    
    
    /**
     * 初始化Config配置（ 微信JSSDK在页面加载时）
     * 官方文档：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
     */
    @RequestMapping("/wxInitConfig")
    public JsonResponse wxInitConfig(@RequestParam("thisUrl")  String thisUrl, HttpServletRequest request, HttpServletResponse response) {
    	 Map<String, String> InitConfigDate = weiXinPayService.getInitConfigDate(thisUrl);
 		 JsonResponse jsonResponse = new JsonResponse();
         jsonResponse.setData(InitConfigDate);
         String jsonStr = JSON.toJSONString(InitConfigDate);
         logger.info("wxInitConfig返回页面参数json："+jsonStr);
         return jsonResponse.setSuccessful();
    }
    
    /**
     * 订单统一支付
     * 官方文档：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
     */
    @RequestMapping("/wxPay")
    public JsonResponse wapAccessToken(@RequestParam("orderNo") String orderNo,@RequestParam("payAmount") String payAmount,
    		UserDetail userDetail,@ClientIp String ip, HttpServletRequest request, HttpServletResponse response) {
         JsonResponse jsonResponse = new JsonResponse();
         double payAmountDouble = Double.parseDouble(payAmount);
        if(payAmountDouble <=0 ){
        	 return jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_PAY_AMOUNT_MUST_GREATER_ZERO);//支付金额需大于0元
        }
        long userId = userDetail.getUserId();
        logger.info("userId{}",userId);
        if(userId <= 0){
        	return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);//未登录，请先登录
        }
        User currentUser = userDetail.getUser();
       
	      //生成订单号 ：年月日+6位随机数
	//        String order_no = sdf.format(date)+""+((int)(Math.random()*99999+100000));
//	        String user_ip = WapPayUtil.getIpAddr(request); //用户ip
	       
	        //1、组装订单发送参数数据
	       
	        //2、调用微信下单接口
	        String weixinId = currentUser.getWeixinId();
	        
	        logger.info("weixinId：[{}]",weixinId);
	        ThirdAppType thirdAppType= ThirdAppType.WEIXIN_PUBLIC_NUM1;
	        BindUserRelation bindUserRelation = userService.getBindUserRelationByUid(weixinId);
	        if(bindUserRelation == null){
	        	 return jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_GET_BIND_ERROR);
	        }
	        logger.info("bindUserRelation:{}",bindUserRelation.toString());
	        String publicOpenId = bindUserRelation.getOpenId();
	        
	        String  productName = "";
	        OrderNew orderNew = orderService.getUserOrderNewByNo(orderNo);//订单

	        //需要支付金额 = 折后总价 + 邮费总价//单位分
	        int total_fee = orderNew.getUserPracticalPayMoneyOfFen();
	        //TODO 为了测试方便暂时采用页面传递过来的数据
	        List<OrderItem> orderItems = orderService.getOrderNewItemsByOrderNO(userId, Long.parseLong(orderNo));
	        if(orderItems.size()>0){
	        	OrderItem orderItem = orderItems.get(0);
	        	long productId = orderItem.getProductId();
	        	Product product = productService.getProductById(productId);
	        	productName = product.getName();
	        }
	        
	    try { 
	        String result = weiXinPayService.unifiedorder(orderNo,productName, total_fee, publicOpenId, ip);
	        //解析微信下单接口返回的XML信息
	        WxPayResultData resultData = weiXinPayService.buildWxPayResultData(result);
	        logger.info("获取统一订单返回结果 result_code:"+resultData.getResult_code()+"---return_code:"+resultData.getReturn_code()+"-----return_msg:"+resultData.getReturn_msg());
	        if("SUCCESS".equals(resultData.getResult_code()) && "SUCCESS".equals(resultData.getReturn_code())){
	            //TODO 保存在支付记录日志到表中
	        	 logger.info("保存在支付记录日志到表中：");
	            //生成sign
	            SortedMap<Object, Object> signMap = weiXinPayService.buildSignData(resultData);
	            jsonResponse.setData(signMap);
	            String jsonStr = JSON.toJSONString(signMap);
	            logger.info("返回页面参数json："+jsonStr);
	            System.out.println("返回页面参数json："+jsonStr);
	        }else{
	        	logger.info("微信支付错误");
	        	System.out.println("微信支付错误1111");
	        	return jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_PAY_UNIFIEDORDER_ERROE);//调用微信出现错误
	        }
	         } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("微信支付错误",e);
	        System.out.println("微信支付错误");
	        return jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_PAY_UNIFIEDORDER_ERROE);//调用微信出现错误
	    }
  		return jsonResponse.setSuccessful();
     }


    


    

	

  
    /**
     * 微信支付异步通知回调（暂时未使用，现用以前支付接口回调通知）
     * @return
     */
    @RequestMapping("wxPayNotify")
    @ResponseBody
    public String wxPayNotify(HttpSession session,HttpServletRequest request){
        try {
            //解析微信支付回调发来的请求数据（XML） 
            Map<String, String> requestMap = weiXinPayService.parseXml2Map(request);
            SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
            logger.info("微信支付回调发来的请求数据request参数：");
            for (Entry<String, String> iterable_element : requestMap.entrySet()) {
                if(!iterable_element.getKey().equals("sign")){
                    parameters.put(iterable_element.getKey(), iterable_element.getValue());
                }
                logger.info("key:"+iterable_element.getKey()+"---value:"+iterable_element.getValue());
            }
            //失败
            if(!requestMap.get("return_code").equals("SUCCESS")){
            	logger.info("微信支付通知返回错误信息:"+requestMap.get("return_msg"));
                return weiXinPayService.notifyReturnXML("SUCCESS", "");
            }
            
            String my_sign = WxSign.createSign(parameters, WapPublicConstants.WX_PAY_KEY);//生成签名
            String wx_sign = requestMap.get("sign");   //获得微信的签名
            if(!WxSign.checkSign(my_sign, wx_sign)){//验证签名 
            	logger.info("签名验证失败my_sign:"+my_sign+",,,,,,wx_sign:"+wx_sign);
                return "FAIL";
            }
            
            
            //根据订单号获取订单
//            String hql = "from CgRechargeRecord crr where crr.orderNo = ?";
//            CgRechargeRecord rechargeRecord = new CgRechargeRecord();//rechargeRecordService.findEntity(hql, new Object[]{requestMap.get("out_trade_no")});
//            logger.info("获取订单中userid："+rechargeRecord.getUserId());
//            CgUser user = new CgUser();//userService.findEntity("from CgUser cu where cu.id = ?", new Object[]{rechargeRecord.getUserId()});
//            logger.info("获得充值用户名称："+user.getName());
            //判断订单是否成功
            if(requestMap.get("result_code").equals("SUCCESS")){  
            	String jsonStr = JSON.toJSONString(requestMap);
                //业务逻辑
            	//修改订单状态等
                logger.error("TODO 待修改订单状态等逻辑,+jsonStr:"+jsonStr);
            	logger.error("订单已经成功  ，在这里编写业务逻辑");
            }
            return weiXinPayService.notifyReturnXML("SUCCESS", "");          
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("接收微信支付回调出现错误",e);
            return "FAIL";
        }
    }
   
}
