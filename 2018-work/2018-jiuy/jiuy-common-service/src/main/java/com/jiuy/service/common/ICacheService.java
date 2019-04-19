package com.jiuy.service.common;


import com.jiuy.model.common.DataDictionary;

/**
 * @version V1.0
 * @Package com.jiuy.service.common
 * @Description: 缓存sercice
 * @author: Aison
 * @date: 2018/4/26 17:49
 * @Copyright: 玖远网络
 */
public interface ICacheService {

    /**
     * 通过code获取字典
     * @param code
     * @date:   2018/4/26 18:14
     * @author: Aison
     */
    DataDictionary getByCode(String code);
    
}
