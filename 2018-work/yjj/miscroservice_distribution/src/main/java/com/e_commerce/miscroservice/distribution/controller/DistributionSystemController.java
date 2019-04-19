package com.e_commerce.miscroservice.distribution.controller;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.Iptools;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.distribution.entity.WhiteList;
import com.e_commerce.miscroservice.distribution.service.DistributionSystemService;
import com.e_commerce.miscroservice.distribution.service.WhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分销体系Controller
 */
@RestController
@RequestMapping( "/distribution" )
public class DistributionSystemController{


    private Log logger = Log.getInstance(DistributionSystemController.class);

    @Autowired
    private WhiteListService whiteListService;
    @Autowired
    private DistributionSystemService distributionSystem;

    /**
     * 我的团队信息
     *
     * @param userId id
     *               <p>
     *               {
     *               "msg": "",
     *               "code": 200,
     *               "data": {
     *               "countDistributor": 0, 分销商
     *               "countStore": 0, 店长
     *               "count": 3, 总数
     *               "countToday": 0 今日新增
     *               }
     *               }
     * @return
     */
    @RequestMapping( "/count/team" )
    public Response findCountTeam(Long userId) {
        JSONObject jsonObject = distributionSystem.findCountTeam (userId);
        if (jsonObject == null) {
            Response.errorMsg ("查询失败");
        }
        return Response.success (jsonObject);
    }

    /**
     * 粉丝-我的粉丝
     *
     * @param userId id
     *               <p>
     *               {
     *               "msg": "",
     *               "code": 200,
     *               "data": {
     *               "countClassA": 2, 一级粉丝
     *               "countClassB": 1, 二级粉丝
     *               "todayIncrease": 0,  今日新增
     *               "count": 3 总粉丝
     *               }
     *               }
     * @return
     */
    @RequestMapping( "/count/follower" )
    public Response findCountFollower(Long userId) {
        return Response.success (distributionSystem.findCountFollower (userId));
    }

    /**
     * 粉丝- 一级粉丝明细
     *
     * @param userId id
     * @param page   页码
     *               <p>
     *               {
     *               "msg": "",
     *               "code": 200,
     *               "data": [
     *               {
     *               "nickName": "humiking", 微信名
     *               "img": "https://wx.qlogo.cn/mmopen/vi_32/tmvkfYWdUrMTgYSkFFnrzNEE9ox5y9tbmOiczAwT9WOrkERudkVys4mPxPiarLpnS1yQV5KPcFu0xqgHVVia1vK9g/0", 头像
     *               "time": "2018-10-17 19:44:54" 时间
     *               },
     *               {
     *               "nickName": "only one",
     *               "img": "https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJWhXiafCWiblLpRZURSWmWrMf0nf0O60d0TTZWhrZUomWWlCa7HOF2icqefrDOFJ9CRbgS8tSFJ7R1A/0",
     *               "time": "2018-10-17 19:44:54"
     *               }
     *               ]
     *               }
     * @return
     */
    @RequestMapping( "/follower/details" )
    public Response findFollowerDetails(Long userId, Integer page) {
        return distributionSystem.findFollowerDetails (userId, page);
    }

    /**
     * 权益-晋升条件
     *
     * @param userId id
     *               <p>
     *               成为分销商条件
     *               {
     *               "grade": 1, 等级0 普通 1店长 2分销商 3 合伙人
     *               "explain": 审核理由
     *               "status": 审核状态-1未发起审核0:待审核，1：通过，2：拒绝
     *               "isTrue": true, 是否满足条件
     *               "condition": { 条件
     *               "classA": 200, 一级粉丝人数
     *               "classAB": 500, 一二级粉丝总数
     *               "countMoney": 100000 当月团队 销售额
     *               },
     *               "done": { 达成状况
     *               "classA": 200,
     *               "classAB": 500,
     *               "countMoney": 100000
     *               }
     *               }
     *               成为合伙人条件
     *               {
     *               "grade": 2, 等级
     *               "isTrue": true, 是否满足条件
     *               "condition": { 条件
     *               "countMoney": 100000, 当月团队 销售额
     *               "distributor": 5 分销商人数
     *               },
     *               "done": { 达成状况
     *               "countMoney": 100000,
     *               "distributor": 5
     *               }
     *               }
     *               成为店长条件
     *               {
     *               "grade": 0,  等级
     *               "isTrue": true,  是否满足条件
     *               "condition": {
     *               "buyCount": 1, 购买数量
     *               "classA": 200 一级粉丝数量
     *               },
     *               "done": {
     *               "buyCount": 1,
     *               "classA": 200
     *               }
     *               }
     * @return
     */
    @RequestMapping( "/promote/condition" )
    public Response promoteCondition(Long userId) {
        return Response.success (distributionSystem.promoteCondition (userId));
    }


