package com.ground.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.dao.mapper.supplier.GroundCustomerStageChangeMapper;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping(value = "/groundUserCustomer")
@Login
public class GroundCustomerAddController {
	private static final Log logger = LogFactory.get();
	@Autowired
	private GroundCustomerStageChangeMapper groundCustomerStageChangeMapper;
	
	/**
	 * 统计指定日期地推人员的个人当日新增客户总数
	 * @param userDetail
	 * @param timeInt 指定日期
	 * @return
	 */
	@RequestMapping(value = "/personal")
	@ResponseBody
	public JsonResponse getPersonalCusAddCount(UserDetail<GroundUser> userDetail,@RequestParam(value="timeInt") Integer timeInt ){
		JsonResponse jsonResponse = new JsonResponse();
		long groundUserId = userDetail.getId();
		if(groundUserId==0){
			logger.error("统计指定日期地推人员的个人当日新增客户总数groundUserId:"+groundUserId);
			jsonResponse.setError("我的Id为空，请确认");
		}
		try {
			int count =groundCustomerStageChangeMapper.getPersonalCusAddCount( groundUserId,timeInt);
			return jsonResponse.setSuccessful().setData(count);
		} catch (Exception e) {
			logger.error("统计指定日期地推人员的个人当日新增客户总数e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	/**
	 * 统计指定日期地推团队当日新增客户总数
	 * @param userDetail
	 * @param timeInt 指定日期
	 * @return
	 */
	@RequestMapping(value = "/group")
	@ResponseBody
	public JsonResponse getGroupCusAddCount(UserDetail<GroundUser> userDetail,@RequestParam(value="timeInt") Integer timeInt ){
		JsonResponse jsonResponse = new JsonResponse();
		long groundUserId = userDetail.getId();
		if(groundUserId==0){
			logger.error("统计指定日期地推团队当日新增客户总数groundUserId:"+groundUserId);
			jsonResponse.setError("我的Id为空，请确认");
		}
		try {
			int count =groundCustomerStageChangeMapper.getGroupCusAddCount( groundUserId,timeInt);
			return jsonResponse.setSuccessful().setData(count);
		} catch (Exception e) {
			logger.error("统计指定日期地推团队当日新增客户总数e:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
}
