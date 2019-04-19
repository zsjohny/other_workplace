package com.jiuy.core.business.facade;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.coupon.CouponTemplateService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.coupon.RangeType;
import com.jiuyuan.entity.Product;
import com.jiuyuan.web.help.JsonResponse;

@Service
public class GloableFacade {

	@Autowired
	private GlobalSettingService globalSettingService;

	@Autowired
	private CouponTemplateService couponTemplateService;

	@Autowired
	private ProductService productService;

	public JsonResponse inviteGiftInfo() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();

		Map<GlobalSettingName, Object> settingMap = globalSettingService.settingOfName(null);

		JSONObject jsonArray_setting = (JSONObject) settingMap.get(GlobalSettingName.INVITE_GIFT_SETTING);
		JSONArray jsonArray_1 = (JSONArray) settingMap.get(GlobalSettingName.INVITE_GIFT_1);
		JSONArray jsonArray_2 = (JSONArray) settingMap.get(GlobalSettingName.INVITE_GIFT_2);
		JSONArray jsonArray_3 = (JSONArray) settingMap.get(GlobalSettingName.INVITE_GIFT_3);

		aeembleInviteRule(jsonArray_1);
		aeembleInviteRule(jsonArray_2);
		aeembleInviteRule(jsonArray_3);

		data.put("inviteGift_setting", jsonArray_setting);
		data.put("inviteGift_1", jsonArray_1);
		data.put("inviteGift_2", jsonArray_2);
		data.put("inviteGift_3", jsonArray_3);

		return jsonResponse.setSuccessful().setData(data);
	}

	public JsonResponse statisticalSetting() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();

		Map<GlobalSettingName, Object> settingMap = globalSettingService.settingOfName(null);

		JSONObject jsonArray_setting = (JSONObject) settingMap.get(GlobalSettingName.STATISTICAL_SETTING);
		data.put("statistical_setting", jsonArray_setting);

		return jsonResponse.setSuccessful().setData(data);
	}

	private void aeembleInviteRule(JSONArray jsonArray) {
		for (Object object : jsonArray) {
			Object templateIdObj = ((JSONObject) object).get("coupon_template_id");
			if (templateIdObj == null)
				return;

			long templateId = Long.parseLong(templateIdObj.toString());
			CouponTemplate couponTemplate = couponTemplateService.search(templateId);
			String name = null;
			if (couponTemplate != null) {
				name = couponTemplate.getName() + couponTemplate.getMoney() + "元"
						+ RangeType.getByValue(couponTemplate.getRangeType()).getDescription() + "代金券（ID:" + templateId
						+ "）";
			} else {
				name = "代金券模板id" + templateId + "参数有误!";
			}

			((JSONObject) object).put("name", name);
		}
	}

	public JsonResponse GiftContentInfo() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.GIFT_CONTENT);

		if (jsonObject.get("content") == null) {
			return jsonResponse.setSuccessful().setData(data);
		}

		JSONArray jsonArray = (JSONArray) jsonObject.get("content");
		try {
			aeembleInviteRule(jsonArray);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}

		data.put("gift_content", jsonObject);

		return jsonResponse.setSuccessful().setData(data);
	}

	public JSONObject getSearchRecommendedProduct(GlobalSettingName searchRecommendedProduct) {
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.SEARCH_RECOMMENDED_PRODUCT);
		JSONArray jsonArray = jsonObject.getJSONArray("products");

		if (jsonArray == null) {
			return jsonObject;
		}

		Set<Long> productIds = new HashSet<>();
		for (Object object : jsonArray) {
			JSONObject jObject = (JSONObject) object;
			productIds.add(jObject.getLong("id"));
		}

		Map<Long, Product> productMap = productService.productMapOfIds(productIds);

		for (Object object : jsonArray) {
			JSONObject jObject = (JSONObject) object;
			long productId = jObject.getLong("id");
			Product product = productMap.get(productId);
			jObject.put("product", product);
		}

		return jsonObject;
	}

	public JsonResponse inviteShareInfo() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.SHARE_GIFT_SETTING);

		String setting = globalSettingService.getSetting(
				GlobalSettingName.getByStringValue(GlobalSettingName.JIUCOIN_GLOBAL_SETTING.getStringValue()));
		JSONObject parseObject = JSON.parseObject(setting);

		JSONObject jsonObject2 = (JSONObject) parseObject.get("invitationSetting");
		jsonObject.put("register_jiubi", jsonObject2.get("eachObtain"));
		jsonObject.put("order_percent", jsonObject2.get("returnPercentage"));
		data.put("share_gift_setting", jsonObject);

		return jsonResponse.setSuccessful().setData(data);
	}

}
