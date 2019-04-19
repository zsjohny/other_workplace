package com.jiuyuan.entity.tag;

import java.util.ArrayList;
import java.util.List;

import com.jiuyuan.entity.product.Tag;

public class TagVO extends Tag {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6956842675833796193L;
	
	private Integer productCount;
	
	private Integer childTagCount;
	
	private List<Tag> childTags = new ArrayList<Tag>();; 

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public List<Tag> getChildTags() {
		return childTags;
	}

	public void setChildTags(List<Tag> childTags) {
		this.childTags = childTags;
	}

	public Integer getChildTagCount() {
		return childTagCount;
	}

	public void setChildTagCount(Integer childTagCount) {
		this.childTagCount = childTagCount;
	}
	
}
