package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.DrawLottery;

/**
 * @author jeff.zhan
 * @version 2016年11月3日 下午8:37:24
 * 
 */

@DBMaster
public interface DrawLotteryMapper {

	List<DrawLottery> load();

	DrawLottery getFirstLottery();

	int updateLastAdjustTime(@Param("id") Long id, @Param("lastAdjustTime") Long lastAdjustTime);

	DrawLottery getById(@Param("id") long id);
	
}
