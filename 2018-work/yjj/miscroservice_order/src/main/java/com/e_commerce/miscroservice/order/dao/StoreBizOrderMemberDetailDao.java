package com.e_commerce.miscroservice.order.dao;

import com.e_commerce.miscroservice.order.entity.StoreBizOrderMemberDetail;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 17:55
 * @Copyright 玖远网络
 */
public interface StoreBizOrderMemberDetailDao{


    /**
     * 保存
     *
     * @param orderDetail orderDetail
     * @return int
     * @author Charlie
     * @date 2018/12/11 17:58
     */
    int save(StoreBizOrderMemberDetail orderDetail);

    StoreBizOrderMemberDetail findByOrderId(Long orderId);
}
