package com.e_commerce.miscroservice.distribution.dao;


import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DistributionSystem;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopAuditManagement;
import com.e_commerce.miscroservice.commons.entity.distribution.UnderMyClassAResponse;

import java.util.List;

public interface DistributionSystemDao {
    DistributionSystem findOne(Long userId);

    /**
     * 保存 分销体系关系
     * @param system
     */
    void save(DistributionSystem system);

    /**
     * 一级粉丝数量
     * @param userId
     * @return
     */
    Integer findClassA(Long userId);

    /**
     * 二级粉丝数量
     * @param userId
     * @return
     */
    Integer findClassB(Long userId);

    /**
     * 分销商旗下 人员数量
     * @param userId
     * @return
     */
    Integer underDistributorSize(Long userId);

    /**
     * 合伙人旗下 人员数量
     * @param userId
     * @return
     */
    Integer underPartnerSize(Long userId);

    /**
     * 我的一级粉丝 分销商数量
     * @param userId
     * @return
     */
    Integer classAUnderDistributorSize(Long userId);


    /**
     * 合伙人旗下 分销商人数
     * @param userId
     * @return
     */
    Integer findUnderPartnerDistributor(Long userId);

    /**
     * 合伙人旗下 店长人数
     * @param userId
     * @return
     */
    Integer findUnderPartnerStore(Long userId);

    /**
     * 今日添加人数
     * @param userId
     * @return
     */
    Integer findUnderPartnerCountToday(Long userId);

    /**
     * 分销商旗下 店长人数
     * @param userId
     * @return
     */
    Integer findUnderDistributorStore(Long userId);

    /**
     * 分销商旗下 今日添加人数
     * @param userId
     * @return
     */
    Integer findUnderDistributorCountToday(Long userId);

    /**
     * 今日新增粉丝数量
     * @param userId
     * @return
     */
    Integer todayIncrease(Long userId);

    /**
     * 我的一级粉丝
     * @param userId
     * @return
     */
    List<UnderMyClassAResponse> findFollowerDetails(Long userId);

    /**
     * 添加审核记录
     * @param auditManagement
     */
    void distributionProposer(ShopAuditManagement auditManagement);

    /**
     * 我的邀请人信息
     * @param userId
     * @return
     */
    UnderMyClassAResponse findHigherInformation(Long userId);

    /**
     * 查询审核
     * @param userId
     * @return
     */
    ShopAuditManagement findShopAuditManage(Long userId);

    /**
     * 更新
     * @param distributionSystem
     */
    void update(DistributionSystem distributionSystem);

    /**
     * 分销商晋升合伙人条件-旗下几个分销商
     * @param userId
     * @return
     */
    Integer findUnderDistributorDistributor(Long userId);
}
