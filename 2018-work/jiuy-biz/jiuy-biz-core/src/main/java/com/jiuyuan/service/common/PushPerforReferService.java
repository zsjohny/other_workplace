package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.GroundDayReportMapper;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.ground.GroundDayReport;
import com.jiuyuan.util.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class PushPerforReferService{

	private static final Log logger = LogFactory.get("供应商订单Service");
	
	@Autowired
	private GroundUserMapper groundUserMapper;
	@Autowired
	private GroundDayReportMapper groundDayReportMapper;

	/**
	 * 获取地推业绩查询列表
	 * @param statisticsTimeStart
	 * @param statisticsTimeEnd
	 * @param name
	 * @param phone
	 * @param grandRoleType
	 * @param province
	 * @param city
	 * @param district
	 * @param administratorId
	 * @param pPhone
	 * @param individualTotalSaleAmountMin
	 * @param individualTotalSaleAmountMax
	 * @param customerRegisterCountMin
	 * @param customerRegisterCountMax
	 * @param customerActiveCountMin
	 * @param customerActiveCountMax
	 * @param pageSize 
	 * @param currentPage 
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getPushStatisticList(long groundUserId,int statisticsTimeStart, int statisticsTimeEnd, String name,
			String phone, int grandRoleType, String province, String city, String district, long administratorId, String pPhone,
			double individualTotalSaleAmountMin, double individualTotalSaleAmountMax, int customerRegisterCountMin,
			int customerRegisterCountMax, int customerActiveCountMin, int customerActiveCountMax,Page<Map<String,Object>> page
			 ) {
		//封装wrapper
		/*Wrapper<GroundUser> groundUserWrapper = encapsulateWrapper(groundUserId,statisticsTimeStart,statisticsTimeEnd,
				name,phone,grandRoleType,province,city,district,pName,pPhone,individualTotalSaleAmountMin,individualTotalSaleAmountMax,
				customerRegisterCountMin,customerRegisterCountMax,customerActiveCountMin,customerActiveCountMax,page);*/
		
		//当上级为区域主管时要查询出所有的下级城市经理
//		long pId = 0;
//		if(!StringUtils.isEmpty(pPhone)){
//			Wrapper<GroundUser> groundUserWrapper = new EntityWrapper<GroundUser>().eq("phone", pPhone);
//			List<GroundUser> groundUserList = groundUserMapper.selectList(groundUserWrapper);
//			if(groundUserList.size()>0){
//				GroundUser groundUser = groundUserList.get(0);
//				if(groundUser.getUserType()==3){
//					pId = groundUser.getId();
//					pPhone = "";
//				}
//			}
//		}
		
		//地推人员业绩查询
		List<Map<String, Object>> selectPageList = groundDayReportMapper.selectPageList(groundUserId,statisticsTimeStart,statisticsTimeEnd,
				name,phone,grandRoleType,province,city,district,administratorId,pPhone,individualTotalSaleAmountMin,individualTotalSaleAmountMax,
				customerRegisterCountMin,customerRegisterCountMax,customerActiveCountMin,customerActiveCountMax,page);
		
		
		/*List<GroundUser> groundUserList = groundUserMapper.selectPage(page, groundUserWrapper);
		List<Map<String,Object>> pushStatisticList = new ArrayList<Map<String,Object>>();
		for (GroundUser groundUser : groundUserList) {
			Map<String,Object> pushStatistic = new HashMap<String,Object>();
			pushStatistic.put("groundUserId",groundUser.getId());
			pushStatistic.put("grandRole",getUserTypeString(groundUser.getUserType()));
			pushStatistic.put("name",groundUser.getName());
			pushStatistic.put("phone",groundUser.getPhone());
			pushStatistic.put("address",groundUser.getProvince()+groundUser.getCity()+groundUser.getDistrict());
			pushStatistic.put("pName",groundUser.getPname());
			pushStatistic.put("pPhone",groundUser.getPphone());
			pushStatistic.put("individualClientCount",groundUser.getIndividualClientCount());
			pushStatistic.put("individualActiveClientCount",(int)groundUser.getIndividualClientCount()*groundUser.getIndividualClientActiveRate()/100);
			pushStatistic.put("individualTotalSaleAmount",groundUser.getIndividualTotalSaleAmount());
			pushStatistic.put("individualIncome",groundUser.getOneselfCost());
			pushStatistic.put("teamClientCount",groundUser.getTeamClientCount());
			pushStatistic.put("teamActiveClientCount",(int)groundUser.getTeamClientCount()*groundUser.getTeamClientActiveRate()/100);
			pushStatistic.put("teamTotalSaleAmount",groundUser.getTeamTotalSaleAmount());
			pushStatistic.put("teamIncome",groundUser.getTeamCost());
			pushStatistic.put("totalIncome",groundUser.getTotalCost());
			pushStatisticList.add(pushStatistic);
		}
		*/
		
		
		
		
		
		
		
		
		return selectPageList;
	}

	/**
	 * 获取用户对应的用户类型
	 * @param userType
	 * @return
	 */
	private String getUserTypeString(int userType) {
		String userTypeString = "";
		switch (userType) {
		case 1:
			userTypeString = "大区总监";
			break;
		case 2:
			userTypeString = "省区经理";
			break;
		case 3:
			userTypeString = "区域主管";
			break;
		case 4:
			userTypeString = "城市经理";
			break;
		}
		return userTypeString;
	}

	/**
	 * 封装wrapper
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
	 * @return
	 */
	private Wrapper<GroundUser> encapsulateWrapper(long groundUserId, long statisticsTimeStart, long statisticsTimeEnd,
			String name, String phone, int grandRoleType, String province, String city, String district, String pName,
			String pPhone, double individualTotalSaleAmountMin, double individualTotalSaleAmountMax,
			int customerRegisterCountMin, int customerRegisterCountMax, int customerActiveCountMin,
			int customerActiveCountMax, Page<Map<String, Object>> page) {
		Wrapper<GroundUser> groundUserWrapper = new EntityWrapper<GroundUser>();
		if(groundUserId!=0){
			groundUserWrapper.eq("id", groundUserId);
		}
		if(!"".equals(name)){
			groundUserWrapper.eq("name", name);
		}
		if(!"".equals(phone)){
			groundUserWrapper.eq("phone", phone);
		}
		if(grandRoleType>0){
			groundUserWrapper.eq("user_type", grandRoleType);
		}
		if(!"".equals(province)){
			groundUserWrapper.eq("province", province);
		}
		if(!"".equals(city)){
			groundUserWrapper.eq("city", city);
		}
		if(!"".equals(district)){
			groundUserWrapper.eq("district", district);
		}
		if(!"".equals(pName)){
			groundUserWrapper.eq("pname", pName);
		}
		if(!"".equals(pPhone)){
			groundUserWrapper.eq("pphone", pPhone);
		}
		if(individualTotalSaleAmountMin>0){
			groundUserWrapper.ge("individual_total_sale_amount", individualTotalSaleAmountMin);
		}
		if(individualTotalSaleAmountMax>0){
			groundUserWrapper.le("individual_total_sale_amount", individualTotalSaleAmountMax);
		}
		if(customerRegisterCountMin>0){
			groundUserWrapper.andNew(" (individual_client_count+team_client_count) >= ",customerRegisterCountMin);
		}
		if(customerRegisterCountMax>0){
			groundUserWrapper.andNew(" (individual_client_count+team_client_count) <= ",customerRegisterCountMax);
		}
		if(customerActiveCountMin>0){
			groundUserWrapper
			.andNew(" ((individual_client_count*individual_client_active_rate+team_client_count*team_client_active_rate)/100) >= ",customerActiveCountMin);
		}
		if(customerActiveCountMax>0){
			groundUserWrapper
			.andNew(" ((individual_client_count*individual_client_active_rate+team_client_count*team_client_active_rate)/100) >= ",customerActiveCountMax);
		}
		return groundUserWrapper;
	} 
	
}