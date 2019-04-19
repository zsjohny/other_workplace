package com.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.GroupDefinitionException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.Status;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.StoreAudit;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.account.UserLoginLog;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.ground.GroundBonusGrant;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.WhitePhoneService;
import com.jiuyuan.service.common.GroundBonusGrantFacade;
import com.jiuyuan.service.common.IGroundUserService;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.service.common.UcpaasService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.SenderUtil;
import com.jiuyuan.util.oauth.common.credential.IRawDataCredentials;
import com.jiuyuan.util.oauth.common.credential.RawDataCredentials;
import com.jiuyuan.util.oauth.sns.common.response.ISnsResponse;
import com.jiuyuan.util.oauth.sns.common.response.SnsResponseType;
import com.jiuyuan.util.oauth.sns.common.user.ISnsEndUser;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2API;
import com.jiuyuan.util.oauth.v2.V2Constants;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.StoreBusinessMapper;
import com.store.dao.mapper.StoreUserMapper;
import com.store.dao.mapper.UserLoginLogMapper;
import com.store.enumerate.StoreAuditStatusEnum;

@Service
public class StoreUserService {

	private static final Logger logger = LoggerFactory.getLogger(StoreUserService.class);
	
	
	//
	// private static int GRANT_COINS = 99 * 5;
	//
	// @Autowired
	// private WhitePhoneService whitePhoneService;

	@Autowired
	private StoreUserMapper userMapper;

	@Autowired
	private StoreLoginDelegator storeLoginDelegator;

	@Autowired
	private UserLoginLogMapper userLoginLogMapper;

	@Autowired
	private StoreAuditServiceShop storeAuditService;

	@Autowired
	private WhitePhoneService whitePhoneService;
	//
	// @Autowired
	// private RegisterFacade registerFacade;
	//
	@Autowired
	private YunXinSmsService yunXinSmsService;

	@Autowired
	private UcpaasService ucpaasService;

	@Autowired
	private ShopGlobalSettingService globalSettingService;
	
	@Autowired
	private GroundBonusGrantFacade groundBonusGrantFacade;

	//
	//
	// @Autowired
	// private MemcachedService memcachedService;
	//
	// @Autowired
	// private UserSharedMapper userSharedMapper;
	//
	// @Autowired
	// private UserMemberMapper userMemberMapper;
	//
	// @Autowired
	// private StoreBusinessMapper storeBusinessMapper;

	@Autowired
	@Qualifier("weiXinV2API4App")
	private WeiXinV2API weiXinV2API;
	
	@Autowired
	private IGroundUserService groundUserService;
	
	@Autowired
	private StoreMapper storeMapper;

	public void cancelAuth(long id) {
		userMapper.cancelAuth(id);
	}

	public StoreBusiness getStoreBusinessByWxaAppId(String wxAppId) {
		return userMapper.getStoreBusinessByWxaAppId(wxAppId);
	}

	public StoreBusiness getStoreBusiness4Login(String userName) {
		return userMapper.getStoreBusiness4Login(userName);
	}

	public StoreBusiness getStoreBusinessByBusinessNumber(String businessNumber) {
		return userMapper.getStoreBusinessByBusinessNumber(businessNumber);
	}

	public StoreBusiness getStoreBusinessByPhone(String phone) {
		return userMapper.getStoreBusinessByPhone(phone);
	}

	public StoreBusiness getStoreBusinessById(long storeId) {
		return userMapper.getStoreBusinessById(storeId);
	}

	public StoreBusiness getStoreBusinessByWeixinId(String weixinId) {
		return userMapper.getStoreBusinessByWeixinId(weixinId);
	}

	public StoreBusiness getStoreBusinessByWeiXinNum(String weiXinNum) {
		if (weiXinNum == null || "".equals(weiXinNum)) {
			return null;
		}
		return userMapper.getStoreBusinessByWeiXinNum(weiXinNum);
	}

