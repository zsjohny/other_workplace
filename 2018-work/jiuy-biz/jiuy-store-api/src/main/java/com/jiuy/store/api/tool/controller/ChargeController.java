/**
 * 
 */
package com.jiuy.store.api.tool.controller;

import java.util.HashMap;
import java.util.Map;
import com.jiuyuan.util.BizUtil;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.ChargeDelegator;
import com.store.service.ChargeFacade;
import com.yujj.util.uri.UriParams;

import javax.annotation.Resource;

/**
 * @author LWS 支付成功后回调
 */
@Controller
@RequestMapping("/pay")
public class ChargeController {
    private static final Logger logger = LoggerFactory.getLogger("PAY");
    
    @Autowired
    private ChargeDelegator chargeDelegator;
    
    @Autowired
    private ChargeFacade chargeFacade;
    
    @Autowired
    private MemcachedService memcachedService;

    @Resource(name = "couponServerNew")
    private ICouponServerNew couponServerNew;


    @Autowired
    private SupplierOrderMapper supplierOrderMapper;


    @RequestMapping("/notify_url")
    @ResponseBody
    public String postPayDisposeAsyn(UriParams uriParams,ClientPlatform clientPlatform) {
        logger.info("app支付异步1");
        String orderNo = chargeDelegator.payCallbackDispose(uriParams, clientPlatform.getVersion());
        try{
            logger.info("app支付异步1 发送优惠券!");
            couponServerNew.grantOrder(orderNo);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
    
    @RequestMapping("/notify_url/weixin")
    @ResponseBody
    public String weixinPayCallback(@RequestBody String requestBody ,ClientPlatform clientPlatform) {
        logger.info("app支付异步2");
        String orderNo =  chargeDelegator.weixinPayCallbackDispose(requestBody, true, clientPlatform.getVersion());
        try{
            logger.info("app支付异步2 发送优惠券!");
            couponServerNew.grantOrder(orderNo);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }





    @RequestMapping("/notify_url/weixin_native")
    @ResponseBody
    public String weixinPayCallbackNative(@RequestBody String requestBody ,ClientPlatform clientPlatform) {
        chargeDelegator.weixinPayCallbackDispose(requestBody, false, clientPlatform.getVersion());
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }



    @RequestMapping("/directpay/success/auth")
    @ResponseBody
    public JsonResponse paySuceess(@RequestParam(value = "order_no")String orderNo, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	long userId = userDetail.getId();
//    	
//    	if(orderNo != null && orderNo.length() >= 24 ){
//    		data = chargeFacade.paySuceess(orderNo, userId);  //老版本订单号
//    	}else {
//    	}
    		data = chargeFacade.paySuceessNew(orderNo, userId); //新版本订单号
        
        // 添加add by dongzhong 20160530    	
        	// 活动抽奖标志
            String groupKey = MemcachedKey.GROUP_KEY_ACTIVITY;
            memcachedService.set(groupKey, orderNo, DateConstants.SECONDS_PER_DAY, 1);
        
    	return jsonResponse.setSuccessful().setData(data);
    }



//    测试接口 可删除
//    @RequestMapping( "test" )
//    @ResponseBody
    public JsonResponse 支付后修改订单状态测试(String orderNo) {
        BizUtil.test (378,"支付后修改订单状态测试");
        String data = chargeFacade.测试会员支付的逻辑 (orderNo);
        return JsonResponse.getInstance ().setData (data);
    }
}
