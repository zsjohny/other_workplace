package com.jiuyuan.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.entity.product.RestrictionCombination;

public class RestrictionCombinationVO extends RestrictionCombination implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5783357966749045758L;
	
	private int productCount;

    private String productsToString;

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

    public String getProductsToString() {
    	if(StringUtils.equals(null, productsToString)) {
    		return "";
    	}
    	
        return productsToString ;
    }

    public void setProductsToString(String productsToString) {
        this.productsToString = productsToString;
    }

    public int getTotalCount() {
        if (StringUtils.equals("", productsToString) || StringUtils.equals(null, productsToString)) {
            return 0;
        }
        return productsToString.split(",").length;
    }

}
