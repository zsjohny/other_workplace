package com.e_commerce.miscroservice.operate.controller.user;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.distribution.OperUserDstbRequest;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;
import com.e_commerce.miscroservice.operate.rpc.dstb.DstbCashOutInRpcService;
import com.e_commerce.miscroservice.operate.service.user.ShopMemberAccountCashOutInService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/18 15:39
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "operator/user/shopMemberAccountCashOutIn" )
public class ShopMemberAccountCashOutInController{

    @Autowired
    private ShopMemberAccountCashOutInService shopMemberAccountCashOutInService;

    @Autowired
    private DstbCashOutInRpcService dstbCashOutInRpcService;


    /**
     * 现金,金币 收支明细
     *
     * @param queryCashOrGoldGoin 1:查询现金,2:查询金币
     * @param userId              会员ID
     * @param userNickName        会员昵称
     * @param userDstbRoleType    分销角色 0无等级,1店长,2分销商,3合伙人
     * @param inOutType           交易类型:
     *                            1:收入,2:支出
     * @param outInContentType    交易内容:
     *                            0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账,
     *                            10.分销商的团队收益入账,11.合伙人的团队收益入账,
     *                            20.签到,21.签到阶段奖励,
     *                            30.订单取消,31.订单抵扣,
     *                            50.提现
     * @param status              资金状态   1:待结算,2:已结算
     * @param storeName           所属商家名
     * @param createTimeCeil      交易开始时间
     * @param createTimeFloor     交易结束时间
     * @param cashSettleTimeFloor 结算开始时间
     * @param cashSettleTimeCeil  结算结束时间
     * @param pageNumber          分页
     * @param pageSize            分页
     *                            "data": {
     *                            "outInPageList": {
     *                            "pageNum": 14,
     *                            "pageSize": 0,
     *                            "size": 8,
     *                            "startRow": 1,
     *                            "endRow": 8,
     *                            "total": 8,
     *                            "pages": 0,
     *                            "list": [
     *                            {
     *                            "orderNo": "4356557657992",  //订单号
     *                            "businessName": "一直么1", //商户名
     *                            "goldCoin": 32, //金币
     *                            "type": 1, //0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账,
     *                            10.分销商的团队收益入账,11.合伙人的团队收益入账,
     *                            20.签到,21.签到阶段奖励,
     *                            30.订单取消,31.订单抵扣,
     *                            50.提现
     *                            "userDstbGrade": 2, //0 无等级 1 店长 2分销商 3合伙人
     *                            "inOutType": 1, //1收入,2支出
     *                            "id": 1, //流水id
     *                            "userNickName": "施云云云云云云云", //用户昵称
     *                            "cash": 128, //现金
     *                            "memberId": 121, //用户会员id
     *                            "status": 2 //1:待结算,2:已结算
     *                            "settleTime": "2018-11-07 15:31:25" //结算时间
     *                            "orderSuccessTime": "2018-11-07 15:31:25" //订单完成时间
     *                            "payTime": "2018-11-07 15:31:25" //下单时间
     *                            }
     *                            ],
     *                            "prePage": 0,
     *                            "nextPage": 0,
     *                            "isFirstPage": false,
     *                            "isLastPage": true,
     *                            "hasPreviousPage": true,
     *                            "hasNextPage": false,
     *                            "lastPage": 0,
     *                            "firstPage": 0
     *                            },
     *                            "satistics": {
     *                            "waitInTotalGoldCoinSum": 0, //待结算总金币
     *                            "historyCashOutSum": 0, //历史提现
     *                            "aliveCashSum": 67.84, //可用现金
     *                            "waitInTotalCashSum": 0, //待结算总现金
     *                            "aliveGoldCoinSum": 16.96, //可用总金币
     *                            "totalGoldCoinSum": 16.96, //金币合计
     *                            "frozenGoldCoinSum": 0,
     *                            "historyGoldCoinOutSum": 0, //金币累计消费
     *                            "totalCashSum": 67.84, //现金合计
     *                            "frozenCashSum": 0
     *                            }
     *                            }
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/10/18 17:17
     */
    @RequestMapping( "listDetail" )
    public Response listDetail(
            @RequestParam( value = "queryCashOrGoldGoin" ) Integer queryCashOrGoldGoin,
            @RequestParam( value = "userId", required = false ) Long userId,
            @RequestParam( value = "userNickName", required = false ) String userNickName,
            @RequestParam( value = "userDstbRoleType", required = false ) Integer userDstbRoleType,
            @RequestParam( value = "inOutType", required = false ) Integer inOutType,
            @RequestParam( value = "outInContentType", required = false ) Integer outInContentType,
            @RequestParam( value = "status", required = false ) Integer status,
            @RequestParam( value = "storeName", required = false ) String storeName,
            @RequestParam( value = "createTimeCeil", required = false ) String createTimeCeil,
            @RequestParam( value = "createTimeFloor", required = false ) String createTimeFloor,
            @RequestParam( value = "cashSettleTimeFloor", required = false ) String cashSettleTimeFloor,
            @RequestParam( value = "cashSettleTimeCeil", required = false ) String cashSettleTimeCeil,
            @RequestParam( value = "pageNumber", required = false, defaultValue = "14" ) Integer pageNumber,
            @RequestParam( value = "pageSize", required = false, defaultValue = "0" ) Integer pageSize
    ) {
        OperUserDstbRequest query = new OperUserDstbRequest ();
        query.setQueryCashOrGoldGoin (queryCashOrGoldGoin);
        query.setUserId (userId);
        query.setUserNickName (userNickName);
        query.setUserDstbRoleType (userDstbRoleType);
        query.setInOutType (inOutType);
        query.setOutInContentType (outInContentType);
        query.setStatus (status);
        query.setStoreName (storeName);

        query.setCreateTimeCeilStr (createTimeCeil);
        query.setCreateTimeFloorStr (createTimeFloor);
        query.setOperTimeFloorStr (cashSettleTimeFloor);
        query.setOperTimeCeilStr (cashSettleTimeCeil);
        query.setPageNumber (pageNumber);
        query.setPageSize (pageSize);
        return Response.success (shopMemberAccountCashOutInService.listDetail (query));
    }


