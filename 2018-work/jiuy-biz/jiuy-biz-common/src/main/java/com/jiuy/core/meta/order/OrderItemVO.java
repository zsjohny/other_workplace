package com.jiuy.core.meta.order;

import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;

public class OrderItemVO extends OrderItem {

    private static final long serialVersionUID = 4213597407531077439L;

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
