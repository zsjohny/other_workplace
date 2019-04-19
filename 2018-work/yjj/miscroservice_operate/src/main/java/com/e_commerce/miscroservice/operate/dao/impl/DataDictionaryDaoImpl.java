package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.operate.dao.DataDictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 16:10
 * @Copyright 玖远网络
 */
@Service
public class DataDictionaryDaoImpl implements DataDictionaryDao{

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    /**
     * 根据groupCode,code 更新字典表的值
     *
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/17 13:44
     */
    @Override
    public void updDataDictionaryByCodeAndGroupCode(DataDictionary dictionary) {
        dataDictionaryDao.updDataDictionaryByCodeAndGroupCode (dictionary);
    }


    /**
     * 根据groupCode查找
     *
     * @param groupCode groupCode
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary>
     * @author Charlie
     * @date 2018/11/6 10:36
     */
    @Override
    public List<DataDictionary> findByGroup(String groupCode) {
        return BeanKit.hasNull (groupCode) ? EmptyEnum.list () :
                MybatisOperaterUtil.getInstance ().finAll (
                        new DataDictionary (),
                        new MybatisSqlWhereBuild (DataDictionary.class)
                                .eq (DataDictionary::getGroupCode, groupCode)
                );
    }
}
