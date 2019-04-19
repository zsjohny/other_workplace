package com.jiuy.operator.common.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;
import com.jiuy.operator.common.system.dao.NoticeDao;
import com.jiuy.operator.common.system.service.ITest1Service;
import com.jiuyuan.util.anno.Login;

/**
 * 总览信息
 *
 * @author fengshuonan
 * @Date 2017年3月4日23:05:54
 */
@Controller
@RequestMapping("/blackboard")
@Login
public class BlackboardController extends BaseController {

	@Autowired
	NoticeDao noticeDao;

	@Autowired
	ITest1Service test1Service;

	/**
	 * 跳转到黑板
	 */
	@RequestMapping("")
	public String blackboard(Model model) {
		List<Map<String, Object>> notices = noticeDao.list(null);
		model.addAttribute("noticeList", notices);
		return "/blackboard.html";
	}

	/**
	 * 获取所有部门列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(String condition) {

		return test1Service.list(142);
	}
}
