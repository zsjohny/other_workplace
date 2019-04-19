package com.e_commerce.miscroservice.distribution.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.distribution.service.DataDictionaryService;
import org.springframework.stereotype.Service;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 16:35
 * @Copyright 玖远网络
 */
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService{
    @Override
    public DataDictionary findByCodeAndGroupCode(String code, String groupCode) {
        return MybatisOperaterUtil.getInstance().findOne(new DataDictionary(),new MybatisSqlWhereBuild (DataDictionary.class).eq(DataDictionary::getCode,code).eq(DataDictionary::getGroupCode,groupCode));
    }
}
