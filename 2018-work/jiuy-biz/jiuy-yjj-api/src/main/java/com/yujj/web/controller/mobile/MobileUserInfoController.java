package com.yujj.web.controller.mobile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.FavoriteType;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderCouponStatus;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.UserBankCardSign;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.entity.account.UserInvite;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.CommentFacade;
import com.yujj.business.facade.UserInviteFacade;
import com.yujj.business.service.FavoriteService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.JiuCoinExchangeLogService;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.UserBankCardSignService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserService;
import com.yujj.business.service.VisitService;
import com.yujj.dao.mapper.OrderItemMapper;
import com.yujj.dao.mapper.StoreBusinessMapper;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.util.file.FileUtil;
import com.yujj.util.uri.UriBuilder;

@Controller
@Login
@RequestMapping("/mobile/user")
public class MobileUserInfoController {

    private static final Logger logger = LoggerFactory.getLogger(MobileUserInfoController.class);
    
    private static final List<OrderStatus> STATUS = CollectionUtil.createList(OrderStatus.UNPAID, OrderStatus.UNCHECK,
        OrderStatus.PAID, OrderStatus.DELIVER, OrderStatus.REFUNDING);
    
	private final String DEFAULT_BASEPATH_NAME = Client.OSS_DEFAULT_BASEPATH_NAME;

    @Autowired
    private CommentFacade commentFacade;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserBankCardSignService userBankCardSignService;
    
    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private VisitService visitService;
//    @Autowired
//    private FinanceLogMapper financeLogMapper;
    @Autowired
    private StoreBusinessMapper storeBusinessMapper; 
    
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private UserInviteFacade userInviteFacade;
    
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private JiuCoinExchangeLogService jiuCoinExchangeLogService;

	@Resource(name = "ossFileUtil")
    private FileUtil fileUtil;
	

	/**
	 * 获取用户基本信息
	 * @param userDetail
	 * @return
	 */
	@RequestMapping(value = "/getBaseUser")
	@ResponseBody
	public JsonResponse getBaseUser(UserDetail userDetail) {
		  	User user = userDetail.getUser();
	        long userId = user.getUserId();
	        Map<String, Object> data = new HashMap<String, Object>();
	        data.put("user", user);
	    	JsonResponse jsonResponse = new JsonResponse();
	    	return jsonResponse.setSuccessful().setData(data);
	}
    
