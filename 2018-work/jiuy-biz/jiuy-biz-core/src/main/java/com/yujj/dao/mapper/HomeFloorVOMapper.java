package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
//import com.store.entity.HomeFloorVO;
import com.yujj.entity.homepage.HomeFloorVO;

@DBMaster
public interface HomeFloorVOMapper {

	public List<HomeFloorVO> getHomeFloors(@Param("pageQuery") PageQuery pageQuery);
	
	public List<HomeFloorVO> getHomeFloors185(@Param("pageQuery") PageQuery pageQuery);
	
	public int getHomeFloorCount();
	
	public int getHomeFloorCount185();
	
	public int getHomeFloorCount186(@Param("activityPlaceId") Long activityPlaceId);
	
	public List<HomeFloorVO> getHomeFloors186(@Param("pageQuery") PageQuery pageQuery, @Param("activityPlaceId") Long activityPlaceId);

	
	public int getHomeFloorCount187(@Param("type") int typeValue, @Param("relatedId") Long relatedId);

	public List<HomeFloorVO> getHomeFloors187(@Param("pageQuery") PageQuery pageQuery, @Param("type") int typeValue, @Param("relatedId") Long relatedId);

}
