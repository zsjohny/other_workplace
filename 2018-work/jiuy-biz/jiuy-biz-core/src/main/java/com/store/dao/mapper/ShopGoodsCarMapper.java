package com.store.dao.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.order.ShopGoodsCar;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 小程序商城购物车 Mapper 接口
 * </p>
 *
 * @author Aison
 * @since 2018-04-19
 */
@DBMaster
public interface ShopGoodsCarMapper extends BaseMapper<ShopGoodsCar> {

    /**
     * 获取某个用户购物车里面所有的商品
     * @param memberId
     * @date:   2018/4/20 14:20
     * @author: Aison
     */
    List<Map<String,Object>> shopGoodsCarList(@Param("memberId") Long memberId,@Param("statusList") List<Integer> statusList);

    ShopGoodsCar selectShopGoodsCar(@Param("memberId") Long memberId,@Param("storeId") Long storeId,@Param("productId")Long productId, @Param("liveProductId") Long liveProductId);
    ShopGoodsCar selectShopGoodsCarNew(@Param( "memberId" ) Long memberId, @Param( "storeId" ) Long storeId, @Param( "productSkuId" ) Long productSkuId, @Param("liveProductId") Long liveProductId);

    ShopGoodsCar selectByMemberId(@Param("id") Long id,@Param("memberId") Long memberId);


    List<ShopGoodsCar> selectShopGoodsCarList(@Param("memberId") Long memberId,@Param("list")List<Long> list);

    List<ShopGoodsCar> selectListNew(@Param("memberId") Long memberId,@Param("list")List<Long> list);
    List<ShopGoodsCar> selectListT(@Param("memberId") Long memberId,@Param("list")List<Long> list);
}