package com.jiuy.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.UserQuestionDao;
import com.jiuy.core.meta.UserQuestion;
import com.jiuyuan.entity.query.PageQuery;

@Service("userQuestionService")
public class UserQuestionServiceImpl implements UserQuestionService{
	
	@Resource
	private UserQuestionDao uqDao;

	@Override
	public List<UserQuestion> search(String content, long yJJNumber, long startTime, long endTime, PageQuery pageQuery) {
		return uqDao.search(content, yJJNumber, startTime, endTime, pageQuery);
	}

	@Override
	public int searchCount(String content, long yJJNumber, long startTime, long endTime) {
		return uqDao.searchCouont(content, yJJNumber, startTime, endTime);
	}

}
