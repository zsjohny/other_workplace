package com.jiuyuan.entity.product;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PayTypeVO implements Serializable {

    private static final long serialVersionUID = -8352649344675273002L;

    private int type;

    private String icon;

    private String name;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	

}
