package com.jiuy.store.tool.controller.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.store.service.store.ServiceAdviceFacade;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 服务通知
 */
@Controller
@RequestMapping("/mobile/advice")
public class ServiceAdviceController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceAdviceController.class);
    Log log = LogFactory.get();
    
    
    @Autowired
    ServiceAdviceFacade serviceAdviceFacade;
	
//    /**
//     * 付款成功通知
//     */
//    @RequestMapping("/paySuccessAdvice")
//    @ResponseBody
//    public JsonResponse paySuccessAdvice(@RequestParam(required = true) int mark,String searchWord,int current, int size , 
//    		UserDetail userDeail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	
//    	serviceAdviceFacade.paySuccessAdvice(1131);
//   	 	//返回数据
//   	 	return jsonResponse.setSuccessful();
//    }
    
//    /**
//     * 订单取消通知
//     */
//    @RequestMapping("/orderCancelAdvice")
//    @ResponseBody
//    public JsonResponse orderCancelAdvice(@RequestParam(required = true) int mark,String searchWord,int current, int size , 
//    		UserDetail userDeail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	serviceAdviceFacade.orderCancelAdvice(1131);
//   	 	//返回数据
//   	 	return jsonResponse.setSuccessful();
//    }
//    
//    /**
//     * 待付款通知
//     */
//    @RequestMapping("/waitPayAdvice")
//    @ResponseBody
//    public JsonResponse waitPayAdvice(UserDetail userDeail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	serviceAdviceFacade.waitPayAdvice(1131);
//   	 	//返回数据
//   	 	return jsonResponse.setSuccessful();
//    }

}