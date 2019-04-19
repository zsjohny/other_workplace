package com.e_commerce.miscroservice.distribution.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DistributionSystem;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopAuditManagement;
import com.e_commerce.miscroservice.commons.entity.distribution.UnderMyClassAResponse;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.DistributionGradeEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.distribution.dao.DistributionSystemDao;
import com.e_commerce.miscroservice.distribution.mapper.DistributionSystemMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class DistributionSystemDaoImpl implements DistributionSystemDao {

    @Resource
    private DistributionSystemMapper distributionSystemMapper;

    @Override
    public DistributionSystem findOne(Long userId) {

        DistributionSystem salesEntity =  MybatisOperaterUtil.getInstance().findOne(new DistributionSystem(),
                new MybatisSqlWhereBuild(DistributionSystem.class)
                        .eq(DistributionSystem::getUserId,userId)
                        .eq(DistributionSystem::getDelStatus, StateEnum.NORMAL)
        );
        return salesEntity;
    }

    @Override
    public void save(DistributionSystem system) {
        MybatisOperaterUtil.getInstance().save(system);
    }
    /**
     * -- 我的一级粉丝数量
     * SELECT COUNT(higher_up) from yjj_distribution_system where higher_up = '121';
     * -- 我的二级粉丝数量
     * SELECT COUNT(top_up) from yjj_distribution_system where top_up = '120';
     * -- 分销商旗下 人员数量
     * SELECT COUNT(distributor_id) from yjj_distribution_system where distributor_id = '120' and distributor_id!=user_id;
     * -- 合伙人旗下 人员数量
     * SELECT COUNT(partner_id) from yjj_distribution_system where partner_id = '120' and partner_id!=user_id;
     * -- 我的一级粉丝 分销商数量
     * SELECT COUNT(higher_up) from yjj_distribution_system where higher_up = '131' and distributor_id = higher_up;
     */
    /**
     * 一级粉丝数量
     *
     * @param userId
     * @return
     */
    @Override
    public Integer findClassA(Long userId) {
        Long size = MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(DistributionSystem.class)
                        .eq(DistributionSystem::getHigherUp,userId)
                        .eq(DistributionSystem::getDelStatus, StateEnum.NORMAL)
        );
        return size.intValue();
    }


    /**
     * 二级粉丝数量
     *
     * @param userId
     * @return
     */
    @Override
    public Integer findClassB(Long userId) {
        Long size = MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(DistributionSystem.class)
                        .eq(DistributionSystem::getTopUp,userId)
                        .eq(DistributionSystem::getDelStatus, StateEnum.NORMAL)
        );
        return size.intValue();
    }

    /**
     * 分销商旗下 人员数量
     *
     * @param userId
     * @return
     */
    @Override
    public Integer underDistributorSize(Long userId) {
        Long size = MybatisOperaterUtil.getInstance()
                .count(
                        new MybatisSqlWhereBuild(DistributionSystem.class)
                                .eq(DistributionSystem::getDistributorId,userId)
                                .neq(DistributionSystem::getUserId,userId)
                                .eq(DistributionSystem::getDelStatus, StateEnum.NORMAL)
                );
        return size.intValue();
    }

    /**
     * 合伙人旗下 人员数量
     *
     * @param userId
     * @return
     */
    @Override
    public Integer underPartnerSize(Long userId) {
        Long size = MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(DistributionSystem.class)
                        .eq(DistributionSystem::getPartnerId,userId)
                        .neq(DistributionSystem::getUserId,userId)
                        .eq(DistributionSystem::getDelStatus, StateEnum.NORMAL)

        );
        return size.intValue();
    }

    /**
     * 我的一级粉丝 分销商数量
     *
     * @param userId
     * @return
     */
    @Override
    public Integer classAUnderDistributorSize(Long userId) {
        Long size = MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(DistributionSystem.class)
                        .eq(DistributionSystem::getHigherUp,userId)
                        .eq(DistributionSystem::getDistributorId,userId)
                        .eq(DistributionSystem::getDelStatus, StateEnum.NORMAL)
        );
        return size.intValue();
    }

    /**
     * 合伙人旗下 分销商人数
     *
     * @param userId
     * @return
     */
    @Override
    public Integer findUnderPartnerDistributor(Long userId) {
        Long size = MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(DistributionSystem.class)
                        .eq(DistributionSystem::getPartnerId,userId)
                        .eq(DistributionSystem::getGrade, DistributionGradeEnum.DISTRIBUTOR.getCode())
                        .eq(DistributionSystem::getDelStatus, StateEnum.NORMAL)
                        .neq(DistributionSystem::getUserId,userId)
        );
        return size.intValue();
    }

    /**
     * 合伙人旗下 店长人数
     *
     * @param userId
     * @return
     */
    @Override
    public Integer findUnderPartnerStore(Long userId) {
        Long size = MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(DistributionSystem.class)
                        .eq(DistributionSystem::getPartnerId,userId)
                        .eq(DistributionSystem::getGrade, DistributionGradeEnum.STORE.getCode())
                        .eq(DistributionSystem::getDelStatus, StateEnum.NORMAL)
        );
        return size.intValue();
    }

    /**
     * 今日添加人数
     *
     * @param userId
     * @return
     */
    @Override
    public Integer findUnderPartnerCountToday(Long userId) {
        return distributionSystemMapper.findUnderPartnerCountToday(userId);

    }

    /**
     * 分销商旗下 店长人数
     *
     * @param userId
     * @return
     */
    @Override
    public Integer findUnderDistributorStore(Long userId) {
        Long size = MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(DistributionSystem.class)
                        .eq(DistributionSystem::getDistributorId,userId)
                        .eq(DistributionSystem::getGrade, DistributionGradeEnum.STORE.getCode())
                        .eq(DistributionSystem::getDelStatus, StateEnum.NORMAL)
        );
        return size.intValue();
    }

    /**
     * 分销商旗下 今日添加人数
     *
     * @param userId
     * @return
     */
    @Override
    public Integer findUnderDistributorCountToday(Long userId) {
        return distributionSystemMapper.findUnderDistributorCountToday(userId);
    }

    /**
     * 今日新增粉丝数量
     *
     * @param userId
     * @return
     */
    @Override
    public Integer todayIncrease(Long userId) {
        return distributionSystemMapper.todayIncrease(userId);
    }

    /**
     * 我的一级粉丝
     *
     * @param userId
     * @return
     */
    @Override
    public List<UnderMyClassAResponse> findFollowerDetails(Long userId) {
//        List<DistributionSystem> list = MybatisOperaterUtil.getInstance().finAll(
//                new DistributionSystem(),
//                new MybatisSqlWhereBuild(DistributionSystem.class)
//                        .eq(DistributionSystem::getHigherUp,userId)
//                        .page(page,10)
//                        .orderBy(MybatisSqlWhereBuild.ORDER.DESC,DistributionSystem::getCreateTime)
//        );
        return distributionSystemMapper.findFollowerDetails(userId);
    }
    /**
     * 添加审核记录
     * @param auditManagement
     */
    @Override
    public void distributionProposer(ShopAuditManagement auditManagement) {
        MybatisOperaterUtil.getInstance().save(auditManagement);
    }

    @Override
    public UnderMyClassAResponse findHigherInformation(Long userId) {
        return distributionSystemMapper.findHigherInformation(userId);
    }

    @Override
    public ShopAuditManagement findShopAuditManage(Long userId) {
        return MybatisOperaterUtil.getInstance().findOne(
                new ShopAuditManagement(),new MybatisSqlWhereBuild(ShopAuditManagement.class).
                        eq(ShopAuditManagement::getUserId,userId)
                        .eq(ShopAuditManagement::getDelStatus,StateEnum.NORMAL).orderBy(MybatisSqlWhereBuild.ORDER.DESC,ShopAuditManagement::getCreateTime)
        );
    }

    @Override
    public void update(DistributionSystem distributionSystem) {
        MybatisOperaterUtil.getInstance().update(distributionSystem,new MybatisSqlWhereBuild(DistributionSystem.class).eq(DistributionSystem::getId,distributionSystem.getId()));
    }
    /**
     * 分销商晋升合伙人条件-旗下几个分销商
     * @param userId
     * @return
     */
    @Override
    public Integer findUnderDistributorDistributor(Long userId) {
        Long size = MybatisOperaterUtil.getInstance().count(
                new MybatisSqlWhereBuild(DistributionSystem.class)
                        .eq(DistributionSystem::getHigherUp,userId)
                        .eq(DistributionSystem::getGrade, DistributionGradeEnum.DISTRIBUTOR.getCode())
                        .eq(DistributionSystem::getDelStatus, StateEnum.NORMAL)
                        .neq(DistributionSystem::getUserId,userId)
        );
        return size.intValue();
    }
}
