package com.jiuy.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jiuyuan.service.common.*;
import com.jiuyuan.util.BizException;
import com.util.LocalMapUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.business.facade.StoreBusinessFacade;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.member.StoreBusinessSearch;
import com.jiuy.core.service.member.BrandBusinessService;
import com.jiuy.core.service.member.StoreAuditService;
import com.jiuy.core.service.member.StoreBusinessService;
import com.jiuy.core.service.member.StoreWxaAdminService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuy.web.controller.util.QrcodeUtils;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.City;
import com.jiuyuan.entity.Province;
import com.jiuyuan.entity.StoreRegister;
import com.jiuyuan.entity.StroreRegisterVO;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/storebusiness")
@Login
public class StoreBusinessController {

	private static final Logger logger = LoggerFactory.getLogger(StoreBusinessController.class);

	@Resource(name = "yjjMemberServiceImplDeprecated")
	private IYjjMemberServiceDeprecated memberService;

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
	@Autowired
	private ShopGlobalSettingService globalSettingService;
	@RequestMapping(value = "/search")
	@ResponseBody
	public JsonResponse search(PageQuery pageQuery,
			@RequestParam(value = "id", required = false, defaultValue = "-1") long id,
			@RequestParam(value = "business_number", required = false, defaultValue = "-1") long businessNumber,
			@RequestParam(value = "business_name", required = false, defaultValue = "") String businessName,
			@RequestParam(value = "status", required = false, defaultValue = "-2") int status,
			@RequestParam(value = "type", required = false, defaultValue = "-1") int businessType,
			@RequestParam(value = "company_name", required = false, defaultValue = "") String companyName,
			@RequestParam(value = "id_card", required = false, defaultValue = "") String idCardNumber,
			@RequestParam(value = "phone_number", required = false, defaultValue = "") String phoneNumber,
			@RequestParam(value = "create_time_min", required = false, defaultValue = "1970-1-1 00:00:00") String createTimeMinStr,
			@RequestParam(value = "create_time_max", required = false, defaultValue = "") String createTimeMaxStr,
			@RequestParam(value = "distribution_status", required = false, defaultValue = "-2") int distributionStatus,
			@RequestParam(value = "province", required = false, defaultValue = "-1") long provinceCode,
			@RequestParam(value = "city", required = false, defaultValue = "-1") long cityCode,
			@RequestParam(value = "business_address", required = false, defaultValue = "") String businessAddress,
			@RequestParam(value = "legal_person", required = false, defaultValue = "") String legalPerson,
			@RequestParam(value = "membStatus", required = false) Integer membStatus,//会员状态
			@RequestParam(value = "super_business_number", required = false, defaultValue = "-1") long superBusinessNumber// 上级门店
	) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();

		// TODO 搜索添加上级门店条件
		StoreBusiness storeBusiness = null;
		long superStoreId = -1;
		if (superBusinessNumber != -1) {
			storeBusiness = storeBusinessService.searchBusinessByBusinessNumber(superBusinessNumber);
			if (storeBusiness != null) {
				superStoreId = storeBusiness.getId();
			}
		}

		// 转换时间
		long createTimeMinL = 0L;
		long createTimeMaxL = -1L;
		try {
			createTimeMinL = DateUtil.convertToMSEL(createTimeMinStr);
			if (!StringUtils.equals(createTimeMaxStr, "")) {
				createTimeMaxL = DateUtil.convertToMSEL(createTimeMaxStr);
			}
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER)
					.setError("startTime:" + createTimeMinL + " endTime:" + createTimeMaxL);
		}

		StoreBusinessSearch storeBusinessSearch = new StoreBusinessSearch();
		storeBusinessSearch.setId(id);
		storeBusinessSearch.setBusinessNumber(businessNumber);
		storeBusinessSearch.setBusinessName(businessName);
		storeBusinessSearch.setStatus(status);
		storeBusinessSearch.setBusinessType(businessType);
		storeBusinessSearch.setCompanyName(companyName);
		storeBusinessSearch.setIdCardNumber(idCardNumber);
		storeBusinessSearch.setCreateTimeMin(createTimeMinL);
		storeBusinessSearch.setCreateTimeMax(createTimeMaxL);
		storeBusinessSearch.setDistributionStatus(distributionStatus);
		storeBusinessSearch.setProvinceCode(provinceCode);
		storeBusinessSearch.setCityCode(cityCode);
		storeBusinessSearch.setBusinessAddress(businessAddress);
		storeBusinessSearch.setLegalPerson(legalPerson);
		storeBusinessSearch.setPhoneNumber(phoneNumber);
		int count = storeBusinessService.searchCount(storeBusinessSearch, superStoreId);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);

