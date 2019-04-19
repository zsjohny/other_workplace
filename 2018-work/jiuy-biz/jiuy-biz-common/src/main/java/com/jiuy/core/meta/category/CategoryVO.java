package com.jiuy.core.meta.category;

import java.util.List;

import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.shopping.DiscountInfo;

public class CategoryVO extends Category{
	
    public CategoryVO() {
    }

    public CategoryVO(long id, String categoryName, long parentId, int status, int categoryType, int weight,
                      String iconUrl,
			String iconOnUrl, String categoryUrl, String description) {
        super(id, categoryName, parentId, status, categoryType, weight, iconUrl, iconOnUrl, categoryUrl, description);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7509475998941861026L;

	private String virtualProducts;

	private String parentName;
	
	private List<DiscountInfo> discountInfos;
	
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getVirtualProducts() {
		return virtualProducts;
	}

	public void setVirtualProducts(String virtualProducts) {
		this.virtualProducts = virtualProducts;
	}

	public List<DiscountInfo> getDiscountInfo() {
		return discountInfos;
	}

	public void setDiscountInfo(List<DiscountInfo> discountInfos) {
		this.discountInfos = discountInfos;
	}
	
}
