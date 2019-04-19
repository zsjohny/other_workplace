package com.e_commerce.miscroservice.distribution.rpc.order;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.TeamOrder;
import com.e_commerce.miscroservice.commons.entity.order.CountTeamOrderMoneyCoinVo;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import com.e_commerce.miscroservice.commons.entity.order.OrderItemSkuVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/11 17:12
 * @Copyright 玖远网络
 */
@FeignClient(value = "ORDER", path = "/order/rpc/shopMemberOrder")
@Component
public interface ShopMemberOrderRpcService{

    @RequestMapping("updateByOrderNoSelective")
    boolean updateByOrderNoSelective(@RequestBody ShopMemberOrder shopMemberOrder);
    /**
     * 当月 店长 分销商当月销售总金额
     * @param userId
     * @param type
     * @return
     */
    @RequestMapping(value = "montyTotalMoney",method = RequestMethod.GET)
    public Double montyTotalMoney(@RequestParam(value = "userId") Long userId, @RequestParam(value = "type") Integer type);

    @RequestMapping(value = "findOrderList",method = RequestMethod.GET)
    public List<ShopMemberOrder> findOrderList(@RequestParam(value = "userId")Long userId, @RequestParam(value = "page")Integer page);


    /**
     * 查找 团队订单列表
     * @param userId
     * @param page
     * @return
     */
    @RequestMapping(value = "findTeamOrderList",method = RequestMethod.GET)
    public List<TeamOrder> findTeamOrderList(@RequestParam(value = "userId")Long userId, @RequestParam(value = "page")Integer page,@RequestParam(value = "orderNo",required = false)String orderNo);

    /**
     * 查找 团队订单信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "findTeamOrder",method = RequestMethod.GET)
    public OrderAccountDetailsResponse findTeamOrder(@RequestParam(value = "userId")Long userId, @RequestParam(value = "orderNo")String orderNo);
    /**
     * 查找 团队今日新增
     * @param userId
     * @return
     */
    @RequestMapping(value = "findTodayTeamOrderSize",method = RequestMethod.GET)
    public Integer findTodayTeamOrderSize(@RequestParam(value = "userId")Long userId);

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "findOrderByOrderNo")
    public ShopMemberOrder findOrderByOrderNo(@RequestParam(value = "orderNo")String orderNo);


    /**
     * 查找 团队总数
     * @param userId
     * @return
     */
    @RequestMapping(value = "findCountOrderSize",method = RequestMethod.GET)
    public Integer findCountOrderSize(@RequestParam(value = "userId")Long userId);

    /**
     * 团队总金币，现金
     * @param userId
     * @return
     */
    @RequestMapping(value = "findCountTeamMoneyAndCoin")
    public CountTeamOrderMoneyCoinVo findCountTeamMoneyAndCoin(@RequestParam(value = "userId")Long userId);

    /**
     * 团队订单 商品信息
     * @param userId
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "findTeamOrderItemSku")
    List<OrderItemSkuVo> findTeamOrderItemSku(@RequestParam(value = "userId")Long userId, @RequestParam(value = "orderNo")String orderNo);

    /**
     * 用户已成功的订单数
     *
     * @param memberId memberId
     * @param excludeOrderNo 排除在外的订单号
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/11/27 15:18
     */
    @RequestMapping(value = "countUserSuccessOrder")
    Long countUserSuccessOrder(@RequestParam("memberId") Long memberId, @RequestParam(value = "excludeOrderNo", required = false) String excludeOrderNo);
}
