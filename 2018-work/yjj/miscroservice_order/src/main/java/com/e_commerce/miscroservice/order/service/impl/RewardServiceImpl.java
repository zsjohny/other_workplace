package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoodsType;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrder;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward;
import com.e_commerce.miscroservice.commons.rpc.proxy.ProxyGoodsRpcService;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.service.impl.BaseServiceImpl;
import com.e_commerce.miscroservice.order.mapper.RewardMapper;
import com.e_commerce.miscroservice.order.rpc.proxy.ProxyCustomerRpcService;
import com.e_commerce.miscroservice.order.service.proxy.ProxyOrderService;
import com.e_commerce.miscroservice.order.service.proxy.ProxyRewardService;
import com.e_commerce.miscroservice.order.vo.ProxyRefereeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RewardServiceImpl extends BaseServiceImpl<ProxyReward> implements ProxyRewardService {

    @Autowired
    ProxyOrderService proxyOrderService;

    @Autowired
    ProxyGoodsRpcService proxyGoodsRpcService;

    @Autowired
    ProxyCustomerRpcService proxyCustomerRpcService;

    @Autowired
    RewardMapper rewardMapper;

    /**
     * 描述 生成订单收益
     * @param proxyOrder
     * @author hyq
     * @date 2018/10/11 12:03
     * @return void
     */
    @Override
    public void getOrderReward(ProxyOrder proxyOrder) {

        if (proxyOrder.getIsProfit().intValue() == 0) {
            return;
            //Response.errorMsg("不是收益订单");
        }

        if (proxyOrder.getIsGrant().intValue() == 1) {
            return;
            //Response.errorMsg("已经发放过收益");
        }

        //获取推荐关系
        ProxyRefereeVo refereeUser = proxyOrderService.getRefereeUser(proxyOrder.getUserId());

        if(refereeUser==null){
            return;
        }

        //获取商品的类型和返利比例
        ProxyGoodsType proxyGoodsType = proxyGoodsRpcService.selectGoodsType(proxyOrder.getGoodsTypeId());

        //这里可以根据order_no来判断一下重复插入

        //存在父级。
        if (refereeUser.getRefereParentId().intValue() != 0) {

            BigDecimal oneLevel2 = proxyGoodsType.getOneLevel();
            BigDecimal twoLevel = proxyGoodsType.getTwoLevel();

            //直属上级的收益
            ProxyReward proxyReward = buildProxyReward(oneLevel2, refereeUser.getRefereeUserId(), proxyOrder);
            this.insert(proxyReward);

            //上上级的收益
            if(twoLevel.subtract(BigDecimal.ZERO).intValue()==0){

            }else {
                ProxyReward proxyReward1 = buildProxyReward(twoLevel, refereeUser.getRefereParentId(), proxyOrder);
                this.insert(proxyReward1);
            }

        } else {

            BigDecimal oneLevel = proxyGoodsType.getOneLevel();
            //直属上级的收益
            ProxyReward proxyReward = buildProxyReward(oneLevel, refereeUser.getRefereeUserId(), proxyOrder);
            this.insert(proxyReward);

//            //不存在父级的话，那直接判断直属上级是市级代理还是县级代理
//            //判断一级是市还是县
//            Integer i = rewardMapper.proxyType(refereeUser.getRefereeUserId());
//            if(i!=null && i.intValue()>0){
//                //市级代理
//                if (i.intValue()==1){
//                    BigDecimal oneLevel = proxyGoodsType.getOneLevel();
//                    //直属上级的收益
//                    ProxyReward proxyReward = buildProxyReward(oneLevel, refereeUser.getRefereeUserId(), proxyOrder);
//                    this.insert(proxyReward);
//                }else{
//                    BigDecimal oneLevel2 = proxyGoodsType.getOneLevel();
//                    //直属上级的收益
//                    ProxyReward proxyReward = buildProxyReward(oneLevel2, refereeUser.getRefereeUserId(), proxyOrder);
//                    this.insert(proxyReward);
//                }
//            }
        }

    }

    /**
     * 描述 根据用户id 订单号 获取本笔订单的收益
     * @param userId
     * @param orderNo
     * @author hyq
     * @date 2018/10/11 12:02
     * @return com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward
     */
    @Override
    public ProxyReward getRewardMoney(long userId, String orderNo) {
        return rewardMapper.getRewardMoney(userId, orderNo);
    }

    private ProxyReward buildProxyReward(BigDecimal level,long userId,ProxyOrder proxyOrder){
        ProxyReward proxyReward = new ProxyReward();
        proxyReward.setUserId(userId);
        proxyReward.setOrderUserId(proxyOrder.getUserId());
        proxyReward.setOrderUserRole(proxyCustomerRpcService.judeCustomerRole(proxyOrder.getUserId()));
        proxyReward.setOrderNo(proxyOrder.getOrderNo());
        proxyReward.setOrderMoney(proxyOrder.getPrice());
        proxyReward.setRewardRate(level);
        proxyReward.setRewardMoney(level.multiply(proxyOrder.getPrice()).setScale (2, BigDecimal.ROUND_DOWN));
        proxyReward.setOrderTime(proxyOrder.getCreateTime());

        return proxyReward;
    }

}
