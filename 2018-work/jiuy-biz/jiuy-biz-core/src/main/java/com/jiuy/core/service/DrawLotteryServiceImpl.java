package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.mapper.DrawLotteryMapper;
import com.jiuyuan.entity.DrawLottery;

/**
 * @author jeff.zhan
 * @version 2016年11月4日 上午10:30:01
 * 
 */

@Service
public class DrawLotteryServiceImpl implements DrawLotteryService {
	
	@Autowired
	private DrawLotteryMapper drawLotteryMapper;
	
	@Override
	public void overWrite(JSONObject draw_lottery) {
		drawLotteryMapper.removeAll();
		
		JSONArray prize = draw_lottery.getJSONArray("prize");
		if (prize == null) {
			return;
		}
		List<DrawLottery> drawLotteries = assemblePublishedLottery(prize);
		if (drawLotteries.size() > 0) {
			drawLotteryMapper.batchUpdate(drawLotteries);
		}
	}

	private List<DrawLottery> assemblePublishedLottery(JSONArray prize) {
		List<DrawLottery> drawLotteries = new ArrayList<>();
		for (Object object : prize) {
			JSONObject prize_item = (JSONObject) object;
			DrawLottery drawLottery = new DrawLottery();
			drawLottery.setAdjustCount(prize_item.getInteger("adjust_count"));
			drawLottery.setAdjustStatus(prize_item.getInteger("adjust_status"));
			drawLottery.setAdjustTime(prize_item.getLong("adjust_time"));
			drawLottery.setAdjustType(prize_item.getInteger("adjust_type"));
			drawLottery.setCount(prize_item.getInteger("count"));
			drawLottery.setId(prize_item.getLong("id"));
			drawLottery.setImage(prize_item.getString("image"));
			drawLottery.setName(prize_item.getString("name"));
			drawLottery.setPercent(prize_item.getInteger("percent"));
			drawLottery.setRankName(prize_item.getString("rank_name"));
			drawLottery.setRelatedId(prize_item.getLong("related_id"));
			drawLottery.setStatus(0);
			drawLottery.setType(prize_item.getInteger("type"));
			drawLottery.setWeight(prize_item.getInteger("weight") == null ? 0 : prize_item.getInteger("weight"));
			drawLottery.setProductId(prize_item.getLong("product_id"));
			drawLottery.setJiuCoin(prize_item.getInteger("jiu_coin"));
			drawLotteries.add(drawLottery);
		}
		return drawLotteries;
	}

	@Override
	public int update(DrawLottery drawLottery) {
		long currentTime = System.currentTimeMillis();
		if (drawLottery.getAdjustStatus() != -1) {
			if (drawLottery.getAdjustType() == 1) {
				drawLottery.setLastAdjustTime(currentTime);
			} else if (drawLottery.getAdjustType() == 2) {
				DateTime dateTime = new DateTime(currentTime);
				DateTime dTime = null;
				if (drawLottery.getAdjustTime() == -1) {
					dTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getHourOfDay(), 0);
					drawLottery.setLastAdjustTime(dTime.getMillis());
				} else {
					dTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), new Integer(drawLottery.getAdjustTime().toString()), 0);
					drawLottery.setLastAdjustTime(dTime.getMillis() > currentTime ? dTime.getMillis() - DateUtils.MILLIS_PER_DAY : dTime.getMillis());
				}
			}
		}
		return drawLotteryMapper.update(drawLottery);
	}
	
}
