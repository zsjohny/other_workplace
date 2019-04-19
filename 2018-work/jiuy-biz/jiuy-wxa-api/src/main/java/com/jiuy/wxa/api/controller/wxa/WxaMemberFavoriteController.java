package com.jiuy.wxa.api.controller.wxa;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.service.WxaMemberFavoriteService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* 会员收藏Controller
* @author Qiuyuefan
*/
@Controller
//@Login
@RequestMapping("/miniapp/member/favorite")
public class WxaMemberFavoriteController {

	 private static final Log logger = LogFactory.get();
    @Autowired
    private WxaMemberFavoriteService shopMemberFavoriteService;
    
    /**
     * 添加或者取消会员收藏记录
     * @param memberId
     * @param productId
     * @param type  类型：0商品
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public JsonResponse addMemberFavoriteRecord(Long storeId,Long memberId,
    		@RequestParam("productId")Long productId,@RequestParam("status")Integer status,
    		@RequestParam("type")Integer type) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
			checkLongin(storeId,memberId);
    		Map<String,String> data = new HashMap<String,String>();
    		
    		int newStatus = shopMemberFavoriteService.addMemberFavoriteRecord(memberId,productId,type,status,storeId);
    		
    		data.put("status", String.valueOf(newStatus));
    		return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
    		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }  
    
    /**
     * 获取会员收藏列表
     * @param memberId
     * @param current
     * @param size
     * @return
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public JsonResponse getMemberFavoriteList(Long storeId,Long memberId,String appId,
    		@RequestParam(value="current",required=false,defaultValue="1")Integer current,
    		@RequestParam(value="size",required=false,defaultValue="100")Integer size) {
//    	logger.error("获取会员收藏列表:"+shopDetail.getId()+"---"+memberDetail.getId());
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
			checkLongin(storeId,memberId);
    		Map<String,Object> data = shopMemberFavoriteService.getMemberFavoriteList(memberId,new Page(current,size),storeId,
    				appId);
    		return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
    		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }

   	/**
   	 * 检验会员是否为空
   	 * @param storeId
   	 */
   	private void checkLongin(Long storeId,Long memberId) {
   	 	if(storeId==null || memberId == null){
   	 		logger.warn("登陆信息为空storeId={},memberId={}",storeId,memberId);
   	 		throw new RuntimeException(ResultCode.COMMON_ERROR_NOT_LOGGED_IN.getDesc());
   	 	}
   	}
}
