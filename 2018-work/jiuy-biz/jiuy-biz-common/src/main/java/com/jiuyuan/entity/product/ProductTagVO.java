package com.jiuyuan.entity.product;

import java.io.Serializable;

import com.jiuyuan.entity.product.ProductTag;

public class ProductTagVO extends ProductTag implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2979851924780136457L;

	private String names;

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}
	
}
