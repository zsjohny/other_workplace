package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.StoreCouponFacade;
import com.jiuy.core.dao.StoreCouponTemplateDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.coupon.StoreCouponTemplateVO;
import com.jiuy.core.meta.coupon.StoreCouponVO;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.core.service.coupon.StoreCouponService;
import com.jiuy.core.service.coupon.StoreCouponTemplateService;
import com.jiuy.core.service.member.StoreBusinessService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.coupon.PublishObjectType;
import com.jiuyuan.constant.coupon.StoreCoupon;
import com.jiuyuan.constant.coupon.StoreCouponTemplate;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.IBrandNewService;
import com.jiuyuan.util.IdsToStringUtil;
import com.jiuyuan.util.anno.AdminOperationLog;

import com.jiuyuan.web.help.JsonResponse;
/**
 * 优惠券
 */
@RequestMapping("/store/coupon")
@Controller
@AdminOperationLog
public class StoreCouponController {
	
	private static final int BRAND_COUPON = 5;
	
	private static final double MIN_AVAILABLE_MONEY = 1;
	
	private static final double MAX_AVAILABLE_MONEY = 10000;
	
	private static final long ONE_YEAR = 365L*24*60*60*1000;
	
	private static final double ZERO = 0;
	
	private static final double ONE_MILLION = 1000000;
	
	@Autowired
	private StoreCouponService storeCouponService;
	
	@Autowired
	private StoreCouponTemplateService storeCouponTemplateService;
	
	@Autowired
	private StoreCouponFacade storeCouponFacade;
	
	@Autowired
	private StoreCouponTemplateDao storeCouponTemplateDao;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private StoreBusinessService storeBusinessService;
	
	@Autowired
	private IBrandNewService brandNewService;

