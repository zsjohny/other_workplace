package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DistributionSystem;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopAuditManagement;
import com.e_commerce.miscroservice.commons.entity.application.user.Member;
import com.e_commerce.miscroservice.commons.entity.distribution.DstbSystemTeamQuery;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopAuditManagementResponse;
import com.e_commerce.miscroservice.commons.entity.distribution.UserInformationResponse;
import com.e_commerce.miscroservice.commons.entity.user.DstbUserVo;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.operate.mapper.ShopMemberMapper;
import com.e_commerce.miscroservice.operate.dao.ShopMemberDao;
import com.e_commerce.miscroservice.operate.entity.request.AuditManagementRequest;
import com.e_commerce.miscroservice.operate.entity.request.UserInformationRequest;
import com.e_commerce.miscroservice.operate.entity.response.UserDetailVo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/18 13:46
 * @Copyright 玖远网络
 */
@Repository
public class ShopMemberDaoImpl implements ShopMemberDao{
    @Resource
    private ShopMemberMapper shopMemberMapper;

    /**
     * 申请管理
     *
     * @param auditManagementRequest
     * @return
     */
    @Override
    public List<Map<String, Object>> auditManage(AuditManagementRequest auditManagementRequest) {
        PageHelper.startPage (auditManagementRequest.getPageNum (), auditManagementRequest.getPageSize ());
        List<Map<String, Object>> dataList = shopMemberMapper.auditManage (auditManagementRequest);
        //格式化日期
        for (Map<String, Object> data : dataList) {
            //默认值是 '/'  @_@ 线上,做个兼容,先这样处理
            String commitTime = data.get ("commitTime").toString ();
            Long commitTimeLong = 0L;
            if (commitTime != null && commitTime.length () > 5) {
                commitTimeLong = Long.parseLong (commitTime);
            }
            data.put ("commitTime", TimeUtils.longFormatString (commitTimeLong));

            String auditTime = data.get ("auditTime").toString ();
            Long auditTimeLong = 0L;
            if (auditTime != null && auditTime.length () > 5) {
                auditTimeLong = Long.parseLong (auditTime);
            }
            data.put ("auditTime", TimeUtils.longFormatString (auditTimeLong));
        }

        return dataList;
    }

    /**
     * 操作申请管理-同意-拒绝
     *
     * @param id
     * @param status
     * @param auditExplain
     * @return
     */
    @Override
    public void operateAudit(Long id, Integer status, String auditExplain) {
        ShopAuditManagement shopAuditManagement = new ShopAuditManagement ();
        if (auditExplain != null) {
            shopAuditManagement.setAuditExplain (auditExplain);
        }
        shopAuditManagement.setAuditTime (System.currentTimeMillis ()+"");
        shopAuditManagement.setStatus (status);
        MybatisOperaterUtil.getInstance ().update (shopAuditManagement, new MybatisSqlWhereBuild (ShopAuditManagement.class).eq (ShopAuditManagement::getId, id));
    }

    /**
     * 小程序用户管理
     *
     * @param obj
     * @return
     */
    @Override
    public List<UserInformationResponse> userInformationList(UserInformationRequest obj) {
        PageHelper.startPage (obj.getPageNum (), obj.getPageSize ());
        List<UserInformationResponse> userInformationList = shopMemberMapper.userInformationList (obj);
        return userInformationList;
    }

    /**
     * 查询用户分销信息
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.operate.entity.response.UserDetailVo
     * @author Charlie
     * @date 2018/11/7 15:30
     */
    @Override
    public UserDetailVo findUserDstbDetail(Long userId) {
        return shopMemberMapper.findUserDstbDetail (userId);
    }


