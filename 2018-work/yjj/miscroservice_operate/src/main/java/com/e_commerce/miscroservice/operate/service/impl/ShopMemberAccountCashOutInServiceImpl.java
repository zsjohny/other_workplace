package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;
import com.e_commerce.miscroservice.commons.entity.distribution.OperUserDstbRequest;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInStatusDetailEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInTypeEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.operate.dao.ShopMemberAccountCashOutInDao;
import com.e_commerce.miscroservice.operate.dao.ShopMemberDao;
import com.e_commerce.miscroservice.operate.entity.response.UserDetailVo;
import com.e_commerce.miscroservice.operate.mapper.ShopMemberAccountCashOutInMapper;
import com.e_commerce.miscroservice.operate.mapper.ShopMemberAccountMapper;
import com.e_commerce.miscroservice.operate.mapper.ShopMemberMapper;
import com.e_commerce.miscroservice.operate.service.user.ShopMemberAccountCashOutInService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/18 15:41
 * @Copyright 玖远网络
 */
@Service
public class ShopMemberAccountCashOutInServiceImpl implements ShopMemberAccountCashOutInService{

    private Log logger = Log.getInstance (ShopMemberAccountCashOutInServiceImpl.class);

    @Autowired
    private ShopMemberDao shopMemberDao;
    @Autowired
    private ShopMemberAccountMapper shopMemberAccountMapper;
    @Autowired
    private ShopMemberAccountCashOutInMapper shopMemberAccountCashOutInMapper;
    @Autowired
    private ShopMemberAccountCashOutInDao shopMemberAccountCashOutInDao;
    @Autowired
    private ShopMemberMapper shopMemberMapper;

    /**
     * 现金收支明细
     *
     * @param query query
     * @return java.util.List<java.util.Map
                    */
    @Override
    public Map<String, Object> listDetail(OperUserDstbRequest query) {
        logger.info ("现金收支明细---查询 query={}", query);
        List<Integer> types = new ArrayList<> (2);
        //查询内容类型
        Integer outInContentType = query.getOutInContentType ();
        if (outInContentType != null) {
            types.add (outInContentType);
        }
        query.setTypes (types.isEmpty () ? null : types);

        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        //查询收支列表
        List<Map<String, Object>> outInList = shopMemberAccountCashOutInMapper.listDetail (query);
        if (! ObjectUtils.isEmpty (outInList)) {
            for (Map<String, Object> map : outInList) {

                map.put ("operTime", TimeUtils.longFormatString (Long.parseLong (map.get ("operTime").toString ())));
                map.put ("settleTime", map.get ("operTime"));
                map.put ("payTime", TimeUtils.longFormatString (Long.parseLong (map.get ("payTime").toString ())));
                map.put ("orderSuccessTime", TimeUtils.longFormatString (Long.parseLong (map.get ("orderSuccessTime").toString ())));
            }

        }
        PageInfo<Map<String, Object>> outInPageList = new PageInfo<> (outInList);
        //查询汇总
        Map<String, BigDecimal> statistics = findStatisticsOutIn (query);

        Map<String, Object> result = new HashMap<> (2);
        result.put ("dataList", outInPageList);
        result.put ("statistics", statistics);
        return result;
    }


    /**
     * 提现详情
     *
     * @param cashOutInId 流水id
     * @return java.lang.Object
     * @author Charlie
     * @date 2018/10/23 11:00
     */
    @Override
    public Map<String, Object> cashOutDetail(Long cashOutInId) {
        ShopMemberAccountCashOutIn history = shopMemberAccountCashOutInDao.findById (cashOutInId);
        if (history == null || ! CashOutInTypeEnum.CASH_OUT_TOTAL.isThis (history.getType ())) {
            return null;
        }

        Map<String, Object> userDetail = shopMemberMapper.findUserAndStoreById (history.getUserId ());
        userDetail.put ("operMoney", history.getOperCash ());
        //1:待结算,2:已结算
        userDetail.put ("status", history.getStatus ());
        //1:交易成功,2:交易失败
        userDetail.put ("resultStatus", history.getInOutType () == 2);
        userDetail.put ("inOutType", history.getInOutType ());
        userDetail.put ("preOderNo", history.getPaymentNo ());
        userDetail.put ("createTime", TimeUtils.stamp2Str (history.getCreateTime ()));
        userDetail.put ("operTime", TimeUtils.longFormatString (history.getOperTime ()));
        userDetail.put ("balanceCash", history.getBalanceCash ());
        return userDetail;
    }


