package com.e_commerce.miscroservice.operate.controller.user;

import com.e_commerce.miscroservice.commons.entity.distribution.DstbSystemTeamQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.AuditManagementRequest;
import com.e_commerce.miscroservice.operate.service.user.ShopMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 小程序会员表
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/25 19:35
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("operator/user/shopMember")
public class ShopMemberController{

    @Autowired
    private ShopMemberService shopMemberService;






    /**
     * 申请管理
     * @param commitTimeStart 提交申请 起
     * @param commitTimeEnd  提交申请 止
     * @param storeName 商店名称
     * @param id id
     * @param status //审核状态  0:待审核，1：通过 ，2：拒绝
     * @param userId 会员id
     * @param nickName 昵称
     * @param auditTimeStart 审核时间 起
     * @param auditTimeEnd 审核时间 止
     * @param pageNum 页码
     * @param pageSize 每页显示数量
     * @return "data": {
     *     "pageNum": 1,
     *     "pageSize": 10,
     *     "size": 5,
     *     "startRow": 1,
     *     "endRow": 5,
     *     "total": 5,
     *     "pages": 1,
     *     "list": [
     *       {
     *         "realName": "阿里啦咯啦咯啦咯",  //真实姓名
     *         "wxNum": "哦哦哦", // 微信昵称
     *         "applicationRole": 3, // 申请角色
     *         "beforeRole": 2, //申请前角色
     *         "phone": "15212341234", // 手艺好
     *         "commitTime": "2018-11-01 01:24:15", //提交时间
     *         "auditTime": "", //审核时间
     *         "idCard": "362330199808156640", //身份证号
     *         "id": 5, //id
     *         "userId": 158, //用户id
     *         "status": 1 //审核状态  0:待审核，1：通过 ，2：拒绝
     *         "storeName": 1 //所属上架
     *       }
     *     ]
     *   }
     */
    @RequestMapping("/audit/manage")
    public Response auditManage(
            @RequestParam(value = "commitTimeStart", required = false) String commitTimeStart,
            @RequestParam(value = "commitTimeEnd", required = false) String commitTimeEnd,
            @RequestParam(value = "storeName", required = false) String storeName,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "realName", required = false) String realName,
            @RequestParam(value = "auditTimeStart", required = false) String auditTimeStart,
            @RequestParam(value = "auditTimeEnd", required = false) String auditTimeEnd,
            Integer pageNum,
            Integer pageSize){
        AuditManagementRequest query = new AuditManagementRequest ();
        query.setCommitTimeStart (commitTimeStart);
        query.setCommitTimeEnd (commitTimeEnd);
        query.setStoreName (storeName);
        query.setId (id);
        query.setStatus (status);
        query.setRealName (realName);
        query.setUserId (userId);
        query.setNickName (nickName);
        query.setAuditTimeStart (auditTimeStart);
        query.setAuditTimeEnd (auditTimeEnd);
        query.setPageNum (pageNum);
        query.setPageSize (pageSize);
        return shopMemberService.auditManage(query);
    }

    /**
     * 操作申请管理-同意-拒绝
     * @param id 审核id
     * @param status 1同意，2拒绝
     * @param auditExplain 审核意见
     */
    @RequestMapping("/operate/audit")
    public Response operateAudit(Long id,Integer status,String auditExplain){
        return shopMemberService.operateAudit(id,status,auditExplain);
    }







    /**
     * 小程序用户查询
     *
     * @param pageSize 分页
     * @param pageNumber 分页
     * @param teamMemberId 会员id
     * @param storeBusinessName 所属商家
     * @param memberName 会员昵称
     * @param grade 角色 分销级别 (0 无等级 1 店长 2分销商 3合伙人)
     * @param superiorUserId 直属上级用户id
     * @param superiorUserName 直属上级昵称
     * @param recommendUserId 推荐人用户id
     * @param recommendUserName 推荐人昵称
     * @param registerTimeCeil 注册时间,低值
     * @param registerTimeFloor 注册时间,高值
     * @param historyCashEarningCeil 累计现金收益,低值
     * @param historyCashEarningFloor 累计现金收益,高值
     * @param historyGoldCoinEarningCeil 累计金币收益,低值
     * @param historyGoldCoinEarningFloor 累计金币收益,高值
     * @return
    "data": {
        "userList": {
        "pageNum": 1,
        "pageSize": 3,
        "size": 3,
        "startRow": 0,
        "endRow": 2,
        "total": 3,
        "pages": 1,
        "list": [
            {
            "userId": 168, //会员id
            "grade": 1, //角色 分销级别 (0 无等级 1 店长 2分销商 3合伙人)
            "userName": "桑里桑气的厨子", //会员昵称
            "teamUserCount": 0, //团队人数
            "fansUserCount": 0, //粉丝人数
            "superiorUserId": 168, //直属上级id
            "superiorUserName": "桑里桑气的厨子", //直属上级昵称
            "superiorUserGrade": 2,
            "recommendUserId": 169,//推荐人id
            "recommendUserName": "七七八八",//推荐人姓名
            "recommendUserGrade": "0", //推荐人级别 (没有则无)
            "storeBusinessName": "一直么1", //所属商家
            "historyCashEarning": 0.00, //累计现金收益
            "historyGoldCoinEarning": 0.00, //累计金币收益
            "createTimeReadable":  "2018-11-07 14:18:51" //注册时间
            }
        ],
        "prePage": 0,
        "nextPage": 0,
        "isFirstPage": true,
        "isLastPage": true,
        "hasPreviousPage": false,
        "hasNextPage": false,
        "navigatePages": 8,
        "navigatepageNums": [
        1
        ],
        "navigateFirstPage": 1,
        "navigateLastPage": 1,
        "firstPage": 1,
        "lastPage": 1
        }
    }
     * @author Charlie
     * @date 2018/11/8 10:31
     */
    @RequestMapping("user/information")
    public Response findUserInformation(
            Integer pageSize,
            Integer pageNumber,
            @RequestParam(value = "storeBusinessName", required = false) String storeBusinessName,
            @RequestParam(value = "teamMemberId", required = false) Long teamMemberId,
            @RequestParam(value = "memberName", required = false) String memberName,
            @RequestParam(value = "grade", required = false) Integer grade,
            @RequestParam(value = "recommendUserId", required = false) Long recommendUserId,
            @RequestParam(value = "recommendUserName", required = false) String recommendUserName,
            @RequestParam(value = "superiorUserId", required = false) Long superiorUserId,
            @RequestParam(value = "superiorUserName", required = false) String superiorUserName,
            @RequestParam(value = "registerTimeCeil", required = false) String registerTimeCeil,
            @RequestParam(value = "registerTimeFloor", required = false) String registerTimeFloor,
            @RequestParam(value = "historyCashEarningCeil", required = false) BigDecimal historyCashEarningCeil,
            @RequestParam(value = "historyCashEarningFloor", required = false) BigDecimal historyCashEarningFloor,
            @RequestParam(value = "historyGoldCoinEarningCeil", required = false) BigDecimal historyGoldCoinEarningCeil,
            @RequestParam(value = "historyGoldCoinEarningFloor", required = false) BigDecimal historyGoldCoinEarningFloor
    ){
        DstbSystemTeamQuery query = new DstbSystemTeamQuery ();
        query.setTeamMemberId (teamMemberId);
        query.setPageSize(pageSize);
        query.setPageNumber(pageNumber);
        query.setStoreBusinessName(storeBusinessName);
        query.setMemberName(memberName);
        query.setGrade(grade);
        query.setRecommendUserId(recommendUserId);
        query.setRecommendUserName(recommendUserName);
        query.setSuperiorUserId(superiorUserId);
        query.setSuperiorUserName(superiorUserName);
        query.setRegisterTimeCeilStr(registerTimeCeil);
        query.setRegisterTimeFloorStr(registerTimeFloor);
        query.setHistoryCashEarningCeil(historyCashEarningCeil);
        query.setHistoryCashEarningFloor(historyCashEarningFloor);
        query.setHistoryGoldCoinEarningCeil(historyGoldCoinEarningCeil);
        query.setHistoryGoldCoinEarningFloor(historyGoldCoinEarningFloor);
        return Response.success (shopMemberService.findUserInformation(query));
    }





    /**
     * 分销团队查询
     *
     * @param userId 团队管理员id
     * @param pageSize 分页
     * @param pageNumber 分页
     * @param teamMemberId 会员id
     * @param memberName 会员昵称
     * @param grade 角色 分销级别 (0 无等级 1 店长 2分销商 3合伙人)
     * @param superiorUserId 直属上级用户id
     * @param superiorUserName 直属上级昵称
     * @param superiorUserGrade 直属上级角色 (0 无等级 1 店长 2分销商 3合伙人)
     * @param registerTimeCeil 注册时间,低值
     * @param registerTimeFloor 注册时间,高值
     * @param historyCashEarningCeil 累计现金收益,低值
     * @param historyCashEarningFloor 累计现金收益,高值
     * @param historyGoldCoinEarningCeil 累计金币收益,低值
     * @param historyGoldCoinEarningFloor 累计金币收益,高值
     * @return "data": {
     *     "userDetail": {
     *       "nickName": "帝如来", //管理员昵称
     *       "storeName": "一直么1", //所属商家
     *       "id": 147,
     *       "teamUserCount": 1, //团队成员合计
     *       "grade": 2, //管理员角色
     *       "partner": 0, //合伙人数量
     *       "distribution": 0, //分销商数量
     *       "store": 1, //店长数量
     *       "common": 0, //普通数量
     *       "userId": 170, //管理员id
     *       "fansUserCount": 200,
     *       "distributorId": 169,
     *       "partnerId": 168
     *     },
     *     "userList": {
     *       "total": 1,
     *       "pages": 1,
     *       "list": [ //列表
     *         {
     *           "userId": 171, //会员ID
     *           "grade": 1, //角色 (0 无等级 1 店长 2分销商 3合伙人 )
     *           "superiorUserId": 170, //直属上级id
     *           "superiorUserName": "七七八八", //直属上级昵称
     *           "superiorUserGrade": 2, //直属上级角色(0 无等级 1 店长 2分销商 3合伙人 )
     *           "teamUserCount": 0, //团队人数
     *           "createTime": 1541572786660,
     *           "createTimeReadable": "2018-11-07 14:39:46"//注册时间
     *         }
     *       ],
     *     }
     *   }
     * @author Charlie
     * @date 2018/11/8 10:31
     */
    @RequestMapping("team/information")
    public Response findTeamsInformation(
            Long userId,
            Integer pageSize,
            Integer pageNumber,
            @RequestParam(value = "teamMemberId", required = false) Long teamMemberId,
            @RequestParam(value = "memberName", required = false) String memberName,
            @RequestParam(value = "grade", required = false) Integer grade,
            @RequestParam(value = "superiorUserId", required = false) Long superiorUserId,
            @RequestParam(value = "superiorUserName", required = false) String superiorUserName,
            @RequestParam(value = "superiorUserGrade", required = false) Integer superiorUserGrade,
            @RequestParam(value = "registerTimeCeil", required = false) String registerTimeCeil,
            @RequestParam(value = "registerTimeFloor", required = false) String registerTimeFloor,
            @RequestParam(value = "historyCashEarningCeil", required = false) BigDecimal historyCashEarningCeil,
            @RequestParam(value = "historyCashEarningFloor", required = false) BigDecimal historyCashEarningFloor,
            @RequestParam(value = "historyGoldCoinEarningCeil", required = false) BigDecimal historyGoldCoinEarningCeil,
            @RequestParam(value = "historyGoldCoinEarningFloor", required = false) BigDecimal historyGoldCoinEarningFloor
    ){
        DstbSystemTeamQuery query = new DstbSystemTeamQuery();
        query.setUserId(userId);
        query.setPageSize(pageSize);
        query.setPageNumber(pageNumber);
        query.setTeamMemberId(teamMemberId);
        query.setMemberName(memberName);
        query.setGrade(grade);
        query.setSuperiorUserId(superiorUserId);
        query.setSuperiorUserName(superiorUserName);
        query.setSuperiorUserGrade(superiorUserGrade);
        query.setRegisterTimeCeilStr(registerTimeCeil);
        query.setRegisterTimeFloorStr(registerTimeFloor);
        query.setHistoryCashEarningCeil(historyCashEarningCeil);
        query.setHistoryCashEarningFloor(historyCashEarningFloor);
        query.setHistoryGoldCoinEarningCeil(historyGoldCoinEarningCeil);
        query.setHistoryGoldCoinEarningFloor(historyGoldCoinEarningFloor);
        return Response.success (shopMemberService.findTeamsInformation(query));
    }




    /**
     * 小程序用户粉丝查询
     *
     * @param userId 团队管理员id
     * @param pageSize 分页
     * @param pageNumber 分页
     * @param teamMemberId 会员id
     * @param memberName 会员昵称
     * @param fansType 粉丝级别,1:1级粉丝,2:二级粉丝, 不传则查所有
     * @param registerTimeCeil 注册时间,低值
     * @param registerTimeFloor 注册时间,高值
     * @param historyCashEarningCeil 累计现金收益,低值
     * @param historyCashEarningFloor 累计现金收益,高值
     * @param historyGoldCoinEarningCeil 累计金币收益,低值
     * @param historyGoldCoinEarningFloor 累计金币收益,高值
     * @return "data": {
     *     "userDetail": {
     *       "nickName": "桑里桑气的厨子",  //
     *       "storeName": "一直么1", //所属商家
     *       "id": 144,
     *       "grade": 1,
     *       "userId": 168,
     *       "fansUserCount": 2, //粉丝合计人数
     *       "fans1UserCount": 1, //1级粉丝人数
     *       "fans2UserCount": 1 //2级粉丝人数
     *     },
     *     "userList": {
     *       "list": [
     *         {
     *           "userId": 169, //会员id
     *           "userName": "七七八八",  //会员昵称
     *           "fansUserCount": 0, //粉丝数量
     *           "historyCashEarning": 0.00, //累计现金收益
     *           "historyGoldCoinEarning": 0.00, //累计金币收益
     *           "createTime": 1541571531123,
     *           "createTimeReadable": "2018-11-07 14:18:51", //注册时间
     *           "whichFans": 1 //粉丝级别
     *         }
     *       ]
     *     }
     *   }
     * @author Charlie
     * @date 2018/11/8 10:31
     */
    @RequestMapping( "fans/information" )
    public Response findFansInformation( Long userId,
                                         Integer pageSize,
                                         Integer pageNumber,
                                         @RequestParam(value = "teamMemberId", required = false) Long teamMemberId,
                                         @RequestParam(value = "memberName", required = false) String memberName,
                                         @RequestParam(value = "fansType", required = false, defaultValue = "0") Integer fansType,
                                         @RequestParam(value = "registerTimeCeil", required = false) String registerTimeCeil,
                                         @RequestParam(value = "registerTimeFloor", required = false) String registerTimeFloor,
                                         @RequestParam(value = "historyCashEarningCeil", required = false) BigDecimal historyCashEarningCeil,
                                         @RequestParam(value = "historyCashEarningFloor", required = false) BigDecimal historyCashEarningFloor,
                                         @RequestParam(value = "historyGoldCoinEarningCeil", required = false) BigDecimal historyGoldCoinEarningCeil,
                                         @RequestParam(value = "historyGoldCoinEarningFloor", required = false) BigDecimal historyGoldCoinEarningFloor
    ) {
        DstbSystemTeamQuery query = new DstbSystemTeamQuery ();
        query.setUserId (userId);
        query.setPageSize(pageSize);
        query.setPageNumber(pageNumber);
        query.setTeamMemberId(teamMemberId);
        query.setMemberName(memberName);
        query.setFansType(fansType);
        query.setRegisterTimeCeilStr(registerTimeCeil);
        query.setRegisterTimeFloorStr(registerTimeFloor);
        query.setHistoryCashEarningCeil(historyCashEarningCeil);
        query.setHistoryCashEarningFloor(historyCashEarningFloor);
        query.setHistoryGoldCoinEarningCeil(historyGoldCoinEarningCeil);
        query.setHistoryGoldCoinEarningFloor(historyGoldCoinEarningFloor);
        return Response.success (shopMemberService.findFansInformation (query));
    }


}
