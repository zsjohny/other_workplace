package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrderResponce;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.dao.RefundOrderDao;
import com.e_commerce.miscroservice.operate.entity.request.RefundOrderFindReqeust;
import com.e_commerce.miscroservice.operate.mapper.RefundOrderMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Create by hyf on 2018/11/30
 */

@Repository
public class RefundOrderDaoImpl implements RefundOrderDao {
    @Resource
    private RefundOrderMapper refundOrderMapper;
    /**
     * 售后订单管理
     * @param obj
     * @return
     */
    @Override
    public List<RefundOrderResponce> findAllRefundOrder(RefundOrderFindReqeust obj) {
        PageHelper.startPage(obj.getPageNumber(),obj.getPageSize());
        List<RefundOrderResponce> list =   refundOrderMapper.findAllRefundOrder(obj);
        return list;
    }
    /**
     * 根据id查询 售后订单
     * @param id
     * @return
     */
    @Override
    public RefundOrder findRefundOrderById(Long id) {
//        RefundOrder refundOrder = MybatisOperaterUtil.getInstance().findOne(new RefundOrder(),new MybatisSqlWhereBuild(RefundOrder.class).eq(RefundOrder::getId,id));
        RefundOrder refundOrder =  refundOrderMapper.findRefundOrderById(id);
        return refundOrder;
    }


    /**
     * 更新售后订单
     * @param refundOrder
     */
    @Override
    public void updateRefundOrder(Long id, Double money, String msg) {
        refundOrderMapper.updateRefundOrder(id,money,msg);
    }
}
