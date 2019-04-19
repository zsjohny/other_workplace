package com.yujj.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.ActivityPlace;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserSharedClickRecord;
import com.jiuyuan.entity.UserSharedRecord;
import com.jiuyuan.entity.UserSharedRecordOrderNew;
import com.jiuyuan.entity.product.ProductShare;
import com.jiuyuan.util.AESUtil;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;
//import com.jiuyuan.web.interceptor.UriBuilder;
import com.yujj.business.service.globalsetting.bean.InvitationSetting;
import com.yujj.business.service.globalsetting.bean.JiucoinGlobalSetting;
import com.yujj.dao.mapper.UserSharedClickMapper;
import com.yujj.dao.mapper.UserSharedMapper;
import com.yujj.dao.mapper.UserSharedRecordOrderNewMapper;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.product.Product;
import com.yujj.util.uri.UriBuilder;
@Service
public class UserSharedService {
	public static final String SHARE_TOKEN_ENCRYPT_PASSWORD = "J&3jj#2~p=";
	
	private static final Logger logger = LoggerFactory.getLogger(UserSharedService.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private UserSharedMapper userSharedMapper;
    @Autowired
    private GlobalSettingService globalSettingService;
    @Autowired
    private UserCoinService userCoinService;

	@Autowired
	private UserSharedClickMapper userSharedClickMapper;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserSharedRecordOrderNewMapper userSharedRecordOrderNewMapper;
	
	@Autowired
	private IdService idService;
	
	@Autowired
	private GlobalSettingParseService globalSettingParseService;
	
	@Autowired
    private ActivityPlaceService activityPlaceService;
	
	public boolean checked(String token, UserDetail userDetail, ClientPlatform clientPlatform) {
		boolean isRegistershare = true;
		long targetUserId = decryptToken(token);
        if (targetUserId > 0) {
            String virtualDeviceId = userDetail.getVirtualDeviceId();
            UserCoinOperation operation = UserCoinOperation.SHARE_CALLBACK;
            if (userCoinService.getUserCoinLogCountByRelatedId(targetUserId, virtualDeviceId, operation) <= 0) {
            	JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
            	JSONObject promoteShareSetting = jiucoin_global_setting.getJSONObject("promoteShareSetting");
            	int maxCountEachClickCycle = promoteShareSetting.getInteger("maxCountEachClickCycle");
            	int clickCycle = promoteShareSetting.getInteger("clickCycle");
            	
                long time = System.currentTimeMillis();
                long startTime = DateUtil.getTodayEnd() - clickCycle * DateUtils.MILLIS_PER_DAY;
                long endTime = DateUtil.getTodayEnd();
                
                
                int count = userCoinService.getUserCoinLogCount(targetUserId, startTime, endTime, operation);
                //每天分享不能超过N次
                if (count >= maxCountEachClickCycle) {  
                	isRegistershare = false;
//                	return "";
                }
                
                int eachClickObtain = promoteShareSetting.getInteger("eachClickObtain");
                userCoinService.updateUserCoin(targetUserId, 0, eachClickObtain, "click", time, operation, null, clientPlatform.getVersion());
                
            }
        }
		return isRegistershare;
	}
	
	/**
	 * 获得文章分享信息
	 * @param userId
	 * @param articalId
	 * @param clientPlatform
	 * @return
	 */
	public Map<String, Object> getArticalUserShareInfo(long userId, long articalId, ClientPlatform clientPlatform) {
		Map<String, Object> data = new HashMap<>();
    	data.put("url", buildUrlAddToken(userId, "/static/app/article.html?id=" + articalId));
    	data.put("grant_coin", getShareCoin(userId, clientPlatform));
		return data;
	}
	/**
	 * 获得商品分享信息
	 * @param userDetail
	 * @param productId
	 * @param clientPlatform
	 * @return  map：userSharedRecordId（预生成分享记录ID）,title（标题）,imageUrl（图标地址）,description（描述）,url（链接地址）,grant_coin（分享得到玖币数量）
	 */
	public Map<String, String> getProductUserShareInfo(long userId, long productId, ClientPlatform clientPlatform) {
		String userSharedRecordId = String.valueOf(idService.getLastLogId(1));
		Product product = productService.getProductById(productId);
		Map<String, String> data = new HashMap<String, String>();
		data.put("userSharedRecordId", userSharedRecordId);// 分享标题  
//        data.put("title", product.getName());
//        String image = "";
//        String[] detailImageArray = product.getDetailImageArray();
//        if (detailImageArray.length > 0) {
//            image = detailImageArray[0];
//        }
//        data.put("imageUrl", image);
//        String  description = "下载俞姐姐客户端，品牌女装全场1折，靓衣20元抢不停，注册更送价值千元玖币！";
//        
//        String url = "/static/app/ProductDetailsUser.html" + "?product_id=" + productId;
//    	if (userDetail.getUser() != null) {
//    		url += "&yjj_number=" + userService.getUser(userDetail.getUserId()).getyJJNumber();
//		}
        
//        JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.PRODUCT_SHARE);
//		for(Object obj : jsonArrayConfirm) {
//			
//			if(((JSONObject)obj).get("description") != null){
//				description = (String) ((JSONObject)obj).get("description");
//			}
//		    if(((JSONObject)obj).get("url") != null){
//		    	url = (String) ((JSONObject)obj).get("url");
//			}
//		}
//		data.put("description", description);
//     	String url = "/static/app/ProductDetailsUser.html" + "?product_id=" + productId;
        String url = "/static/wap/weixin_ProductDetailsUser.html" + "?product_id=" + productId+"&userSharedRecordId="+userSharedRecordId;
        
		ProductShare productShare = productService.getProductShareByProId(productId);
		if(productShare != null){
			if(productShare.getShareTitle() != null && productShare.getShareTitle().trim().length() > 0){
				data.put("title", productShare.getShareTitle());	
			}
			if(productShare.getShareImg() != null && productShare.getShareImg().trim().length() > 0){
				data.put("imageUrl", productShare.getShareImg());	
			}
			if(productShare.getShareDesc() != null && productShare.getShareDesc().trim().length() > 0){
				data.put("description", productShare.getShareDesc());	
			}
		}else{
			data.put("title", product.getName());		
			data.put("description", product.getName());	
			String image = product.getFirstDetailImage();
	        data.put("imageUrl", image);
		}
		data.put("url", buildUrlAddToken(userId, url.trim()));
		data.put("grant_coin", String.valueOf(getShareCoin(userId, clientPlatform)));//分享得玖币

        JiucoinGlobalSetting jiucoinGlobalSetting = globalSettingParseService.getJiucoin_global_setting();
        InvitationSetting invitationSetting  = jiucoinGlobalSetting.getInvitationSetting();
        
        //分享文案
		StringBuilder ruleDescription = new StringBuilder();
		ruleDescription.append("1、每邀请1个新用户注册成功可获得  <span style=\"color:red;\">");
		ruleDescription.append(invitationSetting.getEachObtain());
		ruleDescription.append("</span>个玖币；<br>");
		ruleDescription.append("2、被邀请人每次成功下单，可获得购物金额  <span style=\"color:red;\">");
		ruleDescription.append(invitationSetting.getReturnPercentage()+"%");
		ruleDescription.append("</span> 对应的玖币。 <br>");
		ruleDescription.append("3、您赚取的玖币可以在玖币专区抵钱用，还有机会全额兑换礼品哦");
		data.put("ruleDescription", ruleDescription.toString());
		//data.put("ruleDescription", ruleDescription.toString());
		data.put("ruleLoginTitle", "分享有礼！");
		data.put("ruleNoLoginTitle", "登录后，分享有礼！");
		
		return data;
	}
	/**
	 * 获得专场分享信息
	 * @param userDetail
	 * @param productId
	 * @param clientPlatform
	 * @return  map：userSharedRecordId（预生成分享记录ID）,title（标题）,imageUrl（图标地址）,description（描述）,url（链接地址）,grant_coin（分享得到玖币数量）
	 */
	public Map<String, String> getActivityPlaceUserShareInfo(long userId, long activityPlaceId, ClientPlatform clientPlatform) {
		String userSharedRecordId = String.valueOf(idService.getLastLogId(1));
		ActivityPlace activityPlace = activityPlaceService.getById(activityPlaceId);
		Map<String, String> data = new HashMap<String, String>();
		data.put("userSharedRecordId", userSharedRecordId);
        String url = "/static/app/new/html/activity.html" + "?id=" + activityPlaceId+"&userSharedRecordId="+userSharedRecordId;;
        if(activityPlace.getDescription() != null){
			data.put("description", activityPlace.getDescription());	
		}
		if(activityPlace.getName() != null){
			data.put("name", activityPlace.getName());	
		}
		data.put("url", buildUrlAddToken(userId, url.trim()));
		data.put("grant_coin", String.valueOf(getShareCoin(userId, clientPlatform)));//分享得玖币
        
		return data;
	}
	/**
	 * 分享得玖币
	 * @param userId
	 * @param clientPlatform
	 * @return
	 */
	private int getShareCoin(long userId, ClientPlatform clientPlatform) {
		if(userId == 0){
			 return 0;
		}
		UserCoinOperation operation = UserCoinOperation.SHARE_GRANT;
		
		JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
     	JSONObject promoteShareSetting = jiucoin_global_setting.getJSONObject("promoteShareSetting");
     	int maxCountEachShareCycle = promoteShareSetting.getInteger("maxCountEachShareCycle");
     	int shareCycle = promoteShareSetting.getInteger("shareCycle");
     	
     	long time = System.currentTimeMillis();
     	long startTime = DateUtil.getTodayEnd() - shareCycle * DateUtils.MILLIS_PER_DAY;
     	long endTime = DateUtil.getTodayEnd();

        int count = userCoinService.getUserCoinLogCount(userId, startTime, endTime, operation);
        
        if (count >= maxCountEachShareCycle) {
            return 0;
        }
        int eachShareObtain = promoteShareSetting.getInteger("eachShareObtain");
        userCoinService.updateUserCoin(userId, 0, eachShareObtain, "share", time, operation, null, clientPlatform.getVersion());
        
        return eachShareObtain;
	}
	
	/**
	 * 将连接加上用户ID编码后的token
	 * @param userId
	 * @param url
	 * @return
	 */
	public String buildUrlAddToken(long userId, String url) {
        UriBuilder builder = new UriBuilder(Constants.SERVER_URL + url);
        if(userId != 0){
        	String token = encryptToken(userId);
        	builder.set("token", token);
		}
        return builder.toUri();
    }


	
	
	/**
	 * 添加用户分享记录
	 * @param userSharedRecord
	 * @return
	 */
	public JsonResponse addUserSharedRecord(UserSharedRecord userSharedRecord) {
		JsonResponse jsonResponse = new JsonResponse();
		userSharedRecord.setStatus(0);
		userSharedRecord.setCreateTime(System.currentTimeMillis());
		userSharedRecord.setUpdateTime(userSharedRecord.getCreateTime());
		int record = userSharedMapper.addUserSharedRecord(userSharedRecord);
		//TODO
		/*
		if(record==1){
			return jsonResponse.setSuccessful().setData(record);
		}else{
			return jsonResponse.setResultCode(ResultCode.USER_SHARED_RECORD_ERROR).setError("分享失败，请重试一次");
		}
		*/
		return jsonResponse.setSuccessful();
	}
	
	/**
	 * 分享注册添加玖币
	 * @param userSharedRecordId
	 * @param type
	 * @param userDetail
	 * @param totalCash
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addRegisterJiuCoin(long userSharedRecordId,long userId,ClientPlatform clientPlatform) {
		
		if(userSharedRecordId == 0){
			logger.info("addRegisterJiuCoin分享注册添加玖币分享记录ID不能为空，userSharedRecordId[{}],userId[{}]",userSharedRecordId,userId);
			return ;
		}
		if(userId == 0){
			logger.info("addRegisterJiuCoin分享注册添加玖币分享记录userId不能为空，userSharedRecordId[{}],userId[{}]",userSharedRecordId,userId);
			return ;
		}
		
		JsonResponse jsonResponse = new JsonResponse();
		
		logger.info("分享注册添加玖币开始，userSharedRecordId[{}],userId[{}]",userSharedRecordId,userId);
		
		UserSharedClickRecord userSharedClickRecord = new UserSharedClickRecord();
		UserSharedRecord userSharedRecord = userSharedMapper.getRecordBySharedId(userSharedRecordId);
		
		logger.info("分享注册",userSharedRecord.toString());
		//System.out.println(userSharedRecord.toString());
		userSharedClickRecord.setSharedUserId(userSharedRecord.getUserId());
		userSharedClickRecord.setSharedId(userSharedRecordId);
		userSharedClickRecord.setCreateTime(System.currentTimeMillis());
		userSharedClickRecord.setUserId(userId);
		userSharedClickRecord.setStatus(0);
		//System.out.println(userSharedClickRecord);
		userSharedClickRecord.setType(1);
		logger.info("分享注册1");
		long sharedUserId = userSharedRecord.getUserId(); 
		//分享注册
		//获取规则
		JiucoinGlobalSetting jiucoinGlobalSetting = globalSettingParseService.getJiucoin_global_setting();
		InvitationSetting invitationSetting = jiucoinGlobalSetting.getInvitationSetting();
			//userSharedClickRecord.setJiuCoin(Integer.parseInt(invitationSetting.getEachObtain()));
			//限制天数
			int checkDay = Integer.parseInt(invitationSetting.getInvitationCycle());
			//限制次数
			int checkCount = Integer.parseInt(invitationSetting.getMaxCountCycle());
			//checkDay天内最多checkCount次
			long time = System.currentTimeMillis()-1000*60*60*24*checkDay;
			int count = userSharedClickMapper.getJiuCoinCount(sharedUserId,time,1);
			if(count<checkCount){
				userSharedClickRecord.setJiuCoin(Integer.parseInt(invitationSetting.getEachObtain()));
			}
			/*
			else{
				//分享注册添加玖币次数超过checkDay天checkCount次
				return jsonResponse.setError(ResultCode.SHARED_GET_JIUCOINS_OVER_COUNT.getDesc());
			}
			*/
			logger.info("分享注册2");
		int record = userSharedClickMapper.addUserSharedClickRecord(userSharedClickRecord);
		if(userSharedClickRecord.getJiuCoin()>0){
			logger.info("分享注册",userSharedRecord.toString());
			userCoinService.updateUserCoin(sharedUserId, 0, userSharedClickRecord.getJiuCoin(), userSharedClickRecord.getId()+"", userSharedClickRecord.getCreateTime(),
					UserCoinOperation.SHARED_REGISTER, null, clientPlatform.getVersion());
		}
		//System.out.println(record);
		logger.info("分享注册",userSharedRecord.toString());
		logger.info("分享注册",userSharedClickRecord.toString());
	}
	
