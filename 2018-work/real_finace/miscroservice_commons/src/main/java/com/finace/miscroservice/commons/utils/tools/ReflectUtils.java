package com.finace.miscroservice.commons.utils.tools;

import java.lang.reflect.Method;

/**
 * 
 * @ClassName:     ReflectUtils.java
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-9-3 下午4:17:45
 * <b>Copyright (c)</b> 一桶金版权所有 <br/>
 */
public class ReflectUtils {
	public static Object invokeGetMethod(Class claszz,Object o,String name){
		Object ret=null;
		try {
			Method method=claszz.getMethod("get"+TextUtil.firstCharUpperCase(name));
			ret=method.invoke(o);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret;
	}
	
	
	public static Object invokeSetMethod(Class claszz,Object o,String name,Class[] argTypes,Object[] args){
		Object ret=null;
		try {
			Method method=claszz.getMethod("set"+TextUtil.firstCharUpperCase(name),argTypes);
			ret=method.invoke(o,args);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret;
	}
	
	public static Object invokeSetMethod(Class claszz,Object o,String name,Class argType,Object args){
		Object ret=null;
		try {
			Method method=claszz.getMethod("set"+TextUtil.firstCharUpperCase(name),new Class[]{argType});
			ret=method.invoke(o,new Object[]{argType});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret;
	}
	
}