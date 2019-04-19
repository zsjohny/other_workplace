package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.DrawLottery;
import com.jiuyuan.service.common.MemcachedService;
import com.yujj.dao.mapper.DrawLotteryMapper;

/**
 * @author jeff.zhan
 * @version 2016年11月10日 下午7:21:34
 * 
 */

@Service
public class DrawLotteryService {
	
	@Autowired
	private DrawLotteryMapper drawLotteryMapper;
	
    @Autowired
    private MemcachedService memcachedService;

	public DrawLottery getById(long id) {
		String groupKey = MemcachedKey.GROUP_KEY_DROW_LOTTERY;
		String key = id + "";
		Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
        	return (DrawLottery)obj;
        } else {
        	DrawLottery drawLottery = drawLotteryMapper.getById(id);
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, drawLottery);
            return drawLottery;
        }
		
	}
}
