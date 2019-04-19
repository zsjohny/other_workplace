package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jiuyuan.dao.mapper.supplier.GroundBonusDayReportMapper;
import com.jiuyuan.dao.mapper.supplier.GroundBonusGrantMapper;
import com.jiuyuan.dao.mapper.supplier.GroundDayReportMapper;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.ground.GroundBonusDayReport;
import com.jiuyuan.entity.newentity.ground.GroundDayReport;
import com.jiuyuan.util.DateUtil;

@Service
public class MyIncomeService {
	private static final Logger logger = LoggerFactory.getLogger(MyIncomeService.class);
	
	@Autowired
	private GroundUserMapper groundUserMapper;
	
	@Autowired
	private GroundDayReportMapper groundDayReportMapper;
	
	@Autowired
	private GroundBonusGrantMapper groundBonusGrantMapper;
	
	@Autowired
	private GroundBonusDayReportMapper groundBonusDayReportMapper;
	
	/**
	 * 我的收入
	 * @param grandUserId
	 * @return
	 */
	public Map<String, Object> getMyIncome(long groundUserId) {
		try {
			//获取日报表列表
			Map<String,Object> myIncome = new HashMap<String,Object>();
			Wrapper<GroundDayReport> yesterdayIncomeWrapper = new EntityWrapper<GroundDayReport>().eq("ground_user_id", groundUserId)
					.eq("report_date", DateUtil.getyesterday()).orderBy("report_date",false);
			List<GroundDayReport> groundDayReportYesterdayList = groundDayReportMapper.selectList(yesterdayIncomeWrapper);
			
			//昨日总收入
			Map<String,Object> yesterdayIncome = new HashMap<String,Object>();
			if(groundDayReportYesterdayList.size()>0){
				GroundDayReport groundDayReport = groundDayReportYesterdayList.get(0);
				yesterdayIncome.put("yesterdayAllIncome", groundDayReport.getDayTotalCost());
				yesterdayIncome.put("yesterdayRegisterIncome", groundDayReport.getDayRegCost());
				yesterdayIncome.put("yesterdayActivationIncome", groundDayReport.getDayActivateCost());
				yesterdayIncome.put("yesterdayDealIncome", groundDayReport.getDayOrderCost());
				myIncome.put("yesterdayIncome", yesterdayIncome);
			}else{
				//昨日总收入
				yesterdayIncome.put("yesterdayAllIncome", 0.00);
				yesterdayIncome.put("yesterdayRegisterIncome", 0.00);
				yesterdayIncome.put("yesterdayActivationIncome", 0.00);
				yesterdayIncome.put("yesterdayDealIncome", 0.00);
				myIncome.put("yesterdayIncome", yesterdayIncome);
			} 
			logger.info("我的收入-昨日总收入:"+yesterdayIncome);
			
			GroundUser groundUser = groundUserMapper.selectById(groundUserId);
			//余额
			Map<String,Object> balance = new HashMap<String,Object>();
			//获取待入账金额
			double balanceToBeCredited = groundBonusGrantMapper.getBalanceToBeCredited(groundUserId,DateUtil.getTodayStart());
			//获取已入帐金额
			double balanceCredited = groundUser.getAvailableBalance();
			balance.put("balanceToBeCredited", balanceToBeCredited);
			balance.put("balanceCredited", balanceCredited);
			
			balance.put("balanceAll", String.format("%.2f", (balanceToBeCredited+balanceCredited)));
			myIncome.put("balance", balance);
			logger.info("我的收入-余额:"+balance);
			
			
			//历史收入累计
			Map<String,Object> historyIncome = new HashMap<String,Object>();
			historyIncome.put("historyIncomeAll", groundUser.getTotalCost());
			historyIncome.put("registerIncomeAll", groundUser.getRegCost());
			historyIncome.put("activationIncomeAll", groundUser.getActivateCost());
			historyIncome.put("dealIncomeAll", groundUser.getOrderCost());
			myIncome.put("historyIncome", historyIncome);
			logger.info("我的收入-历史收入累计:"+historyIncome);
	
			//奖金动态
			Wrapper<GroundDayReport> myIncomeWrapper = new EntityWrapper<GroundDayReport>().eq("ground_user_id", groundUserId).orderBy("report_date",false);
			List<GroundDayReport> groundDayReportList = groundDayReportMapper.selectPage(new Page<GroundDayReport>(1,10), myIncomeWrapper);
			List<Map<String,Object>> bonusList = new ArrayList<Map<String,Object>>();
			for (GroundDayReport groundDayReport : groundDayReportList) {
				//获取时间值
				int reportDate = groundDayReport.getReportDate();
				int yesterdayDate = DateUtil.getyesterday();
				String date = DateUtil.buildDateToStr(reportDate);
				if(reportDate == yesterdayDate){
					date = "昨天";
				}
				
				//个人
				Map<String,Object> individualBonus = new HashMap<String,Object>();
				individualBonus.put("time",date);
				individualBonus.put("incomeMoney", groundDayReport.getDayOneselfCost());
				individualBonus.put("registerPersonCount", groundDayReport.getDayOneselfRegBonusCount());
				individualBonus.put("activationPersonCount", groundDayReport.getActiveIndividualClientCount());
				individualBonus.put("dealPersonCount", groundDayReport.getTradeIndividualClientCount());
				individualBonus.put("name", "个人");
				bonusList.add(individualBonus);
				
				//团队
				Map<String,Object> teamBonus = new HashMap<String,Object>();
				teamBonus.put("time",date);
				teamBonus.put("incomeMoney", groundDayReport.getDayTeamCost());
				teamBonus.put("registerPersonCount", groundDayReport.getDayTeamRegBonusCount());
				teamBonus.put("activationPersonCount", groundDayReport.getActiveTeamClientCount());
				teamBonus.put("dealPersonCount", groundDayReport.getTradeTeamClientCount());
				teamBonus.put("name", "团队");
				bonusList.add(teamBonus);
			}
			myIncome.put("bonusList", bonusList);
			
			//只有城市经理才显示体现按钮
			if(groundUser.getUserType()==4){
				myIncome.put("isCityManager", 1);
			}else{
				myIncome.put("isCityManager", 0);
			}
			
			return myIncome;
		} catch (Exception e) {
			logger.error("我的收入e："+e.getMessage());
			throw new RuntimeException("我的收入e："+e.getMessage());
		}
	}

