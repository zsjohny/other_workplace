package com.ground.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.exception.BizExceptionEnum;
import com.admin.core.exception.BussinessException;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.service.common.MyAccountGroundService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping("/myAccount")
@Login
public class MyAccountController {
	private static final Log logger = LogFactory.get(MyAccountController.class);
	
	@Autowired
	private GroundUserMapper groundUserMapper;
	
	@Autowired
	private MyAccountGroundService myAccountGroundService;
	
	private static final int NOT_ORIGINAL_PASSWORD = 0; 
	
    /**
     * 加盐参数
     */
    public final static String hashAlgorithmName = "MD5";

    /**
     * 循环次数
     */
    public final static int hashIterations = 1024;
	
	/**
	 * 修改密码接口
	 */
	@RequestMapping("/modifyPassword")
	@ResponseBody
	@NoLogin
	@AdminOperationLog
	public JsonResponse modifyPassword(@RequestParam("oldPwd") String oldPwd,
			                           @RequestParam("newPwd") String newPwd,
			                           @RequestParam("verifyCode") String verifyCode,
			                           @RequestParam( value = "send_type", required = false, defaultValue = "sms") String sendType,
			                           UserDetail<GroundUser> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long groundUserId = userDetail.getId();
		if(groundUserId == 0){
			logger.info("地推人员信息不能为空，该接口需要登录，请排除问题！");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		String phone = userDetail.getUserDetail().getPhone();
		try {
			
			//检验验证码是否正确
			myAccountGroundService.updatePassword(phone, verifyCode, sendType);
			//验证原始密码
			GroundUser user = groundUserMapper.selectById(groundUserId);
			
			ByteSource salt = new Md5Hash(user.getSalt());
			String oldMd5 = new SimpleHash(hashAlgorithmName, oldPwd, salt, hashIterations).toString();
			if (user.getPassword().equals(oldMd5)) {
				//更新新密码
				String newMd5 = new SimpleHash(hashAlgorithmName, newPwd, salt, hashIterations).toString();
				user.setPassword(newMd5);
				user.setIsOriginalpassword(NOT_ORIGINAL_PASSWORD);
				user.updateById();

				return jsonResponse.setSuccessful();
			} else {
				logger.info("原始密码错误"+phone);
				throw new BussinessException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
			}
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 发送验证码
	 */
	@RequestMapping("/sendVerifyCode")
	@ResponseBody
	@NoLogin
	public JsonResponse sendVerifyCode(UserDetail<GroundUser> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long groundUserId = userDetail.getId();
		if(groundUserId == 0){
			logger.info("地推人员信息不能为空，该接口需要登录，请排除问题！");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		String phone = userDetail.getUserDetail().getPhone();
		try {
			Map<String,Object> map = myAccountGroundService.sendVerifyCode(phone);
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
			
		}
	}
	
	/**
	 * 我的账户信息
	 */
	@RequestMapping("/info")
	@ResponseBody
	public JsonResponse info(UserDetail<GroundUser> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		if(userDetail.getUserDetail() == null){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		long groundUserId = userDetail.getUserDetail().getId();
		GroundUser groundUser = groundUserMapper.selectById(groundUserId);
		//账户信息汇总
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", groundUser.getName());
		map.put("userType", groundUser.getUserType());
		map.put("phone", groundUser.getPhone());
		map.put("province",groundUser.getProvince());
		map.put("city",groundUser.getCity());
		map.put("district",groundUser.getDistrict());
		map.put("individualClientCount", groundUser.getIndividualClientCount());
//		map.put("individualIncome", groundUser.getIndividualIncome());
		map.put("oneself_cost", groundUser.getOneselfCost());
		map.put("individualClientActiveRate", groundUser.getIndividualClientActiveRate());
		map.put("teamClientCount", groundUser.getTeamClientCount());
//		map.put("teamIncome", groundUser.getTeamIncome());
		map.put("team_cost", groundUser.getTeamCost());
		map.put("teamClientActiveRate", groundUser.getTeamClientActiveRate());
		//客户数
		map.put("firstStageIndividualClientCount", groundUser.getFirstStageIndividualClientCount());
		map.put("secondStageIndividualClientCount", groundUser.getSecondStageIndividualClientCount());
		map.put("thirdStageIndividualClientCount", groundUser.getThirdStageIndividualClientCount());
		map.put("otherStageIndividualClientCount", groundUser.getOtherStageIndividualClientCount());
		map.put("firstStageTeamClientCount", groundUser.getFirstStageTeamClientCount());
		map.put("secondStageTeamClientCount", groundUser.getSecondStageTeamClientCount());
		map.put("thirdStageTeamClientCount", groundUser.getThirdStageTeamClientCount());
		map.put("otherStageTeamClientCount", groundUser.getOtherStageTeamClientCount());
		
		//上级信息
		if(groundUser.getPid() == null ||groundUser.getPid() == 0){
			return jsonResponse.setSuccessful().setData(map);
		}
		GroundUser pgroundUser = groundUserMapper.selectById(groundUser.getPid());
		map.put("pid", pgroundUser.getId());
		map.put("puserType", pgroundUser.getUserType());
		map.put("pphone", pgroundUser.getPhone());
		map.put("pname", pgroundUser.getName());
		return jsonResponse.setSuccessful().setData(map);
	}

}