    /**
     * 小程序收益业绩统计
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/12 17:42
     */
    @Override
    public Map<String, Object> performanceListAll(ShopMemAcctCashOutInQuery query) {
        logger.info ("查询小程序收益业绩统计");
        Map<String, Object> result = new HashMap<> (2);

        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        List<ShopMemAcctCashOutInQuery> userList = shopMemberAccountCashOutInMapper.findUsersByQuery (query);
        if (ObjectUtils.isEmpty (userList)) {
            //啥都没有
            result.put ("dataList", new PageInfo<> (userList));

            Map<String, Object> emptyStatistics = new HashMap<> ();
            emptyStatistics.put ("orderCount", 0);
            emptyStatistics.put ("orderTotalMoney", 0);
            emptyStatistics.put ("orderCommissionCash", 0);
            emptyStatistics.put ("orderManagerCash", 0);
            emptyStatistics.put ("orderCommissionGoldCoin", 0);
            emptyStatistics.put ("orderManagerGoldCoin", 0);
            //累计现金收益
            emptyStatistics.put ("historyCash", 0);
            //累计金币收益
            emptyStatistics.put ("historyGoldCoin", 0);
            result.put ("statistics", emptyStatistics);
            return result;
        }

        //查询到的分页
        List<Long> userIds = new ArrayList<> (userList.size ());

        //查询每个用户的团队人数,粉丝人数
        for (ShopMemAcctCashOutInQuery user : userList) {
            Long userId = user.getUserId ();
            Integer grade = user.getUserMemberGrade ();

            //团队人数
            Long teamUserCount = shopMemberDao.countMyTeamWhichRoleUser (userId, grade, null);
            user.setTeamUserCount (teamUserCount);
            //粉丝人数
            Long fansUserCount = shopMemberDao.countMyFansWhichRoleUser (userId, null);
            user.setFansUserCount (fansUserCount);

            userIds.add (userId);
        }

        //订单成交数,订单成交额
        List<Map<String, Object>> userOrderList = shopMemberAccountCashOutInMapper.findOrderTotalCountAndMoneyByUserIds (userIds);
        logger.info ("查询订单成交数,订单成交额 userSize={},查询结果集={}", userIds.size (), userOrderList.size ());
        if (userIds.size () != userOrderList.size ()) {
            logger.error ("查询用户订单成交数,订单成交额, 记录数不匹配");
        }
        for (Map<String, Object> userOrder : userOrderList) {
            Long userId = Long.parseLong (userOrder.get ("userId").toString ());
            //订单总数
            Long orderCount = Long.parseLong (userOrder.get ("orderCount").toString ());
            //订单交易额
            BigDecimal orderDealMoney = new BigDecimal (userOrder.get ("orderDealMoney").toString ());
            for (ShopMemAcctCashOutInQuery user : userList) {
                Long uid = user.getUserId ();
                if (userId.equals (uid)) {
                    user.setOrderCount (orderCount);
                    user.setOrderDealMoney (orderDealMoney);
                    break;
                }
            }
        }

        //用户分销总收益 -- 已结算总订单返佣现金,已结算总订单返佣金,已结算总订单管理现金,已结算总订单管理金币
        List<Map<String, Object>> dstbTotalEarnings = shopMemberAccountCashOutInMapper.findDstbTotalEarningsByUserIds (userIds);
        logger.info ("用户分销总收益 size={}", dstbTotalEarnings.size ());
        for (Map<String, Object> earnings : dstbTotalEarnings) {
            Long userId = Long.parseLong (earnings.get ("userId").toString ());
            //分销总现金收益
            BigDecimal totalCommissionCash = new BigDecimal (earnings.get ("totalCommissionCash").toString ());
            //分销总金币收益
            BigDecimal totalCommissionGoldCoin = new BigDecimal (earnings.get ("totalCommissionGoldCoin").toString ());
            //管理金总现金收益
            BigDecimal totalManagerCash = new BigDecimal (earnings.get ("totalManagerCash").toString ());
            //管理金总金币收益
            BigDecimal totalManagerGoldCoin = new BigDecimal (earnings.get ("totalManagerGoldCoin").toString ());
            for (ShopMemAcctCashOutInQuery user : userList) {
                Long uid = user.getUserId ();
                if (userId.equals (uid)) {
                    user.setTotalCommissionCash (totalCommissionCash);
                    user.setTotalCommissionGoldCoin (totalCommissionGoldCoin);
                    user.setTotalManagerCash (totalManagerCash);
                    user.setTotalManagerGoldCoin (totalManagerGoldCoin);
                    //现金总收益
                    user.setTotalCash (totalCommissionCash.add (totalManagerCash));
                    //金币总收益
                    user.setTotalGoldCoin (totalCommissionGoldCoin.add (totalManagerGoldCoin));
                    break;
                }
            }
        }


        //设置默认值0,最好不要getter,setter中设默认值
        for (ShopMemAcctCashOutInQuery user : userList) {
            if (user.getOrderCount() == null ){
                user.setOrderCount (0L);
            }
            if (user.getOrderDealMoney() == null ){
                user.setOrderDealMoney (BigDecimal.ZERO);
            }
            if (user.getTotalCommissionCash() == null ){
                user.setTotalCommissionCash (BigDecimal.ZERO);
            }
            if (user.getTotalCommissionGoldCoin() == null ){
                user.setTotalCommissionGoldCoin (BigDecimal.ZERO);
            }
            if (user.getTotalManagerCash() == null ){
                user.setTotalManagerCash (BigDecimal.ZERO);
            }
            if (user.getTotalManagerGoldCoin() == null ){
                user.setTotalManagerGoldCoin (BigDecimal.ZERO);
            }
            if (user.getTotalCash() == null ){
                user.setTotalCash (BigDecimal.ZERO);
            }
            if (user.getTotalGoldCoin() == null ){
                user.setTotalGoldCoin (BigDecimal.ZERO);
            }
        }


        //=================================== 汇总 ===================================
        Map<String, Object> statistics = new HashMap<> ();
        //查询没有分页的用户
        List<Long> allUserIdList = shopMemberAccountCashOutInMapper.findUserIdsByQuery (query);
        logger.info ("查询没有分页的用户 size={}", allUserIdList.size ());
        //订单信息汇总
        Map<String, Object> orderStatistics = shopMemberAccountCashOutInMapper.countOrderTotalCountAndMoneyByUserIds (allUserIdList, Arrays.asList (0, 1, 2, 10, 11), null, null);
        statistics.put ("orderCount", orderStatistics.get ("orderCount"));
        statistics.put ("orderTotalMoney", orderStatistics.get ("orderTotalMoney"));
        //收益信息汇总
        Map<String, Object> allTotalEarnings = shopMemberAccountCashOutInMapper.sumDstbTotalEarningsByUserIds (allUserIdList, Collections.singletonList (2));
        BigDecimal orderCommissionCash = new BigDecimal (allTotalEarnings.get ("totalCommissionCash").toString ());
        statistics.put ("orderCommissionCash", orderCommissionCash);
        BigDecimal orderManagerCash = new BigDecimal (allTotalEarnings.get ("totalManagerCash").toString ());
        statistics.put ("orderManagerCash", orderManagerCash);
        BigDecimal orderCommissionGoldCoin = new BigDecimal (allTotalEarnings.get ("totalCommissionGoldCoin").toString ());
        statistics.put ("orderCommissionGoldCoin", orderCommissionGoldCoin);
        BigDecimal orderManagerGoldCoin = new BigDecimal (allTotalEarnings.get ("totalManagerGoldCoin").toString ());
        statistics.put ("orderManagerGoldCoin", orderManagerGoldCoin);
        //累计现金收益
        statistics.put ("historyCash", orderCommissionCash.add (orderManagerCash));
        //累计金币收益
        statistics.put ("historyGoldCoin", orderCommissionGoldCoin.add (orderManagerGoldCoin));


        result.put ("statistics", statistics);
        result.put ("dataList", new PageInfo<> (userList));
        return result;
    }


