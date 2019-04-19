package com.jiuy.wxa.api.controller.wxa;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.service.MemberService;
import com.store.service.ShopMemberOrderService;
import com.store.service.StoreWxaService;
import com.store.service.store.ServiceAdviceFacade;

/**
 * 服务通知
 */
@Controller
@RequestMapping("/miniapp/advice")
public class WxaServiceAdviceController {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Logger logger = LoggerFactory.getLogger(WxaServiceAdviceController.class);
    
    @Autowired
    ServiceAdviceFacade serviceAdviceFacade;
    @Autowired
    MemberService memberService;
    @Autowired
	StoreWxaService storeWxaService;
	@Autowired
	private ShopMemberOrderService shopMemberOrderService;
    
    /**
     * 发送待付款模板通知
     * http://dev.yujiejie.com:31080/miniapp/advice/waitPayAdvice.json?orderId=0
     * @return
     */
    @RequestMapping("/waitPayAdvice")
    @ResponseBody
    public JsonResponse wxaPay(long orderId,@ClientIp String ip, ClientPlatform client, HttpServletRequest request, HttpServletResponse response) {
    	JsonResponse jsonResponse = new JsonResponse();
   	 	try {
   	 		if(orderId>0){
   	 			//发送待付款通知给微信会员
   	 		ShopMemberOrder shopMemberOrder = shopMemberOrderService.getMemberOrderById(orderId);
   	 		int buyWay = shopMemberOrder.getBuyWay();//购买方式 0：普通  1：团购  2：秒杀
	   	 	if(buyWay == 0){
				serviceAdviceFacade.waitPayAdvice(orderId);
			}else{
				logger.info("团购和秒杀无需发送待付款通知");
			}
   	 			
   	 		}else{
   	 			logger.info("发送待付款通知时orderId不能为空");
   	 		}
   	 		return jsonResponse.setSuccessful();
   	 	} catch (Exception e) {
   	 		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }

}
