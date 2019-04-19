package com.e_commerce.miscroservice.distribution.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccount;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;
import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.order.TeamOrder;
import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopMemberVo;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.entity.distribution.CashOutInUpdVo;
import com.e_commerce.miscroservice.commons.entity.distribution.DstbCashOutInVo;
import com.e_commerce.miscroservice.commons.entity.distribution.EarningsVo;
import com.e_commerce.miscroservice.commons.entity.distribution.ShopMemAcctCashOutInQuery;
import com.e_commerce.miscroservice.commons.entity.order.CountTeamOrderMoneyCoinVo;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import com.e_commerce.miscroservice.commons.entity.order.OrderItemSkuVo;
import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderVo;
import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import com.e_commerce.miscroservice.commons.entity.user.StoreWxaVo;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInStatusDetailEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInStatusEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInTypeEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInUpdEnum;
import com.e_commerce.miscroservice.commons.enums.system.DataDictionaryEnums;
import com.e_commerce.miscroservice.commons.enums.user.ShopMembAcctCashOutInTypeEnum;
import com.e_commerce.miscroservice.commons.enums.user.StoreBillEnums;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.*;
import com.e_commerce.miscroservice.commons.utils.wx.PaymentUtils;
import com.e_commerce.miscroservice.distribution.dao.DistributionSystemDao;
import com.e_commerce.miscroservice.distribution.dao.ShopMemberAccountCashOutInDao;
import com.e_commerce.miscroservice.distribution.dao.ShopMemberAccountDao;
import com.e_commerce.miscroservice.distribution.dao.ShopMemberOrderDstbRecordDao;
import com.e_commerce.miscroservice.distribution.rpc.order.ShopMemberOrderRpcService;
import com.e_commerce.miscroservice.distribution.rpc.user.ShopMemberRpcService;
import com.e_commerce.miscroservice.distribution.rpc.user.StoreBusinessAccountRpcService;
import com.e_commerce.miscroservice.distribution.rpc.user.WxaPayConfigRpcService;
import com.e_commerce.miscroservice.distribution.service.*;
import com.e_commerce.miscroservice.distribution.utils.StringUtils;
import com.e_commerce.miscroservice.distribution.utils.WithdrawWhiteBackListUtil;
import com.e_commerce.miscroservice.distribution.vo.AccountDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 16:34
 * @Copyright 玖远网络
 */
@Service
public class ShopMemberAccountServiceImpl implements ShopMemberAccountService {


    private static final String GRADE = "grade";
    private static final String PARTNER = "partner";
    private static final String DISTRIBUTOR = "distributor";
    private static final String HIGHER = "higher";
    private static final String TOPER = "top";
    private static final String CLASS_A = "classA";
    private static final String BUY_COUNT = "buyCount";
    private static final String CER_PATH = "cert/apiclient_cert.p12";
    private static String UPGRADE_CONDITION = "distribution:upgrade:%s";
    /**
     * 与微信交互的,微信提现服务地址
     */
    private static final String WX_CASH_OUT_SERVER_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    private Log logger = Log.getInstance(ShopMemberAccountServiceImpl.class);

    @Autowired
    private WxaPayConfigRpcService wxaPayConfigRpcService;
    @Autowired
    private ShopMemberRpcService shopMemberRpcService;
    @Autowired
    private DistributionSystemUtil distributionSystemUtil;
    @Autowired
    private ShopMemberOrderRpcService shopMemberOrderRpcService;
    @Autowired
    private DstbEarningsStrategyService dstbEarningsStrategyService;
    @Autowired
    private ShopMemberAccountDao shopMemberAccountDao;
    @Autowired
    private ShopMemberAccountCashOutInService shopMemberAccountCashOutInService;
    @Autowired
    private DataDictionaryService dataDictionaryRpcService;

    @Autowired
    private ShopMemberAccountService shopMemberAccountService;
    @Autowired
    private ShopMemberAccountCashOutInDao shopMemberAccountCashOutInDao;

    @Autowired
    private ShopMemberOrderDstbRecordDao shopMemberOrderDstbRecordDao;
    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private DistributionSystemDao distributionSystemDao;

    @Autowired
    private DistributionSystemService distributionSystemService;

    @Autowired
    private StoreBusinessAccountRpcService storeBusinessAccountRpcService;

    /**
     * 添加分销账户流水记录
     *
     * @param vo vo
     * @author Charlie
     * @date 2018/10/9 13:18
     */
    public DstbCashOutInVo addDstbCashOutIn(ShopMemberOrderVo vo) {
        ErrorHelper.declare(!BeanKit.hasNull(vo, vo.getMemberId(), vo.getRealPay(), vo.getStoreId()), "请求参数不合法,为空");
        logger.info("添加分销账户流水记录--orderNo={},userId={},storeId={},实际支付={}", vo.getOrderNo(), vo.getMemberId(), vo.getStoreId(), vo.getRealPay());

        //订单金额0元
        if (vo.getRealPay().compareTo(BigDecimal.ZERO) <= 0) {
            logger.info("添加分销账户流水记录--订单金额<=0,不需要计算分销");
            return null;
        }

        //没有分销收益规则
        List<DstbEarningsStrategy> strategyList = dstbEarningsStrategyService.findAll();
        if (ObjectUtils.isEmpty(strategyList)) {
            logger.info("添加分销账户流水记录--门店用户没有设置分销收益规则 storeId:{}", vo.getStoreId());
            return null;
        }

        //用户不在分销体系中
        JSONObject selfUserDstb = distributionSystemUtil.find(vo.getMemberId());
        if (selfUserDstb == null || BeanKit.hasNull(selfUserDstb.getInteger(GRADE))) {
            logger.info("添加分销账户流水记录--用户没有分销身份");
            return null;
        }
        selfUserDstb.remove("sonList");
        logger.info("添加分销账户流水记录--用户分销身份userId={},selfUserDstb={}", vo.getMemberId(), selfUserDstb);

        //是一个team才会产生分佣
        boolean isTeam = isTeam(selfUserDstb.getInteger(GRADE), selfUserDstb.getLong(HIGHER), selfUserDstb.getLong(TOPER));
        if (!isTeam) {
            logger.info("添加分销账户流水记录--不是team");
            return null;
        }

        //获取分佣,团队收益的流水信息
        return prepareEarningData(vo, strategyList, selfUserDstb);
    }


    @Override
    public JSONObject getUserIncomeStatistics(Long userId) {
        ShopMemberAccount shopMemberAccount = shopMemberAccountDao.findByUserId(userId);
        BigDecimal allCash = new BigDecimal(0);
        BigDecimal aliveCash = new BigDecimal(0);
        BigDecimal waitInTotalCash = new BigDecimal(0);
        BigDecimal allGoldCoin = new BigDecimal(0);
        if (shopMemberAccount != null) {
            allCash = shopMemberAccount.getAllCash();
            aliveCash = shopMemberAccount.getAliveCash();
            waitInTotalCash = shopMemberAccount.getWaitInTotalCash();
            allGoldCoin = shopMemberAccount.getAllGoldCoin();
        }
        Double coin = shopMemberAccountCashOutInDao.findTodayCountMoney(userId);
        JSONObject jsonObject = new JSONObject();
        //总金额
        jsonObject.put("count", allCash);
        //已结算 可用金额 可提现
        jsonObject.put("already", aliveCash);
        // 待结算
        jsonObject.put("wait", waitInTotalCash);
        //金币
        jsonObject.put("goldCash", allGoldCoin);
        //今日金额
        jsonObject.put("todayCoin", coin);


        return jsonObject;
    }

    public static void main(String[] args) {
//        JSONObject jsonObject = new JSONObject ();
//        JSONObject j1 = new JSONObject ();
////        String a = "{\"classA\":200,\"classAB\":500,\"countMoney\",100000}";
////        String b = "{\"distributor\":5,\"countMoney\":500000}";
////        String c = "{\"classA\":200,\"buyCount\":1}";
//        j1.put ("count", 200);
//        j1.put ("already", 1);
//        j1.put ("wait", 1);
//        j1.put ("today", 1);
//
//        JSONObject j2 = new JSONObject ();
//        j2.put ("count", 200);
//        j2.put ("already", 1);
//        j2.put ("wait", 1);
//        j2.put ("today", 1);
//        jsonObject.put ("commissionBill", j1);
//        jsonObject.put ("manageBill", j2);
//        System.out.println (jsonObject);

        String a = "{\"classA\":1,\"buyCount\":2}";
        JSONObject conditionJson = JSONObject.parseObject(a);
        System.out.println(conditionJson);

    }


    /**
     * 分销成功
     *
     * @param vo vo
     * @author Charlie
     * @date 2018/10/10 20:45
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dstbSuccess(ShopMemberOrderVo vo) {
        logger.info("分销成功,待结算佣金入账,生成团队收益待结算 vo={}", vo);
        String orderNo = vo.getOrderNo();
        ErrorHelper.declareNull(orderNo, "未找到订单号");
        ShopMemberOrder shopMemberOrder = shopMemberOrderRpcService.findOrderByOrderNo(orderNo);
        logger.info("分销成功,订单信息={}", shopMemberOrder);
        Long memberId = shopMemberOrder.getMemberId();

        //粉丝升级店长
        distributionSystemService.toStore(memberId, orderNo);


        //查询该订单下的待结算返利流水(多个用户)
        List<ShopMemberAccountCashOutIn> waitCommissionCashOutIn = shopMemberAccountCashOutInService.selectOrderWaitCommission(orderNo);
        boolean existWaitInCommission = !ObjectUtils.isEmpty(waitCommissionCashOutIn);
        if (existWaitInCommission) {
            logger.info("结算待结算返佣流水 orderNo={},size={}", orderNo, waitCommissionCashOutIn.size());
            //结算佣金
            for (ShopMemberAccountCashOutIn cashOutIn : waitCommissionCashOutIn) {
                ShopMemberAccount account = shopMemberAccountDao.findByUserId(cashOutIn.getUserId());
                updCashOutIn(account, cashOutIn, CashOutInUpdEnum.SETTLE_COMMISSION, Optional.empty());
            }
        }


        //查询待结算管理金(和返佣待结算拆开)
        List<ShopMemberAccountCashOutIn> waitManager = shopMemberAccountCashOutInService.selectOrderWaitManager(orderNo);
        logger.info("查询预结算的管理金 size{},orderNo={}", waitManager.size(), orderNo);
        boolean existWaitInManager = !waitManager.isEmpty();
        if (existWaitInManager) {
            //将预待结算设为待结算
            for (ShopMemberAccountCashOutIn cashOutIn : waitManager) {
                ShopMemberAccount account = shopMemberAccountDao.findByUserId(cashOutIn.getUserId());
                updCashOutIn(account, cashOutIn, CashOutInUpdEnum.WAIT_SETTLE_CONDITION_IS_OK_BUT_NO_SETTLE, Optional.empty());
            }
        }


        //订单扩展表(也有可能根本没有收益产生,ShopMemberOrderDstbRecord==null-->skip)
        ShopMemberOrderDstbRecord history = shopMemberOrderDstbRecordDao.selectByOrderNo(orderNo);
        logger.info("分销成功--查询订单扩展记录 orderRecord={}", history);
        if (history != null) {
            ShopMemberOrderDstbRecord updInfo = new ShopMemberOrderDstbRecord();
            updInfo.setId(history.getId());
            updInfo.setOrderSuccessTime(vo.getOrderSuccessTime() == null ? System.currentTimeMillis() : vo.getOrderSuccessTime());
            int rec = shopMemberOrderDstbRecordDao.updateByPrimaryKeySelective(updInfo);
            ErrorHelper.declare(rec == 1, "更新订单扩展表失败");
        }

    }


    /**
     * 账户流水的状态变更
     *
     * @param account 用户账号
     * @param history 流水记录
     * @param updType 变更类型 如 结算->入账 提现->提现成功 提现->提现失败
     * @param updVo   用于记录流水状态修改时,一些额外的参数
     * @author Charlie
     * @date 2018/10/13 15:48
     */
    private void updCashOutIn(ShopMemberAccount account, ShopMemberAccountCashOutIn history, CashOutInUpdEnum updType, Optional<CashOutInUpdVo> updVo) {
        Long outInId = history.getId();
        logger.info("账户流水的状态变更 account={},history={},updType={},updVo={}", account, history, updType, updVo);
        switch (updType) {
            //结算分佣
            case SETTLE_COMMISSION: {
                settleCommission(account, history, outInId);
                break;
            }
            //结算管理金
            case SETTLE_TEAM_IN: {
                settleTeamIn(account, history, outInId);
                break;
            }
            //结算条件成立,但是不结算
            case WAIT_SETTLE_CONDITION_IS_OK_BUT_NO_SETTLE: {
                int rec = shopMemberAccountCashOutInDao.conditionIsOk(outInId);
                logger.info("满足待结算条件, 但是暂不结算 rec={}, outInId={}", rec, outInId);
                break;
            }
            case START_CASH: {
                //开始提现
                startCash(history);
                break;
            }
            case CASH_OUT_SUCCESS: {
                //提现成功
                cashOutSuccess(account, history, updVo);
                break;
            }
            case CASH_OUT_FAILED: {
                //提现失败
                cashOutFailed(account, history, updVo);
                break;
            }
            default:
                ErrorHelper.declare(Boolean.FALSE, "未知的操作类型");
        }
    }


