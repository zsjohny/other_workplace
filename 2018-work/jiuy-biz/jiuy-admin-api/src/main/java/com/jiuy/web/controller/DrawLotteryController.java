package com.jiuy.web.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.mapper.DrawLotteryMapper;
import com.jiuy.core.service.DrawLotteryService;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.DrawLottery;
import com.jiuyuan.web.help.JsonResponse;

/**
 * @author jeff.zhan
 * @version 2016年11月3日 下午7:46:50
 * 
 */

@Controller
@RequestMapping("/draw/lottery")
public class DrawLotteryController {
	
	@Autowired
	private DrawLotteryMapper drawLotteryMapper;
	
	@Autowired
	private DrawLotteryService drawLotteryService;
	
	@Autowired
	private GlobalSettingService GlobalSettingService;
	
	@RequestMapping("/load")
	@ResponseBody
	public JsonResponse load() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		
		drawLotteryService.overWrite(GlobalSettingService.getJsonObject(GlobalSettingName.DRAW_LOTTERY));
		data.put("list", drawLotteryMapper.load());
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	@RequestMapping("/batch/add")
	@ResponseBody
	public JsonResponse batchAdd(@RequestBody String drawLotteryJson) {
		JsonResponse jsonResponse = new JsonResponse();
		List<DrawLottery> drawLotterys = JSON.parseArray(drawLotteryJson, DrawLottery.class);
		long time = System.currentTimeMillis();
		for (DrawLottery drawLottery : drawLotterys) {
			drawLottery.setCreateTime(time);
			drawLottery.setUpdateTime(time);
			drawLottery.setLastAdjustTime(time);
		}
		
		drawLotteryMapper.batchAdd(drawLotterys);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public JsonResponse add(@RequestBody DrawLottery drawLottery) {
		JsonResponse jsonResponse = new JsonResponse();
		long time = System.currentTimeMillis();
		drawLottery.setCreateTime(time);
		drawLottery.setUpdateTime(time);
		drawLottery.setLastAdjustTime(time);
		drawLotteryMapper.add(drawLottery);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(drawLottery.getId());
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public JsonResponse update(@RequestBody DrawLottery drawLottery) {
		JsonResponse jsonResponse = new JsonResponse();
		long time = System.currentTimeMillis();
		drawLottery.setUpdateTime(time);
		
		drawLotteryService.update(drawLottery);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/delete") 
	@ResponseBody
	public JsonResponse delete(@RequestParam("id") long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		drawLotteryMapper.delete(id);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/publish") 
	@ResponseBody
	public JsonResponse publish(@RequestParam(value = "property_name") String propertyName,
			@RequestParam(value = "property_value") String propertyValue) {
		JsonResponse jsonResponse = new JsonResponse();
		
		//存全局数据
		GlobalSettingService.update(propertyName, propertyValue);
		
		try {
			//结合DrawLottery表存prize里的数据
			JSONObject draw_lottery = GlobalSettingService.getJsonObject(GlobalSettingName.DRAW_LOTTERY);
			List<DrawLottery> drawLotteries = drawLotteryMapper.load();
			draw_lottery.put("prize", assemblePrice(drawLotteries, DateUtil.convertToMSEL(draw_lottery.getString("acticity_start"))));
			GlobalSettingService.update(GlobalSettingName.DRAW_LOTTERY.getStringValue(), draw_lottery.toJSONString());

			//更新落后活动开始时间的最后调整时间的奖项记录
			batchUpdateLastAdjustTime(drawLotteries, DateUtil.convertToMSEL(draw_lottery.getString("acticity_start")));
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("活动开始日期转换错误");
		}
		
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}

	private void batchUpdateLastAdjustTime(List<DrawLottery> drawLotteries, long activity_start_time) {
		for (DrawLottery drawLottery : drawLotteries) {
			if (drawLottery.getLastAdjustTime() < activity_start_time) {
				drawLotteryMapper.update(drawLottery.getId(), activity_start_time);
			}
		}
	}

	private JSONArray assemblePrice(List<DrawLottery> drawLotteries, long activity_start_time) {
		JSONArray prize = new JSONArray();
		for (DrawLottery drawLottery : drawLotteries) {
			JSONObject prize_item = new JSONObject();
			prize_item.put("id", drawLottery.getId());
			prize_item.put("count", drawLottery.getCount());
			prize_item.put("name", drawLottery.getName());
			prize_item.put("type", drawLottery.getType());
			prize_item.put("rank_name", drawLottery.getRankName());
			prize_item.put("related_id", drawLottery.getRelatedId());
			prize_item.put("image", drawLottery.getImage());
			prize_item.put("adjust_status", drawLottery.getAdjustStatus());
			prize_item.put("adjust_time", drawLottery.getAdjustTime());
			prize_item.put("adjust_type", drawLottery.getAdjustType());
			prize_item.put("adjust_count", drawLottery.getAdjustCount());
			prize_item.put("last_adjust_time", drawLottery.getLastAdjustTime() < activity_start_time ? activity_start_time : drawLottery.getLastAdjustTime());
			prize_item.put("percent", drawLottery.getPercent());
			prize_item.put("weight", drawLottery.getWeight());
			prize_item.put("product_id", drawLottery.getProductId());
			prize_item.put("jiu_coin", drawLottery.getJiuCoin());
			prize.add(prize_item);
		}
		return prize;
	}
	
	
	
}
