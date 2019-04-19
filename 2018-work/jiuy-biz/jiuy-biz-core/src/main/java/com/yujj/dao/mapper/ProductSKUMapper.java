package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.product.ProductSKUsPropVO;
import com.yujj.entity.product.ProductSKU;

@DBMaster
public interface ProductSKUMapper {

    List<ProductSKU> getProductSKUsOfProduct(long productId);
    
    List<ProductSKU> getAllStatusOfProductSKUs(long productId);

    ProductSKU getProductSKU(long id);

    List<ProductSKU> getProductSKUs(@Param("ids") Collection<Long> ids);

    int updateRemainCount(@Param("id") long id, @Param("by") int by);
    
    int updateRemainCountSecond(@Param("id") long id, @Param("by") int by);
    
    List<ProductSKUsPropVO> getProductSKUsPropVO();
}
