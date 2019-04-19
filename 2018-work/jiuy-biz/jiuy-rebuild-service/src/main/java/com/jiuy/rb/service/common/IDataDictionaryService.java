package com.jiuy.rb.service.common;

import com.jiuy.rb.model.common.DataDictionaryRb;

import java.util.List;

/**
 * 字典
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/15 11:44
 * @Copyright 玖远网络
 */
public interface IDataDictionaryService {

    /**
     * 查找某一组字典
     * @param group_code  group_code
     * @date   2018/4/24 18:45
     * @author Aison
     */
    List<DataDictionaryRb> getDictionaryGroup(String group_code);


    /**
     * 通过code查询字典
     *
     * @param code code
     * @date   2018/4/24 18:43
     * @author Aison
     */
    DataDictionaryRb getByCode(String code,String groupCode);
}
