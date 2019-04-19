package com.e_commerce.miscroservice.operate.service.system;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 16:06
 * @Copyright 玖远网络
 */
public interface DataDictionaryService{

    /**
     * 根据groupCode,code 更新字典表的值
     *
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/17 13:44
     */
    void updDataDictionaryByCodeAndGroupCode(DataDictionary dictionary);

    /**
     * 查询字典表
     * @param dictionary
     * @return
     */
    DataDictionary findDictionaryByCodeAndGroupCode(DataDictionary dictionary);



    /**
     * 分销角色升级条件
     *
     * @author Charlie
     * @date 2018/11/6 10:33
     */
    Map<String, DataDictionary> dstbRoleUpGradeFind();


    void test(String gCode, String code);


    void testUpd(String gCode, String code);

}
