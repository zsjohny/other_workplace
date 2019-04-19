package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.GroundConditionRuleMapper;
import com.jiuyuan.entity.newentity.GroundConditionRule;
import com.jiuyuan.entity.newentity.UserActiveRule;
import com.jiuyuan.entity.newentity.UserTimeRule;

@Service
public class GroundConditionRuleService implements IGroundConditionRuleService {
	@Autowired
	private GroundConditionRuleMapper groundConditionRuleMapper;
	
	
	/**
	 *  获取用户时间阶段规则
	 */
	public UserTimeRule getUserTimeRule() {
		String ruleJson = getRuleJson(GroundConditionRule.USER_TIME_RULE);
		JSONObject jsonStr = JSONObject.parseObject(ruleJson);
		UserTimeRule userTimeRule = new  UserTimeRule(0,0,0);
		userTimeRule.setStage1(jsonStr.getIntValue("stage1"));
		userTimeRule.setStage2(jsonStr.getIntValue("stage2"));
		userTimeRule.setStage3(jsonStr.getIntValue("stage3"));
		return userTimeRule;
	}
	
	
	/**
	 * 获取启用的用户激活规则列表
	 * @return
	 */
	public List<UserActiveRule> getUserActiveRuleList() {
//		TODO 待红实现
		List<UserActiveRule> userTimeRuleList = new ArrayList<UserActiveRule>();
		String ruleJson = getRuleJson(GroundConditionRule.USER_ACTIVE_RULE);
		JSONArray jsonList = JSON.parseArray(ruleJson);
		for (int i = 0; i < jsonList.size(); i++) {
			JSONObject userActiveRuleJson = jsonList.getJSONObject(i);
			String name = userActiveRuleJson.getString("name");
			int type = userActiveRuleJson.getIntValue("type");
			int isOpen = userActiveRuleJson.getIntValue("isOpen");
			double limitValue = userActiveRuleJson.getDoubleValue("limitValue");
			UserActiveRule userActiveRule=new UserActiveRule(name, type, isOpen, limitValue);
			userTimeRuleList.add(userActiveRule);
			}
		return userTimeRuleList;
	}

	
	/**
	 * 获取条件JSON
	 */
	private String getRuleJson(int ruleType ) {
		Wrapper<GroundConditionRule> wrapper = new EntityWrapper<GroundConditionRule>().eq("status", GroundConditionRule.ACTIVE_STATUS_TRUE)
				.eq("rule_type",ruleType).orderBy("create_time", true);
		List<GroundConditionRule> conditionRuleList = groundConditionRuleMapper.selectList(wrapper);
		//多个列表的情况下只取第一个
		if (conditionRuleList!=null && conditionRuleList.size()>0) {
			GroundConditionRule conditionRule = conditionRuleList.get(0);
			return conditionRule.getContent();
		}
		return null;
	}
	
	/**
	 * 获取用户订单交易激活条件
	 * @return  用户激活条件规则订单累计金额
	 */
	public double getActiveRuleCost(){
		double limitMoney = 0;
		Wrapper<GroundConditionRule> wrapper = new EntityWrapper<GroundConditionRule>().eq("rule_type", GroundConditionRule.USER_ACTIVE_RULE);
		List<GroundConditionRule> groundConditionRuleList = groundConditionRuleMapper.selectList(wrapper);
		if(groundConditionRuleList.size()>0){
			GroundConditionRule groundConditionRule = groundConditionRuleList.get(0);
			String content = groundConditionRule.getContent();
			if(!StringUtils.isEmpty(content)){
				//获取用户累计订单商品实付总计限制金额
				limitMoney = Double.parseDouble(content);
			}else{
					throw new RuntimeException("用户订单交易激活条件未配置");
			}
		}else{
			throw new RuntimeException("用户订单交易激活条件未配置");
		}
		return limitMoney;
	}
	
	/**
	 * 获取注册X天后发放奖金天数
	 * @return  天数
	 */
	public int getUserRegAfterDayGrantBonus(){
		int day = 0;
		Wrapper<GroundConditionRule> wrapper = new EntityWrapper<GroundConditionRule>().eq("rule_type", GroundConditionRule.USER_REG_AFTER_DAY_GRANT_BONUS);
		List<GroundConditionRule> groundConditionRuleList = groundConditionRuleMapper.selectList(wrapper);
		if(groundConditionRuleList.size()>0){
			GroundConditionRule groundConditionRule = groundConditionRuleList.get(0);
			String content = groundConditionRule.getContent();
			if(!StringUtils.isEmpty(content)){
				day = Integer.parseInt(content);
			}else{
					throw new RuntimeException("注册X天后发放奖金未配置");
			}
		}else{
			throw new RuntimeException("注册X天后发放奖金未配置");
		}
		return day;
	}
}
