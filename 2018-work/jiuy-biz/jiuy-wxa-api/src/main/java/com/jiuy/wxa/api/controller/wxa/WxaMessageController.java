package com.jiuy.wxa.api.controller.wxa;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.service.MessageService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * <p>
 * 
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
//@Login
@RequestMapping("/miniapp/message")
public class WxaMessageController {
    private static final Logger logger = LoggerFactory.getLogger(WxaMessageController.class);
    
    Log log = LogFactory.get();
    
   
    @Autowired
    MessageService messageService;
    
   

    /**
     * 设置会员未读消息为已读接口（会员进入小程序时调用）
     * @return
     */
    @RequestMapping("/setMemberNoReadIsRead")
    @ResponseBody
    public JsonResponse getMyMemberInfo(
    		@RequestParam("storeId") Long storeId,
    		@RequestParam("memberId") Long memberId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
   	 		checkMember(memberId);
    		messageService.setMemberNoReadIsRead(memberId,storeId);
    		return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }


	private void checkMember(Long id) {
		if(id==null || id == 0){
			logger.info("会员信息不能为空，该接口需要登陆，请排除问题");
			throw new RuntimeException(ResultCode.COMMON_ERROR_NOT_LOGGED_IN.getDesc());
		}
	}
    
    /**
     * 获取未读消息数量(暂时没三秒轮询一次获取)
     * @return
     */
    @RequestMapping("/getMemberNoReadCount")
    @ResponseBody
    public JsonResponse getMemberNoReadCount(
			@RequestParam("storeId") Long storeId,
			@RequestParam("memberId") Long memberId,
			ShopDetail shopDetail,MemberDetail memberDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
//    		logger.info("获取未读消息数量storeId={},memberId={}",storeId, memberId);
    		int count = 0;
    		if(memberId != null && storeId != null){
    			count = messageService.getMemberNoReadMessageCount(memberId,storeId);
    		}
    		Map<String,String> data = new HashMap<String,String>();
    		data.put("memberNoReadMessageCount", String.valueOf(count) );
   	 		return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
    		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 记录会员咨询商家商品接口
		说明：用户在商品详情进入客服会话时记录咨询的商家商品
     * @return
     */
    @RequestMapping("/setMemberMessagePorduct")
    @ResponseBody
    public JsonResponse getMemberNoReadCount(long shopProductId,ShopDetail shopDetail,MemberDetail memberDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	return jsonResponse.setSuccessful();
//    	try {
//    		checkStore(shopDetail);
//   	 		checkMember(memberDetail);
//    		messageService.setMemberMessagePorduct(shopProductId,memberDetail.getId(),shopDetail.getId());
//   	 		return jsonResponse.setSuccessful();
//    	} catch (Exception e) {
//    		e.printStackTrace();
//			return jsonResponse.setError(e.getMessage());
//		}
    }
    
    /**
     * 清除会员咨询商家商品接口
                说明：用户在商品详情进入客服会话时记录咨询的商家商品，
     * @return
     */
    @RequestMapping("/clearMemberMessagePorduct")
    @ResponseBody
    public JsonResponse clearMemberMessagePorduct(ShopDetail shopDetail,MemberDetail memberDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
		return jsonResponse.setSuccessful();
//    	try {
//    		checkStore(shopDetail);
//   	 		checkMember(memberDetail);
//
//   	 		messageService.clearMemberMessagePorduct(memberDetail.getId(),shopDetail.getId());
//   	 		return jsonResponse.setSuccessful();
//    	} catch (Exception e) {
//    		e.printStackTrace();
//			return jsonResponse.setError(e.getMessage());
//		}
    }
   
    
    
    



}