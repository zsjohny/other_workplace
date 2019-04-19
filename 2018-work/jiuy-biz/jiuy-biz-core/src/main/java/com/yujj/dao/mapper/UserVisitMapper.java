package com.yujj.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.visit.UserVisitHistory;
import com.yujj.entity.product.Product;

@DBMaster
public interface UserVisitMapper {

    void addVisitHistory(@Param("userId") long userId, @Param("ids") Long[] ids, @Param("time") long time);

	List<UserVisitHistory> getUserVisitList(@Param("userId") Long userId,
			@Param("pageQuery") PageQuery pageQuery);

	@MapKey("relatedId")
	Map<Long, UserVisitHistory> getVisits();
	
	int getUserVisitListCount(@Param("userId") Long userId);

	int deleteAll(@Param("userId") Long userId);

	List<Product> getBuyGuessProduct(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery, @Param("count") int count, @Param("currentTime") long currentTime);

	List<Product> getSeeAgainProduct(@Param("userId") long userId, @Param("pageQuery") PageQuery pageQuery, @Param("count") int count, @Param("currentTime") long currentTime);
}
