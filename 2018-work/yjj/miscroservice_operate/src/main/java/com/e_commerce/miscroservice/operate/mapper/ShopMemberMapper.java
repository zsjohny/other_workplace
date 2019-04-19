package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.Member;
import com.e_commerce.miscroservice.commons.entity.distribution.DstbSystemTeamQuery;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopAuditManagementResponse;
import com.e_commerce.miscroservice.commons.entity.distribution.UserInformationResponse;
import com.e_commerce.miscroservice.commons.entity.user.DstbUserVo;
import com.e_commerce.miscroservice.operate.entity.request.AuditManagementRequest;
import com.e_commerce.miscroservice.operate.entity.request.UserInformationRequest;
import com.e_commerce.miscroservice.operate.entity.response.UserDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Create by hyf on 2018/10/18
 */

@Mapper
public interface ShopMemberMapper{
    /**
     * 申请管理
     * @param auditManagementRequest
     * @return
     */
    List<Map<String, Object>> auditManage(@Param("auditManagementRequest") AuditManagementRequest auditManagementRequest);

    /**
     * 小程序用户管理
     *
     * @param obj
     * @return
     */
    List<UserInformationResponse> userInformationList(@Param("obj") UserInformationRequest obj);


    /**
     * 查询用户分销信息
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.operate.entity.response.UserDetailVo
     * @author Charlie
     * @date 2018/11/7 15:30
     */
    UserDetailVo findUserDstbDetail(@Param("userId") Long userId);

    /**
     * 根据用户id查找用户和门店信息(这里用map返回, 以后要什么自己往里面加吧)
     *
     * @param userId
     * @return
     */
    Map<String, Object> findUserAndStoreById(@Param("userId")Long userId);
    /**
     * 更新数据库中所有 上级分销商 合伙人
     * @param sunList
     * @param gradeCondition
     * @param userId
     */
    void updateAllSunDistributorOrPartner(@Param("sunList") List<Long> sunList, @Param("gradeCondition") Integer gradeCondition, @Param("userId") Long userId);



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
    Member findMemberById(@Param("memberId") Long memberId);
}
