package com.e_commerce.miscroservice.product.controller.store.shop.product;

import com.e_commerce.miscroservice.commons.entity.product.ShopProductQuery;
import com.e_commerce.miscroservice.commons.enums.product.ShopProductUpdTypeEnum;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.product.service.ShopProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/23 15:52
 * @Copyright 玖远网络
 */

@RestController
@RequestMapping( "/product/store/shopProduct" )
public class ShopProductController{


    @Autowired
    private ShopProductService shopProductService;

    /**

     * 商品管理列表
     *
     * @param soldOut         0：草稿(当前接口暂时没用到)，1：上架， 2：下架
     * @param owns            字符串","逗号拼接 '0'平台供应商商品/非自营, '1'用户自定义款，'2'用户自营平台同款,'1,2'自营
     * @param selfType        0非自营,1自营
     * @param isTop           0非推荐,1推荐(查询全部则不传)
     * @param orderType       排序规则 1浏览降序,2收藏降序
     * @param createTimeCeil  创建时间高值
     * @param createTimeFloor 创建时间低值
     * @param pageSize        分页
     * @param pageNumber      分页
     * @param keywords        搜索关键字
     * @return  "data": {
     *         "dataList": {
     *             "pageNumber": 1,
     *             "pageSize": 100,
     *             "total": 567,
     *             "pages": 6,
     *             "list": [
     *                 {
     *                     "productId": 0, /供应商商品id
     *                     "own": 1,//0'平台供应商商品/非自营, '1'用户自定义款，'2'用户自营平台同款,
     *                     "summaryImage": "",//橱窗图
     *                     "wantMemberCount": 0,//收藏数量
     *                     "tabType": 1,//0：店主精选，1：买手推荐
     *                     "soldOut": 1,//上架状态 0：草稿，1：上架， 2：下架
     *                     "isTop": 1,//是否推荐 1推荐,0非推荐
     *                     "inventory": 0,//库存
     *                     "showCount": 1,//浏览数量
     *                     "xprice": "-",//进货价(只有供应商同款才有!!)
     *                     "price": 25,//零售价
     *                     "name": "到",//名字
     *                     "id": 755 //商品id
     *                 }
     *             ]
     *         }
     *     }
     *
     * @author Charlie
     * @date 2018/11/26 20:03
     */
    @RequestMapping( "list/auth" )
    public Response list(
            @RequestParam( value = "soldOut", required = false ) Integer soldOut,
            @RequestParam( value = "owns", required = false ) Integer[] owns,
            @RequestParam( value = "isTop", required = false ) Integer isTop,
            @RequestParam( value = "selfType", required = false ) Integer selfType,
            @RequestParam( value = "orderType", required = false ) Integer orderType,
            @RequestParam( value = "createTimeCeil", required = false ) String createTimeCeil,
            @RequestParam( value = "createTimeFloor", required = false ) String createTimeFloor,
            @RequestParam( value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam( value = "pageNumber" , defaultValue = "1") Integer pageNumber,
            @RequestParam( value = "keywords" , required = false) String keywords
    ) {
        if (owns == null || owns.length == 0) {
            if (selfType == null) {
                //ignore
            }
            else if (selfType == 0) {
                owns = new Integer[]{0};
            }
            else if (selfType == 1) {
                owns = new Integer[]{1, 2};
            }
            else {
                //ignore
            }
        }

        Integer[] finalOwns = owns;
        return ResponseHelper.shouldLogin (IdUtil.getId ())
                .invokeHasReturnVal (user->{
                    ShopProductQuery query = new ShopProductQuery ();
                    query.setSoldOut (soldOut);
                    query.setOwnsArr (finalOwns);
                    query.setIsTop (isTop);
                    query.setOrderType (orderType);
                    query.setCreateTimeCeilStr (createTimeCeil);
                    query.setCreateTimeFloorStr (createTimeFloor);
                    query.setPageSize (pageSize);
                    query.setPageNumber (pageNumber);
                    query.setKeywords (keywords);
                    query.setStoreId (user);
                    return shopProductService.manageList (query);
                }).returnResponse ();
    }


    /**
     * 批量更新 (post请求)
     * <p>
     * 对商品的更新,编辑的统一接口
     * </p>
     *
         * @param updateType 批量推荐 1
     *                      {
     *                         'updateType'=1,
     *                         'ids'='1,2,3' //批量更新的商品id,以","逗号拼接
     *                         }
     *                   批量取消推荐 2
     *                      {
     *                         'updateType'=2,
     *                         'ids'='1,2,3' //批量更新的商品id,以","逗号拼接
     *                         }
     *                   批量上架 3
     *                      {
     *                         'updateType'=3,
     *                         'ids'='1,2,3' //批量更新的商品id,以","逗号拼接
     *                         }
     *                   批量下架 4
     *                      {
     *                         'updateType'=4,
     *                         'ids'='1,2,3' //批量更新的商品id,以","逗号拼接
     *                         }
     *                   批量删除 5
     *                      {
     *                         'updateType'=5,
     *                         'ids'='1,2,3' //批量更新的商品id,以","逗号拼接
     *                         }
     *                   更新sku库存 10
     *                      {
     *                         'updateType'=10,
     *                         'id'=123, //商品id
     *                         'skuJsonArray'='[{remainCount":3,"colorId":1000,"sizeId":2006}]"' //需要更新的sku信息
     *                                        //remainCount:剩余库存, status:-1下架，0正常(), colorId:颜色id, colorName:颜色, sizeId:尺码id, sizeName:尺码 (所有参数必填!!!)
     *                         }
     *                    删除sku 11
     *                       {
     *                         'updateType'=11,
     *                         'colorId'=123, //颜色id
     *                         'sizeId'=123, //尺码id
     *                         'id'=123, //商品id
     *                       }
     * @param ids        ids 操作的商品id,字符型,以","逗号拼接
     * @param id        商品id
     * @param skuJsonArray    sku更新后的字符串
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/26 20:42
     */
    @RequestMapping( "update/auth" )
    public Response update(
            Integer updateType,
            @RequestParam(value = "ids", required = false) Long[] ids,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "colorId", required = false) Long colorId,
            @RequestParam(value = "sizeId", required = false) Long sizeId,
            @RequestParam(value = "skuJsonArray", required = false) String skuJsonArray
    ) {


        ShopProductQuery shopProductQuery = new ShopProductQuery ();
        shopProductQuery.setIdsArray (ids);
        shopProductQuery.setId (id);
        shopProductQuery.setSkuJsonArray (skuJsonArray);

        shopProductQuery.setColorId (colorId);
        shopProductQuery.setSizeId (sizeId);
        return ResponseHelper.shouldLogin (IdUtil.getId ())
                .invokeHasReturnVal (userId -> {
                    shopProductQuery.setStoreId (userId);
                    return shopProductService.update (ShopProductUpdTypeEnum.create (updateType), shopProductQuery);
                }).returnResponse ();
    }


    /**
     * 商品sku列表
     *
     * @param shopProductId shopProductId
     * @return "data": {
     *         "isCanEditSku": true,  //该商品sku是否可编辑
     *         "shopProductId": 769, //商品id
     *         "dataList": [ //sku列表
     *             {
     *                 "remainCount": 22, //库存
     *                 "colorId": 1002, //颜色id
     *                 "sizeId": 2003, //尺码id
     *                 "colorName": "白色",
     *                 "sizeName": "M"
     *             }
     *         ]
     *     }
     * @author Charlie
     * @date 2018/11/28 19:14
     */
    @RequestMapping( "listSkuByShopProductId/auth" )
    public Response listSkuByShopProductId(@RequestParam("shopProductId") Long shopProductId){
        return ResponseHelper.shouldLogin (IdUtil.getId ())
                .invokeHasReturnVal (userId -> shopProductService.listSkuByShopProductId (userId, shopProductId)).returnResponse ();
    }




}
