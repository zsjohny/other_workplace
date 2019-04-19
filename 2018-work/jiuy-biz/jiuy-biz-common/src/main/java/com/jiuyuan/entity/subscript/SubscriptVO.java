package com.jiuyuan.entity.subscript;

import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.entity.product.Subscript;

public class SubscriptVO extends Subscript {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1147667548901500726L;
	/**
	 * 多个相关联的商品id字符串
	 */
	private String productIdsToString;

	public String getProductIdsToString() {
		if(StringUtils.equals(null, productIdsToString)){
			return "";
		}
		return productIdsToString;
	}

	public void setProductIdsToString(String productIdsToString) {
		this.productIdsToString = productIdsToString;
	}
	
	/**
	 * 返回productIdsToString的id个数
	 * @return
	 */
	public int getTotalCount() {
        if (StringUtils.equals("", productIdsToString) || StringUtils.equals(null, productIdsToString)) {
            return 0;
        }
        return productIdsToString.split(",").length;
    }
}