    /**
     * 提现详情
     *
     * @param cashOutInId 流水id
     *                    "data": {
     *                    "resultStatus": false, //交易是否成功
     *                    "balanceCash": 134.4, //余额
     *                    "inOutType": 2, //1收入2支出
     *                    "createTime": "2018-10-20 15:22:21",//提现时间
     *                    "nickName": "施云云云云云云云", //昵称
     *                    "storeName": "一直么1", //所属上架
     *                    "operMoney": 20, //提现金额
     *                    "userId": 121, //会员ID
     *                    "preOderNo": "-1", //交易编号(交易失败为-1)
     *                    "status": 2, //1:待结算,2:已结算
     *                    "operTime": "2018-10-20 15:22:22"//结算时间
     *                    }
     * @return
     * @author Charlie
     * @date 2018/10/23 11:00
     */
    @RequestMapping( "cashOutDetail" )
    public Response cashOutDetail(Long cashOutInId) {
        return Response.success (shopMemberAccountCashOutInService.cashOutDetail (cashOutInId));
    }


    /**
     * 提现审核
     *
     * @param cashOutId 流水id
     * @param isPass 1 通过, 2确认失败
     * @return 200
     * @author Charlie
     * @date 2018/10/30 20:43
     */
    @RequestMapping( "cashOutAudit" )
    public String cashOutAudit(Long cashOutId,
                               @RequestParam( value = "isPass", defaultValue = "0" ) Integer isPass, HttpServletRequest request) {
        return dstbCashOutInRpcService.cashOutAudit (cashOutId, isPass, HttpUtils.getIpAddress (request));
    }


