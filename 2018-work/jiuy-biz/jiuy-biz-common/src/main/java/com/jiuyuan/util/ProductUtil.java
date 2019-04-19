/**
 * 
 */
package com.jiuyuan.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.store.entity.ProductVOShop;
import com.yujj.entity.product.ProductVO;

/**
* @author DongZhong
* @version 创建时间: 2016年12月14日 下午2:31:01
*/
public class ProductUtil {

	public static List<Map<String, Object>> getProductSimpleList(List<ProductVO> products) {
		if (products == null || products.size() == 0) return new ArrayList<Map<String, Object>>();
		
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        for (ProductVO product : products) {
            productList.add(product.toSimpleMap15());
        }
        return productList;
    }
	
	public static List<Map<String, Object>> getLimitNumProductSimpleList(List<ProductVO> products, int num) {
		if (products == null || products.size() == 0) return new ArrayList<Map<String, Object>>();
		
		List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (ProductVO product : products) {
			if(i < num){
				productList.add(product.toSimpleMap15());
				i++;
			}
		}
		return productList;
	}
	
	public static List<Map<String, Object>> getLimitNumProductSimpleListNEW(List<ProductVOShop> products, int num) {
		if (products == null || products.size() == 0) return new ArrayList<Map<String, Object>>();
		
		List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (ProductVOShop product : products) {
			if(i < num){
				productList.add(product.toSimpleMap15());
				i++;
			}
		}
		return productList;
	}
    
}