    /**
     * 用户分销业绩订单返佣明细
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/13 11:08
     */
    @Override
    public Map<String, Object> performanceListCommission(ShopMemAcctCashOutInQuery query) {

        Map<String, Object> result = new HashMap<> (2);

        //==================================== 列表 ====================================
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        List<Map<String, Object>> dataList = shopMemberAccountCashOutInMapper.listOrderCommissionEarnings (query);
        if (ObjectUtils.isEmpty (dataList)) {
            result.put ("dataList", new PageInfo<> (dataList));
            Map<String, Object> emptyStatistics = new HashMap<> (6);
            emptyStatistics.put ("orderCount", 0);
            emptyStatistics.put ("orderTotalMoney", 0);
            emptyStatistics.put ("orderCommissionCash", 0);
            emptyStatistics.put ("orderCommissionWaitInCash", 0);
            emptyStatistics.put ("orderCommissionGoldCoin", 0);
            emptyStatistics.put ("orderCommissionWaitInGoldCoin", 0);
            result.put ("statistics", emptyStatistics);
            return result;
        }


        //格式化日期
        for (Map<String, Object> data : dataList) {
            long payTime = Long.parseLong (data.get ("payTime").toString ());
            data.put ("payTime", TimeUtils.longFormatString (payTime));
            long orderSuccessTime = Long.parseLong (data.get ("orderSuccessTime").toString ());
            data.put ("orderSuccessTime", TimeUtils.longFormatString (orderSuccessTime));
            long operTime = Long.parseLong (data.get ("operTime").toString ());
            //结算时间
            data.put ("operTime", TimeUtils.longFormatString (operTime));
        }


        //==================================== 汇总 ====================================
        //订单总数,订单交易额
        Map<String, Object> statistics = new HashMap<> (6);
        List<Integer> types;
        if (query.getType () != null) {
            //查指定
            types = Collections.singletonList (query.getType ());
        }
        else {
            //查所有分佣
            types = new ArrayList<> (3);
            types.add (0);
            types.add (1);
            types.add (2);
        }
        query.setTypeList (types);
        query.setJustGoldCoin (null);
        query.setJustCash (null);
        Map<String, Object> orderStatistics = shopMemberAccountCashOutInMapper.userOrderTotalCountAndMoney (query);
        statistics.put ("orderCount", orderStatistics.get ("orderCount"));
        statistics.put ("orderTotalMoney", orderStatistics.get ("orderTotalMoney"));


        //收益信息汇总
        //已结算
        query.setJustCash (null);
        query.setJustGoldCoin (null);
        //已结算分佣金现金, 已结算分佣金币
        query.setStatus (2);
        Map<String, Object> readyIn = shopMemberAccountCashOutInMapper.findUserCashAndGoldCoin (query);
        BigDecimal orderCommissionCash = new BigDecimal (readyIn.get ("totalCash").toString ());
        statistics.put ("orderCommissionCash", orderCommissionCash);
        BigDecimal orderCommissionGoldCoin = new BigDecimal (readyIn.get ("totalGoldCoin").toString ());
        statistics.put ("orderCommissionGoldCoin", orderCommissionGoldCoin);
        //待结算
        query.setStatus (1);
        Map<String, Object> waitIn = shopMemberAccountCashOutInMapper.findUserCashAndGoldCoin (query);
        BigDecimal orderCommissionWaitInCash = new BigDecimal (waitIn.get ("totalCash").toString ());
        statistics.put ("orderCommissionWaitInCash", orderCommissionWaitInCash);
        BigDecimal orderCommissionWaitInGoldCoin = new BigDecimal (waitIn.get ("totalGoldCoin").toString ());
        statistics.put ("orderCommissionWaitInGoldCoin", orderCommissionWaitInGoldCoin);


        //当前用户信息
        UserDetailVo userDetail = shopMemberDao.findUserDstbDetail (query.getUserId ());


        result.put ("userDetail", userDetail);
        result.put ("statistics", statistics);
        result.put ("dataList", new PageInfo<> (dataList));
        return result;
    }