	public List<StoreBusiness> getStoreBusinessAuditList(PageQuery pageQuery, int status) {
		return userMapper.getStoreBusinessAuditList(pageQuery, status);
	}

	// public int resetUserPassword(long userId, String password) {
	// return userMapper.resetUserPassword(userId, password);
	// }

	public int addUserLoginLog(UserLoginLog userLoginLog) {
		return userLoginLogMapper.addUserLoginLog(userLoginLog);
	}

	public UserLoginLog getUserNewestLoginLog(String businessNumber) {

		UserLoginLog userLoginLog = null;

		userLoginLog = userLoginLogMapper.getUserNewestLoginLog(businessNumber);

		return userLoginLog;
	}

	public int updateUserPassword(String userName, String password) {
		return userMapper.resetUserPassword(userName, password);
	}

	public int updateStoreActiveTime(long storeId, long time) {
		return userMapper.updateStoreActiveTime(storeId, time);
	}

	public int updateUserCid(long userId, String userCid) {
		return userMapper.updateUserCid(userId, userCid);
	}

	public int updateUserProtocolTime(long userId) {
		long time = System.currentTimeMillis();
		return userMapper.updateUserProtocolTime(userId, time);
	}

	/**
	 * 修改商家公告
	 */
	public int updateStoreNotice(long storeId, String storeNotice) {
		return userMapper.updateStoreNotice(storeId, storeNotice);
	}

	/**
	 * 同步昵称
	 * 
	 * @param user
	 * @param nickName
	 */
	public void synNickName(long storeId, String userNickName, String weiXinNickName) {
		// 同步昵称
		boolean needUpdate = false;
		if (!StringUtils.equals(userNickName, weiXinNickName)) {
			needUpdate = true;
			userNickName = weiXinNickName;
		}
		if (needUpdate) {
			updateUserNickName(storeId, userNickName);
		}
		/*
		 * 头像不需要同步 String userIcon = user.getUserIcon(); if
		 * (!StringUtils.equals(userIcon, endUser.getAvatar())) { needUpdate =
		 * true; userIcon = endUser.getAvatar(); }
		 */
	}

	@SuppressWarnings("unchecked")
	public JsonResponse weixinRegSendCode(String phone, String sendType) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();

		// String sendPhone = "13675881551";

		if (phone != null && phone.length() == 11) {
			// User user = userMapper.getUserByPhone(phone);
			StoreBusiness storeBusiness = getStoreBusinessByPhone(phone);
			if (storeBusiness != null && storeBusiness.getBindWeixinId() != null
					&& storeBusiness.getBindWeixinId().length() > 0) { // &&
																		// StringUtils.isNotEmpty(storeBusiness.getBindWeixinId())
				return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
			}
			boolean result = false;
			if (whitePhoneService.getWhitePhone(phone) > 0) {

				result = true;
			} else if (sendType != null && sendType.equals("sms")) {

				result = yunXinSmsService.sendCode(phone, 2);
			} else if (sendType != null && sendType.equals("voice")) {

				result = ucpaasService.send(phone, sendType, 2);
			}

			// boolean result = true;

			if (result) {
				data.put("sendResult", "SUCCESS");
			} else {
				if (whitePhoneService.getWhitePhone(phone) == 0) {// 如果手机号不在白名单
					data.put("sendResult", "FAIL");
					return jsonResponse.setResultCode(ResultCode.PHONE_SEND_FAIL);
				} else {
					data.put("sendResult", "SUCCESS");
				}
			}

		} else {
			data.put("sendResult", "ERROR_NUMBER");
			data.put("sendResultMsg", "手机号码有误！");
			return jsonResponse.setResultCode(ResultCode.PHONE_ERROR_NUMBER);
		}

