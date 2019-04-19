package com.yujj.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.query.PageQuery;
import com.yujj.dao.mapper.UserQuestionMapper;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.community.UserQuestion;

@Service
public class UserQuestionService {

    @Autowired
    private UserQuestionMapper userQuestionMapper;

    
    public int addQuestion(String content, UserDetail userDetail) {
    	UserQuestion userQuestion = new UserQuestion();
    	userQuestion.setContent(content);
    	userQuestion.setYjjNumber(userDetail.getUser().getyJJNumber());
    	userQuestion.setUserId(userDetail.getUserId());
    	userQuestion.setCreateTime(System.currentTimeMillis());
    	return userQuestionMapper.addQuestion(userQuestion);
    }

	
    public List<UserQuestion> getUserQuestionList(long userId,  PageQuery pageQuery) {
		return userQuestionMapper.getQuestionList(userId, pageQuery);
	}

}
