package com.jiuyuan.entity.shopping;

import java.util.List;

import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductSKU;

public class CartItemVO extends CartItem {

    private static final long serialVersionUID = 7600784579085518272L;
    
    private Product product;

    private ProductSKU sku;
    
    private List<ProductPropVO> skuProps;
    
    private Category category;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductSKU getSku() {
        return sku;
    }

    public void setSku(ProductSKU sku) {
        this.sku = sku;
    }

    public List<ProductPropVO> getSkuProps() {
        return skuProps;
    }

    public void setSkuProps(List<ProductPropVO> skuProps) {
        this.skuProps = skuProps;
    }

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
