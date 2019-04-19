package com.e_commerce.miscroservice.product.controller.rpc;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.product.entity.ShopProduct;
import com.e_commerce.miscroservice.commons.entity.product.ShopProductQuery;
import com.e_commerce.miscroservice.product.service.ShopProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/22 17:31
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "product/rpc/ShopProductRpcController" )
public class ShopProductRpcController{

    @Autowired
    private ShopProductService shopProductService;



    /**
     * 查询一个对象
     *
     * @param query query
     * @return com.e_commerce.miscroservice.product.entity.ShopProduct
     * @author Charlie
     * @date 2018/11/22 17:39
     */
    @RequestMapping( "selectOne" )
    public ShopProductQuery selectOne(@RequestBody ShopProductQuery query) {
        ShopProduct shopProduct = shopProductService.selectOne (query);
        ShopProductQuery response = new ShopProductQuery ();
        if (shopProduct != null) {
            response.setId (shopProduct.getId ());
            response.setStoreId (shopProduct.getStoreId ());
            response.setName (shopProduct.getName() );
            response.setClothesNumber (shopProduct.getClothesNumber() );
            response.setColorId (shopProduct.getColorId() );
            response.setColorName (shopProduct.getColorName() );
            response.setSizeId (shopProduct.getSizeId() );
            response.setSizeName (shopProduct.getSizeName() );
            response.setPrice (shopProduct.getPrice() );
            response.setStatus (shopProduct.getStatus() );
            response.setSoldOut (shopProduct.getSoldOut() );
            response.setOwn (shopProduct.getOwn() );
            response.setPrice (shopProduct.getPrice() );
            response.setProductId (shopProduct.getProductId() );
        }
        return response;
    }


}
