package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.order.Product;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.product.dao.ProductDao;
import com.e_commerce.miscroservice.product.entity.ProductSku;
import com.e_commerce.miscroservice.product.mapper.ProductMapper;
import com.e_commerce.miscroservice.product.mapper.ProductSkuMapper;
import com.e_commerce.miscroservice.product.vo.ProductVO;
import com.e_commerce.miscroservice.product.vo.SkuOfProductDTO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 16:11
 * @Copyright 玖远网络
 */
@Component
public class ProductDaoImpl implements ProductDao {


    private Log logger = Log.getInstance(ProductDaoImpl.class);
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;


    /**
     * 查询平台直播的商品列表
     *
     * @param vo vo
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/14 16:18
     */
    @Override
    public List<Map<String, Object>> listPlatformLiveSelectProducts(ProductVO vo) {
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        //sql APP商品查询的是DetailImages
        List<Map<String, Object>> res = productMapper.listLiveSelectProducts(vo);

        //查询库存
        if (! res.isEmpty()) {
            List<Long> productIds = res.stream()
                    .mapToLong(value -> (long) value.get("id"))
                    .boxed()
                    .collect(Collectors.toList());

            //查询sku sql
            Map<Long, SkuOfProductDTO> skuOfProductDTOMap = listSkuBySupplierProductIds(productIds);
            //填充库存
            res.stream().forEach(product->{
                Long id = (Long) product.get("id");
                SkuOfProductDTO skuOfProduct = skuOfProductDTOMap.get(id);
                int inventory = skuOfProduct == null ? 0: skuOfProduct.getInventory();
                product.put("inventory", inventory);
            });
        }
        return res;
    }




    /**
     * 查询供应商平台商品的sku信息
     *
     * @param productIds 供应商商品id
     * @return key 供应商商品id, value sku信息
     * @author Charlie
     * @date 2019/1/15 14:43
     */
    @Override
    public Map<Long, SkuOfProductDTO> listSkuBySupplierProductIds(List<Long> productIds) {
        if (ObjectUtils.isEmpty(productIds)) {
            return Collections.emptyMap();
        }
        //查询sku
        List<ProductSku> productSkuList = productSkuMapper.listSkuBySupplierProductIds(productIds, System.currentTimeMillis());
        if (productSkuList.isEmpty()) {
            logger.warn("没有找到sku信息");
            return Collections.emptyMap();
        }

        return SkuOfProductDTO.groupByProductId(productSkuList, true);
    }



    /**
     * 查询商品价格
     *
     * @param shopProductIds shopProductIds
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.order.Product>
     * @author Charlie
     * @date 2019/1/14 19:22
     */
    @Override
    public List<Product> listByIds4InitLiveProduct(List<Long> shopProductIds) {
        return productMapper.listByIds4InitLiveProduct(shopProductIds);
    }


    /**
     * 查询商品的部分信息
     *
     * @param ids ids
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.order.Product>
     * @author Charlie
     * @date 2019/1/16 10:04
     */
    @Override
    public List<Product> findSimpleInfoByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return productMapper.findSimpleInfoByIds(ids);
    }





    /**
     * 查找商品图片
     *
     * @param id id
     * @return com.e_commerce.miscroservice.commons.entity.application.order.Product
     * @author Charlie
     * @date 2019/1/17 9:57
     */
    @Override
    public Product findImg(Long id) {
        return productMapper.findImg(id);
    }
}
