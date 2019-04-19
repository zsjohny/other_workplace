package com.e_commerce.miscroservice.publicaccount.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerAudit;
import com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.publicaccount.dao.ProxyCustomerAuditDao;
import com.e_commerce.miscroservice.publicaccount.entity.enums.ProxyCustomerAuditStatus;
import com.e_commerce.miscroservice.publicaccount.mapper.ProxyCustomerAuditMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 11:03
 * @Copyright 玖远网络
 */
@Component
public class ProxyCustomerAuditDaoImpl implements ProxyCustomerAuditDao{

    @Autowired
    private ProxyCustomerAuditMapper proxyCustomerAuditMapper;

    /**
     * 审核
     *
     * @param status     审核状态
     * @param auditId    auditId
     * @param msg        审核意见
     * @param operUserId 审核人
     * @return int
     * @author Charlie
     * @date 2018/9/22 12:50
     */
    @Override
    public int audit(ProxyCustomerAuditStatus status, Long auditId, String msg, Long operUserId) {
        ProxyCustomerAudit updateInfo = new ProxyCustomerAudit ();
        updateInfo.setAuditStatus (status.getCode ());
        updateInfo.setAuditMsg (msg);
        updateInfo.setUpdateUserId (operUserId);
        return MybatisOperaterUtil.getInstance ().update (
                updateInfo,
                new MybatisSqlWhereBuild (ProxyCustomerAudit.class)
                        .eq (ProxyCustomerAudit::getId, auditId)
                        .eq (ProxyCustomerAudit::getDelStatus, StateEnum.NORMAL)
        );
    }


    /**
     * 查询一个审核
     *
     * @param auditId auditId
     * @return com.e_commerce.miscroservice.publicaccount.po.proxy.customer.ProxyCustomerAudit
     * @author Charlie
     * @date 2018/9/22 13:25
     */
    @Override
    public ProxyCustomerAudit selectById(Long auditId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ProxyCustomerAudit (),
                new MybatisSqlWhereBuild (ProxyCustomerAudit.class)
                        .eq (ProxyCustomerAudit::getId, auditId)
                        .eq (ProxyCustomerAudit::getDelStatus, StateEnum.NORMAL)
        );
    }


    /**
     * 代理商申请列表
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery>
     * @author Charlie
     * @date 2018/10/8 6:56
     */
    @Override
    public List<ProxyCustomerAuditQuery> auditList(ProxyCustomerAuditQuery query) {
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        List<ProxyCustomerAuditQuery> result = proxyCustomerAuditMapper.auditList (query);
        if (! ObjectUtils.isEmpty (result)) {
            for (ProxyCustomerAuditQuery audit : result) {
                audit.setCreateTimeReadable (TimeUtils.stamp2Str (audit.getCreateTime ()));
            }
        }
        return result;
    }


    /**
     * 找最近的一个审核未通过或者失败的申请
     *
     * @param userId userId
     * @return boolean
     * @author Charlie
     * @date 2018/10/22 19:21
     */
    @Override
    @SuppressWarnings ("unchecked")
    public ProxyCustomerAudit recentlyUnCheckOrFailedAudit(Long userId) {
        //审核中的代理
        ProxyCustomerAudit processAudit = MybatisOperaterUtil.getInstance ().findOne (
                new ProxyCustomerAudit (),
                new MybatisSqlWhereBuild (ProxyCustomerAudit.class)
                        .eq (ProxyCustomerAudit::getAuditStatus, 1)
                        .eq (ProxyCustomerAudit::getRecommonUserId, userId)
                        .eq (ProxyCustomerAudit::getType, 1)
                        .eq (ProxyCustomerAudit::getDelStatus, StateEnum.NORMAL)
        );
        if (processAudit != null) {
            return processAudit;
        }

        //最近的,如果是失败则返回
        ProxyCustomerAudit recentlyAudit = MybatisOperaterUtil.getInstance ().findOne (
                new ProxyCustomerAudit (),
                new MybatisSqlWhereBuild (ProxyCustomerAudit.class)
                        .eq (ProxyCustomerAudit::getRecommonUserId, userId)
                        .eq (ProxyCustomerAudit::getDelStatus, StateEnum.NORMAL)
                        .orderBy (MybatisSqlWhereBuild.ORDER.DESC, ProxyCustomerAudit::getCreateTime)
        );
        if (recentlyAudit != null && recentlyAudit.getAuditStatus () == 2) {
            return recentlyAudit;
        }
        return null;
    }
}
