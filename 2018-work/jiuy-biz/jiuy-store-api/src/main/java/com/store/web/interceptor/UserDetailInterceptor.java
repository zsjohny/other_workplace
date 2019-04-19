package com.store.web.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiuy.base.util.HttpUtils;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.model.log.AccessLog;
import com.jiuy.rb.service.common.ILogRbService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jiuyuan.constant.SystemPlatform;
import com.jiuyuan.service.common.IYjjMemberService;
import com.jiuyuan.util.HttpClientUtils;
import com.jiuyuan.util.NumberUtils;
import com.jiuyuan.util.current.ExecutorService;
import com.jiuyuan.util.current.ExecutorTask;
import com.util.LocalMapUtil;
import com.util.ServerPathUtil;
import com.yujj.entity.product.YjjMember;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.CookieConstants;
import com.jiuyuan.entity.account.UserLoginLog;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.StoreBusinessNewService;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.util.http.CookieUtil;
import com.store.entity.DefaultStoreUserDetail;
import com.store.service.StoreUserService;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class UserDetailInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailInterceptor.class);

	@Autowired
	private StoreUserService userService;
	
	@Autowired
	private StoreBusinessNewService storeBusinessNewService;

	@Resource(name = "logRbService")
	private ILogRbService  logRbService;


	@Autowired
	private IYjjMemberService yjjMemberService;

	/**
	 * MAP_KEY会员等级
	 */
	private static String MEMBER_KEY = "member:%s";
	private static ExecutorService userIdRedisPut = new ExecutorService(Runtime.getRuntime().availableProcessors() << 1, "userIdRedisPut");
	Map<String, String> userMap = new HashMap<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		DefaultStoreUserDetail userDetail = new DefaultStoreUserDetail();
//		String header= request.getHeader("token");
//		String uid= request.getHeader("uid");
//		System.out.println("header="+header);
//		System.out.println("uid="+uid);
//		String[] parts = LoginUtil.getLoginCookieParts(request);
		StoreBusiness storeBusiness = null ;
		//获取用户id
		String id = request.getHeader("id");
//		==================================
//		parts = new String[]{"800000003"};
//		==================================
		System.out.println("测试id={}"+id);
		if (id != null) {
//		if (parts != null) {

			try {
//				String businessNumber = parts[0];
//				storeBusiness = storeBusinessNewService.getStoreBusinessByBusinessNumber(Long.parseLong(businessNumber));
				storeBusiness = storeBusinessNewService.getStoreBusinessById(Long.parseLong(id));
				userDetail.setStoreBusiness(storeBusiness);
//				String userId = String.valueOf(storeBusiness.getId());
//				if (storeBusiness != null) {
//					userDetail.setStoreBusiness(storeBusiness);
//					userIdRedisPut.addTask(new ExecutorTask() {
//						@Override
//						public void doJob() {
//							logger.info("HTTP-新服务-设置新id");
//							String url = ServerPathUtil.me().getServer2Url()+ "/user/user/redis/value";
//							String mapValue = userMap.get(businessNumber);
//							if (mapValue==null){
//								userMap.put(businessNumber,userId);
//								Map<String,Object> map = new HashMap<>(2);
//								map.put("key",businessNumber);
//								map.put("value",userId);
//								String response = HttpClientUtils.get (url, map);
//								logger.info("HTTP-返回={}",response);
//							}
//						}
//					});
//				}
				try{
					//将用户放到request中,让其他拦截器可以调用
					if (userDetail != null) {
						request.setAttribute (Constants.KEY_USER_DETAIL, userDetail);
					}

					localMap(request,response,String.valueOf(storeBusiness.getBusinessNumber()),storeBusiness);
//					localMap(request,response,businessNumber,storeBusiness);
				}catch (Exception e){
					logger.error(e.getMessage());
					e.printStackTrace();
				}
				// }

			} catch (Exception e) {
				logger.error("登录异常", e);
				e.printStackTrace();
			}
		}


//
//		String virtualDeviceId = checkAndSetVirtualDeviceId(request, response);
//		userDetail.setVirtualDeviceId(virtualDeviceId);
//
//		request.setAttribute(Constants.KEY_USER_DETAIL, userDetail);

		// 添加小程序访问记录
		// try 一下不要影响正常访问
		try{
			AccessLog log = new AccessLog();
			log.setCreateTime(new Date());
			log.setIp(HttpUtils.getIpAdrress(request));
			log.setType(CouponSysEnum.APP.getCode());
			log.setUri(request.getRequestURI());
			log.setUserId(storeBusiness!=null ? storeBusiness.getId() : null);
			logRbService.syncAccessLog(log);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}



	public void localMap(HttpServletRequest request,HttpServletResponse response,String ids,StoreBusiness storeBusiness  ){
//	    logger.info("会员策略-存储本地会员信息ids={}",ids);
		String member = null;
		String url = request.getRequestURI();
//		logger.info("会员策略-url={}", url);
//		/shop/pay/directpaynew.json
		try {
			if (url.contains("/shop/pay/directpaynew")||url.contains("/shop/pay/directpay/weixin/new")){
			    logger.info("会员策略-清空会员状态={}",ids);
				LocalMapUtil.invalidate(String.format(MEMBER_KEY,ids));
				return;
			}

			member = LocalMapUtil.get(String.format(MEMBER_KEY,ids));
		} catch (Exception e) {
			e.printStackTrace();
		}
//        logger.info("会员策略-member等级={}",member);
		//若用户未存入等级
		if (StringUtils.isEmpty(member)){
		    logger.info("会员策略-初始化会员等级={}",String.format(MEMBER_KEY,ids));
			YjjMember yjjMember = null;
			if (storeBusiness!=null){
				yjjMember = yjjMemberService.findValidMemberByUserId(SystemPlatform.STORE,storeBusiness.getId());
				if (yjjMember!=null&&yjjMember.getDelState()!=0){
					yjjMember=null;
				}
			}
			if (yjjMember!=null){
				member=String.valueOf(yjjMember.getMemberLevel());
				LocalMapUtil.put(String.format(MEMBER_KEY,ids),member);
			}else {
			    member="0";
				LocalMapUtil.put(String.format(MEMBER_KEY,ids),member);
            }
//            logger.info("会员策略-yjjMember={}",member);
		}
		try {
		    response.addHeader("member", member);
		}catch (Exception e){
			logger.error("会员策略-生成会员等级出错");
			e.printStackTrace();
		}
	}

	// 判断15天自动登录方法 ，@@@to do
	public boolean checkLoginStatus(HttpServletRequest request, boolean storeUserFlag) throws Exception {
		String[] parts = LoginUtil.getLoginCookieParts(request);
		if (parts != null) {
			try {
				String userName = parts[0];
				long time = System.currentTimeMillis();
				UserLoginLog userLoginLog = new UserLoginLog();
				if (storeUserFlag) {
					userLoginLog = userService.getUserNewestLoginLog(userName);
				} else {
					// userLoginLog = null;
				}
				if (userLoginLog != null && time - userLoginLog.getCreateTime() < DateUtils.MILLIS_PER_DAY * 15) {
					return true;
				}

			} catch (Exception e) {
				logger.error("登录异常", e);
			}
		}
		return false;
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
