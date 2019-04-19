package com.e_commerce.miscroservice.distribution.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccount;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.entity.order.OrderAccountDetailsResponse;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.distribution.dao.ShopMemberAccountDao;
import com.e_commerce.miscroservice.distribution.mapper.ShopMemberAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/10 17:18
 * @Copyright 玖远网络
 */
@Component
public class ShopMemberAccountDaoImpl implements ShopMemberAccountDao{

    private Log logger = Log.getInstance (ShopMemberAccountDaoImpl.class);

    @Autowired
    private ShopMemberAccountMapper shopMemberAccountMapper;

    @Override
    public ShopMemberAccount findByUserId(Long userId) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new ShopMemberAccount (),
                new MybatisSqlWhereBuild (ShopMemberAccount.class)
                        .eq (ShopMemberAccount::getUserId, userId)
                        .eq (ShopMemberAccount::getDelStatus, StateEnum.NORMAL)
        );
    }

    /**
     * 待结算分佣入账
     *
     * @param id           id
     * @param operCash     待结算金额
     * @param operGoldCoin 待结算金币
     * @return int
     * @author Charlie
     * @date 2018/10/10 18:47
     */
    @Override
    public int updateCommissionWaitCashAndGoldCoin(Long id, BigDecimal operCash, BigDecimal operGoldCoin) {
        return shopMemberAccountMapper.updateCommissionWaitCashAndGoldCoin (id, operCash, operGoldCoin);
    }

    /**
     * 待结算管理金入账
     *
     * @param id           id
     * @param operCash     待结算金额
     * @param operGoldCoin 待结算金币
     * @return int
     * @author Charlie
     * @date 2018/10/10 18:49
     */
    @Override
    public int updateManagerWaitCashAndGoldCoin(Long id, BigDecimal operCash, BigDecimal operGoldCoin) {
        return shopMemberAccountMapper.updateManagerWaitCashAndGoldCoin (id, operCash, operGoldCoin);

    }



    /**
     * 分销返现待结算入账
     *
     * @param id                  账户id
     * @param operCash            operCash
     * @param operGoldCoin        operGoldCoin
     * @return void
     * @author Charlie
     * @date 2018/10/10 21:18
     */
    @Override
    public int waitCommission2Alive(Long id, BigDecimal operCash, BigDecimal operGoldCoin) {
        return shopMemberAccountMapper.updateWaitCommission2Alive (id, operCash, operGoldCoin);
    }


    /**
     * 分销管理金待结算入账
     *
     * @param id           账户id
     * @param operCash     operCash
     * @param operGoldCoin operGoldCoin
     * @return int
     * @author Charlie
     * @date 2018/10/10 21:53
     */
    @Override
    public int waitTeamIn2Alive(Long id, BigDecimal operCash, BigDecimal operGoldCoin) {
        return shopMemberAccountMapper.updateWaitTeamIn2Alive (id, operCash, operGoldCoin);
    }


    /**
     * 获取锁
     *
     * @param id 主键
     * @return boolean
     * @author Charlie
     * @date 2018/10/12 17:57
     */
    @Override
    public boolean lock(Long id) {
        return shopMemberAccountMapper.lock (id) == 1;
    }


    /**
     * 账户预提现
     *
     * @param accountId      账户id
     * @param operCash       提现金额
     * @param fromCommission 从佣金提现
     * @param fromTeamIn     用管理金提现
     * @param isRollBack     是否回滚(提现失败,金额回滚)
     * @return int
     * @author Charlie
     * @date 2018/10/13 22:56
     */
    @Override
    public int accountPreCashOut(Long accountId, BigDecimal operCash, BigDecimal fromCommission, BigDecimal fromTeamIn, boolean isRollBack) {
        if (isRollBack) {
            operCash = BigDecimal.ZERO.subtract (operCash);
            fromCommission = BigDecimal.ZERO.subtract (fromCommission);
            fromTeamIn = BigDecimal.ZERO.subtract (fromTeamIn);
        }
        logger.info ("预提现 是否回滚={}, 操作金额={},操作分佣={}操作管理金={}", isRollBack, operCash, fromCommission, fromTeamIn);
        return shopMemberAccountMapper.accountPreCashOut (accountId, operCash, fromCommission, fromTeamIn);
    }


    /**
     * 更新账户总现金
     *
     * @param id       id
     * @param operCash 操作金额
     * @param isAdd    加钱还是减钱
     * @return int
     * @author Charlie
     * @date 2018/10/14 16:31
     */
    @Override
    public int cashOutSuccess(Long id, BigDecimal operCash, boolean isAdd) {
        operCash = isAdd ? operCash : BigDecimal.ZERO.subtract (operCash);
        logger.info ("提现 id={},operCash={}", id, operCash);
        return shopMemberAccountMapper.cashOutSuccess (id, operCash);
    }


    /**
     * 根据订单号查询
     *
     * @param orderNo orderNo
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn>
     * @author Charlie
     * @date 2018/10/15 10:43
     */
    @Override
    public List<ShopMemberAccountCashOutIn> selectByOrder(String orderNo, Integer status, Integer detailStatus, Integer... types) {
        MybatisSqlWhereBuild where = new MybatisSqlWhereBuild (ShopMemberAccountCashOutIn.class);
        where.eq (ShopMemberAccountCashOutIn::getOrderNo, orderNo);
        if (BeanKit.notNull (types) && types.length > 0) {
            where.in (ShopMemberAccountCashOutIn::getType, types);
        }
        if (BeanKit.notNull (status)) {
            where.eq (ShopMemberAccountCashOutIn::getStatus, status);
        }
        if (BeanKit.notNull (detailStatus)) {
            where.eq (ShopMemberAccountCashOutIn::getDetailStatus, detailStatus);
        }
        return MybatisOperaterUtil.getInstance ().finAll (
                new ShopMemberAccountCashOutIn (),
                where
        );
    }

    @Override
    public void updateShopMemberAccountByUserId(ShopMemberAccount shopMemberAccount, Long userId) {
        MybatisOperaterUtil.getInstance ().update (shopMemberAccount, new MybatisSqlWhereBuild (ShopMemberAccount.class).eq (ShopMemberAccount::getUserId, userId));
    }

    @Override
    public void saveShopMemberAccount(ShopMemberAccount shopMemberAccount) {
        MybatisOperaterUtil.getInstance ().save (shopMemberAccount);
    }

    /**
     * 账户金额-详情-收支详情
     *
     * @param id
     * @param userId
     * @return
     */
    @Override
    public OrderAccountDetailsResponse findOrderAccountDetails(Long id, Long userId) {

        return shopMemberAccountMapper.findOrderAccountDetails(id);
    }




    /**
     * 账户增加可用的金币或现金,并且加到佣金中
     *
     * @param accountId 账号id
     * @param operCash 操作金额
     * @param operGoldCoin 操作现金
     * @param isRollback 是否回滚
     * @author Charlie
     * @date 2018/11/22 16:52
     */
    @Override
    public int addAliveGoldCashAndCommissionCashGold(Long accountId, BigDecimal operCash, BigDecimal operGoldCoin, Boolean isRollback) {
        operCash = operCash == null ? BigDecimal.ZERO : operCash;
        operGoldCoin = operGoldCoin == null ? BigDecimal.ZERO : operGoldCoin;
        if (isRollback) {
            operCash = BigDecimal.ZERO.subtract (operCash);
            operGoldCoin = BigDecimal.ZERO.subtract (operGoldCoin);
        }
        return shopMemberAccountMapper.addAliveGoldCashAndCommissionCashGold (accountId, operCash, operGoldCoin);
    }


}