	/**
	 * 分享下单送玖币
	 * @param orderNo
	 * @param userDetail
	 * @param totalCash
	 * @return
	 */
	public void addOrderJiuCoin(long orderNo) {
		OrderNew orderNew = orderService.getUserOrderNewByNo(String.valueOf(orderNo));
		long userId =orderNew.getUserId();
		
		UserSharedRecordOrderNew userSharedRecordOrderNew = userSharedRecordOrderNewMapper.getUserSharedRecordOrderNew(orderNo);
		if(userSharedRecordOrderNew==null){
			return;
		}
		long userSharedRecordId = userSharedRecordOrderNew.getUserSharedRecordId();
		if(userSharedRecordId==0){
			return;
		}
		logger.info("addOrderJiuCoin 分享下单送玖币开始，orderNo[{}],userSharedRecordId[{}],userId[{}]",orderNo,userSharedRecordId,userId);
		if(userSharedRecordId == 0){
			logger.info("addOrderJiuCoin 分享下单送玖币分享记录ID为空无需发放下单玖币收益，orderNo[{}],userSharedRecordId[{}],userId[{}]",orderNo,userSharedRecordId,userId);
			return;
		}
		
		int type = 2;
		JsonResponse jsonResponse = new JsonResponse();
		
		//金额
		double totalCash  = orderNew.getTotalPay() +  orderNew.getTotalExpressMoney();
		UserSharedRecord userSharedRecord = userSharedMapper.getRecordBySharedId(userSharedRecordId);
		
		UserSharedClickRecord userSharedClickRecord = new UserSharedClickRecord();
		userSharedClickRecord.setSharedUserId(userSharedRecord.getUserId());
		userSharedClickRecord.setSharedId(userSharedRecordId);
		userSharedClickRecord.setCreateTime(System.currentTimeMillis());
		userSharedClickRecord.setStatus(0);
		userSharedClickRecord.setType(type);
		userSharedClickRecord.setUserId(userId);
		userSharedClickRecord.setRelatedId(orderNo);
		//System.out.println(userSharedClickRecord);
		long sharedUserId = userSharedRecord.getUserId(); 
		
		//分享下单
		//获取规则
		JiucoinGlobalSetting jiucoinGlobalSetting = globalSettingParseService.getJiucoin_global_setting();
		InvitationSetting invitationSetting = jiucoinGlobalSetting.getInvitationSetting();
		//限制天数
		int checkDay = Integer.parseInt(invitationSetting.getReturnCycle());
		//注册下单可获得的最大玖币数
		int maxJiuCoins = Integer.parseInt(invitationSetting.getMaxJiuCoinReturnCycle());
		//送的玖币与下单金额的百分比
		int percentage = Integer.parseInt(invitationSetting.getReturnPercentage());
			//1天内获得玖币不能超过1000
			long time = System.currentTimeMillis()-1000*60*60*24*checkDay;
			List<UserSharedClickRecord> userSharedClickRecordList = userSharedClickMapper.getUserSharedClickRecordListBeforeTime(sharedUserId,time,type);
			//1天内获得的总的玖币数
			int jiuCoins = 0;
			for (UserSharedClickRecord userSharedClickRecord2 : userSharedClickRecordList) {
				jiuCoins += userSharedClickRecord2.getJiuCoin();
			}
			int jiuCoin = (int) (totalCash*percentage/100);
			if(jiuCoins<maxJiuCoins){
				if((jiuCoins+jiuCoin)<=maxJiuCoins){
					userSharedClickRecord.setJiuCoin(jiuCoin);
				}else if((jiuCoins+jiuCoin)>1000){
					userSharedClickRecord.setJiuCoin(1000-jiuCoins);
				}
			}
		
		int record = userSharedClickMapper.addUserSharedClickRecord(userSharedClickRecord);
		if(userSharedClickRecord.getJiuCoin()>0){
			userCoinService.updateUserCoin(sharedUserId, 0, userSharedClickRecord.getJiuCoin(), userSharedClickRecord.getId()+"", userSharedClickRecord.getCreateTime(),
					UserCoinOperation.SHARED_ORDER, null, "");
		}
		//System.out.println(record);
	
	}
	
