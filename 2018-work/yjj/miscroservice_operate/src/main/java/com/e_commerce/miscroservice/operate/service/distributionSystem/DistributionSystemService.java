package com.e_commerce.miscroservice.operate.service.distributionSystem;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/9 14:27
 * @Copyright 玖远网络
 */
public interface DistributionSystemService{




    /**
     * 查询分销订单收益记录表
     *
     * @param orderNumber orderNumber
     * @return com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord
     * @author Charlie
     * @date 2018/11/9 15:07
     */
    ShopMemberOrderDstbRecord findShopMemberOrderRecord(String orderNumber);
}
