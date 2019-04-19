package com.yujj.business.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.CodeUseage;
import com.jiuyuan.constant.account.ThirdAppType;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.account.UserRegSource;
import com.jiuyuan.constant.account.UserStatus;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserSharedRecord;
import com.jiuyuan.entity.account.BindUserRelation;
import com.jiuyuan.entity.account.UserBindLog;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.entity.account.UserLoginLog;
import com.jiuyuan.entity.account.UserMember;
import com.jiuyuan.entity.account.YJJNumber;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.service.WhitePhoneService;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.util.oauth.common.credential.IRawDataCredentials;
import com.jiuyuan.util.oauth.common.credential.RawDataCredentials;
import com.jiuyuan.util.oauth.sns.common.response.ISnsResponse;
import com.jiuyuan.util.oauth.sns.common.response.SnsResponseType;
import com.jiuyuan.util.oauth.sns.common.user.ISnsEndUser;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2API;
import com.jiuyuan.util.oauth.v2.V2Constants;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.RegisterFacade;
import com.yujj.dao.mapper.StoreBusinessMapper;
import com.yujj.dao.mapper.UserLoginLogMapper;
import com.yujj.dao.mapper.UserMapper;
import com.yujj.dao.mapper.UserMemberMapper;
import com.yujj.dao.mapper.UserSharedMapper;
import com.yujj.entity.StoreBusiness;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;

@Service
public class UserService {

	 private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	 
	 private static  int GRANT_COINS = 99 * 5;
	 
	 @Autowired
	 private WhitePhoneService whitePhoneService;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserLoginLogMapper userLoginLogMapper;
	
	@Autowired
	private RegisterFacade registerFacade;
	
	@Autowired
	private YunXinSmsService yunXinSmsService;
    @Autowired
    private UserSharedService userSharedService;

    
    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private MemcachedService memcachedService;
    
    @Autowired
    private UserSharedMapper userSharedMapper;
    
    @Autowired
    private UserMemberMapper userMemberMapper;
    
    @Autowired
    private StoreBusinessMapper storeBusinessMapper;
	
    @Autowired
    private UserMemberService userMemberService;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    @Qualifier("weiXinV2API4App")
    private WeiXinV2API weiXinV2API;
    
    @Autowired
    private UserNewMapper userNewMapper;
    
    @Autowired
    private IStoreBusinessNewService storeBusinessNewService;
    
	@Autowired
	private StoreMapper storeMapper;
    

	

	public User getUser(long userId) {
		return userMapper.getUser(userId);
	}
//
//	public User getUser4Login(String userName) {
//		return userMapper.getUser4Login(userName);
//	}
	
//	public User getUserByPhone(String userName) {
//		return userMapper.getUserByPhone(userName);
//	}
	
//	public User getUserByBindPhoneOnly(String userName) {
//		return userMapper.getUserByBindPhoneOnly(userName);
//	}

//	public User getUserByUserNameOnly(String userName) {
//		return userMapper.getUserByUserNameOnly(userName);
//	}

//    public User getUserByRelatedName(String relatedName, UserType userType) {
//        return userMapper.getUserByRelatedName(relatedName, userType);
//	}
    
//    public User getUserByWeixinId(String relatedName) {
//    	return userMapper.getUserByWeixinId(relatedName);
//    }
    
//    public User getUserByBindWeixinOnly(String relatedName) {
//    	return userMapper.getUserByBindWeixinOnly(relatedName);
//    }
    
    public User getUserByAllWay(String userName) {
    	return userMapper.getUserByAllWay(userName);
    }
    
    public List<YJJNumber> getYJJNumberList(long startNum ,long endNum , int limit ,int status) {
    	return userMapper.getYJJNumberList(startNum, endNum, limit , status);
    }

	public synchronized int addUser(User user) {
		return userMapper.addUserNumberIncrease(user);
	}
	
	public int addUserLoginLog(UserLoginLog userLoginLog) {
		//return 0;
		return userLoginLogMapper.addUserLoginLog(userLoginLog);
	}
	
	public UserLoginLog getUserNewestLoginLog(String userName) {
		//return 0;
		UserLoginLog userLoginLog = null;
		
		userLoginLog = userLoginLogMapper.getUserNewestLoginLog(userName);
		
		return userLoginLog;
	}
	

	public int updateUserPassword(long userId, String password) {
		return userMapper.updateUserPassword(userId, password);
	}
	
	public int updateYjjNumberUsed(long numUsed) {
		return userMapper.updateYjjNumberUsed(numUsed );
	}
	
	public int updateUserYjjNumber(long userId, long num) {
		return userMapper.updateUserYjjNumber(userId, num);
	}
	
	public int updateUserCid(long userId, String userCid) {
		return userMapper.updateUserCid(userId, userCid);
	}

    public int updateUserInfo(long userId, String userNickName, String userIcon) {
        return userMapper.updateUserInfo(userId, userNickName, userIcon);
    }

    public int updateUserNickName(long userId, String userNickName) {
        return userMapper.updateUserNickName(userId, userNickName);
    }
    