    @RequestMapping
    @ResponseBody
    public JsonResponse userInfo(UserDetail userDetail) {
        User user = userDetail.getUser();
        long userId = user.getUserId();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", user);

        UserCoin userCoin = userCoinService.getUserCoin(userId);
        data.put("userCoins", userCoin == null ? 0 : userCoin.getAllCoins());
        data.put("favoriteCount", favoriteService.getFavoritesCount(userId, FavoriteType.PRODUCT));
        data.put("visitCount", visitService.getVisitListCount(userId));
        
        int orderCount = orderService.getUserNewOrderCount(userId, null);//这里订单为空表示获取所有订单状态的订单
//        int orderCount = orderService.getUserNewOrderCountByOrderStatus(userId, null);//这里订单为空表示获取所有订单状态的订单
        
        data.put("orderCount", orderCount);
      
        
      //删除旧表改字段已经废弃,如果APP中有使用请APP注意修改
        List<Map<String, Object>> orderStatusList = new ArrayList<Map<String, Object>>();
      data.put("orderStatusList", orderStatusList);//loadOrderStatus(userId)
        
       
        
        data.put("orderNewStatusList", loadOrderNewStatus(userId));
        
        Set<Integer> orderStatuses = new HashSet<Integer>();
    	
    	orderStatuses.add(OrderStatus.DELIVER.getIntValue());
    	orderStatuses.add(OrderStatus.SUCCESS.getIntValue());
		//取某状态订单，不包含母订单
		int totalCount = orderService.getUserNewOrdersCountAfterSale(userId, orderStatuses, "");
		
		data.put("afterSaleTotalCount", totalCount);
        
        data.put("userOrderCountAll", orderService.getUserOrderCountAll(userId));

        data.put("shareContent", shareContent(userId));
        
        data.put("signContent", signContent(userId));

        data.put("userInviteInfo", userInviteInfo(userId));

        data.put("helpInfo", helpInfo(userId));
        
        data.put("aboutCoinUrl", new UriBuilder(Constants.SERVER_URL_HTTPS + "/static/app/gba.html").toUri());
        
        //未读玖币消费记录条数
        int newCoinLogNum = userCoinService.countNewReadStatus(userDetail);
        
        data.put("newCoinLogNum", newCoinLogNum);
        
        data.put("phoneNumber", userService.getUserPhoneNumber(userDetail));
        
        data.put("bindWeixin", userDetail.getUser().getBindWeixin());
        
        int commentCount = commentFacade.getUserCommentCount(userId);
        
        data.put("commentCount", commentCount);
        
        //俞姐姐号
        data.put("yJJNumber", user.getyJJNumber());
        
        data.put("weiXinUnbindTips", "解除绑定后，您将不能使用该微信账号登录俞姐姐号为" + user.getyJJNumber() + "的账户,确定要解除绑定吗?");
        //一网通优惠提示
        data.put("cmbPayTips", "（满30元优惠活动）");
        
        int couponNum = orderService.getUserCouponCount(userDetail.getUserId(), OrderCouponStatus.UNUSED);
        
        data.put("couponNum", couponNum);
        
        data.put("backImgUrl", "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/center/centerbg1021.png");
        
        data.put("giftCenter", "/static/app/giftcoupon.html");
        
        data.put("shareGet", "分享得玖币");
        
        data.put("getCoupon", "获得优惠券");
        
        if(user.getUserPasswordLength() > 0){        	
        	data.put("noPasswordFlag", "false");
        }else{
        	data.put("noPasswordFlag", "true");
        }
        
     //   String deductStr = (String) globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_DEDUCTION_SETTING).get("activityText");
        

//        orderItemMapper.updateItemsCommssion(496 ,0.2,System.currentTimeMillis()); //更新item表commission
////		commission = 0.01 * storeBusiness.getCommissionPercentage() * orderNew.getTotalPay();
//		double commission = orderItemMapper.getItemsCommssionTotal(496);
       // List<Category> categorieListAll = categoryService.getCategories();
//        int afterSaleMinutes = 111;
//        JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
//        try {
//			
//        	for(Object obj : jsonArrayConfirm) {
//        		
//        		if(((JSONObject)obj).get("afterSaleMinutes") != null ){
//        			afterSaleMinutes = (int) ((JSONObject)obj).get("afterSaleMinutes");
//        		}  
//        	} 
//		} catch (Exception e) {
//		}
//        
//        List<Coupon> couponList = new ArrayList<Coupon>();
//        for(Coupon couponTemp : couponList){
//        }
//        long dd = 1;
//        storeBusinessMapper.updateStoreIncome(dd, 55.5);
//   
        
//        JSONObject couponLimitJson = globalSettingService.getJsonObject(GlobalSettingName.COUPON_LIMIT_SET);
//       
//        	String limitCategoryIds = couponLimitJson.get("category_ids").toString();
//        
        return new JsonResponse().setSuccessful().setData(data);
    }

    private Map<String, Object> helpInfo(long userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("helpUrl", Constants.SERVER_URL + "/static/app/help.html");
        result.put("aboutUrl", Constants.SERVER_URL + "/static/app/about.html");
        result.put("agreementlUrl", Constants.SERVER_URL + "/static/app/policy.html");
        return result;
    }

