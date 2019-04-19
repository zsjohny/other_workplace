package com.e_commerce.miscroservice.distribution.service.impl;

import com.e_commerce.miscroservice.commons.annotations.application.AnnotationUtils;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy;
import com.e_commerce.miscroservice.commons.entity.distribution.EarningsStrategyVo;
import com.e_commerce.miscroservice.commons.entity.distribution.EarningsVo;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.distribution.dao.DstbEarningsStrategyDao;
import com.e_commerce.miscroservice.distribution.service.DstbEarningsStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import org.springframework.stereotype.Service;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/8 15:12
 * @Copyright 玖远网络
 */
@Service
public class DstbEarningsStrategyServiceImpl implements DstbEarningsStrategyService{


    private Log logger = Log.getInstance (DstbEarningsStrategyServiceImpl.class);

    @Autowired
    private DstbEarningsStrategyDao dstbEarningsStrategyDao;


    /**
     * 计算分销返利
     *
     * @param strategyList         策略源
     * @param earningsType         0:本人下单返利,1:一级粉丝下单返利,2:二级粉丝下单返利,10:分销商团队收益
     * @param userRoleType         0 无等级 1 店长 2分销商 3合伙人
     * @param realPay              实际支付金额
     * @param goldCoinExchangeRate goldCoinExchangeRate
     * @return 收益情况
     * @author Charlie
     * @date 2018/10/9 19:05
     */
    @Override
    public Optional<EarningsVo> calculateEarnings(List<DstbEarningsStrategy> strategyList, Integer earningsType, Integer userRoleType, BigDecimal realPay, BigDecimal goldCoinExchangeRate) {
        logger.info ("计算分销收益:  收益类型 = " + earningsType + ", 用户分销角色 = " + userRoleType + ", 收益来源金额(订单或佣金) = " + realPay + ", 人民币金币汇率 = " + goldCoinExchangeRate + "");
        if (! ObjectUtils.isEmpty (strategyList) && realPay != null && realPay.compareTo (BigDecimal.ZERO) > 0) {
            for (DstbEarningsStrategy strategy : strategyList) {
                if (ObjectUtils.nullSafeEquals (strategy.getEarningsType (), earningsType)
                        && ObjectUtils.nullSafeEquals (strategy.getUserRoleType (), userRoleType)) {
                    //策略相同时
                    BigDecimal currencyRatio = strategy.getCurrencyRatio ();
                    int dcmStratety = BigDecimal.ROUND_DOWN;
                    logger.info ("计算分销收益:使用计算策略--收益比例={},收益RMB金币比例={}", strategy.getEarningsRatio (), currencyRatio);
                    BigDecimal totalMoney = realPay.multiply (strategy.getEarningsRatio ()).setScale (2, dcmStratety);
                    logger.info ("来自订单总收益({})=收益来源金额({})X收益比例({})", totalMoney, realPay, strategy.getEarningsRatio ());
                    BigDecimal goldCoin = totalMoney.multiply (currencyRatio).multiply (goldCoinExchangeRate).setScale (2, dcmStratety);
                    logger.info ("来自订单金币收益({})=来自订单总收益({})X收益RMB金币比例({})X人民币金币汇率({})", goldCoin, totalMoney, currencyRatio, goldCoinExchangeRate);
                    BigDecimal currencyRatio4Cash = BigDecimal.ONE.subtract (currencyRatio);
                    BigDecimal cash = totalMoney.multiply (currencyRatio4Cash).setScale (2, dcmStratety);
                    logger.info ("来自订单现金收益({})=来自订单总收益({})X收益RMB金币比例({})", cash, totalMoney, currencyRatio4Cash);
                    EarningsVo vo = new EarningsVo (totalMoney, goldCoin, cash, strategy, userRoleType);
                    return Optional.of (vo);
                }
            }
        }
        logger.info ("没有收益");
        return Optional.empty ();
    }

    /**
     * 查找所有策略
     *
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.distributionSystem.DstbEarningsStrategy>
     * @author Charlie
     * @date 2018/10/17 13:48
     */
    @Override
    public List<DstbEarningsStrategy> findAll() {
        return MybatisOperaterUtil.getInstance ().finAll (
                new DstbEarningsStrategy (), new MybatisSqlWhereBuild (DstbEarningsStrategy.class).eq (DstbEarningsStrategy::getDelStatus, StateEnum.NORMAL)
        );
    }


    /**
     * 更新策略
     *
     * @param strategyRequestList 待更新信息
     * @author Charlie
     * @date 2018/10/17 13:49
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void update(List<EarningsStrategyVo> strategyRequestList) {
        logger.info ("更新策略 strategyRequestList={}", strategyRequestList);
        if (ObjectUtils.isEmpty (strategyRequestList)) {
            return;
        }

        List<DstbEarningsStrategy> updInfoList = new ArrayList<> (12);
        //校验必填,组装更新信息
        for (EarningsStrategyVo strategyRequest : strategyRequestList) {
            AnnotationUtils.Result result = AnnotationUtils.verifyNull (strategyRequest);
            ErrorHelper.declare (result.noProblem (), result.msg ());
            List<DstbEarningsStrategy> strategyList = strategyRequest.format2Entity ();
            if (! ObjectUtils.isEmpty (strategyList)) {
                updInfoList.addAll (strategyList);
            }
        }

        for (DstbEarningsStrategy strategy : updInfoList) {
            int rec = dstbEarningsStrategyDao.updateByUserRoleTypeAndEarningsType (strategy);
            logger.info ("更新策略 UserRoleType={},EarningsType={},rec={}", strategy.getUserRoleType (), strategy.getEarningsType (), rec);
        }
    }




    /**
     * 查询所有策略
     *
     * @author Charlie
     * @date 2018/10/17 14:58
     */
    @Override
    public Map<String, EarningsStrategyVo> allStrategy() {
        List<DstbEarningsStrategy> all = dstbEarningsStrategyDao.findAll ();
        logger.info ("查询所有策略 size={}", all.size ());
        if (all.isEmpty ()) {
            return EmptyEnum.map ();
        }
        return EarningsStrategyVo.build (all);
    }





}