    public int updateUserIcon(long userId, String userIcon) {
        return userMapper.updateUserIcon(userId, userIcon);
    }
    
    @SuppressWarnings("unchecked")
    public JsonResponse phoneNumberCommit(String phone,  String verifyCode, long userId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	if (whitePhoneService.getWhitePhone(phone) == 0){//如果手机号不在白名单
    		if ( !registerFacade.verifyPhoneVerifyCode(phone, verifyCode, true, CodeUseage.PHONE_BIND)) {
    			return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
    		}
    	}
    	//User user = getUserByRelatedName(phone, UserType.PHONE);
    	
//    	User user;
//  		List<User> userList = userMapper.getUserListByPhone(phone);
//		if(userList != null && userList.size() >= 2){
//			return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
//		}else if(userList != null && userList.size() == 1){
//			
//			user = userList.get(0);
//			
//			if (user != null) {
//				
//					//如果已绑定在其它用户则不让绑定
//					if(user.getBindPhone() != null && user.getBindPhone().equals(phone)){
//						return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
//						
//					}
////					else if(user.getUserName() != null && user.getUserName().equals(phone) && user.getCreateTime() > DateUtil.parseStrTime2Long("2016-08-16 00:00:00") ){
////						//老微信可以用16号前注册已存在的手机账号实名认证（绑定手机号）
////						return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
////					}
//				
//			}
//		}
		User user = userMapper.getUserByAllWay(phone);
		if (user != null) {
			return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
		}
    	
    	user = userMapper.getUser(userId);
    	String oldPhone = user.getBindPhone();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	int count = userMapper.userPhoneBind(userId, phone);
    	if(count == 1 ){
    		data.put("result", "SUCCESS");
    		data.put("resultMsg", "绑定成功！");
    		
    		UserBindLog  userBindLog = new UserBindLog();
    		userBindLog.setUserId(userId);
    		userBindLog.setCreateTime(System.currentTimeMillis());
    		userBindLog.setNewPhone(phone);
    		userBindLog.setOldPhone(oldPhone);
    		userBindLog.setType(0);
    		userMapper.addUserBindLog(userBindLog);
    		
    	}else{
    		data.put("result", "FAIL");
    		data.put("resultMsg", "绑定失败！");
    	}
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
  	* 微信绑定手机号
  	* @param jsonResponse
  	* @param weixinId
    * @return 0成功、-1手机号已经绑定微信不能再次绑定微信、-2短信发送失败
    */
	public int weixinBindPhoneSendMsg(String phone) {
		//1、验证微信是否符合绑定条件
//		User weixinUser = getUserByAllWay(weixinId);
//		if(weixinUser != null && StringUtils.isNotEmpty(weixinUser.getBindPhone())){//
//			return -1;
//		}
		//1、验证手机是否符合绑定条件
		User phoneUser = getUserByAllWay(phone);
		if(phoneUser != null && StringUtils.isNotEmpty(phoneUser.getBindWeixin())){//
			return -1;
		}
		//2、发送短信验证码
		if (whitePhoneService.getWhitePhone(phone) == 0){//如果手机号不在白名单 
			boolean result = yunXinSmsService.sendCode(phone,1);
			if(!result){
				return -2;
			}
		}
		return 0;
	}
    /**
     * 手机APP端绑定微信
     * @param phone
     * @param verifyCode
     * @param openId
     * @param openuid
     * @param accessToken
     * @param userType
     * @param ip
     * @param response
     * @param client
     * @return
     */
    @SuppressWarnings("unchecked")
  	public JsonResponse weixinRegPhoneNumberCommit(String phone,  String verifyCode,  String openId, String openuid, String accessToken, UserType userType, String ip, HttpServletResponse response,ClientPlatform client) {
          JsonResponse jsonResponse = new JsonResponse();
          if (whitePhoneService.getWhitePhone(phone) == 0){//如果手机号不在白名单
	          //1、验证短信验证码
	          if(!yunXinSmsService.verifyCode(phone, verifyCode)){
	      		return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
	          }
          }
          //2、获取用户微信信息
          Map<String, Object> map = new HashMap<String, Object>();
          map.put(V2Constants.ACCESS_TOKEN, accessToken);
          map.put("openid", openId);
          IRawDataCredentials tokenCredentials = new RawDataCredentials(map);

          ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(tokenCredentials, ip);
          if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
              logger.error("fail to get user, snsResponse:{}", snsResponse);
              return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
          }

          ISnsEndUser endUserObj = snsResponse.getData();

          String weixinId = endUserObj.getPlatformIndependentId();
          String nickName = escapeNickName(endUserObj.getNickName());
  		  String userIconUrl = endUserObj.getAvatar();
          if (!StringUtils.equals(openuid, weixinId)) {
              logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid,weixinId);
              return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
          }