    private Map<String, Object> userInviteInfo(long userId) {
        UserInvite userInvite = userInviteFacade.createNewIfNotExists(userId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("inviteCode", userInvite.getInviteCode());
        result.put("inviteCount", userInvite.getInviteCount());
        return result;
    }

    private Map<String, Object> signContent(long userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("signTitle", "签到");
        result.put("signRule", "签到得玖币");
        return result;
    }

    private Map<String, Object> shareContent(long userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        String title = "20元品牌时代来临，不用剁手买买买，还不上车？";
        String description = "下载俞姐姐客户端，品牌女装全场1折，靓衣20元抢不停，注册更送价值千元玖币！";
        String imageUrl = "/static/img/share-icon.png";
        String shareTitle = "规则说明";
        String shareRule = "分享链接单次点击可得10枚玖币";
        JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.AD_TITLES);
		for(Object obj : jsonArrayConfirm) {
			if(((JSONObject)obj).get("title") != null){
				title = (String) ((JSONObject)obj).get("title");
			}
			if(((JSONObject)obj).get("description") != null){
				description = (String) ((JSONObject)obj).get("description");
			}
		    if(((JSONObject)obj).get("imageUrl") != null){
		    	imageUrl = (String) ((JSONObject)obj).get("imageUrl");
			}
		    if(((JSONObject)obj).get("shareTitle") != null){
		    	shareTitle = (String) ((JSONObject)obj).get("shareTitle");
			}
		    if(((JSONObject)obj).get("shareRule") != null){
		    	shareRule = (String) ((JSONObject)obj).get("shareRule");
			}
		}
        result.put("title", title);
        result.put("description", description);
        result.put("imageUrl", Constants.SERVER_URL + imageUrl);
        result.put("url", MobileGrantController.getUserShareUrl(userId));
        result.put("shareTitle", shareTitle);
        result.put("shareRule", shareRule);
        
//        result.put("title", "20元品牌时代来临，不用剁手买买买，还不上车？");
//        result.put("description", "下载俞姐姐客户端，品牌女装全场1折，靓衣20元抢不停，注册更送价值千元玖币！");
//        result.put("imageUrl", Constants.SERVER_URL + "/static/img/share-icon.png");
//        result.put("url", MobileGrantController.getUserShareUrl(userId));
//        result.put("shareTitle", "规则说明");
//        result.put("shareRule", "分享链接单次点击可得10枚玖币");
        return result;
    }
    //删除旧表
    private List<Map<String, Object>> loadOrderStatus(long userId) {
//        Map<OrderStatus, Map<String, Object>> orderStatusMap = new HashMap<OrderStatus, Map<String, Object>>();
//        List<Map<String, Integer>> countMapList = orderService.getOrderCountMap(userId);
//        for (Map<String, Integer> countMap : countMapList) {
//            OrderStatus status = OrderStatus.parse(countMap.get("OrderStatus"), null);
//            if (status == null || !STATUS.contains(status)) {
//                continue;
//            }
//            
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("count", countMap.get("Count"));
//            map.put("dataUrl", new UriBuilder("/mobile/order/list.json").set("status", status.getIntValue()).toUri());
//            orderStatusMap.put(status, map);
//        }
//
//        List<Map<String, Object>> orderStatusList = new ArrayList<Map<String, Object>>();
//        for (OrderStatus status : STATUS) {
//            Map<String, Object> map = orderStatusMap.get(status);
//            if (map == null) {
//                map = new HashMap<String, Object>();
//                map.put("count", 0);
//                map.put("dataUrl",
//                    new UriBuilder("/mobile/order/list.json").set("status", status.getIntValue()).toUri());
//            }
//            map.put("name", status.name());
//            map.put("status", status.getIntValue());
//            map.put("displayName", status.getDisplayName());
//            orderStatusList.add(map);
//        }
    	//删除旧表改字段已经废弃
    	 List<Map<String, Object>> orderStatusList = new ArrayList<Map<String, Object>>();
        return orderStatusList;
    }
    
    private List<Map<String, Object>> loadOrderNewStatus(long userId) {
    	Map<OrderStatus, Map<String, Object>> orderStatusMap = new HashMap<OrderStatus, Map<String, Object>>();
    	List<Map<String, Integer>> countMapList = orderService.getOrderNewCountMap(userId);
    	for (Map<String, Integer> countMap : countMapList) {
    		OrderStatus status = OrderStatus.parse(countMap.get("OrderStatus"), null);
    		if (status == null || !STATUS.contains(status)) {
    			continue;
    		}
    		
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("count", countMap.get("Count"));
    		map.put("dataUrl", new UriBuilder("/mobile/order/newOrderList.json").set("status", status.getIntValue()).toUri());
    		orderStatusMap.put(status, map);
    	}
    	
    	List<Map<String, Object>> orderStatusList = new ArrayList<Map<String, Object>>();
    	for (OrderStatus status : STATUS) {
    		Map<String, Object> map = orderStatusMap.get(status);
    		if (map == null) {
    			map = new HashMap<String, Object>();
    			map.put("count", 0);
    			map.put("dataUrl",
    					new UriBuilder("/mobile/order/newOrderList.json").set("status", status.getIntValue()).toUri());
    		}
    		map.put("name", status.name());
    		map.put("status", status.getIntValue());
    		map.put("displayName", status.getDisplayName());
    		
    		orderStatusList.add(map);
    	}
    	
    	return orderStatusList;
    }