//		List<StoreBusiness> list = storeBusinessService.search(storeBusinessSearch, pageQueryResult, superStoreId);
		List<StoreBusinessSearch> list = storeBusinessService.searchStoreAndMember(storeBusinessSearch, pageQueryResult, superStoreId, membStatus);



		int personCount = storeBusinessService.searchPersonCount(storeBusinessSearch, superStoreId);

		data.put("storeBusinessList", list);
		data.put("total", pageQueryResult);
		data.put("personCount", personCount);
		return jsonResponse.setSuccessful().setData(data);

	}

	/**
	 * 
	 * @param type
	 *            0:设置账户状态 1：设置分销状态
	 * @return
	 */
	@RequestMapping(value = "/setStatus")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse setStatus(@RequestParam(value = "type") int type, @RequestParam(value = "status") int status,
			@RequestParam(value = "ids") String ids) {
		JsonResponse jsonResponse = new JsonResponse();
		if (type == 0) {// 设置账户状态
			storeBusinessService.setStatusForIds(status, ids);
		} else if (type == 1) {// 设置分销状态
			storeBusinessService.setDistributionStatusForIds(status, ids);
		}
		return jsonResponse.setSuccessful();
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(HttpServletRequest request, @RequestBody StoreBusiness storeBusiness) {

		JsonResponse jsonResponse = new JsonResponse();

		if (StringUtils.equals("", storeBusiness.getBusinessName())) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("请填写商家名称");
		}

		if (StringUtils.equals("", storeBusiness.getPhoneNumber())) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("请填写手机号");
		}

		// 验证商家名称是否存在
		if (checkBusinessName(storeBusiness.getBusinessName(), -1)) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("商家名称已存在");
		}

		// 验证商家手机号是否存在
		if (checkPhoneNumber(storeBusiness.getPhoneNumber(), -1)) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("该手机号已存在");
		}

		// 上级门店信息
		StoreBusiness superStore = null;
		// if (storeBusiness.getSuperBusinessNumber() != 0) {
		// superStore =
		// storeBusinessDao.searchBusinessByBusinessNumber(storeBusiness.getSuperBusinessNumber());
		// if (superStore == null) {
		// return
		// jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("不存在该上级商家号");
		// }
		// }

		// 最大采购提成
		if (storeBusiness.getCommissionPercentage() > 30) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("采购返现率设置上限为30%，请重新输入再提交保存");
		} else if (storeBusiness.getCommissionPercentage() < 0) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("请设置正确的数值");
		}

		// 最大分销提成百分比
		// if (storeBusiness.getSuperBusinessNumber() == 0) {// 是一级门店
		// if (storeBusiness.getMemberCommissionPercentage() > 30) {
		// return
		// jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("分销提成率设置上限为30%，请重新输入再提交保存");
		// } else if (storeBusiness.getMemberCommissionPercentage() < 0) {
		// return
		// jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("请设置正确的数值");
		// }
		// } else {
		// if (storeBusiness.getMemberCommissionPercentage() > 100) {
		// return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN)
		// .setError("分销提成率设置上限为100%，请重新输入再提交保存");
		// } else if (storeBusiness.getMemberCommissionPercentage() < 0) {
		// return
		// jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("请设置正确的数值");
		// }
		// }

		// 判断是否选择了银行卡、支付宝、微信钱包3种方式至少一项
		/*
		 * if(storeBusiness.getBankCardFlag()==0&&storeBusiness.getAlipayFlag()=
		 * =0&&storeBusiness.getWeixinFlag()==0){ return
		 * jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).
		 * setError("银行卡、支付宝、微信钱包3种方式至少选择一项"); }
		 */
		/*
		 * //测试用验证码 if(!StringUtils.equals("1", storeBusiness.getCode())){
		 * //校验验证码
		 * if(!yunXinSmsService.verifyCode(storeBusiness.getPhoneNumber(),
		 * storeBusiness.getCode())){ //校验失败 return
		 * jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).
		 * setError("验证码错误"); } }
		 */

		/* try{ */
		storeBusinessService.add(storeBusiness, superStore);
		/*
		 * }catch (Exception e) { return
		 * jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError(
		 * "新增门店失败"); }
		 */

		return jsonResponse.setSuccessful();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse edit(HttpServletRequest request, @RequestBody StoreBusiness storeBusiness) {

		JsonResponse jsonResponse = new JsonResponse();

		long id = storeBusiness.getId();
		if (id == 0) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("请传入商家id");
		}

		if (StringUtils.equals("", storeBusiness.getBusinessName())) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("请填写商家名称");
		}
		if (StringUtils.equals("", storeBusiness.getPhoneNumber())) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("请填写手机号");
		}

		// 验证商家名称是否存在
		if (checkBusinessName(storeBusiness.getBusinessName(), storeBusiness.getId())) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("商家名称已存在");
		}

		// 验证商家手机号是否存在
		if (checkPhoneNumber(storeBusiness.getPhoneNumber(), storeBusiness.getId())) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("该手机号已存在");
		}

		// 最大采购提成
		if (storeBusiness.getCommissionPercentage() > 30) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("采购返现率设置上限为30%，请重新输入再提交保存");
		} else if (storeBusiness.getCommissionPercentage() < 0) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("请设置正确的数值");
		}

		// 最大分销提成百分比
		// if (storeBusiness.getSuperBusinessNumber() == 0) {// 是一级门店
		// if (storeBusiness.getMemberCommissionPercentage() > 30) {
		// return
		// jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("分销提成率设置上限为30%，请重新输入再提交保存");
		// } else if (storeBusiness.getMemberCommissionPercentage() < 0) {
		// return
		// jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("请设置正确的数值");
		// }
		// } else {
		// if (storeBusiness.getMemberCommissionPercentage() > 100) {
		// return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN)
		// .setError("分销提成率设置上限为100%，请重新输入再提交保存");
		// } else if (storeBusiness.getMemberCommissionPercentage() < 0) {
		// return
		// jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("请设置正确的数值");
		// }
		// }

		// 判断是否选择了银行卡、支付宝、微信钱包3种方式至少一项
		// if(storeBusiness.getBankCardFlag()==0&&storeBusiness.getAlipayFlag()==0&&storeBusiness.getWeixinFlag()==0){
		// return
		// jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("银行卡、支付宝、微信钱包3种方式至少选择一项");
		// }

		// 测试用验证码
		/*
		 * if(!StringUtils.equals("1", storeBusiness.getCode())){ //校验验证码
		 * if(!yunXinSmsService.verifyCode(storeBusiness.getPhoneNumber(),
		 * storeBusiness.getCode())){ //校验失败 return
		 * jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).
		 * setError("验证码错误"); } }
		 */

		/* try { */
		AdminUser userinfo = (AdminUser) request.getSession().getAttribute("userinfo");
		storeBusinessService.updateStoreBusiness(storeBusiness, userinfo);
		/*
		 * } catch (Exception e) { return
		 * jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN); }
		 */

		return jsonResponse.setSuccessful();
	}

	@RequestMapping(value = "/sendcode")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse sendStoreBusinessCode(@RequestParam(value = "phone") String phone) {
		JsonResponse jsonResponse = new JsonResponse();
		boolean sendResult = yunXinSmsService.sendCode(phone, 3);
		if (sendResult) {
			return jsonResponse.setSuccessful();
		} else {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("短信发送失败");
		}

	}

	@RequestMapping(value = "/fetchprovince")
	@ResponseBody
	public JsonResponse fetchProvince() {
		JsonResponse jsonResponse = new JsonResponse();

		Map<String, Object> data = new HashMap<String, Object>();
		List<Province> provinceList = storeBusinessService.getProvinceList();
		data.put("provinceList", provinceList);

		return jsonResponse.setSuccessful().setData(data);
	}

	@RequestMapping(value = "/fetchcity")
	@ResponseBody
	public JsonResponse fetchProvince(@RequestParam(value = "p") long parentId) {
		JsonResponse jsonResponse = new JsonResponse();

		Map<String, Object> data = new HashMap<String, Object>();
		List<City> cityList = storeBusinessService.getCitysByProvinceId(parentId);
		data.put("cityList", cityList);

		return jsonResponse.setSuccessful().setData(data);
	}

	@RequestMapping("/download/qrcode")
	@ResponseBody
	public void downloadQrCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("url") String url, @RequestParam("type") int type, @RequestParam("file_name") String fileName,
			@RequestParam("width") int width, @RequestParam("height") int height) {
		QrcodeUtils.getFile(request, response, AdminConstants.APP_ROOT_PATH + url + "&type=" + type, fileName, width,
				height);
	}

	@RequestMapping("/fetchbrand")
	@ResponseBody
	public JsonResponse fetchBrandRelation(@RequestParam(value = "store_id") long storeId) {
		JsonResponse jsonResponse = new JsonResponse();

		List<BrandLogo> allBrandList = brandbusinessService.getBrandList(); // 获得全部品牌
		List<BrandLogo> relationBrandList = storeBusinessService.getRelationBrandList(storeId);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("allBrandList", allBrandList);
		data.put("relationBrandList", relationBrandList);

		return jsonResponse.setSuccessful().setData(data);
	}

	@RequestMapping("/fetchbrands")
	@ResponseBody
	public JsonResponse fetchBrand() {
		JsonResponse jsonResponse = new JsonResponse();

		List<BrandLogo> allBrandList = brandbusinessService.getBrandList(); // 获得全部品牌

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("allBrandList", allBrandList);
		return jsonResponse.setSuccessful().setData(data);
	}

	/**
	 * 
	 * @param storeIds
	 * @param brandIdsString
	 * @param chooseType
	 *            选择类型 0 应用到所选用户 1 应用到所有用户
	 * @return
	 */
	@RequestMapping("/updatebrand")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateBrandRelation(@RequestParam(value = "store_ids") String storeIds,
			@RequestParam(value = "brandids_string") String brandIdsString,
			@RequestParam(value = "choose_type", required = false, defaultValue = "0") int chooseType,
			@RequestParam(value = "id", required = false, defaultValue = "-1") long id,
			@RequestParam(value = "business_number", required = false, defaultValue = "-1") long businessNumber,
			@RequestParam(value = "business_name", required = false, defaultValue = "") String businessName,
			@RequestParam(value = "status", required = false, defaultValue = "-2") int status,
			@RequestParam(value = "type", required = false, defaultValue = "-1") int businessType,
			@RequestParam(value = "company_name", required = false, defaultValue = "") String companyName,
			@RequestParam(value = "id_card", required = false, defaultValue = "") String idCardNumber,
			@RequestParam(value = "phone_number", required = false, defaultValue = "") String phoneNumber,
			@RequestParam(value = "create_time_min", required = false, defaultValue = "1970-1-1 00:00:00") String createTimeMinStr,
			@RequestParam(value = "create_time_max", required = false, defaultValue = "") String createTimeMaxStr,
			@RequestParam(value = "distribution_status", required = false, defaultValue = "-2") int distributionStatus,
			@RequestParam(value = "province", required = false, defaultValue = "-1") long provinceCode,
			@RequestParam(value = "city", required = false, defaultValue = "-1") long cityCode,
			@RequestParam(value = "business_address", required = false, defaultValue = "") String businessAddress,
			@RequestParam(value = "legal_person", required = false, defaultValue = "") String legalPerson) {
		JsonResponse jsonResponse = new JsonResponse();
		if (chooseType == 1) { // 筛选条件下所有的用户
			// 转换时间
			long createTimeMinL = 0L;
			long createTimeMaxL = -1L;
			try {
				createTimeMinL = DateUtil.convertToMSEL(createTimeMinStr);
				if (!StringUtils.equals(createTimeMaxStr, "")) {
					createTimeMaxL = DateUtil.convertToMSEL(createTimeMaxStr);
				}
			} catch (ParseException e) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER)
						.setError("startTime:" + createTimeMinL + " endTime:" + createTimeMaxL);
			}

			StoreBusinessSearch storeBusinessSearch = new StoreBusinessSearch();
			storeBusinessSearch.setId(id);
			storeBusinessSearch.setBusinessNumber(businessNumber);
			storeBusinessSearch.setBusinessName(businessName);
			storeBusinessSearch.setStatus(status);
			storeBusinessSearch.setBusinessType(businessType);
			storeBusinessSearch.setCompanyName(companyName);
			storeBusinessSearch.setIdCardNumber(idCardNumber);
			storeBusinessSearch.setCreateTimeMin(createTimeMinL);
			storeBusinessSearch.setCreateTimeMax(createTimeMaxL);
			storeBusinessSearch.setDistributionStatus(distributionStatus);
			storeBusinessSearch.setProvinceCode(provinceCode);
			storeBusinessSearch.setCityCode(cityCode);
			storeBusinessSearch.setBusinessAddress(businessAddress);
			storeBusinessSearch.setLegalPerson(legalPerson);
			storeBusinessSearch.setPhoneNumber(phoneNumber);

			List<StoreBusiness> list = storeBusinessService.search(storeBusinessSearch, null);
			ArrayList<Long> storeIdList = new ArrayList<>();
			for (StoreBusiness storeBusiness : list) {
				storeIdList.add(storeBusiness.getId());
			}
			if (storeIdList != null && storeIdList.size() > 0) {
				storeBusinessService.updateAllBrandRelation(brandIdsString, storeIdList);
			} else {
				jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("没有找到需要修改的相关用户");
			}

		} else {
			storeBusinessService.updateBrandRelation(storeIds, brandIdsString);
		}

		return jsonResponse.setSuccessful();
	}

	@RequestMapping("/register/search")
	@ResponseBody
	public JsonResponse registerSearch(PageQuery pageQuery,
			@RequestParam(value = "apply_id", required = false, defaultValue = "-1") long applyId,
			@RequestParam(value = "phone_number", required = false, defaultValue = "") String phoneNumber,
			@RequestParam(value = "business_name", required = false, defaultValue = "") String businessName,
			@RequestParam(value = "status", required = false, defaultValue = "-5") int status,
			@RequestParam(value = "business_type", required = false, defaultValue = "-1") int businessType,
			@RequestParam(value = "company_name", required = false, defaultValue = "") String companyName,
			@RequestParam(value = "super_business_number", required = false, defaultValue = "0") long superBusinessNumber, // 上级门店号
			@RequestParam(value = "start_time", required = false, defaultValue = "") String startTime,
			@RequestParam(value = "end_time", required = false, defaultValue = "") String endTime,
			@RequestParam(value = "id_cart", required = false, defaultValue = "") String idCardNumber,
			@RequestParam(value = "business_address", required = false, defaultValue = "") String businessAddress,
			@RequestParam(value = "legal_person", required = false, defaultValue = "") String LegalPerson,
			@RequestParam(value = "apply_start_time", required = false, defaultValue = "") String stratApplyTime,
			@RequestParam(value = "apply_end_time", required = false, defaultValue = "") String endApplyTime) {
		JsonResponse jsonResponse = new JsonResponse();

		long startCreateTimeL = 0L;
		long endCreateTimeL = 0L;
		long startApplyTimeL = 0L;
		long endApplyTimeL = 0L;
		try {
			startCreateTimeL = DateUtil.convertToMSEL(startTime);
			endCreateTimeL = DateUtil.convertToMSEL(endTime);
			startApplyTimeL = DateUtil.convertToMSEL(stratApplyTime);
			endApplyTimeL = DateUtil.convertToMSEL(endApplyTime);
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
		Map<String, Object> data = new HashMap<String, Object>();

		StoreBusiness storeBusiness = null;
		long superStoreId = 0;
		if (superBusinessNumber != 0) {
			storeBusiness = storeBusinessService.searchBusinessByBusinessNumber(superBusinessNumber);
			if (storeBusiness != null) {
				superStoreId = storeBusiness.getId();
			}
		}

		StroreRegisterVO stroreRegisterVO = new StroreRegisterVO(applyId, phoneNumber, businessName, status,
				businessType, companyName, startCreateTimeL, endCreateTimeL, idCardNumber, businessAddress, LegalPerson,
				startApplyTimeL, endApplyTimeL, superStoreId);

		int totalCount = storeBusinessService.getStoreRegisterCount(stroreRegisterVO);

		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);

		List<StoreRegister> registers = storeBusinessService.getStoreRegister(pageQuery, stroreRegisterVO);
		Map<String, Object> summary = storeBusinessService.getRegisterSummary(stroreRegisterVO);
		data.put("list", registers);
		data.put("total", pageQueryResult);
		data.put("summary", summary);
		return jsonResponse.setSuccessful().setData(data);
	}

	/**
	 * 
	 * @param type
	 *            0:不通过 1:通过 2：创建账户
	 * @param applyIds
	 *            id,id,id...
	 * @param status
	 *            status,status...
	 * @param applyMemo
	 * @return
	 */
	@RequestMapping("/register/changeStatus")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse changeRegisterStatus(@RequestParam(value = "type") int type, // type
																						// 0:申请不通过
																						// 1:申请通过
																						// 2：创建账户
			@RequestParam(value = "apply_ids") String applyIds, // id,id,id...
			@RequestParam(value = "status") String status, // staus,status...
			@RequestParam(value = "apply_memo", required = false, defaultValue = "") String applyMemo, // 申请理由
			@RequestParam(value = "phone_number", required = false, defaultValue = "") String phoneNumbers,
			HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		AdminUser userinfo = (AdminUser) request.getSession().getAttribute("userinfo");
		if (type == 0) {// 改成不通过
			if (splitString(status, type)) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("请选择未审核数据");
			}
			storeBusinessService.updateStoreRegisterForStatus(-1, applyIds, applyMemo, userinfo.getUserName(),
					userinfo.getUserId(), System.currentTimeMillis());
			List<StoreRegister> storeRegisters = storeBusinessService.getStoreregistersForIds(applyIds);
			for (StoreRegister storeRegister : storeRegisters) {// 发送短信
				sendFailedCode(storeRegister.getPhoneNumber());
			}
		} else if (type == 1) {// 改成通过
			if (splitString(status, type)) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("请选择未审核数据");
			}
			storeBusinessService.updateStoreRegisterForStatus(1, applyIds, applyMemo, userinfo.getUserName(),
					userinfo.getUserId(), System.currentTimeMillis());
		} else if (type == 2) {// 创建账户
			if (splitString(status, type)) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("你的选择中有未通过或已创建账户的数据");
			}
			if (phoneNumbers.isEmpty()) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("参数异常");
			}
			int count = storeBusinessDao.checkPhoneNumbers(phoneNumbers);
			if (count == 0) {
				List<StoreRegister> storeRegisters = storeBusinessService.getStoreregistersForIds(applyIds);
				for (StoreRegister storeRegister : storeRegisters) {
					// 验证商家名称是否存在
					if (checkBusinessName(storeRegister.getBusinessName(), -1)) {
						return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("商家名称已存在");
					}
					StoreBusiness storeBusiness = new StoreBusiness(storeRegister.getBusinessName(),
							storeRegister.getCompanyName(), storeRegister.getPhoneNumber(), Double.valueOf("30"),
							storeRegister.getBusinessAddress(), storeRegister.getLegalPerson(),
							storeRegister.getIdCardNumber(), storeRegister.getBusinessType());
					try {
						storeBusinessFacade.add(storeBusiness, storeRegister, userinfo, applyMemo);
					} catch (ParameterErrorException e) {
						return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER)
								.setError(e.getMessage());
					}
				}
			} else {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("你的选择的数据中有手机号已注册使用过");
			}
		}
		return jsonResponse.setSuccessful();
	}

	private void sendFailedCode(String phoneNumber) {
		JSONArray params = new JSONArray();
		yunXinSmsService.send(phoneNumber, params, 3056101);
	}

	/**
	 * 检验门店商家名称是否存在
	 * 
	 * @param businessName
	 * @return true 已存在 false 不存在
	 */
	public boolean checkBusinessName(String businessName, long existId) {
		long id = storeBusinessDao.checkBusinessName(businessName);

		if (id == existId) { // 存在名称的商家自己修改名称
			return false;
		}
		return id > 0 ? true : false;
	}

	/**
	 * 检验门店商家手机号是否存在
	 * 
	 * @param
	 * @return true 已存在 false 不存在
	 */
	public boolean checkPhoneNumber(String phoneNumber, long existId) {
		long id = storeBusinessDao.checkPhoneNumber(phoneNumber);

		if (id == existId) { // 存在号码的商家自己修改手机号码
			return false;
		}
		return id > 0 ? true : false;
	}

	/**
	 * 是否符合要求的数据
	 * 
	 * @param str
	 *            切割的字符串(,)
	 * @param type
	 * @return
	 */
	public boolean splitString(String str, int type) {
		if (!str.isEmpty()) {
			String[] splits = str.split(",");
			for (String string : splits) {
				int status = Integer.parseInt(string);
				if (type == 0 || type == 1) {
					if (status != 0) {
						return true;
					}
				} else if (type == 2) {// 创建账户
					if (status == -1 || status == 2) {
						return true;
					}
				}
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * 获取新门店审核列表
	 * 
	 * @param status
	 *            状态:’0：未审核 1：通过 -1:拒绝 -2禁用 2 未完成’
	 * @param page
	 * @param pageSize
	 * @param isVip
	 * @param keyWord
	 * @param registTimeStart
	 * @param registTimeEnd
	 * @param phoneNumber
	 * @param userName
	 * @param storeBusinessId
	 *            // * @param storeType
	 * @param storeBusinessName
	 * @param storeBusinessAddress
	 * @return
	 */
	@RequestMapping(value = "/audit/list")
	@ResponseBody
	public JsonResponse getAuditlist(@RequestParam("status") Integer status,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "isVip", required = false, defaultValue = "-1") Integer isVip,
			@RequestParam(value = "keyWord", required = false, defaultValue = "") String keyWord,
			@RequestParam(value = "registTimeStart", required = false, defaultValue = "") String registTimeStart,
			@RequestParam(value = "registTimeEnd", required = false, defaultValue = "") String registTimeEnd,
			@RequestParam(value = "phoneNumber", required = false, defaultValue = "") String phoneNumber,
			@RequestParam(value = "userName", required = false, defaultValue = "") String userName,
			@RequestParam(value = "storeBusinessId", required = false, defaultValue = "0") long storeBusinessId,
			// @RequestParam(value = "storeType",required = false, defaultValue
			// = "0")int storeType,
			@RequestParam(value = "storeBusinessName", required = false, defaultValue = "") String storeBusinessName,
			@RequestParam(value = "storeBusinessAddress", required = false, defaultValue = "") String storeBusinessAddress,
			@RequestParam(value = "referenceNumber", required = false, defaultValue = "") String referenceNumber) {
		return storeAuditService.getAuditList(status, new PageQuery(page, pageSize), isVip, keyWord, registTimeStart,
				registTimeEnd, phoneNumber, userName, storeBusinessId, storeBusinessName, storeBusinessAddress,
				referenceNumber);
	}

	/**
	 * 新门店审核通过/拒绝/返回
	 * 
	 * @param id
	 *            审核Id
	 * @param status
	 *            状态:’0：未审核 1：通过 -1:拒绝 -2禁用’
	 * @return
	 */
	@RequestMapping(value = "/audit/allow")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse allowAudit(@RequestParam("id") Long id, @RequestParam("status") Integer status,
			@RequestParam(value = "refuseReason", required = false, defaultValue = "") String refuseReason,
			HttpServletRequest request) {
		try {
			AdminUser userinfo = (AdminUser) request.getSession().getAttribute("userinfo");
			return storeAuditService.updateAuditStatusDispatcher(id, status, userinfo, refuseReason);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResponse().setError("新门店审核通过/拒绝/返回失败");
		}
	}




	/**
	 * 设置门店为vip/非vip
	 * 
	 * @param storeId
	 * @return
	 */
	@RequestMapping(value = "/setvip")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse setVip(@RequestParam("storeId") long storeId, @RequestParam("vip") int vip,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		Long userId = Long.parseLong(session.getAttribute("userid").toString());

		logger.error("设置门店为vipstoreBusiness:storeId--" + storeId + ";vip--" + vip);
		JsonResponse jsonResponse = new JsonResponse();
		boolean flag = storeBusinessService.setVIP(storeId, vip, userId);
		logger.error("设置门店为vipstoreBusiness:flag--" + flag);
		try {
			if (flag) {
				return jsonResponse.setSuccessful();
			} else {
				return jsonResponse.setError("设置门店为vip失败");
			}
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
	}

	/**
	 * 门店编辑
	 * 
	 * @param storeId
	 * @return
	 */
	@RequestMapping(value = "/toupdate")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse toUpdate(@RequestParam("storeId") long storeId) {
		logger.error("门店编辑storeBusiness:storeId--" + storeId);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			StoreBusiness storeBusiness = storeBusinessService.searchBusinessById(storeId);
			logger.error("门店编辑storeBusiness:" + storeBusiness.toString());
			if (storeBusiness == null) {
				return jsonResponse.setError("没有该门店,请确认");
			}
			data.put("storeId", storeBusiness.getId() + "");
			data.put("businessName", storeBusiness.getBusinessName());
			data.put("legalPerson", storeBusiness.getLegalPerson());
			data.put("province", storeBusiness.getProvince());
			data.put("city", storeBusiness.getCity());
			data.put("county", storeBusiness.getCounty());
			data.put("businessAddress", storeBusiness.getBusinessAddress());
			data.put("IdCardNumber", storeBusiness.getLegalIdNumber());
			data.put("qualificationProofImages", storeBusiness.getQualificationProofImages().split(","));
			data.put("referenceNumber", storeBusiness.getOrganizationNo());


			// 填充新增的店铺审核信息，风格，年龄范围，店铺面积
			List<Map<String, Object>> storeStyle = parseJson(globalSettingService.getJsonArray(GlobalSettingName.STORE_STYLE),storeBusiness.getStoreStyle());
			List<Map<String, Object>> storeAreaScope = parseJson(globalSettingService.getJsonArray(GlobalSettingName.STORE_AREA_SCOPE),storeBusiness.getStoreAreaScope()==null?"":storeBusiness.getStoreAreaScope().toString());
			List<Map<String, Object>> storeAge = parseJson(globalSettingService.getJsonArray(GlobalSettingName.STORE_AGE),storeBusiness.getStoreAge());

			Map<String,List<Map<String,Object>>> param = globalSettingService.getStorePriceAndPurc(storeBusiness);


			data.putAll(param);
			data.put("storeStyle", storeStyle);
			data.put("storeAreaScope", storeAreaScope);
			data.put("storeAge", storeAge);

			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}

	}

	/**
	 * 保存门店编辑资料
	 * @param storeId
	 * @param businessName
	 * @param legalPerson
	 * @param province
	 * @param city
	 * @param county
	 * @param businessAddress
	 * @param storeStyle
	 * @param storeAge
	 * @param storeAreaScope
	 * 
	 * var dataList = {};
	 * 
	 *     dataList.storeId=hash;
	 *     dataList.businessName = businessName
	 *     dataList.legalPerson = legalPerson
	 *     dataList.city = city6;
	 *      dataList.county = district6;
	 *      dataList.businessAddress = businessAddress;
	 *      IdCardNumber   法人身份证号码
	 *      qualificationProofImages   资质证明图片，英文逗号分隔的字符串
	 *      referenceNumber   组织机构号
	 *      dataList.storeStyle = shopStyleArray;
	 *      dataList.storeAge = shopAgeArray;
	 *         dataList.storeAreaScope = shopAreaArray;
	 *      dataList.province=province6;
            
           
	 * @return
	 */
	@RequestMapping(value = "/tosave")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse toSave(@RequestParam("storeId") long storeId, 
			@RequestParam("businessName") String businessName,
			@RequestParam("legalPerson") String legalPerson, 
			@RequestParam("province") String province,
			@RequestParam("city") String city,
			@RequestParam("county") String county,
			@RequestParam("businessAddress") String businessAddress,
//			@RequestParam(value = "IdCardNumber",required=false ,defaultValue="") String IdCardNumber,
//			@RequestParam(value = "qualificationProofImages", required = false, defaultValue = "") String qualificationProofImages,
//			@RequestParam(value = "referenceNumber", required = false, defaultValue = "") String referenceNumber,
			@RequestParam(value = "storeStyle") String storeStyle,
			@RequestParam(value = "storeAge") String storeAge,
			@RequestParam(value = "storeAreaScope") int storeAreaScope,
		    @RequestParam(value = "purchaseChannel") String purchaseChannel,
		    @RequestParam(value = "priceLevel") String priceLevel
	 ) {
		
		logger.error("保存门店编辑资料storeBusiness:storeId--" + storeId + ";businessName--" + businessName + ";legalPerson--"
				+ legalPerson + ";province--" + province + ";city--" + city + ";county--" + county
				+ ";businessAddress--" + businessAddress  );
		JsonResponse jsonResponse = new JsonResponse();
		try {
			
			
			StoreBusiness storeBusiness = new StoreBusiness();
			storeBusiness.setId(storeId);
			storeBusiness.setBusinessName(businessName);
			storeBusiness.setLegalPerson(legalPerson);
			storeBusiness.setProvince(province);
			storeBusiness.setCity(city);
			storeBusiness.setCounty(county);
			storeBusiness.setBusinessAddress(businessAddress);
//			storeBusiness.setLegalIdNumber(IdCardNumber);
//			storeBusiness.setQualificationProofImages(qualificationProofImages);
//			storeBusiness.setOrganizationNo(referenceNumber);
			storeBusiness.setStoreAge(storeAge);
			storeBusiness.setStoreStyle(storeStyle);
			storeBusiness.setStoreAreaScope(storeAreaScope);
			storeBusiness.setPurchaseChannel(purchaseChannel);
			storeBusiness.setPriceLevel(priceLevel);
			int record = storeBusinessService.toSaveUpdate(storeBusiness);
			logger.error("保存门店编辑资料storeBusiness:record--" + record);
			if (record != 1) {
				return jsonResponse.setError("保存门店编辑资料失败");
			}
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
	}

	/**
	 * 解析新增店铺审核相关信息json
	 * 
	 * @param jsonArr
	 * @param
	 * @return
	 */
	private List<Map<String, Object>> parseJson(JSONArray jsonArr, String str) {
		
		List<Map<String, Object>> list = new ArrayList<>();
		for (int j = 0; j < jsonArr.size(); j++) {
			JSONObject json = jsonArr.getJSONObject(j);
			Map<String, Object> map = new HashMap<>();
			map.put("id", json.getIntValue("id"));
			map.put("name", json.getString("name"));
			map.put("choosed", false);
			if (str != null && str != "") {
				String[] split = str.split(",");
				for (int i = 0; i < split.length; i++) {
					if (json.getIntValue("id") == Integer.valueOf(split[i])) {
						map.put("choosed", true);
					}
				}
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 开发会员
	 *
	 * @param storeId storeId
	 * @return com.jiuyuan.web.help.JsonResponse
	 * @author Charlie
	 * @date 2018/8/14 19:39
	 */
	@RequestMapping( "openMembershipAccount" )
	@ResponseBody
	public JsonResponse openMembershipAccount(Long storeId, Integer isOpen) {
		JsonResponse response = JsonResponse.getInstance ();
		try {
			if (isOpen == 1) {
				System.out.println ("开通会员-开");
				logger.info ("开通会员-开");
				memberService.openMemberShipAccount (storeId);
				LocalMapUtil.invalidate (String.format ("member:%d",storeId));
			}
			else if (isOpen == 0) {
				System.out.println ("开通会员-关");
				logger.info ("开通会员-关");
				memberService.closeMemberShipAccount (storeId);
				LocalMapUtil.invalidate (String.format ("member:%d",storeId));
			}
			else {
				return response.setError ("未知的操作类型");
			}
			return response.setSuccessful();
		} catch (Exception e) {
			if (e instanceof BizException) {
				String msg = ((BizException) e).getMsg ();
				return response.setError (msg);
			}
			e.printStackTrace ();
			return response.setError(e.getMessage());
		}
	}


}
