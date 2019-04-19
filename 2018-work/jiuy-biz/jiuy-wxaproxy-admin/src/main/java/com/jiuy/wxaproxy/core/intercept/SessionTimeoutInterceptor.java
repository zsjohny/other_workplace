package com.jiuy.wxaproxy.core.intercept;

import java.lang.reflect.Method;

import org.apache.shiro.session.InvalidSessionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.admin.core.base.controller.BaseController;
import com.admin.core.support.HttpKit;
import com.jiuy.wxaproxy.core.shiro.ShiroKit;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;

/**
 * 验证session超时的拦截器
 *
 * @author fengshuonan
 * @date 2017年6月7日21:08:48
 */
@Aspect
@Component
// todo:
// @ConditionalOnProperty(prefix = "guns", name = "session-open", havingValue =
// "true")
public class SessionTimeoutInterceptor extends BaseController {

	@Pointcut("execution(* com.jiuy.wxaproxy.*..controller.*.*(..))")
	public void cutService() {
	}

	@Around("cutService()")
	public Object sessionTimeoutValidate(ProceedingJoinPoint point) throws Throwable {

		String servletPath = HttpKit.getRequest().getServletPath();
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

		if (servletPath.equals("/kaptcha") || servletPath.equals("/login")
				|| servletPath.equals("/global/sessionError")) {
			return point.proceed();
		} else {
			if (ShiroKit.getSession().getAttribute("sessionFlag") == null) {
				ShiroKit.getSubject().logout();
				throw new InvalidSessionException();
			} else {
				return point.proceed();
			}
		}
	}
}
