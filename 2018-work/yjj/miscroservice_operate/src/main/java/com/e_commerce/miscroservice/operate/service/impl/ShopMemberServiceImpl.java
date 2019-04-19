package com.e_commerce.miscroservice.operate.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DistributionSystem;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopAuditManagement;
import com.e_commerce.miscroservice.commons.entity.distribution.DstbSystemTeamQuery;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopAuditManagementResponse;
import com.e_commerce.miscroservice.commons.entity.distribution.UserInformationResponse;
import com.e_commerce.miscroservice.commons.entity.user.DstbUserVo;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.DistributionGradeEnum;
import com.e_commerce.miscroservice.commons.helper.current.ExecutorService;
import com.e_commerce.miscroservice.commons.helper.current.ExecutorTask;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.commons.utils.DistributionSystemUtil;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.operate.dao.ShopMemberDao;
import com.e_commerce.miscroservice.operate.entity.request.AuditManagementRequest;
import com.e_commerce.miscroservice.operate.entity.request.UserInformationRequest;
import com.e_commerce.miscroservice.operate.entity.response.UserDetailVo;
import com.e_commerce.miscroservice.operate.service.user.ShopMemberService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/25 19:44
 * @Copyright 玖远网络
 */
@Service
public class ShopMemberServiceImpl implements ShopMemberService{
    private Log logger = Log.getInstance (ShopMemberServiceImpl.class);
    @Autowired
    private ShopMemberDao shopMemberDao;
    @Autowired
    private DistributionSystemUtil distributionSystemUtil;
    @Autowired
    @Qualifier( "userStrHashRedisTemplate" )
    private ValueOperations<String, String> userStrHashRedisTemplate;
    /**
     * 升级条件
     */
    private static String UPGRADE_CONDITION = "distribution:upgrade:%s";

    /**
     * 申请管理
     *
     * @param auditManagementRequest
     * @return
     */
    @Override
    public Response auditManage(AuditManagementRequest auditManagementRequest) {
        List<Map<String, Object>> list = shopMemberDao.auditManage (auditManagementRequest);
        return Response.success (new PageInfo<> (list));
    }

    private static ExecutorService updateManage = new ExecutorService (Runtime.getRuntime ().availableProcessors () << 1, "updateManage");

    /**
     * 操作申请管理-同意-拒绝
     *
     * @param id
     * @param status
     * @param auditExplain
     * @return
     */
    @Override
    public Response operateAudit(Long id, Integer status, String auditExplain) {
        logger.info ("操作申请管理id={},status={}", id, status);
        shopMemberDao.operateAudit (id, status, auditExplain);
        if (1 == status) {
            ShopAuditManagement shopAuditManagement = shopMemberDao.findById (id);
            if (shopAuditManagement == null) {
                logger.warn ("未发起申请");
                return Response.errorMsg ("未发起申请");
            }
            String saveGrade = userStrHashRedisTemplate.get (String.format (UPGRADE_CONDITION, shopAuditManagement.getUserId ()));

            if (saveGrade == null) {
                logger.warn ("申请条件不满足");
                return Response.errorMsg ("申请条件不满足");
            }
            Integer gradeCondition = Integer.parseInt (saveGrade);

            distributionSystemUtil.update (shopAuditManagement.getUserId (), gradeCondition);
            DistributionSystem distributionSystem = new DistributionSystem ();
            distributionSystem.setUserId (shopAuditManagement.getUserId ());
            distributionSystem.setGrade (gradeCondition);
            if (gradeCondition.equals (DistributionGradeEnum.DISTRIBUTOR.getCode ())) {
                distributionSystem.setDistributorId (shopAuditManagement.getUserId ());
            }
            if (gradeCondition.equals (DistributionGradeEnum.PARTNER.getCode ())) {
                distributionSystem.setDistributorId (shopAuditManagement.getUserId ());
                distributionSystem.setPartnerId (shopAuditManagement.getUserId ());
            }
            shopMemberDao.updateDistribution (distributionSystem);
            updateManage.addTask (new ExecutorTask (){
                @Override
                public void doJob() {
                    logger.info ("刷新 下级的 上级合伙人 或者 上级分销商");
                    JSONArray sunList = distributionSystemUtil.getAllSunListByUserAndGrade (shopAuditManagement.getUserId (), gradeCondition);
                    //更新数据库中所有 上级分销商 合伙人
                    shopMemberDao.updateAllSunDistributorOrPartner (sunList.toJavaList (Long.class), gradeCondition, shopAuditManagement.getUserId ());
                }
            });
        }
        return Response.success ();
    }


