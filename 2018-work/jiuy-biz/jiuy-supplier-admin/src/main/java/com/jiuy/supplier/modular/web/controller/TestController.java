package com.jiuy.supplier.modular.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;
import com.alibaba.fastjson.JSONArray;
import com.jiuy.supplier.common.system.dao.NoticeDao;
import com.jiuy.supplier.modular.biz.service.ITestService;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.service.common.RefundSMSNotificationService;
import com.jiuyuan.service.common.UcpaasService;
import com.jiuyuan.service.common.UserNewService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.GetuiUtil;
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

	@Autowired
	NoticeDao noticeDao;

	@Autowired
	ITestService iTestService;
	
	@Autowired
	private IUserNewService userNewService;
	
	@Autowired
	private RefundSMSNotificationService refundSMSNotificationService;
	
	@Autowired
	private UcpaasService ucpaasService;
	
	@Autowired
	private YunXinSmsService yunXinSmsService;

	

	/**
	 * 跳转到黑板
	 */
	@RequestMapping("")
	public String blackboard(Model model) {
		List<Map<String, Object>> notices = noticeDao.list(null);
		model.addAttribute("noticeList", notices);
		return "/test.html";
	}
	
//	@RequestMapping("/testRollBack")
//	@ResponseBody
//	public JsonResponse testRollBack(){
//		JsonResponse jsonResponse = new JsonResponse();
//		userNewService.insertUser();
//		return jsonResponse.setSuccessful();
//	}
	
//    @RequestMapping("/test1")
//    @ResponseBody
//    public JsonResponse testRefundSMSNotificationService(@RequestParam("messageType") int messageType){
//    	JsonResponse jsonResponse = new JsonResponse();
////    	boolean result = refundSMSNotificationService.sendSMSNotificationAndGEPush(null, messageType, 3265L,"15215135450");
//    	boolean result;
//		try {
//			result = GetuiUtil.pushGeTui("0b9b1d9025851b418581e483382053a5","【俞姐姐门店宝】经平台介入沟通，您提交的退货申请已被平台关闭。","" , "", "", "7" ,String.valueOf(System.currentTimeMillis()));
//			if(result){
//				return jsonResponse.setSuccessful();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	return jsonResponse.setError("短信发送失败！");
//    }
//    @RequestMapping("/test1")
//    @ResponseBody
//    public JsonResponse testRefundSMSNotificationService(){
//    	JsonResponse jsonResponse = new JsonResponse();
//    	JSONArray params1 = new JSONArray();
//    	params1.add((int)(Math.random()*100));
//    	boolean result =  yunXinSmsService.sendNotice("15215135450", params1, 3156001);
//    	if(result){
//    		System.out.println(params1.toJSONString());
//    		return jsonResponse.setSuccessful();
//    	}
//    	return jsonResponse.setError("短信发送失败！");
//    }
//    @RequestMapping("/test1")
//    @ResponseBody
//    public JsonResponse testRefundSMSNotificationService(){
//    	boolean success = ucpaasService.testTemplateSMS(true,"bf556fda1feaeab46ff2d06c6dd96927","1fcdf24ff833a26e86964abbcc72235a","b298d6cc182a48da9ba7f6084d92b6ff","255196","15215135450","");
////		boolean success = ucpaasService.testVoiceCode(true,"bf556fda1feaeab46ff2d06c6dd96927","1fcdf24ff833a26e86964abbcc72235a","7828de81a08244729e6869657f5fb364","15215135450","0010");
//    	if(success){
////			logger.info("成功！");
//			System.out.println("成功！");
//			return new JsonResponse().setSuccessful();
//		}else{
////			logger.info("失败！");
//			System.out.println("失败！");
//			return new JsonResponse().setError("失败！");
//		}
//    }
	
	
	

	
}