	/**
	 * 我的奖金
	 * @param grandUserId
	 * @param phoneNumber
	 * @param searchTimeStart
	 * @param searchTimeEnd
	 * @return
	 */
	public List<Map<String, Object>> getMyBonusList(long grandUserId, String searchTimeStart,
			String searchTimeEnd,int current,int size) {
		try {
			//获取日报表列表
			Wrapper<GroundDayReport> myIncomeWrapper = new EntityWrapper<GroundDayReport>().eq("ground_user_id", grandUserId);
			if(!StringUtils.isEmpty(searchTimeStart)){
				int timeStart = DateUtil.getDateInt(DateUtil.getLongTimeByDate(searchTimeStart));
				myIncomeWrapper.ge("report_date", timeStart);
			}
			if(!StringUtils.isEmpty(searchTimeEnd)){
				int timeEnd = DateUtil.getDateInt(DateUtil.getLongTimeByDate(searchTimeEnd));
				myIncomeWrapper.le("report_date", timeEnd);
			}
			myIncomeWrapper.orderBy("report_date",false);
			List<GroundDayReport> groundDayReportYesterdayList = groundDayReportMapper.selectPage(new Page<GroundDayReport>(current,size), myIncomeWrapper);
			
			//奖金动态
			List<Map<String,Object>> bonusList = new ArrayList<Map<String,Object>>();
			for (GroundDayReport groundDayReport : groundDayReportYesterdayList) {
				
				int reportDate = groundDayReport.getReportDate();
				int yesterdayDate = DateUtil.getyesterday();
				logger.info("reportDate:"+reportDate);
				logger.info("yesterdayDate:"+yesterdayDate);
				
				String date = DateUtil.buildDateToStr(reportDate);
				if(reportDate == yesterdayDate){
					date = "昨天";
				}

				//个人
				Map<String,Object> individualBonus = new HashMap<String,Object>();
				individualBonus.put("id",groundDayReport.getId());
				individualBonus.put("time",date);
				individualBonus.put("incomeMoney", groundDayReport.getDayOneselfCost());
				individualBonus.put("registerPersonCount", groundDayReport.getIndividualClientCount());
				individualBonus.put("activationPersonCount", groundDayReport.getActiveIndividualClientCount());
				individualBonus.put("dealPersonCount", groundDayReport.getTradeIndividualClientCount());
				individualBonus.put("name", "个人");
				bonusList.add(individualBonus);
				
				//团队
				Map<String,Object> teamBonus = new HashMap<String,Object>();
				teamBonus.put("id",groundDayReport.getId());
				teamBonus.put("time",date);
				teamBonus.put("incomeMoney", groundDayReport.getDayTeamCost());
				teamBonus.put("registerPersonCount", groundDayReport.getTeamClientCount());
				teamBonus.put("activationPersonCount", groundDayReport.getActiveTeamClientCount());
				teamBonus.put("dealPersonCount", groundDayReport.getTradeTeamClientCount());
				teamBonus.put("name", "团队");
				bonusList.add(teamBonus);
			}
			
			return bonusList;
		} catch (Exception e) {
			logger.error("我的奖金e："+e.getMessage());
			throw new RuntimeException("我的奖金e："+e.getMessage());
		}
		
	}

