/**
 * 
 */
package com.yujj.entity.product;


/**
* @author DongZhong
* @version 创建时间: 2016年9月23日 下午9:55:00
*/
public class ProductCategoryVO extends ProductCategory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7227164166438491891L;

	private String categoryNames;
	private int maxWeight;

	public String getCategoryNames() {
		return categoryNames;
	}

	public void setCategoryNames(String categoryNames) {
		this.categoryNames = categoryNames;
	}

	public int getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}
}