    /**
     * 业绩统计,管理收益明细查询
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/13 14:44
     */
    @Override
    public Map<String, Object> performanceListManager(ShopMemAcctCashOutInQuery query) {

        Map<String, Object> result = new HashMap<> (2);

        //==================================== 列表 sql ====================================
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        List<Map<String, Object>> earningList = shopMemberAccountCashOutInMapper.listOrderManagerEarnings (query);
        if (ObjectUtils.isEmpty (earningList)) {
            //是空

            //汇总现金
            Map<String, Object> emptyCashStatistics = new HashMap<> (6);
            emptyCashStatistics.put ("orderCount", 0);
            emptyCashStatistics.put ("orderTotalMoney", 0);
            //管理金收益
            emptyCashStatistics.put ("orderManagerCash", 0);
            //分销商收益
            emptyCashStatistics.put ("dstbCash", 0);
            //合伙人收益
            emptyCashStatistics.put ("managerCash", 0);
            //待结算
            emptyCashStatistics.put ("waitInCash", 0);

            //汇总金币
            Map<String, Object> emptyGoldCoinStatistics = new HashMap<> (6);
            emptyGoldCoinStatistics.put ("orderCount", 0);
            emptyGoldCoinStatistics.put ("orderTotalMoney", 0);
            //管理金收益
            emptyGoldCoinStatistics.put ("orderManagerGoldCoin", 0);
            //分销商收益
            emptyGoldCoinStatistics.put ("dstbGoldCoin", 0);
            //合伙人收益
            emptyGoldCoinStatistics.put ("managerGoldCoin", 0);
            //待结算
            emptyGoldCoinStatistics.put ("waitInGoldCoin", 0);


            Map<String, Object> emptyStatistics = new HashMap<> (2);
            emptyStatistics.put ("cashStatistics", emptyCashStatistics);
            emptyStatistics.put ("goldCoinStatistics", emptyGoldCoinStatistics);
            result.put ("statistics", emptyStatistics);
            result.put ("dataList", new PageInfo<> (earningList));
            return result;
        }

        //格式化日期
        for (Map<String, Object> earnings : earningList) {
            earnings.put ("operTime", TimeUtils.longFormatString (Long.parseLong (earnings.get ("operTime").toString ())));
            earnings.put ("orderSuccessTime", TimeUtils.longFormatString (Long.parseLong (earnings.get ("orderSuccessTime").toString ())));
            earnings.put ("orderPayTime", TimeUtils.longFormatString (Long.parseLong (earnings.get ("orderPayTime").toString ())));
            earnings.put ("totalManagerEarnings", new BigDecimal (earnings.get ("partnerManagerEarningsSnapshoot").toString ()).add (new BigDecimal (earnings.get ("dstbManagerEarningsSnapshoot").toString ())));
        }

        //================================= 汇总 =================================

        //订单总数,订单交易额
        List<Integer> types = new ArrayList<> (3);
        types.add (10);
        types.add (11);
        query.setTypeList (types);

        //现金汇总
        Map<String, Object> cashStatistics = new HashMap<> (6);
        //只查现金
        query.setJustCash (1);
        query.setJustGoldCoin (null);
        Map<String, Object> orderStatistics = shopMemberAccountCashOutInMapper.userOrderTotalCountAndMoney (query);
        //成交订单数
        cashStatistics.put ("orderCount", orderStatistics.get ("orderCount"));
        //订单成交额
        cashStatistics.put ("orderTotalMoney", orderStatistics.get ("orderTotalMoney"));


        //金币汇总
        Map<String, Object> goldCoinStatistics = new HashMap<> (6);
        //只查金币
        query.setJustCash (null);
        query.setJustGoldCoin (1);
        orderStatistics = shopMemberAccountCashOutInMapper.userOrderTotalCountAndMoney (query);
        //成交订单数
        goldCoinStatistics.put ("orderCount", orderStatistics.get ("orderCount"));
        //订单成交额
        goldCoinStatistics.put ("orderTotalMoney", orderStatistics.get ("orderTotalMoney"));


        query.setJustCash (null);
        query.setJustGoldCoin (null);
        //已结算(分销商)管理金现金, 已结算管理金金币
        query.setStatus (2);
        query.setTypeList (Arrays.asList (10));
        Map<String, Object> readyInDstb = shopMemberAccountCashOutInMapper.findUserCashAndGoldCoin (query);
        //分销商已结算现金
        BigDecimal readyInDstbCash = new BigDecimal (readyInDstb.get ("totalCash").toString ());
        //分销商已结算金币
        BigDecimal readyInDstbGoldCoin = new BigDecimal (readyInDstb.get ("totalGoldCoin").toString ());


        //已结算(合伙人)管理金现金, 已结算管理金金币
        query.setTypeList (Arrays.asList (11));
        Map<String, Object> readyInPartner = shopMemberAccountCashOutInMapper.findUserCashAndGoldCoin (query);
        //合伙人已结算现金
        BigDecimal readyInPartnerCash = new BigDecimal (readyInPartner.get ("totalCash").toString ());
        //合伙人已结算金币
        BigDecimal readyInPartnerGoldCoin = new BigDecimal (readyInPartner.get ("totalGoldCoin").toString ());


        //待结算管理金现金, 待结算管理金金币
        query.setStatus (1);
        query.setTypeList (Arrays.asList (10, 11));
        Map<String, Object> waitIn = shopMemberAccountCashOutInMapper.findUserCashAndGoldCoin (query);
        //待结算管理金现金
        BigDecimal waitInCash = new BigDecimal (waitIn.get ("totalCash").toString ());
        //待结算管理金金币
        BigDecimal waitInGoldCoin = new BigDecimal (waitIn.get ("totalGoldCoin").toString ());


        //现金汇总
        //管理金收益
        cashStatistics.put ("orderManagerCash", readyInDstbCash.add (readyInPartnerCash));
        //分销商收益
        cashStatistics.put ("dstbCash", readyInDstbCash);
        //合伙人收益
        cashStatistics.put ("partnerCash", readyInPartnerCash);
        //待结算
        cashStatistics.put ("waitInCash", waitInCash);


        //汇总金币
        //管理金收益
        goldCoinStatistics.put ("orderManagerGoldCoin", readyInPartnerGoldCoin.add (readyInDstbGoldCoin));
        //分销商收益
        goldCoinStatistics.put ("dstbGoldCoin", readyInDstbGoldCoin);
        //合伙人收益
        goldCoinStatistics.put ("partnerCashGoldCoin", readyInPartnerGoldCoin);
        //待结算
        goldCoinStatistics.put ("waitInGoldCoin", waitInGoldCoin);


        UserDetailVo userDetail = shopMemberDao.findUserDstbDetail (query.getUserId ());


        Map<String, Object> statistics = new HashMap<> (2);
        statistics.put ("cashStatistics", cashStatistics);
        statistics.put ("goldCoinStatistics", goldCoinStatistics);

        result.put ("statistics", statistics);
        result.put ("dataList", new PageInfo<> (earningList));
        result.put ("userDetail", userDetail);
        return result;
    }


