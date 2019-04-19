package com.jiuyuan.service.common;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.GroundBonusGrantMapper;
import com.jiuyuan.dao.mapper.supplier.GroundBonusRuleMapper;
import com.jiuyuan.dao.mapper.supplier.GroundConditionRuleMapper;
import com.jiuyuan.entity.newentity.GroundConditionRule;
import com.jiuyuan.entity.newentity.UserTimeRule;
import com.jiuyuan.entity.newentity.ground.GroundBonusRule;
import com.xiaoleilu.hutool.json.JSONUtil;
/**
 * 地推规则设置管理
 *
 */
@Service
public class PushInstallService implements IPushInstallService {
	@Autowired
	private GroundConditionRuleMapper groundConditionRuleMapper;
	@Autowired
	private GroundBonusRuleMapper groundBonusRuleMapper;
	
	/**
	 * 添加用户阶段规则
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> addUserActiveRule(int stage1, int stage2, int stage3) {
		
		Wrapper<GroundConditionRule> wrapper = new EntityWrapper<GroundConditionRule>();
		wrapper.eq("rule_type", 1).eq("status", 0);
		List<GroundConditionRule> groundConditionRuleList = groundConditionRuleMapper.selectList(wrapper);
		// 修改旧规则
		GroundConditionRule groundConditionRule = groundConditionRuleList.get(0);
		groundConditionRule.setStatus(GroundConditionRule.ACTIVE_STATUS_FALSE);
		groundConditionRule.setUpdateTime(Calendar.getInstance().getTimeInMillis());
		groundConditionRuleMapper.updateById(groundConditionRule);
		// 添加新规则
		GroundConditionRule newGroundConditionRule = new GroundConditionRule();
		newGroundConditionRule.setRuleType(GroundConditionRule.USER_TIME_RULE);
		newGroundConditionRule.setCreateTime(Calendar.getInstance().getTimeInMillis());
		newGroundConditionRule.setUpdateTime(Calendar.getInstance().getTimeInMillis());
		newGroundConditionRule.setRuleName("用户时间阶段规则");
		newGroundConditionRule.setStatus(GroundConditionRule.ACTIVE_STATUS_TRUE);
		UserTimeRule userTimeRule = new UserTimeRule(stage1, stage2, stage3);
		String string = JSONUtil.parse(userTimeRule).toString();
		newGroundConditionRule.setContent(string);
		groundConditionRuleMapper.insert(newGroundConditionRule);
		Map<String, Object> conditionRuleMap = new HashMap<String, Object>();
		conditionRuleMap.put("stage1", stage1);
		conditionRuleMap.put("stage2", stage2);
		conditionRuleMap.put("stage3", stage3);
		return conditionRuleMap;
	}

	/**
	 * 添加奖金规则
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> addBonusRule(int userType, int bonusType, int sourceType, double bonus) {
		Wrapper<GroundBonusRule> wrapper = new EntityWrapper<GroundBonusRule>();
		wrapper.eq("user_type", userType).eq("bonus_type", bonusType).eq("source_type", sourceType);
		List<GroundBonusRule> groundBonusRuleList = groundBonusRuleMapper.selectList(wrapper);
		for (GroundBonusRule groundBonusRule : groundBonusRuleList) {
			groundBonusRule.setStatus(GroundBonusRule.ACTIVE_STATUS_FALSE);
			groundBonusRule.setUpdateTime(System.currentTimeMillis());
			groundBonusRuleMapper.updateById(groundBonusRule);
		}
		GroundBonusRule groundBonusRule=new GroundBonusRule();
		groundBonusRule.setUserType(userType);
		groundBonusRule.setBonusType(bonusType);
		groundBonusRule.setSourceType(sourceType);
		groundBonusRule.setBonus(bonus);
		groundBonusRule.setStatus(GroundBonusRule.ACTIVE_STATUS_TRUE);
		groundBonusRule.setCreateTime(System.currentTimeMillis());
		groundBonusRule.setUpdateTime(System.currentTimeMillis());
		groundBonusRuleMapper.insert(groundBonusRule);
		Map<String,Object> groundBonusRuleMap=new HashMap<>();
		groundBonusRuleMap.put("userType", userType);
		groundBonusRuleMap.put("bonusType", bonusType);
		groundBonusRuleMap.put("sourceType", sourceType);
		groundBonusRuleMap.put("bonus", bonus);
		return groundBonusRuleMap;
	}
	
}
