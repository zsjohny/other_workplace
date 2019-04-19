package com.store.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ShopPayTypeVO implements Serializable {

    private static final long serialVersionUID = -8352649344675273002L;

    private int type;

    private String icon;

    private String name;
    
    private String account;
    
    private String tips;
    
    private int selected;
    
    private String userName;

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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}
    
	
	

}