    /**
     * 小程序用户管理
     *
     * @param obj
     * @return
     */
    @Override
    public Response userInformationList(UserInformationRequest obj) {
        List<UserInformationResponse> list = shopMemberDao.userInformationList (obj);
//        list.forEach(
//                userInformationResponse -> {
//            userInformationResponse.getHigherUp()
//        });
        return Response.success (list);
    }


    /**
     * 我的团队
     *
     * @param query query
     * @author Charlie
     */
    @Override
    public Map<String, Object> findTeamsInformation(DstbSystemTeamQuery query) {
        Map<String, Object> result = new HashMap<> (2);

        Long userId = query.getUserId ();
        UserDetailVo userDetail = shopMemberDao.findUserDstbDetail (userId);
        logger.info ("查询小程序用户团队 userDetail");
        Integer grade = userDetail.getGrade ();
        //是否有team
        boolean hasSelfTeam = grade > 1;
        if (! hasSelfTeam) {
            //没有team,统计都是0
            userDetail.setPartner (0L);
            userDetail.setDistribution (0L);
            userDetail.setStore (0L);
            userDetail.setCommon (0L);
            //团队人数没有
            userDetail.setTeamUserCount (0L);
            result.put ("userDetail", userDetail);
            result.put ("dataList", null);
            return result;
        }

        //有team, 汇总信息
        Long common = shopMemberDao.countMyTeamWhichRoleUser (userId, grade, DistributionGradeEnum.COMMON.getCode ());
        Long store = shopMemberDao.countMyTeamWhichRoleUser (userId, grade, DistributionGradeEnum.STORE.getCode ());
        Long distribution = shopMemberDao.countMyTeamWhichRoleUser (userId, grade, DistributionGradeEnum.DISTRIBUTOR.getCode ());
        Long partner = shopMemberDao.countMyTeamWhichRoleUser (userId, grade, DistributionGradeEnum.PARTNER.getCode ());
        userDetail.setPartner (partner);
        userDetail.setDistribution (distribution);
        userDetail.setStore (store);
        userDetail.setCommon (common);
        userDetail.setTeamUserCount (common + store + distribution + partner);
        if (userDetail.getTeamUserCount () == 0) {
            result.put ("userDetail", userDetail);
            result.put ("dataList", null);
            return result;
        }


        //查询team用户列表
        //分销商昵称
        Long distributorUserId = userDetail.getDistributorId ();
        String distributorUserName = EmptyEnum.string;
        if (BeanKit.gt0 (distributorUserId)) {
            UserDetailVo dstbUser = shopMemberDao.findUserDstbDetail (distributorUserId);
            distributorUserName = dstbUser == null ? EmptyEnum.string : dstbUser.getNickName ();
        }

        //合伙人昵称
        Long partnerUserId = userDetail.getPartnerId ();
        String partnerUserName = EmptyEnum.string;
        if (BeanKit.gt0 (distributorUserId)) {
            UserDetailVo partnerUser = shopMemberDao.findUserDstbDetail (partnerUserId);
            partnerUserName = partnerUser == null ? EmptyEnum.string : partnerUser.getNickName ();
        }

        query.setPartnerUserName (partnerUserName);
        query.setDistributorUserName (distributorUserName);
        //sql查询
        List<DstbUserVo> teamVoList = shopMemberDao.listDstbTeam (query);


        if (! ObjectUtils.isEmpty (teamVoList)) {
            for (DstbUserVo teamVo : teamVoList) {
                //格式化日期
                teamVo.setCreateTimeReadable (TimeUtils.longFormatString (teamVo.getCreateTime ()));
                //统计人数
                Long myTeamAllUserCount = shopMemberDao.countMyTeamWhichRoleUser (teamVo.getUserId (), teamVo.getGrade (), null);
                teamVo.setTeamUserCount (myTeamAllUserCount);
            }
        }
        result.put ("dataList", new PageInfo<> (teamVoList));
        result.put ("userDetail", userDetail);
        return result;
    }


