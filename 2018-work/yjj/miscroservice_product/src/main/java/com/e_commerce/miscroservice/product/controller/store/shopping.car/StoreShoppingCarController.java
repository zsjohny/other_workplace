package com.e_commerce.miscroservice.product.controller.store.shopping.car;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.product.service.StoreShoppingCartService;
import com.e_commerce.miscroservice.product.vo.StoreShoppingCartQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/30 17:09
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "/product/store/shoppingCar" )
public class StoreShoppingCarController{

    private Log logger = Log.getInstance (StoreShoppingCarController.class);


    @Autowired
    private StoreShoppingCartService storeShoppingCartService;


    /**
     * 我的购物车
     *
     * @param pageSize   pageSize 分页
     * @param pageNumber pageNumber 分页
     * @return "data": {
     * "dataList": [ //购物车列表
     * {
     *     "brandName": "大C", //品牌名称
     *     "brandId": 2938, //品牌id
     *     "skuIdsOfBrand": [41588, 41589], //该品牌下购物车中的sku id
     *     "productList": [ //商品列表
     *           {
     *           "buyCount": 5, //购买数量
     *           "productId": 7844, //商品id
     *           "skuIdsOfProduct": [41588,41589], //该商品下的购物车中sku id
     *           "memberLevel": 0, //商品的会员等级
     *           "BuyCount": 5, //购买数量
     *           "clothesNumber": "大CDC015", //商品款号
     *           "productState": 6, //商品状态 6:上架,其他失效
     *           "productName": "时尚纯色TB风衣DC015（2-3天发货，介意慎拍）\t", //商品名称
     *           "minLadderPrice": 198, //批发最低价
     *           "member_ladder_price_min": 0, //会员批发最低价
     *           "ladderPriceJson": "[{\"count\":1,\"price\":198}]" //普通用户批发阶梯价格jsob
     *           "memberLadderPriceJson": "[]", //会员批发阶梯价格json
     *           "productMainImg": "https://yjj-img-main.oss-cn-/2345642.jpg",// 橱窗图
     *           "isSelected": 0, //购物车中是否被选中:0未选中,1选中
     *           "skusOfProduct": [// 该商品下的sku对象集合
     *                {
     *                "colorName": "杏色", //颜色
     *                "buyCount": 7, //购买数量(购物车中的数量)
     *                "sizeName": "S", //尺码
     *                "productId": 7844,
     *                "cartId": 7923,//购物车id
     *                "brandId": 2938,
     *                "remainCount": 16, //sku的库存
     *                "skuId": 41589 //sku id
     *                }
     *           ]
     *           }
     *     ]
     *     }
     * ]
     * }
     * @author Charlie
     * @date 2018/11/30 17:15
     */
    @RequestMapping( "listMyCar/auth" )
    public Response listMyCar(@RequestParam( value = "pageSize", defaultValue = "10" ) Integer pageSize,
                              @RequestParam( value = "pageNumber", defaultValue = "1" ) Integer pageNumber
    ) {
        return ResponseHelper.shouldLogin (IdUtil.getId ())
                .invokeHasReturnVal (userId -> {
                    StoreShoppingCartQuery query = new StoreShoppingCartQuery ();
                    query.setStoreId (userId);
                    query.setPageNumber (pageNumber);
                    query.setPageSize (pageSize);
                    return storeShoppingCartService.listMyCart (query);
                })
                .returnResponse ();
    }



    /**
     * 删除所有失效的购物车
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/3 15:44
     */
    @RequestMapping( "deleteAllDisableCarts/auth" )
    public Response deleteAllDisableCarts() {
        return ResponseHelper.shouldLogin (IdUtil.getId ())
                .invokeNoReturnVal (userId -> {
                        StoreShoppingCartQuery query = new StoreShoppingCartQuery ();
                        query.setStoreId (userId);
                        storeShoppingCartService.deleteCarts (query);
                }).returnResponse ();
    }



    /**
     * 根据购物车id删除购物车
     *
     * @param cartIds cartIds='1,2,3,5' id的字符串拼接
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/3 15:44
     */
    @RequestMapping( "deleteCartsByIds/auth" )
    public Response deleteCartsByIds(@RequestParam("cartIds") List<Long> cartIds) {
        logger.info ("删除购物车 cartIds={}", cartIds);
        return ResponseHelper.shouldLogin (IdUtil.getId ())
                .invokeNoReturnVal (userId -> {
                    if (! cartIds.isEmpty ()) {
                        StoreShoppingCartQuery query = new StoreShoppingCartQuery ();
                        query.setStoreId (userId);
                        query.setIds (cartIds);
                        storeShoppingCartService.deleteCarts (query);
                    }
                }).returnResponse ();
    }




    @RequestMapping("update/buyCount/auth")
    public Response updateBuyCount(String cartsJsonArray){
        return ResponseHelper.shouldLogin (IdUtil.getId ())
                .invokeNoReturnVal (storeId-> storeShoppingCartService.updateBuyCount (storeId, cartsJsonArray)).returnResponse ();

    }


    /**
     * 购物车商品款号数量
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/2/16 16:22
     */
    @RequestMapping( "clothesNumberCount" )
    public Response clothesNumberCount() {
        return ResponseHelper.shouldLogin (IdUtil.getId ())
                .invokeHasReturnVal (userId -> storeShoppingCartService.clothesNumberCount(userId))
                .returnResponse ();
    }
}
