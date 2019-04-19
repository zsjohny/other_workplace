package com.jiuyuan.entity;

import java.io.Serializable;

/**
* @author WuWanjian
* @version 创建时间: 2016年10月27日 下午3:29:44
*/
public class City implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9174364322206689635L;

	private long CityCode;
	
	private String CityName;
	
	private long ParentCode;

	public long getCityCode() {
		return CityCode;
	}

	public void setCityCode(long cityCode) {
		CityCode = cityCode;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public long getParentCode() {
		return ParentCode;
	}

	public void setParentCode(long parentCode) {
		ParentCode = parentCode;
	}
	
	
}
