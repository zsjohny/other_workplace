package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.DrawLottery;

/**
 * @author jeff.zhan
 * @version 2016年11月3日 下午7:52:56
 * 
 */

@Repository
public class DrawLotteryMapperSqlImpl implements DrawLotteryMapper {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int add(DrawLottery drawLottery) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.DrawLotteryMapperSqlImpl.add", drawLottery);
	}

	@Override
	public int update(DrawLottery drawLottery) {
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.DrawLotteryMapperSqlImpl.update", drawLottery);
	}

	@Override
	public int delete(long id) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("id", id);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.DrawLotteryMapperSqlImpl.delete", params);
	}

	@Override
	public List<DrawLottery> load() {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.DrawLotteryMapperSqlImpl.load");
	}

	@Override
	public int batchAdd(Collection<DrawLottery> drawLotterys) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.DrawLotteryMapperSqlImpl.batchAdd", drawLotterys);
	}

	@Override
	public int batchUpdate(Collection<DrawLottery> drawLotteries) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("drawLotteries", drawLotteries);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.DrawLotteryMapperSqlImpl.batchUpdate", params);
	}

	@Override
	public int removeAll() {
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.DrawLotteryMapperSqlImpl.removeAll");
	}

	@Override
	public int update(Long id, long lastAdjustTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("id", id);
		params.put("lastAdjustTime", lastAdjustTime);
		params.put("updateTime", System.currentTimeMillis());
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.DrawLotteryMapperSqlImpl.update", params);
	}
	
	
}
