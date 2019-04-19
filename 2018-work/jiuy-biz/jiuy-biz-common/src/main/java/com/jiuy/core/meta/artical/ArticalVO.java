package com.jiuy.core.meta.artical;

import java.io.Serializable;

public class ArticalVO extends Artical implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -906192474630361494L;

	
	private String categoryName;
	
	private String parentCategoryName;
	
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getParentCategoryName() {
		return parentCategoryName;
	}

	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}


}
