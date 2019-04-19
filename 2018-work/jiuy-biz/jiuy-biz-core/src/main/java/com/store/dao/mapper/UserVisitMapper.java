package com.store.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.visit.UserVisitHistory;


@DBMaster
public interface UserVisitMapper {

    void addVisitHistory(@Param("userId") long userId, @Param("guideFlag") int guideFlag, @Param("ids") Long[] ids, @Param("time") long time, @Param("type") int type);

	List<UserVisitHistory> getUserVisitList(@Param("userId") Long userId, @Param("guideFlag") int guideFlag,
			@Param("pageQuery") PageQuery pageQuery);

	@MapKey("relatedId")
	Map<Long, UserVisitHistory> getVisits();
	
	int getUserVisitListCount(@Param("userId") Long userId, @Param("guideFlag") int guideFlag);

	int deleteAll(@Param("userId") Long userId);

	List<Product> getBuyGuessProduct(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery, @Param("count") int count, @Param("currentTime") long currentTime);

	List<Product> getSeeAgainProduct(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery, @Param("count") int count, @Param("currentTime") long currentTime);
}
