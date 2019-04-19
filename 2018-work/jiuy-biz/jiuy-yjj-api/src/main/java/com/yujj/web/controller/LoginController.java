package com.yujj.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.spi.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.yujj.business.service.UserInviteService;
import com.yujj.business.service.UserService;
import com.yujj.dao.mapper.UserInviteMapper;
import com.yujj.entity.account.User;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.SecurityService;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.web.controller.delegate.LoginDelegator;
import com.yujj.web.controller.shop.msg.WeiXinMsgService;

@Controller
@RequestMapping("/login")
public class LoginController {
	 private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	  
	   
	@Autowired
	private WeiXinMsgService weiXinMsgService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private UserInviteService userInviteService;
    
    @Autowired
    private UserInviteMapper userInviteMapper;

    @Autowired
    private LoginDelegator loginDelegator;
    
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String loginForm(@RequestParam(value = "target_url", required = false) String targetUrl, 
                            Map<String, Object> model) {
    	logger.info("网页登陆接口");
    	weiXinMsgService.receiveMessage("发送客服消息，收到测说明测试成！！！");
    	logger.info("网页登陆接口，receiveMessage");
    	
        if (StringUtils.isNotBlank(targetUrl)) {
            targetUrl = securityService.getSafeRedirectUrl(targetUrl);
            model.put("targetUrl", targetUrl);
        }
        return "login/form";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse submitLogin(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    @RequestParam(value = "target_url", required = false) String targetUrl,@ClientIp String ip,ClientPlatform client,
                                    HttpServletResponse response) {
    	
    	JsonResponse jsonResponse = loginDelegator.submitLogin(username, password, response ,ip , client);
    	
    	//组装返回数据
        targetUrl = securityService.getSafeRedirectUrl(targetUrl);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("targetUrl", targetUrl);
        return jsonResponse.setData(data).setSuccessful();
    	
//        JsonResponse jsonResponse = new JsonResponse();
        //1、验证账号密码信息
//        User user = userService.getUser4Login(userName);
//        if (user == null) {
//            return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USER_NOT_EXISTS);
//        }else if (!StringUtils.equals(user.getUserPassword(), DigestUtils.md5Hex(password))) {
//            return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_INVALID_PASSWORD);
//        }
        
		//2、生成cockie值放入Header中
//        String cookieValue = LoginUtil.buildLoginCookieValue(user.getUserRelatedName(), user.getUserType());
//        response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
        
        //临时活动代码段2017-02-14至2017-02-28，2017-02-28之后可去除
        //lingShiHuoDongDaiMaDuan(userName, user);
        
        //3、记录用户登录日志
//		addUserLoginLog(ip, client, user);
//        return jsonResponse.setData(data).setSuccessful();
    }
    
//    /**
//     * 记录用户登录日志
//     * @param ip
//     * @param client
//     * @param user
//     */
//	private void addUserLoginLog(String ip, ClientPlatform client, User user) {
//		UserLoginLog userLoginLog = new UserLoginLog();
//        userLoginLog.setUserId(user.getUserId());
//        if(client != null){
//        	userLoginLog.setClientType(client.getPlatform().getValue());
//        	userLoginLog.setClientVersion(client.getVersion());	
//
//        }
//        userLoginLog.setIp(ip);
//        userLoginLog.setCreateTime(System.currentTimeMillis());
//        userService.addUserLoginLog(userLoginLog);
//	}
//    /**
//     * 临时活动代码段2017-02-14至2017-02-28，2017-02-28之后可去除
//     * @param userName
//     * @param user
//     */
//	private void lingShiHuoDongDaiMaDuan(String userName, User user) {
//		//活动临时代码
//        long nowTime = System.currentTimeMillis();
//        long startTime = 0;
//    	long endTime = 0;
//    	try {
//			startTime = DateUtil.parseStrTime2Long("2017-02-14 00:00:00");
////			startTime = DateUtil.parseStrTime2Long("2017-02-20 00:00:00");
//			endTime = DateUtil.parseStrTime2Long("2017-02-28 23:59:59");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	if(nowTime >= startTime && nowTime <= endTime && user.getCreateTime() >= startTime && user.getCreateTime() <= endTime){
//    		//判断是否第一次登录
//    		UserLoginLog userLoginLog = userService.getUserNewestLoginLog(userName);
//    		if(userLoginLog == null ){
//    			//再判断是否是被邀请的
//    			UserInviteRecord userInviteRecord = userInviteService.getByInvitedUserId(user.getUserId());
//    			if(userInviteRecord != null && userInviteRecord.getUserId() > 0){
////    				userInviteMapper.incrUserInviteCount(userInviteRecord.getUserId()); //邀请人userid  test test
//    				UserInvite userInvite = userInviteService.getUserInvite(userInviteRecord.getUserId()); //
//    				if (userInvite == null) {
//    					//  return;
//    				}else {
//    					userInviteMapper.incrUserInviteCount(userInviteRecord.getUserId()); //邀请人userid
//    				}
//    				
//    			}
//    		}
//    		
//    	}
//	}
}
