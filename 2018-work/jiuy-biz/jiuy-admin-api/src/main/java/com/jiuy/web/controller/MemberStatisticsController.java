package com.jiuy.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.OrderNewFacade;
import com.jiuy.core.meta.memberstatistics.ChannelStatisticsBean;
import com.jiuy.core.meta.memberstatistics.ChannelStatisticsSearchBean;
import com.jiuy.core.meta.memberstatistics.TemplateStatisticsBean;
import com.jiuy.core.meta.memberstatistics.TemplateStatisticsSeniorBean;
import com.jiuy.core.service.MemberStatisticsService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.MemberStatistics;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;

import com.jiuyuan.web.help.JsonResponse;
@Controller
@RequestMapping("/memberStatistics")
@Login
public class MemberStatisticsController {
	
	@Resource
	private MemberStatisticsService memberStatisticsService;
	
	@Resource
	private OrderNewFacade orderNewFacade;
	
	@RequestMapping(value="/productSale")
	@ResponseBody	
	public JsonResponse productSale(PageQuery pageQuery,
			@RequestParam(value="product_id", required=false, defaultValue="-1") long productId,
			@RequestParam(value="product_name", required=false, defaultValue="") String productName,
			@RequestParam(value="skuId", required=false, defaultValue="-1") int skuId,
			@RequestParam(value="season", required=false, defaultValue="-1") int season,
			@RequestParam(value="years", required=false, defaultValue="-1") int years,
			@RequestParam(value="brand_name", required=false, defaultValue="") String brandName,
			@RequestParam(value="classify", required=false, defaultValue="") String classify,
			@RequestParam(value="start_time", required=false,defaultValue="") String startTime,
			@RequestParam(value="end_time", required=false,defaultValue="") String endTime,
			@RequestParam(value="sort", required=false, defaultValue="1") int sort) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();

        long startCreateTimeL = 0L;
        long endCreateTimeL = 0L;
    	try {
    		startCreateTimeL = DateUtil.convertToMSEL(startTime);		
    		endCreateTimeL = DateUtil.convertToMSEL(endTime);
    		if(endCreateTimeL==0){
    			endCreateTimeL = System.currentTimeMillis();
    		}
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
    	MemberStatistics memberStatistics = new MemberStatistics(startCreateTimeL,endCreateTimeL,productId,productName,skuId,season,years,brandName,classify);
    	int totalCount = memberStatisticsService.getProductSaleCount(memberStatistics);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String, String>> list = memberStatisticsService.searchProductSale(pageQuery, memberStatistics,sort);
		Map<String, String> totalMap = memberStatisticsService.getProductSaleTotal(memberStatistics);							
		data.put("list", list);
		data.put("total", pageQueryResult);
		data.put("summary", totalMap);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 
	 * @param pageQuery
	 * @param yjjNumber
	 * @param superiorYjjNumber
	 * @param userStatus		-1 为 全部
	 * @param userType
	 * @param orderCountMin
	 * @param orderCountMax
	 * @param pvCountMin
	 * @param pvCountMax
	 * @param clientSrc
	 * @param startCreateTime
	 * @param endCreateTime
	 * @return
	 */
	@RequestMapping(value="/channelStatistics")
	@ResponseBody	
	public JsonResponse channelSearch(PageQuery pageQuery,
			@RequestParam(value="yjj_number", required=false)String yjjNumber,
			@RequestParam(value="superior_yjj_number", required=false)String superiorYjjNumber,
			@RequestParam(value="user_status",required=false,defaultValue="-1")int userStatus,
			@RequestParam(value="user_type",required=false,defaultValue="-1")int userType,
			@RequestParam(value="order_count_min",required=false,defaultValue="0")int orderCountMin,
			@RequestParam(value="order_count_max",required=false,defaultValue="-1")int orderCountMax,
			@RequestParam(value="pv_count_min",required=false,defaultValue="0")int pvCountMin,
			@RequestParam(value="pv_count_max",required=false,defaultValue="-1")int pvCountMax,
			@RequestParam(value="client_src",required=false,defaultValue="-1")int clientSrc,
			@RequestParam(value="start_create_time",required=false,defaultValue="0")long startTimeL,
			@RequestParam(value="end_create_time",required=false,defaultValue="-1")long endTimeL){
		
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();
    	
    	ChannelStatisticsSearchBean channelStatisticsSearchBean = new ChannelStatisticsSearchBean(yjjNumber, superiorYjjNumber, userStatus, orderCountMin, orderCountMax, pvCountMin, pvCountMax, startTimeL, endTimeL, userType, clientSrc);
    	
    	List<ChannelStatisticsBean> list = memberStatisticsService.searchChannelStatistics(channelStatisticsSearchBean,pageQuery);
    	int count = memberStatisticsService.getChannelStatisticsCount(channelStatisticsSearchBean);
    	
    	//获取符合要求的userid
    	Map<String, Object> resultMap = new HashMap<>();
    	resultMap.put("totalPVCount", 0);
    	resultMap.put("totalOrderCount", 0);
    	resultMap.put("totalMoney", 0);
    	resultMap.put("totalUserCount", 0);
    	
    	//查询第一页记录时才计算一次总和
    	if(pageQuery.getPage()==1){
    		//当前汇总
            resultMap = memberStatisticsService.sumCurrentChannelTotal(channelStatisticsSearchBean);
    	}
    	
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);
		data.put("list", list);
    	data.put("currentTotal", resultMap);
    	data.put("total", pageQueryResult);
    	
		return jsonResponse.setSuccessful().setData(data);
	}
	

