package com.jiuy.rb.service.common;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.enums.GlobalSettingEnum;
import com.jiuy.rb.model.common.GlobalSettingRb;


/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title 全局配置表service
 * @package jiuy-biz
 * @description
 * @date 2018/6/19 17:51
 * @copyright 玖远网络
 */
public interface IGlobalSettingService{

    /**
     * 根据propertyValue查询
     *
     * @param globalSetting 表对象
     * @return com.jiuy.rb.model.common.GlobalSettingRb
     * @auther Charlie(唐静)
     * @date 2018/6/19 18:03
     */
    GlobalSettingRb findByPropName(GlobalSettingEnum globalSetting);


    /**
     * 根据propertyValue查询
     *
     * @param propertyName 属性名
     * @return com.jiuy.rb.model.common.GlobalSettingRb
     * @auther Charlie(唐静)
     * @date 2018/6/19 18:03
     */
    GlobalSettingRb findByPropName(String propertyName);

    /**
     * 根据id更新全局表
     *
     * @param globalSetting 表对象
     * @param userSession 用户信息
     * @return com.jiuy.base.model.MyLog<com.jiuy.rb.model.common.GlobalSettingRb>
     * @author Charlie(唐静)
     * @date 2018/6/28 17:27
     */
    MyLog<GlobalSettingRb> updateByPrimaryKey(GlobalSettingRb globalSetting, UserSession userSession);


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
    MyLog<GlobalSettingRb> updPublishMoneyRecord(Number money, Number publishCount, UserSession userSession);
}
