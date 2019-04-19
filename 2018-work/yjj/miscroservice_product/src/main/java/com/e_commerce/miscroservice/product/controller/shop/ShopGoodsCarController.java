package com.e_commerce.miscroservice.product.controller.shop;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.product.service.ShopGoodsCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/21 9:34
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "product/shop/shopGoodsCar" )
public class ShopGoodsCarController {

    @Autowired
    private ShopGoodsCarService listShopCar;

    /**
     * 购物车列表
     *
     * @return 参考老系统的购物车列表返回字段
     * @author Charlie
     * @date 2019/1/21 14:01
     */
    @RequestMapping( "listShopCar" )
    public Response listShopCar() {
        return ResponseHelper.shouldLogin()
                .invokeHasReturnVal(userId -> listShopCar.listShopCar(userId)).returnResponse();
    }


}
