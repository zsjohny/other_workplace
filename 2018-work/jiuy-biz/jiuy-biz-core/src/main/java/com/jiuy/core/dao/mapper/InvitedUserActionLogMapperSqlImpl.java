package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.InvitedUserActionLog;


/**
 * @author jeff.zhan
 * @version 2016年12月21日 下午4:08:25
 * 
 */

@Repository
public class InvitedUserActionLogMapperSqlImpl implements InvitedUserActionLogMapper {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public InvitedUserActionLog getByUserId(long userId, int action) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("userId", userId);
		params.put("action", action);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.InvitedUserActionLogMapperSqlImpl.getByUserId", params);
	}
}