    /**
     * 团队订单返佣明细
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/13 19:21
     */
    @Override
    public Map<String, Object> performanceListManagerTeamEarnings(ShopMemAcctCashOutInQuery query) {

        Map<String, Object> result = new HashMap<> (2);

        UserDetailVo userDetail = shopMemberDao.findUserDstbDetail (query.getUserId ());
        ErrorHelper.declareNull (userDetail, "没有用户信息");
        result.put ("userDetail", userDetail);

        ShopMemberOrderDstbRecord record = shopMemberAccountCashOutInDao.findOrderRecord (query.getOrderNo ());
        ErrorHelper.declareNull (record, "没有订单信息");
        Map<String, Object> orderDetail = new HashMap<> (2);
        orderDetail.put ("orderNo", record.getOrderNo ());
        orderDetail.put ("orderMoney", record.getOrderMoney ());
        orderDetail.put ("orderSuccessTime", TimeUtils.longFormatString (record.getPayTime ()));
        result.put ("orderDetail", orderDetail);

        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        query.setSourceId (query.getUserId ());
        //=========================================== 查询列表sql ===========================================
        List<Map<String, Object>> teamOrderCmsList = shopMemberAccountCashOutInMapper.teamOrderCommission (query);
        if (ObjectUtils.isEmpty (teamOrderCmsList)) {
            //啥都没有
            result.put ("dataList", new PageInfo<> (teamOrderCmsList));
            Map<String, Object> empty = new HashMap<> (2);
            empty.put ("teamCount", 0);
            //现金收益
            empty.put ("cashTotalEarnings", 0);
            //实发现金收益
            empty.put ("cashEarnings", 0);
            //待结算
            empty.put ("waitInCashEarnings", 0);
            //金币收益
            empty.put ("goldCoinTotalEarnings", 0);
            //实发金币收益
            empty.put ("goldCoinEarnings", 0);
            //待结算
            empty.put ("waitInGoldCoinEarnings", 0);
            result.put ("statistics", empty);
            return result;
        }


        //格式化时间
        for (Map<String, Object> data : teamOrderCmsList) {
            data.put ("payTime", TimeUtils.longFormatString (Long.parseLong (data.get ("operTime").toString ())));
            data.put ("orderSuccessTime", TimeUtils.longFormatString (Long.parseLong (data.get ("operTime").toString ())));
            data.put ("operTime", TimeUtils.longFormatString (Long.parseLong (data.get ("operTime").toString ())));
        }

        result.put ("dataList", new PageInfo<> (teamOrderCmsList));

        //=========================================== 汇总 ===========================================
        Map<Object, Object> statistics = new HashMap<> (8);
        //总人数
        statistics.put ("teamCount", teamOrderCmsList.size ());

        //实发现金收益
        BigDecimal cashEarnings = BigDecimal.ZERO;
        //待结算
        BigDecimal waitInCashEarnings = BigDecimal.ZERO;
        //实发金币收益
        BigDecimal goldCoinEarnings = BigDecimal.ZERO;
        //待结算
        BigDecimal waitInGoldCoinEarnings = BigDecimal.ZERO;
        for (Map<String, Object> data : teamOrderCmsList) {
            int status = Integer.parseInt (data.get ("status").toString ());
            BigDecimal operCash = new BigDecimal (data.get ("operCash").toString ());
            BigDecimal operGoldCoin = new BigDecimal (data.get ("operGoldCoin").toString ());
            if (status == 1) {
                //待发
                waitInCashEarnings = waitInCashEarnings.add (operCash);
                waitInGoldCoinEarnings = waitInGoldCoinEarnings.add (operGoldCoin);
            }
            else if (status == 2) {
                //已发
                cashEarnings = cashEarnings.add (operCash);
                goldCoinEarnings = goldCoinEarnings.add (operGoldCoin);
            }
            else {
                //ignore
            }
        }
        statistics.put ("cashEarnings", cashEarnings);
        statistics.put ("waitInCashEarnings", waitInCashEarnings);
        statistics.put ("goldCoinEarnings", goldCoinEarnings);
        statistics.put ("waitInGoldCoinEarnings", waitInGoldCoinEarnings);
        //现金收益 = 实发现金收益+待结算
        statistics.put ("cashTotalEarnings", cashEarnings.add (waitInCashEarnings));
        //金币收益 = 实发金币收益+待结算
        statistics.put ("goldCoinTotalEarnings", goldCoinEarnings.add (waitInGoldCoinEarnings));

        result.put ("statistics", statistics);

        return result;
    }





