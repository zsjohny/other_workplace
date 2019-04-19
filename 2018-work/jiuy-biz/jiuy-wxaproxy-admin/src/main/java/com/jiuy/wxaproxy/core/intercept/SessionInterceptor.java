package com.jiuy.wxaproxy.core.intercept;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.admin.core.base.controller.BaseController;
import com.admin.core.util.HttpSessionHolder;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;

/**
 * 静态调用session的拦截器
 *
 * @author fengshuonan
 * @date 2016年11月13日 下午10:15:42
 */
@Aspect
@Component
public class SessionInterceptor extends BaseController {

    @Pointcut("execution(* com.jiuy.wxaproxy.*..controller.*.*(..))")
    public void cutService() {
    }

    @Around("cutService()")
    public Object sessionKit(ProceedingJoinPoint point) throws Throwable {
    	
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

        HttpSessionHolder.put(super.getHttpServletRequest().getSession());
        try {
            return point.proceed();
        } finally {
            HttpSessionHolder.remove();
        }
    }
}
