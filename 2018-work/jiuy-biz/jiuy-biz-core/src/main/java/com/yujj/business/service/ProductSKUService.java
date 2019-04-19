package com.yujj.business.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.product.ProductSKUsPropVO;
import com.yujj.dao.mapper.ProductSKUMapper;
import com.yujj.entity.product.ProductSKU;

@Service
public class ProductSKUService {

    @Autowired
    private ProductSKUMapper productSKUMapper;
        
    public List<ProductSKU> getProductSKUsOfProduct(long productId) {
        return productSKUMapper.getProductSKUsOfProduct(productId);
    }
    
    public List<ProductSKU> getAllStatusOfProductSKUs(long productId) {
    	return productSKUMapper.getAllStatusOfProductSKUs(productId);
    }

    public ProductSKU getProductSKU(long skuId) {
        return productSKUMapper.getProductSKU(skuId);
    }

    public List<ProductSKU> getProductSKUs(Collection<Long> skuIds) {
        return productSKUMapper.getProductSKUs(skuIds);
    }

    public int updateRemainCount(long skuId, int by) {
        return productSKUMapper.updateRemainCount(skuId, by);
    }
    
    public int updateRemainCountSecond(long skuId, int by) {
    	return productSKUMapper.updateRemainCountSecond(skuId, by);
    }
    
    
    public List<ProductSKUsPropVO> getProductSKUsPropVO() {
        return productSKUMapper.getProductSKUsPropVO();
    }
}
