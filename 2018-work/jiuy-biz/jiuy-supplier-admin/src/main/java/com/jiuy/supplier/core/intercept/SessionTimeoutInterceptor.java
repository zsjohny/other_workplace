package com.jiuy.supplier.core.intercept;

import org.apache.shiro.session.InvalidSessionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.admin.core.base.controller.BaseController;
import com.admin.core.support.HttpKit;
import com.jiuy.supplier.core.shiro.ShiroKit;

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

	@Pointcut("execution(* com.jiuy.supplier.*..controller.*.*(..))")
	public void cutService() {
	}

	@Around("cutService()")
	public Object sessionTimeoutValidate(ProceedingJoinPoint point) throws Throwable {

		String servletPath = HttpKit.getRequest().getServletPath();

		if (servletPath.equals("/kaptcha") || servletPath.equals("/login") || servletPath.startsWith("/quartzJob")
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
