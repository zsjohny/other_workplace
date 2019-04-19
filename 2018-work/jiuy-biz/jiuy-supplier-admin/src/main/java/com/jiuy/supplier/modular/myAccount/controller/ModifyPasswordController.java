package com.jiuy.supplier.modular.myAccount.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.supplier.common.system.persistence.dao.UserMapper;
import com.jiuy.supplier.common.system.persistence.model.User;
import com.admin.core.base.controller.BaseController;
import com.admin.core.exception.BizExceptionEnum;
import com.admin.core.exception.BussinessException;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.LOWarehouseNew;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.service.common.IMyAccountSupplierService;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 修改密码控制器
 *
 * @author fengshuonan
 * @Date 2017-10-10 11:18:52
 */
@Controller
@RequestMapping("/modifyPassword")
@Login
public class ModifyPasswordController extends BaseController {
	
	private static final Log logger = LogFactory.get("ModifyPasswordController"); 
	
	private static final int NOT_ORIGINAL_PASSWORD = 0;
	
	private static final String TEMP_ROLEID = "6";
	
	private static final String regString = "^(?=.*?[A-Za-z]+)(?=.*?[0-9]+)(?=.*?[A-Z]).*.{8,}$";
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private IUserNewService supplierUserService;
	
	@Autowired
	private IMyAccountSupplierService myAccountSupplierService;

    private String PREFIX = "/myAccount/modifyPassword/";
    
	/**
	 * test获取商家信息，和商家关联品牌信息
	 */
	@RequestMapping("/testInfo")
	@ResponseBody
	public JsonResponse testInfo(){
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> map = new HashMap<String,Object>();
        long id = ShiroKit.getUser().getId();
        UserNew supplierUser = supplierUserService.getSupplierUserInfo(id);
        long BrandId = supplierUser.getBrandId();
		BrandNew supplierBrand= supplierUserService.getSupplierBrandInfo(BrandId);
		LOWarehouseNew supplierLOWarehouse =  supplierUserService.getSupplierLowarehouse(supplierUser.getLowarehouseId());
		map.put("supplierUser", supplierUser);
		map.put("supplierLOWarehouse", supplierLOWarehouse);
		map.put("supplierBrand", supplierBrand);
		return jsonResponse.setSuccessful().setData(map);
	}
	
	/**
	 * 手机发送获取验证码
	 * @return
	 */
	@RequestMapping("/sendVerifyCode")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse sendVerifyCode(@RequestParam("phoneNumber") String phoneNumber){
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if(supplierId == 0){
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
		long supplierId = ShiroKit.getUser().getId();
		if(supplierId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			//校验密码是否符合规范
			boolean t = newPwd.matches("^(?=.*?[A-Za-z]+)(?=.*?[0-9]+)(?=.*?[A-Z]).*.{8,}$");
			if(!t){
				logger.info("供应商修改的新密码不符合规范！");
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
				user.setIsOriginalpassword(NOT_ORIGINAL_PASSWORD);
				user.setRoleid(user.getRoleid()==null?TEMP_ROLEID:user.getRoleid());
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
        return PREFIX + "modifyPassword.html";
    }

    /**
     * 跳转到添加修改密码
     */
    @RequestMapping("/modifyPassword_add")
    public String modifyPasswordAdd() {
        return PREFIX + "modifyPassword_add.html";
    }

    /**
     * 跳转到修改修改密码
     */
    @RequestMapping("/modifyPassword_update/{modifyPasswordId}")
    public String modifyPasswordUpdate(@PathVariable Integer modifyPasswordId, Model model) {
        return PREFIX + "modifyPassword_edit.html";
    }
    
}
