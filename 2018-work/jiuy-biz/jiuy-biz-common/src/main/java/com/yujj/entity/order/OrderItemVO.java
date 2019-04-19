package com.yujj.entity.order;

import com.yujj.business.assembler.composite.ProductComposite;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductSKU;

public class OrderItemVO extends OrderItem implements ProductComposite {

    private static final long serialVersionUID = 4213597407531077439L;

    private Product product;
    

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

	@Override
    public void assemble(Product product) {
        setProduct(product);
    }

}
