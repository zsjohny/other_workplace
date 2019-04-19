package com.jiuyuan.service.common;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.Status;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.dao.mapper.supplier.GroundUserLoginLogMapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.GroundUserLoginLog;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.web.help.JsonResponse;


@Service
public class GroundUserLoginDelegator {
	private static final Logger logger = LoggerFactory.getLogger(GroundUserLoginDelegator.class);
	
	@Autowired
	private GroundUserLoginLogMapper groundUserLoginLogMapper;
	
    /**
     * 加盐参数
     */
    public final static String hashAlgorithmName = "MD5";

    /**
     * 循环次数
     */
    public final static int hashIterations = 1024;
	
	@Autowired
	private IGroundUserService groundUserService;

	/**
	 * 手机登陆，判断用户名和密码是否为空
	 * 
	 * @param username
	 * @param password
	 * @param response
	 * @param ip
	 * @param client
	 * @return
	 */
	public JsonResponse mobileSubmitLogin(String phone, String password, HttpServletResponse response, String ip,
			ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();
		if (checkNull(phone)) {
			return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USERNAME_NULL);
		}
		if (checkNull(password)) {
			return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_PASSWORD_NULL);
		}
		return submitLogin(phone, password, response, ip, client);
	}
	
	/**
	 * 判断是否为空或者空字符串
	 * 
	 * @param str
	 * @return
	 */
	public boolean checkNull(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 登录验证
	 * 
	 * @param username
	 * @param password
	 * @param response
	 * @param ip
	 * @param client
	 * @return
	 */
	public JsonResponse submitLogin(String phone, String password, HttpServletResponse response, String ip,
			ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,2,5-9])|(177))\\d{8}$";
		Pattern pattern = Pattern.compile(regex);
		boolean flag = pattern.matcher(phone).matches();// 判断用户的手机号是否合法
		if(!flag){
			throw new RuntimeException(ResultCode.PHONE_ERROR_NUMBER.getDesc());
		}		
		GroundUser groundUser = null;
		groundUser = groundUserService.getGroundUserByPhone(phone);
		if(groundUser == null){
			throw new RuntimeException(ResultCode.LOGIN_ERROR_INVALID_USERNAME_OR_PASSWORD.getDesc());
		}
			ByteSource salt = new Md5Hash(groundUser.getSalt());
			String passwordMD5 = new SimpleHash(hashAlgorithmName, password,salt , hashIterations).toString();
			if(!groundUser.getPassword().equals(passwordMD5)){
				throw new RuntimeException(ResultCode.LOGIN_ERROR_INVALID_USERNAME_OR_PASSWORD.getDesc());
			}
        //是否被禁用
			int status = groundUser.getStatus();
			if(status == Status.DELETE.getIntValue()){
				throw new RuntimeException(ResultCode.LOGIN_ERROR_INVALID_USERNAME_OR_PASSWORD.getDesc());
			}
			if(status == Status.HIDE.getIntValue()){
				throw new RuntimeException(ResultCode.LOGIN_ERROR_FORBIDDEN_USERNAME.getDesc());
			}
		Map<String, Object> data = loginUser(groundUser, response, ip, client);
		return jsonResponse.setSuccessful().setData(data);
	}

	public Map<String, Object> loginUser(GroundUser groundUser, HttpServletResponse response, String ip,
			ClientPlatform client) {
//
		Map<String, Object> data = new HashMap<String, Object>();
		String cookieValue = LoginUtil.buildLoginCookieValue(groundUser.getPhone()+ "", UserType.PHONE);
		response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
		logger.debug("cookie :{}", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
		
		//是否为原始密码
		int isOriginalpassword = groundUser.getIsOriginalpassword();
		data.put("isOriginalpassword", isOriginalpassword == 1?true:false);
		// 记录用户登录日志
		GroundUserLoginLog groundUserLoginLog = new GroundUserLoginLog();
		Long userId = new Long(groundUser.getId());
		groundUserLoginLog.setUserid(userId);
		if (client != null) {
			groundUserLoginLog.setClientType(client.getPlatform().getValue());
			groundUserLoginLog.setClientVersion(client.getVersion());
		}
		groundUserLoginLog.setIp(ip);
		groundUserLoginLog.setCreateTime(System.currentTimeMillis());
		groundUserLoginLogMapper.insert(groundUserLoginLog);
		return data;
	}

	public JsonResponse submitLogin(String username, String password, HttpServletResponse response) {

		return submitLogin(username, password, response, "", null);
	}
}
