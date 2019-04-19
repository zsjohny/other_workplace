package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.GroundCustomerStageChangeMapper;
import com.jiuyuan.dao.mapper.supplier.GroundDayReportMapper;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.entity.newentity.UserTimeRule;
import com.jiuyuan.entity.newentity.ground.GroundConstant;
import com.jiuyuan.entity.newentity.ground.GroundCustomerStageChange;
import com.jiuyuan.entity.newentity.ground.GroundDayReport;
import com.jiuyuan.util.DateUtil;

@Service
public class GroundCustomerStageChangeService implements IGroundCustomerStageChangeService {
	
	private static final long ONE_DAY = 1l*24*60*60*1000;
	@Autowired
	private GroundCustomerStageChangeMapper groundCustomerStageChangeMapper;
	
	@Autowired
	private IGroundConditionRuleService groundConditionRuleService;
	
	@Autowired
	private GroundDayReportMapper groundDayReportMapper;
	
	@Autowired
	private GroundUserMapper groundUserMapper;
	
    /* (non-Javadoc)
	 * @see com.jiuyuan.service.common.IGroundCustomerStageChangeService#insertGroundCustomerStageChange(long, java.lang.Long, java.lang.String, int, int, int, com.jiuyuan.entity.newentity.UserTimeRule)
	 */
    @Override
	@Transactional(rollbackFor = Exception.class)
	public int insertGroundCustomerStageChange(long storeId, Long groundUserId, String superIds, int oneStageTime,
			int twoStageTime, int threeStageTime, UserTimeRule userTimeRule) {
		int totalCount = 0;
		
		//第一条增加客户-门店注册 count:1 注册审核通过时间，日期
		GroundCustomerStageChange groundCustomerStageChange1 = new GroundCustomerStageChange(
				                  superIds, groundUserId, storeId, 
				                  System.currentTimeMillis(), DateUtil.getToday(), 
				                  Long.valueOf(GroundCustomerStageChange.ADD_CUSTOMER),
				                  GroundConstant.STAGE_TYPE_FIRST,
				                  System.currentTimeMillis());
//		groundCustomerStageChange1.setStoreId(storeId);
//		groundCustomerStageChange1.setSuperIds(superIds);
//		groundCustomerStageChange1.setGroundUserId(groundUserId);
//		
//		groundCustomerStageChange1.setCreateTime(System.currentTimeMillis());
//		groundCustomerStageChange1.setStageChangeTime(System.currentTimeMillis());
//		groundCustomerStageChange1.setStageChangeDate(DateUtil.getToday());
//
//		groundCustomerStageChange1.setStageType(GroundCustomerStageChange.FIRST_STAGE_TYPE);
//		groundCustomerStageChange1.setChangeCount(Long.valueOf(GroundCustomerStageChange.ADD_CUSTOMER));
//		
		int count1 = groundCustomerStageChangeMapper.insert(groundCustomerStageChange1);
		totalCount = totalCount+count1;
		//第二条减少客户-第一阶段 count:-1 第一阶段结束时间，日期
		GroundCustomerStageChange groundCustomerStageChange2 = new GroundCustomerStageChange(
				                  superIds, groundUserId, storeId, 
				                  System.currentTimeMillis()+ONE_DAY*userTimeRule.getStage1(),
				                  oneStageTime, 
				                  Long.valueOf(GroundCustomerStageChange.DELETE_CUSTOMER), 
				                  GroundConstant.STAGE_TYPE_FIRST, 
				                  System.currentTimeMillis());
//		groundCustomerStageChange2.setStoreId(storeId);
//		groundCustomerStageChange2.setSuperIds(superIds);
//		groundCustomerStageChange2.setGroundUserId(groundUserId);
//		groundCustomerStageChange2.setCreateTime(System.currentTimeMillis());
//		
//		groundCustomerStageChange2.setStageChangeTime(System.currentTimeMillis()+ONE_DAY*userTimeRule.getStage1());
//		groundCustomerStageChange2.setStageChangeDate(oneStageTime);
//
//		groundCustomerStageChange2.setStageType(GroundCustomerStageChange.FIRST_STAGE_TYPE);
//		groundCustomerStageChange2.setChangeCount(Long.valueOf(GroundCustomerStageChange.DELETE_CUSTOMER));
//		
		int count2 = groundCustomerStageChangeMapper.insert(groundCustomerStageChange2);
		totalCount = totalCount+count2;
		//第三条增加客户-第二阶段count:1 第二阶段开始时间，日期(即第一阶段结束时间，日期)
		GroundCustomerStageChange groundCustomerStageChange3 = new GroundCustomerStageChange(
				                  superIds, groundUserId, storeId,
				                  System.currentTimeMillis()+ONE_DAY*userTimeRule.getStage1(),
				                  oneStageTime,
				                  Long.valueOf(GroundCustomerStageChange.ADD_CUSTOMER),
				                  GroundConstant.STAGE_TYPE_SECOND,
				                  System.currentTimeMillis());
//		groundCustomerStageChange3.setStoreId(storeId);
//		groundCustomerStageChange3.setSuperIds(superIds);
//		groundCustomerStageChange3.setGroundUserId(groundUserId);
//		groundCustomerStageChange3.setCreateTime(System.currentTimeMillis());
//		
//		groundCustomerStageChange3.setStageChangeTime(System.currentTimeMillis()+ONE_DAY*userTimeRule.getStage1());
//		groundCustomerStageChange3.setStageChangeDate(oneStageTime);
//
//		groundCustomerStageChange3.setStageType(GroundCustomerStageChange.SECOND_STAGE_TYPE);
//		groundCustomerStageChange3.setChangeCount(Long.valueOf(GroundCustomerStageChange.ADD_CUSTOMER));
//		
		int count3 = groundCustomerStageChangeMapper.insert(groundCustomerStageChange3);
		totalCount = totalCount+count3;
		//第四条减少客户-第二阶段count:-1 第二阶段结束时间，日期
		GroundCustomerStageChange groundCustomerStageChange4 = new GroundCustomerStageChange(
				                  superIds, groundUserId, storeId,
				                  System.currentTimeMillis()+ONE_DAY*userTimeRule.getStage2(),
				                  twoStageTime,
				                  Long.valueOf(GroundCustomerStageChange.DELETE_CUSTOMER),
				                  GroundConstant.STAGE_TYPE_SECOND,
				                  System.currentTimeMillis());
//		groundCustomerStageChange4.setStoreId(storeId);
//		groundCustomerStageChange4.setSuperIds(superIds);
//		groundCustomerStageChange4.setGroundUserId(groundUserId);
//		groundCustomerStageChange4.setCreateTime(System.currentTimeMillis());
//		
//		groundCustomerStageChange4.setStageChangeTime(System.currentTimeMillis()+ONE_DAY*userTimeRule.getStage2());
//		groundCustomerStageChange4.setStageChangeDate(twoStageTime);
//
//		groundCustomerStageChange4.setStageType(GroundCustomerStageChange.SECOND_STAGE_TYPE);
//		groundCustomerStageChange4.setChangeCount(Long.valueOf(GroundCustomerStageChange.DELETE_CUSTOMER));
//		
		int count4 = groundCustomerStageChangeMapper.insert(groundCustomerStageChange4);
		totalCount = totalCount+count4;
		//第五条增加客户-第三阶段count:1 第三阶段开始时间，日期
		GroundCustomerStageChange groundCustomerStageChange5 = new GroundCustomerStageChange();
		groundCustomerStageChange5.setStoreId(storeId);
		groundCustomerStageChange5.setSuperIds(superIds);
		groundCustomerStageChange5.setGroundUserId(groundUserId);
		groundCustomerStageChange5.setCreateTime(System.currentTimeMillis());
		
		groundCustomerStageChange5.setStageChangeTime(System.currentTimeMillis()+ONE_DAY*userTimeRule.getStage2());
		groundCustomerStageChange5.setStageChangeDate(twoStageTime);

		groundCustomerStageChange5.setStageType(GroundConstant.STAGE_TYPE_THIRD);
		groundCustomerStageChange5.setChangeCount(Long.valueOf(GroundCustomerStageChange.ADD_CUSTOMER));
		
		int count5 = groundCustomerStageChangeMapper.insert(groundCustomerStageChange5);
		totalCount = totalCount+count5;
		//第六条减少客户-第三阶段count:-1 第三阶段结束时间，日期
		GroundCustomerStageChange groundCustomerStageChange6 = new GroundCustomerStageChange();
		groundCustomerStageChange6.setStoreId(storeId);
		groundCustomerStageChange6.setSuperIds(superIds);
		groundCustomerStageChange6.setGroundUserId(groundUserId);
		groundCustomerStageChange6.setCreateTime(System.currentTimeMillis());
		
		groundCustomerStageChange6.setStageChangeTime(System.currentTimeMillis()+ONE_DAY*userTimeRule.getStage3());
		groundCustomerStageChange6.setStageChangeDate(threeStageTime);

		groundCustomerStageChange6.setStageType(GroundConstant.STAGE_TYPE_THIRD);
		groundCustomerStageChange6.setChangeCount(Long.valueOf(GroundCustomerStageChange.DELETE_CUSTOMER));
		
		int count6 = groundCustomerStageChangeMapper.insert(groundCustomerStageChange6);
		totalCount = totalCount+count6;
		//第七条增加客户-其他阶段 count:1
		GroundCustomerStageChange groundCustomerStageChange7 = new GroundCustomerStageChange();
		groundCustomerStageChange7.setStoreId(storeId);
		groundCustomerStageChange7.setSuperIds(superIds);
		groundCustomerStageChange7.setGroundUserId(groundUserId);
		groundCustomerStageChange7.setCreateTime(System.currentTimeMillis());
		
		groundCustomerStageChange7.setStageChangeTime(System.currentTimeMillis()+ONE_DAY*userTimeRule.getStage3());
		groundCustomerStageChange7.setStageChangeDate(threeStageTime);

		groundCustomerStageChange7.setStageType(GroundConstant.STAGE_TYPE_OTHER);
		groundCustomerStageChange7.setChangeCount(Long.valueOf(GroundCustomerStageChange.ADD_CUSTOMER));
		
		int count7 = groundCustomerStageChangeMapper.insert(groundCustomerStageChange7);
		totalCount = totalCount+count7;
		return totalCount;
		
	}
    /**
     * 执行地推客户阶段变化，形成日报表
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void executeGroundCustomerStageChange(int date) {
		//计算个人第一阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
		List<Map<String,Object>> firstStageIndividualClientChange= groundCustomerStageChangeMapper.getStageIndividualClientChange(date, GroundConstant.STAGE_TYPE_FIRST);
		//计算个人第二阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
		List<Map<String,Object>> secondStageIndividualClientChange = groundCustomerStageChangeMapper.getStageIndividualClientChange(date, GroundConstant.STAGE_TYPE_SECOND);
		//计算个人第三阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
		List<Map<String,Object>> thirdStageIndividualClientChange = groundCustomerStageChangeMapper.getStageIndividualClientChange(date, GroundConstant.STAGE_TYPE_THIRD);
		//计算个人其他阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
		List<Map<String,Object>> forthStageIndividualClientChange = groundCustomerStageChangeMapper.getStageIndividualClientChange(date, GroundConstant.STAGE_TYPE_OTHER);
		//计算团队第一阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
		List<Map<String,Object>> firstStageTeamClientChange= groundCustomerStageChangeMapper.getStageTeamClientChange(date, GroundConstant.STAGE_TYPE_FIRST);
		//计算团队第二阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
		List<Map<String,Object>> secondStageTeamClientChange = groundCustomerStageChangeMapper.getStageTeamClientChange(date, GroundConstant.STAGE_TYPE_SECOND);
		//计算团队第三阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
		List<Map<String,Object>> thirdStageTeamClientChange = groundCustomerStageChangeMapper.getStageTeamClientChange(date, GroundConstant.STAGE_TYPE_THIRD);
		//计算团队其他阶段当日变化客户数，可为增加客户数或减少客户数，可为正数和负数
		List<Map<String,Object>> forthStageTeamClientChange = groundCustomerStageChangeMapper.getStageTeamClientChange(date, GroundConstant.STAGE_TYPE_OTHER);
		    if(firstStageIndividualClientChange != null&& firstStageIndividualClientChange.size() >0){
		    	int i = groundDayReportMapper.insertStageIndividualClientChange(firstStageIndividualClientChange,date, GroundConstant.STAGE_TYPE_FIRST);
		    }
		    if(secondStageIndividualClientChange != null&& secondStageIndividualClientChange.size()>0){
		    	int i = groundDayReportMapper.insertStageIndividualClientChange(secondStageIndividualClientChange,date, GroundConstant.STAGE_TYPE_SECOND);
		    }
		    if(thirdStageIndividualClientChange != null&& thirdStageIndividualClientChange.size()>0){
		    	int i = groundDayReportMapper.insertStageIndividualClientChange(thirdStageIndividualClientChange,date,  GroundConstant.STAGE_TYPE_THIRD);
		    }
		    if(forthStageIndividualClientChange != null&& forthStageIndividualClientChange.size()>0){
		    	int i = groundDayReportMapper.insertStageIndividualClientChange(forthStageIndividualClientChange,date,  GroundConstant.STAGE_TYPE_OTHER);
		    }
			if(firstStageTeamClientChange != null&& firstStageTeamClientChange.size()>0){
				int i = groundDayReportMapper.insertStageTeamClientChange(firstStageTeamClientChange,date,  GroundConstant.STAGE_TYPE_FIRST);
			}
			if(secondStageTeamClientChange != null&& secondStageTeamClientChange.size()>0){
				int i = groundDayReportMapper.insertStageTeamClientChange(secondStageTeamClientChange,date,  GroundConstant.STAGE_TYPE_SECOND);
			}
			if(thirdStageTeamClientChange != null&& thirdStageTeamClientChange.size()>0){
				int i = groundDayReportMapper.insertStageTeamClientChange(thirdStageTeamClientChange,date,  GroundConstant.STAGE_TYPE_THIRD);
			}
			if(forthStageTeamClientChange != null&& forthStageTeamClientChange.size()>0){
				int i = groundDayReportMapper.insertStageTeamClientChange(forthStageTeamClientChange,date,  GroundConstant.STAGE_TYPE_OTHER);
				
			}
	}

}