    /**
     * 小程序用户粉丝查询
     *
     * @param query userId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/7 15:57
     */
    @Override
    public Map<String, Object> findFansInformation(DstbSystemTeamQuery query) {
        Long userId = query.getUserId ();
        //用户信息
        UserDetailVo userDetail = shopMemberDao.findUserDstbDetail (userId);
        logger.info ("查询小程序用户粉丝 userDetail");

        //汇总信息
        Long fans1Count = shopMemberDao.countMyFansWhichRoleUser (userId, 1);
        Long fans2Count = shopMemberDao.countMyFansWhichRoleUser (userId, 2);
        userDetail.setFans1UserCount (fans1Count);
        userDetail.setFans2UserCount (fans2Count);
        userDetail.setFansUserCount (fans1Count + fans2Count);

        //没有粉丝就直接返回了
        if (userDetail.getFansUserCount () == 0) {
            Map<String, Object> result = new HashMap<> (2);
            result.put ("userDetail", userDetail);
            result.put ("dataList", null);
            return result;
        }


        //sql查询
        //初始化查询参数,pageHelp好像有bug,先这样搞
        query.setTopUserId (query.getUserId ());
        query.setHigherUserId (query.getUserId ());
        List<DstbUserVo> fansVoList = shopMemberDao.listDstbFans (query);


        if (! ObjectUtils.isEmpty (fansVoList)) {
            for (DstbUserVo fan : fansVoList) {
                //格式化日期
                fan.setCreateTimeReadable (TimeUtils.longFormatString (fan.getCreateTime ()));
                //统计粉丝人数
                Long myFansAllUserCount = shopMemberDao.countMyFansWhichRoleUser (fan.getUserId (), null);
                fan.setFansUserCount (myFansAllUserCount);
            }
        }

        Map<String, Object> result = new HashMap<> (2);
        result.put ("dataList", new PageInfo<> (fansVoList));
        result.put ("userDetail", userDetail);
        return result;
    }


    /**
     * 小程序用户查询
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/8 10:31
     */
    @Override
    public Map<String, Object> findUserInformation(DstbSystemTeamQuery query) {
        //sql查询
        List<DstbUserVo> dstbUserVos = shopMemberDao.listDstbUser (query);

        if (! ObjectUtils.isEmpty (dstbUserVos)) {
            for (DstbUserVo userVo : dstbUserVos) {
                //日期格式化
                userVo.setCreateTimeReadable (TimeUtils.longFormatString (userVo.getCreateTime ()));
                Long userId = userVo.getUserId ();
                Integer grade = userVo.getGrade ();
                //团队人数
                Long teamUserCount = shopMemberDao.countMyTeamWhichRoleUser (userId, grade, null);
                userVo.setTeamUserCount (teamUserCount);
                //粉丝人数
                Long fansUserCount = shopMemberDao.countMyFansWhichRoleUser (userId, null);
                userVo.setFansUserCount (fansUserCount);
            }
        }

        Map<String, Object> result = new HashMap<> (2);
        result.put ("dataList", new PageInfo<> (dstbUserVos));
        return result;
    }


}
