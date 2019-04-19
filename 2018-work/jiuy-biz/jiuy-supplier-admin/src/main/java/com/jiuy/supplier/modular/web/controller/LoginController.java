package com.jiuy.supplier.modular.web.controller;

import static com.admin.core.support.HttpKit.getIp;

import java.util.List;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.admin.core.base.controller.BaseController;
import com.admin.core.exception.InvalidKaptchaException;
import com.admin.core.log.LogManager;
import com.admin.core.node.MenuNode;
import com.admin.core.util.ApiMenuFilter;
import com.admin.core.util.KaptchaUtil;
import com.admin.core.util.ToolUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.code.kaptcha.Constants;
import com.jiuy.supplier.common.system.dao.MenuDao;
import com.jiuy.supplier.common.system.persistence.dao.UserMapper;
import com.jiuy.supplier.common.system.persistence.model.User;
import com.jiuy.supplier.core.log.factory.LogTaskFactory;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping
public class LoginController extends BaseController{
	
	private static final Log logger = LogFactory.get("LoginController");
	//初始化密码或者默认密码，1：使用 0：未使用
	private static final int ORIGINAL_PASSWORD=1;
	
	private static final String TEST_PASSWORD="111111";
	
	@Autowired
    MenuDao menuDao;

    @Autowired
    UserMapper userMapper;
    

    
    

    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        //获取菜单列表
        List<Integer> roleList = ShiroKit.getUser().getRoleList();
        
        Integer id = ShiroKit.getUser().getId();
        Wrapper<User> wrapper = new EntityWrapper<User>();
		wrapper.eq("id", id);
		List<User> list = userMapper.selectList(wrapper);
		if(list == null || list.size() == 0){
			ShiroKit.getSubject().logout();
			return "/login.html";
		}
		int isOriginalpassword = list.get(0).getIsOriginalpassword();
		if(isOriginalpassword == ORIGINAL_PASSWORD){
			ShiroKit.getSubject().logout();
            model.addAttribute("tips", "请务必修改初始密码");
            return "/login.html";
		}
        //判断用户是否有角色
        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }
        //获取资源菜单
        List<MenuNode> menus = menuDao.getMenusByRoleIds(roleList);
        List<MenuNode> titles = MenuNode.buildTitle(menus);
        titles = ApiMenuFilter.build(titles);
        
        model.addAttribute("titles", titles);

        //获取用户头像
        User user = userMapper.selectById(id);
        String avatar = user.getAvatar();
        model.addAttribute("avatar", avatar);

        return "/index.html";
    }
    
    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }
    
    /**
     * 点击登录执行的动作
     * 登录后若未曾更改初始密码，则进入修改密码页面，要求用户更改密码初始密码
     * 与获取供应商信息接口合并
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @AdminOperationLog
    public String loginVali() {

        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String remember = super.getPara("remember");
        
        //验证验证码是否正确
        if (KaptchaUtil.getKaptchaOnOff()) {
            String kaptcha = super.getPara("kaptcha").trim();
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new InvalidKaptchaException();
            }
        }

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        currentUser.login(token);

        ShiroUser shiroUser = ShiroKit.getUser();
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("username", shiroUser.getAccount());

        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));

        ShiroKit.getSession().setAttribute("sessionFlag", true);
        int isOriginalPwd = userMapper.selectById(shiroUser.getId()).getIsOriginalpassword();
        if(isOriginalPwd == ORIGINAL_PASSWORD ){
        	return REDIRECT+"/modifyPassword";
        }
        return REDIRECT + "/";
    }
    
    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @AdminOperationLog
    public String logOut() {
        LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getUser().getId(), getIp()));
        ShiroKit.getSubject().logout();
        return REDIRECT + "/login";
    }
    
}
