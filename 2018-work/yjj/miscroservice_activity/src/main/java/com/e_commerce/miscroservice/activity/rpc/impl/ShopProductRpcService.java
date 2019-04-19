package com.e_commerce.miscroservice.activity.rpc.impl;

import com.e_commerce.miscroservice.commons.entity.product.ShopProductQuery;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/22 17:43
 * @Copyright 玖远网络
 */

@FeignClient(value = "product", path = "product/rpc/ShopProductRpcController")
@Component
public interface ShopProductRpcService{

    /**
     * 查询一个对象
     *
     * @param query query
     * @return com.e_commerce.miscroservice.product.entity.ShopProduct
     * @author Charlie
     * @date 2018/11/22 17:39
     */
    @RequestMapping( "selectOne" )
    ShopProductQuery selectOne(@RequestBody ShopProductQuery query);

}
