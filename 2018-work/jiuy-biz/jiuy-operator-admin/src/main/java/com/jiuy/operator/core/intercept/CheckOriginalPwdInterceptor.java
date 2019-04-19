package com.jiuy.operator.core.intercept;

import static com.admin.core.support.HttpKit.getIp;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.admin.core.base.controller.BaseController;
import com.admin.core.log.LogManager;
import com.jiuy.operator.common.system.persistence.dao.UserMapper;
import com.jiuy.operator.common.system.persistence.model.User;
import com.jiuy.operator.core.log.factory.LogTaskFactory;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;

@Aspect
@Component
public class CheckOriginalPwdInterceptor extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(CheckOriginalPwdInterceptor.class);
	
	
	@Autowired
	private UserMapper UserMapper;
	
//	@Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//    	 // logger.info("登录检查拦截器CheckLoginInterceptor开始");
//        HandlerMethod method = (HandlerMethod) handler;
//        logger.info("验证");
//        boolean noLoginRequired = AnnotationUtils.findAnnotation(
//        		method.getMethod(), NoLogin.class) != null;
//        if (noLoginRequired) {
//        	  //logger.info("登录检查拦截器CheckLoginInterceptor----不需要验证");
//            return true;
//        }
//
//        boolean loginRequired =
//            AnnotationUtils.findAnnotation(method.getBeanType(), Login.class) != null ||
//                AnnotationUtils.findAnnotation(method.getMethod(), Login.class) != null;
//        if (!loginRequired) {
//        	 // logger.info("登录检查拦截器CheckLoginInterceptor----不需要验证");
//            return true;
//        }
//        ShiroUser shiroUser = ShiroKit.getUser();
//        User user = UserMapper.selectById(shiroUser.getId());
//        if (user.getId() > 0) {
//        	// logger.info("登录检查拦截器CheckLoginInterceptor----需要验证----验证通过");
//        	if(user.getIsOriginalpassword() == User.IS_ORIGINAL_PWD){
//        		JsonpResponse jsonpResponse = new JsonpResponse(request.getParameter("jsonp_callback"));
//                jsonpResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_UPDATE_PWD);
//                String text = "407";
//                HttpUtil.sendResponse(response, MimeTypes.APPLICATION_JSON, "UTF-8", text);
//                return false;
//        	}
//            return true;
//        }else{
//        	  //logger.info("登录检查拦截器CheckLoginInterceptor----需要验证----验证不通过");
//        }
//        return false;
//    }

	
	@Pointcut("execution(* com.jiuy.operator.*..controller.*.*(..))")
    public void cutService() {
    }

    @Around("cutService()")
    public Object checkOriginalPwd(ProceedingJoinPoint point) throws Throwable {
    	
    	String methodName=point.getSignature().getName(); 
    	Class<?> classTarget=point.getTarget().getClass();  
        Class<?>[] par= ((MethodSignature) point.getSignature()).getParameterTypes();  
        Method objMethod=classTarget.getMethod(methodName, par);
        boolean noLoginRequired = 
       		 AnnotationUtils.findAnnotation(classTarget, NoLogin.class) != null||
       		 AnnotationUtils.findAnnotation(objMethod, NoLogin.class) != null;
        if (noLoginRequired) {
       	  //logger.info("登录检查拦截器CheckLoginInterceptor----不需要验证");
          return point.proceed();
        }
        boolean loginRequired =
           AnnotationUtils.findAnnotation(classTarget, Login.class) != null ||
           AnnotationUtils.findAnnotation(objMethod, Login.class) != null;
        if (!loginRequired) {
       	 // logger.info("登录检查拦截器CheckLoginInterceptor----不需要验证");
           return point.proceed();
        }
    	
//        if(ShiroKit.getUser() == null){
//        	return point.proceed();
//        }
    	long userId = ShiroKit.getUser().getId();
    	User user = UserMapper.selectById(userId);
        try {
        	if(user.getIsOriginalpassword() == User.NOT_ORIGINAL_PWD ){
        		return point.proceed();
        	}else{
        		logger.info("该密码为原始密码，请先修改密码！userId:"+userId);
                LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getUser().getId(), getIp()));
                ShiroKit.getSubject().logout();
                return REDIRECT + "/login";
        	}
        }catch (Exception e){
        	logger.info(e.getMessage());
        	throw new Exception(e.getMessage());
        }
    }

}
