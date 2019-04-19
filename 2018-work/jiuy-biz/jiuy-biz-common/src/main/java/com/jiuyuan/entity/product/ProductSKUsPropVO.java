package com.jiuyuan.entity.product;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.yujj.entity.product.ProductPropValue;

public class ProductSKUsPropVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7044722821354570467L;

	private long productId;
	private String propertyIds;

	public String getPropertyIds() {
		return propertyIds;
	}

	public void setPropertyIds(String propertyIds) {
		this.propertyIds = propertyIds;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public Map<String, String> getPropValues(Map<Long, ProductPropValue> map) {
		String propColor = "";
		String propSize = "";
		
		Map<String, String> propsMap = new HashMap<String, String>();
		
		String[] propPairsArray = StringUtils.split(getPropertyIds(), " ");
		for (String propPairss : propPairsArray) {

			String[] propPairs = StringUtils.split(propPairss, ";");
			for (String propPair : propPairs) {
				String[] parts = StringUtils.split(propPair, ":");
				
				if (StringUtils.equals("7", parts[0])){
					propColor += " " + map.get(Long.parseLong(parts[1]));
				} else if (StringUtils.equals("8", parts[0])){
					propSize += " " + map.get(Long.parseLong(parts[1]));
				}
			}
		}

		propsMap.put("propColors", propColor);
		propsMap.put("propSizes", propSize);
			
		return propsMap;
	}
}
