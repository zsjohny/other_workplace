package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.TeamOrder;
import com.e_commerce.miscroservice.commons.entity.order.CountTeamOrderMoneyCoinVo;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import com.e_commerce.miscroservice.commons.entity.order.OrderItemSkuVo;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.order.dao.ShopMemberOrderDao;
import com.e_commerce.miscroservice.order.mapper.ShopMemberOrderMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/12 11:13
 * @Copyright 玖远网络
 */
@Repository
public class ShopMemberOrderDaoImpl implements ShopMemberOrderDao {
    @Resource
    private ShopMemberOrderMapper shopMemberOrderMapper;
    /**
     * 当月 粉丝 团队 自购总金额
     * @param userId
     * @param type
     * @return
     */
    @Override
    public Double storeTheMoneyTotalMoney(Long userId, Integer type) {
        return shopMemberOrderMapper.storeTheMoneyTotalMoney(userId,type);
    }
    /**
     * 分销商当月总购金额
     * @param userId
     * @param type
     * @return
     */
    @Override
    public Double findCountMoneyShopMemberOrderByUser(Long userId, Integer type) {
        return shopMemberOrderMapper.findCountMoneyShopMemberOrderByUser(userId,type);
    }
    /**
     * 查找 订单列表
     * @param userId
     * @param page
     * @return
     */
    @Override
    public List<ShopMemberOrder> findOrderList(Long userId, Integer page)
    {
//        List<ShopMemberOrder> list = MybatisOperaterUtil.getInstance()
//                .finAll(
//                        new ShopMemberOrder(),
//                        new MybatisSqlWhereBuild(ShopMemberOrder.class)
//                                .eq(ShopMemberOrder::getMemberId,userId)
//                                .page(page,10)
//        );
        PageHelper.startPage(page,10);
        List<ShopMemberOrder> list = shopMemberOrderMapper.findOrderList(userId);
        return list;
    }
    /**
     * 查找 团队订单列表
     * @param userId
     * @param page
     * @param orderNo
     * @return
     */
    @Override
    public List<TeamOrder> findTeamOrderList(Long userId, Integer page, String orderNo) {
        PageHelper.startPage(page,10);
        List<TeamOrder> list = shopMemberOrderMapper.findTeamOrderList(userId,orderNo);

        return list;
    }
    /**
     * 今日团队订单新增
     * @param userId
     * @return
     */
    @Override
    public Integer findTodayTeamOrderSize(Long userId) {
        return shopMemberOrderMapper.findTodayTeamOrderSize(userId);
    }
    /**
     * 查找 团队订单信息
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public OrderAccountDetailsResponse findTeamOrder(Long userId, String orderNo) {
        OrderAccountDetailsResponse response = shopMemberOrderMapper.findTeamOrder(userId,orderNo);
        if (response!=null){
            response.setPayTime(TimeUtils.longFormatString(Long.valueOf(response.getPayTime())));
            response.setCreateTime(TimeUtils.longFormatString(Long.valueOf(response.getCreateTime())));
            response.setConfirmSignedTime(TimeUtils.longFormatString(Long.valueOf(response.getConfirmSignedTime())));
        }
        return response;
    }
    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    @Override
    public ShopMemberOrder findOrderByOrderNo(String orderNo) {
        return shopMemberOrderMapper.findOrderByOrderNo(orderNo);
    }
    /**
     * 团队总数
     * @param userId
     * @return
     */
    @Override
    public Integer findCountOrderSize(Long userId)
    {
        return shopMemberOrderMapper.findCountOrderSize(userId);
    }
    /**
     * 团队总金币，现金
     * @param userId
     * @return
     */
    @Override
    public CountTeamOrderMoneyCoinVo findCountTeamMoneyAndCoin(Long userId) {
        return shopMemberOrderMapper.findCountTeamMoneyAndCoin(userId);
    }
    /**
     * 团队订单 商品信息
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public List<OrderItemSkuVo> findTeamOrderItemSku(Long userId, String orderNo) {
        return shopMemberOrderMapper.findTeamOrderItemSku(userId,orderNo);
    }
}