    @RequestMapping(value = "/resetpwd/commit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse resetPasswordCommit(@RequestParam("oldPassword") String oldPassword,
                                                 @RequestParam("password") String password,
                                                 @RequestParam("dupPassword") String dupPassword, UserDetail userDetail){
    	JsonResponse jsonResponse = new JsonResponse();

    	User user = userDetail.getUser();
    	if (user.getUserPassword() != null && user.getUserPassword().trim().length() > 0 && !StringUtils.equals(user.getUserPassword(), DigestUtils.md5Hex(oldPassword))) {
    		return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_INVALID_PASSWORD);
    	}

    	if (!StringUtils.equals(password, dupPassword)) {
    		return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PASSWORD_MISMATCH);
    	}    	
    	
        userService.updateUserPassword(user.getUserId(), DigestUtils.md5Hex(password));
    	
        return jsonResponse.setSuccessful();
    }
    
    @RequestMapping(value = "/updatecid")
    @ResponseBody
    public JsonResponse updateUserCid(@RequestParam("userCid") String userCid, UserDetail userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	User user = userDetail.getUser();
    	if(userCid!=null&&userCid.length()>0){
    		userService.updateUserCid(user.getUserId(), userCid);		
    	}
    	return jsonResponse.setSuccessful();
    }
    
    @RequestMapping(value = "/getbankcardsign", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getBankCardSign(UserDetail userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	User user = userDetail.getUser();
    	
    	UserBankCardSign userBankCardSign=userBankCardSignService.getUserBankCardSign(user.getUserId());
    	Map<String, Object> result = new HashMap<String, Object>();
		result.put("userBankCardSign", userBankCardSign);
        return jsonResponse.setSuccessful().setData(result);
    }

    @RequestMapping(value = "/icon/upload", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse iconChange(HttpServletRequest request, HttpServletResponse response, UserDetail userDetail){
    	JsonResponse jsonResponse = new JsonResponse();

		String storePath = null;
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multiservlet = (MultipartHttpServletRequest) request;
				MultipartFile file = multiservlet.getFile("file");
				storePath = fileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file);
			} else {
			}
		} catch (IOException e) {
			return jsonResponse.setResultCode(ResultCode.USER_UPLOAD_ERROR);
		}
		userService.updateUserIcon(userDetail.getUserId(), storePath);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("iconUrl", storePath);
        return jsonResponse.setSuccessful().setData(result);
    }
    
    //获取用户玖币增加列表
