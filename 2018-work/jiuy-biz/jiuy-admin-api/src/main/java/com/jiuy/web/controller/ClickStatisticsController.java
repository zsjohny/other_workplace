package com.jiuy.web.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.ClickStatisticsFacade;
import com.jiuy.core.meta.clickstatistics.ClickStatisticsSearch;
import com.jiuy.core.meta.clickstatistics.ClickStatisticsVO;
import com.jiuy.core.service.ClickStatisticsService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/homepagestatistics")
@Login
public class ClickStatisticsController {
	
	@Resource
	private ClickStatisticsFacade clickStatisticsFacade;
	
	@Resource
	private ClickStatisticsService csService;
	
	@RequestMapping(value = "/search" , method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse search(@RequestParam(value = "page", required = false, defaultValue = "1")int page,
			@RequestParam(value = "page_size", required = false, defaultValue="20")int pageSize,
			@RequestParam(value = "start_time", required = false, defaultValue = "1970-1-1 00:00:00")String startTime,
			@RequestParam(value = "end_time", required =false, defaultValue="")String endTime,
			@RequestParam(value = "code", required =false, defaultValue="")String code,
			@RequestParam(value = "statistics_id", required = false, defaultValue="-1")long statisticsId,
			@RequestParam(value = "start_floor_id",required= false, defaultValue="-1")long startFloorId,
			@RequestParam(value = "end_floor_id",required = false, defaultValue="-1")long endFloorId,
			@RequestParam(value = "floor_name",required = false, defaultValue="")String floorName,
			@RequestParam(value = "template_id",required = false, defaultValue="")String templateId,
			@RequestParam(value = "serial_number",required = false, defaultValue="")String serialNumber,
			@RequestParam(value = "start_order_count",required = false,defaultValue="-1")long startRelatedOrderCount,
			@RequestParam(value = "end_order_count",required = false, defaultValue = "-1")long endRelatedOrderCount,
			@RequestParam(value = "start_click_count",required = false, defaultValue = "-1")long startClickCount,
			@RequestParam(value = "end_click_count",required = false, defaultValue = "-1")long endClickCount,
			@RequestParam(value = "sort",required = false, defaultValue = "0")int sort){
		
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery query = new PageQuery(page, pageSize);
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(query, queryResult);
		
		ClickStatisticsSearch clickStatisticsSearch = new ClickStatisticsSearch();
		clickStatisticsSearch.setId(statisticsId);
		clickStatisticsSearch.setCode(code);
		clickStatisticsSearch.setStartFloorId(startFloorId);
		clickStatisticsSearch.setEndFloorId(endFloorId);
		clickStatisticsSearch.setFloorName(floorName);
		clickStatisticsSearch.setTemplateId(templateId);
		clickStatisticsSearch.setSerialNumber(serialNumber);
		clickStatisticsSearch.setStartRelatedOrderCount(startRelatedOrderCount);
		clickStatisticsSearch.setEndRelatedOrderCount(endRelatedOrderCount);
		clickStatisticsSearch.setStartClickCount(startClickCount);
		clickStatisticsSearch.setEndClickCount(endClickCount);
		
		long endTimeL = -1L;
    	long startTimeL = 0L;
    	try {
			startTimeL = DateUtil.convertToMSEL(startTime);
			if(!StringUtils.equals(endTime, "")){
				endTimeL = DateUtil.convertToMSEL(endTime);
			}
		} catch (ParseException e) {
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("startTime:" + startTime + " endTime:" + endTime);
		}
		
    	List<ClickStatisticsVO> list = clickStatisticsFacade.search(clickStatisticsSearch, startTimeL, endTimeL,queryResult,sort);
    	int count = csService.searchCount(clickStatisticsSearch, startTimeL, endTimeL);
    	
    	queryResult.setRecordCount(count);
        
		data.put("statisticsList", list);
		data.put("total", queryResult);
    	
		return jsonResponse.setSuccessful().setData(data);
		
	}
	
}