    /**
     * 权益-申请
     *
     * @param userId id
     *               <p>
     *               {
     *               "msg": "",
     *               "code": 200,
     *               "data": {}
     *               }
     * @return
     */
    @RequestMapping( "/distribution/proposer" )
    public Response distributionProposer(Long userId, String realName, String wxNum, String phone, String idCard) {
        return distributionSystem.distributionProposer (userId, realName, wxNum, phone, idCard);
    }


    /**
     * 分销广告位
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/14 18:18
     */
    @RequestMapping( "/my/informationAd" )
    public Response myInformationAd(Long userId) {
        return Response.success (distributionSystem.myInformationAd (userId));
    }



    /**
     * 我的个人中心
     *
     * @param userId id
     *               <p>
     *               {
     *               "msg": "",
     *               "code": 200,
     *               "data": {
     *               "grade": 0,等级,0普通,1店长,2分销商,3合伙人
     *               "userIncomeStatistics": {
     *               "todayCoin": 1,  今日金额
     *               "wait": 1, 待结算
     *               "already": 1, 已结算 可用金额 可提现
     *               "count": 200, 总金额
     *               "goldCash": 1 金币
     *               },
     *               "higherInformation": {
     *               "nickName":"nickName", 昵称
     *               "img":"img", 头像
     *               "time":"time"   时间
     *               },
     *               "countFollower": {
     *               "countClassA": 2, 一级粉丝
     *               "countClassB": 1, 二级粉丝
     *               "todayIncrease": 0,  今日新增
     *               "count": 3 总粉丝
     *               },
     *               "countTeam": {
     *               "countDistributor": 0, 分销商
     *               "countStore": 0, 店长
     *               "count": 3, 总数
     *               "countToday": 0 今日新增
     *               }
     *               }
     *               }
     * @return
     */
    @RequestMapping( "/my/information" )
    public Response myInformation(Long userId) {
        return Response.success (distributionSystem.myInformation (userId));
    }


    /**
     * 绑定粉丝
     *
     * @param userId 用户id
     * @param fans   粉丝id
     * @return
     */
    @RequestMapping( "/binding/fans" )
    public Response bindingFans(Long userId, Long fans) {
        return distributionSystem.bindingFans (userId, fans);
    }



    /**
     * 操作白名单
     *
     * @param type 白名单类型
     * @param targetId 目标id
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/14 15:45
     */
    @RequestMapping( "operWhiteList" )
    private Response operWhiteList(
            @RequestParam( "targetId" ) Long targetId,
            @RequestParam( value = "type" , defaultValue = "1") Integer type,
            @RequestParam( value = "note" , required = false) String note,
            @RequestParam( value = "oper" , defaultValue = "add") String oper
    ) {
        WhiteList whiteList = new WhiteList ();
        whiteList.setTargetId (targetId);
        whiteList.setType (type);
        whiteList.setNote (note);
        if ("add".equals (oper)) {
            logger.info ("加入白名单 ip={},type={},targetId={}", Iptools.gainRealIp (), type, targetId);
            whiteListService.add (whiteList);
        }
        else if ("del".equals (oper)) {
            logger.info ("删除白名单 ip={},type={},targetId={}", Iptools.gainRealIp (), type, targetId);
            whiteListService.del (whiteList);
        }
        else {
            return Response.error ("未知操作类型");
        }
        return Response.success ();
    }


    /**
     * 是否展示分析页面
     *
     * @param type 白名单类型
     * @param targetId 目标id
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/14 15:45
     */
    @RequestMapping( "checkAuth" )
    private Response checkAuth(
            @RequestParam( "targetId" ) Long targetId,
            @RequestParam( value = "type" , defaultValue = "1") Integer type
    ) {
        return Response.success (whiteListService.checkAuth (type, targetId));
    }






}
