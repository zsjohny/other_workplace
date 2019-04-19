package com.jiuy.core.meta.logistics;

import com.jiuyuan.entity.logistics.LOPostage;

public class LOLPostageVO extends LOPostage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6748181396528471158L;
	
	private String provinceName;
	
	private String cityName;

	public String getLocationName() {
		return provinceName + " " + cityName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}
