/**
 * 
 */
package com.jiuy.web.controller.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.service.admin.AdminLogService;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.CouponPermission;
import com.jiuyuan.util.anno.FinancialPermisson;



/**
 * @author LWS
 */
@Component
@Aspect
public class AOPCommon {
    Logger logger = Logger.getLogger(AOPCommon.class);

	@Resource
	public AdminLogService alService;

    @Pointcut("execution(* com.jiuy.web.controller.*Controller*.*(..))")
    public void pointCut() {
    }

    /*
    @Around("pointCut()")
    public Object checkLogingStatus(ProceedingJoinPoint pjp) {
		String redirect = "redirect:/loginpage";
        try {
            Object objTarget = pjp.getTarget();
            String methodName = pjp.getSignature().getName();
            Method method = BeanUtils.findMethodWithMinimalParameters(objTarget.getClass(), methodName);
            boolean noLoginRequired = AnnotationUtils.findAnnotation(method, NoLogin.class) != null;
            if (noLoginRequired) {
                return pjp.proceed();
            }
            boolean loginRequired = AnnotationUtils.findAnnotation(objTarget.getClass(), Login.class) != null ||
                AnnotationUtils.findAnnotation(method, Login.class) != null;
            if (!loginRequired) {
                return pjp.proceed();
            } else {

				HttpSession session = getCurrentSession();
				if (session.getAttribute("userid") != null) {
//					logger.debug("user check success!!");
					try {
						return pjp.proceed();
					} catch (Throwable e) {
						logger.error("error happens after enhancing!!", e);
                    }
				} else {
					logger.debug("user check failed!!");
					return redirect;
                }
			}
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        logger.debug("user status not found!!");
        return redirect;
    }*/



    @SuppressWarnings("unused")
	@Around("pointCut()")
    public Object addLogInfo(ProceedingJoinPoint pjp) {
//    	String ip = "";
        try {
            Object objTarget = pjp.getTarget();
            Object[] args = pjp.getArgs();

            String methodName = pjp.getSignature().getName();
            Method method = BeanUtils.findMethodWithMinimalParameters(objTarget.getClass(), methodName);
            
            boolean logRequired = AnnotationUtils.findAnnotation(objTarget.getClass(), AdminOperationLog.class) != null ||
                AnnotationUtils.findAnnotation(method, AdminOperationLog.class) != null;
            if (logRequired) {
				HttpSession session = getCurrentSession();

				AdminUser userinfo = (AdminUser)session.getAttribute("userinfo");
				String ip = (String) session.getAttribute("ip");
				StringBuilder parameters = new StringBuilder();
				for (Parameter parameter : method.getParameters()) {
					parameters.append(parameter.toString());
				}
				if (userinfo != null)
					alService.addAdminLog(userinfo, objTarget.getClass().getCanonicalName(), userinfo.getUserName() + "执行" + objTarget.getClass().getCanonicalName() + "->" 
							+ method.getName() +"(" + Arrays.toString(pjp.getArgs()) + ")" +  " 操作成功", ip);
			}
			return pjp.proceed();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }
    
    @SuppressWarnings("unused")
    @Around("pointCut()")
    public Object checkCouponPermission(ProceedingJoinPoint pjp) {
        try {
            Object objTarget = pjp.getTarget();
            Object[] args = pjp.getArgs();

            String methodName = pjp.getSignature().getName();
            Method method = BeanUtils.findMethodWithMinimalParameters(objTarget.getClass(), methodName);
            
            boolean checkRequired = AnnotationUtils.findAnnotation(objTarget.getClass(), CouponPermission.class) != null ||
                AnnotationUtils.findAnnotation(method, CouponPermission.class) != null;
            if (checkRequired) {
				HttpSession session = getCurrentSession();

				AdminUser userinfo = (AdminUser)session.getAttribute("userinfo");
				if (userinfo == null || userinfo.getUserId() != 14) {
					return "";
				} else {
				}
			}
			return pjp.proceed();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }
    
    @Around("pointCut()")
    public Object checkFinancialPermission(ProceedingJoinPoint pjp) {
        try {
            Object objTarget = pjp.getTarget();

            String methodName = pjp.getSignature().getName();
            Method method = BeanUtils.findMethodWithMinimalParameters(objTarget.getClass(), methodName);

            boolean checkRequired =
                AnnotationUtils.findAnnotation(objTarget.getClass(), FinancialPermisson.class) != null ||
                    AnnotationUtils.findAnnotation(method, FinancialPermisson.class) != null;
            if (checkRequired) {
                HttpSession session = getCurrentSession();

                AdminUser userinfo = (AdminUser) session.getAttribute("userinfo");
                if (userinfo == null || !AdminConstants.FINANCE_NAME.contains(userinfo.getUserName())) {
                    System.out.println("permission deny");
                    return "";
                } else {
                    System.out.println("permission right");
                }
            }
            return pjp.proceed();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

	/**
	 * 获取当前session 对象
	 * 
	 * @return
	 */
	private HttpSession getCurrentSession(){
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return req.getSession();
    }
	
	
}
