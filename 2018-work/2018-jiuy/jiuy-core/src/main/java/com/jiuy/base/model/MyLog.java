package com.jiuy.base.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.jiuy.base.annotation.PrimaryKey;
import com.jiuy.base.enums.ModelType;
import com.jiuy.base.util.Biz;
import lombok.Data;


/**
 * 日志类
 * @author Aison
 * @version V1.0
 * @Copyright 玖远网络
 * @date 2018/6/5 16:22
 */
@Data
public class MyLog<T>{

	/**
	 * 实际的日志
	 */
	private List<String> log;

	/**
	 * 操作的用户
	 */
	private UserSession userSession;

	/**
	 * 日志是属于哪个模块的
	 */
	private ModelType model;

	/**
	 * 操作的方法名称
	 */
	private String method;

	/**
	 * 某些service需要返回数据
	 */
	private T data;

	/**
	 * 操作的数据的id
	 */
	private String dataId;


	public String getDataId() {
		return dataId;
	}

	private void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public T getData() {
		return data;
	}

	public MyLog<T> setData(T data) {
		this.data = data;
		return this;
	}

	/**
	 * 添加的日志
	 * @param logObject 需要记录日志的对象
	 * @param userSession 操作用户
	 * @author Aison
	 * @date 2018/6/5 18:31
	 */
	public static <E> MyLog<E> addLog(Object logObject,UserSession userSession ) {
		return new MyLog<E>(logObject,Type.add,userSession);
	}

	/**
	 * 修改的日志
	 * @param logOld 操前的对象
	 * @param logNew 操作后的对象
	 * @param userSession 操作用户
	 * @author Aison
	 * @date 2018/6/5 18:31
	 */
	public static <E> MyLog<E> update(Object logOld,Object logNew,UserSession userSession) {
		return new MyLog<>(logOld,logNew,userSession);
	}

	/**
	 * 删除的日志
	 * @param logObj 操前的对象
	 * @param userSession 操作用户
	 * @author Aison
	 * @date 2018/6/5 18:31
	 */
	public static <E> MyLog<E> del(Object logObj,UserSession userSession) {
		return new MyLog<>(logObj,Type.del,userSession);
	}

	public MyLog(){

	}
	/**
	 * 添加 删除的日志
	 * @param ob 添加的对象
	 * @param dbType 操作类型
	 * @author Aison
	 * @date 2018/6/5 16:31
	 */
	public MyLog(Object ob, Type dbType,UserSession userSession) {
		moreLog(ob, dbType);
		this.userSession = userSession;
		getPrimaryKey(ob);
	}

	/**
	 * 修改的日志
	 * @param oldObj 老的对象
	 * @param newObj 新的对象
	 * @author Aison
	 * @date 2018/6/5 16:31
	 */
	public MyLog(Object oldObj,Object newObj, UserSession userSession) {
		StringBuilder stringBuilder = Biz.compareVoBuffer(oldObj,newObj);
		stringBuilder.insert(0, Type.up.getName());
		getList().add(stringBuilder.toString());
		this.userSession = userSession;
		getPrimaryKey(oldObj);
	}

	/**
	 * 更多添加日志
	 * @param ob 添加的对象
	 * @param dbType 操作类型
	 * @author Aison
	 * @date 2018/6/5 16:30
	 */
	public MyLog moreLog(Object ob, Type dbType) {
		StringBuffer sb = Biz.singlObjectStr(ob);
		sb.insert(0, dbType.getName());
		getList().add(sb.toString());
		return this;
	}

	/**
	 * 更多修改日志
	 * @param oldOb 老的对象
	 * @param newOb 新的对象
	 * @param dbType 操作类型
	 * @author Aison
	 * @date 2018/6/5 16:30
	 */
	public MyLog moreLog(Object oldOb,Object newOb, Type dbType) {
		StringBuilder sb = Biz.compareVoBuffer(oldOb,newOb);
		sb.insert(0, dbType.getName());
		getList().add(sb.toString());
		return this;
	}

	/**
	 * 查询某个属性的主键值
	 * @param object
	 * @author Aison
	 * @date 2018/6/6 10:22
	 */
	private Object getPrimaryKey(Object object){
		Field[] fields = object.getClass().getDeclaredFields();
		Field key = null;
		for (Field field : fields) {
			PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
			if(primaryKey!=null) {
				key = field;
				break;
			}
		}
		Object id = null;
		if(key!=null) {
			key.setAccessible(true);
			try{
				id = key.get(object);
			}catch (Exception e) {
			    e.printStackTrace();
			}
		}
		this.dataId = id == null ? "" : id.toString();
		return id;
	}


	private List<String> getList() {
		if (log == null || log.isEmpty()) {
			log = new ArrayList<>();
		}
		return this.log;
	}

	public enum Type {
		/**
		 * 添加类型的枚举
		 */
		add("添加  "),
		/**
		 * 删除类型的枚举
		 */
		del("删除  "),
		/**
		 * 修改类型的枚举
		 */
		up("修改  ");
		private String name;
		Type(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

}
