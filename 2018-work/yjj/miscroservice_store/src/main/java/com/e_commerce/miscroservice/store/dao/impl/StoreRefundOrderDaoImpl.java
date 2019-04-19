package com.e_commerce.miscroservice.store.dao.impl;


import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.store.dao.StoreRefundOrderDao;
import com.e_commerce.miscroservice.store.entity.response.RefundOrderListResponse;
import com.e_commerce.miscroservice.store.entity.vo.*;
import com.e_commerce.miscroservice.store.mapper.StoreRefundOrderMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

import static java.awt.SystemColor.info;

/**
 * 售后列表
 */
@Repository
public class StoreRefundOrderDaoImpl implements StoreRefundOrderDao {

    @Resource
    private StoreRefundOrderMapper storeRefundOrderMapper;
    @Override
    public PageInfo<RefundOrderListResponse> findRefundOrderListByUserId(Integer pageNum, Integer pageSize, Long userId) {
        PageHelper.startPage(pageNum,pageSize);
        List<RefundOrderListResponse> list = storeRefundOrderMapper.findRefundOrderListByUserId(userId);
        for (RefundOrderListResponse refundOrderListResponse : list) {
            StoreOrderItemNew storeOrderItem = storeRefundOrderMapper.selectOrderItem(refundOrderListResponse.getOrderNo(), refundOrderListResponse.getSkuId());
            if (storeOrderItem==null){
                return null;//根据skuid和商品订单号没有查到商品详情
            }
            StoreOrderNew storeOrderNew = storeRefundOrderMapper.selectByOrderNo(refundOrderListResponse.getOrderNo());
            refundOrderListResponse.setOrderStatus(storeOrderNew.getOrderStatus().toString());
            refundOrderListResponse.setTotalBuyCount(storeOrderItem.getBuyCount());
            refundOrderListResponse.setPracticalTotalPay(storeOrderItem.getTotalPay()+storeOrderItem.getTotalExpressMoney());
            long productId = storeOrderItem.getProductId();
            ProductNew product = storeRefundOrderMapper.selectProdectById(productId);
            refundOrderListResponse.setImg(product.getFirstDetailImage());//商品图片
            refundOrderListResponse.setName(product.getName());//商品名称
            String skuSnapshot = storeOrderItem.getSkuSnapshot();
            if(StringUtils.isEmpty(skuSnapshot)){
                refundOrderListResponse.setColor("");
                refundOrderListResponse.setSize("");
            }else{
                String[] split = skuSnapshot.split("  ");
                String[] color = split[0].split(":");
                String[] size = split[1].split(":");
                refundOrderListResponse.setColor(color[1]);//商品颜色
                refundOrderListResponse.setSize(size[1]);//商品尺码
            }
        }
        PageInfo<RefundOrderListResponse> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public StoreRefundOrder getRefundOrderByOrderNoUnderWayOrSuccess(Long orderNo)
    {
        return MybatisOperaterUtil.getInstance().findOne(new StoreRefundOrder(),new MybatisSqlWhereBuild(StoreRefundOrder.class).eq(StoreRefundOrder::getOrderNo,orderNo));
    }

    @Override
    public int insertOneRefundOrder(StoreRefundOrder refundOrder)
    {
        return MybatisOperaterUtil.getInstance().save(refundOrder);
    }

    @Override
    public StoreRefundOrder findRefundOrderById(Long refundOrderId) {
        return MybatisOperaterUtil.getInstance().findOne(new StoreRefundOrder(),new MybatisSqlWhereBuild(StoreRefundOrder.class).eq(StoreRefundOrder::getId,refundOrderId));
    }
}
