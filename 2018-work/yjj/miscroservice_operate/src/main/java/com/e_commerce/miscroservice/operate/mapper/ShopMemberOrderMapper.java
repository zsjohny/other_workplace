package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/8 19:16
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopMemberOrderMapper{

    /**
     * 查询一个
     *
     * @param query
     * @return com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderQuery
     */
    ShopMemberOrderQuery selectOne(ShopMemberOrderQuery query);

    /**
     * 通过主键查
     * @param id 主键
     */
    ShopMemberOrderQuery selectByPrimaryKey(Long id);

    /**
     * 小程序订单查询
     *
     * @param query query
     * @author Charlie
     * @date 2018/11/8 19:12
     */
    List<Map<String,Object>> listOrder(ShopMemberOrderQuery query);
}
