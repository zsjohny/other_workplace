package com.ground.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.GroundUserLoginDelegator;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping
public class LoginController {
	private static final Log logger = LogFactory.get(LoginController.class);
	
	protected static String REDIRECT = "redirect:";
	
	@Autowired
	private GroundUserLoginDelegator loginDelegator;
	

	
	@Autowired
	private GroundUserMapper groundUserMapper;
	
//	private static final String PREFIX = "static";
	
//	/**
//	 * 切换到登录页面
//	 * @return
//	 */
//	@RequestMapping(value = "/login")
//	@ResponseBody
//	public String login(UserDetail<GroundUser> userDetail){
//		if(userDetail.getUserDetail() != null ){
//			return REDIRECT + "/";
//		}else{
//			return PREFIX+"/login.html";
//		}
//	}
	
    /**
     * 跳转到主页
     */
//    @RequestMapping(value = "/")
//    @ResponseBody
//    public String index(UserDetail<GroundUser> userDetail) {
//    	if(userDetail.getUserDetail() == null){
//    		return REDIRECT +"/login";
//    	}
//        //获取菜单列表
//        int groundUserId = userDetail.getUserDetail().getId();
//        int userType = userDetail.getUserDetail().getUserType();
//        
//        Wrapper<GroundUser> wrapper = new EntityWrapper<GroundUser>();
//        wrapper.eq("id", groundUserId);
//        List<GroundUser> list = groundUserMapper.selectList(wrapper);
//        //退出登录
//        if(list == null || list.size() == 0){
//        	
//        }
//
//
////        
////        Integer id = ShiroKit.getUser().getId();
////        Wrapper<User> wrapper = new EntityWrapper<User>();
////		wrapper.eq("id", id);
////		List<User> list = userMapper.selectList(wrapper);
////		if(list == null || list.size() == 0){
////			ShiroKit.getSubject().logout();
////			return "/login.html";
////		}
////		int isOriginalpassword = list.get(0).getIsOriginalpassword();
////		if(isOriginalpassword == ORIGINAL_PASSWORD){
////			ShiroKit.getSubject().logout();
////            model.addAttribute("tips", "请务必修改初始密码");
////            return "/login.html";
////		}
////        //判断用户是否有角色
////        if (roleList == null || roleList.size() == 0) {
////            ShiroKit.getSubject().logout();
////            model.addAttribute("tips", "该用户没有角色，无法登陆");
////            return "/login.html";
////        }
////        //获取资源菜单
////        List<MenuNode> menus = menuDao.getMenusByRoleIds(roleList);
////        List<MenuNode> titles = MenuNode.buildTitle(menus);
////        titles = ApiMenuFilter.build(titles);
////        
////        model.addAttribute("titles", titles);
////
////        //获取用户头像
////        User user = userMapper.selectById(id);
////        String avatar = user.getAvatar();
////        model.addAttribute("avatar", avatar);
//
//        return PREFIX+"/modifyPassword.html";
//    }
    
    /**
     * 
     */
	

	
	/**
	 * 提交登录
	 * @param username
	 * @param password
	 * @param response
	 * @param ip
	 * @param client
	 * @return
	 */
	@RequestMapping(value = "/submitLogin") // , method = RequestMethod.POST
	@ResponseBody
	public JsonResponse submitLogin(@RequestParam("phone") String phone,
			@RequestParam("password") String password, HttpServletResponse response, @ClientIp String ip,
			ClientPlatform client) {
		try {
			return loginDelegator.mobileSubmitLogin(phone, password, response, ip, client);
		} catch (Exception e) {
			e.printStackTrace();
			JsonResponse jsonResponse = new JsonResponse();
			return jsonResponse.setError(e.getMessage());
		}
	}
	
    /**
     * 退出登录
     * @param response
     * @return
     */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public JsonResponse logout(HttpServletResponse response) {
		LoginUtil.deleteLoginCookie(response);
		return new JsonResponse().setSuccessful();
	}

}
