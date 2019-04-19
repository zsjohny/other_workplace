package com.jiuy.wxaproxy.common.constant.dictmap.factory;

import java.lang.reflect.Method;

import com.admin.core.exception.BizExceptionEnum;
import com.admin.core.exception.BussinessException;
import com.jiuy.wxaproxy.common.constant.factory.IWxaproxyConstantFactory;
import com.jiuy.wxaproxy.common.constant.factory.WxaproxyConstantFactory;

/**
 * 字段的包装创建工厂
 *
 * @author fengshuonan
 * @date 2017-05-06 15:12
 */
public class DictFieldWarpperFactory {

	 public static Object createFieldWarpper(Object field, String methodName) {
	        IWxaproxyConstantFactory me = WxaproxyConstantFactory.me();
	        try {
	            Method method = IWxaproxyConstantFactory.class.getMethod(methodName, field.getClass());
	            Object result = method.invoke(me, field);
	            return result;
	        } catch (Exception e) {
	            try {
	                Method method = IWxaproxyConstantFactory.class.getMethod(methodName, Integer.class);
	                Object result = method.invoke(me, Integer.parseInt(field.toString()));
	                return result;
	            } catch (Exception e1) {
	                throw new BussinessException(BizExceptionEnum.ERROR_WRAPPER_FIELD);
	            }
	        }
	    }

}