    /**
     * 结算管理金
     *
     * @param account account
     * @param history history
     * @param outInId outInId
     * @author Charlie
     * @date 2018/11/18 19:15
     */
    private void settleTeamIn(ShopMemberAccount account, ShopMemberAccountCashOutIn history, Long outInId) {
        int rec = shopMemberAccountCashOutInService.updStatus(outInId,
                CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode(), CashOutInStatusEnum.ALREADY_SETTLE_ACCOUNT.getCode(),
                System.currentTimeMillis(), null,
                CashOutInStatusDetailEnum.WAIT_GRANT.getCode(), CashOutInStatusDetailEnum.SUCCESS.getCode()
        );
        logger.info("待结算入账,关闭流水 rec:{}", rec);
        ErrorHelper.declare(rec == 1, "更新流水状态失败 outInId=" + outInId);
        rec = shopMemberAccountDao.waitTeamIn2Alive(account.getId(), history.getOperCash(), history.getOperGoldCoin());
        logger.info("分销管理金待结算入账 rec:{}", rec);
        ErrorHelper.declare(rec == 1, "更新账户失败 outInId=" + outInId);
    }

    /**
     * 结算佣金
     *
     * @param account account
     * @param history history
     * @param outInId outInId
     * @author Charlie
     * @date 2018/11/18 19:15
     */
    private void settleCommission(ShopMemberAccount account, ShopMemberAccountCashOutIn history, Long outInId) {
        int rec = shopMemberAccountCashOutInService.updStatus(
                outInId,
                1, 2,
                System.currentTimeMillis(),
                null,
                CashOutInStatusDetailEnum.WAIT.getCode(), CashOutInStatusDetailEnum.SUCCESS.getCode()
        );
        logger.info("待结算入账,关闭流水 rec:{}", rec);
        ErrorHelper.declare(rec == 1, "更新流水状态失败");
        //更新账户
        rec = shopMemberAccountDao.waitCommission2Alive(
                account.getId(),
                history.getOperCash(),
                history.getOperGoldCoin()
        );
        logger.info("分销返现待结算入账 rec:{}", rec);
        ErrorHelper.declare(rec == 1, "更新账户失败 outInId=" + outInId);
    }

    /**
     * 开始提现
     *
     * @param history history
     * @author Charlie
     * @date 2018/11/18 19:14
     */
    private void startCash(ShopMemberAccountCashOutIn history) {
        List<ShopMemberAccountCashOutIn> allCashOutIn = shopMemberAccountDao.selectByOrder(
                history.getOrderNo(),
                CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode(), CashOutInStatusDetailEnum.WAIT.getCode(),
                CashOutInTypeEnum.CASH_OUT_TOTAL.getCode(), CashOutInTypeEnum.CASH_OUT_COMMISSION.getCode(), CashOutInTypeEnum.CASH_OUT_MANAGER.getCode()
        );
        for (ShopMemberAccountCashOutIn outIn : allCashOutIn) {
            ShopMemberAccountCashOutIn upd = new ShopMemberAccountCashOutIn();
            upd.setDetailStatus(CashOutInStatusDetailEnum.WAIT_GRANT.getCode());
            int rec = MybatisOperaterUtil.getInstance().update(
                    upd, new MybatisSqlWhereBuild(ShopMemberAccountCashOutIn.class)
                            .eq(ShopMemberAccountCashOutIn::getId, outIn.getId())
                            .eq(ShopMemberAccountCashOutIn::getDetailStatus, CashOutInStatusDetailEnum.WAIT.getCode())
                            .in(ShopMemberAccountCashOutIn::getDelStatus, StateEnum.NORMAL, StateEnum.HIDE)
            );
            logger.info("开始提现 rec:{},outInId={}", rec, outIn.getId());
            ErrorHelper.declare(rec == 1, "更新流水状态失败");
        }
    }

    /**
     * 提现成功
     *
     * @param account account
     * @param history history
     * @param updVo   updVo
     * @author Charlie
     * @date 2018/11/18 19:14
     */
    private void cashOutSuccess(ShopMemberAccount account, ShopMemberAccountCashOutIn history, Optional<CashOutInUpdVo> updVo) {
        /* 更新账户 */
        int rec = shopMemberAccountDao.cashOutSuccess(account.getId(), history.getOperCash(), false);
        logger.info("提现成功,更新扣除总现金 rec={}", rec);
        ErrorHelper.declare(rec == 1, "更新账户失败");

        /* 关闭流水 */
        ShopMemberAccount accountNew = shopMemberAccountDao.findByUserId(history.getUserId());
        List<ShopMemberAccountCashOutIn> allCashOutIn = shopMemberAccountDao.selectByOrder(
                history.getOrderNo(),
                CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode(), CashOutInStatusDetailEnum.WAIT_GRANT.getCode(),
                CashOutInTypeEnum.CASH_OUT_TOTAL.getCode(), CashOutInTypeEnum.CASH_OUT_COMMISSION.getCode(), CashOutInTypeEnum.CASH_OUT_MANAGER.getCode()
        );
        ErrorHelper.declare(!allCashOutIn.isEmpty(), "未找到提现信息");
        for (ShopMemberAccountCashOutIn outIn : allCashOutIn) {
            Integer type = outIn.getType();
            if (CashOutInTypeEnum.CASH_OUT_TOTAL.isThis(type)) {
                rec = shopMemberAccountCashOutInService.cashOutSuccess(outIn, updVo.get(), accountNew.getAliveCash(), 1);
                logger.info("提现成功,关闭流水 rec:{},outInId={}", rec, outIn.getId());
                ErrorHelper.declare(rec == 1, "更新流水状态失败");
            }
            if (CashOutInTypeEnum.CASH_OUT_COMMISSION.isThis(type)) {
                rec = shopMemberAccountCashOutInService.cashOutSuccess(outIn, updVo.get(), accountNew.getCommissionAliveCash(), 1);
                logger.info("提现成功,关闭流水 rec:{},outInId={}", rec, outIn.getId());
                ErrorHelper.declare(rec == 1, "更新流水状态失败");
            }
            if (CashOutInTypeEnum.CASH_OUT_MANAGER.isThis(type)) {
                rec = shopMemberAccountCashOutInService.cashOutSuccess(outIn, updVo.get(), accountNew.getManageAliveCash(), 1);
                logger.info("提现成功,关闭流水 rec:{},outInId={}", rec, outIn.getId());
                ErrorHelper.declare(rec == 1, "更新流水状态失败");
            }
        }
    }

    /**
     * 提现失败
     *
     * @param account
     * @param history history
     * @param updVo   updVo
     * @author Charlie
     * @date 2018/11/18 19:13
     */
    private void cashOutFailed(ShopMemberAccount account, ShopMemberAccountCashOutIn history, Optional<CashOutInUpdVo> updVo) {
        List<ShopMemberAccountCashOutIn> allCashOutIn = shopMemberAccountDao.selectByOrder(
                history.getOrderNo(),
                CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode(), CashOutInStatusDetailEnum.WAIT_GRANT.getCode(),
                CashOutInTypeEnum.CASH_OUT_TOTAL.getCode(), CashOutInTypeEnum.CASH_OUT_COMMISSION.getCode(), CashOutInTypeEnum.CASH_OUT_MANAGER.getCode()
        );

        ErrorHelper.declare(!allCashOutIn.isEmpty(), "未找到提现信息");
        BigDecimal totalOperCash = BigDecimal.ZERO;
        BigDecimal commissionOperCash = BigDecimal.ZERO;
        BigDecimal teamInOperCash = BigDecimal.ZERO;
        for (ShopMemberAccountCashOutIn cashOutIn : allCashOutIn) {
            Integer type = cashOutIn.getType();
            if (CashOutInTypeEnum.CASH_OUT_TOTAL.isThis(type)) {
                totalOperCash = cashOutIn.getOperCash();
            }
            if (CashOutInTypeEnum.CASH_OUT_COMMISSION.isThis(type)) {
                commissionOperCash = cashOutIn.getOperCash();
            }
            if (CashOutInTypeEnum.CASH_OUT_MANAGER.isThis(type)) {
                teamInOperCash = cashOutIn.getOperCash();
            }
        }
        logger.info("提现失败,回滚可用总额={},可用佣金={},可用管理金={}", totalOperCash, commissionOperCash, teamInOperCash);
        int rec = shopMemberAccountDao.accountPreCashOut(account.getId(), totalOperCash, commissionOperCash, teamInOperCash, true);
        ErrorHelper.declare(rec == 1, "更新账户失败");

        /* 关闭流水 */
        for (ShopMemberAccountCashOutIn outIn : allCashOutIn) {
            Integer type = outIn.getType();
            if (CashOutInTypeEnum.CASH_OUT_TOTAL.isThis(type)) {
                rec = shopMemberAccountCashOutInService.cashOutSuccess(outIn, updVo.get(), account.getAliveCash(), -1);
                logger.info("提现失败,关闭流水 rec:{},outInId={}", rec, outIn.getId());
                ErrorHelper.declare(rec == 1, "更新流水状态失败");
            }
            if (CashOutInTypeEnum.CASH_OUT_COMMISSION.isThis(type)) {
                rec = shopMemberAccountCashOutInService.cashOutSuccess(outIn, updVo.get(), account.getCommissionAliveCash(), -1);
                logger.info("提现失败,关闭流水 rec:{},outInId={}", rec, outIn.getId());
                ErrorHelper.declare(rec == 1, "更新流水状态失败");
            }
            if (CashOutInTypeEnum.CASH_OUT_MANAGER.isThis(type)) {
                rec = shopMemberAccountCashOutInService.cashOutSuccess(outIn, updVo.get(), account.getManageAliveCash(), -1);
                logger.info("提现失败,关闭流水 rec:{},outInId={}", rec, outIn.getId());
                ErrorHelper.declare(rec == 1, "更新流水状态失败");
            }
        }
    }


    /**
     * 团队收益待结算到可用
     *
     * @author Charlie
     * @date 2018/10/10 21:46
     */
    @Override
    public Map<String, Integer> teamInWait2Alive() {
        Map<String, Integer> result = new HashMap<>(2);
        //需要操作数
        result.put("total", 0);
        //成功数量
        result.put("successCount", 0);

        //分销返利结算
        List<ShopMemberAccountCashOutIn> waitTeamInGroupByOrderNo = shopMemberAccountCashOutInService.listWaitTeamIn();
        logger.info("团队收益待结算到可用--查询所有待结算的分销管理金流水, size={}", waitTeamInGroupByOrderNo.size());
        if (waitTeamInGroupByOrderNo.isEmpty()) {
            return result;
        }

        result.put("total", waitTeamInGroupByOrderNo.size());
        //循环的更新返利用户的流水以及更新账户
        for (ShopMemberAccountCashOutIn cashOutIn : waitTeamInGroupByOrderNo) {
            try {
                shopMemberAccountService.doWaitTeamIn2Alive(cashOutIn);
                result.put("successCount", result.get("successCount") + 1);
            } catch (ErrorHelper e) {
                logger.error("将待结算的管理奖金失败", e.getMsg());
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("将待结算的管理奖金失败");
                e.printStackTrace();
            }
        }

        return result;
    }


