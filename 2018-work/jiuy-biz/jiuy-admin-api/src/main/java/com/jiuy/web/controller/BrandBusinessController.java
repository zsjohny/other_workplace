package com.jiuy.web.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.dao.BrandBusinessDao;
import com.jiuy.core.dao.LOWarehouseDao;
import com.jiuy.core.meta.member.BrandBusinessSearch;
import com.jiuy.core.service.member.BrandBusinessService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.BrandBusiness;
import com.jiuyuan.entity.City;
import com.jiuyuan.entity.Province;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
* @author WuWanjian
* @version 创建时间: 2016年10月27日 下午5:12:38
*/
@Controller
@RequestMapping("/brandbusiness")
@Login
public class BrandBusinessController {
	
	private static final Logger logger = LoggerFactory.getLogger(BrandBusinessController.class);
	
	@Resource
	private BrandBusinessService brandBusinessService;
	
	@Resource
	private BrandBusinessDao brandBusinessDao;
	
	@Resource
	private LOWarehouseDao lOWarehouseDao;
	
	@Resource
	private YunXinSmsService yunXinSmsService;
	
	@RequestMapping(value="/search")
	@ResponseBody
	public JsonResponse search(@RequestParam(value="page", required=false, defaultValue="1")int page,
	@RequestParam(value="page_size", required=false, defaultValue="10")int pageSize,
	@RequestParam(value="id", required=false, defaultValue="0")long id,
	@RequestParam(value="business_number", required=false, defaultValue="-1")long businessNumber,
	@RequestParam(value="phone", required=false, defaultValue="")String phone,
	@RequestParam(value="company_name", required=false, defaultValue="")String companyName,
	@RequestParam(value="status", required=false, defaultValue="-2")int status,
	@RequestParam(value="store_number_min", required=false, defaultValue="-1")int storeNumberMin,
	@RequestParam(value="store_number_max", required=false, defaultValue="-1")int storeNumberMax,
	@RequestParam(value="create_time_min", required=false, defaultValue="1970-1-1 00:00:00")String createTimeMinStr,
	@RequestParam(value="create_time_max", required=false, defaultValue="")String createTimeMaxStr,
	@RequestParam(value="cash_income_min", required=false, defaultValue="-1")double cashIncomeMin,
	@RequestParam(value="cash_income_max", required=false, defaultValue="-1")double cashIncomeMax,
	@RequestParam(value="available_balance_min", required=false, defaultValue="-1")double availableBalanceMin,
	@RequestParam(value="available_balance_max", required=false, defaultValue="-1")double availableBalanceMax){
		
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		PageQuery query = new PageQuery(page, pageSize);
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(query, queryResult);
		
		//转换时间
		long createTimeMinL = 0L;
		long createTimeMaxL = -1L;
		try {
			createTimeMinL = DateUtil.convertToMSEL(createTimeMinStr);
			if(!StringUtils.equals(createTimeMaxStr, "")){
				createTimeMaxL = DateUtil.convertToMSEL(createTimeMaxStr);
			}
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("startTime:" + createTimeMinL + " endTime:" + createTimeMaxL);
		}
		
		BrandBusinessSearch brandBusinessSearch = new BrandBusinessSearch();
		brandBusinessSearch.setId(id);
		brandBusinessSearch.setBusinessNumber(businessNumber);
		brandBusinessSearch.setPhoneNumber(phone);
		brandBusinessSearch.setCompanyName(companyName);
		brandBusinessSearch.setStatus(status);
		brandBusinessSearch.setStoreNumberMin(storeNumberMin);
		brandBusinessSearch.setStoreNumberMax(storeNumberMax);
		brandBusinessSearch.setCreateTimeMin(createTimeMinL);
		brandBusinessSearch.setCreateTimeMax(createTimeMaxL);
		brandBusinessSearch.setCashIncomeMin(cashIncomeMin);
		brandBusinessSearch.setCashIncomeMax(cashIncomeMax);
		brandBusinessSearch.setAvailableBalanceMin(availableBalanceMin);
		brandBusinessSearch.setAvailableBalanceMax(availableBalanceMax);
		
		List<Map<String,Object>> list = brandBusinessService.search(brandBusinessSearch, queryResult);
		int count = brandBusinessService.searchCount(brandBusinessSearch);
		
		queryResult.setRecordCount(count);
		
		data.put("brandBusinessList", list);
		data.put("total", queryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	/**
	 * 添加供应商信息接口
	 * @param businessName
	 * @param status
	 * @param companyName
	 * @param storeNumber 门店数
	 * @param phoneNumber
	 * @param brandId
	 * @param businessAddress
	 * @param licenseNumber
	 * @param taxId
	 * @param legalPerson
	 * @param province
	 * @param city
	 * @param brandDescription
	 * @param brandLogo
	 * @param brandShowImgs
	 * @param settlementDate
	 * @param bankAccountName
	 * @param bankName
	 * @param bankAccountNo
	 * @param bankCardFlag
	 * @param alipayFlag
	 * @param alipayAccount
	 * @param alipayName
	 * @param idCardNumber
	 * @param lOWarehouseId
	 * @param minWithdrawal
	 * @param bond
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(@RequestParam("businessName") String businessName,@RequestParam("status") int status,
			                @RequestParam("companyName") String companyName,@RequestParam(value = "storeNumber",required = false,defaultValue = "0") int storeNumber,
			                @RequestParam(value = "phoneNumber",required = false,defaultValue = "") String phoneNumber,@RequestParam("brandId") long brandId,
			                @RequestParam(value="businessAddress",required = false) String businessAddress,@RequestParam(value = "licenseNumber",required=false) String licenseNumber,
			                @RequestParam(value="taxId",required = false) String taxId,@RequestParam(value = "legalPerson",required = false) String legalPerson,
			                @RequestParam("province") String province,@RequestParam("city") String city,
			                @RequestParam(value = "brandDescription",required = false) String brandDescription,@RequestParam(value = "brandLogo",required = false) String brandLogo,
			                @RequestParam(value = "brandShowImgs",required = false) String brandShowImgs,@RequestParam(value = "settlementDate",required = false,defaultValue = "1") int settlementDate,
			                @RequestParam(value = "bankAccountName",required = false) String bankAccountName, @RequestParam(value = "bankName",required= false) String bankName,
			                @RequestParam(value = "bankAccountNo",required = false) String bankAccountNo,@RequestParam(value = "bankCardFlag",required = false,defaultValue = "0") int bankCardFlag,
			                @RequestParam(value = "alipayFlag",required = false,defaultValue = "0") int alipayFlag, @RequestParam(value = "alipayAccount",required = false) String alipayAccount,
			                @RequestParam(value = "alipayName",required = false) String alipayName,@RequestParam(value = "idCardNumber",required = false) String idCardNumber,
			                @RequestParam("lOWarehouseId") long lOWarehouseId, @RequestParam("minWithdrawal") double minWithdrawal,
			                @RequestParam("bond") double bond,@RequestParam(value = "password",required = false,defaultValue = "") String password,
			                @RequestParam(value = "storeBusinessPhone",required = false,defaultValue = "") String storeBusinessPhone,
			HttpServletRequest request){
		
		JsonResponse jsonResponse = new JsonResponse();
		UserNew brandBusiness = new UserNew();
//		BrandBusiness brandBusiness = new BrandBusiness();
		brandBusiness.setBusinessName(businessName);
		brandBusiness.setStatus(status);
		brandBusiness.setCompanyName(companyName);
		brandBusiness.setStoreNumber(storeNumber);
		brandBusiness.setPhone(phoneNumber);
		brandBusiness.setBrandId(brandId);
		brandBusiness.setBusinessAddress(businessAddress);
		brandBusiness.setLicenseNumber(licenseNumber);
		brandBusiness.setTaxid(taxId);
		brandBusiness.setLegalPerson(legalPerson);
		brandBusiness.setProvince(province);
		brandBusiness.setCity(city);
		brandBusiness.setBrandDescription(brandDescription);
		brandBusiness.setBrandLogo(brandLogo);
		brandBusiness.setBrandShowImgs(brandShowImgs);
		brandBusiness.setSettlementDate(settlementDate);
		brandBusiness.setBankAccountName(bankAccountName);
		brandBusiness.setBankName(bankName);
		brandBusiness.setBankAccountNo(bankAccountNo);
		brandBusiness.setBankCardFlag(bankCardFlag);
		brandBusiness.setAlipayFlag(alipayFlag);
		brandBusiness.setAlipayName(alipayName);
		brandBusiness.setLowarehouseId(lOWarehouseId);
		brandBusiness.setBond(bond);
		brandBusiness.setAlipayAccount(alipayAccount);
		brandBusiness.setIdCardNumber(idCardNumber);
		brandBusiness.setMinWithdrawal(minWithdrawal);
		
		
		
		
		//判断商家名称是否存在
		if(checkBusinessName(brandBusiness.getBusinessName(),-1)){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("商家名称已存在");
		}
		
		//判断是否选择了银行卡、支付宝、微信钱包3种方式至少一项
		if(brandBusiness.getBankCardFlag()==0&&brandBusiness.getAlipayFlag()==0&&brandBusiness.getWeixinFlag()==0){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("银行卡、支付宝、微信钱包3种方式至少选择一项");
		}
		
		//判断品牌是否为0
		if(brandBusiness.getBrandId()==0){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("请选择关联的品牌");
		}
		
		//判断仓库是否为0
		if(brandBusiness.getLowarehouseId() == 0){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("请选择关联的仓库");
		}
		//判断仓库是否已经被选择
		if(checkLOWarehouseId(brandBusiness.getLowarehouseId(),-1)){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("该仓库已被关联");
		}
		//判断品牌是否已经被选择
		if(checkBrandId(brandBusiness.getBrandId(), -1)){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("该品牌已被关联");
		}
		
		/*if(!StringUtils.equals("1", brandBusiness.getCode())){
			//校验验证码
			if(!yunXinSmsService.verifyCode(brandBusiness.getPhoneNumber(), brandBusiness.getCode())){
				//校验失败
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("验证码错误");
			}
		}*/
		
		try {
			boolean success = brandBusinessService.add(brandBusiness,password,storeBusinessPhone);
			if(!success){
				
				throw new RuntimeException("品牌商家信息添加失败！");
			}
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
		
		
	}
	/**
	 * 检验品牌是否已经被选择
	 * @param brandId
	 * @param existId
	 * @return
	 */
	public boolean checkBrandId(long brandId, long existId){
		long id = brandBusinessDao.checkBrandId(brandId);
		if(id==existId){	//存在品牌的商家自己修改品牌
			return false;
		}
		return id>0?true : false;
	}
	/**
	 * 检验仓库是否已经被选择
	 * @param lOWarehouseId
	 * @return
	 */
	private boolean checkLOWarehouseId(long lOWarehouseId,long existId) {
		long id = brandBusinessDao.checkLOWarehouseId(lOWarehouseId);
		if(id==existId){	//存在仓库的商家自己修改品牌
			return false;
		}
		return id>0?true : false;
	}
	/**
	 * 编辑商家接口
	 * @param businessName 商家名称
	 * @param status  状态(1：启用 2：冻结 3：删除）
	 * @param companyName
	 * @param storeNumber
	 * @param phoneNumber
	 * @param brandId  关联品牌id
	 * @param businessAddress
	 * @param licenseNumber
	 * @param taxId
	 * @param legalPerson
	 * @param province
	 * @param city
	 * @param brandDescription
	 * @param brandLogo
	 * @param brandShowImgs   
	 * @param settlementDate  设置结算时间
	 * @param bankAccountName
	 * @param bankName
	 * @param bankAccountNo
	 * @param bankCardFlag
	 * @param alipayFlag
	 * @param alipayAccount
	 * @param alipayName    
	 * @param idCardNumber  身份证
	 * @param lOWarehouseId 仓库id
	 * @param minWithdrawal 最低提现额
	 * @param bond 保证金
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value="/edit" )
	@AdminOperationLog
	@ResponseBody
	public JsonResponse edit(@RequestParam("id") long id,
			                 @RequestParam("businessName") String businessName,@RequestParam("status") int status,
                             @RequestParam("companyName") String companyName,@RequestParam(value = "storeNumber",required = false,defaultValue = "0") int storeNumber,
                             @RequestParam(value = "phoneNumber",required = false,defaultValue = "") String phoneNumber,@RequestParam("brandId") long brandId,
                             @RequestParam(value="businessAddress",required = false) String businessAddress,@RequestParam(value = "licenseNumber",required=false) String licenseNumber,
                             @RequestParam(value="taxId",required = false) String taxId,@RequestParam(value = "legalPerson",required = false) String legalPerson,
                             @RequestParam("province") String province,@RequestParam("city") String city,
                             @RequestParam(value = "brandDescription",required = false) String brandDescription,@RequestParam(value = "brandLogo",required = false) String brandLogo,
                             @RequestParam(value = "brandShowImgs",required = false) String brandShowImgs,@RequestParam(value = "settlementDate",required = false,defaultValue = "1") int settlementDate,
                             @RequestParam(value = "bankAccountName",required = false) String bankAccountName, @RequestParam(value = "bankName",required= false) String bankName,
                             @RequestParam(value = "bankAccountNo",required = false) String bankAccountNo,@RequestParam(value = "bankCardFlag",required = false,defaultValue = "0") int bankCardFlag,
                             @RequestParam(value = "alipayFlag",required = false,defaultValue = "0") int alipayFlag, @RequestParam(value = "alipayAccount",required = false) String alipayAccount,
                             @RequestParam(value = "alipayName",required = false) String alipayName,@RequestParam(value = "idCardNumber",required = false) String idCardNumber,
                             @RequestParam(value = "lOWarehouseId",required = false,defaultValue = "0") long lOWarehouseId, @RequestParam("minWithdrawal") double minWithdrawal,
                             @RequestParam("bond") double bond,@RequestParam(value = "password",required = false,defaultValue = "") String password,
                             @RequestParam(value = "storeBusinessPhone",required = false,defaultValue = "") String storeBusinessPhone,
			HttpServletRequest request){
		
		JsonResponse jsonResponse = new JsonResponse();	
		UserNew brandBusiness = new UserNew();
//		BrandBusiness brandBusiness = new BrandBusiness();
		brandBusiness.setId(id);
		brandBusiness.setBusinessName(businessName);
		brandBusiness.setStatus(status);
		brandBusiness.setCompanyName(companyName);
		brandBusiness.setStoreNumber(storeNumber);
		brandBusiness.setPhone(phoneNumber);
		brandBusiness.setBrandId(brandId);
		brandBusiness.setBusinessAddress(businessAddress);
		brandBusiness.setLicenseNumber(licenseNumber);
		brandBusiness.setTaxid(taxId);
		brandBusiness.setLegalPerson(legalPerson);
		brandBusiness.setProvince(province);
		brandBusiness.setCity(city);
		brandBusiness.setBrandDescription(brandDescription);
		brandBusiness.setBrandLogo(brandLogo);
		brandBusiness.setBrandShowImgs(brandShowImgs);
		brandBusiness.setSettlementDate(settlementDate);
		brandBusiness.setBankAccountName(bankAccountName);
		brandBusiness.setBankName(bankName);
		brandBusiness.setBankAccountNo(bankAccountNo);
		brandBusiness.setBankCardFlag(bankCardFlag);
		brandBusiness.setAlipayFlag(alipayFlag);
		brandBusiness.setAlipayName(alipayName);
		brandBusiness.setLowarehouseId(lOWarehouseId);
		brandBusiness.setBond(bond);
		brandBusiness.setAlipayAccount(alipayAccount);
		brandBusiness.setIdCardNumber(idCardNumber);
		brandBusiness.setMinWithdrawal(minWithdrawal);
		//判断商家名称是否存在
		if(checkBusinessName(brandBusiness.getBusinessName(),brandBusiness.getId())){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("商家名称已存在");
		}
		
		//判断是否选择了银行卡、支付宝、微信钱包3种方式至少一项
		if(brandBusiness.getBankCardFlag()==0&&brandBusiness.getAlipayFlag()==0&&brandBusiness.getWeixinFlag()==0){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("银行卡、支付宝、微信钱包3种方式至少选择一项");
		}
		
		//判断品牌是否为0
		if(brandBusiness.getBrandId()==0){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("请选择关联的品牌");
		}
		//判断仓库是否为0
		if(brandBusiness.getLowarehouseId() == 0){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("请选择关联的仓库");
		}
		//判断仓库是否已经被选择
		if(checkLOWarehouseId(brandBusiness.getLowarehouseId(),brandBusiness.getId())){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("该仓库已被关联");
		}
		//判断品牌是否已经被选择
		
		if(checkBrandId(brandBusiness.getBrandId(), brandBusiness.getId())){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("该品牌已被关联");
		}
		
		/*if(!StringUtils.equals("1", brandBusiness.getCode())){
			//校验验证码
			if(!yunXinSmsService.verifyCode(brandBusiness.getPhoneNumber(), brandBusiness.getCode())){
				//校验失败
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("验证码错误");
			}
			
		}*/
		
		try {
			brandBusinessService.updateBrandBusiness(brandBusiness,password,storeBusinessPhone);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError(e.getMessage());
		}
		
		return jsonResponse.setSuccessful();
	}
	/**
	 * 初始化密码，随机密码并发送短信通知
	 * 3120104  您好，您的账户密码已初始化为%s，为确保后续使用安全，请及时登录“俞姐姐门店宝”供应端管理系统修改密码。
	 * @param phone
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/initPassword")
	@ResponseBody
	public JsonResponse initPassword(@RequestParam(value = "phone",required = false,defaultValue = "") String phone,
			                         @RequestParam("id") long id){
		JsonResponse jsonResponse = new JsonResponse();
		boolean sendResult = false;
		try {
			sendResult = brandBusinessService.sendInitPassword(phone,id);
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
		if(sendResult){
			return jsonResponse.setSuccessful();
		}else{
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("短信发送失败");
		}
	}
	
	@RequestMapping(value="/sendcode")
	@ResponseBody
	public JsonResponse sendStoreBusinessCode(@RequestParam(value="phone")String phone){
		JsonResponse jsonResponse = new JsonResponse();
		boolean sendResult = yunXinSmsService.sendCode(phone, 3);
		if(sendResult){
			return jsonResponse.setSuccessful();
		}else {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("短信发送失败");
		}
		
	}
	
	@RequestMapping(value="/fetchprovince")
	@ResponseBody
	public JsonResponse fetchProvince(){
		JsonResponse jsonResponse = new JsonResponse();
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<Province> provinceList = brandBusinessService.getProvinceList();
		data.put("provinceList", provinceList);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/fetchcity")
	@ResponseBody
	public JsonResponse fetchProvince(@RequestParam(value="p")long parentId){
		JsonResponse jsonResponse = new JsonResponse();
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<City> cityList = brandBusinessService.getCitysByProvinceId(parentId);
		data.put("cityList", cityList);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/fetchbrand")
	@ResponseBody
	public JsonResponse fetchBrand(){
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<BrandLogo> brandList = brandBusinessService.getBrandListWithClothNumberPrefix();
		
		data.put("brandList", brandList);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	
	/**
	 * 检验品牌商家名称是否存在
	 * @param businessName
	 * @return	true 已存在	false 不存在
	 */
	public boolean checkBusinessName(String businessName ,long existId){
		long id = brandBusinessDao.checkBusinessName(businessName);
		if(id==existId){	//存在名称的商家自己修改名称
			return false;
		}
		return id>0?true : false;
	}
	
	
}
