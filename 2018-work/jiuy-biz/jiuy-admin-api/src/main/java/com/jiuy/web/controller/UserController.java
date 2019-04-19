/**
 * created on 2015/06/20
 * 1、登录注册相关逻辑控制器，处理首页数据加载、用户登录、注册等控制
 * 2、增加用户信息修改功能
 * 
 * updated on 2015/09/06
 * 1. 按照新的用户模型进行校验处理
 * 2. 支持多种验证方式
 */
package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.service.DbService;
import com.jiuy.core.service.UserService;
import com.jiuy.core.service.admin.AdminLogService;
import com.jiuy.web.controller.util.JSONStringUtil;
import com.jiuy.web.controller.util.model.CommonResponseObject;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
 * @author LWS
 *
 */
@Controller
public class UserController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5690282629777199748L;
	
	private static Logger logger = Logger.getLogger(UserController.class);
	
	/**
	 * 邮箱地址连接串
	 *//*
	private final String CONCAT_STRING = "ZZZZZZ";
	private final String MAIL_SUBJECT = "俞姐姐账户电子邮件";
	
	private final boolean _DEBUG = true;*/
	
	@Resource
	private UserService userService;	

	@Resource
	public AdminLogService alService;
	
	@Resource
	private DbService dbService;
	
	/**
	 * 修复数据
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/db", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@Login
	public String database(HttpServletRequest request, ModelMap modelMap){
//		dbService.db();
		CommonResponseObject<Object> resp = new CommonResponseObject<Object>(0, "", "");
		String responseString = JSONStringUtil.object2String(resp);
		return responseString;
	}

	
	   
	/*******************************处理页面用户请求****************************/
	/**
	 * 登录使用的请求处理方法
	 * 创建日期: 2015/05/20
	 * 创建内容: 获取用户名密码进行登录校验
	 * 修改记录：
	 * 修改者：LWS
	 * 修改日期：2015/05/23
	 * 1）增加密码MD5加密；
	 * 
	 * 修改者：LWS
	 * 修改日期：2015/05/25
	 * 1）修改控制器返回类型为JSON
	 * 
	 * 修改者：LWS
	 * 修改日期：2015/05/26
	 * 1）增加session控制
	 * 
	 * 修改者：LWS
	 * 修改日期：2015/12/12
	 * 1)修改管理員帳戶模型
	 * 
	 * @author LWS
	 * 
	 * @param username
	 * @param password
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/login", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@AdminOperationLog
	public String login(HttpServletRequest request,String username,String password,ModelMap modelMap)
	{
    	String ip = CommonResponseObject.getRemortIP(request);
//    	String ip = "";
		AdminUser user = new AdminUser();
		user.setUserPassword(DigestUtils.md5Hex(password));
		user.setUserName(username);
		AdminUser userinfo = userService.login(user);
		CommonResponseObject<Object> resp = null;
		if(userinfo == null) {
			resp = new CommonResponseObject<Object>(1, "用户名或者密码错误",username);
		}else{
			resp = new CommonResponseObject<Object>(0, "", userinfo);
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(30*60);//以秒为单位
			session.setAttribute("userid", userinfo.getUserId());
			session.setAttribute("userinfo", userinfo);
			session.setAttribute("ip", ip);
			
			alService.addAdminLog(userinfo, "UserController-->login", userinfo.getUserName()+"登录成功", ip);
		}
		String responseString = JSONStringUtil.object2String(resp);
		return responseString;
	} 
	
	
	/**
	 * 注销登录请求处理方法
	 * 创建日期: 2015/05/27
	 * 创建内容: 获取session置为失效
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/logout", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@AdminOperationLog
	public String logout(HttpServletRequest request,ModelMap modelMap){
    	String ip = CommonResponseObject.getRemortIP(request);
//    	String ip = "";
		if(null != request){
			HttpSession session = request.getSession();
			AdminUser userinfo = (AdminUser)session.getAttribute("userinfo");
			session.invalidate();
			session = null;
			if (userinfo != null)
				alService.addAdminLog(userinfo, "UserController-->logout", userinfo.getUserName()+"登出成功", ip);
		}
		

		
		CommonResponseObject<String> resp = null;
		resp = new CommonResponseObject<String>(0, "", "success");
		String responseString = JSONStringUtil.object2String(resp);
		return responseString;
	}

	/************************************修改密码**************************************/
	@RequestMapping(value = "/change_pwd", method = RequestMethod.GET)
	@AdminOperationLog
	public String classify() {
		return "page/change_pwd";
	}
	
	/************************************欢迎**************************************/
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	@Login
	@ResponseBody
	public JsonResponse welcome() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, String> data = new HashMap<String, String>();
		data.put("info", "欢迎使用俞姐姐后台管理系统！");
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 修改密码请求处理方法
	 * 创建日期: 2016/06/27
	 * 创建内容: 获取session置为失效
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/resetPassword", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@Login
	public String resetPassword(HttpServletRequest request,String oldPassword,String newPassword, String newPassword2, ModelMap modelMap){
		Long userId = (Long)request.getSession().getAttribute("userid");
				
		if (userId == null) return JSONStringUtil.object2String(new CommonResponseObject<String>(-1, "用户没有登录", "failed"));
		
		if (!newPassword.equals(newPassword2)) return JSONStringUtil.object2String(new CommonResponseObject<String>(-1, "用户两次密码不一致", "failed"));
		
		int count = userService.updateUserPassword(userId,DigestUtils.md5Hex(oldPassword),DigestUtils.md5Hex(newPassword));
		CommonResponseObject<String> resp = null;
		if(count == 1){
			request.getSession().invalidate();
			
			resp = new CommonResponseObject<String>(0, "", "success");
		}else{
			resp = new CommonResponseObject<String>(-2, "用户修改密码失败", "failed");
		}
		// 返回整个用户信息对象
		String responseString = JSONStringUtil.object2String(resp);
		return responseString;
	}
		
	/**
	 * 发送修改密码请求
	 * 创建日期: 2015/05/27
	 * 创建内容: 生成修改密码链接
	 * 
	 * @param email
	 * @return
	 *//*
	@RequestMapping(value = "/sendmodpassemail", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String sendmodpassemail(HttpServletRequest request, String email,String validcode) {
		logger.debug("check email:" + email);
		HttpSession session = request.getSession();
		String validateCode = (String) session.getAttribute("validateCode");
		String responseString = null;
		CommonResponseObject<String> resp = null;
		if(validateCode.equals(validcode)){
			boolean exsist = userService.checkEmailStatus(email);
			if(exsist){
				// 處理郵件內容
				StringBuffer sb = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				StringBuffer sb3 = new StringBuffer();
				sb.append("您好：<br/>");
				sb.append("<br/>");
				sb.append(email);
				sb.append("在俞姐姐www.yjj.com 发起找回密码请求，请点击以下链接进行密码找回：");
				sb2.append("<a href='");
				if (_DEBUG) {
					sb3.append("http://localhost:8080/sas/getpassword?token=");
				} else {
					sb3.append("http://www.yujiejie.com/getpassword?token=");
				}
				String preToken = mailEncode(email,null,null);
				sb3.append(preToken);
				sb2.append(sb3);
				sb2.append("'>找回密码</a>");
				sb.append(sb2);
				// 发送邮件
				MailUtil.send(MAIL_SUBJECT, sb.toString(), email);
				resp = new CommonResponseObject<String>(0, "", sb3.toString());
				responseString = JSONStringUtil.object2String(resp);
			}else{
				resp = new CommonResponseObject<String>(0, "failed", email);
				responseString = JSONStringUtil.object2String(resp);
			}
		}else{
			resp = new CommonResponseObject<String>(0, "failed", validcode);
			responseString = JSONStringUtil.object2String(resp);
		}
		return responseString; 
	}
	
	*//**
	 * 处理修改密码请求
	 * 创建日期: 2015/05/27
	 * 创建内容: 更新用户密码
	 * 
	 * @param email
	 * @param password
	 * @return
	 *//*
	@RequestMapping(value = "/modifypassword", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String modifypassword(String email, String password){
		int count = userService.updateUserPassword(email,getEncoded(password));
		CommonResponseObject<String> resp = null;
		if(count == 1){
			resp = new CommonResponseObject<String>(0, "", "success");
		}else{
			resp = new CommonResponseObject<String>(0, "", "failed");
		}
		// 返回整个用户信息对象
		String responseString = JSONStringUtil.object2String(resp);
		return responseString;
	}
	
	/**************************处理页面加载请求**********************************//*
	/**
	 * 请求加载登录页面
	 * 创建日期: 2015/05/24
	 * 创建内容: 直接返回登陆页面URL
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/")
	public String getLogin(ModelMap modelMap){
		/*modelMap.addAttribute("lib_root", "src/js/lib/nej/");*/
		return "/";
	//	return "page/change_pwd";
	}
	
    /**
	 * 请求加载忘记密码填写邮箱页面
	 * 创建日期: 2015/06/02
	 * 创建内容: 直接返回忘记密码填写邮箱页面
	 * 
	 * @param modelMap
	 * @return
	 *//*
	@RequestMapping(value = "/getforgotenpass", produces = { "application/json;charset=UTF-8" })
	public String getForgorttenPass(ModelMap modelMap){
		return "page/login_findpwd";
	}
	
	/**
	 * 修改密码页面
	 * 创建日期: 2015/05/27
	 * 创建内容: 1. 解析token，可以是激活码可以是链接
	 * 		  2. 判断是否已经超时
	 * 		  3. 生成实际修改密码页面
	 * 
	 * @param token
	 * @param modelMap
	 * @return
	 *//*
	@RequestMapping("/getpassword")
	public String getpassword(String token,ModelMap modelMap)
	{
		logger.debug("activate password modification url:" + token);
		String email = mailDecode(token,true);
		if(!"".equals(email)){
			modelMap.addAttribute("email", email);
			return "page/register_findpwd";
		}else{
			return "commonredirect";
		}
	}
	
	*//**
	 * 生成验证码图片
	 * @author modified from http://www.osblog.net/code/141.html
	 * 
	 * @param req
	 * @param resp
	 *//*
	@RequestMapping("/image")
	public void loadImage(HttpServletRequest req, HttpServletResponse resp) {
		*//**
		 * 验证码图片的宽度。
		 *//*
		final int width = 70;
		*//**
		 * 验证码图片的高度。
		 *//*
		final int height = 30;
		*//**
		 * 验证码字符个数
		 *//*
		final int codeCount = 4;
		*//**
		 * xx
		 *//*
		int xx = 0;
		*//**
		 * 字体高度
		 *//*
		int fontHeight;
		*//**
		 * codeY
		 *//*
		int codeY;
		*//**
		 * codeSequence
		 *//*
		String[] codeSequence = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m",
				"n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
				"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
				"M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		xx = width / (codeCount + 2); // 生成随机数的水平距离
		fontHeight = height - 12; // 生成随机数的数字高度
		codeY = height - 8; // 生成随机数的垂直距离

		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D gd = buffImg.createGraphics();

		// 创建一个随机数生成器类
		Random random = new Random();

		// 将图像填充为白色
		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		// 设置字体。
		gd.setFont(font);

		// 画边框。
		gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);

		// 随机产生4条干扰线，使图象中的认证码不易被其它程序探测到。
		gd.setColor(Color.BLACK);
		for (int i = 0; i < 4; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gd.drawLine(x, y, x + xl, y + yl);
		}

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			String strRand = String.valueOf(codeSequence[random.nextInt(27)]);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = random.nextInt(125);
			green = random.nextInt(255);
			blue = random.nextInt(200);

			// 用随机产生的颜色将验证码绘制到图像中。
			gd.setColor(new Color(red, green, blue));
			gd.drawString(strRand, (i + 1) * xx, codeY);

			// 将产生的四个随机数组合在一起。
			randomCode.append(strRand);
		}
		// 将四位数字的验证码保存到Session中。
		HttpSession session = req.getSession();
		session.setAttribute("validateCode", randomCode.toString());

		// 禁止图像缓存。
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);

		resp.setContentType("image/jpeg");

		// 将图像输出到Servlet输出流中。
		ServletOutputStream sos = null;
		try {
			sos = resp.getOutputStream();
			ImageIO.write(buffImg, "jpeg", sos);
			sos.close();
			sos = null;
		} catch (IOException e) {
			logger.error(e);
		}

	}
	
	


    /**
     * 判断用户是否存在
     * @param request
     * @param userId
     * @param modelMap
     * @return
     *//*
    @RequestMapping(value = "/checkuser", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String checkUser(HttpServletRequest request,String userId,ModelMap modelMap)
    {
        YjjUser user = new YjjUser();
        user.setUserName(userId);
        YjjUser userinfo = userService.login(user);
        //存在用户为0，不存在为1.先默认存在
        String responseString="";
        if (null!= userinfo){
            responseString = "0";
        }else{
            responseString = "1";
        }
        return responseString;
    }*/
	
}
