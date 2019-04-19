package com.jiuy.web.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.business.facade.GloableFacade;
import com.jiuy.core.dao.GlobalSettingDao;
import com.jiuy.core.dao.mapper.BinaryDataDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.core.service.coupon.CouponTemplateService;
import com.jiuy.core.service.coupon.StoreCouponTemplateService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.PropertyName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.entity.BinaryData;
import com.jiuyuan.entity.GlobalSetting;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.util.ImageUtil;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@RequestMapping("/setting")
@Controller
@Login
public class GlobalSettingController {
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private GloableFacade gloableFacade;
    
	@Autowired
	private CouponTemplateService couponTemplateService;
	
	@Autowired
	private StoreCouponTemplateService storeCouponTemplateService;
	
	@Autowired
	private BinaryDataDao binaryDataDao;
	
	@Autowired
	private GlobalSettingDao globalSettingDao;
	
	
	
	

	@RequestMapping(value = "/add")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(@RequestParam(value = "property_name") String propertyName, 
			@RequestParam(value = "property_value") String propertyValue,
			@RequestParam(value = "group_id", required = false, defaultValue = "0") int groupId,
			@RequestParam(value = "group_value", required = false, defaultValue = "") String groupName,
			@RequestParam(value = "description", required = false, defaultValue = "") String description) {
		JsonResponse jsonResponse = new JsonResponse();
		
		GlobalSetting globalSetting = new GlobalSetting();
		globalSetting.setPropertyName(propertyName);
		globalSetting.setPropertyValue(propertyValue);
		globalSetting.setGroupId(groupId);
		globalSetting.setGroupName(groupName);
		globalSetting.setDescription(description);
		
		globalSettingService.add(globalSetting);
		
		return jsonResponse.setSuccessful();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
    @AdminOperationLog
	public JsonResponse update(@RequestParam(value = "property_name") String propertyName,
			@RequestParam(value = "property_value") String propertyValue,
			@RequestParam(value = "group_id", required = false, defaultValue = "0") int groupId,
			@RequestParam(value = "group_value", required = false, defaultValue = "") String groupName,
			@RequestParam(value = "description", required = false, defaultValue = "") String description) {
		JsonResponse jsonResponse = new JsonResponse();
		
		GlobalSetting globalSetting = new GlobalSetting();
		globalSetting.setPropertyName(propertyName);
		globalSetting.setPropertyValue(propertyValue);
		globalSetting.setGroupName(groupName);
		globalSetting.setGroupId(groupId);
		globalSetting.setDescription(description);
		globalSetting.setUpdateTime(System.currentTimeMillis());
		
		globalSettingService.update(globalSetting);
		
		if (GlobalSettingName.getByStringValue(propertyName) == GlobalSettingName.WATERMARK) {
			JSONObject jsonObject = JSON.parseObject(propertyValue);
			String url = jsonObject.getString("image");
			
			if (!StringUtils.equals("", url)) {
				long time = System.currentTimeMillis();
				BinaryData binaryData = new BinaryData();
				binaryData.setContent(ImageUtil.getImageFromURL(url));
				binaryData.setCreateTime(time);
				binaryData.setUpdateTime(time);
				binaryData.setType(0);
				
				binaryDataDao.add(binaryData);
			}
		}
		
		//添加推广季节权重表
		if(GlobalSettingName.getByStringValue(propertyName) == GlobalSettingName.PROMOTION_SEASON_NEW){
			globalSettingService.addProductSeasonWeight(propertyValue);
		}
		
		return jsonResponse.setSuccessful();
	}
	
	/**
	 * 修改敏感词
	 * @param property_value
	 * @return
	 */
	@RequestMapping(value = "/updateSensitiveWord")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse updateSensitiveWord( String property_value) {
		return updateProperty(GlobalSettingName.SENSITIVE_WORD.getStringValue(),property_value);
	}
	
	
	@RequestMapping(value = "/updateProperty")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse updateProperty( String property_name,String property_value) {
		JsonResponse jsonResponse = new JsonResponse();
		if(StringUtils.isEmpty(property_value)){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
		}
		
		GlobalSettingName globalSettingName = null;
		try {
			globalSettingName = getGlobalSettingName(property_name);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		GlobalSetting globalSetting = globalSettingService.getItem(globalSettingName);
		jsonResponse = update( property_name,  property_value, globalSetting.getGroupId(), globalSetting.getGroupName(),  globalSetting.getDescription());
		return jsonResponse;
	}
	
	
	
	
	@RequestMapping("/str/{property_name}")
	@ResponseBody
	public JsonResponse getStr(@PathVariable("property_name") String propertyName) {
		JsonResponse jsonResponse = new JsonResponse();
		GlobalSettingName globalSettingName = null;
		try {
			globalSettingName = getGlobalSettingName(propertyName);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		Map<String, Object> data = new HashMap<>();
		String str = globalSettingService.getSetting(globalSettingName);
		data.put("value", str);
			
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/jsonarray/{property_name}")
	@ResponseBody
	public JsonResponse getJsonArray(@PathVariable("property_name") String propertyName) {
		JsonResponse jsonResponse = new JsonResponse();
		GlobalSettingName globalSettingName = null;
		try {
			globalSettingName = getGlobalSettingName(propertyName);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		Map<String, Object> data = new HashMap<>();
		JSONArray jsonArray = globalSettingService.getJsonArray(globalSettingName);
		data.put("list", jsonArray);
			
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/jsonobject/{property_name}")
	@ResponseBody
	public JsonResponse getJsonObject(@PathVariable("property_name") String propertyName) {
		JsonResponse jsonResponse = new JsonResponse();
		GlobalSettingName globalSettingName = null;
		try {
			globalSettingName = getGlobalSettingName(propertyName);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		Map<String, Object> data = new HashMap<>();
		JSONObject jsonObject = globalSettingService.getJsonObject(globalSettingName);
		
		if (GlobalSettingName.FETCH_COUPON == globalSettingName) {
			JSONArray jsonArray = jsonObject.getJSONArray("setting");
			if (jsonArray != null) {
				for (Object item : jsonArray) {
					JSONObject jObject = (JSONObject) item;
					Long coupon_template_id = jObject.getLong("coupon_template_id");
					CouponTemplate couponTemplate = couponTemplateService.search(coupon_template_id);
					jObject.put("coupon_template", couponTemplate);
				}
			}
		}
		
		data.put("jsonObject", jsonObject);
		return jsonResponse.setSuccessful().setData(data);
	}
	/**
	 * 
	 * @param type  非必传(不传时初始化，传值后对应发放对象的value)例如:1对应新注册用户		2 邀请会员绑定
	 * @return
	 */
	@RequestMapping("/store/coupon/send")
	@ResponseBody
	public JsonResponse getStoreCouponSendSetting(@RequestParam(value = "type", required = false, defaultValue = "-1") int type) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.STORE_COUPON_SEND_SETTING);
					
		 if(type != -1){
			 JSONArray storeArray = jsonObject.getJSONArray("setting");
			 for (Object object : storeArray) {
				if(((JSONObject)object).getIntValue("type") == type){ 
					JSONObject storeJsonObject = globalSettingService.getSettingByStringPropertyName(((JSONObject)object).getString("propertyName"));
					JSONArray storeJsonArray = storeJsonObject.getJSONArray("setting");
					jsonObject = storeJsonObject;
					if (storeJsonArray != null) {
						for (Object item : storeJsonArray) {
							JSONObject jObject = (JSONObject) item;
							Long coupon_template_id = jObject.getLong("coupon_template_id");
							StoreCouponTemplate couponTemplate = storeCouponTemplateService.search(coupon_template_id);
							jObject.put("coupon_template", couponTemplate);
						}
					} else {
						jsonObject = null;
					}
				}
			}
		}

		data.put("jsonObject", jsonObject);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 
	 * @param type  非必传(不传时初始化，传值后对应发放对象的value)例如:1对应新注册用户		2 关注门店
	 * @return
	 */
	@RequestMapping("/yjj/coupon/send")
	@ResponseBody
	public JsonResponse getYjjCouponSendSetting(@RequestParam(value = "type", required = false, defaultValue = "-1") int type){
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.YJJ_COUPON_SEND_SETTING);
					
		 if(type != -1){
			 JSONArray storeArray = jsonObject.getJSONArray("setting");
			 for (Object object : storeArray) {
				if(((JSONObject)object).getIntValue("type") == type){ 
					JSONObject storeJsonObject = globalSettingService.getSettingByStringPropertyName(((JSONObject)object).getString("propertyName"));
					JSONArray storeJsonArray = storeJsonObject.getJSONArray("setting");
					jsonObject = storeJsonObject;
					if (storeJsonArray != null) {
						for (Object item : storeJsonArray) {
							JSONObject jObject = (JSONObject) item;
							Long coupon_template_id = jObject.getLong("coupon_template_id");
							CouponTemplate couponTemplate = couponTemplateService.search(coupon_template_id);
							jObject.put("coupon_template", couponTemplate);
						}
					} else {
						jsonObject = null;
					}
				}
			}
		}

		data.put("jsonObject", jsonObject);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/statistical/commit")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse added(@RequestParam(value = "property_name") String propertyName, 
			@RequestParam(value = "property_value") String propertyValue,
			@RequestParam(value = "group_id", required = false, defaultValue = "0") int groupId,
			@RequestParam(value = "group_value", required = false, defaultValue = "") String groupName,
			@RequestParam(value = "description", required = false, defaultValue = "") String description){
		JsonResponse jsonResponse = new JsonResponse();
		
		globalSettingService.updateStatisticalSetting(propertyName, propertyValue, groupId, groupName, description);
		
		return jsonResponse.setSuccessful();
	}
	
	@RequestMapping(value = "/global")
	@ResponseBody
    public JsonResponse loadGlobalPageInfo() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
        Collection<ProductPropValue> seasonList = propertyService.getPropertyByNameId(PropertyName.SEASON);
        
        Map<GlobalSettingName, Object> settingMap = globalSettingService.settingOfName(null);

        data.put("navigationBar", globalSettingService.getJsonArray(GlobalSettingName.NAV_BAR));
        data.put("seasonList", seasonList);
    	data.put("all_discount", settingMap.get(GlobalSettingName.ALL_DISCOUNT));
    	data.put("promotion_season", settingMap.get(GlobalSettingName.PROMOTION_SEASON));
    	data.put("start_ad_page", settingMap.get(GlobalSettingName.START_PAGE_AD));
    	data.put("version_update", settingMap.get(GlobalSettingName.VERSION_UPDATE));
    	data.put("first_discount", settingMap.get(GlobalSettingName.FIRST_DISCOUNT));
    	data.put("remain_count_tips", settingMap.get(GlobalSettingName.REMAIN_COUNT_TIPS));
    	data.put("watermark", settingMap.get(GlobalSettingName.WATERMARK));
    	data.put("recommended_product", settingMap.get(GlobalSettingName.RECOMMENDED_PRODUCT));
    	data.put("category_first_navigation", settingMap.get(GlobalSettingName.CATEGORY_FIRST_NAVIGATION));
    	data.put("promotion_season_new", settingMap.get(GlobalSettingName.PROMOTION_SEASON_NEW));
    	data.put("share_gift_setting", settingMap.get(GlobalSettingName.SHARE_GIFT_SETTING));
    	
    	JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.REGISTER_COUPONS);
    	JSONArray jsonArray = jsonObject.getJSONArray("coupons");
		if (jsonArray != null) {
			for (Object item : jsonArray) {
				JSONObject jObject = (JSONObject) item;
				Long coupon_template_id = jObject.getLong("template_id");
				CouponTemplate couponTemplate = couponTemplateService.search(coupon_template_id);
				jObject.put("coupon_template", couponTemplate);
			}
		}
		data.put("register_coupons", jsonObject);
    	
    	
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/invite/share")
	@ResponseBody
    public JsonResponse inviteShareInfo() {
		return gloableFacade.inviteShareInfo();
	}
	
	@RequestMapping(value = "/invite/gift")
	@ResponseBody
    public JsonResponse inviteGiftInfo() {
		return gloableFacade.inviteGiftInfo();
	}
	
	@RequestMapping(value = "/statistical/set")
	@ResponseBody
	public JsonResponse statisticalSetting() {
		return gloableFacade.statisticalSetting();
	}
	
	@RequestMapping(value = "/gift/content")
	@ResponseBody
    public JsonResponse GiftContentInfo() {
		return gloableFacade.GiftContentInfo();
	}
	
	@RequestMapping(value = "/shoppingCartBanner", method = RequestMethod.GET)
    @AdminOperationLog
	public String shoppingCartBanner2(ModelMap modelMap) {
		GlobalSettingName globalSettingName = GlobalSettingName.getByStringValue("shoppingCartBanner");
		if (globalSettingName == null) {
			return "";
		}
		
		JSONArray jsonArray = globalSettingService.getJsonArray(globalSettingName);
    	modelMap.put("list", jsonArray);
			
		return "page/backend/shoppingcartpage";
	}
	
	@RequestMapping(value = "/shopping/cart/banner", method = RequestMethod.GET)
    @AdminOperationLog
    @ResponseBody
	public JsonResponse shoppingCartBanner() {
		JsonResponse jsonResponse = new JsonResponse();
		GlobalSettingName globalSettingName = GlobalSettingName.getByStringValue("shoppingCartBanner");
		if (globalSettingName == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
		}
		
		Map<String, Object> data = new HashMap<>();
		JSONArray jsonArray = globalSettingService.getJsonArray(globalSettingName);
		data.put("list", jsonArray);
			
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	@RequestMapping("/draw/lottery")
	@ResponseBody
	public JsonResponse drawLottery() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		JSONObject draw_lottery = globalSettingService.getJsonObject(GlobalSettingName.DRAW_LOTTERY);
		data.put("draw_lottery", draw_lottery);
		data.put("remain_percent", calcRemainPercent(draw_lottery.getJSONArray("prize")));
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	private int calcRemainPercent(JSONArray prize) {
		int remain_percent = 100;
		if (prize != null && prize.size() > 0) {
			for (Object object : prize) {
				JSONObject prize_item = (JSONObject)object;
				remain_percent -= prize_item.getInteger("percent");
			}
		}
		return remain_percent;
	}

	private GlobalSettingName getGlobalSettingName(String propertyName) {
		GlobalSettingName globalSettingName = GlobalSettingName.getByStringValue(propertyName);
		if (globalSettingName == null) {
			throw new ParameterErrorException("无对应的propertyName!");
		}
		
		return globalSettingName;
	}
}
