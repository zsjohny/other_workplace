package com.e_commerce.miscroservice.distribution.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.distribution.dao.ShopMemberOrderDstbRecordDao;
import com.e_commerce.miscroservice.distribution.mapper.ShopMemberOrderDstbRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/16 17:17
 * @Copyright 玖远网络
 */
@Component
public class ShopMemberOrderDstbRecordDaoImpl implements ShopMemberOrderDstbRecordDao{
    @Autowired
    private ShopMemberOrderDstbRecordMapper shopMemberOrderDstbRecordMapper;


    /**
     * 新增
     *
     * @param orderRecord 实体
     * @return int
     * @author Charlie
     * @date 2018/10/16 17:25
     */
    @Override
    public int insertSelective(ShopMemberOrderDstbRecord orderRecord) {
        return shopMemberOrderDstbRecordMapper.insertSelective(orderRecord);
    }

    /**
     * 根据订单号查询订单拓展信息
     *
     * @param orderNo
     * @return
     */
    @Override
    public ShopMemberOrderDstbRecord findByOrderNo(String orderNo) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ShopMemberOrderDstbRecord(),
                new MybatisSqlWhereBuild(ShopMemberOrderDstbRecord.class)
                .eq(ShopMemberOrderDstbRecord::getOrderNo,orderNo)
        );
    }


    /**
     * 根据id更新
     *
     * @param updInfo updInfo
     * @return int
     * @author Charlie
     * @date 2018/10/16 19:02
     */
    @Override
    public int updateByPrimaryKeySelective(ShopMemberOrderDstbRecord updInfo) {
        return shopMemberOrderDstbRecordMapper.updateByPrimaryKeySelective (updInfo);
    }



    /**
     * 根据订单号查找
     *
     * @param orderNo orderNo
     * @return com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord
     * @author Charlie
     * @date 2018/10/17 9:51
     */
    @Override
    public ShopMemberOrderDstbRecord selectByOrderNo(String orderNo) {
        if (orderNo == null) {
            return null;
        }
        return MybatisOperaterUtil.getInstance ().findOne (
                new ShopMemberOrderDstbRecord (),
                new MybatisSqlWhereBuild (ShopMemberOrderDstbRecord.class)
                        .eq (ShopMemberOrderDstbRecord::getOrderNo, orderNo)
        );
    }
}
