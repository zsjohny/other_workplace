package com.e_commerce.miscroservice.distribution.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.distribution.CashOutInUpdVo;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInStatusDetailEnum;
import com.e_commerce.miscroservice.commons.enums.distributionSystem.CashOutInStatusEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.distribution.dao.ShopMemberAccountCashOutInDao;
import com.e_commerce.miscroservice.distribution.service.ShopMemberAccountCashOutInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 20:01
 * @Copyright 玖远网络
 */
@Service
public class ShopMemberAccountCashOutInServiceImpl implements ShopMemberAccountCashOutInService{

    @Autowired
    private ShopMemberAccountCashOutInDao shopMemberAccountCashOutInDao;

    @Override
    public int insertSelective(ShopMemberAccountCashOutIn cashOutIn) {
        return shopMemberAccountCashOutInDao.insertSelective (cashOutIn);
    }

    /**
     * 查询某一订单的分销返利未结算的流水
     *
     * @param orderNo 订单号
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn>
     * @author Charlie
     * @date 2018/10/10 20:59
     */
    @Override
    public List<ShopMemberAccountCashOutIn> selectOrderWaitCommission(String orderNo) {
        return MybatisOperaterUtil.getInstance ().finAll (
                new ShopMemberAccountCashOutIn (),
                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                        .in (ShopMemberAccountCashOutIn::getType, 0, 1, 2)
                        .eq (ShopMemberAccountCashOutIn::getStatus, CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode ())
                        .eq (ShopMemberAccountCashOutIn::getOrderNo, orderNo)
                        .eq (ShopMemberAccountCashOutIn::getInOutType, 1)
                        .eq (ShopMemberAccountCashOutIn::getDelStatus, StateEnum.NORMAL)
        );
    }


    /**
     * 查询所有待结算的团队收益流水
     *
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn>
     * @author Charlie
     * @date 2018/10/10 21:48
     */
    @Override
    public List<ShopMemberAccountCashOutIn> listWaitTeamIn() {
        return MybatisOperaterUtil.getInstance ().finAll (
                new ShopMemberAccountCashOutIn (),
                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                        .in (ShopMemberAccountCashOutIn::getType, 10, 11)
                        .eq (ShopMemberAccountCashOutIn::getStatus, CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode ())
                        .eq (ShopMemberAccountCashOutIn::getDetailStatus, CashOutInStatusDetailEnum.WAIT_GRANT.getCode ())
                        .eq (ShopMemberAccountCashOutIn::getDelStatus, StateEnum.NORMAL)
                        .eq (ShopMemberAccountCashOutIn::getInOutType, 1)
        );
    }


    /**
     * 提现成功
     *
     * @param history     history
     * @param updVo       updVo
     * @param balanceCash 提现的余额
     * @param isSuccess   1成功,-1失败
     * @return int
     * @author Charlie
     * @date 2018/10/14 16:12
     */
    @Override
    public int cashOutSuccess(ShopMemberAccountCashOutIn history, CashOutInUpdVo updVo, BigDecimal balanceCash, Integer isSuccess) {
        ShopMemberAccountCashOutIn updInfo = new ShopMemberAccountCashOutIn ();
        if (updVo != null) {
            updInfo.setRemark (updVo.getErrCodeDes ());
            updInfo.setPaymentNo (updVo.getPaymentNo ());
        }
        updInfo.setBalanceCash (balanceCash);
        updInfo.setOperTime (System.currentTimeMillis ());
        switch (isSuccess) {
            case 1:
                //成功
                updInfo.setStatus (CashOutInStatusEnum.ALREADY_SETTLE_ACCOUNT.getCode ());
                updInfo.setDetailStatus (CashOutInStatusDetailEnum.SUCCESS.getCode ());
                updInfo.setOperTime (System.currentTimeMillis ());
                break;
            case -1:
                //失败
                updInfo.setStatus (CashOutInStatusEnum.ALREADY_SETTLE_ACCOUNT.getCode ());
                updInfo.setDetailStatus (CashOutInStatusDetailEnum.FAILED.getCode ());
                break;
                default:
                    throw ErrorHelper.me ("未知的提现状态类型");
        }

        return MybatisOperaterUtil.getInstance ().update (
                updInfo,
                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                        .in (ShopMemberAccountCashOutIn::getDelStatus, 0, 2, 3)
                        .eq (ShopMemberAccountCashOutIn::getId, history.getId ())
                        .eq (ShopMemberAccountCashOutIn::getOrderNo, history.getOrderNo ())
                        .eq (ShopMemberAccountCashOutIn::getStatus, CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode ())
        );
    }


