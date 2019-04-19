package com.jiuyuan.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用于切换ids的格式
 * @author nijin
 *
 */
public class IdsToStringUtil {
  
	/**
	 * 由id,id,id转换成格式,id,id,id,
	 */
	public static String getIdsToString(String ids){
		if(ids.isEmpty()){
			return "";
		}
		String[] idsArray = ids.trim().split(",");
		StringBuffer stringBuffer = new StringBuffer(",");
		for(String id:idsArray){
			stringBuffer.append(id).append(",");
		}
		return stringBuffer.toString();
	}
	/**
	 * 把ids转换成集合{",id,", ",id,"}
	 * 
	 */
	public static List<String> getIdsToList(String ids){
		if(ids.isEmpty()){
			return null;
		}
		String[] idsArray = ids.trim().split(",");
		List<String> list = new ArrayList<String>();
		for(String id:idsArray){
			if(!id.trim().equals("")){
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(",").append(id.trim()).append(",");
				list.add(stringBuffer.toString());
			}
		}
		return list;
	}
	/**
	 * 把ids转换成集合{"id","id"}
	 * 
	 */
	public static List<String> getIdsToListNoKomma(String ids){
		if(ids.isEmpty()){
			return new ArrayList<String>();
		}
		String[] idsArray = ids.trim().split(",");
		List<String> list = new ArrayList<String>();
		for(String id:idsArray){
			if(!id.trim().equals("")){
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(id.trim());
				list.add(stringBuffer.toString());
			}
		}
		return list;
	}
	/**
	 * 把ids转换成集合{"id","id"}
	 * 
	 */
	public static List<Long> getIdsToListNoKommaL(String ids){
		if(ids.isEmpty()){
			return new ArrayList<Long>();
		}
		String[] idsArray = ids.trim().split(",");
		List<Long> list = new ArrayList<Long>();
		for(String id:idsArray){
			if(!id.trim().equals("")){
				Long subid = Long.parseLong(id);
				list.add(subid);
			}
		}
		return list;
	}
	/**
	 * 把集合List<String>转换成String ,name,name,
	 * @param args
	 */
	public static String ListToString(List<String> list){
		StringBuffer stringBuffer = new StringBuffer();
		for(String name:list){
			stringBuffer.append(",").append(name);
		}
		return stringBuffer.append(",").toString();
	}
	/**
	 * 把集合List<String>转换成String ,name,name,
	 * @param args
	 */
	public static String SetToString(Set<String> set){
		StringBuffer stringBuffer = new StringBuffer();
		for(String name:set){
			stringBuffer.append(",").append(name);
		}
		return stringBuffer.append(",").toString();
	}
	
	
	
	public static void main(String[] args) {
		String a =IdsToStringUtil.getIdsToString("8989,897371");
		List<String> b = getIdsToList("");
		for(String test:b){
			System.out.println(test);
		}
	}
}
