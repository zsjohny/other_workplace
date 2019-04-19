package com.jiuy.core.service;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.entity.DrawLottery;

/**
 * @author jeff.zhan
 * @version 2016年11月4日 上午10:29:37
 * 
 */

public interface DrawLotteryService {

	void overWrite(JSONObject jsonObject);

	int update(DrawLottery drawLottery);

}
