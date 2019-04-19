package com.jiuy.base.model;

import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * 用户session
 * @author Aison
 * @version V1.0
 * @Copyright 玖远网络
 * @date 2018/6/5 16:07
 */
@Data
public class UserSession {


	private Long userId;

	private String userName;

	private String userPhone;

	private String orgName;

	private String orgCode;

	private String unitOrgCode;

	private String unitOrgName;

	private String fullOrgName;

	private List<Map<String, Object>> userMenus;

	private static ThreadLocal<UserSession> userSessionThreadLocal = new ThreadLocal<>();

	/**
	 * 非常重要：请不要在service层中使用此放方法.. 此方法只允许在controller中使用 如果需要传递值给service请使用形参的方式
	 * @author Aison
	 * @date 2018/6/5 18:19
	 */
	public static UserSession getUserSession(){
		return userSessionThreadLocal.get();
	};

	/**
	 * 设置userSession
	 * @param userSession 用户对象
	 * @author Aison
	 * @date 2018/6/6 10:29
	 */
	public static void setUserSession(UserSession userSession){
		userSessionThreadLocal.set(userSession);
	}

}