	/**
	 * 获取当前用户的分享列表(前台只需要用到商品id,商品图片,商品名称,商品价格,商品市场价,商品的玖币价格,分享对应商品获得的玖币)
	 * @param userId
	 * @return
	 */
	public JsonResponse mySharedList(long userId) {
		List<String> productStringList = new ArrayList<String>();
		JsonResponse jsonResponse = new JsonResponse();
		List<Map<Object,Object>> productList = new ArrayList<Map<Object,Object>>();
		List<Long> productIdList = new ArrayList<Long>();
		Map<String,Object> data = new HashMap<String,Object>();
		List<UserSharedRecord> userSharedRecordList = userSharedMapper.getUserSharedList(userId);
		for (UserSharedRecord userSharedRecord : userSharedRecordList) {
			Long productId = userSharedRecord.getRelatedId();
			int index = productIdList.indexOf(productId);
			int jiuCoin = 0;
			Map<Object,Object> productMap = null;
			if(index==-1){
				Product product = productService.getProductById(productId);
				
				//组装productsMap
				productMap = new HashMap<Object,Object>();
				//分享记录Id
				productMap.put("userSharedRecordId", userSharedRecord.getId());
				//分享的商品Id
				productMap.put("productId", product.getId());
				//商品图片
				productMap.put("image", product.getFirstDetailImage());
				//商品名称
				productMap.put("productName", product.getName());
				//商品价格
				productMap.put("productPrice", product.getCurrenCash());
				//商品市场价
				productMap.put("productMarketPrice", product.getMarketPrice());
				//商品的玖币价格
				productMap.put("currentJiuCoin", product.getCurrentJiuCoin());
				productList.add(productMap);
				productIdList.add(productId);
			}else{
				productMap = productList.get(index);
				//productList.remove(index);
				jiuCoin = (int) productMap.get("jiuCoin");
			}
			List<UserSharedClickRecord> userSharedClickRecordList = userSharedClickMapper.getUserSharedClickRecordListBySharedId(userSharedRecord.getId());
			for (UserSharedClickRecord userSharedClickRecord : userSharedClickRecordList) {
				jiuCoin += userSharedClickRecord.getJiuCoin();
			}
			//分享对应商品获得的玖币
			productMap.put("jiuCoin", jiuCoin);
		}
		data.put("productList", productList);
		return jsonResponse.setSuccessful().setData(data);
	}

	
	private String encryptToken(long userId) {
		return AESUtil.encrypt(String.valueOf(userId), "UTF-8", SHARE_TOKEN_ENCRYPT_PASSWORD);
	}
	
	private long decryptToken(String token ) {
		long targetUserId = 0;
		if (StringUtils.isNotBlank(token)) {
            targetUserId = NumberUtils.toLong(AESUtil.decrypt(token, "UTF-8", SHARE_TOKEN_ENCRYPT_PASSWORD), 0);
        }
		return targetUserId;
	}

}
