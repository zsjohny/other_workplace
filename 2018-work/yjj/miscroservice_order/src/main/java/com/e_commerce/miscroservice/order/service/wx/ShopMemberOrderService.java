package com.e_commerce.miscroservice.order.service.wx;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.TeamOrder;
import com.e_commerce.miscroservice.commons.entity.order.CountTeamOrderMoneyCoinVo;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import com.e_commerce.miscroservice.commons.entity.order.OrderItemSkuVo;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.order.entity.RequestAddress;
import com.e_commerce.miscroservice.order.entity.ShopMemberOrderItem;
import com.e_commerce.miscroservice.order.vo.StoreBizOrderQuery;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/11 17:12
 * @Copyright 玖远网络
 */
public interface ShopMemberOrderService{


    /**
     * 根据订单号更新订单信息(订单号必填)
     *
     * @param shopMemberOrder shopMemberOrder
     * @return int
     * @author Charlie
     * @date 2018/10/11 17:43
     */
    int updateByOrderNoSelective(ShopMemberOrder shopMemberOrder);

    /**
     * 当月 粉丝 团队 自购总金额
     * @param userId
     * @param type
     * @return
     */
    Double theMoneyTotalMoney(Long userId, Integer type);

    /**
     * 查找 订单列表
     * @param userId
     * @param page
     * @return
     */
    List<ShopMemberOrder> findOrderList(Long userId, Integer page);

    /**
     * 查找 团队订单列表
     * @param userId
     * @param page
     * @param orderNo
     * @return
     */
    List<TeamOrder> findTeamOrderList(Long userId, Integer page, String orderNo);

    /**
     * 查找 团队今日新增
     * @param userId
     * @return
     */
    Integer findTodayTeamOrderSize(Long userId);

    /**
     * 查找 团队订单信息
     * @param userId
     * @param orderNo
     * @return
     */
    OrderAccountDetailsResponse findTeamOrder(Long userId, String orderNo);

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    ShopMemberOrder findOrderByOrderNo(String orderNo);

    /**
     * 团队总数
     * @param userId
     * @return
     */
    Integer findCountOrderSize(Long userId);
    /**
     * 团队总金币，现金
     * @param userId
     * @return
     */
    CountTeamOrderMoneyCoinVo findCountTeamMoneyAndCoin(Long userId);

    /**
     * 团队订单 商品信息
     * @param userId
     * @param orderNo
     * @return
     */
    List<OrderItemSkuVo> findTeamOrderItemSku(Long userId, String orderNo);

    /**
     * 用户已成功的订单数
     *
     * @param memberId memberId
     * @param excludeOrderNo 排除在外的订单号
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/11/27 15:18
     */
    Long countUserSuccessOrder(Long memberId, String excludeOrderNo);


    /**
     * 生成订单
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/11 15:05
     */
    Map<String,Object> createOrder(StoreBizOrderQuery query);


    /**
     * 预支付
     *
     * @param query request
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/11 20:35
     */
    String prePayOrder(StoreBizOrderQuery query);



    void paySuccess(StoreBizOrderQuery query);

    /**
     * 设置为默认地址
     * @param memberId
     * @param id
     * @return
     */
    Response defaultAddress( Long memberId,Long id);

    /**
     * 获取当前收货地址
     * @param type
     * @param storeId
     * @param memberId
     * @return
     */
    Response getAddress(int type,Long storeId,Long memberId);

    /**
     * 添加收货地址
     * @param requestAddress
     * @return
     */
    Response addAddress(RequestAddress requestAddress);
    /**
     * 查询某一个具体的收货地址
     */
    Response selectAddress(Long memberId,Long id);
    /**
     * 保存修改的地址
     */
    Response updateAddress(RequestAddress requestAddress);

    /**
     * 查询订单详情
     *
     * @param orderItemId orderItemId
     * @return com.e_commerce.miscroservice.order.entity.ShopMemberOrderItem
     * @author Charlie
     * @date 2019/1/30 10:22
     */
    ShopMemberOrderItem findBySql(Long orderItemId);
}
