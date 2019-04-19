package com.jiuy.operator.modular.myAccount.controller;


import com.admin.core.base.controller.BaseController;
import com.admin.core.exception.BizExceptionEnum;
import com.admin.core.exception.BussinessException;
import com.jiuy.operator.common.system.persistence.dao.UserMapper;
import com.jiuy.operator.common.system.persistence.model.User;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.IMyAccountSupplierService;
import com.jiuyuan.service.common.MyAccountSupplierService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 修改密码控制器
 *
 * @author fengshuonan
 * @Date 2018-01-11 16:58:19
 */
@Controller
@RequestMapping("/changePassword")
public class ChangePasswordController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(ChangePasswordController.class);

    private String PREFIX = "/myAccount/changePassword/";
    
	private static final int NOT_ORIGINAL_PASSWORD = 0;
	
	private static final String TEMP_ROLEID = "6";
	
	private static final String regString = "^(?=.*?[A-Za-z]+)(?=.*?[0-9]+)(?=.*?[A-Z]).*.{8,}$";
	
    
    @Autowired
    private IMyAccountSupplierService myAccountSupplierService;
    
	@Autowired
    private UserMapper userMapper;
    
    /**
	 * 手机发送获取验证码
	 * @return
	 */
	@RequestMapping("/sendVerifyCode")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse sendVerifyCode(@RequestParam("phoneNumber") String phoneNumber){
		JsonResponse jsonResponse = new JsonResponse();
		long id = ShiroKit.getUser().getId();
		if(id == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			Map<String,Object> map = myAccountSupplierService.sendVerifyCode(phoneNumber);
            return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 提交修改的密码
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	@AdminOperationLog
   public JsonResponse commit(@RequestParam("verifyCode") String verifyCode,
		                      @RequestParam(value = "send_type",required=false,defaultValue = "sms") String sendType,
		                      @RequestParam("oldPwd") String oldPwd,
		                      @RequestParam("newPwd") String newPwd,
		                      @RequestParam("phoneNumber") String phoneNumber,
   		                      HttpServletResponse response ){
		JsonResponse jsonResponse = new JsonResponse();
		long id = ShiroKit.getUser().getId();
		if(id == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			//校验密码是否符合规范
			boolean t = newPwd.matches("^(?=.*?[A-Za-z]+)(?=.*?[0-9]+)(?=.*?[A-Z]).*.{8,}$");
			if(!t){
				logger.info("修改的新密码不符合规范！");
				return jsonResponse.setError("新密码输入有误，密码需要8位或8位字符以上，包含数字、字母，且至少含有一个大写字母");
			}
			//检验验证码是否正确
			myAccountSupplierService.checkVerifyCode(phoneNumber, verifyCode, sendType);
			//验证原始密码
			Integer userId = ShiroKit.getUser().getId();
			User user = userMapper.selectById(userId);
			String oldMd5 = ShiroKit.md5(oldPwd, user.getSalt());
			if (user.getPassword().equals(oldMd5)) {
				//更新新密码
				String newMd5 = ShiroKit.md5(newPwd, user.getSalt());
				user.setPassword(newMd5);
				user.setIsOriginalpassword(User.NOT_ORIGINAL_PWD);
				user.updateById();

				return jsonResponse.setSuccessful();
			} else {
				logger.info("原始密码错误"+phoneNumber);
				throw new BussinessException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
			}
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
	}


    /**
     * 跳转到修改密码首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "changePassword.html";
    }

    /**
     * 跳转到添加修改密码
     */
    @RequestMapping("/changePassword_add")
    public String modifyPasswordAdd() {
        return PREFIX + "changePassword_add.html";
    }

    /**
     * 跳转到修改修改密码
     */
    @RequestMapping("/changePassword_update/{changePasswordId}")
    public String modifyPasswordUpdate(@PathVariable Integer modifyPasswordId, Model model) {
        return PREFIX + "changePassword_edit.html";
    }

}
