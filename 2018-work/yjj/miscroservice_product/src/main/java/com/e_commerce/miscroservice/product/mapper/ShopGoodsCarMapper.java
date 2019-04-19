package com.e_commerce.miscroservice.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/21 9:44
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopGoodsCarMapper {

    /**
     * 获取某个用户购物车里面所有的商品
     * @param memberId
     * @date:   2018/4/20 14:20
     * @author: Aison
     */
    List<Map<String,Object>> shopGoodsCarList(@Param("memberId") Long memberId, @Param("statusList") List<Integer> statusList);
}
