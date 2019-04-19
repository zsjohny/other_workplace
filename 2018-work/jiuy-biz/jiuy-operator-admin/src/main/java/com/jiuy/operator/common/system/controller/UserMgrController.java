package com.jiuy.operator.common.system.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.admin.common.annotion.BussinessLog;
import com.admin.common.annotion.Permission;
import com.admin.common.constant.DictConst;
import com.admin.common.constant.SysConst;
import com.admin.common.constant.state.ManagerStatus;
import com.admin.core.base.controller.BaseController;
import com.admin.core.base.tips.Tip;
import com.admin.core.config.GunsProperties;
import com.admin.core.datascope.DataScope;
import com.admin.core.db.Db;
import com.admin.core.exception.BizExceptionEnum;
import com.admin.core.exception.BussinessException;
import com.admin.core.log.LogObjectHolder;
import com.admin.core.util.ToolUtil;
import com.jiuy.operator.common.constant.factory.OperatorConstantFactory;
import com.jiuy.operator.common.system.dao.UserMgrDao;
import com.jiuy.operator.common.system.factory.UserFactory;
import com.jiuy.operator.common.system.persistence.dao.UserMapper;
import com.jiuy.operator.common.system.persistence.model.User;
import com.jiuy.operator.common.system.transfer.UserDto;
import com.jiuy.operator.common.system.warpper.UserWarpper;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuy.operator.core.shiro.ShiroUser;
import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.IMyAccountSupplierService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.util.file.OSSFileUtil;

/**
 * 系统管理员控制器
 *
 * @author fengshuonan
 * @Date 2017年1月11日 下午1:08:17
 */
@Controller
@RequestMapping("/mgr")
@Login
public class UserMgrController extends BaseController {
	private static Logger logger = Logger.getLogger(UserMgrController.class);
//	private final String DEFAULT_BASEPATH_NAME = "";//ThirdPartService.OSS_DEFAULT_BASEPATH_NAME;
	private final String DEFAULT_BASEPATH_NAME = Client.OSS_DEFAULT_BASEPATH_NAME;
	
	private String reg = "^(?=.*?[A-Za-z]+)(?=.*?[0-9]+)(?=.*?[A-Z]).*.{8,}$";
	@Autowired
	private OSSFileUtil ossFileUtil;
	
	private static String PREFIX = "/system/user/";
	
    @Autowired
    private IMyAccountSupplierService myAccountSupplierService;

	@Resource
	private GunsProperties gunsProperties;

	@Resource
	private UserMgrDao managerDao;

	@Resource
	private UserMapper userMapper;
	

	/**
	 * 跳转到查看管理员列表的页面
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "user.html";
	}

	/**
	 * 跳转到查看管理员列表的页面
	 */
	@RequestMapping("/user_add")
	public String addView() {
		return PREFIX + "user_add.html";
	}