/*    @RequestMapping(value = "/coinchangelist/increase")
    @ResponseBody
    public JsonResponse coinIncreaseList(PageQuery pageQuery,UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        
       
        Map<String, Object> data = new HashMap<String, Object>();
        int totalCount = userCoinService.getIncreaseListCount(userDetail);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        data.put("pageQuery", pageQueryResult);
        
    	List<UserCoinLog> coinLogList = userCoinService.increaseList(userDetail, pageQuery);
    	for(UserCoinLog coins:coinLogList){
    		if(coins.getOperation()!=null&&coins.getOperation().getIntValue()>0){	
    			coins.setOperationName(UserCoinOperationDetail.getNameByValue(coins.getOperation().getIntValue()).getName());
    		}
    	}
    	    	
    	data.put("data", coinLogList);
        return jsonResponse.setSuccessful().setData(data);
    }*/
    @RequestMapping(value = "/pointChangeList")
    @ResponseBody
    public JsonResponse pointChangeList(PageQuery pageQuery, UserDetail userDetail) throws ParseException {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = userCoinService.pointChangeList(pageQuery, userDetail);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/pointChangeIncreaseList")
    @ResponseBody
    public JsonResponse pointChangeIncreaseList(PageQuery pageQuery, UserDetail userDetail) {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = userCoinService .pointChangeIncreaseList(pageQuery, userDetail);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/pointChangeReduceList")
    @ResponseBody
    public JsonResponse pointChangeReduceList(PageQuery pageQuery, UserDetail userDetail) {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = userCoinService .pointChangeReduceList(pageQuery, userDetail);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 
     * @param pageQuery
     * @param userDetail
     * @param type -1 全部 1 兑换优惠券 2 兑换商品
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    @RequestMapping(value = "/pointExchangeList")
    @ResponseBody
    public JsonResponse pointExchangeList(PageQuery pageQuery, UserDetail userDetail, @RequestParam(value = "type", required = false, defaultValue = "-1") int type) throws IllegalAccessException, InvocationTargetException {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = jiuCoinExchangeLogService.pointExchangeList(pageQuery, userDetail, type);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    

    //玖币充值提示信息
    @RequestMapping(value = "/coinChargeTips")
    @ResponseBody
    public JsonResponse coinChargeTips(PageQuery pageQuery,UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("tipTop1", "玖币充值温馨提示：");
    	data.put("tipTop2", "1、玖币充值后，仅充值账户可用；\n2、玖币充值后，可多次使用，且没有有效时间；\n3、每个玖币充值码仅可使用一次，充值码有效期60天，请及时充值。");
    	data.put("tipBottom", "如图所示，图片中所显示的二维码即为玖币充值二维码；充值二维码下方的数字及字母代码即为玖币充值码。");
    	data.put("imgTipUrl", "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/res/batchtips.png");
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    
    //邮箱/微信老用户绑定手机号发送验证码
    @RequestMapping(value = "/sendVerifyCode", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse sendPhoneVerifyCode(@RequestParam("phone") String phone,
            ClientPlatform clientPlatform, UserDetail userDetail, @ClientIp String ip) {
    	logger.error("MobileSecurityController sendPhoneVerifyCode clientPlatform:{}, ip:{}", clientPlatform.getPlatform(), ip);
    	return userService.sendPhoneVerifyCode(phone);
    }
    //手机用户解绑微信
    @RequestMapping(value = "/sendVerifyCodeLogon")
    @ResponseBody
    public JsonResponse sendPhoneVerifyCode(
            ClientPlatform clientPlatform, UserDetail userDetail, @ClientIp String ip) {
    	logger.error("MobileSecurityController sendPhoneVerifyCode clientPlatform:{}, ip:{}", clientPlatform.getPlatform(), ip);
    	
    	return userService.sendPhoneVerifyCode(userDetail);
    }
    
    //邮箱/老微信用户手机绑定提交
    @RequestMapping(value = "/phoneBind/commit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse phoneNumberCommit(@RequestParam("phone") String phone,
    		@RequestParam("verify_code") String verifyCode,
    		UserDetail userDetail) {
    	return userService.phoneNumberCommit(phone, verifyCode, userDetail.getUserId());
    }
    
    
    //微信绑定提交
    @RequestMapping(value = "/weiXinBind/commit" , method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse weiXinBindCommit(@RequestParam("phone") String phone,
    		@RequestParam("verify_code") String verifyCode,
    		@RequestParam(value = "weiXinId", required = true) String weiXinId,
    		@RequestParam(value = "nickName", required = true) String nickName,
    		@RequestParam(value = "userIconUrl", required = true) String userIconUrl,
    		UserDetail userDetail) {
    	return userService.weiXinBindCommit(phone, verifyCode, userDetail.getUserId(), weiXinId, nickName, userIconUrl);
    }
    
    //微信解绑提交
    @RequestMapping(value = "/weiXinUnBind/commit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse weiXinUnBindCommit(@RequestParam("phone") String phone,
    		@RequestParam("verify_code") String verifyCode,
    		UserDetail userDetail) {
    	return userService.weiXinUnBindCommit(phone, verifyCode, userDetail.getUserId());
    }

    
}
