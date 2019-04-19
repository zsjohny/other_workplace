package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrderResponce;
import com.e_commerce.miscroservice.operate.entity.request.RefundOrderFindReqeust;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create by hyf on 2018/11/30
 */
@Mapper
public interface RefundOrderMapper {
    /**
     * 售后订单管理
     * @param obj
     * @return
     */
    List<RefundOrderResponce> findAllRefundOrder(@Param("obj") RefundOrderFindReqeust obj);

    RefundOrder findRefundOrderById(@Param("id") Long id);

    /**
     * 更新售后订单 结束订单
     * @param id
     * @param money
     * @param msg
     */
    void updateRefundOrder(@Param("id") Long id, @Param("money") Double money, @Param("msg") String msg);
}
