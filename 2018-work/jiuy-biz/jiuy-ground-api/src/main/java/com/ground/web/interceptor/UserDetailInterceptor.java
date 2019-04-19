package com.ground.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.CookieConstants;
import com.jiuyuan.entity.newentity.DefaultGroundUserDetail;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.service.common.IGroundUserService;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.util.http.CookieUtil;

public class UserDetailInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailInterceptor.class);

//	@Autowired
//	private StoreUserService userService;
	
	@Autowired
	private IGroundUserService groundUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		DefaultGroundUserDetail userDetail = new DefaultGroundUserDetail();
		String[] parts = LoginUtil.getLoginCookieParts(request);
		if (parts != null) {
			try {
				String phone = parts[0];
				// long time = System.currentTimeMillis();
				// UserLoginLog userLoginLog =
				// userService.getUserNewestLoginLog(businessNumber);
				// if(userLoginLog != null && time -
				// userLoginLog.getCreateTime() < DateUtils.MILLIS_PER_DAY * 15
				// ){
				// UserType userType =
				// UserType.parse(Integer.parseInt(parts[1]));

				//StoreBusiness storeBusiness = userService.getStoreBusinessByBusinessNumber(businessNumber);
				GroundUser groundUser = groundUserService.getGroundUserByPhone(phone);
				if (groundUser != null) {
					userDetail.setGroundUser(groundUser);
				}
				// }

			} catch (Exception e) {
				logger.error("", e);
				e.printStackTrace();
			}
		}

		String virtualDeviceId = checkAndSetVirtualDeviceId(request, response);
		userDetail.setVirtualDeviceId(virtualDeviceId);

		request.setAttribute(Constants.KEY_USER_DETAIL, userDetail);

		return true;
	}

	private String checkAndSetVirtualDeviceId(HttpServletRequest request, HttpServletResponse response) {
		String virtualDeviceId = CookieUtil.getCookieValue(request, CookieConstants.COOKIE_NAME_VIRTUAL_DEVICEID, "");
		if (StringUtils.isNotBlank(virtualDeviceId)) {
			return virtualDeviceId;
		}
		virtualDeviceId = System.currentTimeMillis() + RandomStringUtils.randomNumeric(3);
		int expires = Integer.MAX_VALUE;
		String cookieValue = CookieUtil.buildCookieHeaderValue(CookieConstants.COOKIE_NAME_VIRTUAL_DEVICEID,
				virtualDeviceId, "/", CookieConstants.COOKIE_DOMAIN, expires, true);
		response.addHeader("Set-Cookie", cookieValue);
		return virtualDeviceId;
	}

}
