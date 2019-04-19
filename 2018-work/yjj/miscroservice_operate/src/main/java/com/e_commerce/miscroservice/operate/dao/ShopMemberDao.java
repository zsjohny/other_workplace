package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DistributionSystem;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopAuditManagement;
import com.e_commerce.miscroservice.commons.entity.application.user.Member;
import com.e_commerce.miscroservice.commons.entity.distribution.DstbSystemTeamQuery;
import com.e_commerce.miscroservice.commons.entity.distribution.UserInformationResponse;
import com.e_commerce.miscroservice.commons.entity.user.DstbUserVo;
import com.e_commerce.miscroservice.operate.entity.request.AuditManagementRequest;
import com.e_commerce.miscroservice.operate.entity.request.UserInformationRequest;
import com.e_commerce.miscroservice.operate.entity.response.UserDetailVo;

import java.util.List;
import java.util.Map;

/**
 * Create by hyf on 2018/10/18
 */
public interface ShopMemberDao{
    /**
     * 申请管理
     * @param auditManagementRequest
     * @return
     */
    List<Map<String, Object>> auditManage(AuditManagementRequest auditManagementRequest);

    /**
     * 操作申请管理-同意-拒绝
     * @param id
     * @param status
     * @param auditExplain
     * @return
     */
    void operateAudit(Long id, Integer status, String auditExplain);

    /**
     * 小程序用户管理
     *
     * @param obj
     * @return
     */
    List<UserInformationResponse> userInformationList(UserInformationRequest obj);


    /**
     * 查询用户分销信息
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.operate.entity.response.UserDetailVo
     * @author Charlie
     * @date 2018/11/7 15:30
     */
    UserDetailVo findUserDstbDetail(Long userId);



    /**
     * 我{userId}的身份是{grade}, 查询{code}人数
     * <p>
     * 比如, 我的team的合伙人人数,我的team的分销商人数,我的team的店长人数,我的team的普通粉丝人数
     * </p>
     *
     * @param userId 用户id
     * @param grade  用户的角色
     * @param whichRoleInMyTeam   whichRoleInMyTeam
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/11/7 19:48
     */
    Long countMyTeamWhichRoleUser(Long userId, Integer grade, Integer whichRoleInMyTeam);

    ShopAuditManagement findById(Long id);

    /**
     * 更新
     * @param distributionSystem
     */
    void updateDistribution(DistributionSystem distributionSystem);

    /**
     * 更新数据库中所有 上级分销商 合伙人
     * @param sunList
     * @param gradeCondition
     * @param userId
     */
    void updateAllSunDistributorOrPartner(List<Long> sunList, Integer gradeCondition, Long userId);


    /**
     * 分销团队
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.user.DstbUserTeamVo>
     * @author Charlie
     * @date 2018/11/7 19:31
     */
    List<DstbUserVo> listDstbTeam(DstbSystemTeamQuery query);



    /**
     * 该用户的 {whichFansInMyTeam}级粉丝人数
     *
     * @param userId userId
     * @param whichFansInMyTeam whichFansInMyTeam
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/11/8 9:18
     */
    Long countMyFansWhichRoleUser(Long userId, Integer whichFansInMyTeam);



    /**
     * 用户粉丝列表
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.user.DstbUserTeamVo>
     * @author Charlie
     * @date 2018/11/7 19:31
     */
    List<DstbUserVo> listDstbFans(DstbSystemTeamQuery query);


    /**
     * 小程序用户查询
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/8 10:31
     */
    List<DstbUserVo> listDstbUser(DstbSystemTeamQuery query);

    /**
     * 查询用户
     * @param memberId
     * @return
     */
    Member findMemberById(Long memberId);
}
