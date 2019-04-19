package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.order.dao.StoreBizOrderMemberDetailDao;
import com.e_commerce.miscroservice.order.entity.StoreBizOrderMemberDetail;
import org.springframework.stereotype.Component;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 17:56
 * @Copyright 玖远网络
 */
@Component
public class StoreBizOrderMemberDetailDaoImpl implements StoreBizOrderMemberDetailDao{


    /**
     * 保存
     *
     * @param orderDetail orderDetail
     * @return int
     * @author Charlie
     * @date 2018/12/11 17:58
     */
    @Override
    public int save(StoreBizOrderMemberDetail orderDetail) {
        return MybatisOperaterUtil.getInstance ().save (orderDetail);
    }

    @Override
    public StoreBizOrderMemberDetail findByOrderId(Long orderId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new StoreBizOrderMemberDetail (),
                new MybatisSqlWhereBuild (StoreBizOrderMemberDetail.class)
                        .eq (StoreBizOrderMemberDetail::getOrderId, orderId)
                        .eq (StoreBizOrderMemberDetail::getDelStatus, StateEnum.NORMAL)
        );
    }
}
