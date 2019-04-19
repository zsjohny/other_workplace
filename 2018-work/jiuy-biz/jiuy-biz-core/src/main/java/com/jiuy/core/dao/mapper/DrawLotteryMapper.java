package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.DrawLottery;

/**
 * @author jeff.zhan
 * @version 2016年11月3日 下午7:52:42
 * 
 */

public interface DrawLotteryMapper {

	int add(DrawLottery drawLottery);

	int update(DrawLottery drawLottery);

	int delete(long id);

	List<DrawLottery> load();

	int batchAdd(Collection<DrawLottery> drawLotterys);

	int batchUpdate(Collection<DrawLottery> drawLotteries);

	int removeAll();

	int update(Long id, long lastAdjustTime);

}