    /**
     * 业绩统计查询
     *
     * @param payTimeCeil       支付时间(统计时间),高值
     * @param payTimeFloor      支付时间(统计时间),低值
     * @param storeName         所属商家
     * @param userId            会员ID
     * @param userName          会员昵称
     * @param operCashCeil      累计现金收益
     * @param operCashFloor     累计现金收益
     * @param operGoldCoinCeil  累计金币收益
     * @param operGoldCoinFloor 累计金币收益
     * @param pageNumber        pageNumber
     * @param pageSize          pageSize
     * @return "data": {
     * "statistics": {
     * "orderManagerGoldCoin": 32.00, //实收管理收益金币(个)
     * "orderCommissionCash": 12.80, //实收订单返佣现金(元)
     * "orderTotalMoney": 201.00, //订单成交总额(元)
     * "historyCash": 25.60, //累计现金收益(元)
     * "orderCount": 2, //订单成交总数(个)
     * "historyGoldCoin": 64.00, //累计金币收益(个)
     * "orderCommissionGoldCoin": 32.00, //实收订单返佣金币(个)
     * "orderManagerCash": 12.80 //实收管理收益现金(元)
     * },
     * "dataList": {
     * "total": 4,
     * "list": [
     * {
     * "userId": 168, //会员ID
     * "userName": "桑里桑气的厨子", //会员昵称
     * "storeName": "一直么1", //所属商家
     * "userMemberGrade": 1, //角色
     * "teamUserCount": 0, //团队人数
     * "fansUserCount": 0 //粉丝人数
     * "orderCount": 1, //订单成交总数(个)
     * "orderDealMoney": 100.00, //订单成交总额(元)
     * "totalCommissionCash": 12.80, //实收订单返佣现金(元)
     * "totalCommissionGoldCoin": 32.00, //实收订单返佣金币(个)
     * "totalManagerCash": 0.00, //实收管理收益现金(元)
     * "totalManagerGoldCoin": 0.00, //累计金币收益(个)
     * "totalCash": 12.80, //累计现金收益(元)
     * "totalGoldCoin": 32.00 //累计金币收益(个)
     * }
     * ],
     * "firstPage": 1
     * }
     * }
     * @author Charlie
     * @date 2018/11/12 17:17
     */
    @RequestMapping( "performanceList/all" )
    public Response performanceListAll(
            Integer pageNumber,
            Integer pageSize,
            @RequestParam( value = "payTimeCeil", required = false ) String payTimeCeil,
            @RequestParam( value = "payTimeFloor", required = false ) String payTimeFloor,
            @RequestParam( value = "storeName", required = false ) String storeName,
            @RequestParam( value = "userId", required = false ) Long userId,
            @RequestParam( value = "userName", required = false ) String userName,
            @RequestParam( value = "operCashCeil", required = false ) BigDecimal operCashCeil,
            @RequestParam( value = "operCashFloor", required = false ) BigDecimal operCashFloor,
            @RequestParam( value = "operGoldCoinCeil", required = false ) BigDecimal operGoldCoinCeil,
            @RequestParam( value = "operGoldCoinFloor", required = false ) BigDecimal operGoldCoinFloor
    ) {
        ShopMemAcctCashOutInQuery query = new ShopMemAcctCashOutInQuery ();
        query.setPayTimeCeilStr (payTimeCeil);
        query.setPayTimeFloorStr (payTimeFloor);
        query.setStoreName (storeName);
        query.setUserId (userId);
        query.setUserName (userName);
        query.setOperCashCeil (operCashCeil);
        query.setOperCashFloor (operCashFloor);
        query.setOperGoldCoinCeil (operGoldCoinCeil);
        query.setOperGoldCoinFloor (operGoldCoinFloor);
        query.setPageSize (pageSize);
        query.setPageNumber (pageNumber);
        return Response.success (shopMemberAccountCashOutInService.performanceListAll (query));
    }


