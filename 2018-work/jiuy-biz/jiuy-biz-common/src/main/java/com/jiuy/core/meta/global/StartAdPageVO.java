package com.jiuy.core.meta.global;

import java.io.Serializable;

public class StartAdPageVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4821889626688690350L;
	
	private String imageUrl;
	
	private String linkUrl; 
	
	private int delayTime;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	
}
