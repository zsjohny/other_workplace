package com.jiuyuan.entity;

import java.io.Serializable;

/**
* @author WuWanjian
* @version 创建时间: 2016年10月27日 下午3:24:26
*/
public class Province implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4778326362784892733L;

	private long provinceCode;
	
	private String provinceName;

	public long getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(long provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	
}