    /**
     * 订单返佣明细查询
     *
     * @param userId          userId
     * @param payTimeCeil     payTimeCeil
     * @param payTimeFloor    payTimeFloor
     * @param status          status
     * @param userMemberId    userMemberId
     * @param userName        userName
     * @param userMemberGrade userMemberGrade
     * @param orderNo         orderNo
     * @param type            type
     * @param pageNumber      pageNumber
     * @param pageSize        pageSize
     * @return "data": {
     * "userDetail": {
     * "nickName": "桑里桑气的厨子", //会员昵称
     * "storeName": "一直么1", //所属商家
     * "userId": 168, //会员id
     * },
     * "statistics": {
     * "orderCount": 1, //订单成交总数(个)
     * "orderCommissionWaitInGoldCoin": 32.00, //实收订单返佣金币(个)
     * "orderCommissionCash": 12.80, //实收订单返佣现金(元)
     * "orderTotalMoney": 0.01, //订单成交总额(元)
     * "orderCommissionGoldCoin": 32.00, //待结算返佣金币合计(个)
     * "orderCommissionWaitInCash": 12.80 //待结算返佣现金合计(元)
     * },
     * "dataList": {
     * "list": [
     * {
     * "orderNo": "15415751955074903", //订单编号
     * "shouldInGoldCoin": 32.00, //应返佣现金(元)
     * "operGoldCoin": 32.00, //实际返佣金币
     * "payTime": "2018-11-07 15:20:57", //下单时间
     * "currencyRatio": 0.20, //返佣金币比例
     * "shouldInCash": 12.80, //应返佣现金
     * "type": 1, //0自分佣,1一级分佣,2二级分佣
     * "orderMemberUserName": "七七八八", //会员昵称
     * "payMoney": 0.01, //订单实付金额
     * "orderMemberGrade": 0, //会员角色
     * "orderSuccessTime": "", //订单完成时间
     * "orderMemberId": 169, //会员id
     * "operCash": 12.80, //实际返佣现金
     * "status": 2, //1待结算,2已结算
     * "earningsRaio": 0.16, //返佣比例
     * "operTime": "" //结算时间
     * }
     * ]
     * }
     * }
     * @author Charlie
     * @date 2018/11/13 20:34
     */
    @RequestMapping( "performanceList/commission" )
    public Response performanceListCommission(
            Long userId,
            @RequestParam( value = "payTimeCeil", required = false ) String payTimeCeil,
            @RequestParam( value = "payTimeFloor", required = false ) String payTimeFloor,
            @RequestParam( value = "status", required = false ) Integer status,
            @RequestParam( value = "userMemberId", required = false ) Long userMemberId,
            @RequestParam( value = "userName", required = false ) String userName,
            @RequestParam( value = "userMemberGrade", required = false ) Integer userMemberGrade,
            @RequestParam( value = "orderNo", required = false ) String orderNo,
            @RequestParam( value = "type", required = false ) Integer type,
            Integer pageNumber,
            Integer pageSize
    ) {
        ShopMemAcctCashOutInQuery query = new ShopMemAcctCashOutInQuery ();
        query.setUserId (userId);
        query.setPayTimeFloorStr (payTimeFloor);
        query.setPayTimeCeilStr (payTimeCeil);
        query.setStatus (status);
        query.setUserMemberId (userMemberId);
        query.setUserName (userName);
        query.setUserMemberGrade (userMemberGrade);
        query.setOrderNo (orderNo);
        query.setType (type);
        query.setPageNumber (pageNumber);
        query.setPageSize (pageSize);
        return Response.success (shopMemberAccountCashOutInService.performanceListCommission (query));
    }


