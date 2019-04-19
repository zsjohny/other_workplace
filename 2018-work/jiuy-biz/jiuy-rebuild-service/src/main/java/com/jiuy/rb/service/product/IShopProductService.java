package com.jiuy.rb.service.product;


import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.base.model.MyPage;
import com.jiuy.rb.enums.ShopActivityKindEnum;
import com.jiuy.rb.model.product.*;

import java.util.List;
import java.util.Map;

/**
 * 小程序商品
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 17:50
 * @Copyright 玖远网络
 */
public interface IShopProductService {

    /**
     * 通过id获取某个小程序商品
     *
     * @param productId productId
     * @author Aison
     * @date 2018/7/5 17:52
     * @return com.jiuy.rb.model.product.ShopProductRb
     */
    ShopProductRb getById(Long productId);

    /**
     *描述 通过ids获取小程序商品列表
     ** @param ids  productId
     * @author hyq
     * @date 2018/8/14 9:07
     * @return java.util.List<com.jiuy.rb.model.product.ShopProductRb>
     */
    List<ShopProductRb> selectByIds(List<String> ids);

    /**
     *描述 通过ids获取小程序商品列表
     ** @param query  query
     * @author hyq
     * @date 2018/8/14 9:07
     * @return java.util.List<com.jiuy.rb.model.product.ShopProductRb>
     */
    List<ShopProductRb> selectByIds(ShopProductRbQuery query);

    /**
     * 查询小程序商品列表
     *
     * @param query query
     * @author Aison
     * @date 2018/7/6 13:58
     * @return com.jiuy.base.model.MyPage<com.jiuy.rb.model.product.ShopProductRb>
     */
    MyPage<ShopProductRb> getShopProductList(ShopProductRbQuery query);



    /**
     * 分享
     *
     * @param page page
     * @param page secondBuyShopProductId 当前秒杀活动商品id Nullable
     * @param page teamBuyShopProductId 当前团购活动商品id Nullable
     * @author Aison
     * @date 2018/7/6 15:12
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
     Page<Map<String,Object>> getShareProduct(Page<Map<String,Object>> page);



    /**
     * 获取排序后的团购活动列表
     *
     * @param query query
     * @author Charlie
     * @date 2018/7/29 19:49
     */
    MyPage<TeamBuyActivityRbQuery> listTeamBuyActivityWithOrder(TeamBuyActivityRbQuery query);


    /**
     * 获取排序后的秒杀活动列表
     *
     * @param query query
     * @author Charlie
     * @date 2018/7/29 19:49
     */
    MyPage<SecondBuyActivityRbQuery> listSecondBuyActivityWithOrder(SecondBuyActivityRbQuery query);


    /**
     * 活动库存回滚
     * <p>
     *     取消订单
     * </p>
     *
     * @param activityId 活动id
     * @param kind 活动种类 {@link ShopActivityKindEnum}
     * @param count 释放库存数量
     * @author Charlie
     * @date 2018/8/3 18:02
     */
    void releaseInventory(Long activityId, ShopActivityKindEnum kind, Integer count);




    /**
     * 将活动库存放入缓存
     *
     * @param storeId 门店id
     * @param activityId 活动id
     * @param kind {@link ShopActivityKindEnum}
     * @author Charlie
     * @date 2018/8/3 19:40
     */
    void putActivityInCacheIfNoExist(Long storeId, Long activityId, ShopActivityKindEnum kind);


    /**
     * 查询
     *
     * @param teamQuery teamQuery
     * @return com.jiuy.rb.model.product.TeamBuyActivityRb
     * @author Charlie
     * @date 2018/8/6 14:08
     */
    TeamBuyActivityRb selectOne(TeamBuyActivityRbQuery teamQuery);


    /**
     * 查询
     *
     * @param secondQuery secondQuery
     * @return com.jiuy.rb.model.product.SecondBuyActivityRb
     * @author Charlie
     * @date 2018/8/6 14:08
     */
    SecondBuyActivityRb selectOne(SecondBuyActivityRbQuery secondQuery);


    /**
     * 首页活动
     *
     * @param wxVersion 小程序版本号
     * @param storeId 门店id
     * @return 团购和秒杀活动
     * @author Charlie
     * @date 2018/8/7 10:23
     */
    Map<String,Object> homeActivity(String wxVersion, Long storeId);

    /**
     * 创建一个商品
     *
     * @param param param
     * @return com.jiuy.rb.model.product.ShopProductRb
     * @author Charlie
     * @date 2018/9/3 17:56
     */
    ShopProductRb createShopProduct(ShopProductRbQuery param);

    /**
     * 编辑
     *
     * @param param param
     * @author Charlie
     * @date 2018/9/4 14:59
     */
    void editShopProduct(ShopProductRbQuery param);
    /**
     * 查询商品详情
     *
     * @param shopProductId shopProductId
     * @param storeId storeId
     * @return com.jiuy.rb.model.product.ShopProductRbQuery
     * @author Charlie
     * @date 2018/9/8 22:19
     */
    ShopProductRbQuery queryProductDetail(Long shopProductId, Long storeId);
    /**
     * 查询供应商商品信息,已小程序商品信息的格式返回
     *
     * @param productId productId
     * @return ShopProductRbQuery
     * @author Charlie
     * @date 2018/9/12 15:33
     */
    ShopProductRbQuery supplierProduct2ShopProduct(Long productId);

    List<ShopProductRb> selectList(ShopProductRbQuery shopProductRbQuery);
}
