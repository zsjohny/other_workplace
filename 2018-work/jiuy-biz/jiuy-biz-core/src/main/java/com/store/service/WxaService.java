package com.store.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.store.StoreWxa;
import com.store.dao.mapper.ThirdAuthLogMapper;
import com.store.entity.ThirdAuthLog;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import java.util.Calendar;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21
 *  extends ServiceImpl<MessageMapper); Message>
 */
@Service
public class WxaService {
	private static final Log logger = LogFactory.get();
	
	@Autowired
	private ThirdAuthLogMapper thirdAuthLogMapper;
	 
	@Autowired
	private StoreUserService storeUserService;
	
	@Autowired
	private StoreWxaService storeWxaService;
	 

	 /**
	  * 小程序授权通知
	  * @param createTime
	  * @param infoType
	  * @param authorizerAppid
	  * @param authorizationCode
	  * @param authorizationCodeExpiredTime
	  */
	public void wxaAuthNotification(String thirdAppId, String createTime, String infoType, String authorizerAppid,
			String authorizationCode, String authorizationCodeExpiredTime) {
		
		if("authorized".equals(infoType)){//授权成功通知
			auth(authorizerAppid);
		}else if("unauthorized".equals(infoType)){//取消授权
			cancelAuth(authorizerAppid);
		}else if("updateauthorized".equals(infoType)){//更新授权
			logger.info("更新授权信息，authorizerAppid："+authorizerAppid);
		}else{
			logger.info("无法识别的infoType，infoType："+infoType);
		}
		
		
		//2、添加授权记录
		addThirdAuthLog(thirdAppId, createTime, infoType, authorizerAppid, authorizationCode, authorizationCodeExpiredTime);
		
	}

	
	
	/**
	 * 取消小程序授权商家
	 * @param authorizerAppid
	 */
	private void cancelAuth(String authorizerAppid) {
		// TODO 待完善 ,待测试
		//1、清空商家appid字段
		logger.info("取消授权信息，authorizerAppid："+authorizerAppid);
		StoreBusiness storeBusiness = storeUserService.getStoreBusinessByWxaAppId(authorizerAppid);
		storeUserService.cancelAuth(storeBusiness.getId());
		//2、？？？
		
		//3、？？？
	}
	/**
	 * 小程序授权商家
	 * @param authorizerAppid
	 */
	private void auth(String authorizerAppid) {
		// TODO 绑定商家
		// 1、商家添加小程序信息
				logger.info("商家添加小程序信息,待实现");
				logger.info("待确定怎么解决绑定哪个商家的问题");
				//方案1采用前端回调进行添加绑定信息
				//方案2开始绑定之前将商家ID和appid存入缓存，绑定授权通知时冲缓存中获得商家和appId的对应关系
//				StoreBusiness storeBusiness = storeUserService.getStoreBusinessByAppId(thirdAppId);
//				if(storeBusiness == null){
//					logger.info("小程序授权通知失败，商家信息storeBusiness为空，请排查问题，appId："+thirdAppId);
//					return;
//				}
//				storeBusiness.setWxaAppId(authorizerAppid);
	}
	
	/**
	 * 添加授权记录
	 * @param thirdAppId
	 * @param createTime
	 * @param infoType
	 * @param authorizerAppid
	 * @param authorizationCode
	 * @param authorizationCodeExpiredTime
	 */
	private void addThirdAuthLog(String thirdAppId, String createTime, String infoType, String authorizerAppid,
			String authorizationCode, String authorizationCodeExpiredTime) {
		ThirdAuthLog thirdAuthLog = new ThirdAuthLog(); 
		thirdAuthLog.setAdminId(null);
		thirdAuthLog.setThirdAppId(thirdAppId);
		thirdAuthLog.setInfoType(infoType);
		thirdAuthLog.setAuthorizerAppId(authorizerAppid);
		thirdAuthLog.setAuthorizationCode(authorizationCode);
		thirdAuthLog.setAuthorizationCodeExpiredTime(Integer.valueOf(authorizationCodeExpiredTime));
		thirdAuthLog.setAuthorizerCreateTime(Long.parseLong(createTime));
		thirdAuthLog.setCreateTime(DateUtil.current(false));
		thirdAuthLogMapper.insert(thirdAuthLog);
		logger.info("添加授权记录成功,thirdAuthLog："+thirdAuthLog);
	}


