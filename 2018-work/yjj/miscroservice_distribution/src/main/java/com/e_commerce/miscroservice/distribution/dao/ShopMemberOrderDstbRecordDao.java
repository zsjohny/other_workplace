package com.e_commerce.miscroservice.distribution.dao;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/16 17:17
 * @Copyright 玖远网络
 */
public interface ShopMemberOrderDstbRecordDao{


    /**
     * 新增
     *
     * @param orderRecord 实体
     * @return int
     * @author Charlie
     * @date 2018/10/16 17:25
     */
    int insertSelective(ShopMemberOrderDstbRecord orderRecord);

    /**
     * 根据订单号查询订单拓展信息
     * @param orderNo
     * @return
     */
    ShopMemberOrderDstbRecord findByOrderNo(String orderNo);



    /**
     * 根据id更新
     *
     * @param updInfo updInfo
     * @return int
     * @author Charlie
     * @date 2018/10/16 19:02
     */
    int updateByPrimaryKeySelective(ShopMemberOrderDstbRecord updInfo);



    /**
     * 根据订单号查找
     *
     * @param orderNo orderNo
     * @return com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord
     * @author Charlie
     * @date 2018/10/17 9:51
     */
    ShopMemberOrderDstbRecord selectByOrderNo(String orderNo);
}
