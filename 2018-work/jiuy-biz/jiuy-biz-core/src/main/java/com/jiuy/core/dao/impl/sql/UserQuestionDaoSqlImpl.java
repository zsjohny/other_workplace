package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.UserQuestionDao;
import com.jiuy.core.meta.UserQuestion;
import com.jiuyuan.entity.query.PageQuery;
@Repository
public class UserQuestionDaoSqlImpl implements UserQuestionDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<UserQuestion> search(String content, long yJJNumber, long startTime, long endTime, PageQuery pageQuery) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("content", content);
		params.put("yJJNumber", yJJNumber);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.UserQuestionDaoSqlImpl.search",params);
	}

	@Override
	public int searchCouont(String content, long yJJNumber, long startTime, long endTime) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("content", content);
		params.put("yJJNumber", yJJNumber);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.UserQuestionDaoSqlImpl.searchCount",params);
	}

}
