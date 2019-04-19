package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.CouponFacade;
import com.yujj.entity.account.UserDetail;
import com.yujj.exception.ParameterErrorException;

/**
 * 代金券
 * @author Jeff.Zhan
 *
 */
@Controller
@RequestMapping("/mobile/coupon")
@Login
public class MobileCouponController {
	
	@Autowired
	private CouponFacade couponFacade;
	
	@RequestMapping("/fetchcenter/list")
	@ResponseBody
	public JsonResponse loadfetchCenterCoupons(UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		
		List<Map<String, Object>> list = couponFacade.loadFetchCenterCoupons(userDetail.getUserId());
		data.put("list", list);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/fetchcenter/fetch")
	@ResponseBody
	public JsonResponse fetchCoupons(UserDetail userDetail, @RequestParam("template_id") Long templateId) {
		
		JsonResponse jsonResponse = new JsonResponse();
		try {
			couponFacade.fetchCoupon(templateId, 1, userDetail.getUserId(), CouponGetWay.FETCH);
		} catch (ParameterErrorException e) {
			return jsonResponse.setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
}
