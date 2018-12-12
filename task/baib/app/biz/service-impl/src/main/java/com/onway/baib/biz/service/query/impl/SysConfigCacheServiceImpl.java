package com.onway.baib.biz.service.query.impl; /**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */


import com.onway.baib.biz.service.query.SysConfigCacheService;
import com.onway.baib.core.enums.SysConfigCacheKeyEnum;
import com.onway.baib.core.model.SysConfigModel;
import com.onway.car.biz.service.inner.base.AbstractOperateService;
import com.onway.platform.common.base.QueryResult;
import com.onway.platform.common.service.template.ServiceCheckCallback;
import com.onway.platform.common.service.util.AssertUtil;

/**
 * 缓存配置服务
 * 
 * @author guangdong.li
 * @version $Id: SysConfigCacheServiceImpl.java, v 0.1 17 Feb 2016 15:43:14 guangdong.li Exp $
 */
public class SysConfigCacheServiceImpl extends AbstractOperateService implements
        SysConfigCacheService {

    public QueryResult<SysConfigModel> getConfigValue(final SysConfigCacheKeyEnum configKeyEnum) {
        final QueryResult<SysConfigModel> result = new QueryResult<SysConfigModel>(true);

        serviceTemplate.executeWithoutTransaction(result, new ServiceCheckCallback() {

            @Override
            public void check() {
                AssertUtil.notNull(configKeyEnum, "参数枚举为空!");
            }

            @Override
            public void executeService() {
                //【1】 调用核心服务查询本地缓存
                String configvalue = sysConfigCacheManager.getConfigValue(configKeyEnum);
                SysConfigModel sysConfigModel = new SysConfigModel();
                sysConfigModel.setConfigName(configKeyEnum.name());
                sysConfigModel.setConfigValue(configvalue);

                //【2】 设置返回结果
                result.setResultObject(sysConfigModel);
            }

        });

        return result;
    }

}
