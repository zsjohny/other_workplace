package com.e_commerce.miscroservice.operate.service.user;

import com.e_commerce.miscroservice.commons.entity.distribution.DstbSystemTeamQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.AuditManagementRequest;
import com.e_commerce.miscroservice.operate.entity.request.UserInformationRequest;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/25 19:43
 * @Copyright 玖远网络
 */
public interface ShopMemberService{
    /**
     * 申请管理
     * @param auditManagementRequest
     * @return
     */
    Response auditManage(AuditManagementRequest auditManagementRequest);

    /**
     * 操作申请管理-同意-拒绝
     * @param id
     * @param status
     * @param auditExplain
     * @return
     */
    Response operateAudit(Long id, Integer status, String auditExplain);

    /**
     * 小程序用户管理
     * @param obj
     * @return
     */
    Response userInformationList(UserInformationRequest obj);

    /**
     * 小程序用户详情
     * @param id
     * @return
     */
//    Response userInformationDetail(Long id);

    /**
     * 我的团队
     * @param query
     * @return
     */
    Map<String, Object> findTeamsInformation(DstbSystemTeamQuery query);



    /**
     * 小程序用户粉丝查询
     *
     * @param query userId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/7 15:57
     */
    Map<String, Object> findFansInformation(DstbSystemTeamQuery query);



    /**
     * 小程序用户查询
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/8 10:31
     */
    Map<String, Object> findUserInformation(DstbSystemTeamQuery query);
}