    /**
     * 管理金收益明细查询
     *
     * @param userId       管理员id
     * @param payTimeCeil  统计时间
     * @param payTimeFloor 统计时间
     * @param status       状态 1待结算,1已结算
     * @param userMemberId 下单会员id
     * @param orderNo      订单号
     * @param pageNumber   pageNumber
     * @param pageSize     pageSize
     * @return "data": {
     * "statistics": {
     * "cashStatistics": { //现金汇总
     * "orderCount": 1, //订单成交数
     * "orderManagerCash": 12.80, //实收管理收益现金(元)
     * "dstbCash": 12.80, //分销商现金
     * "waitInCash": 12.80, //待结算现金
     * "orderTotalMoney": 0.01, //订单成交额
     * "partnerCash": 0.00 //合伙人现金
     * },
     * "goldCoinStatistics": { //金币汇总//
     * "orderCount": 1,
     * "orderManagerGoldCoin": 32.00,
     * "dstbGoldCoin": 32.00,
     * "partnerCashGoldCoin": 0.00,
     * "orderTotalMoney": 0.01,
     * "waitInGoldCoin": 32.00
     * }
     * },
     * "userDetail": {
     * "nickName": "桑里桑气的厨子", //团队管理员昵称
     * "storeName": "一直么1", //所属商家
     * "grade": 1, //团队管理员角色
     * "userId": 168, //团队管理员ID
     * },
     * "dataList": {
     * "list": [
     * {
     * "dstbGoldCoin": 32.00, //分销商收益金币
     * "totalManagerEarnings": 32.00, //团队订单返佣奖金(元)
     * "partnerEarningsRatio": 0.00, //合伙人管理奖比例
     * "orderNo": "15415751955074903", //订单号
     * "dstbManagerEarningsSnapshoot": 16.00, //分销商总收益
     * "dstbCash": 12.80, //分销商收益现金
     * "partnerCash": 0.00, //合伙人收益现金
     * "userName": "七七八八", //用户昵称
     * "dstbCurrencyRatio": 0.20, //分销商收益金币现金比例
     * "memberUserGrade": 0, //用户角色
     * "partnerManagerEarningsSnapshoot": 0.00, //合伙人总收益
     * "partnerGoldCoin": 0.00, //合伙人金币收益
     * "orderSuccessTime": "", //订单完成时间
     * "partnerCurrencyRatio": 0.00, //合伙人收益金币现金比例
     * "orderMemberId": 169, //下单会员id
     * "orderPayTime": "2018-11-07 15:20:57", //下单时间
     * "dstbEarningsRatio": 0.16, //分销商管理奖比例
     * "status": 1, //1待结算,2已结算
     * "operTime": "" //结算时间
     * "dstbShouldInCash": 分销商应收现金
     * "dstbShouldInGoldCoin": 分销商应收金币
     * "partnerShouldInCash": 合伙人应收现金
     * "partnerShouldInGoldCoin": 合伙人应收金币
     * }
     * ]
     * }
     * }
     * @author Charlie
     * @date 2018/11/13 20:31
     */
    @RequestMapping( "performanceList/manager" )
    public Response performanceListManager(
            Long userId,
            @RequestParam( value = "payTimeCeil", required = false ) String payTimeCeil,
            @RequestParam( value = "payTimeFloor", required = false ) String payTimeFloor,
            @RequestParam( value = "status", required = false ) Integer status,
            @RequestParam( value = "userMemberId", required = false ) Long userMemberId,
            @RequestParam( value = "orderNo", required = false ) String orderNo,
            Integer pageNumber,
            Integer pageSize
    ) {
        ShopMemAcctCashOutInQuery query = new ShopMemAcctCashOutInQuery ();
        query.setUserId (userId);
        query.setPayTimeFloorStr (payTimeFloor);
        query.setPayTimeCeilStr (payTimeCeil);
        query.setStatus (status);
        query.setUserMemberId (userMemberId);
        query.setOrderNo (orderNo);
        query.setPageNumber (pageNumber);
        query.setPageSize (pageSize);
        return Response.success (shopMemberAccountCashOutInService.performanceListManager (query));
    }


