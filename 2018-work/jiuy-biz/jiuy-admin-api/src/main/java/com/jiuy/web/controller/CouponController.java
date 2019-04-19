package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.business.facade.CouponFacade;
import com.jiuy.core.dao.CouponTemplateDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.coupon.Coupon;
import com.jiuy.core.meta.coupon.CouponTemplate;
import com.jiuy.core.meta.coupon.CouponTemplateVO;
import com.jiuy.core.meta.coupon.CouponVO;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.coupon.CouponService;
import com.jiuy.core.service.coupon.CouponTemplateService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.GlobalSetting;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 优惠券
 */
@RequestMapping("/coupon")
@Controller
@AdminOperationLog
public class CouponController {
	private static final int OPEN = 1;
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private CouponTemplateService couponTemplateService;
	
	@Autowired
	private CouponFacade couponFacade;
	
	@Autowired
	private CouponTemplateDao couponTemplateDao;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@RequestMapping("/template/search")
	@ResponseBody
	public JsonResponse searchTemplate(PageQuery pageQuery,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "range_type", required = false) Integer rangeType,
			@RequestParam(value = "limit", required = false) Integer isLimit,
			@RequestParam(value = "publish_count_min", required = false) Integer publishCountMin,
			@RequestParam(value = "publish_count_max", required = false) Integer publishCountMax,
			@RequestParam(value = "money_min", required = false) Double moneyMin,
			@RequestParam(value = "money_max", required = false) Double moneyMax,
			@RequestParam(value = "validity_start_time", required = false) Long validityStartTime,
			@RequestParam(value = "validity_end_time", required = false) Long validityEndTime) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		CouponTemplate couponTemplate = new CouponTemplate(id, name, type, rangeType, validityStartTime, validityEndTime, isLimit, null, null, null, null);
		
        int totalCount =
            couponTemplateService.searchCount(couponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	
        data.put("total", pageQueryResult);

		List<CouponTemplateVO> list = couponFacade.search(pageQuery, couponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax);
		data.put("list", list);

		data.put("coupon_money_info", globalSettingService.getJsonObject(GlobalSettingName.COUPON));
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/templatevo/{id}")
	@ResponseBody
	public JsonResponse template(@PathVariable(value = "id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		CouponTemplateVO couponTemplateVO = null;
		
		try {
			couponTemplateVO = couponFacade.loadVOInfo(id);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		data.put("info", couponTemplateVO);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/template/add")
	@ResponseBody
	public JsonResponse addTemplate(@RequestParam(value = "name") String name,
			@RequestParam(value = "type") Integer type,
			@RequestParam(value = "range_type") Integer rangeType,
			@RequestParam(value = "money") Double money,
			@RequestParam(value = "limit") Integer isLimit,
			@RequestParam(value = "validity_start_time") Long validityStartTime,
			@RequestParam(value = "validity_end_time") Long validityEndTime,
			@RequestParam(value = "range", required = false) String rangeContent,
			@RequestParam(value = "coexist", required = false) Integer coexist,
			@RequestParam(value = "limit_money", required = false, defaultValue = "0") Double limitMoney,
			@RequestParam(value = "exchangeJiuCoinSetting", required = false, defaultValue = "0")int exchangeJiuCoinSetting,
			@RequestParam(value = "exchangeJiuCoinCost", required = false, defaultValue = "0")int exchangeJiuCoinCost,
			@RequestParam(value = "exchangeLimitTotalCount", required = false, defaultValue = "0")int exchangeLimitTotalCount,
			@RequestParam(value = "exchangeLimitSingleCount", required = false, defaultValue = "0")int exchangeLimitSingleCount,
			@RequestParam(value = "exchangeStartTime", required = false, defaultValue = "0")long exchangeStartTime,
			@RequestParam(value = "exchangeEndTime", required = false, defaultValue = "0")long exchangeEndTime,
			@RequestParam(value = "promotionJiuCoinSetting", required = false, defaultValue = "0")int promotionJiuCoinSetting,
			@RequestParam(value = "promotionJiuCoin", required = false, defaultValue = "0")int promotionJiuCoin,
			@RequestParam(value = "promotionStartTime", required = false, defaultValue = "0")long promotionStartTime,
			@RequestParam(value = "promotionEndTime", required = false, defaultValue = "0")long promotionEndTime) {
		JsonResponse jsonResponse = new JsonResponse();
		
		//CouponTemplate couponTemplate = new CouponTemplate(null, name, type, rangeType, validityStartTime, validityEndTime, isLimit, money, rangeContent, coexist, limitMoney);
		CouponTemplate couponTemplate = new CouponTemplate(null, name, type, money, rangeType, rangeContent, validityStartTime, validityEndTime, isLimit, coexist, limitMoney, exchangeJiuCoinSetting, exchangeJiuCoinCost, exchangeLimitTotalCount, exchangeLimitSingleCount, exchangeStartTime, exchangeEndTime, promotionJiuCoinSetting, promotionJiuCoin, promotionStartTime, promotionEndTime);
		
		couponTemplateService.add(couponTemplate);
        
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/template/update")
	@ResponseBody
	public JsonResponse updateTemplate(@RequestParam("id") Long id,
			@RequestParam("name") String name,
			@RequestParam("type") Integer type,
			@RequestParam("range_type") Integer rangeType,
			@RequestParam("money") Double money,
			@RequestParam("limit") Integer isLimit,
			@RequestParam("validity_start_time") Long validityStartTime,
			@RequestParam("validity_end_time") Long validityEndTime,
			@RequestParam(value = "range", required = false) String rangeContent,
			@RequestParam(value = "coexist", required = false) Integer coexist,
			@RequestParam(value = "limit_money", required = false, defaultValue = "0") Double limitMoney,
			@RequestParam(value = "exchangeJiuCoinSetting", required = false, defaultValue = "0")int exchangeJiuCoinSetting,
			@RequestParam(value = "exchangeJiuCoinCost", required = false, defaultValue = "0")int exchangeJiuCoinCost,
			@RequestParam(value = "exchangeLimitTotalCount", required = false, defaultValue = "0")int exchangeLimitTotalCount,
			@RequestParam(value = "exchangeLimitSingleCount", required = false, defaultValue = "0")int exchangeLimitSingleCount,
			@RequestParam(value = "exchangeStartTime", required = false, defaultValue = "0")long exchangeStartTime,
			@RequestParam(value = "exchangeEndTime", required = false, defaultValue = "0")long exchangeEndTime,
			@RequestParam(value = "promotionJiuCoinSetting", required = false, defaultValue = "0")int promotionJiuCoinSetting,
			@RequestParam(value = "promotionJiuCoin", required = false, defaultValue = "0")int promotionJiuCoin,
			@RequestParam(value = "promotionStartTime", required = false, defaultValue = "0")long promotionStartTime,
			@RequestParam(value = "promotionEndTime", required = false, defaultValue = "0")long promotionEndTime) {
		JsonResponse jsonResponse = new JsonResponse();
		
		//CouponTemplate couponTemplate = new CouponTemplate(id, name, type, rangeType, validityStartTime, validityEndTime, isLimit, money, rangeContent, coexist, limitMoney);
		CouponTemplate couponTemplate = new CouponTemplate(id, name, type, money, rangeType, rangeContent, validityStartTime, validityEndTime, isLimit, coexist, limitMoney, exchangeJiuCoinSetting, exchangeJiuCoinCost, exchangeLimitTotalCount, exchangeLimitSingleCount, exchangeStartTime, exchangeEndTime, promotionJiuCoinSetting, promotionJiuCoin, promotionStartTime, promotionEndTime);
		
		try {
			couponTemplateService.update(couponTemplate);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
        
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/template/delete")
	@ResponseBody
	public JsonResponse updateTemplate(@RequestParam("id") Long id,
									   @RequestParam(value = "status") Integer status) {
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			couponTemplateService.update(id, status);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
        
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 发行
	 * @param id
	 * @param money
	 * @param publishCount
	 * @return
	 */
	@RequestMapping("/template/publish")
	@ResponseBody
	public JsonResponse publishTemplate(HttpSession httpSession,
			@RequestParam(value = "id") Long id,
			@RequestParam(value = "money") Double money,
			@RequestParam(value = "publish_count") Integer publishCount) {
		JsonResponse jsonResponse = new JsonResponse();
		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		long adminId = adminUser.getUserId();
		
		try {
			couponFacade.updateTemplate(id, money, publishCount, adminId);
        } catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
        
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
    /**
     * 发放
     * 
     * @param id 代金券指定发放下传,id为代金券id
     * @param templateId 代金券模板id
     * @param money 面值
     * @param yjjNumbers "111,222,333" 用逗号隔开
     * @param type 0:指定用户(俞姐姐号), 1:所有用户, 2:排除用户, 3:指定手机用户
     * @param count 代金券指定发放下，传1。
     * @param pushStatus -1:不推，0:待推送
     */
	@RequestMapping("/grant")
	@ResponseBody
    public JsonResponse grant(HttpSession httpSession,
							  @RequestParam(value = "id", required = false) Long id,
                              @RequestParam("template_id") Long templateId, 
    						  @RequestParam("money") Double money,
                              @RequestParam("yjj_numbers") String yjjNumbers,
							  @RequestParam("type") int type,
							  @RequestParam("count") int count,
							  @RequestParam("push_status") Integer pushStatus,
							  @RequestParam(value = "push_title", required = false) String pushTitle,
							  @RequestParam(value = "push_description", required = false) String pushDescription,
							  @RequestParam(value = "push_url", required = false) String pushUrl,
							  @RequestParam(value = "push_image", required = false) String pushImage) throws Exception{
		JsonResponse jsonResponse = new JsonResponse();
		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		long adminId = adminUser.getUserId();
		Map<String, Object> data = null;
        try {
        	
        	data = couponFacade.grant(id, templateId, money, yjjNumbers, type, count, pushStatus, pushTitle, pushDescription,
                pushUrl, pushImage, adminId);
        } catch (ParameterErrorException e) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
        }
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	/**
	 * 
	 * @param pageQuery
	 * @param name
	 * @param type 0 : 代金券
	 * @param rangeType 范围类型 0: 通用, 1:分类, 2:限额订单, 3:限定商品, 4:品牌
	 * @param isLimit 优惠限制 0:无 1:有
	 * @param moneyMin
	 * @param moneyMax
	 * @param validityStartTime
	 * @param validityEndTime
	 * @param status -2：过期 -1:作废  0:未用 1:已用  2:已发放 3:未发放
	 * @param yjjNumber
	 * @param code
	 * @return
	 */
	@RequestMapping("/search")
	@ResponseBody
	public JsonResponse search(PageQuery pageQuery,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "range_type", required = false) Integer rangeType,
			@RequestParam(value = "limit", required = false) Integer isLimit,
			@RequestParam(value = "money_min", required = false) Double moneyMin,
			@RequestParam(value = "money_max", required = false) Double moneyMax,
			@RequestParam(value = "validity_start_time", required = false) Long validityStartTime,
			@RequestParam(value = "validity_end_time", required = false) Long validityEndTime,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "yjj_number", required = false) Long yjjNumber,
			@RequestParam(value = "code", required = false) String code) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
        Coupon coupon =
            new Coupon(null, code, name, type, rangeType, validityStartTime, validityEndTime, isLimit, yjjNumber, status);
		
        int totalCount = couponService.searchCount(coupon, moneyMin, moneyMax);

        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);

        List<CouponVO> list = couponFacade.search(pageQuery, coupon, moneyMin, moneyMax);
		
        data.put("total", pageQueryResult);
        data.put("list", list);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 
	 * @param id
	 * @param status -1:作废  0:未用 1:已用 
	 * @return
	 */
    @RequestMapping("/update")
	@ResponseBody
	public JsonResponse update(@RequestParam("id") Long id,
                               @RequestParam(value = "status", required = false) Integer status) {
	    JsonResponse jsonResponse = new JsonResponse();

	    try {
	    	couponService.update(id, status);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}

        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
    
    @RequestMapping("/template/{id}")
    @ResponseBody
    public JsonResponse templateDescription(@PathVariable("id") Long id, 
    		@RequestParam(value = "start_time", required = false) Long startTime,
    		@RequestParam(value = "end_time", required = false) Long endTime) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<>();
    	
    	CouponTemplate couponTemplate = couponTemplateDao.search(id);
    	data.put("couponTemplate", couponTemplate);
    	data.put("start_time", startTime);
    	data.put("end_time", endTime);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/global/restriction")
    @ResponseBody
    public JsonResponse restrictionSetting() {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<>();
    	
    	data.put("coupon_limit_set", globalSettingService.getJsonObject(GlobalSettingName.COUPON_LIMIT_SET));
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/global/specify/restriction")
    @ResponseBody
    public JsonResponse specifyRestrictionSetting() {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<>();
    	
    	data.put("specify_coupon_limit_set", globalSettingService.getJsonObject(GlobalSettingName.SPECIFY_COUPON_LIMIT_SET));
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping("/template/jiucoinmall/search")
	@ResponseBody
	public JsonResponse searchJiuCoinMallTemplate(PageQuery pageQuery,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "range_type", required = false) Integer rangeType,
			@RequestParam(value = "limit", required = false) Integer isLimit,
			@RequestParam(value = "publish_count_min", required = false) Integer publishCountMin,
			@RequestParam(value = "publish_count_max", required = false) Integer publishCountMax,
			@RequestParam(value = "money_min", required = false) Double moneyMin,
			@RequestParam(value = "money_max", required = false) Double moneyMax,
			@RequestParam(value = "validity_start_time", required = false) Long validityStartTime,
			@RequestParam(value = "validity_end_time", required = false) Long validityEndTime) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		CouponTemplate couponTemplate = new CouponTemplate(id, name, type, rangeType, validityStartTime, validityEndTime, isLimit, null, null, null, null);
		
        int totalCount =
            couponTemplateService.searchCount(couponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	
        data.put("total", pageQueryResult);

		List<CouponTemplateVO> list = couponFacade.search(pageQuery, couponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax);
		
		for(CouponTemplateVO couponTemplateVO : list){
			if(couponTemplateVO.getExchangeJiuCoinSetting()==0){
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("该优惠券无法使用玖币兑换");
			}
		}
		
		data.put("list", list);

		data.put("coupon_money_info", globalSettingService.getJsonObject(GlobalSettingName.COUPON));
		
		return jsonResponse.setSuccessful().setData(data);
	}
    
    /**
     * 获取当前首次登录门店APP优惠券提示设置
     */
    @RequestMapping("/getSettingOfAppFirstLoginCoupon")
    @ResponseBody
    public JsonResponse getSettingOfAppFirstLoginCoupon(){
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,Object> data = new HashMap<String,Object>();
    	JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.NEW_USER_COUPON);
    	String couponImage = ((JSONObject)jsonArray.get(0)).getString("couponImage");
    	boolean flag = ((JSONObject)jsonArray.get(0)).getBoolean("switch");
    	data.put("couponImage", couponImage);
    	data.put("switch", flag);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 编辑当前首次登录门店APP优惠券提示设置
     * @param propertyValue 
     * 
     */
    @RequestMapping("/updateSettingOfAppFirstLoginCoupon")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse updateSettingOfAppFirstLoginCoupon(@RequestParam("propertyValue") String propertyValue
    		                                               ){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		globalSettingService.update(GlobalSettingName.NEW_USER_COUPON.getStringValue(), propertyValue);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
    }
    
}
