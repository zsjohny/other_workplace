package com.jiuyuan.entity.storeorder;

import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午5:56:19
*/
public class StoreOrderItemVO extends StoreOrderItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1681531506528967663L;

	private Product product;
    
    private ProductSKU productSKU;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

	public ProductSKU getProductSKU() {
		return productSKU;
	}

	public void setProductSKU(ProductSKU productSKU) {
		this.productSKU = productSKU;
	}
}
