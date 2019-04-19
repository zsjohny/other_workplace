package com.jiuy.wxa.api.controller.jfinalweixin;

import javax.servlet.http.HttpServletResponse;

import com.jiuy.rb.enums.CouponSendEnum;
import com.jiuy.rb.enums.CouponStateEnum;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.util.CouponAcceptVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.EncodeUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.member.ShopMember;
import com.store.service.MemberService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import java.util.Map;

/**
 * 接收处理消息接口
 * 
 * 为JFinal 微信提供的业务http业务接口
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping("/wxa")
public class MemberReceiveController {

	private static final Log logger = LogFactory.get();
    @Autowired
    MemberService memberService;

    @Autowired
	private ICouponServerNew couponServerNew;


    /**
     * 登陆接口
     * @param unionId unionId
     * @param appId appId
	 * @param from 0 是自主注册 1是邀请注册
     * @return JsonResponse
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public JsonResponse submitLogin(
    		@RequestParam(required = true) String unionId,
    		 String appId,
    		 String from,
    		 long storeId,
			HttpServletResponse response) {
    	JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> retMap = memberService.login(unionId,appId,storeId,response,from);
    	ShopMember member = (ShopMember) retMap.get("member");
    	if(member != null){
            Object coupon = retMap.get("coupon");
            if(coupon !=null && (Boolean) coupon){
                sendCoupon(retMap);
            }
    		return jsonResponse.setSuccessful().setData(member);
    	}else{
    		return jsonResponse.setSuccessful().setResultCode(ResultCode.WXA_LOGIN_FAIL);
    	}
    	
    }

	/**
	 * 小程序注册发送优惠券
	 * @param memberMap memberMap
	 * @author Aison
	 * @date 2018/8/3 18:57
	 */
	private void sendCoupon(Map<String,Object> memberMap) {
		try{
			ShopMember member = (ShopMember) memberMap.get("member");
			CouponAcceptVo accept = new CouponAcceptVo(member.getId(),null,null,CouponSysEnum.WXA,CouponSendEnum.REGISTER,CouponStateEnum.NOT_USE);
			couponServerNew.grant(accept);
		}catch (Exception e) {
		    e.printStackTrace();
		}
	}

    /**
     * 调用授权接口
     */
    @RequestMapping("/authoriz")
    @ResponseBody
    public JsonResponse authoriz(
    		@RequestParam(required = true) String unionId,
    		 String appId, 
    		 long storeId,
    		 String from,
    		 String nickName, String headImg,
            @RequestParam(value = "uId", required = false) String uId,
			@RequestParam(value = "sex", required = false) Integer sex ) {
    	//处理头像特殊字符问题
    	nickName = EncodeUtil.decodeBase64(nickName);
    	//处理特殊字符
    	nickName = EncodeUtil.filterEmoji(nickName);
		Map<String,Object> retMap = memberService.addMember( unionId,  nickName, headImg, appId,storeId,from, sex);
    	ShopMember member = (ShopMember) retMap.get("member");
   	 	JsonResponse jsonResponse = new JsonResponse();
   	 	if(member != null){
			Object coupon = retMap.get("coupon");
			if(coupon !=null && (Boolean) coupon){
				sendCoupon(retMap);
			}
	 		return jsonResponse.setSuccessful().setData(member);
	 	}else{
	 		return jsonResponse.setSuccessful().setResultCode(ResultCode.WXA_AUTH_FAIL);
	 	}
   	 	
    }
}