    /**
     * 团队订单返佣明细
     *
     * @param userId            userId
     * @param orderNo           orderNo
     * @param userMemberId      userMemberId
     * @param userName          userName
     * @param type              type
     * @param status            status
     * @param id                id
     * @param operCashCeil      operCashCeil
     * @param operCashFloor     operCashFloor
     * @param operGoldCoinCeil  operGoldCoinCeil
     * @param operGoldCoinFloor operGoldCoinFloor
     * @param pageNumber        pageNumber
     * @param pageSize          pageSize
     * @return "data": {
     * "orderDetail": {
     * "orderMoney": 100.00, //订单实付
     * "orderNo": "15415751955074903", // 订单编号
     * "orderSuccessTime": "2018-10-10 01-21-21" //下单时间
     * },
     * "userDetail": {
     * "nickName": "桑里桑气的厨子",
     * "storeName": "一直么1",
     * "grade": 1,
     * "userId": 168,
     * },
     * "dataList": {
     * "list": [
     * {
     * "operGoldCoin": 32.00, //实收金币
     * "userGrade": 2, //用户角色
     * "payTime": "", //下单时间
     * "currencyRatio": 0.20, //金币现金比例
     * "orderEarningsSnapshoot": 16.00 //收益
     * "earningsRatio": 0.16, //返佣比例
     * "shouldInCash": 12.80, //应收益现金
     * "shouldInGoldCoin": 12.80, //应收金币
     * "userName": "七七八八", //会员昵称
     * "type": 2, //收益类型
     * "userId": 169, //会员id
     * "orderSuccessTime": "", //订单完成时间
     * "id": 117, //收益id
     * "operCash": 12.80, //实际收益现金
     * "status": 1, //1待结算,2已结算
     * "operTime": "" //结算时间
     * }
     * ]
     * },
     * "statistics": {
     * "waitInGoldCoinEarnings": 32.00, //待结算金币收益(个)
     * "cashTotalEarnings": 12.80, //现金收益(元)
     * "cashEarnings": 0, //实发现金收益(元)
     * "teamCount": 1, //成团人数
     * "waitInCashEarnings": 12.80, //待结算现金收益(元)
     * "goldCoinEarnings": 0, //实发金币收益(个)
     * "goldCoinTotalEarnings": 32.00 //金币收益(个)
     * }
     * }
     * @author Charlie
     * @date 2018/11/13 19:21
     */
    @RequestMapping( "performanceList/teamEarnings" )
    public Response performanceListManagerTeamEarnings(
            Long userId,
            String orderNo,
            @RequestParam( value = "userMemberId", required = false ) Long userMemberId,
            @RequestParam( value = "userName", required = false ) String userName,
            @RequestParam( value = "type", required = false ) Integer type,
            @RequestParam( value = "status", required = false ) Integer status,
            @RequestParam( value = "id", required = false ) Long id,
            @RequestParam( value = "operCashCeil", required = false ) BigDecimal operCashCeil,
            @RequestParam( value = "operCashFloor", required = false ) BigDecimal operCashFloor,
            @RequestParam( value = "operGoldCoinCeil", required = false ) BigDecimal operGoldCoinCeil,
            @RequestParam( value = "operGoldCoinFloor", required = false ) BigDecimal operGoldCoinFloor,
            Integer pageNumber,
            Integer pageSize
    ) {
        ShopMemAcctCashOutInQuery query = new ShopMemAcctCashOutInQuery ();
        query.setUserId (userId);
        query.setOrderNo (orderNo);
        query.setUserMemberId (userMemberId);
        query.setUserName (userName);
        query.setType (type);
        query.setStatus (status);
        query.setId (id);
        query.setOperCashCeil (operCashCeil);
        query.setOperCashFloor (operCashFloor);
        query.setOperGoldCoinCeil (operGoldCoinCeil);
        query.setOperGoldCoinFloor (operGoldCoinFloor);
        query.setPageNumber (pageNumber);
        query.setPageSize (pageSize);
        return Response.success (shopMemberAccountCashOutInService.performanceListManagerTeamEarnings (query));
    }


    /**
     * 提现列表
     *
     * @param pageSize pageSize
     * @param pageNumber pageNumber
     * @param userName 用户昵称
     * @param storeName 商户名
     * @param createTimeCeil 用户提现时间
     * @param createTimeFloor 用户提现时间
     * @param detailStatus 1待处理,2提现成功,-2提现失败,4提现中
     * @return "data": {
     *     "dataList": {
     *       "list": [
     *         {
     *           "detailStatus": 1, //1待处理,2提现成功,-2提现失败,4提现中
     *           "UserName": "桑里桑气的厨子", //用户昵称
     *           "orderNo": "843afd4b868c435e8fc7cedaeba6f056", //订单号
     *           "createTime": "2018-11-19 09:51:55", //提现申请时间
     *           "successTime": "", //审核结算时间
     *           "storeName": "有意义", //商户名
     *           "userId": 174, //用户id
     *           "outInId": 132, //流水id
     *         }
     *       ]
     *     }
     *   }
     * @author Charlie
     * @date 2018/11/19 13:49
     */
    @RequestMapping( "listCashOut" )
    public Response listCashOut(
            Integer pageSize,
            Integer pageNumber,
            @RequestParam( value = "userName", required = false ) String userName,
            @RequestParam( value = "storeName", required = false ) String storeName,
            @RequestParam( value = "createTimeCeil", required = false ) String createTimeCeil,
            @RequestParam( value = "createTimeFloor", required = false ) String createTimeFloor,
            @RequestParam( value = "detailStatus", required = false ) Integer detailStatus
    ) {
        ShopMemAcctCashOutInQuery query = new ShopMemAcctCashOutInQuery ();
        query.setPageSize (pageSize);
        query.setPageNumber (pageNumber);
        query.setUserName (userName);
        query.setStoreName (storeName);
        query.setCreateTimeCeil (createTimeCeil);
        query.setCreateTimeFloor (createTimeFloor);
        query.setDetailStatus (detailStatus);
        return Response.success (shopMemberAccountCashOutInService.listCashOut(query));
    }
}