    /**
     * 提现列表
     *
     * @param query query
     * @return com.github.pagehelper.PageInfo<com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery>
     * @author Charlie
     * @date 2018/11/18 10:24
     */
    @Override
    public Map<String, Object> listCashOut(ShopMemAcctCashOutInQuery query) {
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        List<Map<String, Object>> rows = shopMemberAccountCashOutInMapper.listCashOut (query);
        //格式化日期,审核状态
        for (Map<String, Object> data : rows) {
            data.put ("createTime", TimeUtils.stamp2Str (((Timestamp) data.get ("createTime"))));
            data.put ("successTime", TimeUtils.longFormatString (Long.parseLong (data.get ("successTime").toString ())));
        }
        return Collections.singletonMap ("dataList", new PageInfo<> (rows));
    }





    /**
     * 汇总
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/10/22 20:05
     */
    private Map<String, BigDecimal> findStatisticsOutIn(OperUserDstbRequest query) {
        boolean noNeedStatistics = BeanKit.notNull (query.getOutInContentType ())
                || BeanKit.notNull (query.getInOutType ())
                || BeanKit.notNull (query.getInOutType ())
                || BeanKit.notNull (query.getCashSettleTimeCeil ())
                || BeanKit.notNull (query.getCashSettleTimeFloor ())
                || BeanKit.notNull (query.getCreateTimeCeil ())
                || BeanKit.notNull (query.getCreateTimeFloor ());

        if (noNeedStatistics) {
            return null;
        }
        Map<String, BigDecimal> statistics = null;
        logger.info ("查询汇总");
        List<Long> userIds = shopMemberAccountMapper.findUserIds (query);
        logger.info ("按条件查询用户id={}", userIds);
        if (! ObjectUtils.isEmpty (userIds)) {
            statistics = shopMemberAccountMapper.satisticsByUserId (userIds);
            BigDecimal waitInTotalGoldCoinSum = statistics.get ("waitInTotalGoldCoinSum");
            BigDecimal waitInTotalCashSum = statistics.get ("waitInTotalCashSum");
            BigDecimal frozenGoldCoinSum = statistics.get ("frozenGoldCoinSum");
            BigDecimal frozenCashSum = statistics.get ("frozenCashSum");
            BigDecimal aliveCashSum = statistics.get ("aliveCashSum");
            BigDecimal aliveGoldCoinSum = statistics.get ("aliveGoldCoinSum");


            //总=待结待入账+可用
            BigDecimal totalCashSum = waitInTotalCashSum.add (aliveCashSum);
            BigDecimal totalGoldCoinSum = waitInTotalGoldCoinSum.add (aliveGoldCoinSum);
            statistics.put ("totalCashSum", totalCashSum);
            statistics.put ("totalGoldCoinSum", totalGoldCoinSum);
        }
        return statistics;
    }


    /**
     * 是否需要汇总
     *
     * @param query query
     * @return boolean
     * @author Charlie
     * @date 2018/10/20 15:44
     */
    private boolean isNeedStatistics(OperUserDstbRequest query) {
        if (BeanKit.hasNull (query)) {
            return Boolean.FALSE;
        }

        if (
                BeanKit.notNull (query.getOutInContentType ())
                        || BeanKit.notNull (query.getInOutType ())
                        || BeanKit.notNull (query.getInOutType ())
                        || BeanKit.notNull (query.getCashSettleTimeCeil ())
                        || BeanKit.notNull (query.getCashSettleTimeFloor ())
                        || BeanKit.notNull (query.getCreateTimeCeil ())
                        || BeanKit.notNull (query.getCreateTimeFloor ())
                ) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
