package com.jiuy.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.jiuyuan.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.define.JiuyMultipartFile;
import com.jiuy.core.business.facade.StoreBusinessFacade;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.member.BrandBusinessService;
import com.jiuy.core.service.member.StoreAuditService;
import com.jiuy.core.service.member.StoreBusinessService;
import com.jiuy.core.service.member.StoreWxaAdminService;
import com.jiuy.core.util.file.FileUtil;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.ThirdPartService;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.store.StoreWxa;
import com.jiuyuan.entity.store.StoreWxaCode;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.EncodeUtil;
import com.jiuyuan.util.WebUtil;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.spring.ControllerUtil;
import com.jiuyuan.web.help.JsonResponse;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;


@Controller
@RequestMapping("/wxa")
// @Login
public class WxaCodeController {

	private final String DEFAULT_BASEPATH_NAME = ThirdPartService.OSS_DEFAULT_BASEPATH_NAME;

	@Resource(name = "ossFileUtil")
	private FileUtil fileUtil;

	private static final Logger logger = LoggerFactory.getLogger(WxaCodeController.class);
	@Resource
	private StoreBusinessService storeBusinessService;
	@Resource
	private StoreWxaAdminService storeWxaAdminService;

	@Resource
	private StoreAuditService storeAuditService;

	@Resource
	private StoreBusinessDao storeBusinessDao;

	@Resource
	private BrandBusinessService brandbusinessService;

	@Resource
	private YunXinSmsService yunXinSmsService;

	@Resource
	private StoreBusinessFacade storeBusinessFacade;

	public static String weixinServiceUrl = AdminConstants.WEIXIN_SERVER_URL;

	public static String getGoToAuthUrl = "/code/getGoToAuthUrl";
	public static String gotoAuthCallback = "/code/gotoAuthCallback";
	public static String bind_tester = "/code/bind_tester";
	public static String uploadCode = "/code/uploadCode";
	public static String submitAudit = "/code/submitAudit";
	public static String getWxaCategory = "/code/getWxaCategory";
	public static String getLastAuditState = "/code/getLastAuditState";
	public static String releaseWxa = "/code/releaseWxa";
	public static String getWxaQrcodeUrl = "/code/getWxaQrcodeUrl";
	public static String getOnlineWxaQrcodeUrl = "/code/getOnlineWxaQrcodeUrl";

	public static String refresh_authorizer_token = "/code/refresh_authorizer_token";
	public static String changeVisitStatus = "/code/changeVisitStatus";
	public static String getPageConfig = "/code/getPageConfig";

	/**
	 * 获取小程序信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getWxaInfo")
	@ResponseBody
	public JsonResponse getWxaInfo(long storeId) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
//		logger.info(JSONObject.toJSONString(" 获取小程序信息，store:" + JSON.toJSONString(store) + ",storeId:" + storeId));
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		StoreWxaCode storeWxaCode = storeWxaAdminService.getStoreWxaCodeByStoreId(storeId);

		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("store", store);// 
		logger.info("获取小程序信息，store:" + JSON.toJSONString(store));
		retMap.put("wxa", storeWxa);// 小程序信息
		retMap.put("wxaCode", storeWxaCode);// 小程序代码信息
		//		logger.info(JSONObject.toJSONString(" 获取小程序信息，retMap:" + JSON.toJSONString(retMap) + ",storeId:" + storeId));
		return jsonResponse.setSuccessful().setData(retMap);
	}
	
	/**
	 * 设置是否开通小程序
	 * @return
	 */
	@RequestMapping(value = "/setOpenWxa")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse setOpenWxa(long storeId, int openWxaState, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		//添加 微信小程序开通时间以及 结束时间
		StoreBusiness user = storeBusinessDao.getByStoreId (storeId);
		//用户首次开通会员,初始化开通日期,否则不更新
		long openTime;
		long afterYearTime;
		if (user.getWxaCloseTime () == null || user.getWxaCloseTime () == 0) {
			openTime = System.currentTimeMillis ();
			afterYearTime = DateUtil.afterTime(1,0,0);
		}
		else {
			openTime = user.getWxaOpenTime ();
			afterYearTime = user.getWxaCloseTime ();
		}
		storeBusinessDao.setOpenWxa(storeId,openWxaState, openTime,afterYearTime);
		return jsonResponse.setSuccessful();
	}
	
	
	/**
	 * 设置小程序类型
	 * @return
	 */
	@RequestMapping(value = "/setWxaType")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse setWxaType(long storeId, int wxaType, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		storeBusinessDao.setWxaType(storeId,wxaType);
		
		return jsonResponse.setSuccessful();
	}
	
	
	