    /**
     * 将待结算管理金结算
     *
     * @param cashOutIn cashOutIn
     * @author Charlie
     * @date 2018/10/16 23:42
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public /*synchronized */void doWaitTeamIn2Alive(ShopMemberAccountCashOutIn cashOutIn) {
//        String orderNo = cashOutIn.getOrderNo ();
        Long userId = cashOutIn.getUserId();
//        Long outInId = cashOutIn.getId ();
        //订单扩展表
//        ShopMemberOrderDstbRecord history = shopMemberOrderDstbRecordDao.selectByOrderNo (orderNo);
//        ErrorHelper.declareNull (history, "未找到订单记录信息 outInId=" + outInId);
        //该订单下该用户是否还有其他已结算的分佣流水
//        long count = shopMemberAccountCashOutInService.countUserHasSettleOutInByOrder (userId, orderNo);
//        logger.info ("该订单下该用户是否还有其他已结算的分佣流水 count={}", count);
        //查询该订单下
//        CashOutInUpdVo cashOutInUpdVo = new CashOutInUpdVo ();
//        cashOutInUpdVo.setOrderRealPay (history.getOrderMoney ());
//        cashOutInUpdVo.setNeedSetOrderInfo (count == 0);
//        Optional<CashOutInUpdVo> inUpdVo = Optional.of (cashOutInUpdVo);

        ShopMemberAccount account = shopMemberAccountDao.findByUserId(userId);
        updCashOutIn(account, cashOutIn, CashOutInUpdEnum.SETTLE_TEAM_IN, Optional.empty());
    }


    /**
     * 账单统计
     *
     * @param userId
     * @return
     */
    @Override
    public Response countBill(Long userId) {
        ShopMemberAccount shopMemberAccount = shopMemberAccountDao.findByUserId(userId);
        JSONObject jsonObject = new JSONObject();
        JSONObject commissionBill = commissionBill(userId, shopMemberAccount);
        JSONObject manageBill = manageBill(userId, shopMemberAccount);
        jsonObject.put("commissionBill", commissionBill);
        jsonObject.put("manageBill", manageBill);
        JSONObject jsonObject1 = distributionSystemUtil.find(userId);
        Integer grade = 0;
        if (jsonObject1 != null) {
            grade = jsonObject1.getInteger(GRADE);
        }
        jsonObject.put("grade", grade);

        return Response.success(jsonObject);
    }

    /**
     * 账单明细
     *
     * @param type
     * @param inOutType
     * @param status
     * @param userId
     * @param page
     * @return
     */
    @Override
    public Response findBillDetails(Integer choose, Integer type, Integer inOutType, Integer status, Long userId, Integer page) {
        List<ShopMemberAccountCashOutIn> list = shopMemberAccountCashOutInDao.findBillDetails(choose, type, inOutType, status, userId, page);
        List<ShopMemAcctCashOutInQuery> list2 = new ArrayList<>();
        for (ShopMemberAccountCashOutIn shopMemberAccountCashOutIn : list) {
            ShopMemAcctCashOutInQuery shopMemAcctCashOutInQuery = new ShopMemAcctCashOutInQuery();
            BeanUtils.copyProperties(shopMemberAccountCashOutIn, shopMemAcctCashOutInQuery);
            Integer types = shopMemberAccountCashOutIn.getType() == null ? 0 : shopMemberAccountCashOutIn.getType();
            shopMemAcctCashOutInQuery.setType(IntegerTypeExchangeUtils.shopMemberAccountTypeChange(types));
            shopMemAcctCashOutInQuery.setStrTime(TimeUtils.stamp2Str(shopMemberAccountCashOutIn.getCreateTime()));
            list2.add(shopMemAcctCashOutInQuery);
        }

//        list2.forEach(
//                shopMemberAccountCashOutIn -> { shopMemberAccountCashOutIn.
//                        setType(IntegerTypeExchangeUtils.shopMemberAccountTypeChange(shopMemberAccountCashOutIn.getType()));
//
//                }
//
//        );
        return Response.success(list2);
    }

    /**
     * 金币账单信息
     *
     * @param userId
     * @return
     */
    @Override
    public JSONObject countGoldBill(Long userId) {
        ShopMemberAccount shopMemberAccount = shopMemberAccountDao.findByUserId(userId);
        BigDecimal count = new BigDecimal(0);
        BigDecimal already = new BigDecimal(0);
        BigDecimal wait = new BigDecimal(0);
        if (shopMemberAccount != null) {
            already = shopMemberAccount.getAliveGoldCoin();
            wait = shopMemberAccount.getWaitInTotalGoldCoin();
            count = shopMemberAccount.getAllGoldCoin();

        }
        JSONObject jsonObject = new JSONObject();
        Double todayGoldCountMoney = shopMemberAccountCashOutInDao.findTodayGoldCountMoney(userId);
        jsonObject.put("count", count);
        jsonObject.put("already", already);
        jsonObject.put("wait", wait);
        jsonObject.put("today", todayGoldCountMoney);
        return jsonObject;
    }

