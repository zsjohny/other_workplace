package com.xiaoluo.util;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 代表aop执行的顺序的问题，值越小越优先--可以用于验证和写日志的顺序先后
@Order(1)
@Aspect
@Component
public class SpringAopUtil {

	// logger日志结合打印
	Logger logger = Log4jUtil.init(SpringAopUtil.class);

	// 前置通知，*包含任意个位置的..任意参数
	@Before(value = "execution(public com.ouliao.controller.versionfirst.UserCallMarkController.*(.. ))")
	public void beforeValidation() {
		System.out.println("____________________________");
		logger.fatal("into validation..");
	}

	// 后置通知
	// JoinPoint代表切入点
	@After(value = "execution(public  com.ouliao.controller.versionfirst.UserCallMarkController.*(.. ))")
	public void afterValidation(JoinPoint joinPoint) {
		System.out.println("...11111.");
		logger.warn("end of validation....");
		// String methodName = joinPoint.getSignature().getName();
		// System.out.println(methodName);
	}

	/*
	 * // 环绕通知
	 *
	 * @Around(value = "execution(public * com.aop.proxy.Shopping.*(.. )) )")
	 * public Object aroundValidation(ProceedingJoinPoint point) { // 执行方法
	 * Object object = null;
	 *
	 * try { // 相当于前置通知 System.out.println("into globalHandle..."); object =
	 * point.proceed(); // 相当于后置返回通知 } catch (Throwable e) { // 相当于异常通知 } //
	 * 相当于后置通知 return point;
	 *
	 * }
	 */

}
