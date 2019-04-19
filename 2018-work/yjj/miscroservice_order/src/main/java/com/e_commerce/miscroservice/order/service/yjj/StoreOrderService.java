package com.e_commerce.miscroservice.order.service.yjj;

import com.e_commerce.miscroservice.order.vo.StoreOrderDTO;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/17 11:01
 */
public interface StoreOrderService {


    /**
     * 平台发货15天
     *
     * @param dto dto
     * @return Map
     * @author Charlie
     * @date 2019/2/17 11:21
     */
    void platformInsteadOfSendGoodsAfter15Days(StoreOrderDTO dto);
}
