package com.yujj.web.controller.mobile;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ActivityPlace;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserSharedRecord;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.util.spring.ControllerUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.ActivityPlaceService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserService;
import com.yujj.business.service.UserSharedService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.product.Product;

@Controller
@RequestMapping("/mobile/share")
public class MobileShareController implements checked {

    
	
    @Autowired
    private ProductService productService;
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserCoinService userCoinService;
    
    @Autowired
    private UserSharedService userSharedService;
    
    @Autowired
    private ActivityPlaceService activityPlaceService;
    
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getProductShareData(UserDetail userDetail, @RequestParam("id") long productId, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();

        Product product = productService.getProductById(productId);
        if (product == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }
        

        Map<String, String> data = userSharedService.getProductUserShareInfo(userDetail.getUserId(), productId, clientPlatform);
		
        return jsonResponse.setSuccessful().setData(data);
    }

    
    @RequestMapping(value = "/artical", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getArticalShareData(UserDetail userDetail, @RequestParam("artical_id") long articalId, ClientPlatform clientPlatform) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = userSharedService.getArticalUserShareInfo(userDetail.getUserId(), articalId, clientPlatform);
    	return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping(value = "/activityPlace")
    @ResponseBody
    public JsonResponse getActivityPlaceShareData(UserDetail userDetail, @RequestParam("activityPlaceId") long activityPlaceId, ClientPlatform clientPlatform) {
    	JsonResponse jsonResponse = new JsonResponse();

        ActivityPlace activityPlace = activityPlaceService.getById(activityPlaceId);
        if (activityPlace == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

        Map<String, String> data = userSharedService.getActivityPlaceUserShareInfo(userDetail.getUserId(), activityPlaceId, clientPlatform);
		
        return jsonResponse.setSuccessful().setData(data);
    }
	
	@RequestMapping(value = "/clicked", method = RequestMethod.GET)
    @NoLogin
    public String shareView(String token, UserDetail userDetail, ClientPlatform clientPlatform) {
		
		boolean isRegistershare = userSharedService.checked(token, userDetail, clientPlatform);
        if(isRegistershare){
        	return ControllerUtil.redirect(Constants.SERVER_URL_HTTPS + "/static/app/login/registershare.html");
		}else{
			return  "";
		}
    }
	
	/**
     * 添加用户分享记录
     * @param type	分享内容类型 0：其他 1：商品 2:文章
     * @param relatedId	相关id
     * @param channel	分享渠道 0：其他 1：微信好友 2:微信朋友圈 3：QQ 4:QQ空间 5:腾讯微博 6:新浪微博
     * @param userDetail
     * @return
     */
   @NoLogin
   @RequestMapping(value = "/add")
   @ResponseBody
   public JsonResponse add(@RequestParam("relatedId") long relatedId,
		   					@RequestParam("channel") int channel,
		   					@RequestParam("userSharedRecordId") long userSharedRecordId,
		   					@RequestParam("type") int type,
		   					UserDetail userDetail) {
	   	UserSharedRecord userSharedRecord = new UserSharedRecord();
	   	userSharedRecord.setId(userSharedRecordId);
	   	userSharedRecord.setType(type);
	   	userSharedRecord.setRelatedId(relatedId);
	   	userSharedRecord.setChannel(channel);
	    userSharedRecord.setUserId(userDetail.getUserId());//分享记录用户ID为0表示用户未登陆
	   	return userSharedService.addUserSharedRecord(userSharedRecord);
   }
	
    /**
     * 获取当前用户的分享列表
     * @param userDetail
     * @return
     */
    @RequestMapping(value = "/mySharedList")
    @Login
    @ResponseBody
    public JsonResponse mySharedList(UserDetail userDetail) {
    	return userSharedService.mySharedList(userDetail.getUserId());
    }
}
