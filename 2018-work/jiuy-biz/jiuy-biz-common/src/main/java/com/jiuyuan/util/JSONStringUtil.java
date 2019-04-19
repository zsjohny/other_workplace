/**
 * 
 */
package com.jiuyuan.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * @author LWS
 * 
 * 用于JSON对象和String描述的互相转换
 */
public class JSONStringUtil<T extends Object> {
	/**
	 * get json string from object list
	 * 
	 * @param sourceList
	 * @return
	 */
	public static <T> String list2JSONArrayString(List<T> sourceList) {
		String json_arr = "[]";
		if (null == sourceList || sourceList.size() == 0) {
			return json_arr;
		}
		json_arr = JSON.toJSONString(sourceList, false); // set false to no
															// formatted
		logger.debug("result:");
		logger.debug(json_arr);
		return json_arr;
	}

	/**
	 * get jsonarray object from json string
	 * 
	 * @param source
	 * @return
	 */
	public static JSONArray string2JSONArray(String source) {
		if (null == source) {
			return null;
		}
		JSONArray jsonArray = JSON.parseArray(source);
		for (Object o : jsonArray) {
			logger.debug(o);
		}
		logger.debug(jsonArray);
		return jsonArray;
	}

	/**
	 * get object list from json string
	 * 
	 * @param source
	 * @param type 需要转换的类型
	 *           
	 * @return
	 */
	public static <T> List<T> string2ObjectList(String source, Class<T> type) {
		if (source == null) {
			return new ArrayList<T>(0);
		}
		// JSON array -> List
		List<T> objectList = (List<T>) JSON.parseArray(source, type);
		return objectList;
	}
	
	/**
	 * get object from json string description 
	 * 
	 * @param source
	 * @return
	 */
	public static <T> T string2Object(String source, Class<T> type){
		return JSON.parseObject(source, type);
	}
	
	/**
	 * get json string from object 
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> String object2String(T obj){
		return JSON.toJSONString(obj);
	}

	private static final Logger logger = Logger.getLogger(JSONStringUtil.class);
}
