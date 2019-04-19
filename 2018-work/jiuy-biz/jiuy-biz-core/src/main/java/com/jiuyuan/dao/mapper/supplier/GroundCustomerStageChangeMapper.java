package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ground.GroundCustomerStageChange;


@DBMaster
public interface GroundCustomerStageChangeMapper extends BaseMapper<GroundCustomerStageChange> {

	int getPersonalCusAddCount(@Param("groundUserId") long groundUserId,@Param("today") Integer today);
	
	int getGroupCusAddCount(@Param("groundUserId") long groundUserId,@Param("today") Integer today);

	List<Map<String,Object>> getStageIndividualClientChange(@Param("date")int date,@Param("stageType") int stageType);

	List<Map<String, Object>> getStageTeamClientChange(@Param("date")int date,@Param("stageType") int stageType);

	
}
