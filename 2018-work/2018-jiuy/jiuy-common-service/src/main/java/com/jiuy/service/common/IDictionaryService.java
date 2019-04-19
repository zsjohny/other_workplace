package com.jiuy.service.common;

import com.jiuy.model.common.DataDictionary;

/**
 * @version V1.0
 * @Package com.jiuy.common.service
 * @Description: 数据字典service
 * @author: Aison
 * @date: 2018/4/24 17:30
 * @Copyright: 玖远网络
 */
public interface IDictionaryService {

    /**
     * 通过code获取某个字典
     * @param code
     * @param group
     * @date:   2018/4/24 17:31
     * @author: Aison
     */
    DataDictionary getByCode(String code,String group);

}