    /**
     * 佣金奖 信息
     *
     * @param userId
     * @param shopMemberAccount
     * @return
     */
    public JSONObject commissionBill(Long userId, ShopMemberAccount shopMemberAccount) {
        BigDecimal count = new BigDecimal(0);
        BigDecimal already = new BigDecimal(0);
        BigDecimal wait = new BigDecimal(0);
        Double commissionTodayCountMoney = shopMemberAccountCashOutInDao.findCommissionTodayCountMoney(userId);
        if (shopMemberAccount != null) {
            //这里的已结算,是可用,提现后会减,与后台的已结算是两回事
            already = shopMemberAccount.getCommissionAliveCash();
            wait = shopMemberAccount.getCommissionWaitInTotalCash();
            count = already.add(wait);

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", count);
        jsonObject.put("already", already);
        jsonObject.put("wait", wait);
        jsonObject.put("today", commissionTodayCountMoney);
        return jsonObject;
    }

    /**
     * 管理奖  信息
     *
     * @param userId
     * @param shopMemberAccount
     * @return
     */
    public JSONObject manageBill(Long userId, ShopMemberAccount shopMemberAccount) {
        BigDecimal count = new BigDecimal(0);
        BigDecimal already = new BigDecimal(0);
        BigDecimal wait = new BigDecimal(0);
        if (shopMemberAccount != null) {
            count = shopMemberAccount.getManageAliveCash().add(shopMemberAccount.getManageWaitInTotalCash());
            already = shopMemberAccount.getManageAliveCash();
            wait = shopMemberAccount.getManageWaitInTotalCash();
        }
        Double manageTodayCountMoney = shopMemberAccountCashOutInDao.findManageTodayCountMoney(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", count);
        jsonObject.put("already", already);
        jsonObject.put("wait", wait);
        jsonObject.put("today", manageTodayCountMoney);
        return jsonObject;
    }


    /**
     * 记录下单后的分销返利
     *
     * @param vo 订单信息
     * @author Charlie
     * @date 2018/10/11 17:00
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDstbFromOrder(ShopMemberOrderVo vo) {
        //记录分销返利流水
        DstbCashOutInVo data = addDstbCashOutIn(vo);
        logger.info("分销团队收益流水 vo:{}", data);
        if (data == null) {
            return;
        }

        List<ShopMemberAccountCashOutIn> history = MybatisOperaterUtil.getInstance().finAll(
                new ShopMemberAccountCashOutIn(),
                new MybatisSqlWhereBuild(ShopMemberAccountCashOutIn.class)
                        .in(ShopMemberAccountCashOutIn::getType, 0, 1, 2, 10, 11)
                        .eq(ShopMemberAccountCashOutIn::getOrderNo, data.getOrderNo())
                        .eq(ShopMemberAccountCashOutIn::getInOutType, 1)
        );
        ErrorHelper.declare(ObjectUtils.isEmpty(history), "该订单号已生成过分销流水,不可重复添加");

        ShopMemAcctCashOutInQuery selfCommission = data.getSelfCommission();
        ShopMemAcctCashOutInQuery higherCommission = data.getHigherCommission();
        ShopMemAcctCashOutInQuery topCommission = data.getTopCommission();
        ShopMemAcctCashOutInQuery dstbTeamIn = data.getDstbTeamIn();
        ShopMemAcctCashOutInQuery partnerTeamIn = data.getPartnerTeamIn();

        //多个分销收益角色可能是一个人,避免重复创建账户
        Map<Long, AccountDTO> accountMap = new HashMap<>(4);
        /* 新增分销返利流水 */
        logger.info("新增自分佣流水");
        addCashOutIfNoAccountPutInMap(vo, selfCommission, accountMap);
        logger.info("新增上级分佣流水");
        addCashOutIfNoAccountPutInMap(vo, higherCommission, accountMap);
        logger.info("新增上级上分佣流水");
        addCashOutIfNoAccountPutInMap(vo, topCommission, accountMap);
        logger.info("新增分销商管理流水");
        addCashOutIfNoAccountPutInMap(vo, dstbTeamIn, accountMap);
        logger.info("新增合伙人管理流水");
        addCashOutIfNoAccountPutInMap(vo, partnerTeamIn, accountMap);

        //创建初始化订单扩展表
        ShopMemberOrderDstbRecord historyRecord = shopMemberOrderDstbRecordDao.selectByOrderNo(vo.getOrderNo());
        ErrorHelper.declare(historyRecord == null, "该订单已经有过记录, 不能反复插入");
        ShopMemberOrderDstbRecord orderRecord = new ShopMemberOrderDstbRecord();
        if (BeanKit.notNull(selfCommission)) {
            orderRecord.setSelfCommissionUserId(selfCommission.getUserId());
        }
        if (BeanKit.notNull(higherCommission)) {
            orderRecord.setHigherCommissionUserId(higherCommission.getUserId());
        }
        if (BeanKit.notNull(topCommission)) {
            orderRecord.setTopCommissionUserId(topCommission.getUserId());
        }
        orderRecord.setDistributor(data.getDistributor());
        orderRecord.setOrderNo(vo.getOrderNo());
        orderRecord.setPartner(data.getPartner());
        orderRecord.setExchangeRate(data.getExchangeRate());
        orderRecord.setOrderMoney(vo.getRealPay());
        BigDecimal totalCommissionCash = BigDecimal.ZERO;
        BigDecimal totalCommissionGoldCoin = BigDecimal.ZERO;
        if (BeanKit.notNull(selfCommission)) {
            totalCommissionCash = totalCommissionCash.add(selfCommission.getOperCash());
            totalCommissionGoldCoin = totalCommissionGoldCoin.add(selfCommission.getOperGoldCoin());
        }
        if (BeanKit.notNull(higherCommission)) {
            totalCommissionCash = totalCommissionCash.add(higherCommission.getOperCash());
            totalCommissionGoldCoin = totalCommissionGoldCoin.add(higherCommission.getOperGoldCoin());
        }
        if (BeanKit.notNull(topCommission)) {
            totalCommissionCash = totalCommissionCash.add(topCommission.getOperCash());
            totalCommissionGoldCoin = totalCommissionGoldCoin.add(topCommission.getOperGoldCoin());
        }

        BigDecimal totalManagerCash = BigDecimal.ZERO;
        BigDecimal totalManagerGoldCoin = BigDecimal.ZERO;
        if (BeanKit.notNull(dstbTeamIn)) {
            totalManagerCash = totalManagerCash.add(dstbTeamIn.getOperCash());
            totalManagerGoldCoin = totalManagerGoldCoin.add(dstbTeamIn.getOperGoldCoin());
        }
        if (BeanKit.notNull(partnerTeamIn)) {
            totalManagerCash = totalManagerCash.add(partnerTeamIn.getOperCash());
            totalManagerGoldCoin = totalManagerGoldCoin.add(partnerTeamIn.getOperGoldCoin());
        }
        orderRecord.setTotalCommissionCash(totalCommissionCash);
        orderRecord.setTotalCommissionGoldCoin(totalCommissionGoldCoin);
        orderRecord.setTotalManagerCash(totalManagerCash);
        orderRecord.setTotalManagerGoldCoin(totalManagerGoldCoin);
        orderRecord.setPayTime(vo.getPayTime() == null ? System.currentTimeMillis() : vo.getPayTime());
        int rec = shopMemberOrderDstbRecordDao.insertSelective(orderRecord);
        ErrorHelper.declare(rec == 1, "新增订单扩展表失败");
    }

    /**
     * 新增分销流水
     * <p> 会在accountMap中查找账户,如果有则直接记录流水,并将账户放在map中 </p>
     * <p> 入股用户没有账户,则创建新账户,并放在accountMap中,并记录流水 </p>
     *
     * @param vo 订单信息
     * @param cashOutVO 新增流水信息
     * @param accountMapCache 用户账户
     * @author Charlie
     * @date 2019/1/24 16:50
     */
    private void addCashOutIfNoAccountPutInMap(ShopMemberOrderVo vo, ShopMemAcctCashOutInQuery cashOutVO, Map<Long, AccountDTO> accountMapCache) {
        if (cashOutVO == null) {
            return;
        }
        Long userId = cashOutVO.getUserId();
        AccountDTO accountDTO = accountMapCache.get(userId);
        if (accountDTO != null && accountDTO.getAccount() != null) {
            //有账户,直接调用
            ShopMemberAccount account = accountDTO.getAccount();
            addCashOutIn(account, cashOutVO, vo);
        }
        else {
            //不知道有没有账户
            AccountDTO hAccount = addCashOutIn(cashOutVO, vo);
            accountMapCache.put(hAccount.getUserId(), hAccount);
        }
    }


    /**
     * 拼接分销流水data
     *
     * @param orderVo      订单
     * @param strategyList 卖家分销策略
     * @param selfUserDstb 订单买家的分销关系
     * @return com.e_commerce.miscroservice.commons.entity.distribution.DstbCashOutInVo
     * @author Charlie
     * @date 2018/10/10 15:34
     */
    private DstbCashOutInVo prepareEarningData(ShopMemberOrderVo orderVo, List<DstbEarningsStrategy> strategyList, JSONObject selfUserDstb) {
        //金币人民币兑换汇率
        DataDictionary dataDictionary = dataDictionaryRpcService.findByCodeAndGroupCode(DataDictionaryEnums.GOLD_COIN_EXCHANGE_RATE.getCode(), DataDictionaryEnums.GOLD_COIN_EXCHANGE_RATE.getGroupCode());
        ErrorHelper.declareNull(dataDictionary, "金币人民币兑换汇率");
        BigDecimal exchangeRate = new BigDecimal(dataDictionary.getVal());
        logger.info("金币人民币兑换汇率={}", exchangeRate);
        String orderNumber = orderVo.getOrderNo();
        //team合伙人
        Long teamPartner = selfUserDstb.getLong(PARTNER);
        //team分销商
        Long teamDistributor = selfUserDstb.getLong(DISTRIBUTOR);
        logger.info("team合伙人={},team分销商={}", teamPartner, teamDistributor);


        DstbCashOutInVo result = new DstbCashOutInVo();
        result.setOrderNo(orderVo.getOrderNo());
        result.setPartner(teamPartner);
        result.setDistributor(teamDistributor);
        result.setExchangeRate(exchangeRate);
        result.setSelfAccount(shopMemberAccountDao.findByUserId(orderVo.getMemberId()));
        Map<Long, ShopMemberAccount> cache = new HashMap<>(5);
        //上级账号
        if (selfUserDstb.getLong(HIGHER) != null) {
            Long userId = selfUserDstb.getLong(HIGHER);
            ShopMemberAccount account = shopMemberAccountDao.findByUserId(userId);
            result.setHigherAccount(account);
            cache.put(userId, account);
        }
        //上上级账号
        if (selfUserDstb.getLong(TOPER) != null) {
            Long userId = selfUserDstb.getLong(TOPER);
            ShopMemberAccount account = cache.get(userId);
            if (BeanKit.hasNull(account)) {
                account = shopMemberAccountDao.findByUserId(userId);
                cache.put(userId, account);
            }
            result.setTopAccount(account);
        }
        //分销商账号
        if (teamDistributor != null) {
            ShopMemberAccount account = cache.get(teamDistributor);
            if (BeanKit.hasNull(account)) {
                account = shopMemberAccountDao.findByUserId(teamDistributor);
                cache.put(teamDistributor, account);
            }
            result.setDstbAccount(account);
        }
        //合伙人账号
        if (teamPartner != null) {
            ShopMemberAccount account = cache.get(teamPartner);
            if (BeanKit.hasNull(account)) {
                account = shopMemberAccountDao.findByUserId(teamPartner);
                cache.put(teamPartner, account);
            }
            result.setTopAccount(account);
        }

        ShopMemberAccount dstbAccount = result.getDstbAccount();
        ShopMemberAccount partnerAccount = result.getPartnerAccount();

        //====================================== 自己下单分佣 ======================================
        Long userId = orderVo.getMemberId();
        Integer userGrade = selfUserDstb.getInteger(GRADE);
        //0:本人下单返利
        int earningsType = 0;
        //计算佣金
        logger.info("计算本人返佣开始 ======================= userId={},userGrade={}", userId, userGrade);
        Optional<EarningsVo> selfCommission = dstbEarningsStrategyService.calculateEarnings(strategyList, earningsType, userGrade, orderVo.getRealPay(), exchangeRate);
        logger.info("计算本人返佣结束 =======================");
        if (selfCommission.isPresent()) {
            EarningsVo earningsVo = selfCommission.get();
            logger.info("本人下单返利 总收益={},现金收益={},金币收益={}", earningsVo.getTotalMoney(), earningsVo.getCash(), earningsVo.getGoldCoin());
            //自己分佣流水
            int type = ShopMembAcctCashOutInTypeEnum.SELF_COMMISSION.code();
            ShopMemAcctCashOutInQuery cashOutInInfo = buildWaitInCashOutIn(orderNumber, userId, type, earningsVo, result.getSelfAccount(), 1);
            result.setSelfCommission(cashOutInInfo);

            BigDecimal snapshoot = cashOutInInfo.getOrderEarningsSnapshoot();

            //分销商团队收益流水
            logger.info("计算本人返佣的管理金(分销商)开始 ======================= ");
            ShopMemAcctCashOutInQuery dstbCashOutIn = teamInCashOutIn(strategyList, orderNumber, snapshoot, exchangeRate, teamDistributor, teamDistributor, 2, dstbAccount);
            logger.info("计算本人返佣的管理金(分销商)结束 ======================= result={}", dstbCashOutIn);
            result.appendDstbTeamIn(dstbCashOutIn);

            //合伙人收益流水
            logger.info("计算本人返佣的管理金(合伙人)开始 ======================= ");
            ShopMemAcctCashOutInQuery partnerCashOutIn = teamInCashOutIn(strategyList, orderNumber, snapshoot, exchangeRate, teamPartner, teamPartner, 3, partnerAccount);
            logger.info("计算本人返佣的管理金(合伙人)结束 ======================= result={}", partnerCashOutIn);
            result.appendPartnerTeamIn(partnerCashOutIn);
        }


        //====================================== 一级分佣 ======================================
        Long higherUserId = selfUserDstb.getLong(HIGHER);
        logger.info("上级userId={}", higherUserId);
        if (higherUserId != null) {
            JSONObject higherUser = distributionSystemUtil.find(higherUserId);
            ErrorHelper.declareNull(higherUser, "未找到上级分销者");
            higherUser.remove("sonList");
            logger.info("1级分佣 上级user={}", higherUser);

            int higherType = ShopMembAcctCashOutInTypeEnum.FANS_1_COMMISSION.code();
            Integer higherGrade = higherUser.getInteger(GRADE);
            //计算佣金
            //1:一级粉丝下单返利
            int higherEarningsType = 1;
            logger.info("计算1级粉丝返佣开始 ======================= ");
            Optional<EarningsVo> higherCommission = dstbEarningsStrategyService.calculateEarnings(strategyList, higherEarningsType, higherGrade, orderVo.getRealPay(), exchangeRate);
            logger.info("计算1级粉丝返佣结束 =======================");
            if (higherCommission.isPresent()) {
                EarningsVo earningsVo = higherCommission.get();
                logger.info("1级粉丝下单返利 总收益={},现金收益={},金币收益={}", earningsVo.getTotalMoney(), earningsVo.getCash(), earningsVo.getGoldCoin());
                //一级分佣流水
                ShopMemAcctCashOutInQuery cashOutInInfo = buildWaitInCashOutIn(orderNumber, higherUserId, higherType, earningsVo, result.getHigherAccount(), 1);
                result.setHigherCommission(cashOutInInfo);

                BigDecimal snapshoot = cashOutInInfo.getOrderEarningsSnapshoot();

                //分销商团队收益流水
                logger.info("计算1级粉丝返佣的管理金(分销商)开始 ======================= ");
                ShopMemAcctCashOutInQuery dstbCashOutIn = teamInCashOutIn(strategyList, orderNumber, snapshoot, exchangeRate, higherUser.getLong(DISTRIBUTOR), teamDistributor, 2, dstbAccount);
                logger.info("计算1级粉丝返佣的管理金(分销商)结束 ======================= result={}", dstbCashOutIn);
                result.appendDstbTeamIn(dstbCashOutIn);

                //合伙人收益流水
                logger.info("计算1级粉丝返佣的管理金(分销商)开始 ======================= ");
                ShopMemAcctCashOutInQuery partnerCashOutIn = teamInCashOutIn(strategyList, orderNumber, snapshoot, exchangeRate, higherUser.getLong(PARTNER), teamPartner, 3, partnerAccount);
                logger.info("计算1级粉丝返佣的管理金(分销商)结束 ======================= result={}", partnerCashOutIn);
                result.appendPartnerTeamIn(partnerCashOutIn);
            }
        }


        //====================================== 二级分佣 ======================================
        Long topUserId = selfUserDstb.getLong(TOPER);
        logger.info("上上级userId={}", topUserId);
        if (topUserId != null) {
            JSONObject topUser = distributionSystemUtil.find(topUserId);
            ErrorHelper.declareNull(topUser, "未找到上上级分销者");
            topUser.remove("sonList");
            logger.info("2级分佣 上上级user={}", topUser);

            int topType = ShopMembAcctCashOutInTypeEnum.FANS_2_COMMISSION.code();
            Integer topGrade = topUser.getInteger(GRADE);
            //计算佣金
            //2级粉丝下单返利
            int higherEarningsType = 2;
            logger.info("计算2级粉丝返佣开始 ======================= userId={},topGrade={}", topUserId, topGrade);
            Optional<EarningsVo> topCommission = dstbEarningsStrategyService.calculateEarnings(strategyList, higherEarningsType, topGrade, orderVo.getRealPay(), exchangeRate);
            logger.info("计算2级粉丝返佣结束 =======================");
            if (topCommission.isPresent()) {
                EarningsVo earningsVo = topCommission.get();
                logger.info("2级粉丝下单返利 总收益={},现金收益={},金币收益={}", earningsVo.getTotalMoney(), earningsVo.getCash(), earningsVo.getGoldCoin());
                //一级分佣流水
                ShopMemAcctCashOutInQuery cashOutInInfo = buildWaitInCashOutIn(orderNumber, topUserId, topType, earningsVo, result.getTopAccount(), 1);
                result.setTopCommission(cashOutInInfo);

                BigDecimal snapshoot = cashOutInInfo.getOrderEarningsSnapshoot();
                //分销商团队收益流水
                logger.info("计算2级粉丝返佣的管理金(分销商)开始 ======================= ");
                ShopMemAcctCashOutInQuery dstbCashOutIn = teamInCashOutIn(strategyList, orderNumber, snapshoot, exchangeRate, topUser.getLong(DISTRIBUTOR), teamDistributor, 2, dstbAccount);
                logger.info("计算2级粉丝返佣的管理金(分销商)结束 ======================= result={}", dstbCashOutIn);
                result.appendDstbTeamIn(dstbCashOutIn);

                //合伙人收益流水
                logger.info("计算1级粉丝返佣的管理金(合伙人)开始 ======================= ");
                ShopMemAcctCashOutInQuery partnerCashOutIn = teamInCashOutIn(strategyList, orderNumber, snapshoot, exchangeRate, topUser.getLong(PARTNER), teamPartner, 3, partnerAccount);
                logger.info("计算1级粉丝返佣的管理金(合伙人)结束 ======================= result={}", partnerCashOutIn);
                result.appendPartnerTeamIn(partnerCashOutIn);
            }
        }

        logger.info("计算分销返利和团队收益结果 result={}", result);
        return result;
    }


    /**
     * 获取分销流水记录
     *
     * @param strategyList             收益策略
     * @param orderNumber              订单号
     * @param realPay                  订单实际支付
     * @param goldCoinExchangeRate     金币人民币兑换汇率
     * @param currentUserManagerUserId 当前分佣收益者的管理者(当前受益者可能不属于下单者所属的team中,比如分销-分销-分销-店家关系)
     * @param teamManagerUserId        下单者所属team的管理者
     * @param grade                    2:分销商,3:合伙人
     * @return 分销流水
     * @author Charlie
     * @date 2018/10/10 13:46
     */
    private ShopMemAcctCashOutInQuery teamInCashOutIn(List<DstbEarningsStrategy> strategyList, String orderNumber,
                                                      BigDecimal realPay,
                                                      BigDecimal goldCoinExchangeRate, Long currentUserManagerUserId,
                                                      Long teamManagerUserId,
                                                      Integer grade,
                                                      ShopMemberAccount account) {
        //团队收益
        int type = 10;
        //流水类型
        int inType;
        switch (grade) {
            case 2: {
                inType = ShopMembAcctCashOutInTypeEnum.DISTRIBUTOR_TEAM_IN.code();
                break;
            }
            case 3: {
                inType = ShopMembAcctCashOutInTypeEnum.PARTNER_TEAM_IN.code();
                break;
            }
            default:
                throw ErrorHelper.me("未知的团队收益者 grand:" + grade);
        }

        boolean isSampleTeamManger = teamManagerUserId != null && ObjectUtils.nullSafeEquals(teamManagerUserId, currentUserManagerUserId);
        logger.info("是否是同一个team的管理者={},管理角色={},订单所属team的管理者userId={},当前管理者的userId={}",
                isSampleTeamManger, grade, teamManagerUserId, currentUserManagerUserId);
        //计算收益
        if (isSampleTeamManger) {
            logger.info("计算管理金收益--管理角色={}", grade);
            Optional<EarningsVo> managerCommission = dstbEarningsStrategyService.calculateEarnings(strategyList, type, grade, realPay, goldCoinExchangeRate);
            if (managerCommission.isPresent()) {
                EarningsVo earningsVo = managerCommission.get();
                logger.info("管理金返利 总收益={},现金收益={},金币收益={}", earningsVo.getTotalMoney(), earningsVo.getCash(), earningsVo.getGoldCoin());
                return buildWaitInCashOutIn(orderNumber, teamManagerUserId, inType, earningsVo, account, 2);
            }
        }
        logger.info("没有管理收益");
        return null;
    }


    /**
     * 创建一个待入账的流水实体
     *
     * @param orderNumber 订单号
     * @param userId      流水所属用户
     * @param type        流水类型 {@link ShopMemberAccountCashOutIn#type}
     * @param ev          收益详情
     * @param account     账户
     * @param dstbType    1:分佣奖金,2:管理奖金
     * @return com.e_commerce.miscroservice.commons.entity.application.user.ShopMemberAccountCashOutIn
     * @author Charlie
     * @date 2018/10/9 16:32
     */
    private ShopMemAcctCashOutInQuery buildWaitInCashOutIn(String orderNumber, Long userId, int type, EarningsVo ev, ShopMemberAccount account, Integer dstbType) {

        ShopMemAcctCashOutInQuery info = new ShopMemAcctCashOutInQuery();
        info.setInOutType(1);
        info.setUserId(userId);
        info.setOrderNo(orderNumber);
        info.setPaymentNo(BeanKit.uuid());
        //金币
        info.setOperGoldCoin(ev.getGoldCoin());
        //返现
        info.setOperCash(ev.getCash());
        info.setType(type);
        info.setOrderEarningsSnapshoot(ev.getTotalMoney());
        //流水策略
        DstbEarningsStrategy strategy = ev.getStrategy();
        info.setEarningsRatio(strategy.getEarningsRatio());
        info.setCurrencyRatio(strategy.getCurrencyRatio());
        info.setUserDstbGrade(ev.getGrade());
        info.setStatus(CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode());
        info.setDetailStatus(CashOutInStatusDetailEnum.WAIT.getCode());
        return info;
    }


    /**
     * 是否是一个team
     *
     * @param grade    当前用户级别
     * @param higherUp 上级userId
     * @param topUp    上上级userId
     * @return boolean
     * @author Charlie
     * @date 2018/10/9 16:33
     */
    private boolean isTeam(Integer grade, Long higherUp, Long topUp) {
        if (grade == null) {
            //不在代理体系中
            return Boolean.FALSE;
        }
        if (grade > 0) {
            logger.info("是否是一个team--本身是管理者");
            return Boolean.TRUE;
        }

        if (isManager(higherUp)) {
            logger.info("是否是一个team--上级是管理者");
            return Boolean.TRUE;
        }

        if (isManager(topUp)) {
            logger.info("是否是一个team--上上级是管理者");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    /**
     * 用户是否是管理者
     *
     * @param userId userId
     * @return true : 是管理者
     * @author Charlie
     * @date 2018/10/9 16:52
     */
    private boolean isManager(Long userId) {
        if (userId != null) {
            JSONObject userInfo = distributionSystemUtil.find(userId);
            if (userInfo == null) {
                ErrorHelper.declareNull(userInfo, "未找到代理");
            }
            Integer grade = userInfo.getInteger("grade");
            if (grade != null && grade > 0) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 记录流水,更新账户信息,如果账户不存在则新建
     *
     * @param cashOutIn cashOutIn
     * @param order     订单信息
     * @return 流水id
     * @author Charlie
     * @date 2018/10/10 18:07
     */
    private AccountDTO addCashOutIn(ShopMemAcctCashOutInQuery cashOutIn, ShopMemberOrderVo order) {
        if (cashOutIn == null) {
            return null;
        }
        ShopMemberAccount account = findByUser(cashOutIn.getUserId());
        return addCashOutIn(account, cashOutIn, order);
    }


    /**
     * 记录流水,更新账户信息,如果账户不存在则新建
     *
     * @param cashOutIn cashOutIn
     * @param order     订单信息
     * @return 流水id
     * @author Charlie
     * @date 2018/10/10 18:07
     */
    private AccountDTO addCashOutIn(ShopMemberAccount account, ShopMemAcctCashOutInQuery cashOutIn, ShopMemberOrderVo order) {
        logger.info("记录流水 cashOutIn={}, account={}", cashOutIn, account);
        if (cashOutIn == null) {
            return null;
        }
        int rec = shopMemberAccountCashOutInService.insertSelective(cashOutIn);
        ErrorHelper.declare(rec == 1, "生成一条流水记录失败");

        if (account == null) {
            //创建新账户
            account = createAccount(cashOutIn, order);
        } else {
            //增加账户收益
            addAccountEarnings(account, cashOutIn);
        }

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccount(account);
        accountDTO.setCashOutId(cashOutIn.getId());
        accountDTO.setUserId(account.getUserId());
        return accountDTO;
    }

    /**
     * 根据流水更新账户的现金,金币
     *
     * @param account   account
     * @param cashOutIn cashOutIn
     * @author Charlie
     * @date 2018/11/29 15:04
     */
    private void addAccountEarnings(ShopMemberAccount account, ShopMemAcctCashOutInQuery cashOutIn) {
        int rec;
        BigDecimal operCash = cashOutIn.getOperCash();
        BigDecimal operGoldCoin = cashOutIn.getOperGoldCoin();
        Integer type = cashOutIn.getType();

        //更新
        logger.info("更新账号资金 cashOutIn:{},account:{}", cashOutIn, account);
        Long accountId = account.getId();
        ShopMemberQuery query = new ShopMemberQuery();
        query.setId(account.getUserId());
        ShopMemberVo shopMember = shopMemberRpcService.findOne(query);

        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
        yjjStoreBusinessAccountLog.setMemberId(account.getUserId());
        yjjStoreBusinessAccountLog.setOperMoney(operCash.doubleValue());
        if (CashOutInTypeEnum.isCommissionCashIn(type)) {
            //分佣待结算入账
            rec = shopMemberAccountDao.updateCommissionWaitCashAndGoldCoin(accountId, operCash, operGoldCoin);
            ErrorHelper.declare(rec == 1, "待结算分佣奖金入账");
            yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.PAY.getCode());
            yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.CUT_PAYMENT.getValue() + "-" + shopMember.getUserNickname() + "的佣金");
            yjjStoreBusinessAccountLog.setType(StoreBillEnums.CUT_PAYMENT_SUCCESS.getCode());
            //账户修改
//            storeBusinessAccountRpcService.updateStoreBusinessAccount(yjjStoreBusinessAccountLog);
        } else if (CashOutInTypeEnum.isManagerCashIn(type)) {
            logger.info("管理奖预待结算流水记录 ignore");
            rec = shopMemberAccountDao.updateManagerWaitCashAndGoldCoin(account.getId(), operCash, operGoldCoin);
            logger.info("预待结算到待结算,流水至待结算,更新账户 rec:{}", rec);
            ErrorHelper.declare(rec == 1, "更新账户失败");
            yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.PAY.getCode());
            yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.CUT_PAYMENT.getValue() + "-" + shopMember.getUserNickname() + "的管理奖");
            yjjStoreBusinessAccountLog.setType(StoreBillEnums.CUT_PAYMENT_SUCCESS.getCode());

            //账户修改
//            storeBusinessAccountRpcService.updateStoreBusinessAccount(yjjStoreBusinessAccountLog);
        } else if (CashOutInTypeEnum.CASH_OUT_TOTAL.isThis(type)) {
            logger.info("提现 orderNo={}", cashOutIn.getOrderNo());
            //拆分的佣金提现
            ShopMemberAccountCashOutIn cmsOutAdd = new ShopMemberAccountCashOutIn();
            cmsOutAdd.setUserId(cashOutIn.getUserId());
            cmsOutAdd.setInOutType(2);
            cmsOutAdd.setOperCash(cashOutIn.getCashOutCommission());
            cmsOutAdd.setOriginalCash(account.getCommissionAliveCash());
            cmsOutAdd.setOrderNo(cashOutIn.getOrderNo());
            cmsOutAdd.setDelStatus(3);
            cmsOutAdd.setStatus(CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode());
            cmsOutAdd.setDetailStatus(CashOutInStatusDetailEnum.WAIT.getCode());
            cmsOutAdd.setType(CashOutInTypeEnum.CASH_OUT_COMMISSION.getCode());
            rec = shopMemberAccountCashOutInService.insertSelective(cmsOutAdd);
            ErrorHelper.declare(rec == 1, "生成一条佣金提现的流水记录失败");
            logger.info("生成一条管理金提现的流水记录 账户id={},佣金预提现金额={},管理金原来可用金={}", accountId, cmsOutAdd.getOperCash(), cmsOutAdd.getOriginalCash());
            if (cashOutIn.getCashOutTeamIn() != null && BigDecimal.ZERO.compareTo(cashOutIn.getCashOutTeamIn()) != 0) {
                //拆分的管理金提现
                ShopMemberAccountCashOutIn managerOutAdd = new ShopMemberAccountCashOutIn();
                managerOutAdd.setUserId(cashOutIn.getUserId());
                managerOutAdd.setInOutType(2);
                managerOutAdd.setOperCash(cashOutIn.getCashOutTeamIn());
                managerOutAdd.setOriginalCash(account.getManageAliveCash());
                managerOutAdd.setOrderNo(cashOutIn.getOrderNo());
                managerOutAdd.setStatus(CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode());
                managerOutAdd.setDetailStatus(CashOutInStatusDetailEnum.WAIT.getCode());
                managerOutAdd.setDelStatus(3);
                managerOutAdd.setType(CashOutInTypeEnum.CASH_OUT_MANAGER.getCode());
                rec = shopMemberAccountCashOutInService.insertSelective(managerOutAdd);
                logger.info("生成一条管理金提现的流水记录 账户id={},管理金预提现金额={},管理金原来可用金={}", accountId, managerOutAdd.getOperCash(), managerOutAdd.getOriginalCash());
                ErrorHelper.declare(rec == 1, "生成一条管理金提现的流水记录失败");
            }
            //更新总可用金,佣金可佣金,管理金可佣金
            rec = shopMemberAccountDao.accountPreCashOut(accountId, operCash, cashOutIn.getCashOutCommission(), cashOutIn.getCashOutTeamIn(), false);
            logger.info("账户预提现 账户id={},预提现金额={},原来可用金={},rec={}", accountId, operCash, account.getAliveCash(), rec);
            ErrorHelper.declare(rec == 1, "预提现失败");
            yjjStoreBusinessAccountLog.setInOutType(StoreBillEnums.PAY.getCode());
            yjjStoreBusinessAccountLog.setRemarks(StoreBillEnums.WITHDRAW_CASH.getValue() + "-" + "资金提现");
            yjjStoreBusinessAccountLog.setType(StoreBillEnums.WITHDRAW_CASH_SUCCESS.getCode());
            //账户修改
            storeBusinessAccountRpcService.updateStoreBusinessAccount(yjjStoreBusinessAccountLog);
        } else if (CashOutInTypeEnum.SHARE.isThis(type)) {
            //分享
            rec = shopMemberAccountDao.addAliveGoldCashAndCommissionCashGold(accountId, operCash, operGoldCoin, false);
            logger.info("分享商品金币入账 账户di={},操作金币={},操作现金={},rec={}", accountId, operGoldCoin, operCash, rec);
            ErrorHelper.declare(rec == 1, "分享商品金币入账失败");
        } else if (CashOutInTypeEnum.NEW_USER_INVITEE.isThis(type)) {
            //新用户登录
            rec = shopMemberAccountDao.addAliveGoldCashAndCommissionCashGold(accountId, operCash, operGoldCoin, false);
            logger.info("新用户登录 账户di={},操作金币={},操作现金={},rec={}", accountId, operGoldCoin, operCash, rec);
            ErrorHelper.declare(rec == 1, "新用户登录金币入账失败");
        } else {
            ErrorHelper.declare(false, "更新账号待结算现金和金币,不支持的类型");
        }
    }

    /**
     * 创建一个账号
     *
     * @param cashOutIn cashOutIn
     * @param order     订单
     * @author Charlie
     * @date 2018/10/16 13:58
     */
    private ShopMemberAccount createAccount(ShopMemAcctCashOutInQuery cashOutIn, ShopMemberOrderVo order) {
        ShopMemberAccount account = new ShopMemberAccount();
        Integer type = cashOutIn.getType();
        BigDecimal operCash = cashOutIn.getOperCash();
        BigDecimal operGoldCoin = cashOutIn.getOperGoldCoin();
        logger.info("创建账号 userId={}", cashOutIn.getUserId());
        account.setUserId(cashOutIn.getUserId());
        if (CashOutInTypeEnum.isCommissionCashIn(type)) {
            //佣金
            logger.info("创建账号--返佣收益");
            account.setWaitInTotalCash(operCash);
            account.setWaitInTotalGoldCoin(operGoldCoin);
            account.setCommissionWaitInTotalCash(operCash);
            account.setCommissionWaitInTotalGoldCoin(operGoldCoin);
            account.setAllCash(operCash);
            account.setAllGoldCoin(operGoldCoin);
        } else if (CashOutInTypeEnum.isManagerCashIn(type)) {
            //管理金收益创建账号,只有在预待结算状态下才会出现没有账号的情况,这时候就创建一个都是0的账号
            logger.info("管理金预待结算,创建都是0的账号");
        } else if (CashOutInTypeEnum.SHARE.isThis(type)) {
            //分享(分享算到分佣里)
            logger.info("创建账号--分享的收益");
            account.setAllCash(operCash);
            account.setAliveCash(operCash);
            account.setHistoryCashEarning(operCash);
            account.setCommissionAliveCash(operCash);

            account.setAllGoldCoin(operGoldCoin);
            account.setAliveGoldCoin(operGoldCoin);
            account.setHistoryGoldCoinEarning(operGoldCoin);
            account.setCommissionAliveGoldCoin(operGoldCoin);
        } else if (CashOutInTypeEnum.NEW_USER_INVITEE.isThis(type)) {
            //被邀请得收益(分享算到分佣里)
            logger.info("创建账号--被邀请得收益");
            account.setAllCash(operCash);
            account.setAliveCash(operCash);
            account.setHistoryCashEarning(operCash);
            account.setCommissionAliveCash(operCash);

            account.setAllGoldCoin(operGoldCoin);
            account.setAliveGoldCoin(operGoldCoin);
            account.setHistoryGoldCoinEarning(operGoldCoin);
            account.setCommissionAliveGoldCoin(operGoldCoin);
        } else {
            ErrorHelper.declare(false, "创建账号,初始化待结算现金和金币,不支持的类型");
        }
        int rec = MybatisOperaterUtil.getInstance().save(account);
        ErrorHelper.declare(rec == 1, "创建一个新的账号");
        return account;
    }


    /**
     * 用户提现
     *
     * @param userId    用户id
     * @param operMoney 提现金额
     * @author Charlie
     * @date 2018/10/11 19:46
     */
    @Override
    public Map<String, Object> cashOut(Long userId, BigDecimal operMoney, HttpServletRequest request) {

        logger.info("提现 userId={}", userId);
        /* 先校验店家的确开通了提现功能 */
        ShopMemberQuery query = new ShopMemberQuery();
        query.setId(userId);
        ShopMemberVo shopMember = shopMemberRpcService.findOne(query);
        ErrorHelper.declareNull(shopMember, "未找到用户");
        //        2018年12月28日 hyf
        if (StringUtils.isEmpty(shopMember.getWxPhone())) {
            shopMember.setWxPhone("defaults");
        }
        ErrorHelper.declareNull(shopMember.getWxPhone(), 512, "未绑定手机号");

        String openId = shopMember.getBindWeixin();
        ErrorHelper.declareNull(openId, "未找到用户信息");

        Long storeId = shopMember.getStoreId();
        StoreWxaVo config = wxaPayConfigRpcService.findByStoreId(storeId);
        logger.info("查找用户所在店家 storeId={},config={}", storeId, config);
        ErrorHelper.declareNull(config, 530, "店家尚未开通提现功能");
        String appId = config.getAppId();
        String mchId = config.getMchId();
        String paternerKey = config.getPayKey();
        if (BeanKit.hasNull(appId, mchId, paternerKey, config.getCerPath())) {
            ErrorHelper.declareNull(config, "店家尚未开通提现功能");
        }

        /* 初始化提现流水,冻结账户资金 */
        Long cashOutId = shopMemberAccountService.preCashOut(userId, operMoney);
        ErrorHelper.declareNull(cashOutId, "申请提现失败");
        ShopMemberAccountCashOutIn cashOutIn = shopMemberAccountCashOutInService.findById(cashOutId);
        ErrorHelper.declareNull(cashOutId, "提现失败");

        Timestamp createTime = cashOutIn.getCreateTime();
        Map<String, Object> response = new HashMap<>(4);
        response.put("createTime", TimeUtils.stamp2Str(createTime));
        response.put("money", operMoney.toString());
        response.put("username", shopMember.getUserNickname());
        return response;
    }


    /**
     * 调用微信服务扣钱
     *
     * @param operMoney operMoney
     * @param ip        ip
     * @author Charlie
     * @date 2018/10/14 23:26
     */
    private Map<String, String> send2WeiXinCashOut(String appId,
                                                   String mchId,
                                                   String openId,
                                                   String orderNo,
                                                   String paternerKey,
                                                   String cerPath,
                                                   BigDecimal operMoney,
                                                   String ip

    ) {
        // 调用微信的接口
        Map<String, String> param = new HashMap<>(10);
        // 获取微信openid
        // 查询微信支付配置信息
        // 商户账号 mch_appid 是
        param.put("mch_appid", appId);
        // 商户号 mchid 是
        param.put("mchid", mchId);
        // 随机字符串 	nonce_str 是
        param.put("nonce_str", BeanKit.uuid());
        // 校验用户姓名选项	check_name 是
        param.put("check_name", "NO_CHECK");
        // 收款用户姓名
        // 企业付款描述信息	desc	是
        param.put("desc", "提现");
        // 用户openid	openid 是
        param.put("openid", openId);
        // 金额	amount	是 单位分
        param.put("amount", operMoney.multiply(new BigDecimal("100")).longValue() + "");
        // Ip地址	spbill_create_ip	是
        param.put("spbill_create_ip", ip);
        // 商户订单号	partner_trade_no 是
        param.put("partner_trade_no", orderNo);
        logger.info("map===> ", param);
        String sign = PaymentUtils.createSign(param, paternerKey);
        param.put("sign", sign);

        String xml = PaymentUtils.toXml(param);
        logger.info("发起提现请求-- url={},xml={}", WX_CASH_OUT_SERVER_URL, xml);
        String xmlResult = HttpUtils.postSSL(WX_CASH_OUT_SERVER_URL, xml, cerPath, mchId);
        return PaymentUtils.xmlToMap(xmlResult);
    }


    /**
     * 预提现
     * <p>初始化一个提现的流水记录</p>
     *
     * @param userId    用户id
     * @param operMoney 操作的金额
     * @return 流水id
     * @author Charlie
     * @date 2018/10/13 16:40
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long preCashOut(Long userId, BigDecimal operMoney) {
        logger.info("提现 userId={},operMoney={}", userId, operMoney);
        ErrorHelper.declare(operMoney != null && operMoney.compareTo(BigDecimal.ZERO) > 0, "提现金额不合法");
        ShopMemberAccount account = shopMemberAccountDao.findByUserId(userId);
        ErrorHelper.declareNull(account, "未找到用户账户");
        boolean lock = shopMemberAccountDao.lock(account.getId());
        ErrorHelper.declare(lock, "锁定账户失败,请稍后再试");


        //校验是否可进行提现操作
        verifyIsCanCashOut(account, operMoney);

        /* 记录流水,修改账户 */
        ShopMemAcctCashOutInQuery addInfo = new ShopMemAcctCashOutInQuery();
        addInfo.setUserId(userId);
        addInfo.setInOutType(2);
        addInfo.setOperCash(operMoney);
        addInfo.setOriginalCash(account.getAliveCash());
        addInfo.setOrderNo(IdGenerator.getCurrentTimeId("", String.format("%07d", userId)));
        addInfo.setStatus(CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode());
        addInfo.setDetailStatus(CashOutInStatusDetailEnum.WAIT.getCode());
        addInfo.setType(CashOutInTypeEnum.CASH_OUT_TOTAL.getCode());
        BigDecimal commissionAliveCash = account.getCommissionAliveCash();
        logger.info("commissionAliveCash={}, operMoney={}", commissionAliveCash, operMoney);
        if (commissionAliveCash.compareTo(operMoney) >= 0) {
            //佣金可用金足够
            addInfo.setCashOutCommission(operMoney);
            addInfo.setCashOutTeamIn(BigDecimal.ZERO);
        } else {
            //佣金+管理金
            addInfo.setCashOutCommission(commissionAliveCash);
            addInfo.setCashOutTeamIn(operMoney.subtract(commissionAliveCash));
        }
        //记录流水
//        Long newCashOutInId = addCashOutIn(account, addInfo, null);
        AccountDTO accountDTO = addCashOutIn(account, addInfo, null);
        Long newCashOutInId = accountDTO.getCashOutId();
        logger.info("预提现--新增提现中流水 newCashOutInId={}", newCashOutInId);
        ErrorHelper.declareNull(newCashOutInId, "新建提现流水失败");
        return newCashOutInId;
    }


