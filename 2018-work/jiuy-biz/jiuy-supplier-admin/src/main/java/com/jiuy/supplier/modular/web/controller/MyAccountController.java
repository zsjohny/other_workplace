//package com.jiuy.supplier.modular.web.controller;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.baomidou.mybatisplus.mapper.EntityWrapper;
//import com.baomidou.mybatisplus.mapper.Wrapper;
//import com.jiuy.supplier.common.exception.BizExceptionEnum;
//import com.jiuy.supplier.common.exception.BussinessException;
//import com.jiuy.supplier.common.persistence.dao.UserMapper;
//import com.jiuy.supplier.common.persistence.model.User;
//import com.jiuy.supplier.core.base.controller.BaseController;
//import com.jiuy.supplier.core.shiro.ShiroKit;
//import com.jiuyuan.constant.ResultCode;
//import com.jiuyuan.entity.ClientPlatform;
//import com.jiuyuan.entity.UserDetail;
//import com.jiuyuan.ext.spring.web.method.ClientIp;
//import com.jiuyuan.util.anno.AdminOperationLog;
//import com.store.service.StoreUserService;
//import com.supplier.entity.SupplierBrand;
//import com.supplier.entity.SupplierLOWarehouse;
//import com.supplier.entity.SupplierUser;
//import com.supplier.service.MyAccountSupplierService;
//import com.supplier.service.SupplierUserService;
//import com.xiaoleilu.hutool.log.Log;
//import com.xiaoleilu.hutool.log.LogFactory;
//import com.yujj.web.helper.JsonResponse;
//
//
//
//@Controller
//@RequestMapping(value="/myAccount")
//public class MyAccountController extends BaseController{
//	
//	private static final Log logger = LogFactory.get("MyAccountController"); 
//	
//	private static final int NOT_ORIGINAL_PASSWORD = 0;
//	
//	private static final String TEMP_ROLEID = "6";
//	
//	@Autowired
//	private UserMapper userMapper;
//	
//	@Autowired
//	private SupplierUserService supplierUserService;
//	
//	@Autowired
//	private MyAccountSupplierService myAccountSupplierService;
//	
//
//	
//	/**
//	 * test获取商家信息，和商家关联品牌信息
//	 */
//	@RequestMapping("/testInfo")
//	@ResponseBody
//	public JsonResponse testInfo(){
//		JsonResponse jsonResponse = new JsonResponse();
//		Map<String,Object> map = new HashMap<String,Object>();
//        long id = ShiroKit.getUser().getId();
//        SupplierUser supplierUser = supplierUserService.getSupplierUserInfo(id);
//        long BrandId = supplierUser.getBrandId();
//		SupplierBrand supplierBrand= supplierUserService.getSupplierBrandInfo(BrandId);
//		SupplierLOWarehouse supplierLOWarehouse =  supplierUserService.getSupplierLowarehouse(supplierUser.getLowarehouseId());
//		map.put("supplierUser", supplierUser);
//		map.put("supplierLOWarehouse", supplierLOWarehouse);
//		map.put("supplierBrand", supplierBrand);
//		return jsonResponse.setSuccessful().setData(map);
//	}
//
//	
//	/**
//	 * 登录到修改密码页面
//	 */
//    @RequestMapping(value = "/password")
//    public String login() {
//       
//      return "/myAccount/modifyPassword/modifyPassword.html";
//    }
//	/**
//	 * 手机发送获取验证码
//	 * @return
//	 */
//	@RequestMapping("/sendVerifyCode")
//	@ResponseBody
//	public JsonResponse sendVerifyCode(@RequestParam("phoneNumber") String phoneNumber){
//		JsonResponse jsonResponse = new JsonResponse();
//		long supplierId = ShiroKit.getUser().getId();
//		if(supplierId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//		try {
//			Map<String,Object> map = myAccountSupplierService.sendVerifyCode(phoneNumber);
//            return jsonResponse.setSuccessful().setData(map);
//		} catch (Exception e) {
//			return jsonResponse.setError(e.getMessage());
//		}
//	}
//	
//	/**
//	 * 提交修改的密码
//	 * @return
//	 */
//	@RequestMapping("/commit")
//	@ResponseBody
//	@AdminOperationLog
//   public JsonResponse commit(@RequestParam("verifyCode") String verifyCode,
//		                      @RequestParam(value = "send_type",required=false,defaultValue = "sms") String sendType,
//		                      @RequestParam("oldPwd") String oldPwd,
//		                      @RequestParam("newPwd") String newPwd,
//		                      @RequestParam("phoneNumber") String phoneNumber,
//   		                      HttpServletResponse response ){
//		JsonResponse jsonResponse = new JsonResponse();
//		long supplierId = ShiroKit.getUser().getId();
//		if(supplierId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//		try {
//			//检验验证码是否正确
//			myAccountSupplierService.updatePassword(phoneNumber, verifyCode, sendType);
//			//验证原始密码
//			Integer userId = ShiroKit.getUser().getId();
//			User user = userMapper.selectById(userId);
//			String oldMd5 = ShiroKit.md5(oldPwd, user.getSalt());
//			if (user.getPassword().equals(oldMd5)) {
//				//更新新密码
//				String newMd5 = ShiroKit.md5(newPwd, user.getSalt());
//				user.setPassword(newMd5);
//				user.setIsOriginalpassword(NOT_ORIGINAL_PASSWORD);
//				user.setRoleid(user.getRoleid()==null?TEMP_ROLEID:user.getRoleid());
//				user.updateById();
//
//				return jsonResponse.setSuccessful();
//			} else {
//				throw new BussinessException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
//			}
//		} catch (Exception e) {
//			return jsonResponse.setError(e.getMessage());
//		}
//	}
//}
