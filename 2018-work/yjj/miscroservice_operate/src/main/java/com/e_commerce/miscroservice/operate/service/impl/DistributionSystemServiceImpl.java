package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.dao.ShopMemberAccountCashOutInDao;
import com.e_commerce.miscroservice.operate.service.distributionSystem.DistributionSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/9 14:27
 * @Copyright 玖远网络
 */
@Service
public class DistributionSystemServiceImpl implements DistributionSystemService{


    @Autowired
    private ShopMemberAccountCashOutInDao shopMemberAccountCashOutInDao;







    /**
     * 查询分销订单收益记录表
     *
     * @param orderNumber orderNumber
     * @return com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord
     * @author Charlie
     * @date 2018/11/9 15:07
     */
    @Override
    public ShopMemberOrderDstbRecord findShopMemberOrderRecord(String orderNumber) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ShopMemberOrderDstbRecord (),
                new MybatisSqlWhereBuild (ShopMemberOrderDstbRecord.class)
                        .eq (ShopMemberOrderDstbRecord::getOrderNo, orderNumber)
        );
    }
}
