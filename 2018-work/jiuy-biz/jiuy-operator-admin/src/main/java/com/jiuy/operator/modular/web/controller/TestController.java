package com.jiuy.operator.modular.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;
import com.admin.core.template.config.OfficialContextConfig;
import com.admin.core.template.engine.OfficialCommonTemplateEngine;
import com.admin.core.util.ToolUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuy.operator.common.system.dao.NoticeDao;
import com.jiuy.operator.modular.biz.service.ITestService;
import com.jiuyuan.dao.mapper.supplier.GroundBonusGrantMapper;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.UserTimeRule;
import com.jiuyuan.entity.newentity.ground.GroundBonusGrant;
import com.jiuyuan.service.common.GroundConditionRuleService;
import com.jiuyuan.util.IdsToStringUtil;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 总览信息
 *
 * @author fengshuonan
 * @Date 2017年3月4日23:05:54
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController {
	
	private static final long THREE_DAYS = 3L*24*60*60*1000;

//	@Autowired
//	NoticeDao noticeDao;
//
//	@Autowired
//	ITestService iTestService;
//	
//	@Autowired
//	private GroundConditionRuleService groundConditionRuleService;
//	
//	@Autowired
//	private GroundUserMapper groundUserMapper;
//	
//	@Autowired
//	private GroundBonusGrantMapper groundBonusGrantMapper;
	
	
	
//	@RequestMapping("/getRuleTime")
//	@ResponseBody
//	public JsonResponse getRuleTime(){
//		JsonResponse jsonResponse = new JsonResponse();
//		UserTimeRule userTimeRule = groundConditionRuleService.getUserTimeRule();
//		return jsonResponse.setSuccessful().setData(userTimeRule);
//	}

	

//	/**
//	 * 跳转到黑板
//	 */
//	@RequestMapping("")
//	public String blackboard(Model model) {
//		List<Map<String, Object>> notices = noticeDao.list(null);
//		model.addAttribute("noticeList", notices);
//		return "/test.html";
//	}
//	/**
//	 * 跳转到黑板
//	 */
//	@RequestMapping("/testGround")
//	@ResponseBody
//	public String testGround(Model model) {
//		//获取地推人员信息
////		GroundUser groundUser = groundUserMapper.selectById(44);
////		String superIds = groundUser.getSuperIds();
////		List<Long> list = IdsToStringUtil.getIdsToListNoKommaL(superIds);
////		list.add(44L);
////		//更改发放注册奖金时间,个人以及团队
////		Wrapper<GroundBonusGrant> wrapper = new EntityWrapper<GroundBonusGrant>();
////		wrapper.in("ground_user_id", list).eq("store_id", 3229);
////		List<GroundBonusGrant> groundBonusGrantList = groundBonusGrantMapper.selectList(wrapper);
////		for(GroundBonusGrant groundBonusGrant:groundBonusGrantList){
////			groundBonusGrant.setIntoTime(System.currentTimeMillis()+THREE_DAYS);
////			groundBonusGrant.setAllowGetOutTime(System.currentTimeMillis()+THREE_DAYS);
////			groundBonusGrantMapper.updateById(groundBonusGrant);
////		}
//		return "/test.html";
//	}
	
	public static void main(String[] args) {

	}

	
}
