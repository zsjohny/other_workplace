package com.jiuy.rb.mapper.order;

import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.order.ShopMemberOrderRb;
import com.jiuy.rb.model.order.ShopMemberOrderRbQuery;
import com.jiuyuan.entity.storeorder.StoreOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 
 * 会员订单表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年07月17日 上午 11:25:54
 * @Copyright 玖远网络 
 */
public interface ShopMemberOrderRbMapper extends BaseMapper<ShopMemberOrderRb>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     *
     * 接口描述:
     *
     * @param:
     * @return:
     * @auther: hyq
     * @date: 2018/7/27 10:05
     */
    List<Map<String,Object>> selectMemberOrderList(ShopMemberOrderRb query);


    /**
     * 用户即将成团的订单
     *
     * @param query
     *          需要参数 : storeId
     *          需要参数 : memberId
     *          需要参数 : current 当前时间
     * @return java.util.List
     * @author Charlie
     * @date 2018/7/29 22:43
     */
    List<Map<String,Object>> teamBuyActivityUnderwayList(ShopMemberOrderRbQuery query);

    /**
     * 用户已经成团的订单
     *
     * @param query
     *          需要参数 : storeId
     *          需要参数 : memberId
     * @return java.util.List
     * @author Charlie
     * @date 2018/7/29 22:43
     */
    List<Map<String,Object>> teamBuyActivityOKList(ShopMemberOrderRbQuery query);


    /**
     * 即将成团 数量
     *
     * @param query query
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/8/10 8:36
     */
    Integer teamBuyActivityUnderwayCount(ShopMemberOrderRbQuery query);


    /**
     * 根据小程序订单号查询
     * @param shopMemberOrderId
     * @return
     */
    StoreOrder findStoreOrderByMemberOrderId(@Param("shopMemberOrderId") long shopMemberOrderId);


    /**
     * 根据小程序订单号 确认订单
     * @param shopMemberOrderId
     */
    void upStoreByShopMemberOrderId(@Param("shopMemberOrderId") long shopMemberOrderId);
}