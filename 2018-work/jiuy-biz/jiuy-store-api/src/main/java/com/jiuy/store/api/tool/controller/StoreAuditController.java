package com.jiuy.store.api.tool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2API;
import com.jiuyuan.web.help.JsonResponse;
import com.store.enumerate.StoreAuditStatusEnum;
import com.store.service.StoreAuditServiceShop;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping("/store/audit")
public class StoreAuditController {
	private static final Log logger = LogFactory.get();
	
    @Autowired
    private StoreAuditServiceShop storeAuditService;
    
    @Autowired
    @Qualifier("weiXinV2API4App")
    private WeiXinV2API weiXinV2API;
    
    @RequestMapping(value = "/list")//, method = RequestMethod.POST
    @ResponseBody
    public JsonResponse getAuditlist(@RequestParam("status") StoreAuditStatusEnum status) {
        return storeAuditService.getAuditList(status);
    }
    
    @RequestMapping(value = "/updateStatus/auth")//, method = RequestMethod.POST
    @ResponseBody
    public JsonResponse updateStatus(@RequestParam("storeId") long storeId, @RequestParam("status") int status, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
			//待开发（TODO）
    		return storeAuditService.auditStoreBusiness(storeId, status, userDetail);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    
//    @RequestMapping(value = "/verifyCommit")//, method = RequestMethod.POST
//    @ResponseBody
//    public JsonResponse verifyCommit(@RequestParam("phone") String phone,@RequestParam("verify_code") String verifyCode,
//    		HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
//    	return loginDelegator.verifyCommit(phone, verifyCode, response ,ip , client);
//    }
//    

}
