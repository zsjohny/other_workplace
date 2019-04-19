package com.jiuy.rb.service.impl.common;

import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.enums.ModelType;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.enums.GlobalSettingEnum;
import com.jiuy.rb.mapper.common.GlobalSettingRbMapper;
import com.jiuy.rb.model.common.GlobalSettingRb;
import com.jiuy.rb.model.common.GlobalSettingRbQuery;
import com.jiuy.rb.service.common.IGlobalSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title 全局配置表
 * @package jiuy-biz
 * @description
 * @date 2018/6/19 17:53
 * @copyright 玖远网络
 */
@Service("globalSettingService")
public class GlobalSettingServiceImpl implements IGlobalSettingService{

    @Autowired
    private GlobalSettingRbMapper globalSettingRbMapper;


    /**
     * 优惠券发行总金额
     */
    private static final String PUBLISHED_MONEY = "published_money";
    /** 账户总额 */
    private static final String TOTAL_MONEY = "total_money";

    /**
     * 根据propertyValue查询
     *
     * @param globalSetting object
     * @return com.jiuy.rb.model.common.GlobalSettingRb
     * @auther Charlie(唐静)
     * @date 2018/6/19 18:03
     */
    @Override
    public GlobalSettingRb findByPropName(GlobalSettingEnum globalSetting) {
        GlobalSettingRbQuery query = new GlobalSettingRbQuery();
        query.setPropertyName(globalSetting.getPropertyValue());
        return globalSettingRbMapper.selectOne(query);
    }



    /**
     * 根据propertyValue查询
     *
     * @param propertyName 属性名
     * @return com.jiuy.rb.model.common.GlobalSettingRb
     * @auther Charlie(唐静)
     * @date 2018/6/19 18:03
     */
    @Override
    public GlobalSettingRb findByPropName(String propertyName) {

        GlobalSettingRb query = new GlobalSettingRb();
        query.setPropertyName(propertyName);
        return globalSettingRbMapper.selectOne(query);
    }



    /**
     * 根据id更新全局表
     *
     * @param globalSetting 全局表对象
     * @param userSession 用户信息
     * @return com.jiuy.base.model.MyLog<com.jiuy.rb.model.common.GlobalSettingRb>
     * @author Charlie(唐静)
     * @date 2018/6/28 17:27
     */
    @MyLogs(logInfo = "更新全局表", model = ModelType.COMMON)
    @Override
    public MyLog<GlobalSettingRb> updateByPrimaryKey(GlobalSettingRb globalSetting, UserSession userSession) {
        globalSettingRbMapper.updateByPrimaryKey(globalSetting);
        return new MyLog<>(globalSetting, MyLog.Type.up, userSession);
    }



    /**
     * 更新账户发行金额
     *
     * @param money 优惠券模版面额
     * @param publishCount 数量
     * @param userSession 用户信息
     * @return MyLog<GlobalSettingRb>
     * @author Charlie(唐静)
     * @date 2018/6/28 16:50
     */
    @MyLogs(logInfo = "更新账户发行金额", model = ModelType.COMMON)
    @Override
    public MyLog<GlobalSettingRb> updPublishMoneyRecord(Number money, Number publishCount, UserSession userSession) {

        // 校验余额充裕
        GlobalSettingRb globalSetting = findByPropName(GlobalSettingEnum.STORE_COUPON_PUBLISH_BALANCE);
        Declare.existResource(globalSetting);
        boolean isEnough = isBalanceEnough4Publish(money, publishCount, globalSetting);
        Declare.state(isEnough, "账户余额不足,请充值后在发行优惠券");

        /* 更新全局表 */
        Map<String, Object> publishHistory = Biz.jsonStrToMap(globalSetting.getPropertyValue());
        // 历史发行金额
        BigDecimal publishMoney = new BigDecimal(publishHistory.get(PUBLISHED_MONEY).toString());
        // 预发行金额
        BigDecimal estimatedMoney = Biz.multiply(money, publishCount);
        // 更新后的发行金额
        publishMoney = publishMoney.add(estimatedMoney);

        publishHistory.put(PUBLISHED_MONEY, publishMoney.toString());
        globalSetting.setPropertyValue(Biz.obToJson(publishHistory));
        globalSetting.setUpdateTime(System.currentTimeMillis());
        globalSettingRbMapper.updateByPrimaryKey(globalSetting);
        return new MyLog<>(globalSetting, MyLog.Type.add, userSession);
    }



    /**
     * 发放优惠券, 余额是否充足
     *
     * @param unitPrice 单价
     * @param count 数量
     * @return boolean
     * @author Charlie(唐静)
     * @date 2018/6/28 15:34
     */
    private boolean isBalanceEnough4Publish(Number unitPrice, Number count, GlobalSettingRb globalSetting) {
        // 预算金额
        BigDecimal estimatedMoney = Biz.multiply(unitPrice, count);

        Map<String, Object> publishHistory = Biz.jsonStrToMap(globalSetting.getPropertyValue());
        // 历史发行金额
        BigDecimal publishMoney = new BigDecimal(publishHistory.get(PUBLISHED_MONEY).toString());
        // 账户总金额
        BigDecimal totalMoney = new BigDecimal(publishHistory.get(TOTAL_MONEY).toString());
        return totalMoney.compareTo(publishMoney.add(estimatedMoney)) >= 0;
    }



}
