package com.jiuy.core.meta.homepage;

import java.io.Serializable;

public class HomeFloorVO extends HomeFloor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3800437153313220054L;

	private String imgUrl;
	
	private String homeTemplateName;
	
	public String getHomeTemplateName() {
		return homeTemplateName;
	}
	
	public void setHomeTemplateName(String homeTemplateName) {
		this.homeTemplateName = homeTemplateName;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
}