	/**
	 * 我的奖金动态(详细版)
	 * @param grandUserId
	 * @param searchTimeStart
	 * @param searchTimeEnd
	 * @param current
	 * @param size
	 * @return
	 */
	public List<Map<String, Object>> getMyDetailedBonusList(long grandUserId, String searchTimeStart,
			String searchTimeEnd, Integer current, Integer size) {
		try {
			//获取日报表列表
			Wrapper<GroundBonusDayReport> myIncomeWrapper = new EntityWrapper<GroundBonusDayReport>().eq("ground_user_id", grandUserId);
			if(!StringUtils.isEmpty(searchTimeStart)){
				int timeStart = DateUtil.getDateInt(DateUtil.getLongTimeByDate(searchTimeStart));
				myIncomeWrapper.ge("report_date", timeStart);
			}
			if(!StringUtils.isEmpty(searchTimeEnd)){
				int timeEnd = DateUtil.getDateInt(DateUtil.getLongTimeByDate(searchTimeEnd));
				myIncomeWrapper.le("report_date", timeEnd);
			}
			myIncomeWrapper.orderBy("report_date",false).orderBy("source_type", true);
			List<GroundBonusDayReport> groundBonusDayReportYesterdayList = groundBonusDayReportMapper
					.selectPage(new Page<GroundBonusDayReport>(current,size), myIncomeWrapper);
			
			//奖金动态
			List<Map<String,Object>> bonusList = new ArrayList<Map<String,Object>>();
			for (GroundBonusDayReport groundBonusDayReport : groundBonusDayReportYesterdayList) {
				int reportDate = groundBonusDayReport.getReportDate();
				int yesterdayDate = DateUtil.getyesterday();
				logger.info("reportDate:"+reportDate);
				logger.info("yesterdayDate:"+yesterdayDate);
				
				String date = DateUtil.buildDateToStr(reportDate);
				if(reportDate == yesterdayDate){
					date = "昨天";
				}

				if(groundBonusDayReport.getSourceType()==0){
					//个人
					Map<String,Object> individualBonus = new HashMap<String,Object>();
					individualBonus.put("id",groundBonusDayReport.getId());
					individualBonus.put("time",date);
					individualBonus.put("incomeMoney", groundBonusDayReport.getDayTotalCost());
					individualBonus.put("registerPersonCount", groundBonusDayReport.getDayRegCount());
					individualBonus.put("activationPersonCount", groundBonusDayReport.getDayActivateCount());
					individualBonus.put("dealPersonCount", groundBonusDayReport.getDayOrderCount());
					individualBonus.put("name", "个人");
					bonusList.add(individualBonus);
				}else{
					//团队
					Map<String,Object> teamBonus = new HashMap<String,Object>();
					teamBonus.put("id",groundBonusDayReport.getId());
					teamBonus.put("time",date);
					teamBonus.put("incomeMoney", groundBonusDayReport.getDayTotalCost());
					teamBonus.put("registerPersonCount", groundBonusDayReport.getDayRegCount());
					teamBonus.put("activationPersonCount", groundBonusDayReport.getDayActivateCount());
					teamBonus.put("dealPersonCount", groundBonusDayReport.getDayOrderCount());
					teamBonus.put("name", groundBonusDayReport.getDirectGroundUserName());
					bonusList.add(teamBonus);
				}
			}
			return bonusList;
		} catch (Exception e) {
			logger.error("我的奖金动态e："+e.getMessage());
			throw new RuntimeException("我的奖金动态e："+e.getMessage());
		}
	}


	
}