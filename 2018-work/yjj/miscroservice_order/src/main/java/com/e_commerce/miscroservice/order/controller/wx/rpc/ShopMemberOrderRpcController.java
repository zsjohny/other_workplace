package com.e_commerce.miscroservice.order.controller.wx.rpc;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.TeamOrder;
import com.e_commerce.miscroservice.commons.entity.order.CountTeamOrderMoneyCoinVo;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import com.e_commerce.miscroservice.commons.entity.order.OrderItemSkuVo;
import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderItemQuery;
import com.e_commerce.miscroservice.order.entity.ShopMemberOrderItem;
import com.e_commerce.miscroservice.order.service.wx.ShopMemberOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/11 17:13
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping("/order/rpc/shopMemberOrder")
public class ShopMemberOrderRpcController{

    @Autowired
    private ShopMemberOrderService shopMemberOrderService;

    /**
     * 更加订单号修改订单
     *
     * @param shopMemberOrder shopMemberOrder
     * @return true 操作成功
     * @author Charlie
     * @date 2018/10/11 17:57
     */
    @RequestMapping( "updateByOrderNoSelective" )
    public boolean updateByOrderNoSelective(@RequestBody ShopMemberOrder shopMemberOrder) {
        try {
            shopMemberOrderService.updateByOrderNoSelective(shopMemberOrder);
        } catch (Exception e) {
            e.printStackTrace ();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 当月 店长 分销商当月销售总金额
     * @param userId
     * @param type
     * @return
     */
    @RequestMapping(value = "montyTotalMoney",method = RequestMethod.GET)
    public Double montyTotalMoney(@RequestParam(value = "userId") Long userId, @RequestParam(value = "type") Integer type){
        return shopMemberOrderService.theMoneyTotalMoney(userId,type);
    }

    /**
     * 查找 订单列表
     * @param userId
     * @param page
     * @return
     */
    @RequestMapping(value = "findOrderList",method = RequestMethod.GET)
    public List<ShopMemberOrder> findOrderList(@RequestParam(value = "userId")Long userId, @RequestParam(value = "page")Integer page){
        return shopMemberOrderService.findOrderList(userId,page);
    }

    /**
     * 查找 团队订单列表
     * @param userId
     * @param page
     * @return
     */
    @RequestMapping(value = "findTeamOrderList",method = RequestMethod.GET)
    public List<TeamOrder> findTeamOrderList(@RequestParam(value = "userId")Long userId, @RequestParam(value = "page")Integer page,@RequestParam(value = "orderNo",required = false)String orderNo){
        return shopMemberOrderService.findTeamOrderList(userId,page,orderNo);
    }

    /**
     * 查找 团队订单信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "findTeamOrder",method = RequestMethod.GET)
    public OrderAccountDetailsResponse findTeamOrder(@RequestParam(value = "userId")Long userId, @RequestParam(value = "orderNo")String orderNo){
        return shopMemberOrderService.findTeamOrder(userId,orderNo);
    }


    /**
     * 查找 团队今日新增
     * @param userId
     * @return
     */
    @RequestMapping(value = "findTodayTeamOrderSize",method = RequestMethod.GET)
    public Integer findTodayTeamOrderSize(@RequestParam(value = "userId")Long userId){
        return shopMemberOrderService.findTodayTeamOrderSize(userId);
    }

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "findOrderByOrderNo")
    public ShopMemberOrder findOrderByOrderNo(@RequestParam(value = "orderNo")String orderNo){
        return shopMemberOrderService.findOrderByOrderNo(orderNo);
    }

    /**
     * 查找 团队总数
     * @param userId
     * @return
     */
    @RequestMapping(value = "findCountOrderSize",method = RequestMethod.GET)
    public Integer findCountOrderSize(@RequestParam(value = "userId")Long userId){
        return shopMemberOrderService.findCountOrderSize(userId);
    }

    /**
     * 团队总金币，现金
     * @param userId
     * @return
     */
    @RequestMapping(value = "findCountTeamMoneyAndCoin")
    public CountTeamOrderMoneyCoinVo findCountTeamMoneyAndCoin(@RequestParam(value = "userId")Long userId){
        return shopMemberOrderService.findCountTeamMoneyAndCoin(userId);
    }

    /**
     * 团队订单 商品信息
     * @param userId
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "findTeamOrderItemSku")
    List<OrderItemSkuVo> findTeamOrderItemSku(@RequestParam(value = "userId")Long userId, @RequestParam(value = "orderNo")String orderNo){
        return shopMemberOrderService.findTeamOrderItemSku(userId,orderNo);
    }



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
    public Long countUserSuccessOrder(@RequestParam("memberId") Long memberId, @RequestParam(value = "excludeOrderNo", required = false) String excludeOrderNo){
        return shopMemberOrderService.countUserSuccessOrder (memberId, excludeOrderNo);
    }

    @RequestMapping( "findItemByItemId" )
    public ShopMemberOrderItemQuery findItemByItemId(@RequestParam( "orderItemId" ) Long orderItemId) {

        ShopMemberOrderItem item = shopMemberOrderService.findBySql(orderItemId);
        if (item == null) {
            return null;
        }
        ShopMemberOrderItemQuery ret = new ShopMemberOrderItemQuery();
        ret.setProductName(item.getName());
        ret.setId(item.getId());
        ret.setCount(item.getCount());
        ret.setProductSkuId(item.getProductSkuId());
        ret.setLiveProductId(item.getLiveProductId());
        return ret;
    }

}