	/**
	 * 添加体验者 "/code/bind_tester?appId="+appId+"&testerId="+testerId;
	 * 
	 * @return
	 */
	@RequestMapping(value = "/bind_tester")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse bind_tester(long storeId, String testerId, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		map.put("testerId", testerId);
		Response<String> resp = Requests.get(weixinServiceUrl + bind_tester).params(map).text();
		String ret = resp.getBody();
		logger.info("添加体验者完成bind_tester,ret:" + ret);
		return jsonResponse.setSuccessful().setData(ret);
	}
	
	/**
	 * 设置商户号
	 * @Override
	 * 	public int setMchId(long storeId, String mchId);
	public int setPayKey(long storeId, String payKey);
	
	public int setMchId(long storeId,String mchId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("id", storeId);
		params.put("mchId", mchId);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.setMchId",params);
	}

	@Override
	public int setPayKey(long storeId,String payKey) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("id", storeId);
		params.put("payKey", payKey);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreBusinessDaoSqlImpl.setPayKey",params);
	}
	 * @return
	 */
	@RequestMapping(value = "/setMchId")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse setMchId(long storeId, String mchId, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		storeWxaAdminService.setMchId(storeId,mchId);
		
		return jsonResponse.setSuccessful();
	}
	
	/**
	 * 设置商户秘钥
	 * @return
	 */
	@RequestMapping(value = "/setPayKey")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse setPayKey(long storeId, String payKey, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		storeWxaAdminService.setPayKey(storeId,payKey);
		
		return jsonResponse.setSuccessful();
	}

	/**
	 * 设置商户秘钥
	 * @return
	 */
	@RequestMapping(value = "/setOnlineWxaVersion")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse setOnlineWxaVersion(long storeId, String onlineWxaVersion, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			storeBusinessService.setOnlineWxaVersion(storeId,onlineWxaVersion);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return jsonResponse.setSuccessful();
	}
	