    /**
     * 提现
     *
     * @param query query
     * @param rsMap wxResMap
     * @author Charlie
     * @date 2018/10/14 15:31
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doCashOutCallback(ShopMemberAccountCashOutIn query, Map<String, String> rsMap) {
        logger.info("提现--doCashOut  cashOutInId={},wxResMap={}", query.getId(), rsMap);
        ShopMemberAccount account = shopMemberAccountDao.findByUserId(query.getUserId());

        ShopMemberAccountCashOutIn cashOutIn = shopMemberAccountCashOutInDao.findCashOutById(query.getId());
        ErrorHelper.declare(CashOutInStatusDetailEnum.WAIT_GRANT.isThis(cashOutIn.getDetailStatus()), "流水状态异常");

        int lock = shopMemberAccountCashOutInDao.lock(cashOutIn.getId());
        ErrorHelper.declare(lock == 1, "流水锁定失败");

        boolean isWxSuccess = isWxSuccess(rsMap);
        logger.info("提现成功? -- {},cashOutInId={}", isWxSuccess, cashOutIn.getId());
        String errCodeDes = rsMap.get("err_code_des");
        String payNo = rsMap.get("payment_no");
        CashOutInUpdVo cashOutInUpdVo = new CashOutInUpdVo();
        cashOutInUpdVo.setPaymentNo(payNo);
        cashOutInUpdVo.setErrCodeDes(errCodeDes);
        //更新流水
        updCashOutIn(
                account,
                cashOutIn,
                isWxSuccess ? CashOutInUpdEnum.CASH_OUT_SUCCESS : CashOutInUpdEnum.CASH_OUT_FAILED,
                Optional.of(cashOutInUpdVo)
        );
        logger.info("更新提现成功,cashOutInId={}", cashOutIn.getId());
    }


    private boolean isWxSuccess(Map<String, String> wxResMap) {
        return "SUCCESS".equalsIgnoreCase(wxResMap.get("result_code"));
    }


    /**
     * 用户是否可以提现
     *
     * @param account   用户账号
     * @param operMoney 提现金额
     * @return boolean
     * @author Charlie
     * @date 2018/10/11 20:13
     */
    private void verifyIsCanCashOut_过时(ShopMemberAccount account, BigDecimal operMoney) {
        //提现限额
        DataDictionary limitMoneyDict = dataDictionaryRpcService.findByCodeAndGroupCode(
                DataDictionaryEnums.CASH_OUT_LIMIT_MONEY.getCode(),
                DataDictionaryEnums.CASH_OUT_LIMIT_MONEY.getGroupCode()
        );
        ErrorHelper.declareNull(limitMoneyDict, "未找到提现限额配置");

        BigDecimal limitMoney = new BigDecimal(limitMoneyDict.getVal());

        BigDecimal aliveCash = account.getAliveCash();
        logger.info("提现金额限制={},账户可用金={},操作金额={}", limitMoney, aliveCash, operMoney);
        ErrorHelper.declare(aliveCash.compareTo(operMoney) >= 0, "账户余额不足");
        if (operMoney.compareTo(limitMoney) >= 0) {
            //提现金额≥500元时，随时可提现
            return;
        } else {
            //余额不足限期提现
//            String limitTime = "yyyy-MM-dd HH+1,2,3,4,5,6,7";
            DataDictionary dict = dataDictionaryRpcService.findByCodeAndGroupCode(
                    DataDictionaryEnums.CASH_OUT_LIMIT_TIME.getCode(),
                    DataDictionaryEnums.CASH_OUT_LIMIT_TIME.getGroupCode()
            );
            ErrorHelper.declareNull(dict, "获取提现日期规则失败");
            String limitTime = dict.getVal();
            logger.info("提现日期限制={}", limitTime);
            ErrorHelper.declareNull(limitTime, "获取提现日期规则失败");
            String[] temp = limitTime.split("\\+");
            ErrorHelper.declare(temp.length == 2, "获取提现日期规则，格式错误");
            String limitDate = temp[0];
            String limitWeek = temp[1];

            //提现金额＜500元时，每周三可提现
            Date current = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current);
            int year = calendar.get(Calendar.YEAR);
            String verifyTime = limitDate.replace("yyyy", year + "");
            int month = calendar.get(Calendar.MONTH) + 1;
            verifyTime = verifyTime.replace("MM", month > 9 ? month + "" : "0" + month);
            int day = calendar.get(Calendar.DATE);
            verifyTime = verifyTime.replace("dd", day > 9 ? day + "" : "0" + day);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            verifyTime = verifyTime.replace("HH", hour > 9 ? hour + "" : "0" + hour);
            String nowTime = new SimpleDateFormat("yyyy-MM-dd HH").format(current);
            //这个校验先不做
//            ErrorHelper.declare (nowTime.equals (verifyTime), "未满足提现日期规则");

            int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            week = week == 0 ? 7 : week;
            ErrorHelper.declare(limitWeek.contains(week + ""), "未满足提现日期规则");
        }
    }


    /**
     * 用户是否可以提现
     *
     * @param account   用户账号
     * @param operMoney 提现金额
     * @return boolean
     * @author Charlie
     * @date 2018/10/11 20:13
     */
    private void verifyIsCanCashOut(ShopMemberAccount account, BigDecimal operMoney) {
        //提现限额
        DataDictionary limitMoneyDict = dataDictionaryRpcService.findByCodeAndGroupCode(
                DataDictionaryEnums.CASH_OUT_LIMIT_MONEY.getCode(),
                DataDictionaryEnums.CASH_OUT_LIMIT_MONEY.getGroupCode()
        );
        ErrorHelper.declareNull(limitMoneyDict, "未找到提现限额配置");

        //{"minMoney":"50","maxMoney":"20000","assignDateMaxMoney":"作废"}
        String comment = limitMoneyDict.getComment();
        logger.info("提现配置 config={}", comment);
        ErrorHelper.declareNull(comment, "网络繁忙,请稍后再试");
        JSONObject cfgJson = JSONObject.parseObject(comment);

        BigDecimal maxMoney = cfgJson.getBigDecimal("maxMoney");
        maxMoney = maxMoney == null ? new BigDecimal("20000") : maxMoney;

        BigDecimal minMoney = cfgJson.getBigDecimal("minMoney");
        minMoney = minMoney == null ? new BigDecimal("50") : minMoney;

        ErrorHelper.declare(maxMoney.compareTo(operMoney) >= 0, "最高提现金额" + maxMoney + "元");
        ErrorHelper.declare(minMoney.compareTo(operMoney) <= 0, "最低提现金额" + minMoney + "元");

        BigDecimal aliveCash = account.getAliveCash();
        logger.info("操作金额={}", aliveCash, operMoney);
        ErrorHelper.declare(aliveCash.compareTo(operMoney) >= 0, "账户余额不足");

        /*BigDecimal assignDateMaxMoney = cfgJson.getBigDecimal ("assignDateMaxMoney");
        assignDateMaxMoney = assignDateMaxMoney == null ? new BigDecimal ("500") : assignDateMaxMoney;
        if (operMoney.compareTo (assignDateMaxMoney) >= 0) {
            //提现金额≥500元时，随时可提现
            return;
        }
        else {
            //余额不足限期提现
//            String limitTime = "yyyy-MM-dd HH+1,2,3,4,5,6,7";
            DataDictionary dict = dataDictionaryRpcService.findByCodeAndGroupCode (
                    DataDictionaryEnums.CASH_OUT_LIMIT_TIME.getCode (),
                    DataDictionaryEnums.CASH_OUT_LIMIT_TIME.getGroupCode ()
            );
            ErrorHelper.declareNull (dict, "获取提现日期规则失败");
            String limitTime = dict.getVal ();
            logger.info ("提现日期限制={}", limitTime);
            ErrorHelper.declareNull (limitTime, "获取提现日期规则失败");
            String[] temp = limitTime.split ("\\+");
            ErrorHelper.declare (temp.length == 2, "获取提现日期规则，格式错误");
            String limitDate = temp[0];
            String limitWeek = temp[1];

            //提现金额＜500元时，每周三可提现
            Date current = new Date ();
            Calendar calendar = Calendar.getInstance ();
            calendar.setTime (current);
            int year = calendar.get (Calendar.YEAR);
            String verifyTime = limitDate.replace ("yyyy", year + "");
            int month = calendar.get (Calendar.MONTH) + 1;
            verifyTime = verifyTime.replace ("MM", month > 9 ? month + "" : "0" + month);
            int day = calendar.get (Calendar.DATE);
            verifyTime = verifyTime.replace ("dd", day > 9 ? day + "" : "0" + day);
            int hour = calendar.get (Calendar.HOUR_OF_DAY);
            verifyTime = verifyTime.replace ("HH", hour > 9 ? hour + "" : "0" + hour);
            String nowTime = new SimpleDateFormat ("yyyy-MM-dd HH").format (current);
            //这个校验先不做
//            ErrorHelper.declare (nowTime.equals (verifyTime), "未满足提现日期规则");

            int week = calendar.get (Calendar.DAY_OF_WEEK) - 1;
            week = week == 0 ? 7 : week;
            ErrorHelper.declare (limitWeek.contains (week + ""), "未满足提现日期规则");
        }*/
    }


    /**
     * 团队订单列表信息
     *
     * @param userId
     * @param page
     * @param orderNo
     * @return
     */
    @Override
    public Response teamOrderList(Long userId, Integer page, String orderNo) {
        logger.info("userId={}团队订单列表查询", userId);
        if (StringUtils.isEmpty(orderNo)) {
            orderNo = "-1";
        }
        List<TeamOrder> list = shopMemberOrderRpcService.findTeamOrderList(userId, page, orderNo);

        return Response.success(list);
    }

    /**
     * 团队订单信息
     *
     * @param userId
     * @return
     */
    @Override
    public Response teamOrderCount(Long userId) {
        logger.info("userId={}团队订单信息查询", userId);
        Integer todayTeamOrderSize = shopMemberOrderRpcService.findTodayTeamOrderSize(userId);
        CountTeamOrderMoneyCoinVo countTeamOrderMoneyCoinVo = shopMemberOrderRpcService.findCountTeamMoneyAndCoin(userId);
        Integer countOrder = shopMemberOrderRpcService.findCountOrderSize(userId);
        JSONObject jsonObject = new JSONObject();
        if (countTeamOrderMoneyCoinVo != null) {

            jsonObject.put("todaySize", todayTeamOrderSize);
            jsonObject.put("coin", countTeamOrderMoneyCoinVo.getCoin() == null ? 0d : countTeamOrderMoneyCoinVo.getCoin());
            jsonObject.put("money", countTeamOrderMoneyCoinVo.getMoney() == null ? 0d : countTeamOrderMoneyCoinVo.getMoney());
            jsonObject.put("countOrder", countOrder);
        } else {

            jsonObject.put("todaySize", 0);
            jsonObject.put("coin", 0);
            jsonObject.put("money", 0);
            jsonObject.put("countOrder", 0);
        }
        return Response.success(jsonObject);
    }

    /**
     * 团队订单信息详情
     *
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public Response teamOrderDetail(Long userId, String orderNo) {
        logger.info("orderNo={},userId={}订单信息详情", orderNo, userId);
        ShopMemberOrderDstbRecord shopMemberOrderDstbRecord = shopMemberOrderDstbRecordDao.findByOrderNo(orderNo);
        List<ShopMemberAccountCashOutIn> shopMemberAccountCashOutIn = shopMemberAccountCashOutInDao.findByOrderNo(orderNo, CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode());
        BigDecimal commissionCash = BigDecimal.ZERO;
        BigDecimal totalCommissionGoldCoin = BigDecimal.ZERO;
        BigDecimal totalManagerCash = BigDecimal.ZERO;
        BigDecimal totalManagerGoldCoin = BigDecimal.ZERO;
        for (ShopMemberAccountCashOutIn cashOutIn : shopMemberAccountCashOutIn) {
            //            0.自有订单分销返现,1.一级粉丝返现入账,2.二级粉丝返现入账,10.分销商的团队收益入账,11.合伙人的团队收益入账
            if (userId.equals(cashOutIn.getUserId())) {

                //佣金返现
                if (0 == cashOutIn.getType() || 1 == cashOutIn.getType() || 2 == cashOutIn.getType()) {
                    commissionCash = commissionCash.add(cashOutIn.getOperCash());
                    totalCommissionGoldCoin = totalCommissionGoldCoin.add(cashOutIn.getOperGoldCoin());

                }
                //管理奖返现
                if (10 == cashOutIn.getType() || 11 == cashOutIn.getType()) {
                    totalManagerCash = totalManagerCash.add(cashOutIn.getOperCash());
                    totalManagerGoldCoin = totalManagerGoldCoin.add(cashOutIn.getOperGoldCoin());

                }
            }
        }
        shopMemberOrderDstbRecord.setTotalCommissionCash(commissionCash);
        shopMemberOrderDstbRecord.setTotalCommissionGoldCoin(totalCommissionGoldCoin);
        shopMemberOrderDstbRecord.setTotalManagerCash(totalManagerCash);
        shopMemberOrderDstbRecord.setTotalManagerGoldCoin(totalManagerGoldCoin);
        OrderAccountDetailsResponse shopMemberOrder = shopMemberOrderRpcService.findTeamOrder(userId, orderNo);
        List<OrderItemSkuVo> orderItemSkuVo = shopMemberOrderRpcService.findTeamOrderItemSku(userId, orderNo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("order", shopMemberOrder);
        jsonObject.put("orderItemSku", orderItemSkuVo);
        jsonObject.put("record", shopMemberOrderDstbRecord);
        return Response.success(jsonObject);
    }

    @Override
    public ShopMemberAccount findByUser(Long userId) {

        return shopMemberAccountDao.findByUserId(userId);
    }

    @Override
    public void updateShopMemberAccountByUserId(ShopMemberAccount shopMemberAccount, Long userId) {
        shopMemberAccountDao.updateShopMemberAccountByUserId(shopMemberAccount, userId);
    }

    @Override
    public void saveShopMemberAccount(Long userId, BigDecimal coin) {
        ShopMemberAccount credit = new ShopMemberAccount();
        credit.setUserId(Long.valueOf(userId));
        credit.setAliveGoldCoin(coin);
        credit.setHistoryGoldCoinEarning(coin);
        credit.setSignGoldCoin(coin);
        credit.setAllGoldCoin(coin);
        shopMemberAccountDao.saveShopMemberAccount(credit);
    }

    /**
     * 账户金额-详情-收支详情
     *
     * @param id
     * @param userId
     * @return
     */
    @Override
    public Response findOrderAccountDetails(Long id, Long userId) {
        OrderAccountDetailsResponse orderAccountDetailsResponse = shopMemberAccountDao.findOrderAccountDetails(id, userId);
        if (orderAccountDetailsResponse != null) {
            if (orderAccountDetailsResponse.getOrderNumber() != null) {
                List<OrderItemSkuVo> orderItemSkuVos = shopMemberOrderRpcService.findTeamOrderItemSku(userId, orderAccountDetailsResponse.getOrderNumber());
                orderAccountDetailsResponse.setOrderItemSkuVoList(orderItemSkuVos);
                orderAccountDetailsResponse.setOperTime(TimeUtils.stamp2Str(Timestamp.valueOf(orderAccountDetailsResponse.getOperTime())));
                orderAccountDetailsResponse.setPayTime(TimeUtils.longFormatString(Long.valueOf(orderAccountDetailsResponse.getPayTime())));
                orderAccountDetailsResponse.setConfirmSignedTime(TimeUtils.longFormatString(Long.valueOf(orderAccountDetailsResponse.getConfirmSignedTime())));

            }
//            String operTime = orderAccountDetailsResponse.getOperTime();
//            String payTime = orderAccountDetailsResponse.getPayTime();
//            Integer type = orderAccountDetailsResponse.getType();
            orderAccountDetailsResponse.setType(IntegerTypeExchangeUtils.shopMemberAccountTypeChange(orderAccountDetailsResponse.getType()));
//            ShopMemberOrderDstbRecord shopMemberOrderDstbRecord = shopMemberOrderDstbRecordDao.findByOrderNo(orderAccountDetailsResponse.getOrderNumber());
//            shopMemberOrderDstbRecord.getTotalCommissionCash();
//            shopMemberOrderDstbRecord.getTotalManagerCash();
//            shopMemberOrderDstbRecord.getTotalCommissionGoldCoin();
//            shopMemberOrderDstbRecord.getTotalManagerGoldCoin();
//            orderAccountDetailsResponse.setCoin(shopMemberOrderDstbRecord.getTotalCommissionGoldCoin().add(shopMemberOrderDstbRecord.getTotalManagerGoldCoin()).doubleValue());
//            orderAccountDetailsResponse.setMoney(shopMemberOrderDstbRecord.getTotalManagerCash().add(shopMemberOrderDstbRecord.getTotalCommissionCash()).doubleValue());
        }

        return Response.success(orderAccountDetailsResponse);
    }


    /**
     * 提现申请审核
     * <p>后台提现,就一个一个提好了</p>
     *
     * @param cashOutId cashOutId
     * @param isPass    isPass
     * @param ip        ip
     * @author Charlie
     * @date 2018/10/30 20:29
     */
    @Override
    public void cashOutAudit(Long cashOutId, Integer isPass, String ip) {
        logger.info("审核提现申请 cashOutIn={}, isPass={}", cashOutId, isPass);
        ShopMemberAccountCashOutIn cashOutIn = shopMemberAccountCashOutInDao.findCashOutById(cashOutId);
        ErrorHelper.declareNull(cashOutId, "没有找到提现记录");

        Long userId = cashOutIn.getUserId();
        BigDecimal operMoney = cashOutIn.getOperCash();
        Map<String, String> resMap;
        if (isPass == 1) {
            //开始提现
            ErrorHelper.declare(CashOutInStatusDetailEnum.WAIT.isThis(cashOutIn.getDetailStatus()), "流水状态错误,或已提现");
            resMap = shopMemberAccountService.startCashOut(ip, cashOutIn, userId, operMoney);
            /* 微信提现后更新流水,账户 */
            shopMemberAccountService.doCashOutCallback(cashOutIn, resMap);
        }
//        else if (isPass == 0) {
//            //拒绝提现 暂时没辙业务
//            resMap = new HashMap<> (4);
//            resMap.put ("err_code_des", "微信提现审核不通过");
//            resMap.put ("payment_no", "-1");
//            resMap.put ("result_code", "failed");
//            /* 微信提现后更新流水,账户 */
//            shopMemberAccountService.doCashOutCallback (cashOutIn, resMap);
//        }
        else {
            //ignore
        }


    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> startCashOut(String ip, ShopMemberAccountCashOutIn cashOutIn, Long userId, BigDecimal operMoney) {

        Map<String, String> resMap;
        ShopMemberQuery query = new ShopMemberQuery();
        query.setId(userId);
        ShopMemberVo shopMember = shopMemberRpcService.findOne(query);
        ErrorHelper.declareNull(shopMember, "未找到用户");
        String openId = shopMember.getBindWeixin();
        ErrorHelper.declareNull(openId, "未找到用户信息");

        Long storeId = shopMember.getStoreId();


        StoreWxaVo config = wxaPayConfigRpcService.findByStoreId(storeId);
        logger.info("查找用户所在店家 storeId={},config={}", storeId, config);
        ErrorHelper.declareNull(config, 530, "店家尚未开通提现功能");
        String appId = config.getAppId();
        String mchId = config.getMchId();
        String paternerKey = config.getPayKey();
        String cerPath = config.getCerPath();
        if (BeanKit.hasNull(appId, mchId, paternerKey, cerPath)) {
            ErrorHelper.declare(false, "店家尚未开通提现功能");
        }

        //更新流水
        logger.info("更新流水状态,开始提现");
        updCashOutIn(null, cashOutIn, CashOutInUpdEnum.START_CASH, Optional.empty());


        //添加白名单
        if (WithdrawWhiteBackListUtil.exist(storeId)) {
            logger.info("商家Id={}在白名单中被过滤", storeId);
            return WithdrawWhiteBackListUtil.success(cashOutIn.getOrderNo());
        }

        /* 向微信请求提现 */
        try {
            logger.info("向微信请求提现");
            resMap = send2WeiXinCashOut(appId, mchId, openId, cashOutIn.getOrderNo(), paternerKey, cerPath, operMoney, ip);
        } catch (Exception e) {
            e.printStackTrace();

            logger.error("向微信发送提现申请失败 userId={},operMoney={}", userId, operMoney);
            DebugUtils.todo("模拟提现成功");
            resMap = new HashMap<>(4);
            resMap.put("err_code_des", "向微信返送提现http请求失败");
            resMap.put("payment_no", "-1");
            resMap.put("result_code", "failed");
//            resMap.put ("err_code_des", "模拟提现成功");
//            resMap.put ("payment_no", BeanKit.uuid ());
//            resMap.put ("result_code", "SUCCESS");
        }
        return resMap;
    }


    /**
     * 增加收益流水,根据流水类型
     *
     * @param requestData requestData
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/22 16:55
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addCashOutInByType(ShopMemAcctCashOutInQuery requestData) {
        Map<String, Object> result = new HashMap<>(4);

        Integer type = requestData.getType();
        logger.info("新增流水 type={},userId={}", type, requestData.getUserId());


        CashOutInTypeEnum typeEnum = CashOutInTypeEnum.find(type);
        if (typeEnum == null) {
            logger.error("未知的流水类型 type={}", type);
            return EmptyEnum.map();
        }


        ShopMemAcctCashOutInQuery addInfo = new ShopMemAcctCashOutInQuery();
        addInfo.setUserId(requestData.getUserId());
        addInfo.setType(type);
        switch (typeEnum) {
            case SHARE: {
                //分享
                addInfo.setOperGoldCoin(requestData.getOperGoldCoin());
                addInfo.setOperCash(requestData.getOperCash());
                addInfo.setFromId(requestData.getFromId());
                addInfo.setInOutType(1);
                addInfo.setDetailStatus(CashOutInStatusDetailEnum.SUCCESS.getCode());
                addInfo.setStatus(CashOutInStatusEnum.ALREADY_SETTLE_ACCOUNT.getCode());
                addInfo.setOperTime(System.currentTimeMillis());
//                Long cashOutIn = addCashOutIn(addInfo, null);
                AccountDTO accountDTO = addCashOutIn(addInfo, null);
                result.put("cashOutInId", accountDTO.getCashOutId());
                return result;
            }
            case NEW_USER_INVITEE: {
                //新用户被邀请登录获取收益
                newUserInviteeEarnings(requestData, addInfo);
                result.put("isOk", true);
                return result;
            }
            default:


        }

        return EmptyEnum.map();
    }


    /**
     * 新用户被邀请登录获取收益
     *
     * @param requestData requestData
     * @param addInfo     addInfo
     * @author Charlie
     * @date 2018/11/29 16:07
     */
    private void newUserInviteeEarnings(ShopMemAcctCashOutInQuery requestData, ShopMemAcctCashOutInQuery addInfo) {
        DataDictionary dict = dataDictionaryService.findByCodeAndGroupCode(DataDictionaryEnums.SHARE_PRODUCT_CONFIG.getCode(), DataDictionaryEnums.SHARE_PRODUCT_CONFIG.getGroupCode());
        ErrorHelper.declareNull(dict, "新用户被邀请登录获取收益,没有收益信息");
        logger.info("新用户被邀请登录获取收益,收益信息", dict.getComment());

        //查询收益
        JSONObject configJson = JSONObject.parseObject(dict.getComment());
        Integer typeSwitch = configJson.getInteger("typeSwitch");
        //做个兼容,默认现金生效
        typeSwitch = typeSwitch == null ? 2 : typeSwitch;

        BigDecimal acceptUserCashEarnings = BigDecimal.ZERO;
        BigDecimal acceptGoldEarnings = BigDecimal.ZERO;
        switch (typeSwitch) {
            case 1:
                //现金收益
                acceptUserCashEarnings = configJson.getBigDecimal("acceptUserCashEarnings");
                if (acceptUserCashEarnings == null || acceptUserCashEarnings.compareTo(BigDecimal.ZERO) < 0) {
                    ErrorHelper.declare(false, "收益配置错误");
                }
                break;
            case 2:
                //金币收益
                acceptGoldEarnings = configJson.getBigDecimal("acceptUserGoldCoinEarnings");
                if (acceptGoldEarnings == null || acceptGoldEarnings.compareTo(BigDecimal.ZERO) < 0) {
                    ErrorHelper.declare(false, "收益配置错误");
                }
                break;
            default:
                ErrorHelper.declare(false, "未知的收益类型");
        }


        //邀请者id
        Long sendUserId = requestData.getFromId();

        //获取收益
        addInfo.setOperCash(acceptUserCashEarnings);
        addInfo.setOperGoldCoin(acceptGoldEarnings);
        addInfo.setFromId(sendUserId);
        addInfo.setInOutType(1);
        addInfo.setDetailStatus(CashOutInStatusDetailEnum.SUCCESS.getCode());
        addInfo.setStatus(CashOutInStatusEnum.ALREADY_SETTLE_ACCOUNT.getCode());
        addInfo.setOperTime(System.currentTimeMillis());
        addCashOutIn(addInfo, null);

        //绑定关系
        Response res = distributionSystemService.bindingFans(sendUserId, requestData.getUserId());
        logger.info("新用户被邀请登录,绑定分销关系 res={}", res);
    }


}
