package com.jiuy.operator.modular.web.controller;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.dao.mapper.DayReportMapper;
import com.jiuyuan.service.common.IGroundCustomerStageChangeService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/dailyReport")
@Login
public class GroundUserDailyReportController {
	
	@Autowired
	private IGroundCustomerStageChangeService groundCustomerStageChangeService;
	
//	@Autowired
//	private DayReportMapper dayReportMapper;
	
	@RequestMapping("/testFisrtIndividual")
	@ResponseBody
	public JsonResponse testFisrtIndividual(){
		JsonResponse jsonResponse = new JsonResponse();
//		groundCustomerStageChangeService.executeGroundCustomerStageChange(20171210);
//		dayReportMapper.executeStoreDailyReport(DateUtil.getTodayStart() - DateUtils.MILLIS_PER_DAY, DateUtil.getTodayEnd() - DateUtils.MILLIS_PER_DAY);
		return jsonResponse.setSuccessful();
	}

}