	/**
	 * 上传代码 window.location.href =f
	 * "/code/uploadCode?appId="+appId+"&storeId="+storeId+"&templateId="+templateId+"&testerId="+testerId;
	 * 
	 * @return
	 */
	@RequestMapping(value = "/uploadCode")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse uploadCode(long storeId, String templateId, String version, String desc,
			HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		map.put("storeId", storeId);
		map.put("storeName", store.getBusinessName());
		map.put("templateId", templateId);
		map.put("version", version);
		map.put("desc", desc);

		Response<String> resp = Requests.get(weixinServiceUrl + uploadCode).params(map).text();
		String ret = resp.getBody();
		logger.info("上传代码完成uploadCode,ret:" + ret);

		// 添加小程序代码信息
		storeWxaAdminService.updateWxaCodeUploadCode(storeId, templateId, version, desc);

		return jsonResponse.setSuccessful().setData(ret);
	}

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<>();
		map.put("appId", "wx99c3b7a95e854fe3");
		map.put("storeName", "门店首页");
		map.put("storeId", "11878");
		map.put("templateId", "70");
		map.put("version", "3.8.5");
		map.put("desc", "店铺");
		Response<String> resp = Requests.get("http://47.96.153.80:30080/code/uploadCode").params(map).text();
		System.out.println(resp.getBody());
	}

	/**
	 * 查看二维码
	 * https://weixintest.yujiejie.com/code/getWxaQrcodeUrl?appId=wxc85ba29a5a96637b
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getWxaQrcodeUrl")
	@ResponseBody
	public JsonResponse getWxaQrcodeUrl(long storeId, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		String url = weixinServiceUrl + getWxaQrcodeUrl;
		logger.info("url:" + url);
		Response<String> resp = Requests.get(url).params(map).text();
		String ret = resp.getBody();
		logger.info("查看二维码submitAudit,ret:" + ret);
		return jsonResponse.setSuccessful().setData(ret);
	}

	/**
	 * 提交审核 window.location.href = "/code/submitAudit?appId="+appId;
	 * 
	 * @return
	 */
	@RequestMapping(value = "/submitAudit")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse submitAudit(long storeId, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		StoreWxaCode storeWxaCode = storeWxaAdminService.getStoreWxaCodeByStoreId(storeId);
		if (storeWxaCode == null) {
			return jsonResponse.setError("没有版本信息");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		map.put("testVersion", storeWxaCode.getTestVersion());
		Response<String> resp = Requests.get(weixinServiceUrl + submitAudit).params(map).text();
		String ret = resp.getBody();
		logger.info("提交审核完成submitAudit,ret:" + ret);

		// 添加小程序代码信息
		storeWxaAdminService.updateWxaCodeSubmitAudit(storeId);

		return jsonResponse.setSuccessful().setData(ret);
	}

	/**
	 * 修改小程序线上代码的可见状态（用于停用小程序）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/changeVisitStatus")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse changeVisitStatus(long storeId, int state, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		map.put("state", state);
		Response<String> resp = Requests.get(weixinServiceUrl + changeVisitStatus).params(map).text();
		String ret = resp.getBody();
		logger.info("修改小程序线上代码的可见状态change_visitstatus,ret:" + ret);
		return jsonResponse.setSuccessful().setData(ret);
	}

	/**
	 * 查看页面配置 window.location.href = "/code/getPageConfig?appId="+appId;
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getPageConfig")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse getPageConfig(long storeId, String status, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		Response<String> resp = Requests.get(weixinServiceUrl + getPageConfig).params(map).text();
		String ret = resp.getBody();
		logger.info("查看页面配置getPageConfig,ret:" + ret);
		return jsonResponse.setSuccessful().setData(ret);
	}

	//
	/**
	 * 刷新token window.location.href =
	 * "/code/refresh_authorizer_token?appId="+appId;
	 * 
	 * @return
	 */
	@RequestMapping(value = "/refresh_authorizer_token")
	@ResponseBody
	public JsonResponse refresh_authorizer_token(long storeId, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		Response<String> resp = Requests.get(weixinServiceUrl + refresh_authorizer_token).params(map).text();
		String ret = resp.getBody();
		logger.info("刷新token完成refresh_authorizer_token,ret:" + ret);
		return jsonResponse.setSuccessful().setData(ret);
	}

	/**
	 * 获取类目 window.location.href = "/code/getWxaCategory?appId="+appId;
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getWxaCategory")
	@ResponseBody
	public JsonResponse getWxaCategory(long storeId, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		Response<String> resp = Requests.get(weixinServiceUrl + getWxaCategory).params(map).text();
		String ret = resp.getBody();
		logger.info("获取类目完成getWxaCategory,ret:" + ret);
		return jsonResponse.setSuccessful().setData(ret);
	}

	/**
	 * 获取最后本表审核结果 window.location.href = "/code/getLastAuditState?appId="+appId;
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getLastAuditState")
	@ResponseBody
	public JsonResponse getLastAuditState(long storeId, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		Response<String> resp = Requests.get(weixinServiceUrl + getLastAuditState).params(map).text();
		String ret = resp.getBody();
		logger.info("获取最后本表审核结果getLastAuditState,ret:" + ret);
		return jsonResponse.setSuccessful().setData(ret);
	}

	//

	/**
	 * 发布小程序 window.location.href = "/code/releaseWxa?appId="+appId;
	 * 
	 * @return
	 */
	@RequestMapping(value = "/releaseWxa")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse releaseWxa(long storeId, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		Response<String> resp = Requests.get(weixinServiceUrl + releaseWxa).params(map).text();
		String ret = resp.getBody();
		logger.info("发布小程序releaseWxa,ret:" + ret);

		return jsonResponse.setSuccessful().setData(ret);
	}

	/**
	 * 查看正式二维码
	 * https://weixintest.yujiejie.com/code/getWxaQrcodeUrl?appId=wxc85ba29a5a96637b
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getOnlineWxaQrcodeUrl")
	@ResponseBody
	public JsonResponse getOnlineWxaQrcodeUrl(long storeId, HttpServletRequest request) throws IOException {
		JsonResponse jsonResponse = new JsonResponse();
		StoreBusiness store = storeBusinessDao.getByStoreId(storeId);
		StoreWxa storeWxa = storeWxaAdminService.getStoreWxaByStoreId(storeId);
		if (storeWxa == null) {
			return jsonResponse.setResultCode(ResultCode.WXA_AUTH_SERVER_ERROR);
		}
		//1、获取微信返回信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", storeWxa.getAppId());
		String url = weixinServiceUrl + getOnlineWxaQrcodeUrl;
		logger.info("微信工程获取发布版二维码接口url:" + url+",map："+JSONObject.toJSONString(map));
		Response<String> resp = Requests.post(url).params(map).text();
		String imgPath = resp.getBody();
		
	
    	// 上传阿里云存储图片
    	File tmpFile = new File(imgPath);
		MultipartFile file = new JiuyMultipartFile(tmpFile);
    	String onlineWxaQrcodeUrl = fileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file,null);
    	tmpFile.delete();
    	//修改发布版二维码字段
    	if(StringUtils.isNotEmpty(onlineWxaQrcodeUrl)){
    		logger.info("更改小程序发布版二维码，开始保存二维码URL，storeId:" + storeId + ",onlineWxaQrcodeUrl:" + onlineWxaQrcodeUrl);
    		int updRet = storeWxaAdminService.updateWxaOnlineWxaQrcodeUrl(storeId, onlineWxaQrcodeUrl);
    		logger.info("更改小程序发布版二维码，storeId:" + storeId + ",onlineWxaQrcodeUrl:" + onlineWxaQrcodeUrl + ",updRet:"+ updRet);
    	}
		
		return jsonResponse.setSuccessful().setData(onlineWxaQrcodeUrl);
	}

	

	

	/**
	 * 获取授权路径
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getGoToAuthUrl")
	@ResponseBody
	public JsonResponse getGoToAuthUrl(long storeId, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		if (StringUtils.isEmpty(weixinServiceUrl)) {
			logger.info("weixinServiceUrl为空，请检查配置！！！！！！");
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		logger.info("获取授权路径 weixinServiceUrl:" + weixinServiceUrl);
		logger.info("获取授权路径 getGoToAuthUrl:" + getGoToAuthUrl);
		Response<String> resp = Requests.get(weixinServiceUrl + getGoToAuthUrl).params(map).text();
		String authUrl = resp.getBody();
		logger.info("authUrl:" + authUrl);
		// 拼接上回调地址
		StringBuilder urlBuilder = new StringBuilder(authUrl);
		String baseUrl = WebUtil.getWebBaseUrl(request);
		String redirect_uri = baseUrl + "/wxa/gotoAuthCallback?storeId=" + storeId;
		logger.info("redirect_uri:" + redirect_uri);
		urlBuilder.append("&redirect_uri=").append(redirect_uri);
		logger.info("urlBuilder.toString():" + urlBuilder.toString());
		return jsonResponse.setSuccessful().setData(urlBuilder.toString());
	}

	/**
	 * 授权回调地址
	 * 
	 * @return
	 */
	@RequestMapping(value = "/gotoAuthCallback")
	// @ResponseBody
	public String gotoAuthCallback(String auth_code, String expires_in, String storeId, HttpServletRequest request) {
		logger.info("gotoAuthCallback   auth_code:" + auth_code + "，expires_in：" + expires_in + ",storeId:" + storeId);
		// TODO 调用微信工程进行绑定操作
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("auth_code", auth_code);
		map.put("expires_in", expires_in);
		map.put("storeId", storeId);
		logger.info("weixinServiceUrl:" + weixinServiceUrl);
		logger.info("gotoAuthCallback:" + gotoAuthCallback);
		Response<String> resp = Requests.get(weixinServiceUrl + gotoAuthCallback).params(map).text();
		String ret = resp.getBody();
		logger.info("绑定小程序返回数据为ret:" + ret);
		String tipMsg = "";
		if (StringUtils.isEmpty(ret)) {
			tipMsg = "调用授权接口返回数据为空，请尽快排查问题!";
		} else {

			// {"successful":true,"error":null,"code":0,"data":null,"html":null}
			JSONObject retJSON = JSONObject.parseObject(ret);
			int code = retJSON.getIntValue("code");
			String data = retJSON.getString("data");
			if (StringUtils.isEmpty(data)) {
				tipMsg = "绑定成功";
			} else {
				tipMsg = data;
				logger.info("绑定小程序返回数据为ret:" + ret);
			}
		}
		// 跳转提交
		String url = "/new/html/wxaManage.html?storeId=" + storeId + "&tipMsg=" + EncodeUtil.encodeURL(tipMsg);
		return ControllerUtil.redirect(url);
	}
	
	/**
	 * 取消小程序授权（解除小程序和门店的关联）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/cancelAuth")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse cancelAuth(long storeId, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		storeWxaAdminService.cancelAuth(storeId);
		return jsonResponse.setSuccessful();
	}

}