	/**
	 * 获取优惠券模板列表
	 * @param pageQuery
	 * @param id 模板ID
	 * @param name 模板名称
	 * @param type 类型：0  代金券 
	 * @param rangeType 范围类型 0: 通用, 1:分类, 2:限额订单, 3:限定商品, 4:免邮, 5:品牌
	 * @param isLimit 优惠限制 0:无 1:有
	 * @param publishCountMin 发行量下限
	 * @param publishCountMax 发行量上限
	 * @param limitMoneyMin 订单限额
	 * @param limitMoneyMax 订单限额
	 * @param moneyMin 面值下限
	 * @param moneyMax 面值上限
	 * @param validityStartTime 有效期起始时间
	 * @param validityEndTime 有效期截止时间
	 * @return
	 */
	@RequestMapping("/template/search")
	@ResponseBody
	public JsonResponse searchTemplate(PageQuery pageQuery,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false,defaultValue = "0") Integer type,
			@RequestParam(value = "range_type", required = false) Integer rangeType,
			@RequestParam(value = "range_type_ids" , required = false, defaultValue ="") String rangeTypeIds,
			@RequestParam(value = "limit", required = false) Integer isLimit,
			@RequestParam(value = "publish_count_min", required = false) Integer publishCountMin,
			@RequestParam(value = "publish_count_max", required = false) Integer publishCountMax,
			@RequestParam(value = "limit_money_min", required = false) Double limitMoneyMin,
			@RequestParam(value = "limit_money_max", required = false) Double limitMoneyMax,
			@RequestParam(value = "money_min", required = false) Double moneyMin,
			@RequestParam(value = "money_max", required = false) Double moneyMax,
			@RequestParam(value = "validity_start_time", required = false) Long validityStartTime,
			@RequestParam(value = "validity_end_time", required = false) Long validityEndTime) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		StoreCouponTemplate storeCouponTemplate = new StoreCouponTemplate(id, name, type, rangeType, validityStartTime, validityEndTime, isLimit, null, null, null, null);
		List<String> ids=IdsToStringUtil.getIdsToList(rangeTypeIds);
		
        int totalCount =
            storeCouponTemplateService.searchCount(storeCouponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax,limitMoneyMin,limitMoneyMax,ids);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	
        data.put("total", pageQueryResult);

		List<StoreCouponTemplateVO> list = storeCouponFacade.search(pageQuery, storeCouponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax,limitMoneyMin,limitMoneyMax,ids);
		data.put("list", list);

		data.put("coupon_money_info", globalSettingService.getJsonObject(GlobalSettingName.STORE_COUPON));
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/templatevo/{id}")
	@ResponseBody
	public JsonResponse template(@PathVariable(value = "id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		StoreCouponTemplateVO storeCouponTemplateVO = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			map= storeCouponFacade.loadVOInfo(id);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		data.put("info", map);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 添加优惠券模板
	 * @param name
	 * @param type 0:代金券
	 * @param rangeType 范围类型 0: 通用, 1:分类, 2:限额订单, 3:限定商品, 4:免邮, 5:品牌
	 * @param money 面值
	 * @param ids ids字符串
	 * @param isLimit 
	 * @param validityStartTime
	 * @param validityEndTime
	 * @param rangeContent
	 * @param coexist
	 * @param limitMoney
	 * @param exchangeJiuCoinSetting
	 * @param exchangeJiuCoinCost
	 * @param exchangeLimitTotalCount
	 * @param exchangeLimitSingleCount
	 * @param exchangeStartTime
	 * @param exchangeEndTime
	 * @param promotionJiuCoinSetting
	 * @param promotionJiuCoin
	 * @param promotionStartTime
	 * @param promotionEndTime
	 * @return
	 */
	@RequestMapping("/template/add")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addTemplate(@RequestParam(value = "name") String name,
			@RequestParam(value = "type") Integer type,
			@RequestParam(value = "range_type") Integer rangeType,
			@RequestParam(value = "ids",required = false,defaultValue = "") String ids,
			@RequestParam(value = "money") double money,
			@RequestParam(value = "limit") Integer isLimit,
			@RequestParam(value = "validity_start_time") Long validityStartTime,
			@RequestParam(value = "validity_end_time") Long validityEndTime,
			@RequestParam(value = "range", required = false) String rangeContent,
			@RequestParam(value = "coexist", required = false) Integer coexist,
			@RequestParam(value = "limit_money", required = false, defaultValue = "0") double limitMoney,
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
		ids = IdsToStringUtil.getIdsToString(ids);
		List<String> idList = IdsToStringUtil.getIdsToListNoKomma(ids);
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//验证该优惠券
			if(money < MIN_AVAILABLE_MONEY || money> MAX_AVAILABLE_MONEY ){
				throw new RuntimeException("优惠券面值无效");
			}
			if(money != Math.round(money)){
				throw new RuntimeException("优惠券面值必须为整数");
			}
			if(validityStartTime<System.currentTimeMillis()){
				throw new RuntimeException("无效开始时间");
			}
			if(validityEndTime<System.currentTimeMillis()){
				throw new RuntimeException("无效结束时间");
			}
			if(validityEndTime>System.currentTimeMillis()+ONE_YEAR){
				throw new RuntimeException("优惠券有效期最长为1年");
			}
			if(limitMoney < ZERO){
				throw new RuntimeException("订单限额必须>=0");
			}
			if(limitMoney >ONE_MILLION){
				throw new RuntimeException("订单限额不能超过100万");
			}
			if(limitMoney != Math.round(limitMoney)){
				throw new RuntimeException("订单限额必须为整数");
			}
			String names = "";
			if(rangeType ==BRAND_COUPON){
				if(idList == null||idList.size()==0){
					throw new RuntimeException("添加品牌优惠券必须添加品牌ID！");
				}
				//先进行搜索品牌ID,判断是否存在,当输入的品牌ID全部存在返回true
				map = brandNewService.CheckBrandList(idList);
				if(!(boolean)map.get("equal")){
					return jsonResponse.setError("品牌ID输入不正确").setData(map);
				}
				names = IdsToStringUtil.SetToString((Set<String>)map.get("rightNameSet"));
			}
			StoreCouponTemplate storeCouponTemplate = new StoreCouponTemplate(null, name, type, money, rangeType, rangeContent, validityStartTime, validityEndTime, isLimit, coexist, limitMoney, exchangeJiuCoinSetting, exchangeJiuCoinCost, exchangeLimitTotalCount, exchangeLimitSingleCount, exchangeStartTime, exchangeEndTime, promotionJiuCoinSetting, promotionJiuCoin, promotionStartTime, promotionEndTime);
			storeCouponTemplate.setRangeTypeIds(ids);
			storeCouponTemplate.setRangeTypeNames(names);
			int i = storeCouponTemplateService.add(storeCouponTemplate);
			if(i == -1){
				throw new RuntimeException("优惠券模板生成失败！");
			}
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
		return jsonResponse.setSuccessful();
	}
	
	@RequestMapping("/template/update")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateTemplate(@RequestParam("id") Long id,
			@RequestParam("name") String name,
			@RequestParam(value="type",required = false, defaultValue = "0") Integer type,
			@RequestParam("range_type") Integer rangeType,
			@RequestParam(value = "ids",required = false,defaultValue = "") String ids,
			@RequestParam("money") double money,
			@RequestParam("isLimit") Integer isLimit,
			@RequestParam("validity_start_time") Long validityStartTime,
			@RequestParam("validity_end_time") Long validityEndTime,
			@RequestParam(value = "range", required = false) String rangeContent,
			@RequestParam(value = "coexist", required = false) Integer coexist,
			@RequestParam(value = "limit_money", required = false, defaultValue = "0") double limitMoney,
			@RequestParam(value = "exchangeJiuCoinSetting", required = false, defaultValue = "0")int exchangeJiuCoinSetting,
			@RequestParam(value = "exchangeJiuCoinCost", required = false, defaultValue = "0")int exchangeJiuCoinCost,
			@RequestParam(value = "exchangeLimitTotalCount", required = false, defaultValue = "0")int exchangeLimitTotalCount,
			@RequestParam(value = "exchangeLimitSingleCount", required = false, defaultValue = "0")int exchangeLimitSingleCount,
			@RequestParam(value = "exchangeStartTime", required = false, defaultValue = "0")long exchangeStartTime,
			@RequestParam(value = "exchangeEndTime", required = false, defaultValue = "0")long exchangeEndTime,
			@RequestParam(value = "promotionJiuCoinSetting", required = false, defaultValue = "0")int promotionJiuCoinSetting,
			@RequestParam(value = "promotionJiuCoin", required = false, defaultValue = "0")int promotionJiuCoin,
			@RequestParam(value = "promotionStartTime", required = false, defaultValue = "0")long promotionStartTime,
			@RequestParam(value = "promotionEndTime", required = false, defaultValue = "0")long promotionEndTime
			) {
		JsonResponse jsonResponse = new JsonResponse();
		ids = IdsToStringUtil.getIdsToString(ids);
		List<String> idList = IdsToStringUtil.getIdsToListNoKomma(ids);
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//验证该优惠券
			if(money < MIN_AVAILABLE_MONEY || money> MAX_AVAILABLE_MONEY ){
				throw new RuntimeException("优惠券面值无效");
			}
			if(money != Math.round(money)){
				throw new RuntimeException("优惠券面值必须为整数");
			}
			if(validityStartTime<System.currentTimeMillis()){
				throw new RuntimeException("无效开始时间");
			}
			if(validityEndTime<System.currentTimeMillis()){
				throw new RuntimeException("无效结束时间");
			}
			if(validityEndTime>System.currentTimeMillis()+ONE_YEAR){
				throw new RuntimeException("优惠券有效期最长为1年");
			}
			if(limitMoney < ZERO){
				throw new RuntimeException("订单限额必须>=0");
			}
			if(limitMoney >ONE_MILLION){
				throw new RuntimeException("订单限额不能超过100万");
			}
			if(limitMoney != Math.round(limitMoney)){
				throw new RuntimeException("订单限额必须为整数");
			}
			String names ="";
			if(rangeType ==BRAND_COUPON){
				if(idList == null||idList.size()==0){
					throw new RuntimeException("添加品牌优惠券必须添加品牌ID！");
				}
				//先进行搜索品牌ID,判断是否存在,当输入的品牌ID全部存在返回true
				map = brandNewService.CheckBrandList(idList);
				if(!(boolean)map.get("equal")){
					return jsonResponse.setError("品牌ID输入不正确").setData(map);
				}
				names = IdsToStringUtil.SetToString((Set<String>)map.get("rightNameSet"));
			}
			//获取发行模板情况
			StoreCouponTemplateVO storeCouponTemplateVO = null;
			Map<String,Object> storeCouponTemplateVOMap= storeCouponFacade.loadVOInfo(id);
			storeCouponTemplateVO =(StoreCouponTemplateVO) storeCouponTemplateVOMap.get("storeCouponTemplateVO");
			if(storeCouponTemplateVO.getUsedCount() > 0 || storeCouponTemplateVO.getPublishCount() > 0 || storeCouponTemplateVO.getGrantCount() > 0){
				throw new RuntimeException("不能编辑该代金券模板,该代金券模板正在使用！");
			}
			
			StoreCouponTemplate storeCouponTemplate = new StoreCouponTemplate(id, name, type, money, rangeType, rangeContent, validityStartTime, validityEndTime, isLimit, coexist, limitMoney, exchangeJiuCoinSetting, exchangeJiuCoinCost, exchangeLimitTotalCount, exchangeLimitSingleCount, exchangeStartTime, exchangeEndTime, promotionJiuCoinSetting, promotionJiuCoin, promotionStartTime, promotionEndTime);
			storeCouponTemplate.setRangeTypeIds(ids);
			storeCouponTemplate.setRangeTypeNames(names);
			int i = storeCouponTemplateService.update(storeCouponTemplate);
			if(i == -1){
				throw new RuntimeException("优惠券模板编辑失败！");
			}
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/template/delete")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateTemplate(@RequestParam("id") Long id,
									   @RequestParam(value = "status") Integer status) {
		JsonResponse jsonResponse = new JsonResponse();
		
		StoreCouponTemplateVO storeCouponTemplateVO = null;		
		try {
			Map<String,Object> storeCouponTemplateVOMap= storeCouponFacade.loadVOInfo(id);
			storeCouponTemplateVO =(StoreCouponTemplateVO) storeCouponTemplateVOMap.get("storeCouponTemplateVO");
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		if(storeCouponTemplateVO.getUsedCount() > 0 || storeCouponTemplateVO.getPublishCount() > 0 || storeCouponTemplateVO.getGrantCount() > 0){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("不能删除该代金券模板");
		}else{						
			try {
				storeCouponTemplateService.update(id, status);
			} catch (ParameterErrorException e) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
			}
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
	@AdminOperationLog
	public JsonResponse publishTemplate(HttpSession httpSession,
			@RequestParam(value = "id") Long id,
			@RequestParam(value = "money") Double money,
			@RequestParam(value = "publish_count") Integer publishCount) {
		JsonResponse jsonResponse = new JsonResponse();
		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		long adminId = adminUser.getUserId();
		
		try {
			storeCouponFacade.updateTemplate(id, money, publishCount, adminId);
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
     * @param type 0:指定用户(俞姐姐号,商家号), 1:所有用户, 3:注册手机用户(手机号码)
     * @param count 代金券指定发放下，传1。
     */
	@RequestMapping("/grant")
	@ResponseBody
	@AdminOperationLog
    public JsonResponse grant(HttpSession httpSession,
							  @RequestParam(value = "id", required = false) Long id,
                              @RequestParam("template_id") Long templateId, 
    						  @RequestParam("money") Double money,
    						  @RequestParam(value = "yjj_numbers",required = false,defaultValue = "") String yjjNumbers,
                              //@RequestParam("publish_object_numbers") String publishObjectNumbers,
							  @RequestParam("type") int type,
							  @RequestParam("count") int count,
							//  @RequestParam("push_status") Integer pushStatus,
							  @RequestParam(value = "push_title", required = false) String pushTitle,
							  @RequestParam(value = "push_description", required = false) String pushDescription,
							  @RequestParam(value = "push_url", required = false) String pushUrl,
							  @RequestParam(value = "push_image", required = false) String pushImage
							  ) throws Exception{
		JsonResponse jsonResponse = new JsonResponse();
		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		long adminId = adminUser.getUserId();
		Map<String, Object> data = null;
		List<String> publishObjectNumbersList = IdsToStringUtil.getIdsToListNoKomma(yjjNumbers);
		Map<String,Object> map = new HashMap<String,Object>();
        try {
         	if(type != PublishObjectType.ALL_STOREBUSINESS.getValue()){
         		//如果是发放给指定用户
        		map = storeCouponService.checkPublishObjectNumbers(publishObjectNumbersList,type);
        		if(!(boolean)map.get("equal")){
        			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("发放对象输入有误").setData(map);
        		}
        		//组装电话号码
        		Set<String> rightNumberSet =(Set<String>)map.get("rightNumberSet");
        		Iterator<String> iterator = rightNumberSet.iterator();
        		List<String> rightNumberlist = new ArrayList<>();
        		while(iterator.hasNext()){
        			rightNumberlist.add(iterator.next());
        		}
        		// 发放 必备参数 模版id, 发送对象的电话号码, 发放数量, 发放类型
        		data = storeCouponFacade.grant(id, templateId, money, rightNumberlist, type, count, 0, pushTitle, pushDescription,
        				pushUrl, pushImage, adminId);
        	}
        	if(type == PublishObjectType.ALL_STOREBUSINESS.getValue()){
         		// 发放给所有用户
        		data = storeCouponFacade.grant(id, templateId, money, null, type, count, 0, pushTitle, pushDescription, pushUrl, pushImage, adminId);
        	}
        } catch (ParameterErrorException e) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
        }
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	/**
	 * 
	 * @param pageQuery
	 * @param name 模板名称 
	 * @param type 0 : 代金券
	 * @param rangeType 范围类型 0: 通用, 1:分类, 2:限额订单, 3:限定商品, 4:免邮, 5:品牌
	 * @param isLimit 优惠限制 0:无 1:有
	 * @param moneyMin 面值下限
	 * @param moneyMax 面值上限
	 * @param limitMoneyMin 订单限额下限
	 * @param limitMoneyMax 订单限额上限
	 * @param validityStartTime 有效期起始
	 * @param validityEndTime 有效期截止
	 * @param status -2：过期 -1:作废  0:未用 1:已用  2:已发放 3:未发放
	 * @param
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
			@RequestParam(value = "limit_money_min", required = false) Double limitMoneyMin,
	        @RequestParam(value = "limit_money_max", required = false) Double limitMoneyMax,
			@RequestParam(value = "validity_start_time", required = false) Long validityStartTime,
			@RequestParam(value = "validity_end_time", required = false) Long validityEndTime,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "yjj_number", required = false) Long businessNumber,
			@RequestParam(value = "code", required = false) String code) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
        StoreCoupon storeCoupon =
            new StoreCoupon(null, code, name, type, rangeType, validityStartTime, validityEndTime, isLimit, businessNumber, status);
		
        int totalCount = storeCouponService.searchCount(storeCoupon, moneyMin, moneyMax , limitMoneyMin , limitMoneyMax);

        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);

        List<StoreCouponVO> list = storeCouponFacade.search(pageQuery, storeCoupon, moneyMin, moneyMax , limitMoneyMin , limitMoneyMax);
		
        data.put("total", pageQueryResult);

		data.put("list", list);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	private boolean checkEmpty(String string) {
		if(string == null ||string.equals("")){
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param id
	 * @param status -1:作废  0:未用 1:已用 
	 * @return
	 */
    @RequestMapping("/update")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse update(@RequestParam("id") Long id,
                               @RequestParam(value = "status", required = false) Integer status) {
	    JsonResponse jsonResponse = new JsonResponse();

	    try {
	    	storeCouponService.update(id, status);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}

        return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
    
    @RequestMapping("/template/{id}")
    @ResponseBody
    public JsonResponse templateDescription(@PathVariable("id") Long id, 
    		@RequestParam(value = "start_time", required = false) Long startTime,
    		@RequestParam(value = "end_time", required = false) Long endTime) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<>();
    	
    	StoreCouponTemplate storeCouponTemplate = storeCouponTemplateDao.search(id);
    	data.put("storeCouponTemplate", storeCouponTemplate);
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
    
//  @RequestMapping("/template/jiucoinmall/search")
//	@ResponseBody
//	public JsonResponse searchJiuCoinMallTemplate(PageQuery pageQuery,
//			@RequestParam(value = "id", required = false) Long id,
//			@RequestParam(value = "name", required = false) String name,
//			@RequestParam(value = "type", required = false) Integer type,
//			@RequestParam(value = "range_type", required = false) Integer rangeType,
//			@RequestParam(value = "limit", required = false) Integer isLimit,
//			@RequestParam(value = "publish_count_min", required = false) Integer publishCountMin,
//			@RequestParam(value = "publish_count_max", required = false) Integer publishCountMax,
//			@RequestParam(value = "money_min", required = false) Double moneyMin,
//			@RequestParam(value = "money_max", required = false) Double moneyMax,
//			@RequestParam(value = "validity_start_time", required = false) Long validityStartTime,
//			@RequestParam(value = "validity_end_time", required = false) Long validityEndTime) {
//		JsonResponse jsonResponse = new JsonResponse();
//		Map<String, Object> data = new HashMap<String, Object>();
//		
//		StoreCouponTemplate storeCouponTemplate = new StoreCouponTemplate(id, name, type, rangeType, validityStartTime, validityEndTime, isLimit, null, null, null, null);
//		
//        int totalCount =
//        		storeCouponTemplateService.searchCount(storeCouponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax);
//        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	
//        data.put("total", pageQueryResult);
//
//		List<StoreCouponTemplateVO> list = storeCouponFacade.search(pageQuery, storeCouponTemplate, publishCountMin, publishCountMax, moneyMin, moneyMax);
//		
//		for(StoreCouponTemplateVO storeCouponTemplateVO : list){
//			if(storeCouponTemplateVO.getExchangeJiuCoinSetting()==0){
//				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("该优惠券无法使用玖币兑换");
//			}
//		}
//		
//		data.put("list", list);
//
//		data.put("coupon_money_info", globalSettingService.getJsonObject(GlobalSettingName.COUPON));
//		
//		return jsonResponse.setSuccessful().setData(data);
//	}
    
    /**
     * 获取正常的门店总数量
     * @return int
     */
    @RequestMapping("/get/userCount")
	@ResponseBody
	public JsonResponse searchStoreCount(){
    	JsonResponse jsonResponse = new JsonResponse();
    	int count = storeBusinessService.searchStoreCount(0);
    	return jsonResponse.setSuccessful().setData(count);
    }
    

}
