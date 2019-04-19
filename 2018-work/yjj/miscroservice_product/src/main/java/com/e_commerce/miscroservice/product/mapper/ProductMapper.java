package com.e_commerce.miscroservice.product.mapper;

import com.e_commerce.miscroservice.commons.entity.application.order.Product;
import com.e_commerce.miscroservice.product.vo.LiveProductVO;
import com.e_commerce.miscroservice.product.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 16:18
 * @Copyright 玖远网络
 */
@Mapper
public interface ProductMapper {


    /**
     * 查询平台直播的商品列表
     *
     * @param vo vo
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/14 16:18
     */
    List<Map<String,Object>> listLiveSelectProducts(ProductVO vo);




    /**
     * 查询商品价格
     *
     * @param shopProductIds shopProductIds
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.order.Product>
     * @author Charlie
     * @date 2019/1/14 19:22
     */
    List<Product> listByIds4InitLiveProduct(@Param( "shopProductIds" ) List<Long> shopProductIds);


    /**
     * 查询商品的部分信息
     *
     * @param ids ids
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.order.Product>
     * @author Charlie
     * @date 2019/1/16 10:04
     */
    List<Product> findSimpleInfoByIds(@Param( "ids" ) List<Long> ids);

    /**
     * 查找商品图片
     *
     * @param id id
     * @return com.e_commerce.miscroservice.commons.entity.application.order.Product
     * @author Charlie
     * @date 2019/1/17 9:57
     */
    Product findImg(@Param( "id" ) Long id);
}
