package com.jiuy.core.service;

import java.util.List;

import com.jiuy.core.meta.UserQuestion;
import com.jiuyuan.entity.query.PageQuery;

public interface UserQuestionService {
	
	List<UserQuestion> search(String content, long yJJNumber, long startTime, long endTime,PageQuery pageQuery);
	
	int searchCount(String content, long yJJNumber, long startTime, long endTime);
}
