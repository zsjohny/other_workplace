package com.e_commerce.miscroservice.product.mapper;

import com.e_commerce.miscroservice.product.vo.ProductPropertyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/31 16:14
 */
@Mapper
public interface DynamicPropertyProductMapper {
    /**
     *查询商品动态属性
     *
     * @param productId productId
     * @return java.util.List<com.e_commerce.miscroservice.product.vo.ProductPropertyDTO>
     * @author Charlie
     * @date 2019/1/31 16:34
     */
    List<ProductPropertyDTO> listDynaPropAndValue(@Param( "productId" ) Long productId);
}
