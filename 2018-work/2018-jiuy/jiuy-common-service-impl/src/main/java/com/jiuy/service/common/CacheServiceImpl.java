package com.jiuy.service.common;

import com.jiuy.mapper.common.DataDictionaryMapper;
import com.jiuy.model.common.DataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存service
 * @version V1.0
 * @author Aison
 * @date 2018/4/26 17:50
 * @Copyright 玖远网络
 */
@Service("cacheService")
public class CacheServiceImpl implements  ICacheService{

    private static final Map<String,DataDictionary> pageCache = new HashMap<>();

    private DataDictionaryMapper dictionaryMapper;

    @Autowired
    public void setDictionaryMapper(DataDictionaryMapper dictionaryMapper) {
        this.dictionaryMapper = dictionaryMapper;
        fillCacheDictionary();
    }

    private void fillCacheDictionary() {
        if(pageCache.size()>0) {
            return ;
        }
        synchronized (pageCache) {
            if(pageCache.size()>0) {
                return ;
            }
            DataDictionary dictionary = new DataDictionary();
            dictionary.setGroupCode("page_group");
            List<DataDictionary> dictionaries=  dictionaryMapper.selectList(dictionary);
            dictionaries.forEach(action->{
                pageCache.put(action.getCode(),action);
            });
        }
    }

    /**
     * 通过code获取字典
     * @param code 字典编码
     * @date   2018/4/26 18:14
     * @author Aison
     */
    @Override
    public DataDictionary  getByCode(String code) {
        return  pageCache.get(code);
    }
}
