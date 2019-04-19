package com.e_commerce.miscroservice.store.dao.impl;


import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.store.dao.StoreRefundOrderActionLogDao;
import com.e_commerce.miscroservice.store.entity.emuns.RefundOrderActionLogEnum;
import com.e_commerce.miscroservice.store.entity.vo.StoreRefundOrderActionLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 售后列表
 */
@Repository
public class StoreRefundOrderActionLogDaoImpl implements StoreRefundOrderActionLogDao {

    @Override
    public void addActionLog(RefundOrderActionLogEnum refundOrderActionLogEnum, Long id) {
        long time = System.currentTimeMillis();
        StoreRefundOrderActionLog log = new StoreRefundOrderActionLog();
        log.setActionName(refundOrderActionLogEnum.getDesc());
        log.setActionTime(time);
        log.setRefundOrderId(id);
        MybatisOperaterUtil.getInstance().save(log);
    }

    /**
     * 查询售后日志
     *
     * @param refundOrderId
     * @return
     */
    @Override
    public List<StoreRefundOrderActionLog> getRefundOrderActionLogList(Long refundOrderId) {
        return MybatisOperaterUtil.getInstance().finAll(new StoreRefundOrderActionLog(), new MybatisSqlWhereBuild(StoreRefundOrderActionLog.class).eq(StoreRefundOrderActionLog::getId,refundOrderId));
    }
}
