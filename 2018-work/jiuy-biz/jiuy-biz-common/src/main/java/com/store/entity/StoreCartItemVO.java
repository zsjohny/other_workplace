package com.store.entity;

import java.util.List;

import com.jiuyuan.entity.ProductSKU;




public class StoreCartItemVO extends StoreCartItem {

    private static final long serialVersionUID = 7600784579085518272L;
    
    private ProductVOShop product;

    private ProductSKU sku;
    
    private List<ProductPropVO> skuProps;
    
    private ShopCategory category;

    public ProductVOShop getProduct() {
        return product;
    }

    public void setProduct(ProductVOShop product) {
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

	public ShopCategory getCategory() {
		return category;
	}

	public void setCategory(ShopCategory category) {
		this.category = category;
	}

}
