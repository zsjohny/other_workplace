package com.jiuy.base.util;

import java.lang.reflect.InvocationTargetException;

import org.springframework.web.context.ContextLoader;

@SuppressWarnings("unchecked")
public class BeanUtil {

	public static <T> T getBean(String bean) {

		return (T) ContextLoader.getCurrentWebApplicationContext().getBean(bean);
	}

	public static <T> T getBean(Class<?> bean) {
		return (T) ContextLoader.getCurrentWebApplicationContext().getBean(bean);
	}

	public static <T> T invoke(String bean, String method) {

		Object ob = getBean(bean);
		Object rs = null;
		try {
			rs = ob.getClass().getMethod(method).invoke(ob);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) rs;
	}

	public static <T> T invoke(Class<?> bean, String method) {
		Object ob = getBean(bean);
		Object rs = null;
		try {
			rs = ob.getClass().getMethod(method).invoke(ob);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) rs;
	}

	public static <T> T invoke(String bean, String method, Class<?>[] paramClass, Object... args) {
		Object ob = getBean(bean);
		Object rs = null;
		try {
			rs = ob.getClass().getMethod(method, paramClass).invoke(ob, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) rs;
	}

	/**
	 * 抛出异常
	 * 
	 * @param bean
	 * @param method
	 * @param paramClass
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @author lcm
	 * @createTime 2017年4月12日 下午6:00:35
	 */
	public static <T> T invokeWithThrowException(String bean, String method, Class<?>[] paramClass, Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {
		Object ob = getBean(bean);
		Object rs = null;
		rs = ob.getClass().getMethod(method, paramClass).invoke(ob, args);
		return (T) rs;
	}

	public static <T> T invoke(Class<?> bean, String method, Class<?>[] paramClass, Object... args) {
		Object ob = getBean(bean);
		Object rs = null;
		try {
			rs = ob.getClass().getMethod(method, paramClass).invoke(ob, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) rs;
	}

	/**
	 * 抛出异常
	 * 
	 * @param bean
	 * @param method
	 * @param paramClass
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @author lcm
	 * @createTime 2017年4月12日 下午6:00:03
	 */
	public static <T> T invokeWithThrowException(Class<?> bean, String method, Class<?>[] paramClass, Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {
		Object ob = getBean(bean);
		Object rs = null;
		rs = ob.getClass().getMethod(method, paramClass).invoke(ob, args);
		return (T) rs;
	}

	// class invokeOb<T>{
	//
	// private Object obj;
	//
	// private Method method;
	//
	// public invokeOb getMehod(String bean,String methodName,Class<?> ...
	// paramClass){
	// this.obj = getBean(bean);
	// try {
	// this.method = this.obj.getClass().getMethod(methodName, paramClass);
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// }
	// return this;
	// }
	//
	// public invokeOb getMehod(Class<?> bean,String methodName,Class<?> ...
	// paramClass){
	// this.obj = getBean(bean);
	// try {
	// this.method = this.obj.getClass().getMethod(methodName, paramClass);
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// }
	// return this;
	// }
	// }
}
