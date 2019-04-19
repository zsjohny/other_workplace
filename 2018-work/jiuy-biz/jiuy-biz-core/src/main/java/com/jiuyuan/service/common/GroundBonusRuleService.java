package com.jiuyuan.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.GroundBonusRuleMapper;
import com.jiuyuan.entity.newentity.ground.GroundBonusRule;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 地推奖金规则服务类
 * @author Administrator
 */
@Service
public class GroundBonusRuleService implements IGroundBonusRuleService {
	private static final Log logger = LogFactory.get(GroundBonusGrantFacade.class);
	
	@Autowired
	private GroundBonusRuleMapper groundBonusRuleMapper;

	/**
	 * 获取地推奖金（金额或百分比）
	 * @param userType  用户类型：1(大区总监)、2(省区经理)、3(区域主管)、4(城市经理)
	 * @param bonusType  奖金类型：1(门店注册奖金)、2（门店激活） 、3（第一阶段门店交易奖金），4（第二阶段门店交易奖金）、5（第三阶段门店交易奖金） 可以调用bean中的常量
	 * @param sourceType 奖金来源类型：0个人、1团体
	 * @return 奖金：阶段1 2 3 为:提成百分比, 注册激活为:奖金金额
	 */
	public double getGroundBonusRule(int userType, int bonusType, int sourceType) {
		Wrapper<GroundBonusRule> wrapper = new EntityWrapper<GroundBonusRule>().eq("status", GroundBonusRule.ACTIVE_STATUS_TRUE).eq("user_type",userType)
				                                                               .eq("bonus_type", bonusType).eq("source_type", sourceType);
		 List<GroundBonusRule> groundBonusRuleList = groundBonusRuleMapper.selectList(wrapper);
		//多个列表的情况下只取第一个
		if (groundBonusRuleList!=null && groundBonusRuleList.size()>0) {
			GroundBonusRule groundBonusRule = groundBonusRuleList.get(0);
			return groundBonusRule.getBonus();
		}else{
			String msg = "没有找到对应的奖金设置，请尽快排查问题，userType："+userType
					+",bonusType"+bonusType+",sourceType:"+sourceType+",status:"+GroundBonusRule.ACTIVE_STATUS_TRUE;
			logger.info(msg);
			throw new RuntimeException(msg);
		}
		
	}
}
