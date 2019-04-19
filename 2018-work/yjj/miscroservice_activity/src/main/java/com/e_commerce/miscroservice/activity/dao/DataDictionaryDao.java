package com.e_commerce.miscroservice.activity.dao;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.enums.system.DataDictionaryEnums;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 16:10
 * @Copyright 玖远网络
 */
public interface DataDictionaryDao{


    /**
     * 查询字典表
     *
     * @param dictionaryEnums dictionary
     * @return com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary
     * @author Charlie
     * @date 2018/10/23 16:00
     */
    DataDictionary findDictionaryByCodeAndGroupCode(DataDictionaryEnums dictionaryEnums);


    List<DataDictionary> findByGroup(String groupCode);
}
