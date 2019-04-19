
package com.jfinal.third.api;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;

/**
 *
 *
 * @author 赵兴林
 *
 */
public class MsgCallbackInterceptor implements Interceptor {
	 private static final Log logger = Log.getLog(MsgCallbackInterceptor.class);

	@Override
	public void intercept(Invocation inv) {
		 Controller controller = inv.getController();
		logger.info("***MsgCallbackInterceptor");
		inv.invoke();


	}








}