    /**
     * 根据id查找
     *
     * @param cashOutId cashOutId
     * @return com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn
     * @author Charlie
     * @date 2018/10/14 22:18
     */
    @Override
    public ShopMemberAccountCashOutIn findById(Long cashOutId) {
        return cashOutId == null ? null : MybatisOperaterUtil.getInstance ().findOne (
                new ShopMemberAccountCashOutIn (),
                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                        .eq (ShopMemberAccountCashOutIn::getId, cashOutId)
        );
    }


    /**
     * 根据订单号查询预结算的管理金
     *
     * @param orderNo orderNo
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn>
     * @author Charlie
     * @date 2018/10/16 18:06
     */
    @Override
    public List<ShopMemberAccountCashOutIn> selectOrderWaitManager(String orderNo) {
        return MybatisOperaterUtil.getInstance ().finAll (
                new ShopMemberAccountCashOutIn (),
                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                        .in (ShopMemberAccountCashOutIn::getType, 10, 11)
                        .eq (ShopMemberAccountCashOutIn::getStatus, CashOutInStatusEnum.WAIT_SETTLE_ACCOUNT.getCode ())
                        .eq (ShopMemberAccountCashOutIn::getDetailStatus, CashOutInStatusDetailEnum.WAIT.getCode ())
                        .eq (ShopMemberAccountCashOutIn::getOrderNo, orderNo)
                        .eq (ShopMemberAccountCashOutIn::getInOutType, 1)
                        .eq (ShopMemberAccountCashOutIn::getDelStatus, StateEnum.NORMAL)
        );
    }


    /**
     * 更新状态
     *
     * @param id           id
     * @param sourceStatus 原来状态
     * @param targetStatus 更新后状态
     * @param operTime     操作时间
     * @param inOutType    收入支出类型
     * @return int
     * @author Charlie
     * @date 2018/10/16 20:06
     */
    @Override
    public int updStatus(Long id, int sourceStatus, int targetStatus, long operTime, Integer inOutType, int sourceDetailStatus, int targetDetailStatus) {
        if (BeanKit.notNull (id)) {
            ShopMemberAccountCashOutIn upd = new ShopMemberAccountCashOutIn ();
            upd.setOperTime (operTime);
            upd.setStatus (targetStatus);
            upd.setInOutType (inOutType);
            upd.setDetailStatus (targetDetailStatus);
            return MybatisOperaterUtil.getInstance ().update (
                    upd,
                    new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
                            .eq (ShopMemberAccountCashOutIn::getId, id)
                            .eq (ShopMemberAccountCashOutIn::getStatus, sourceStatus)
                            .eq (ShopMemberAccountCashOutIn::getDetailStatus, sourceDetailStatus)
                            .eq (ShopMemberAccountCashOutIn::getDelStatus, StateEnum.NORMAL)
            );
        }
        return 0;
    }


//    /**
//     * 查询该用户在订单下该订单下已结算的分佣流水数量
//     *
//     * @param userId  userId
//     * @param orderNo orderNo
//     * @return int
//     * @author Charlie
//     * @date 2018/10/16 23:27
//     */
//    @Override
//    public long countUserHasSettleOutInByOrder(Long userId, String orderNo) {
//        return MybatisOperaterUtil.getInstance ().count (
//                new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class)
//                        .in (ShopMemberAccountCashOutIn::getType, 0, 1, 2, 10, 11)
//                        .eq (ShopMemberAccountCashOutIn::getUserId, userId)
//                        .eq (ShopMemberAccountCashOutIn::getOrderNo, orderNo)
//                        .eq (ShopMemberAccountCashOutIn::getStatus, CashOutInStatusEnum.ALREADY_SETTLE_ACCOUNT.getCode ())
//
//        );
//    }

    @Override
    public List<ShopMemberAccountCashOutIn> findLogList(Integer page, Long userId) {
        return shopMemberAccountCashOutInDao.findLogList (page, userId);
    }

    @Override
    public List<ShopMemberAccountCashOutIn> findPeriodicalPrizeMonthLog(Long userId) {
        return shopMemberAccountCashOutInDao.findPeriodicalPrizeMonthLog (userId);
    }

    /**
     * 保存账户流水
     *
     * @param shopMemberAccount
     * @return
     */
    @Override
    public void saveAccountLog(ShopMemberAccountCashOutIn shopMemberAccount) {
        shopMemberAccountCashOutInDao.insertSelective (shopMemberAccount);
    }



    /**
     * 历史金币,现金收益
     *
     * @param userId userId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/14 18:06
     */
    @Override
    public Map<String, BigDecimal> findHistoryShareEarnings(Long userId) {
        return shopMemberAccountCashOutInDao.findHistoryShareEarnings (userId);
    }



    /**
     * 最近分享
     *
     * @return java.util.List
     * @author Charlie
     * @date 2018/12/15 11:42
     */
    @Override
    public List<Map<String, Object>> recentlyPlatformShares() {
        return shopMemberAccountCashOutInDao.recentlyPlatformShares ();
    }


}