    /**
     * 我{userId}的身份是{grade}, 查询{whichRoleInMyTeam}人数
     * <p>
     * 比如, 我的team的合伙人人数,我的team的分销商人数,我的team的店长人数,我的team的普通粉丝人数
     * 如果code == null, 则查询所有
     * </p>
     *
     * @param userId 用户id
     * @param grade  用户的角色
     * @param whichRoleInMyTeam   whichRoleInMyTeam,
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/11/7 19:48
     */
    @Override
    public Long countMyTeamWhichRoleUser(Long userId, Integer grade, Integer whichRoleInMyTeam) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild (DistributionSystem.class);
        mybatisSqlWhereBuild.eq (DistributionSystem::getDelStatus, StateEnum.NORMAL);
        //用户的领导者角色
        if (grade == null) {
            return 0L;
        }
        else if (grade < 2) {
            return 0L;
        }
        else if (grade == 2) {
            mybatisSqlWhereBuild.eq (DistributionSystem::getDistributorId, userId);
        }
        else if (grade == 3) {
            mybatisSqlWhereBuild.eq (DistributionSystem::getPartnerId, userId);
        }
        else {
            throw new IllegalArgumentException ("未知的用户分销角色 grade=" + grade);
        }
        //查询用户的下属级别
        if (whichRoleInMyTeam != null) {
            mybatisSqlWhereBuild.eq (DistributionSystem::getGrade, whichRoleInMyTeam);
        }
        return MybatisOperaterUtil.getInstance ().count (mybatisSqlWhereBuild);
    }


    @Override
    public ShopAuditManagement findById(Long id) {
        return MybatisOperaterUtil.getInstance ().findOne (new ShopAuditManagement (), new MybatisSqlWhereBuild (ShopAuditManagement.class).eq (ShopAuditManagement::getId, id).eq (ShopAuditManagement::getDelStatus, StateEnum.NORMAL));
    }

    @Override
    public void updateDistribution(DistributionSystem distributionSystem) {
        MybatisOperaterUtil.getInstance ().update (distributionSystem, new MybatisSqlWhereBuild (DistributionSystem.class).eq (DistributionSystem::getUserId, distributionSystem.getUserId ()));
    }

    /**
     * 更新数据库中所有 上级分销商 合伙人
     *
     * @param sunList
     * @param gradeCondition
     * @param userId
     */
    @Override
    public void updateAllSunDistributorOrPartner(List<Long> sunList, Integer gradeCondition, Long userId) {
        shopMemberMapper.updateAllSunDistributorOrPartner (sunList, gradeCondition, userId);
    }


    /**
     * 分销团队
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.user.DstbUserTeamVo>
     * @author Charlie
     * @date 2018/11/7 19:31
     */
    @Override
    public List<DstbUserVo> listDstbTeam(DstbSystemTeamQuery query) {
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        return shopMemberMapper.listDstbTeam (query);
    }






    /**
     * 该用户的 {whichFansInMyTeam}级粉丝人数
     *
     * @param userId userId
     * @param whichFansInMyTeam whichFansInMyTeam
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/11/8 9:18
     */
    @Override
    public Long countMyFansWhichRoleUser(Long userId,Integer whichFansInMyTeam) {
        MybatisSqlWhereBuild where = new MybatisSqlWhereBuild (DistributionSystem.class);
        where.eq (DistributionSystem::getDelStatus, StateEnum.NORMAL);

        if (whichFansInMyTeam == null) {
            where.groupBefore ()
                    .eq (DistributionSystem::getTopUp, userId)
                    .or ()
                    .eq (DistributionSystem::getHigherUp, userId)
                    .groupAfter ();
        }
        else if (whichFansInMyTeam == 1) {
            where.eq (DistributionSystem::getHigherUp, userId);
        }
        else if (whichFansInMyTeam == 2) {
            where.eq (DistributionSystem::getTopUp, userId);
        }
        else {
            throw new IllegalArgumentException ("未知的粉丝类型 whichFansInMyTeam=" + whichFansInMyTeam);
        }
        return MybatisOperaterUtil.getInstance ().count (where);
    }






    /**
     * 用户粉丝列表
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.user.DstbUserTeamVo>
     * @author Charlie
     * @date 2018/11/7 19:31
     */
    @Override
    public List<DstbUserVo> listDstbFans(DstbSystemTeamQuery query) {
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        return shopMemberMapper.listDstbFans(query);
    }



    /**
     * 小程序用户查询
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/8 10:31
     */
    @Override
    public List<DstbUserVo> listDstbUser(DstbSystemTeamQuery query) {
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        return shopMemberMapper.listDstbUser (query);
    }

    @Override
    public Member findMemberById(Long memberId) {
        return shopMemberMapper.findMemberById(memberId);
    }
}
