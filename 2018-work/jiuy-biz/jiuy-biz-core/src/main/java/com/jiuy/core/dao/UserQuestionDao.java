package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.meta.UserQuestion;
import com.jiuyuan.entity.query.PageQuery;

public interface UserQuestionDao {
	
	List<UserQuestion> search(String content, long yJJNumber, long startTime, long endTime,PageQuery pageQuery);
	
	int searchCouont(String content, long yJJNumber, long startTime, long endTime);
}
