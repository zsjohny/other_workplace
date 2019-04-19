package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.GroundUserMonthCost;
import com.jiuyuan.entity.newentity.ground.GroundDayReport;


@DBMaster
public interface GroundDayReportMapper extends BaseMapper<GroundDayReport> {

	int insertStageIndividualClientChange(@Param("stageIndividualClientChange") List<Map<String, Object>> StageIndividualClientChange,@Param("date")int date,@Param("stageType")int stageType );

	int insertStageTeamClientChange(@Param("stageTeamClientChange") List<Map<String, Object>> StageTeamClientChange,@Param("date")int date,@Param("stageType")int stageType);
	
	List<GroundUserMonthCost> getGroundUserMonthCost(@Param("date1")long date1,@Param("date2")long date2);
	
	/**
	 * 地推人员业绩查询
	 * @param groundUserId
	 * @param statisticsTimeStart
	 * @param statisticsTimeEnd
	 * @param name
	 * @param phone
	 * @param grandRoleType
	 * @param province
	 * @param city
	 * @param district
	 * @param pName
	 * @param pPhone
	 * @param individualTotalSaleAmountMin
	 * @param individualTotalSaleAmountMax
	 * @param customerRegisterCountMin
	 * @param customerRegisterCountMax
	 * @param customerActiveCountMin
	 * @param customerActiveCountMax
	 * @param page
	 * @param pId
	 * @return
	 */
	List<Map<String, Object>> selectPageList(@Param("groundUserId")long groundUserId, 
			@Param("statisticsTimeStart")long statisticsTimeStart, 
			@Param("statisticsTimeEnd")long statisticsTimeEnd,
			@Param("name")String name, 
			@Param("phone")String phone, 
			@Param("grandRoleType")int grandRoleType, 
			@Param("province")String province, 
			@Param("city")String city, 
			@Param("district")String district,
			@Param("administratorId")long administratorId, 
			@Param("pPhone")String pPhone, 
			@Param("individualTotalSaleAmountMin")double individualTotalSaleAmountMin,
			@Param("individualTotalSaleAmountMax")double individualTotalSaleAmountMax,
			@Param("customerRegisterCountMin")int customerRegisterCountMin, 
			@Param("customerRegisterCountMax")int customerRegisterCountMax, 
			@Param("customerActiveCountMin")int customerActiveCountMin, 
			@Param("customerActiveCountMax")int customerActiveCountMax,
			@Param("page")Page<Map<String, Object>> page 
			);

	
	/**
	 * 获取我的奖金列表
	 * @param groundUserId
	 * @return
	 */
	List<Map<String, Object>> getMyBonusList(long groundUserId);
	
}
