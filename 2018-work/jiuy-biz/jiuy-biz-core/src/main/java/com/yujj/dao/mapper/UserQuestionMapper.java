package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.entity.community.UserQuestion;

@DBMaster
public interface UserQuestionMapper {

	int addQuestion(UserQuestion userQuestion);

    List<UserQuestion> getQuestionList(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery);

}