        //手机绑定手机号不需要添加平台关联信息
        int ret = weixinBindPhone(0,phone, weixinId,null,nickName,userIconUrl,ip,client,response);
        if(ret == -1){//微信已经绑定此手机号
        	return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_ALREADY_BINDED_LOGIN);
        }else if(ret == -2){//微信已经被他人绑定,请用手机号登录
        	return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED_LOGIN);
        }else if(ret == -3){//手机已被注册或绑定，请使用其他手机号
        	return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
        }
        //获取供应商信息
        Wrapper<com.jiuyuan.entity.newentity.StoreBusiness> storeBusinessWrapper = new EntityWrapper<com.jiuyuan.entity.newentity.StoreBusiness>();
        storeBusinessWrapper.eq("BindWeixinId", weixinId);
        List<com.jiuyuan.entity.newentity.StoreBusiness> storeBusinessList = storeMapper.selectList(storeBusinessWrapper);
        com.jiuyuan.entity.newentity.StoreBusiness storeBusiness = new com.jiuyuan.entity.newentity.StoreBusiness();
        if(storeBusinessList != null && storeBusinessList.size()>0){
        	storeBusiness = storeBusinessList.get(0);
        }
        long supplierId = storeBusiness.getSupplierId();
        //查看关联供应商
        if(supplierId == 0){
        	Wrapper<UserNew> wrapper = new EntityWrapper<UserNew>();
        	wrapper.eq("phone", storeBusiness.getPhoneNumber());
        	List<UserNew> list = userNewMapper.selectList(wrapper);
        	if(list != null && list.size() > 0){
        		try {
        			storeBusinessNewService.updateSupplierIdById(storeBusiness.getId(),list.get(0).getId());
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
        	}
        }
        
        Map<String, Object> data = new HashMap<String, Object>();
        User user = getUserByAllWay(weixinId); 
        data.put("gainCoins", getUnavalCoins(user));
        data.put("result", "SUCCESS");
       	data.put("resultMsg", "绑定成功！");
        return jsonResponse.setSuccessful().setData(data);
      }
   
   	 
    /**
	 * 微信绑定手机
	 * @param userSharedRecordId 分享记录ID
	 * @param phone
	 * @param weixinId//微信用户唯一ID
	 * @param publiOpenId//公众号应用ID
	 * @param nickName
	 * @param userIconUrl
	 * @return 0成功、-1微信已经绑定此手机号、-2微信已经被他人绑定,请用手机号登录、-3手机已被注册或绑定，请使用其他手机号
	 */
	public int weixinBindPhone(long userSharedRecordId,String phone, String weixinId,String publiOpenId, String nickName, String userIconUrl,String ip,ClientPlatform client,HttpServletResponse response) {
			
			User userByWeixin = getUserByAllWay(weixinId);
			logger.info("weixinBindPhone分享注册添加玖币logger，userSharedRecordId[{}],phone[{}]",userSharedRecordId,phone);
	        long userId = 0;
//	        1、微信绑定手机号
	        User userByPhone = userMapper.getUserByAllWay(phone);
	        if(userByWeixin == null){
	        	if(userByPhone == null){
	        		logger.info("Weixin user and phone user is all null");
	        		//3.1、添加信息微信用户同时绑定手机号
	        		userId = addUserWeixinAndPhone(phone,UserType.WEIXIN, client,weixinId,nickName,userIconUrl);
	        	}else if(userByPhone != null){
	        		logger.info("Weixin user is null  and phone user is not null");
	        		//3.2、绑定微信到手机账号
	        		bindWeixinToPhone(userByPhone.getUserId(),weixinId,nickName,userIconUrl);
	        		userId = userByPhone.getUserId();
	        	}
	        }else if(userByWeixin != null ){//微信账号为空则提示用户
	        	String bingPhone = userByWeixin.getBindPhone();
	        	if(StringUtils.isNotEmpty(bingPhone)){//如果微信账号已经绑定了手机号则提示用户
	        		if(bingPhone.equals(phone)){
	        			logger.info("Weixin user is not null and bingPhone is toBindPhone ");
	        			return -1;//jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_ALREADY_BINDED_LOGIN);
	        		}else{
	        			logger.info("Weixin user is not null and bingPhone is not toBindPhone ");
	        			return -2;//jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED_LOGIN);
	        		}
	        	}else if(userByPhone != null ){
	        		//微信没绑定手机、手机也没绑定微信的情况下，将微信绑定到手机账号上，将之前的微信账号删除
	        		if(StringUtils.isEmpty(userByPhone.getBindWeixin())){
	        			logger.info("Weixin user is not null and bindPhone is null and phone user is not null and bindWeixin is null");
	        			bindWeixinToPhone(userByPhone.getUserId(),weixinId,nickName,userIconUrl);
	        			userMapper.delWeixinUserByUserId(userByWeixin.getUserId());
	        			logger.info("del weixin user userId : " +userByWeixin.getUserId());
	        		}else{
	        			logger.info("Weixin user is not null and phone user is not null ");
		        		return -3;//jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
	        		}
	        	}else if(userByPhone == null ){
	        		logger.info("Weixin user is not null and phone user is null ");
	        		//3.3、绑定手机到微信账号
	        		bingPhoneToWeixin(userByWeixin.getUserId(),phone);
	        		userId = userByWeixin.getUserId();
	        	}
	        }
	        
	        //2、/添加公众号中间表信息 //app不会添加
	  	  	addBindUserRelation(weixinId, publiOpenId, userId);
	  	  	
	  	  	logger.info("绑定门店userSharedRecordId:"+userSharedRecordId);
	    	//  添加分享注册玖币收益
	  	  	if(userSharedRecordId != 0){
	  	  		userSharedService.addRegisterJiuCoin(userSharedRecordId, userId,client);
	  	  		
	  	  		//如果分享者绑定了门店，那么被分享者注册时也默认绑定同一家门店
		  	  	UserSharedRecord userSharedRecord = userSharedMapper.getRecordBySharedId(userSharedRecordId);
		  	  	logger.info("绑定门店userSharedRecord:"+userSharedRecord.toString());
		  	  	if(userSharedRecord!=null){
			  	  	UserMember userMember = userMemberMapper.getByUserId(userSharedRecord.getUserId());
			  	  	logger.info("绑定门店userMember:"+userMember.toString());
					if (userMember != null) {
						userMemberMapper.addDistributionPartners(userMember.getId());
						String inviteRelation = "";
						if(userMember.getInviteRelation() != null ){
							inviteRelation = userMember.getInviteRelation()+ userSharedRecord.getUserId() +",";
						}else{
							inviteRelation = "," + userSharedRecord.getUserId() +",";
						}
						logger.info("绑定门店inviteRelation:"+inviteRelation);
						logger.info("绑定门店belongStoreId:"+userMember.getBelongStoreId());
						if (userMember.getBelongStoreId() != null && userMember.getBelongStoreId() > 0) {
							//registerDelegator.bindStoreRelation(user, userMember.getBelongStoreId(), userSharedRecord.getUserId(), inviteRelation , client);
							StoreBusiness storeBusiness = storeBusinessMapper.getById(userMember.getBelongStoreId());
							
							long time = System.currentTimeMillis();
							UserMember userMemberNew = new UserMember(userId, time, time, 0, userSharedRecord.getUserId(), 0, storeBusiness.getBusinessName(), userMember.getBelongStoreId() ,inviteRelation);
							logger.info("绑定门店userMemberNew:"+userMemberNew.toString());
							userMemberService.add(userMemberNew, time);
							
							Activity activity = activityService.getActivity("collectstore");
							logger.info("绑定门店activity:"+activity.toString());
							if (activity != null ) {		
								GRANT_COINS = activity.getGrantAmountRandom();
							}
							
							userCoinService.updateUserCoin(userId, 0, GRANT_COINS, activity.getActivityCode(), System.currentTimeMillis(), UserCoinOperation.ACTIVITY, null, client.getVersion());
						}
					}
		  	  	}
	  	  	}
	    	
	  	  	
	        //3、做登陆操作
	        User loginUser = getUserByAllWay(weixinId); 
	        loginUser(response, loginUser, ip,client);
	        logger.error("--------WAP--------login is ok =  user:"+loginUser.toString());
	        return 0;
	}
	
	
	
	 /**
		 * 登陆用户（后期多处登陆修改为统一调用该方法）
		 * 操作1：添加用户新到Headr中
		 * 操作2：记录用户登陆日志
		 * @param response
		 * @param user
		 */
