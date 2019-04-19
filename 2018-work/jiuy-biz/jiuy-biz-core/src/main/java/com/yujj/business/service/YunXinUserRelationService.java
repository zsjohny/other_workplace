package com.yujj.business.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.yunxin.UserYxRelation;
import com.yujj.dao.mapper.UserYxRelationMapper;

@Service
public class YunXinUserRelationService {



	@Autowired
	private UserYxRelationMapper userYxRelationMapper;

	public Map<Long, UserYxRelation> getUserYxRelations(Collection<Long> userIds) {
		Map<Long, UserYxRelation> result = userYxRelationMapper
				.getUserYxRelations(userIds);

		if (result == null) {
			result = new HashMap<Long, UserYxRelation>();
		}

		return result;
	}
	
	public int createYxUser(UserYxRelation userRelation) {
		return userYxRelationMapper.saveOrUpdate(userRelation);
	}

	public int updateYxUser(UserYxRelation userRelation) {
		return userYxRelationMapper.saveOrUpdate(userRelation);
	}

	public int updateToken(Long userId, String token) {
		return userYxRelationMapper.updateUserToken(userId, token);
	}

}
	