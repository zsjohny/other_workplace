package com.jiuy.store.api.tool.controller;

import java.util.List;
import java.util.Map;

import com.jiuy.base.exception.BizException;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.enums.CouponSendEnum;
import com.jiuy.rb.enums.CouponStateEnum;
import com.jiuy.rb.enums.CouponSysEnum;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.util.CouponAcceptVo;
import com.jiuyuan.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.IStoreCouponNewService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/supplier/coupon")
public class SupplierCouponController {
	private static final Logger logger = LoggerFactory.getLogger(SupplierCouponController.class);
	
	@Autowired
	private IStoreCouponNewService storeCouponNewService;


	@Autowired
	private ICouponServerNew couponServerNew;

	/**
	 * 品牌优惠券领取
	 */
	@RequestMapping("/drawSupplierCouponTemplate/auth")
	@ResponseBody
	@AdminOperationLog
	@Login
	public JsonResponse drawSupplierCouponTemplate(@RequestParam("supplierCouponTemplateId") long  supplierCouponTemplateId,
			                                    UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long userId = userDetail.getId();
		long businessNumber = userDetail.getUserDetail().getBusinessNumber();
	 	if(userId == 0){
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
	    }
	 	try {
	 		// storeCouponNewService.drawSupplierCouponTemplate(supplierCouponTemplateId, userId, businessNumber);
			CouponAcceptVo accept = new CouponAcceptVo(null,userId,null,CouponSysEnum.APP,CouponSendEnum.RECEIVE_SELF,CouponStateEnum.NOT_USE);
			accept.setTempId(supplierCouponTemplateId);
			couponServerNew.grant(accept);
	 		return jsonResponse.setSuccessful();
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (com.jiuy.base.exception.BizException e) {
			return handerException(e);
		}catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
		
	}
	
	
	/**
	 * 品牌列表领取供应商优惠券
	 */
	@RequestMapping("/brandListDrawCoupon")
	@ResponseBody
	public JsonResponse brandListDrawCoupon(@RequestParam("brandId") long brandId,
											UserDetail<StoreBusiness> userDetail){

		JsonResponse jsonResponse = new JsonResponse();
		try {
			// List<Map<String,Object>> list = storeCouponNewService.brandListDrawCoupon(brandId);
			List<Map<String,Object>> list = couponServerNew.appCouponList(brandId);
			return jsonResponse.setSuccessful().setData(list);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
	}



	private JsonResponse handerException(Exception e) {

		e.printStackTrace();
		if (e instanceof BizException) {
			BizException be = (BizException) e;
			return new JsonResponse().setError(be.getMsg());
		}
		else {
			return new JsonResponse().setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
	}



}
