package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyGoods;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrder;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrderQuery;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward;
import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.proxy.PayResult;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.service.impl.BaseServiceImpl;
import com.e_commerce.miscroservice.commons.rpc.proxy.ProxyGoodsRpcService;
import com.e_commerce.miscroservice.commons.utils.pay.XmlUtil;
import com.e_commerce.miscroservice.order.mapper.ProxyOrderMapper;
import com.e_commerce.miscroservice.order.rpc.proxy.ProxyCustomerRpcService;
import com.e_commerce.miscroservice.order.rpc.proxy.ProxyUserRpcService;
import com.e_commerce.miscroservice.order.service.proxy.ProxyOrderService;
import com.e_commerce.miscroservice.order.service.proxy.ProxyRewardService;
import com.e_commerce.miscroservice.order.utils.enums.OrderStatusEnum;
import com.e_commerce.miscroservice.order.vo.ProxyRefereeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProxyOrderServiceImpl extends BaseServiceImpl<ProxyOrder> implements ProxyOrderService {

    private Log logger = Log.getInstance(ProxyOrderServiceImpl.class);

    @Autowired
    ProxyOrderMapper proxyOrderMapper;

    @Autowired
    private ProxyOrderService proxyOrderService;

    @Autowired
    private ProxyGoodsRpcService proxyGoodsRpcService;

    @Autowired
    private ProxyRewardService proxyRewardService;

    @Autowired
    private ProxyCustomerRpcService proxyCustomerRpcService;

    @Autowired
    private ProxyUserRpcService proxyUserRpcService;


    /**
     * 描述 获取订单支付后的订单详情
     * @param orderId 订单号
     * @author hyq
     * @date 2018/9/25 17:29
     * @return com.e_commerce.miscroservice.commons.utils.Response
     */
    @Override
    public List<ProxyOrder> getOrderByOrderId(String orderId) {
        return proxyOrderMapper.getOrderByOrderId(orderId);
    }

    /**
     * 描述 根据订单号获取订单
     * @param userId
     * @param pageNum
     * @param pageSize
     * @author hyq
     * @date 2018/10/8 15:23
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrderQuery>
     */
    @Override
    public List<ProxyOrderQuery> getOrderByUserId(long userId,int pageNum,int pageSize) {
        BasePage.setPageParams(pageNum,pageSize);
        return proxyOrderMapper.getOrderByUserId(userId);
    }

    /**
     * 描述 根据用户类型获取订单列表
     * @param userId
     * @param type 1 客户 2 代理  3 自己
     * @param pageNum
     * @param pageSize
     * @author hyq
     * @date 2018/10/8 15:26
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrderQuery>
     */
    @Override
    public List<ProxyOrderQuery> getOrderByUserIdAndType(long userId, int type, int pageNum, int pageSize) {

        BasePage.setPageParams(pageNum,pageSize);
        if(type==1){
            return proxyOrderMapper.getOrderByUserIdAndTypeOne(userId);
        }else{
            return proxyOrderMapper.getOrderByUserIdAndTypeTwo(userId);
        }

    }

    @Override
    public List<ProxyReward> getRewardOrderList(long userId,String startTime ,String endTime, int pageNum, int pageSize,List<Integer> isGrants) {
        BasePage.setPageParams(pageNum,pageSize);
        return proxyOrderMapper.getRewardOrderList(userId,startTime,endTime,isGrants);
    }

    @Override
    public List<ProxyOrder> getOrderList(ProxyOrderQuery query) {
        BasePage.setPageParams(query.getPageNum(),query.getPageSize());
        return proxyOrderMapper.getOrderList(query);
    }

    @Override
    public PageInfo<ProxyOrder> getProfitOrderList(int isProfit, int pageNum, int pageSize) {
        BasePage.setPageParams(pageNum,pageSize);
        List<ProxyOrder> profitOrderList = proxyOrderMapper.getProfitOrderList (isProfit);
        return new PageInfo<> (profitOrderList);
    }

    /**
     * 描述 获取推荐关系
     * @param userId
     * @author hyq
     * @date 2018/10/11 11:59
     * @return com.e_commerce.miscroservice.order.vo.ProxyRefereeVo
     */
    @Override
    public ProxyRefereeVo getRefereeUser(long userId) {
        return proxyOrderMapper.getRefereeUser(userId);
    }

    @Override
    public ProxyRefereeVo getAllRefereeUser(long userId) {
        return proxyOrderMapper.getAllRefereeUser(userId);
    }

    /**
     * 描述 获取代理名字
     * @param userId
     * @author hyq
     * @date 2018/10/11 12:00
     * @return java.lang.String
     */
    @Override
    public String getRefereeCustomerName(long userId) {
        return proxyOrderMapper.getRefereeCustomerName(userId);
    }

    /**
     * 描述 获取公众号名字
     * @param userId
     * @author hyq
     * @date 2018/10/11 12:00
     * @return java.lang.String
     */
    @Override
    public String getRefereePublicName(long userId) {
        return proxyOrderMapper.getRefereePublicName(userId);
    }

    /**
     * 描述 汇总收益金额
     * @param userId
     * @param isGrants
     * @author hyq
     * @date 2018/10/8 19:11
     * @return java.math.BigDecimal
     */
    @Override
    public BigDecimal getCollectReward(long userId, List<Integer> isGrants) {
        return proxyOrderMapper.getCollectReward(userId, isGrants);
    }

    /**
     * 描述 获取当日汇总收益
     * @param userId
    * @param isGrants
     * @author hyq
     * @date 2018/10/11 12:01
     * @return java.math.BigDecimal
     */
    @Override
    public BigDecimal getTodayCollectReward(long userId, List<Integer> isGrants) {
        return proxyOrderMapper.getTodayCollectReward(userId, isGrants);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doUpdOrderAfterPay(String result) {
        logger.info ("代理会员支付异步 result={}", result);
        PayResult payResult = null;
        try {
            payResult = (PayResult)XmlUtil.xml2Object(result, PayResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw ErrorHelper.me ("XML 解析错误");
        }

        boolean isPaid = "SUCCESS".equals (payResult.getReturn_code ());

        List<ProxyOrder> orderByOrderId = proxyOrderService.getOrderByOrderId(payResult.getOut_trade_no());
        if (isPaid) {
            //支付成功

            if (orderByOrderId != null || orderByOrderId.size() > 0) {
                ProxyOrder proxyOrder = orderByOrderId.get(0);

                //防止数据重复操作
                if (OrderStatusEnum.SUCCESS.isThis(proxyOrder.getStatus())) {
                    return;
                }

                ProxyGoods proxyGoods = proxyGoodsRpcService.selectById(proxyOrder.getGoodsId());

                ProxyGoods proxyGoods1 = new ProxyGoods();
                proxyGoods1.setId(proxyOrder.getGoodsId());
                int i =proxyGoods.getAllVolume().intValue()+1;
                proxyGoods1.setAllVolume(i);

                //更新销量
                proxyGoodsRpcService.saveGoods(proxyGoods1);

                proxyOrder.setPayOrderNo(payResult.getTransaction_id());
                proxyOrder.setStatus(OrderStatusEnum.SUCCESS.getCode());

                //这里开始生成收益，就生成几条reward
                proxyRewardService.getOrderReward(proxyOrder);

                proxyOrder.setIsGrant(1);
                proxyOrderService.updateById(proxyOrder);

                //ProxyGoods proxyGoods = proxyGoodsRpcService.selectById(proxyOrder.getGoodsId());
                PublicAccountUser byUserId = proxyCustomerRpcService.findByUserId(proxyOrder.getUserId());

                //创建store账号
                logger.info("购买商品成功，开始生成会员:orderId{}:goodId{}"+payResult.getOut_trade_no(),proxyOrder.getGoodsId());
                //公账号买的会员类型+100, 以区别APP端购买的会员商品
                int memberTypeFromPublic = proxyOrder.getGoodsTypeId ().intValue () + 100;
                proxyUserRpcService.buyMember("公众号",null,null,1,null,payResult.getOut_trade_no()
                        ,proxyOrder.getPrice().doubleValue(),byUserId.getPhone(),null, memberTypeFromPublic);
            }

        } else {

            //支付失败
            if (orderByOrderId != null || orderByOrderId.size() > 0) {
                ProxyOrder proxyOrder = orderByOrderId.get(0);

                //防止数据重复操作
                if (OrderStatusEnum.FAIL.isThis(proxyOrder.getStatus())) {
                    return;
                }

                proxyOrder.setPayOrderNo(payResult.getTransaction_id());
                proxyOrder.setStatus(OrderStatusEnum.FAIL.getCode());

                proxyOrderService.updateById(proxyOrder);
            }
        }
    }


}