	/**
	 * 跳转到角色分配页面
	 */
	// @RequiresPermissions("/mgr/role_assign") //利用shiro自带的权限检查
	@Permission
	@RequestMapping("/role_assign/{userId}")
	public String roleAssign(@PathVariable Integer userId, Model model) {
		if (ToolUtil.isEmpty(userId)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		User user = (User) Db.create(UserMapper.class).selectOneByCon("id", userId);
		model.addAttribute("userId", userId);
		model.addAttribute("userAccount", user.getAccount());
		return PREFIX + "user_roleassign.html";
	}

	/**
	 * 跳转到编辑管理员页面
	 */
	@Permission
	@RequestMapping("/user_edit/{userId}")
	public String userEdit(@PathVariable Integer userId, Model model) {
		if (ToolUtil.isEmpty(userId)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		assertAuth(userId);
		User user = this.userMapper.selectById(userId);
		model.addAttribute(user);
		model.addAttribute("roleName", OperatorConstantFactory.me().getRoleName(user.getRoleid()));
		model.addAttribute("deptName", OperatorConstantFactory.me().getDeptName(user.getDeptid()));
		LogObjectHolder.me().set(user);
		return PREFIX + "user_edit.html";
	}

	/**
	 * 跳转到查看用户详情页面
	 */
	@RequestMapping("/user_info")
	public String userInfo(Model model) {
		Integer userId = ShiroKit.getUser().getId();
		if (ToolUtil.isEmpty(userId)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		User user = this.userMapper.selectById(userId);
		model.addAttribute(user);
		model.addAttribute("roleName", OperatorConstantFactory.me().getRoleName(user.getRoleid()));
		model.addAttribute("deptName", OperatorConstantFactory.me().getDeptName(user.getDeptid()));
		LogObjectHolder.me().set(user);
		return PREFIX + "user_view.html";
	}

	/**
	 * 跳转到修改密码界面
	 */
	@RequestMapping("/user_chpwd")
	public String chPwd() {
		return PREFIX + "user_chpwd.html";
	}
    
	/**
	 * 修改当前用户的密码
	 */
	@RequestMapping("/changePwd")
	@ResponseBody
	public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
		if (!newPwd.equals(rePwd)) {
			throw new BussinessException(BizExceptionEnum.TWO_PWD_NOT_MATCH);
		}
		//校验新密码
		boolean t = newPwd.matches(reg);
		if(!t){
			throw new BussinessException(BizExceptionEnum.NEW_PWD_NOT_LEGAL);
		}
		Integer userId = ShiroKit.getUser().getId();
		User user = userMapper.selectById(userId);
		String oldMd5 = ShiroKit.md5(oldPwd, user.getSalt());
		if (user.getPassword().equals(oldMd5)) {
			String newMd5 = ShiroKit.md5(newPwd, user.getSalt());
			user.setPassword(newMd5);
			user.setIsOriginalpassword(User.IS_ORIGINAL_PWD);
			user.updateById();
			return SUCCESS_TIP;
		} else {
			throw new BussinessException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
		}
	}

	/**
	 * 查询管理员列表
	 */
	@RequestMapping("/list")
	@Permission
	@ResponseBody
	public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime,
			@RequestParam(required = false) String endTime, @RequestParam(required = false) Integer deptid) {
		if (ShiroKit.isAdmin()) {
			List<Map<String, Object>> users = managerDao.selectUsers(null, name, beginTime, endTime, deptid);
			return new UserWarpper(users).warp();
		} else {
			DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
			List<Map<String, Object>> users = managerDao.selectUsers(dataScope, name, beginTime, endTime, deptid);
			return new UserWarpper(users).warp();
		}
	}

	/**
	 * 添加管理员
	 */
	@RequestMapping("/add")
	@BussinessLog(value = "添加管理员", key = "account", dict = DictConst.UserDict)
	@Permission(SysConst.ADMIN_NAME)
	@ResponseBody
	public Tip add(@Valid UserDto user, BindingResult result) {
		if (result.hasErrors()) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
        //校验手机号是否为空
		if(user.getPhone() == null ||user.getPhone().equals("")){
			throw new BussinessException(BizExceptionEnum.PHONE_NOT_FILL);
		}
		// 判断账号是否重复
		User theUser = managerDao.getByAccount(user.getAccount());
		if (theUser != null) {
			throw new BussinessException(BizExceptionEnum.USER_ALREADY_REG);
		}
        
		// 完善账号信息
		user.setSalt(ShiroKit.getRandomSalt(5));
		user.setPassword(ShiroKit.md5(user.getPassword(), user.getSalt()));
		user.setStatus(ManagerStatus.OK.getCode());
		user.setCreatetime(new Date());

		this.userMapper.insert(UserFactory.createUser(user));
		return SUCCESS_TIP;
	}

	/**
	 * 修改管理员
	 *
	 * @throws NoPermissionException
	 */
	@RequestMapping("/edit")
	@BussinessLog(value = "修改管理员", key = "account", dict = DictConst.UserDict)
	@ResponseBody
	public Tip edit(@Valid UserDto user, BindingResult result) throws NoPermissionException {
		if (result.hasErrors()) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		//手机号码必填
		if(user.getPhone() == null ||user.getPhone().equals("")){
			throw new BussinessException(BizExceptionEnum.PHONE_NOT_FILL);
		}
		if (ShiroKit.hasRole(SysConst.ADMIN_NAME)) {
			this.userMapper.updateById(UserFactory.createUser(user));
			return SUCCESS_TIP;
		} else {
			assertAuth(user.getId());
			ShiroUser shiroUser = ShiroKit.getUser();
			if (shiroUser.getId().equals(user.getId())) {
				this.userMapper.updateById(UserFactory.createUser(user));
				return SUCCESS_TIP;
			} else {
				throw new BussinessException(BizExceptionEnum.NO_PERMITION);
			}
		}
	}

	/**
	 * 删除管理员（逻辑删除）
	 */
	@RequestMapping("/delete")
	@BussinessLog(value = "删除管理员", key = "userId", dict = DictConst.UserDict)
	@Permission
	@ResponseBody
	public Tip delete(@RequestParam Integer userId) {
		if (ToolUtil.isEmpty(userId)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		// 不能删除超级管理员
		if (userId.equals(SysConst.ADMIN_ID)) {
			throw new BussinessException(BizExceptionEnum.CANT_DELETE_ADMIN);
		}
		assertAuth(userId);
		this.managerDao.setStatus(userId, ManagerStatus.DELETED.getCode());
		return SUCCESS_TIP;
	}

	/**
	 * 查看管理员详情
	 */
	@RequestMapping("/view/{userId}")
	@ResponseBody
	public User view(@PathVariable Integer userId) {
		if (ToolUtil.isEmpty(userId)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		assertAuth(userId);
		return this.userMapper.selectById(userId);
	}

	/**
	 * 重置管理员的密码
	 */
	@RequestMapping("/reset")
	@BussinessLog(value = "重置管理员密码", key = "userId", dict = DictConst.UserDict)
	@Permission(SysConst.ADMIN_NAME)
	@ResponseBody
	public Tip reset(@RequestParam Integer userId) {
		if (ToolUtil.isEmpty(userId)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		assertAuth(userId);
		User user = this.userMapper.selectById(userId);
		user.setSalt(ShiroKit.getRandomSalt(5));
		user.setPassword(ShiroKit.md5(SysConst.DEFAULT_PWD, user.getSalt()));
		user.setIsOriginalpassword(User.IS_ORIGINAL_PWD);
		this.userMapper.updateById(user);
		return SUCCESS_TIP;
	}

	/**
	 * 冻结用户
	 */
	@RequestMapping("/freeze")
	@BussinessLog(value = "冻结用户", key = "userId", dict = DictConst.UserDict)
	@Permission(SysConst.ADMIN_NAME)
	@ResponseBody
	public Tip freeze(@RequestParam Integer userId) {
		if (ToolUtil.isEmpty(userId)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		// 不能冻结超级管理员
		if (userId.equals(SysConst.ADMIN_ID)) {
			throw new BussinessException(BizExceptionEnum.CANT_FREEZE_ADMIN);
		}
		assertAuth(userId);
		this.managerDao.setStatus(userId, ManagerStatus.FREEZED.getCode());
		return SUCCESS_TIP;
	}

	/**
	 * 解除冻结用户
	 */
	@RequestMapping("/unfreeze")
	@BussinessLog(value = "解除冻结用户", key = "userId", dict = DictConst.UserDict)
	@Permission(SysConst.ADMIN_NAME)
	@ResponseBody
	public Tip unfreeze(@RequestParam Integer userId) {
		if (ToolUtil.isEmpty(userId)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		assertAuth(userId);
		this.managerDao.setStatus(userId, ManagerStatus.OK.getCode());
		return SUCCESS_TIP;
	}

	/**
	 * 分配角色
	 */
	@RequestMapping("/setRole")
	@BussinessLog(value = "分配角色", key = "userId,roleIds", dict = DictConst.UserDict)
	@Permission(SysConst.ADMIN_NAME)
	@ResponseBody
	public Tip setRole(@RequestParam("userId") Integer userId, @RequestParam("roleIds") String roleIds) {
		if (ToolUtil.isOneEmpty(userId, roleIds)) {
			throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
		}
		// 不能修改超级管理员
		if (userId.equals(SysConst.ADMIN_ID)) {
			throw new BussinessException(BizExceptionEnum.CANT_CHANGE_ADMIN);
		}
		assertAuth(userId);
		this.managerDao.setRoles(userId, roleIds);
		return SUCCESS_TIP;
	}

	/**
	 * 上传图片(上传到项目的webapp/static/img)  method = RequestMethod.POST,
	 */
	@RequestMapping( path = "/upload")
	@ResponseBody
	public Map<String, String> upload(@RequestPart("file") MultipartFile picture) {
		String pictureName = UUID.randomUUID().toString() + ".jpg";
		try {
			String fileSavePath = gunsProperties.getFileUploadPath();
			picture.transferTo(new File(fileSavePath + pictureName));
		} catch (Exception e) {
			throw new BussinessException(BizExceptionEnum.UPLOAD_ERROR);
		}
		Map<String, String> file = new HashMap<String, String>();
		file.put("filename", pictureName);
		return file;
	}
	
	/**
	 * 上传文件到阿里云OSS上
	 *  创建内容: 1）上传文件到OSS中 
	 *  2） 将文件名存储到session中
	 *
	 * @param request
	 * @param response
	 * @return 例子：{"filename":https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15087254452311508725445231.jpg}
	 *  method = RequestMethod.POST
	 */
	@RequestMapping(value = "/ossUpload")
//    @AdminOperationLog
	@ResponseBody
//	public Map<String, String> uploadImageFromSession(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
	public Map<String, String> ossUpload(@RequestPart("file") MultipartFile file, ModelMap modelMap,HttpServletRequest request, HttpServletResponse response) {
		String path = null;
		String oldPath = request.getParameter("oldPath");
//		String needWaterMark = request.getParameter("need_water_mark");
		Map<String, String> result = new HashMap<String, String>();
		
		try {
			if (request instanceof MultipartHttpServletRequest) {
				logger.debug("yes you are!");
//				MultipartHttpServletRequest multiservlet = (MultipartHttpServletRequest) request;
//				MultipartFile file = multiservlet.getFile("file");
				if (file == null) {
					logger.error("请求中没有file对象，请排查问题" );
					logger.error("request file null oldPath:" + oldPath);
					return result;
				}
				logger.debug("request file name :" + file.getName());
				path = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file);
				
				//覆盖旧路径则删除
				if(StringUtils.isNotEmpty(StringUtils.trim(oldPath))){
					String key = oldPath.split("/")[oldPath.split("/").length - 1];
					ossFileUtil.removeFile(DEFAULT_BASEPATH_NAME, key);
				}
			} else {
				logger.debug("no wrong request!");
			}
			modelMap.addAttribute("images", path);
			result.put("filename", path);
			logger.info("上传文件接口返回数据，result:"+result.toString());
			return result;
		} catch (IOException e) {
			logger.error("上传文件出现异常", e);
		}
		return result;
	}

	/**
	 * 判断当前登录的用户是否有操作这个用户的权限
	 */
	private void assertAuth(Integer userId) {
		if (ShiroKit.isAdmin()) {
			return;
		}
		List<Integer> deptDataScope = ShiroKit.getDeptDataScope();
		User user = this.userMapper.selectById(userId);
		Integer deptid = user.getDeptid();
		if (deptDataScope.contains(deptid)) {
			return;
		} else {
			throw new BussinessException(BizExceptionEnum.NO_PERMITION);
		}

	}
}