//		public void loginUser(HttpServletResponse response, User user) {
//			loginUser(response,user,null,null) ;
//		}
	 /**
		 * 登陆用户（后期多处登陆修改为统一调用该方法）
		 * 操作1：添加用户新到Headr中
		 * 操作2：记录用户登陆日志
		 * @param response
		 * @param user
		 */
		public void loginUser(HttpServletResponse response, User user,String ip, ClientPlatform client) {
			//添加用户信息到Header中,生成cockie值放入Header中
	    	addSetCookie(response, user);
	    	 //3、记录用户登录日志
	        addUserLoginLog(ip, client, user);
		}
	
	
		/**
	     * 记录用户登录日志
	     * @param ip
	     * @param client
	     * @param user
	     */
		public void addUserLoginLog(String ip, ClientPlatform client, User user) {
			UserLoginLog userLoginLog = new UserLoginLog();
	        userLoginLog.setUserId(user.getUserId());
	        if(client != null){
	        	userLoginLog.setClientType(client.getPlatform().getValue());
	        	userLoginLog.setClientVersion(client.getVersion());	
	        }
	        userLoginLog.setIp(ip);
	        userLoginLog.setCreateTime(System.currentTimeMillis());
	        addUserLoginLog(userLoginLog);
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//	private void addSetCookie(HttpServletResponse response, User user) {
//		String cookieValue = LoginUtil.buildLoginCookieValue(user.getUserRelatedName(), user.getUserType());
//  		response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
//	}
	
	/**
     * 添加用户信息到Header中
     * @param response
     * @param user
     */
	public void addSetCookie(HttpServletResponse response, User user) {
		String cookieValue = LoginUtil.buildLoginCookieValue(user.getUserRelatedName(), user.getUserType());
        response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
        //logger.debug("cookie :{},userId:{}", LoginUtil.buildLoginCookieHeaderValue(cookieValue),user.getUserId());
	}

	private void addLoginLog(String ip, ClientPlatform client, User user) {
		UserLoginLog userLoginLog = new UserLoginLog();
          userLoginLog.setUserId(user.getUserId());
          if(client != null){
          	userLoginLog.setClientType(client.getPlatform().getValue());
          	userLoginLog.setClientVersion(client.getVersion());	
          }
          if(StringUtils.isNotEmpty(ip)){
        	  userLoginLog.setIp(ip);
          }
          userLoginLog.setCreateTime(System.currentTimeMillis());
          addUserLoginLog(userLoginLog);
	}

	/**
	 * 根据公众号OpenId获得User
	 * @param publicOpenId
	 * @return
	 */
	public User getUserByPublicOpenId(String publicOpenId) {
		 User user = null;
		BindUserRelation bindUserRelation= userMapper.getBindUserRelationByOpenId(publicOpenId,ThirdAppType.WEIXIN_PUBLIC_NUM1.getIntValue());
		if(bindUserRelation != null){
//			String weixinId = bindUserRelation.getUId();
//		    user = getUserByAllWay(weixinId);
			long userId = bindUserRelation.getUserId();
			user = getUser(userId);
		}
		
		return user;
	}
	
	public User checkUserByUserId(long userId) {
		
		User user = userMapper.getUser(userId);
		if(user == null){
			return null;
		}
		
		String weixinId = user.getBindWeixin();
		BindUserRelation bindUserRelation= userMapper.getBindUserRelationByUid(weixinId,ThirdAppType.WEIXIN_PUBLIC_NUM1.getIntValue());
		if(bindUserRelation == null){
			return null;
		}
		
		return user;
	}
  
    /**
     * 添加公众号中间表信息
     * @param weixinId
     * @param publiOpenId
     * @param userId
     * @param platformType
     */
	public void addBindUserRelation(String weixinId, String publicOpenId, long userId ) {
		if(publicOpenId != null){
			//判断是否已经存在
			BindUserRelation bindUserRelation= userMapper.getBindUserRelationByOpenId(publicOpenId,ThirdAppType.WEIXIN_PUBLIC_NUM1.getIntValue());
			if(bindUserRelation == null){
				//添加
				ThirdAppType thirdAppType = ThirdAppType.WEIXIN_PUBLIC_NUM1;
		  		BindUserRelation newBindUserRelation = new BindUserRelation();
		  		newBindUserRelation.setUserId(userId);
		  		newBindUserRelation.setOpenId(publicOpenId);
		  		newBindUserRelation.setUId(weixinId);
		  		newBindUserRelation.setType(thirdAppType);
		  		userMapper.addBindUserRelation(newBindUserRelation);
			}else{
				long bindUserRelationUserId = bindUserRelation.getUserId();
				if(bindUserRelationUserId != userId){
					long bindUserRelationId = bindUserRelation.getId();
					userMapper.updBindUserRelationUserId(bindUserRelationId,userId);
				}
			}
		}
	}
	/**
	 * 根据公众号OpenId获得User绑定关系对象
	 * @param publicOpenId
	 * @return
	 */
	public BindUserRelation getBindUserRelationByOpenId(  String openId ) {
		return userMapper.getBindUserRelationByOpenId(openId,ThirdAppType.WEIXIN_PUBLIC_NUM1.getIntValue());
	}
	
	public BindUserRelation getBindUserRelationByUid( String weixinId ) {
		
		return userMapper.getBindUserRelationByUid(weixinId,ThirdAppType.WEIXIN_PUBLIC_NUM1.getIntValue());
	}
	 
	
   
    

	
	

	 /**
     * 添加用户绑定日志
     * @param phone 手机号
     * @param userId
     */
    private void addUserBindPhoneLog(String phone, long userId) {
		UserBindLog  userBindLog = new UserBindLog();
		userBindLog.setUserId(userId);
		userBindLog.setCreateTime(System.currentTimeMillis());
		userBindLog.setNewPhone(phone);
		userBindLog.setType(0);
		userMapper.addUserBindLog(userBindLog);
	}
    



	private int getUnavalCoins(User user) {
			long userId = user.getUserId();
            UserCoin coins = userCoinService.getUserCoin(userId);
            int gainCoins = coins == null ? 0 : coins.getUnavalCoins();
		return gainCoins;
	}



//	private void addSetCookie(HttpServletResponse response, User user) {
//		String cookieValue = LoginUtil.buildLoginCookieValue(user.getUserRelatedName(), user.getUserType());
//  		response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
//	}

//	private void addLoginLog(String ip, ClientPlatform client, User user) {
//		UserLoginLog userLoginLog = new UserLoginLog();
//          userLoginLog.setUserId(user.getUserId());
//          if(client != null){
//          	userLoginLog.setClientType(client.getPlatform().getValue());
//          	userLoginLog.setClientVersion(client.getVersion());	
//          }
//          userLoginLog.setIp(ip);
//          userLoginLog.setCreateTime(System.currentTimeMillis());
//          addUserLoginLog(userLoginLog);
//	}

	

   
	  /**
     * 添加用户并绑定手机号
     * @param phone
     * @param userType
     * @param client
     * @param weixinId
     * @param nickName
     * @param userIconUrl
     */
    private long addUserWeixinAndPhone(String phone,UserType userType,ClientPlatform client,String weixinId ,String nickName,String userIconUrl) {
    	
    	long time = System.currentTimeMillis();
  		User user = new User();
  		user.setUserName(weixinId);
//  		user.setUserRelatedName(client.getPlatform().getValue());
  		user.setUserRelatedName(weixinId);
  		user.setBindWeixin(weixinId);
  		user.setUserType(userType);
  		user.setUserNickname(nickName);
  		user.setUserIcon(userIconUrl);
  		user.setUserPassword("");
//  		user.setUserPassword(DigestUtils.md5Hex("nopassword"));
  		user.setStatus(UserStatus.NORMAL);
  		user.setCreateTime(time);
  		user.setUpdateTime(time);
  		if(client != null && client.getPlatform() != null && client.getPlatform().isIOS()){
        	user.setRegistrationSource(UserRegSource.IPHONE.getIntValue());
        }else if(client != null && client.getPlatform() != null && client.getPlatform().isAndroid()){
        	user.setRegistrationSource(UserRegSource.ANDROID.getIntValue());
        }else if(client != null && client.getPlatform() != null && client.getPlatform().is("web")){
        	user.setRegistrationSource(UserRegSource.WEB.getIntValue());
        }else{
        	user.setRegistrationSource(UserRegSource.WEB.getIntValue());
        }
  		user.setBindPhone(phone);
  		
  		registerFacade.addUser(user, 0, client);
  		long userId = user.getUserId();

  		//添加绑定日志
  		addUserBindPhoneLog(phone, userId);
  		
  		return userId;
	}
    /**
     * 绑定微信到手机账号
     * @param userId
     * @param weixinId
     * @param nickName
     * @param userIconUrl
     */
	private void bindWeixinToPhone(long userId, String weixinId,String nickName,String userIconUrl) {
		userMapper.userWeixinBind(userId,weixinId,nickName,userIconUrl);
		addUserBindWeixinLog(weixinId,userId);
	}

	/**
	 * 绑定手机号到微信账号
	 * @param userId
	 * @param phone
	 */
	private void bingPhoneToWeixin(long userId, String phone) {
		userMapper.userPhoneBind(userId,phone);
		addUserBindPhoneLog(phone, userId);
	}

    /**
     * 添加用户绑定日志
     * @param phone 微信ID
     * @param userId
     */
    private void addUserBindWeixinLog(String weixinId, long userId) {
		UserBindLog  userBindLog = new UserBindLog();
		userBindLog.setUserId(userId);
		userBindLog.setCreateTime(System.currentTimeMillis());
		userBindLog.setWeixinId(weixinId);
		userBindLog.setType(1);
		userMapper.addUserBindLog(userBindLog);
	}

	private String escapeNickName(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (!Character.isHighSurrogate(ch) && !Character.isLowSurrogate(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    public JsonResponse weiXinBindCommit(String phone,  String verifyCode, long userId, String weiXinId, String nickName, String userIconUrl) {
    	JsonResponse jsonResponse = new JsonResponse();
    	if (whitePhoneService.getWhitePhone(phone) == 0){//如果手机号不在白名单
    		if ( !registerFacade.verifyPhoneVerifyCode(phone, verifyCode, true, CodeUseage.PHONE_BIND)) {
    			return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
    		}
    	}
    	User user = getUserByAllWay(weiXinId);
    	if (user != null) {
    		return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED);
    	}
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	int count = userMapper.userWeixinBind(userId, weiXinId, nickName, userIconUrl);
    	if(count == 1 ){
    		data.put("result", "SUCCESS");
    		data.put("resultMsg", "绑定成功！");
    		
    		UserBindLog  userBindLog = new UserBindLog();
	      	userBindLog.setUserId(userId);
	      	userBindLog.setCreateTime(System.currentTimeMillis());
	      	userBindLog.setWeixinId(weiXinId);
	      	userBindLog.setType(1);
	      	userMapper.addUserBindLog(userBindLog);
    	}else{
    		data.put("result", "FAIL");
    		data.put("resultMsg", "绑定失败！");
    	}
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @SuppressWarnings("unchecked")
    public JsonResponse weiXinUnBindCommit(String phone,  String verifyCode, long userId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	if (whitePhoneService.getWhitePhone(phone) == 0){//如果手机号不在白名单
    		if ( !registerFacade.verifyPhoneVerifyCode(phone, verifyCode, true, CodeUseage.PHONE_BIND)) {
        		return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
        	}
    	}
    	
    	
    	User user = userMapper.getUser(userId);
    	String weixinId = user.getBindWeixin();
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	int count = userMapper.userWeixinUnbind(userId);
    	if(count == 1 ){
    		data.put("result", "SUCCESS");
    		data.put("resultMsg", "解绑成功！");
    		
    		UserBindLog  userBindLog = new UserBindLog();
	      	userBindLog.setUserId(userId);
	      	userBindLog.setCreateTime(System.currentTimeMillis());
	      	userBindLog.setWeixinId(weixinId);
	      	userBindLog.setType(2);
	      	userMapper.addUserBindLog(userBindLog);
    	}else{
    		data.put("result", "FAIL");
    		data.put("resultMsg", "解绑失败！");
    	}
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    
    @SuppressWarnings("unchecked")
    public JsonResponse sendPhoneVerifyCode(UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	//String sendPhone = "13675881551";
    	String sendPhone = getUserPhoneNumber(userDetail);
		if(sendPhone != null && sendPhone.length() == 11){
			data.put("sendPhone", sendPhone);
			boolean result = yunXinSmsService.sendCode(sendPhone,1);
			if(result){
				data.put("sendResult", "SUCCESS");
				
			}else{
				data.put("sendResult", "FAIL");
				return jsonResponse.setResultCode(ResultCode.PHONE_SEND_FAIL);
			}
			
		}else{
			data.put("sendResult", "ERROR_NUMBER");
			data.put("sendResultMsg", "手机号码有误！");
			return jsonResponse.setResultCode(ResultCode.PHONE_ERROR_NUMBER);
		}
    	

    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @SuppressWarnings("unchecked")
    public JsonResponse weixinRegSendPhoneVerifyCode(String phone) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	//String sendPhone = "13675881551";
    	
    	if(phone != null && phone.length() == 11){
//    		User user = userMapper.getUserByPhone(phone);
    		User user = userMapper.getUserByAllWay(phone);
    		if (user != null) {
    			return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
    		}
    		
    		boolean result = yunXinSmsService.sendCode(phone,1);
    		if(result){
    			data.put("sendResult", "SUCCESS");
    			
    		}else{
    			data.put("sendResult", "FAIL");
    			return jsonResponse.setResultCode(ResultCode.PHONE_SEND_FAIL);
    		}
    		
    	}else{
    		data.put("sendResult", "ERROR_NUMBER");
    		data.put("sendResultMsg", "手机号码有误！");
    		return jsonResponse.setResultCode(ResultCode.PHONE_ERROR_NUMBER);
    	}
    	
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @SuppressWarnings("unchecked")
    public JsonResponse sendPhoneVerifyCode(String phone)  {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	if(phone != null && phone.length() == 11){
//    		List<User> userList = userMapper.getUserListByPhone(phone);
//    		if(userList != null && userList.size() >= 2){
//    			return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
//    		}else if(userList != null && userList.size() == 1){
//    			User user = userList.get(0);
//    			if (user != null) {
//					return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
//				}
//    		}
    		User user = userMapper.getUserByAllWay(phone);
    		if (user != null) {
				return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
			}

	    	
			boolean result = yunXinSmsService.sendCode(phone,1);
			if(result){
				data.put("sendResult", "SUCCESS");
			
			}else{
				data.put("sendResult", "FAIL");
				return jsonResponse.setResultCode(ResultCode.PHONE_SEND_FAIL);
			}
    		
    	}else{
    		data.put("sendResult", "ERROR_NUMBER");
    		data.put("sendResultMsg", "手机号码有误！");
    		return jsonResponse.setResultCode(ResultCode.PHONE_ERROR_NUMBER);
    	}
    	
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    

    
    @SuppressWarnings("unchecked")
    public JsonResponse weixinRegSendCode(String phone)  {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	//String sendPhone = "13675881551";
    	
    	if(phone != null && phone.length() == 11){
//    		User user = userMapper.getUserByPhone(phone);
    		User user = userMapper.getUserByAllWay(phone);
    		if (user != null && StringUtils.isNotEmpty(user.getBindWeixin())) {
    			return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
    		}
    		
    		boolean result = yunXinSmsService.sendCode(phone,1);
    		
    		if(result){
    			data.put("sendResult", "SUCCESS");
    		}else{
    			if (whitePhoneService.getWhitePhone(phone) == 0){//如果手机号不在白名单
    				data.put("sendResult", "FAIL");
        			return jsonResponse.setResultCode(ResultCode.PHONE_SEND_FAIL);
        		}else{
        			data.put("sendResult", "SUCCESS");
        		}
    		}
    		
    	}else{
    		data.put("sendResult", "ERROR_NUMBER");
    		data.put("sendResultMsg", "手机号码有误！");
    		return jsonResponse.setResultCode(ResultCode.PHONE_ERROR_NUMBER);
    	}
    	
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    public String getUserPhoneNumber(UserDetail userDetail) {
    	String sendPhone = "";
    	if(userDetail.getUser().getUserType() == UserType.EMAIL){
			if(userDetail.getUser().getBindPhone() == null || userDetail.getUser().getBindPhone().length() != 11){
				return sendPhone;
			} else { sendPhone = userDetail.getUser().getBindPhone();
			}
			
		}else if(userDetail.getUser().getUserType() == UserType.PHONE && userDetail.getUser().getUserName().length() == 11){
			sendPhone = userDetail.getUser().getUserName();
		}else if(userDetail.getUser().getUserType() == UserType.WEIXIN && userDetail.getUser().getBindPhone()!= null && userDetail.getUser().getBindPhone().length() == 11){
			sendPhone = userDetail.getUser().getBindPhone();
		}
    	return sendPhone;
    }

	public User getUserByYJJNumber(long yJJNumber) {
		return userMapper.getUserByYJJNumber(yJJNumber);
	}

	public int updateUserInvite(long userId, int weekInviteCount) {
		long time = System.currentTimeMillis();
		
		return userMapper.updateUserInvite(userId, weekInviteCount, time);
	}

	public int updateWeekInviteOrderCount(long userId, int weekInviteOrderCount) {
		long time = System.currentTimeMillis();
		return userMapper.update(userId, weekInviteOrderCount, time);
	}

	/**
     * 同步昵称
     * @param user
     * @param nickName
     */
	public void synNickName(long userId,String userNickName, String weiXinNickName) {
		//同步昵称
		boolean needUpdate = false;
		if (!StringUtils.equals(userNickName, weiXinNickName)) {
			needUpdate = true;
			userNickName = weiXinNickName;
		}
		if (needUpdate) {
			updateUserNickName(userId, userNickName);
		}
		/* 头像不需要同步
		String userIcon = user.getUserIcon();
		if (!StringUtils.equals(userIcon, endUser.getAvatar())) {
		    needUpdate = true;
		    userIcon = endUser.getAvatar();
		}
		 */
	}


    
	
	
	//设置俞姐姐号
//	List<YJJNumber> yjjNumberList = null;
//	// getMemCache
//	String groupKey = MemcachedKey.GROUP_KEY_YJJNUMBER;
//	String key = "yjjnumber";
//	Object obj = memcachedService.get(groupKey, key);
//	if (obj != null && ((List<YJJNumber>)obj).size()>0) {
//		yjjNumberList = (List<YJJNumber>)obj;
//	}
//	else {
//		//分段去取10组1000个未使用俞姐姐号放入内存使用
//		yjjNumberList = getYJJNumberList(1, 100000, 1000 , 0);
//		List<YJJNumber> yjjNumberList2 = getYJJNumberList(100000, 200000, 1000 , 0);
//		List<YJJNumber> yjjNumberList3 = getYJJNumberList(200000, 300000, 1000 , 0);
//		List<YJJNumber> yjjNumberList4 = getYJJNumberList(300000, 400000, 1000 , 0);
//		List<YJJNumber> yjjNumberList5 = getYJJNumberList(400000, 500000, 1000 , 0);
//		List<YJJNumber> yjjNumberList6 = getYJJNumberList(500000, 600000, 1000 , 0);
//		List<YJJNumber> yjjNumberList7 = getYJJNumberList(600000, 700000, 1000 , 0);
//		List<YJJNumber> yjjNumberList8 = getYJJNumberList(700000, 800000, 1000 , 0);
//		List<YJJNumber> yjjNumberList9 = getYJJNumberList(800000, 900000, 1000 , 0);
//		List<YJJNumber> yjjNumberList10 = getYJJNumberList(900000, 1000000, 1000 , 0);
//		
//		if(yjjNumberList2 != null && yjjNumberList2 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList2);
//		}
//		if(yjjNumberList3 != null && yjjNumberList3 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList3);
//		}
//		if(yjjNumberList4 != null && yjjNumberList4 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList4);
//		}
//		if(yjjNumberList5 != null && yjjNumberList5 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList5);
//		}
//		if(yjjNumberList6 != null && yjjNumberList6 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList6);
//		}
//		if(yjjNumberList7 != null && yjjNumberList7 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList7);
//		}
//		if(yjjNumberList8 != null && yjjNumberList8 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList8);
//		}
//		if(yjjNumberList9 != null && yjjNumberList9 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList9);
//		}
//		if(yjjNumberList10 != null && yjjNumberList10 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList10);
//		}
//		memcachedService.set(groupKey, key, DateConstants.SECONDS_FOREVER, yjjNumberList);
//	}
//	
//	
//	
//	int randomNum = (int) (Math.random() * yjjNumberList.size()) ;
//	
//	YJJNumber number = yjjNumberList.get(randomNum);
//	
//	int k = 0;
//while(number.getNumber() <= 0 && k++ < 5){
//	yjjNumberList.remove(randomNum);
//	randomNum = (int) (Math.random() * yjjNumberList.size()) ;
//	number = yjjNumberList.get(randomNum);
//}
//	
//	user.setyJJNumber(number.getNumber());

		
//	yjjNumberList.remove(randomNum);
//	updateYjjNumberUsed(number.getNumber());
//	
//	// setMemCache
//	memcachedService.set(groupKey, key, DateConstants.SECONDS_FOREVER, yjjNumberList);
	
	
	public StoreBusiness getStoreBusinessByPhone(String phone) {
		return userMapper.getStoreBusinessByPhone(phone);
	}	
	
	public StoreBusiness getStoreBusiness4Login(String userName) {
		return userMapper.getStoreBusiness4Login(userName);
	}
	
}
