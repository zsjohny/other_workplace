package com.jiuyuan.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductFilterVO implements Serializable {

    private static final long serialVersionUID = -8352649344675273002L;

    private long id;

    private String propertyValue;

    private String iconUrl;

    private String iconOnUrl;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getIconOnUrl() {
		return iconOnUrl;
	}

	public void setIconOnUrl(String iconOnUrl) {
		this.iconOnUrl = iconOnUrl;
	}




}