	/**
	 * 
	 *
	 * 返回结果示例
		{
		    "authorizer_info": {
		        "nick_name": "微信SDK Demo Special",//授权方昵称
		        "head_img": "http://wx.qlogo.cn/mmopen/GPy",//授权方头像
		        "service_type_info": {//授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
		            "id": 2
		        },
		        "verify_type_info": {//授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
		            "id": 0
		        },
		        "user_name": "gh_eb5e3a772040",//授权方公众号的原始ID
		        "principal_name": "腾讯计算机系统有限公司",//公众号的主体名称
		        "alias": "paytest01",//授权方公众号所设置的微信号，可能为空
		        "business_info": {"open_store": 0, "open_scan": 0, "open_pay": 0, "open_card": 0,"open_shake": 0
		        },//用以了解以下功能的开通状况（0代表未开通，1代表已开通）：open_store:是否开通微信门店功能、 open_scan:是否开通微信扫商品功能、 open_pay:是否开通微信支付功能、 open_card:是否开通微信卡券功能、open_shake:是否开通微信摇一摇功能
		        "qrcode_url": "URL", //二维码图，片的URL开发者最好自行也进行保存
		    },
		    "authorization_info": {//授权信息
		        "appid": "wxf8b4f85f3a794e77",//授权方appid
		        "func_info": [//公众号授权给开发者的权限集列表，ID为1到15时分别代表：1消息管理权限、2用户管理权限、3帐号服务权限、4网页服务权限、5微信小店权限、6微信多客服权限、7群发与通知权限、8微信卡券权限、9微信扫一扫权限、10微信连WIFI权限、11素材管理权限、12微信摇周边权限、13微信门店权限、14微信支付权限、15自定义菜单权限
		            {"funcscope_category": {"id": 1 }},{ "funcscope_category": {"id": 2}}, { "funcscope_category": { "id": 3}}
		        ]
		    }
		}
		
	 */
	public String wxaAuth(String storeId,String authorizer_appid,String authorizer_info_json) {
		//1、校验appId和storeId是否可用
		StoreWxa storeWxaByStoreId =storeWxaService.getStoreWxaByStoreId(storeId);
		if(storeWxaByStoreId != null){
			logger.info("商家已经绑定了小程序，不能再次绑定，请排查原因,storeId:"+storeId);
			return "商家已经绑定了小程序，不能再次绑定，请排查原因,storeId:"+storeId;
		}
				
		StoreWxa storeWxaByAppId =storeWxaService.getStoreWxaByAppId(authorizer_appid);
		if(storeWxaByAppId != null){
			logger.info("appId已经绑定了其他商家，不能再次绑定");
			return "appId已经绑定了其他商家，不能再次绑定";
		}
		
		//1、授权方基本信息JSON解析数据
		JSONObject jsonObj = JSONObject.parseObject(authorizer_info_json); 
		JSONObject authorizer_info = jsonObj.getJSONObject("authorizer_info");
		String nick_name = null;
		String head_img = null;;
		String user_name = null;;
		String principal_name = null;;
		String idc = null;;
		String signature = null;
		String alias = null;;
		String qrcode_url = null;;
		if(authorizer_info != null){
			nick_name = authorizer_info.getString("nick_name");
			head_img = authorizer_info.getString("head_img");
			user_name = authorizer_info.getString("user_name");
			principal_name = authorizer_info.getString("principal_name");
			alias = authorizer_info.getString("alias");
			qrcode_url = authorizer_info.getString("qrcode_url");
			idc = authorizer_info.getString("idc");
			signature = authorizer_info.getString("signature");
		}

		
		//2、添加小程序基本信息记录
		storeWxaService.add(storeId,authorizer_appid,nick_name,head_img,user_name,principal_name,idc,signature,alias,qrcode_url,authorizer_info_json);

		//3、绑定小程序到商家字段中
		StoreBusiness storeBusiness = storeUserService.getStoreBusinessById(Long.parseLong(storeId));

		Long wxaOpenTime = null;
		Long wxaCloseTime = null;
		if (storeBusiness.getWxaCloseTime ()==0) {
			logger.info ("开通小程序,并初始化小程序开通,关闭时间");
			Calendar current = Calendar.getInstance ();
			wxaOpenTime = current.getTimeInMillis ();
			current.add (Calendar.YEAR, 1);
			wxaCloseTime = current.getTimeInMillis ();
		}

		logger.info ("小程序关闭时间 wxaCloseTime={}", wxaCloseTime);
		String weiXinNum = user_name;
		String wxaAppId = authorizer_appid;
		int ret = storeUserService.wxaAuth(storeId,wxaAppId,weiXinNum, wxaOpenTime, wxaCloseTime);
		logger.info("storeUserService.wxaAuth 返回的结果："+ret);
		return "绑定成功";
	}


}