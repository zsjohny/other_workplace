package com.jiuyuan.entity.comment;

import java.util.List;

import com.yujj.business.assembler.composite.ProductComposite;
import com.yujj.entity.comment.Comment;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductSKU;

public class CommentVO extends Comment implements ProductComposite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8269003014109563278L;
	
	private Product product;

    private ProductSKU sku;
    
    private List<ProductPropVO> skuProps;
        
	@Override
	public void assemble(Product product) {
		this.setProduct(product);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductPropVO> getSkuProps() {
		return skuProps;
	}

	public void setSkuProps(List<ProductPropVO> skuProps) {
		this.skuProps = skuProps;
	}

	public ProductSKU getSku() {
		return sku;
	}

	public void setSku(ProductSKU sku) {
		this.sku = sku;
	}
}
