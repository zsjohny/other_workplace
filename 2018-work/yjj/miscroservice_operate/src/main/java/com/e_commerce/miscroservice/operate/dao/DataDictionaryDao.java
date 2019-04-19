package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 16:10
 * @Copyright 玖远网络
 */
public interface DataDictionaryDao{

    /**
     * 根据groupCode,code 更新字典表的值
     *
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/17 13:44
     */
    void updDataDictionaryByCodeAndGroupCode(DataDictionary dictionary);

    List<DataDictionary> findByGroup(String groupCode);
}
