package com.jiuy.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.service.share.ShareService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/shareStatistics")
@Login
public class ShareStatisticsController {
	@Resource
	private ShareService shareService;

	/**
	 * 
	 * @param startTime  查询开始时间
	 * @param endTime   查询结束时间
	 * @return
	 */
	@RequestMapping(value = "/all")
	@ResponseBody
	public JsonResponse productSale(
			@RequestParam(value = "start_time", required = false, defaultValue = "0") long startTime,
			@RequestParam(value = "end_time", required = false, defaultValue = "0") long endTime) {
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();
		long totayZeroTime = DateUtil.getDayZeroTime(System.currentTimeMillis());// 当前0点

		if (startTime == 0 && endTime == 0) {// 初始化
			List<Map<String, Object>> list = new ArrayList();
			Map<String, Object> totayMap = shareService.getTotayData(totayZeroTime,System.currentTimeMillis());//今天的数据
			list.add(putDayString(totayMap, "今天"));
			list.add(putDayString(shareService.getDayReportData(getDayTime(1),totayZeroTime), "昨天"));
			list.add(putDayString(shareService.getDayReportData(getDayTime(3),totayZeroTime),"近3天"));
			list.add(putDayString(shareService.getDayReportData(getDayTime(7),totayZeroTime),"近7天"));
			list.add(putDayString(shareService.getDayReportData(getDayTime(30),totayZeroTime),"近30天"));
			list.add(putDayString(shareService.getDayReportData(getDayTime(90),totayZeroTime),"近90天"));
			list.add(putDayString(shareService.getDayReportData(getDayTime(180),totayZeroTime),"近180天"));
			Map<String, Object> allMap = shareService.getDayReportData(0,totayZeroTime);//全部数据，需要加上今天的数据
			allMap.put("clickCount", ((BigDecimal) allMap.get("clickCount")).longValue()+(Long)totayMap.get("clickCount"));
			allMap.put("orderCount", ((BigDecimal) allMap.get("orderCount")).longValue()+(Long)totayMap.get("orderCount"));
			allMap.put("payCount", ((BigDecimal) allMap.get("payCount")).longValue()+(Long)totayMap.get("payCount"));
			allMap.put("payMoney", ((BigDecimal)allMap.get("payMoney")).add((BigDecimal)totayMap.get("payMoney")));
			allMap.put("productCount", ((BigDecimal) allMap.get("productCount")).longValue()+(Long)totayMap.get("productCount"));
			allMap.put("registerCount", ((BigDecimal) allMap.get("registerCount")).longValue()+(Long)totayMap.get("registerCount"));
			allMap.put("shareCount", ((BigDecimal) allMap.get("shareCount")).longValue()+(Long)totayMap.get("shareCount"));
			list.add(putDayString(allMap, "全部"));
			data.put("list", list);
		} else  {
			Map<String, Object> totayMap = new HashMap();
			if (endTime == 0) {
				endTime = System.currentTimeMillis();
			}
			if (endTime > totayZeroTime) {// 需要计算今天的数据
				totayMap = shareService.getTotayData(totayZeroTime, endTime);
				endTime = totayZeroTime;
			}
			Map<String, Object> allMap = shareService.getDayReportData(startTime, endTime);
			if (totayMap != null && totayMap.size() > 1) {
				allMap.put("clickCount", ((BigDecimal) allMap.get("clickCount")).longValue()+(Long)totayMap.get("clickCount"));
				allMap.put("orderCount", ((BigDecimal) allMap.get("orderCount")).longValue()+(Long)totayMap.get("orderCount"));
				allMap.put("payCount", ((BigDecimal) allMap.get("payCount")).longValue()+(Long)totayMap.get("payCount"));
				allMap.put("payMoney", ((BigDecimal)allMap.get("payMoney")).add((BigDecimal)totayMap.get("payMoney")));
				allMap.put("productCount", ((BigDecimal) allMap.get("productCount")).longValue()+(Long)totayMap.get("productCount"));
				allMap.put("registerCount", ((BigDecimal) allMap.get("registerCount")).longValue()+(Long)totayMap.get("registerCount"));
				allMap.put("shareCount", ((BigDecimal) allMap.get("shareCount")).longValue()+(Long)totayMap.get("shareCount"));
			}
			data.put("map", allMap);
		}
		return jsonResponse.setSuccessful().setData(data);
	}
	/**
	 * 
	 * @param yjjNumber	俞姐姐号
	 * @param productId  商品Id
	 * @param type   分享渠道 0：其他 1：微信好友 2:微信朋友圈 3：QQ 4:QQ空间 5:腾讯微博 6:新浪微博  （默认不传或-1）
	 * @param start_time 分享时间
	 * @param end_time	
	 * @param phone	手机号
	 * @param clothes_number 商品款号
	 * @param start_jiubi	玖币
	 * @param end_jiubi
	 * @return
	 */
	@RequestMapping(value = "/record")
	@ResponseBody
	public JsonResponse record(PageQuery pageQuery,
			@RequestParam(value = "yjjNumber", required = false, defaultValue = "0") long yjjNumber,
			@RequestParam(value = "productId", required = false, defaultValue = "0") long productId,
			@RequestParam(value = "type", required = false, defaultValue = "-1") int type,
			@RequestParam(value = "start_time", required = false, defaultValue = "0") long startTime,
			@RequestParam(value = "end_time", required = false, defaultValue = "0") long endTime,
			@RequestParam(value = "phone", required = false, defaultValue = "0") long phone,
			@RequestParam(value = "clothes_number", required = false, defaultValue = "") String clothesNumber,
			@RequestParam(value = "start_jiubi", required = false, defaultValue = "0") long startJiuBi,
			@RequestParam(value = "end_jiubi", required = false, defaultValue = "0") long endJiuBi) {
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();
		
    	int totalCount = shareService.getRecordCount(yjjNumber,productId,type,startTime,endTime,phone,clothesNumber,startJiuBi,endJiuBi);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String, String>> list = shareService.searchShareRecord(pageQuery, yjjNumber,productId,type,startTime,endTime,phone,clothesNumber,startJiuBi,endJiuBi);
		
		data.put("list", list);
		data.put("total", pageQueryResult);
		return jsonResponse.setSuccessful().setData(data);
	}
		/**
		 * 
		 * @param pageQuery
		 * @param shareYjjNumber	分享着俞姐姐号
		 * @param yjjNumber			用户俞姐姐号
		 * @param type		type1：注册 2:下单   （默认不传或传-1)
		 * @param startTime   分享时间
		 * @param endTime
		 * @param phone    手机号
		 * @param shareId    分享ID
		 * @return
		 */
	@RequestMapping(value = "/click/record")
	@ResponseBody
	public JsonResponse clickRecord(PageQuery pageQuery,
			@RequestParam(value = "share_yjjNumber", required = false, defaultValue = "0") long shareYjjNumber,
			@RequestParam(value = "yjjNumber", required = false, defaultValue = "0") long yjjNumber,
			@RequestParam(value = "type", required = false, defaultValue = "-1") int type,
			@RequestParam(value = "start_time", required = false, defaultValue = "0") long startTime,
			@RequestParam(value = "end_time", required = false, defaultValue = "0") long endTime,
			@RequestParam(value = "phone", required = false, defaultValue = "0") long phone,
			@RequestParam(value = "share_phone", required = false, defaultValue = "0") long sharePhone,
			@RequestParam(value = "shareId", required = false, defaultValue = "0") long shareId) {
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();
		
    	int totalCount = shareService.getClickRecordCount(shareYjjNumber,yjjNumber,type,startTime,endTime,phone,sharePhone,shareId);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String, String>> list = shareService.searchClickShareRecord(pageQuery, shareYjjNumber,yjjNumber,type,startTime,endTime,phone,sharePhone,shareId);
		
		data.put("list", list);
		data.put("total", pageQueryResult);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	public static long getDayTime(int day) {
		long time = DateUtil.getDayZeroTime(System.currentTimeMillis()) - DateUtils.MILLIS_PER_DAY * day;
		return time;
	}
	
	public static Map<String, Object> putDayString(Map<String, Object> map,String str) {
		map.put("day", str);
		return map;
	}
}
