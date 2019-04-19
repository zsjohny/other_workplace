package com.jiuy.core.meta.product;

import java.io.Serializable;
import java.util.List;

import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.product.ProductSKUVO;
import com.store.entity.ProductCategoryVO;
import com.store.entity.ProductPropVO;

public class ProductVO implements Serializable {

    private static final long serialVersionUID = -7940517601774707460L;

    private Product product;
    
    private List<ProductPropVO> baseProperties;

    private List<ProductSKUVO> productSKUs;
    
    private List<ProductCategoryVO> productCategories;

    private String warehouseName;
    private String warehouseName2;
    
    private int visitCount;
    
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ProductPropVO> getBaseProperties() {
        return baseProperties;
    }

    public void setBaseProperties(List<ProductPropVO> baseProperties) {
        this.baseProperties = baseProperties;
    }

    public List<ProductSKUVO> getProductSKUs() {
        return productSKUs;
    }

    public void setProductSKUs(List<ProductSKUVO> productSKUs) {
        this.productSKUs = productSKUs;
    }

    public List<ProductCategoryVO> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategoryVO> productCategories) {
        this.productCategories = productCategories;
    }

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public String getWarehouseName2() {
		return warehouseName2;
	}

	public void setWarehouseName2(String warehouseName2) {
		this.warehouseName2 = warehouseName2;
	}
    
}