		return jsonResponse.setSuccessful().setData(data);
	}

	@SuppressWarnings("unchecked")
	public JsonResponse sendPhoneVerifyCode(String phone) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();

		// String sendPhone = "13675881551";

		if (phone != null && phone.length() == 11) {
			boolean result = false;
			if (whitePhoneService.getWhitePhone(phone) > 0) {

				result = true;
			} else {
				result = yunXinSmsService.sendCode(phone, 2);
			}

			// boolean result = true;

			if (result) {
				data.put("sendResult", "SUCCESS");
			} else {
				if (whitePhoneService.getWhitePhone(phone) == 0) {// 如果手机号不在白名单
					data.put("sendResult", "FAIL");
					return jsonResponse.setResultCode(ResultCode.PHONE_SEND_FAIL);
				} else {
					data.put("sendResult", "SUCCESS");
				}
			}

		} else {
			data.put("sendResult", "ERROR_NUMBER");
			data.put("sendResultMsg", "手机号码有误！");
			return jsonResponse.setResultCode(ResultCode.PHONE_ERROR_NUMBER);
		}

		return jsonResponse.setSuccessful().setData(data);
	}

	/**
	 * 绑定微信
	 * 
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
	public JsonResponse weixinRegPhoneNumberCommit(String phone, String verifyCode, String sendType, String openId,
			String openuid, String accessToken, UserType userType, String ip, HttpServletRequest request,
			HttpServletResponse response, ClientPlatform client) {
		JsonResponse jsonResponse = new JsonResponse();
		if (phone == null || phone.trim().length() != 11) {
			return jsonResponse.setResultCode(ResultCode.PHONE_ERROR_NUMBER);
		}
		if (whitePhoneService.getWhitePhone(phone) == 0) {// 如果手机号不在白名单
			// 1、验证短信验证码
			// if(!yunXinSmsService.verifyCode(phone, verifyCode)){
			// return
			// jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
			// }
			if (sendType.equals("sms") && !yunXinSmsService.verifyCode(phone, verifyCode)) {
				return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
			} else if (sendType.equals("voice") && !ucpaasService.verifyCode(phone, verifyCode)) {
				return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
			}
		}
		// 2、获取用户微信信息
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
			logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid, weixinId);
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		// User user = getUserByAllWay(weixinId);
		// data.put("gainCoins", getUnavalCoins(user));
		data.put("result", "SUCCESS");
		data.put("resultMsg", "绑定成功！");
		data.put("needFillData", "YES");
		data.put("nextStep", "fillData");
		// 手机绑定手机号不需要添加平台关联信息
		int ret = weixinBindPhone(0, phone, weixinId, null, nickName, userIconUrl, ip, client, request, response);
		if (ret == -1) {// 微信已经绑定此手机号
			return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_ALREADY_BINDED_LOGIN);
		} else if (ret == -2) {// 微信已经被他人绑定,请用手机号登录
			return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED_LOGIN);
		} else if (ret == -3) {// 手机已被注册或绑定，请使用其他手机号
			return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
		} else if (ret == 1) {// 微信绑定已存在手机用户成功
			data.put("needFillData", "NO");
			data.put("nextStep", "index");
		}

		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 新用户填充资料2.4版本开始使用
	 * 
	 * @param phone
	 * @param verifyCode
	 * @param openId
	 * @param openuid
	 * @param accessToken
	 * @param userType
	 * @param ip
	 * @param response
	 * @param client
	 * @param client2
	 * @param response
	 * @param firstFill
	 * @param ip
	 * @param qualificationProofImages 
//	 * @param storeType 
	 * @param referenceNumber 
	 * @param storeAreaScope 
	 * @param storeAge 
	 * @param storeStyle 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public synchronized JsonResponse fillDataCommitV24(String businessName, String province, String city, String county,
			String businessAddress, String legalPerson, String idNumber, String regPhone, ClientPlatform client,
			UserDetail userDetail, HttpServletResponse response, String ip, String qualificationProofImages, String referenceNumber,
			String storeStyle, String storeAge, Integer storeAreaScope,String priceLevel,String purchaseChannel
	) {

		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		logger.info("fillDataCommitV24");
		
		StoreBusiness storeBusiness = getStoreBusinessByPhone(regPhone);
		if (storeBusiness == null) {
			return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USER_NOT_EXISTS);
		}
		
//		int checkCount = userMapper.getStoreBusinessCountByBusinessNumber(businessName);
//		if (checkCount > 0) {
//			int[] statusArray = { 0, 1, -2 };
//			int count = userMapper.selectAuditCountByStoreId(storeBusiness.getId(), statusArray);
//			if (count > 0) {
//				return jsonResponse.setResultCode(ResultCode.DUPLICATION_OF_BUSINESS_NAME);
//			}
//		}
		
		//2.3版本的没有上传资质图片的老用户
		if(storeAuditService.chectStoreBusiness(storeBusiness)){
			if(storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.pass.getIntValue()) > 0 || 
					storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.submit.getIntValue()) > 0){
				userMapper.updateStoreAuditStatus(storeBusiness.getId(),StoreAudit.UNFINISHED);
			}
		}
		
		int checkCount = userMapper.getStoreBusinessCountByBusinessNumber(businessName,storeBusiness.getId());
		if (checkCount > 0) {
//			int[] statusArray = { 0, 1, -2 };
//			int count = userMapper.selectAuditCountByStoreId(storeBusiness.getId(), statusArray);
//			if (count > 0) {
				return jsonResponse.setResultCode(ResultCode.DUPLICATION_OF_BUSINESS_NAME);
//			}
		}
		
		int[] statusArray = { 0, 1, -2 };
		int count = userMapper.selectAuditCountByStoreId(storeBusiness.getId(), statusArray);
		if (count > 0) {
			return jsonResponse.setResultCode(ResultCode.NO_SUBMIT_AUDIT_AGAIN);
		}

		if (storeBusiness.getPhoneNumber() == null && storeBusiness.getBindWeixinId() == null) {
			return jsonResponse.setResultCode(ResultCode.WEIXIN_OR_PHONE_UNBIND);
		}
		if (storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.submit.getIntValue()) > 0) {
			return jsonResponse.setResultCode(ResultCode.ALREADY_SUBMIT_AUDIT_INFO);
		}
		if (storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.pass.getIntValue()) > 0) {
			return jsonResponse.setResultCode(ResultCode.ALREADY_PASS_AUDIT_INFO);
		}
		if (storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.forbidden.getIntValue()) > 0) {
			return jsonResponse.setResultCode(ResultCode.ALREADY_FORBIDDEN_AUDIT_INFO);
		}
//		if (storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.fail.getIntValue()) > 0) {
//			return jsonResponse.setResultCode(ResultCode.ALREADY_FORBIDDEN_AUDIT_INFO);
//		}
		//校验是否再次填写地推人员手机号
		if(referenceNumber == null){
			logger.info("地推人手机号不合法！");
			return jsonResponse.setError("地推人手机号不合法！");
		}
		GroundUser groundUser = groundUserService.getGroundUserByPhoneWithAllStatus(referenceNumber);
		if(storeBusiness.getBusinessName() != null ){
			//是再次填写
            if(storeBusiness.getGroundUserPhone()!= null && referenceNumber == "" ||
            		storeBusiness.getGroundUserPhone() == null && !referenceNumber.equals("")){
            	logger.info("地推人手机号不可修改！");
            	return jsonResponse.setError("地推人手机号不可修改!");
            }
            if(storeBusiness.getGroundUserPhone() != null && referenceNumber != ""){
            	if(!storeBusiness.getGroundUserPhone().equals(referenceNumber)){
                	logger.info("地推人手机号不可修改！");
                	return jsonResponse.setError("地推人手机号不可修改!");
            	}
            }
			logger.info("地推人再次填写，手机号与之前相符，可以继续填充资料！");
		}else{
			//第一次填写
			if(!StringUtils.isEmpty(referenceNumber)){//没有推荐人手机号
				//有推荐人手机号
				if(groundUser == null){//找不到地推人员
					logger.info("地推人手机号不存在!");
					return jsonResponse.setError("地推人手机号不存在!");
				}else{
					//绑定地推人员
			        if(groundUser.getStatus() == Status.HIDE.getIntValue()){
			        	logger.info("地推人手机号不可用！");
			        	return jsonResponse.setError("地推人手机号不可用！");
			        }
					storeBusiness.setGroundUserId(groundUser.getId());
					String superIds = groundUser.getSuperIds();
					storeBusiness.setSuperIds(superIds);
					storeBusiness.setGroundUserName(groundUser.getName());
					storeBusiness.setGroundUserPhone(referenceNumber);
					logger.info("第一次填写地推人员的信息在门店信息表中！");
					//门店注册填资料时发放个人注册奖金和团队注册奖金
					logger.info("开始发放奖金groundBonusGrantFacade.grantRegBonus");
					groundBonusGrantFacade.grantRegBonus(groundUser.getId(),storeBusiness.getId());
					logger.info("新用户创建用户，添加注册奖金完成");
				}
			}
		}



		storeBusiness.setPriceLevel(priceLevel);
		storeBusiness.setPurchaseChannel(purchaseChannel);
		storeBusiness.setProvince(province);
		storeBusiness.setCity(city);
		storeBusiness.setCounty(county);
		storeBusiness.setBusinessAddress(businessAddress);
		storeBusiness.setLegalIdNumber(idNumber);
		storeBusiness.setLegalPerson(legalPerson);
		storeBusiness.setBusinessName(businessName);
		storeBusiness.setCompanyName(businessName);
		
		//店铺注册优化
		storeBusiness.setStoreAge(storeAge);
		storeBusiness.setStoreAreaScope(storeAreaScope);
		storeBusiness.setStoreStyle(storeStyle);
		
		
//		storeBusiness.setStoreType(storeType);
		storeBusiness.setQualificationProofImages(qualificationProofImages);
		List<StoreAudit> storeAuditList = storeAuditService.getAuditByStoreId(storeBusiness.getId());
		StoreAudit storeAudit = new StoreAudit();
		long nowTime = System.currentTimeMillis();
		if (storeAuditList.size()<=0) {
			storeAudit.setUpdateTime(nowTime);
			storeAudit.setStoreId(storeBusiness.getId());
//			if(storeAuditService.chectStoreBusiness(storeBusiness)){
//				storeAudit.setStatus(-1);
//			}else{
			storeAudit.setStatus(0);
//			}
			storeAudit.setCreateTime(nowTime);
			userMapper.saveStoreAudit(storeAudit);
		}else{
//			storeAudit = storeAuditList.get(0);
//			storeAudit.setStatus(0);
//			storeAudit.setUpdateTime(nowTime);
			userMapper.updateStoreAuditStatus(storeBusiness.getId(), 0);
		}
		// update by Charlie(唐静) 18/05/21 新认证
//		storeBusiness.setAuditStatus(0);
		storeBusiness.setDataAuditStatus(0);
		userMapper.fillStoreBusinessData(storeBusiness);
		
			// 通知后台管理人员、有新用户注册需要审核
			// 邮箱通知 &微信通知
			SenderUtil.sendAudit(globalSettingService.getJsonObject(GlobalSettingName.STORE_AUDIT_SEND));

		
		// if(firstFill){
		// storeLoginDelegator.loginUser(storeBusiness, response, ip,client);
		// }
		return jsonResponse.setSuccessful().setData(data);

	}

	

	/**
	 * 微信绑定手机
	 * 
	 * @param userSharedRecordId
	 *            分享记录ID
	 * @param phone
	 * @param weixinId//微信用户唯一ID
	 * @param publiOpenId//公众号应用ID
	 * @param nickName
	 * @param userIconUrl
	 * @return 0成功、-1微信已经绑定此手机号、-2微信已经被他人绑定,请用手机号登录、-3手机已被注册或绑定，请使用其他手机号
	 */
	public int weixinBindPhone(long userSharedRecordId, String phone, String weixinId, String publiOpenId,
			String nickName, String userIconUrl, String ip, ClientPlatform client, HttpServletRequest request,
			HttpServletResponse response) {

		StoreBusiness userByWeixin = getStoreBusinessByWeixinId(weixinId);

		long storeId = 0;
		int result = 0;
		// 1、微信绑定手机号
		StoreBusiness userByPhone = getStoreBusinessByPhone(phone);
		if (userByWeixin == null) {
			if (userByPhone == null) {
				logger.info("Weixin user and phone user is all null");
				// 3.1、添加信息微信用户同时绑定手机号
				storeId = addUserWeixinAndPhone(phone, UserType.WEIXIN, client, weixinId, nickName, userIconUrl);
			} else if (userByPhone != null) {
				if (userByPhone.getBindWeixinId() != null && userByPhone.getBindWeixinId().trim().length() > 0) {
					logger.info("Weixin user is null  and phone user is not null");
					return -3;
				}

				// 3.2、绑定微信到手机账号
				int count = oldUserBindWeixin(userByPhone.getId(), weixinId, nickName, userIconUrl);
				if (count != 1) {
					return -3;
				} else {
					result = 1;
				}
				// userId = userByPhone.getUserId();
			}
		}

		// 2、/添加公众号中间表信息 //app不会添加
		// addBindUserRelation(weixinId, publiOpenId, userId);

		// 添加分享注册玖币收益
		// if(userSharedRecordId != 0){
		// userSharedService.addRegisterJiuCoin(userSharedRecordId,
		// userId,client);
		// }

		// 3、做登陆操作
		StoreBusiness storeBusiness = getStoreBusinessByWeixinId(weixinId);

		storeLoginDelegator.loginUser(storeBusiness, request, response, ip, client);
		logger.info("--------WAP--------login is ok =  user:" + JSON.toJSONString(storeBusiness));
		return result;
	}

	/**
	 * 添加用户并绑定手机号
	 * 
	 * @param phone
	 * @param userType
	 * @param client
	 * @param weixinId
	 * @param nickName
	 * @param userIconUrl
	 */
	public long addUserWeixinAndPhone(String phone, UserType userType, ClientPlatform client, String weixinId,
			String nickName, String userIconUrl) {

		long time = System.currentTimeMillis();
		StoreBusiness storeBusiness = new StoreBusiness();
		storeBusiness.setCreateTime(time);
		storeBusiness.setUpdateTime(time);
		storeBusiness.setBindWeixinIcon(userIconUrl);
		storeBusiness.setBindWeixinName(nickName);
		storeBusiness.setBindWeixinId(weixinId);
		storeBusiness.setUserPassword("");
		storeBusiness.setUserName(weixinId);
		storeBusiness.setPhoneNumber(phone);
		storeBusiness.setActiveTime(0L);
        storeBusiness.setStatus(0);
        storeBusiness.setDistributionStatus(0);
        storeBusiness.setBankCardFlag(0);
        storeBusiness.setAlipayFlag(0);
        storeBusiness.setWeixinFlag(0);
        storeBusiness.setBusinessType(0);
//		userMapper.addStoreBusiness(storeBusiness);
		storeMapper.insert(storeBusiness);
		storeBusiness.setBusinessNumber(storeBusiness.getId() + 800000000);
		storeBusiness.setUpdateTime(System.currentTimeMillis());
		storeMapper.updateById(storeBusiness);
//		userMapper.updateUserBusinessNumber(storeBusiness.getId(), storeBusiness.getId() + 800000000);
		long storeId = storeBusiness.getId();
		// 添加绑定日志
		// addUserBindPhoneLog(phone, userId);

		return storeId;
	}

	public long addPhoneUser(String phone) {

		long time = System.currentTimeMillis();

		StoreBusiness storeBusiness = new StoreBusiness();
		storeBusiness.setCreateTime(time);
		storeBusiness.setUpdateTime(time);

		storeBusiness.setUserPassword("");
		storeBusiness.setUserName(phone);
		storeBusiness.setPhoneNumber(phone);
        storeBusiness.setStatus(0);
        storeBusiness.setDistributionStatus(0);
        storeBusiness.setBankCardFlag(0);
        storeBusiness.setAlipayFlag(0);
        storeBusiness.setWeixinFlag(0);
        storeBusiness.setBusinessType(0);
		userMapper.addStoreBusiness(storeBusiness);
		long storeId = storeBusiness.getId();
		// 添加绑定日志
		// addUserBindPhoneLog(phone, userId);

		return storeId;
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

	public int updateUserNickName(long storeId, String userNickName) {
		return userMapper.updateUserNickName(storeId, userNickName);
	}

	public int wxaAuth(String storeId, String wxaAppId, String weiXinNum, Long wxaOpenTime, Long wxaCloseTime) {
		//并且wxa_business_type设为2,is_open_wxa设为1
		Map<String, Object> map = new HashMap(6);
		map.put("storeId", storeId);
		map.put("wxaAppId", wxaAppId);
		map.put("weiXinNum", weiXinNum);
		map.put ("wxaOpenTime", wxaOpenTime);
		map.put ("wxaCloseTime", wxaCloseTime);
		return userMapper.wxaAuth(map);
	}

	public int oldUserBindWeixin(long id, String platformIndependentId, String nickName, String avatar) {
		long time = System.currentTimeMillis();
		StoreBusiness storeBusiness = new StoreBusiness();
		storeBusiness.setUpdateTime(time);
		storeBusiness.setBindWeixinIcon(avatar);
		storeBusiness.setBindWeixinName(nickName);
		storeBusiness.setBindWeixinId(platformIndependentId);
		storeBusiness.setId(id);

		return userMapper.oldUserBindWeixin(storeBusiness);
	}

	/**
	 * 获取用户的同步开关状态和倍率
	 * @return
	 */
	public Map<String, Object> getSynchronousButtonStatusAndRate(long storeId) {
		StoreBusiness storeBusiness = userMapper.getStoreBusinessById(storeId);
		Map<String,Object> synchronousButtonStatusAndRate = new HashMap<String,Object>();
		synchronousButtonStatusAndRate.put("synchronousButtonStatus", storeBusiness.getSynchronousButtonStatus());
		synchronousButtonStatusAndRate.put("rate", storeBusiness.getRate());
		return synchronousButtonStatusAndRate;
	}
	
//=================================================================================================================================================================
	/**
	 * 新用户填充资料2.3版本,2.4版本开始不再使用
	 * 
	 * @param phone
	 * @param verifyCode
	 * @param openId
	 * @param openuid
	 * @param accessToken
	 * @param userType
	 * @param ip
	 * @param response
	 * @param client
	 * @param client2
	 * @param response
	 * @param firstFill
	 * @param ip
	 * @param qualificationProofImages 
//	 * @param storeType 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized JsonResponse fillDataCommit(String businessName, String province, String city, String county,
			String businessAddress, String legalPerson, String idNumber, String regPhone, ClientPlatform client,
			UserDetail userDetail, HttpServletResponse response, String ip) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		int checkCount = userMapper.getStoreBusinessCountByBusinessNumber(businessName,0);
		if (checkCount > 0) {
			return jsonResponse.setResultCode(ResultCode.DUPLICATION_OF_BUSINESS_NAME);
		}
		StoreBusiness storeBusiness = getStoreBusinessByPhone(regPhone);
		// StoreBusiness storeBusiness =
		// getStoreBusinessById(userDetail.getId());
		if (storeBusiness == null) {
			return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USER_NOT_EXISTS);
		}

		int[] statusArray = { 0, 1, -2 };
		int count = userMapper.selectAuditCountByStoreId(storeBusiness.getId(), statusArray);
		if (count > 0) {
			return jsonResponse.setResultCode(ResultCode.NO_SUBMIT_AUDIT_AGAIN);
		}

		if (storeBusiness.getPhoneNumber() == null && storeBusiness.getBindWeixinId() == null) {
			return jsonResponse.setResultCode(ResultCode.WEIXIN_OR_PHONE_UNBIND);
		}
		if (storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.submit.getIntValue()) > 0) {
			return jsonResponse.setResultCode(ResultCode.ALREADY_SUBMIT_AUDIT_INFO);
		}
		if (storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.pass.getIntValue()) > 0) {
			return jsonResponse.setResultCode(ResultCode.ALREADY_PASS_AUDIT_INFO);
		}
		if (storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.forbidden.getIntValue()) > 0) {
			return jsonResponse.setResultCode(ResultCode.ALREADY_FORBIDDEN_AUDIT_INFO);
		}

		storeBusiness.setProvince(province);
		storeBusiness.setCity(city);
		storeBusiness.setCounty(county);
		storeBusiness.setBusinessAddress(businessAddress);
		storeBusiness.setLegalIdNumber(idNumber);
		storeBusiness.setLegalPerson(legalPerson);
		storeBusiness.setBusinessName(businessName);
		storeBusiness.setCompanyName(businessName);
		userMapper.fillStoreBusinessData(storeBusiness);

		int auditWaitNum = storeAuditService.getAuditCount(storeBusiness.getId(),
				StoreAuditStatusEnum.submit.getIntValue());
		if (auditWaitNum == 0) {
			long nowTime = System.currentTimeMillis();
			StoreAudit storeAudit = new StoreAudit();
			storeAudit.setCreateTime(nowTime);
			storeAudit.setUpdateTime(nowTime);
			storeAudit.setStoreId(storeBusiness.getId());
//			if(storeAuditService.chectStoreBusiness(storeBusiness)){
//				storeAudit.setStatus(2);
//			}else{
				storeAudit.setStatus(0);
//			}
			userMapper.saveStoreAudit(storeAudit);

			// 通知后台管理人员、有新用户注册需要审核
			// 邮箱通知 &微信通知
			SenderUtil.sendAudit(globalSettingService.getJsonObject(GlobalSettingName.STORE_AUDIT_SEND));

		}
		// if(firstFill){
		// storeLoginDelegator.loginUser(storeBusiness, response, ip,client);
		// }
		return jsonResponse.setSuccessful().setData(data);

	}

	/**
	 * 如果审核拒绝，清空门店的省市区信息
	 * @param storeId
	 */
	public void cleanStoreBusinessProvinceCityCounty(long storeId) {
		userMapper.cleanStoreBusinessProvinceCityCounty(storeId);
	}

	public int updateSupplierId(long supplierId, long storeId) {
		return userMapper.updateSupplierId(supplierId,storeId);
	}
	
	/**
	 * 增加门店头图
	 * @param id
	 * @param storeDisplayImages
	 */
	public void addStoreBusinessStoreDisplayImages(long storeId, String storeDisplayImages) {
		userMapper.addStoreBusinessStoreDisplayImages(storeId,storeDisplayImages);
	}

    /**
     * 新增一个用户 , 3.7.5新版本, 默认审核通过
     * @param: phone
     * @return: com.jiuyuan.entity.newentity.StoreBusiness
     * @auther: Charlie(唐静)
     * @date: 2018/5/17 11:14
     */
    public StoreBusiness add(StoreBusiness user) {
        userMapper.addStoreBusiness(user);
        return user;
    }


}