	@RequestMapping(value="/category/statistics")
	@ResponseBody	
	public JsonResponse categoryStatistics(PageQuery pageQuery,
			@RequestParam(value="category_name",required=false)String categoryName,
			@RequestParam(value="start_time",required=false,defaultValue="0")long startTimeL,
			@RequestParam(value="end_time",required=false,defaultValue="-1")long endTimeL,
			@RequestParam(value="sort", required=false, defaultValue="1")int sort){
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();
		
		if(endTimeL == -1){
			System.currentTimeMillis();
		}
    	
    	List<Map<String, Object>> result = memberStatisticsService.searchCategoryStatistics(pageQuery, categoryName, startTimeL, endTimeL,sort);
    	int count = memberStatisticsService.getCategoryStatisticsCount(categoryName, startTimeL, endTimeL);
    	
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);
    	
    	data.put("list", result);
    	data.put("total", pageQueryResult);
    	
    	return jsonResponse.setSuccessful().setData(data);
	}

	@RequestMapping(value="/productSale/detail")
	@ResponseBody	
	public JsonResponse productSaleDetail(PageQuery pageQuery,
			@RequestParam(value="product_id", required=true) long productId,
			@RequestParam(value="start_time", required=false,defaultValue="") String startTime,
			@RequestParam(value="end_time", required=false,defaultValue="") String endTime) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();

        long startCreateTimeL = 0L;
        long endCreateTimeL = 0L;
    	try {
    		startCreateTimeL = DateUtil.convertToMSEL(startTime);		
    		endCreateTimeL = DateUtil.convertToMSEL(endTime);
    		if(endCreateTimeL==0){
    			endCreateTimeL = System.currentTimeMillis();
    		}
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
    	MemberStatistics memberStatistics = new MemberStatistics(startCreateTimeL,endCreateTimeL,productId);
    	int totalCount = memberStatisticsService.getProductDetailSaleCount(memberStatistics);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String, String>> list = memberStatisticsService.searchProductDetailSale(pageQuery, memberStatistics);
		data.put("list", list);
		data.put("total", pageQueryResult);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/provinceSale")
	@ResponseBody	
	public JsonResponse provinceSale(PageQuery pageQuery,
			@RequestParam(value="province_name", required=false,defaultValue = "") String provinceName,
			@RequestParam(value="start_time", required=false,defaultValue="") String startTime,
			@RequestParam(value="end_time", required=false,defaultValue="") String endTime,
			@RequestParam(value="sort", required=false, defaultValue="1")int sort) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();

        long startCreateTimeL = 0L;
        long endCreateTimeL = 0L;
    	try {
    		startCreateTimeL = DateUtil.convertToMSEL(startTime);		
    		endCreateTimeL = DateUtil.convertToMSEL(endTime);
    		if(endCreateTimeL==0){
    			endCreateTimeL = System.currentTimeMillis();
    		}
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
    	MemberStatistics memberStatistics = new MemberStatistics(startCreateTimeL,endCreateTimeL,provinceName);
    	int totalCount = memberStatisticsService.getProvinceSaleCount(memberStatistics);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String, String>> list = memberStatisticsService.searchProvinceSale(pageQuery, memberStatistics,sort);
		Map<String, String> totalMap = memberStatisticsService.getProvinceSaleTotal(memberStatistics);
		data.put("list", list);
		data.put("total", pageQueryResult);
		data.put("summary", totalMap);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/citySale")
	@ResponseBody	
	public JsonResponse citySale(PageQuery pageQuery,
			@RequestParam(value="city_name", required=false,defaultValue = "") String cityName,
			@RequestParam(value="start_time", required=false,defaultValue="") String startTime,
			@RequestParam(value="end_time", required=false,defaultValue="") String endTime,
			@RequestParam(value="sort", required=false, defaultValue="1")int sort) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();

        long startCreateTimeL = 0L;
        long endCreateTimeL = 0L;
    	try {
    		startCreateTimeL = DateUtil.convertToMSEL(startTime);		
    		endCreateTimeL = DateUtil.convertToMSEL(endTime);
    		if(endCreateTimeL==0){
    			endCreateTimeL = System.currentTimeMillis();
    		}
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
    	MemberStatistics memberStatistics = new MemberStatistics(cityName,startCreateTimeL,endCreateTimeL);
    	int totalCount = memberStatisticsService.getCitySaleCount(memberStatistics);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String, String>> list = memberStatisticsService.searchCitySale(pageQuery, memberStatistics,sort);
		Map<String, String> totalMap = memberStatisticsService.getCitySaleTotal(memberStatistics);
		data.put("list", list);
		data.put("total", pageQueryResult);
		data.put("summary", totalMap);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/brandStatistics")
	@ResponseBody	
	public JsonResponse brandStatistics(PageQuery pageQuery,
			@RequestParam(value="brand_name",required=false)String brandName,
			@RequestParam(value="start_time",required=false,defaultValue="0")long startTimeL,
			@RequestParam(value="end_time",required=false,defaultValue="-1")long endTimeL,
			@RequestParam(value="sort", required=false, defaultValue="1")int sort){
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();
		if(endTimeL == -1){
			endTimeL = System.currentTimeMillis();
		}
    	
    	List<Map<String, Object>> result = memberStatisticsService.searchBrandStatistics(pageQuery, brandName, startTimeL, endTimeL,sort);
    	int count = memberStatisticsService.getBrandStatisticsCount(brandName, startTimeL, endTimeL);
    	
    	Map<String, Object> currentTotal = memberStatisticsService.sumCurrentBrandStatisticsTotal(brandName, startTimeL, endTimeL);
    	
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);
    	
    	data.put("list", result);
    	data.put("total", pageQueryResult);
    	data.put("currentTotal", currentTotal);
    	
    	return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/pageCategory")
	@ResponseBody	
	public JsonResponse pageCategoryStatistics(PageQuery pageQuery,
			@RequestParam(value="category_name", required=false,defaultValue = "") String categoryName,
			@RequestParam(value="category_id", required=false,defaultValue= "") String categoryId,
			@RequestParam(value="start_time", required=false,defaultValue="") String startTime,
			@RequestParam(value="end_time", required=false,defaultValue="") String endTime) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();

        long startCreateTimeL = 0L;
        long endCreateTimeL = 0L;
    	try {
    		startCreateTimeL = DateUtil.convertToMSEL(startTime);		
    		endCreateTimeL = DateUtil.convertToMSEL(endTime);
    		if(endCreateTimeL==0){
    			endCreateTimeL = System.currentTimeMillis();
    		}
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
    	MemberStatistics memberStatistics = new MemberStatistics(categoryName,categoryId,startCreateTimeL,endCreateTimeL);
    	int totalCount = memberStatisticsService.getPageCategoryCount(memberStatistics);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String, String>> list = memberStatisticsService.searchPageCategory(pageQuery, memberStatistics);
		data.put("list", list);
		data.put("total", pageQueryResult);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/page/productDetail")
	@ResponseBody	
	public JsonResponse pageProductDetailStatistics(PageQuery pageQuery,
			@RequestParam(value="product_name", required=false,defaultValue = "") String productName,
			@RequestParam(value="product_id", required=false,defaultValue= "") String productId,
			@RequestParam(value="start_time", required=false,defaultValue="") String startTime,
			@RequestParam(value="end_time", required=false,defaultValue="") String endTime) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();

        long startCreateTimeL = 0L;
        long endCreateTimeL = 0L;
    	try {
    		startCreateTimeL = DateUtil.convertToMSEL(startTime);		
    		endCreateTimeL = DateUtil.convertToMSEL(endTime);
    		if(endCreateTimeL==0){
    			endCreateTimeL = System.currentTimeMillis();
    		}
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
    	MemberStatistics memberStatistics = new MemberStatistics(productName,productId,startCreateTimeL,endCreateTimeL);
    	int totalCount = memberStatisticsService.getPageProductDetailCount(memberStatistics);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String, String>> list = memberStatisticsService.searchPageProductDetail(pageQuery, memberStatistics);
		Map<String, Object> suMap = memberStatisticsService.getSummaryPageProductDetail(memberStatistics);
		data.put("list", list);
		data.put("total", pageQueryResult);
		data.put("sum", suMap);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/user")
	@ResponseBody	
	public JsonResponse userStatistics(
			@RequestParam(value="start_time", required=false,defaultValue="") String startTime,
			@RequestParam(value="end_time", required=false,defaultValue="") String endTime) {
		JsonResponse jsonResponse = new JsonResponse();

        long startCreateTimeL = 0L;
        long endCreateTimeL = 0L;
    	try {
    		startCreateTimeL = DateUtil.convertToMSEL(startTime);		
    		endCreateTimeL = DateUtil.convertToMSEL(endTime);
    		if(endCreateTimeL==0){
    			endCreateTimeL = System.currentTimeMillis();
    		}
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
    	MemberStatistics memberStatistics = new MemberStatistics(startCreateTimeL,endCreateTimeL);
        
    	Map<String, Object> data = memberStatisticsService.searchUserStatistics(memberStatistics);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/template")
	@ResponseBody	
	public JsonResponse templateStatistics(PageQuery pageQuery,@RequestParam(value="start_time",required=false,defaultValue="0")long startTime,
			@RequestParam(value="end_time",required=false,defaultValue="0")long endTime){
		JsonResponse jsonResponse = new JsonResponse();
		HashMap<String, Object> data = new HashMap<String,Object>();
		if(endTime==0){
			endTime = System.currentTimeMillis();
		}
		List<TemplateStatisticsBean> list = memberStatisticsService.searchTemplateStatistics(pageQuery,startTime, endTime);
		data.put("list", list);
		
		int totalCount = memberStatisticsService.searchTemplateStatisticsCount(startTime, endTime);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/template/senior")
	@ResponseBody
	public JsonResponse templateStatisticsSenior(PageQuery pageQuery,
			@RequestParam(value="click_start_time",required=false,defaultValue="0")long clickStartTime,
			@RequestParam(value="click_end_time",required=false,defaultValue="-1")long clickEndTime,
			@RequestParam(value="promote_start_time",required=false,defaultValue="0")long promoteStartTime,
			@RequestParam(value="promote_end_time",required=false,defaultValue="-1")long promoteEndTime,
			@RequestParam(value="belong_page",required=false,defaultValue="0")int belongPageId,
			@RequestParam(value="floor_name",required=false)String floorName,
			@RequestParam(value="template_id",required=false,defaultValue="")String templateId,
			@RequestParam(value="serial_number",required=false,defaultValue="")String serialNumber,
			@RequestParam(value="sort",required=false,defaultValue="0")int sort,
			@RequestParam(value="code",required=false,defaultValue="")String code){
		JsonResponse jsonResponse = new JsonResponse();
		HashMap<String, Object> data = new HashMap<String,Object>(); 
		
		if(clickEndTime==-1){
			clickEndTime = System.currentTimeMillis();
		}
	
		if(promoteEndTime==-1){
			promoteEndTime = System.currentTimeMillis();
		}
		
		List<TemplateStatisticsSeniorBean> list = memberStatisticsService.searchTemplateStatisticsSenior(belongPageId, floorName, code, templateId, serialNumber, clickStartTime, clickEndTime, promoteStartTime, promoteEndTime, sort,pageQuery);
		data.put("list", list);
		
		int totalCount = memberStatisticsService.getTemplateStatisticsSeniorCount(belongPageId, floorName, code, templateId, serialNumber, clickStartTime, clickEndTime, promoteStartTime, promoteEndTime, sort);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	
        data.put("total", pageQueryResult);
        
        Map<String, Object> currentTotal = memberStatisticsService.sumTemplateStatisticsSenior(belongPageId, floorName, code, templateId, serialNumber, clickStartTime, clickEndTime, promoteStartTime, promoteEndTime);
		
        /*Map<String, Object> currentOrder = memberStatisticsService.sumTemplateStatisticsSeniorOrder(belongPageId, floorName, code, templateId, serialNumber, clickStartTime, clickEndTime, promoteStartTime, promoteEndTime);*/
		
		data.put("currentTotal", currentTotal);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value="/looked/more")
	@ResponseBody	
	public JsonResponse lookedMoreStatistics(PageQuery pageQuery,
			@RequestParam(value="start_time",required=false,defaultValue="")String startTime,
			@RequestParam(value="end_time",required=false,defaultValue="")String endTime,
			@RequestParam(value="product_id",required=false,defaultValue="-1")long productId,
			@RequestParam(value="product_name",required=false,defaultValue="")String productName){
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();

		 long startCreateTimeL = 0L;
	     long endCreateTimeL = 0L;
	    	try {
	    		startCreateTimeL = DateUtil.convertToMSEL(startTime);		
	    		endCreateTimeL = DateUtil.convertToMSEL(endTime);
	    		if(endCreateTimeL == 0){
	    			endCreateTimeL = System.currentTimeMillis();
	    		}
			} catch (ParseException e) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
			}
	    	MemberStatistics memberStatistics = new MemberStatistics(startCreateTimeL,endCreateTimeL,productId,productName);
	    	int totalCount = memberStatisticsService.getLookMoreCount(memberStatistics);
	        
	        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

			List<Map<String, String>> list = memberStatisticsService.searchLookMore(pageQuery, memberStatistics);
			int pvCount =memberStatisticsService.getLookMorePv(memberStatistics);
			data.put("list", list);
			data.put("total", pageQueryResult);
			data.put("pvCount", pvCount);
    	return jsonResponse.setSuccessful().setData(data);
	}	
	
	@RequestMapping(value="/looked/see")
	@ResponseBody	
	public JsonResponse lookedStatistics(PageQuery pageQuery,
			@RequestParam(value="start_time",required=false,defaultValue="")String startTime,
			@RequestParam(value="end_time",required=false,defaultValue="")String endTime){
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();

		 long startCreateTimeL = 0L;
	     long endCreateTimeL = 0L;
	    	try {
	    		startCreateTimeL = DateUtil.convertToMSEL(startTime);		
	    		endCreateTimeL = DateUtil.convertToMSEL(endTime);
	    		if(endCreateTimeL == 0){
	    			endCreateTimeL = System.currentTimeMillis();
	    		}
			} catch (ParseException e) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
			}
	    List<Map<String, Object>> list = getLookDataPerDay(startCreateTimeL,endCreateTimeL,pageQuery);
	    PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, lookCount);	

    	data.put("list", list);
    	data.put("total", pageQueryResult);
    	return jsonResponse.setSuccessful().setData(data);
	}
	
	public static int lookCount; 
	private List<Map<String, Object>> getLookDataPerDay(long startTime, long endTime, PageQuery pageQuery) {
			DateTime begin = new DateTime(startTime);
	        DateTime end = new DateTime(endTime);
	        Period p = new Period(begin, end, PeriodType.days());
	        int days = p.getDays();
	        lookCount=days+1;
	        
	        Map<String, Object> pvMap = memberStatisticsService.lookPvPerDay(startTime, endTime);
	        Map<String, Object> clickMap = memberStatisticsService.lookClickPerDay(startTime, endTime);
	        Map<String, Object> relationOrderMap = memberStatisticsService.lookOrderPerDay(startTime, endTime);

	        int j =	days -(pageQuery.getPage()-1)*pageQuery.getPageSize();
	        int k = days-pageQuery.getPage()*pageQuery.getPageSize()+1;
		        if(k<0){
		        	k=0;
		        }
	        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	        for (int i = j; i >=k ; i--) {
	            Map<String, Object> map = new HashMap<String, Object>();
	            String dayTime = begin.plusDays(i).toString("yyyy-MM-dd");

	            map.put("day", dayTime);
	            map.put("pv",(pvMap.get(dayTime)!= null)?pvMap.get(dayTime):0);
	            map.put("click",(clickMap.get(dayTime) != null)?clickMap.get(dayTime):0);
	            map.put("orderCount", (relationOrderMap != null && relationOrderMap.size()>0)?((relationOrderMap.get(dayTime) != null)?((MemberStatistics)relationOrderMap.get(dayTime)).getCount():0):0);
	            map.put("money", (relationOrderMap != null && relationOrderMap.size()>0)?((relationOrderMap.get(dayTime) != null)?((MemberStatistics)relationOrderMap.get(dayTime)).getMoney():0):0);
	            list.add(map);
	        }
	        return list;
	}
	/**
	 * 
	 * @param pageQuery
	 * @param type   0:全部  1:常规  2:专场
	 * @param startTimeL
	 * @param endTimeL
	 * @return
	 */
	@RequestMapping(value="/pageStatistics")
	@ResponseBody	
	public JsonResponse pageStatistics(PageQuery pageQuery,
			@RequestParam(value="type",required=false,defaultValue= "0")int type,
			@RequestParam(value="start_time",required=false,defaultValue="0")long startTimeL,
			@RequestParam(value="end_time",required=false,defaultValue="0")long endTimeL){
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();
		if(endTimeL == 0){
			endTimeL = System.currentTimeMillis();
		}
		
    	List<Map<String, Object>> result = memberStatisticsService.searchPageStatistics(pageQuery, type, startTimeL, endTimeL);
    	
    	Map<String, Object> currentTotal = memberStatisticsService.sumPageStatisticsTotal(type, startTimeL, endTimeL);

    	data.put("list", result);
    	data.put("currentTotal", currentTotal);
    	
    	return jsonResponse.setSuccessful().setData(data);
	}
	/**
	 * 
	 * @param pageQuery
	 * @param type	0:5个list;  1：商品销售排行  2：品类销售 3：品牌销售 4：省销售 5：市销售
	 * @param sort	1：销售额降序 2:升序 3：销量降序 4：升序 5：订单量降序 6：升序 7：下单会员数降序 8：升序
	 * @param startTimeL
	 * @param endTimeL
	 * @return
	 */
	@RequestMapping(value="/saleStatistics")
	@ResponseBody	
	public JsonResponse saleStatistics(PageQuery pageQuery,
			@RequestParam(value="type", required=false, defaultValue= "0")int type,
			@RequestParam(value="sort", required=false, defaultValue="1")int sort,
			@RequestParam(value="start_time",required=false,defaultValue="0")long startTimeL,
			@RequestParam(value="end_time",required=false,defaultValue="0")long endTimeL){
		Map<String, Object> data = new HashMap<String, Object>();
		
		JsonResponse jsonResponse = new JsonResponse();
		
		if(endTimeL == 0){
			endTimeL = System.currentTimeMillis();
		}
		
		if(type == 0){
			MemberStatistics memberStatistics1 = new MemberStatistics(startTimeL,endTimeL,-1,"",-1,-1,-1,"","");
			List<Map<String, String>> productList = memberStatisticsService.searchProductSale(pageQuery, memberStatistics1,sort);
			List<Map<String, Object>> categoryList = memberStatisticsService.searchCategoryStatistics(pageQuery, null, startTimeL, endTimeL,sort);
			List<Map<String, Object>> brandList = memberStatisticsService.searchBrandStatistics(pageQuery, null, startTimeL, endTimeL,sort);
			MemberStatistics memberStatistics2 = new MemberStatistics(startTimeL,endTimeL,"");	      
			List<Map<String, String>> provinceList = memberStatisticsService.searchProvinceSale(pageQuery, memberStatistics2,sort);
			MemberStatistics memberStatistics3 = new MemberStatistics("",startTimeL,endTimeL);	        
			List<Map<String, String>> cityList = memberStatisticsService.searchCitySale(pageQuery, memberStatistics3,sort);
			data.put("cityList", cityList);
			data.put("provinceList", provinceList);
			data.put("brandList", brandList);
			data.put("categoryList", categoryList);
			data.put("productList", productList);
		}else if(type ==1){
			MemberStatistics memberStatistics = new MemberStatistics(startTimeL,endTimeL,-1,"",-1,-1,-1,"","");
			List<Map<String, String>> productList = memberStatisticsService.searchProductSale(pageQuery, memberStatistics,sort);
			data.put("productList", productList);
		}else if(type ==2){
			List<Map<String, Object>> categoryList = memberStatisticsService.searchCategoryStatistics(pageQuery, null, startTimeL, endTimeL,sort);
			data.put("categoryList", categoryList);
		}else if( type ==3){
			List<Map<String, Object>> brandList = memberStatisticsService.searchBrandStatistics(pageQuery, null, startTimeL, endTimeL,sort);
			data.put("brandList", brandList);
		}else if(type ==4){
			MemberStatistics memberStatistics = new MemberStatistics(startTimeL,endTimeL,"");	      
			List<Map<String, String>> provinceList = memberStatisticsService.searchProvinceSale(pageQuery, memberStatistics,sort);
			data.put("provinceList", provinceList);
		}else if(type ==5){
			MemberStatistics memberStatistics = new MemberStatistics("",startTimeL,endTimeL);	        
			List<Map<String, String>> cityList = memberStatisticsService.searchCitySale(pageQuery, memberStatistics,sort);
			data.put("cityList", cityList);
		}
		Map<String, Object> totalMap = orderNewFacade.getTotalDataForTime(startTimeL,endTimeL);
		data.put("totalData", totalMap);
    	return jsonResponse.setSuccessful().setData(data);
	}	
}
