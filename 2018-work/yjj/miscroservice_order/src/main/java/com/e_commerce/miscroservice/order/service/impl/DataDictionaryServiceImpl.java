package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.enums.system.DataDictionaryEnums;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.order.service.system.DataDictionaryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 14:43
 * @Copyright 玖远网络
 */
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService{


    @Override
    public DataDictionary findDictionaryByCodeAndGroupCode(DataDictionaryEnums dictionaryEnums) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new DataDictionary(),
                new MybatisSqlWhereBuild (DataDictionary.class)
                        .eq (DataDictionary::getGroupCode, dictionaryEnums.getGroupCode ())
                        .eq (DataDictionary::getCode, dictionaryEnums.getCode ())
        );
    }

    @Override
    public List<DataDictionary> getByGroupAndLikeComment(String group, String comment) {
        return MybatisOperaterUtil.getInstance ().finAll (
                new DataDictionary(),
                new MybatisSqlWhereBuild (DataDictionary.class)
                        .eq (DataDictionary::getGroupCode, group)
                        .like (DataDictionary::getComment, comment)
        );
    }
}
