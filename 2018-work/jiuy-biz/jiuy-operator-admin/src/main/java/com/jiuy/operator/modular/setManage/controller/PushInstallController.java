package com.jiuy.operator.modular.setManage.controller;

import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.GroundConditionRuleMapper;
import com.jiuyuan.entity.newentity.GroundConditionRule;
import com.jiuyuan.entity.newentity.UserTimeRule;
import com.jiuyuan.service.common.IPushInstallService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.json.JSONUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 设置管理控制器
 *
 * @author fengshuonan
 * @Date 2017-11-08 19:25:26
 */
@Controller
@RequestMapping("/pushInstall")
@Login
public class PushInstallController extends BaseController {
	private static final Log logger = LogFactory.get();

	private String PREFIX = "/setManage/pushInstall/";
	
	@Autowired
	private IPushInstallService  pushInstallService;
	
	@Autowired
	private GroundConditionRuleMapper groundConditionRuleMapper;

	/**
	 * 跳转到设置管理首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "pushInstall.html";
	}

	/**
	 * 跳转到添加设置管理
	 */
	@RequestMapping("/pushInstall_add")
	public String pushInstallAdd() {
		return PREFIX + "pushInstall_add.html";
	}

	/**
	 * 跳转到修改设置管理
	 */
	@RequestMapping("/pushInstall_update/{pushInstallId}")
	public String pushInstallUpdate(@PathVariable Integer pushInstallId, Model model) {
		return PREFIX + "pushInstall_edit.html";
	}

	/**
	 * 获取设置管理列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public JsonResponse list(@RequestParam(value = "condition", required = false, defaultValue = "") String condition) {
		JsonResponse jsonResponse = new JsonResponse();
		Wrapper<GroundConditionRule> wrapper = new EntityWrapper<GroundConditionRule>();
		try {
			if (StringUtils.isEmpty(condition)) {
				wrapper.eq("rule_type", GroundConditionRule.USER_ACTIVE_RULE).eq("status",
						GroundConditionRule.ACTIVE_STATUS_TRUE);
				List<GroundConditionRule> groundConditionRuleList = groundConditionRuleMapper.selectList(wrapper);
				GroundConditionRule groundConditionRule = groundConditionRuleList.get(0);
				return jsonResponse.setSuccessful().setData(groundConditionRule.getContent());
			} else {
				wrapper.eq("rule_type", GroundConditionRule.USER_ACTIVE_RULE).eq("status",
						GroundConditionRule.ACTIVE_STATUS_TRUE);
				List<GroundConditionRule> groundConditionRuleList = groundConditionRuleMapper.selectList(wrapper);
				GroundConditionRule groundConditionRule = groundConditionRuleList.get(0);
				groundConditionRule.setContent(condition);
				groundConditionRuleMapper.updateById(groundConditionRule);
				return jsonResponse.setSuccessful().setData("ok");
			}
		} catch (Exception e) {
			logger.error("用户激活条件设置e：" + e.getMessage());
			throw new RuntimeException("用户激活条件设置e：" + e.getMessage());
		}
	}

	/**
	 * 添加用户阶段规则
	 * @param stage1
	 * @param stage2
	 * @param stage3
	 * @return
	 */
	@RequestMapping(value = "/addUserStageRule")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addUserStageRule(@RequestParam(value = "stage1", required = true) int stage1,
			@RequestParam(value = "stage2", required = true) int stage2,
			@RequestParam(value = "stage3", required = true) int stage3) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Map<String, Object> userStageRule = pushInstallService.addUserActiveRule(stage1, stage2, stage3);
			return jsonResponse.setSuccessful().setData(userStageRule);
		} catch (Exception e) {
			logger.error("添加用户时间阶段规则e：" + e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	/**
	 * 添加奖金规则
	 * @param userType
	 * @param bounsType
	 * @param sourceType
	 * @param bonus
	 * @return
	 */
	@RequestMapping(value = "/addBounsRule")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addBonusRule(
			@RequestParam(value = "userType", required = true) int userType,
			@RequestParam(value = "bounsType", required = true) int bounsType,
			@RequestParam(value = "sourceType", required = true) int sourceType,
			@RequestParam(value = "bonus", required = true) double bonus) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Map<String, Object> bonusRule =pushInstallService.addBonusRule(userType,bounsType,sourceType,bonus);
			return jsonResponse.setSuccessful().setData(bonusRule);
		} catch (Exception e) {
			logger.error("添加用户奖金规则e：" + e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}

	/**
	 * 删除设置管理
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete() {
		return SUCCESS_TIP;
	}

	/**
	 * 修改设置管理
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update() {
		return super.SUCCESS_TIP;
	}

	/**
	 * 设置管理详情
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public Object detail() {
		return null;
	}
}
