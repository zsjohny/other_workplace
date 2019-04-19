package com.e_commerce.miscroservice.order.service.system;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.enums.system.DataDictionaryEnums;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 14:36
 * @Copyright 玖远网络
 */
public interface DataDictionaryService{


    /**
     * 查询字典表
     * @param dictionaryEnums
     * @return
     */
    DataDictionary findDictionaryByCodeAndGroupCode(DataDictionaryEnums dictionaryEnums);

    List<DataDictionary> getByGroupAndLikeComment(String group, String comment);
}
